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
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101201CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101202CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101204CA;
import org.marc.everest.rmim.ca.r020403.interaction.PRPA_IN101205CA;
import org.marc.everest.rmim.ca.r020403.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.ca.r020403.vocabulary.ProcessingID;
import org.marc.everest.rmim.ca.r020403.vocabulary.ResponseMode;

public class PlaceholderPersonComprehensiveFulfiller extends BaseFulfiller {

	private static Logger logger = Logger.getLogger(PlaceholderPersonComprehensiveFulfiller.class);

	public PRPA_IN101202CA addPerson(PRPA_IN101201CA query) {
		if (query == null) {
			logger.debug("Null initial query");
		}

		PRPA_IN101202CA response = new PRPA_IN101202CA(new II(UUID.randomUUID()), TS.now(), ResponseMode.Immediate, PRPA_IN101202CA.defaultInteractionId(), PRPA_IN101202CA.defaultProfileId(), ProcessingID.Training, AcknowledgementCondition.Always);
		org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity> controlActEvent = new org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.ControlActEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity>();
		response.setControlActEvent(controlActEvent);

		// PRPA_IN101202CA / controlActEvent / subject / registrationEvent / identifiedEntity / id @ extension & root
		org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity> subject = new org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.Subject2<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity>();
		controlActEvent.setSubject(subject);
		org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.RegistrationEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity> registrationEvent = new org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.RegistrationEvent<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity>();
		subject.setRegistrationEvent(registrationEvent);

		org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity> anotherSubject = new org.marc.everest.rmim.ca.r020403.mfmi_mt700726ca.Subject4<org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity>();
		registrationEvent.setSubject(anotherSubject);

		org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity identifiedEntity = new org.marc.everest.rmim.ca.r020403.prpa_mt101106ca.IdentifiedEntity();
		anotherSubject.setRegisteredRole(identifiedEntity);

		identifiedEntity.setId(new SET<II>());
		identifiedEntity.getId().add(new II("2.16.840.1.113883.4.57", "444114567"));

		// see if identified person and asOtherIds are necessary
		return response;
	}

	public PRPA_IN101205CA revisePerson(PRPA_IN101204CA request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Revising " + request);
		}

		PRPA_IN101205CA response = Utils.newInstance(PRPA_IN101205CA.class);
		return response;
	}

}
