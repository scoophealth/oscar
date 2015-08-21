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

import java.util.Arrays;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;

public class LifestageObservationModel extends AbstractObservationModel {
	public EntryRelationship getEntryRelationship(String lifeStage) {
		if(Mappings.lifeStageCode.containsKey(lifeStage)) {
			entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.SUBJ);
			entryRelationship.setContextConductionInd(true);
			entryRelationship.setTemplateId(Arrays.asList(new II(Constants.ObservationOids.LIFESTAGE_OBSERVATION_TEMPLATE_ID)));

			CD<String> value = new CD<String>(Mappings.lifeStageCode.get(lifeStage), Constants.CodeSystems.SNOMED_CT_OID);
			value.setCodeSystemName(Constants.CodeSystems.SNOMED_CT_NAME);
			value.setDisplayName(Mappings.lifeStageName.get(lifeStage));

			observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
			observation.getCode().setCodeEx(Constants.ObservationType.LIFEOBS.toString());
			observation.setValue(value);
		} else {
			entryRelationship = null;
		}

		return entryRelationship;
	}
}
