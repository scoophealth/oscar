/*
 * Utilities.java
 *
 * Created on May 31, 2007, 2:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public class Utilities {
       
    private Utilities() {
    	// utils shouldn't be instantiated
    }
    
    public static ArrayList<String> separateMessages(String fileName) throws Exception{
                
        ArrayList<String> messages = new ArrayList<String>();
        try{
            InputStream is = new FileInputStream(fileName);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            String line = null;
            boolean firstPIDflag = false; //true if the first PID segment has been processed false otherwise
            boolean firstMSHflag = false; //true if the first MSH segment has been processed false otherwise
            //String mshSeg = br.readLine();
            
            StringBuilder sb = new StringBuilder();
            String mshSeg = "";
            
            while ((line = br.readLine()) != null) {
                if (line.length() > 3){
                    if (line.substring(0, 3).equals("MSH")){
                        if (firstMSHflag){
                            messages.add(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        mshSeg = line;
                        firstMSHflag = true;
                        firstPIDflag = false;
                    } else if (line.substring(0, 3).equals("PID")){
                        if (firstPIDflag){
                            messages.add(sb.toString());
                            sb.delete(0, sb.length());
                            sb.append(mshSeg + "\r\n");
                        }
                        firstPIDflag = true;
                    }
                    sb.append(line + "\r\n");
                }
            }
                       
            // add the last message
            messages.add(sb.toString());
            
            is.close();
            br.close();
        }catch(Exception e){
            throw e;
        }
        
        return(messages);
    }
    
    
    /**
     *
     * Save a Jakarta FormFile to a preconfigured place.
     *
     * @param file
     * @return
     */
    public static String saveFile(InputStream stream,String filename ){
        String retVal = null;
        
        
        try {
            OscarProperties props = OscarProperties.getInstance();
            //properties must exist
            String place= props.getProperty("DOCUMENT_DIR");
            
            if(!place.endsWith("/"))
                place = new StringBuilder(place).insert(place.length(),"/").toString();
            retVal = place+"LabUpload."+filename.replaceAll(".enc", "")+"."+(new Date()).getTime();
            
            //write the  file to the file specified
            OutputStream os = new FileOutputStream(retVal);
            
            int bytesRead = 0;
            while ((bytesRead = stream.read()) != -1){
                os.write(bytesRead);
            }
            os.close();
            
            //close the stream
            stream.close();
        }catch (FileNotFoundException fnfe) {
            MiscUtils.getLogger().error("Error", fnfe);
            return retVal;
            
        }catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
            return retVal;
        }
        return retVal;
    }    
    
    
    /*
     *  Return a string corresponding to the data in a given InputStream
     */
    public static String inputStreamAsString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        
        stream.close();
        br.close();
        return sb.toString();
    }
}
