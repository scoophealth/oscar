/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.dao.CdsHospitalisationDaysDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FunctionalCentreAdmissionDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.common.model.CdsHospitalisationDays;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.common.model.FunctionalCentreAdmission;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;

public final class Cds4ReportUIBean {

	private static Logger logger = MiscUtils.getLogger();

	private static FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private static CdsHospitalisationDaysDao cdsHospitalisationDaysDao = (CdsHospitalisationDaysDao) SpringUtils.getBean("cdsHospitalisationDaysDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static FunctionalCentreAdmissionDao functionalCentreAdmissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
	
	public static final int NUMBER_OF_COHORT_BUCKETS = 11;
	private static final int NUMBER_OF_DATA_ROW_COLUMNS=NUMBER_OF_COHORT_BUCKETS+1;

	enum MinMax {
		MIN, MAX
	}

	public static class SingleMultiAdmissions {
		// key=clientId
		public HashMap<Integer, CdsClientForm> singleAdmissions = new HashMap<Integer, CdsClientForm>();

		// this is a map where key=0-10 representing each cohort bucket., value is a collection of CdsClientForms
		public MultiValueMap singleAdmissionCohortBuckets = new MultiValueMap();

		// key=clientId
		public HashMap<Integer, CdsClientForm> multipleAdmissionsLatestForms = new HashMap<Integer, CdsClientForm>();
		
		public ArrayList<CdsClientForm> multipleAdmissionsAllForms = new ArrayList<CdsClientForm>();

		// this is a map where key=0-10 representing each cohort bucket., value is a collection of CdsClientForms
		public MultiValueMap multipleAdmissionCohortBuckets = new MultiValueMap();
		
		public void addUniqueCdsProgramFormToMultipleAdmissionsAllForms(CdsClientForm cdsClientForm)
		{
			for (CdsClientForm tempForm : multipleAdmissionsAllForms)
			{
				if (tempForm.getCdsFormVersion().equals(cdsClientForm.getCdsFormVersion()) && tempForm.getAdmissionId().equals(cdsClientForm.getAdmissionId()))
				{
					if (cdsClientForm.getCreated().after(tempForm.getCreated()))
					{
						multipleAdmissionsAllForms.remove(tempForm);
						multipleAdmissionsAllForms.add(cdsClientForm);
						return;
					}
					// else do nothing and keep the old one.
				}
			}
		}
	}

	/** key=admissionId, value=admission */
	private HashMap<Integer, FunctionalCentreAdmission> admissionMap = null;
	private SingleMultiAdmissions singleMultiAdmissions=null;
	private FunctionalCentre functionalCentre=null;
	private GregorianCalendar startDate=new GregorianCalendar();
	private GregorianCalendar endDateExclusive=new GregorianCalendar();
	private HashSet<String> providerIdsToReportOn=null;
	private HashSet<Integer> programIdsToReportOn=null;
	private LoggedInInfo loggedInInfo=null;
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public Cds4ReportUIBean(LoggedInInfo loggedInInfo, String functionalCentreId, Date startDate, Date endDateInclusive, String[] providerIdList, HashSet<Integer> programIds) {

		this.loggedInInfo=loggedInInfo;
		this.startDate.setTime(startDate);
		this.endDateExclusive.setTime(endDateInclusive);
		this.endDateExclusive.add(GregorianCalendar.DAY_OF_YEAR, 1); // add 1 to make exclusive
		
		// put providerId's in a Hash for quicker searches.
		if (providerIdList!=null)
		{
			providerIdsToReportOn=new HashSet<String>();
			for (String s : providerIdList)
			{
				providerIdsToReportOn.add(s);
			}
		}
		
		programIdsToReportOn=programIds;
		
		functionalCentre=functionalCentreDao.find(functionalCentreId);
		
		admissionMap=getAdmissionMap();
		
		singleMultiAdmissions = getAdmissionsSortedSingleMulti();
	}
	
	public String getFunctionalCentreDescription()
	{
		return(StringEscapeUtils.escapeHtml(functionalCentre.getAccountId()+", "+functionalCentre.getDescription()));
	}
	
	public String getDateRangeForDisplay() 
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar displayEndDate=(GregorianCalendar) endDateExclusive.clone();
		displayEndDate.add(GregorianCalendar.DAY_OF_YEAR, -1);
		return(StringEscapeUtils.escapeHtml(simpleDateFormat.format(startDate.getTime())+" to "+simpleDateFormat.format(displayEndDate.getTime())+" (inclusive)"));
	}

	public static List<CdsFormOption> getCdsFormOptions()
	{
		return(cdsFormOptionDao.findByVersion("4"));
	}
	
