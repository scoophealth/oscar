/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;

import com.quatro.model.LookupCodeValue;

public class GenericIntakeSearchAction extends BaseGenericIntakeAction {

	private static Log LOG = LogFactory.getLog(GenericIntakeSearchAction.class);

	private static final List<LookupCodeValue> genders = new ArrayList<LookupCodeValue>();

	static {
		LookupCodeValue lv1 = new LookupCodeValue();
		lv1.setCode("M");
		lv1.setDescription("Male");
		genders.add(lv1);
		LookupCodeValue lv2 = new LookupCodeValue();
		lv2.setCode("F");
		lv2.setDescription("Female");
		genders.add(lv2);
		LookupCodeValue lv3 = new LookupCodeValue();
		lv3.setCode("T");
		lv3.setDescription("Transgender");
		genders.add(lv3);
		LookupCodeValue lv4 = new LookupCodeValue();
		lv4.setCode("O");
		lv4.setDescription("Other");
		genders.add(lv4);
	}

	// Forwards
	private static final String FORWARD_SEARCH_FORM = "searchForm";
	private static final String FORWARD_INTAKE_EDIT = "intakeEdit";

	private ClientImageDAO clientImageDAO = null;
	
	public void setClientImageDAO(ClientImageDAO clientImageDAO) {
		this.clientImageDAO = clientImageDAO;
	}
	
	private SurveyManager surveyManager;

