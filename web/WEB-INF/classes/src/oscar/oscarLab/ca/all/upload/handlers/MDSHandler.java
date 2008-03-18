/*
 * MDSHandler.java
 *
 * Created on May 23, 2007, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

/**
 *
 * @author wrighd
 */
public class MDSHandler implements MessageHandler  {

    Logger logger = Logger.getLogger(MDSHandler.class);
    
    public String parse(String fileName,int fileId){
        
        Utilities u = new Utilities();
        MessageUploader uploader = new MessageUploader();
        int i = 0;
        try {

            StringBuffer audit = new StringBuffer();
            ArrayList messages = u.separateMessages(fileName);
            for (i=0; i < messages.size(); i++){
                
                String msg = (String) messages.get(i);
                String auditLine = uploader.routeReport("MDS", msg,fileId)+"\n";
                audit.append(auditLine);

            }
            logger.info("Parsed OK");

            return(audit.toString());

        } catch (Exception e) {
            uploader.clean(fileId);
            logger.error("Could not parse message", e);
            return null;
        }

    }   
    
}