	private SingleMultiAdmissions getAdmissionsSortedSingleMulti() {
		SingleMultiAdmissions singleMultiAdmissions = new SingleMultiAdmissions();

		List<CdsClientForm> cdsForms = cdsClientFormDao.findSignedCdsForms(loggedInInfo.getCurrentFacility().getId(), "4", startDate.getTime(), endDateExclusive.getTime());
		logger.debug("valid cds form count, "+loggedInInfo.getCurrentFacility().getId()+", 4, "+startDate.getTime()+", "+endDateExclusive.getTime()+", "+cdsForms.size());
		
		// sort into single and multiple admissions
		for (CdsClientForm form : cdsForms) {
			logger.debug("valid cds form, id="+form.getId());
			
			// make sure form is for an admission for which we're interested, i.e. admissions are filtered by program and admission time already
			FunctionalCentreAdmission admission = admissionMap.get(form.getAdmissionId());
			if (admission == null) {
				logger.debug("cds form missing admission / or not in admission we're interested in dueto program or time restriction. formId="+form.getId()+", admissionId="+form.getAdmissionId());
				continue;
			}
			else
			{
				// check if the cds form is signed by a provider we're reporting on.
				if (providerIdsToReportOn!=null) // if we've been asked to filter
				{
					if (!providerIdsToReportOn.contains(form.getProviderNo())) // if the provider isn't in our allowed list
					{
						continue;
					}
				}
			}
			
			Integer clientId = form.getClientId();

			CdsClientForm existingForm = singleMultiAdmissions.multipleAdmissionsLatestForms.get(clientId);
			// if this person already has multiple admissions
			if (existingForm != null) {
				logger.debug("multiple admissions 3+ forms. formId="+form.getId()+", otherFormId="+existingForm.getId());
				singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
				singleMultiAdmissions.addUniqueCdsProgramFormToMultipleAdmissionsAllForms(form);
			} else // this person either has one previous or no previous admissions
			{
				existingForm = singleMultiAdmissions.singleAdmissions.get(clientId);
				// this means we have 1 previous admission
				if (existingForm != null) {
					
					// check that it's actually multiple admission and not just multiple forms for 1 admission
					
					// they are the same, just pick the newer form
					if (existingForm.getAdmissionId().equals(form.getAdmissionId()))
					{
						if (form.getCreated().after(existingForm.getCreated()))
						{
							singleMultiAdmissions.singleAdmissions.put(clientId, form);
						}
					}
					else
					{
						logger.debug("multiple admissions, 2 forms : formId="+form.getId()+", otherFormId="+existingForm.getId());
						
						singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
						singleMultiAdmissions.singleAdmissions.remove(clientId);
	
						singleMultiAdmissions.addUniqueCdsProgramFormToMultipleAdmissionsAllForms(existingForm);
						singleMultiAdmissions.addUniqueCdsProgramFormToMultipleAdmissionsAllForms(form);
					}
				} else // we have no previous admission
				{
					logger.debug("single admissions : formId="+form.getId());
					singleMultiAdmissions.singleAdmissions.put(clientId, form);
				}
			}
		}

		// sort single admissions into cohort buckets
		for (CdsClientForm form : singleMultiAdmissions.singleAdmissions.values()) {
			FunctionalCentreAdmission admission = admissionMap.get(form.getAdmissionId());
			int bucket = getCohortBucket(admission);
			singleMultiAdmissions.singleAdmissionCohortBuckets.put(bucket, form);
			
			logger.debug("cds form id="+form.getId()+", admission="+admission.getId()+", cohort bucket="+bucket);
		}

		// sort multiple admissions into cohort buckets
		for (CdsClientForm form : singleMultiAdmissions.multipleAdmissionsAllForms) {
			FunctionalCentreAdmission admission = admissionMap.get(form.getAdmissionId());
			int bucket = getCohortBucket(admission);
			singleMultiAdmissions.multipleAdmissionCohortBuckets.put(bucket, form);
			
			logger.debug("cds form id="+form.getId()+", admission="+admission.getId()+", cohort bucket="+bucket);
		}

		return(singleMultiAdmissions);
	}

