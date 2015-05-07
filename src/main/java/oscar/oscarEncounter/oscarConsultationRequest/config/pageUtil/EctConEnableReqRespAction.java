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


package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Property;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class EctConEnableReqRespAction extends Action
{
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "u", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
		
    	PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean(PropertyDao.class);
    	ConsultationServiceDao serviceDao = (ConsultationServiceDao)SpringUtils.getBean(ConsultationServiceDao.class);
    	
    	ConsultationManager manager = new ConsultationManager();
    	EctConEnableReqRespForm eRRForm = (EctConEnableReqRespForm)form;
    	
		Property consultRequestEnabled = new Property(manager.CON_REQUEST_ENABLED);
		Property consultResponseEnabled = new Property(manager.CON_RESPONSE_ENABLED);
		
		List<Property> results = propertyDao.findByName(manager.CON_REQUEST_ENABLED);
		if (results.size()>0) consultRequestEnabled = results.get(0);
		results = propertyDao.findByName(manager.CON_RESPONSE_ENABLED);
		if (results.size()>0) consultResponseEnabled = results.get(0);
		
		consultRequestEnabled.setValue(eRRForm.isConsultRequestEnabled()?manager.ENABLED_YES:null);
		consultResponseEnabled.setValue(eRRForm.isConsultResponseEnabled()?manager.ENABLED_YES:null);
		
		propertyDao.merge(consultRequestEnabled);
		propertyDao.merge(consultResponseEnabled);
		
		ConsultationServices referringDocService = serviceDao.findReferringDoctorService(serviceDao.WITH_INACTIVE);
		if (referringDocService==null) referringDocService = new ConsultationServices(serviceDao.REFERRING_DOCTOR);
		if (eRRForm.isConsultResponseEnabled()) referringDocService.setActive(serviceDao.ACTIVE);
		else referringDocService.setActive(serviceDao.INACTIVE);
		
		serviceDao.merge(referringDocService);
        
        request.setAttribute("ENABLE_REQUEST_RESPONSE_UPDATED", "updated");
        return mapping.findForward("success");
    }
}
