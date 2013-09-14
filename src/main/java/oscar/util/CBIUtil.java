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

package oscar.util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.OcanSubmissionLogDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.OcanSubmissionLog;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

import com.cbi.ws.CBIService;
import com.cbi.ws.CBIServiceImplService;
import com.cbi.ws.CbiDataItem;
import com.cbi.ws.CbiServiceResult;

public class CBIUtil
{
	private static Logger logger = Logger.getLogger(CBIUtil.class);
	private static OscarProperties oscarProperties = OscarProperties.getInstance();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	// submit cbi data to cbi web service
	public void submitCBIData(Admission admission, OcanStaffForm ocanStaffForm) throws Exception
	{
		logger.debug("in CBIUtil : submitCBIData");
		logger.debug("admission = " + admission);
		logger.debug("ocanStaffForm = " + ocanStaffForm);

		// invoke webservice to submit data
		CbiServiceResult cbiServiceResult = invokeWebService(admission, ocanStaffForm);
		String result = "true";
		String transactionId = null;
		String resultMsg = "Submitted Successfully";

		if (cbiServiceResult != null)
		{
			result = (cbiServiceResult.getResultCode() == 0) ? "true" : "false";
			resultMsg = cbiServiceResult.getDescription();
			if (result.equals("true"))
			{
				transactionId = cbiServiceResult.getReason();
			}
			else
			{
				resultMsg = resultMsg + " - " + cbiServiceResult.getReason();
			}
		}

		resultMsg = resultMsg + " - ASSESSMENT ID : " + ocanStaffForm.getAssessmentId();

		Integer submissionId = insertIntoSubmissionLog(result, transactionId, resultMsg);
		logger.debug("submissionId = " + submissionId);

		// update submission id in OcanStaffForm
		ocanStaffForm.setSubmissionId(submissionId);
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		ocanStaffFormDao.saveEntity(ocanStaffForm);

		logger.debug("out CBIUtil : submitCBIData");
	}

	private CbiServiceResult invokeWebService(Admission admission, OcanStaffForm ocanStaffForm) throws Exception
	{
		logger.debug("invoking CBI web service");

		String CBI_WS_URL = oscarProperties.getProperty("CBI_WS_URL");
		String CBI_WS_USERNAME = oscarProperties.getProperty("CBI_WS_USERNAME");
		String CBI_WS_PASSWORD = oscarProperties.getProperty("CBI_WS_PASSWORD");

		logger.info("CBI_WS_URL = " + CBI_WS_URL);

		URL baseUrl = com.cbi.ws.CBIServiceImplService.class.getResource(".");
		URL wsdlLocation = new URL(baseUrl, CBI_WS_URL);

		CBIServiceImplService cbiServiceImplService = new CBIServiceImplService(wsdlLocation);
		CBIService cbiService = cbiServiceImplService.getCBIServiceImplPort();

		BindingProvider bindingProvider = (BindingProvider) cbiService;

		Map<String, List<String>> credentials = new HashMap<String, List<String>>();

		credentials.put("Username", Collections.singletonList(CBI_WS_USERNAME));
		credentials.put("password", Collections.singletonList(CBI_WS_PASSWORD));
		bindingProvider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, credentials);

		CbiDataItem cbiDataItem = getCbiDataItem(admission, ocanStaffForm);
		CbiServiceResult result = cbiService.processCBIItem(cbiDataItem);

		logger.debug("result = " + result);

		if (result != null)
		{
			logger.info("----------------------------------------");
			logger.info("result.getDescription() = " + result.getDescription());
			logger.info("result.getReason() = " + result.getReason());
			logger.info("result.getResultCode() = " + result.getResultCode());
			logger.info("----------------------------------------");
		}