	private HashMap<Integer, FunctionalCentreAdmission> getAdmissionMap() {

		// put admissions into map so it's easier to retrieve by id.
		HashMap<Integer, FunctionalCentreAdmission> admissionMap = new HashMap<Integer, FunctionalCentreAdmission>();

		List<Program> programs=programDao.getProgramsByFacilityIdAndFunctionalCentreId(loggedInInfo.getCurrentFacility().getId(), functionalCentre.getId());
		
		for (Program program : programs) {
			if (programIdsToReportOn!=null && !programIdsToReportOn.contains(program.getId())) continue;
			
			//List<Admission> admissions = admissionDao.getAdmissionsByProgramAndDate(program.getId(), startDate.getTime(), endDateExclusive.getTime());
			//Instead use functaionalCentreAdmission
			List<FunctionalCentreAdmission> fcAdmissions = functionalCentreAdmissionDao.getAllAdmissionsByFunctionalCentreIdAndDates(program.getFunctionalCentreId(), startDate.getTime(), endDateExclusive.getTime());
			logger.debug("corresponding cds admissions count (before provider filter) :"+fcAdmissions.size());

			for (FunctionalCentreAdmission admission : fcAdmissions) {
				admissionMap.put(admission.getId().intValue(), admission);
			}
		}

		return admissionMap;
	}
	
	private boolean isAdmissionForSelectedProviders(Admission admission)
	{
		if (providerIdsToReportOn==null) return(true);
		
		return(providerIdsToReportOn.contains(admission.getProviderNo()));
	}

	private static int getCohortBucket(FunctionalCentreAdmission admission) {
		if (admission==null) return(0);
		
		Date dischargeDate = new Date(); // default duration calculation to today if not discharged.
		if (admission.getDischargeDate() != null) dischargeDate = admission.getDischargeDate();

		//The cohort dates should be based on service initiation date
		int years = DateUtils.yearDifference(admission.getServiceInitiationDate(), dischargeDate);
		if (years > 10) years = 10; // limit everything above 10 years to the 10 year bucket.

		return(years);
	}

	private static CdsClientForm getNewerForm(CdsClientForm form1, CdsClientForm form2) {
		if (form1.getCreated().after(form2.getCreated())) return(form1);
		else return(form2);
	}

	
	public int[] getDataRow(CdsFormOption cdsFormOption) {
		if (cdsFormOption.getCdsDataCategory().startsWith("007-")) return(get007DataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("07a-")) return(get07aDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("008-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("009-")) return(get009DataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("010-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("10a-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("10b-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("011-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("012-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("013-")) return(getGenericAllAnswersDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("014-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("015-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("016-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("16a-")) return(getGenericAllAnswersDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("017-")) return(getGenericAllAnswersDataLine(cdsFormOption));
		//else if (cdsFormOption.getCdsDataCategory().startsWith("018-")) return(getGenericAllAnswersDataLineForNewAdmissionsDuringReportingPerdiod(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("018-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("019-")) return(getGenericAllAnswersDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("020-")) return(get020DataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("021-")) return(get021DataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("022-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("023-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("024-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("24a-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("025-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("25a-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("026-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("027-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("028-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("029-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("29a-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("030-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("031-")) return(getGenericLatestAnswerDataLine(cdsFormOption));
		else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}

	private static int[] getNotAvailableDataLine() {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];
		for (int i=0;i<dataRow.length; i++) dataRow[i]=-1;
		return(dataRow);		
	}

	private int[] get007DataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=getNotAvailableDataLine();

		if ("007-01".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countAnonymous(singleMultiAdmissions.multipleAdmissionsLatestForms.values(), false);
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countAnonymous(bucket, false);
			}			
		} else if ("007-02".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countNoAdmission(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countNoAdmission(bucket);
			}			
		} else if ("007-03".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countAnonymous(singleMultiAdmissions.multipleAdmissionsLatestForms.values(), true);
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countAnonymous(bucket, true);
			}			
		} else if ("007-04".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=singleMultiAdmissions.multipleAdmissionsLatestForms.size();
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				int size = 0;

				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.multipleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) size = bucket.size();
				else size=0;

				dataRow[i+1]=size;
			}
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
		}

		return(dataRow);
	}

