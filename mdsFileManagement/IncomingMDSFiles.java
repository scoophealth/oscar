/*
 * IncomingMDSFiles.java
 *
 * Created on September 8, 2003, 9:46 AM
 *
 *-move methods from IncomingLabManagement
    -checkForBusyFile
    -filesToParse
    -getCurrentModifiedTime
    -getFileNamesToParse
    -getLastModifiedTime
    -putWorkingFile
    -removeWorkingFile
    -setLastModifiedTime
 *
 */

import java.io.*;
import java.util.logging.*;
import java.util.*;
import java.lang.*;
/**
 *
 * @author  root
 */
public class IncomingMDSFiles {
        Logger logger;
    
    /** Creates a new instance of IncomingMDSFiles */
    public IncomingMDSFiles() {
        logger = Logger.getLogger("mdsFileManagement.IncomingLabManagement");
    }
    
    /**
     *checkForBusyFile
     * @return boolean : this returns true if the BUSY.TXT is there
     */    
    public boolean checkForBusyFile(String busyFile){
        boolean exists = (new File(busyFile)).exists();           
        return  exists;
    }
    
    public boolean putWorkingFile(String workingFile){
        boolean success = false;
        try {
            File file = new File(workingFile);
    
            // Create file if it does not exist
            success = file.createNewFile();
            if (success) {
                // File did not exist and was created
            } else {
                // File already exists
            }
        } catch (IOException e) {}
        return success;
    }
    
    public boolean removeWorkingFile(String workingFile){
     boolean success = (new File(workingFile)).delete();
     if (!success) {
        // Deletion failed
     }
     return success;
    }
    
    public boolean filesToParse(String incomingHL7dir,String moddedTime){
        boolean parsingToBeDone = false;
        //Check when all files were modified
        
        long curModdedTime = getCurrentModifiedTime(incomingHL7dir);
        long lastModdedTime   = getLastModifiedTime(moddedTime);
        try {
            if ( curModdedTime != lastModdedTime ){
                parsingToBeDone = true;
            }
        }catch( NumberFormatException e){
          logger.info("Looks to be the first time Program Runs mod file"+moddedTime+" has not been created :"
                      +"so we give it the benefit of the doubnt and parse the files");      
           parsingToBeDone = true;
        }
        
        System.out.println("Current Modded time = "+curModdedTime+" Last Modded Time "+lastModdedTime);
        
        return parsingToBeDone;
    }
    public long getCurrentModifiedTime(String incomingHL7dir){
       File file = new File(incomingHL7dir);           
       return file.lastModified(); 
    }
    
    
    public long getLastModifiedTime(String moddedTime){
        //READ FILE  
        String str = "0";
        try {
           BufferedReader in = new BufferedReader(new FileReader(moddedTime));               
           str = in.readLine();           
           in.close();
        } catch (IOException e) {
           logger.severe("Can't read from modified file :"+moddedTime);   
           logger.severe(e.getMessage()); 
        }
        
        System.out.println("this is what str is "+str);
        long retval = 0;
        
        try{ retval = Long.parseLong(str); }
        catch(Exception e2){ retval = 0; }
            
        return retval;
    }
    
    public void setLastModifiedTime(long modTime,String moddedTimeFile){
        try {
           BufferedWriter out = new BufferedWriter(new FileWriter(moddedTimeFile));
           out.write(Long.toString(modTime));
           out.close();
        } catch (IOException e) {
           logger.severe("Can't write to modified file "+moddedTimeFile);        
        }
    }
    
    public ArrayList getFileNamesToParse(String incomingHL7dir){
        ArrayList retval = new ArrayList();
        File dir = new File(incomingHL7dir);
    
        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i=0; i<children.length; i++) {
            // Get filename of file or directory
            if (!children[i].endsWith("TXT")){
                //System.out.println("filename :"+children[i]);
                retval.add(children[i]);
            }
            //String filename = children[i];
            }
        }
        return retval;
    }
    
    
    public boolean moveCorruptedFile(String filename,String errorHL7dir){
       boolean retval = true;
       // File (or directory) to be moved
       File file = new File(filename);
       Calendar cal =  Calendar.getInstance();
       Long lon = new Long(cal.getTimeInMillis());
       String str = new String ( file.getName()+"."+lon.toString() );
       
       File file2 = new File(str);        
       boolean success = file.renameTo(file2);
       file = null;
       file2 = null;
       file = new File(str);
       if (!success) {
        // File was not successfully renamed
          logger.severe(" file: "+filename+" was unable to be renamed to "+str);
          retval = false;
       }else{                                   
          // Destination directory
          File dir = new File(errorHL7dir);

          // Move file to new directory
          boolean success2 = file.renameTo(new File(dir, str));
          if (!success2) {
             // File was not successfully moved
            logger.severe(" file: "+str+" was unable to be moved to the "+errorHL7dir+" directory");
            retval = false;
          }else{
            logger.warning("file: "+str+" was corrupted, please examines its contents");
          }
       }
       return retval;
    }
    
    public boolean moveCompletedFile(String filename,String completedHL7dir){
       boolean retval = true;
       // File (or directory) to be moved
       File file = new File(filename);
       Calendar cal =  Calendar.getInstance();
       Long lon = new Long(cal.getTimeInMillis());
       String str = new String ( file.getName()+"."+lon.toString() );
                       
       File file2 = new File(str);        
       boolean success = file.renameTo(file2);
       file = null;
       file2 = null;
       file = new File(str);
       if (!success) {
        // File was not successfully renamed
          logger.severe(" file: "+file.getName()+" was unable to be renamed to "+str);
          retval = false;
       }else{                     
          // Destination directory
          File dir = new File(completedHL7dir);
    
          // Move file to new directory       
          boolean success2 = file.renameTo(new File(dir, str));
          if (!success2) {
             // File was not successfully moved
             logger.severe(" file: "+str+" was unable to be moved to the "+completedHL7dir+" directory");
             retval = false;
          }else{
             logger.info("file: "+str+" has been processed, It has been moved to the completedHL7dir");
          }
       }
       return retval;
    }
    
}
