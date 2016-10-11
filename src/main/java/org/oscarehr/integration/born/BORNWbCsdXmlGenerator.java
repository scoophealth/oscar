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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BORNPathwayMappingDao;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
//import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.BORNPathwayMapping;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.CodingSystemManager;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import ca.bornontario.wbcsd.AssociatedRourke;
import ca.bornontario.wbcsd.BORNWBCSDBatch;
import ca.bornontario.wbcsd.BORNWBCSDBatchDocument;
import ca.bornontario.wbcsd.CountryProvince;
import ca.bornontario.wbcsd.Gender;
import ca.bornontario.wbcsd.HealthProblem;
import ca.bornontario.wbcsd.ImmunizationData;
import ca.bornontario.wbcsd.Medication;
import ca.bornontario.wbcsd.PatientInfo;
import ca.bornontario.wbcsd.PrimaryPhysicianType;
import ca.bornontario.wbcsd.ProblemsDiagnosisCodeSystem;
import ca.bornontario.wbcsd.Referral;
import ca.bornontario.wbcsd.ReferralCategory;
import ca.bornontario.wbcsd.ReferralCategoryName;
import ca.bornontario.wbcsd.ReferringProviderType;
import ca.bornontario.wbcsd.Vaccine;
import ca.bornontario.wbcsd.VisitData;
import oscar.OscarProperties;
import oscar.util.StringUtils;

public class BORNWbCsdXmlGenerator {

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	//private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private Logger logger = MiscUtils.getLogger();

