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
package org.oscarehr.e2e.extension;

import org.marc.everest.annotations.ConformanceType;
import org.marc.everest.annotations.Property;
import org.marc.everest.annotations.PropertyType;
import org.marc.everest.annotations.Structure;
import org.marc.everest.annotations.StructureType;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;

@Structure(model = "POCD_MT000040", name = "Observation", structureType = StructureType.MESSAGETYPE)
public class ObservationWithConfidentialityCode extends Observation {
	private CE<x_BasicConfidentialityKind> confidentialityCode;

	public ObservationWithConfidentialityCode(x_ActMoodDocumentObservation eventoccurrence) {
		this.setMoodCode(new CS<x_ActMoodDocumentObservation>(eventoccurrence));
	}

	@Property(conformance = ConformanceType.REQUIRED, name = "confidentialityCode", namespaceUri = "http://standards.pito.bc.ca/E2E-DTC/cda", propertyType = PropertyType.NONSTRUCTURAL)
	public CE<x_BasicConfidentialityKind> getConfidentialityCode() {
		return confidentialityCode;
	}

	public void setConfidentialityCode(CE<x_BasicConfidentialityKind> confidentialityCode) {
		this.confidentialityCode = confidentialityCode;
	}
}
