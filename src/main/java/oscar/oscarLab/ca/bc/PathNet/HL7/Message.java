/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarLab.ca.bc.PathNet.HL7;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarLab.ca.bc.PathNet.HL7.V2_3.MSH;
import oscar.oscarLab.ca.bc.PathNet.HL7.V2_3.PID;

/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class Message {
   Logger _logger = Logger.getLogger(this.getClass());
   //Logger _logger = Logger.getLogger("oscar.oscarLab.ca.bc");
   private static final String lineBreak = "\n",
   insert = "INSERT INTO hl7_message ( date_time ) VALUES ( '@date_time' );";
   private String now;
   private PID pid = null;
   private MSH msh = null;
   private Node current;
   public Message(String now) {
      MiscUtils.getLogger().debug("Should be a new LOG MESSAGE FILE NOW"+now);
      _logger.debug("Message object Instantiated now = "+now);
      this.now = now;
      this.current = null;
   }

   //Parses HL7 message. Splits at the line breaks and then checks to see what the line starts with
   //If the line starts with MSH it create a new MSH message and calls parse on the MSH object
   //If the line starts with PID it create a new PID Node and calls parse on it
   //  Parse returns a in instance of itself (PID Node)
   //If the line starts with NTE it calls parse on current Node, which is the PID node
   //If the line starts with anything else the pid.parse is called which.  The PID object handles parsing
   //   any other line ie( ORC,OBR, OBX ) and returns and instance of them selves to (Current)
   //This is how NTE objects get attached to OBX and OBR... because the NTE will follow the OBR or OBX that it was intended for.
   public void Parse(String data) {
      _logger.debug("Parsing HL7 message");
      String[] lines = data.split(lineBreak);
      int count = lines.length;
      _logger.debug("Parsing "+count+" lines");
      for (int i = 0; i < count; ++i) {
         _logger.debug("line: "+lines[i]);
         if (lines[i].startsWith("MSH")) {
            this.msh = new MSH();
            current = this.msh.Parse(lines[i]);
         }
         else if (lines[i].startsWith("PID")) {
            this.pid = new PID();
            current = this.pid.Parse(lines[i]);
         }
         else if (lines[i].startsWith("NTE")) {
            if (current != null) {
               current.Parse(lines[i]);
            }
         }
         else if (this.pid != null) {
            current = this.pid.Parse(lines[i]);
         }
      }
   }
   public String toString() {
      return pid.toString();
   }
   protected String getSql() {
      return insert.replaceAll("@date_time", this.now);
   }
   private String getLastInsertedIdSql() {
      return "SELECT LAST_INSERT_ID();";
   }


   //Method runs insert into hl7_message table and retrieves the insert id.
   //The calls the MSH.toDatabase method passing in the insert id from hl7_message
   public void ToDatabase() throws SQLException {
      MiscUtils.getLogger().debug("sql "+this.getSql());
      DBHandler.RunSQL(this.getSql());
      ResultSet result = DBHandler.GetSQL(this.getLastInsertedIdSql());
      int parent = 0;
      if (result.next()) {
         parent = result.getInt(1);
      }
      if (parent == 0)
         throw new SQLException("Could not get last inserted id");
      msh.ToDatabase(parent);
      int id = pid.ToDatabase(parent);
      linkToProvider(parent,id);
      patientRouteReport(parent);
   }

   public void linkToProvider(int parent, int id){
      //public void providerRouteReport (int segmentID) {
        try {

            String sql;
            try {
                String providerMinistryNo;
                String[] subStrings;
                String[] conDoctors;
                String providerNo = null;

                sql = "select ordering_provider, result_copies_to from  hl7_obr where pid_id = '"+id+"'";

                ResultSet rs = DBHandler.GetSQL(sql);
                boolean addedToProviderLabRouting = false;
                if ( rs.next() ) {
                    //OLD CODE AT BOTTOM
                    ArrayList<String> listOfProviderNo = new ArrayList<String>();
                    // route lab first to admitting doctor
                    subStrings = oscar.Misc.getString(rs, "ordering_provider").split("\\^");
                    providerMinistryNo = subStrings[0]; //StringUtils.returnStringToFirst(subStrings[0].substring(1, subStrings[0].length())," ");
                    // check that this is a legal provider
                    MiscUtils.getLogger().debug("looking for "+providerMinistryNo);
                    providerNo = getProviderNoFromBillingNo(providerMinistryNo);
                    if ( providerNo != null) {  // provider found in database
                        listOfProviderNo.add(providerNo);
                    }  // provider not found

                    // next route to consulting doctor(s)
                    if ( ! oscar.Misc.getString(rs, "result_copies_to").equals("") ) {
                        conDoctors = oscar.Misc.getString(rs, "result_copies_to").split("~");
                        for (int i = 1; i <= conDoctors.length; i++) {
                            subStrings = conDoctors[i-1].split("\\^");
                            providerMinistryNo = subStrings[0];//StringUtils.returnStringToFirst(subStrings[0].substring(1, subStrings[0].length())," ");
                            // check that this is a legal provider
                            MiscUtils.getLogger().debug("looking for 2 "+providerMinistryNo);
                            providerNo = getProviderNoFromBillingNo(providerMinistryNo);
                            if ( providerNo != null) {  // provider found in database
                               if (!listOfProviderNo.contains(providerNo)){
                                  listOfProviderNo.add(providerNo);
                               }
                            }   // provider not found
                        }
                    }
                    /////

                    ProviderLabRouting routing = new ProviderLabRouting();
                    if (listOfProviderNo.size() > 0) {  // provider found in database
                       for(int p = 0; p < listOfProviderNo.size(); p++){
                          String prov = listOfProviderNo.get(p);
                          //sql ="insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+prov+"', '"+parent+"', 'N','BCP')";

                          //DBHandler.RunSQL(sql);
                          routing.route(parent, prov, DbConnectionFilter.getThreadLocalDbConnection(), "BCP");
                       }
                       addedToProviderLabRouting =true;
                    }   // provider not found

                    if(!addedToProviderLabRouting){
                       sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+parent+"', 'N','BCP')";
                       MiscUtils.getLogger().debug(sql);
                       DBHandler.RunSQL(sql);
                    }

                } else { // major error
                    MiscUtils.getLogger().debug("sql "+sql);
                    throw new Exception("Corresponding PV1 entry not found!");
                }
                rs.close();
            } catch (Exception e) {
                MiscUtils.getLogger().debug("Error in providerRouteReport:"+e);

                sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+parent+"', 'N','BCP')";
                MiscUtils.getLogger().debug(sql);
                DBHandler.RunSQL(sql);
            }
        } catch (Exception e) {
            MiscUtils.getLogger().debug("Database error in providerRouteReport:"+e);
        }


   }

   public String getProviderNoFromBillingNo(String providerMinistryNo){
       String ret = null;
       String sql = "select provider_no from provider where ohip_no='"+providerMinistryNo+"'";
       MiscUtils.getLogger().debug(sql);
       boolean hasNext = false;
       try {

          ResultSet rsr = DBHandler.GetSQL(sql);
          if (!rsr.next()){
             sql = "select provider_no from provider where ohip_no='0"+providerMinistryNo+"'";
             MiscUtils.getLogger().debug("\n\n"+sql+"\n\n");
             rsr = DBHandler.GetSQL(sql);
             if (rsr.next()){
                hasNext = true;
             }
          }else{
             hasNext = true;
          }
          if(hasNext){
             ret = rsr.getString("provider_no");
          }
          rsr.close();
       }catch(Exception e){
          MiscUtils.getLogger().error("Error", e);
       }
       return ret;
    }

   ////
   public void patientRouteReport (int segmentID) {
      try {

         String sql;
         try {
            //sql ="select healthNumber, patientName, dOB, sex from mdsPID where segmentID='"+segmentID+"'";
            sql = "select external_id as healthNumber, patient_name as patientName, date_of_birth as dOB, sex from hl7_pid where message_id = '"+segmentID+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if ( rs.next() ) {
               String lastName = oscar.Misc.getString(rs, "patientName").split("\\^")[0].toUpperCase();
               String firstName = oscar.Misc.getString(rs, "patientName").split("\\^")[1].toUpperCase();
               //String dobYear = oscar.Misc.getString(rs,"dOB").substring(0,4);
               //String dobMonth = oscar.Misc.getString(rs,"dOB").substring(4,6);
               //String dobDay = oscar.Misc.getString(rs,"dOB").substring(6,8);
               Date d = rs.getDate("dOB");

               Format formatter;
               formatter = new SimpleDateFormat("yyyy");  // 2002
               String dobYear = formatter.format(d);
               formatter = new SimpleDateFormat("MM");    // 01
               String dobMonth = formatter.format(d);
               formatter = new SimpleDateFormat("dd");    // 09
               String dobDay = formatter.format(d);

               String demoNo = null;
               if ( !oscar.Misc.getString(rs, "healthNumber").trim().equals("") ) {
                  // patient's health number is known - check initials, DOB match
                  sql = "select demographic_no from demographic where hin='"+oscar.Misc.getString(rs, "healthNumber")+"' and " +
                        "last_name like '"+StringEscapeUtils.escapeSql(lastName.substring(0,1))+"%' and " +
                        "first_name like '"+StringEscapeUtils.escapeSql(firstName.substring(0,1))+"%' and year_of_birth='"+dobYear+"' and " +
                        "month_of_birth='"+dobMonth+"' and date_of_birth='"+dobDay+"' and sex like '"+oscar.Misc.getString(rs, "sex").toUpperCase()+"%' and " +
                        "patient_status='AC'";
                  ResultSet rs2 = DBHandler.GetSQL(sql);
                  if ( rs2.next() ) {
                     demoNo = rs2.getString("demographic_no");
                     sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,created) values ('"+rs2.getString("demographic_no")+"', '"+segmentID+"','BCP',now())";
                  } else {
                     sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,created) values ('0', '"+segmentID+"','BCP',now())";
                  }
                  DBHandler.RunSQL(sql);
               } else {
                  // patient's health number is unknown - search by name, DOB, sex
                  sql = "select demographic_no from demographic where last_name='"+StringEscapeUtils.escapeSql(lastName)+"' and " +
                        "first_name like '"+StringEscapeUtils.escapeSql(firstName)+"%' and year_of_birth='"+dobYear+"' and " +
                        "month_of_birth='"+dobMonth+"' and date_of_birth='"+dobDay+"' and sex like '"+oscar.Misc.getString(rs, "sex").toUpperCase()+"%' and " +
                        "patient_status='AC'";
                  MiscUtils.getLogger().debug(sql);
                  ResultSet rs3 = DBHandler.GetSQL(sql);
                  if ( rs3.next() ) {
                     demoNo = rs3.getString("demographic_no");
                     sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,created) values ('"+rs3.getString("demographic_no")+"', '"+segmentID+"','BCP',now())";
                  } else {
                     sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,created) values ('0', '"+segmentID+"','BCP',now())";
                  }
                  DBHandler.RunSQL(sql);
               }

               //NOT ALL DOCS WANT ALL LABS ECHO'D INTO THERE INBOX
               if (demoNo != null){
                  patientProviderRoute(""+segmentID,demoNo);
               }
            } else { // major error
               throw new Exception("Corresponding PID entry not found!");
            }
            rs.close();
         } catch (Exception e) {
            MiscUtils.getLogger().debug("Error in patientRouteReport:"+e);
            MiscUtils.getLogger().error("Error", e);
            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,created) values ('0', '"+segmentID+"','BCP',now())";
            DBHandler.RunSQL(sql);
         }
      } catch (Exception e) {
         MiscUtils.getLogger().debug("Database error in patientRouteReport:"+e);
      }
   }

   ////
   public void patientProviderRoute(String lab_no, String demographic_no){
       try {

          String sql;
          sql ="select provider_no from demographic where demographic_no = '"+demographic_no+"'";
          ResultSet rs = DBHandler.GetSQL(sql);

          if ( rs.next() ) {
             String prov_no  = oscar.Misc.getString(rs, "provider_no");
             if ( prov_no != null && !prov_no.trim().equals("")){
                sql = "select status from providerLabRouting where lab_type = 'BCP' and provider_no ='"+prov_no+"' and lab_no = '"+lab_no+"'";
                MiscUtils.getLogger().debug(sql);
                ResultSet rs2 = DBHandler.GetSQL(sql);
                if ( !rs2.next() ) {
                   //sql = "insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+prov_no+"', '"+lab_no+"', 'N','BCP')";

                   //DBHandler.RunSQL(sql);
                   ProviderLabRouting router = new ProviderLabRouting();
                   router.route(lab_no, prov_no, DbConnectionFilter.getThreadLocalDbConnection(), "BCP");
                } else {
                   MiscUtils.getLogger().debug("prov was "+prov_no);
                }
                rs2.close();
             }
          }
          rs.close();
       } catch (Exception e) {
          MiscUtils.getLogger().debug("Database error in patientProviderRoute:"+e);
       }
    }


   /*UNUSED CODE FROM ABOVE
    // route lab first to admitting doctor
                    subStrings = oscar.Misc.getString(rs,"ordering_provider").split("\\^");
                    providerMinistryNo = StringUtils.returnStringToFirst(subStrings[0].substring(1, subStrings[0].length())," ");
                    // check that this is a legal provider
                    providerNo = getProviderNoFromOhipNo(providerMinistryNo);
                    if ( providerNo != null) {  // provider found in database
                        sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+providerNo+"', '"+parent+"', 'N','BCP')";
                        DBHandler.RunSQL(sql);
                        addedToProviderLabRouting =true;
                    }  // provider not found

                    // next route to consulting doctor(s)
                    if ( ! oscar.Misc.getString(rs,"result_copies_to").equals("") ) {
                        conDoctors = oscar.Misc.getString(rs,"result_copies_to").split("~");
                        for (int i = 1; i <= conDoctors.length; i++) {
                            subStrings = conDoctors[i-1].split("\\^");
                            providerMinistryNo = StringUtils.returnStringToFirst(subStrings[0].substring(1, subStrings[0].length())," ");
                            // check that this is a legal provider
                            providerNo = getProviderNoFromOhipNo(providerMinistryNo);
                            if ( providerNo != null) {  // provider found in database
                                // ignore duplicates in case admitting doctor == consulting doctor
                                sql ="insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+providerNo+"', '"+parent+"', 'N','BCP')";
                                DBHandler.RunSQL(sql);
                                addedToProviderLabRouting =true;
                            }   // provider not found
                        }
                    }
    */
}
