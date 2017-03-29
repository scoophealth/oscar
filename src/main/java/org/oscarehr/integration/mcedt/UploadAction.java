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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.util.ConversionUtils;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.UploadData;

public class UploadAction extends DispatchAction {
	
	private static Logger logger = MiscUtils.getLogger();

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return mapping.findForward("success");
	}
	
	public ActionForward cancelUpload(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		clearSession(request);
		
		saveMessages(request.getSession(), ActionUtils.addMessage("global.cancel"));
		return mapping.findForward("cancel");
	}

	private void clearSession(HttpServletRequest request) {
	    for(String attrName : new String[] {McedtConstants.SESSION_KEY_RESOURCE_LIST, 
				McedtConstants.SESSION_KEY_MCEDT_UPLOADS}) {
			request.getSession().removeAttribute(attrName);
		}
    }

	public ActionForward addNew(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("addNew");
	}
	
	public ActionForward removeSelected(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		List<UploadData> uploads = ActionUtils.getUploadList(request.getSession());
		
		List<Integer> indices = getIndices(request);
		java.util.Collections.sort(indices, Collections.reverseOrder());
		for(Integer i : indices) {
			if (i < uploads.size()) {
				uploads.remove(i.intValue());
			}
		}
		return mapping.findForward("success");
	}
	
	private List<Integer> getIndices(HttpServletRequest request) {
	    List<Integer> result = new ArrayList<Integer>();
	    for(String resource : request.getParameterValues("resourceId")) {
	    	Integer i = ConversionUtils.fromIntString(resource);
	    	if (i != null) {
	    		result.add(i);
	    	}
	    }
	    return result;
    }

	public ActionForward uploadToMcedt(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		List<UploadData> uploads = ActionUtils.getUploadList(request.getSession());
		
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			ResourceResult result = delegate.upload(uploads);
			
			clearSession(request);
						
			saveMessages(request.getSession(), ActionUtils.addMessage("resourceAction.submit.success", McedtMessageCreator.resourceResultToString(result)));
			return mapping.findForward("done");
		} catch (Exception e) {
			logger.error("Unable to upload to MCEDT", e);
			
			saveErrors(request, ActionUtils.addMessage("resourceAction.submit.failure", McedtMessageCreator.exceptionToString(e)));
		}
		
		return mapping.findForward("success");
	}
	
}
