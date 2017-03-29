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

import java.io.ByteArrayOutputStream;
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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.OcanSubmissionLogDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.OcanSubmissionLog;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.FunctionalCentreAdmissionDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.FunctionalCentreAdmission;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

import com.cbi.ws.CBIService;
import com.cbi.ws.CbiDataItem;
import com.cbi.ws.CbiServiceResult;

public class CBIUtil
{
	private static Logger logger = Logger.getLogger(CBIUtil.class);
	private static OscarProperties oscarProperties = OscarProperties.getInstance();
	
	// submit cbi data to cbi web service
	public void submitCBIData(OcanStaffForm ocanStaffForm) throws Exception
	{
		logger.debug("in CBIUtil : submitCBIData");		
		logger.debug("ocanStaffForm = " + ocanStaffForm);

		// invoke webservice to submit data
		CbiServiceResult cbiServiceResult = invokeWebService(ocanStaffForm);
		String result = "true";
		String transactionId = null;
		String resultMsg = "";

		if (cbiServiceResult != null)
		{
			result = (cbiServiceResult.getResultCode() == 1002) ? "false" : "true";  //All data errors trigger the same error code 1002
			resultMsg = cbiServiceResult.getDescription() + " " + cbiServiceResult.getReason();
			transactionId = cbiServiceResult.getReason();		
			
		}

		resultMsg = resultMsg + " - FORM ID : " + ocanStaffForm.getId();

		Integer submissionId = insertIntoSubmissionLog(result, transactionId, resultMsg);
		logger.debug("submissionId = " + submissionId);

		// update submission id in OcanStaffForm
		ocanStaffForm.setSubmissionId(submissionId);
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		ocanStaffFormDao.merge(ocanStaffForm);

		logger.debug("out CBIUtil : submitCBIData");
	}

	private CbiServiceResult invokeWebService(OcanStaffForm ocanStaffForm) throws Exception
	{
		logger.debug("invoking CBI web service");

		String CBI_WS_URL = oscarProperties.getProperty("CBI_WS_URL");
		String CBI_WS_USERNAME = oscarProperties.getProperty("CBI_WS_USERNAME");
		String CBI_WS_PASSWORD = oscarProperties.getProperty("CBI_WS_PASSWORD");
		String CBI_WS_CLIENT_QNAME = oscarProperties.getProperty("CBI_WS_CLIENT_QNAME");
		String CBI_WS_CLIENT_QNAME_URL = oscarProperties.getProperty("CBI_WS_CLIENT_QNAME_URL");		
	
		logger.info("CBI_WS_URL = " + CBI_WS_URL);

		URL url = new URL(CBI_WS_URL);
		QName qname = new QName(CBI_WS_CLIENT_QNAME_URL, CBI_WS_CLIENT_QNAME);

		Service service = Service.create(url, qname);

		CBIService cbiService = service.getPort(CBIService.class);

		Map<String, Object> req_ctx = ((BindingProvider)cbiService).getRequestContext();
		req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CBI_WS_URL);

		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Username", Collections.singletonList(CBI_WS_USERNAME));
		headers.put("Password", Collections.singletonList(CBI_WS_PASSWORD));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
 		
		//add logging handler only if debug mode is on.. 
		//in info mode no need to create a handler object
		if(logger.isDebugEnabled())
			addLoggingHandler(cbiService);
		
		CbiDataItem cbiDataItem = getCbiDataItem(ocanStaffForm);
		CbiServiceResult result = cbiService.processCBIItem(cbiDataItem);
		
		logger.debug("result = " + result);

		if (result != null)
		{			
			logger.debug("CBI response result.getDescription() = " + result.getDescription());
			logger.debug("CBI response result.getReason() = " + result.getReason());
			logger.debug("CBI response result.getResultCode() = " + result.getResultCode());			
		}

