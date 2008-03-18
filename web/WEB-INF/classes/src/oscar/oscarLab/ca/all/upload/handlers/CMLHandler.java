/*
 * CMLHandler.java
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
public class CMLHandler implements MessageHandler  {

    Logger logger = Logger.getLogger(CMLHandler.class);
    
   
    public String parse(String fileName,int fileId){
        
        Utilities u = new Utilities();    
        MessageUploader uploader = new MessageUploader();
        int i = 0;
        try {
            ArrayList messages = u.separateMessages(fileName);            
            for (i=0; i < messages.size(); i++){
                
                String msg = (String) messages.get(i);
                uploader.routeReport("CML", msg,fileId);
                
            }            
        } catch (Exception e) {
            uploader.clean(fileId);
            logger.error("Could not upload message: ", e);
            e.printStackTrace();
            return null;
        }
        return("success");
        
    }
    
   
    
    
    
}