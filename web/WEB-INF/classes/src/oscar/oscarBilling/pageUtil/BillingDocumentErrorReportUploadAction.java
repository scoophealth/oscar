// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarBilling.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.FormFile;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarBilling.bean.*;
import oscar.OscarProperties;


public class BillingDocumentErrorReportUploadAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        BillingDocumentErrorReportUploadForm frm = (BillingDocumentErrorReportUploadForm) form;                
        request.getSession().setAttribute("BillingDocumentErrorReportUploadForm", frm);
        FormFile file1 = frm.getFile1();
        ArrayList messages = new ArrayList();
        ActionErrors errors = new ActionErrors();  
        
        if(!saveFile(file1)){
            errors.add(errors.GLOBAL_ERROR,
            new ActionError("errors.fileNotAdded"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        else{
            if(getData(file1.getFileName(), request))
                return mapping.findForward("success");
            else{
                errors.add(errors.GLOBAL_ERROR,
                new ActionError("errors.incorrectFileFormat"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }
    }
     
    /**
     * 
     * Save a Jakarta FormFile to a preconfigured place.
     * 
     * @param file
     * @return
     */
    public static boolean saveFile(FormFile file){
        String retVal = null;        
        boolean isAdded = true;
        
        try {
            //retrieve the file data
            ByteArrayOutputStream baos = new
            ByteArrayOutputStream();
            InputStream stream = file.getInputStream();
            OscarProperties props = OscarProperties.getInstance();

            //properties must exist            
            String place= props.getProperty("DOCUMENT_DIR");
            
            if(!place.endsWith("/"))
                    place = new StringBuffer(place).insert(place.length(),"/").toString();
            retVal = place+file.getFileName();
            System.out.println(retVal);
            //write the file to the file specified
            OutputStream bos = new FileOutputStream(retVal);
            int bytesRead = 0;
            byte[] buffer = file.getFileData();
            while ((bytesRead = stream.read(buffer)) != -1){
                    bos.write(buffer, 0, bytesRead);
            }
            bos.close();

            //close the stream
            stream.close();
        }
        catch (FileNotFoundException fnfe) {
            
            System.out.println("File not found");
            fnfe.printStackTrace();            
            return isAdded=false;
            
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return isAdded=false;
        }

        return isAdded;
    }
    
    /**
    * 
    * Write to database
    * 
    * @param fileName - the filename to store
    *     
    */
    private void write2Database(String fileName){
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "INSERT INTO measurementCSSLocation(location) VALUES('" + fileName + "')";
            System.out.println("Sql Statement: " + sql);
            db.RunSQL(sql);            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }       
    }
    
      /**
     * 
     * Get Data from the file.
     * 
     * @param file
     * @return
     */
    private boolean getData(String fileName, HttpServletRequest request){
        boolean isGot = false;
        
        try{
            OscarProperties props = OscarProperties.getInstance();        
            //properties must exist            
            String filepath= props.getProperty("DOCUMENT_DIR");
            if(!filepath.endsWith("/"))
                        filepath = new StringBuffer(filepath).insert(filepath.length(),"/").toString();
            FileInputStream file = new FileInputStream(filepath + fileName);
            System.out.println("file path: " + filepath+fileName);
            //Assign associated report Name
            ArrayList messages = new ArrayList();
            String ReportName=""; 
            String ReportFlag="";

            if (fileName.substring(0,1).compareTo("E")==0){ 
                ReportName="Claims Error Report"; 
                BillingClaimsErrorReportBeanHandler hd = generateReportE(file);
                request.setAttribute("claimsErrors", hd);
                isGot = hd.verdict;
            }
            if (fileName.substring(0,1).compareTo("B")==0){ 
                ReportName="Claim Batch Acknowledgement Report"; 
                BillingClaimBatchAcknowledgementReportBeanHandler hd = generateReportB(file);
                request.setAttribute("batchAcks", hd);
                isGot = hd.verdict;
            }
            if (fileName.substring(0,1).compareTo("X")==0){ 
                ReportName="Claim File Rejection Report"; 
                messages=generateReportX(file);
                request.setAttribute("messages", messages);
                isGot = reportXIsGenerated;
            }
            if (fileName.substring(0,1).compareTo("R")==0){ 
                ReportName="EDT OBEC Output Specification"; 
                BillingEDTOBECOutputSpecificationBeanHandler hd = generateReportR(file);
                request.setAttribute("outputSpecs", hd);
                isGot = hd.verdict;
            }
            request.setAttribute("ReportName", ReportName);
        }
        catch (FileNotFoundException fnfe) {
            
            System.out.println("File not found");
            fnfe.printStackTrace();            
            return isGot=false;
            
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return isGot=false;
        }
        return isGot;
    }
 
    /**
     * 
     * Generate Claims Error Report (E).
     * 
     * @param file
     * @return BillingClaimsErrorReportBeanHandler
     */
     private BillingClaimsErrorReportBeanHandler generateReportE(FileInputStream file){
        BillingClaimsErrorReportBeanHandler hd = new BillingClaimsErrorReportBeanHandler(file);        
        
        return hd;     
     }
    
    /**
     * 
     * Generate Claim Batch Acknowledgement Report (B).
     * 
     * @param file
     * @return BillingClaimBatchAcknowlegementReportBeanHandler
     */
     private BillingClaimBatchAcknowledgementReportBeanHandler generateReportB(FileInputStream file){
        BillingClaimBatchAcknowledgementReportBeanHandler hd = new BillingClaimBatchAcknowledgementReportBeanHandler(file);        
        
        return hd;     
     }
     
    /**
     * 
     * Generate Claim File Rejection Report (X).
     * 
     * @param file
     * @return
     */
     private boolean reportXIsGenerated = true;
     private ArrayList generateReportX(FileInputStream file){
        ArrayList messages = new ArrayList();
        messages.add("M01 | Message Reason         Length     Msg Type   Filler  Record Image");
        messages.add("M02 | File:    File Name    Date:   Mail Date   Time: Mail Time     Process Date");
        InputStreamReader reader = new InputStreamReader(file);
        BufferedReader input = new BufferedReader(reader);
        String nextline;
        try{
            while ((nextline=input.readLine())!=null){
                String headerCount = nextline.substring(2,3);

                if (headerCount.compareTo("1") == 0){
                    String recordLength=nextline.substring(23,28);
                    String msgType=nextline.substring(28,31);
                    String filler=nextline.substring(32,39);
                    String error=nextline.substring(39,76);
                    String explain=nextline.substring(3,23);
                    String msg = "M01 | " +explain+"   " + recordLength + "   " + msgType+"   "+filler+"   "+URLEncoder.encode(error);
                    messages.add(msg);

                }
                if (headerCount.compareTo("2") == 0){
                    String mailFile=nextline.substring(8,20);
                    String mailDate=nextline.substring(25,33);
                    String mailTime=nextline.substring(38,44);
                    String batchProcessDate=nextline.substring(50,58);
                    String msg = "M02 | File:   "+mailFile+"    "+"Date:   "+mailDate+"   "+"Time: "+mailTime+"     PDate: "+batchProcessDate;         
                    messages.add(msg);
                }
            }
            
        }
        catch (IOException ioe) {
            ioe.printStackTrace();            
        }
        catch (StringIndexOutOfBoundsException ioe) {
            reportXIsGenerated =  false;   
        }
        return messages;
     }


    /**
     * 
     * Generate EDT OBEC Output Specification (R).
     * 
     * @param file
     * @return BillingEDTOBECOutputSpecificationBeanHandler
     */
     private BillingEDTOBECOutputSpecificationBeanHandler generateReportR(FileInputStream file){
        BillingEDTOBECOutputSpecificationBeanHandler hd = new BillingEDTOBECOutputSpecificationBeanHandler(file);
        Vector outputSpecVector = hd.getEDTOBECOutputSecifiationBeanVector();
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            for(int i=0; i<outputSpecVector.size(); i++){
                BillingEDTOBECOutputSpecificationBean bean = (BillingEDTOBECOutputSpecificationBean) outputSpecVector.elementAt(i);
                String hin = bean.getHealthNo();
                String responseCode = bean.getResponseCode();
                String sql = "SELECT * FROM batchEligibility where responseCode='" + responseCode + "'";
                ResultSet rs = db.GetSQL(sql);                    
                
                String sqlDemo = "SELECT * FROM demographic WHERE hin='" + hin + "'";
                ResultSet rsDemo = db.GetSQL(sqlDemo);
                
                if (rsDemo.next()){
                    if(rsDemo.getString("ver").compareTo(bean.getVersion())==0){
                        String sqlVer = "UPDATE demographic SET ver ='##' WHERE hin='" + hin + "'";
                        db.RunSQL(sqlVer);
                        String sqlAlert = "SELECT * FROM demographiccust where demographic_no ='" + rsDemo.getString("demographic_no") + "'";
                        System.out.println("Select Demo sql: " + sqlAlert);
                        ResultSet rsAlert = db.GetSQL(sqlAlert);
                        if(rsAlert.next() && rs.next()){
                            String newAlert = rsAlert.getString("cust3") + "\n" + rs.getString("MOHResponse") + "- " + rs.getString("reason");
                            String newAlertSql = "UPDATE demographiccust SET cust3 = '" + newAlert + "' where demographic_no='" + rsDemo.getString("demographic_no") + "'";
                            System.out.println("Update alert msg: " + newAlertSql);
                            db.RunSQL(newAlertSql);
                        }
                        rsAlert.close();   
                    }
                    rs.close();                    
                    rsDemo.close();
                }
                         
            }
            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }            
           
        
        return hd;     
     }
     
     
}