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
package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ConsultationRequestTo1;
import org.springframework.stereotype.Component;

@Component
public class ConsultationRequestConverter extends AbstractConverter<ConsultationRequest, ConsultationRequestTo1> {
	
	public ConsultationRequest getAsDomainObject(LoggedInInfo loggedInInfo, ConsultationRequestTo1 t, ConsultationRequest d) throws ConversionException {
		d.setAllergies(t.getAllergies());
		d.setAppointmentDate(t.getAppointmentDate());
		d.setAppointmentTime(t.getAppointmentTime());
		d.setClinicalInfo(t.getClinicalInfo());
		d.setConcurrentProblems(t.getConcurrentProblems());
		d.setCurrentMeds(t.getCurrentMeds());
		d.setDemographicId(t.getDemographicId());
		d.setFollowUpDate(t.getFollowUpDate());
		d.setLetterheadAddress(t.getLetterheadAddress());
		d.setLetterheadFax(t.getLetterheadFax());
		d.setLetterheadName(t.getLetterheadName());
		d.setLetterheadPhone(t.getLetterheadPhone());
		d.setPatientWillBook(t.isPatientWillBook());
		d.setProviderNo(t.getProviderNo());
		d.setReasonForReferral(t.getReasonForReferral());
		d.setReferralDate(t.getReferralDate());
		d.setSendTo(t.getSendTo());
		d.setServiceId(t.getServiceId());
		d.setSignatureImg(t.getSignatureImg());
		d.setSiteName(t.getSiteName());
		d.setStatus(t.getStatus());
		d.setStatusText(t.getStatusText());
		d.setUrgency(t.getUrgency());
		
		return d;
	}
	
	@Override
	public ConsultationRequest getAsDomainObject(LoggedInInfo loggedInInfo, ConsultationRequestTo1 t) throws ConversionException {
		return getAsDomainObject(loggedInInfo, t, new ConsultationRequest());
	}

	@Override
	public ConsultationRequestTo1 getAsTransferObject(LoggedInInfo loggedInInfo, ConsultationRequest d) throws ConversionException {
		ConsultationRequestTo1 t = new ConsultationRequestTo1();
		
		t.setId(d.getId());
		t.setAllergies(d.getAllergies());
		t.setAppointmentDate(d.getAppointmentDate());
		t.setAppointmentTime(d.getAppointmentTime());
		t.setClinicalInfo(d.getClinicalInfo());
		t.setConcurrentProblems(d.getConcurrentProblems());
		t.setCurrentMeds(d.getCurrentMeds());
		t.setDemographicId(d.getDemographicId());
		t.setFollowUpDate(d.getFollowUpDate());
		t.setLetterheadAddress(d.getLetterheadAddress());
		t.setLetterheadFax(d.getLetterheadFax());
		t.setLetterheadName(d.getLetterheadName());
		t.setLetterheadPhone(d.getLetterheadPhone());
		t.setPatientWillBook(d.isPatientWillBook());
		t.setProviderNo(d.getProviderNo());
		t.setReasonForReferral(d.getReasonForReferral());
		t.setReferralDate(d.getReferralDate());
		t.setSendTo(d.getSendTo());
		t.setServiceId(d.getServiceId());
		t.setSignatureImg(d.getSignatureImg());
		t.setSiteName(d.getSiteName());
		t.setStatus(d.getStatus());
		t.setStatusText(d.getStatusText());
		t.setUrgency(d.getUrgency());
		t.setProfessionalSpecialist((new ProfessionalSpecialistConverter()).getAsTransferObject(loggedInInfo, d.getProfessionalSpecialist()));
	
		return t;
	}
	

}