	public void setSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("genders", getGenders());
		return mapping.findForward(FORWARD_SEARCH_FORM);
	}

	public ActionForward searchFromRemoteAdmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer remoteReferralId = Integer.parseInt(request.getParameter("remoteReferralId"));

			ReferralWs referralWs = CaisiIntegratorManager.getReferralWs();
			Referral remoteReferral = referralWs.getReferral(remoteReferralId);

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(), remoteReferral.getSourceCaisiDemographicId());

			GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
			intakeSearchBean.setFirstName(demographicTransfer.getFirstName());
			intakeSearchBean.setGender(demographicTransfer.getGender().name());
			intakeSearchBean.setHealthCardNumber(demographicTransfer.getHin());
			intakeSearchBean.setHealthCardVersion(demographicTransfer.getHinVersion());
			intakeSearchBean.setLastName(demographicTransfer.getLastName());

			if (demographicTransfer.getBirthDate() != null) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(demographicTransfer.getBirthDate());
				intakeSearchBean.setYearOfBirth(String.valueOf(cal.get(Calendar.YEAR)));
				intakeSearchBean.setMonthOfBirth(String.valueOf(cal.get(Calendar.MONTH)));
				intakeSearchBean.setDayOfBirth(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			}
		} catch (MalformedURLException e) {
			LOG.error("Unexpected Error.", e);
		} catch (WebServiceException e) {
			LOG.error("Unexpected Error.", e);
		}

		return (search(mapping, form, request, response));
	}

	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

		// UCF
		request.getSession().setAttribute("survey_list", surveyManager.getAllFormsForCurrentProviderAndCurrentFacility());

		List<Demographic> localMatches = localSearch(intakeSearchBean);
		intakeSearchBean.setLocalMatches(localMatches);

		intakeSearchBean.setSearchPerformed(true);
		request.setAttribute("genders", getGenders());

		if (LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled()) {
			createRemoteList(request, intakeSearchBean);
		}

		// if matches found display results, otherwise create local intake
		@SuppressWarnings("unchecked")
		List<MatchingDemographicTransferScore> remoteMatches = (List<MatchingDemographicTransferScore>) request.getAttribute("remoteMatches");
		
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
	    if(roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
	    	return mapping.findForward(FORWARD_SEARCH_FORM);
	    }
	    
		if (!localMatches.isEmpty() || (remoteMatches != null && remoteMatches.size() > 0)) {
			return mapping.findForward(FORWARD_SEARCH_FORM);
		} else {
			return createLocal(mapping, form, request, response);
		}
	}

	private void createRemoteList(HttpServletRequest request, GenericIntakeSearchFormBean intakeSearchBean) {
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();

			MatchingDemographicParameters parameters = new MatchingDemographicParameters();
			parameters.setMaxEntriesToReturn(10);
			parameters.setMinScore(7);

			String temp = StringUtils.trimToNull(intakeSearchBean.getFirstName());
			parameters.setFirstName(temp);

			temp = StringUtils.trimToNull(intakeSearchBean.getLastName());
			parameters.setLastName(temp);

			temp = StringUtils.trimToNull(intakeSearchBean.getHealthCardNumber());
			parameters.setHin(temp);

			GregorianCalendar cal = new GregorianCalendar();
			{
				MiscUtils.setToBeginningOfDay(cal);

				temp = StringUtils.trimToNull(intakeSearchBean.getYearOfBirth());
				if (temp != null) cal.set(Calendar.YEAR, Integer.parseInt(temp));

				temp = StringUtils.trimToNull(intakeSearchBean.getMonthOfBirth());
				if (temp != null) cal.set(Calendar.MONTH, Integer.parseInt(temp));

				temp = StringUtils.trimToNull(intakeSearchBean.getDayOfBirth());
				if (temp != null) cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp));

				parameters.setBirthDate(cal.getTime());
			}

			List<MatchingDemographicTransferScore> integratedMatches = demographicWs.getMatchingDemographics(parameters);
			request.setAttribute("remoteMatches", integratedMatches);

			List<CachedFacility> allFacilities = CaisiIntegratorManager.getRemoteFacilities();
			HashMap<Integer, String> facilitiesNameMap = new HashMap<Integer, String>();
			for (CachedFacility cachedFacility : allFacilities)
				facilitiesNameMap.put(cachedFacility.getIntegratorFacilityId(), cachedFacility.getName());

			request.setAttribute("facilitiesNameMap", facilitiesNameMap);
		} catch (WebServiceException e) {
			LOG.warn("Error connecting to integrator. " + e.getMessage());
			LOG.debug("Error connecting to integrator.", e);
		} catch (Exception e) {
			LOG.error("Unexpected error.", e);
		}
	}

	public ActionForward createLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

		return forwardIntakeEditCreate(mapping, request, Demographic.create(intakeSearchBean.getFirstName(), intakeSearchBean.getLastName(), intakeSearchBean.getGender(), intakeSearchBean.getMonthOfBirth(), intakeSearchBean.getDayOfBirth(), intakeSearchBean.getYearOfBirth(), intakeSearchBean
        .getHealthCardNumber(), intakeSearchBean.getHealthCardVersion()));
	}

	public ActionForward updateLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

		//TODO: Erclerk - go to their consent.
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
	    if(roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
	    	request.setAttribute("demographicNo", new Long(intakeSearchBean.getDemographicId()));
	    	return mapping.findForward("clientEdit");
	    }
	    
		
		return forwardIntakeEditUpdate(mapping, intakeSearchBean.getDemographicId(), request);
	}

	/**
	 * This method is run from at least 2 locations, 1 is from "new client" and a remote client is found. 2 is from admitting remote referrals.
	 */
	public ActionForward copyRemote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int remoteFacilityId = Integer.parseInt(request.getParameter("remoteFacilityId"));
			int remoteDemographicId = Integer.parseInt(request.getParameter("remoteDemographicId"));

			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);

			Demographic demographic = new Demographic();
			demographic.setFirstName(demographicTransfer.getFirstName());
			demographic.setLastName(demographicTransfer.getLastName());
			
			if (demographicTransfer.getBirthDate()!=null)
			{
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(demographicTransfer.getBirthDate());
				demographic.setBirthDay(cal);
			}
			
			if (demographicTransfer.getGender()!=null) demographic.setSex(demographicTransfer.getGender().name());
	
			demographic.setPatientStatus("AC");
			demographic.setDateJoined(new Date());
			
			//TODO: if this is ER clerk, go to their consent form.
			//client.setProviderNo(providerNo);
			//clientManager.saveClient(client);
			String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
		    if(roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
		    	clientManager.saveClient(demographic);
		    	request.setAttribute("demographicNo", new Long(demographic.getDemographicNo()));
		    	String providerNo = ((Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER)).getProviderNo();		    	
		    	this.erClerklinkRemoteDemographic(remoteFacilityId, remoteDemographicId, providerNo, demographic);
		    	return mapping.findForward("clientEdit");
		    }
		    
			return forwardIntakeEditCreate(mapping, request, demographic);
		} catch (Exception e) {
			log.error("Unexpected error.", e);
			return (unspecified(mapping, form, request, response));
		}
	}

	private List<Demographic> localSearch(GenericIntakeSearchFormBean intakeSearchBean) {
		ClientSearchFormBean clientSearchBean = new ClientSearchFormBean();
		clientSearchBean.setFirstName(intakeSearchBean.getFirstName());
		clientSearchBean.setLastName(intakeSearchBean.getLastName());
		clientSearchBean.setGender(intakeSearchBean.getGender());
		clientSearchBean.setSearchOutsideDomain(true);
		clientSearchBean.setSearchUsingSoundex(true);

		return clientManager.search(clientSearchBean);
	}

	protected ActionForward forwardIntakeEditCreate(ActionMapping mapping, HttpServletRequest request, Demographic client) {
		request.getSession().setAttribute(CLIENT, client);

		StringBuilder parameters = new StringBuilder(PARAM_START);
		parameters.append(METHOD).append(PARAM_EQUALS).append(EDIT_CREATE).append(PARAM_AND);
		parameters.append(TYPE).append(PARAM_EQUALS).append(Intake.QUICK);

		copyParameter(request, "remoteReferralId", parameters);
		copyParameter(request, "remoteFacilityId", parameters);
		copyParameter(request, "remoteDemographicId", parameters);

		addDestinationProgramId(request, parameters);

		request.setAttribute("genders", getGenders());

		return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
	}

	private void addDestinationProgramId(HttpServletRequest request, StringBuilder parameters) {
		String remoteReferralId = StringUtils.trimToNull(request.getParameter("remoteReferralId"));
		if (remoteReferralId != null) {
			try {
				ReferralWs referralWs = CaisiIntegratorManager.getReferralWs();
				Referral remoteReferral = referralWs.getReferral(Integer.parseInt(remoteReferralId));
				parameters.append("&destinationProgramId=");
				parameters.append(remoteReferral.getDestinationCaisiProgramId());
			} catch (MalformedURLException e) {
				LOG.error("Unexpected error.", e);
			} catch (WebServiceException e) {
				LOG.error("Unexpected error.", e);
			}
		}
	}

	private void copyParameter(HttpServletRequest request, String parameterName, StringBuilder url) {
		String temp = StringUtils.trimToNull(request.getParameter(parameterName));
		if (temp != null) {
			if (url.indexOf("?") < 0) url.append("?");
			else url.append("&");
			url.append(parameterName);
			url.append("=");
			url.append(temp);
		}
	}

	protected ActionForward forwardIntakeEditUpdate(ActionMapping mapping, Integer clientId, HttpServletRequest request) {
		StringBuilder parameters = new StringBuilder(PARAM_START);
		parameters.append(METHOD).append(PARAM_EQUALS).append(EDIT_UPDATE).append(PARAM_AND);
		parameters.append(TYPE).append(PARAM_EQUALS).append(Intake.QUICK).append(PARAM_AND);
		parameters.append(CLIENT_ID).append(PARAM_EQUALS).append(clientId);

		copyParameter(request, "remoteReferralId", parameters);
		addDestinationProgramId(request, parameters);

		return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
	}

	public static List<LookupCodeValue> getGenders() {
		return genders;
	}
	
	private void erClerklinkRemoteDemographic(int remoteFacilityId, int remoteDemographicId, String providerNo, Demographic client) {
		
		try {			
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();

			// link the clients
			demographicWs.linkDemographics(providerNo, client.getDemographicNo(), remoteFacilityId, remoteDemographicId);

			// copy image if exists
			{
				DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);

				if (demographicTransfer.getPhoto() != null) {
					ClientImage clientImage = new ClientImage();
					clientImage.setDemographic_no(client.getDemographicNo());
					clientImage.setImage_data(demographicTransfer.getPhoto());
					clientImage.setImage_type("jpg");
					clientImageDAO.saveClientImage(clientImage);
				}
			}
		}
		catch (MalformedURLException e) {
			LOG.error(e);
		}
		catch (WebServiceException e) {
			LOG.error(e);
		}
	}

}
