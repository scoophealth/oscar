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


package org.oscarehr.eyeform.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeformConsultationReportDao;
import org.oscarehr.eyeform.model.EyeformConsultationReport;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConReportListAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	EyeformConsultationReportDao crDao = SpringUtils.getBean(EyeformConsultationReportDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("unspecified");
		return list(mapping, form, request, response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		DynaValidatorForm testForm = (DynaValidatorForm) form;
		ConsultationReportFormBean crBean = (ConsultationReportFormBean)testForm.get("cr");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
        	throw new SecurityException("missing required security object (_billing)");
        }
		
		EyeformConsultationReport cr = new EyeformConsultationReport();
		if(crBean.getStatus() != null && crBean.getStatus().length()>0) {
			cr.setStatus(crBean.getStatus());
		}
		if(crBean.getProviderNo() != null && crBean.getProviderNo().length()>0) {
			cr.setProviderNo(crBean.getProviderNo());
		}
		
		//Filter by sites
		boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
		if(bMultisites)
		{
			String siteId = request.getParameter("siteId");
			if(siteId!=null && siteId.trim().length()>0 && !siteId.equals("-1"))
			{
				cr.setSiteId(Integer.parseInt(siteId));
			}
			
			String letterheadName = request.getParameter("letterheadName");
			if(letterheadName!=null && letterheadName.trim().length()>0 && !letterheadName.equals("-1"))
			{
				cr.setProviderNo(letterheadName);
			}
		}
		
		if(crBean.getDemographicNo() != null && crBean.getDemographicNo().length()>0) {
			cr.setDemographicNo(Integer.parseInt(crBean.getDemographicNo()));
			if(crBean.getDemographicName() == null || crBean.getDemographicName().equals("")) {
				Demographic d = demographicDao.getDemographic(crBean.getDemographicNo());
				if(d != null) {
					crBean.setDemographicName(d.getFormattedName());
				}
			}
		}
		
		Date startDate = null;
		Date endDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if(crBean.getStartDate() != null && crBean.getStartDate().length()>0) {
			try {
				startDate = formatter.parse(crBean.getStartDate());
			}catch(ParseException e) {
				logger.error("Error",e);
			}
		}
		if(crBean.getEndDate() != null && crBean.getEndDate().length()>0) {
			try {
				endDate = formatter.parse(crBean.getEndDate());
			}catch(ParseException e) {
				logger.error("Error",e);
			}
		}
		if(startDate == null && endDate == null) {
			endDate=new Date();
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.YEAR,-1);//Don't pull one year data, instead only pull one day data to make page loading quickly.
			cal.add(Calendar.DATE, -1);
			startDate = cal.getTime();
		}
		
		
		List<Provider> pl = providerDao.getActiveProviders();
		crBean.setProviderList(pl);

		List<EyeformConsultationReport> results = crDao.search(cr,startDate,endDate);
		for(EyeformConsultationReport crtmp:results) {
			crtmp.setDemographic(demographicDao.getClientByDemographicNo(crtmp.getDemographicNo()));
			crtmp.setProvider(providerDao.getProvider(crtmp.getProviderNo()));
		}
		request.setAttribute("conReportList",results);
		request.setAttribute("cr",cr);		
		request.setAttribute("dmname", crBean.getDemographicName());
		
		return mapping.findForward("list");
	}
}
