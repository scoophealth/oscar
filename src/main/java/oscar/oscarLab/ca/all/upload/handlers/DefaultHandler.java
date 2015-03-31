/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/*
 * DefaultHandler.java
 *
 * Created on May 23, 2007, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class DefaultHandler implements MessageHandler {
    Logger logger = Logger.getLogger(DefaultHandler.class);
    String hl7Type = null;

    String getHl7Type(){
        return hl7Type;
    }
    
    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName,int fileId, String ipAddr){
        Document xmlDoc = getXML(fileName);
        
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
                        logger.debug("using xml HL7 Type "+getHl7Type());
                        MessageUploader.routeReport(loggedInInfo, serviceName, getHl7Type(), hl7Body,fileId);
                    }
                }
            }catch(Exception e){
            	MessageUploader.clean(fileId);
                logger.error("ERROR:", e);
                return null;
            }
        }else{
            int i = 0;
            try{
                ArrayList<String> messages = Utilities.separateMessages(fileName);
                for (i=0; i < messages.size(); i++){
                    String msg = messages.get(i);
                    logger.info("using HL7 Type "+getHl7Type());
                    MessageUploader.routeReport(loggedInInfo, serviceName, getHl7Type(), msg,fileId);
                }
            }catch(Exception e){
            	MessageUploader.clean(fileId);
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
    
    
    //TODO: Dont think this needs to be in this class.  Better as a util method
    public String readTextFile(String fullPathFilename) throws IOException {
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader reader = new BufferedReader(new FileReader(fullPathFilename));
                        
        char[] chars = new char[1024];
        int numRead = 0;
        while( (numRead = reader.read(chars)) > -1){
                sb.append(String.valueOf(chars));       
        }

        reader.close();

        return sb.toString();
    }

}
