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
package org.oscarehr.common.dao;

//-----------------------------------------------------------------------------------------------------------------------
//*
//*
//* Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
//* This software is published under the GPL GNU General Public License.
//* This program is free software; you can redistribute it and/or
//* modify it under the terms of the GNU General Public License
//* as published by the Free Software Foundation; either version 2
//* of the License, or (at your option) any later version. *
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
//* along with this program; if not, write to the Free Software
//* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
//*
//* <OSCAR TEAM>
//* This software was written for the
//* Department of Family Medicine
//* McMaster University
//* Hamilton
//* Ontario, Canada
//*
//-----------------------------------------------------------------------------------------------------------------------

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.CtlDocumentPK;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicQueryFavourite;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.form.FrmLabReq07Record;


public class OntarioMDSpec4DataTest extends DaoTestFixtures {
	protected AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	protected Integer oscarProgramID;
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreAllTables();
	}

	Document getDocument(String doctype,String docdesc,String docxml,String docfilename,String doccreator,String responsible,String source,Integer program_id,
			Date updatedatetime,String  status,String contenttype,Integer public1,Date observationdate,String reviewer,Date reviewdatetime,Integer number_of_pages,Integer appointment_no){
		Document document = new Document();
		document.setDoctype(doctype);
		document.setDocdesc(docdesc);
		document.setDocxml(docxml);
		document.setDocfilename(docfilename);
		document.setDoccreator(doccreator);
		document.setObservationdate(observationdate);
		document.setUpdatedatetime(updatedatetime);
		document.setStatus(status.charAt(0));
		document.setContenttype(contenttype);
		document.setPublic1(public1.byteValue());
		document.setNumberofpages(number_of_pages);
		document.setResponsible(responsible);
		document.setProgramId(program_id);
		return document;
 }

 CtlDocument getCtlDocument(String module,Integer moduleId,Integer documentNo,String status){
  	CtlDocument ctlDocument = new CtlDocument();
  	ctlDocument.getId().setModuleId(moduleId);
  	ctlDocument.setStatus(status);
  	ctlDocument.setId(new CtlDocumentPK(documentNo,module));
  	return ctlDocument;
  }



	Dxresearch getDxResearch(String codingSystem, String code,String status,Date startDate,Date updateDate,Integer demographicNo){
		Dxresearch dxresearch = new Dxresearch();
		dxresearch.setCodingSystem(codingSystem);
		dxresearch.setDxresearchCode(code);
		dxresearch.setStatus(status.toCharArray()[0]);
		dxresearch.setStartDate(startDate);
		dxresearch.setUpdateDate(updateDate);
		dxresearch.setDemographicNo(demographicNo);
		return dxresearch;
	}


	ProgramProvider getProgramProvider(Integer oscarProgramID,Provider provider){
		ProgramProvider programProvider = new ProgramProvider();
		programProvider.setProgramId(new Long(""+oscarProgramID));
		programProvider.setProviderNo(provider.getProviderNo());
		programProvider.setRoleId(1L);
		return programProvider;
	}

	Admission getAdmission(Demographic demographic,Date admDate,Integer oscarProgramID){
		Admission admission = new Admission();
		admission.setProgramId(oscarProgramID);
		admission.setClient(demographic);
		admission.setClientId(demographic.getDemographicNo());
		admission.setProviderNo(demographic.getProviderNo());
		admission.setAdmissionDate(admDate);
		admission.setAdmissionStatus("current");
		admission.setTeamId(null);
		return admission;
	}

	Provider getProvider(String firstName,String lastName,String providerNo,String ohipNo,String providerType,String specialty,String sex){
		Provider provider = new Provider();
		provider.setFirstName(firstName);
		provider.setLastName(lastName);
		provider.setProviderNo(providerNo);
		provider.setOhipNo(ohipNo);
		provider.setSpecialty(specialty);
		provider.setProviderType(providerType);
		provider.setSex(sex);
		provider.setSignedConfidentiality(new Date());
		provider.setStatus("1");
		return provider;
	}

	Demographic getDemographic(String title,String lastName,String firstName, String hin,String ver,String yearOfBirth,String monthOfBirth,String dateOfBirth,String sex,String address,String city,String province,String postal,String phone,String patientStatus,String rosterStatus,String providerNo){
		Demographic demographic = new Demographic();
		demographic.setLastName(lastName);
		demographic.setFirstName(firstName);
		demographic.setHin(hin);
		demographic.setVer(ver);
		demographic.setYearOfBirth(yearOfBirth);
		demographic.setMonthOfBirth(monthOfBirth);
		demographic.setDateOfBirth(dateOfBirth);
		demographic.setSex(sex);
		demographic.setAddress(address);
		demographic.setCity(city);
		demographic.setProvince(province);
		demographic.setPostal(postal);
		demographic.setPhone(phone);
		demographic.setPatientStatus(patientStatus);
		demographic.setRosterStatus(rosterStatus);
		demographic.setProviderNo(providerNo);
		demographic.setHcType("ON");
		return demographic;
	}


	CaseManagementNote getCaseManagementNoteWithIssueSet(Date observationDate,Demographic demographic,String providerNo,String note,Integer oscarProgramID,Set<CaseManagementIssue> caseManagementIssue){
		CaseManagementNote cmn=new CaseManagementNote();
	    cmn.setUpdate_date(observationDate);
	    cmn.setObservation_date(observationDate);
	    cmn.setDemographic_no(""+demographic.getDemographicNo());
	    cmn.setProviderNo(providerNo);
	    cmn.setNote(note);
	    cmn.setSigned(true);
	    cmn.setSigning_provider_no(providerNo);
	    cmn.setProgram_no(""+oscarProgramID);
	    cmn.setReporter_caisi_role("1");
	    cmn.setReporter_program_team("0");
	    cmn.setPassword("NULL");
	    cmn.setLocked(false);
	    cmn.setHistory("note");
	    cmn.setPosition(0);
		cmn.setIssues(caseManagementIssue);
	    return cmn;


	}


	CaseManagementNote getCaseManagementNote(Date observationDate,Demographic demographic,String providerNo,String note,Integer oscarProgramID,CaseManagementIssue caseManagementIssue){
		CaseManagementNote cmn=new CaseManagementNote();
	    cmn.setUpdate_date(observationDate);
	    cmn.setObservation_date(observationDate);
	    cmn.setDemographic_no(""+demographic.getDemographicNo());
	    cmn.setProviderNo(providerNo);
	    cmn.setNote(note);
	    cmn.setSigned(true);
	    cmn.setSigning_provider_no(providerNo);
	    cmn.setProgram_no(""+oscarProgramID);
	    cmn.setReporter_caisi_role("1");
	    cmn.setReporter_program_team("0");
	    cmn.setPassword("NULL");
	    cmn.setLocked(false);
	    cmn.setHistory("note");
	    cmn.setPosition(0);
	    if (caseManagementIssue != null){
	    	Set<CaseManagementIssue> set = new HashSet();
	    	set.add(caseManagementIssue);
			cmn.setIssues(set);
	    }
	    return cmn;
	}



	CaseManagementIssue getCaseMangementIssue(Demographic demographic,Issue issue,Integer oscarProgramID,String type){

		CaseManagementIssue caseManagementIssue = new CaseManagementIssue();
		caseManagementIssue.setDemographic_no(demographic.getDemographicNo());
		caseManagementIssue.setIssue_id(issue.getId());
		caseManagementIssue.setProgram_id(oscarProgramID);
		caseManagementIssue.setType(type);
		return caseManagementIssue;
	}

	String getName(Demographic demographic){
		return demographic.getLastName()+", "+demographic.getFirstName();
	}

	Appointment getAppointment(Date apptDate, int startHour, int startMinute,int durationInMins,Date createdOn,String creator,Demographic demographic,String providerNo,String status){
		Appointment a = new Appointment();
		Calendar cal = Calendar.getInstance();
		cal.setTime(apptDate);
		a.setAppointmentDate(cal.getTime());
		cal.set(Calendar.HOUR_OF_DAY, startHour);
		cal.set(Calendar.MINUTE, startMinute);
		a.setStartTime(cal.getTime());
		cal.add(Calendar.MINUTE,durationInMins);
		a.setEndTime(cal.getTime());
		a.setCreateDateTime(createdOn);
		a.setCreator(creator);
		a.setDemographicNo(demographic.getDemographicNo());
		a.setProviderNo(providerNo);
		a.setStatus(status);
		a.setUpdateDateTime(createdOn);
		a.setName(getName(demographic));
		return a;
	}


	Drug getDrug(String providerNo,	Integer demographicId, Date rxDate,Date endDate,Date writtenDate,String brandName,int gcnSeqNo,	String customName,float takeMin,float takeMax,String freqCode,
		String duration,String durUnit,String quantity,	Integer repeat,	boolean noSubs,	boolean prn,String special,String special_instruction,String genericName,String atc,Integer scriptNo,String regionalIdentifier,
		String unit,String method,String route,String drugForm,	Date createDate,String dosage,boolean customInstructions,String unitName,Boolean longTerm,Boolean pastMed,	Boolean patientCompliance,String outsideProviderName,
		String outsideProviderOhip ,Boolean hideFromDrugProfile,   Boolean customNote){

	    Drug drug = new Drug();
		drug.setProviderNo(providerNo);
		drug.setDemographicId(demographicId);
		drug.setRxDate(rxDate);
		drug.setEndDate(endDate);
		drug.setWrittenDate(writtenDate);
		drug.setBrandName(brandName);
		drug.setGcnSeqNo(gcnSeqNo);
		drug.setCustomName(customName);
		drug.setTakeMin(takeMin);
		drug.setTakeMax(takeMax);
		drug.setFreqCode(freqCode);
		drug.setDuration(duration);
		drug.setDurUnit(durUnit);
		drug.setQuantity(quantity);
		drug.setRepeat(repeat);
		drug.setNoSubs(noSubs);
		drug.setPrn(prn);
		drug.setSpecial(special);
		drug.setSpecialInstruction(special_instruction);
		drug.setGenericName(genericName);
		drug.setAtc(atc);
		drug.setScriptNo(scriptNo);
		drug.setRegionalIdentifier(regionalIdentifier);
		drug.setUnit(unit);
		drug.setMethod(method);
		drug.setRoute(route);
		drug.setDrugForm(drugForm);
		drug.setCreateDate(createDate);
		drug.setDosage(dosage);
		drug.setCustomInstructions(customInstructions);
		drug.setUnitName(unitName);
		drug.setLongTerm(longTerm);
		drug.setPastMed(pastMed);
		drug.setPatientCompliance(patientCompliance);
		drug.setOutsideProviderName(outsideProviderName);
		drug.setOutsideProviderOhip(outsideProviderOhip);
		drug.setHideFromDrugProfile(hideFromDrugProfile);
		drug.setCustomNote(customNote);
		drug.setPosition(0);
		return drug;
	}



	Date getDate(Date referenceDate,int calField, int addingValue){
		Calendar cal = Calendar.getInstance();
		cal.setTime(referenceDate);
		cal.add(calField,addingValue);
		return cal.getTime();
	}

	static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date getDate(String dateStr){
		Date date = null;
	    try {
	       date = formatter.parse(dateStr);
	    } catch (ParseException e){ MiscUtils.getLogger().error("Error: ",e);}
	    return date   ;
	}

	public static Date getTime(String dateStr){
		Date date = null;
	    try {
	       date = timeFormat.parse(dateStr);
	    } catch (ParseException e){ MiscUtils.getLogger().error("Error: ",e);}
	    return date   ;
	}

	public static Date getDateAndTime(String dateStr){
		Date date = null;
	    try {
	       date = dateTimeFormat.parse(dateStr);
	    } catch (ParseException e){ MiscUtils.getLogger().error("Error: ",e);}
	    return date   ;
	}


	  BillingONCHeader1 getBilling(Integer headerId,String hin,String ver,String dob, String payProgram,String payee, String faciltyNum,Date admissionDate,String location,Integer demographicNo,
			  String providerNo,Integer appointmentNo,String demographicName,String sex,String province,Date billingDate,Date billingTime,String total,String paid,String statValue,
			  String comment,String providerOhipNo,String providerRmaNo,String apptProviderNo,String asstProviderNo,String creator){
		  BillingONCHeader1 billingONHeader1 = new BillingONCHeader1();
		  billingONHeader1.setHeaderId(headerId);
		  billingONHeader1.setTranscId("HE");
		  billingONHeader1.setRecId("H");
		  billingONHeader1.setHin(hin);
		  billingONHeader1.setVer(ver);
		  billingONHeader1.setDob(dob);
		  billingONHeader1.setPayProgram(payProgram);
		  billingONHeader1.setPayee(payee);
		  billingONHeader1.setRefNum("");
		  billingONHeader1.setFaciltyNum(faciltyNum);
		  billingONHeader1.setAdmissionDate(admissionDate);
		  billingONHeader1.setRefLabNum("");
		  billingONHeader1.setManReview("");
		  billingONHeader1.setLocation(location);
		  billingONHeader1.setDemographicNo(demographicNo);
		  billingONHeader1.setProviderNo(providerNo);
		  billingONHeader1.setAppointmentNo(appointmentNo);
		  billingONHeader1.setDemographicName(demographicName);
		  billingONHeader1.setSex(sex);
		  billingONHeader1.setProvince(province);
		  billingONHeader1.setBillingDate(billingDate);
		  billingONHeader1.setBillingTime(billingTime);
		  billingONHeader1.setTotal(new BigDecimal(total));
		  billingONHeader1.setPaid(new BigDecimal(paid));
		  billingONHeader1.setStatus(statValue);
		  billingONHeader1.setComment(comment);
		  billingONHeader1.setVisitType("00");
		  billingONHeader1.setProviderOhipNo(providerOhipNo);
		  billingONHeader1.setProviderRmaNo(providerRmaNo);
		  billingONHeader1.setApptProviderNo(apptProviderNo);
		  billingONHeader1.setAsstProviderNo(asstProviderNo);
		  billingONHeader1.setCreator(creator);

		  return billingONHeader1;
	 /*
         creator: 999998
      timestamp1: 2011-06-03 13:52:44
          clinic: NULL
	  */
	  }

		Properties getLabReqProperties(){
			Properties labreq = new Properties();
	        labreq.put("formEdited","2011-06-01 15:04:34");
	        labreq.put("b_glucose","1");
			labreq.put("b_glucose_random","0");
			labreq.put("b_glucose_fasting","1");
			labreq.put("b_hba1c","1");
			labreq.put("b_creatinine","1");
			labreq.put("b_uricAcid","0");
			labreq.put("b_sodium","0");
			labreq.put("b_potassium","0");
			labreq.put("b_chloride","0");
			labreq.put("b_ck","0");
			labreq.put("b_alt","0");
			labreq.put("b_alkPhosphatase","0");
			labreq.put("ohip","0");
			labreq.put("formCreated","2011-06-01");
			labreq.put("patientFirstName","June");
			labreq.put("b_timeNextDose2","");
			labreq.put("clinicCity","Hamilton");
			labreq.put("b_timeNextDose1","");
			labreq.put("b_timeLastDose2","");
			labreq.put("b_timeLastDose1","");
			labreq.put("provider_no","999998");
			labreq.put("m_vaginalRectal","0");
			labreq.put("m_vaginal","0");
			labreq.put("b_acRatioUrine","0");
			labreq.put("healthNumber","8888999904");
			labreq.put("patientCity","Toronto");
			labreq.put("b_patientsTelNo","");
			labreq.put("clinicianContactUrgent","");
			labreq.put("b_childsAgeDays","");
			labreq.put("b_tsh","0");
			labreq.put("b_timeCollected2","");
			labreq.put("b_timeCollected1","");
			labreq.put("b_therapeuticDrugMonitoring","0");
			labreq.put("i_rubella","0");
			labreq.put("m_cervical","0");
			labreq.put("patientAddress","456 Main Street");
			labreq.put("b_vitaminB12","0");
			labreq.put("m_blank","0");
			labreq.put("reqProvName","oscardoc, doctor");
			labreq.put("v_chronicHepatitis","0");
			labreq.put("version","ZE");
			labreq.put("oprn","");
			labreq.put("b_albumin","0");
			labreq.put("aci","");
			labreq.put("patientPC","M6P 4J4");
			labreq.put("b_urinalysis","0");
			labreq.put("h_cbc","0");
			labreq.put("form_class","0");
			labreq.put("sex","F");
			labreq.put("i_pregnancyTest","0");
			labreq.put("submit","0");
			labreq.put("v_acuteHepatitis","0");
			labreq.put("birthDate","1937-06-06");
			labreq.put("patientBirthMth","06");
			labreq.put("phoneNumber","416-555-6789");
			labreq.put("m_otherSwabsSource","");
			labreq.put("m_otherSwabsPus","0");
			labreq.put("clinicAddress","Hamilton");
			labreq.put("m_gcSource","");
			labreq.put("b_childsAgeHours","");
			labreq.put("m_throat","0");
			labreq.put("patientLastName","Elder");
			labreq.put("m_sputum","0");
			labreq.put("b_neonatalBilirubin","0");
			labreq.put("m_fecalOccultBlood","0");
			labreq.put("m_urine","0");
			labreq.put("m_blankText","");
			labreq.put("b_nameDrug2","");
			labreq.put("m_stoolOvaParasites","0");
			labreq.put("b_nameDrug1","");
			labreq.put("m_gc","0");
			labreq.put("i_prenatal","0");
			labreq.put("h_prothrombinTime","0");
			labreq.put("patientBirthYear","1937");
			labreq.put("m_chlamydiaSource","");
			labreq.put("i_repeatPrenatalAntibodies","0");
			labreq.put("b_ferritin","0");
			labreq.put("wcb","0");
			labreq.put("v_immuneStatus","0");
			labreq.put("m_stoolCulture","0");
			labreq.put("province","ON");
			labreq.put("m_woundSource","");
			labreq.put("patientBirthDay","06");
			labreq.put("demographic_no","6");
			labreq.put("i_mononucleosisScreen","0");
			labreq.put("b_lipidAssessment","0");
			labreq.put("m_wound","0");
			labreq.put("provName","Welby, Marcus");
			labreq.put("thirdParty","0");
			labreq.put("clinicName","McMaster Hospital");
			labreq.put("b_bilirubin","1");
			labreq.put("m_specimenCollectionTime","");
			//labreq.put("o_otherTests16: NULL
			//labreq.put("o_otherTests15: NULL
			//labreq.put("o_otherTests14: NULL
			//labreq.put("o_otherTests13: NULL
			//labreq.put("o_otherTests12: NULL
			//labreq.put("o_otherTests11: NULL
			labreq.put("o_otherTests10","");
			labreq.put("o_otherTests9","");
			labreq.put("o_otherTests8","");
			labreq.put("o_otherTests7","");
			labreq.put("o_otherTests6","");
			//labreq.put("o_specimenCollectionDate",": NULL
			labreq.put("o_otherTests5","");
			labreq.put("clinicPC","L0R 4K3");
			labreq.put("o_otherTests4","");
			labreq.put("o_otherTests3","");
			labreq.put("o_otherTests2","");
			labreq.put("b_cliniciansTelNo","");
			labreq.put("o_otherTests1","");
			labreq.put("patientName","Elder, June");
			labreq.put("b_dateSigned","2011-06-01");
			labreq.put("practitionerNo","0000-111112-00");
			labreq.put("m_chlamydia","0");
			labreq.put("v_immune_HepatitisC","0");
			labreq.put("v_immune_HepatitisB","0");
			labreq.put("v_immune_HepatitisA","0");
	        return labreq;
		}

		Prevention getPrevention(Date preventionDate,String type,Integer demographicNo,String providerNo,boolean ineligible,boolean refused){
			Prevention prevention = new Prevention();
	        prevention.setPreventionDate(preventionDate);
	        prevention.setPreventionType(type);
	        prevention.setDemographicId(demographicNo);
	        prevention.setProviderNo(providerNo);
	        prevention.setCreatorProviderNo(providerNo);
	        if(ineligible){
	        	prevention.setIneligible(true);
	        }
	        if(refused){
	        	prevention.setRefused(true);
	        }
	        return prevention;
		}

		Measurement getMeasurement(String type,Date dateEntered,Integer demographicNo,String dataField,String providerNo){
	    	Measurement measurement = new Measurement();
	    	measurement.setType(type);
	    	measurement.setCreateDate(dateEntered);
	    	measurement.setDateObserved(dateEntered);
	    	measurement.setDemographicId(demographicNo);
	    	measurement.setDataField(dataField);
	    	measurement.setProviderNo(providerNo);
	    	measurement.setMeasuringInstruction("");
	    	measurement.setComments("");
	    	measurement.setAppointmentNo(0);
	    	return measurement;
		}

		DemographicContact getDemographicContact(Integer demographicNo, String creator, String contactId,Date created,Integer type,String category,String role,String sdm,String ec){
			DemographicContact demographicContact = new DemographicContact();
	    	demographicContact.setDemographicNo(demographicNo);
	    	demographicContact.setContactId(contactId);
	    	demographicContact.setCreated(created);
	    	demographicContact.setUpdateDate(created);
	    	demographicContact.setType(type);
	    	demographicContact.setCategory(category);
	    	demographicContact.setRole(role);
	    	demographicContact.setCreator(creator);
	    	demographicContact.setSdm(sdm);
	    	demographicContact.setEc(ec);
	    	return demographicContact;
		}

		CaseManagementNoteExt getCaseManagementNoteExt(Long noteId,String keyVal,String value){
			CaseManagementNoteExt caseManagementNoteExt = new CaseManagementNoteExt();
			caseManagementNoteExt.setNoteId(noteId);
			caseManagementNoteExt.setKeyVal(keyVal);
			caseManagementNoteExt.setValue(value);
        	return caseManagementNoteExt;
		}

		SecUserRole getSecUserRole(String providerNo,String rolename,Boolean active){
			SecUserRole secuserrole = new SecUserRole();
			secuserrole.setProviderNo(providerNo);
			secuserrole.setRoleName(rolename);
			secuserrole.setActive(active);
			return secuserrole;

		}

		Allergy getAllergy(String demographic,Date entryDate,String description,Integer hiclseqno,Integer hicSeqno,Integer agcsp,Integer agccs,Integer typeCode,String reaction, String drugrefId,boolean archived,Date startDate,String severityOfReaction,String onsetOfReaction,String regionalIdentifier,String lifeStage){
	        Allergy allergy = new Allergy();
	        allergy.setDemographicNo(Integer.valueOf(demographic));
	        allergy.setEntryDate(entryDate);
	        allergy.setDescription(description);
	        allergy.setHiclSeqno(hiclseqno);
	        allergy.setHicSeqno(hicSeqno);
	        allergy.setAgcsp(agcsp);
	        allergy.setAgccs(agccs);
	        allergy.setTypeCode(typeCode);
	        allergy.setReaction(reaction);
	        allergy.setDrugrefId(drugrefId);
	        allergy.setArchived(archived);
	        allergy.setStartDate(startDate);
	        allergy.setSeverityOfReaction(severityOfReaction);
	        allergy.setOnsetOfReaction(onsetOfReaction);
	        allergy.setRegionalIdentifier(regionalIdentifier);
	        allergy.setLifeStage(lifeStage);
			return allergy;
		}

		String justYear(Date date){
			Format formatter = new SimpleDateFormat("yyyy");
			return formatter.format(date);
		}

		String justMonth(Date date){
			Format formatter = new SimpleDateFormat("MM");
			return formatter.format(date);
		}

		String justDay(Date date){
			Format formatter = new SimpleDateFormat("dd");
			return formatter.format(date);
		}


	@Test
	public void test() {
		setupOntarioMDSpec4Data();
		
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.set(Calendar.YEAR, -20);
		Date referenceDate = cal.getTime();
		
		List<Admission>admissions = admissionDao.getAdmissionsByProgramAndAdmittedDate(oscarProgramID, referenceDate, today);
		Assert.assertTrue("Admissions should not be empty", !admissions.isEmpty());
		
	}

	public void setupOntarioMDSpec4Data(){
		OscarAppointmentDao appointmentDao=(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		DxresearchDAO dxResearchDAO = (DxresearchDAO) SpringUtils.getBean("dxresearchDAO");		
		ProgramProviderDAO programProviderDAO = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		CaseManagementNoteDAO  caseManagementNoteDAO = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
		IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
		CaseManagementIssueDAO caseManagementIssueDAO = (CaseManagementIssueDAO) SpringUtils.getBean("caseManagementIssueDAO");
		CaseManagementNoteExtDAO caseManagementNoteExtDAO = (CaseManagementNoteExtDAO) SpringUtils.getBean("CaseManagementNoteExtDAO");
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		DemographicContactDao demographicContactDao = (DemographicContactDao) SpringUtils.getBean("demographicContactDao");
		CaseManagementNoteLinkDAO CaseManagementNoteLinkDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");
		DemographicQueryFavouritesDao demoQueryFavouriteDao = (DemographicQueryFavouritesDao) SpringUtils.getBean("demographicQueryFavouritesDao");

		ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
		PreventionDao preventionDao = (PreventionDao) SpringUtils.getBean("preventionDao");
		MeasurementDao measurementsDao = SpringUtils.getBean(MeasurementDao.class);
		SecurityDao securityDao = (SecurityDao) SpringUtils.getBean("securityDao");
		SecUserRoleDao secuserroleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
		AllergyDao allergyDao = (AllergyDao) SpringUtils.getBean("allergyDao");
		DocumentDao documentDao = (DocumentDao) SpringUtils.getBean("documentDao");
		CtlDocumentDao ctlDocumentDao = (CtlDocumentDao) SpringUtils.getBean("ctlDocumentDao");
		TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
		

		oscarProgramID = programDao.getProgramIdByProgramName("OSCAR");

		if(oscarProgramID == null){
			Program program = new Program();
			program.setFacilityId(1);
			program.setName("OSCAR");
			program.setEmergencyNumber("");
			program.setMaxAllowed(999999);
			program.setHoldingTank(false);
			program.setType("Bed");
			program.setProgramStatus("active");
			program.setAllowBatchAdmission(false);
			program.setAllowBatchDischarge(false);
			program.setHic(false);
			program.setExclusiveView("");
			program.setDefaultServiceRestrictionDays(0);
			program.setAddress("1 Anystreet");
			program.setPhone("555-555-5555");
			program.setFax("555-555-5556");
			program.setUrl("google.ca");
			program.setEmail("noreply@caisi.ca");
			program.setEmergencyNumber("555-555-5555");

			programDao.saveProgram(program);

			oscarProgramID = program.getId();
		}

		Issue medHistory = issueDao.findIssueByCode("MedHistory");
		Issue concerns = issueDao.findIssueByCode("Concerns");
		Issue reminders = issueDao.findIssueByCode("Reminders");
		Issue famHistory = issueDao.findIssueByCode("FamHistory");
		Issue riskFactors = issueDao.findIssueByCode("RiskFactors");
		Issue alzheimers = issueDao.findIssueByCode("3310");
		Issue parkinsons = issueDao.findIssueByCode("332");
		Issue typhoidFever = issueDao.findIssueByCode("0020");
		Issue cesarean = issueDao.findIssueByCode("6697");
		Issue acuteOtitis = issueDao.findIssueByCode("38022");
		Issue pneumonia = issueDao.findIssueByCode("486");
		Issue diabetes = issueDao.findIssueByCode("250");
		Issue corneal = issueDao.findIssueByCode("37110");
		Issue classicalMigraine = issueDao.findIssueByCode("3460");




		Calendar cal = Calendar.getInstance();

		Date referenceDate = cal.getTime();
		Date today = referenceDate;

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(referenceDate);
		cal2.add(Calendar.YEAR,-10);
		Date tenYearsAgo = cal2.getTime();
		cal2.add(Calendar.YEAR,-10);
		Date twentyYearsAgo = cal2.getTime();



		cal2 = Calendar.getInstance();
		cal2.setTime(referenceDate);
		cal2.add(Calendar.DAY_OF_YEAR, -1);
		cal2 = Calendar.getInstance();
		cal2.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = cal2.getTime();

		Date threeMonthsAgo = getDate(referenceDate,Calendar.MONTH, -3);
		Date eightWeeksAgo  = getDate(referenceDate,Calendar.WEEK_OF_YEAR,-8);
		Date sixWeeksAgo = getDate(referenceDate,Calendar.WEEK_OF_YEAR,-6);
		Date twoWeeksAgo = getDate(referenceDate,Calendar.WEEK_OF_YEAR,-2);
		Date twoMonthsAgo = getDate(referenceDate,Calendar.MONTH,-2);
		Date oneWeekAgo = getDate(referenceDate,Calendar.WEEK_OF_YEAR,-1);
		Date sixtyThreeYearsAgoPlusTwoWeeks = getDate(getDate(referenceDate,Calendar.YEAR,-63),Calendar.WEEK_OF_YEAR,-2);
		Date eightMonthsAgo = getDate(referenceDate,Calendar.MONTH,-8);
		Date fourMonthsAgo = getDate(referenceDate,Calendar.MONTH,-4);


		//Dr. Marcus Welby (DRW)  User and Physician, Provider Number=111112, Specialty=00
		Provider drw = getProvider("Marcus","Welby","111112","111112","doctor","00","M");

		//Dr. Livingstone (DRL)  User and Physician
		Provider drl = getProvider("Livingstone","drl","100",null,"doctor","00","M");

		//Dr. Kimble (DRK)  User and Physician
		Provider drk = getProvider("Kimble","drk","101",null,"doctor","00","M");

		//Dr. Trainee (DRT) User and Family Practice Resident and Trainee
		Provider drt = getProvider("Trainee","drt","102",null,"doctor","00","M");

		//Dr. Samson (DRS)  User and Physician
		Provider drs = getProvider("Samson","drs","103",null,"doctor","00","M");

		//Dr. David Dolittle (DRD)  physician
		Provider drd = getProvider("Dolittle","David","104",null,"doctor","00","M");

		//Dr. Donald Doless (DRX) physician
		Provider drx = getProvider("Doless","Donald","105",null,"doctor","00","M");

		//Dr. Adam Arquette (DRAA)  physician
		Provider draa =getProvider("Arquette","Adam","106",null,"doctor","00","M");

		//Dr. Brandon Brolin (DRBB)  physician
		Provider drbb =getProvider("Brolin","Brandon","107",null,"doctor","00","M");

		//Dr Christian Cruz (DRCC)  physician
		Provider drcc = getProvider("Cruz","Christian","108",null,"doctor","00","M");

		//Dr. Dan Dalton (DRDD)  physician
		Provider drdd = getProvider("Dalton","Dan","109",null,"doctor","00","M");

		//Julie Care (JC)  user
		Provider jc = getProvider("Care","Julie","110",null,"doctor","00","F");

		//Nancy Nurse (NN)  user
		Provider nn = getProvider("Nurse","Nancy","111",null,"doctor","00","F");

		//Sally Scheduler (SS)  user
		Provider ss =getProvider("Scheduler","Sally","112",null,"doctor","00","F");

		//Lisa Greeting (LG)  user
		Provider lg =getProvider( "Greeting","Lisa","113",null,"doctor","00","F");


		providerDao.saveProvider(drw);
		MiscUtils.getLogger().info("WELBY PROVIDER NO " + drw.getProviderNo());
		providerDao.saveProvider(drl);
		providerDao.saveProvider(drk);
		providerDao.saveProvider(drt);
		providerDao.saveProvider(drs);
		providerDao.saveProvider(drd);
		providerDao.saveProvider(drx);
		providerDao.saveProvider(draa);
		providerDao.saveProvider(drbb);
		providerDao.saveProvider(drcc);
		providerDao.saveProvider(drdd);

		providerDao.saveProvider(jc);
		providerDao.saveProvider(nn);
		providerDao.saveProvider(ss);
		providerDao.saveProvider(lg);


		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drw));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drl));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drk));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drt));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drs));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drd));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drx));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,draa));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drbb));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drcc));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,drdd));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,jc));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,nn));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,ss));
		programProviderDAO.saveProgramProvider(getProgramProvider(oscarProgramID,lg));

		ProgramProvider programProvider = new ProgramProvider();
		programProvider.setProgramId(new Long(""+oscarProgramID));
		programProvider.setProviderNo("999998");
		programProvider.setRoleId(1L);

		programProviderDAO.saveProgramProvider(programProvider);


		Integer BRemotelockset = 1;
		Integer BLocallockset = 1;
		Date dateExpiredate = null;
		Integer BExpireset = 0;
		//DRW
		securityDao.persist(new Security("drw", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", drw.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(drw.getProviderNo(),"doctor",true));
		//DRL
		securityDao.persist(new Security("drl", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", drl.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(drl.getProviderNo(),"doctor",true));
		//DRK
		securityDao.persist(new Security("drk", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", drk.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(drk.getProviderNo(),"doctor",true));
		//DRT
		securityDao.persist(new Security("drt", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", drt.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(drt.getProviderNo(),"doctor",true));
		//DRS
		securityDao.persist(new Security("drs", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", drs.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(drs.getProviderNo(),"doctor",true));
		//jc
		securityDao.persist(new Security("jc", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", jc.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(jc.getProviderNo(),"doctor",true));
		//nn
		securityDao.persist(new Security("nn", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", nn.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(nn.getProviderNo(),"doctor",true));
		//ss
		securityDao.persist(new Security("ss", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", ss.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(ss.getProviderNo(),"receptionist",true));
		//lg
		securityDao.persist(new Security("lg", "-51-282443-97-5-9410489-60-1021-45-127-12435464-32", lg.getProviderNo(),"1117",BRemotelockset, BLocallockset,  dateExpiredate,  BExpireset, Boolean.FALSE,1));
		secuserroleDao.save(getSecUserRole(lg.getProviderNo(),"receptionist",true));

		MiscUtils.getLogger().info("Adding Providers");
		String hin= null, ver= null, address= null, city= null, province= null, postal= null, phone= null;


	    //PATIENT SETS

		//PAP
		DemographicQueryFavourite demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("34");
		demographicQueryFavourite.setEndYear("71");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("1");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRLPAPS");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);

		//MAM
		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("49");
		demographicQueryFavourite.setEndYear("71");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("1");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRLMAM");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);

		//FOBT
		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("49");
		demographicQueryFavourite.setEndYear("75");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("0");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRLFOBT");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);

		//Plus 65
		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("2");
		demographicQueryFavourite.setStartYear("64");
		demographicQueryFavourite.setEndYear("");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("0");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRLplus65");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);

		//KIDS
		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("18m");
		demographicQueryFavourite.setEndYear("30m");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("0");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRL18-30m letters");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);

/*  I dont think these are needed
		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("18m");
		demographicQueryFavourite.setEndYear("3");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("0");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRL Child Imm");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);


		demographicQueryFavourite = new DemographicQueryFavourite();
		demographicQueryFavourite.setSelects("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"demographic_no\"/></root>");
		demographicQueryFavourite.setAge("4");
		demographicQueryFavourite.setStartYear("18m");
		demographicQueryFavourite.setEndYear("42m");
		demographicQueryFavourite.setFirstName("");
		demographicQueryFavourite.setLastName("");
		demographicQueryFavourite.setRosterStatus("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\"RO\"/></root>");
		demographicQueryFavourite.setSex("0");
		demographicQueryFavourite.setProviderNo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item value=\""+drl.getProviderNo()+"\"/></root>");
		demographicQueryFavourite.setPatientStatus("");
		demographicQueryFavourite.setQueryName("DRL Child 30t42");
		demographicQueryFavourite.setArchived("1");

		demoQueryFavouriteDao.persist(demographicQueryFavourite);
	*/

		//Patients for DRW
		/*Patient1: Mr. Eric Idle, OHN:1123581314, Version Code: AB;DOB: 31/May/1958; Sex M: No activity for 10 years.Diagnosed with Type2 Diabetes.Adverse Reaction: Penicillin*/
		Demographic ericIdle = getDemographic("Mr","Idle", "Eric",  "1123581314", "AB", "1958", "05", "31", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(ericIdle);
		MiscUtils.getLogger().info("Saving to admission");
		admissionDao.saveAdmission(getAdmission(ericIdle,referenceDate, oscarProgramID));

		MiscUtils.getLogger().info("Adding Eric Idle");
		//Add icd9:250 to trigger the dm flowsheet
		dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,ericIdle.getDemographicNo()));
		//Add note for Type2 Diabetes

		CaseManagementIssue caseManagementIssue = getCaseMangementIssue(ericIdle,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssue);

		CaseManagementNote cmn=getCaseManagementNote(tenYearsAgo,ericIdle,drw.getProviderNo(),"Type 2 Diabetes",oscarProgramID, caseManagementIssue);
        caseManagementNoteDAO.saveNote(cmn);

        Allergy allergy = getAllergy(""+ericIdle.getDemographicNo(),tenYearsAgo,"PENICILLINS",0,0,0,0,10,"","42063",false,null,"1","1",null,null);
        allergyDao.persist(allergy);










		/*Patient2: Ms. Erica Idle, OHN:  2468101213, Version Code: AB;DOB 05/Aug/1970; Sex F; No activity for 10 years.*/
		Demographic ericaIdle = getDemographic("Ms","Idle", "Erica",  "2468101213", "AB", "1970", "08", "05", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(ericaIdle);
		admissionDao.saveAdmission(getAdmission(ericaIdle,referenceDate, oscarProgramID));

		MiscUtils.getLogger().info("Adding Erica Idle");


		/*Patient3: Ms. April Apricot, 		 OHN: 9754321017, Version Code:  ZA; DOB 5/Aug/1960; Sex F;		 Diagnosed: Type2 Diabetes.		*/
		Demographic aprilApricot = getDemographic("Ms","Apricot", "April",  "9754321017", "ZA", "1960", "08", "05", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(aprilApricot);
		admissionDao.saveAdmission(getAdmission(aprilApricot,referenceDate, oscarProgramID));
		dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,aprilApricot.getDemographicNo()));

		CaseManagementIssue caseManagementIssueAprilApricotDM = getCaseMangementIssue(aprilApricot,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueAprilApricotDM);

        caseManagementNoteDAO.saveNote(getCaseManagementNote(tenYearsAgo,aprilApricot,drw.getProviderNo(),"Type 2 Diabetes",oscarProgramID, caseManagementIssueAprilApricotDM));





		/*Patient4: Ms. Jeremy Doe,OHN: none, Version Code: none,DOB: 04/Apr/1940,Sex: M,Address Type: Home, Street Address : 123 Maple Street,  Municipality: Toronto, Province/State: Ontario, Country: Canada, Postal Code: M5R 2J3,Home Telephone: 416-767-0011
		 Diagnosed: Alzheimers, age of onset 63.Family History: Mother diagnosed with Alzheimers, age of onset 70.Validation Scenarios v4.0
		*/
		Demographic jeremyDoe = getDemographic("Ms","Doe", "Jeremy",  hin, ver, "1940", "04", "04", "M", "123 Maple Street", "Toronto", "Ontario", "M5R 2J3", "416-767-0011", "AC", "RO", drw.getProviderNo());
		demographicDao.save(jeremyDoe);
		admissionDao.saveAdmission(getAdmission(jeremyDoe,referenceDate, oscarProgramID));

		CaseManagementIssue caseManagementIssueJeremyDoeDM = getCaseMangementIssue(jeremyDoe,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJeremyDoeDM);

        CaseManagementNote jeremyDoeAlzeimersNote = getCaseManagementNote(tenYearsAgo,jeremyDoe,drw.getProviderNo(),"Alzheimers at age 63",oscarProgramID, caseManagementIssueJeremyDoeDM);
        caseManagementNoteDAO.saveNote(jeremyDoeAlzeimersNote);

        //TODO: follow up age of onset isn't in ongoing concerns



        CaseManagementIssue caseManagementIssueJeremyDoeFamHxAlzh = getCaseMangementIssue(jeremyDoe,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJeremyDoeFamHxAlzh);
        CaseManagementNote jeremyDoeFamHxAlzeimersNote = getCaseManagementNote(tenYearsAgo,jeremyDoe,drw.getProviderNo(),"Mother diagnosed with Alzheimer at age 70",oscarProgramID, caseManagementIssueJeremyDoeFamHxAlzh);
        caseManagementNoteDAO.saveNote(jeremyDoeFamHxAlzeimersNote);

        CaseManagementNoteExt  jeremyDoeFamilyHistoryAlzeimersNoteExt = new CaseManagementNoteExt();
        jeremyDoeFamilyHistoryAlzeimersNoteExt.setNoteId(caseManagementIssueJeremyDoeFamHxAlzh.getId());
        jeremyDoeFamilyHistoryAlzeimersNoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        jeremyDoeFamilyHistoryAlzeimersNoteExt.setValue("70");

        caseManagementNoteExtDAO.save(jeremyDoeFamilyHistoryAlzeimersNoteExt);



		/*Patient5: Mrs. Jane Gray;OHN: none, Version Code: none, DOB: 03/Mar/40; Sex: F.		 Records include two notes: Heavy smoker until 1995 and BP hard to measure.*/
		Demographic janeGray = getDemographic("Mrs","Gray", "Jane",  hin, ver, "1940", "03", "03", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(janeGray);
		admissionDao.saveAdmission(getAdmission(janeGray,referenceDate, oscarProgramID));

		CaseManagementNote janeGrayNote1 = getCaseManagementNote(tenYearsAgo,janeGray,drw.getProviderNo(),"Heavy smoker until 1995",oscarProgramID,null);
        caseManagementNoteDAO.saveNote(janeGrayNote1);

		CaseManagementNote janeGrayNote2 = getCaseManagementNote(tenYearsAgo,janeGray,drw.getProviderNo(),"BP hard to measure",oscarProgramID,null);
        caseManagementNoteDAO.saveNote(janeGrayNote2);

		/*Patient6: Mrs. June Elder;OHN: 8888999904, Version Code: ZE,DOB 06/Jun/37; Sex F,Address Type: Home, Street Address: 456 Main Street,  Municipality: Toronto, Province/State: Ontario, Postal Code: M6P 4J4,Home Telephone: 416-555-6789
		 Diagnosed: Type-2 Diabetes, Start Date =1980
		            Allergic Rhinitis, Start Date =1990
		            Angina pectoris
		 Allergies: penicillin causing mild rash
		 Adverse Reaction (Intolerance): celebrex intolerance , severity=high;
		 Past Medical and Surgical History: none
		 Family History: Mother had Alzheimers, onset age 70.
		 Risk Factor: History of heavy alcohol consumption.
		 Medical Alerts and Special Needs: none
		 Overdue Targeted Health Maintenance Activity: Influenza vaccination
		 Overdue Preventive Care: Overdue for annual physical
		 Overdue Referral: Geriatrician
		 Outstanding Lab Request:  one GFR, one Bilirubin, one Glucose Fasting and one HbA1C  (see Lab Reports / Lab Results)
		 Scanned Lab Results: none
		 Imported (attached) Text Documents: include any text file
		 Imported (attached) Scanned Documents: include a jpeg file
		*/
		Demographic juneElder = getDemographic("Mrs","Elder", "June",  "8888999904", "ZE", "1937", "06", "06", "F", "456 Main Street", "Toronto", "Ontario", "M6P 4J4", "416-555-6789", "AC", "RO", drw.getProviderNo());
		demographicDao.save(juneElder);
		admissionDao.saveAdmission(getAdmission(juneElder,referenceDate, oscarProgramID));


		//DM
		dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,juneElder.getDemographicNo()));
		CaseManagementIssue caseManagementIssueJuneElderDM = getCaseMangementIssue(juneElder,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJuneElderDM);
		CaseManagementNote juneElderDMNote =getCaseManagementNote(tenYearsAgo,juneElder,drw.getProviderNo(),"Type 2 Diabetes",oscarProgramID, caseManagementIssueJuneElderDM);
		caseManagementNoteDAO.saveNote(juneElderDMNote);


        CaseManagementNoteExt  juneElderOngoingConcerndm2NoteExt = new CaseManagementNoteExt();
        juneElderOngoingConcerndm2NoteExt.setNoteId(juneElderDMNote.getId());
        juneElderOngoingConcerndm2NoteExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        juneElderOngoingConcerndm2NoteExt.setDateValue("1980-01-01");
        caseManagementNoteExtDAO.save(juneElderOngoingConcerndm2NoteExt);



        //Anging pectoris
        dxResearchDAO.save(getDxResearch("icd9","413","A",tenYearsAgo,tenYearsAgo,juneElder.getDemographicNo()));
		CaseManagementIssue caseManagementIssueJuneElderAngingPectoris = getCaseMangementIssue(juneElder,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJuneElderAngingPectoris);
        CaseManagementNote juneElderAngingPectorisNote =getCaseManagementNote(tenYearsAgo,juneElder,drw.getProviderNo(),"Anging pectoris",oscarProgramID, caseManagementIssueJuneElderAngingPectoris);
        caseManagementNoteDAO.saveNote(juneElderAngingPectorisNote);

        CaseManagementNoteExt  juneElderOngoingConcernAngingPectorisNoteExt = new CaseManagementNoteExt();
        juneElderOngoingConcernAngingPectorisNoteExt.setNoteId(juneElderAngingPectorisNote.getId());
        juneElderOngoingConcernAngingPectorisNoteExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        juneElderOngoingConcernAngingPectorisNoteExt.setDateValue("1990-01-01");
        caseManagementNoteExtDAO.save(juneElderOngoingConcernAngingPectorisNoteExt);

        //Allergic Rhinitis
        dxResearchDAO.save(getDxResearch("icd9","477","A",tenYearsAgo,tenYearsAgo,juneElder.getDemographicNo()));
		CaseManagementIssue caseManagementIssueJuneElderAllergicRhinitis = getCaseMangementIssue(juneElder,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJuneElderAllergicRhinitis);
        caseManagementNoteDAO.saveNote(getCaseManagementNote(tenYearsAgo,juneElder,drw.getProviderNo(),"Allergic Rhinitis",oscarProgramID, caseManagementIssueJuneElderAllergicRhinitis));

		//ALLERGIES
        allergy = getAllergy(""+juneElder.getDemographicNo(),tenYearsAgo,"PENICILLINS",0,0,0,0,10,"Causing mild rash","42063",false,null,"1","1",null,null);
        allergyDao.persist(allergy);


        //ADVERSE REACTION
        allergy = getAllergy(""+juneElder.getDemographicNo(),tenYearsAgo,"CELEBREX 100MG",0,0,0,0,13,"celebrex intolerance","8879",false,null,"3","1","02239941",null);
        allergyDao.persist(allergy);

        allergy = getAllergy(""+juneElder.getDemographicNo(),tenYearsAgo,"CELEBREX 200MG",0,0,0,0,13,"celebrex intolerance","8878",false,null,"3","1","02239942",null);
        allergyDao.persist(allergy);


        //Family History: Mother had Alzheimers, onset age 70.
        CaseManagementIssue caseManagementIssueJuneElderFamHxAlzh = getCaseMangementIssue(juneElder,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJuneElderFamHxAlzh);
        CaseManagementNote juneElderFamHxAlzeimersNote = getCaseManagementNote(tenYearsAgo,juneElder,drw.getProviderNo(),"Mother diagnosed with Alzheimer at age 70",oscarProgramID, caseManagementIssueJuneElderFamHxAlzh);
        caseManagementNoteDAO.saveNote(juneElderFamHxAlzeimersNote);

        CaseManagementNoteExt  juneElderFamilyHistoryAlzeimersNoteExt = new CaseManagementNoteExt();
        juneElderFamilyHistoryAlzeimersNoteExt.setNoteId(caseManagementIssueJeremyDoeFamHxAlzh.getId());
        juneElderFamilyHistoryAlzeimersNoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        juneElderFamilyHistoryAlzeimersNoteExt.setValue("70");

        caseManagementNoteExtDAO.save(juneElderFamilyHistoryAlzeimersNoteExt);

        //Risk Factor: History of heavy alcohol consumption.
        CaseManagementIssue caseManagementIssueJuneElderRskFact = getCaseMangementIssue(juneElder,riskFactors,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueJuneElderRskFact);
        CaseManagementNote juneElderRskFactNote = getCaseManagementNote(tenYearsAgo,juneElder,drw.getProviderNo(),"History of heavy alcohol consumption",oscarProgramID, caseManagementIssueJuneElderRskFact);
        caseManagementNoteDAO.saveNote(juneElderRskFactNote);


        //Overdue Targeted Health Maintenance Activity: Influenza vaccination
        Prevention juneElderPrevention = new Prevention();
        juneElderPrevention.setPreventionDate(twentyYearsAgo);
        juneElderPrevention.setPreventionType("Td");
        juneElderPrevention.setDemographicId(juneElder.getDemographicNo());
        juneElderPrevention.setProviderNo(drw.getProviderNo());
        juneElderPrevention.setCreatorProviderNo(drw.getProviderNo());

        preventionDao.persist(juneElderPrevention);

        //Overdue Preventive Care: Overdue for annual physical
        Tickler tickler = new Tickler();
        tickler.setDemographicNo(juneElder.getDemographicNo());
        tickler.setCreator(drw.getProviderNo());
        tickler.setAssignee(drw);
        tickler.setMessage("Needs Annual Physical");
        tickler.setProgramId(oscarProgramID);
        tickler.setServiceDate(lastWeek);
        tickler.setUpdateDate(lastWeek);
        ticklerManager.addTickler(getLoggedInInfo(),tickler);

        //TODO:  Overdue Referral: Geriatrician

        //<%!  %>Outstanding Lab Request:  one GFR, one Bilirubin, one Glucose Fasting and one HbA1C  (see Lab Reports / Lab Results)
        FrmLabReq07Record labReqDao = new FrmLabReq07Record();
        try{
        	labReqDao.saveFormRecord(getLabReqProperties()); //:TODO Need to pass changing data to getLabReqProperties
        }catch(Exception juneElderLabReq){
        	MiscUtils.getLogger().error("Error",juneElderLabReq);
        }

        Document document = getDocument("lab","Example text document","","exampleDoc.txt",drw.getProviderNo(),drw.getProviderNo(),null,-1,tenYearsAgo,"A","text/plain",0,tenYearsAgo,null,null,0,0);
        documentDao.persist(document);

        CtlDocument cltDocument =  getCtlDocument("demographic",juneElder.getDemographicNo(),Integer.parseInt(""+document.getId()),"A");
        MiscUtils.getLogger().info(" ctldoc "+cltDocument.toString());
        ctlDocumentDao.persist(cltDocument);

        document = getDocument("lab","Example text JPG","","exampleJPG.jpg",drw.getProviderNo(),drw.getProviderNo(),null,-1,tenYearsAgo,"A","image/jpeg",0,tenYearsAgo,null,null,0,0);
        documentDao.persist(document);

        cltDocument =  getCtlDocument("demographic",juneElder.getDemographicNo(),Integer.parseInt(""+document.getId()),"A");
        ctlDocumentDao.persist(cltDocument);


        //Aug 12, 2005 Aug 12, 2005 Aug 12, 2005     Glyburide 1 2.5 mg qd 10 10 tab oral DRW
        Date aug122005 = getDate("2005-08-12");
        Appointment app = getAppointment(aug122005, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
        appointmentDao.persist(app);
        
    	CaseManagementNote juneElderAugNote =getCaseManagementNote(aug122005,juneElder,drw.getProviderNo(),"Rx Glyburide",oscarProgramID, null);
    	juneElderAugNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElderAugNote);
		Integer scriptNo = 0;
		Drug drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), aug122005,getDate(aug122005,Calendar.DAY_OF_YEAR,+10),aug122005,"GLYBURIDE 2.5MG",5562,null,1,1,"QD","10","D","10",0,false,false,"GLYBURIDE 2.5MG Take 1 TAB QD PO for 10 days","","GLYBURIDE 2.5 MG",
				"A10BB01",scriptNo,"02350459","tab","Take","PO","TABLET",aug122005,"2.5 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);



        //Oct 22, 2005 Oct 22, 2005 Oct 22, 2005 Glyburide 2 2.5 mg bid 80 20 2 tab oral DRW
		Date oct222005 = getDate("2005-10-22");
        app = getAppointment(oct222005, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	CaseManagementNote juneElderOctNote =getCaseManagementNote(oct222005,juneElder,drw.getProviderNo(),"Rx Glyburide",oscarProgramID, null);
    	juneElderOctNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElderOctNote);
		scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), oct222005,getDate(oct222005,Calendar.DAY_OF_YEAR,+60),oct222005,"GLYBURIDE 2.5MG",5562,null,2,2,"BID","20","D","80",2,false,false,"GLYBURIDE 2.5MG Take 1 TAB QD PO for 10 days","","GLYBURIDE 2.5 MG",
				"A10BB01",scriptNo,"02350459","tab","Take","PO","TABLET",aug122005,"2.5 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);


		//Jan 6, 2006 Jan 6 2006 Jan 6, 2006 Glyburide 2 5 mg qid 160 20 3 tab oral DRW
		Date jan62006 = getDate("2006-01-06");
        app = getAppointment(jan62006, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	CaseManagementNote juneElderJanNote =getCaseManagementNote(jan62006,juneElder,drw.getProviderNo(),"Rx Glyburide",oscarProgramID, null);
    	juneElderJanNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElderJanNote);
		scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), jan62006,getDate(jan62006,Calendar.DAY_OF_YEAR,+80),jan62006,"GLYBURIDE 5MG",5561,null,2,2,"QD","20","D","160",3,false,false,"GLYBURIDE 5MG Take 2 TAB QID PO for 20 days Qty:160 Repeats:3","","GLYBURIDE 2.5 MG",
				"A10BB01",scriptNo,"02350467","tab","Take","PO","TABLET",jan62006,"5.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);


		//May 21, 2007 May 21, 2007 May 21, 2007 Glyburide 2 5 mg qid 240 30 3 tab oral DRW

		Date may212006 = getDate("2006-05-21");
        app = getAppointment(may212006, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	CaseManagementNote juneElderMayNote =getCaseManagementNote(may212006,juneElder,drw.getProviderNo(),"Rx Glyburide",oscarProgramID, null);
    	juneElderJanNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElderMayNote);
		scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), may212006,getDate(may212006,Calendar.DAY_OF_YEAR,+120),may212006,"GLYBURIDE 5MG",5561,null,2,2,"QD","10","D","240",3,false,false,"GLYBURIDE 5MG Take 2 TAB QID PO for 30 days Qty:240 Repeats:3","","GLYBURIDE 2.5 MG",
				"A10BB01",scriptNo,"02350467","tab","Take","PO","TABLET",may212006,"5.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);


		//EP** Feb 18, 2008 Feb 18, 2008 Diabeta 1 2.5 mg tid 30 10 tab oral DRB
		Date feb182008 = getDate("2008-02-18");
        scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), feb182008,getDate(feb182008,Calendar.DAY_OF_YEAR,+10),feb182008,"DIABETA 2.5MG",8531,null,1,1,"TID","10","D","30",0,false,false,"DIABETA 2.5MG Take 1 TAB TID PO for 10 days Qty:30 Repeats:0","","GLYBURIDE",
				"A10BB01",scriptNo,"02224550","tab","Take","PO","TABLET",feb182008,"5.0 MG",false,"",false,false,false,"Dr.Bill Bauman","",false,false);
		drugDao.addNewDrug(drug);


		//EP** Mar 2, 2008 Mar 2, 2008 Diabeta 2 5 mg qid 80 10 2 tab oral DRB
		Date mar022008 = getDate("2008-03-02");
        scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), mar022008,getDate(mar022008,Calendar.DAY_OF_YEAR,+30),mar022008,"DIABETA 5MG",8531,null,2,2,"QID","10","D","80",2,false,false,"DIABETA 5MG Take 2 TAB QID PO for 10 days Qty:80 Repeats:2","","GLYBURIDE",
				"A10BB01",scriptNo,"02224569","tab","Take","PO","TABLET",mar022008,"5.0 MG",false,"",false,false,false,"Dr.Bill Bauman","",false,false);
		drugDao.addNewDrug(drug);




		//Mar 23, 2008 Mar 23, 2008 Mar 23, 2008 Glyburide 2 5 mg qid 240 30 tab oral DRW

		Date mar232008 = getDate("2008-03-23");
        app = getAppointment(mar232008, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	CaseManagementNote juneElderMarNote =getCaseManagementNote(mar232008,juneElder,drw.getProviderNo(),"Rx Glyburide",oscarProgramID, null);
    	juneElderJanNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElderMarNote);
		scriptNo = 0;
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), mar232008,getDate(mar232008,Calendar.DAY_OF_YEAR,+10),mar232008,"GLYBURIDE 5MG",5561,null,1,1,"QD","10","D","240",0,false,false,"GLYBURIDE 5MG Take 2 TAB QID PO for 30 days Qty:240 Repeats:0","","GLYBURIDE 2.5 MG",
				"A10BB01",scriptNo,"02350467","tab","Take","PO","TABLET",jan62006,"5.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);

		//Apr 27, 2008 Apr 27, 2008 Apr 27, 2008 PMS-Amoxicillin 10 ml / 2 tsp 125 mg / 5 ml tid 300 ml 10 liquid oral DRW
        //ATC:J01CA04   DIN:02230245   GSQNo:10043 GN:AMOXICILLIN (AMOXICILLIN TRIHYDRATE)




		app = getAppointment(threeMonthsAgo, 10, 15,15,tenYearsAgo,drw.getProviderNo(),juneElder,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	CaseManagementNote juneElder3MonthsAgoNote =getCaseManagementNote(mar232008,juneElder,drw.getProviderNo(),"Nardil,Celebrex,Saw Palmetto,Caduet",oscarProgramID, null);
    	juneElderJanNote.setAppointmentNo(app.getId());
		caseManagementNoteDAO.saveNote(juneElder3MonthsAgoNote);
		scriptNo = 0;

		//3 mths ago* 3 mths ago* 3 mths ago* Nardil 1 15 mg bid 20 10 tab oral DRW
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), threeMonthsAgo,getDate(threeMonthsAgo,Calendar.DAY_OF_YEAR,+10),threeMonthsAgo,"NARDIL 15MG",11870,null,1,1,"BID","10","D","20",0,false,false,"NARDIL 15MG Take 1 TAB BID PO for 10 days Qty:20 Repeats:0","","PHENELZINE (PHENELZINE SULFATE)",
				"N06AF03",scriptNo,"00476552","tab","Take","PO","TABLET",jan62006,"15.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);

		//3 mths ago* 3 mths ago* 3 mths ago* Celebrex 100mg 2 100 mg bid 40 10 tab oral DRW
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), threeMonthsAgo,getDate(threeMonthsAgo,Calendar.DAY_OF_YEAR,+10),threeMonthsAgo,"CELEBREX 100MG",8879,null,1,1,"BID","10","D","20",0,false,false,"CELEBREX 100MG Take 2 TAB BID PO for 10 days Qty:40 Repeats:0","","CELECOXIB",
				"M01AH01",scriptNo,"02239941","tab","Take","PO","TABLET",jan62006,"15.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);


		//3 mths ago* 3 mths ago* 3 mths ago* Saw Palmetto 1 500 mg bid 80 40 tab oral DRW
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), threeMonthsAgo,getDate(threeMonthsAgo,Calendar.DAY_OF_YEAR,+40),threeMonthsAgo,"SAW PALMETTO 500MG",34787,null,1,1,"BID","40","D","80",0,false,false,"SAW PALMETTO 500MG Take 2 TAB BID PO for 40 days Qty:80 Repeats:0","","SAW PALMETTO",
				"",scriptNo,"02243566","tab","Take","PO","TABLET",jan62006,"500.0 MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);
		//3 mths ago* 3 mths ago* 3 mths ago* Caduet 1 od 10 tab oral DRW 02273306
		drug  = getDrug(drw.getProviderNo(),	juneElder.getDemographicNo(), threeMonthsAgo,getDate(threeMonthsAgo,Calendar.DAY_OF_YEAR,+10),threeMonthsAgo,"CADUET",34787,null,1,1,"OD","10","D","",0,false,false,"CADUET Take 1 TAB OD PO for 10 days","","AMLODIPINE (AMLODIPINE BESYLATE)",
				"C10BX03",scriptNo,"02273306","tab","Take","PO","TABLET",jan62006,"40/10MG",false,"",false,false,false,"","",false,false);
		drugDao.addNewDrug(drug);

		//PID Patient OHN VC DOB Sex Zyban-Start-Date service-date code/units/dx type/status Appointments

		//7 Anne Barber 5333333333 ZA 12/Dec/1970 F - 11/Nov/2008 8 wks ago A003A/ 1/ 006 HCP/Not submitted
		Demographic anneBarber = getDemographic(null,"Barber", "Anne",  "5333333333", "ZA", "1970", "12", "12", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(anneBarber);
		admissionDao.saveAdmission(getAdmission(anneBarber,referenceDate, oscarProgramID));
		Date zybanDate = getDate("2008-11-11");
    	drug  = getDrug(drw.getProviderNo(),	anneBarber.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//8 Ben Chase 4444444444 ZB 08/Aug/1935 M 09/Sep/2006 1 mth ago No billing entered  APPT 1 mth ago
		Demographic benChase = getDemographic(null,"Chase", "Anne",  "4444444444", "ZB", "1935", "08", "08", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(benChase);
		admissionDao.saveAdmission(getAdmission(benChase,referenceDate, oscarProgramID));
		app = getAppointment(eightWeeksAgo, 10, 15,15,eightWeeksAgo,drw.getProviderNo(),benChase,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	zybanDate = getDate("2006-09-09");
    	drug  = getDrug(drw.getProviderNo(),	benChase.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);

		//9 Catherine Deville 5555555555 ZC 07/July/1947 F 02/Feb/2008 6 wks ago A003A/ 1 /787 HCP/Submitted not paid   APPT 6 wks ago
		Demographic catherineDeville = getDemographic(null,"Deville", "Catherine",  "5555555555", "ZC", "1947", "07", "07", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(catherineDeville);
		admissionDao.saveAdmission(getAdmission(catherineDeville,referenceDate, oscarProgramID));
		app = getAppointment(sixWeeksAgo, 10, 15,15,sixWeeksAgo,drw.getProviderNo(),catherineDeville,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	zybanDate = getDate("2008-02-02");
    	drug  = getDrug(drw.getProviderNo(),	catherineDeville.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//10 Dennis Eaton 4666666666 ZE 04/Apr/1944 M 05/Apr/2008 6 wks ago A005A/ 1 /009 Private/not paid

		Demographic dennisEaton = getDemographic(null,"Eaton", "Dennis",  "4666666666", "ZE", "1944", "04", "04", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(dennisEaton);
		admissionDao.saveAdmission(getAdmission(dennisEaton,referenceDate, oscarProgramID));
		zybanDate = getDate("2008-04-05");
    	drug  = getDrug(drw.getProviderNo(),	dennisEaton.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);



		//11 Ethel Freidman 7777777777 ZF 05/May/1945 F 06/May/2008 6 wks ago A001A/ 1 /493 HCP/Not submitted
		Demographic ethelFreidman = getDemographic(null,"Freidman", "Ethel",  "7777777777", "ZF", "1945", "05", "05", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(ethelFreidman);
		admissionDao.saveAdmission(getAdmission(ethelFreidman,referenceDate, oscarProgramID));
		zybanDate = getDate("2008-06-06");
    	drug  = getDrug(drw.getProviderNo(),	ethelFreidman.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//12 Francis Gaul 1888888888 ZG 06/Jun/1966 M 07/Jun/2010 6 wks ago A003A/ 1 /202 HCP/Paid
		Demographic francisGual = getDemographic(null,"Gual", "Francis",  "1888888888", "ZG", "1966", "06", "06", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(francisGual);
		admissionDao.saveAdmission(getAdmission(francisGual,referenceDate, oscarProgramID));
		zybanDate = getDate("2008-06-07");
    	drug  = getDrug(drw.getProviderNo(),	francisGual.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);



		//13 Gina Huff 9999999999 ZH 07/Jul/1965 F 07/Jul/2009 2 wks ago A007A/ 1 /724 HCP/Paid
		Demographic ginaHuff = getDemographic(null,"Huff", "Gina",  "9999999999", "ZH", "2009", "07", "07", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(ginaHuff);
		admissionDao.saveAdmission(getAdmission(ginaHuff,referenceDate, oscarProgramID));
		zybanDate = getDate("2008-06-07");
    	drug  = getDrug(drw.getProviderNo(),	ginaHuff.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//14 John Henry 8888777797 ZX 09/Sep/1961 M 2 mths ago K030A/ 1 /006 HCP/Submitted not paid     APPT 2 mth ago to Check BP
		Demographic johnHenry = getDemographic(null,"Huff", "Gina",  "8888777797", "ZX", "1961", "09", "09", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(johnHenry);
		admissionDao.saveAdmission(getAdmission(johnHenry,referenceDate, oscarProgramID));
		app = getAppointment(twoMonthsAgo, 10, 15,15,twoMonthsAgo,drw.getProviderNo(),johnHenry,drw.getProviderNo(),"t");
		app.setReason("Check BP");
    	appointmentDao.persist(app);


		//15 Henry Ip 1111222236 ZI 08/Aug/1940 M 03/Jan/2011 2 wks ago
		Demographic henryIp = getDemographic(null,"Ip", "Henry",  "1111222236", "ZI", "1940", "08", "08", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(henryIp);
		admissionDao.saveAdmission(getAdmission(henryIp,referenceDate, oscarProgramID));
		app = getAppointment(twoWeeksAgo, 10, 15,15,twoWeeksAgo,drw.getProviderNo(),henryIp,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	zybanDate = getDate("2011-01-03");
    	drug  = getDrug(drw.getProviderNo(),	henryIp.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//16 Isaac Jackson 2222111136 ZJ 09/Sep/1950 M 03/Jan/2011 10 wks ago A004A/ 1 /724  Private/Paid
		Demographic isaacJackson = getDemographic(null,"Jackson", "Isaac",  "1111222236", "ZI", "1940", "08", "08", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(isaacJackson);
		admissionDao.saveAdmission(getAdmission(isaacJackson,referenceDate, oscarProgramID));
		zybanDate = getDate("2011-01-03");
    	drug  = getDrug(drw.getProviderNo(),	isaacJackson.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);




		//17 Nancy Jackson 7777888897 ZJ 10/Oct/1980 F 1 wk ago K013A/ 2 /300 HCP/Not submitted       APPT 1 wk ago
		Demographic nancyJackson = getDemographic(null,"Jackson", "Nancy",  "7777888897", "ZJ", "1980", "10", "10", "F", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(nancyJackson);
		admissionDao.saveAdmission(getAdmission(nancyJackson,referenceDate, oscarProgramID));
		app = getAppointment(oneWeekAgo, 10, 15,15,oneWeekAgo,drw.getProviderNo(),nancyJackson,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);



		//18 John Kim 3333444457 ZJ 03/Mar/1960 M 05/Jan/2011 2 wks ago A003A/ 1 /724 HCP/Not submitted
		Demographic johnKim = getDemographic(null,"Kim", "John",  "3333444457", "ZJ", "1960", "03", "03", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(johnKim);
		admissionDao.saveAdmission(getAdmission(johnKim,referenceDate, oscarProgramID));
		zybanDate = getDate("2011-01-05");
    	drug  = getDrug(drw.getProviderNo(),	johnKim.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//19 Ken Law 4444333357 ZK 04/Apr/1975 M 02/Feb/2010 10 wks ago A007A/ 1 /724 HCP/Not submitted
		Demographic kenLaw = getDemographic(null,"Law", "Ken",  "4444333357", "ZK", "1975", "04", "04", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(kenLaw);
		admissionDao.saveAdmission(getAdmission(kenLaw,referenceDate, oscarProgramID));
		zybanDate = getDate("2011-02-02");
    	drug  = getDrug(drw.getProviderNo(),	kenLaw.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);
    	
		//20 Lou Malatesta 5555666675 ZL 05/May/1955 M 03/Mar/2010       APPT 1 mth ago
		Demographic louMalatesta = getDemographic(null,"Malatesta", "Lou",  "5555666675", "ZL", "1955", "05", "05", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(louMalatesta);
		admissionDao.saveAdmission(getAdmission(louMalatesta,referenceDate, oscarProgramID));
		app = getAppointment(oneWeekAgo, 10, 15,15,oneWeekAgo,drw.getProviderNo(),louMalatesta,drw.getProviderNo(),"t");
    	appointmentDao.persist(app);
    	zybanDate = getDate("2010-03-03");
    	drug  = getDrug(drw.getProviderNo(),	louMalatesta.getDemographicNo(), zybanDate,getDate(zybanDate,Calendar.DAY_OF_YEAR,+1000),zybanDate,"ZYBAN 150MG",8838,null,1,1,"BID","1000","D","1000",0,false,false,"ZYBAN 150MG Take 1 TAB BID PO for 1000 days Qty:1000 Repeats:0","","BUPROPION HYDROCHLORIDE",
				"N06AX12",scriptNo,"02238441","tab","Take","PO","TABLET",jan62006,"150.0 MG",false,"",true,false,false,"","",false,false);
    	drugDao.addNewDrug(drug);


		//21 Randy Turner 6666555575 ZT 20/Jun/1936 M        APPT Today - flu shot
		Demographic randyTurner = getDemographic(null,"Turner", "Randy",  "6666555575", "ZT", "1936", "06", "20", "M", address, city, province, postal, phone, "AC", "RO", drw.getProviderNo());
		demographicDao.save(randyTurner);
		admissionDao.saveAdmission(getAdmission(randyTurner,referenceDate, oscarProgramID));
		app = getAppointment(today, 10, 15,15,today,drw.getProviderNo(),randyTurner,drw.getProviderNo(),"t");
		app.setReason("Flu Shot");
    	appointmentDao.persist(app);


		//Livingstone
    	/////////////

    	//PAP DATA
    	//ID FirstName LastName DateOfBirth Sex
    	//1 Anna Ant 		10-Aug-1977 F 1-Jan-2002 Hysterectomy 5-Jan-2009
    	Demographic annaAnt = getDemographic(null,"Ant", "Anna",  hin, ver, "1977", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	annaAnt.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(annaAnt);
    	admissionDao.saveAdmission(getAdmission(annaAnt,referenceDate, oscarProgramID));
    	Prevention prevention = getPrevention(getDate("2009-01-05"),"PAP",annaAnt.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-05"),"MAM",annaAnt.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-24"),"FLU",annaAnt.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-01-05"),"FOBT",annaAnt.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);



    	//2 Bette Bailey 	10-Aug-1977 F 1-Jan-2002 27-Aug-2008
    	Demographic betteBailey = getDemographic(null,"Bailey", "Bette",  hin, ver, "1977", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	betteBailey.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(betteBailey);
    	admissionDao.saveAdmission(getAdmission(betteBailey,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-08-27"),"PAP",betteBailey.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-08-27"),"MAM",betteBailey.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-27"),"FOBT",betteBailey.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//3 Cathy  Carr 	10-Aug-1977 F 1-Jan-2002 24-Sep-2010
    	Demographic cathyCarr = getDemographic(null,"Carr", "Cathy",  hin, ver, "1977", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	cathyCarr.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(cathyCarr);
    	admissionDao.saveAdmission(getAdmission(cathyCarr,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"PAP",cathyCarr.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",cathyCarr.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);



    	//4 Donna  Day 		10-Aug-1976 F
    	Demographic donnaDay = getDemographic(null,"Day", "Donna",  hin, ver, "1976", "08", "10", "F", address, city, province, postal, phone, "AC", "NR", drl.getProviderNo());
    	demographicDao.save(donnaDay);
    	admissionDao.saveAdmission(getAdmission(donnaDay,referenceDate, oscarProgramID));


    	//5 Ellen  Eastman 	17-Oct-1945 F 1-Jan-2002 24-Jan-2010 22-Dec-2009 Refused
    	Demographic ellenEastman = getDemographic(null,"Eastman", "Ellen",  hin, ver, "1945", "10", "17", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	ellenEastman.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(ellenEastman);
    	admissionDao.saveAdmission(getAdmission(ellenEastman,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-01-24"),"PAP",ellenEastman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-12-22"),"PAP",ellenEastman.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-02-26"),"MAM",ellenEastman.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-05-13"),"MAM",ellenEastman.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-09-24"),"FOBT",ellenEastman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-06-24"),"FLU",ellenEastman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//PAPF

		Measurement measurements=  getMeasurement("PAPF",getDate("2009-12-22"),ellenEastman.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2008-05-23"),ellenEastman.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//6 Felicia Foster 	18-Nov-1945 F 1-Jan-2002 24-Sep-2010 Hysterectomy 29-Sep-2010 10-Jun-2010 ? 10-Sep-2010 App
    	Demographic feliciaFoster = getDemographic(null,"Foster", "Felicia",  hin, ver, "1945", "10", "18", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	feliciaFoster.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(feliciaFoster);
    	admissionDao.saveAdmission(getAdmission(feliciaFoster,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"PAP",feliciaFoster.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-29"),"PAP",feliciaFoster.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-12-25"),"MAM",feliciaFoster.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",feliciaFoster.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-29"),"FOBT",feliciaFoster.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-26"),"FLU",feliciaFoster.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2010-06-10"),feliciaFoster.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PAPF",getDate("2010-09-10"),feliciaFoster.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FOBF",getDate("2008-05-12"),feliciaFoster.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FOBF",getDate("2008-08-08"),feliciaFoster.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2010-10-22"),feliciaFoster.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//7 Gloria  Grant 	19-Dec-1945 F 1-Jan-2002 27-Jul-2008
    	Demographic gloriaGrant = getDemographic(null,"Grant", "Gloria",  hin, ver, "1945", "12", "19", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	gloriaGrant.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(gloriaGrant);
    	admissionDao.saveAdmission(getAdmission(gloriaGrant,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-07-27"),"PAP",gloriaGrant.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-12-30"),"MAM",gloriaGrant.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-27"),"FOBT",gloriaGrant.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-22"),"FOBT",gloriaGrant.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("FOBF",getDate("2010-10-22"),gloriaGrant.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//8 Hillary Hopkins 20-Jan-1945 F 1-Jan-2002 27-Jul-2008 15-May-2010 ?
    	Demographic hillaryHopkins = getDemographic(null,"Hopkins", "Hillary",  hin, ver, "1945", "01", "20", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	hillaryHopkins.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(hillaryHopkins);
    	admissionDao.saveAdmission(getAdmission(hillaryHopkins,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-07-27"),"PAP",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-07-25"),"MAM",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-08-23"),"MAM",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-27"),"FOBT",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-06-24"),"FLU",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-26"),"FLU",hillaryHopkins.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2010-05-15"),hillaryHopkins.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2010-08-23"),hillaryHopkins.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FOBF",getDate("2010-10-22"),hillaryHopkins.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2010-10-22"),hillaryHopkins.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//11 Irina Itzak 	23-Apr-1945 F 1-Jan-2002 13-Apr-2011
    	Demographic irinaItzak = getDemographic(null,"Itzak", "Irina",  hin, ver, "1945", "04", "23", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	irinaItzak.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(irinaItzak);
    	admissionDao.saveAdmission(getAdmission(irinaItzak,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2011-04-13"),"PAP",irinaItzak.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-04-25"),"MAM",irinaItzak.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-12-24"),"FOBT",irinaItzak.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-11-28"),"FLU",irinaItzak.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("MAMF",getDate("2010-08-23"),irinaItzak.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2010-09-24"),irinaItzak.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//12 Jane  Jacobs 	24-May-1945 F 1-Jan-2002 27-Jul-2008 15-Sep-2010 ? 16-Oct-2010 Refused
    	Demographic janeJacobs = getDemographic(null,"Jacobs", "Jane",  hin, ver, "1945", "05", "24", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	janeJacobs.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(janeJacobs);
    	admissionDao.saveAdmission(getAdmission(janeJacobs,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-07-27"),"PAP",janeJacobs.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-16"),"PAP",janeJacobs.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2005-11-12"),"MAM",janeJacobs.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-20"),"FLU",janeJacobs.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-29"),"FOBT",janeJacobs.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);


    	measurements=  getMeasurement("PAPF",getDate("2010-09-15"),janeJacobs.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PAPF",getDate("2010-10-16"),janeJacobs.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2008-08-19"),janeJacobs.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2008-09-20"),janeJacobs.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2008-10-21"),janeJacobs.getDemographicNo(),"P1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2010-10-22"),janeJacobs.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//13 Karla  Kotts 	24-May-1945 F 1-Jan-2002 27-Jul-2006 15-Sep-2010 ? 16-Oct-2010 ?
    	Demographic karlaKotts = getDemographic(null,"Kotts", "Karla",  hin, ver, "1945", "05", "24", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	karlaKotts.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(karlaKotts);
    	admissionDao.saveAdmission(getAdmission(karlaKotts,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2006-07-27"),"PAP",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-24"),"PAP",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-02-28"),"MAM",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2004-01-12"),"MAM",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-12-26"),"FLU",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-10-22"),"FLU",karlaKotts.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-11-29"),"FOBT",karlaKotts.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2010-09-15"),karlaKotts.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PAPF",getDate("2010-10-16"),karlaKotts.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2009-09-09"),karlaKotts.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2009-10-22"),karlaKotts.getDemographicNo(),"P1",drl.getProviderNo());
    	measurementsDao.persist(measurements);


    	////13 Karla  Kotts 	24-May-1945 F 1-Jan-2002 24-Aug-2008 15-Sep-2010 ? 16-Oct-2010 ?
    	//Demographic annaAnt = getDemographic(null,"Ant", "Anna",  hin, ver, "1977", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	//annaAnt.setRosterDate(getDate("2002-01-01"));
    	//demographicDao.save(annaAnt);
    	//14 Laura Lauzan 	25-Jun-1946 F 1-Jan-2002 24-Aug-2008 18-Oct-2010 App
    	Demographic lauraLauzan = getDemographic(null,"Lauzan", "Laura",  hin, ver, "1946", "06", "25", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	lauraLauzan.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(lauraLauzan);
    	admissionDao.saveAdmission(getAdmission(lauraLauzan,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-08-24"),"PAP",lauraLauzan.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-24"),"MAM",lauraLauzan.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"MAM",lauraLauzan.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-27"),"FOBT",lauraLauzan.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2005-12-19"),"COLONOSCOPY",lauraLauzan.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2010-10-18"),lauraLauzan.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MAMF",getDate("2010-09-21"),lauraLauzan.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);



    	//15 Maria  Maya 	26-Jul-1946 F 1-Jan-2002 24-Dec-2008
    	Demographic mariaMaya = getDemographic(null,"Maya", "Maria",  hin, ver, "1946", "07", "26", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	mariaMaya.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(mariaMaya);
    	admissionDao.saveAdmission(getAdmission(mariaMaya,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-12-24"),"PAP",mariaMaya.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"MAM",mariaMaya.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",mariaMaya.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2005-05-19"),"COLONOSCOPY",mariaMaya.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);



    	//16 Nelly Norwood 	27-Aug-1946 F 			 24-Sep-2010
    	Demographic nellyNorwood = getDemographic(null,"Norwood", "Nelly",  hin, ver, "1946", "08", "27", "F", address, city, province, postal, phone, "AC", "NR", drl.getProviderNo());
    	demographicDao.save(nellyNorwood);
    	admissionDao.saveAdmission(getAdmission(nellyNorwood,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"PAP",nellyNorwood.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"MAM",nellyNorwood.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-08-27"),"FOBT",nellyNorwood.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-12-19"),"COLONOSCOPY",nellyNorwood.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//17 Olivia Ossmond 10-Aug-1941 F 1-Jan-2002 Hysterectomy 29-Sep-2010
    	Demographic oliviaOssmond = getDemographic(null,"Ossmond", "Olivia",  hin, ver, "1941", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	oliviaOssmond.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(oliviaOssmond);
    	admissionDao.saveAdmission(getAdmission(oliviaOssmond,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-09-29"),"PAP",oliviaOssmond.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-12-29"),"MAM",oliviaOssmond.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",oliviaOssmond.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-12-19"),"COLONOSCOPY",oliviaOssmond.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-11-22"),"FLU",oliviaOssmond.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);



    	//18 Priscilla Petcu 10-Aug-1941 F 1-Jan-2002 Hysterectomy 29-Dec-2009 15-Dec-2009 Refused
    	Demographic priscillaPetcu = getDemographic(null,"Petcu", "Priscilla",  hin, ver, "1941", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	priscillaPetcu.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(priscillaPetcu);
    	admissionDao.saveAdmission(getAdmission(priscillaPetcu,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-12-15"),"PAP",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-12-29"),"PAP",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-12-29"),"MAM",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),true,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2005-12-19"),"COLONOSCOPY",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-02-26"),"FLU",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-11-22"),"FLU",priscillaPetcu.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2010-12-15"),priscillaPetcu.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FOBF",getDate("2010-12-24"),priscillaPetcu.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//19 Serrena Singleton 10-Aug-1940 F 1-Jan-2002 24-Sep-2010 15-Nov-2009 ? 17-Dec-2009 Refused
    	Demographic serrenaSingleton = getDemographic(null,"Singleton", "Serrena",  hin, ver, "1940", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	serrenaSingleton.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(serrenaSingleton);
    	admissionDao.saveAdmission(getAdmission(serrenaSingleton,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"PAP",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-12-27"),"PAP",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"MAM",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2008-11-22"),"FLU",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-11-24"),"FLU",serrenaSingleton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("PAPF",getDate("2009-11-15"),serrenaSingleton.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PAPF",getDate("2009-12-17"),serrenaSingleton.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);


    	//20 Tania Truman 	10-Aug-1935 F 1-Jan-2002 24-Sep-2010
    	Demographic taniaTruman = getDemographic(null,"Truman", "Tania",  hin, ver, "1935", "08", "10", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	taniaTruman.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(taniaTruman);
    	admissionDao.saveAdmission(getAdmission(taniaTruman,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2008-09-24"),"PAP",taniaTruman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"MAM",taniaTruman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",taniaTruman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-02-24"),"FLU",taniaTruman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-26"),"FLU",taniaTruman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//COLORECTAL
    	//9 Sam Senior 21-Feb-1945 M 1-Jan-2002 24-Sep-2010
    	Demographic samSenior = getDemographic(null,"Senior", "Sam",  hin, ver, "1945", "02", "21", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	samSenior.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(samSenior);
    	admissionDao.saveAdmission(getAdmission(samSenior,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",samSenior.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("FLUF",getDate("2010-09-20"),samSenior.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FLUF",getDate("2010-10-22"),samSenior.getDemographicNo(),"P1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//10 Tom Totter 22-Mar-1945 M 1-Jan-2002 24-Sep-2010
    	Demographic tomTotter = getDemographic(null,"Totter", "Tom",  hin, ver, "1945", "03", "22", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	tomTotter.setRosterDate(getDate("2002-01-01"));
    	demographicDao.save(tomTotter);
    	admissionDao.saveAdmission(getAdmission(tomTotter,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-24"),"FOBT",tomTotter.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-06-26"),"FLU",tomTotter.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-28"),"FLU",tomTotter.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//KIDS
    	//1 Avril Angel 18-Sep-2009 F 18-Sep-2009 29-Dec-2009 3
    	Demographic avrilAngel = getDemographic(null,"Angel", "Avril",  hin, ver, "2009", "09", "18", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	avrilAngel.setRosterDate(getDate("2009-09-18"));
    	demographicDao.save(avrilAngel);
    	admissionDao.saveAdmission(getAdmission(avrilAngel,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2009-12-29"),"DTaP-IPV-Hib",avrilAngel.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-10-29"),"DTaP-IPV-Hib",avrilAngel.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-08-29"),"DTaP-IPV-Hib",avrilAngel.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);




    	//2 Bill Bauman 18-Sep-2009 M 18-Sep-2009 0 17-Jul-2010 ?
    	Demographic billBauman = getDemographic(null,"Bauman", "Bill",  hin, ver, "2009", "09", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	billBauman.setRosterDate(getDate("2009-09-18"));
    	demographicDao.save(billBauman);
    	admissionDao.saveAdmission(getAdmission(billBauman,referenceDate, oscarProgramID));

    	measurements=  getMeasurement("CIMF",getDate("2010-07-17"),billBauman.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//3 Chris Churchill 18-Sep-2009 M 18-Sep-2009 29-Dec-2009 4 17-Dec-2010 App
    	Demographic chrisChurchill = getDemographic(null,"Churchill", "Chris",  hin, ver, "2009", "09", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	chrisChurchill.setRosterDate(getDate("2009-09-18"));
    	demographicDao.save(chrisChurchill);
    	admissionDao.saveAdmission(getAdmission(chrisChurchill,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2009-10-29"),"MMR",chrisChurchill.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2009-10-29"),"DTaP-IPV-Hib",chrisChurchill.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-11-29"),"DTaP-IPV-Hib",chrisChurchill.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-08-29"),"DTaP-IPV-Hib",chrisChurchill.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-17"),chrisChurchill.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//4 Dan Dayton 18-Sep-2009 M 18-Sep-2009 29-Dec-2010 2 17-Dec-2010 Refused
    	Demographic danDayton = getDemographic(null,"Dayton", "Dan",  hin, ver, "2009", "09", "18", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	danDayton.setRosterDate(getDate("2009-09-18"));
    	demographicDao.save(danDayton);
    	admissionDao.saveAdmission(getAdmission(danDayton,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-12-29"),"DTaP-IPV-Hib",danDayton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-29"),"DTaP-IPV-Hib",danDayton.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-12-17"),"DTaP-IPV-Hib",danDayton.getDemographicNo(),drl.getProviderNo(),false,true);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-17"),danDayton.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//5 Edward Edison 18-Aug-2010 M 18-Aug-2009 26-Dec-2010 3 17-Nov-2010 ? 18-Dec-2010 ?
    	Demographic edwardEdison = getDemographic(null,"Edison", "Edward",  hin, ver, "2010", "08", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	edwardEdison.setRosterDate(getDate("2010-08-18"));
    	demographicDao.save(edwardEdison);
    	admissionDao.saveAdmission(getAdmission(edwardEdison,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-12-26"),"DTaP-IPV-Hib",edwardEdison.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-29"),"DTaP-IPV-Hib",edwardEdison.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-08-29"),"DTaP-IPV-Hib",edwardEdison.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-11-17"),edwardEdison.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-18"),edwardEdison.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//6 Frank Ford 18-Aug-2009 M 18-Aug-2009 26-Dec-2009 4 17-Nov-2010 ? 18-Dec-2010 App
    	Demographic frankFord = getDemographic(null,"Ford", "Frank",  hin, ver, "2009", "08", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	frankFord.setRosterDate(getDate("2009-08-18"));
    	demographicDao.save(frankFord);
    	admissionDao.saveAdmission(getAdmission(frankFord,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2009-12-26"),"MMR",frankFord.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-10-29"),"DTaP-IPV-Hib",frankFord.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-08-29"),"DTaP-IPV-Hib",frankFord.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-06-29"),"DTaP-IPV-Hib",frankFord.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-11-17"),frankFord.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-18"),frankFord.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);


    	//7 George Godard 18-Aug-2009 M 18-Aug-2009 26-Dec-2009 2 17-Nov-2010 ? 18-Dec-2010 Refused
    	Demographic georgeGodard = getDemographic(null,"Godard", "George",  hin, ver, "2009", "08", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	georgeGodard.setRosterDate(getDate("2009-08-18"));
    	demographicDao.save(georgeGodard);
    	admissionDao.saveAdmission(getAdmission(georgeGodard,referenceDate, oscarProgramID));


    	prevention = getPrevention(getDate("2010-12-18"),"DTaP-IPV-Hib",georgeGodard.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2009-12-26"),"DTaP-IPV-Hib",georgeGodard.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-10-29"),"DTaP-IPV-Hib",georgeGodard.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-11-17"),georgeGodard.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-18"),georgeGodard.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	//8 Harry Hope 18-Jul-2009 M 18-Jul-2009 0 13-Oct-2010 ? 14-Nov-2010 ? 25-Dec-2010 ?
    	Demographic harryHope = getDemographic(null,"Hope", "Harry",  hin, ver, "2009", "07", "18", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	harryHope.setRosterDate(getDate("2009-07-18"));
    	demographicDao.save(harryHope);
    	admissionDao.saveAdmission(getAdmission(harryHope,referenceDate, oscarProgramID));

    	measurements=  getMeasurement("CIMF",getDate("2010-10-13"),harryHope.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2010-11-14"),harryHope.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2010-12-15"),harryHope.getDemographicNo(),"P1",drl.getProviderNo());
    	measurementsDao.persist(measurements);


    	//9 Jennifer Jackson 9-Mar-2009 F 9-Mar-2009 26-Dec-2010 5 14-Nov-2010 Refused
    	Demographic jenniferJackson = getDemographic(null,"Jackson", "Jennifer",  hin, ver, "2009", "03", "09", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	jenniferJackson.setRosterDate(getDate("2009-03-09"));
    	demographicDao.save(jenniferJackson);
    	admissionDao.saveAdmission(getAdmission(jenniferJackson,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-11-14"),"MMR",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2010-12-26"),"MMR",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-10-29"),"MMR",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2010-08-18"),"DTaP-IPV-Hib",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2009-06-26"),"DTaP-IPV-Hib",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-04-29"),"DTaP-IPV-Hib",jenniferJackson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-11-14"),jenniferJackson.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);


    	//10 Kate Kay 21-Aug-2008 F 21-Aug-2008 26-Dec-2010 3
    	Demographic kateKay = getDemographic(null,"Kay", "Kate",  hin, ver, "2008", "08", "21", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	kateKay.setRosterDate(getDate("2008-08-21"));
    	demographicDao.save(kateKay);
    	admissionDao.saveAdmission(getAdmission(kateKay,referenceDate, oscarProgramID));



    	prevention = getPrevention(getDate("2010-12-26"),"DTaP-IPV-Hib",kateKay.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	prevention = getPrevention(getDate("2010-10-26"),"DTaP-IPV-Hib",kateKay.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-08-29"),"DTaP-IPV-Hib",kateKay.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//11 Leonard Lambert 21-Aug-2008 M 21-Aug-2008 26-Sep-2009 5
    	Demographic leonardLambert = getDemographic(null,"Lambert", "Leonard",  hin, ver, "2008", "08", "21", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	leonardLambert.setRosterDate(getDate("2008-08-21"));
    	demographicDao.save(leonardLambert);
    	admissionDao.saveAdmission(getAdmission(leonardLambert,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2009-09-26"),"MMR",leonardLambert.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-07-26"),"MMR",leonardLambert.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-05-29"),"DTaP-IPV-Hib",leonardLambert.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-03-26"),"DTaP-IPV-Hib",leonardLambert.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-01-29"),"DTaP-IPV-Hib",leonardLambert.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//12 Monique Morales 21-Aug-2009 F 21-Aug-2009 26-Sep-2010 5
    	Demographic moniqueMorales = getDemographic(null,"Morales", "Monique",  hin, ver, "2009", "08", "21", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	moniqueMorales.setRosterDate(getDate("2009-08-21"));
    	demographicDao.save(moniqueMorales);
    	admissionDao.saveAdmission(getAdmission(moniqueMorales,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-26"),"MMR",moniqueMorales.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",moniqueMorales.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",moniqueMorales.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",moniqueMorales.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",moniqueMorales.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);


    	//13 Nick Neilson 21-Dec-2010 F 21-Dec-2009 26-Sep-2010 2
    	Demographic nickNeilson = getDemographic(null,"Neilson", "Nick",  hin, ver, "2009", "12", "21", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	nickNeilson.setRosterDate(getDate("2009-12-21"));
    	demographicDao.save(nickNeilson);
    	admissionDao.saveAdmission(getAdmission(nickNeilson,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-26"),"DTaP-IPV-Hib",nickNeilson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"DTaP-IPV-Hib",nickNeilson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",nickNeilson.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);



    	//14 Olivia Ortega 21-Aug-2009 F 26-Sep-2010 5
    	Demographic oliviaOrtega = getDemographic(null,"Ortega", "Olivia",  hin, ver, "2009", "08", "21", "F", address, city, province, postal, phone, "AC", "NR", drl.getProviderNo());
    	demographicDao.save(oliviaOrtega);
    	admissionDao.saveAdmission(getAdmission(oliviaOrtega,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-26"),"MMR",oliviaOrtega.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",oliviaOrtega.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",oliviaOrtega.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",oliviaOrtega.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",oliviaOrtega.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);



    	//15 Paul Perez 21-Sep-2009 M 21-Sep-2009 26-Sep-2010 5
    	Demographic paulPerez = getDemographic(null,"Perez", "Paul",  hin, ver, "2009", "09", "21", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	paulPerez.setRosterDate(getDate("2009-09-21"));
    	demographicDao.save(paulPerez);
    	admissionDao.saveAdmission(getAdmission(paulPerez,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-09-26"),"MMR",paulPerez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",paulPerez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",paulPerez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",paulPerez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",paulPerez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//16 Raul Ramirez 17-Mar-2009 M 17-Mar-2009 23-May-2010 5
    	Demographic raulRamirez = getDemographic(null,"Ramirez", "Raul",  hin, ver, "2009", "03", "17", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	raulRamirez.setRosterDate(getDate("2009-03-17"));
    	demographicDao.save(raulRamirez);
    	admissionDao.saveAdmission(getAdmission(raulRamirez,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-05-23"),"MMR",raulRamirez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"MMR",raulRamirez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",raulRamirez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-11-26"),"DTaP-IPV-Hib",raulRamirez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-09-29"),"DTaP-IPV-Hib",raulRamirez.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//17 Simon Sandford 25-Jan-2009 M 25-Jan-2009 25-Oct-2010 5
    	Demographic simonSandford = getDemographic(null,"Sandford", "Simon",  hin, ver, "2009", "01", "25", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	simonSandford.setRosterDate(getDate("2009-01-25"));
    	demographicDao.save(simonSandford);
    	admissionDao.saveAdmission(getAdmission(simonSandford,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-10-25"),"MMR",simonSandford.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",simonSandford.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",simonSandford.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",simonSandford.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",simonSandford.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//18 Tom Tolman 17-Feb-2008 M 17-Feb-2008 25-Oct-2009 3
    	Demographic tomTolman = getDemographic(null,"Tolman", "Tom",  hin, ver, "2008", "02", "17", "M", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	tomTolman.setRosterDate(getDate("2008-02-17"));
    	demographicDao.save(tomTolman);
    	admissionDao.saveAdmission(getAdmission(tomTolman,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2009-05-25"),"DTaP-IPV-Hib",tomTolman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-03-26"),"DTaP-IPV-Hib",tomTolman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2009-01-29"),"DTaP-IPV-Hib",tomTolman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//19 Ursula Ulman 23-Dec-2009 F 23-Dec-2009 25-Dec-2010 5
    	Demographic ursulaUlman = getDemographic(null,"Ulman", "Ursula",  hin, ver, "2009", "12", "23", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	ursulaUlman.setRosterDate(getDate("2009-12-23"));
    	demographicDao.save(ursulaUlman);
    	admissionDao.saveAdmission(getAdmission(ursulaUlman,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-12-25"),"MMR",ursulaUlman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",ursulaUlman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",ursulaUlman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",ursulaUlman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",ursulaUlman.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	//20 Vivian Vernon 23-Dec-2007 F 23-Dec-2007 20-Oct-2010 5
    	Demographic vivianVernon = getDemographic(null,"Vernon", "Vivian",  hin, ver, "2007", "12", "23", "F", address, city, province, postal, phone, "AC", "RO", drl.getProviderNo());
    	vivianVernon.setRosterDate(getDate("2007-12-23"));
    	demographicDao.save(vivianVernon);
    	admissionDao.saveAdmission(getAdmission(vivianVernon,referenceDate, oscarProgramID));

    	prevention = getPrevention(getDate("2010-10-20"),"MMR",vivianVernon.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-07-26"),"MMR",vivianVernon.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-05-29"),"DTaP-IPV-Hib",vivianVernon.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-03-26"),"DTaP-IPV-Hib",vivianVernon.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	prevention = getPrevention(getDate("2010-01-29"),"DTaP-IPV-Hib",vivianVernon.getDemographicNo(),drl.getProviderNo(),false,false);
    	preventionDao.persist(prevention);

    	measurements=  getMeasurement("CIMF",getDate("2010-02-24"),vivianVernon.getDemographicNo(),"L2",drl.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CIMF",getDate("2009-12-24"),vivianVernon.getDemographicNo(),"L1",drl.getProviderNo());
    	measurementsDao.persist(measurements);




    	//DRS
    	Demographic coleenCopper = getDemographic("Mrs","Copper", "Coleen","4444444441", "CC", "1950", "01", "01", "F", "55 Applewood", "Toronto", "Ontario", "A1B 1B1", "416.123.4567", "AC", "RO", drs.getProviderNo());
    	coleenCopper.setHcRenewDate(getDate("2012-01-01"));
    	coleenCopper.setHcType("ON");
    	coleenCopper.setChartNo("556677");
    	//prefered lang english
    	coleenCopper.setSpokenLanguage("french");
    	coleenCopper.setRosterDate(getDate("2010-02-01"));
    	coleenCopper.setEffDate(getDate("2000-02-01"));
    	coleenCopper.setSin("777888999");
    	coleenCopper.setPhone2("647.123.4567");
    	//cell coleenCopper.setPhone2("905.123.4567");

    	coleenCopper.setEmail("coleen.copper@hotmail.com");
    	demographicDao.save(coleenCopper);
    	//Notes Patient frequently misses appointments
    	admissionDao.saveAdmission(getAdmission(coleenCopper,referenceDate, oscarProgramID));

    	//alternative contacts
    	Demographic conanCopper = getDemographic("Mr","Copper", "Conan",hin, ver, "1900", "01", "01", "M", address, city, province, postal, "416.112.2334", "NA", "NR", drs.getProviderNo());
    	conanCopper.setPhone2("905.1122334");
    	demographicDao.save(conanCopper);
    	admissionDao.saveAdmission(getAdmission(conanCopper,referenceDate, oscarProgramID));

    	DemographicContact demographicContact = getDemographicContact(coleenCopper.getDemographicNo(), drw.getProviderNo(), ""+conanCopper.getDemographicNo(),referenceDate,1,"personal","Husband","true","true");
    	demographicContactDao.persist(demographicContact);

    	Demographic leanneLennon = getDemographic(null,"Lennon", "Leanne",hin, ver, "1900", "01", "01", "M", address, city, province, postal, "647.112.2334", "NA", "NR", drs.getProviderNo());
    	leanneLennon.setEmail("sister@yahoo.com");
       	demographicDao.save(leanneLennon);
    	admissionDao.saveAdmission(getAdmission(leanneLennon,referenceDate, oscarProgramID));


    	demographicContact = getDemographicContact(coleenCopper.getDemographicNo(),drw.getProviderNo(), ""+leanneLennon.getDemographicNo(),referenceDate,1,"personal","Sister","false","false");
    	demographicContactDao.persist(demographicContact);

    	Demographic claireCopper = getDemographic("Mr","Copper", "Claire",hin, ver, "1900", "01", "01", "M", address, city, province, postal, "416.112.2334", "NA", "NR", drs.getProviderNo());
    	claireCopper.setPhone2("712.1122334");
    	//work extension 11223344
    	demographicDao.save(claireCopper);
    	admissionDao.saveAdmission(getAdmission(claireCopper,referenceDate, oscarProgramID));


    	demographicContact = getDemographicContact(coleenCopper.getDemographicNo(), drw.getProviderNo(), ""+claireCopper.getDemographicNo(),referenceDate,1,"personal","Daughter","false","false");
    	demographicContactDao.persist(demographicContact);

    	//fam med hx

    	//Family History: Mother had Alzheimers, onset age 70.
        CaseManagementIssue caseManagementIssuecoleenCopperMotherAlzh = getCaseMangementIssue(coleenCopper,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperMotherAlzh);
        CaseManagementIssue caseManagementIssuecoleenCopperMotherAlzhICD9 = getCaseMangementIssue(coleenCopper,alzheimers,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperMotherAlzhICD9);
    	Set<CaseManagementIssue> set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperMotherAlzh);
    	set.add(caseManagementIssuecoleenCopperMotherAlzhICD9);



        CaseManagementNote coleenCopperFamHxAlzeimersNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Mother diagnosed with Alzheimer",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxAlzeimersNote);

        CaseManagementNoteExt  coleenCopperFamilyHistoryAlzeimersNoteExt = new CaseManagementNoteExt();
        coleenCopperFamilyHistoryAlzeimersNoteExt.setNoteId(coleenCopperFamHxAlzeimersNote.getId());
        coleenCopperFamilyHistoryAlzeimersNoteExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperFamilyHistoryAlzeimersNoteExt.setDateValue(getDate("2000-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryAlzeimersNoteExt);

        coleenCopperFamilyHistoryAlzeimersNoteExt = getCaseManagementNoteExt(coleenCopperFamHxAlzeimersNote.getId(),CaseManagementNoteExt.LIFESTAGE,"A");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryAlzeimersNoteExt);

        coleenCopperFamilyHistoryAlzeimersNoteExt = getCaseManagementNoteExt(coleenCopperFamHxAlzeimersNote.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"Alzheimer's disease with late onset");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryAlzeimersNoteExt);

        coleenCopperFamilyHistoryAlzeimersNoteExt = getCaseManagementNoteExt(coleenCopperFamHxAlzeimersNote.getId(),CaseManagementNoteExt.TREATMENT,"Attends Day Program 3x/wk");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryAlzeimersNoteExt);

        coleenCopperFamilyHistoryAlzeimersNoteExt = getCaseManagementNoteExt(coleenCopperFamHxAlzeimersNote.getId(),CaseManagementNoteExt.RELATIONSHIP,"mother");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryAlzeimersNoteExt);

        CaseManagementNote coleenCopperFamHxAlzhNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"High risk for wandering",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxAlzhNoteAnnotation);

        CaseManagementNoteLink caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperFamHxAlzhNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperFamHxAlzeimersNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);



        CaseManagementIssue caseManagementIssuecoleenCopperFatherParkinsons = getCaseMangementIssue(coleenCopper,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperFatherParkinsons);
        CaseManagementIssue caseManagementIssuecoleenCopperFatherParkinsonsICD9 = getCaseMangementIssue(coleenCopper,parkinsons,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperFatherParkinsonsICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperMotherAlzh);
    	set.add(caseManagementIssuecoleenCopperFatherParkinsonsICD9);

        CaseManagementNote coleenCopperFamHxParkinsonsNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Father diagnosed with Parkinsons",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxParkinsonsNote);

        CaseManagementNoteExt  coleenCopperFamilyHistoryParkinsonsNoteExt = new CaseManagementNoteExt();
        coleenCopperFamilyHistoryParkinsonsNoteExt.setNoteId(coleenCopperFamHxParkinsonsNote.getId());
        coleenCopperFamilyHistoryParkinsonsNoteExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperFamilyHistoryParkinsonsNoteExt.setDateValue(getDate("1990-03-01"));
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryParkinsonsNoteExt);

        coleenCopperFamilyHistoryParkinsonsNoteExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.LIFESTAGE,"A");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryParkinsonsNoteExt);

        coleenCopperFamilyHistoryParkinsonsNoteExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"Parkinsons Disease");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryParkinsonsNoteExt);

        coleenCopperFamilyHistoryParkinsonsNoteExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.TREATMENT,"Rasagiline 1mg/once a day");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryParkinsonsNoteExt);

        coleenCopperFamilyHistoryParkinsonsNoteExt =getCaseManagementNoteExt( coleenCopperFamHxParkinsonsNote.getId(), CaseManagementNoteExt.RELATIONSHIP, "father");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryParkinsonsNoteExt);

        CaseManagementNote coleenCopperFamHxParkinsonsNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Living Independently",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxParkinsonsNoteAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperFamHxParkinsonsNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperFamHxParkinsonsNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);


        ///Brother typhoid
        CaseManagementIssue caseManagementIssuecoleenCopperBrotherTyphoid = getCaseMangementIssue(coleenCopper,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperBrotherTyphoid);
        CaseManagementIssue caseManagementIssuecoleenCopperBrotherTyphoidICD9 = getCaseMangementIssue(coleenCopper,typhoidFever,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperBrotherTyphoidICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperMotherAlzh);
    	set.add(caseManagementIssuecoleenCopperFatherParkinsonsICD9);

        CaseManagementNote coleenCopperFamHxTyphoidNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Brother-Typhoid Fever",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxTyphoidNote);

        CaseManagementNoteExt coleenCopperFamilyHistoryTyphoidExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.AGEATONSET,"3 Years");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryTyphoidExt);

        coleenCopperFamilyHistoryTyphoidExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.TREATMENT,"Concerta");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryTyphoidExt);

        coleenCopperFamilyHistoryTyphoidExt =getCaseManagementNoteExt( coleenCopperFamHxParkinsonsNote.getId(), CaseManagementNoteExt.RELATIONSHIP, "brother");
        caseManagementNoteExtDAO.save(coleenCopperFamilyHistoryTyphoidExt);

        CaseManagementNote coleenCopperFamHxtyphoidNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Doing well in school",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperFamHxtyphoidNoteAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperFamHxtyphoidNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperFamHxTyphoidNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);


    	//pat med hx

        //Classic cesarean

        CaseManagementIssue caseManagementIssuecoleenCopperPastMedCesearean = getCaseMangementIssue(coleenCopper,medHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedCesearean);
        CaseManagementIssue caseManagementIssuecoleenCopperPastMedCeseareanICD9 = getCaseMangementIssue(coleenCopper,cesarean,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedCeseareanICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperPastMedCesearean);
    	set.add(caseManagementIssuecoleenCopperPastMedCeseareanICD9);

        CaseManagementNote coleenCopperPastMedCesareanNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Classical Cesarean",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedCesareanNote);

        CaseManagementNoteExt coleenCopperpastHistCesareanExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.LIFESTAGE,"A");
        caseManagementNoteExtDAO.save(coleenCopperpastHistCesareanExt);

        coleenCopperpastHistCesareanExt = new CaseManagementNoteExt();
        coleenCopperpastHistCesareanExt.setNoteId(coleenCopperFamHxParkinsonsNote.getId());
		coleenCopperpastHistCesareanExt.setKeyVal(CaseManagementNoteExt.PROCEDUREDATE);
        coleenCopperpastHistCesareanExt.setDateValue(getDate("2005-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperpastHistCesareanExt);

        coleenCopperpastHistCesareanExt =getCaseManagementNoteExt( coleenCopperFamHxParkinsonsNote.getId(), CaseManagementNoteExt.RELATIONSHIP, "brother");
        caseManagementNoteExtDAO.save(coleenCopperpastHistCesareanExt);

        CaseManagementNote coleenCopperPastMedHxCesareanNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Failure to progress",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedHxCesareanNoteAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastMedHxCesareanNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperPastMedCesareanNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);

        //Liver Lobectomy

        CaseManagementIssue caseManagementIssuecoleenCopperPastMedLiverLobectomy = getCaseMangementIssue(coleenCopper,medHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedLiverLobectomy);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperPastMedLiverLobectomy);

        CaseManagementNote coleenCopperPastMedLiverLobectomyNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Lobectomy Liver",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedLiverLobectomyNote);

        CaseManagementNoteExt coleenCopperpastHisLiverLobectomyExt = getCaseManagementNoteExt(coleenCopperFamHxParkinsonsNote.getId(),CaseManagementNoteExt.LIFESTAGE,"A");
        caseManagementNoteExtDAO.save(coleenCopperpastHisLiverLobectomyExt);

        CaseManagementNote coleenCopperPastMedHxLiverLobectomyNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Motor Vehicle Accident",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedHxLiverLobectomyNoteAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastMedHxLiverLobectomyNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperPastMedLiverLobectomyNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);

        //Acute otitis externa

        CaseManagementIssue caseManagementIssuecoleenCopperPastMedacuteExterna = getCaseMangementIssue(coleenCopper,medHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedacuteExterna);
        CaseManagementIssue caseManagementIssuecoleenCopperPastMedacuteExternaICD9 = getCaseMangementIssue(coleenCopper,acuteOtitis,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedacuteExternaICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperPastMedacuteExterna);
    	set.add(caseManagementIssuecoleenCopperPastMedacuteExternaICD9);

        CaseManagementNote coleenCopperPastMedAcuteOtitisNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Acute otitis externa, noninfective",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedAcuteOtitisNote);

        CaseManagementNoteExt coleenCopperpastHistAcuteOtitisExt = new CaseManagementNoteExt();
        coleenCopperpastHistAcuteOtitisExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperpastHistAcuteOtitisExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperpastHistAcuteOtitisExt.setDateValue(getDate("2009-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperpastHistAcuteOtitisExt);

        coleenCopperpastHistAcuteOtitisExt = new CaseManagementNoteExt();
        coleenCopperpastHistAcuteOtitisExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperpastHistAcuteOtitisExt.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
        coleenCopperpastHistAcuteOtitisExt.setDateValue(getDate("2009-03-01"));
        caseManagementNoteExtDAO.save(coleenCopperpastHistAcuteOtitisExt);


        //Pneumonia

        CaseManagementIssue caseManagementIssuecoleenCopperPastMedPneumonia = getCaseMangementIssue(coleenCopper,medHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedPneumonia);
        CaseManagementIssue caseManagementIssuecoleenCopperPastMedPneumoniaICD9 = getCaseMangementIssue(coleenCopper,pneumonia,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperPastMedPneumoniaICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperPastMedPneumonia);
    	set.add(caseManagementIssuecoleenCopperPastMedPneumoniaICD9);

        CaseManagementNote coleenCopperPastMedPneumoniaNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Pneumonia Unspecified",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedPneumoniaNote);

        CaseManagementNoteExt coleenCopperpastHistPneumoniaExt = getCaseManagementNoteExt(coleenCopperPastMedPneumoniaNote.getId(),CaseManagementNoteExt.LIFESTAGE,"T");
        caseManagementNoteExtDAO.save(coleenCopperpastHistPneumoniaExt);

        CaseManagementNote coleenCopperPastMedHxPneumoniaNoteAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Community Aquired",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastMedHxPneumoniaNoteAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastMedHxPneumoniaNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperPastMedPneumoniaNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);


    	//ongoing concerns

        dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,coleenCopper.getDemographicNo()));
		//Add note

        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsdm2 = getCaseMangementIssue(coleenCopper,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsdm2);
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsdm2ICD9 = getCaseMangementIssue(coleenCopper,diabetes,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsdm2ICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsdm2);
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsdm2ICD9);


        CaseManagementNote coleenCopperOngoingConcernsdm2Note = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Non-insulin-dependent diabetes melitus",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperOngoingConcernsdm2Note);

        CaseManagementNoteExt coleenCopperOngoingConcernsdm2Ext = getCaseManagementNoteExt(coleenCopperOngoingConcernsdm2Note.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"ongoing");
        caseManagementNoteExtDAO.save(coleenCopperOngoingConcernsdm2Ext);

        coleenCopperOngoingConcernsdm2Ext = new CaseManagementNoteExt();
        coleenCopperOngoingConcernsdm2Ext.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperOngoingConcernsdm2Ext.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperOngoingConcernsdm2Ext.setDateValue(getDate("2006-02-01"));
        caseManagementNoteExtDAO.save(coleenCopperOngoingConcernsdm2Ext);


        CaseManagementNote coleenCopperPastOngoingConcernsdm2Annotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Well controlled",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastOngoingConcernsdm2Annotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastMedHxPneumoniaNoteAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperPastMedPneumoniaNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);

        //corneal pigmentations
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsConreal = getCaseMangementIssue(coleenCopper,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsConreal);
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsConrealICD9 = getCaseMangementIssue(coleenCopper,corneal,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsConrealICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsConreal);
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsConrealICD9);

        CaseManagementNote coleenCopperOngoingConcernsConrealNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Corneal Pigmentations and deposits",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperOngoingConcernsConrealNote);

        CaseManagementNoteExt coleenCopperOngoingConcernsConrealExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"ongoing");
        caseManagementNoteExtDAO.save(coleenCopperOngoingConcernsConrealExt);

        coleenCopperOngoingConcernsConrealExt = new CaseManagementNoteExt();
        coleenCopperOngoingConcernsConrealExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperOngoingConcernsConrealExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperOngoingConcernsConrealExt.setDateValue(getDate("2006-02-01"));
        caseManagementNoteExtDAO.save(coleenCopperOngoingConcernsConrealExt);


        CaseManagementNote coleenCopperPastOngoingConcernsCornealAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"U/S Annually to follow",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastOngoingConcernsCornealAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastOngoingConcernsCornealAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperOngoingConcernsConrealNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);

        //Migraine
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsMigraine = getCaseMangementIssue(coleenCopper,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsMigraine);
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsMigraineICD9 = getCaseMangementIssue(coleenCopper,classicalMigraine,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsMigraineICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsMigraine);
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsMigraineICD9);

        CaseManagementNote coleenCopperOngoingConcernsMigraineNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Migraine with aura",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperOngoingConcernsMigraineNote);

        CaseManagementNoteExt coleenCopperOngoingConcernsMigraineExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsMigraineNote.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"suspected");
        caseManagementNoteExtDAO.save(coleenCopperOngoingConcernsMigraineExt);

        //Hypertension
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsHypertn = getCaseMangementIssue(coleenCopper,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsHypertn);
        CaseManagementIssue caseManagementIssuecoleenCopperOngoingConcernsHypertnICD9 = getCaseMangementIssue(coleenCopper,classicalMigraine,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperOngoingConcernsHypertnICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsHypertn);
    	set.add(caseManagementIssuecoleenCopperOngoingConcernsHypertnICD9);

        CaseManagementNote coleenCopperOngoingConcernsHypertnNote = getCaseManagementNoteWithIssueSet(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Hypertensive heart disease with congestive heart failure",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(coleenCopperOngoingConcernsHypertnNote);


    	//risk factors

        //second hand smoke
        CaseManagementIssue caseManagementIssuecoleenCopperRiskFactorSmoke = getCaseMangementIssue(coleenCopper,riskFactors,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperRiskFactorSmoke);

        CaseManagementNote coleenCopperRiskFactorSmokeNote = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Exposure to second hand-smoke",oscarProgramID, caseManagementIssuecoleenCopperRiskFactorSmoke);
        caseManagementNoteDAO.saveNote(coleenCopperRiskFactorSmokeNote);

        CaseManagementNoteExt coleenCopperRiskFactorSmokeExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"ongoing");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorSmokeExt);

         coleenCopperRiskFactorSmokeExt = new CaseManagementNoteExt();
        coleenCopperRiskFactorSmokeExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperRiskFactorSmokeExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperRiskFactorSmokeExt.setDateValue(getDate("1980-02-01"));
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorSmokeExt);

        coleenCopperRiskFactorSmokeExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.LIFESTAGE,"A");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorSmokeExt);

        coleenCopperRiskFactorSmokeExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.AGEATONSET,"30");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorSmokeExt);

        coleenCopperRiskFactorSmokeExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.EXPOSUREDETAIL,"Spouse smokes 2 packs per day");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorSmokeExt);


        //Pollution
        CaseManagementIssue caseManagementIssuecoleenCopperRiskFactorPollution = getCaseMangementIssue(coleenCopper,riskFactors,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperRiskFactorPollution);

        CaseManagementNote coleenCopperRiskFactorPollutionNote = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Pollution",oscarProgramID, caseManagementIssuecoleenCopperRiskFactorSmoke);
        caseManagementNoteDAO.saveNote(coleenCopperRiskFactorPollutionNote);



        CaseManagementNoteExt coleenCopperRiskFactorPollutionExt = new CaseManagementNoteExt();
        coleenCopperRiskFactorPollutionExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperRiskFactorPollutionExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperRiskFactorPollutionExt.setDateValue(getDate("1960-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorPollutionExt);

        coleenCopperRiskFactorPollutionExt = new CaseManagementNoteExt();
        coleenCopperRiskFactorPollutionExt.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        coleenCopperRiskFactorPollutionExt.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
        coleenCopperRiskFactorPollutionExt.setDateValue(getDate("1970-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorPollutionExt);

        coleenCopperRiskFactorPollutionExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.AGEATONSET,"10");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorPollutionExt);

        coleenCopperRiskFactorPollutionExt = getCaseManagementNoteExt(coleenCopperOngoingConcernsConrealNote.getId(),CaseManagementNoteExt.EXPOSUREDETAIL,"Live close to a factory");
        caseManagementNoteExtDAO.save(coleenCopperRiskFactorPollutionExt);



        CaseManagementNote coleenCopperPastRiskFactorPollutionAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Reports cough during air quality advisories",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperPastRiskFactorPollutionAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperPastRiskFactorPollutionAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperRiskFactorPollutionNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);

        //Allergies
        allergy = getAllergy(""+coleenCopper.getDemographicNo(),getDate("2005-02-01"),"PENICILLIN G SOD INJ PWS 1000000UNIT/VIAL",0,0,0,0,13,"Adverse Reaction - Rash","11231",false,getDate("2005-01-01"),"1","1","01930672","A");
        allergyDao.persist(allergy);

        CaseManagementNote coleenCopperAdverseReactionAnnotation = getCaseManagementNote(getDate("2005-02-01"),coleenCopper,drs.getProviderNo(),"Responded to Benadryl",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperAdverseReactionAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(3);
        caseManagementNoteLink.setNoteId(coleenCopperAdverseReactionAnnotation.getId());
        caseManagementNoteLink.setTableId(Long.parseLong(""+allergy.getId()));

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);



        allergy = getAllergy(""+coleenCopper.getDemographicNo(),getDate("2005-02-01"),"PEANUTS",0,0,0,0,0,"Anaphylaxis","11231",false,null,"2","1",null,"I");
        allergyDao.persist(allergy);

        CaseManagementNote coleenCopperAllergyAnnotation = getCaseManagementNote(getDate("2005-02-01"),coleenCopper,drs.getProviderNo(),"Carries epipen",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperAllergyAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(3);
        caseManagementNoteLink.setNoteId(coleenCopperAllergyAnnotation.getId());
        caseManagementNoteLink.setTableId(Long.parseLong(""+allergy.getId()));

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);




    	//medications
        //TODO:need multiple dosage column

    	//immunizations
        //TODO:Need preventionExt
        prevention = getPrevention(getDate("2008-01-01"),"TB",coleenCopper.getDemographicNo(),drs.getProviderNo(),false,false);

    	preventionDao.persist(prevention);





    	//labs

    	//appts
    	Appointment coleenCopperApp = getAppointment(getDate("2010-01-01"), 10, 10,10,tenYearsAgo,drs.getProviderNo(),coleenCopper,drs.getProviderNo(),"t");
    	coleenCopperApp.setReason("Follow-ip Glucose");
    	coleenCopperApp.setNotes("wants information about accupuncture");
    	appointmentDao.persist(coleenCopperApp);

    	Appointment febFirstApp = getAppointment(getDate("2010-02-01"), 11, 20,15,tenYearsAgo,drs.getProviderNo(),coleenCopper,drs.getProviderNo(),"t");
    	febFirstApp.setReason("Flu Immuniztion");
    	febFirstApp.setNotes("Note 1");
    	appointmentDao.persist(febFirstApp);

    	app = getAppointment(getDate("2010-03-01"), 13, 30,20,tenYearsAgo,drs.getProviderNo(),coleenCopper,drs.getProviderNo(),"t");
    	app.setReason("Annual Checkup");
    	appointmentDao.persist(app);

    	app = getAppointment(getDate("2010-03-15"), 14, 40,10,tenYearsAgo,drs.getProviderNo(),coleenCopper,drs.getProviderNo(),"c");
    	app.setReason("Annual Checkup");
    	app.setNotes("Patient was out of town on vacation");
    	appointmentDao.persist(app);
    	//notes

    	CaseManagementNote coleenCopperNote1 =getCaseManagementNote(getDate("2010-01-01"),coleenCopper,drs.getProviderNo(),"S: c/o 2 days urinary frequency; associated with dysuria, and gross hematuria this am. \nO:NAD  BP 145/80; HR 72 regular, RR 15, HT 5'7\", Wt 167lbs \nA: Acute cystitis, Type 2 DM, Asthma \nP: Macrobid 100 mg BID x3 days, urine C&S sent. \n Signed by Dr.Samson Jan 1,2010 10:40 ",oscarProgramID, null);
    	coleenCopperNote1.setAppointmentNo(coleenCopperApp.getId());
    	coleenCopperNote1.setCreate_date(getDateAndTime("2010-01-01 10:20:00"));
    	coleenCopperNote1.setUpdate_date(getDateAndTime("2010-01-01 10:40:00"));
    	coleenCopperNote1.setSigned(true);
		caseManagementNoteDAO.saveNote(coleenCopperNote1);

		CaseManagementNote coleenCopperNote2 =getCaseManagementNote(getDate("2010-01-01"),coleenCopper,drs.getProviderNo(),"S:Diabetes F/U.  Reports SMBG 1x/d; glucometer log reviewed, fasting glucose  averaging 8.0; currently exercising 1x per week; continues to smoke 1ppd \nO:H&N: no carotid bruits \nA:Type 2 DM \nP:Macrobid 100 mg BID x3 days, urine C&S sent",oscarProgramID, null);
    	coleenCopperNote2.setAppointmentNo(febFirstApp.getId());
    	coleenCopperNote2.setCreate_date(getDate("2010-02-01"));
    	caseManagementNoteDAO.saveNote(coleenCopperNote1);

    	//documents

    	//measurements
    	//smoking status   SKST
    	measurements=  getMeasurement("SKST",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SKST",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SKST",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//smoking packs/day   POSK
    	measurements=  getMeasurement("POSK",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"2",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("POSK",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"1",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Mesaured patient weight   WT
    	measurements=  getMeasurement("WT",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"90",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WT",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"80",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WT",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"70",drs.getProviderNo());
    	measurementsDao.persist(measurements);
    	//Height    HT
    	measurements=  getMeasurement("HT",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"170",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HT",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"170",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HT",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"170",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Waist Circumfrence    WC
    	measurements=  getMeasurement("WC",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"80",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WC",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"70",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WC",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"60",drs.getProviderNo());
    	measurementsDao.persist(measurements);
    	//BP    bP
    	measurements=  getMeasurement("BP",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"120/70",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"140/80",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"160/90",drs.getProviderNo());
    	measurementsDao.persist(measurements);
    	//Dilated Eye Exam  EYEE
    	measurements=  getMeasurement("EYEE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EYEE",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EYEE",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Foot Exam    FTE
    	measurements=  getMeasurement("FTE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Neurological Exam  PANE
    	measurements=  getMeasurement("PANE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counselling completed - nutrition    MCCN
    	measurements=  getMeasurement("MCCN",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCN",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counselling completed - excersize    MCCE
    	measurements=  getMeasurement("MCCE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCE",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counselling completed - Smoking Cessation  MCCS
    	measurements=  getMeasurement("MCCS",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCS",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counselling - other   MCCO
    	measurements=  getMeasurement("MCCO",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCO",getDate("2011-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//collaborative Goal      CGSD
    	measurements=  getMeasurement("MCCE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"aaaa",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCE",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"bbbb",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//self management     SMCD
    	measurements=  getMeasurement("SMCD",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMCD",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//education dm   DMME
    	measurements=  getMeasurement("DMME",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("DMME",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//#of hypoglcemic    HYPE
    	measurements=  getMeasurement("HYPE",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HYPE",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//self mon bg    SMBG
    	measurements=  getMeasurement("SMBG",getDate("2009-01-01"),coleenCopper.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMBG",getDate("2010-01-01"),coleenCopper.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);






    	//Alerts

    	CaseManagementIssue caseManagementIssuecoleenCopperAlerts = getCaseMangementIssue(coleenCopper,reminders,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuecoleenCopperAlerts);

        CaseManagementNote coleenCopperTransporationAlertNote = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Transportation Needs",oscarProgramID, caseManagementIssuecoleenCopperAlerts);
        caseManagementNoteDAO.saveNote(coleenCopperTransporationAlertNote);



        CaseManagementNoteExt coleenCopperTransportationAlertExt = new CaseManagementNoteExt();
        coleenCopperTransportationAlertExt.setNoteId(coleenCopperTransporationAlertNote.getId());
        coleenCopperTransportationAlertExt.setKeyVal(CaseManagementNoteExt.STARTDATE);
        coleenCopperTransportationAlertExt.setDateValue(getDate("2009-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperTransportationAlertExt);


        coleenCopperTransportationAlertExt = new CaseManagementNoteExt();
        coleenCopperTransportationAlertExt.setNoteId(coleenCopperTransporationAlertNote.getId());
        coleenCopperTransportationAlertExt.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
        coleenCopperTransportationAlertExt.setDateValue(getDate("2010-01-01"));
        caseManagementNoteExtDAO.save(coleenCopperTransportationAlertExt);

        CaseManagementNote coleenCopperTransportationRiskAnnotation = getCaseManagementNote(tenYearsAgo,coleenCopper,drs.getProviderNo(),"Spouse unavailable",oscarProgramID, null);
        caseManagementNoteDAO.saveNote(coleenCopperTransportationRiskAnnotation);

        caseManagementNoteLink = new CaseManagementNoteLink();
        caseManagementNoteLink.setTableName(1);
        caseManagementNoteLink.setNoteId(coleenCopperTransportationRiskAnnotation.getId());
        caseManagementNoteLink.setTableId(coleenCopperTransporationAlertNote.getId());

        CaseManagementNoteLinkDao.save(caseManagementNoteLink);




    	Demographic silviuSilver = getDemographic("Mr","Silver", "Silviu","5555555555", "SS", "2000", "01", "01", "M", "155", "Toronto", "Ontario", "A2B 2B2", "416.234.5678", "AC", "RO", drs.getProviderNo());
    	silviuSilver.setRosterDate(getDate("2008-02-17"));
    	silviuSilver.setHcRenewDate(getDate("2012-01-01"));
    	silviuSilver.setHcType("M");
    	silviuSilver.setChartNo("2000");
    	//pref French
    	silviuSilver.setSpokenLanguage("english");

    	demographicDao.save(silviuSilver);
    	admissionDao.saveAdmission(getAdmission(silviuSilver,referenceDate, oscarProgramID));


    	dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,silviuSilver.getDemographicNo()));
		//Add note

        CaseManagementIssue caseManagementIssuesilviuSilverOngoingConcernsdm2 = getCaseMangementIssue(silviuSilver,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuesilviuSilverOngoingConcernsdm2);
        CaseManagementIssue caseManagementIssuesilviuSilverOngoingConcernsdm2ICD9 = getCaseMangementIssue(silviuSilver,diabetes,oscarProgramID,"doctor");
        caseManagementIssueDAO.saveIssue(caseManagementIssuesilviuSilverOngoingConcernsdm2ICD9);
    	set = new HashSet<CaseManagementIssue>();
    	set.add(caseManagementIssuesilviuSilverOngoingConcernsdm2);
    	set.add(caseManagementIssuesilviuSilverOngoingConcernsdm2ICD9);


        CaseManagementNote silviuSilverOngoingConcernsdm2Note = getCaseManagementNoteWithIssueSet(tenYearsAgo,silviuSilver,drs.getProviderNo(),"Non-insulin-dependent diabetes melitus",oscarProgramID, set);
        caseManagementNoteDAO.saveNote(silviuSilverOngoingConcernsdm2Note);

        CaseManagementNoteExt silviuSilverOngoingConcernsdm2Ext = getCaseManagementNoteExt(silviuSilverOngoingConcernsdm2Note.getId(),CaseManagementNoteExt.PROBLEMSTATUS,"ongoing");
        caseManagementNoteExtDAO.save(silviuSilverOngoingConcernsdm2Ext);

        silviuSilverOngoingConcernsdm2Ext = getCaseManagementNoteExt(silviuSilverOngoingConcernsdm2Note.getId(),CaseManagementNoteExt.LIFESTAGE,"T");
        caseManagementNoteExtDAO.save(silviuSilverOngoingConcernsdm2Ext);

        silviuSilverOngoingConcernsdm2Ext = new CaseManagementNoteExt();
        silviuSilverOngoingConcernsdm2Ext.setNoteId(coleenCopperPastMedAcuteOtitisNote.getId());
        silviuSilverOngoingConcernsdm2Ext.setKeyVal(CaseManagementNoteExt.STARTDATE);
        silviuSilverOngoingConcernsdm2Ext.setDateValue(getDate("2006-02-01"));
        caseManagementNoteExtDAO.save(silviuSilverOngoingConcernsdm2Ext);

        //immunizations
        //TODO:Need preventionExt


      //measurements
    	//smoking status   SKST
    	measurements=  getMeasurement("SKST",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SKST",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//smoking packs/day   POSK
    	// 3 cigs a day  pack is considered 20 (from wikipedia) 3/20

    	measurements=  getMeasurement("POSK",getDate("2012-08-08"),silviuSilver.getDemographicNo(),"0.15",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Mesaured patient weight   WT


    	measurements=  getMeasurement("WT",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"78",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WT",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"80",drs.getProviderNo());
    	measurementsDao.persist(measurements);
    	//Height    HT


    	measurements=  getMeasurement("HT",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"176",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Waist Circumfrence    WC


    	measurements=  getMeasurement("WC",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"103",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WC",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"105",drs.getProviderNo());
    	measurementsDao.persist(measurements);
    	//BP    bP

    	measurements=  getMeasurement("BP",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"145/140",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"91/90",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Dilated Eye Exam  EYEE
    	measurements=  getMeasurement("EYEE",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EYEE",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Foot Exam    FTE
    	measurements=  getMeasurement("FTE",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FTE",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Neurological Exam  PANE
    	measurements=  getMeasurement("PANE",getDate("2010-01-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PANE",getDate("2011-01-11"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counselling completed - nutrition    MCCN
    	measurements=  getMeasurement("MCCN",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCN",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counselling completed - excersize    MCCE
    	measurements=  getMeasurement("MCCE",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCE",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counselling completed - Smoking Cessation  MCCS
    	measurements=  getMeasurement("MCCS",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCS",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counselling - other   MCCO
    	measurements=  getMeasurement("MCCO",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCO",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//collaborative Goal      CGSD
    	measurements=  getMeasurement("MCCE",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Goals Text 123",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCE",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Goals Text 456",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//self management     SMCD
    	measurements=  getMeasurement("SMCD",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMCD",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//education dm   DMME
    	measurements=  getMeasurement("DMME",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("DMME",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//#of hypoglcemic    HYPE
    	measurements=  getMeasurement("HYPE",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"9",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HYPE",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"10",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//self mon bg    SMBG
    	measurements=  getMeasurement("SMBG",getDate("2010-06-01"),silviuSilver.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMBG",getDate("2011-03-01"),silviuSilver.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);






    	Demographic gordonGold = getDemographic("Mr","Gold", "Gordon","9999999993", "GG", "1990", "01", "01", "M", address, city, province, postal, phone, "AC", "RO", drs.getProviderNo());
    	gordonGold.setRosterDate(getDate("2008-02-17"));
    	demographicDao.save(gordonGold);
    	admissionDao.saveAdmission(getAdmission(gordonGold,referenceDate, oscarProgramID));



    	//DRK Patient

    	/*
    	Patient: Mrs Debbie Diabetes; OHN: 1111111124, Version Code: ZE, Sex F, Address Type: Home, Street Address: 304-17 Ellerslie Ave, Municipality: North York, Province/State: Ontario, Postal Code: M3C 4M5, Home Telephone: 416-555-7459.
DOB: 63 years old as of the Validation Execution
Family History:Mother  Type 2 DM, died at 71 yo
Ongoing Problems:Hypertension, age at onset 60,Type 2 Diabetes, age at onset 55 Seasonal Rhinitis, age at onset 50 Depression (diagnosed after todays visit)
Drug Allergies:  Amoxicillin  mild rash
Adverse Reactions: Codeine  xs GI upset
Past Medical and Surgical History:	TAH  BSO at 48 yo (menometrorrhagia)
    	 */

    	Demographic debbieDiabetes = getDemographic("Mrs","Diabetes", "Debbie","1111111124", "ZE", justYear(sixtyThreeYearsAgoPlusTwoWeeks), justMonth(sixtyThreeYearsAgoPlusTwoWeeks), justDay(sixtyThreeYearsAgoPlusTwoWeeks), "F", "304-17 Ellerslie Ave", "North York", "Ontario", "M3C 4M5", "416.555.7459", "AC", "NR", drk.getProviderNo());
    	demographicDao.save(debbieDiabetes);
    	admissionDao.saveAdmission(getAdmission(debbieDiabetes,referenceDate, oscarProgramID));


    	//Family History: Mother had Alzheimers, onset age 70.
        CaseManagementIssue caseManagementIssueDebbieDiabetesFamHxAlzh = getCaseMangementIssue(debbieDiabetes,famHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssueDebbieDiabetesFamHxAlzh);
        CaseManagementNote debbieDiabetesFamHxAlzeimersNote = getCaseManagementNote(tenYearsAgo,debbieDiabetes,drk.getProviderNo(),"Mother diagnosed with Alzheimer at age 71",oscarProgramID, caseManagementIssueDebbieDiabetesFamHxAlzh);
        caseManagementNoteDAO.saveNote(debbieDiabetesFamHxAlzeimersNote);

        CaseManagementNoteExt  debbieDiabetesFamilyHistoryAlzeimersNoteExt = new CaseManagementNoteExt();
        debbieDiabetesFamilyHistoryAlzeimersNoteExt.setNoteId(caseManagementIssueJeremyDoeFamHxAlzh.getId());
        debbieDiabetesFamilyHistoryAlzeimersNoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        debbieDiabetesFamilyHistoryAlzeimersNoteExt.setValue("71");

        caseManagementNoteExtDAO.save(debbieDiabetesFamilyHistoryAlzeimersNoteExt);
        //Type 2 Diabetes, age at onset 55
        dxResearchDAO.save(getDxResearch("icd9","250","A",tenYearsAgo,tenYearsAgo,debbieDiabetes.getDemographicNo()));
		CaseManagementIssue caseManagementIssuedebbieDiabetesDM = getCaseMangementIssue(debbieDiabetes,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuedebbieDiabetesDM);
		CaseManagementNote debbieDiabetesDMNote =getCaseManagementNote(tenYearsAgo,debbieDiabetes,drk.getProviderNo(),"Type 2 Diabetes",oscarProgramID, caseManagementIssuedebbieDiabetesDM);
		caseManagementNoteDAO.saveNote(debbieDiabetesDMNote);


        CaseManagementNoteExt  debbieDiabetesOngoingConcerndm2NoteExt = new CaseManagementNoteExt();
        debbieDiabetesOngoingConcerndm2NoteExt.setNoteId(debbieDiabetesDMNote.getId());
        debbieDiabetesOngoingConcerndm2NoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        debbieDiabetesOngoingConcerndm2NoteExt.setValue("55");
        caseManagementNoteExtDAO.save(debbieDiabetesOngoingConcerndm2NoteExt);

        //Hypertension, age at onset 60

        CaseManagementIssue caseManagementIssuedebbieDiabetesHTN = getCaseMangementIssue(debbieDiabetes,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuedebbieDiabetesHTN);
		CaseManagementNote debbieDiabetesHTNNote =getCaseManagementNote(tenYearsAgo,debbieDiabetes,drk.getProviderNo(),"Hypertension",oscarProgramID, caseManagementIssuedebbieDiabetesHTN);
		caseManagementNoteDAO.saveNote(debbieDiabetesHTNNote);


        CaseManagementNoteExt  debbieDiabetesOngoingConcernHTNNoteExt = new CaseManagementNoteExt();
        debbieDiabetesOngoingConcernHTNNoteExt.setNoteId(debbieDiabetesDMNote.getId());
        debbieDiabetesOngoingConcernHTNNoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        debbieDiabetesOngoingConcernHTNNoteExt.setValue("55");
        caseManagementNoteExtDAO.save(debbieDiabetesOngoingConcernHTNNoteExt);

        //Seasonal Rhinitis, age at onset 50

        CaseManagementIssue caseManagementIssuedebbieSeasonalRhinitis = getCaseMangementIssue(debbieDiabetes,concerns,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuedebbieSeasonalRhinitis);
		CaseManagementNote debbieDiabetesSeasonalRhinitisNote =getCaseManagementNote(tenYearsAgo,debbieDiabetes,drk.getProviderNo(),"Seasonal Rhinitis",oscarProgramID, caseManagementIssuedebbieSeasonalRhinitis);
		caseManagementNoteDAO.saveNote(debbieDiabetesSeasonalRhinitisNote);


        CaseManagementNoteExt  debbieDiabetesOngoingConcernSeasonalRhinitisNoteExt = new CaseManagementNoteExt();
        debbieDiabetesOngoingConcernSeasonalRhinitisNoteExt.setNoteId(debbieDiabetesDMNote.getId());
        debbieDiabetesOngoingConcernSeasonalRhinitisNoteExt.setKeyVal(CaseManagementNoteExt.AGEATONSET);
        debbieDiabetesOngoingConcernSeasonalRhinitisNoteExt.setValue("50");
        caseManagementNoteExtDAO.save(debbieDiabetesOngoingConcernSeasonalRhinitisNoteExt);


        //TAH

        CaseManagementIssue caseManagementIssuedebbieTAH = getCaseMangementIssue(debbieDiabetes,medHistory,oscarProgramID,"nurse");
        caseManagementIssueDAO.saveIssue(caseManagementIssuedebbieTAH);
		CaseManagementNote debbieDiabetesTAHNote =getCaseManagementNote(tenYearsAgo,debbieDiabetes,drk.getProviderNo(),"TAH - BSO at 48 yo (menometrorrhagia)",oscarProgramID, caseManagementIssuedebbieTAH);
		caseManagementNoteDAO.saveNote(debbieDiabetesTAHNote);

		//Drug Allergies: Amoxicillin mild rash
		allergy = getAllergy(""+debbieDiabetes.getDemographicNo(),tenYearsAgo,"AMINOPENICILLINS",0,0,0,0,10,"mild rash","42228",false,null,"1","1",null,null);
        allergyDao.persist(allergy);

		//Adverse Reactions:Codeine  xs GI upset
		allergy = getAllergy(""+debbieDiabetes.getDemographicNo(),tenYearsAgo,"CODEINE 30MG TAB",0,0,0,0,13,"xs GI upset","4704",false,null,"1","1",null,null);
        allergyDao.persist(allergy);

    	String eightMonthValidationVisit = "Reason for Visit: Diabetes F/U \n\nSubjective:\nPatient states she is feeling well  excited to share some good news\nSmoking Status:quit smoking 3 weeks ago\nSelf Monitoring BG: 1x/d\n2 hr PC BG: Glucometer reading today 11 mmol/L\nHypoglycemic Episodes:none\nCollaborative Goal Setting: Excercise 20min 5x/wk, reports up to 20 min 3x/wk\nSelf Management Challenges:Excess salt in diet\nMedications: reviewed, compliant, except Lipitor left at cottage, missed one wk, Continues with daily ASA. No medication S/E reported.\n\nObjective:\nBP: 140/80; HR:72 reg.; Waist Circumference 80cm; Ht: 170cm; Wt:80 kg; BMI:27.7\nH&N: HEENT normal\nChest: clear\nCVS: Normal HS,no murmurs\nAbdo: Soft, normal BS, no masses, tenderness, or bruits\n Foot Exam: skin intact, good nail care,ppp equal, good cap refill\nNeurological Exam: 10-g monofilament - normal bilat\nLabs reiewed labs ordered "+getDate(eightMonthsAgo,Calendar.MONTH,-3)+" - A1C, FPG not at target\n\nAssessment:\nDM F/U - BP, FPG,HbA1C, BMI not at target, Glucometer Calibration and eye exam due, \nPatient reluctant to increase Metformin due to concerns regarding GI S/E.\n\nPlan:\nBP MgmtL Increase to Vasotec 10 mg qd #30 rpt 6; rpt 6; RN visit for BP check2 wks; lab req for lytes and eGFR in 10 days\nDM Mgmt:R/O Metformin; Add Glyburide 5 mg qd #30 rpt 6, use and S/E reviewed. Script Provided.\nEducation Nutrition:agrees to increase nutrional compliance\nGlucometer Calibration with fasting lab visit\nBook F/U DM visit 3/12 in three months, with fasting lab and ACR one wk prior.\nReferred for retinal exam";

    	CaseManagementNote debbieDiabetesEightMonthVisit = getCaseManagementNote(eightMonthsAgo,debbieDiabetes,drk.getProviderNo(),eightMonthValidationVisit,oscarProgramID, null);
    	//coleenCopperNote2.setAppointmentNo(febFirstApp.getId());
    	//coleenCopperNote2.setCreate_date(getDate("2010-02-01"));
    	caseManagementNoteDAO.saveNote(debbieDiabetesEightMonthVisit);

    	String fourMonthValidationVisit = "Reason for Visit: BP Check\nRN Visit\n\n\nSubjective:Self Monitoring BG: 2x/d\nSmoking Status: no\nCollaborative Goal Setting: Increase exercise to 25min 5x/wk\nHypoglycemic Episodes: 0\n\nMedication: States no concerns noted with increase in Vasotec, continues with all other medications as prescribed\n\nObjective:BP: 120/70  regular size cuff, left arm, sitting;	HR 78 reg.\nWaist Circumference: 75 cm ; Ht: 170 cm; Wt: 75 kg; BMI: 26.0\nFoot Exam: normal\nNeurological Exam: 128 Hz tuning fork D1 normal\n\nAssessment:Type 2 DMBP now at target; \nLab review: electrolytes, eGFR remain within normal limits.\n\nPlan:Lipid Mgmt: R/O Lipitor 20 mg once daily.\n BP Mgmt: Hydrochlorothiazid 25 mg, QID\nPatient advised re: above; F/U with PCP 3 months as instructed on "+eightMonthsAgo+"\nSelf-mgmt goal reinforced.";

    	CaseManagementNote debbieDiabetesFourMonthVisit = getCaseManagementNote(fourMonthsAgo,debbieDiabetes,drk.getProviderNo(),fourMonthValidationVisit,oscarProgramID, null);
    	//coleenCopperNote2.setAppointmentNo(febFirstApp.getId());
    	//coleenCopperNote2.setCreate_date(getDate("2010-02-01"));
    	caseManagementNoteDAO.saveNote(debbieDiabetesFourMonthVisit);


    	Date eightMinus3 = getDate(eightMonthsAgo,Calendar.MONTH,-3);
    	Date eightMinus16 = getDate(eightMonthsAgo,Calendar.MONTH,-16);
    	Date referenceMinus1Week = getDate(referenceDate,Calendar.WEEK_OF_YEAR,-1);
    	Date fourMinus1Week = getDate(fourMonthsAgo,Calendar.WEEK_OF_YEAR,-1);

    	//Self Monitoring BG (Yes/No)
    	measurements=  getMeasurement("SMBG",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drk.getProviderNo());
    	measurements.setComments("1x/d");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMBG",eightMinus3,debbieDiabetes.getDemographicNo(),"No",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMBG",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drk.getProviderNo());
    	measurements.setComments("4x/wk");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMBG",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drk.getProviderNo());
    	measurements.setComments("2x/d");
    	measurementsDao.persist(measurements);

    	//"# Of Hypoglycemic Episodes (since last assessed)

    	measurements=  getMeasurement("HYPE",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"0",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HYPE",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"0",drk.getProviderNo());
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("HYPE",eightMinus3,debbieDiabetes.getDemographicNo(),"3",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HYPE",eightMinus16,debbieDiabetes.getDemographicNo(),"4",drk.getProviderNo());
    	measurementsDao.persist(measurements);



    	//Blood Pressure
    	measurements=  getMeasurement("BP",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"120/70",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"140/80",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",eightMinus3,debbieDiabetes.getDemographicNo(),"150/80",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BP",eightMinus16,debbieDiabetes.getDemographicNo(),"160/90",drk.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Height
    	measurements=  getMeasurement("HT",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"170",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HT",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"170",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HT",eightMinus16,debbieDiabetes.getDemographicNo(),"170",drk.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Weight
    	measurements=  getMeasurement("WT",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"75",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WT",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"80",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WT",eightMinus16,debbieDiabetes.getDemographicNo(),"85",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	//BMI (Body Mass Index)
    	measurements=  getMeasurement("BMI",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"26",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BMI",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"27.7",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("BMI",eightMinus16,debbieDiabetes.getDemographicNo(),"29.4",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Waist Circumference
    	measurements=  getMeasurement("WC",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"75",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WC",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"80",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("WC",eightMinus16,debbieDiabetes.getDemographicNo(),"85",drk.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Smoking Status (Yes/No)

    	measurements=  getMeasurement("SKST",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SKST",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SKST",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Heavy Smoker");
    	measurementsDao.persist(measurements);

    	//Smoking Packs per Day
    	measurements=  getMeasurement("POSK",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("POSK",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("POSK",eightMinus16,debbieDiabetes.getDemographicNo(),"2",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Dilated Eye Exam (Retinal Exam)
    	measurements=  getMeasurement("EYEE",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Referred");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EYEE",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EYEE",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Foot Exam    FTE
    	measurements=  getMeasurement("FTE",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FTE",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Completed");
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("FTE",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FTE",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);


    	//Neurological Exam:  10-g monofilament  or  128 Hz tuning fork D1

    	measurements=  getMeasurement("PANE",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PANE",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("PANE",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("PANE",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurements.setComments("Normal");
    	measurementsDao.persist(measurements);

    	//Fasting Glucose Meter  lab result comparison (Calibrated Yes/No)
    	measurements=  getMeasurement("FGLC",eightMinus3,debbieDiabetes.getDemographicNo(),"No",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FGLC",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Education  Diabetes (Yes/No) DMME
    	measurements=  getMeasurement("DMME",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("DMME",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Education  Nutrition (lipids) (Yes/No) EDNL
    	measurements=  getMeasurement("EDNL",eightMinus3,debbieDiabetes.getDemographicNo(),"YEs",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EDNL",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Education  Nutrition (diabetes) (Yes/No) EDND
    	measurements=  getMeasurement("EDND",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EDND",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counseling Completed  Nutrition (Yes/No) MCCN
    	measurements=  getMeasurement("MCCN",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counseling Completed  Exercise (Yes/No) MCCE
    	measurements=  getMeasurement("MCCE",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCE",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Motivational Counseling Completed  Smoking Cessation (Yes/No) MCCS
    	measurements=  getMeasurement("MCCS",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("MCCS",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Motivational Counseling  Other (Yes/No) MCCO
    	measurements=  getMeasurement("MCCO",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Collaborative Goal Setting/Self Management Goals (Indicate Goal) CGSD

    	measurements=  getMeasurement("CGSD",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"Exercise to 25min 5x/wk",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CGSD",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Excersize to 20min 5x/wk",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("CGSD",eightMinus3,debbieDiabetes.getDemographicNo(),"Increase excercise to 20min 5x/wk",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("CGSD",eightMinus16,debbieDiabetes.getDemographicNo(),"Agrees to self monitoring of glucose 2x/wk",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Self Management Challenges/Barriers to Self Management (Indicate Challenge) SMCD
    	measurements=  getMeasurement("SMCD",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Excess salt in diet",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("SMCD",eightMinus3,debbieDiabetes.getDemographicNo(),"Financial - glucometer supplies",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("SMCD",eightMinus16,debbieDiabetes.getDemographicNo(),"Financial - glucometer supplies",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//ASA (Acetylsalicylic acid) Use (Yes/No) ASAU
    	measurements=  getMeasurement("ASAU",fourMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("ASAU",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("ASAU",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("ASAU",eightMinus16,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Influenza Vaccine
    	prevention = getPrevention(eightMinus3,"FLU",debbieDiabetes.getDemographicNo(),drk.getProviderNo(),false,false);
    	preventionDao.persist(prevention);
    	prevention = getPrevention(eightMinus16,"FLU",debbieDiabetes.getDemographicNo(),drk.getProviderNo(),false,false);
    	preventionDao.persist(prevention);
    	//Pneumococcal Vaccine  Pneumovax
    	prevention = getPrevention(eightMinus3,"Pneumovax",debbieDiabetes.getDemographicNo(),drk.getProviderNo(),false,false);
    	preventionDao.persist(prevention);
    	//Erectile Dysfunction EDGI
    	measurements=  getMeasurement("EDGI",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//ECG ECG
    	measurements=  getMeasurement("ECG",eightMinus3,debbieDiabetes.getDemographicNo(),"Yes",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Psychosocial  Screening TODO: where does this go?

    	//referenceMinus1Week
    	//fourMinus1Week
    	//HbA1C (Glycated haemoglobin) A1C

    	measurements=  getMeasurement("A1C",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"6.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("A1C",eightMinus3,debbieDiabetes.getDemographicNo(),"8.0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("A1C",eightMinus16,debbieDiabetes.getDemographicNo(),"9.0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Fasting Plasma Glucose/AC (or Preprandial Glucose) FBS
    	measurements=  getMeasurement("FBS",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"5.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FBS",eightMinus3,debbieDiabetes.getDemographicNo(),"7.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FBS",eightMinus16,debbieDiabetes.getDemographicNo(),"8.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//2 hr PC BG FBPC
    	measurements=  getMeasurement("FBPC",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"6",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	measurements=  getMeasurement("FBPC",eightMonthsAgo,debbieDiabetes.getDemographicNo(),"11",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FBPC",eightMinus3,debbieDiabetes.getDemographicNo(),"10",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("FBPC",eightMinus16,debbieDiabetes.getDemographicNo(),"15",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//LDL-C LDL
    	measurements=  getMeasurement("LDL",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"3.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("LDL",eightMinus3,debbieDiabetes.getDemographicNo(),"2.0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("LDL",eightMinus16,debbieDiabetes.getDemographicNo(),"4.0",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//HDL-C HDL
    	measurements=  getMeasurement("HDL",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"4.1",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HDL",eightMinus3,debbieDiabetes.getDemographicNo(),"4.2",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("HDL",eightMinus16,debbieDiabetes.getDemographicNo(),"4.2",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//TC:HDL-C Ratio TCHD
    	measurements=  getMeasurement("TCHD",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"1.7",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("TCHD",eightMinus3,debbieDiabetes.getDemographicNo(),"1.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("TCHD",eightMinus16,debbieDiabetes.getDemographicNo(),"3.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	//Triglycerides TG
    	measurements=  getMeasurement("TG",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"1.4",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("TG",eightMinus3,debbieDiabetes.getDemographicNo(),"1.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("TG",eightMinus16,debbieDiabetes.getDemographicNo(),"2.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);


    	//Random Urinary ACR (Albumin to Creatinine Ratio) ACR
    	measurements=  getMeasurement("ACR",referenceMinus1Week,debbieDiabetes.getDemographicNo(),"2.4",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("ACR",eightMinus3,debbieDiabetes.getDemographicNo(),"2.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("ACR",eightMinus16,debbieDiabetes.getDemographicNo(),"3.5",drs.getProviderNo());
    	measurementsDao.persist(measurements);



    	//eGFR EGFR
    	measurements=  getMeasurement("EGFR",fourMinus1Week,debbieDiabetes.getDemographicNo(),"55",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EGFR",eightMinus3,debbieDiabetes.getDemographicNo(),"55",drs.getProviderNo());
    	measurementsDao.persist(measurements);

    	measurements=  getMeasurement("EGFR",eightMinus16,debbieDiabetes.getDemographicNo(),"65",drs.getProviderNo());
    	measurementsDao.persist(measurements);







	}







}
