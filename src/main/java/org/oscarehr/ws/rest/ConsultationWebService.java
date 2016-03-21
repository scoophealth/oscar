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
package org.oscarehr.ws.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.BORNPathwayMappingDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.BORNPathwayMapping;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.ConsultResponseDoc;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.consultations.ConsultationRequestSearchFilter;
import org.oscarehr.consultations.ConsultationResponseSearchFilter;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.conversion.ConsultationRequestConverter;
import org.oscarehr.ws.rest.conversion.ConsultationResponseConverter;
import org.oscarehr.ws.rest.conversion.ConsultationServiceConverter;
import org.oscarehr.ws.rest.conversion.DemographicConverter;
import org.oscarehr.ws.rest.conversion.ProfessionalSpecialistConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.ReferralResponse;
import org.oscarehr.ws.rest.to.model.ConsultationAttachmentTo1;
import org.oscarehr.ws.rest.to.model.ConsultationRequestSearchResult;
import org.oscarehr.ws.rest.to.model.ConsultationRequestTo1;
import org.oscarehr.ws.rest.to.model.ConsultationResponseSearchResult;
import org.oscarehr.ws.rest.to.model.ConsultationResponseTo1;
import org.oscarehr.ws.rest.to.model.FaxConfigTo1;
import org.oscarehr.ws.rest.to.model.LetterheadTo1;
import org.oscarehr.ws.rest.to.model.ProfessionalSpecialistTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.eform.EFormUtil;
import oscar.oscarDemographic.data.RxInformation;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConversionUtils;

@Path("/consults")
@Component("consultationWebService")
public class ConsultationWebService extends AbstractServiceImpl {

	Pattern namePtrn = Pattern.compile("sorting\\[(\\w+)\\]");
	
	@Autowired
	ConsultationManager consultationManager;
	
	@Autowired
	CaseManagementManager caseManagementManager;
	
	@Autowired
	DemographicManager demographicManager;
	
	@Autowired
	ProviderDao providerDao;
	
	@Autowired
	FaxConfigDao faxConfigDao;
	
	@Autowired
	ClinicDAO clinicDAO;
	
	@Autowired
	UserPropertyDAO userPropertyDAO;
	
	@Autowired
	ConsultationServiceDao consultationServiceDao;
	
	private ConsultationRequestConverter requestConverter = new ConsultationRequestConverter();
	private ConsultationResponseConverter responseConverter = new ConsultationResponseConverter();
	private ConsultationServiceConverter serviceConverter = new ConsultationServiceConverter();
	private ProfessionalSpecialistConverter specialistConverter = new ProfessionalSpecialistConverter();
	private DemographicConverter demographicConverter = new DemographicConverter();
	
	
	/********************************
	 * Consultation Request methods *
	 ********************************/
	@POST
	@Path("/searchRequests")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AbstractSearchResponse<ConsultationRequestSearchResult> searchRequests(JSONObject json) {
		AbstractSearchResponse<ConsultationRequestSearchResult> rp = new AbstractSearchResponse<ConsultationRequestSearchResult>();
				
		int count = consultationManager.getConsultationCount(convertRequestJSON(json));

		if(count>0) {
			List<ConsultationRequestSearchResult> items =  consultationManager.search(getLoggedInInfo(), convertRequestJSON(json));
			//convert items to a ConsultationRequestSearchResult object
			rp.setContent(items);
			rp.setTotal(count);
		}
		
		return rp;
	}
	
