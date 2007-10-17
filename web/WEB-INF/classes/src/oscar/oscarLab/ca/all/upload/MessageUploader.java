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
import oscar.oscarLab.ForwardingRules;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MDSHandler;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.util.UtilDateUtilities;

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
    public String routeReport(String type, String hl7Body,int fileId) throws Exception{
        
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
            String priority = h.getMsgPriority();
            String requestingClient = h.getDocName();
            String reportStatus = h.getOrderStatus();
            String accessionNum = h.getAccessionNum();
            ArrayList docNums = h.getDocNums();
            int finalResultCount = h.getOBXFinalResultCount();
            String obrDate = h.getMsgDate();
            
            // reformat date
            String format = "yyyy-MM-dd HH:mm:ss".substring(0, obrDate.length()-1);
            obrDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(obrDate, format), "yyyy-MM-dd HH:mm:ss");
            
            
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
            String next = "";
            if (disciplineArray != null && disciplineArray.size() > 0)
                next = (String) disciplineArray.get(0);
            
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
            
             
            String fileUploadCheck_id = ""+ fileId;
            
            
            String insertStmt = "INSERT INTO hl7TextMessage (lab_id, message, type, fileUploadCheck_id) VALUES ('', ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertStmt);
            pstmt.setString(1, new String(base64.encode(hl7Body.getBytes("ASCII")), "ASCII"));
            pstmt.setString(2, type);
            pstmt.setString(3, fileUploadCheck_id);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            String insertID = null;
            if(rs.next())
                insertID = rs.getString(1);
            
            insertStmt = "INSERT INTO hl7TextInfo (lab_no, last_name, first_name, sex, health_no, result_status, final_result_count, obr_date, priority, requesting_client, discipline, report_status, accessionNum)"+
                    " VALUES ('"+insertID+"', '"+lastName.replaceAll("'", "\\\\'")+"', '"+firstName.replaceAll("'", "\\\\'")+"', '"+sex+"', '"+hin.replaceAll("'", "\\\\'")+"', '"+resultStatus+"', '"+finalResultCount+"', '"+obrDate.replaceAll("'", "\\\\'")+"', '"+priority+
                    "', '"+requestingClient.replaceAll("'", "\\\\'")+"', '"+discipline.replaceAll("'", "\\\\'")+"', '"+reportStatus+"', '"+accessionNum.replaceAll("'", "\\\\'")+"')";
            
            pstmt = conn.prepareStatement(insertStmt);
            pstmt.executeUpdate();
            pstmt.close();
            
            String demProviderNo = patientRouteReport( insertID, lastName, firstName, sex, dob, hin, db.GetConnection());
            providerRouteReport( insertID, docNums, db.GetConnection(), demProviderNo, type);
            db.CloseConn();
            
            retVal = h.audit();
      
        }catch(Exception e){
            logger.error("Error uploading lab to database");
            throw e;
        }
        
        return(retVal);
        
    }
    
    /**
     *  Attempt to match the doctors from the lab to a provider
     */
    private void providerRouteReport(String labId, ArrayList docNums, Connection conn, String altProviderNo, String labType) throws Exception {
        
        ArrayList providerNums = new ArrayList();
        PreparedStatement pstmt;
        String sql = "";
        if (docNums != null){
            for (int i=0; i < docNums.size(); i++){
                if (labType.equals("PATHL7")){
                    sql = "select provider_no from provider where ohip_no = '"+( (String) docNums.get(i) )+"'";
                }else{
                    sql = "select provider_no from provider where billing_no = '"+( (String) docNums.get(i) )+"'";
                }
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()){
                    providerNums.add(rs.getString("provider_no"));
                }
                rs.close();
                pstmt.close();
            }
        }
        
        ProviderLabRouting routing = new ProviderLabRouting();
        if (providerNums.size() > 0 ){
            for (int i=0; i<providerNums.size(); i++){
                String provider_no = (String) providerNums.get(i);
                routing.route(labId, provider_no, conn, "HL7");
            }
        }else{
            routing.route(labId, "0", conn, "HL7");
            routing.route(labId, altProviderNo, conn, "HL7");
        }
        
    }
    
    
    /**
     *  Attempt to match the patient from the lab to a demographic, return the patients provider
     *  which is to be used then no other provider can be found to match the patient to.
     */
    private String patientRouteReport(String labId, String lastName, String firstName, String sex, String dob, String hin, Connection conn) throws SQLException {
        
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

            if (dob != null && !dob.equals("")){
                String[] dobArray = dob.split("-");
                dobYear = dobArray[0];
                dobMonth = dobArray[1];
                dobDay = dobArray[2];
            }
            
            if(!firstName.equals(""))
                firstName = firstName.substring(0, 1);
            if(!lastName.equals(""))
                lastName = lastName.substring(0, 1);
            
            if( hinMod.equals("%")){
                sql = "select demographic_no, provider_no from demographic where" +
                        " last_name like '"+lastName+"%' and " +
                        " first_name like '"+firstName+"%' and " +
                        " year_of_birth like '"+dobYear+"' and " +
                        " month_of_birth like '"+dobMonth+"' and "+
                        " date_of_birth like '"+dobDay+"' and " +
                        " sex like '"+sex+"%' ";
            }else if( OscarProperties.getInstance().getBooleanProperty("LAB_NOMATCH_NAMES","yes") ){
                sql = "select demographic_no, provider_no from demographic where hin='"+hinMod+"' and " +
                        " year_of_birth like '"+dobYear+"' and " +
                        " month_of_birth like '"+dobMonth+"' and "+
                        " date_of_birth like '"+dobDay+"' and " +
                        " sex like '"+sex+"%' ";
            } else{
                sql = "select demographic_no, provider_no from demographic where hin='"+hinMod+"' and " +
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
                provider_no = rs.getString("provider_no");
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
            }else{
                Hl7textResultsData rd = new Hl7textResultsData();
                rd.populateMeasurementsTable(labId, demo);
            }
            
            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('"+demo+"', '"+labId+"','HL7')";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            
            pstmt.close();
        }catch (SQLException sqlE){
            logger.info("NO MATCHING PATIENT FOR LAB id ="+labId);
            throw sqlE;
        }
        
        return provider_no;
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
                
                sql = "SELECT MAX(id) AS id FROM fileUploadCheck";
                pstmt = conn.prepareStatement(sql);
                ResultSet rs2 = pstmt.executeQuery();
                if (rs2.next()){
                    sql = "DELETE FROM fileUploadCheck where id = '"+rs2.getString("id")+"'";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.executeUpdate();
                }
                    
                    
            }
            
            pstmt.close();
            logger.info("Successfully cleaned the database");
            
        }catch(SQLException e){
            logger.error("Could not clean database: ", e);
        }
    }    
}
