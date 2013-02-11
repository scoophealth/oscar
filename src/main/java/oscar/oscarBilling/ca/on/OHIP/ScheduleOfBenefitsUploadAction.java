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


package oscar.oscarBilling.ca.on.OHIP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import org.oscarehr.util.MiscUtils;

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

		boolean forceUpdate = false; 
		boolean updateAssistantFees = checkBox(request.getParameter("updateAssistantFees")); 
		boolean updateAnaesthetistFees = checkBox(request.getParameter("updateAnaesthetistFees")); 
		BigDecimal updateAssistantFeesValue = updateAssistantFees ? getBDValue(request.getParameter("updateAssistantFeesValue")) : null; 
		BigDecimal updateAnaesthetistFeesValue = updateAnaesthetistFees ? getBDValue(request.getParameter("updateAnaesthetistFeesValue")) : null; 
		try{  

			InputStream is = importFile.getInputStream();
			filename = importFile.getFileName();

			ScheduleOfBenefits sob = new ScheduleOfBenefits();
			String codeChanges  = request.getParameter("showChangedCodes");
			String newCodes = request.getParameter("showNewCodes");            

			boolean showNewCodes = checkBox(newCodes);
			boolean showChangedCodes = checkBox(codeChanges);              
			forceUpdate = checkBox(request.getParameter("forceUpdate"));

			warnings = sob.processNewFeeSchedule(is,showNewCodes,showChangedCodes,forceUpdate, updateAssistantFeesValue, updateAnaesthetistFeesValue);

		}catch(Exception e){ 
			MiscUtils.getLogger().error("Error", e); 
			outcome = "exception";
		} 
		MiscUtils.getLogger().debug("warnings "+warnings.size());
		request.setAttribute("warnings",warnings);
		request.setAttribute("outcome", outcome);
		request.setAttribute("forceUpdate", forceUpdate);
		if (forceUpdate) {    	   
			return mapping.findForward("forceUpdate");    	   
		}
		return mapping.findForward("success");
	}

	public ScheduleOfBenefitsUploadAction() { }

	private BigDecimal getBDValue(String value) { 
		if (value == null || value.trim().equals("")) { return BigDecimal.ZERO; } 
		return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP); 
	} 



	/**
	 * 
	 * @param stream
	 * @param filename
	 * @return boolean
	 */
	public static boolean saveFile(InputStream stream,String filename ){
		String retVal = null;        
		boolean isAdded = true;

		try {
			//retrieve the file data
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
