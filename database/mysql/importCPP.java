import java.sql.*;
import java.util.UUID;
import java.util.Properties;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class importCPP {
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
			ResultSet rs, rsUpdate;

			Class.forName(driver);
			
                        uri += dbName;
			Connection con = DriverManager.getConnection(uri, user, passwd);

			stmt = con.createStatement();	
                        stmtUpdate = con.createStatement();
                        
                        System.out.println("Grabbing cpp issues from issue table");
                        PreparedStatement pstmt = con.prepareStatement("select issue_id from issue where code =?");
                        PreparedStatement insert = con.prepareStatement("insert into issue (code, description, role, update_date) values(?,?,'nurse',now())",PreparedStatement.RETURN_GENERATED_KEYS);
                        String[][] iCodes = {{"OMeds","Other Meds as part of cpp"},{"SocHistory","Social History as part of cpp"},{"MedHistory","Medical History as part of cpp"},{"Concerns","Ongoing Concerns as part of cpp"},{"Reminders","Reminders as part of cpp"}};
                        ResultSet rs1;
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
                        
                        System.out.println("Grabbing list of patients and respective cpp");
                        rs = stmt.executeQuery("select client_id,program_id from admission order by client_id");
                        pstmt = con.prepareStatement("select provider_no,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,update_date from casemgmt_cpp where demographic_no = ? and id = (select max(id) from casemgmt_cpp where demographic_no = ?)");
                        PreparedStatement pstmt2 = con.prepareStatement("select id from casemgmt_issue where demographic_no = ? and issue_id = ?");
                        PreparedStatement pstmt3 = con.prepareStatement("insert into casemgmt_issue (demographic_no,issue_id,program_id,type,update_date) values(?,?,?,'nurse',now())",PreparedStatement.RETURN_GENERATED_KEYS);
                        PreparedStatement pstmt4 = con.prepareStatement("insert into casemgmt_note (update_date, demographic_no, provider_no, note,  signed, signing_provider_no, include_issue_innote, program_no, " +
                                "reporter_caisi_role, reporter_program_team, history, password, locked, uuid, observation_date) Values(?,?,?,?,true,'doctor doe'," +
                                "false,?,'1','0',?,'','0',?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                        PreparedStatement pstmt5 = con.prepareStatement("insert into casemgmt_issue_notes Values(?,?)");
                        ResultSet rs2,rs3,rs4;
                        long cIssueId;
                        UUID uuid;
                        ArrayList<Integer> clients = new ArrayList<Integer>();
                        while(rs.next()) {
                            int client_id = rs.getInt(1);
                            if( clients.contains( new Integer(client_id) ) ) {
                                System.out.println("Multiple record for " + client_id + " skipping");
                                continue;
                            }
                            
                            clients.add(client_id);
                            pstmt.setString(1,rs.getString(1));
                            pstmt.setString(2,rs.getString(1));
                            rs2 = pstmt.executeQuery();
                            if( rs2.next() ) {
                                String cpp = rs2.getString("socialHistory");
                                if( cpp != null && !cpp.equals("") ) {
                                    System.out.println("Inserting social history for " + rs.getString(1));
                                    pstmt2.setString(1,rs.getString(1));
                                    pstmt2.setLong(2,issueIds[1]);
                                    rs3 = pstmt2.executeQuery();
                                    if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString(1));
                                        pstmt3.setLong(2,issueIds[1]);
                                        pstmt3.setLong(3,rs.getInt("program_id"));
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();
                                    }
                                    else
                                        cIssueId = rs3.getLong(1);
                                    
                                    uuid = UUID.randomUUID();
                                    pstmt4.setTimestamp(1,rs2.getTimestamp("update_date"));
                                    pstmt4.setString(2,rs.getString(1));
                                    pstmt4.setString(3,rs2.getString("provider_no"));
                                    pstmt4.setString(4,cpp);
                                    pstmt4.setString(5,rs.getString("program_id"));
                                    pstmt4.setString(6,cpp);
                                    pstmt4.setString(7,uuid.toString());
                                    pstmt4.setTimestamp(8,rs2.getTimestamp("update_date"));
                                    pstmt4.executeUpdate();
                                    rs4 = pstmt4.getGeneratedKeys();
                                    rs4.next();
                                    pstmt5.setLong(1,cIssueId);
                                    pstmt5.setLong(2,rs4.getLong(1));
                                    pstmt5.executeUpdate();
                                    rs3.close();
                                    rs4.close();
                                }
                                cpp = rs2.getString("familyHistory");
                                if( cpp != null && !cpp.equals("") ) {
                                    System.out.println("Inserting other Meds for " + rs.getString(1));
                                    pstmt2.setString(1,rs.getString(1));
                                    pstmt2.setLong(2,issueIds[0]);
                                    rs3 = pstmt2.executeQuery();
                                    if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString(1));
                                        pstmt3.setLong(2,issueIds[0]);
                                        pstmt3.setLong(3,rs.getInt("program_id"));
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();
                                    }
                                    else
                                        cIssueId = rs3.getLong(1);
                                    
                                    uuid = UUID.randomUUID();
                                    pstmt4.setTimestamp(1,rs2.getTimestamp("update_date"));
                                    pstmt4.setString(2,rs.getString(1));
                                    pstmt4.setString(3,rs2.getString("provider_no"));
                                    pstmt4.setString(4,cpp);
                                    pstmt4.setString(5,rs.getString("program_id"));
                                    pstmt4.setString(6,cpp);
                                    pstmt4.setString(7,uuid.toString());
                                    pstmt4.setTimestamp(8,rs2.getTimestamp("update_date"));
                                    pstmt4.executeUpdate();
                                    rs4 = pstmt4.getGeneratedKeys();
                                    rs4.next();
                                    pstmt5.setLong(1,cIssueId);
                                    pstmt5.setLong(2,rs4.getLong(1));
                                    pstmt5.executeUpdate();
                                    rs3.close();
                                    rs4.close();
                                }
                                
                                cpp = rs2.getString("medicalHistory");
                                if( cpp != null && !cpp.equals("") ) {
                                    System.out.println("Inserting Medical History for " + rs.getString(1));
                                    pstmt2.setString(1,rs.getString(1));
                                    pstmt2.setLong(2,issueIds[2]);
                                    rs3 = pstmt2.executeQuery();
                                    if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString(1));
                                        pstmt3.setLong(2,issueIds[2]);
                                        pstmt3.setLong(3,rs.getInt("program_id"));
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();
                                    }
                                    else
                                        cIssueId = rs3.getLong(1);
                                    
                                    uuid = UUID.randomUUID();
                                    pstmt4.setTimestamp(1,rs2.getTimestamp("update_date"));
                                    pstmt4.setString(2,rs.getString(1));
                                    pstmt4.setString(3,rs2.getString("provider_no"));
                                    pstmt4.setString(4,cpp);
                                    pstmt4.setString(5,rs.getString("program_id"));
                                    pstmt4.setString(6,cpp);
                                    pstmt4.setString(7,uuid.toString());
                                    pstmt4.setTimestamp(8,rs2.getTimestamp("update_date"));
                                    pstmt4.executeUpdate();
                                    rs4 = pstmt4.getGeneratedKeys();
                                    rs4.next();
                                    pstmt5.setLong(1,cIssueId);
                                    pstmt5.setLong(2,rs4.getLong(1));
                                    pstmt5.executeUpdate();
                                    rs3.close();
                                    rs4.close();
                                }
                                
                                
                                cpp = rs2.getString("ongoingConcerns");
                                if( cpp != null && !cpp.equals("") ) {
                                    System.out.println("Inserting ongoing Concerns for " + rs.getString(1));
                                    pstmt2.setString(1,rs.getString(1));
                                    pstmt2.setLong(2,issueIds[3]);
                                    rs3 = pstmt2.executeQuery();
                                    if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString(1));
                                        pstmt3.setLong(2,issueIds[3]);
                                        pstmt3.setLong(3,rs.getInt("program_id"));
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();
                                    }
                                    else
                                        cIssueId = rs3.getLong(1);
                                    
                                    uuid = UUID.randomUUID();
                                    pstmt4.setTimestamp(1,rs2.getTimestamp("update_date"));
                                    pstmt4.setString(2,rs.getString(1));
                                    pstmt4.setString(3,rs2.getString("provider_no"));
                                    pstmt4.setString(4,cpp);
                                    pstmt4.setString(5,rs.getString("program_id"));
                                    pstmt4.setString(6,cpp);
                                    pstmt4.setString(7,uuid.toString());
                                    pstmt4.setTimestamp(8,rs2.getTimestamp("update_date"));
                                    pstmt4.executeUpdate();
                                    rs4 = pstmt4.getGeneratedKeys();
                                    rs4.next();
                                    pstmt5.setLong(1,cIssueId);
                                    pstmt5.setLong(2,rs4.getLong(1));
                                    pstmt5.executeUpdate();
                                    rs3.close();
                                    rs4.close();
                                }
                                
                                cpp = rs2.getString("reminders");
                                if( cpp != null && !cpp.equals("") ) {
                                    System.out.println("Inserting Reminders for " + rs.getString(1));
                                    pstmt2.setString(1,rs.getString(1));
                                    pstmt2.setLong(2,issueIds[4]);
                                    rs3 = pstmt2.executeQuery();
                                    if( !rs3.next() ) {
                                        pstmt3.setString(1,rs.getString(1));
                                        pstmt3.setLong(2,issueIds[4]);
                                        pstmt3.setLong(3,rs.getInt("program_id"));
                                        pstmt3.executeUpdate();
                                        rs4 = pstmt3.getGeneratedKeys();
                                        rs4.next();
                                        cIssueId = rs4.getLong(1);
                                        rs4.close();
                                    }
                                    else
                                        cIssueId = rs3.getLong(1);
                                    
                                    uuid = UUID.randomUUID();
                                    pstmt4.setTimestamp(1,rs2.getTimestamp("update_date"));
                                    pstmt4.setString(2,rs.getString(1));
                                    pstmt4.setString(3,rs2.getString("provider_no"));
                                    pstmt4.setString(4,cpp);
                                    pstmt4.setString(5,rs.getString("program_id"));
                                    pstmt4.setString(6,cpp);
                                    pstmt4.setString(7,uuid.toString());
                                    pstmt4.setTimestamp(8,rs2.getTimestamp("update_date"));
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
                        }
                        
                        
                        
                        
                        
                        
                        
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
}