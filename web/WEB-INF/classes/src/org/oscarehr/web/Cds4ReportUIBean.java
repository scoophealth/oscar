/*
 * Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class Cds4ReportUIBean {

	private static Logger logger = MiscUtils.getLogger();

	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");

	private static final char ROW_TERMINATOR = '>';
	private static final int NUMBER_OF_COHORT_BUCKETS = 11;

	enum MinMax {
		MIN, MAX
	}

	public static class SingleMultiAdmissions {
		public HashMap<Integer, CdsClientForm> singleAdmissions = new HashMap<Integer, CdsClientForm>();
		// this is a map where key=0-10 representing each cohort bucket., value is a collection of CdsClientForms
		public MultiValueMap singleAdmissionCohortBuckets = new MultiValueMap();

		public HashMap<Integer, CdsClientForm> multipleAdmissionsLatestForms = new HashMap<Integer, CdsClientForm>();
		public ArrayList<CdsClientForm> multipleAdmissionsAllForms = new ArrayList<CdsClientForm>();

	}

	/**
	 * End dates should be treated as inclusive.
	 */
	public static ArrayList<String> getAsciiExportData(int[] caisiProgramIds, int startYear, int startMonth, int endYear, int endMonth, String ministryOrganisationNumber, String ministryProgramNumber, String ministryFunctionCode, String[] serviceLanguages, String[] serviceDeliveryLhins, HashMap<String,CdsManualLineEntry> manualSections, boolean measureServiceRecipientSatisfaction, boolean measureServiceRecipientFamiltySatisfaction, boolean qualityImprovementStrategies, boolean participateInAccreditation) {

		GregorianCalendar startDate = new GregorianCalendar(startYear, startMonth, 1);
		GregorianCalendar endDate = new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive

		ArrayList<String> asciiTextFileRows = new ArrayList<String>();
		asciiTextFileRows.add(getHeader(ministryOrganisationNumber, ministryProgramNumber, ministryFunctionCode) + ROW_TERMINATOR);
		
		SingleMultiAdmissions singleMultiAdmissions = sortSingleMultiAdmission(caisiProgramIds, startDate, endDate);

		for (CdsFormOption cdsFormOption : cdsFormOptionDao.findByVersion("4")) {
			String dataLine=null;
			String dataCategory=cdsFormOption.getCdsDataCategory();
			
			if (dataCategory.startsWith("006-")) dataLine=get006DataLine(cdsFormOption, serviceLanguages);
			else if (dataCategory.startsWith("10b-")) dataLine=get10bDataLine(cdsFormOption, serviceDeliveryLhins);
			else if (dataCategory.startsWith("032-")) dataLine=get032DataLine(cdsFormOption, measureServiceRecipientSatisfaction, measureServiceRecipientFamiltySatisfaction, qualityImprovementStrategies, participateInAccreditation);
			else if (manualSections.get(dataCategory)!=null) dataLine=getManualSectionDataLine(manualSections.get(cdsFormOption.getCdsDataCategory()));
			else dataLine=getAdmissionDataLine(cdsFormOption, singleMultiAdmissions);
			
			if (dataLine!=null) asciiTextFileRows.add(dataCategory + dataLine + ROW_TERMINATOR);
		}

		return (asciiTextFileRows);
	}

	private static String getManualSectionDataLine(CdsManualLineEntry cdsManualLineEntry) {
		StringBuilder sb=new StringBuilder();
		
		sb.append(padTo6(cdsManualLineEntry.multipleAdmissions));
		sb.append(padTo6(cdsManualLineEntry.cohort0));
		sb.append(padTo6(cdsManualLineEntry.cohort1));
		sb.append(padTo6(cdsManualLineEntry.cohort2));
		sb.append(padTo6(cdsManualLineEntry.cohort3));
		sb.append(padTo6(cdsManualLineEntry.cohort4));
		sb.append(padTo6(cdsManualLineEntry.cohort5));
		sb.append(padTo6(cdsManualLineEntry.cohort6));
		sb.append(padTo6(cdsManualLineEntry.cohort7));
		sb.append(padTo6(cdsManualLineEntry.cohort8));
		sb.append(padTo6(cdsManualLineEntry.cohort9));
		sb.append(padTo6(cdsManualLineEntry.cohort10));
		
		return(sb.toString());
	}

	private static SingleMultiAdmissions sortSingleMultiAdmission(int[] caisiProgramIds, GregorianCalendar startDate, GregorianCalendar endDate) {
		SingleMultiAdmissions singleMultiAdmissions = new SingleMultiAdmissions();

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		List<CdsClientForm> cdsForms = cdsClientFormDao.findLatestSignedCdsForms(loggedInInfo.currentFacility.getId(), "4", startDate.getTime(), endDate.getTime());
		logger.debug("valid cds form count : "+cdsForms.size());
		
		HashMap<Integer, Admission> admissionMap = getAdmissionMap(caisiProgramIds, startDate, endDate);

		// sort into single and multiple admissions
		for (CdsClientForm form : cdsForms) {
			logger.debug("valid cds form, id="+form.getId());
			
			Admission admission = admissionMap.get(form.getAdmissionId());
			if (admission != null) {
				Integer clientId = form.getClientId();

				CdsClientForm existingForm = singleMultiAdmissions.multipleAdmissionsLatestForms.get(clientId);
				// if this person already has multiple admissions
				if (existingForm != null) {
					logger.debug("multiple admissions 3+ forms. formId="+form.getId()+", otherFormId="+existingForm.getId());
					singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
					singleMultiAdmissions.multipleAdmissionsAllForms.add(form);
				} else // this person either has one previous or no previous admissions
				{
					existingForm = singleMultiAdmissions.singleAdmissions.get(clientId);
					// this means we have 1 previous admission
					if (existingForm != null) {
						logger.debug("multiple admissions, 2 forms : formId="+form.getId()+", otherFormId="+existingForm.getId());

						singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
						singleMultiAdmissions.singleAdmissions.remove(clientId);

						singleMultiAdmissions.multipleAdmissionsAllForms.add(existingForm);
						singleMultiAdmissions.multipleAdmissionsAllForms.add(form);
					} else // we have no previous admission
					{
						logger.debug("single admissions : formId="+form.getId());
						singleMultiAdmissions.singleAdmissions.put(clientId, form);
					}
				}
			}
			else
			{
				logger.debug("cds form missing admission. formId="+form.getId()+", admissionId="+form.getAdmissionId());
			}
		}

		// sort single admissions into cohort buckets
		for (CdsClientForm form : singleMultiAdmissions.singleAdmissions.values()) {
			Admission admission = admissionMap.get(form.getAdmissionId());
			int bucket = getCohortBucket(admission);
			singleMultiAdmissions.singleAdmissionCohortBuckets.put(bucket, form);
			
			logger.debug("cds form id="+form.getId()+", admission="+admission.getId()+", cohort bucket="+bucket);
		}

		return (singleMultiAdmissions);
	}

	private static int getCohortBucket(Admission admission) {
		Date dischargeDate = new Date(); // default duration calculation to today if not discharged.
		if (admission.getDischargeDate() != null) dischargeDate = admission.getDischargeDate();

		int years = MiscUtils.calculateYearDifference(dischargeDate, admission.getAdmissionDate());
		if (years > 10) years = 10; // limit everything above 10 years to the 10 year bucket.

		return (years);
	}

	private static CdsClientForm getNewerForm(CdsClientForm form1, CdsClientForm form2) {
		if (form1.getCreated().after(form2.getCreated())) return (form1);
		else return (form2);
	}

	private static HashMap<Integer, Admission> getAdmissionMap(int[] caisiProgramIds, GregorianCalendar startDate, GregorianCalendar endDate) {

		// put admissions into map so it's easier to retrieve by id.
		HashMap<Integer, Admission> admissionMap = new HashMap<Integer, Admission>();

		for (int caisiProgramId : caisiProgramIds) {
			List<Admission> admissions = admissionDao.getAdmissionsByProgramAndDate(caisiProgramId, startDate.getTime(), endDate.getTime());

			logger.debug("corresponding cds admissions count:"+admissions.size());
			
			for (Admission admission : admissions) {
				admissionMap.put(admission.getId().intValue(), admission);
				logger.debug("valid cds admission, id="+admission.getId());
			}
		}

		return admissionMap;
	}

	private static String getAdmissionDataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {

		if (cdsFormOption.getCdsDataCategory().startsWith("007-")) return (get007DataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("008-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("009-")) return (get009DataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("010-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("10a-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("011-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("012-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("013-")) return (getGenericAllAnswersDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("014-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("015-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("016-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("16a-")) return (getGenericAllAnswersDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("017-")) return (getGenericAllAnswersDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("018-")) return (getGenericAllAnswersDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("019-")) return (getGenericAllAnswersDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("020-")) return (get020DataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("021-")) return (get021DataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("022-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("023-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("024-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("24a-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("025-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("25a-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("026-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("027-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("028-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("029-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("29a-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("030-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("031-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());
	}

	private static String get006DataLine(CdsFormOption cdsFormOption, String[] serviceLanguages) {

		if ("006-01".equals(cdsFormOption.getCdsDataCategory())) {
			if (contains(serviceLanguages, "en")) return(getZeroDataLine());
		} else if ("006-02".equals(cdsFormOption.getCdsDataCategory())) {
			if (contains(serviceLanguages, "fr")) return(getZeroDataLine());
		} else if ("006-03".equals(cdsFormOption.getCdsDataCategory())) {
			if (contains(serviceLanguages, "other")) return(getZeroDataLine());
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		// language is not supported therefore don't write the line
		return(null);
    }
	
	private static String get032DataLine(CdsFormOption cdsFormOption, boolean measureServiceRecipientSatisfaction, boolean measureServiceRecipientFamiltySatisfaction, boolean qualityImprovementStrategies, boolean participateInAccreditation) {

		if ("032-01".equals(cdsFormOption.getCdsDataCategory())) {
			if (measureServiceRecipientSatisfaction) return(getZeroDataLine());
		} else if ("032-02".equals(cdsFormOption.getCdsDataCategory())) {
			if (measureServiceRecipientFamiltySatisfaction) return(getZeroDataLine());
		} else if ("032-03".equals(cdsFormOption.getCdsDataCategory())) {
			if (qualityImprovementStrategies) return(getZeroDataLine());
		} else if ("032-04".equals(cdsFormOption.getCdsDataCategory())) {
			if (participateInAccreditation) return(getZeroDataLine());
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		// language is not supported therefore don't write the line
		return(null);
    }
	
	private static String get10bDataLine(CdsFormOption cdsFormOption, String[] serviceDeliveryLhins) {

		if (contains(serviceDeliveryLhins, cdsFormOption.getCdsDataCategory())) return(getZeroDataLine());

		return(null);
    }
	
	private static boolean contains(String[] list, String value)
	{
		for (String s : list)
		{
			if (value.equals(s)) return(true);
		}
		
		return(false);
	}

	private static String get021DataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("021-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionAnswerCountsDataLine(singleMultiAdmissions, "currentTotalNumberOfEpisodes", "0"));
		} else if ("021-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionSumScalarAnswersDataLine(singleMultiAdmissions, "currentTotalNumberOfEpisodes"));
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionSumScalarAnswersDataLine(singleMultiAdmissions, "currentTotalNumberOfHospitalisedDays"));
		} else if ("020-04".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionAnswerCountsDataLine(singleMultiAdmissions, "currentTotalNumberOfHospitalisedDays", "-1"));
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		return (sb.toString());
	}

	private static String get020DataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("020-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionAnswerCountsDataLine(singleMultiAdmissions, "baselineTotalNumberOfHospitalisedDays", "0"));
		} else if ("020-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionSumScalarAnswersDataLine(singleMultiAdmissions, "baselineTotalNumberOfEpisodes"));
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionSumScalarAnswersDataLine(singleMultiAdmissions, "baselineTotalNumberOfHospitalisedDays"));
		} else if ("020-04".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get02xQuestionAnswerCountsDataLine(singleMultiAdmissions, "baselineTotalNumberOfHospitalisedDays", "-1"));
		} else if ("020-05".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get020QuestionAvgScalarDataLine(singleMultiAdmissions, "ageOfFirstPsychiatricHospitalization"));
		} else if ("020-06".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get020QuestionAvgScalarDataLine(singleMultiAdmissions, "ageOfOnsetOfMentalIllness"));
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		return (sb.toString());
	}

	private static String get02xQuestionSumScalarAnswersDataLine(SingleMultiAdmissions singleMultiAdmissions, String question) {
		StringBuilder sb = new StringBuilder();

		// get multi admissions
		sb.append(get02xQuestionSumScalarAnswers(question, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

		// get cohort buckets
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) sb.append(get02xQuestionSumScalarAnswers(question, bucket));
			else sb.append(padTo6(0));
		}

		return (sb.toString());
	}

	private static String get02xQuestionAnswerCountsDataLine(SingleMultiAdmissions singleMultiAdmissions, String question, String answerToCount) {
		StringBuilder sb = new StringBuilder();

		// get multi admissions
		sb.append(get02xQuestionAnswerCounts(question, answerToCount, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

		// get cohort buckets
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) sb.append(get02xQuestionAnswerCounts(question, answerToCount, bucket));
			else sb.append(padTo6(0));
		}

		return (sb.toString());
	}

	private static String get020QuestionAvgScalarDataLine(SingleMultiAdmissions singleMultiAdmissions, String question) {
		StringBuilder sb = new StringBuilder();

		// get multi admissions
		sb.append(get020QuestionAvgScalarAnswers(question, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

		// get cohort buckets
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) sb.append(get020QuestionAvgScalarAnswers(question, bucket));
			else sb.append(padTo6(0));
		}

		return (sb.toString());
	}

	private static String get009DataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("009-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 0, 15));
		} else if ("009-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 16, 17));
		} else if ("009-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 18, 24));
		} else if ("009-04".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 25, 34));
		} else if ("009-05".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 35, 44));
		} else if ("009-06".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 45, 54));
		} else if ("009-07".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 55, 64));
		} else if ("009-08".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 65, 74));
		} else if ("009-09".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 75, 84));
		} else if ("009-10".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(get009AgeRangeDataLine(singleMultiAdmissions, 85, 200));
		} else if ("009-11".equals(cdsFormOption.getCdsDataCategory())) {
			// get multiple admissions
			sb.append(get009MultipleAdmissionCountByNoAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			// get cohorts
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				sb.append(get009MultipleAdmissionCountByNoAge(bucket));
			}
		} else if ("009-12".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByMinMaxAge(MinMax.MIN, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));
			sb.append(getCohortCountsByMinMaxAge(MinMax.MIN, singleMultiAdmissions));
		} else if ("009-13".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByMinMaxAge(MinMax.MAX, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));
			sb.append(getCohortCountsByMinMaxAge(MinMax.MAX, singleMultiAdmissions));
		} else if ("009-14".equals(cdsFormOption.getCdsDataCategory())) {
			// get multiple admissions
			sb.append(getMultipleAdmissionCountByAvgAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			// get cohorts
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				sb.append(getMultipleAdmissionCountByAvgAge(bucket));
			}
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		return (sb.toString());
	}

	private static String get009AgeRangeDataLine(SingleMultiAdmissions singleMultiAdmissions, int startAge, int endAge) {
		StringBuilder sb = new StringBuilder();

		// get multiple admission data
		sb.append(get009MultipleAdmissionCountByAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values(), startAge, endAge));

		// get cohort buckets
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(get009MultipleAdmissionCountByAge(bucket, startAge, endAge));
		}

		return (sb.toString());
	}

	private static String get009MultipleAdmissionCountByAge(Collection<CdsClientForm> forms, int startAge, int endAge) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = form.getClientAge();
				if (age != null && age >= startAge && age <= endAge) count++;
			}
		}

		return (padTo6(count));
	}

	private static String get007DataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("007-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(padTo6(singleMultiAdmissions.multipleAdmissionsLatestForms.size()));
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				int size = 0;

				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) size = bucket.size();

				sb.append(padTo6(size));
			}
		} else if ("007-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append("INCOMPLETE_NO_PRE_ADMISSION_DATA");
		} else if ("007-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getZeroDataLine());
		} else if ("007-04".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(padTo6(singleMultiAdmissions.multipleAdmissionsLatestForms.size()));
			sb.append(getZeroCohortData());
			sb.append("NON_SENSICAL_DATA");
		} else {
			sb.append("Error, missing case : " + cdsFormOption.getCdsDataCategory());
		}

		return (sb.toString());
	}

	private static String getGenericAllAnswersDataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		// get multiadmissions number
		sb.append((getAnswerCounts(cdsFormOption.getCdsDataCategory(), singleMultiAdmissions.multipleAdmissionsAllForms)));

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> cdsForms = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getAnswerCounts(cdsFormOption.getCdsDataCategory(), cdsForms));
		}

		return (sb.toString());
	}

	private static String getGenericLatestAnswerDataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		// get multiadmissions number
		sb.append((getAnswerCounts(cdsFormOption.getCdsDataCategory(), singleMultiAdmissions.multipleAdmissionsLatestForms.values())));

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> cdsForms = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getAnswerCounts(cdsFormOption.getCdsDataCategory(), cdsForms));
		}

		return (sb.toString());
	}

	private static String getAnswerCounts(String cdsAnswer, Collection<CdsClientForm> cdsForms) {

		int totalCount = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				CdsClientFormData result = cdsClientFormDataDao.findByAnswer(form.getId(), cdsAnswer);
				if (result != null) totalCount++;
			}
		}

		return (padTo6(totalCount));
	}

	private static String get02xQuestionAnswerCounts(String cdsQuestion, String cdsAnswer, Collection<CdsClientForm> cdsForms) {

		int totalCount = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), cdsQuestion);
				for (CdsClientFormData formData : results) {
					if (cdsAnswer.equals(formData.getAnswer())) totalCount++;
				}
			}
		}

		return (padTo6(totalCount));
	}

	private static String get02xQuestionSumScalarAnswers(String cdsQuestion, Collection<CdsClientForm> cdsForms) {

		int totals = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), cdsQuestion);
				for (CdsClientFormData formData : results) {
					try {
						int temp = Integer.parseInt(formData.getAnswer());
						// -1 means they didn't answer
						if (temp != -1) totals = totals + temp;
					} catch (NumberFormatException e) {
						logger.error("Expected a number here. answer=" + formData.getAnswer(), e);
					}
				}
			}
		}

		return (padTo6(totals));
	}

	private static String get020QuestionAvgScalarAnswers(String cdsQuestion, Collection<CdsClientForm> cdsForms) {

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

		return (padTo6(avg));
	}

	public static String getFilename(String ministryOrganisationNumber, String ministryProgramNumber, String ministryFunctionCode) {
		// stubbed for now
		// ooooopppppfff.Txt
		// Where:
		// ooooo is the MOHLTC assigned Service Organization number
		// ppppp is the MOHLTC assigned Program number
		// fff is the CDS-MH Function Code

		return (getHeader(ministryOrganisationNumber, ministryProgramNumber, ministryFunctionCode) + ".Txt");
	}

	private static String getHeader(String ministryOrganisationNumber, String ministryProgramNumber, String ministryFunctionCode) {
		// ooooopppppfff
		// Where:
		// ooooo is the MOHLTC assigned Service Organization number
		// ppppp is the MOHLTC assigned Program number
		// fff is the CDS-MH Function Code

		return (pad(ministryOrganisationNumber,5)+pad(ministryProgramNumber,5)+pad(ministryFunctionCode,3));
	}

	private static String padTo6(int i) {
		StringBuilder sb = new StringBuilder();

		if (i < 10) sb.append("0");
		if (i < 100) sb.append("0");
		if (i < 1000) sb.append("0");
		if (i < 10000) sb.append("0");
		if (i < 100000) sb.append("0");

		sb.append(i);

		return (sb.toString());
	}

	private static String pad(String originalString, int requiredLength)
	{
		if (originalString.length()>requiredLength) throw(new IllegalArgumentException("original string longer than required length. string="+originalString+", requiredLenth="+requiredLength));
		if (originalString.length()==requiredLength) return(originalString);
		else
		{
			int pad=requiredLength-originalString.length();
			StringBuilder sb=new StringBuilder();
		
			for (int i=0; i<pad; i++) sb.append('0');
			
			sb.append(originalString);
		
			return(sb.toString());
		}
	}

	private static String getZeroMultipleAdmissionsData() {
		return ("000000");
	}

	private static String getZeroCohortData() {
		return ("000000");
	}

	private static String getAllZeroCohortData() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i <= 10; i++) {
			sb.append(getZeroCohortData());
		}

		return (sb.toString());
	}

	private static String getZeroDataLine() {
		return (getZeroMultipleAdmissionsData() + getAllZeroCohortData());
	}

	private static String get009MultipleAdmissionCountByNoAge(Collection<CdsClientForm> forms) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = form.getClientAge();
				if (age == null) count++;
			}
		}

		return (padTo6(count));
	}

	private static String getCohortCountsByMinMaxAge(MinMax minMax, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getMultipleAdmissionCountByMinMaxAge(minMax, bucket));
		}

		return (sb.toString());
	}

	private static String getMultipleAdmissionCountByMinMaxAge(MinMax minMax, Collection<CdsClientForm> forms) {
		Integer minMaxAge = null;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				minMaxAge = nullSafeMinMax(minMax, minMaxAge, form.getClientAge());
			}
		}

		if (minMaxAge == null) minMaxAge = 0;

		return (padTo6(minMaxAge));
	}

	private static Integer nullSafeMinMax(MinMax minMax, Integer i1, Integer i2) {
		if (i1 == null) return (i2);
		if (i2 == null) return (i1);

		if (minMax == MinMax.MAX) return (Math.max(i1.intValue(), i2.intValue()));
		else if (minMax == MinMax.MIN) return (Math.min(i1.intValue(), i2.intValue()));
		else throw (new IllegalStateException("okay I missed something : minMax=" + minMax + ", i1=" + i1 + ", i2=" + i2));
	}

	private static String getMultipleAdmissionCountByAvgAge(Collection<CdsClientForm> forms) {
		int totalPeople = 0;
		int totalAge = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				if (form.getClientAge() != null) {
					totalPeople++;
					totalAge = totalAge + form.getClientAge();
				}
			}
		}

		int avg = 0;
		if (totalPeople > 0) avg = totalAge / totalPeople;

		return (padTo6(avg));
	}
}
