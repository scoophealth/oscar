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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.marc.everest.datatypes.ANY;
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
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActClassObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ObservationInterpretation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationPhysicalPerformer;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.e2e.constant.BodyConstants.Labs;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.LabComponent;
import org.oscarehr.e2e.model.export.AbstractExportModelTest;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class ResultComponentModelTest extends AbstractExportModelTest {
	public static MeasurementDao measurementDao;
	public static MeasurementsExtDao measurementsExtDao;

	public static Measurement measurement;
	public static List<MeasurementsExt> measurementsExt;

	public static LabComponent labComponent;
	public static LabComponent nullLabComponent;
	public static ResultComponentModel resultComponentModel;

	@BeforeClass
	public static void beforeClass() {
		measurementDao = SpringUtils.getBean(MeasurementDao.class);
		measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);

		measurement = measurementDao.find(Constants.Runtime.VALID_LAB_MEASUREMENT);
		measurementsExt = measurementsExtDao.getMeasurementsExtByMeasurementId(Constants.Runtime.VALID_LAB_MEASUREMENT);
		resultComponentModel = new ResultComponentModel();
	}

	@Before
	public void before() {
		labComponent = new LabComponent(measurement, measurementsExt);
		nullLabComponent = new LabComponent(null, null);
	}

	@Test
	public void resultComponentModelNullTest() {
		assertNotNull(new ResultComponentModel());
	}

	@Test
	public void resultComponentStructureTest() {
		resultComponentStructureTestHelper(labComponent);
	}

	@Test
	public void resultComponentStructureNullTest() {
		resultComponentStructureTestHelper(nullLabComponent);
	}

	public void resultComponentStructureTestHelper(LabComponent labComponent) {
		Component4 component = resultComponentModel.getComponent(labComponent);
		assertNotNull(component);
		assertEquals(ActRelationshipHasComponent.HasComponent, component.getTypeCode().getCode());
		assertTrue(component.getContextConductionInd().toBoolean());
		assertEquals(Constants.TemplateOids.RESULT_COMPONENT_TEMPLATE_ID, component.getTemplateId().get(0).getRoot());

		Observation observation = component.getClinicalStatementIfObservation();
		assertNotNull(observation);
		assertEquals(ActClassObservation.OBS, observation.getClassCode().getCode());
		assertEquals(x_ActMoodDocumentObservation.Eventoccurrence, observation.getMoodCode().getCode());

		assertNotNull(observation.getId());
		assertNotNull(observation.getCode());
		assertNotNull(observation.getStatusCode());
	}

	public Observation observationHelper(LabComponent labComponent) {
		Component4 component = resultComponentModel.getComponent(labComponent);
		return component.getClinicalStatementIfObservation();
	}

	@Test
	public void idTest() {
		SET<II> ids = observationHelper(labComponent).getId();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertEquals(Constants.EMR.EMR_OID, id.getRoot());
		assertFalse(EverestUtils.isNullorEmptyorWhitespace(id.getExtension()));
		assertEquals(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.accession.toString()), id.getExtension());
	}

	@Test
	public void idNullTest() {
		SET<II> ids = observationHelper(nullLabComponent).getId();
		assertNotNull(ids);

		II id = ids.get(0);
		assertNotNull(id);
		assertTrue(id.isNull());
		assertEquals(NullFlavor.NoInformation, id.getNullFlavor().getCode());
	}

	@Test
	public void codeTest() {
		CD<String> code = observationHelper(labComponent).getCode();
		assertNotNull(code);
		assertEquals(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.identifier.toString()), code.getCode());
		assertEquals(Constants.CodeSystems.PCLOCD_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.PCLOCD_NAME, code.getCodeSystemName());
	}

	@Test
	public void codeNullTest() {
		CD<String> code = observationHelper(nullLabComponent).getCode();
		assertNotNull(code);
		assertTrue(code.isNull());
		assertEquals(NullFlavor.NoInformation, code.getNullFlavor().getCode());
	}

	@Test
	public void textTest() {
		String name = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.name.toString());
		ED text = observationHelper(labComponent).getText();
		assertNotNull(text);
		assertEquals(name, new String(text.getData()));
	}

	@Test
	public void textNullTest() {
		ED text = observationHelper(nullLabComponent).getText();
		assertNull(text);
	}

	@Test
	public void statusCodeActiveTest() {
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.olis_status.toString());
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.olis_status.toString(), "P");

		CS<ActStatus> status = observationHelper(labComponent).getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Active, status.getCode());
	}

	@Test
	public void statusCodeCompleteTest() {
		CS<ActStatus> status = observationHelper(labComponent).getStatusCode();
		assertNotNull(status);
		assertEquals(ActStatus.Completed, status.getCode());
	}

	@Test
	public void statusCodeNullTest() {
		CS<ActStatus> status = observationHelper(nullLabComponent).getStatusCode();
		assertNotNull(status);
		assertTrue(status.isNull());
		assertEquals(NullFlavor.NoInformation, status.getNullFlavor().getCode());
	}

	@Test
	public void effectiveTimeTest() {
		String datetime = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.datetime.toString());
		IVL<TS> ivl = observationHelper(labComponent).getEffectiveTime();
		assertNotNull(ivl);
		assertEquals(EverestUtils.buildTSFromDate(EverestUtils.stringToDate(datetime), TS.SECONDNOTIMEZONE), ivl.getLow());
	}

	@Test
	public void effectiveTimeNullTest() {
		IVL<TS> ivl = observationHelper(nullLabComponent).getEffectiveTime();
		assertNull(ivl);
	}

	@Test
	@Ignore
	public void valuePQTest() {
		String dataField = labComponent.getMeasurement().getDataField();
		String unit = "un it";
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.unit.toString());
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.unit.toString(), unit);

		ANY value = observationHelper(labComponent).getValue();
		assertNotNull(value);
		assertEquals(PQ.class, value.getDataType());

		PQ pq = (PQ) value;
		assertEquals(new BigDecimal(dataField), pq.getValue());
		assertEquals(unit.replaceAll("\\s","_"), pq.getUnit());
	}

	@Test
	public void valueSTValueUnitTest() {
		String dataField = "test";
		String unit = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.unit.toString());
		labComponent.getMeasurement().setDataField(dataField);

		ANY value = observationHelper(labComponent).getValue();
		assertNotNull(value);
		assertEquals(ST.class, value.getDataType());

		ST st = (ST) value;
		assertEquals(dataField.concat(" ").concat(unit), st.getValue());
	}

	@Test
	public void valueSTValueOnlyTest() {
		String dataField = labComponent.getMeasurement().getDataField();
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.unit.toString());

		ANY value = observationHelper(labComponent).getValue();
		assertNotNull(value);
		assertEquals(ST.class, value.getDataType());

		ST st = (ST) value;
		assertEquals(dataField, st.getValue());
	}

	@Test
	public void valueNullTest() {
		ANY value = observationHelper(nullLabComponent).getValue();
		assertNull(value);
	}

	@Test
	public void interpretationCodeAbnormalTest() {
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.abnormal.toString());
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.abnormal.toString(), Labs.ABNORMAL_CODE);

		SET<CE<ObservationInterpretation>> interpretationCodes = observationHelper(labComponent).getInterpretationCode();
		assertNotNull(interpretationCodes);

		CE<ObservationInterpretation> interpretation = interpretationCodes.get(0);
		assertNotNull(interpretation);
		assertEquals(ObservationInterpretation.Abnormal, interpretation.getCode());
		assertEquals(Labs.ABNORMAL, interpretation.getDisplayName());
	}

	@Test
	public void interpretationCodeNormalTest() {
		SET<CE<ObservationInterpretation>> interpretationCodes = observationHelper(labComponent).getInterpretationCode();
		assertNotNull(interpretationCodes);

		CE<ObservationInterpretation> interpretation = interpretationCodes.get(0);
		assertNotNull(interpretation);
		assertEquals(ObservationInterpretation.Normal, interpretation.getCode());
		assertEquals(Labs.NORMAL, interpretation.getDisplayName());
	}

	@Test
	public void interpretationCodeNullTest() {
		SET<CE<ObservationInterpretation>> interpretationCodes = observationHelper(nullLabComponent).getInterpretationCode();
		assertNull(interpretationCodes);
	}

	@Test
	public void performerTest() {
		String labname = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.labname.toString());
		String datetime = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.datetime.toString());

		ArrayList<Performer2> performers = observationHelper(labComponent).getPerformer();
		assertNotNull(performers);
		assertEquals(1, performers.size());

		Performer2 performer = performers.get(0);
		assertNotNull(performer);
		assertEquals(ParticipationPhysicalPerformer.PRF, performer.getTypeCode().getCode());
		assertEquals(Constants.TemplateOids.PERFORMER_PARTICIPATION_TEMPLATE_ID, performer.getTemplateId().get(0).getRoot());
		assertNotNull(performer.getTime());
		assertEquals(EverestUtils.buildTSFromDate(EverestUtils.stringToDate(datetime), TS.SECONDNOTIMEZONE), performer.getTime().getLow());

		AssignedEntity assignedEntity = performer.getAssignedEntity();
		assertNotNull(assignedEntity);
		assertNotNull(assignedEntity.getId());
		assertEquals(1, assignedEntity.getId().size());

		II ii = assignedEntity.getId().get(0);
		assertNotNull(ii);
		assertTrue(ii.isNull());
		assertEquals(NullFlavor.NoInformation, ii.getNullFlavor().getCode());

		Organization organization = assignedEntity.getRepresentedOrganization();
		assertNotNull(organization);
		assertNotNull(organization.getName());

		ON on = organization.getName().get(0);
		assertNotNull(on);
		assertEquals(EntityNameUse.OfficialRecord, on.getUse().get(0).getCode());
		assertNotNull(on.getParts());
		assertEquals(1, on.getParts().size());
		assertEquals(new ENXP(labname), on.getPart(0));
	}

	@Test
	public void performerNullTest() {
		ArrayList<Performer2> performers = observationHelper(nullLabComponent).getPerformer();
		assertNull(performers);
	}

	@Test
	public void specimenCollectionValidTest() {
		String datetime = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.datetime.toString());

		EntryRelationship entryRelationship = observationHelper(labComponent).getEntryRelationship().get(0);
		assertNotNull(entryRelationship);
		assertEquals(x_ActRelationshipEntryRelationship.HasComponent, entryRelationship.getTypeCode().getCode());
		assertTrue(entryRelationship.getContextConductionInd().toBoolean());

		Procedure procedure = entryRelationship.getClinicalStatementIfProcedure();
		assertNotNull(procedure);
		assertNotNull(procedure.getEffectiveTime());
		assertEquals(EverestUtils.buildTSFromDate(EverestUtils.stringToDate(datetime), TS.SECONDNOTIMEZONE), procedure.getEffectiveTime().getLow());
	}

	@Test
	public void specimenCollectionNullTest() {
		EntryRelationship entryRelationship = observationHelper(nullLabComponent).getEntryRelationship().get(0);
		assertNull(entryRelationship);
	}

	@Test
	public void commentTest() {
		EntryRelationship entryRelationship = observationHelper(labComponent).getEntryRelationship().get(1);
		assertNotNull(entryRelationship);
	}

	@Test
	public void commentNullTest() {
		EntryRelationship entryRelationship = observationHelper(nullLabComponent).getEntryRelationship().get(1);
		assertNull(entryRelationship);
	}

	@Test
	public void referenceRangeRangeTest() {
		String range = "range";
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.minimum.toString());
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.range.toString(), range);

		ArrayList<ReferenceRange> referenceRanges = observationHelper(labComponent).getReferenceRange();
		assertNotNull(referenceRanges);
		assertEquals(1, referenceRanges.size());
		assertNotNull(referenceRanges.get(0));

		ObservationRange observationRange = referenceRanges.get(0).getObservationRange();
		assertNotNull(observationRange);

		CD<String> code = observationRange.getCode();
		assertNotNull(code);
		assertEquals(Labs.NORMAL_CODE, code.getCode());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_NAME, code.getCodeSystemName());
		assertEquals(Labs.NORMAL, code.getDisplayName());

		ED text = observationRange.getText();
		assertNotNull(text);
		assertTrue(new String(text.getData()).contains(range));

		assertNotNull(observationRange.getValue());
		assertEquals(ST.class, observationRange.getValue().getDataType());
		ST value = (ST) observationRange.getValue();
		assertTrue(value.getValue().contains(range));
	}

	@Test
	public void referenceRangeMinTest() {
		String min = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.minimum.toString());

		ArrayList<ReferenceRange> referenceRanges = observationHelper(labComponent).getReferenceRange();
		assertNotNull(referenceRanges);
		assertEquals(1, referenceRanges.size());
		assertNotNull(referenceRanges.get(0));

		ObservationRange observationRange = referenceRanges.get(0).getObservationRange();
		assertNotNull(observationRange);

		CD<String> code = observationRange.getCode();
		assertNotNull(code);
		assertEquals(Labs.NORMAL_CODE, code.getCode());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_NAME, code.getCodeSystemName());
		assertEquals(Labs.NORMAL, code.getDisplayName());

		ED text = observationRange.getText();
		assertNotNull(text);
		assertTrue(new String(text.getData()).contains(min));

		assertNotNull(observationRange.getValue());
		assertEquals(IVL.class, observationRange.getValue().getDataType());
		@SuppressWarnings("unchecked")
		IVL<PQ> value = (IVL<PQ>) observationRange.getValue();
		assertEquals(new BigDecimal(min), value.getLow().getValue());
	}

	@Test
	public void referenceRangeMaxTest() {
		String max = "1234";
		labComponent.getMeasurementsMap().remove(Constants.MeasurementsExtKeys.minimum.toString());
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.maximum.toString(), max);

		ArrayList<ReferenceRange> referenceRanges = observationHelper(labComponent).getReferenceRange();
		assertNotNull(referenceRanges);
		assertEquals(1, referenceRanges.size());
		assertNotNull(referenceRanges.get(0));

		ObservationRange observationRange = referenceRanges.get(0).getObservationRange();
		assertNotNull(observationRange);

		CD<String> code = observationRange.getCode();
		assertNotNull(code);
		assertEquals(Labs.NORMAL_CODE, code.getCode());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_NAME, code.getCodeSystemName());
		assertEquals(Labs.NORMAL, code.getDisplayName());

		ED text = observationRange.getText();
		assertNotNull(text);
		assertTrue(new String(text.getData()).contains(max));

		assertNotNull(observationRange.getValue());
		assertEquals(IVL.class, observationRange.getValue().getDataType());
		@SuppressWarnings("unchecked")
		IVL<PQ> value = (IVL<PQ>) observationRange.getValue();
		assertEquals(new BigDecimal(max), value.getHigh().getValue());
	}

	@Test
	public void referenceRangeMinMaxTest() {
		String min = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.minimum.toString());
		String max = "1234";
		labComponent.getMeasurementsMap().put(Constants.MeasurementsExtKeys.maximum.toString(), max);

		ArrayList<ReferenceRange> referenceRanges = observationHelper(labComponent).getReferenceRange();
		assertNotNull(referenceRanges);
		assertEquals(1, referenceRanges.size());
		assertNotNull(referenceRanges.get(0));

		ObservationRange observationRange = referenceRanges.get(0).getObservationRange();
		assertNotNull(observationRange);

		CD<String> code = observationRange.getCode();
		assertNotNull(code);
		assertEquals(Labs.NORMAL_CODE, code.getCode());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_OID, code.getCodeSystem());
		assertEquals(Constants.CodeSystems.OBSERVATION_INTERPRETATION_NAME, code.getCodeSystemName());
		assertEquals(Labs.NORMAL, code.getDisplayName());

		ED text = observationRange.getText();
		assertNotNull(text);
		assertTrue(new String(text.getData()).contains(min));
		assertTrue(new String(text.getData()).contains(max));

		assertNotNull(observationRange.getValue());
		assertEquals(IVL.class, observationRange.getValue().getDataType());
		@SuppressWarnings("unchecked")
		IVL<PQ> value = (IVL<PQ>) observationRange.getValue();
		assertEquals(new BigDecimal(min), value.getLow().getValue());
		assertEquals(new BigDecimal(max), value.getHigh().getValue());
	}

	@Test
	public void referenceRangeNullTest() {
		ArrayList<ReferenceRange> referenceRanges = observationHelper(nullLabComponent).getReferenceRange();
		assertNull(referenceRanges);
	}
}
