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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActClassDocumentEntryOrganizer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.LabComponent;
import org.oscarehr.e2e.model.PatientExport.LabOrganizer;
import org.oscarehr.e2e.util.EverestUtils;

public class ResultOrganizerModel {
	private LabOrganizer labOrganizer;

	public EntryRelationship getEntryRelationship(LabOrganizer labOrganizer) {
		if(labOrganizer == null) {
			this.labOrganizer = new LabOrganizer(null, null);
		} else {
			this.labOrganizer = labOrganizer;
		}

		EntryRelationship entryRelationship = new EntryRelationship();
		entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.HasComponent);
		entryRelationship.setContextConductionInd(true);
		entryRelationship.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.RESULT_ORGANIZER_TEMPLATE_ID)));

		Organizer organizer = new Organizer(x_ActClassDocumentEntryOrganizer.BATTERY);
		organizer.setId(getIds());
		organizer.setCode(getCode());
		organizer.setStatusCode(getStatusCode());
		organizer.setComponent(getResultComponents());

		entryRelationship.setClinicalStatement(organizer);
		return entryRelationship;
	}

	private SET<II> getIds() {
		if(labOrganizer.getGroupId() != null) {
			return EverestUtils.buildUniqueId(Constants.IdPrefixes.LabOBR, labOrganizer.getGroupId());
		} else {
			return null;
		}
	}

	private CD<String> getCode() {
		CD<String> code = new CD<String>();
		code.setNullFlavor(NullFlavor.Unknown);
		return code;
	}

	private CS<ActStatus> getStatusCode() {
		CS<ActStatus> statusCode = new CS<ActStatus>();
		if(labOrganizer.getReportStatus() == null) {
			statusCode.setNullFlavor(NullFlavor.Unknown);
		} else if(labOrganizer.getReportStatus().equalsIgnoreCase("P")) {
			statusCode.setCodeEx(ActStatus.Active);
		} else {
			statusCode.setCodeEx(ActStatus.Completed);
		}
		return statusCode;
	}

	private ArrayList<Component4> getResultComponents() {
		ArrayList<Component4> resultComponents = new ArrayList<Component4>();

		List<LabComponent> labComponents = labOrganizer.getLabComponent();
		if(labComponents.isEmpty()) {
			resultComponents.add(new ResultComponentModel().getComponent(null));
		} else {
			for(LabComponent labComponent : labComponents) {
				resultComponents.add(new ResultComponentModel().getComponent(labComponent));
			}
		}

		return resultComponents;
	}
}
