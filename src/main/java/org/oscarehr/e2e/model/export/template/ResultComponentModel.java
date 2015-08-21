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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.PQ;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component4;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ObservationRange;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Performer2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Procedure;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ReferenceRange;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ObservationInterpretation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentProcedureMood;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.BodyConstants.Labs;
import org.oscarehr.e2e.model.PatientExport.LabComponent;
import org.oscarehr.e2e.model.export.template.observation.CommentObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class ResultComponentModel {
	private LabComponent labComponent;

	public Component4 getComponent(LabComponent labComponent) {
		if(labComponent == null) {
			this.labComponent = new LabComponent(null, null);
		} else {
			this.labComponent = labComponent;
		}

		Component4 component = new Component4(ActRelationshipHasComponent.HasComponent, new BL(true));
		component.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.RESULT_COMPONENT_TEMPLATE_ID)));
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		Observation observation = new Observation();
		observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
		observation.setId(getIds());
		observation.setCode(getCode());
		observation.setText(getText());
		observation.setStatusCode(getStatusCode());
		observation.setEffectiveTime(getTime());
		observation.setValue(getValue());
		observation.setInterpretationCode(getInterpretationCode());
		observation.setPerformer(getPerformer());
		observation.setReferenceRange(getReferenceRange());

		entryRelationships.add(getSpecimenCollection());
		entryRelationships.add(getResultNotes());

		observation.setEntryRelationship(entryRelationships);
		component.setClinicalStatement(observation);
		return component;
	}

	private SET<II> getIds() {
		String accession = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.accession.toString());
		II ii = new II();
		if(EverestUtils.isNullorEmptyorWhitespace(accession)) {
			ii.setNullFlavor(NullFlavor.NoInformation);
		} else {
			ii.setRoot(Constants.EMR.EMR_OID);
			ii.setExtension(accession);
		}

		return new SET<II>(ii);
	}

	private CD<String> getCode() {
		String identifier = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.identifier.toString());
		CD<String> code = new CD<String>();
		if(EverestUtils.isNullorEmptyorWhitespace(identifier)) {
			code.setNullFlavor(NullFlavor.NoInformation);
		} else {
			code.setCodeEx(identifier);
			code.setCodeSystem(Constants.CodeSystems.PCLOCD_OID);
			code.setCodeSystemName(Constants.CodeSystems.PCLOCD_NAME);
		}

		return code;
	}

	private ED getText() {
		String name = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.name.toString());
		if(!EverestUtils.isNullorEmptyorWhitespace(name)) {
			return new ED(name);
		}

		return null;
	}

	private CS<ActStatus> getStatusCode() {
		String olis_status = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.olis_status.toString());
		CS<ActStatus> actStatus = new CS<ActStatus>();
		if(EverestUtils.isNullorEmptyorWhitespace(olis_status)) {
			actStatus.setNullFlavor(NullFlavor.NoInformation);
		} else if(olis_status.equalsIgnoreCase("P")) {
			actStatus.setCodeEx(ActStatus.Active);
		} else {
			actStatus.setCodeEx(ActStatus.Completed);
		}

		return actStatus;
	}

	private IVL<TS> getTime() {
		String datetime = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.datetime.toString());
		IVL<TS> ivl = null;
		TS startTime = EverestUtils.buildTSFromDate(EverestUtils.stringToDate(datetime), TS.SECONDNOTIMEZONE);
		if(startTime != null) {
			ivl = new IVL<TS>(startTime, null);
		}

		return ivl;
	}

	private ANY getValue() {
		String dataField = labComponent.getMeasurement().getDataField();
		String unit = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.unit.toString());
		ANY value = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(dataField)) {
			if(!EverestUtils.isNullorEmptyorWhitespace(unit)) {
				try {
					value = new PQ(new BigDecimal(dataField), unit.replaceAll("\\s","_"));
				} catch (NumberFormatException e) {
					value = new ST(dataField.concat(" ").concat(unit));
				}
			} else {
				value = new ST(dataField);
			}
		}

		return value;
	}

	private SET<CE<ObservationInterpretation>> getInterpretationCode() {
		String abnormal = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.abnormal.toString());
		SET<CE<ObservationInterpretation>> interpretationCodes = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(abnormal)) {
			CE<ObservationInterpretation> interpretation = new CE<ObservationInterpretation>();
			if(abnormal.equalsIgnoreCase(Labs.ABNORMAL_CODE)) {
				interpretation.setCodeEx(ObservationInterpretation.Abnormal);
				interpretation.setDisplayName(Labs.ABNORMAL);
			} else {
				interpretation.setCodeEx(ObservationInterpretation.Normal);
				interpretation.setDisplayName(Labs.NORMAL);
			}
			interpretationCodes = new SET<CE<ObservationInterpretation>>(interpretation);
		}

		return interpretationCodes;
	}

	private ArrayList<Performer2> getPerformer() {
		String labname = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.labname.toString());
		ArrayList<Performer2> performers = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(labname)) {
			Performer2 performer = new Performer2();
			AssignedEntity assignedEntity = new AssignedEntity();
			Organization organization = new Organization();

			II ii = new II();
			ii.setNullFlavor(NullFlavor.NoInformation);
			ON on = new ON(EntityNameUse.OfficialRecord, Arrays.asList(new ENXP(labname)));
			organization.setName(new SET<ON>(on));

			assignedEntity.setId(new SET<II>(ii));
			assignedEntity.setRepresentedOrganization(organization);

			performer.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.PERFORMER_PARTICIPATION_TEMPLATE_ID)));
			performer.setTime(getTime());
			performer.setAssignedEntity(assignedEntity);
			performers = new ArrayList<Performer2>(Arrays.asList(performer));
		}

		return performers;
	}

	private EntryRelationship getSpecimenCollection() {
		String datetime = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.datetime.toString());
		EntryRelationship entryRelationship = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(datetime)) {
			Procedure procedure = new Procedure(x_DocumentProcedureMood.Eventoccurrence);
			procedure.setEffectiveTime(getTime());

			entryRelationship = new EntryRelationship();
			entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.HasComponent);
			entryRelationship.setContextConductionInd(true);
			entryRelationship.setClinicalStatement(procedure);
		}
		return entryRelationship;
	}

	private EntryRelationship getResultNotes() {
		String comments = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.comments.toString());
		Date date = labComponent.getMeasurement().getDateObserved();
		EntryRelationship entryRelationship = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(comments)) {
			entryRelationship = new CommentObservationModel().getEntryRelationship(comments, date, null);
		}
		return entryRelationship;
	}

	private ArrayList<ReferenceRange> getReferenceRange() {
		String range = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.range.toString());
		String minimum = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.minimum.toString());
		String maximum = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.maximum.toString());
		String unit = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.unit.toString());

		ObservationRange observationRange = new ObservationRange();
		ReferenceRange referenceRange = new ReferenceRange(observationRange);
		CD<String> code = new CD<String>(Labs.NORMAL_CODE, Constants.CodeSystems.OBSERVATION_INTERPRETATION_OID);
		code.setCodeSystemName(Constants.CodeSystems.OBSERVATION_INTERPRETATION_NAME);
		code.setDisplayName(Labs.NORMAL);
		observationRange.setCode(code);

		String unitField = "";
		if(!EverestUtils.isNullorEmptyorWhitespace(unit)) {
			unitField = " ".concat(unit);
		}

		if(!EverestUtils.isNullorEmptyorWhitespace(range)) {
			observationRange.setText(new ED(range.concat(unitField)));
			observationRange.setValue(new ST(range.concat(unitField)));
		} else if (!EverestUtils.isNullorEmptyorWhitespace(minimum) || !EverestUtils.isNullorEmptyorWhitespace(maximum)) {
			if(EverestUtils.isNullorEmptyorWhitespace(minimum)) {
				observationRange.setText(new ED("Reference range is less than ".concat(maximum).concat(unitField)));
			} else if(EverestUtils.isNullorEmptyorWhitespace(maximum)) {
				observationRange.setText(new ED("Reference range is greater than ".concat(minimum).concat(unitField)));
			} else {
				observationRange.setText(new ED("Reference range is between ".concat(minimum).concat(" and ").concat(maximum).concat(unitField)));
			}

			// Either we get a complete, properly formatted PQ value with units, or we don't include it at all
			PQ low, high;
			try {
				low = new PQ(new BigDecimal(minimum), unit.replaceAll("\\s","_"));
			} catch (Exception e) {
				low = null;
			}
			try {
				high = new PQ(new BigDecimal(maximum), unit.replaceAll("\\s","_"));
			} catch (Exception e) {
				high = null;
			}

			if(low != null || high != null) {
				observationRange.setValue(new IVL<PQ>(low, high));
			}
		} else {
			return null;
		}

		return new ArrayList<ReferenceRange>(Arrays.asList(referenceRange));
	}
}
