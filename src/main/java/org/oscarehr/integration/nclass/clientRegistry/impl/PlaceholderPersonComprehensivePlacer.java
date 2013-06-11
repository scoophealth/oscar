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

import java.util.UUID;

import org.apache.log4j.Logger;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CV;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101201CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101202CA;
import org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.IdOrganization;
import org.marc.everest.rmim.ca.r020403.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.ca.r020403.vocabulary.ProcessingID;
import org.marc.everest.rmim.ca.r020403.vocabulary.ResponseMode;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.nclass.clientRegistry.PersonComprehensivePlacer;

public class PlaceholderPersonComprehensivePlacer extends BasePlacer implements PersonComprehensivePlacer {

	private static Logger logger = Logger.getLogger(PlaceholderPersonComprehensivePlacer.class);

	private PlaceholderPersonComprehensiveFulfiller personComprehensiveFulfiller = new PlaceholderPersonComprehensiveFulfiller();

	@Override
	public void addPerson(Demographic demographic) {
		PRPA_IN101201CA query = new PRPA_IN101201CA(new II(UUID.randomUUID()), TS.now(), 
				ResponseMode.Immediate, PRPA_IN101201CA.defaultInteractionId(), PRPA_IN101201CA.defaultProfileId(), 
				ProcessingID.Training, AcknowledgementCondition.Always);
		org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity> controlActEvent =
				new org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>();
		query.setControlActEvent(controlActEvent);
		
		org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>
		 	subject = new org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>();
		controlActEvent.setSubject(subject);
		
		org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.RegistrationRequest<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>
			registrationRequest = new org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.RegistrationRequest<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>(); 
		subject.setRegistrationRequest(registrationRequest);
		
		org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity> anotherSubject = new
				org.marc.everest.rmim.ca.r020403.mfmi_mt700711ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity>();
		registrationRequest.setSubject(anotherSubject);
		
		org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity identifiedEntity = new org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity();
		anotherSubject.setRegisteredRole(identifiedEntity);
		
		initIdentifiedEntity(identifiedEntity, demographic);
		
		PRPA_IN101202CA response = personComprehensiveFulfiller.addPerson(query);
		if (response == null) {
			logger.debug("Null response " + response);
		}
	}

	private void initIdentifiedEntity(org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.IdentifiedEntity identifiedEntity, Demographic demo) {		
		org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person person = new org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person();
		identifiedEntity.setIdentifiedPerson(person);
		
		LIST<PN> pns = new LIST<PN>();
		PN pn = PN.fromFamilyGiven(EntityNameUse.Legal, demo.getLastName(), demo.getFirstName());
		pns.add(pn);
		person.setName(pns);

		LIST<TEL> tel = new LIST<TEL>();
		if (demo.getPhone() != null) {
			tel.add(new TEL(demo.getPhone()));
		}
		if (demo.getPhone() != null) {
			tel.add(new TEL(demo.getPhone2()));
		}
		person.setTelecom(tel);
		
		if ("M".equals(demo.getSex())) {
			person.setAdministrativeGenderCode(new CV<org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender>(org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender.Male));
		} else if ("F".equals(demo.getSex())) {
			person.setAdministrativeGenderCode(new CV<org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender>(org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender.Female));
		} else {
			person.setAdministrativeGenderCode(new CV<org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender>(org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender.Undifferentiated));
		}

		person.setBirthTime(demo.getBirthDay());
		
		// FIXME assume we only deal with alive patients
		person.setDeceasedInd(new BL(false));
		
		LIST<AD> addr = new LIST<AD>();
		AD ad = new AD();
		ad.getPart().add(new ADXP(demo.getAddress()));
		ad.getPart().add(new ADXP(demo.getCity(), AddressPartType.City));
		ad.getPart().add(new ADXP(demo.getProvince(), AddressPartType.State));
		ad.getPart().add(new ADXP(demo.getPostal(), AddressPartType.PostalCode));
		addr.add(ad);
		person.setAddr(addr);
		
		org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs otherId = new org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs();
		// FIXME get proper root for this ID - will be based on "per clinic" approach
		otherId.setId(new II("2.16.840.1.113883.4.50", demo.getDemographicNo().toString()));
		otherId.setCode(new CV<String>("DL", "2.16.840.1.113883.2.20.5.2"));
		IdOrganization idOrganization = new IdOrganization();
		// FIXME set proper clinic name
		idOrganization.setName("OSCAR");
		otherId.setAssigningIdOrganization(idOrganization);
		person.getAsOtherIDs().add(otherId);

		// FIXME add proper personal relationship handling
		/*
		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.PersonalRelationship personalRelationship = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.PersonalRelationship();
		personalRelationship.setCode("FTH", "2.16.840.1.113883.5.111");
		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.ParentPerson parentPerson = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.ParentPerson();
		parentPerson.setId(new II("2.16.840.1.113883.4.57", "444111234"));
		parentPerson.setName(PN.fromFamilyGiven(EntityNameUse.Legal, "Neville", "Johnson"));
		personalRelationship.setRelationshipHolder(parentPerson);
		person.getPersonalRelationship().add(personalRelationship);
		*/

		org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.LanguageCommunication lang = new org.marc.everest.rmim.ca.r020403.prpa_mt101102ca.LanguageCommunication(new CV<String>(demo.getSpokenLanguage(), "2.16.840.1.113883.6.121"));
		person.getLanguageCommunication().add(lang);
    }

}
