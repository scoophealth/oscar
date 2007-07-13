/*
 * MessageUploader.java
 *
 * Created on June 18, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.upload;

import java.sql.*;
import java.util.ArrayList;
import java.text.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MDSHandler;
import oscar.oscarLab.ca.all.parsers.MessageHandler;

/**
 *
 * @author wrighd
 */
public class MessageUploader {
    
    Logger logger = Logger.getLogger(MessageUploader.class);
    
    /**
     * Creates a new instance of MessageUploader
     */
    public MessageUploader() {
    }
    
    /**
     *  Insert the lab into the proper tables of the database
     */
    public String routeReport(String type, String hl7Body) throws Exception{
        
        String retVal = "";
        try{
            
            MessageHandler h = Factory.getInstance().getHandler(type, hl7Body);
            Base64 base64 = new Base64();
            
            String firstName = h.getFirstName();
            String lastName = h.getLastName();
            String dob = h.getDOB();
            String sex = h.getSex();
            String hin = h.getHealthNum();
            String resultStatus = "";
            String obrDate = h.getServiceDate();
            String priority = "";
            String requestingClient = h.getDocName();
            String reportStatus = h.getOrderStatus();
            String accessionNum = h.getAccessionNum();
            ArrayList docNums = h.getDocNums();
            int finalResultCount = h.getOBXFinalResultCount();
                       
            int i=0;
            int j=0;
            while(resultStatus.equals("") && i < h.getOBRCount()){
                j = 0;
                while(resultStatus.equals("") && j < h.getOBXCount(i)){
                    if(h.isOBXAbnormal(i, j))
                        resultStatus = "A";
                    j++;
                }
                i++;
            }
            
            ArrayList disciplineArray = h.getHeaders();
            String next = (String) disciplineArray.get(0);
            
            int sepMark;
            if ((sepMark = next.indexOf("<br />")) < 0 ){
                if ((sepMark = next.indexOf(" ")) < 0)
                    sepMark = next.length();
            }            
            String discipline = next.substring(0, sepMark).trim();    
            
            for ( i=1; i < disciplineArray.size(); i++){
                
                next = (String) disciplineArray.get(i);
                if ((sepMark = next.indexOf("<br />")) < 0 ){
                    if ((sepMark = next.indexOf(" ")) < 0)
                        sepMark = next.length();
                }
                
                if (!next.trim().equals(""))
                    discipline = discipline+"/"+next.substring(0, sepMark);
            }
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection conn = db.GetConnection();
            String insertStmt = "INSERT INTO hl7TextMessage (lab_id, message, type) VALUES ('', ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertStmt);
            pstmt.setString(1, new String(base64.encode(hl7Body.getBytes("ASCII")), "ASCII"));
            pstmt.setString(2, h.getMsgType());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            String insertID = null;
            if(rs.next())
                insertID = rs.getString(1);
            
            insertStmt = "INSERT INTO hl7TextInfo (lab_no, last_name, first_name, sex, health_no, result_status, final_result_count, obr_date, priority, requesting_client, discipline, report_status, accessionNum)"+
                    " VALUES ('"+insertID+"', '"+lastName.replaceAll("'", "")+"', '"+firstName.replaceAll("'", "")+"', '"+sex+"', '"+hin+"', '"+resultStatus+"', '"+finalResultCount+"', '"+obrDate+"', '"+priority+
                    "', '"+requestingClient+"', '"+discipline+"', '"+reportStatus+"', '"+accessionNum+"')";
            
            pstmt = conn.prepareStatement(insertStmt);
            pstmt.executeUpdate();
            pstmt.close();
            
            patientRouteReport( insertID, lastName, firstName, sex, dob, hin, db.GetConnection());
            providerRouteReport( insertID, docNums, db.GetConnection());
            db.CloseConn();
            
            
            //handle mds differently because it requires an acknowledgement message
            if (type != null && type.equals("MDS")){
                // MessageHandler h must be cast as an MDSHandler because the methods
                // used here are specific to the MDSHandler
                MDSHandler mds = (MDSHandler) h;
                String formType = mds.getFormType();
                String clientNum = mds.getClientRef();
                String date = mds.getMSHDate();
                String time = mds.getMSHTime();
                String healthCardVC = mds.getHealthNumVersion();
                String name = lastName.toUpperCase()+", "+firstName.toUpperCase();
                
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss");
                java.util.Date dateObject = new java.util.Date();
                String procDateTime = dateFormat.format(dateObject);
                
                retVal = procDateTime+"  REC  "+reportStatus+"  "+formType+"  "+accessionNum+"  "+hin+"  "
                        +healthCardVC+"  "+name+"  "+clientNum+"  "+date+"  "+time;
            }
            
            
            
        }catch(Exception e){
            logger.error("Error uploading lab to database");
            throw e;
        }
        
        return(retVal);
        
    }
    