	@GET
	@Path("/getRequest")
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationRequestTo1 getRequest(@QueryParam("requestId")Integer requestId, @QueryParam("demographicId")Integer demographicId) {
		ConsultationRequestTo1 request = new ConsultationRequestTo1();
		
		if (requestId>0) {
			request = requestConverter.getAsTransferObject(getLoggedInInfo(), consultationManager.getRequest(getLoggedInInfo(), requestId));
			request.setAttachments(getRequestAttachments(requestId, request.getDemographicId(), ConsultationAttachmentTo1.ATTACHED));
		} else {
			request.setDemographicId(demographicId);
			
			RxInformation rx = new RxInformation();
			String info = rx.getAllergies(getLoggedInInfo(), demographicId.toString());
			if (StringUtils.isNotBlank(info)) request.setAllergies(info);
			info = rx.getCurrentMedication(demographicId.toString());
			if (StringUtils.isNotBlank(info)) request.setCurrentMeds(info);
		}

		request.setLetterheadList(getLetterheadList());
		request.setFaxList(getFaxList());
		request.setServiceList(serviceConverter.getAllAsTransferObjects(getLoggedInInfo(), consultationManager.getConsultationServices()));
		request.setSendToList(providerDao.getActiveTeams());
		request.setProviderNo(getLoggedInInfo().getLoggedInProviderNo());
		
		return request;
	}
	
	@GET
	@Path("/getRequestAttachments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ConsultationAttachmentTo1> getRequestAttachments(@QueryParam("requestId")Integer requestId, @QueryParam("demographicId")Integer demographicIdInt, @QueryParam("attached")boolean attached) {
		List<ConsultationAttachmentTo1> attachments = new ArrayList<ConsultationAttachmentTo1>();
		String demographicId = demographicIdInt.toString();
		
		List<EDoc> edocs = EDocUtil.listDocs(getLoggedInInfo(), demographicId, requestId.toString(), attached);
		getDocuments(edocs, attached, attachments);
		
		List<EFormData> eforms = EFormUtil.listPatientEFormsShowLatestOnly(demographicId);
		getEformsForRequest(eforms, attached, attachments, requestId);
		
		List<LabResultData> labs = new CommonLabResultData().populateLabResultsData(getLoggedInInfo(), demographicId, requestId.toString(), attached);
		getLabs(labs, demographicId, attached, attachments);
		
		return attachments;
	}

	@POST
	@Path("/saveRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationRequestTo1 saveRequest(ConsultationRequestTo1 data) {
		ConsultationRequest request = null;
		
		if (data.getId()==null) { //new consultation request
			request = requestConverter.getAsDomainObject(getLoggedInInfo(), data);
		} else {
			request = requestConverter.getAsDomainObject(getLoggedInInfo(), data, consultationManager.getRequest(getLoggedInInfo(), data.getId()));
		}
		request.setProfessionalSpecialist(consultationManager.getProfessionalSpecialist(data.getProfessionalSpecialist().getId()));
		consultationManager.saveConsultationRequest(getLoggedInInfo(), request);
	    if (data.getId()==null) data.setId(request.getId());
	    
		//save attachments
		saveRequestAttachments(data);
		
		return data;
	}
	
	@GET
	@Path("/eSendRequest")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericRESTResponse eSendRequest(@QueryParam("requestId")Integer requestId) {
		GenericRESTResponse rp = new GenericRESTResponse();
		try {
            consultationManager.doHl7Send(getLoggedInInfo(), requestId);
            rp.setSuccess(true);
            rp.setMessage("Referral Electronically Sent");
        } catch (Exception e) {
        	MiscUtils.getLogger().error("Error contacting remote server.", e);
        	rp.setSuccess(false);
        	rp.setMessage("There was an error sending electronically, please try again or manually process the referral.");
        }
		return rp;
	}
	
	
	/********************************
	 * Consultation Response methods *
	 ********************************/
	@POST
	@Path("/searchResponses")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AbstractSearchResponse<ConsultationResponseSearchResult> searchResponses(JSONObject json) {
		AbstractSearchResponse<ConsultationResponseSearchResult> rp = new AbstractSearchResponse<ConsultationResponseSearchResult>();
				
		int count = consultationManager.getConsultationCount(convertResponseJSON(json));

		if(count>0) {
			List<ConsultationResponseSearchResult> items =  consultationManager.search(getLoggedInInfo(), convertResponseJSON(json));
			//convert items to a ConsultationResponseSearchResult object
			rp.setContent(items);
			rp.setTotal(count);
		}
		return rp;
	}
	
