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


package oscar.login;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.service.AcceptableUseAgreementManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.pageUtil.LabUploadForm;

/**
 *
 * @author rjonasz
 */
public class UploadLoginTextAction extends Action {
    private static Logger _logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
			throw new SecurityException("missing required security object (_admin)");
		}
    	
       InputStream fis = null;
       FileOutputStream fos = null;
       LabUploadForm frm = (LabUploadForm) form;
       FormFile importFile = frm.getImportFile();
       boolean error = false;
       
       String validDurationNumber = request.getParameter("validDurationNumber");// verify it's a number
	   String validDurationPeriod = request.getParameter("validDurationPeriod");//verify it's one of these year month weeks days
	   String validForever = request.getParameter("validForever");
	   String foreverFrom = request.getParameter("foreverFrom");
       
	   _logger.debug("validDurationNumber "+validDurationNumber+" validDurationPeriod "+" validForever "+validForever+" foreverFrom "+foreverFrom );
	   
	   PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
	   Property prop = null;
	   
	   if(validForever != null && validForever.equals("forever")){
		   prop = new Property();
		   prop.setName("aua_valid_from");
		   prop.setValue(foreverFrom);		   
	   }else{ //time period was selected
		   try{
			   Integer.parseInt(validDurationNumber);
		   }catch(Exception e){
			   _logger.error("Not an Int:"+validDurationNumber,e);
		   }
		   
		   if(validDurationPeriod != null && ("year".equals(validDurationPeriod) || "month".equals(validDurationPeriod) ||  "weeks".equals(validDurationPeriod) || "days".equals(validDurationPeriod)   )){
			    prop = new Property();
			    prop.setName("aua_valid_duration");
			    prop.setValue(validDurationNumber +" "+validDurationPeriod);
		   }else{
			   _logger.error("Not a valid Period :"+validDurationPeriod);
		   }
	   }
	   
	   if(prop != null){
		   //Check to see if prop is still the same as last time.
		   Property latestProperty = AcceptableUseAgreementManager.findLatestProperty();
		   if(latestProperty == null || !prop.getValue().equals(latestProperty.getValue())){
			   propertyDao.persist(prop);
		   }else{
			   _logger.debug("No need to update. Same AcceptableUse Property as it was before");
		   }
	   }
	   

       try {
            if( importFile.getFileName().length() > 0 ) {
                fis = importFile.getInputStream();
                String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/OSCARloginText.txt";
                fos = new FileOutputStream(savePath);
                byte[] buf = new byte[128*1024];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
                error = false;
            }
       } catch( Exception e) {
           MiscUtils.getLogger().error("Error", e);  
           error = true;
       }

       request.setAttribute("error", error);
       return mapping.findForward("success");
    }
}
