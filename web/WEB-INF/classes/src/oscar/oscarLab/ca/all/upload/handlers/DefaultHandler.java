/*
 * DefaultHandler.java
 *
 * Created on May 23, 2007, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

/**
 *
 * @author wrighd
 */
public class DefaultHandler implements MessageHandler {
    Logger logger = Logger.getLogger(DefaultHandler.class);
    String hl7Type = null;

    String getHl7Type(){
        return hl7Type;
    }
    
    public String parse(String fileName,int fileId){
        Document xmlDoc = getXML(fileName);
        MessageUploader uploader = new MessageUploader();
        
        /*
         *  If the message is in xml format parse through all the nodes looking for
         *  data that contains a pid segment
         */
        if(xmlDoc != null){
            String hl7Body = null;
            int msgCount = 0;
            try{
                NodeList allNodes = xmlDoc.getElementsByTagNameNS("*","*");
                for (int i=1; i<allNodes.getLength(); i++){
                    hl7Body = allNodes.item(i).getFirstChild().getTextContent();
                    
                    if (hl7Body != null && hl7Body.indexOf("\nPID|") > 0){
                        msgCount++;
                        logger.info("using xml HL7 Type "+getHl7Type());
                        uploader.routeReport(getHl7Type(), hl7Body,fileId);
                    }
                }
            }catch(Exception e){
                uploader.clean(fileId);
                logger.error("ERROR:", e);
                return null;
            }
        }else{
            Utilities u = new Utilities();
            int i = 0;
            try{
                ArrayList messages = u.separateMessages(fileName);
                for (i=0; i < messages.size(); i++){
                    String msg = (String) messages.get(i);
                    logger.info("using HL7 Type "+getHl7Type());
                    uploader.routeReport(getHl7Type(), msg,fileId);
                }
            }catch(Exception e){
                uploader.clean(fileId);
                logger.error("ERROR:", e);
                return null;
            }
        }
        return("success");
    }
    
    
    
    /*
     *  Return the message as an xml document if it is in the xml format
     */
    private Document getXML(String fileName){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            Document doc = factory.newDocumentBuilder().parse(new FileInputStream(fileName));
            return(doc);
            
            // Ignore exceptions and return false
        }catch(Exception e){
            return(null);
        }
    }   
    
}