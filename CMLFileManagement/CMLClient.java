/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabFileManagement
 * 
 * CMLClient.java
 *
 * Created on September 22, 2005, 9:52 AM
 */

package oscar_mcmaster.CMLFileManagement;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import java.util.logging.*;
import javax.xml.parsers.*;
import org.apache.commons.httpclient.protocol.*;
import org.w3c.dom.*;
import oscar_mcmaster.CMLFileManagement.*;

/**
 *
 * @author  Jay Gallagher
 */
public class CMLClient {
   
   static int ConnectionRefused = 0;
   static int Success = 1;
   static int UploadedPreviously = 2;
   static int Exception = 3;
   static int DatabaseNotStarted = 3;
   static int AccessDenied = 4;
   
   static Logger logger;
   static String busyFile        = "BUSY.TXT";
   static String workingFile     = "UPLD.TXT";
   static String incomingHL7dir  = "c:/Data/mds";
   static String moddedTime      = "MODTIME.TXT";    
   static String errorHL7dir     = "./mdsError";
   static String dupsHL7dir      = "./mdsDups";
   static String completedHL7dir = "./mdsCompleted";
   static String auditLogFile    = "CURHST.0";
   static String url             = null;
   static String key             = null;
   static String sep             = File.separator;
   
   /** Creates a new instance of CMLClient */
   public CMLClient() {
   }
   
   static void loadProperties(String filename) throws Exception{
       Properties labProperties = new Properties();
       File file = new File(filename);
       InputStream inStream = new FileInputStream(file);
       labProperties.load(inStream);
       
       busyFile        = labProperties.getProperty("busyFile");
       workingFile     = labProperties.getProperty("workingFile");
       incomingHL7dir  = labProperties.getProperty("incomingHL7dir");
       moddedTime      = labProperties.getProperty("moddedTime");
       errorHL7dir     = labProperties.getProperty("errorHL7dir");
       dupsHL7dir      = labProperties.getProperty("dupsHL7dir");
       completedHL7dir = labProperties.getProperty("completedHL7dir");
       auditLogFile    = labProperties.getProperty("auditLogFile");
       url             = labProperties.getProperty("serverUrl"); 
       key             = labProperties.getProperty("serverKey");               
       
       logger.info("busyFile "+busyFile+"\n"+
                   "workingFile "+workingFile+"\n"+
                   "incomingHL7dir "+incomingHL7dir+" prop "+labProperties.getProperty("incomingHL7dir") +"\n"+
                   "moddedTime "+moddedTime+"\n"+
                   "errorHL7dir "+errorHL7dir+"\n"+
                   "dupsHL7dir "+dupsHL7dir+"\n"+
                   "completedHL7dir "+completedHL7dir+"\n"+
                   "auditLogFile "+auditLogFile);              
       
    }
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      String URL = "http://localhost:8084/oscar/lab/CMLlabUpload.do";            
      // TODO code application logic here
      //load properties file
      //   should have username , password , url for uploading into OSCAR
      
      initLogger();
      try{
         if ( args[0] == null ){ throw new Exception("Usage Exception"); }
        //   logger.info("Loading Properties file "+args[0]);
         loadProperties(args[0]);
           
      }catch(Exception loadingEx){
          // logger.severe("Error loading properties file");
         System.out.println("Usage: CMLClient <properties file>");
         System.exit(2);
      }
      
      System.out.println("sep looks like this "+sep);
      LabFileManagement fileMan = new LabFileManagement();
                
