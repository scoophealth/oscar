/*
 * IncomingLabManagement.java
 *
 * Created on August 6, 2003, 3:59 PM
 */

import java.io.*;
import java.util.logging.*;
import java.lang.*;
import java.util.*;
import oscar.oscarMDSLab.*;
import oscar.oscarMDSLab.dbUtil.*;

/**
 *
 * @author  root
 */
public class IncomingLabManagement {
    
    
    static Logger logger;
    static String busyFile        = "BUSY.TXT";
    static String workingFile     = "UPLD.TXT";
    //static String incomingHL7dir  = "./mds";
    static String incomingHL7dir  = "c:/Data/mds";
    static String moddedTime      = "MODTIME.TXT";    
    //static String errorHL7dir     = "./mdsError";
    //static String dupsHL7dir      = "./mdsDups";
    //static String completedHL7dir = "./mdsCompleted";
    static String errorHL7dir     = "./mdsError";
    static String dupsHL7dir      = "./mdsDups";
    static String completedHL7dir = "./mdsCompleted";
    static String auditLogFile    = "CURHST.0";
    
    
    /** Creates a new instance of IncomingLabManagement */
    public IncomingLabManagement() {}
    
    
    static void initLogger(){
      logger = Logger.getLogger("mdsFileManagement.IncomingLabManagement");
        try {
            // Create an appending file handler
            boolean append = true;
            //FileHandler handler = new FileHandler("c:/Data/LabManagement.log", append);
            FileHandler handler = new FileHandler("LabManagement.log", append);
            handler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);             
            // Add to the desired logger
            //ConsoleHandler handler2 = new ConsoleHandler();                       
            logger.addHandler(handler);
            //logger.addHandler(handler2);
        } catch (IOException e) {}         
        
    }
  
    
    static void loadProperties(String filename){
       LabProperties labProperties = LabProperties.getInstance();
       boolean loaded = labProperties.loader(filename);       
       if(!loaded){          
          System.exit(1);
       }
       busyFile        = labProperties.getProperty("busyFile");
       workingFile     = labProperties.getProperty("workingFile");
       incomingHL7dir  = labProperties.getProperty("incomingHL7dir");
       moddedTime      = labProperties.getProperty("moddedTime");
       errorHL7dir     = labProperties.getProperty("errorHL7dir");
       dupsHL7dir      = labProperties.getProperty("dupsHL7dir");
       completedHL7dir = labProperties.getProperty("completedHL7dir");
       auditLogFile    = labProperties.getProperty("auditLogFile");
       
       logger.info("busyFile "+busyFile+"\n"+
                   "workingFile "+workingFile+"\n"+
                   "incomingHL7dir "+incomingHL7dir+" prop "+labProperties.getProperty("incomingHL7dir") +"\n"+
                   "moddedTime "+moddedTime+"\n"+
                   "errorHL7dir "+errorHL7dir+"\n"+
                   "dupsHL7dir "+dupsHL7dir+"\n"+
                   "completedHL7dir "+completedHL7dir+"\n"+
                   "auditLogFile "+auditLogFile);              
       
    }
    public static void main(String[] args) {
        initLogger();
        logger.info("Start ");
        try{
           if ( args[0] == null ){ throw new Exception("Usage Exception"); }
           logger.info("Loading Properties file "+args[0]);
           loadProperties(args[0]);
           
        }catch(Exception loadingEx){
           logger.severe("Error loading properties file");
           System.out.println("Usage: IncomingLabManagement <properties file>");
           System.exit(2);
        }
        DBHandler db = new DBHandler();
            
        
        IncomingMDSFiles inMDS = new IncomingMDSFiles();
                
        if (!inMDS.checkForBusyFile(incomingHL7dir+"/"+busyFile) ){                                                //CHeck for "BUSY.TXT"
            
            logger.info("Busy File Not Found");
            
            inMDS.putWorkingFile(incomingHL7dir+"/"+workingFile);
            try{
                if (inMDS.filesToParse(incomingHL7dir,moddedTime)){
            
                    ArrayList fileNamesList = inMDS.getFileNamesToParse(incomingHL7dir,auditLogFile);
                    
                    for (int i = 0 ; i < fileNamesList.size();i++){
                        String filename = (String) fileNamesList.get(i);
                        logger.info("About to parse file "+filename);
                        BulkFileParse bfp = new BulkFileParse();
                        
                                                                                    
                        if(bfp.parseFile(incomingHL7dir+"/"+filename)){
                             bfp.processResult(incomingHL7dir+"/"+auditLogFile,dupsHL7dir);                              
                             inMDS.moveCompletedFile(incomingHL7dir+"/"+filename,completedHL7dir);                                                          
                        }else{                        
                            logger.severe("file :"+incomingHL7dir+"/"+filename+" corrupted being moved to the corrupted file directory");
                            inMDS.moveCorruptedFile(incomingHL7dir+"/"+filename,errorHL7dir);
                        }                                                                                                          
                    }                    
                }else{
                    logger.info("No files To Parse");
                }
            }catch (Exception e1){
                logger.severe("Major Mishap: "+e1.getMessage());
                e1.printStackTrace();
            }
            inMDS.removeWorkingFile(incomingHL7dir+"/"+workingFile);
            inMDS.setLastModifiedTime(inMDS.getCurrentModifiedTime(incomingHL7dir),moddedTime);
        }       
        try{
        db.CloseConn();
        }catch(Exception connEx){
           connEx.printStackTrace();
        }
        logger.info("END");        
    }
        
    
}
