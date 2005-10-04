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
 * Ontario, Canada   Creates a new instance of LabUploadAction
 *
 *
 * LabUploadAction.java
 *
 * Created on August 22, 2005, 10:20 PM
 */

package oscar.oscarLab.ca.on.CML.Upload;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import oscar.*;
import oscar.oscarDB.*;
import oscar.oscarLab.*;
import oscar.oscarLab.ca.bc.PathNet.*;
import oscar.oscarLab.ca.bc.PathNet.HL7.*;
import oscar.oscarLab.ca.on.CML.*;
import oscar.util.*;


/**
 *
 * @author Jay Gallagher
 */
public class LabUploadAction extends Action {
   Logger _logger = Logger.getLogger(this.getClass());
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       LabUploadForm frm = (LabUploadForm) form; 
       FormFile importFile = frm.getImportFile();
       ArrayList warnings = new ArrayList();
       String filename = "";
       String proNo = (String) request.getSession().getAttribute("user");
       if(proNo == null){
          proNo = "";
       }
       String key = request.getParameter("key");
       String keyToMatch =  OscarProperties.getInstance().getProperty("CML_UPLOAD_KEY");
       System.out.println("key="+key);
       String outcome = "";
       
       if (key != null && keyToMatch != null && keyToMatch.equals(key)){
        
          try{  
             System.out.println("Lab Upload content type = "+importFile.getContentType());
             InputStream is = importFile.getInputStream();
             filename = importFile.getFileName();

             FileUploadCheck fileC = new FileUploadCheck();
             boolean fileUploadedSuccessfully = false;
             try{
                fileUploadedSuccessfully = fileC.addFile(filename,is,proNo);
                if (!fileUploadedSuccessfully){
                   outcome = "uploadedPreviously";
                }
             }catch(Exception addFileEx){
                   outcome = "databaseNotStarted";
             }
             is.reset();

             System.out.println("Was file uploaded successfully ?"+fileUploadedSuccessfully);
             if (fileUploadedSuccessfully){

                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(isr);                                       

                ABCDParser abc = new ABCDParser();     
                abc.parse(in);              
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);             
                abc.save(db.GetConnection());
                db.CloseConn();

                //conn.close(); 
                //SAVE FILE TO DISK
                is.reset();
                saveFile(is, filename);             
                outcome = "uploaded";
             }
          }catch(Exception e){ 
             e.printStackTrace(); 
             outcome = "exception";
          }  
       
       }else{
          outcome = "accessDenied";
       }
       request.setAttribute("outcome", outcome);
       System.out.println("forwarding outcome "+outcome);
       return mapping.findForward("success");
    }
   
   
   public LabUploadAction() {
   }
   
   
   /**
     * 
     * Save a Jakarta FormFile to a preconfigured place.
     * 
     * @param file
     * @return
     */
   public static boolean saveFile(InputStream stream,String filename ){
      String retVal = null;        
      boolean isAdded = true;
        
      try {
         OscarProperties props = OscarProperties.getInstance();
         //properties must exist            
         String place= props.getProperty("DOCUMENT_DIR");
            
         if(!place.endsWith("/"))
            place = new StringBuffer(place).insert(place.length(),"/").toString();
         retVal = place+"LabUpload."+filename+"."+(new Date()).getTime();
         System.out.println(retVal);
            
         //write the  file to the file specified
         OutputStream bos = new FileOutputStream(retVal);
         int bytesRead = 0;            
         while ((bytesRead = stream.read()) != -1){
            bos.write(bytesRead);
         }
         bos.close();

         //close the stream
         stream.close();
      }catch (FileNotFoundException fnfe) {

         System.out.println("File not found");
         fnfe.printStackTrace();            
         return isAdded=false;

      }catch (IOException ioe) {
         ioe.printStackTrace();
         return isAdded=false;
      }   
      return isAdded;
   }
   
}
