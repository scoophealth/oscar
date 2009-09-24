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

	private static Logger logger=MiscUtils.getLogger();
	
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
	public static ArrayList<String> getAsciiExportData(int programId, int startYear, int startMonth, int endYear, int endMonth) {

		GregorianCalendar startDate = new GregorianCalendar(startYear, startMonth, 1);
		GregorianCalendar endDate = new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive

		ArrayList<String> asciiTextFileRows = new ArrayList<String>();
		asciiTextFileRows.add(getHeader(programId) + ROW_TERMINATOR);

		SingleMultiAdmissions singleMultiAdmissions = sortSingleMultiAdmission(programId, startDate, endDate);

		for (CdsFormOption cdsFormOption : cdsFormOptionDao.findByVersion("4")) {
			asciiTextFileRows.add(cdsFormOption.getCdsDataCategory() + getDataLine(cdsFormOption, singleMultiAdmissions) + ROW_TERMINATOR);
		}

		return (asciiTextFileRows);
	}

	private static SingleMultiAdmissions sortSingleMultiAdmission(int programId, GregorianCalendar startDate, GregorianCalendar endDate) {
		SingleMultiAdmissions singleMultiAdmissions = new SingleMultiAdmissions();

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		List<CdsClientForm> cdsForms = cdsClientFormDao.findLatestSignedCdsForms(loggedInInfo.currentFacility.getId(), "4", startDate.getTime(), endDate.getTime());

		HashMap<Integer, Admission> admissionMap = getAdmissionMap(programId, startDate, endDate);

		// sort into single and multiple admissions
		for (CdsClientForm form : cdsForms) {
			Admission admission = admissionMap.get(form.getAdmissionId());
			if (admission != null) {
				Integer clientId = form.getClientId();

				CdsClientForm existingForm = singleMultiAdmissions.multipleAdmissionsLatestForms.get(clientId);
				// if this person already has multiple admissions
				if (existingForm != null) {
					singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
					singleMultiAdmissions.multipleAdmissionsAllForms.add(form);
				} else // this person either has one previous or no previous admissions
				{
					existingForm = singleMultiAdmissions.singleAdmissions.get(clientId);
					// this means we have 1 previous admission
					if (existingForm != null) {
						singleMultiAdmissions.multipleAdmissionsLatestForms.put(clientId, getNewerForm(existingForm, form));
						singleMultiAdmissions.singleAdmissions.remove(clientId);

						singleMultiAdmissions.multipleAdmissionsAllForms.add(existingForm);
						singleMultiAdmissions.multipleAdmissionsAllForms.add(form);
					} else // we have no previous admission
					{
						singleMultiAdmissions.singleAdmissions.put(clientId, form);
					}
				}
			}
		}

		// sort single admissions into cohort buckets
		for (CdsClientForm form : singleMultiAdmissions.singleAdmissions.values()) {
			Admission admission = admissionMap.get(form.getAdmissionId());
			int bucket = getCohortBucket(admission);
			singleMultiAdmissions.singleAdmissionCohortBuckets.put(bucket, form);
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

	private static HashMap<Integer, Admission> getAdmissionMap(int programId, GregorianCalendar startDate, GregorianCalendar endDate) {
		List<Admission> admissions = admissionDao.getAdmissionsByProgramAndDate(programId, startDate.getTime(), endDate.getTime());

		// put admissions into map so it's easier to retrieve by id.
		HashMap<Integer, Admission> admissionMap = new HashMap<Integer, Admission>();
		for (Admission admission : admissions)
			admissionMap.put(admission.getId().intValue(), admission);
		return admissionMap;
	}

	private static String getDataLine(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {

		if (cdsFormOption.getCdsDataCategory().startsWith("007-")) return (getDataLine007(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("008-")) return (getGenericLatestAnswerDataLine(cdsFormOption, singleMultiAdmissions));
		else if (cdsFormOption.getCdsDataCategory().startsWith("009-")) return (getDataLine009(cdsFormOption, singleMultiAdmissions));
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
		else if (cdsFormOption.getCdsDataCategory().startsWith("020-")) return (getDataLine020(cdsFormOption, singleMultiAdmissions));
		else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());
	}

	private static String getDataLine020(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("020-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getQuestionAnswerCounts("baselineTotalNumberOfHospitalisedDays", "0", singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			for (int i=0; i<NUMBER_OF_COHORT_BUCKETS; i++)
			{
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) sb.append(getQuestionAnswerCounts("baselineTotalNumberOfHospitalisedDays", "0", bucket));
				else sb.append(padTo6(0));
			}
			
		} else if ("020-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getQuestionSumScalarAnswers("baselineTotalNumberOfEpisodes", singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			for (int i=0; i<NUMBER_OF_COHORT_BUCKETS; i++)
			{
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) sb.append(getQuestionSumScalarAnswers("baselineTotalNumberOfEpisodes", bucket));
				else sb.append(padTo6(0));
			}
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getQuestionSumScalarAnswers("baselineTotalNumberOfHospitalisedDays", singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			for (int i=0; i<NUMBER_OF_COHORT_BUCKETS; i++)
			{
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) sb.append(getQuestionSumScalarAnswers("baselineTotalNumberOfHospitalisedDays", bucket));
				else sb.append(padTo6(0));
			}
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getQuestionAnswerCounts("baselineTotalNumberOfHospitalisedDays", "-1", singleMultiAdmissions.multipleAdmissionsLatestForms.values()));

			for (int i=0; i<NUMBER_OF_COHORT_BUCKETS; i++)
			{
				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) sb.append(getQuestionAnswerCounts("baselineTotalNumberOfHospitalisedDays", "-1", bucket));
				else sb.append(padTo6(0));
			}
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		return (sb.toString());
	}

	private static String getDataLine009(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		if ("009-01".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 0, 15));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 0, 15));
		} else if ("009-02".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 16, 17));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 16, 17));
		} else if ("009-03".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 18, 24));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 18, 24));
		} else if ("009-04".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 25, 34));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 25, 34));
		} else if ("009-05".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 35, 44));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 35, 44));
		} else if ("009-06".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 45, 54));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 45, 54));
		} else if ("009-07".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 55, 64));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 55, 64));
		} else if ("009-08".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 65, 74));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 65, 74));
		} else if ("009-09".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 75, 84));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 75, 84));
		} else if ("009-10".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAge(singleMultiAdmissions, 85, 200));
			sb.append(getCohortCountsByAge(singleMultiAdmissions, 85, 200));
		} else if ("009-11".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByNoAge(singleMultiAdmissions));
			sb.append(getCohortCountsByNoAge(singleMultiAdmissions));
		} else if ("009-12".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByMinMaxAge(MinMax.MIN, singleMultiAdmissions));
			sb.append(getCohortCountsByMinMaxAge(MinMax.MIN, singleMultiAdmissions));
		} else if ("009-13".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByMinMaxAge(MinMax.MAX, singleMultiAdmissions));
			sb.append(getCohortCountsByMinMaxAge(MinMax.MAX, singleMultiAdmissions));
		} else if ("009-14".equals(cdsFormOption.getCdsDataCategory())) {
			sb.append(getMultipleAdmissionCountByAvgAge(singleMultiAdmissions));
			sb.append(getCohortCountsByAvgAge(singleMultiAdmissions));
		} else return ("Error, missing case : " + cdsFormOption.getCdsDataCategory());

		return (sb.toString());
	}

	private static String getDataLine007(CdsFormOption cdsFormOption, SingleMultiAdmissions singleMultiAdmissions) {
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

	private static String getQuestionAnswerCounts(String cdsQuestion, String cdsAnswer, Collection<CdsClientForm> cdsForms) {

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

	private static String getQuestionSumScalarAnswers(String cdsQuestion, Collection<CdsClientForm> cdsForms) {

		int totals = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), cdsQuestion);
				for (CdsClientFormData formData : results) {
					try {
	                    int temp=Integer.parseInt(formData.getAnswer());
	                    // -1 means they didn't answer
	                    if (temp!=-1) totals=totals+temp;
                    } catch (NumberFormatException e) {
                    	logger.error("Expected a number here. answer="+formData.getAnswer(), e);
                    }
				}
			}
		}

		return (padTo6(totals));
	}
	public static String getFilename(int programId) {
		// stubbed for now
		// ooooopppppfff.Txt
		// Where:
		// ooooo is the MOHLTC assigned Service Organization number
		// ppppp is the MOHLTC assigned Program number
		// fff is the CDS-MH Function Code

		return (getHeader(programId) + ".Txt");
	}

	private static String getHeader(int programId) {
		// ooooopppppfff
		// Where:
		// ooooo is the MOHLTC assigned Service Organization number
		// ppppp is the MOHLTC assigned Program number
		// fff is the CDS-MH Function Code

		return ("incomplete_" + programId + "_ooooopppppfff");
	}

	private static String padTo6(int i) {
		StringBuilder sb = new StringBuilder();

		if (i < 9) sb.append("0");
		if (i < 99) sb.append("0");
		if (i < 999) sb.append("0");
		if (i < 9999) sb.append("0");
		if (i < 99999) sb.append("0");

		sb.append(i);

		return (sb.toString());
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

	private static String getCohortCountsByAge(SingleMultiAdmissions singleMultiAdmissions, int startAge, int endAge) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getMultipleAdmissionCountByAge(bucket, startAge, endAge));
		}

		return (sb.toString());
	}

	private static String getMultipleAdmissionCountByAge(SingleMultiAdmissions singleMultiAdmissions, int startAge, int endAge) {
		return (getMultipleAdmissionCountByAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values(), startAge, endAge));
	}

	private static String getMultipleAdmissionCountByAge(Collection<CdsClientForm> forms, int startAge, int endAge) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = form.getClientAge();
				if (age != null && age >= startAge && age <= endAge) count++;
			}
		}

		return (padTo6(count));
	}

	private static String getCohortCountsByNoAge(SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getMultipleAdmissionCountByNoAge(bucket));
		}

		return (sb.toString());
	}

	private static String getMultipleAdmissionCountByNoAge(SingleMultiAdmissions singleMultiAdmissions) {
		return (getMultipleAdmissionCountByNoAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values()));
	}

	private static String getMultipleAdmissionCountByNoAge(Collection<CdsClientForm> forms) {
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

	private static String getMultipleAdmissionCountByMinMaxAge(MinMax minMax, SingleMultiAdmissions singleMultiAdmissions) {
		return (getMultipleAdmissionCountByMinMaxAge(minMax, singleMultiAdmissions.multipleAdmissionsLatestForms.values()));
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

	private static String getCohortCountsByAvgAge(SingleMultiAdmissions singleMultiAdmissions) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			sb.append(getMultipleAdmissionCountByAvgAge(bucket));
		}

		return (sb.toString());
	}

	private static String getMultipleAdmissionCountByAvgAge(SingleMultiAdmissions singleMultiAdmissions) {
		return (getMultipleAdmissionCountByAvgAge(singleMultiAdmissions.multipleAdmissionsLatestForms.values()));
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
