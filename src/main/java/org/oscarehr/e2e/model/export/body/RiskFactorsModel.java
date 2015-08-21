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
import java.util.Arrays;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.template.AuthorParticipationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class RiskFactorsModel {
	private CaseManagementNote riskFactor;

	private SET<II> ids;
	private CD<String> code;
	private ActStatus statusCode;
	private ArrayList<Author> authors;
	private ArrayList<Component4> components;

	public RiskFactorsModel(CaseManagementNote riskFactor) {
		if(riskFactor == null) {
			this.riskFactor = new CaseManagementNote();
		} else {
			this.riskFactor = riskFactor;
		}

		setIds();
		setCode();
		setStatusCode();
		setAuthor();
		setComponentObservation();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(riskFactor.getObservation_date() != null) {
			sb.append(riskFactor.getObservation_date());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(riskFactor.getNote())) {
			sb.append(" ".concat(riskFactor.getNote().replaceAll("\\\\n", "\n")));
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.RiskFactors, riskFactor.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		CD<String> code = new CD<String>("40514009", Constants.CodeSystems.SNOMED_CT_OID);
		code.setCodeSystemName(Constants.CodeSystems.SNOMED_CT_NAME);
		this.code = code;
	}

	public ActStatus getStatusCode() {
		return statusCode;
	}

	private void setStatusCode() {
		if(!riskFactor.isArchived()) {
			this.statusCode = ActStatus.Active;
		} else {
			this.statusCode = ActStatus.Completed;
		}
	}

	public ArrayList<Author> getAuthor() {
		return authors;
	}

	private void setAuthor() {
		this.authors = new ArrayList<Author>();
		this.authors.add(new AuthorParticipationModel(riskFactor.getProviderNo()).getAuthor(riskFactor.getUpdate_date()));
	}

	public ArrayList<Component4> getComponentObservation() {
		return components;
	}

	private void setComponentObservation() {
		Component4 component = new Component4(ActRelationshipHasComponent.HasComponent, new BL(true));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		observation.setId(getIds());
		observation.setCode(getObservationCode());
		observation.setText(getObservationName());
		observation.setEffectiveTime(getObservationDate());
		observation.setValue(getObservationValue());

		component.setClinicalStatement(observation);
		this.components = new ArrayList<Component4>(Arrays.asList(component));
	}

	protected CD<String> getObservationCode() {
		CD<String> code = new CD<String>();
		code.setNullFlavor(NullFlavor.NoInformation);
		return code;
	}

	protected ED getObservationName() {
		ED text = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(riskFactor.getNote())) {
			text = new ED(riskFactor.getNote());
		}

		return text;
	}

	protected IVL<TS> getObservationDate() {
		IVL<TS> ivl = null;
		TS startTime = EverestUtils.buildTSFromDate(riskFactor.getObservation_date());
		if(startTime != null) {
			ivl = new IVL<TS>(startTime, null);
		}

		return ivl;
	}

	protected ST getObservationValue() {
		ST value = new ST();
		if(!EverestUtils.isNullorEmptyorWhitespace(riskFactor.getNote())) {
			value.setValue(riskFactor.getNote());
		} else {
			value.setNullFlavor(NullFlavor.NoInformation);
		}

		return value;
	}
}
