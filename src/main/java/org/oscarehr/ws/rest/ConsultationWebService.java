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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.consultations.ConsultationSearchFilter;
import org.oscarehr.consultations.ConsultationSearchFilter.SORTDIR;
import org.oscarehr.consultations.ConsultationSearchFilter.SORTMODE;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.conversion.ConsultationDetailConverter;
import org.oscarehr.ws.rest.conversion.ConsultationServiceConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.model.ConsultationDetailTo1;
import org.oscarehr.ws.rest.to.model.ConsultationSearchResult;
import org.oscarehr.ws.rest.to.model.FaxConfigTo1;
import org.oscarehr.ws.rest.to.model.LetterheadTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.RxInformation;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

@Path("/consults")
@Component("consultationWebService")
public class ConsultationWebService extends AbstractServiceImpl {

	Pattern namePtrn = Pattern.compile("sorting\\[(\\w+)\\]");
	
	@Autowired
	ConsultationManager consultationManager;
	
	@Autowired
	SecurityInfoManager securityInfoManager;
	
	@Autowired
	CaseManagementManager caseManagementManager;
	
	@Autowired
	ProviderDao providerDao;
	
	@Autowired
	FaxConfigDao faxConfigDao;
	
	@Autowired
	ClinicDAO clinicDAO;
	
	@Autowired
	UserPropertyDAO userPropertyDAO;
	
	private ConsultationDetailConverter consultationDetailConverter = new ConsultationDetailConverter();
	
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AbstractSearchResponse<ConsultationSearchResult> search(JSONObject json) {
		AbstractSearchResponse<ConsultationSearchResult> response = new AbstractSearchResponse<ConsultationSearchResult>();

		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_con", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		//List<ConsultationSearchResult> results = new ArrayList<ConsultationSearchResult>();
		
				
		int count = consultationManager.getConsultationCount(convertJSON(json));

		if(count>0) {
			List<ConsultationSearchResult> items =  consultationManager.search(getLoggedInInfo(), convertJSON(json));
			//convert items to a ConsultationSearchResult object
			response.setContent(items);
			response.setTotal(count);
		}
		
		return response;
	}
	
	@GET
	@Path("/getDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationDetailTo1 getDetail(@QueryParam("requestId")Integer requestId, @QueryParam("demographicId")Integer demographicId) {
		ConsultationDetailTo1 detail = new ConsultationDetailTo1();
		
		if (requestId>0) {
			detail = consultationDetailConverter.getAsTransferObject(getLoggedInInfo(), consultationManager.getRequest(getLoggedInInfo(), requestId));
		} else {
			detail.setDemographicId(demographicId);
			
			RxInformation rx = new RxInformation();
			String info = rx.getAllergies(demographicId.toString());
			if (StringUtils.isNotBlank(info)) detail.setAllergies(info);
			info = rx.getCurrentMedication(demographicId.toString());
			if (StringUtils.isNotBlank(info)) detail.setCurrentMeds(info);
		}

		detail.setLetterheadList(getLetterheadList());
		detail.setFaxList(getFaxList());
		detail.setServiceList((new ConsultationServiceConverter()).getAllAsTransferObjects(getLoggedInInfo(), consultationManager.getConsultationServices()));
		detail.setSendToList(providerDao.getActiveTeams());
		detail.setProviderNo(getLoggedInInfo().getLoggedInProviderNo());
		
		return detail;
	}
	
	@GET
	@Path("/getAttachments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAttachments(@QueryParam("requestId")Integer requestIdInt, @QueryParam("demographicId")String demographicId) {
		List<String> attachmentNames = new ArrayList<String>();
		if (requestIdInt>0) {
			String requestId = requestIdInt.toString();
			List<EDoc> edocList = EDocUtil.listDocs(getLoggedInInfo(), demographicId, requestId, EDocUtil.ATTACHED);
			for (EDoc edoc : edocList) {
				attachmentNames.add(oscar.util.StringUtils.maxLenString(edoc.getDescription(),19,16,"..."));
			}
			
			List<LabResultData> labs = new CommonLabResultData().populateLabResultsData(demographicId, requestId, CommonLabResultData.ATTACHED);
			for (LabResultData lab : labs) {
				attachmentNames.add(lab.getDiscipline()+" "+lab.getDateTime());
			}
		}
		return attachmentNames;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ConsultationDetailTo1 saveConsultDetail(ConsultationDetailTo1 data) {
		ConsultationRequest request = null;
		
		if (data.getId()==null) { //new consultation request
			request = consultationDetailConverter.getAsDomainObject(getLoggedInInfo(), data);
			request.setProfessionalSpecialist(consultationManager.getProfessionalSpecialist(data.getProfessionalSpecialist().getId()));
		} else {
			request = consultationDetailConverter.getAsDomainObject(getLoggedInInfo(), data, consultationManager.getRequest(getLoggedInInfo(), data.getId()));
		}
		consultationManager.saveConsultationRequest(getLoggedInInfo(), request);
		
	    if (data.getId()==null) data.setId(request.getId());
		return data;
	}
	
	
	
	//private methods
	
	private Date convertJSONDate(String val) {
		try {
			return javax.xml.bind.DatatypeConverter.parseDateTime(val).getTime();
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Error parsing date - " + val);
		}
		return null;
	}
	
	private ConsultationSearchFilter convertJSON(JSONObject json) {
		ConsultationSearchFilter filter = new ConsultationSearchFilter();
		
		filter.setAppointmentEndDate(convertJSONDate((String)json.get("appointmentEndDate")));
		filter.setAppointmentStartDate(convertJSONDate((String)json.get("appointmentStartDate")));
		filter.setDemographicNo((Integer)json.get("demographicNo"));
		filter.setNumToReturn((Integer)json.get("numToReturn"));
		filter.setReferralEndDate(convertJSONDate((String)json.get("referralEndDate")));
		filter.setReferralStartDate(convertJSONDate((String)json.get("referralStartDate")));
		filter.setStartIndex((Integer)json.get("startIndex"));
		filter.setStatus((String)json.get("status"));
		filter.setTeam((String)json.get("team"));
		
		JSONObject params = json.getJSONObject("params");
		if(params != null) {
			for(Object key:params.keySet()) {
				Matcher nameMtchr = namePtrn.matcher((String)key);
				if (nameMtchr.find()) {
				   String var = nameMtchr.group(1);
				   filter.setSortMode(SORTMODE.valueOf(var));
				   filter.setSortDir(SORTDIR.valueOf(params.getString((String)key)));
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
}
