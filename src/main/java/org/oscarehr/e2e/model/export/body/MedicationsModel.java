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

import java.util.Calendar;
import java.util.Date;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.oscarehr.common.model.Drug;
import org.oscarehr.e2e.constant.BodyConstants;
import org.oscarehr.e2e.constant.BodyConstants.Medications;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.export.template.ConsumableModel;
import org.oscarehr.e2e.model.export.template.MedicationPrescriptionEventModel;
import org.oscarehr.e2e.model.export.template.observation.DateObservationModel;
import org.oscarehr.e2e.model.export.template.observation.UnboundObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class MedicationsModel {
	private Drug drug;

	private SET<II> ids;
	private CD<String> code;
	private ActStatus statusCode;
	private Consumable consumable;
	private EntryRelationship recordType;
	private EntryRelationship lastReviewDate;
	private EntryRelationship prescriptionInformation;

	public MedicationsModel(Drug drug) {
		if(drug == null) {
			this.drug = new Drug();
		} else {
			this.drug = drug;
		}

		setIds();
		setCode();
		setStatusCode();
		setConsumable();
		setRecordType();
		setLastReviewDate();
		setPrescriptionInformation();
	}

	private Boolean isActiveDrug(Date date) {
		try {
			// Add a day to date to mark drugs ending "today" as still active
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);

			if(new Date().before(c.getTime())) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getGenericName())) {
			sb.append(drug.getGenericName());
		} else {
			sb.append(drug.getBrandName());
		}

		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getDosage())) {
			sb.append(" " + drug.getDosage());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getFreqCode())) {
			sb.append(" " + drug.getFreqCode());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getDuration())) {
			sb.append(" " + drug.getDuration());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getDurUnit())) {
			sb.append(" " + drug.getDurUnit());
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.Medications, drug.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		this.code = new CD<String>(Constants.SubstanceAdministrationType.DRUG.toString(), Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID);
		this.code.setCodeSystemName(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME);
		this.code.setDisplayName(BodyConstants.Medications.DRUG_THERAPY_ACT_NAME);
	}

	public ActStatus getStatusCode() {
		return statusCode;
	}

	private void setStatusCode() {
		if(!drug.isArchived() && drug.getLongTerm() || isActiveDrug(drug.getEndDate())) {
			this.statusCode = ActStatus.Active;
		} else {
			this.statusCode = ActStatus.Completed;
		}
	}

	public Consumable getConsumable() {
		return consumable;
	}

	private void setConsumable() {
		this.consumable = new ConsumableModel().getConsumable(drug);
	}

	public EntryRelationship getRecordType() {
		return recordType;
	}

	private void setRecordType() {
		String value;
		if(drug.getLongTerm()) {
			value = Medications.LONG_TERM;
		} else {
			value = Medications.SHORT_TERM;
		}
		this.recordType = new UnboundObservationModel().getEntryRelationship(value);
	}

	public EntryRelationship getLastReviewDate() {
		return lastReviewDate;
	}

	private void setLastReviewDate() {
		this.lastReviewDate = new DateObservationModel().getEntryRelationship(drug.getLastUpdateDate());
	}

	public EntryRelationship getPrescriptionInformation() {
		return prescriptionInformation;
	}

	private void setPrescriptionInformation() {
		this.prescriptionInformation = new MedicationPrescriptionEventModel().getEntryRelationship(drug);
	}
}
