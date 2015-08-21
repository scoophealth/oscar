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
package org.oscarehr.e2e.model.export.body;

import java.util.ArrayList;
import java.util.List;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.Lab;
import org.oscarehr.e2e.model.PatientExport.LabComponent;
import org.oscarehr.e2e.model.PatientExport.LabOrganizer;
import org.oscarehr.e2e.model.export.template.AuthorParticipationModel;
import org.oscarehr.e2e.model.export.template.ResultOrganizerModel;
import org.oscarehr.e2e.util.EverestUtils;

public class LabsModel {
	private Lab lab;
	private Hl7TextInfo hl7TextInfo;

	private SET<II> ids;
	private CD<String> code;
	private ED text;
	private ArrayList<Author> authors;
	private ArrayList<EntryRelationship> resultOrganizers;

	public LabsModel(Lab lab) {
		if(lab == null) {
			this.lab = new Lab(new Hl7TextInfo());
		} else {
			this.lab = lab;
		}
		this.hl7TextInfo = this.lab.getHl7TextInfo();

		setIds();
		setCode();
		setText();
		setAuthor();
		setResultOrganizers();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(!EverestUtils.isNullorEmptyorWhitespace(hl7TextInfo.getDiscipline())) {
			sb.append("Discipline: ".concat(hl7TextInfo.getDiscipline()));
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(hl7TextInfo.getObrDate())) {
			sb.append("\nOBR: ".concat(hl7TextInfo.getObrDate()));
		}
		for(LabOrganizer labOrganizer : lab.getLabOrganizer()) {
			for(LabComponent labComponent : labOrganizer.getLabComponent()) {
				if(!EverestUtils.isNullorEmptyorWhitespace(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.name.toString()))) {
					sb.append("\n".concat(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.name.toString())));
				}
				if(!EverestUtils.isNullorEmptyorWhitespace(labComponent.getMeasurement().getDataField())) {
					sb.append(": ".concat(labComponent.getMeasurement().getDataField()));
				}
				if(!EverestUtils.isNullorEmptyorWhitespace(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.unit.toString()))) {
					sb.append(" ".concat(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.unit.toString())));
				}
			}
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.Lab, hl7TextInfo.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		this.code = new CD<String>();
		this.code.setNullFlavor(NullFlavor.NoInformation);
	}

	public ED getText() {
		return text;
	}

	private void setText() {
		if(!EverestUtils.isNullorEmptyorWhitespace(hl7TextInfo.getDiscipline())) {
			this.text = new ED(lab.getHl7TextInfo().getDiscipline());
		} else {
			this.text = null;
		}
	}

	public ArrayList<Author> getAuthor() {
		return authors;
	}

	private void setAuthor() {
		this.authors = new ArrayList<Author>();
		this.authors.add(new AuthorParticipationModel().getAuthor(EverestUtils.stringToDate(lab.getRequestDate()), hl7TextInfo.getRequestingProvider()));
	}

	public ArrayList<EntryRelationship> getResultOrganizers() {
		return resultOrganizers;
	}

	private void setResultOrganizers() {
		this.resultOrganizers = new ArrayList<EntryRelationship>();

		List<LabOrganizer> labOrganizers = lab.getLabOrganizer();
		if(labOrganizers.isEmpty()) {
			this.resultOrganizers.add(new ResultOrganizerModel().getEntryRelationship(null));
		} else {
			for(LabOrganizer labOrganizer : labOrganizers) {
				this.resultOrganizers.add(new ResultOrganizerModel().getEntryRelationship(labOrganizer));
			}
		}
	}
}
