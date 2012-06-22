/*n
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

import java.sql.*;
import java.util.UUID;
import java.util.Properties;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 *The import program performs 8 functions:
 *1) Creates a program called OSCAR.  All providers and demographics will belong here
 *2) Creates a dummy provider to sign imported encounter notes from eChart table
 *3) Copy all OSCAR providers with the 'all' privilege to OSCAR eChart as defined in secUserRole to program_provider
 *4) Set up access to program OSCAR by inserting OSCAR in program_access and specifying first 6 caisi roles, listed in access_type, in table program_access_roles for each provider
 *5) Copy all rows from demographic table into admission table, linking each record to OSCAR and provider: the progam combined with the caisi role effectively allows each provider to view demographic
 *6) Copy the most recent encounter from the eChart table to casemgmt_note table -- each note is signed by the dummy provider and each note is assigned a uuid for tracking history
 *7) Copy the demographic cpp from the eChart table to casemgmt_cpp table AND create one note for each cpp linked with a cpp issue
 *8) Split charts are then copied as per step 6 above
 */

public class importCasemgmt {
	public static void main( String[] args ) {

		if( args.length != 1 ) {
			System.out.println("Usage: java -cp .:/path/to/mysql-connector-java-3.0.11-stable-bin.jar importCasemgmt 'path/to/oscar.properties'");
			return;
		}

		try {
                        FileInputStream fin = new FileInputStream(args[0]);        
                        Properties prop = new Properties();
                        prop.load(fin);                                                
                        
                        String driver = prop.getProperty("db_driver");
                        System.out.println("Driver " + driver);
                        
                        String uri = prop.getProperty("db_uri");
                        System.out.println("URI " + uri);
                        
                        String dbName = prop.getProperty("db_name");
                        System.out.println("DB NAME " + dbName);
                        
                        String user = prop.getProperty("db_username");
                        System.out.println("DB USER " + user);
                        
                        String passwd = prop.getProperty("db_password");
                        System.out.println("DB PASSWD " + passwd);
                        
			Statement stmt, stmtUpdate;
			ResultSet rs, rs1, rsUpdate;

			Class.forName(driver);
			
                        uri += dbName;
			Connection con = DriverManager.getConnection(uri, user, passwd);

			stmt = con.createStatement();	
                        stmtUpdate = con.createStatement();
                        System.out.println("Checking for OSCAR program");
                        String sql = "select id from program where name = 'OSCAR'";
                        rs1 = stmt.executeQuery(sql);
                        String programId;
                        if( !rs1.next() ) {
                            System.out.println("Creating OSCAR program");
                        
                            int result = stmt.executeUpdate("insert into program (facilityId, name,emergencyNumber,maxAllowed,holdingTank,type,programStatus,allowBatchAdmission,allowBatchDischarge,hic,exclusiveView,defaultServiceRestrictionDays,userDefined,enableEncounterTime,enableEncounterTransportationTime) " +
                                "Values(1,'OSCAR','','99999',0,'Bed','active',0,0,0,'',0,0,0,0)", Statement.RETURN_GENERATED_KEYS);
                        
                            rs = stmt.getGeneratedKeys();
                        
                            rs.next();
                        
                            programId = rs.getString(1);
                            rs.close();
                            System.out.println("INSERT into program " + programId);
                        }
                        else {
                            System.out.println("OSCAR program already present -- skipping");
                            programId = rs1.getString(1);
                            rs1.close();
                        }                        
                        
                        System.out.println("Checking for additional providers to add");
                        sql = "select provider_no from provider where provider_no = '000000'";
                        rs1 = stmt.executeQuery(sql);
                        
                        if( !rs1.next() ) {
                            rs1.close();
                            System.out.println("Creating dummy provider to sign imported notes");
                            stmt.executeUpdate("INSERT INTO provider (provider_no, last_name, first_name, provider_type, specialty, team, sex, dob, address, phone, work_phone, ohip_no, rma_no," +
                                    "billing_no, hso_no, status, comments, provider_activity) " +
                                    "VALUES ('000000','doe','doctor','doctor','','','','0001-01-01','','','','','','','','1','','')");
                            stmt.executeUpdate("insert into `secUserRole` (provider_no, role_name, orgcd, activeyn) values('000000', 'doctor', 'R0000001', 1)");
                        }
                        else
                            System.out.println("Dummy provider present -- skipping");
                        
                        //we have to make sure we only grant perms to entitled providers
                        sql = "select roleUserGroup from secObjPrivilege where objectName = '_eChart' and privilege = 'x'";
                        rs = stmt.executeQuery(sql);
                        ArrayList<String> secObjs  = new ArrayList<String>();
                        while( rs.next() ) {
                            secObjs.add(rs.getString(1));
                        }
                        
                        rs.close();
                        sql = "select provider_no, role_name from secUserRole where provider_no in (select provider_no from provider) ";
                        if( secObjs.size() > 0 )
                            sql += "and ";
                        
                        for( int idx = 0; idx < secObjs.size(); ++idx ) {
                            sql += "role_name = '" + secObjs.get(idx) + "'";
                            if( idx <= secObjs.size() - 2)
                                sql += " or ";
                        }
                        
                        sql += " order by role_name";
                        rs = stmt.executeQuery(sql);
                        
                        //we got to watch out for duplicate entries
                        //right now we default to first record as they are alpha sorted i.e. doctor locum nurse
                        ArrayList<String> providers = new ArrayList<String>();
                        String role_name, prov;
                        int role_id;
                        PreparedStatement insert = con.prepareStatement("insert into program_provider (program_id,provider_no,role_id) Values('" + programId + "',?,?)");
                        PreparedStatement pcheck = con.prepareStatement("select id from program_provider where provider_no = ?");
                        while( rs.next() ) {                            
                            prov = rs.getString("provider_no");
                            pcheck.setString(1,prov);
                            rs1 = pcheck.executeQuery();
                            if( !rs1.next() ) {
                                role_name = rs.getString("role_name");                                
                                insert.setString(1, prov);
                                
                                if( role_name.equalsIgnoreCase("nurse") )
                                    role_id = 2;
                                else
                                    role_id = 1;

                                insert.setInt(2, role_id);

                                if( insert.executeUpdate() != 1 )
                                    throw new SQLException("insert into program_provider failed" + prov);
                                                                
                                System.out.println("Imported provider " + prov);
                            }
                            else
                                System.out.println("provider " + prov + " present -- skipping");
                            
                            rs1.close();
                        }
                        
                        rs.close();
                        insert.close();
                        System.out.println("Checking CAISI role permissions");
                        sql = "Select program_id from program_access where program_id = " + programId;
                        rs1 = stmt.executeQuery(sql);
                        if( !rs1.next() ) {
                            System.out.println("Setting up CAISI role permissions");
                            String id;                        
                            insert = con.prepareStatement("insert into program_access (program_id,access_type_id,all_roles) Values(" + programId + ",?,false)", PreparedStatement.RETURN_GENERATED_KEYS);
                            PreparedStatement roleInsert = con.prepareStatement("insert into program_access_roles (id,role_id) Values(?,1),(?,2)");
                            for( int idx = 1; idx < 9; ++idx ) {

                                insert.setString(1, String.valueOf(idx));

                                if( insert.executeUpdate() != 1 )
                                        throw new SQLException("Setting up CAISI role permissions failed");

                                rs = insert.getGeneratedKeys();
                                rs.next();                                                        

                                roleInsert.setInt(1, rs.getInt(1));
                                roleInsert.setInt(2, rs.getInt(1));

                                if( roleInsert.executeUpdate() != 2 )
                                        throw new SQLException("Setting up CAISI role permissions failed");

                                rs.close();
                            }

                            insert.close();
                            roleInsert.close();
                            System.out.println("Doctors and Nurses now have full CAISI privs");
                        }
                        else
                            System.out.println("skipping CAISI role permissions already done");
                        rs1.close();
                        
                        System.out.println("Importing OSCAR patients into CAISI");
                        
                        sql = "select demographic_no, date_joined, provider_no from demographic";
                        rs = stmt.executeQuery(sql);
                        insert = con.prepareStatement("insert into admission (client_id,program_id,provider_no,admission_date,admission_status,team_id,temporary_admission_flag) Values(?,'" + programId +"',?,?,'current',0,0)");
                        pcheck = con.prepareStatement("select client_id from admission where client_id = ?");
                        int i = 1;
                        while( rs.next() ) {                            
                            pcheck.setInt(1,rs.getInt("demographic_no"));
                            rs1 = pcheck.executeQuery();
                            if( !rs1.next() ) {
                                insert.setInt(1, rs.getInt("demographic_no"));
                                insert.setString(2, rs.getString("provider_no"));
                                insert.setDate(3, rs.getDate("date_joined"));
                            
                                if( insert.executeUpdate() != 1 )
                                       throw new SQLException("insert into admission failed " + rs.getString("demographic_no"));
                            
                                ++i;
                                if( i > 4 ) {
                                    System.out.println("OK");
                                    i = 1;
                                }
                                
                                System.out.print(rs.getString("demographic_no") + " ");
                            }                           
                        }
                        rs.close();
                        insert.close();
                        System.out.println("OK -- Done");
                        System.out.println("Importing current eChart records. Be patient this may take a few minutes.");
                        
                        System.out.println("Grabbing cpp issues from issue table");
                        PreparedStatement pstmt = con.prepareStatement("select issue_id from issue where code =?");
                        insert = con.prepareStatement("insert into issue (code, description, role, update_date) values(?,?,'nurse',now())",PreparedStatement.RETURN_GENERATED_KEYS);
                        String[][] iCodes = {{"OMeds","Other Meds as part of cpp"},{"SocHistory","Social History as part of cpp"},{"MedHistory","Medical History as part of cpp"},{"Concerns","Ongoing Concerns as part of cpp"},{"Reminders","Reminders as part of cpp"}};
                        long[] issueIds = new long[iCodes.length];
                        for( int idx = 0; idx < iCodes.length; ++idx) {
                            pstmt.setString(1,iCodes[idx][0]);
                            rs = pstmt.executeQuery();                            
                            if( !rs.next() ) {
                                System.out.println(iCodes[idx][0] + " not found. Inserting");
                                insert.setString(1,iCodes[idx][0]);
                                insert.setString(2,iCodes[idx][1]);
                                insert.executeUpdate();
                                rs1 = insert.getGeneratedKeys();
                                rs1.next();
                                issueIds[idx] = rs1.getLong(1);
                                rs1.close();
                            }
                            else
                                issueIds[idx] = rs.getLong(1);
                            
                            System.out.println(issueIds[idx] + " : " + iCodes[idx][0]);
                            rs.close();
                        }
                       
                        pstmt.close();

                        PreparedStatement pstmt2 = con.prepareStatement("select id from casemgmt_issue where demographic_no = ? and issue_id = ?");
                        PreparedStatement pstmt3 = con.prepareStatement("insert into casemgmt_issue (demographic_no,issue_id, program_id,type,update_date) values(?,?," + programId + ",'nurse',now())",PreparedStatement.RETURN_GENERATED_KEYS);
                        PreparedStatement pstmt4 = con.prepareStatement("insert into casemgmt_note (update_date, demographic_no, provider_no, note,  signed, signing_provider_no, include_issue_innote, program_no, " +
                                "reporter_caisi_role, reporter_program_team, history, password, locked, uuid, observation_date) Values(?,?,?,?,true,'000000'," +
                                "false," + programId + ",'1','0',?,'','0',?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                        PreparedStatement pstmt5 = con.prepareStatement("insert into casemgmt_issue_notes (id, note_id) Values(?,?)");
                        
                        
                        sql = "select * from eChart e left join (select max(eChartId) eChartId from eChart where subject != 'SPLIT CHART' group by demographicNo) " + 
                                "mx using(eChartId) where e.eChartId = mx.eChartId and e.subject != 'SPLIT CHART'";
                        
                        rs = stmt.executeQuery(sql);
                        insert = con.prepareStatement("insert into casemgmt_note (update_date, demographic_no, provider_no, note,  signed, signing_provider_no, include_issue_innote, program_no, " +
                                "reporter_caisi_role, reporter_program_team, history, password, locked, uuid, observation_date) Values(?,?,?,?,true,'000000'," +
                                "false,'" + programId + "','1','0',?,'','0',?,?)");
                        PreparedStatement cppInsert = con.prepareStatement("insert into casemgmt_cpp (demographic_no,provider_no,socialHistory,familyHistory,medicalHistory,ongoingConcerns," +
                                "reminders,update_date) Values(?,?,?,?,?,?,?,?)");
                        pcheck = con.prepareStatement("select note_id from casemgmt_note where demographic_no = ? and (signing_provider_no = '000000' or (note = ? and update_date = ?))");
                        UUID uuid;
                        String note;                        
                        ResultSet rs3,rs4;
                        long cIssueId, msecs;
                        Date d;
                        while( rs.next() ) {
                            pcheck.setString(1, rs.getString("demographicNo"));
                            pcheck.setString(2, rs.getString("encounter"));
                            
                            msecs = rs.getTimestamp("timeStamp").getTime();
                            msecs += (1000*60*2);
                            
                            d = new Date(msecs);
                            pcheck.setDate(3, d);
                            rs1 = pcheck.executeQuery();
                            if( rs1.next() ) {
                                System.out.println("EChart for " + rs.getString("demographicNo") + " already present -- skipping");
                                continue;
                            }
                            uuid = UUID.randomUUID();
                           
                            Timestamp time = new Timestamp(msecs);
                            
                            insert.setTimestamp(1, time);
                            insert.setString(2, rs.getString("demographicNo"));
                            insert.setString(3, rs.getString("providerNo"));
                            note = formatNote(new StringBuffer(rs.getString("encounter")));
                            note = "LAST CHART\n" + note;
                            insert.setString(4, note);
                            insert.setString(5, note);
                            insert.setString(6, uuid.toString());
                            insert.setTimestamp(7, time);
                            
                            if( insert.executeUpdate() != 1 )
                                    throw new SQLException("inserting case note for " + rs.getString("demographicNo") + " failed");
                            
                            insert.clearParameters();
                            System.out.println("Imported note for " + rs.getString("demographicNo"));
                            
                            cppInsert.setString(1, rs.getString("demographicNo"));
                            cppInsert.setString(2, rs.getString("providerNo"));
                            cppInsert.setString(3, rs.getString("socialHistory"));
                            cppInsert.setString(4, rs.getString("familyHistory"));
                            cppInsert.setString(5, rs.getString("medicalHistory"));
                            cppInsert.setString(6, rs.getString("ongoingConcerns"));
                            cppInsert.setString(7, rs.getString("reminders"));
                            cppInsert.setTimestamp(8, rs.getTimestamp("timeStamp"));
                            
                            if( cppInsert.executeUpdate() != 1 )
                                    throw new SQLException(sql);
                            
                            cppInsert.clearParameters();
                            System.out.println("Imported eChart cpp for " + rs.getString("demographicNo"));
                            System.out.println("Creating cpp issue note");
                            
                            note = rs.getString("socialHistory");
                            if( note != null && !note.equals("") ) {
                                System.out.println("Inserting social history for " + rs.getString("demographicNo"));
                                pstmt2.setString(1,rs.getString("demographicNo"));
                                pstmt2.setLong(2,issueIds[1]);
                                rs3 = pstmt2.executeQuery();
                                if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString("demographicNo"));
                                        pstmt3.setLong(2,issueIds[1]);
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();                                          
                                }
                                else
                                    cIssueId = rs3.getLong(1);

                                uuid = UUID.randomUUID();
                                pstmt4.setTimestamp(1,rs.getTimestamp("timeStamp"));
                                pstmt4.setString(2,rs.getString("demographicNo"));
                                pstmt4.setString(3,rs.getString("providerNo"));
                                pstmt4.setString(4,note);
                                pstmt4.setString(5,note);
                                pstmt4.setString(6,uuid.toString());
                                pstmt4.setTimestamp(7,rs.getTimestamp("timeStamp"));
                                pstmt4.executeUpdate();
                                rs4 = pstmt4.getGeneratedKeys();
                                rs4.next();
                                pstmt5.setLong(1,cIssueId);
                                pstmt5.setLong(2,rs4.getLong(1));
                                pstmt5.executeUpdate();
                                rs3.close();
                                rs4.close();
                                                               
                            }
                            
                            note = rs.getString("familyHistory");
                            if( note != null && !note.equals("") ) {
                                System.out.println("Inserting other Meds for " + rs.getString("demographicNo"));
                                pstmt2.setString(1,rs.getString("demographicNo"));
                                pstmt2.setLong(2,issueIds[0]);
                                rs3 = pstmt2.executeQuery();
                                if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString("demographicNo"));
                                        pstmt3.setLong(2,issueIds[0]);
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();                                          
                                }
                                else
                                    cIssueId = rs3.getLong(1);

                                uuid = UUID.randomUUID();
                                pstmt4.setTimestamp(1,rs.getTimestamp("timeStamp"));
                                pstmt4.setString(2,rs.getString("demographicNo"));
                                pstmt4.setString(3,rs.getString("providerNo"));
                                pstmt4.setString(4,note);
                                pstmt4.setString(5,note);
                                pstmt4.setString(6,uuid.toString());
                                pstmt4.setTimestamp(7,rs.getTimestamp("timeStamp"));
                                pstmt4.executeUpdate();
                                rs4 = pstmt4.getGeneratedKeys();
                                rs4.next();
                                pstmt5.setLong(1,cIssueId);
                                pstmt5.setLong(2,rs4.getLong(1));
                                pstmt5.executeUpdate();
                                rs3.close();
                                rs4.close();
                                                               
                            }
                            note = rs.getString("medicalHistory");
                            if( note != null && !note.equals("") ) {
                                System.out.println("Inserting Medical History for " + rs.getString("demographicNo"));
                                pstmt2.setString(1,rs.getString("demographicNo"));
                                pstmt2.setLong(2,issueIds[2]);
                                rs3 = pstmt2.executeQuery();
                                if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString("demographicNo"));
                                        pstmt3.setLong(2,issueIds[2]);
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();                                          
                                }
                                else
                                    cIssueId = rs3.getLong(1);

                                uuid = UUID.randomUUID();
                                pstmt4.setTimestamp(1,rs.getTimestamp("timeStamp"));
                                pstmt4.setString(2,rs.getString("demographicNo"));
                                pstmt4.setString(3,rs.getString("providerNo"));
                                pstmt4.setString(4,note);
                                pstmt4.setString(5,note);
                                pstmt4.setString(6,uuid.toString());
                                pstmt4.setTimestamp(7,rs.getTimestamp("timeStamp"));
                                pstmt4.executeUpdate();
                                rs4 = pstmt4.getGeneratedKeys();
                                rs4.next();
                                pstmt5.setLong(1,cIssueId);
                                pstmt5.setLong(2,rs4.getLong(1));
                                pstmt5.executeUpdate();
                                rs3.close();
                                rs4.close();
                                                               
                            }
                            note = rs.getString("ongoingConcerns");
                            if( note != null && !note.equals("") ) {
                                System.out.println("Inserting ongoing Concerns for " + rs.getString("demographicNo"));
                                pstmt2.setString(1,rs.getString("demographicNo"));
                                pstmt2.setLong(2,issueIds[3]);
                                rs3 = pstmt2.executeQuery();
                                if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString("demographicNo"));
                                        pstmt3.setLong(2,issueIds[3]);
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();                                          
                                }
                                else
                                    cIssueId = rs3.getLong(1);

                                uuid = UUID.randomUUID();
                                pstmt4.setTimestamp(1,rs.getTimestamp("timeStamp"));
                                pstmt4.setString(2,rs.getString("demographicNo"));
                                pstmt4.setString(3,rs.getString("providerNo"));
                                pstmt4.setString(4,note);
                                pstmt4.setString(5,note);
                                pstmt4.setString(6,uuid.toString());
                                pstmt4.setTimestamp(7,rs.getTimestamp("timeStamp"));
                                pstmt4.executeUpdate();
                                rs4 = pstmt4.getGeneratedKeys();
                                rs4.next();
                                pstmt5.setLong(1,cIssueId);
                                pstmt5.setLong(2,rs4.getLong(1));
                                pstmt5.executeUpdate();
                                rs3.close();
                                rs4.close();
                                                               
                            }
                            note = rs.getString("reminders");
                            if( note != null && !note.equals("") ) {
                                System.out.println("Inserting Reminders for " + rs.getString("demographicNo"));
                                pstmt2.setString(1,rs.getString("demographicNo"));
                                pstmt2.setLong(2,issueIds[4]);
                                rs3 = pstmt2.executeQuery();
                                if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString("demographicNo"));
                                        pstmt3.setLong(2,issueIds[4]);
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();                                          
                                }
                                else
                                    cIssueId = rs3.getLong(1);

                                uuid = UUID.randomUUID();
                                pstmt4.setTimestamp(1,rs.getTimestamp("timeStamp"));
                                pstmt4.setString(2,rs.getString("demographicNo"));
                                pstmt4.setString(3,rs.getString("providerNo"));
                                pstmt4.setString(4,note);
                                pstmt4.setString(5,note);
                                pstmt4.setString(6,uuid.toString());
                                pstmt4.setTimestamp(7,rs.getTimestamp("timeStamp"));
                                pstmt4.executeUpdate();
                                rs4 = pstmt4.getGeneratedKeys();
                                rs4.next();
                                pstmt5.setLong(1,cIssueId);
                                pstmt5.setLong(2,rs4.getLong(1));
                                pstmt5.executeUpdate();
                                rs3.close();
                                rs4.close();
                                                               
                            }
                            
                        }
                        
                        rs.close();
                        cppInsert.close();
                        System.out.println("Finished current notes.");
                        System.out.println("Importing split charts");
                        
                        sql = "select * from eChart e where e.subject = 'SPLIT CHART'";
                        pcheck = con.prepareStatement("select note_id from casemgmt_note where update_date = ? and demographic_no = ? and provider_no = ? and signing_provider_no = '000000' and note like 'SPLIT CHART%'");
                        rs = stmt.executeQuery(sql);
                        while( rs.next() ) {
                            d = new Date(rs.getTimestamp("timeStamp").getTime());
                            pcheck.setDate(1,d);
                            pcheck.setString(2, rs.getString("demographicNo"));
                            pcheck.setString(3, rs.getString("providerNo"));
                            rs1 = pcheck.executeQuery();
                            if( rs1.next() ) {
                                System.out.println(rs.getString("demographicNo") + " already present -- skipping split chart");
                                continue;
                            }

                            uuid = UUID.randomUUID();                                                        
                            
                            insert.setTimestamp(1, rs.getTimestamp("timeStamp"));
                            insert.setString(2, rs.getString("demographicNo"));
                            insert.setString(3, rs.getString("providerNo"));
                            note = formatNote(new StringBuffer(rs.getString("encounter")));
                            note = "SPLIT CHART...\n" + note;
                            insert.setString(4, note);
                            insert.setString(5, note);
                            insert.setString(6, uuid.toString());
                            insert.setTimestamp(7, rs.getTimestamp("timeStamp"));
                            
                            if( insert.executeUpdate() != 1 )
                                    throw new SQLException(sql);
                            
                            System.out.println("Imported split chart for " + rs.getString("demographicNo"));
                        }
                        
                        insert.clearParameters();
                        System.out.println("It's been fun but we're all done!");
			
			con.close();
                         
		}
                catch( FileNotFoundException e ) {
                    System.out.println("Could not open properties file:\n" + e.getMessage());
                }
                catch( IOException e ) {
                    System.out.println("Error reading properties file\n" + e.getMessage());
                }                
		catch( Exception e ) {
			System.out.println("DB ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}
        
         public static String formatNote(StringBuffer note) {
                final int MAXLENGTH = 80;
                final int ORIGTHRESHOLD = 88;

                int curLen = 0;
                int origLen = 0;
                int space = 0;
                StringBuffer temp = new StringBuffer();
                
                for( int idx = 0; idx < note.length(); ++idx ) {
                    ++curLen;
                    ++origLen;
                    
                    if( note.charAt(idx) == '\r' )                        
                        ;
                    else if( note.charAt(idx) == '\n' ) {
                        if( origLen >= ORIGTHRESHOLD ) {
                            temp.append(' ');
                        }
                        else {
                            temp.append(note.charAt(idx));
                            curLen = 0;
                        }
                        origLen = 0;
                    }
                    else if( note.charAt(idx) == ' ' ) {
                        temp.append(' ');
                        space = temp.length() - 1;
                    }
                    else
                        temp.append(note.charAt(idx));
                    
                    if( curLen > MAXLENGTH && (note.charAt(idx) == '*' || note.charAt(idx) == '_') ) {
                        temp.deleteCharAt(temp.length()-1);
                        --curLen;
                        --origLen;
                    }
                    else if( curLen > MAXLENGTH ) {
                        if( space > 0 ) {                            
                            temp.setCharAt(space, '\n');                            
                            space = 0;
                        }
                        else
                            temp.append('\n');
                        
                        curLen = 0;
                    }   
                    
                } //end for
                
                return temp.toString();
        }        	

}
