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

package org.oscarehr.consultations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.PaginationUtils;
import org.oscarehr.util.SpringUtils;

@Deprecated
public class ConsultationAction extends Action {

	private ConsultationService consultationRequestService = SpringUtils.getBean(ConsultationService.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ConsultationServiceDao consultationServiceDao = SpringUtils.getBean(ConsultationServiceDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private PaginationUtils paginationUtils = new PaginationUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
        	throw new SecurityException("missing required security object (_con)");
        }
		
		//grab and execute query
		ConsultationQuery query = new ConsultationQuery();
		paginationUtils.loadPaginationQuery(request, query);
		query.setDateType(request.getParameter("dateType"));
		query.setComplete(request.getParameter("complete"));
		query.setDateType(request.getParameter("dateType"));
		query.setWithOption(request.getParameter("withOption"));
		query.setTeam(request.getParameter("team"));
		query.setStatus(request.getParameter("status"));
		
		//sort out the totals
		Map<String, Object> map = new HashMap<String, Object>();
		int total = consultationRequestService.getConsultationCount(query);
		map.put("iTotalRecords", total);
		map.put("iTotalDisplayRecords", total);
		
		//these are our display objects
		List<ConsultationData> list = new ArrayList<ConsultationData>();
		for (ConsultationRequest consult : consultationRequestService.listConsultationRequests(loggedInInfo, query)) {
			ConsultationData data = convertToConsultationDataObject(consult);
			list.add(data);
		}
		map.put("aaData", JSONArray.fromObject(list));

		//return as JSON
		JSONObject jsonObject = JSONObject.fromObject(map);
		PrintWriter out = null;
		try {
			if (null != jsonObject) {
				out = response.getWriter();
				response.getWriter().print(jsonObject.toString());
			}
		} catch (IOException e1) {

		} finally {
			if (null != out) {
				out.close();
			}
		}
		
		return null;
	}
	
	private ConsultationData convertToConsultationDataObject(ConsultationRequest consult) {
		//get the data i need to support
		ConsultationData data = new ConsultationData();
		Demographic demo = this.demographicDao.getDemographicById(consult.getDemographicId());
		ConsultationServices services = this.consultationServiceDao.find(consult.getServiceId());
		
		data.setStatus(consult.getStatus());
		data.setUrgency(paginationUtils.parseInt(consult.getUrgency()));
		
		if ("-1".equals(consult.getSendTo())) {
			data.setSendTo(null);
		} else {
			data.setSendTo(consult.getSendTo());
		}
		data.setPatient(demo.getFormattedName());
			
		String providerId = demo.getProviderNo();
		if (providerId != null && !providerId.equals("")) {
			Provider provider = providerDao.getProvider(providerId);
			data.setProviderName(provider.getFormattedName());
			data.setProviderNo(provider.getProviderNo());
		} else {
			data.setProviderName(null);
		}
		data.setServiceId(consult.getServiceId().toString());
		data.setServiceDesc(services.getServiceDesc());
		if (consult.getProfessionalSpecialist() == null) {
			data.setSpecialistName(null);
		} else {
			ProfessionalSpecialist specialist = consult.getProfessionalSpecialist();
			String specialistName = specialist.getLastName() + ", " + specialist.getFirstName();
			data.setSpecialistName(specialistName);
		}
		data.setDemographicNo(consult.getDemographicId().toString());
		if (consult.getReferralDate() != null) {
			data.setReferralDate(DateFormatUtils.format(consult.getReferralDate(),"yyyy-MM-dd"));
		}
		data.setId(consult.getId().toString());
		
		Date apptDate = consult.getAppointmentDate();
		Date apptTime = consult.getAppointmentTime();
		if(apptDate != null && apptTime != null) {
			data.setAppointmentDate(DateFormatUtils.format(apptDate,"yyyy-MM-dd") + " " + DateFormatUtils.format(apptTime,"HH:mm"));
		}
		data.setPatientWillBook(consult.isPatientWillBook());

		apptDate = consult.getFollowUpDate();
		if (apptDate != null) {
			data.setFollowUpDate(DateFormatUtils.format(apptDate,"yyyy-MM-dd"));
		}
		return data;
	}
}
