/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.model.export.template.observation;

import java.util.ArrayList;
import java.util.Arrays;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.util.EverestUtils;

public class InstructionObservationModel extends AbstractObservationModel {
	public EntryRelationship getEntryRelationship(String value) {
		if(!EverestUtils.isNullorEmptyorWhitespace(value)) {
			entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.SUBJ);
			entryRelationship.setContextConductionInd(true);
			entryRelationship.setTemplateId(Arrays.asList(new II(Constants.ObservationOids.INSTRUCTION_OBSERVATION_TEMPLATE_ID)));

			observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
			observation.getCode().setCodeEx(Constants.ObservationType.INSTRUCT.toString());
			observation.setParticipant(getParticipant());
			observation.setText(new ED(value));
		} else {
			entryRelationship = null;
		}

		return entryRelationship;
	}

	private ArrayList<Participant2> getParticipant() {
		Participant2 participant = new Participant2();
		ParticipantRole role = new ParticipantRole(new CS<String>(Constants.RoleClass.ROL.toString()));

		participant.setTypeCode(new CS<ParticipationType>(ParticipationType.PrimaryInformationRecipient));
		participant.setContextControlCode(new CS<ContextControl>(ContextControl.OverridingPropagating));

		role.setCode(Constants.RoleClass.PAT.toString(), Constants.CodeSystems.ROLE_CLASS_OID);
		role.getCode().setCodeSystemName(Constants.CodeSystems.ROLE_CLASS_NAME);
		role.setPlayingEntityChoice(new PlayingEntity(EntityClassRoot.Person));

		participant.setParticipantRole(role);

		return new ArrayList<Participant2>(Arrays.asList(participant));
	}
}
