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

import static org.oscarehr.integration.mcedt.web.ActionUtils.getDefaultServiceId;
import static org.oscarehr.integration.mcedt.web.ActionUtils.getResourceIds;
import static org.oscarehr.integration.mcedt.web.ActionUtils.getServiceId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.integration.mcedt.McedtMessageCreator;
import org.oscarehr.integration.mcedt.ResourceForm;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.ResponseResult;
import ca.ontario.health.edt.TypeListResult;

public class InfoAction extends DispatchAction {
	private static Logger logger = Logger.getLogger(ResourceAction.class);
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<BigInteger> resourceIds = getResourceIds(request);
		String serviceId= getServiceId(request);
		if (serviceId==null || serviceId.trim().equals("")) serviceId= getDefaultServiceId();
		
		try{
		
			//temorary code as mcedt is not working
			/*DetailData detailData = new DetailData();
			detailData.setResourceID(resourceIds.get(0));
			detailData.setDescription("description");
			detailData.setResourceType("resourceType");						
			request.setAttribute("detaildata", detailData);
			
			Detail detail = new Detail();
			detail.getData().add(detailData);
			request.setAttribute("detail", detail);
			
			request.getSession().setAttribute("info", "true");*/
			//----------
			
		EDTDelegate delegate = DelegateFactory.newDelegate(serviceId);
		Detail detail = delegate.info(resourceIds);
		request.setAttribute("detail", detail);				
		request.getSession().setAttribute("info", "true");
		
		return mapping.findForward("success");
		}catch(Exception e){
			logger.error("Unable to load resource info ", e);
			saveErrors(request, ActionUtils.addMessage("updateAction.unspecified.errorLoading", McedtMessageCreator.exceptionToString(e)));
			request.getSession().setAttribute("info", "false");
			return mapping.findForward("success");
		}
	}
	
	/*public ActionForward reSubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<BigInteger> resourceIds = getResourceIds(request);

		try{				
			ResourceResult result=null;
			EDTDelegate delegate = DelegateFactory.newDelegate();
			if (resourceIds.size()>0) result =delegate.submit(resourceIds);
			for (ResponseResult edtResponse: result.getResponse()) {
				if (edtResponse.getResult().getCode().equals("IEDTS0001")) {
					saveMessages(request, ActionUtils.addMessage("uploadAction.submit.success", McedtMessageCreator.resourceResultToString(result)));
				} else {
					saveErrors(request, ActionUtils.addMessage("uploadAction.submit.failure", edtResponse.getDescription()+": "+edtResponse.getResult().getMsg()));			
				}
			}				

			return mapping.findForward("success");
		}catch(Exception e){
			logger.error("Unable to submit resource ", e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.submit.failure", McedtMessageCreator.exceptionToString(e)));			
			return mapping.findForward("success");
		}
		
	}*/		
	
	public ActionForward deleteFiles(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  {		
		List<BigInteger> ids = getResourceIds(request);
		String serviceId= getServiceId(request);
		if (serviceId==null || serviceId.trim().equals("")) serviceId= getDefaultServiceId();
		
		ResourceResult result = null;
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate(serviceId);
			result = delegate.delete(ids);
		} catch (Exception e) {
			logger.error("Unable to delete", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.delete.fault", McedtMessageCreator.exceptionToString(e)));
		}		
		
		
		ActionMessages messages = new ActionMessages();
		if (result != null) {
			for(ResponseResult r : result.getResponse()) {
				messages.add(ActionUtils.addMessage("resourceAction.delete.success", McedtMessageCreator.responseResultToString(r)));
			}
		}
		
		saveMessages(request, messages);
		
		//get the updated list from mcedt and save to session
		List<DetailDataCustom> resourceList = getResourceList(request,form);		
		
		request.getSession().setAttribute("resourceListSent",resourceList );
		
		return mapping.findForward("success");
	}	
	
	
	private List<DetailDataCustom> getResourceList(HttpServletRequest request, ActionForm form) {
	    Detail result = ActionUtils.getDetails(request);
	    List<DetailDataCustom> resourceList =new ArrayList<DetailDataCustom>();
	    ResourceForm resourceForm = (ResourceForm)form;	    	        
	    	    
	    if (result == null) {
		    try {
		    	String resourceType = resourceForm.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}
	    			    	
		    	BigInteger resultSize = null;
		    	
		    	EDTDelegate delegate = DelegateFactory.newDelegate(resourceForm.getServiceIdSent());		    	
		    	result = delegate.list(resourceType, resourceForm.getStatusAsResourceStatus(), resourceForm.getPageNoAsBigInt());								
		    	
		    	if(result!=null)
		    		resultSize = result.getResultSize();
		    	
				request.getSession().setAttribute("resultSize",resultSize);
				
		    	if(request.getSession().getAttribute("resourceTypeList")==null){
					resourceForm.setTypeListResult(ActionUtils.getTypeList(request, delegate));
					request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
				}
				else{
					resourceForm.setTypeListResult((TypeListResult)request.getSession().getAttribute("resourceTypeList"));
				}
		    	
				if(result != null && result.getData()!=null  && result.getResultSize() != null){ 
									
					DetailDataCustom detailDataK;
					for(DetailData detailData : result.getData()){
						
						//add to list if only of certain status
						//if(ActionUtils.filterResourceStatus(detailData)){	
							detailDataK = new DetailDataCustom();																					
							detailDataK = ActionUtils.mapDetailData((ResourceForm)form, detailDataK, detailData);							
							resourceList.add(detailDataK);
						//}						
					}												

					if(resourceList.size()>0){						
						//Collections.sort(resourceList, DetailDataCustom.ResourceIdComparator);										
						request.getSession().setAttribute("resourceListDL",resourceList);
					}
				} else if (result.getResultSize() == null)	
					// if a result is returned with no size, meaning you are accessing a list that is not permitted, one response will be returned holding the error message
					saveErrors(request,ActionUtils.addMessage("resourceAction.getResourceList.fault", result.getData().get(0).getResult().getMsg()));							
				
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			
			}
	    }	    
	    return resourceList;
    }		
	
	public ActionForward changeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)  {
		
		ResourceForm resourceForm = (ResourceForm)form;
		String prvStatus = (String)request.getSession().getAttribute("resourceStatus");
		String currStatus = resourceForm.getStatus();		
		if(prvStatus.equalsIgnoreCase(currStatus)){
			resourceForm.setPageNo(1);
			request.getSession().setAttribute("resourceStatus",currStatus );
		}
		
		List<DetailDataCustom> resourceList = getResourceList(request,form);		
		
		request.getSession().setAttribute("resourceListSent",resourceList );

		return mapping.findForward("success");
	}
}
