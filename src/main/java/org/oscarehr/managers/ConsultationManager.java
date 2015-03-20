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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.ConsultRequestDao;
import org.oscarehr.common.dao.ConsultResponseDao;
import org.oscarehr.common.dao.ConsultResponseDocDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.ConsultResponseDoc;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.consultations.ConsultationRequestSearchFilter;
import org.oscarehr.consultations.ConsultationResponseSearchFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ConsultationRequestSearchResult;
import org.oscarehr.ws.rest.to.model.ConsultationResponseSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class ConsultationManager {

	@Autowired
	ConsultRequestDao consultationRequestDao;
	@Autowired
	ConsultResponseDao consultationResponseDao;
	@Autowired
	ConsultationServiceDao serviceDao;
	@Autowired
	ProfessionalSpecialistDao professionalSpecialistDao;
	@Autowired
	ConsultDocsDao requestDocDao;
	@Autowired
	ConsultResponseDocDao responseDocDao;
	@Autowired
	PropertyDao propertyDao;
	@Autowired
	SecurityInfoManager securityInfoManager;

	public final String CON_REQUEST_ENABLED = "consultRequestEnabled";
	public final String CON_RESPONSE_ENABLED = "consultResponseEnabled";
	public final String ENABLED_YES = "Y";
	
	public List<ConsultationRequestSearchResult> search(LoggedInInfo loggedInInfo, ConsultationRequestSearchFilter filter) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		List<ConsultationRequestSearchResult> r = new  ArrayList<ConsultationRequestSearchResult>();
		List<Object[]> result = consultationRequestDao.search(filter);
		
		for(Object[] items:result) {
			ConsultationRequest consultRequest = (ConsultationRequest)items[0];
			LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.searchRequest", "id="+consultRequest.getId());
			r.add(convertToRequestSearchResult(items));
		}
		return r;
	}
	
	public List<ConsultationResponseSearchResult> search(LoggedInInfo loggedInInfo, ConsultationResponseSearchFilter filter) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		List<ConsultationResponseSearchResult> r = new  ArrayList<ConsultationResponseSearchResult>();
		List<Object[]> result = consultationResponseDao.search(filter);
		
		for(Object[] items:result) {
			ConsultationResponse consultResponse = (ConsultationResponse)items[0];
			LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.searchResponse", "id="+consultResponse.getId());
			r.add(convertToResponseSearchResult(items));
		}
		return r;
	}
	
	public int getConsultationCount(ConsultationRequestSearchFilter filter) {
		return consultationRequestDao.getConsultationCount2(filter);
	}
	
	public int getConsultationCount(ConsultationResponseSearchFilter filter) {
		return consultationResponseDao.getConsultationCount(filter);
	}
	
	public ConsultationRequest getRequest(LoggedInInfo loggedInInfo, Integer id) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		ConsultationRequest request = consultationRequestDao.find(id);
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.getRequest", "id="+request.getId());
		
		return request;
	}
	
	public ConsultationResponse getResponse(LoggedInInfo loggedInInfo, Integer id) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		ConsultationResponse response = consultationResponseDao.find(id);
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.getResponse", "id="+response.getId());
		
		return response;
	}
	
	public List<ConsultationServices> getConsultationServices() {
		List<ConsultationServices> services = serviceDao.findActive();
		for (ConsultationServices service : services) {
			if (service.getServiceDesc().equals(serviceDao.REFERRING_DOCTOR)) {
				services.remove(service);
				break;
			}
		}
		return services;
	}
	
	public List<ProfessionalSpecialist> getReferringDoctorList() {
		ConsultationServices service = serviceDao.findReferringDoctorService(serviceDao.ACTIVE_ONLY);
		return (service==null) ? null : service.getSpecialists();
	}
	
	public ProfessionalSpecialist getProfessionalSpecialist(Integer id) {
		return professionalSpecialistDao.find(id);
	}
	
	public void saveConsultationRequest(LoggedInInfo loggedInInfo, ConsultationRequest request) {
		if (request.getId()==null) { //new consultation request
			checkPrivilege(loggedInInfo, securityInfoManager.WRITE);
			
			ProfessionalSpecialist specialist = request.getProfessionalSpecialist();
			request.setProfessionalSpecialist(null);
			consultationRequestDao.persist(request);
			
			request.setProfessionalSpecialist(specialist);
			consultationRequestDao.merge(request);
		} else {
			checkPrivilege(loggedInInfo, securityInfoManager.UPDATE);
			
			consultationRequestDao.merge(request);
		}
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.saveConsultationRequest", "id="+request.getId());
	}
	
	public void saveConsultationResponse(LoggedInInfo loggedInInfo, ConsultationResponse response) {
		if (response.getId()==null) { //new consultation response
			checkPrivilege(loggedInInfo, securityInfoManager.WRITE);
			
			consultationResponseDao.persist(response);
		} else {
			checkPrivilege(loggedInInfo, securityInfoManager.UPDATE);
			
			consultationResponseDao.merge(response);
		}
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.saveConsultationResponse", "id="+response.getId());
	}
	
	public List<ConsultDocs> getConsultRequestDocs(LoggedInInfo loggedInInfo, Integer requestId) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		List<ConsultDocs> docs = requestDocDao.findByRequestId(requestId);
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.getConsultRequestDocs", "consult id="+requestId);
		
		return docs;
	}
	
	public List<ConsultResponseDoc> getConsultResponseDocs(LoggedInInfo loggedInInfo, Integer responseId) {
		checkPrivilege(loggedInInfo, securityInfoManager.READ);
		
		List<ConsultResponseDoc> docs = responseDocDao.findByResponseId(responseId);
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.getConsultResponseDocs", "consult id="+responseId);
		
		return docs;
	}
	
	public void saveConsultRequestDoc(LoggedInInfo loggedInInfo, ConsultDocs doc) {
		checkPrivilege(loggedInInfo, securityInfoManager.UPDATE);
		
		if (doc.getId()==null) { //new consultation attachment
			requestDocDao.persist(doc);
		} else {
			requestDocDao.merge(doc); //only used for setting doc "deleted"
		}
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.saveConsultRequestDoc", "id="+doc.getId());
	}
	
	public void saveConsultResponseDoc(LoggedInInfo loggedInInfo, ConsultResponseDoc doc) {
		checkPrivilege(loggedInInfo, securityInfoManager.UPDATE);
		
		if (doc.getId()==null) { //new consultation attachment
			responseDocDao.persist(doc);
		} else {
			responseDocDao.merge(doc); //only used for setting doc "deleted"
		}
		LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.saveConsultResponseDoc", "id="+doc.getId());
	}
	
	public void enableConsultRequestResponse(boolean conRequest, boolean conResponse) {
		Property consultRequestEnabled = new Property(CON_REQUEST_ENABLED);
		Property consultResponseEnabled = new Property(CON_RESPONSE_ENABLED);
		
		List<Property> results = propertyDao.findByName(CON_REQUEST_ENABLED);
		if (results.size()>0) consultRequestEnabled = results.get(0);
		results = propertyDao.findByName(CON_RESPONSE_ENABLED);
		if (results.size()>0) consultResponseEnabled = results.get(0);
		
		consultRequestEnabled.setValue(conRequest?ENABLED_YES:null);
		consultResponseEnabled.setValue(conResponse?ENABLED_YES:null);
		
		propertyDao.merge(consultRequestEnabled);
		propertyDao.merge(consultResponseEnabled);
		
		ConsultationServices referringDocService = serviceDao.findReferringDoctorService(serviceDao.WITH_INACTIVE);
		if (referringDocService==null) referringDocService = new ConsultationServices(serviceDao.REFERRING_DOCTOR);
		if (conResponse) referringDocService.setActive(serviceDao.ACTIVE);
		else referringDocService.setActive(serviceDao.INACTIVE);
		
		serviceDao.merge(referringDocService);
	}
	
	public boolean isConsultRequestEnabled() {
		List<Property> results = propertyDao.findByName(CON_REQUEST_ENABLED);
		if (results.size()>0 && ENABLED_YES.equals(results.get(0).getValue())) return true;
		return false;
	}
	
	public boolean isConsultResponseEnabled() {
		List<Property> results = propertyDao.findByName(CON_RESPONSE_ENABLED);
		if (results.size()>0 && ENABLED_YES.equals(results.get(0).getValue())) return true;
		return false;
	}
	
	
	
	private ConsultationRequestSearchResult convertToRequestSearchResult(Object[] items) {
		ConsultationRequestSearchResult result = new ConsultationRequestSearchResult();
		
		ConsultationRequest consultRequest = (ConsultationRequest)items[0];
		ProfessionalSpecialist professionalSpecialist = (ProfessionalSpecialist)items[1];
		ConsultationServices consultationServices = (ConsultationServices)items[2];
		Demographic demographic = (Demographic)items[3];
		Provider provider = (Provider)items[4];
		
		
		result.setAppointmentDate(joinDateAndTime(consultRequest.getAppointmentDate(),consultRequest.getAppointmentTime()));
		result.setConsultant(professionalSpecialist);
		result.setDemographic(demographic);
		result.setId(consultRequest.getId());
		result.setLastFollowUp(consultRequest.getFollowUpDate());
		result.setMrp(provider);
		result.setReferralDate(consultRequest.getReferralDate());
		result.setServiceName(consultationServices.getServiceDesc());
		result.setStatus(consultRequest.getStatus());
		
		if("1".equals(result.getStatus())) {
			result.setStatusDescription("Nothing");
		} else if("2".equals(result.getStatus())) {
			result.setStatusDescription("Pending Specialist Callback");
		} else if("3".equals(result.getStatus())) {
			result.setStatusDescription("Pending Patient Callback");
		} else if("4".equals(result.getStatus())) {
			result.setStatusDescription("Completed");
		} else if("5".equals(result.getStatus())) {
			result.setStatusDescription("Cancelled");
		}
		
		result.setUrgency(consultRequest.getUrgency());
		if("1".equals(result.getUrgency())) {
			result.setUrgencyDescription("Urgent");
		} else if("2".equals(result.getUrgency())) {
			result.setUrgencyDescription("Non-Urgent");
		}else if("3".equals(result.getUrgency())) {
			result.setUrgencyDescription("Return");
		}

		if(consultRequest.getSendTo() != null && !consultRequest.getSendTo().isEmpty() && !consultRequest.getSendTo().equals("-1")) {
			result.setTeamName(consultRequest.getSendTo());	
		}
		
		return result;
	}
	
	private ConsultationResponseSearchResult convertToResponseSearchResult(Object[] items) {
		ConsultationResponseSearchResult result = new ConsultationResponseSearchResult();
		
		ConsultationResponse consultResponse = (ConsultationResponse)items[0];
		ProfessionalSpecialist professionalSpecialist = (ProfessionalSpecialist)items[1];
		Demographic demographic = (Demographic)items[2];
		Provider provider = (Provider)items[3];
		
		
		result.setAppointmentDate(joinDateAndTime(consultResponse.getAppointmentDate(),consultResponse.getAppointmentTime()));
		result.setReferringDoctor(professionalSpecialist);
		result.setDemographic(demographic);
		result.setId(consultResponse.getId());
		result.setLastFollowUp(consultResponse.getFollowUpDate());
		result.setProvider(provider);
		result.setReferralDate(consultResponse.getReferralDate());
		result.setStatus(consultResponse.getStatus());
		
		if("1".equals(result.getStatus())) {
			result.setStatusDescription("Nothing");
		} else if("2".equals(result.getStatus())) {
			result.setStatusDescription("Pending Referring Doctor Callback");
		} else if("3".equals(result.getStatus())) {
			result.setStatusDescription("Pending Patient Callback");
		} else if("4".equals(result.getStatus())) {
			result.setStatusDescription("Completed");
		} else if("5".equals(result.getStatus())) {
			result.setStatusDescription("Cancelled");
		}
		
		result.setUrgency(consultResponse.getUrgency());
		if("1".equals(result.getUrgency())) {
			result.setUrgencyDescription("Urgent");
		} else if("2".equals(result.getUrgency())) {
			result.setUrgencyDescription("Non-Urgent");
		}else if("3".equals(result.getUrgency())) {
			result.setUrgencyDescription("Return");
		}

		if(consultResponse.getSendTo() != null && !consultResponse.getSendTo().isEmpty() && !consultResponse.getSendTo().equals("-1")) {
			result.setTeamName(consultResponse.getSendTo());	
		}
		
		return result;
	}
	
	private Date joinDateAndTime(Date date, Date time) {
		
		if(date == null || time == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
	
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(time);
		
		cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
		
		return cal.getTime();
	}
	
	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", privilege, null)) {
			throw new RuntimeException("Access Denied");
		}
	}
}
