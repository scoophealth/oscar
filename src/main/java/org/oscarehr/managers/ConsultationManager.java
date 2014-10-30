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

import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.consultations.ConsultationDao;
import org.oscarehr.consultations.ConsultationSearchFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ConsultationSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class ConsultationManager {

	@Autowired
	ConsultationDao consultationDao;
	
	public List<ConsultationSearchResult> search(LoggedInInfo loggedInInfo, ConsultationSearchFilter filter) {
		 List<ConsultationSearchResult> r = new  ArrayList<ConsultationSearchResult>();
		List<Object[]> result = consultationDao.search(filter);
		
		for(Object[] items:result) {
			ConsultationRequest consultRequest = (ConsultationRequest)items[0];
			LogAction.addLogSynchronous(loggedInInfo,"ConsultationManager.search", "id="+consultRequest.getId());
			r.add(convertToResult(items));
		}
		return r;
	}
	
	public int getConsultationCount(ConsultationSearchFilter filter) {
		return consultationDao.getConsultationCount2(filter);
	}
	
	private ConsultationSearchResult convertToResult(Object[] items) {
		ConsultationSearchResult result = new ConsultationSearchResult();
		
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
		} else if("6".equals(result.getStatus())) {
			result.setStatusDescription("Preliminary");
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
}
 