		return result;
	}

	private CbiDataItem getCbiDataItem(Admission admission, OcanStaffForm ocanStaffForm) throws Exception
	{
		CbiDataItem cbiDataItem = new CbiDataItem();

		cbiDataItem.setOrgId(ocanStaffForm.getFacilityId());
		cbiDataItem.setFcAdmissionId(admission.getId() + "");

		//set functional center id
		if(admission.getProgram()!=null && admission.getProgram().getFunctionalCentreId()!=null && 
				admission.getProgram().getFunctionalCentreId().trim().length()>0)
			cbiDataItem.setFcId(Integer.parseInt(admission.getProgram().getFunctionalCentreId().replaceAll("[\\t ]+", "")));

		cbiDataItem.setClientId(ocanStaffForm.getClientId() + "");

		if(admission.getAdmissionDate()!=null)
		{
			GregorianCalendar admissionCal = new GregorianCalendar();
			admissionCal.setTime(admission.getAdmissionDate());
			XMLGregorianCalendar admissionDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(admissionCal);
			
			cbiDataItem.setFcReferralDate(admissionDate);
			cbiDataItem.setFcAdmissionDate(admissionDate);
			cbiDataItem.setFcServiceInitDate(admissionDate);
		}

		if(admission.getDischargeDate()!=null)
		{
			GregorianCalendar dischargeCal = new GregorianCalendar();
			dischargeCal.setTime(admission.getDischargeDate());
			XMLGregorianCalendar dischargeDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(dischargeCal);
			cbiDataItem.setFcDischargeDate(dischargeDate);
		}

		if(ocanStaffForm.getHcNumber()!=null && ocanStaffForm.getHcNumber().trim().length()>0)
			cbiDataItem.setHealthCardNo(Long.parseLong(ocanStaffForm.getHcNumber()));

		cbiDataItem.setFirstname(ocanStaffForm.getFirstName());
		cbiDataItem.setLastnameAtBirth(ocanStaffForm.getLastNameAtBirth());
		cbiDataItem.setLastnameCurrent(ocanStaffForm.getLastName());

		//set dob
		if(ocanStaffForm.getDateOfBirth()!=null && ocanStaffForm.getDateOfBirth().trim().length()>0)
		{
			GregorianCalendar calDOB = new GregorianCalendar();
			calDOB.setTime(dateFormatter.parse(ocanStaffForm.getDateOfBirth()));
			XMLGregorianCalendar xmlDOB = DatatypeFactory.newInstance().newXMLGregorianCalendar(calDOB);
			cbiDataItem.setDateOfBirth(xmlDOB);
		}

		if(ocanStaffForm.getEstimatedAge()!=null && ocanStaffForm.getEstimatedAge().trim().length()>0)
			cbiDataItem.setAge(Integer.valueOf(ocanStaffForm.getEstimatedAge()));

		cbiDataItem.setGender(ocanStaffForm.getGender());
		cbiDataItem.setAddress1(ocanStaffForm.getAddressLine1());
		cbiDataItem.setAddress2(ocanStaffForm.getAddressLine2());
		cbiDataItem.setCity(ocanStaffForm.getCity());
		cbiDataItem.setPostalCode(ocanStaffForm.getPostalCode());

		//cbiDataItem.setLhinOfResidence(value);
		cbiDataItem.setPhone(ocanStaffForm.getPhoneNumber());

		return cbiDataItem;
	}

	private Integer insertIntoSubmissionLog(String result, String transactionId, String resultMsg)
	{
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");

		OcanSubmissionLog submissionLog = new OcanSubmissionLog();
		submissionLog.setResult(result);
		submissionLog.setTransactionId(transactionId);
		submissionLog.setResultMessage(resultMsg);

		ocanSubmissionLogDao.persist(submissionLog);

		return submissionLog.getId();
	}

	// checks whether the record is already submitted to cbi service or not.
	// if submissionId = 0, then it is not submitted
	// else if record_created_date > record_submitted_date then it is not
	// submitted
	// if log entry is not created for the record, then it is not submitted
	public boolean isFormDataAlreadySubmitted(OcanStaffForm ocanStaffForm)
	{
		// if ocanStaffForm=null, then it will consider it as submitted
		boolean flg = true;

		if (ocanStaffForm != null)
		{
			// if submission id = 0 , then it is not submitted yet.
			if (ocanStaffForm.getSubmissionId() == 0)
			{
				flg = false;
			}
			else
			{
				// if submitted id is not 0, then check the submission date/time
				// if submission_date_time < created_date_time then it is not
				// submitted
				OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils
						.getBean("ocanSubmissionLogDao");
				OcanSubmissionLog submissionLog = ocanSubmissionLogDao.find(ocanStaffForm.getSubmissionId());

				if (submissionLog != null)
				{
					if (ocanStaffForm.getCreated() != null && submissionLog.getSubmitDateTime() != null)
					{
						if (ocanStaffForm.getCreated().compareTo(submissionLog.getSubmitDateTime()) > 0)
							flg = false;
					}
				}
				else
				{
					// if log entry not found.. it means record is not submitted
					flg = false;
				}
			}
		}

		return flg;
	}

	/***
	 * get submitted log records based on the date
	 */
	public List<OcanSubmissionLog> getSubmissionLogRecords(Date submissionDate)
	{
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");
		List<OcanSubmissionLog> list = ocanSubmissionLogDao.findBySubmissionDate(submissionDate);
		return list;
	}

	/**
	 * get admission details for which admission_status = 'current' and program
	 * type is not 'community' and program's functionalCentreId is not null
	 *
	 */
	public List<Admission> getAdmissionDetailsToBeSubmittedToCBI()
	{
		List<Admission> admissionList = null;

		// get programs whose type is not 'community' .. just consider
		// bed/service program
		List<Program> programList = getPrograms();

		if (programList != null)
		{
			admissionList = new ArrayList<Admission>();

			AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
			List<Admission> tempAdmissionList = null;

			for (Program program : programList)
			{
				// only consider those program whose function center id is not
				// null
				if (program.getFunctionalCentreId() != null && program.getFunctionalCentreId().trim().length() > 0)
				{
					// get admission made in this program
					tempAdmissionList = admissionDao.getCurrentAdmissionsByProgramId(program.getId());
					admissionList.addAll(tempAdmissionList);
				}
			}
		}

		return admissionList;
	}

	private List<Program> getPrograms()
	{
		ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");

		// get bed programs
		Program[] bedProgramsArr = programDao.getBedPrograms();

		// get service programs
		List<Program> serviceProgramList = programDao.getServicePrograms();

		// combine programs
		List<Program> finalProgramList = new ArrayList<Program>();
		if (bedProgramsArr != null)
			finalProgramList.addAll(Arrays.asList(bedProgramsArr));
		if (serviceProgramList != null)
			finalProgramList.addAll(serviceProgramList);

		return finalProgramList;
	}

	public OcanStaffForm getCBIFormData(Integer clientId)
	{
		Integer facilityId = getFacilityId(clientId);
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, clientId, "CBI");

		return ocanStaffForm;
	}

	public OcanStaffForm getCBIFormDataBySubmissionId(Integer submissionId)
	{
		OcanStaffForm ocanStaffForm = null;

		Integer facilityId = LoggedInInfo.loggedInInfo.get().currentFacility.getId();
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		List<OcanStaffForm> ocanStaffFormList = ocanStaffFormDao.findBySubmissionId(facilityId, submissionId);
		if (ocanStaffFormList != null && ocanStaffFormList.size() > 0)
		{
			ocanStaffForm = ocanStaffFormList.get(0);
		}

		return ocanStaffForm;
	}

	// get facility id from client id
	/**
	 * 1) get provide no. from client id and get facility id of that provider no
	 * 2) if no facility id associted with provider no.. then get the first
	 * facility from facility table
	 */
	public Integer getFacilityId(Integer clientId)
	{
		Integer facilityId = 0;

		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic = demographicDao.getDemographic(clientId + "");
		if (demographic != null)
		{
			String providerNo = demographic.getProviderNo();

			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			List<Integer> facilityIds = providerDao.getFacilityIds(providerNo);
			if (facilityIds == null || facilityIds.size() == 0)
			{
				// if provider not associated with facility then get the 1st one
				// from facility table
				FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
				List<Facility> facilities = facilityDao.findAll(true);
				if (facilities != null && facilities.size() >= 1)
				{
					Facility fac = facilities.get(0);
					facilityId = fac.getId();
				}
			}
			else
			{
				facilityId = facilityIds.get(0);
			}
		}

		return facilityId;
	}

	public List<Date> getSubmissionDateList()
	{
		List<Date> list = null;

		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");
		List<OcanSubmissionLog> submissionLogsList = ocanSubmissionLogDao.findAll();
		if (submissionLogsList != null)
		{
			list = new ArrayList<Date>();

			for (OcanSubmissionLog ocanSubmissionLog : submissionLogsList)
			{
				list.add(ocanSubmissionLog.getSubmitDateTime());
			}
		}

		return list;
	}

	/**
	 * generate tree structure to be display in admin screen
	 *
	 * @param list
	 * @return
	 */
	public StringBuffer generateTree(List<Date> list)
	{
		StringBuffer strbuff = new StringBuffer();

		LinkedHashMap<String, LinkedHashMap<String, HashSet<String>>> resultMap = prepareDataStructure(list);
		if (resultMap != null)
		{
			Set<String> yearSet = resultMap.keySet();
			for (String year : yearSet)
			{
				strbuff.append("<ul class='ul1'>");
				strbuff.append("<li>");
				strbuff.append("<a href='#'>");
				strbuff.append(year);
				strbuff.append("</a>");
				LinkedHashMap<String, HashSet<String>> monthMap = resultMap.get(year);
				if (monthMap != null)
				{
					Set<String> monthSet = monthMap.keySet();
					for (String month : monthSet)
					{
						strbuff.append("<ul>");
						strbuff.append("<li>");
						strbuff.append("<a href='#'>");
						strbuff.append(month);
						strbuff.append("</a>");
						strbuff.append("<ul>");
						Set<String> daySet = monthMap.get(month);
						for (String day : daySet)
						{
							strbuff.append("<li>");
							strbuff.append("<a href='#' onclick='onclick_tree_item(this);'>");
							strbuff.append(year + "-" + month + "-" + day);
							strbuff.append("</a>");
							strbuff.append("</li>");
						}
						strbuff.append("</ul>");
						strbuff.append("</li>");
						strbuff.append("</ul>");
					}
				}

				strbuff.append("</li>");
				strbuff.append("</ul>");
			}
		}

		return strbuff;
	}

	/**
	 * data structure - LinkedHashMap<YEAR-VALUE, LinkedHashMap<MONTH-VALUE,
	 * HashSet<String>(day-of-months)>>
	 */
	public LinkedHashMap<String, LinkedHashMap<String, HashSet<String>>> prepareDataStructure(List<Date> list)
	{
		LinkedHashMap<String, LinkedHashMap<String, HashSet<String>>> resultMap = new LinkedHashMap<String, LinkedHashMap<String, HashSet<String>>>();

		if (list != null)
		{
			String year = "", month = "", day = "";
			Calendar cal = null;
			for (Date date : list)
			{
				cal = Calendar.getInstance();
				cal.setTime(date);

				year = cal.get(Calendar.YEAR) + "";
				month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
				day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

				if (resultMap.get(year) == null)
				{
					LinkedHashMap<String, HashSet<String>> monthMap = new LinkedHashMap<String, HashSet<String>>();
					resultMap.put(year, monthMap);
				}

				LinkedHashMap<String, HashSet<String>> monthMap = resultMap.get(year);
				if (monthMap.get(month) == null)
				{
					HashSet<String> daySet = new HashSet<String>();
					monthMap.put(month, daySet);
				}

				HashSet<String> dayList = monthMap.get(month);
				dayList.add(day);
			}
		}

		return resultMap;
	}

}