		return result;
	}


	private CbiDataItem getCbiDataItem(OcanStaffForm ocanStaffForm) throws Exception
	{
		CbiDataItem cbiDataItem = new CbiDataItem();
		
		FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");	
		
		cbiDataItem.setOrgId(String.valueOf(facilityDao.find(ocanStaffForm.getFacilityId()).getOrgId()));
		
		//cbiDataItem.setFcAdmissionId(ocanStaffForm.getAdmissionId() + "");		
		cbiDataItem.setProgramEnrollmentId(ocanStaffForm.getAdmissionId() + "");
		
		FunctionalCentreAdmissionDao fc_admissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
		FunctionalCentreAdmission fc_admission = fc_admissionDao.find(ocanStaffForm.getAdmissionId());
		//set functional center id
		if(fc_admission.getFunctionalCentreId()!=null && fc_admission.getFunctionalCentreId().trim().length()>0)
			cbiDataItem.setFcId(Integer.parseInt(fc_admission.getFunctionalCentreId().replaceAll("[\\t ]+", "")));
				
		cbiDataItem.setClientId(ocanStaffForm.getClientId() + "");						
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if(ocanStaffForm.getAdmissionDate()!=null)
		{
			cbiDataItem.setFcAdmissionDate(asXMLGregorianCalendar(formatter.format(ocanStaffForm.getAdmissionDate())));
			
		}
		if(ocanStaffForm.getReferralDate()!=null)
		{
			cbiDataItem.setFcReferralDate(asXMLGregorianCalendar(formatter.format(ocanStaffForm.getReferralDate())));
		}
		if(ocanStaffForm.getServiceInitDate()!=null)
		{	
			cbiDataItem.setFcServiceInitDate(asXMLGregorianCalendar(formatter.format(ocanStaffForm.getServiceInitDate())));
		}

		if(ocanStaffForm.getDischargeDate()!=null)
		{
			cbiDataItem.setFcDischargeDate(asXMLGregorianCalendar(formatter.format(ocanStaffForm.getDischargeDate())));
		}

		if(ocanStaffForm.getHcNumber()!=null && ocanStaffForm.getHcNumber().trim().length()>0)
			cbiDataItem.setHealthCardNo(Long.parseLong(ocanStaffForm.getHcNumber()));

		cbiDataItem.setFirstname(ocanStaffForm.getFirstName());
		cbiDataItem.setLastnameAtBirth(ocanStaffForm.getLastNameAtBirth());
		cbiDataItem.setLastnameCurrent(ocanStaffForm.getLastName());

		//set dob
		if(ocanStaffForm.getDateOfBirth()!=null && ocanStaffForm.getDateOfBirth().trim().length()>0)
		{
			cbiDataItem.setDateOfBirth(asXMLGregorianCalendar(ocanStaffForm.getDateOfBirth()));
		}

		if(ocanStaffForm.getEstimatedAge()!=null && ocanStaffForm.getEstimatedAge().trim().length()>0)
			cbiDataItem.setAge(Integer.valueOf(ocanStaffForm.getEstimatedAge()));

		cbiDataItem.setGender(ocanStaffForm.getGender());
		cbiDataItem.setAddress1(ocanStaffForm.getAddressLine1());
		cbiDataItem.setAddress2(ocanStaffForm.getAddressLine2());
		cbiDataItem.setCity(ocanStaffForm.getCity()); //province?	
		cbiDataItem.setPostalCode(ocanStaffForm.getPostalCode());
		cbiDataItem.setPhone(ocanStaffForm.getPhoneNumber());
		
		OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");			
		cbiDataItem.setLhinOfResidence(ocanStaffFormDataDao.findByQuestion(ocanStaffForm.getId(), "service_recipient_lhin").size()>0?ocanStaffFormDataDao.findByQuestion(ocanStaffForm.getId(), "service_recipient_lhin").get(0).getAnswer():"");	

		cbiDataItem.setAction("I");
		return cbiDataItem;
	}

	private Integer insertIntoSubmissionLog(String result, String transactionId, String resultMsg)
	{
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");

		OcanSubmissionLog submissionLog = new OcanSubmissionLog();
		submissionLog.setResult(result);
		submissionLog.setTransactionId(transactionId);
		submissionLog.setResultMessage(resultMsg);
		submissionLog.setSubmissionType("CBI");
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
			//Find if this admissionId already has cbi form submitted.
			//If there is no one ever submitted, then submit
			//If there is one already submitted, 
			
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
	public List<OcanSubmissionLog> getCbiSubmissionLogRecords(Date submissionDate)
	{
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");
		List<OcanSubmissionLog> list = ocanSubmissionLogDao.findBySubmissionDateType(submissionDate, "CBI");
		return list;
	}

	public List<OcanSubmissionLog> getCbiSubmissionLogRecords(Date submissionStartDate, Date submissionEndDate)
	{
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");
		GregorianCalendar endDateInclusive=new GregorianCalendar();
		endDateInclusive.setTime(submissionEndDate);
		endDateInclusive.add(GregorianCalendar.DAY_OF_YEAR, 1);
		List<OcanSubmissionLog> list = ocanSubmissionLogDao.findBySubmissionDateType(submissionStartDate, endDateInclusive.getTime(), "CBI");
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
	
	
	public List<OcanStaffForm> getUnsubmittedCbiForms() {
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		List<OcanStaffForm> allCbiForms = ocanStaffFormDao.getLatestCbiFormsByGroupOfAdmissionId();
		List<OcanStaffForm> toBeSubmittedCbiForms = new ArrayList<OcanStaffForm>();
		Integer admissionId_new = 0;
		Integer admissionId_old = 0;
		if(allCbiForms!=null) {
			//Only submit the latest SIGNED cbi form for each admission_id
			for(OcanStaffForm form : allCbiForms) {
				admissionId_new = form.getAdmissionId();
				if(admissionId_old.intValue()!=admissionId_new.intValue()) {
					if(form.getSubmissionId()==0 && form.isSigned()) //submissionId=0 means not submitted yet
						toBeSubmittedCbiForms.add(form);					
				}
				admissionId_old = form.getAdmissionId();
			}				
		}
		return toBeSubmittedCbiForms;
	}
	
	public OcanStaffForm getLatestCbiFormByDemographicNoAndProgramId(Integer facilityId, Integer demographicNo, Integer programId) {
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
		Admission admission = admissionDao.getAdmission(programId, demographicNo);
		Integer admissionId = Integer.valueOf(admission.getId().intValue());
		OcanStaffForm cbiForm = ocanStaffFormDao.findLatestCbiFormsByFacilityAdmissionId(facilityId, admissionId, null); //could be signed or unsigned
		return cbiForm;				
	}
	
	
	private List<Program> getPrograms()
	{
		ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");

		// get bed programs
		Program[] bedProgramsArr = programDao.getProgramsByType(null, Program.BED_TYPE, null).toArray(new Program[0]);

		// get service programs
		List<Program> serviceProgramList = programDao.getProgramsByType(null, Program.SERVICE_TYPE, null);

		// combine programs
		List<Program> finalProgramList = new ArrayList<Program>();
		if (bedProgramsArr != null)
			finalProgramList.addAll(Arrays.asList(bedProgramsArr));
		if (serviceProgramList != null)
			finalProgramList.addAll(serviceProgramList);

		return finalProgramList;
	}

	public OcanStaffForm getCBIFormData(LoggedInInfo loggedInInfo, Integer clientId)
	{
		Integer facilityId = getFacilityId(loggedInInfo, clientId);
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, clientId, "CBI");

		return ocanStaffForm;
	}

	public OcanStaffForm getCBIFormDataBySubmissionId(Integer facilityId, Integer submissionId)
	{
		OcanStaffForm ocanStaffForm = null;

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
	public Integer getFacilityId(LoggedInInfo loggedInInfo, Integer clientId)
	{
		Integer facilityId = 0;

		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
		Demographic demographic = demographicManager.getDemographic(loggedInInfo, clientId);
		if (demographic != null)
		{
			String providerNo = demographic.getProviderNo();

			
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
		List<OcanSubmissionLog> submissionLogsList = ocanSubmissionLogDao.findAllByType("CBI");
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
	
	public static String getCbiSubmissionFailureWarningMessage(Integer facilityId, String providerNo) {
		
		StringBuilder messages = new StringBuilder();		
	
		OcanSubmissionLogDao ocanSubmissionLogDao = (OcanSubmissionLogDao) SpringUtils.getBean("ocanSubmissionLogDao");
		OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
		List<OcanSubmissionLog> failedSubmissionList = ocanSubmissionLogDao.findFailedSubmissionsByType("CBI");
		if(failedSubmissionList!=null && !failedSubmissionList.isEmpty()) {
			boolean appendFailureMessage = true;						
			for(OcanSubmissionLog submissionLog : failedSubmissionList) {
				OcanStaffForm cbiForm = ocanStaffFormDao.findByProviderAndSubmissionId(providerNo, submissionLog.getId(),"CBI");
				if(cbiForm!=null) {					
					if(appendFailureMessage) {
						messages.append("Failed to upload CBI forms: ");
						appendFailureMessage = false;
					}
					messages.append("form id:" +cbiForm.getId() +", client id:"+cbiForm.getClientId() + " ,client name:" + cbiForm.getFirstName() + " " + cbiForm.getLastName() + " ; " );
				}
			}
		}
		
		return messages.toString();
		
		
		
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
	
	
	private void addLoggingHandler(CBIService cbiService)
	{
		BindingProvider bindingProvider = (BindingProvider) cbiService;
		Binding binding = bindingProvider.getBinding();
		List<Handler> handlerList = binding.getHandlerChain();

		handlerList.add(new SOAPHandler<SOAPMessageContext>()
		{
			@Override
			public boolean handleMessage(SOAPMessageContext context)
			{
				SOAPMessage msg = context.getMessage();
				Boolean outboundProperty = (Boolean) context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

				try
				{
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					msg.writeTo(bout);
					
					String requestResponseStr = "================= cbi response xml =================";
					if(outboundProperty)
						requestResponseStr = "-------------------- cbi request xml -----------------------";
					
					logger.debug(requestResponseStr+" : "+bout.toString());
				}
				catch (Exception e)
				{
					logger.error("error while printing soap message");
				}
				return true;
			}

			@Override
			public boolean handleFault(SOAPMessageContext context)
			{
				return false;
			}

			@Override
			public void close(MessageContext context)
			{
			}

			@Override
			public Set<QName> getHeaders()
			{
				return null;
			}

		});
		binding.setHandlerChain(handlerList);
		
	}
	
	
	
	private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            logger.error("Exception while obtaining DatatypeFactory instance");
        }
    }  

	 private static XMLGregorianCalendar asXMLGregorianCalendar(String fromDate)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			

	        java.util.Date date = null;

	        try { 
				fromDate = fromDate.concat(" 12:13:14"); //Because CBI server does not handle time zone properly, if time is 00:00:00, it will cut one day off. So have to put fake time here.
				date = sdf.parse(fromDate); 
			} catch (Exception e) { 
				logger.error("error while setting cbi date"); 
			}

	        if (date == null) {
	            return null;
	        } else {
	            GregorianCalendar gc = new GregorianCalendar();
	            gc.clear();	        
	            String timezone = gc.getTimeZone().getDisplayName();    
	            gc.setTimeInMillis(date.getTime());
	            XMLGregorianCalendar xmlCalendar = df.newXMLGregorianCalendar(gc);
	            	            
	            xmlCalendar.setTimezone(-300); //always set it as Eastern Standard Time because of CBI server code problem. We don't care about time anyway.
	                       
	            return xmlCalendar;
	        }
	    }
	 
	 
}
