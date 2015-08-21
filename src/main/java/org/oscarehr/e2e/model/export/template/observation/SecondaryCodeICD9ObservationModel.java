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
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.util.EverestUtils;

public class SecondaryCodeICD9ObservationModel extends AbstractObservationModel {
	public EntryRelationship getEntryRelationship(String value) {
		entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.HasComponent);
		entryRelationship.setContextConductionInd(true);
		entryRelationship.setTemplateId(Arrays.asList(new II(Constants.ObservationOids.SECONDARY_CODE_ICD9_OBSERVATION_TEMPLATE_ID)));

		observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		observation.getCode().setCodeEx(Constants.ObservationType.ICD9CODE.toString());

		CD<String> icd9Value = new CD<String>();
		if(!EverestUtils.isNullorEmptyorWhitespace(value)) {
			String description = EverestUtils.getICD9Description(value);
			if(!EverestUtils.isNullorEmptyorWhitespace(description)) {
				icd9Value.setDisplayName(description);
			}
			icd9Value.setCodeEx(value);
			icd9Value.setCodeSystem(Constants.CodeSystems.ICD9_OID);
			icd9Value.setCodeSystemName(Constants.CodeSystems.ICD9_NAME);
		} else {
			icd9Value.setNullFlavor(NullFlavor.Unknown);
		}
		observation.setValue(icd9Value);

		return entryRelationship;
	}
}
