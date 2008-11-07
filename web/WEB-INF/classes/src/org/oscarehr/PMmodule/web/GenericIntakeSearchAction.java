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
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.caisi_integrator.ws.client.Referral;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicScore;
import org.oscarehr.caisi_integrator.ws.client.ReferralWs;
import org.oscarehr.util.SessionConstants;

import com.quatro.service.LookupManager;

public class GenericIntakeSearchAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeSearchAction.class);

    // Forwards
    private static final String FORWARD_SEARCH_FORM = "searchForm";
    private static final String FORWARD_INTAKE_EDIT = "intakeEdit";

    private SurveyManager surveyManager;
    private LookupManager lookupManager;
    private CaisiIntegratorManager caisiIntegratorManager;

    public void setSurveyManager(SurveyManager mgr) {
        this.surveyManager = mgr;
    }

    public void setLookupManager(LookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setCaisiIntegratorManager(CaisiIntegratorManager caisiIntegratorManager) {
        this.caisiIntegratorManager = caisiIntegratorManager;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("genders", lookupManager.LoadCodeList("GEN", true, null, null));
        return mapping.findForward(FORWARD_SEARCH_FORM);
    }

    public ActionForward searchFromRemoteAdmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
        
        try {
        	Integer remoteReferralId=Integer.parseInt(request.getParameter("remoteReferralId"));
        	
			ReferralWs referralWs = caisiIntegratorManager.getReferralWs(currentFacilityId);
			Referral remoteReferral=referralWs.getReferral(remoteReferralId);

			DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacilityId);
			CachedDemographic cachedDemographic=demographicWs.getCachedDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(), remoteReferral.getSourceCaisiDemographicId());
			
	        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
	        intakeSearchBean.setFirstName(cachedDemographic.getFirstName());
	        intakeSearchBean.setGender(cachedDemographic.getGender());
	        intakeSearchBean.setHealthCardNumber(cachedDemographic.getHin());
	        intakeSearchBean.setHealthCardVersion(cachedDemographic.getHinVersion());
	        intakeSearchBean.setLastName(cachedDemographic.getLastName());
	        intakeSearchBean.setYearOfBirth(String.valueOf(cachedDemographic.getBirthDate().getYear()));
	        intakeSearchBean.setMonthOfBirth(String.valueOf(cachedDemographic.getBirthDate().getMonth()));
	        intakeSearchBean.setDayOfBirth(String.valueOf(cachedDemographic.getBirthDate().getDay()));
        }
		catch (MalformedURLException e) {
			LOG.error("Unexpected Error.", e);
		}
		catch (WebServiceException e) {
			LOG.error("Unexpected Error.", e);
		}

        return(search(mapping, form, request, response));
    }
    
    
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        // UCF
        request.getSession().setAttribute("survey_list", surveyManager.getAllForms());

        List<Demographic> localMatches = localSearch(intakeSearchBean);
        intakeSearchBean.setLocalMatches(localMatches);

        intakeSearchBean.setSearchPerformed(true);
        request.setAttribute("genders", lookupManager.LoadCodeList("GEN", true, null, null));

        int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);

        if (caisiIntegratorManager.isIntegratorEnabled(currentFacilityId)) {
            createRemoteList(request, intakeSearchBean, currentFacilityId);
        }

        // if matches found display results, otherwise create local intake
        List<MatchingDemographicScore> remoteMatches=(List<MatchingDemographicScore>) request.getAttribute("remoteMatches");
        if (!localMatches.isEmpty() || (remoteMatches!=null && remoteMatches.size()>0)) {
            return mapping.findForward(FORWARD_SEARCH_FORM);
        }
        else {
            return createLocal(mapping, form, request, response);
        }
    }

	private void createRemoteList(HttpServletRequest request, GenericIntakeSearchFormBean intakeSearchBean, int currentFacilityId) {
		try {
		    DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacilityId);

		    MatchingDemographicParameters parameters = new MatchingDemographicParameters();
		    parameters.setMaxEntriesToReturn(10);
		    parameters.setMinScore(7);

		    String temp = StringUtils.trimToNull(intakeSearchBean.getFirstName());
		    parameters.setFirstName(temp);

		    temp = StringUtils.trimToNull(intakeSearchBean.getLastName());
		    parameters.setLastName(temp);

		    temp = StringUtils.trimToNull(intakeSearchBean.getHealthCardNumber());
		    parameters.setHin(temp);

		    XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		    {
		        temp = StringUtils.trimToNull(intakeSearchBean.getYearOfBirth());
		        if (temp != null) cal.setYear(Integer.parseInt(temp));

		        temp = StringUtils.trimToNull(intakeSearchBean.getMonthOfBirth());
		        if (temp != null) cal.setMonth(Integer.parseInt(temp));

		        temp = StringUtils.trimToNull(intakeSearchBean.getDayOfBirth());
		        if (temp != null) cal.setDay(Integer.parseInt(temp));

		        cal.setTime(0, 0, 0);
		    }
		    parameters.setBirthDate(cal);

		    List<MatchingDemographicScore> integratedMatches = demographicWs.getMatchingDemographics(parameters);
		    if (LOG.isDebugEnabled()) {
		        for (MatchingDemographicScore r : integratedMatches)
		            LOG.debug("*** do itegrated search results : " + r.getCachedDemographic() + " : " + r.getScore());
		    }
		    request.setAttribute("remoteMatches", integratedMatches);

		    List<CachedFacility> allFacilities = caisiIntegratorManager.getRemoteFacilities(currentFacilityId);
		    HashMap<Integer, String> facilitiesNameMap = new HashMap<Integer, String>();
		    for (CachedFacility cachedFacility : allFacilities)
		        facilitiesNameMap.put(cachedFacility.getIntegratorFacilityId(), cachedFacility.getName());

		    request.setAttribute("facilitiesNameMap", facilitiesNameMap);
		}
		catch (WebServiceException e) {
		    LOG.warn("Error connecting to integrator. " + e.getMessage());
		    LOG.debug("Error connecting to integrator.", e);
		}
		catch (Exception e) {
		    LOG.error("Unexpected error.", e);
		}
	}

    public ActionForward createLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        return forwardIntakeEditCreate(mapping, request, createClient(intakeSearchBean, true));
    }

    public ActionForward updateLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        return forwardIntakeEditUpdate(mapping, intakeSearchBean.getDemographicId(), request);
    }

    /**
     * This method is run from at least 2 locations, 
     * 1 is from "new client" and a remote client is found.
     * 2 is from admitting remote referrals. 
     */
    public ActionForward copyRemote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int remoteFacilityId=Integer.parseInt(request.getParameter("remoteFacilityId"));
            int remoteDemographicId=Integer.parseInt(request.getParameter("remoteDemographicId"));

            int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
            DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacilityId);
            CachedDemographic cachedDemographic=demographicWs.getCachedDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);
            
            XMLGregorianCalendar cal=cachedDemographic.getBirthDate();
            Demographic demographic = Demographic.create(cachedDemographic.getFirstName(), cachedDemographic.getLastName(), cal==null?null:make0PrependedDateString(cal.getMonth()), cal==null?null:make0PrependedDateString(cal.getDay()), cal==null?null:String.valueOf(cal.getYear()), cachedDemographic.getHin(), null, true);
            demographic.setCity(cachedDemographic.getCity());
            demographic.setProvince(cachedDemographic.getProvince());
            demographic.setSin(cachedDemographic.getSin());
            
            return forwardIntakeEditCreate(mapping, request, demographic);
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(unspecified(mapping, form, request, response));
        }
    }

    private String make0PrependedDateString(int i)
    {
    	if (i<=9) return("0"+i);
    	else return(""+i);
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

    private Demographic createClient(GenericIntakeSearchFormBean intakeSearchBean, boolean populateDefaultBirthDate) {
        return Demographic.create(intakeSearchBean.getFirstName(), intakeSearchBean.getLastName(), intakeSearchBean.getMonthOfBirth(), intakeSearchBean.getDayOfBirth(), intakeSearchBean.getYearOfBirth(), intakeSearchBean.getHealthCardNumber(),
                intakeSearchBean.getHealthCardVersion(), populateDefaultBirthDate);
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
        
        request.setAttribute("genders", lookupManager.LoadCodeList("GEN", true, null, null));

        return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
    }

	private void addDestinationProgramId(HttpServletRequest request, StringBuilder parameters) {
		String remoteReferralId=StringUtils.trimToNull(request.getParameter("remoteReferralId"));
        if (remoteReferralId!=null)
        {
			try {
	        	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);

	        	ReferralWs referralWs = caisiIntegratorManager.getReferralWs(currentFacilityId);
				Referral remoteReferral=referralWs.getReferral(Integer.parseInt(remoteReferralId));
				parameters.append("&destinationProgramId=");
				parameters.append(remoteReferral.getDestinationCaisiProgramId());
			}
			catch (MalformedURLException e) {
				LOG.error("Unexpected error.", e);
			}
			catch (WebServiceException e) {
				LOG.error("Unexpected error.", e);
			}
        }
	}

	private void copyParameter(HttpServletRequest request, String parameterName, StringBuilder url) {
		String temp=StringUtils.trimToNull(request.getParameter(parameterName));
		if (temp!=null)
        {
        	if (url.indexOf("?")<0) url.append("?");
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

}
