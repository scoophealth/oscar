/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
package org.oscarehr.integration.mcedt.web;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.integration.mcedt.McedtMessageCreator;
import org.oscarehr.integration.mcedt.ResourceForm;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceStatus;
import ca.ontario.health.edt.TypeListResult;


public class ResourceAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(ResourceAction.class);
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		//functions needed for the upload page
		ActionUtils.removeSuccessfulUploads(request);
		ActionUtils.removeUploadResponseResults(request);
		ActionUtils.removeSubmitResponseResults(request);
		Date startDate= ActionUtils.getOutboxTimestamp();
		Date endDate = new Date();
		if (startDate!=null && endDate!=null) {
			ActionUtils.moveOhipToOutBox(startDate,endDate);
			ActionUtils.moveObecToOutBox(startDate,endDate);			
			ActionUtils.setOutboxTimestamp(endDate);
		}
		ActionUtils.setUploadResourceId(request, new BigInteger("-1"));
		
		
		if(request.getSession().getAttribute("resourceList")!=null){
			request.getSession().removeAttribute("resourceList");
		}		
		if(request.getSession().getAttribute("resourceID")!=null){
			request.getSession().removeAttribute("resourceID");
		}
		if(request.getSession().getAttribute("info")!=null){
			request.getSession().removeAttribute("info");
		}		
		return mapping.findForward("success");
	}
	
	//----------------------------------
	public ActionForward loadDownloadList(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	HttpServletResponse response)  {		

		List<DetailDataCustom> resourceList;
		try{			
			
			/*ResourceForm resourceForm= (ResourceForm)form;
			resourceForm.setResourceType("");
			resourceForm.setPageNo(1);*/
			resetPage(form);
			resourceList = loadList(form,request,response,ResourceStatus.DOWNLOADABLE);			
			
			if(resourceList.size()>0){																					
				request.getSession().setAttribute("resourceListDL",resourceList);
			}			
			
		}
		catch(Exception e) {
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("success");
		}				
		 
		return mapping.findForward("successUserDownload");		
	}	
	
	private void resetPage(ActionForm form){
		ResourceForm resourceForm = (ResourceForm)form;
		resourceForm.setResourceType("");
		resourceForm.setStatus("");
		resourceForm.setPageNo(1);		
	}
	
	public ActionForward loadSentList(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)  {
		ResourceForm resourceForm = (ResourceForm) form;
		//resourceForm.setStatus("");
		resetPage(form);
		List<DetailDataCustom> resourceList;		
		List<DetailDataCustom> resourceListFiltered = new ArrayList<DetailDataCustom>();
		
		try{
			if(request.getSession().getAttribute("resourceTypeList")==null){
				EDTDelegate delegate = DelegateFactory.newDelegate();
				resourceForm.setTypeListResult(getTypeList(request, delegate));
				//request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
				//List<TypeListData> typeListData = getTypeList(request, delegate).getData();
				//request.getSession().setAttribute("typeListData",typeListData);
			}
			//resourceForm.setResourceType("");
			//resourceForm.setPageNo(1);
			resourceForm.setStatus("UPLOADED");			
			resourceList = loadList(form,request,response,ResourceStatus.UPLOADED);
			/*if(resourceList.size()>0){
				for(DetailDataCustom detailDataK:resourceList){
					//if(ActionUtils.filterResourceStatus(detailDataK)){
						resourceListFiltered.add(detailDataK);
					//}
				}				

				request.getSession().setAttribute("resourceListSent",resourceListFiltered);
			}*/
			request.getSession().setAttribute("resourceListSent",resourceList);
			request.getSession().setAttribute("resourceStatus","UPLOADED");
		}
		catch(Exception e){
			return mapping.findForward("successUserSent");
		}
		return mapping.findForward("successUserSent");
	}
			
	public List<DetailDataCustom> loadList( ActionForm form, HttpServletRequest request, 
		HttpServletResponse response, ResourceStatus resourceStatus)  {
			
			ResourceForm resourceForm = (ResourceForm) form;						
			
			try{
				EDTDelegate delegate = DelegateFactory.newDelegate();			
				
				
				if(request.getSession().getAttribute("resourceTypeList")==null){
					resourceForm.setTypeListResult(getTypeList(request, delegate));
					request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
				}
				else{
					resourceForm.setTypeListResult((TypeListResult)request.getSession().getAttribute("resourceTypeList"));
				}			

			return getResourceList(request, resourceForm, delegate, resourceStatus);
		}
		catch(Exception e) {
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return null;
		}					
		
	}		
			
	private List<DetailDataCustom> getResourceList(HttpServletRequest request, ResourceForm form, EDTDelegate delegate, ResourceStatus resourceStatus) {
	    Detail result = ActionUtils.getDetails(request);
	    List<DetailDataCustom> resourceList =new ArrayList<DetailDataCustom>();	    
	    
	    
	    if (result == null) {
		    try {
		    	String resourceType = form.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}	    	
					
				result = delegate.list(resourceType, resourceStatus, form.getPageNoAsBigInt());
				
				BigInteger resultSize = null;				
				if(result!=null)
					resultSize = result.getResultSize();				
				request.getSession().setAttribute("resultSize",resultSize);
				
				if(result!=null && result.getData()!=null){ 
									
					DetailDataCustom detailDataK;
					for(DetailData detailData : result.getData()){						

						detailDataK = new DetailDataCustom();
						detailDataK = ActionUtils.mapDetailData(form, detailDataK, detailData);
						resourceList.add(detailDataK);						
					}							
					
				}							
					
				
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			
			}
	    }	    
	    return resourceList;
    }	
	
	
	public ActionForward changeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return reset(mapping, form, request, response);
	}

	private ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ActionUtils.removeDetails(request);
		return unspecified(mapping, form, request, response);
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
	
	/*public ActionForward delete(ActionMapping mapping, ActionForm form, 
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
	}*/
	
	/*public ActionForward submit(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	}*/

	/*public ActionForward download(ActionMapping mapping, ActionForm form, 
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
	}*/
}