	@GET
	@Path("/getResponse")
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationResponseTo1 getResponse(@QueryParam("responseId")Integer responseId, @QueryParam("demographicNo")Integer demographicNo) {
		ConsultationResponseTo1 response = new ConsultationResponseTo1();
		
		if (responseId>0) {
			ConsultationResponse responseD = consultationManager.getResponse(getLoggedInInfo(), responseId);
			response = responseConverter.getAsTransferObject(getLoggedInInfo(), responseD);
			
			demographicNo = responseD.getDemographicNo();
			
			ProfessionalSpecialist referringDoctorD = consultationManager.getProfessionalSpecialist(responseD.getReferringDocId());
			response.setReferringDoctor(specialistConverter.getAsTransferObject(getLoggedInInfo(), referringDoctorD));
			
			response.setAttachments(getResponseAttachments(responseId, demographicNo, ConsultationAttachmentTo1.ATTACHED));
		} else {
			response.setProviderNo(getLoggedInInfo().getLoggedInProviderNo());
			RxInformation rx = new RxInformation();
			String info = rx.getAllergies(getLoggedInInfo(), demographicNo.toString());
			if (StringUtils.isNotBlank(info)) response.setAllergies(info);
			info = rx.getCurrentMedication(demographicNo.toString());
			if (StringUtils.isNotBlank(info)) response.setCurrentMeds(info);
		}

		Demographic demographicD = demographicManager.getDemographicWithExt(getLoggedInInfo(), demographicNo);
		response.setDemographic(demographicConverter.getAsTransferObject(getLoggedInInfo(), demographicD));
		
		response.setLetterheadList(getLetterheadList());
		response.setReferringDoctorList(getReferringDoctorList());
		response.setFaxList(getFaxList());
		response.setSendToList(providerDao.getActiveTeams());
		
		return response;
	}
	
	@GET
	@Path("/getResponseAttachments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ConsultationAttachmentTo1> getResponseAttachments(@QueryParam("responseId")Integer responseId, @QueryParam("demographicNo")Integer demographicNoInt, @QueryParam("attached")boolean attached) {
		List<ConsultationAttachmentTo1> attachments = new ArrayList<ConsultationAttachmentTo1>();
		String demographicNo = demographicNoInt.toString();
		
		List<EDoc> edocList = EDocUtil.listResponseDocs(getLoggedInInfo(), demographicNo, responseId.toString(), attached);
		getDocuments(edocList, attached, attachments);
		
		List<EFormData> eformList = EFormUtil.listPatientEFormsShowLatestOnly(demographicNo);
		getEformsForResponse(eformList, attached, attachments, responseId);
		
		List<LabResultData> labs = new CommonLabResultData().populateLabResultsDataConsultResponse(getLoggedInInfo(), demographicNo, responseId.toString(), attached);
		getLabs(labs, demographicNo, attached, attachments);
		
		return attachments;
	}

	@POST
	@Path("/saveResponse")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationResponseTo1 saveResponse(ConsultationResponseTo1 data) {
		ConsultationResponse response = null;
		
		if (data.getId()==null) { //new consultation response
			response = responseConverter.getAsDomainObject(getLoggedInInfo(), data);
		} else {
			response = responseConverter.getAsDomainObject(getLoggedInInfo(), data, consultationManager.getResponse(getLoggedInInfo(), data.getId()));
		}
		consultationManager.saveConsultationResponse(getLoggedInInfo(), response);
	    if (data.getId()==null) data.setId(response.getId());
	    
		//save attachments
		saveResponseAttachments(data);
		
		return data;
	}
	
