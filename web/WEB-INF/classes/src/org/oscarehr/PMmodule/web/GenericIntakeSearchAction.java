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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;

import com.quatro.service.LookupManager;

public class GenericIntakeSearchAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeSearchAction.class);

    // Forwards
    private static final String FORWARD_SEARCH_FORM = "searchForm";
    private static final String FORWARD_INTAKE_EDIT = "intakeEdit";

    protected SurveyManager surveyManager;
    protected LookupManager lookupManager;

    public void setSurveyManager(SurveyManager mgr) {
        this.surveyManager = mgr;
    }
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}


    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
        return mapping.findForward(FORWARD_SEARCH_FORM);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
        
        //UCF
		request.getSession().setAttribute("survey_list", surveyManager.getAllForms());
		
        boolean allowOnlyOptins=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
        List<Demographic> localMatches = localSearch(intakeSearchBean, allowOnlyOptins);
        intakeSearchBean.setLocalMatches(localMatches);

        intakeSearchBean.setSearchPerformed(true);
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
        
        // if matches found display results, otherwise create local intake
        if (!localMatches.isEmpty()) {
            return mapping.findForward(FORWARD_SEARCH_FORM);
        } else {
            return createLocal(mapping, form, request, response);
        }
    }

    public ActionForward createLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        return forwardIntakeEditCreate(mapping, request, createClient(intakeSearchBean, true));
    }

    public ActionForward updateLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        return forwardIntakeEditUpdate(mapping, intakeSearchBean.getDemographicId());
    }

    private List<Demographic> localSearch(GenericIntakeSearchFormBean intakeSearchBean, boolean allowOnlyOptins) {
        ClientSearchFormBean clientSearchBean = new ClientSearchFormBean();
        clientSearchBean.setFirstName(intakeSearchBean.getFirstName());
        clientSearchBean.setLastName(intakeSearchBean.getLastName());
        clientSearchBean.setGender(intakeSearchBean.getGender());
        clientSearchBean.setSearchOutsideDomain(true);
        clientSearchBean.setSearchUsingSoundex(true);

        return clientManager.search(clientSearchBean, allowOnlyOptins);
    }

    private Demographic createClient(GenericIntakeSearchFormBean intakeSearchBean, boolean populateDefaultBirthDate) {
        return Demographic.create(intakeSearchBean.getFirstName(), intakeSearchBean.getLastName(), intakeSearchBean.getMonthOfBirth(), intakeSearchBean.getDayOfBirth(), intakeSearchBean.getYearOfBirth(), intakeSearchBean.getHealthCardNumber(), intakeSearchBean.getHealthCardVersion(), populateDefaultBirthDate);
    }

    protected ActionForward forwardIntakeEditCreate(ActionMapping mapping, HttpServletRequest request, Demographic client) {
        request.getSession().setAttribute(CLIENT, client);

        StringBuilder parameters = new StringBuilder(PARAM_START);
        parameters.append(METHOD).append(PARAM_EQUALS).append(EDIT_CREATE).append(PARAM_AND);
        parameters.append(TYPE).append(PARAM_EQUALS).append(Intake.QUICK);

        return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
    }

    protected ActionForward forwardIntakeEditUpdate(ActionMapping mapping, Integer clientId) {
        StringBuilder parameters = new StringBuilder(PARAM_START);
        parameters.append(METHOD).append(PARAM_EQUALS).append(EDIT_UPDATE).append(PARAM_AND);
        parameters.append(TYPE).append(PARAM_EQUALS).append(Intake.QUICK).append(PARAM_AND);
        parameters.append(CLIENT_ID).append(PARAM_EQUALS).append(clientId);

        return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
    }

}