	private int countAnonymous(Collection<CdsClientForm> bucket, boolean isAnonymous) {
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				Demographic demographic=demographicDao.getDemographicById(form.getClientId());
				if (isAnonymous && demographic.getAnonymous()!=null) count++;
				else if (!isAnonymous && demographic.getAnonymous()==null) count++;
			}
		}
		
	    return(count);
    }

	private int countNoAdmission(Collection<CdsClientForm> bucket) {
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				if (form.getAdmissionId()==null) count++;
			}
		}
		
	    return(count);
    }

	private int[] get07aDataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=getNotAvailableDataLine();

		if ("07a-01".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countWaitingForAssesment(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countWaitingForAssesment(bucket);
			}			
		} else if ("07a-02".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countDaysWaitingForAssesment(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countDaysWaitingForAssesment(bucket);
			}
		} else if ("07a-03".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countWaitingForService(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countWaitingForService(bucket);
			}
		} else if ("07a-04".equals(cdsFormOption.getCdsDataCategory())) {
			dataRow[0]=countDaysWaitingForService(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=countDaysWaitingForService(bucket);
			}
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
		}
					
		return(dataRow);
	}
	
	private int countWaitingForAssesment(Collection<CdsClientForm> bucket) {
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				Date initialContactDate=form.getInitialContactDate();
				if (initialContactDate!=null && initialContactDate.before(endDateExclusive.getTime()))
				{
					Date assessmentDate=form.getAssessmentDate();
					if (assessmentDate==null || assessmentDate.after(endDateExclusive.getTime())) count++;
				}
			}
		}
		
	    return(count);
    }

	private int countDaysWaitingForAssesment(Collection<CdsClientForm> bucket) {
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				Date initialContactDate=form.getInitialContactDate();
				if (initialContactDate!=null && initialContactDate.before(endDateExclusive.getTime()))
				{
					Date assessmentDate=form.getAssessmentDate();
					Date endCountDate=null;
					
					if (assessmentDate==null) endCountDate=endDateExclusive.getTime();
					else if (assessmentDate.after(endDateExclusive.getTime())) endCountDate=endDateExclusive.getTime();
					else endCountDate=assessmentDate;

					count=count+(int)(DateUtils.getNumberOfDaysBetweenTwoDates(initialContactDate, endCountDate));
				}
			}
		}
		
	    return(count);
    }

	private int countWaitingForService(Collection<CdsClientForm> bucket) {
	//days wated for service inititaion = service initiation date - assessment date
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				Date assessmentDate=form.getAssessmentDate();
				if (assessmentDate!=null && assessmentDate.before(endDateExclusive.getTime()))
				{
					//Admission admission=admissionMap.get(form.getAdmissionId());					
					//if (admission==null || admission.getAdmissionDate().after(endDateExclusive.getTime())) count++;
					
					Date serviceInitiationDate = form.getServiceInitiationDate();
					if(serviceInitiationDate == null || serviceInitiationDate.after(endDateExclusive.getTime())) count ++;
				}
			}
		}
		
	    return(count);
    }

	private int countDaysWaitingForService(Collection<CdsClientForm> bucket) {
		int count=0;

		if (bucket!=null)
		{
			for (CdsClientForm form : bucket)
			{
				Date assessmentDate=form.getAssessmentDate();
				if (assessmentDate!=null && assessmentDate.before(endDateExclusive.getTime()))
				{	
					//Admission admission=admissionMap.get(form.getAdmissionId());	
					Date serviceInitiationDate = form.getServiceInitiationDate();
					Date endCountDate=null;
					/*
					if (admission==null) endCountDate=endDateExclusive.getTime();
					else if (admission.getAdmissionDate().after(endDateExclusive.getTime())) endCountDate=endDateExclusive.getTime();
					else endCountDate=admission.getAdmissionDate();
					*/
					if(serviceInitiationDate == null)
						endCountDate = endDateExclusive.getTime();
					else if(serviceInitiationDate.after(endDateExclusive.getTime())) 
						endCountDate = endDateExclusive.getTime();
					else
						endCountDate = serviceInitiationDate;
					
					count=count+(int)(DateUtils.getNumberOfDaysBetweenTwoDates(assessmentDate, endCountDate));
				}
			}
		}
		
	    return(count);
    }
	
	private int[] getGenericLatestAnswerDataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=getNotAvailableDataLine();

		// get multiadmissions number
		dataRow[0]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), singleMultiAdmissions.multipleAdmissionsLatestForms.values());

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> cdsForms = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), cdsForms);
		}

		return(dataRow);
	}

	private static int getAnswerCounts(String cdsAnswer, Collection<CdsClientForm> cdsForms) {

		int totalCount = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				CdsClientFormData result = cdsClientFormDataDao.findByAnswer(form.getId(), cdsAnswer);
				if (result != null) totalCount++;
			}
		}

		return(totalCount);
	}

	private int[] get009DataLine(CdsFormOption cdsFormOption) {
		if ("009-01".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(0, 15));
		} else if ("009-02".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(16, 17));
		} else if ("009-03".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(18, 24));
		} else if ("009-04".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(25, 34));
		} else if ("009-05".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(35, 44));
		} else if ("009-06".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(45, 54));
		} else if ("009-07".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(55, 64));
		} else if ("009-08".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(65, 74));
		} else if ("009-09".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(75, 84));
		} else if ("009-10".equals(cdsFormOption.getCdsDataCategory())) {
			return(get009AgeRangeDataLine(85, 200));
		} else if ("009-11".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			dataRow[0]=get009MultipleAdmissionCountByNoAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values());

			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=get009MultipleAdmissionCountByNoAge(bucket);
			}
			
			return(dataRow);		
		} else if ("009-12".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();
			dataRow[0]=getMultipleAdmissionCountByMinMaxAge(MinMax.MIN, singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			populateCohortCountsByMinMaxAge(dataRow, MinMax.MIN);
			return(dataRow);
		} else if ("009-13".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();
			dataRow[0]=getMultipleAdmissionCountByMinMaxAge(MinMax.MAX, singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			populateCohortCountsByMinMaxAge(dataRow, MinMax.MAX);
			return(dataRow);
		} else if ("009-14".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multiple admissions
			dataRow[0]=getMultipleAdmissionCountByAvgAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values());

			// get cohorts
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=getMultipleAdmissionCountByAvgAge(bucket);
			}

			return(dataRow);		
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}
	
	private int[] get009AgeRangeDataLine(int startAge, int endAge) {
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=get009MultipleAdmissionCountByAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values(), startAge, endAge);

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=get009MultipleAdmissionCountByAge(bucket, startAge, endAge);
		}
		
		return(dataRow);		
	}

	private int get009MultipleAdmissionCountByAge(Collection<CdsClientForm> forms, int startAge, int endAge) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = getClientAgeAtReportTime(form.getClientId());
				if (age != null && age >= startAge && age <= endAge) count++;
			}
		}

		return(count);
	}

	private int get009MultipleAdmissionCountByNoAge(Collection<CdsClientForm> forms) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = getClientAgeAtReportTime(form.getClientId());
				if (age == null) count++;
			}
		}

		return(count);
	}

	private int getMultipleAdmissionCountByMinMaxAge(MinMax minMax, Collection<CdsClientForm> forms) {
		Integer minMaxAge = null;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				minMaxAge = nullSafeMinMax(minMax, minMaxAge, getClientAgeAtReportTime(form.getClientId()));
			}
		}

		if (minMaxAge == null) minMaxAge = 0;

		return(minMaxAge);
	}

	private static Integer nullSafeMinMax(MinMax minMax, Integer i1, Integer i2) {
		if (i1 == null) return(i2);
		if (i2 == null) return(i1);

		if (minMax == MinMax.MAX) return(Math.max(i1.intValue(), i2.intValue()));
		else if (minMax == MinMax.MIN) return(Math.min(i1.intValue(), i2.intValue()));
		else throw (new IllegalStateException("okay I missed something : minMax=" + minMax + ", i1=" + i1 + ", i2=" + i2));
	}

	private void populateCohortCountsByMinMaxAge(int[] dataRow, MinMax minMax) {
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=getMultipleAdmissionCountByMinMaxAge(minMax, bucket);
		}
	}

	private  int getMultipleAdmissionCountByAvgAge(Collection<CdsClientForm> forms) {
		int totalPeople = 0;
		int totalAge = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age=getClientAgeAtReportTime(form.getClientId());
				if (age != null) {
					totalPeople++;
					totalAge = totalAge + age;
				}
			}
		}

		int avg = 0;
		if (totalPeople > 0) avg = totalAge / totalPeople;

		return(avg);
	}

	private int[] getGenericAllAnswersDataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), singleMultiAdmissions.multipleAdmissionsAllForms);

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> cdsForms = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), cdsForms);
		}
		
		return(dataRow);		
	}

	private int[] getGenericAllAnswersDataLineForNewAdmissionsDuringReportingPerdiod(CdsFormOption cdsFormOption) {
		// section 18 only counts NEW admissions during that reporting period
		
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), getOnlyNewAdmissionDuringReportPeriod(singleMultiAdmissions.multipleAdmissionsAllForms));

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> cdsForms = getOnlyNewAdmissionDuringReportPeriod(singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i));
			dataRow[i+1]=getAnswerCounts(cdsFormOption.getCdsDataCategory(), cdsForms);
		}
		
		return(dataRow);		
	}

	private ArrayList<CdsClientForm> getOnlyNewAdmissionDuringReportPeriod(Collection<CdsClientForm> forms)
	{
		if (forms==null) return(new ArrayList<CdsClientForm>());
		
		ArrayList<CdsClientForm> results=new ArrayList<CdsClientForm>();
		
		for (CdsClientForm form : forms)
		{
			FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
			
			if (admission==null) continue;
			
			if (startDate.getTime().before(admission.getAdmissionDate()) && endDateExclusive.getTime().after(admission.getAdmissionDate()))
			{
				results.add(form);
			}
		}
		
		return(results);
	}
	
	private int[] get020DataLine(CdsFormOption cdsFormOption) {
		if ("020-01".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			AccumulatorMap<Integer> multipleAdmissionHospitalisations=getCdsHospitalisationAdmissionCount2YearsBeforeProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			dataRow[0]=multipleAdmissionHospitalisations.countInstancesOfValue(0);
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				AccumulatorMap<Integer> singleAdmissionHospitalisations=getCdsHospitalisationAdmissionCount2YearsBeforeProgramAdmission(bucket);
				dataRow[i+1]=singleAdmissionHospitalisations.countInstancesOfValue(0);
			}

			return(dataRow);
		} else if ("020-02".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			AccumulatorMap<Integer> multipleAdmissionHospitalisations=getCdsHospitalisationAdmissionCount2YearsBeforeProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			dataRow[0]=multipleAdmissionHospitalisations.getTotalOfAllValues();
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				AccumulatorMap<Integer> singleAdmissionHospitalisations=getCdsHospitalisationAdmissionCount2YearsBeforeProgramAdmission(bucket);
				dataRow[i+1]=singleAdmissionHospitalisations.getTotalOfAllValues();
			}

			return(dataRow);
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			dataRow[0]=getCdsHospitalisationDaysCount2YearsBeforeProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=getCdsHospitalisationDaysCount2YearsBeforeProgramAdmission(bucket);
			}

			return(dataRow);
		} else if ("020-04".equals(cdsFormOption.getCdsDataCategory())) {
			return(getRefusedHospitalisationLine());
		} else if ("020-05".equals(cdsFormOption.getCdsDataCategory())) {
			return(get020QuestionAvgScalarDataLine("ageOfFirstPsychiatricHospitalization"));
		} else if ("020-06".equals(cdsFormOption.getCdsDataCategory())) {
			return(get020QuestionAvgScalarDataLine("ageOfOnsetOfMentalIllness"));
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}

	private AccumulatorMap<Integer> getCdsHospitalisationAdmissionCount2YearsBeforeProgramAdmission(Collection<CdsClientForm> forms)
	{
		// key = clientId, value = # of hospital admissions
		AccumulatorMap<Integer> admissionHospitalisations=new AccumulatorMap<Integer>();
		
		if (forms!=null)
		{
			for (CdsClientForm form : forms)
			{
				List<CdsHospitalisationDays> hospitalisationDays=getHopitalisationDays2YearsBeforeAdmission(form);
				admissionHospitalisations.increment(form.getClientId(), hospitalisationDays.size());
			}
		}
		
		return(admissionHospitalisations);
	}
	
	private List<CdsHospitalisationDays> getHopitalisationDays2YearsBeforeAdmission(CdsClientForm form)
	{
		FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
		
		GregorianCalendar startBound=(GregorianCalendar) admission.getAdmissionCalendar().clone();
		startBound.add(GregorianCalendar.YEAR, -2); // 2 years prior to admission
		// materialise results
		startBound.getTimeInMillis(); 
		
		return(getHopitalisationDaysDuringPeriod(form, startBound, admission.getAdmissionCalendar()));
	}

	private int[] get020QuestionAvgScalarDataLine(String question) {
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=get020QuestionAvgScalarAnswers(question, singleMultiAdmissions.multipleAdmissionsLatestForms.values());

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=get020QuestionAvgScalarAnswers(question, bucket);
		}
		
		return(dataRow);		
	}

	private static int get020QuestionAvgScalarAnswers(String cdsQuestion, Collection<CdsClientForm> cdsForms) {

		int totalSum = 0;
		int totalPeople = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), cdsQuestion);
				for (CdsClientFormData formData : results) {
					try {
						int temp = Integer.parseInt(formData.getAnswer());
						// -1 means they didn't answer
						if (temp != -1) {
							totalSum = totalSum + temp;
							totalPeople++;
						}
					} catch (NumberFormatException e) {
						logger.error("Expected a number here. answer=" + formData.getAnswer(), e);
					}
				}
			}
		}

		int avg = 0;
		if (totalPeople != 0) avg = totalSum / totalPeople;

		return(avg);
	}

	private int[] get021DataLine(CdsFormOption cdsFormOption) {
		if ("021-01".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			AccumulatorMap<Integer> multipleAdmissionHospitalisations=getCdsHospitalisationAdmissionCountDuringProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			dataRow[0]=multipleAdmissionHospitalisations.countInstancesOfValue(0);
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				AccumulatorMap<Integer> singleAdmissionHospitalisations=getCdsHospitalisationAdmissionCountDuringProgramAdmission(bucket);
				dataRow[i+1]=singleAdmissionHospitalisations.countInstancesOfValue(0);
			}

			return(dataRow);
		} else if ("021-02".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			AccumulatorMap<Integer> multipleAdmissionHospitalisations=getCdsHospitalisationAdmissionCountDuringProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			dataRow[0]=multipleAdmissionHospitalisations.getTotalOfAllValues();
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				AccumulatorMap<Integer> singleAdmissionHospitalisations=getCdsHospitalisationAdmissionCountDuringProgramAdmission(bucket);
				dataRow[i+1]=singleAdmissionHospitalisations.getTotalOfAllValues();
			}

			return(dataRow);
		} else if ("021-03".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=getNotAvailableDataLine();

			// get multi admissions number
			dataRow[0]=getCdsHospitalisationDaysCountDuringProgramAdmission(singleMultiAdmissions.multipleAdmissionsAllForms);
			
			// get cohort numbers
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				dataRow[i+1]=getCdsHospitalisationDaysCountDuringProgramAdmission(bucket);
			}

			return(dataRow);
		} else if ("021-04".equals(cdsFormOption.getCdsDataCategory())) {
			return(getRefusedHospitalisationLine());
		} else if ("021-05".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(1));
		} else if ("021-06".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(2));
		} else if ("021-07".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(3));
		} else if ("021-08".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(4));
		} else if ("021-09".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(5));
		} else if ("021-10".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(6));
		} else if ("021-11".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(7));
		} else if ("021-12".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(8));
		} else if ("021-13".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(9));
		} else if ("021-14".equals(cdsFormOption.getCdsDataCategory())) {
			return(get021HospitalDays(10));
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}
	
	private int[] getRefusedHospitalisationLine()
	{
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=countRefusedHospitalisationInfo(singleMultiAdmissions.multipleAdmissionsLatestForms.values());
		
		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=countRefusedHospitalisationInfo(bucket);
		}

		return(dataRow);
	}
	
	private int countRefusedHospitalisationInfo(Collection<CdsClientForm> cdsForms) {
		int totalCount = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), "refused21");
				if (results.size()>0 && "on".equals(results.get(0).getAnswer())) totalCount++;
			}
		}

		return(totalCount);
	}

	private List<CdsHospitalisationDays> getHopitalisationDaysDuringAdmission(CdsClientForm form)
	{
		FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
		return(getHopitalisationDaysDuringPeriod(form, admission.getAdmissionCalendar(), admission.getDischargeCalendar()));
	}
	
	private List<CdsHospitalisationDays> getHopitalisationDaysDuringPeriod(CdsClientForm form, GregorianCalendar startBound, GregorianCalendar endBound)
	{
		ArrayList<CdsHospitalisationDays> results=new ArrayList<CdsHospitalisationDays>();
		
		List<CdsHospitalisationDays> allHospitalDays=cdsHospitalisationDaysDao.findByClientId(form.getClientId());

		for (CdsHospitalisationDays temp : allHospitalDays)
		{
			// program :  ------- A ------- D --------
			// Hosp    :    A D 
			// Hosp    :*   A           D 
			// Hosp    :*   A                     D
			// Hosp    :*            A  D
			// Hosp    :*            A            D
			// Hosp    :                        A D 
			
			// so if H admission before P discharge && H discharge after P Admission
			if ((endBound ==null || temp.getAdmitted().before(endBound)) &&
				(temp.getDischarged()==null || temp.getDischarged().after(startBound)))
			{
				results.add(temp);
			}
		}
		
		return(results);
	}

	
	private AccumulatorMap<Integer> getCdsHospitalisationAdmissionCountDuringProgramAdmission(Collection<CdsClientForm> forms)
	{
		// key = clientId, value = # of hospital admissions
		AccumulatorMap<Integer> admissionHospitalisations=new AccumulatorMap<Integer>();
		
		if (forms!=null)
		{
			for (CdsClientForm form : forms)
			{
				List<CdsHospitalisationDays> hospitalisationDays=getHopitalisationDaysDuringAdmission(form);
				admissionHospitalisations.increment(form.getClientId(), hospitalisationDays.size());
			}
		}
		
		return(admissionHospitalisations);
	}
	
	private int getCdsHospitalisationDaysCountDuringProgramAdmission(Collection<CdsClientForm> forms)
	{
		int numberOfDaysInHospital=0;
		
		if (forms!=null)
		{
			for (CdsClientForm form : forms)
			{
				List<CdsHospitalisationDays> hospitalisationDays=getHopitalisationDaysDuringAdmission(form);
				FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
				
				GregorianCalendar endBound=admission.getDischargeCalendar();
				if (endBound==null) endBound=endDateExclusive;
				
				numberOfDaysInHospital=numberOfDaysInHospital+getTotalDayCount(hospitalisationDays, admission.getAdmissionCalendar(), endBound);
			}
		}
		
		return(numberOfDaysInHospital);
	}
	
	private int getCdsHospitalisationDaysCount2YearsBeforeProgramAdmission(Collection<CdsClientForm> forms)
	{
		int numberOfDaysInHospital=0;
		
		if (forms!=null)
		{
			for (CdsClientForm form : forms)
			{
				List<CdsHospitalisationDays> hospitalisationDays=getHopitalisationDays2YearsBeforeAdmission(form);
				FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
				
				GregorianCalendar startBound=(GregorianCalendar) admission.getAdmissionCalendar().clone();
				startBound.add(GregorianCalendar.YEAR, -2); // 2 years prior to admission
				// materialise results
				startBound.getTimeInMillis(); 
				
				numberOfDaysInHospital=numberOfDaysInHospital+getTotalDayCount(hospitalisationDays, startBound, admission.getAdmissionCalendar());
			}
		}
		
		return(numberOfDaysInHospital);
	}
	
	private int getTotalDayCount(List<CdsHospitalisationDays> hospitalisationDays, GregorianCalendar startBound, GregorianCalendar endBound) {
	    int daysCount=0;
	    
	    for (CdsHospitalisationDays cdsHospitalisationDays : hospitalisationDays)
	    {
	    	daysCount=daysCount+getTotalDayCount(cdsHospitalisationDays, startBound, endBound);
	    }
	    
	    return(daysCount);
    }

	/**
	 * The bounds on the dates is to ensure we don't count days in the hospital beyond our desired range, i.e. the stay for the admission, or reporting period etc.
	 */
	private int getTotalDayCount(CdsHospitalisationDays cdsHospitalisationDays, Calendar startBoundingDate, Calendar endBoundingDate) {

		// check the hospital days are within bounding dates
		if (cdsHospitalisationDays.getDischarged()!=null && cdsHospitalisationDays.getDischarged().before(startBoundingDate)) return(0);
		if (cdsHospitalisationDays.getAdmitted().after(endBoundingDate)) return(0);
		
		// limit to max bounding dates
		Calendar fromCal=startBoundingDate;
		if (cdsHospitalisationDays.getAdmitted().after(startBoundingDate)) fromCal=cdsHospitalisationDays.getAdmitted();
		
		Calendar toCal=endBoundingDate;		
		if (cdsHospitalisationDays.getDischarged()!=null &&  cdsHospitalisationDays.getDischarged().before(endBoundingDate)) toCal=cdsHospitalisationDays.getDischarged();

	    return (int) (DateUtils.getNumberOfDaysBetweenTwoDates(fromCal, toCal));
    }

	/**
	 * @param admissionPeriod refers to "year 1 hospital days" = 1, etc.. with respect to cds section 021-05 through 021-14
	 */
	private int getCdsHospitalisationDaysCountDuringPeriod(Collection<CdsClientForm> forms, int admissionPeriod) {

		int totalDays=0;
		
		if (forms!=null)
		{
			for (CdsClientForm form : forms)
			{
				List<CdsHospitalisationDays> allFormHospitalisationDays=getHopitalisationDaysDuringAdmission(form);
	
				FunctionalCentreAdmission admission=admissionMap.get(form.getAdmissionId());
				if (admission!=null)
				{
					Calendar startBoundingDate=getStartBound(admission, admissionPeriod);
					Calendar endBoundingDate=getEndBound(admission, admissionPeriod);
					
					for (CdsHospitalisationDays cdsHospitalisationDays : allFormHospitalisationDays)
					{
						totalDays=totalDays+getTotalDayCount(cdsHospitalisationDays, startBoundingDate, endBoundingDate);
					}
				}
			}
		}
		
		return(totalDays);
	}

	private Calendar getEndBound(FunctionalCentreAdmission admission, int admissionPeriod) {

		Calendar endBound=getStartBound(admission, admissionPeriod);
		endBound.add(Calendar.YEAR, 1);

		return(endBound);
    }

	private Calendar getStartBound(FunctionalCentreAdmission admission, int admissionPeriod) {

		Calendar startBound=new GregorianCalendar();
		startBound.setTime(admission.getAdmissionDate()); 
		startBound.add(Calendar.YEAR, admissionPeriod-1);
		
	    return(startBound);
    }

	private int[] get021HospitalDays(int year)
	{
		int[] dataRow=getNotAvailableDataLine();

		// get multi admissions number
		dataRow[0]=getCdsHospitalisationDaysCountDuringPeriod(singleMultiAdmissions.multipleAdmissionsAllForms, year);
		
		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			dataRow[i+1]=getCdsHospitalisationDaysCountDuringPeriod(bucket, year);
		}

		return(dataRow);
	}
	
	public static int getCohortTotal(int[] dataRow)
	{
		int total=0;
		
		for (int i=1; i<dataRow.length; i++)
		{
			if (dataRow[i]!=-1) total=total+dataRow[i];
		}
		
		return(total);
	}
	
	public Integer getClientAgeAtReportTime(int clientId)
	{
		Demographic demographic=demographicDao.getDemographicById(clientId);
		return(DateUtils.getAge(demographic.getBirthDay(), endDateExclusive));
	}
}