	@GET
	@Path("/getReferralPathwaysByService")
	@Produces(MediaType.APPLICATION_JSON)
	public ReferralResponse getReferralPathwaysByService(@QueryParam("serviceName") String serviceName) {
		ReferralResponse response = new ReferralResponse();
		
		//check for a mapping, or else just use the BORN service name.
		BORNPathwayMappingDao bornPathwayMappingDao = SpringUtils.getBean(BORNPathwayMappingDao.class);
		List<BORNPathwayMapping> mappings = bornPathwayMappingDao.findByBornPathway(serviceName);
		List<ProfessionalSpecialist> specs = new ArrayList<ProfessionalSpecialist>();
		
		if(mappings.isEmpty()) {
			specs = consultationManager.findByService(getLoggedInInfo(), serviceName);
			ConsultationServices cs = consultationServiceDao.findByDescription(serviceName);
			if(cs != null) {
				response.getServices().add(serviceConverter.getAsTransferObject(getLoggedInInfo(), cs));
			}
		} else {
			for(BORNPathwayMapping mapping:mappings) {
				specs.addAll(consultationManager.findByServiceId(getLoggedInInfo(), mapping.getServiceId()));
				ConsultationServices cs = consultationServiceDao.find(mapping.getServiceId());
				if(cs != null) {
					response.getServices().add(serviceConverter.getAsTransferObject(getLoggedInInfo(), cs));
				}
			}
		}
		
		ProfessionalSpecialistConverter converter = new ProfessionalSpecialistConverter();
		response.setSpecialists(converter.getAllAsTransferObjects(getLoggedInInfo(), specs));
		
		return response;
	}
	
	@GET
	@Path("/getProfessionalSpecialist")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfessionalSpecialistTo1 getProfessionalSpecialist(@QueryParam("specId") Integer specId) {
		
		ProfessionalSpecialist ps = consultationManager.getProfessionalSpecialist(specId);
		if(ps != null) {
			ProfessionalSpecialistConverter converter = new ProfessionalSpecialistConverter();
			return converter.getAsTransferObject(getLoggedInInfo(), ps);	
		}
		
		return null;
	}
	
	
	
	/*******************
	 * private methods *
	 *******************/
	private Date convertJSONDate(String val) {
		try {
			return javax.xml.bind.DatatypeConverter.parseDateTime(val).getTime();
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Error parsing date - " + val);
		}
		return null;
	}
	
	private ConsultationRequestSearchFilter convertRequestJSON(JSONObject json) {
		ConsultationRequestSearchFilter filter = new ConsultationRequestSearchFilter();
		
		filter.setAppointmentEndDate(convertJSONDate((String)json.get("appointmentEndDate")));
		filter.setAppointmentStartDate(convertJSONDate((String)json.get("appointmentStartDate")));
		filter.setDemographicNo((Integer)json.get("demographicNo"));
		filter.setMrpNo((Integer)json.get("mrpNo"));
		filter.setNumToReturn((Integer)json.get("numToReturn"));
		filter.setReferralEndDate(convertJSONDate((String)json.get("referralEndDate")));
		filter.setReferralStartDate(convertJSONDate((String)json.get("referralStartDate")));
		filter.setStartIndex((Integer)json.get("startIndex"));
		filter.setStatus((Integer)json.get("status"));
		filter.setTeam((String)json.get("team"));
		
		JSONObject params = json.getJSONObject("params");
		if(params != null) {
			for(Object key:params.keySet()) {
				Matcher nameMtchr = namePtrn.matcher((String)key);
				if (nameMtchr.find()) {
				   String var = nameMtchr.group(1);
				   filter.setSortMode(ConsultationRequestSearchFilter.SORTMODE.valueOf(var));
				   filter.setSortDir(ConsultationRequestSearchFilter.SORTDIR.valueOf(params.getString((String)key)));
				}
			}
		}
		return filter;
	}
	
