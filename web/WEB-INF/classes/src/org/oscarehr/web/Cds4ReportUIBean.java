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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class Cds4ReportUIBean {

	private static Logger logger = MiscUtils.getLogger();

	private static FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");

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
	}

	private SingleMultiAdmissions singleMultiAdmissions=null;
	private FunctionalCentre functionalCentre=null;
	private GregorianCalendar startDate=null;
	private GregorianCalendar endDate=null;
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public Cds4ReportUIBean(String functionalCentreId, int startYear, int startMonth, int endYear, int endMonth) {

		startDate = new GregorianCalendar(startYear, startMonth, 1);
		endDate = new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive
		
		functionalCentre=functionalCentreDao.find(functionalCentreId);
		
		singleMultiAdmissions = getAdmissionsSortedSingleMulti(functionalCentreId, startDate, endDate);
	}
	
	public String getFunctionalCentreDescription()
	{
		return(StringEscapeUtils.escapeHtml(functionalCentre.getAccountId()+", "+functionalCentre.getDescription()));
	}
	
	public String getDateRangeForDisplay() 
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar displayEndDate=(GregorianCalendar) endDate.clone();
		displayEndDate.add(GregorianCalendar.MONTH, -1);
		return(StringEscapeUtils.escapeHtml(simpleDateFormat.format(startDate.getTime())+" to "+simpleDateFormat.format(displayEndDate.getTime())+" (inclusive)"));
	}

	public static List<CdsFormOption> getCdsFormOptions()
	{
		return(cdsFormOptionDao.findByVersion("4"));
	}
	
	private static SingleMultiAdmissions getAdmissionsSortedSingleMulti(String functionalCentreAccountId, GregorianCalendar startDate, GregorianCalendar endDate) {
		SingleMultiAdmissions singleMultiAdmissions = new SingleMultiAdmissions();

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		List<CdsClientForm> cdsForms = cdsClientFormDao.findLatestSignedCdsForms(loggedInInfo.currentFacility.getId(), "4", startDate.getTime(), endDate.getTime());
		logger.debug("valid cds form count : "+cdsForms.size());
		
		HashMap<Integer, Admission> admissionMap = getAdmissionMap(functionalCentreAccountId, startDate, endDate);

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

		// sort multiple admissions into cohort buckets
		for (CdsClientForm form : singleMultiAdmissions.multipleAdmissionsAllForms) {
			Admission admission = admissionMap.get(form.getAdmissionId());
			int bucket = getCohortBucket(admission);
			singleMultiAdmissions.multipleAdmissionCohortBuckets.put(bucket, form);
			
			logger.debug("cds form id="+form.getId()+", admission="+admission.getId()+", cohort bucket="+bucket);
		}

		return(singleMultiAdmissions);
	}

	private static HashMap<Integer, Admission> getAdmissionMap(String functionalCentreId, GregorianCalendar startDate, GregorianCalendar endDate) {

		// put admissions into map so it's easier to retrieve by id.
		HashMap<Integer, Admission> admissionMap = new HashMap<Integer, Admission>();

		List<Program> programs=programDao.getProgramsByFunctionalCentreId(functionalCentreId);
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		for (Program program : programs) {
			// only report on admissions to programs in our facility
			if (program.getFacilityId()!=loggedInInfo.currentFacility.getId().intValue()) continue;
			
			List<Admission> admissions = admissionDao.getAdmissionsByProgramAndDate(program.getId(), startDate.getTime(), endDate.getTime());

			logger.debug("corresponding cds admissions count:"+admissions.size());
			
			for (Admission admission : admissions) {
				admissionMap.put(admission.getId().intValue(), admission);
				logger.debug("valid cds admission, id="+admission.getId());
			}
		}

		return admissionMap;
	}

	private static int getCohortBucket(Admission admission) {
		Date dischargeDate = new Date(); // default duration calculation to today if not discharged.
		if (admission.getDischargeDate() != null) dischargeDate = admission.getDischargeDate();

		int years = MiscUtils.calculateYearDifference(dischargeDate, admission.getAdmissionDate());
		if (years > 10) years = 10; // limit everything above 10 years to the 10 year bucket.

		return(years);
	}

	private static CdsClientForm getNewerForm(CdsClientForm form1, CdsClientForm form2) {
		if (form1.getCreated().after(form2.getCreated())) return(form1);
		else return(form2);
	}

	
	public int[] getDataRow(CdsFormOption cdsFormOption) {
		if (cdsFormOption.getCdsDataCategory().startsWith("007-")) return(get007DataLine(cdsFormOption));
		else if (cdsFormOption.getCdsDataCategory().startsWith("07a-")) return(getNotAvailableDataLine());
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
		else if (cdsFormOption.getCdsDataCategory().startsWith("018-")) return(getGenericAllAnswersDataLine(cdsFormOption));
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

	private int[] get007DataLine(CdsFormOption cdsFormOption) {
		if ("007-01".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

			dataRow[0]=singleMultiAdmissions.multipleAdmissionsLatestForms.size();
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				int size = 0;

				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) size = bucket.size();
				else size=0;

				dataRow[i+1]=size;
			}
			
			return(dataRow);
		} else if ("007-02".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else if ("007-03".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else if ("007-04".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

			dataRow[0]=singleMultiAdmissions.multipleAdmissionsLatestForms.size();
			
			for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
				int size = 0;

				@SuppressWarnings("unchecked")
				Collection<CdsClientForm> bucket = singleMultiAdmissions.multipleAdmissionCohortBuckets.getCollection(i);
				if (bucket != null) size = bucket.size();
				else size=0;

				dataRow[i+1]=size;
			}
			
			return(dataRow);
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}

	private static int[] getNotAvailableDataLine() {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];
		for (int i=0;i<dataRow.length; i++) dataRow[i]=-1;
		return(dataRow);		
	}

	private int[] getGenericLatestAnswerDataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

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
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

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
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];
			dataRow[0]=getMultipleAdmissionCountByMinMaxAge(MinMax.MIN, singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			populateCohortCountsByMinMaxAge(dataRow, MinMax.MIN);
			return(dataRow);
		} else if ("009-13".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];
			dataRow[0]=getMultipleAdmissionCountByMinMaxAge(MinMax.MAX, singleMultiAdmissions.multipleAdmissionsLatestForms.values());
			populateCohortCountsByMinMaxAge(dataRow, MinMax.MAX);
			return(dataRow);
		} else if ("009-14".equals(cdsFormOption.getCdsDataCategory())) {
			int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

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
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

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

	private static int get009MultipleAdmissionCountByAge(Collection<CdsClientForm> forms, int startAge, int endAge) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = form.getClientAge();
				if (age != null && age >= startAge && age <= endAge) count++;
			}
		}

		return(count);
	}

	private static int get009MultipleAdmissionCountByNoAge(Collection<CdsClientForm> forms) {
		int count = 0;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				Integer age = form.getClientAge();
				if (age == null) count++;
			}
		}

		return(count);
	}

	private static int getMultipleAdmissionCountByMinMaxAge(MinMax minMax, Collection<CdsClientForm> forms) {
		Integer minMaxAge = null;

		if (forms != null) {
			for (CdsClientForm form : forms) {
				minMaxAge = nullSafeMinMax(minMax, minMaxAge, form.getClientAge());
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

	private static int getMultipleAdmissionCountByAvgAge(Collection<CdsClientForm> forms) {
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

		return(avg);
	}

	private int[] getGenericAllAnswersDataLine(CdsFormOption cdsFormOption) {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

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

	private int[] get020DataLine(CdsFormOption cdsFormOption) {
		if ("020-01".equals(cdsFormOption.getCdsDataCategory())) {
			return(get02xQuestionAnswerCountsDataLine("baselineTotalNumberOfHospitalisedDays", "0"));
		} else if ("020-02".equals(cdsFormOption.getCdsDataCategory())) {
			return(get02xQuestionSumScalarAnswersDataLine("baselineTotalNumberOfEpisodes"));
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			return(get02xQuestionSumScalarAnswersDataLine("baselineTotalNumberOfHospitalisedDays"));
		} else if ("020-04".equals(cdsFormOption.getCdsDataCategory())) {
			return(get02xQuestionAnswerCountsDataLine("baselineTotalNumberOfHospitalisedDays", "-1"));
		} else if ("020-05".equals(cdsFormOption.getCdsDataCategory())) {
			return(get020QuestionAvgScalarDataLine("ageOfFirstPsychiatricHospitalization"));
		} else if ("020-06".equals(cdsFormOption.getCdsDataCategory())) {
			return(get020QuestionAvgScalarDataLine("ageOfOnsetOfMentalIllness"));
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
	}

	private int[] get02xQuestionAnswerCountsDataLine(String question, String answerToCount) {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

		// get multi admissions number
		dataRow[0]=get02xQuestionAnswerCounts(question, answerToCount, singleMultiAdmissions.multipleAdmissionsLatestForms.values());

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) dataRow[i+1]=get02xQuestionAnswerCounts(question, answerToCount, bucket);
			else dataRow[i+1]=-1;
			
		}
		
		return(dataRow);		
	}

	private static int get02xQuestionAnswerCounts(String cdsQuestion, String cdsAnswer, Collection<CdsClientForm> cdsForms) {

		int totalCount = 0;

		if (cdsForms != null) {
			for (CdsClientForm form : cdsForms) {
				List<CdsClientFormData> results = cdsClientFormDataDao.findByQuestion(form.getId(), cdsQuestion);
				for (CdsClientFormData formData : results) {
					if (cdsAnswer.equals(formData.getAnswer())) totalCount++;
				}
			}
		}

		return(totalCount);
	}


	private int[] get02xQuestionSumScalarAnswersDataLine(String question) {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

		// get multi admissions number
		dataRow[0]=get02xQuestionSumScalarAnswers(question, singleMultiAdmissions.multipleAdmissionsLatestForms.values());

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) dataRow[i+1]=get02xQuestionSumScalarAnswers(question, bucket);
			else dataRow[i+1]=-1;
		}
		
		return(dataRow);		
	}

	private static int get02xQuestionSumScalarAnswers(String cdsQuestion, Collection<CdsClientForm> cdsForms) {

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

		return(totals);
	}

	private int[] get020QuestionAvgScalarDataLine(String question) {
		int[] dataRow=new int[NUMBER_OF_DATA_ROW_COLUMNS];

		// get multi admissions number
		dataRow[0]=get020QuestionAvgScalarAnswers(question, singleMultiAdmissions.multipleAdmissionsLatestForms.values());

		// get cohort numbers
		for (int i = 0; i < NUMBER_OF_COHORT_BUCKETS; i++) {
			@SuppressWarnings("unchecked")
			Collection<CdsClientForm> bucket = singleMultiAdmissions.singleAdmissionCohortBuckets.getCollection(i);
			if (bucket != null) dataRow[i+1]=get020QuestionAvgScalarAnswers(question, bucket);
			else dataRow[i+1]=-1;
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

	private static int[] get021DataLine(CdsFormOption cdsFormOption) {
		if ("021-01".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else if ("021-02".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else if ("020-03".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else if ("020-04".equals(cdsFormOption.getCdsDataCategory())) {
			return(getNotAvailableDataLine());
		} else {
			logger.error("Missing case, cdsFormOption="+cdsFormOption.getCdsDataCategory());
			return(getNotAvailableDataLine());
		}
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
}
