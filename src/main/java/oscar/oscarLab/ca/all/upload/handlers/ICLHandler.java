/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

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
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.ICLUtilities;

/*
 * @author David Daley, Ithream
 */
public class ICLHandler implements MessageHandler  {
    
    Logger logger = Logger.getLogger(ICLHandler.class);
    
    public String parse(String serviceName, String fileName,int fileId){
        
        ICLUtilities u = new ICLUtilities();
        int i = 0;
        try {
            ArrayList<String> messages = u.separateMessages(fileName);
            for (i=0; i < messages.size(); i++){
                
                String msg = messages.get(i);
                MessageUploader.routeReport(serviceName, "ICL", msg,fileId);
                
            }
            
            updateLabStatus(messages.size());
        } catch (Exception e) {

        	MessageUploader.clean(fileId);
            logger.error("Could not upload message", e);
            return null;
        }
        return("success");
        
    }
    
    
    // recheck the abnormal status of the last 'n' labs
    private void updateLabStatus(int n) throws SQLException {
        String sql = "SELECT lab_no, result_status FROM hl7TextInfo ORDER BY lab_no DESC";
        
        
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next() && n > 0){
            
            // only recheck the result status if it is not already set to abnormal
            if (!oscar.Misc.getString(rs, "result_status").equals("A")){
                oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler(oscar.Misc.getString(rs, "lab_no"));
                int i=0;
                int j=0;
                String resultStatus = "";
                while(resultStatus.equals("") && i < h.getOBRCount()){
                    j = 0;
                    while(resultStatus.equals("") && j < h.getOBXCount(i)){
                        if(h.isOBXAbnormal(i, j)){
                            resultStatus = "A";
                            sql = "UPDATE hl7TextInfo SET result_status='A' WHERE lab_no='"+oscar.Misc.getString(rs, "lab_no")+"'";
                            DBHandler.RunSQL(sql);
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
