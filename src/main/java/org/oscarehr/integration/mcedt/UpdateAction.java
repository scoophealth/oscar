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
package org.oscarehr.integration.mcedt;

import static org.oscarehr.integration.mcedt.ActionUtils.clearUpdateList;
import static org.oscarehr.integration.mcedt.ActionUtils.getResourceIds;
import static org.oscarehr.integration.mcedt.ActionUtils.getUpdateList;
import static org.oscarehr.integration.mcedt.McedtConstants.SESSION_KEY_UPLOAD_DETAILS;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.UpdateRequest;

public class UpdateAction extends DispatchAction {
	
	private static Logger logger = MiscUtils.getLogger();
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		List<BigInteger> resourceIds = getResourceIds(request);
		
		Detail details = (Detail) request.getSession().getAttribute(SESSION_KEY_UPLOAD_DETAILS);
		if (details == null) {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			try {
				details = delegate.info(resourceIds);
			} catch (Exception e) {
				logger.error("Error loading " + resourceIds, e);
				
				saveErrors(request, ActionUtils.addMessage("updateAction.unspecified.errorLoading"));
				return mapping.findForward("initial");
			}
			for(DetailData d : details.getData()) {
				d.setModifyTimestamp(null);
			}
			request.getSession().setAttribute(SESSION_KEY_UPLOAD_DETAILS, details);
		}
		return mapping.findForward("initial");
	}
	
	public ActionForward addUpdateRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) {
		List<UpdateRequest> updates = getUpdateList(request);

		UpdateForm updateForm = (UpdateForm) form;
		UpdateRequest update = updateForm.toUpdateRequest();
		updates.add(update);
		
		Detail details = (Detail) request.getSession().getAttribute(SESSION_KEY_UPLOAD_DETAILS);
		for(DetailData d : details.getData()) {
			if (d.getResourceID().equals(update.getResourceID())) {
				markUpdated(d);
			}
		}
		return mapping.findForward("initial");
	}

	private void markUpdated(DetailData d) {
	    d.setModifyTimestamp(new DummyXMLGregorianCalendar());
    }
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		clearUpdateList(request);
		request.getSession().removeAttribute(SESSION_KEY_UPLOAD_DETAILS);
		return mapping.findForward("cancel");
	}
	
	public ActionForward sendUpdateRequest(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
				
		List<UpdateRequest> updates = getUpdateList(request);
		if (updates.isEmpty()) {
			saveErrors(request.getSession(), ActionUtils.addMessage("updateAction.sendUpdateRequest.emptyUpdates"));
			return mapping.findForward("success");
		}
		
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			ResourceResult result = delegate.update(updates);
			clearUpdateList(request);
			saveMessages(request.getSession(), ActionUtils.addMessage("updateAction.sendUpdateRequest.success", McedtMessageCreator.resourceResultToString(result)));
			
			cancel(mapping, form, request, response);
		} catch (Exception e) {
			logger.error("Unable to update", e);
			
			request.setAttribute("message", "Error updating: " + e.getMessage());
			saveErrors(request, ActionUtils.addMessage("updateAction.sendUpdateRequest.failure", McedtMessageCreator.exceptionToString(e)));
		}
		
		return mapping.findForward("success");
	}

}
