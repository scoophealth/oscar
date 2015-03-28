/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.born;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
//import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.managers.CodingSystemManager;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.StringUtils;
import ca.bornontario.wbcsd.BORNWBCSDBatch;
import ca.bornontario.wbcsd.BORNWBCSDBatchDocument;
import ca.bornontario.wbcsd.Gender;
import ca.bornontario.wbcsd.HealthProblem;
import ca.bornontario.wbcsd.ImmunizationData;
import ca.bornontario.wbcsd.Medication;
import ca.bornontario.wbcsd.PatientInfo;
import ca.bornontario.wbcsd.ProblemsDiagnosisCodeSystem;
import ca.bornontario.wbcsd.VisitData;

public class BORNWbCsdXmlGenerator {

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	//private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private Logger logger = MiscUtils.getLogger();

	private PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private PreventionExtDao preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);

	
	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private DxresearchDAO dxResearchDAO = SpringUtils.getBean(DxresearchDAO.class);
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public boolean addToStream(Writer os, XmlOptions opts, Integer demographicNo, boolean useClinicInfoForOrganizationId) throws IOException {
		BORNWBCSDBatchDocument bornBatchDocument = BORNWBCSDBatchDocument.Factory.newInstance();
		BORNWBCSDBatch bornBatch = bornBatchDocument.addNewBORNWBCSDBatch();
		PatientInfo patientInfo = bornBatch.addNewPatientInfo();

		//populate patient info
		populatePatientInfo(patientInfo, demographicNo, useClinicInfoForOrganizationId);

		populateVisitData(patientInfo, demographicNo);

		populateImmunizationData(patientInfo, demographicNo);

		////business validation

		//TODO: xml validation - how can we report the problem better??
		XmlOptions m_validationOptions = new XmlOptions();
		ArrayList<Object> validationErrors = new ArrayList<Object>();
		m_validationOptions.setErrorListener(validationErrors);
		if (!bornBatchDocument.validate(m_validationOptions)) {
			MiscUtils.getLogger().warn("BORN WBCSD XML failed validation");
			for (Object o : validationErrors) {
				XmlValidationError xve = (XmlValidationError) o;
				MiscUtils.getLogger().warn(o);
			}
		}

		//write to stream provided
		bornBatchDocument.save(os, opts);

		MiscUtils.getLogger().debug("Wrote out batch document for patient " + demographicNo);
		return true;

	}

	private void populateVisitData(PatientInfo patientInfo, Integer demographicNo) {
		
		//get all the measurements, and put into 'date' buckets
		//get all the dx entries, and put into 'date' buckets
		//get all drugs, and put into 'date' buckets
		
		//TODO:do we need to filter by statuses?
		List<Drug> drugs = drugDao.findByDemographicId(demographicNo);
		
		Map<String,List<Object>> map = new HashMap<String,List<Object>>();
		
		for(Measurement m:measurementDao.findByDemographicNo(demographicNo)) {
			String date = dateFormatter.format(m.getCreateDate());
			addToDateMap(map,date,m);
		}
		
		for(Dxresearch dx:dxResearchDAO.findNonDeletedByDemographicNo(demographicNo)) {
			String date = dateFormatter.format(dx.getUpdateDate());
			addToDateMap(map,date,dx);
		}
		
		for(Drug drug:drugs) {
			String date = dateFormatter.format(drug.getCreateDate());
			addToDateMap(map,date,drug);
		}
		
		for(String date: map.keySet()) {
			List<Object> items = map.get(date);
			
			VisitData visitData = patientInfo.addNewVisitData();
		
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormatter.parse(date));
				visitData.setVisitDate(cal);
			} catch(ParseException e) {
				logger.warn("can't reparse date?");
			}
			
			for(Object item:items) {
				if(item instanceof Measurement) {
					populateMeasurement(visitData, (Measurement)item);
				}
				if(item instanceof Dxresearch) {
					populateProblem(visitData,(Dxresearch)item);
				}
				if(item instanceof Drug) {
					populateDrug(visitData,(Drug)item);
				}
			}
		}
	}
	
	private void populateMeasurement(VisitData visitData, Measurement measurement) {
		if("WT".equals(measurement.getType())) {
			try {
				int wt = Integer.parseInt(measurement.getDataField()) * 1000;
				visitData.setWeight( wt); //convert from kg to grams
			}catch(NumberFormatException e) {
				MiscUtils.getLogger().warn("can't parse weight measurement (id="+measurement.getId()+")");
			}
		} else if("HT".equals(measurement.getType())) {
			try {
				BigDecimal ht =new BigDecimal(measurement.getDataField());
				visitData.setHeight(ht);
			} catch(NumberFormatException e) {
				MiscUtils.getLogger().warn("can't parse height measurement (id="+measurement.getId()+")");
			}
			
		}
	}
	
	private void populateProblem(VisitData visitData, Dxresearch dx) {
		HealthProblem problem = visitData.addNewHealthProblem();
		
		
		problem.setProblemDiagnosisCode(dx.getDxresearchCode());
		
		if("icd9".equals(dx.getCodingSystem())) {
			problem.setProblemDiagnosisCodeSystem(ProblemsDiagnosisCodeSystem.ICD_9);
			
			CodingSystemManager csm = SpringUtils.getBean(CodingSystemManager.class);
			String descr = csm.getCodeDescription(dx.getCodingSystem(), dx.getDxresearchCode());
			if(descr != null) {
				problem.setProblemDiagnosisName(descr);
			}
		}
		problem.setProblemStatus(String.valueOf(dx.getStatus()));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dx.getStartDate());
		problem.setProblemDiagnosisDate(cal);
		
	/*
		problem.setProblemOnsetDate(arg0);
		*/
	}
	
	private void populateDrug(VisitData visitData, Drug drug) {
		Medication medication = visitData.addNewMedication();
	
		if(drug.getBrandName() != null && !drug.getBrandName().isEmpty()) {
			medication.setMedicationName(drug.getBrandName());
		}
		
		if(drug.getRegionalIdentifier() != null && !drug.getRegionalIdentifier().isEmpty()) {
			try {
				medication.setMedicationDIN(Integer.parseInt(drug.getRegionalIdentifier()));
			} catch(NumberFormatException e) {
				logger.warn("regional identifier is not a number (id="+drug.getId()+")");
			}
		}
		//if(drug.getDosage() != null && !drug.getDosage().isEmpty()) {
		//	medication.setMedicationDosage(drug.getDosage());
		//}
		
		if(drug.getRefillQuantity() != null) {
			medication.setMedicationNumberofRefills(drug.getRefillQuantity()+"");
		}
		/*
		medication.setMedicationDosage(arg0);
		medication.setMedicationDrugStrength(arg0);
		
		medication.setMedicationFrequency(arg0);
		
		medication.setMedicationStartDate(arg0);
		*/
	}
	
	private void addToDateMap(Map<String,List<Object>> map, String date, Object obj) {
		List<Object> items = map.get(date);
		if(items == null) {
			items = new ArrayList<Object>();
			map.put(date, items);
		}
		items.add(obj);
	}

	private void populateImmunizationData(PatientInfo patientInfo, Integer demographicNo) {
		List<Prevention> preventions = preventionDao.findActiveByDemoId(demographicNo);

		if (preventions.isEmpty()) {
			return;
		}

		
		for(Prevention prevention:preventions) {
			List<PreventionExt> exts = preventionExtDao.findByPreventionId(prevention.getId());
			ImmunizationData immunizationData = patientInfo.addNewImmunizationData();
			Calendar cal = Calendar.getInstance();
			cal.setTime(prevention.getPreventionDate());
			immunizationData.setDateReceived(cal);
			immunizationData.setVaccineName(prevention.getPreventionType()); 
			/*
			immunizationData.setVaccineDIN(arg0);
			immunizationData.setDoseNum(arg0);
			immunizationData.setExpiryDate(arg0);
			immunizationData.setInitials(arg0);
			immunizationData.setInjectionSite(arg0);
			*/
			for(PreventionExt ext:exts) {
				if("lot".equals(ext.getkeyval())) {
					immunizationData.setLotNumber(ext.getVal());
				} else if("comments".equals(ext.getkeyval())) {
					immunizationData.setComments(ext.getVal());
				}
			}
			
			
			
			
		}

	}

	/*
	 * Populates from rourke, master record, and chart
	 */
	private void populatePatientInfo(PatientInfo patientInfo, Integer demographicNo, boolean useClinicInfoForOrganizationId) {

		Demographic demographic = demographicDao.getDemographicById(demographicNo);
		if (demographic == null) {
			logger.warn("cannot find demographic!");
		}

		patientInfo.setUniqueVendorIDSequence(demographicNo.toString());
		patientInfo.setFirstName(demographic.getFirstName());
		patientInfo.setLastName(demographic.getLastName());
		patientInfo.setDOB(new XmlCalendar(demographic.getBirthDayAsString()));

		if ("M".equals(demographic.getSex())) patientInfo.setGender(Gender.M);
		else if ("F".equals(demographic.getSex())) patientInfo.setGender(Gender.F);
		else patientInfo.setGender(Gender.U);

		if (StringUtils.filled(demographic.getChartNo())) patientInfo.setChartNumber(String.valueOf(demographic.getDemographicNo()));
		else patientInfo.setChartNumber("0");

		if (StringUtils.filled(demographic.getHin())) patientInfo.setHealthCardNum(demographic.getHin());
		else patientInfo.setHealthCardNum("0");

		patientInfo.setHealthCardType(0);
		if (StringUtils.filled(demographic.getHcType())) {
			if (demographic.getHcType().equals("ON")) patientInfo.setHealthCardType(1);
			if (demographic.getHcType().equals("QC")) patientInfo.setHealthCardType(2);
		}

		//TODO
		/*
		patientInfo.setResidentAddressLine1(arg0);
		patientInfo.setResidentAddressLine2(arg0);
		patientInfo.setResidentCity(arg0);
		patientInfo.setResidentCountryProvince(arg0);
		patientInfo.setResidentPostalCode(arg0);
		*/

		patientInfo.setOrganizationID(OscarProperties.getInstance().getProperty("born_orgcode"));

		if (useClinicInfoForOrganizationId) {
			ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
			ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();
			patientInfo.setOrganizationID(clinicInfo.getFacilityName());
		}
	}

}
