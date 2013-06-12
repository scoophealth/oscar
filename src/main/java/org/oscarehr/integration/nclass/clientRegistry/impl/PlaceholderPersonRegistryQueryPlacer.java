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
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.ca.r020403.coct_mt090102ca.AssignedEntity;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101004CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101103CA;
import org.marc.everest.rmim.ca.r020403.mcci_mt002200ca.Device1;
import org.marc.everest.rmim.ca.r020403.mcci_mt002200ca.Device2;
import org.marc.everest.rmim.ca.r020403.mcci_mt002200ca.Receiver;
import org.marc.everest.rmim.ca.r020403.mcci_mt002200ca.Sender;
import org.marc.everest.rmim.ca.r020403.mfmi_mt700751ca.Author;
import org.marc.everest.rmim.ca.r020403.mfmi_mt700751ca.ControlActEvent;
import org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.ParameterList;
import org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.PersonName;
import org.marc.everest.rmim.ca.r020403.quqi_mt120008ca.QueryByParameter;
import org.marc.everest.rmim.ca.r020403.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.ca.r020403.vocabulary.ProcessingID;
import org.marc.everest.rmim.ca.r020403.vocabulary.QueryRequestLimit;
import org.marc.everest.rmim.ca.r020403.vocabulary.ResponseMode;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.nclass.clientRegistry.PersonRegistryQueryPlacer;
import org.oscarehr.integration.nclass.clientRegistry.model.Candidate;

public class PlaceholderPersonRegistryQueryPlacer extends BasePlacer implements PersonRegistryQueryPlacer {

	private static Logger logger = Logger.getLogger(PlaceholderPersonRegistryQueryPlacer.class);

	private PlaceholderPersonRegistryQueryFulfiller placeholderPersonRegistryQueryFulfiller = new PlaceholderPersonRegistryQueryFulfiller();

	@Override
	public Candidate findCandidate(Demographic demographic) {
		PRPA_IN101103CA findCandidates = toQuery(demographic);
		PRPA_IN101004CA foundCandidates = placeholderPersonRegistryQueryFulfiller.findCandidates(findCandidates);
		Candidate result = fromResponse(foundCandidates);
		return result;
	}

	private Candidate fromResponse(PRPA_IN101004CA response) {
		Candidate result = new Candidate();

		try {
			org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> controlActEvent = response.getControlActEvent();

			org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity> subject = (org.marc.everest.rmim.ca.r020403.mfmi_mt700717ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101002ca.IdentifiedEntity>) controlActEvent.getSubject().getRegistrationEvent().getSubject();

			org.marc.everest.rmim.ca.r020403.prpa_mt101001ca.Person person = subject.getRegisteredRole().getIdentifiedPerson();
			if (person.getName() != null && !person.getName().isEmpty()) {
				LIST<PN> nameList = person.getName();
				PN name = nameList.get(0);

				if (name != null && !name.getParts().isEmpty()) {
					result.setFirst(name.getPart(0).getValue());
					result.setLast(name.getPart(1).getValue());
				}
			}

			for (org.marc.everest.rmim.ca.r020403.prpa_mt101104ca.OtherIDs oid : person.getAsOtherIDs()) {
				result.addId(oid.getId().getExtension(), oid.getAssigningIdOrganization().getName().getValue());
			}
		} catch (Exception e) {
			logger.warn("Unable to extract expected information from response", e);
		}

		return result;
	}

	/**
	 * Creates new query instance
	 * 
	 * @param demographic
	 *            Demographic to create query for
	 * @return Returns the new query
	 */
	private PRPA_IN101103CA toQuery(Demographic demographic) {
		PRPA_IN101103CA findCandidates = new PRPA_IN101103CA(new II(UUID.randomUUID()), // II.TOKEN in pCS
		        TS.now(), ResponseMode.Immediate, PRPA_IN101103CA.defaultInteractionId(), PRPA_IN101103CA.defaultProfileId(), ProcessingID.Training, AcknowledgementCondition.Always);
		
		// Setup sender
		findCandidates.setSender(new Sender(
				new TEL(getSender().getUrl()),
				new Device1(new II("1.3.6.1.4.1.33349.3.1.1.22", getSender().getName()), 
					new ST(getSender().getName()),
					new ST(getSender().getName()), 
					null, 
					null, 
					null
				)));
		
		findCandidates.setReceiver(new Receiver(new TEL(getReceiver().getUrl()), 
			new Device2(
					new II("1.3.6.1.4.1.33349.3.1.1.2", "CR")
			)
		));
		
		// Setup request
		findCandidates.setControlActEvent(new ControlActEvent<ParameterList>());
		findCandidates.getControlActEvent().setCode(PRPA_IN101103CA.defaultTriggerEvent());
		findCandidates.getControlActEvent().setEffectiveTime(TS.now());

		// Add author data
		if (getProvider() != null) {
			findCandidates.getControlActEvent().setAuthor(new Author(TS.now()));
			findCandidates.getControlActEvent().getAuthor().setAuthorPerson(new AssignedEntity(SET.createSET(new II("1.2.3.4", "FS-39485")), getProviderAsPerson()));
		}

		// Query control data
		findCandidates.getControlActEvent().setQueryByParameter(new QueryByParameter<ParameterList>());
		findCandidates.getControlActEvent().getQueryByParameter().setQueryId(UUID.randomUUID());
		findCandidates.getControlActEvent().getQueryByParameter().setInitialQuantity(10);
		findCandidates.getControlActEvent().getQueryByParameter().setInitialQuantityCode(QueryRequestLimit.Record);

		// Parameter list
		findCandidates.getControlActEvent().getQueryByParameter().setParameterList(new ParameterList());
		findCandidates.getControlActEvent().getQueryByParameter().getParameterList().setAdministrativeGender(new org.marc.everest.rmim.ca.r020403.prpa_mt101103ca.AdministrativeGender(Utils.toAdminGender(demographic.getSex())));
		findCandidates.getControlActEvent().getQueryByParameter().getParameterList().getPersonName().add(new PersonName(PN.fromEN(EN.createEN(EntityNameUse.Search, new ENXP(demographic.getFirstName(), EntityNamePartType.Given), new ENXP(demographic.getLastName(), EntityNamePartType.Family)))));
		return findCandidates;
	}
}
