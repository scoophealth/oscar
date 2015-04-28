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


package oscar.oscarLab.ca.bc.PathNet.pageUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.bc.PathNet.Connection;
import oscar.oscarLab.ca.bc.PathNet.HL7.Message;


/**
 *
 * @author Jay Gallagher
 */
public class LabUploadAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	Logger _logger = Logger.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
  	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
  			throw new SecurityException("missing required security object (_lab)");
  		}
  	   
       LabUploadForm frm = (LabUploadForm) form;
       FormFile importFile = frm.getImportFile();
       String filename = "";
       String proNo = (String) request.getSession().getAttribute("user");
       String outcome = "";

       try{
          MiscUtils.getLogger().debug("Lab Upload content type = "+importFile.getContentType());
          InputStream is = importFile.getInputStream();
          filename = importFile.getFileName();

          int check = FileUploadCheck.addFile(filename,is,proNo);
          is.reset();
          if (check != FileUploadCheck.UNSUCCESSFUL_SAVE){
             Connection connection = new Connection();
             ArrayList<String> messages = connection.Retrieve(is);
             if (messages != null) {
                try {
                   int size = messages.size();

                   String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                   for (int i = 0; i < size; i++) {
                      if (_logger.isDebugEnabled()){ _logger.debug("Call Message Constructor for message # "+i); }
                      Message message = new Message(now);
                      if (_logger.isDebugEnabled()){ _logger.debug("Call Message.Parse for message # "+i); }
                      message.Parse( messages.get(i));
                      if (_logger.isDebugEnabled()){ _logger.debug("Call Message.ToDatabase for message # "+i); }
                      message.ToDatabase();
                   }
                   outcome = "success";
                }
                catch (Exception ex) {
                   //success = false; //<- for future when transactional
                   _logger.error("Error - oscar.PathNet.Contorller - Message: "+ ex.getMessage()+ " = "+ ex.toString(), ex);
                   outcome = "exception";
                }
               //connection.Acknowledge(success);
             }
             //SAVE FILE TO DISK
             is.reset();
             saveFile(is, filename);
          }else{
             outcome = "uploadedPreviously";
          }
       }catch(Exception e){
          MiscUtils.getLogger().error("Error", e);
          outcome = "exception";
       }
       request.setAttribute("outcome", outcome);
       return mapping.findForward("success");
    }


   public LabUploadAction() {
   }


 /**
  * Save a Jakarta FormFile to a preconfigured place.
  * @param stream
  * @param filename
  * @return boolean
  */
    private static boolean saveFile(InputStream stream,String filename ){
        String retVal = null;
        boolean isAdded = true;

        try {
            //retrieve the file data
           // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //InputStream stream = file.getInputStream();
            OscarProperties props = OscarProperties.getInstance();

            //properties must exist
            String place= props.getProperty("DOCUMENT_DIR");

            if(!place.endsWith("/"))
                    place = new StringBuilder(place).insert(place.length(),"/").toString();
            retVal = place+"LabUpload."+filename+"."+(new Date()).getTime();
            MiscUtils.getLogger().debug(retVal);
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

            MiscUtils.getLogger().debug("File not found");
            MiscUtils.getLogger().error("Error", fnfe);
            return isAdded=false;

        }
        catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
            return isAdded=false;
        }

        return isAdded;
    }

}
