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
package org.oscarehr.e2e.model.export.template;

import java.util.Arrays;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.header.AuthorModel;

public class ProviderParticipationModel extends AuthorModel {
	public ProviderParticipationModel(String providerNo) {
		super(providerNo);
	}

	public Participant2 getProvider() {
		Participant2 participant = new Participant2(ParticipationType.PrimaryPerformer, ContextControl.OverridingPropagating);
		ParticipantRole participantRole = new ParticipantRole(new CD<String>(Constants.RoleClass.PROV.toString()));
		PlayingEntity playingEntity = new PlayingEntity(EntityClassRoot.Person);

		playingEntity.setName(person.getName());
		playingEntity.setDesc(new ED("Provider"));

		participantRole.setPlayingEntityChoice(playingEntity);
		participantRole.setId(ids);

		participant.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.PROVIDER_PARTICIPATION_TEMPLATE_ID)));
		participant.setParticipantRole(participantRole);

		return participant;
	}
}
