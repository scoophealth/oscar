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

import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.oscarehr.e2e.constant.Constants;

abstract class AbstractObservationModel {
	protected EntryRelationship entryRelationship;
	protected Observation observation;

	protected AbstractObservationModel() {
		this.entryRelationship = new EntryRelationship();
		this.observation = new Observation();

		CD<String> code = new CD<String>();
		code.setCodeSystem(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID);
		code.setCodeSystemName(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME);
		observation.setCode(code);

		entryRelationship.setClinicalStatement(observation);
	}
}
