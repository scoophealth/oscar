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

import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ProfessionalSpecialistTo1;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalSpecialistConverter extends AbstractConverter<ProfessionalSpecialist, ProfessionalSpecialistTo1> {

	@Override
	public ProfessionalSpecialist getAsDomainObject(LoggedInInfo loggedInInfo, ProfessionalSpecialistTo1 t) throws ConversionException {
		ProfessionalSpecialist d = new ProfessionalSpecialist();

		d.setFirstName(t.getFirstName());
		d.setLastName(t.getLastName());
		d.setProfessionalLetters(t.getProfessionalLetters());
		d.setStreetAddress(t.getStreetAddress());
		d.setPhoneNumber(t.getPhoneNumber());
		d.setFaxNumber(t.getFaxNumber());
		d.setWebSite(t.getWebSite());
		d.setEmailAddress(t.getEmailAddress());
		d.setSpecialtyType(t.getSpecialtyType());
		d.seteDataUrl(t.geteDataUrl());
		d.seteDataOscarKey(t.geteDataOscarKey());
		d.seteDataServiceKey(t.geteDataServiceKey());
		d.seteDataServiceName(t.geteDataServiceName());
		d.setAnnotation(t.getAnnotation());
		d.setReferralNo(t.getReferralNo());
		d.setInstitutionId(t.getInstitutionId());
		d.setDepartmentId(t.getDepartmentId());
		d.setEformId(t.getEformId());
		
		return d;
	}

	@Override
	public ProfessionalSpecialistTo1 getAsTransferObject(LoggedInInfo loggedInInfo,ProfessionalSpecialist d) throws ConversionException {
		ProfessionalSpecialistTo1 t = new ProfessionalSpecialistTo1();

		t.setId(d.getId());
		t.setFirstName(d.getFirstName());
		t.setLastName(d.getLastName());
		t.setName(d.getLastName() + ", " + d.getFirstName());
		t.setProfessionalLetters(d.getProfessionalLetters());
		t.setStreetAddress(d.getStreetAddress());
		t.setPhoneNumber(d.getPhoneNumber());
		t.setFaxNumber(d.getFaxNumber());
		t.setWebSite(d.getWebSite());
		t.setEmailAddress(d.getEmailAddress());
		t.setSpecialtyType(d.getSpecialtyType());
		t.seteDataUrl(d.geteDataUrl());
		t.seteDataOscarKey(d.geteDataOscarKey());
		t.seteDataServiceKey(d.geteDataServiceKey());
		t.seteDataServiceName(d.geteDataServiceName());
		t.setAnnotation(d.getAnnotation());
		t.setReferralNo(d.getReferralNo());
		t.setInstitutionId(d.getInstitutionId());
		t.setDepartmentId(d.getDepartmentId());
		t.setEformId(d.getEformId());
		
		return t;
	}

}
