/*
 * GDMLHandler.java
 *
 * Created on May 23, 2007, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;


import java.util.ArrayList;
import java.io.*;
import org.apache.log4j.Logger;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

/**
 *
 * @author wrighd
 */
public class GDMLHandler implements MessageHandler  {
    
    Logger logger = Logger.getLogger(GDMLHandler.class);
    
    public String parse(String fileName){
        
        Utilities u = new Utilities();
        MessageUploader uploader = new MessageUploader();
        int i = 0;
        try {
            ArrayList messages = u.separateMessages(fileName);
            for (i=0; i < messages.size(); i++){
                
                String msg = (String) messages.get(i);
                uploader.routeReport("GDML", msg);
                
            }
            logger.info("Parsed OK");
        } catch (Exception e) {
            uploader.clean(i+1);
            logger.error("Could not upload message", e);
            return null;
        }
        return("success");
        
    }  
    
}