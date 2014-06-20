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

import static org.oscarehr.integration.mcedt.ActionUtils.getResourceIds;

import java.math.BigInteger;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DownloadData;
import ca.ontario.health.edt.DownloadResult;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.ResponseResult;
import ca.ontario.health.edt.TypeListResult;

public class ResourceAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(ResourceAction.class);
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ResourceForm resourceForm = (ResourceForm) form; 
		
		EDTDelegate delegate = DelegateFactory.newDelegate();
		resourceForm.setTypeListResult(getTypeList(request, delegate));
		resourceForm.setDetail(getResourceList(request, resourceForm, delegate));

		return mapping.findForward("success");
	}
	
	public ActionForward changeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return reset(mapping, form, request, response);
	}

	private ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ActionUtils.removeDetails(request);
		return unspecified(mapping, form, request, response);
    }

	private Detail getResourceList(HttpServletRequest request, ResourceForm form, EDTDelegate delegate) {
	    Detail result = ActionUtils.getDetails(request);
	    if (result == null) {
		    try {
		    	String resourceType = form.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}
				result = delegate.list(resourceType, form.getStatusAsResourceStatus(), form.getPageNoAsBigInt());
				ActionUtils.setDetails(request, result);
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			}
	    }
	    return result;
    }

	private TypeListResult getTypeList(HttpServletRequest request, EDTDelegate delegate) {
		TypeListResult result = ActionUtils.getTypeList(request); 
		if (result == null) {
			try {
				result = delegate.getTypeList();
				ActionUtils.setTypeList(request, result);
			} catch (Exception e) {
				logger.error("Unable to load type list", e);
				
				saveErrors(request, ActionUtils.addMessage("resourceAction.getTypeList.fault", McedtMessageCreator.exceptionToString(e)));
			}
		}
		return result;
    }
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResourceForm resourceForm = (ResourceForm) form;
		List<BigInteger> ids = getResourceIds(request);
		
		ResourceResult result = null;
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			result = delegate.delete(ids);
		} catch (Exception e) {
			logger.error("Unable to delete", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.delete.fault", McedtMessageCreator.exceptionToString(e)));
		}
		reset(mapping, resourceForm, request, response);
		
		ActionMessages messages = new ActionMessages();
		if (result != null) {
			for(ResponseResult r : result.getResponse()) {
				messages.add(ActionUtils.addMessage("resourceAction.delete.success", McedtMessageCreator.responseResultToString(r)));
			}
		}
		saveMessages(request, messages);
		
		return mapping.findForward("success");
	}
	
	public ActionForward submit(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		List<BigInteger> ids = getResourceIds(request);
		
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			ResourceResult result = delegate.submit(ids);
			
			reset(mapping, form, request, response);
			saveMessages(request, ActionUtils.addMessage("resourceAction.submit.success", McedtMessageCreator.resourceResultToString(result)));
		} catch (Exception e) {
			logger.error("Unable to submit", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.submit.failure", McedtMessageCreator.exceptionToString(e)));
		}
		
		return mapping.findForward("success");
	}

	public ActionForward download(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {		
		List<BigInteger> ids = getResourceIds(request);		
		DownloadResult downloadResult = null;
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate();
			downloadResult = delegate.download(ids);
		} catch (Exception e) {
			saveErrors(request, ActionUtils.addMessage("resourceAction.download.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("success");
		}

		response.setContentType("application/zip");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition","attachment; filename=\"mcedt_download_" + System.currentTimeMillis() + ".zip\"");
		
		ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
		for(DownloadData d : downloadResult.getData()) {
			byte[] inputBytes = d.getContent();
			
			String name = d.getResourceID().toString();
			ZipEntry ze = new ZipEntry(name);
			ze.setComment(d.getDescription());
			ze.setSize(inputBytes.length);
			
			zos.putNextEntry(ze);
			zos.write(inputBytes);
			zos.closeEntry();
			zos.flush();
		}
		zos.close();

		return null;
	}
}
