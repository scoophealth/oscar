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

package oscar.oscarBilling.ca.on.OHIP;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import oscar.OscarProperties;
import oscar.oscarLab.ca.bc.PathNet.pageUtil.LabUploadForm;



/**
 *
 * @author Jay Gallagher
 */
public class ScheduleOfBenefitsUploadAction extends Action {
   Logger _logger = Logger.getLogger(this.getClass());
   
   boolean checkBox (String str){
      boolean check = false;
      if ( str != null && str.equals("on")){
         check = true;
      }
      return check;
   }
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       LabUploadForm frm = (LabUploadForm) form; 
       FormFile importFile = frm.getImportFile();
       List warnings = new ArrayList();
       String filename = "";
       String proNo = (String) request.getSession().getAttribute("user");
       String outcome = "";
        
       try{  
          //System.out.println("Schedule Of Benefits content type = "+importFile.getContentType());
          InputStream is = importFile.getInputStream();
          filename = importFile.getFileName();
          
          ScheduleOfBenefits sob = new ScheduleOfBenefits();
          String codeChanges  = request.getParameter("showChangedCodes");
          String newCodes = request.getParameter("showNewCodes");            
          
          
          boolean showNewCodes = checkBox(newCodes);
          boolean showChangedCodes = checkBox(codeChanges);
          
          warnings = sob.processNewFeeSchedule(is,showNewCodes,showChangedCodes);                    
          
       }catch(Exception e){ 
          e.printStackTrace(); 
          outcome = "exception";
       } 
       System.out.println("warnings "+warnings.size());
       request.setAttribute("warnings",warnings);
       request.setAttribute("outcome", outcome);
       return mapping.findForward("success");
    }
      
    public ScheduleOfBenefitsUploadAction() {
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
            //retrieve the file data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //InputStream stream = file.getInputStream();
            OscarProperties props = OscarProperties.getInstance();

            //properties must exist            
            String place= props.getProperty("DOCUMENT_DIR");
            
            if(!place.endsWith("/"))
                    place = new StringBuffer(place).insert(place.length(),"/").toString();
            retVal = place+"LabUpload."+filename+"."+(new Date()).getTime();
            System.out.println(retVal);
            //write the file to the file specified
            OutputStream bos = new FileOutputStream(retVal);
            int bytesRead = 0;
            //byte[] buffer = file.getFileData();
            //while ((bytesRead = stream.read(buffer)) != -1){
            //   bos.write(buffer, 0, bytesRead);            
            while ((bytesRead = stream.read()) != -1){
                    bos.write(bytesRead);
            }
            bos.close();

            //close the stream
            stream.close();
        }
        catch (FileNotFoundException fnfe) {
            
            System.out.println("File not found");
            fnfe.printStackTrace();            
            return isAdded=false;
            
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return isAdded=false;
        }

        return isAdded;
    }
   
}
