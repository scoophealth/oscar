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

import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ConsultationResponseTo1;
import org.springframework.stereotype.Component;

@Component
public class ConsultationResponseConverter extends AbstractConverter<ConsultationResponse, ConsultationResponseTo1> {
	
	public ConsultationResponse getAsDomainObject(LoggedInInfo loggedInInfo, ConsultationResponseTo1 t, ConsultationResponse d) throws ConversionException {
		d.setResponseDate(t.getResponseDate());
		d.setReferralDate(t.getReferralDate());
		d.setReferringDocId(t.getReferringDoctor().getId());
		d.setAppointmentDate(t.getAppointmentDate());
		d.setAppointmentTime(t.getAppointmentTime());
		d.setAppointmentNote(t.getAppointmentNote());
		d.setFollowUpDate(t.getFollowUpDate());
		d.setReferralReason(t.getReferralReason());
		d.setExamination(t.getExamination());
		d.setImpression(t.getImpression());
		d.setPlan(t.getPlan());
		d.setClinicalInfo(t.getClinicalInfo());
		d.setCurrentMeds(t.getCurrentMeds());
		d.setConcurrentProblems(t.getConcurrentProblems());
		d.setAllergies(t.getAllergies());
		d.setProviderNo(t.getProviderNo());
		d.setDemographicNo(t.getDemographic().getDemographicNo());
		d.setStatus(t.getStatus());
		d.setSendTo(t.getSendTo());
		d.setUrgency(t.getUrgency());
		d.setSignatureImg(t.getSignatureImg());
		d.setLetterheadName(t.getLetterheadName());
		d.setLetterheadAddress(t.getLetterheadAddress());
		d.setLetterheadPhone(t.getLetterheadPhone());
		d.setLetterheadFax(t.getLetterheadFax());
		
		return d;
	}
	
	@Override
	public ConsultationResponse getAsDomainObject(LoggedInInfo loggedInInfo, ConsultationResponseTo1 t) throws ConversionException {
		return getAsDomainObject(loggedInInfo, t, new ConsultationResponse());
	}

	@Override
	public ConsultationResponseTo1 getAsTransferObject(LoggedInInfo loggedInInfo, ConsultationResponse d) throws ConversionException {
		ConsultationResponseTo1 t = new ConsultationResponseTo1();
		
		/* cannot set the following 2 fields from domain object
		 * t.setDemographic(...);
		 * t.setReferringDoctor(...);
		*/
		t.setId(d.getId());
		t.setResponseDate(d.getResponseDate());
		t.setReferralDate(d.getReferralDate());
		t.setAppointmentDate(d.getAppointmentDate());
		t.setAppointmentTime(d.getAppointmentTime());
		t.setAppointmentNote(d.getAppointmentNote());
		t.setFollowUpDate(d.getFollowUpDate());
		t.setReferralReason(d.getReferralReason());
		t.setExamination(d.getExamination());
		t.setImpression(d.getImpression());
		t.setPlan(d.getPlan());
		t.setClinicalInfo(d.getClinicalInfo());
		t.setCurrentMeds(d.getCurrentMeds());
		t.setConcurrentProblems(d.getConcurrentProblems());
		t.setAllergies(d.getAllergies());
		t.setProviderNo(d.getProviderNo());
		t.setStatus(d.getStatus());
		t.setSendTo(d.getSendTo());
		t.setUrgency(d.getUrgency());
		t.setSignatureImg(d.getSignatureImg());
		t.setLetterheadName(d.getLetterheadName());
		t.setLetterheadAddress(d.getLetterheadAddress());
		t.setLetterheadPhone(d.getLetterheadPhone());
		t.setLetterheadFax(d.getLetterheadFax());
	
		return t;
	}
}
