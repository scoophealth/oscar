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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.marc.everest.datatypes.ANY;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PQ;
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
import org.oscarehr.common.model.Measurement;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.constant.BodyConstants.ClinicallyMeasuredObservations;
import org.oscarehr.e2e.model.export.template.AuthorParticipationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class ClinicallyMeasuredObservationsModel {
	private static Logger log = Logger.getLogger(ClinicallyMeasuredObservationsModel.class.getName());
	private Measurement measurement;

	private SET<II> ids;
	private CD<String> code;
	private ActStatus statusCode;
	private ArrayList<Author> authors;

	public ClinicallyMeasuredObservationsModel(Measurement measurement) {
		if(measurement == null) {
			this.measurement = new Measurement();
		} else {
			this.measurement = measurement;
		}

		setIds();
		setCode();
		setStatusCode();
		setAuthor();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(!EverestUtils.isNullorEmptyorWhitespace(EverestUtils.getTypeDescription(measurement.getType()))) {
			sb.append(EverestUtils.getTypeDescription(measurement.getType()));
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(measurement.getDataField())) {
			sb.append(": ".concat(measurement.getDataField()));
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(Mappings.measurementUnitMap.get(measurement.getType()))) {
			sb.append(" ".concat(Mappings.measurementUnitMap.get(measurement.getType())));
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(measurement.getMeasuringInstruction())) {
			sb.append(" (".concat(measurement.getMeasuringInstruction()).concat(")"));
		}
		if(measurement.getDateObserved() != null) {
			sb.append(" ".concat(measurement.getDateObserved().toString()));
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.ClinicalMeasuredObservations, measurement.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		this.code = new CD<String>();
		this.code.setNullFlavor(NullFlavor.NoInformation);
	}

	public ActStatus getStatusCode() {
		return statusCode;
	}

	private void setStatusCode() {
		this.statusCode = ActStatus.Completed;
	}

	public ArrayList<Author> getAuthor() {
		return authors;
	}

	private void setAuthor() {
		this.authors = new ArrayList<Author>();
		this.authors.add(new AuthorParticipationModel(measurement.getProviderNo()).getAuthor(measurement.getCreateDate()));
	}

	public ArrayList<Component4> getComponents() {
		ArrayList<Component4> components = new ArrayList<Component4>();
		if(ClinicallyMeasuredObservations.BLOOD_PRESSURE_CODE.equals(measurement.getType())) {
			// Parse Systolic and Diastolic Blood Pressure integers
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(measurement.getDataField());
			List<Integer> values = new ArrayList<Integer>();
			while(matcher.find()) {
				values.add(Integer.parseInt(matcher.group()));
			}

			// Make a deep copy of measurement object
			Measurement tempMeasurement = null;
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);
				out.writeObject(measurement);
				out.flush();
				out.close();
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
				tempMeasurement = (Measurement) in.readObject();
			} catch(Exception e) {
				log.error(e.toString(), e);
			}

			// Add Systolic Component
			tempMeasurement.setType(ClinicallyMeasuredObservations.DIASTOLIC_CODE);
			tempMeasurement.setDataField(values.get(0).toString());
			components.add(new ComponentObservation().getComponent(tempMeasurement));

			// Add Diastolic Component
			tempMeasurement.setType(ClinicallyMeasuredObservations.SYSTOLIC_CODE);
			tempMeasurement.setDataField(values.get(1).toString());
			components.add(new ComponentObservation().getComponent(tempMeasurement));
		} else {
			components.add(new ComponentObservation().getComponent(measurement));
		}

		return components;
	}

	class ComponentObservation {
		private Measurement measurement;

		public Component4 getComponent(Measurement measurement) {
			if(measurement == null) {
				this.measurement = new Measurement();
			} else {
				this.measurement = measurement;
			}

			Component4 component = new Component4(ActRelationshipHasComponent.HasComponent, new BL(true));
			Observation observation = new Observation();

			observation.setMoodCode(x_ActMoodDocumentObservation.Eventoccurrence);
			observation.setId(getComponentIds());
			observation.setCode(getComponentCode());
			observation.setText(getComponentText());
			observation.setEffectiveTime(getComponentTime());
			observation.setValue(getComponentValue());

			component.setClinicalStatement(observation);
			return component;
		}

		private SET<II> getComponentIds() {
			return EverestUtils.buildUniqueId(Constants.IdPrefixes.ClinicalMeasuredObservations, measurement.getId());
		}

		private CD<String> getComponentCode() {
			CD<String> code = new CD<String>();
			if(Mappings.measurementCodeMap.get(measurement.getType()) != null) {
				code.setCodeEx(Mappings.measurementCodeMap.get(measurement.getType()));
				code.setCodeSystem(Constants.CodeSystems.LOINC_OID);
				code.setCodeSystemName(Constants.CodeSystems.LOINC_NAME);
				code.setCodeSystemVersion(Constants.CodeSystems.LOINC_VERSION);
			} else {
				code.setNullFlavor(NullFlavor.Unknown);
			}
			return code;
		}

		private ED getComponentText() {
			String text = new String();
			if(!EverestUtils.isNullorEmptyorWhitespace(EverestUtils.getTypeDescription(measurement.getType()))) {
				text = EverestUtils.getTypeDescription(measurement.getType());
			}
			if(!EverestUtils.isNullorEmptyorWhitespace(measurement.getMeasuringInstruction())) {
				text = text.concat(" (").concat(measurement.getMeasuringInstruction().concat(")"));
			}

			if(!text.isEmpty()) {
				return new ED(text);
			}
			return null;
		}

		private IVL<TS> getComponentTime() {
			IVL<TS> ivl = null;
			TS startTime = EverestUtils.buildTSFromDate(measurement.getDateObserved(), TS.SECONDNOTIMEZONE);
			if(startTime != null) {
				ivl = new IVL<TS>(startTime, null);
			}

			return ivl;
		}

		private ANY getComponentValue() {
			String dataField = measurement.getDataField();
			String unit = Mappings.measurementUnitMap.get(measurement.getType());
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
	}
}
