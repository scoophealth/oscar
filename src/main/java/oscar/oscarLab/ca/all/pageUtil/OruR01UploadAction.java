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


package oscar.oscarLab.ca.all.pageUtil;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import oscar.util.DateUtils;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

public class OruR01UploadAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private static Logger logger = MiscUtils.getLogger();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
    		throw new SecurityException("missing required security object (_lab)");
    	}
    	
    	try {
	        OruR01UploadForm oruR01UploadForm = (OruR01UploadForm) form;
	        FormFile formFile=oruR01UploadForm.getUploadFile();
	        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	        
	        Demographic demographic=getDemographicObject(oruR01UploadForm); 
	        
	        ObservationData observationData=new ObservationData();
	        observationData.subject=oruR01UploadForm.getSubject();
	        observationData.textMessage=oruR01UploadForm.getTextMessage();
	        observationData.binaryDataFileName=formFile.getFileName();
	        observationData.binaryData=formFile.getFileData();
	        
	    	Provider sendingProvider=loggedInInfo.getLoggedInProvider();

	    	ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
	    	ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(oruR01UploadForm.getProfessionalSpecialistId());
	        
	    	ClinicDAO clinicDAO=(ClinicDAO) SpringUtils.getBean("clinicDAO");
	    	Clinic clinic=clinicDAO.getClinic();
	    	
	        ORU_R01 hl7Message=OruR01.makeOruR01(clinic, demographic, observationData, sendingProvider, professionalSpecialist);
	        
	        int statusCode=SendingUtils.send(loggedInInfo, hl7Message, professionalSpecialist);
	        
	        if (HttpServletResponse.SC_OK==statusCode) WebUtils.addInfoMessage(request.getSession(), "Data successfully send.");
	        else throw(new ServletException("Error sending data. response code="+statusCode));
	        
        } catch (Exception e) {
	        logger.error("Unexpected error.", e);
	        WebUtils.addErrorMessage(request.getSession(), "An error occurred while sending this data, please try again or send this manually.");
        }

    	return(mapping.findForward("result"));
    }


	private static Demographic getDemographicObject(OruR01UploadForm oruR01UploadForm)
    {
    	Demographic demographic=new Demographic();
    	
    	demographic.setLastName(oruR01UploadForm.getClientLastName());
    	demographic.setFirstName(oruR01UploadForm.getClientFirstName());
    	demographic.setSex(oruR01UploadForm.getClientGender());
    	demographic.setHin(oruR01UploadForm.getClientHealthNumber());
    	
    	try
    	{
    		String temp=StringUtils.trimToNull(oruR01UploadForm.getClientBirthDay());
    		if (temp!=null)
    		{
	    		GregorianCalendar gregorianCalendar=DateUtils.toGregorianCalendarDate(temp);
	    		demographic.setBirthDay(gregorianCalendar);
    		}
    	}
    	catch (Exception e)
    	{
    		logger.warn("Error parsing date:"+oruR01UploadForm.getClientBirthDay(), e);
    	}
    	
    	return(demographic);
    }
}