    /**
     *  Attempt to match the doctors from the lab to a provider
     */
    private void providerRouteReport(String labId, ArrayList docNums, Connection conn) throws SQLException {
        
        ArrayList providerNums = new ArrayList();
        PreparedStatement pstmt;
        if (docNums != null){
            for (int i=0; i < docNums.size(); i++){
                String sql = "select provider_no from provider where billing_no = '"+( (String) docNums.get(i) )+"'";
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()){
                    providerNums.add(rs.getString("provider_no"));
                }
                rs.close();
                pstmt.close();
            }
        }
        
        if (providerNums.size() == 0){
            logger.info("No matching providers could be found for lab: "+labId);
            String sql = "insert into providerLabRouting (provider_no, lab_no, status, lab_type) values('0', '"+labId+"', 'N', 'HL7')";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        }else{
            for (int i=0; i<providerNums.size(); i++){
                String provider_no = (String) providerNums.get(i);
                String sql = "insert into providerLabRouting (provider_no, lab_no, status, lab_type) values('"+provider_no+"', '"+labId+"', 'N', 'HL7')";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                pstmt.close();
            }
        }
        
    }
    
    
    /**
     *  Attempt to match the patient from the lab to a demographic
     */
    private void patientRouteReport(String labId, String lastName, String firstName, String sex, String dob, String hin, Connection conn) throws SQLException {

        String sql;
        String demo = "0";
        String provider_no = "0";
        //19481015
        String dobYear = "%";
        String dobMonth = "%";
        String dobDay = "%";
        String hinMod = "%";
        
        int count =0 ;
        try{
            
            if(hin != null){
                hinMod = new String(hin);
                if (hinMod.length() == 12){
                    hinMod = hinMod.substring(0,10);
                }
            }
            
            if (dob != null && dob.length() == 8){
                dobYear = dob.substring(0,4);
                dobMonth = dob.substring(5,7);
                dobDay = dob.substring(8,10);
            }
            
            if(!firstName.equals(""))
                firstName = firstName.substring(0, 1);
            if(!lastName.equals(""))
                lastName = lastName.substring(0, 1);
            
            if( hinMod.equals("%")){
                sql = "select demographic_no from demographic where" +
                        " last_name like '"+lastName+"%' and " +
                        " first_name like '"+firstName+"%' and " +
                        " year_of_birth like '"+dobYear+"' and " +
                        " month_of_birth like '"+dobMonth+"' and "+
                        " date_of_birth like '"+dobDay+"' and " +
                        " sex like '"+sex+"%' ";
            }else if( OscarProperties.getInstance().getBooleanProperty("LAB_NOMATCH_NAMES","yes") ){
                sql = "select demographic_no from demographic where hin='"+hinMod+"' and " +
                        " year_of_birth like '"+dobYear+"' and " +
                        " month_of_birth like '"+dobMonth+"' and "+
                        " date_of_birth like '"+dobDay+"' and " +
                        " sex like '"+sex+"%' ";
            } else{
                sql = "select demographic_no from demographic where hin='"+hinMod+"' and " +
                        " last_name like '"+lastName+"%' and " +
                        " first_name like '"+firstName+"%' and " +
                        " year_of_birth like '"+dobYear+"' and " +
                        " month_of_birth like '"+dobMonth+"' and "+
                        " date_of_birth like '"+dobDay+"' and " +
                        " sex like '"+sex+"%' ";
            }
            
            
            logger.info(sql);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                count ++;
                demo = rs.getString("demographic_no");
            }
            rs.close();
            pstmt.close();
        }catch (SQLException sqlE){
            throw sqlE;
        }
        
        try{
            if (count != 1){
                demo = "0";
                logger.info("Could not find patient for lab: "+labId+ " # of possible matches :"+count);
            }
            
            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('"+demo+"', '"+labId+"','HL7')";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            
            /*
            sql = "insert into providerLabRouting (provider_no, lab_no, status, lab_type) values('"+provider_no+"', '"+labId+"', 'N', 'HL7')";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
             */
            
            pstmt.close();
        }catch (SQLException sqlE){
            logger.info("NO MATCHING PATIENT FOR LAB id ="+labId);
            throw sqlE;
        }
    }
    
    /**
     *  Used when errors occur to clean the database of labs that have not been
     *  inserted into all of the necessary tables
     */
    public void clean(int recordCount){
        
        try{
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            Connection conn = db.GetConnection();
            PreparedStatement pstmt;
            ResultSet rs;
            String sql;
            
            sql = "SELECT lab_id FROM hl7TextMessage";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            int lab_id = 0;
            if(rs.last())
                lab_id = rs.getInt(1);
            
            for(int i=0; i < recordCount; i++){
                sql = "DELETE FROM hl7TextInfo where lab_no='"+(lab_id - i)+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                
                sql = "DELETE FROM hl7TextMessage where lab_id='"+(lab_id - i)+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                
                sql = "DELETE FROM providerLabRouting where lab_no='"+(lab_id - i)+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                
                sql = "DELETE FROM patientLabRouting where lab_no='"+(lab_id - i)+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
            }
            
            pstmt.close();
            logger.info("Successfully cleaned the database");
            
        }catch(SQLException e){
            logger.error("Could not clean database: ", e);
        }
    }
    
}
