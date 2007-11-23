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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.caisi.integrator.model.Client;
import org.caisi.integrator.model.transfer.ClientTransfer;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericIntakeSearchAction extends BaseGenericIntakeAction {

    private static Log LOG = LogFactory.getLog(GenericIntakeSearchAction.class);

    // Forwards
    private static final String FORWARD_SEARCH_FORM = "searchForm";
    private static final String FORWARD_INTAKE_EDIT = "intakeEdit";

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(FORWARD_SEARCH_FORM);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
        intakeSearchBean.setLocalAgencyUsername(IntegratorManager.getLocalAgency().getIntegratorUsername());
        
        Collection<ClientTransfer> remoteMatches = new ArrayList<ClientTransfer>();

        // search for remote matches if integrator enabled
        if (integratorManager.isEnabled()) {
            remoteMatches = remoteSearch(intakeSearchBean);
            intakeSearchBean.setRemoteMatches(remoteMatches.toArray(new ClientTransfer[remoteMatches.size()]));
        }


        boolean allowOnlyOptins=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
        List<Demographic> localMatches = localSearch(intakeSearchBean, allowOnlyOptins);
        intakeSearchBean.setLocalMatches(localMatches);

        intakeSearchBean.setSearchPerformed(true);
        
        // if matches found display results, otherwise create local intake
        if (!localMatches.isEmpty() || !remoteMatches.isEmpty()) {
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

    public ActionForward copyRemote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;

        try {
            return forwardIntakeEditCreateBasedOnRemote(mapping, request, intakeSearchBean.getRemoteAgency(), intakeSearchBean.getRemoteAgencyDemographicNo());
        } catch (IntegratorException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("integrator.error", e.getMessage()));
            saveErrors(request, messages);

            return mapping.findForward(FORWARD_SEARCH_FORM);
        }
    }

    private Collection<ClientTransfer> remoteSearch(GenericIntakeSearchFormBean intakeSearchBean) {
        try {
            return integratorManager.matchClient(createClient(intakeSearchBean, false));
        } catch (IntegratorException e) {
            LOG.error(e);

            return new ArrayList<ClientTransfer>();
        }
    }

    private List<Demographic> localSearch(GenericIntakeSearchFormBean intakeSearchBean, boolean allowOnlyOptins) {
        ClientSearchFormBean clientSearchBean = new ClientSearchFormBean();
        clientSearchBean.setFirstName(intakeSearchBean.getFirstName());
        clientSearchBean.setLastName(intakeSearchBean.getLastName());
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

    protected ActionForward forwardIntakeEditCreateBasedOnRemote(ActionMapping mapping, HttpServletRequest request, String remoteAgency, Long remoteAgencyDemographicNo) {
        StringBuilder parameters = new StringBuilder(PARAM_START);
        parameters.append(METHOD).append(PARAM_EQUALS).append(EDIT_CREATE_REMOTE).append(PARAM_AND);
        parameters.append(TYPE).append(PARAM_EQUALS).append(Intake.QUICK);
        parameters.append(PARAM_AND).append(REMOTE_AGENCY).append(PARAM_EQUALS).append(remoteAgency);
        parameters.append(PARAM_AND).append(REMOTE_AGENCY_DEMOGRAPHIC_NO).append(PARAM_EQUALS).append(remoteAgencyDemographicNo);

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
