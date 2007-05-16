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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeSearchFormBean;

public class GenericIntakeSearchAction extends BaseAction {
	
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
		
		// search for remote matches
		Demographic[] remoteMatches = remoteSearch(intakeSearchBean);
		intakeSearchBean.setRemoteMatches(remoteMatches);
		
		// if no remote matches, search for local matches
		if (!intakeSearchBean.isRemoteMatch()) {
			List<?> localMatches = localSearch(intakeSearchBean);
			intakeSearchBean.setLocalMatches(localMatches);
		}
		
		// if matches found display results, otherwise create local intake
		if (intakeSearchBean.isRemoteMatch() || intakeSearchBean.isLocalMatch()) {
			return mapping.findForward(FORWARD_SEARCH_FORM);
		} else {
			return createLocal(mapping, form, request, response);
		}
	}
	
	public ActionForward createLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
		
		return forwardIntakeEditCreate(mapping, request, createClient(intakeSearchBean));
	}
	
	public ActionForward updateLocal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
		
		return forwardIntakeEditUpdate(mapping, intakeSearchBean.getClientId());
	}
	
	public ActionForward copyRemote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	GenericIntakeSearchFormBean intakeSearchBean = (GenericIntakeSearchFormBean) form;
    	
    	try {
            Demographic remoteClient = integratorManager.getDemographic(intakeSearchBean.getAgencyId(), intakeSearchBean.getClientId());
            
            return forwardIntakeEditCreate(mapping, request, remoteClient);
        } catch (IntegratorException e) {
    		ActionMessages messages = new ActionMessages();
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("integrator.error", e.getMessage()));
    		saveErrors(request, messages);
    		
    		return mapping.findForward(FORWARD_SEARCH_FORM);
        }
    }
	
	private Demographic[] remoteSearch(GenericIntakeSearchFormBean intakeSearchBean) {
		Demographic[] matches = null;
		
		try {
			matches = integratorManager.matchClient(createClient(intakeSearchBean));
		} catch (IntegratorException e) {
			LOG.error(e);
		}
		
		return matches;
	}

	private List<?> localSearch(GenericIntakeSearchFormBean intakeSearchBean) {
		ClientSearchFormBean clientSearchBean = new ClientSearchFormBean();
		clientSearchBean.setFirstName(intakeSearchBean.getFirstName());
		clientSearchBean.setLastName(intakeSearchBean.getLastName());
		clientSearchBean.setSearchOutsideDomain(true);
		clientSearchBean.setSearchUsingSoundex(true);

		return clientManager.search(clientSearchBean);
	}
		
	private Demographic createClient(GenericIntakeSearchFormBean intakeSearchBean) {
		return Demographic.create(intakeSearchBean.getFirstName(), intakeSearchBean.getLastName(), intakeSearchBean.getMonthOfBirth(), intakeSearchBean.getDayOfBirth(), intakeSearchBean.getYearOfBirth(), intakeSearchBean.getHealthCardNumber(), intakeSearchBean.getHealthCardVersion());
	}
	
	protected ActionForward forwardIntakeEditCreate(ActionMapping mapping, HttpServletRequest request, Demographic client) {
		request.getSession().setAttribute(GenericIntakeEditAction.CLIENT, client);

    	StringBuilder parameters = new StringBuilder(PARAM_START);
    	parameters.append(GenericIntakeEditAction.METHOD).append(PARAM_EQUALS).append(GenericIntakeEditAction.CREATE).append(PARAM_AND);
    	parameters.append(GenericIntakeEditAction.TYPE).append(PARAM_EQUALS).append(Intake.QUICK);
    	
    	return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
    }

	protected ActionForward forwardIntakeEditUpdate(ActionMapping mapping, Integer clientId) {
		StringBuilder parameters = new StringBuilder(PARAM_START);
		parameters.append(GenericIntakeEditAction.METHOD).append(PARAM_EQUALS).append(GenericIntakeEditAction.UPDATE).append(PARAM_AND);
		parameters.append(GenericIntakeEditAction.TYPE).append(PARAM_EQUALS).append(Intake.QUICK).append(PARAM_AND);
		parameters.append(GenericIntakeEditAction.CLIENT_ID).append(PARAM_EQUALS).append(clientId);
		
		return createRedirectForward(mapping, FORWARD_INTAKE_EDIT, parameters);
	}
	
}
