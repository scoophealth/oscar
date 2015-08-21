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

import java.util.Arrays;
import java.util.List;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalStatement;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.BodyConstants.AdvanceDirectives;
import org.oscarehr.e2e.util.EverestUtils;

public class AdvanceDirectivesPopulator extends AbstractBodyPopulator<AdvanceDirectivesPopulator> {
	AdvanceDirectivesPopulator() {
		bodyConstants = AdvanceDirectives.getConstants();
		populateClinicalStatement(Arrays.asList(this));
	}

	@Override
	public ClinicalStatement populateClinicalStatement(List<AdvanceDirectivesPopulator> list) {
		return null;
	}

	@Override
	public ClinicalStatement populateNullFlavorClinicalStatement() {
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		observation.setId(EverestUtils.buildUniqueId(Constants.IdPrefixes.AdvanceDirectives, 0));
		observation.setCode(new CD<String>() {{setNullFlavor(NullFlavor.NoInformation);}});
		observation.setText(new ED() {{setNullFlavor(NullFlavor.NoInformation);}});
		observation.setStatusCode(ActStatus.Completed);
		observation.setEffectiveTime(new IVL<TS>() {{setNullFlavor(NullFlavor.NoInformation);}});

		return observation;
	}

	@Override
	public List<String> populateText() {
		return null;
	}
}