	private PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private PreventionExtDao preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	
	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private DxresearchDAO dxResearchDAO = SpringUtils.getBean(DxresearchDAO.class);
	private ConsultationRequestDao consultationRequestDao = SpringUtils.getBean(ConsultationRequestDao.class);
	private ConsultationServiceDao consultationServiceDao = SpringUtils.getBean(ConsultationServiceDao.class);
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public boolean addToStream(Writer os, XmlOptions opts, Integer demographicNo, boolean useClinicInfoForOrganizationId) throws IOException {
		BORNWBCSDBatchDocument bornBatchDocument = BORNWBCSDBatchDocument.Factory.newInstance();
		BORNWBCSDBatch bornBatch = bornBatchDocument.addNewBORNWBCSDBatch();
		PatientInfo patientInfo = bornBatch.addNewPatientInfo();

		//populate patient info
		populatePatientInfo(patientInfo, demographicNo, useClinicInfoForOrganizationId);

		populateVisitData(patientInfo, demographicNo);

		populateImmunizationData(patientInfo, demographicNo);

		populateReferralData(patientInfo, demographicNo);
		
		////business validation
		//business validation
		if (!isAgeLessThan7y(patientInfo)) {
			MiscUtils.getLogger().warn("Child is over 6.5 years old..skipping");
			return false;
		}
				
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
		
			XmlCalendar cal = new XmlCalendar(date);
			visitData.setVisitDate(cal);
			
			
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
	
	private void populateReferralData(PatientInfo patientInfo, Integer demographicNo) {
		List<Integer> requestIds = new ArrayList<Integer>();
		
		List<String> services = Arrays.asList("Autism Intervention Services","Blind Low Vision Program","Child Care","Child Protection Services",
				"Children's Mental Health Services","Children's Treatment Centre","Community Care Access Centre","Community Parks and Recreation Programs",
				"Dental Services","Family Resource Programs","Healthy Babies Healthy Children","Infant Development Program","Infant Hearing Program",
				"Ontario Early Years Centre","Paediatrician/Developmental Paediatrician","Preschool Speech and Language Program","Public Health",
				"Schools","Services for Physical and Developmental Disabilities","Services for the Hearing Impaired","Services for the Visually Impaired",
				"Specialized Child Care Programming","Specialized Medical Services");
		
		List<ConsultationRequest> consultRequests = consultationRequestDao.findByDemographicAndServices(demographicNo, services);
		for(ConsultationRequest consult:consultRequests) {
			if(!requestIds.contains(consult.getId())) { //only add if it hasn't been added before
				requestIds.add(consult.getId());
				addReferral(patientInfo, demographicNo, consult, null);
			}
		}
		
		//get the ones that reverse map to a BORN pathway as well
		BORNPathwayMappingDao bornPathwayMappingDao = SpringUtils.getBean(BORNPathwayMappingDao.class);
	
		for(BORNPathwayMapping mapping:bornPathwayMappingDao.findAll()) {
			ConsultationServices service = consultationServiceDao.find(mapping.getServiceId());
			if(service != null) {
				consultRequests = consultationRequestDao.findByDemographicAndService(demographicNo, service.getServiceDesc());
				for(ConsultationRequest consult:consultRequests) {
					if(!requestIds.contains(consult.getId())) { //only add if it hasn't been added before
						requestIds.add(consult.getId());
						addReferral(patientInfo, demographicNo, consult, mapping.getBornPathway());
					}
				}
			}
		}
		
	}
	
	private void addReferral(PatientInfo patientInfo, Integer demographicNo, ConsultationRequest consult, String mappedCategory ) {
		Referral referral = patientInfo.addNewReferral();
		
		Demographic demographic = demographicDao.getDemographic(demographicNo.toString());
		Provider mrp = providerDao.getProvider(demographic.getProviderNo());
		ConsultationServices service = consultationServiceDao.find(consult.getServiceId());
		ProfessionalSpecialist professionalSpecialist = consult.getProfessionalSpecialist();
		Provider provider = providerDao.getProvider(consult.getProviderNo());
		
		if(mrp != null) {
			referral.setPrimaryPhysician(demographic.getProviderNo());
			referral.setPrimaryPhysicianType(PrimaryPhysicianType.X_3);
		}
		
		
		setReferralCategory(referral.addNewReferralCategory(),(mappedCategory==null)?service.getServiceDesc():mappedCategory);
	
		if(referral.getReferralCategory().getReferralCategoryName().equals(ReferralCategory.UNKN)) {
			referral.setReferralCategoryOther(service.getServiceDesc());
		}
		
		referral.setReferralDate(new XmlCalendar(dateFormatter.format(consult.getReferralDate())));

		referral.setReferralStatus(getStatusText(consult.getStatus()));
		referral.setReferralUrgency(consult.getUrgency());
		
		if(professionalSpecialist != null) {
			referral.setReferredToPerson(professionalSpecialist.getLastName() + ", " + professionalSpecialist.getFirstName());
		}
		
		referral.setReferringProvider(provider.getProviderNo());
		referral.setReferringProviderType(ReferringProviderType.X_3);
		
		if(consult.getSource() != null) {
			referral.setAssociatedRourke(getAssociatedRourke(consult.getSource()));
		}
		/*
		referral.setReferredToSite(arg0);
		*/
	}
	
	private AssociatedRourke.Enum getAssociatedRourke(String source) {
		if(source == null || "".equals(source)) {
			return null;
		}
		
		switch(source) {
		case "1w": 
			return AssociatedRourke.RBRW_01;
		case "2w": 
			return AssociatedRourke.RBRW_02;
		case "1m":
			return AssociatedRourke.RBRM_01;
		case "2m":
			return AssociatedRourke.RBRM_02;
		case "4m":
			return AssociatedRourke.RBRM_04;
		case "6m":
			return AssociatedRourke.RBRM_06;
		case "9m":
			return AssociatedRourke.RBRM_09;
		case "12m":
			return AssociatedRourke.RBRM_12_13;
		case "15m":
			return AssociatedRourke.RBRM_15;
		case "18m":
			return AssociatedRourke.RBRM_18;
		case "2y":
			return AssociatedRourke.RBRY_2_3;
		case "4y":
			return AssociatedRourke.RBRY_4_5;
			
		}
		
		return null;
	}
	
	private String getStatusText(String status) {
		String val = "N/A";
		
		if("1".equals(status)) {
			return "Nothing";
		} else if("2".equals(status)) {
			return "Pending Specialist Callback";
		} if("3".equals(status)) {
			return "Pending Patient Callback";
		} if("4".equals(status)) {
			return "Completed";
		}
		return val;
	}
	
	public void setReferralCategory(ReferralCategoryName result,String serviceName) {
		switch(serviceName) {
		case "Autism Intervention Services":
			result.setReferralCategoryName(ReferralCategory.AIS);
			break;
		case "Blind Low Vision Program":
			result.setReferralCategoryName( ReferralCategory.BLVP);
			break;
		case "Child Care":
			result.setReferralCategoryName( ReferralCategory.CC);
			break;
		case "Child Protection Services":
			result.setReferralCategoryName( ReferralCategory.CPS);
			break;
		case "Children's Mental Health Services":
			result.setReferralCategoryName( ReferralCategory.CMHS);
			break;
		case "Children's Treatment Centre":
			result.setReferralCategoryName( ReferralCategory.CTC);
			break;
		case "Community Care Access Centre":
			result.setReferralCategoryName( ReferralCategory.CCAC);
			break;
		case "Community Parks and Recreation Programs":
			result.setReferralCategoryName( ReferralCategory.CPRP);
			break;
		case "Dental Services":
			result.setReferralCategoryName( ReferralCategory.DS);
			break;
		case "Family Resource Programs":
			result.setReferralCategoryName( ReferralCategory.FSP);
			break;
		case "Healthy Babies Healthy Children":
			result.setReferralCategoryName( ReferralCategory.HBHC);
			break;
		case "Infant Development Program":
			result.setReferralCategoryName( ReferralCategory.IDP);
			break;
		case "Infant Hearing Program":
			result.setReferralCategoryName( ReferralCategory.IHP);
			break;
		case "Ontario Early Years Centre":
			result.setReferralCategoryName( ReferralCategory.OEYC);
			break;
		case "Paediatrician/Developmental Paediatrician":
			result.setReferralCategoryName( ReferralCategory.PAED);
			break;
		case "Preschool Speech and Language Program":
			result.setReferralCategoryName( ReferralCategory.PSLP);
			break;
		case "Public Health":
			result.setReferralCategoryName( ReferralCategory.PH);
			break;
		case "Schools":
			result.setReferralCategoryName( ReferralCategory.SCHL);
			break;
		case "Services for Physical and Developmental Disabilities":
			result.setReferralCategoryName( ReferralCategory.SPDD);
			break;
		case "Services for the Hearing Impaired":
			result.setReferralCategoryName( ReferralCategory.SHI);
			break;
		case "Services for the Visually Impaired":
			result.setReferralCategoryName( ReferralCategory.SVO);
			break;
		case "Specialized Child Care Programming":
			result.setReferralCategoryName( ReferralCategory.SCCP);
			break;
		case "Specialized Medical Services":
			result.setReferralCategoryName( ReferralCategory.SMS);
			break;
		default:
			result.setReferralCategoryName(ReferralCategory.UNKN);
		}

	}
	
	private void populateMeasurement(VisitData visitData, Measurement measurement) {
		if("WT".equals(measurement.getType())) {
			try {
				double wt = Double.parseDouble(measurement.getDataField()) * 1000;
				visitData.setWeight( (int)wt); //convert from kg to grams
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
		
		if(!"icd9".equals(dx.getCodingSystem())) {
			return;
		}
		
		HealthProblem problem = visitData.addNewHealthProblem();
		
		
		problem.setProblemDiagnosisCode(dx.getDxresearchCode());
		
		problem.setProblemDiagnosisCodeSystem(ProblemsDiagnosisCodeSystem.ICD_9);
		
		
		CodingSystemManager csm = SpringUtils.getBean(CodingSystemManager.class);
		String descr = csm.getCodeDescription(dx.getCodingSystem(), dx.getDxresearchCode());
		if(descr != null) {
			problem.setProblemDiagnosisName(descr);
		}
		problem.setProblemStatus(String.valueOf(dx.getStatus()));
		
		problem.setProblemDiagnosisDate(new XmlCalendar(dateFormatter.format(dx.getStartDate())));
		/*
		problem.setProblemOnsetDate(arg0);
		*/
	}
	
	private void populateDrug(VisitData visitData, Drug drug) {
		Medication medication = visitData.addNewMedication();
	
		if(drug.getBrandName() != null && !drug.getBrandName().isEmpty()) {
			medication.setMedicationName(drug.getBrandName());
		}
		
		if(medication.getMedicationName()==null || medication.getMedicationName().isEmpty()) {
			medication.setMedicationName(drug.getCustomName());
		}
		
		if(medication.getMedicationName()==null || medication.getMedicationName().isEmpty()) {
			medication.setMedicationName(drug.getGenericName());
		}
		
		if(medication.getMedicationName()==null || medication.getMedicationName().isEmpty()) {
			logger.warn("Could not find name for this drug! " + drug.getId());
		}
		
		if(drug.getRegionalIdentifier() != null && !drug.getRegionalIdentifier().isEmpty()) {
			try {
				medication.setMedicationDIN(drug.getRegionalIdentifier());
			} catch(NumberFormatException e) {
				logger.warn("regional identifier is not a number (id="+drug.getId()+")");
			}
		}
		
		if(drug.getRefillQuantity() != null) {
			medication.setMedicationNumberofRefills(drug.getRefillQuantity()+"");
		}
		/*
		medication.setMedicationDosage(arg0);
		medication.setMedicationDrugStrength(arg0);
		
		medication.setMedicationFrequency(arg0);
		*/
		medication.setMedicationStartDate(new XmlCalendar(dateFormatter.format(drug.getRxDate())));
		
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

		ImmunizationData immunizationData = patientInfo.addNewImmunizationData();
		
		
		for(Prevention prevention:preventions) {
			Vaccine vaccine = immunizationData.addNewVaccine();
			
			List<PreventionExt> exts = preventionExtDao.findByPreventionId(prevention.getId());
			vaccine.setDateReceived(new XmlCalendar(dateFormatter.format(prevention.getPreventionDate())));
			vaccine.setVaccineName(prevention.getPreventionType()); 
			/*
			immunizationData.setVaccineDIN(arg0);
			immunizationData.setDoseNum(arg0);
			immunizationData.setExpiryDate(arg0);
			immunizationData.setInitials(arg0);
			immunizationData.setInjectionSite(arg0);
			*/
			for(PreventionExt ext:exts) {
				if("lot".equals(ext.getkeyval())) {
					vaccine.setLotNumber(ext.getVal());
				} else if("comments".equals(ext.getkeyval())) {
					vaccine.setComments(ext.getVal());
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

		patientInfo.setChartNumber(String.valueOf(demographic.getDemographicNo()));


		if (StringUtils.filled(demographic.getHin())) patientInfo.setHealthCardNum(demographic.getHin());
		else patientInfo.setHealthCardNum("0");

		patientInfo.setHealthCardType(0);
		if (StringUtils.filled(demographic.getHcType())) {
			if (demographic.getHcType().equals("ON")) patientInfo.setHealthCardType(1);
			if (demographic.getHcType().equals("QC")) patientInfo.setHealthCardType(2);
		}

		if (StringUtils.filled(demographic.getAddress())) patientInfo.setResidentAddressLine1(demographic.getAddress());
		if (StringUtils.filled(demographic.getCity())) patientInfo.setResidentCity(demographic.getCity());
		if (StringUtils.filled(demographic.getProvince())) {
			String province = demographic.getProvince();
			if (province.equals("AB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_AB);
			else if (province.equals("BC")) patientInfo.setResidentCountryProvince(CountryProvince.CA_BC);
			else if (province.equals("MB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_MB);
			else if (province.equals("NB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NB);
			else if (province.equals("NL")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NL);
			else if (province.equals("NT")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NT);
			else if (province.equals("NS")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NS);
			else if (province.equals("NU")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NU);
			else if (province.equals("ON")) patientInfo.setResidentCountryProvince(CountryProvince.CA_ON);
			else if (province.equals("PE")) patientInfo.setResidentCountryProvince(CountryProvince.CA_PE);
			else if (province.equals("QC")) patientInfo.setResidentCountryProvince(CountryProvince.CA_QC);
			else if (province.equals("SK")) patientInfo.setResidentCountryProvince(CountryProvince.CA_SK);
			else if (province.equals("YT")) patientInfo.setResidentCountryProvince(CountryProvince.CA_YT);
			else if (province.startsWith("US")) patientInfo.setResidentCountryProvince(CountryProvince.USA);
			else patientInfo.setResidentCountryProvince(CountryProvince.UNKN);
		}
		if (StringUtils.filled(demographic.getPostal())) {
			patientInfo.setResidentPostalCode(demographic.getPostal().replace(" ", ""));
		}

		patientInfo.setOrganizationID(OscarProperties.getInstance().getProperty("born_orgcode"));

		if (useClinicInfoForOrganizationId) {
			ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
			ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();
			patientInfo.setOrganizationID(clinicInfo.getFacilityName());
		}
	}

	private boolean isAgeLessThan7y(PatientInfo patientInfo) {	
		LocalDate date1 = new LocalDate(LocalDate.fromCalendarFields(patientInfo.getDOB()));
        LocalDate date2 = new LocalDate(new java.util.Date());
        PeriodType monthDay = PeriodType.months();
        Period difference = new Period(date1, date2, monthDay);
        int months = difference.getMonths();
        
        if(months >= ((12*6)+6)) {
        	return false;
        }
        return true;
	}
}
