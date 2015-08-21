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
package org.oscarehr.e2e.populator.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer2;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.BodyConstants.OrdersAndRequests;
import org.oscarehr.e2e.model.export.template.AuthorParticipationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class OrdersAndRequestsPopulator extends AbstractBodyPopulator<OrdersAndRequestsPopulator> {
	OrdersAndRequestsPopulator() {
		bodyConstants = OrdersAndRequests.getConstants();
		populateClinicalStatement(Arrays.asList(this));
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<OrdersAndRequestsPopulator> list) {
		return null;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		Observation observation = new Observation(x_ActMoodDocumentObservation.RQO);

		observation.setId(EverestUtils.buildUniqueId(Constants.IdPrefixes.Referrals, 0));
		observation.setCode(new CD<String>() {{setNullFlavor(NullFlavor.NoInformation);}});
		observation.setText(new ED() {{setNullFlavor(NullFlavor.NoInformation);}});
		observation.setEffectiveTime(new IVL<TS>() {{setNullFlavor(NullFlavor.NoInformation);}});

		II ii = new II();
		ii.setNullFlavor(NullFlavor.NoInformation);
		AssignedEntity assignedEntity = new AssignedEntity();
		assignedEntity.setId(new SET<II>(ii));
		Performer2 performer = new Performer2(assignedEntity);
		observation.setPerformer(new ArrayList<Performer2>(Arrays.asList(performer)));
		observation.setAuthor(new ArrayList<Author>(Arrays.asList(new AuthorParticipationModel().getAuthor(null))));

		return observation;
	}

	@Override
	public List<String> populateText() {
		return null;
	}
}
