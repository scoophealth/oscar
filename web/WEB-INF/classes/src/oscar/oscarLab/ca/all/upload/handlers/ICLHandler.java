/*
 * ICLHandler.java
 * Created on Feb. 23, 2009
 * Modified by David Daley, Indivica
 * Derived from GDMLHandler.java, by wrighd
 */
package oscar.oscarLab.ca.all.upload.handlers;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.ICLUtilities;

/*
 * @author David Daley, Ithream
 */
public class ICLHandler implements MessageHandler  {
    
    Logger logger = Logger.getLogger(ICLHandler.class);
    
    public String parse(String fileName,int fileId){
        
        ICLUtilities u = new ICLUtilities();
        MessageUploader uploader = new MessageUploader();
        int i = 0;
        try {
            ArrayList messages = u.separateMessages(fileName);
            for (i=0; i < messages.size(); i++){
                
                String msg = (String) messages.get(i);
                uploader.routeReport("ICL", msg,fileId);
                
            }
            
            updateLabStatus(messages.size());
        } catch (Exception e) {

            uploader.clean(fileId);
            logger.error("Could not upload message", e);
            return null;
        }
        return("success");
        
    }
    
    
    // recheck the abnormal status of the last 'n' labs
    private void updateLabStatus(int n) throws SQLException {
        Hl7textResultsData data = new Hl7textResultsData();
        String sql = "SELECT lab_no, result_status FROM hl7TextInfo ORDER BY lab_no DESC";
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        
        ResultSet rs = db.GetSQL(sql);
        while(rs.next() && n > 0){
            
            // only recheck the result status if it is not already set to abnormal
            if (!db.getString(rs,"result_status").equals("A")){
                Factory f = new Factory();
                oscar.oscarLab.ca.all.parsers.MessageHandler h = f.getInstance().getHandler(db.getString(rs,"lab_no"));
                int i=0;
                int j=0;
                String resultStatus = "";
                while(resultStatus.equals("") && i < h.getOBRCount()){
                    j = 0;
                    while(resultStatus.equals("") && j < h.getOBXCount(i)){
                        if(h.isOBXAbnormal(i, j)){
                            resultStatus = "A";
                            sql = "UPDATE hl7TextInfo SET result_status='A' WHERE lab_no='"+db.getString(rs,"lab_no")+"'";
                            db.RunSQL(sql);
                        }
                        j++;
                    }
                    i++;
                }
            }
            
            n--;
        }
    }
    
}