	private ConsultationResponseSearchFilter convertResponseJSON(JSONObject json) {
		ConsultationResponseSearchFilter filter = new ConsultationResponseSearchFilter();
		
		filter.setAppointmentEndDate(convertJSONDate((String)json.get("appointmentEndDate")));
		filter.setAppointmentStartDate(convertJSONDate((String)json.get("appointmentStartDate")));
		filter.setDemographicNo((Integer)json.get("demographicNo"));
		filter.setMrpNo((Integer)json.get("mrpNo"));
		filter.setNumToReturn((Integer)json.get("numToReturn"));
		filter.setReferralEndDate(convertJSONDate((String)json.get("referralEndDate")));
		filter.setReferralStartDate(convertJSONDate((String)json.get("referralStartDate")));
		filter.setResponseEndDate(convertJSONDate((String)json.get("responseEndDate")));
		filter.setResponseStartDate(convertJSONDate((String)json.get("responseStartDate")));
		filter.setStartIndex((Integer)json.get("startIndex"));
		filter.setStatus((Integer)json.get("status"));
		filter.setTeam((String)json.get("team"));
		
		JSONObject params = json.getJSONObject("params");
		if(params != null) {
			for(Object key:params.keySet()) {
				Matcher nameMtchr = namePtrn.matcher((String)key);
				if (nameMtchr.find()) {
				   String var = nameMtchr.group(1);
				   filter.setSortMode(ConsultationResponseSearchFilter.SORTMODE.valueOf(var));
				   filter.setSortDir(ConsultationResponseSearchFilter.SORTDIR.valueOf(params.getString((String)key)));
				}
			}
		}
		return filter;
	}

	private List<LetterheadTo1> getLetterheadList() {
		List<LetterheadTo1> letterheadList = new ArrayList<LetterheadTo1>();
		
		//clinic letterhead
		Clinic clinic = clinicDAO.getClinic();
		LetterheadTo1 letterhead = new LetterheadTo1(clinic.getClinicName(), clinic.getClinicName());
		
		String clinicPhone = StringUtils.trimToEmpty(clinic.getClinicPhone());
		String clinicAddress = buildAddress(clinic.getClinicAddress(), clinic.getClinicCity(), clinic.getClinicProvince(), clinic.getClinicPostal());
		
		letterhead.setPhone(clinicPhone);
		letterhead.setAddress(clinicAddress);
		
		letterheadList.add(letterhead);
		
		//provider letterheads
		//- find non-empty phone/address in the following priority:
		//- 1) UserProperty ("property" table)
		//- 2) Provider
		//- 3) Clinic
		List<Provider> providerList = providerDao.getActiveProviders();
		for (Provider provider : providerList) {
			String providerNo = provider.getProviderNo();
			if (providerNo.equals("-1")) continue; //skip user "system"

			letterhead = new LetterheadTo1(providerNo, provider.getFormattedName());
			
        	String propValue = readProperty(providerNo, "rxPhone");
        	if (propValue!=null) {
        		letterhead.setPhone(propValue);
        	}
        	else if (StringUtils.isNotBlank(provider.getWorkPhone())) {
        		letterhead.setPhone(provider.getWorkPhone().trim());
        	}
        	else {
        		letterhead.setPhone(clinicPhone);
        	}
			
			propValue = readProperty(providerNo, "rxAddress");
			if (propValue!=null) {
				letterhead.setAddress(buildAddress(propValue, readProperty(providerNo, "rxCity"), readProperty(providerNo, "rxProvince"), readProperty(providerNo, "rxPostal")));
			}
			else if (StringUtils.isNotBlank(provider.getAddress())) {
				letterhead.setAddress(provider.getAddress().trim());
			}
			else {
				letterhead.setAddress(clinicAddress);
			}
			
			letterheadList.add(letterhead);
		}
		
		return letterheadList;
	}
	
	private List<FaxConfigTo1> getFaxList() {
		List<FaxConfigTo1> faxList = new ArrayList<FaxConfigTo1>();
		List<FaxConfig> faxConfigList = faxConfigDao.findAll(null, null);
		for (FaxConfig faxConfig : faxConfigList) {
			FaxConfigTo1 faxConfigTo1 = new FaxConfigTo1();
			faxList.add(faxConfigTo1);
			faxConfigTo1.setFaxUser(faxConfig.getFaxUser());
			faxConfigTo1.setFaxNumber(faxConfig.getFaxNumber());
		}
		return faxList;
	}
	
