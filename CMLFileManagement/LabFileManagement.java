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
 *
 * LabFileManagement.java
 *
 * Created on September 25, 2005, 3:26 PM
 */

package oscar_mcmaster.CMLFileManagement;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Jay Gallagher
 */
public class LabFileManagement {
   Logger logger;
   
   public LabFileManagement() {
      logger = Logger.getLogger("mdsFileManagement.IncomingLabManagement");    
   }
                  
    /**
     *checkForBusyFile
     * @return boolean : this returns true if the BUSY.TXT is there
     */    
    public boolean checkForBusyFile(String busyFile){
        boolean exists = (new File(busyFile)).exists();           
        logger.info("Checking for Busy File, busy file: "+exists );
        return  exists;
    }
    
    public boolean putWorkingFile(String workingFile){
        logger.info("Creating Working File");
        boolean success = false;
        try {
            File file = new File(workingFile);
    
            // Create file if it does not exist
            success = file.createNewFile();
            if (success) {
                // File did not exist and was created
            } else {
                // File already exists
               logger.info("Working file already existed");
            }
        } catch (IOException e) {
           logger.severe("Error Creating Working File:"+workingFile+ " Error "+e.getMessage());
        }
        return success;
    }
    
    public boolean removeWorkingFile(String workingFile){
       logger.info("Removing Working File");
     boolean success = (new File(workingFile)).delete();
     if (!success) {
        // Deletion failed
        logger.severe("Removing Working File failed");
     }
     return success;
    }
    
    public boolean filesToParse(String incomingHL7dir,String moddedTime){
       logger.info("Checking for Files to parse");
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
                      +"so we give it the benefit of the doubt and parse the files");      
           parsingToBeDone = true;
        }
        
        logger.info("Current Modded time = "+curModdedTime+" Last Modded Time "+lastModdedTime);
        
        return parsingToBeDone;
    }
    
    public long getCurrentModifiedTime(String incomingHL7dir){
       logger.info("Getting Last Modified time of "+incomingHL7dir);
       File file = new File(incomingHL7dir);           
       return file.lastModified(); 
    }
    
    
    public long getLastModifiedTime(String moddedTime){
        //READ FILE  
        logger.info ("Reading Last Modified Time from: "+moddedTime);
        String str = "0";
        try {
           BufferedReader in = new BufferedReader(new FileReader(moddedTime));               
           str = in.readLine();           
           in.close();
        } catch (IOException e) {
           logger.severe("Can't read from modified file :"+moddedTime);   
           logger.severe(e.getMessage()); 
        }
                
        long retval = 0;
        
        try{ retval = Long.parseLong(str); }
        catch(Exception e2){ retval = 0; }
            
        return retval;
    }
    
    public void setLastModifiedTime(long modTime,String moddedTimeFile){
       logger.info("Writeing Last Modified Time to: "+moddedTimeFile);
        try {
           BufferedWriter out = new BufferedWriter(new FileWriter(moddedTimeFile));
           out.write(Long.toString(modTime));
           out.close();
        } catch (IOException e) {
           logger.severe("Can't write to modified file "+moddedTimeFile);        
        }
    }
    
    public ArrayList getFileNamesToParse(String incomingHL7dir,String auditLogFile){
        logger.info("Getting Files Names to Parse");
        ArrayList retval = new ArrayList();
        File dir = new File(incomingHL7dir);
    
        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
           logger.severe("Dir "+incomingHL7dir+" does not exist or is not a directory");
        } else {
            for (int i=0; i<children.length; i++) {
            // Get filename of file or directory
            logger.info("filename: "+children[i]);
            if (!children[i].endsWith("TXT") && !children[i].equals(auditLogFile)){
                logger.info("Adding filename :"+children[i] +" to list of files to be parsed");
                retval.add(children[i]);
            }
            //String filename = children[i];
            }
        }
        return retval;
    }
    
    
//    public boolean moveCorruptedFile(String filename,String errorHL7dir){
//       logger.info("Moving Corrupted File: "+filename);
//       boolean retval = true;
//       // File (or directory) to be moved
//       File file = new File(filename);
//       Calendar cal =  Calendar.getInstance();
//       Long lon = new Long(cal.getTimeInMillis());
//       String str = new String ( file.getName()+"."+lon.toString() );
//       
//       File file2 = new File(str);        
//       boolean success = file.renameTo(file2);
//       file = null;
//       file2 = null;
//       file = new File(str);
//       if (!success) {
//        // File was not successfully renamed
//          logger.severe(" file: "+filename+" was unable to be renamed to "+str);
//          retval = false;
//       }else{                                   
//          // Destination directory
//          File dir = new File(errorHL7dir);
//
//          // Move file to new directory
//          boolean success2 = file.renameTo(new File(dir, str));
//          if (!success2) {
//             // File was not successfully moved
//            logger.severe(" file: "+str+" was unable to be moved to the "+errorHL7dir+" directory");
//            retval = false;
//          }else{
//            logger.warning("file: "+str+" was corrupted, please examines its contents");
//          }
//       }
//       return retval;
//    }
//    
    public boolean moveCorruptedFile(String filename,String errorHL7dir){
       logger.info("Moving Corrupted File: "+filename);
       boolean ret = moveFile( filename,errorHL7dir);
       if(ret){
          logger.info("file: "+filename+" has been processed, It has been moved to the completedHL7dir");
       }              
       return ret;
    }
    
    public boolean moveDupsFile(String filename,String dupsHL7dir){
       logger.info("Moving Duplicate File: "+filename);
       boolean ret = moveFile( filename,dupsHL7dir);
       if(ret){
          logger.info("file: "+filename+" has been processed, It has been moved to the dupsHL7dir");
       }              
       return ret;
    }
    
    public boolean moveCompletedFile(String filename,String completedHL7dir){
       logger.info("Moving Completed File: "+filename);
       boolean ret = moveFile( filename,completedHL7dir);
       if(ret){
          logger.info("file: "+filename+" has been processed, It has been moved to the completedHL7dir");
       }              
       return ret;
    }
    
    
    public boolean moveFile(String filename,String completedHL7dir){
       
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
             logger.severe(" file: "+str+" was moved to the "+completedHL7dir+" directory");
          }
       }
       return retval;
    }
    
}




