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

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.INT;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RelatedSubject;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Subject;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubject;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.PatientExport.FamilyHistoryEntry;
import org.oscarehr.e2e.model.export.template.observation.LifestageObservationModel;
import org.oscarehr.e2e.model.export.template.observation.SecondaryCodeICD9ObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class FamilyHistoryModel {
	private FamilyHistoryEntry familyHistory;

	private SET<II> ids;
	private CD<String> code;
	private ED text;
	private IVL<TS> effectiveTime;
	private CD<String> value;
	private Subject subject;
	private EntryRelationship treatmentComment;
	private EntryRelationship billingCode;
	private EntryRelationship lifestageOnset;
	private EntryRelationship ageAtOnset;

	public FamilyHistoryModel(FamilyHistoryEntry familyHistoryEntry) {
		if(familyHistoryEntry == null) {
			this.familyHistory = new FamilyHistoryEntry(null, null);
		} else {
			this.familyHistory = familyHistoryEntry;
		}

		setIds();
		setCode();
		setText();
		setEffectiveTime();
		setValue();
		setSubject();
		setTreatmentComment();
		setBillingCode();
		setLifestageOnset();
		setAgeAtOnset();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(familyHistory.getFamilyHistory().getObservation_date() != null) {
			sb.append(familyHistory.getFamilyHistory().getObservation_date());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(familyHistory.getFamilyHistory().getNote())) {
			sb.append(" ".concat(familyHistory.getFamilyHistory().getNote().replaceAll("\\\\n", "\n")));
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.FamilyHistory, familyHistory.getFamilyHistory().getId());
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
		ED text = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(familyHistory.getFamilyHistory().getNote())) {
			text = new ED(familyHistory.getFamilyHistory().getNote());
		}
		this.text = text;
	}

	public IVL<TS> getEffectiveTime() {
		return effectiveTime;
	}

	private void setEffectiveTime() {
		IVL<TS> ivl = new IVL<TS>();
		TS startTime = EverestUtils.buildTSFromDate(familyHistory.getFamilyHistory().getObservation_date());
		if(startTime != null) {
			ivl.setLow(startTime);
		} else {
			ivl.setNullFlavor(NullFlavor.NoInformation);
		}

		this.effectiveTime = ivl;
	}

	public CD<String> getValue() {
		return value;
	}

	private void setValue() {
		this.value = new CD<String>();
		this.value.setNullFlavor(NullFlavor.Unknown);
	}

	public Subject getSubject() {
		return subject;
	}

	private void setSubject() {
		RelatedSubject relatedSubject = new RelatedSubject(x_DocumentSubject.PersonalRelationship);
		Subject subject = new Subject(ContextControl.OverridingPropagating, relatedSubject);

		String relationship = familyHistory.getExtMap().get(CaseManagementNoteExt.RELATIONSHIP);
		CE<String> code = new CE<String>();
		if(!EverestUtils.isNullorEmptyorWhitespace(relationship)) {
			code.setCodeSystem(Constants.CodeSystems.ROLE_CODE_OID);
			code.setCodeSystemName(Constants.CodeSystems.ROLE_CODE_NAME);
			code.setDisplayName(relationship);
			if(Mappings.personalRelationshipRole.containsKey(relationship.toLowerCase())) {
				code.setCodeEx(Mappings.personalRelationshipRole.get(relationship.toLowerCase()));
			} else {
				code.setCodeEx("OTH");
			}
		} else {
			code.setNullFlavor(NullFlavor.NoInformation);
		}
		relatedSubject.setCode(code);

		this.subject = subject;
	}

	public EntryRelationship getTreatmentComment() {
		return treatmentComment;
	}

	private void setTreatmentComment() {
		EntryRelationship entryRelationship = null;

		if(!EverestUtils.isNullorEmptyorWhitespace(familyHistory.getExtMap().get(CaseManagementNoteExt.TREATMENT))) {
			Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
			entryRelationship = new EntryRelationship(x_ActRelationshipEntryRelationship.SUBJ, new BL(true), observation);

			CD<String> code = new CD<String>(Constants.ObservationType.TRTNOTE.toString(), Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID);
			code.setCodeSystemName(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME);

			CD<String> value = new CD<String>();
			value.setNullFlavor(NullFlavor.NoInformation);

			observation.setCode(code);
			observation.setText(new ED(familyHistory.getExtMap().get(CaseManagementNoteExt.TREATMENT)));
			observation.setValue(value);
		}

		this.treatmentComment = entryRelationship;
	}

	public EntryRelationship getBillingCode() {
		return billingCode;
	}

	private void setBillingCode() {
		this.billingCode = new SecondaryCodeICD9ObservationModel().getEntryRelationship(familyHistory.getFamilyHistory().getBilling_code());
	}

	public EntryRelationship getLifestageOnset() {
		return lifestageOnset;
	}

	private void setLifestageOnset() {
		this.lifestageOnset = new LifestageObservationModel().getEntryRelationship(familyHistory.getExtMap().get(CaseManagementNoteExt.LIFESTAGE));
	}

	public EntryRelationship getAgeAtOnset() {
		return ageAtOnset;
	}

	private void setAgeAtOnset() {
		EntryRelationship entryRelationship = null;

		try {
			Integer age = Integer.parseInt(familyHistory.getExtMap().get(CaseManagementNoteExt.AGEATONSET));
			INT value = new INT(age);

			CD<String> code = new CD<String>("30972-4", Constants.CodeSystems.LOINC_OID);
			code.setCodeSystemName(Constants.CodeSystems.LOINC_NAME);
			code.setDisplayName("Age at onset");

			Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
			entryRelationship = new EntryRelationship(x_ActRelationshipEntryRelationship.HasComponent, new BL(true), observation);

			observation.setCode(code);
			observation.setValue(value);
		} catch (Exception e) {
			entryRelationship = null;
		}

		this.ageAtOnset = entryRelationship;
	}
}