	private List<ProfessionalSpecialistTo1> getReferringDoctorList() {
		List<ProfessionalSpecialistTo1> refDocList = new ArrayList<ProfessionalSpecialistTo1>();
		
		List<ProfessionalSpecialist> list = consultationManager.getReferringDoctorList();
		if (list!=null) {
			for (ProfessionalSpecialist specialist : list) {
				refDocList.add(specialistConverter.getAsTransferObject(getLoggedInInfo(), specialist));
			}
		}
		return refDocList;
	}
	
	private String buildAddress(String address, String city, String province, String postal) {
		address = StringUtils.trimToEmpty(address) + " " + StringUtils.trimToEmpty(city);
		address = StringUtils.trimToEmpty(address) + " " + StringUtils.trimToEmpty(province);
		address = StringUtils.trimToEmpty(address) + " " + StringUtils.trimToEmpty(postal);
		return StringUtils.trimToEmpty(address);
	}
	
	private String readProperty(String providerNo, String key) {
		UserProperty prop = userPropertyDAO.getProp(providerNo, key);
		if (prop!=null) return StringUtils.trimToNull(prop.getValue());
		else return null;
	}
	
	private void getDocuments(List<EDoc> edocs, boolean attached, List<ConsultationAttachmentTo1> attachments) {
		for (EDoc edoc : edocs) {
			String url = "dms/ManageDocument.do?method=display&doc_no="+edoc.getDocId();
			attachments.add(new ConsultationAttachmentTo1(ConversionUtils.fromIntString(edoc.getDocId()), ConsultationAttachmentTo1.TYPE_DOC, attached, edoc.getDescription(), url));
		}
	}
	
	private void getLabs(List<LabResultData> labs, String demographicNo, boolean attached, List<ConsultationAttachmentTo1> attachments) {
		for (LabResultData lab : labs) {
			String displayName = lab.getDiscipline()+" "+lab.getDateTime();
			
			String url = null;
			if (lab.isMDS()) url = "oscarMDS/SegmentDisplay.jsp?demographicId="+demographicNo+"&segmentID="+lab.getSegmentID();
			else if (lab.isCML()) url = "lab/CA/ON/CMLDisplay.jsp?demographicId="+demographicNo+"&segmentID="+lab.getSegmentID();
			else if (lab.isHL7TEXT()) url = "lab/CA/ALL/labDisplay.jsp?demographicId="+demographicNo+"&segmentID="+lab.getSegmentID();
			else url = "lab/CA/BC/labDisplay.jsp?demographicId="+demographicNo+"&segmentID="+lab.getSegmentID();
			
			attachments.add(new ConsultationAttachmentTo1(ConversionUtils.fromIntString(lab.getLabPatientId()), ConsultationAttachmentTo1.TYPE_LAB, attached, displayName, url));
		}
	}
	
	private void getEformsForRequest(List<EFormData> eforms, boolean attached, List<ConsultationAttachmentTo1> attachments, Integer consultId) {
		List<ConsultDocs> docs = consultationManager.getConsultRequestDocs(getLoggedInInfo(), consultId);
		List<Integer> docNos = new ArrayList<Integer>();
		if (docs!=null) {
			for (ConsultDocs doc : docs) {
				if (doc.getDocType().equals(ConsultDocs.DOCTYPE_EFORM)) docNos.add(doc.getDocumentNo());
			}
		}
		getEforms(eforms, attached, attachments, docNos);
	}
	
	private void getEformsForResponse(List<EFormData> eforms, boolean attached, List<ConsultationAttachmentTo1> attachments, Integer consultId) {
		List<ConsultResponseDoc> docs = consultationManager.getConsultResponseDocs(getLoggedInInfo(), consultId);
		List<Integer> docNos = new ArrayList<Integer>();
		if (docs!=null) {
			for (ConsultResponseDoc doc : docs) {
				if (doc.getDocType().equals(ConsultResponseDoc.DOCTYPE_EFORM)) docNos.add(doc.getDocumentNo());
			}
		}
		getEforms(eforms, attached, attachments, docNos);
	}
	
