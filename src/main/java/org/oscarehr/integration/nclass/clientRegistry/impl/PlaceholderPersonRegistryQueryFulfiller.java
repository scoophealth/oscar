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
package org.oscarehr.integration.nclass.clientRegistry.impl;

import java.util.List;
import java.util.UUID;

import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CV;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101004CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101103CA;
import org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.IdOrganization;
import org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.AdministrativeGender;
import org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.PersonBirthtime;
import org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.PersonName;
import org.marc.everest.rmim.ca.r020403.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.ca.r020403.vocabulary.ProcessingID;
import org.marc.everest.rmim.ca.r020403.vocabulary.ResponseMode;

public class PlaceholderPersonRegistryQueryFulfiller extends BaseFulfiller {

	public PRPA_IN101004CA findCandidates(PRPA_IN101103CA query) {
		org.marc.everest.rmim.ca.r020403.mfmi_mt700751ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.ParameterList> event = query.getControlActEvent();

		// Get request information
		AdministrativeGender gender = event.getQueryByParameter().getParameterList().getAdministrativeGender();
		PersonBirthtime birthTime = event.getQueryByParameter().getParameterList().getPersonBirthtime();

		List<PersonName> personNameList = query.getControlActEvent().getQueryByParameter().getParameterList().getPersonName();

		org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity entity = new org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity();
		for (PersonName personName : personNameList) {
			String first = personName.getValue().getPart(0).getValue();
			String last = personName.getValue().getPart(1).getValue();

			entity.setIdentifiedPerson(toIdentifiedPerson(first, last, gender, birthTime));
			break;
		}

		// Create dummy response
		PRPA_IN101004CA response = new PRPA_IN101004CA(new II(UUID.randomUUID()), TS.now(), 
				ResponseMode.Immediate, PRPA_IN101004CA.defaultInteractionId(), PRPA_IN101004CA.defaultProfileId(), 
				ProcessingID.Training, AcknowledgementCondition.Always);

		org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> controlActEvent = new org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity>();
		response.setControlActEvent(controlActEvent);

		org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> identifiedSubject = new org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity>();
		controlActEvent.setSubject(identifiedSubject);

		org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.RegistrationEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> registrationEvent = new org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.RegistrationEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity>();
		identifiedSubject.setRegistrationEvent(registrationEvent);

		org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> subject = new org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity>();
		registrationEvent.setSubject(subject);
		subject.setRegisteredRole(entity);

		org.marc.everest.rmim.ca.r020403.repc_mt000007ca.Custodian custodian = new org.marc.everest.rmim.ca.r020403.repc_mt000007ca.Custodian();
		org.marc.everest.rmim.ca.r020403.coct_mt090310ca.AssignedDevice assignedDevice = new org.marc.everest.rmim.ca.r020403.coct_mt090310ca.AssignedDevice();
		assignedDevice.setId(new II("2.16.840.1.113883.19.3.77.18", "BUS"));
		custodian.setAssignedDevice(assignedDevice);
		
		registrationEvent.setCustodian(custodian);		
		return response;
	}

	private org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person toIdentifiedPerson(String first, String last, AdministrativeGender gender, PersonBirthtime birthTime) {
		org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person person = new org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person();

		if (gender != null && gender.getValue() != null && gender.getValue().getCode() != null) {
			person.setAdministrativeGenderCode(new CV<org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender>(gender.getValue().getCode()));
		}
		if (birthTime != null && birthTime.getValue() != null) {
			person.setBirthTime(birthTime.getValue().getDateValue());
		}
		
		LIST<PN> pns = new LIST<PN>();
		PN pn = PN.fromFamilyGiven(EntityNameUse.Legal, last, first);
		pns.add(pn);
		person.setName(pns);

		LIST<AD> addr = new LIST<AD>();
		AD ad = new AD();
		ad.getPart().add(new ADXP("1532 Home Street"));
		ad.getPart().add(new ADXP("Ann Arbor", AddressPartType.City));
		ad.getPart().add(new ADXP("MI", AddressPartType.State));
		ad.getPart().add(new ADXP("99999", AddressPartType.PostalCode));
		addr.add(ad);
		person.setAddr(addr);

		org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs otherId = new org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs();
		// generate dummy UUID
		otherId.setId(new II("2.16.840.1.113883.4.50", UUID.randomUUID().toString()));
		otherId.setCode(new CV<String>("DL", "2.16.840.1.113883.2.20.5.2"));
		IdOrganization idOrganization = new IdOrganization();
		idOrganization.setName("Department of National Defence");
		otherId.setAssigningIdOrganization(idOrganization);
		person.getAsOtherIDs().add(otherId);

		otherId = new org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs();
		// generate dummy UUID
		otherId.setId(new II("2.16.840.1.113883.4.50", UUID.randomUUID().toString()));
		otherId.setCode(new CV<String>("DL", "2.16.840.1.113883.2.20.5.2"));
		idOrganization = new IdOrganization();
		idOrganization.setName("British Columbia Ministry of Transportation");
		otherId.setAssigningIdOrganization(idOrganization);
		person.getAsOtherIDs().add(otherId);

		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.PersonalRelationship personalRelationship = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.PersonalRelationship();
		personalRelationship.setCode("FTH", "2.16.840.1.113883.5.111");
		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.ParentPerson parentPerson = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.ParentPerson();
		parentPerson.setId(new II("2.16.840.1.113883.4.57", "444111234"));
		parentPerson.setName(PN.fromFamilyGiven(EntityNameUse.Legal, "Neville", "Johnson"));
		personalRelationship.setRelationshipHolder(parentPerson);
		person.getPersonalRelationship().add(personalRelationship);

		personalRelationship = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.PersonalRelationship();
		personalRelationship.setCode("MTH", "2.16.840.1.113883.5.111");
		parentPerson = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.ParentPerson();
		parentPerson.setId(new II("2.16.840.1.113883.4.57", "444112345"));
		parentPerson.setName(PN.fromFamilyGiven(EntityNameUse.Legal, "Nelda", "Johnson"));
		personalRelationship.setRelationshipHolder(parentPerson);
		person.getPersonalRelationship().add(personalRelationship);

		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.LanguageCommunication lang = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.LanguageCommunication(new CV<String>("en", "2.16.840.1.113883.6.121"));
		person.getLanguageCommunication().add(lang);

		return person;
	}

}
