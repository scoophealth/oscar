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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;

public class ProviderParticipationModelTest extends AbstractExportModelTest {
	@Test
	public void providerParticipationStructureTest() {
		Participant2 provider = new ProviderParticipationModel(Constants.Runtime.VALID_PROVIDER.toString()).getProvider();
		assertNotNull(provider);
		assertEquals(ParticipationType.PrimaryPerformer, provider.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, provider.getContextControlCode().getCode());
		assertTrue(provider.getTemplateId().contains(new II(Constants.TemplateOids.PROVIDER_PARTICIPATION_TEMPLATE_ID)));

		ParticipantRole participantRole = provider.getParticipantRole();
		assertNotNull(participantRole);
		assertNotNull(participantRole.getId());

		PlayingEntity playingEntity = participantRole.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertNotNull(playingEntity.getDesc());

		SET<PN> names = playingEntity.getName();
		assertNotNull(names);
		assertFalse(names.isNull());
		assertFalse(names.isEmpty());
		assertNotNull(names.get(0));
	}

	@Test
	public void providerParticipationNullTest() {
		Participant2 provider = new ProviderParticipationModel(null).getProvider();
		assertNotNull(provider);
		assertEquals(ParticipationType.PrimaryPerformer, provider.getTypeCode().getCode());
		assertEquals(ContextControl.OverridingPropagating, provider.getContextControlCode().getCode());
		assertTrue(provider.getTemplateId().contains(new II(Constants.TemplateOids.PROVIDER_PARTICIPATION_TEMPLATE_ID)));

		ParticipantRole participantRole = provider.getParticipantRole();
		assertNotNull(participantRole);
		assertNotNull(participantRole.getId());

		PlayingEntity playingEntity = participantRole.getPlayingEntityChoiceIfPlayingEntity();
		assertNotNull(playingEntity);
		assertNotNull(playingEntity.getDesc());

		SET<PN> names = playingEntity.getName();
		assertNotNull(names);
		assertFalse(names.isNull());
		assertFalse(names.isEmpty());
		assertNotNull(names.get(0));
		assertTrue(names.get(0).isNull());
		assertEquals(NullFlavor.NoInformation, names.get(0).getNullFlavor().getCode());
	}
}