	private void getEforms(List<EFormData> eforms, boolean attached, List<ConsultationAttachmentTo1> attachments, List<Integer> docNos) {
		for (EFormData eform : eforms) {
			boolean found = false;
			for (Integer docNo : docNos) {
				if (eform.getId().equals(docNo)) {
					found = true; break;
				}
			}
			if (attached==found) {
				//if attached is wanted and attached found		OR
				//if detached is wanted and attached not found
				String url = "eform/efmshowform_data.jsp?fdid="+eform.getId();
				String displayName = eform.getFormName()+" "+eform.getFormDate();
				attachments.add(new ConsultationAttachmentTo1(ConversionUtils.fromIntString(eform.getId()), ConsultationAttachmentTo1.TYPE_EFORM, attached, displayName, url));
			}
		}
	}
	
	private void saveRequestAttachments(ConsultationRequestTo1 request) {
		List<ConsultationAttachmentTo1> newAttachments = request.getAttachments();
		List<ConsultDocs> currentDocs = consultationManager.getConsultRequestDocs(getLoggedInInfo(), request.getId());
		if (newAttachments==null || currentDocs==null) return;
		
		//first assume all current docs detached (set delete)
		for (ConsultDocs doc : currentDocs) {
			doc.setDeleted(ConsultDocs.DELETED);
		}
		
		//compare current & new, remove from current list the unchanged ones - no need to update them
		for (ConsultationAttachmentTo1 newAtth : newAttachments) {
			boolean isNew = true;
			for (ConsultDocs doc : currentDocs) {
				if (doc.getDocType().equals(newAtth.getDocumentType()) && doc.getDocumentNo()==newAtth.getDocumentNo()) {
					currentDocs.remove(doc);
					isNew = false;
					break;
				}
			}
			if (isNew) { //save the new attachment
				consultationManager.saveConsultRequestDoc(getLoggedInInfo(), new ConsultDocs(request.getId(), newAtth.getDocumentNo(), newAtth.getDocumentType(), getLoggedInInfo().getLoggedInProviderNo()));
			}
		}
		
		//update what remains in current docs, they are detached (set delete)
		for (ConsultDocs doc : currentDocs) {
			consultationManager.saveConsultRequestDoc(getLoggedInInfo(), doc);
		}
	}
	
	private void saveResponseAttachments(ConsultationResponseTo1 response) {
		List<ConsultationAttachmentTo1> newAttachments = response.getAttachments();
		List<ConsultResponseDoc> currentDocs = consultationManager.getConsultResponseDocs(getLoggedInInfo(), response.getId());
		if (newAttachments==null || currentDocs==null) return;
		
		//first assume all current docs detached (set delete)
		for (ConsultResponseDoc doc : currentDocs) {
			doc.setDeleted(ConsultResponseDoc.DELETED);
		}
		
		//compare current & new, remove from current list the unchanged ones - no need to update them
		for (ConsultationAttachmentTo1 newAtth : newAttachments) {
			boolean isNew = true;
			for (ConsultResponseDoc doc : currentDocs) {
				if (doc.getDocType().equals(newAtth.getDocumentType()) && doc.getDocumentNo()==newAtth.getDocumentNo()) {
					currentDocs.remove(doc);
					isNew = false;
					break;
				}
			}
			if (isNew) { //save the new attachment
				consultationManager.saveConsultResponseDoc(getLoggedInInfo(), new ConsultResponseDoc(response.getId(), newAtth.getDocumentNo(), newAtth.getDocumentType(), getLoggedInInfo().getLoggedInProviderNo()));
			}
		}
		
		//update what remains in current docs, they are detached (set delete)
		for (ConsultResponseDoc doc : currentDocs) {
			consultationManager.saveConsultResponseDoc(getLoggedInInfo(), doc);
		}
	}
}