      if (!fileMan.checkForBusyFile(incomingHL7dir+sep+busyFile) ){                                                //CHeck for "BUSY.TXT"
            
         logger.info("Busy File Not Found");
            
            fileMan.putWorkingFile(incomingHL7dir+sep+workingFile);
            try{
                System.out.println("Files to parse  "+incomingHL7dir+" "+moddedTime);
                if (fileMan.filesToParse(incomingHL7dir,moddedTime)){
            
                    ArrayList fileNamesList = fileMan.getFileNamesToParse(incomingHL7dir,auditLogFile);
                    
                    for (int i = 0 ; i < fileNamesList.size();i++){
                        String filename = (String) fileNamesList.get(i);
                        //logger.info("About to parse file "+filename);
                        CMLClient cml = new CMLClient();
                        
                        int returnCode = cml.sendFileToServer(url,incomingHL7dir+sep+filename,key);
                                                              
                        if(returnCode == Success){                             
                            fileMan.moveCompletedFile(incomingHL7dir+sep+filename,completedHL7dir);                                                          
                        }else if (returnCode == ConnectionRefused){                        
                            logger.severe("file :"+incomingHL7dir+sep+filename+" could not be uploaded, Connection Refused : "+url);
                        }else if (returnCode == DatabaseNotStarted){                           
                            logger.severe("file :"+incomingHL7dir+sep+filename+" could not be uploaded, Database has not been started ");                            
                        }else if(returnCode == AccessDenied){
                            logger.severe("file :"+incomingHL7dir+sep+filename+" could not be uploaded, Access Denied");                                                        
                        }else if (returnCode == UploadedPreviously){                           
                            logger.severe("file :"+incomingHL7dir+sep+filename+" could not be uploaded, Previously Uploaded ");
                            fileMan.moveDupsFile(incomingHL7dir+sep+filename,dupsHL7dir);                                                   
                        }else if (returnCode == Exception){                           
                           logger.severe("file :"+incomingHL7dir+sep+filename+" corrupted being moved to the corrupted file directory");
                           fileMan.moveCorruptedFile(incomingHL7dir+sep+filename,errorHL7dir);
                        }
   
   
                        
                    }                    
                }else{
                    logger.info("No files To Parse");
                }
            }catch (Exception e1){
                logger.severe("Major Mishap: "+e1.getMessage());
                e1.printStackTrace();
            }
            fileMan.removeWorkingFile(incomingHL7dir+sep+workingFile);
            fileMan.setLastModifiedTime(fileMan.getCurrentModifiedTime(incomingHL7dir),moddedTime);
        }                                              
      
      //check directory for new file
      // this could be a specific file or any file in the directory
      // might have to do some figuring and processing.
      
      
      //Start web service and send file 
      // should i open the file or just send it as a multi part form
      
      //XML RPC or HTTPclient
      
      
   }
   
   int sendFileToServer(String URL,String filename,String key){
      //File f1 = new File("/home/jay/documents/labs/Apr271133");
      File f1 = new File(filename);
      return sendFileToServer(URL,f1,key);
   }
   
   int sendFileToServer(String URL,File f1,String key){      
      int returnCode = this.ConnectionRefused;
      try{
         
      Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
      Protocol.registerProtocol("https", easyhttps);
         
      HttpClient client = new HttpClient();
         
      
      MultipartPostMethod mPost = new MultipartPostMethod(URL);
      client.setConnectionTimeout(8000);
      
      //File f1 = new File("/home/jay/documents/labs/Apr271133");      
      mPost.addParameter("importFile",f1.getName(),f1);
      mPost.addParameter("key",key);
      
      int statuscode = client.executeMethod(mPost);
      System.out.println("StatusLine>>>"+ mPost.getStatusLine());
      String xml  = mPost.getResponseBodyAsString();
      xml = xml.trim();
      
      System.out.println(xml);//mPost.getResponseBodyAsString());
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      Document doc = factory.newDocumentBuilder().parse(mPost.getResponseBodyAsStream());
      //Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
      
      NodeList nl = doc.getElementsByTagName("outcome");
      
      String outcome = ( (Element) nl.item(0)  ).getFirstChild().getNodeValue();
      System.out.println("outcome "+outcome);
      
      if (outcome == null){
         returnCode = this.Exception;
      }else if (outcome.equals("uploadedPreviously")){
         returnCode = this.UploadedPreviously;         
      }else if (outcome.equals("databaseNotStarted")){
         returnCode = this.DatabaseNotStarted;   
      }else if (outcome.equals("uploaded")){
         returnCode = this.Success;   
      }else if (outcome.equals("exception")){
         returnCode = this.Exception;   
      }else if (outcome.equals("accessDenied")){
         returnCode = this.AccessDenied;
      }
      
      mPost.releaseConnection();
      
      }catch(ConnectException connEx){
         returnCode = ConnectionRefused;   
      }catch(Exception e){
         e.printStackTrace();
      }
      return returnCode;
   }
   
   
   static void initLogger(){
      logger = Logger.getLogger("CMLClient");
        try {
            // Create an appending file handler
            boolean append = true;
            //FileHandler handler = new FileHandler("c:/Data/LabManagement.log", append);
            FileHandler handler = new FileHandler("CMLLabManagement.log", append);
            handler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);             
            // Add to the desired logger
            //ConsoleHandler handler2 = new ConsoleHandler();                       
            logger.addHandler(handler);
            //logger.addHandler(handler2);
        } catch (IOException e) {}         
        
    }
}
