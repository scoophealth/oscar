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

import static org.oscarehr.integration.mcedt.McedtConstants.REQUEST_ATTR_KEY_RESOURCE_ID;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.integration.mcedt.McedtMessageCreator;
import org.oscarehr.integration.mcedt.ResourceForm;

import oscar.OscarProperties;
import oscar.util.ConversionUtils;
import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.DownloadData;
import ca.ontario.health.edt.DownloadResult;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceStatus;
import ca.ontario.health.edt.TypeListData;
import ca.ontario.health.edt.TypeListResult;

public class DownloadAction extends DispatchAction{
	private static Logger logger = Logger.getLogger(DownloadAction.class);
	private boolean isFileToDownload=false;


	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		DownloadForm resourceForm = (DownloadForm) form; 
		
		try{
			if(request.getSession().getAttribute("resourceTypeList")==null){
				EDTDelegate delegate = DelegateFactory.newDelegate();
				resourceForm.setTypeListResult(getTypeList(request, delegate));
				request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
			}
			else{
				resourceForm.setTypeListResult((TypeListResult)request.getSession().getAttribute("resourceTypeList"));
			}
			Detail result = ActionUtils.getDetails(request);
		    if (result == null) {
		    	for (String serviceId: ActionUtils.getServiceIds()) {
		    		EDTDelegate delegate = DelegateFactory.newDelegate(serviceId);
		    		result= getResourceList(request, resourceForm, delegate, serviceId, result);		    		
		    	}
		    	List<DetailDataCustom> resourceList= resourceForm.getData();
		    	if(resourceList.size()>0){
					//ActionUtils.setDetails(request, result);
					//Collections.sort(resourceList, DetailDataCustom.ResourceIdComparator);
					//setting the first element to downloading to view on the jsp
					resourceList.get(0).setDownloadStatus("Downloading");
					resourceForm.setData(resourceList);
				
					request.getSession().setAttribute("resourceList",resourceList);
					request.getSession().setAttribute("resourceID",resourceList.get(0).getResourceID());
				} else{
					request.getSession().setAttribute("resourceID",BigInteger.ZERO);
				}
		    	resourceForm.setDetail(result);
			}
			
		}
		catch(Exception e) {
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("success");
		}
		
		return mapping.findForward("success");
	}		
	
	private Detail getResourceList(HttpServletRequest request, DownloadForm form, EDTDelegate delegate, String serviceId, Detail result) {
		    try {
		    	String resourceType = form.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}
				//original code
		    	//result = delegate.list(resourceType, form.getStatusAsResourceStatus(), form.getPageNoAsBigInt());		    	
		    	
		    	//filtering out the downloadable files
		    	form.getStatusAsResourceStatus();				
				result = delegate.list(resourceType, ResourceStatus.DOWNLOADABLE, form.getPageNoAsBigInt());					
				
				List<DetailDataCustom> resourceList= form.getData();
				if (resourceList==null||resourceList.size()<1) resourceList = new ArrayList<DetailDataCustom>(); 
				
				if(result!=null && result.getData()!=null && result.getResultSize() != null){ 
					/*filtering the list to contain only the files that have not been downloaded*/
					//get last downloaded resourceid				
					BigInteger lastDownLoadedID = new BigInteger(getLastDownloadedID());
					
					//creating list with only new downloadable files				
					DetailDataCustom detailDataK;
					for(DetailData detailData : result.getData()){
						if(detailData.getResourceID().compareTo(lastDownLoadedID) > 0){
							detailDataK = new DetailDataCustom();
							detailDataK.setCreateTimestamp(detailData.getCreateTimestamp());
							detailDataK.setDescription(detailData.getDescription());
							detailDataK.setModifyTimestamp(detailData.getModifyTimestamp());
							detailDataK.setResourceID(detailData.getResourceID());
							
							//detailDataK.setResourceType(detailData.getResourceType());
							detailDataK.setResourceType(getTypeDescription(form,detailData.getResourceType()));
							
							detailDataK.setResult(detailData.getResult());
							detailDataK.setStatus(detailData.getStatus());
							detailDataK.setDownloadStatus("Waiting");
							detailDataK.setServiceId(serviceId);
							
							resourceList.add(detailDataK);
						}
					}
					if(resourceList.size()>0){
						//ActionUtils.setDetails(request, result);
						Collections.sort(resourceList, DetailDataCustom.ResourceIdComparator);
						form.setData(resourceList);				
						request.getSession().setAttribute("resourceList",resourceList);
					}
				}			
				
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			
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
	
	private String getTypeDescription(DownloadForm form, String typeCode){
		String typeDesc="";
			for(TypeListData typeListData:form.getTypeListResult().getData()){
				if(typeListData.getResourceType().trim().equalsIgnoreCase(typeCode.trim())){
					typeDesc = typeListData.getDescriptionEn();
					break;
				}
			}
		return typeDesc;
	}
	
	public ActionForward download(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  {
		List<BigInteger> ids = getResourceIds(request);
		Collections.sort(ids);
		
		List<DetailDataCustom> resourceList=ActionUtils.getResourceList(request);
		String serviceId=new String();
		for (DetailDataCustom resource :resourceList) {
			if (resource.getResourceID().equals(ids.get(0))) {
				serviceId=resource.getServiceId();
				break;
			}
		}
		//-
		DownloadResult downloadResult = null;		
		
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate(serviceId);			

				downloadResult = delegate.download(ids);		
				
				//----------start to save file
				for(DownloadData d : downloadResult.getData()) {
					String inboxFolder = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
					File document = new File(inboxFolder + File.separator +  d.getDescription()); 				
					byte[] inputBytes = d.getContent();
					
					
					FileUtils.writeByteArrayToFile(document, inputBytes);
					updateLastDownloadedID(d.getResourceID().toString());					
					 
				}									
				//----------end of saving file
				
				//updating the downloading file status to Downloaded
				for(DetailDataCustom detailDatak:resourceList){
					if(detailDatak.getResourceID().equals(ids.get(0))){
						detailDatak.setDownloadStatus("Download Completed");
					}
				}
				
				//updating the next waiting file status to Downloading to display to user
				boolean isFileToWating=false;
				for(DetailDataCustom detailDatak:resourceList){
					if(detailDatak.getDownloadStatus().equals("Waiting")){
						detailDatak.setDownloadStatus("Downloading");
						request.getSession().setAttribute("resourceID",detailDatak.getResourceID());
						isFileToWating=true;
						break;
					}											
				}
				
				if(!isFileToWating){
					request.getSession().setAttribute("resourceID",BigInteger.ZERO);
					ActionUtils.removeResourceList(request);
				}
				
				
			//}		

		} catch (Exception e) {
			if(ActionUtils.getResourceList(request)!=null){
				ActionUtils.removeResourceList(request);
			}		
			if(request.getSession().getAttribute("resourceID")!=null){
				request.getSession().removeAttribute("resourceID");
			}
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("success");						
		}

		//return null;
		return mapping.findForward("success");
	}
	
	private String getLastDownloadedID(){
		String resourceID= "0";
		String inboxFolder = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
		String lastDownloadedFile = OscarProperties.getInstance().getProperty("mcedt.last.downloadedID.file");
		
		try{
			File document = new File(inboxFolder + File.separator + lastDownloadedFile);						
			List<String> lastId = FileUtils.readLines(document);
			
			if(lastId!=null && StringUtils.isNumeric(lastId.get(0)))
				resourceID = lastId.get(0);
			else
				resourceID = "0";
				
		}
		catch(Exception e){
			logger.error("Unable to get Last Download ID ", e);
		}
		
		return resourceID;
	}
	
	private void updateLastDownloadedID(String lastID){
		boolean writeResult=false;
		String inboxFolder = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
		String lastDownloadedFile = OscarProperties.getInstance().getProperty("mcedt.last.downloadedID.file");
		
		
		try{
			File document = new File(inboxFolder + File.separator + lastDownloadedFile);
			FileUtils.write(document, lastID, false);						
			
		}
		catch(Exception e) {
			logger.error("Unable to update Last Download ID ", e);						
		}
		
	}
	
	static List<BigInteger> getResourceIds(HttpServletRequest request) {
		String[] resourceIds = request.getParameterValues(REQUEST_ATTR_KEY_RESOURCE_ID);

		List<BigInteger> ids = new ArrayList<BigInteger>();
		if (resourceIds == null) {
			return ids;
		}

		for (String i : resourceIds) {
			ids.add(BigInteger.valueOf(ConversionUtils.fromIntString(i)));
		}
		return ids;
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getSession().getAttribute("resourceList")!=null){
			request.getSession().removeAttribute("resourceList");
		}		
		if(request.getSession().getAttribute("resourceID")!=null){
			request.getSession().removeAttribute("resourceID");
		}
		return mapping.findForward("cancel");
	}	
	
	public ActionForward userDownload(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  {		
		List<BigInteger> ids = getResourceIds(request);
		Collections.sort(ids);
		DownloadResult downloadResult = null;	
		DownloadForm downloadForm= (DownloadForm) form;
		try {
			EDTDelegate delegate = DelegateFactory.newDelegate(downloadForm.getServiceId()==null? ActionUtils.getDefaultServiceId():downloadForm.getServiceId());			

			downloadResult = delegate.download(ids);			

			//----------start to save file
			for(DownloadData d : downloadResult.getData()) {
				String inboxFolder = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
				File document = new File(inboxFolder + File.separator +  d.getDescription()); 				
				byte[] inputBytes = d.getContent();
									
				FileUtils.writeByteArrayToFile(document, inputBytes);													
										
			}									
			//----------end of saving file														
			saveMessages(request, ActionUtils.addMessage("resourceAction.submit.success", McedtMessageCreator.downloadResultToString(downloadResult)));
			
							
		} catch (Exception e) {
			
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("error");						
		}		
		
		return mapping.findForward("success");
	}		
	
	public ActionForward changeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)  {
		List<DetailDataCustom> resourceList = getResourceList(request,form);		
		
		request.getSession().setAttribute("resourceListSent",resourceList );

		return mapping.findForward("success");
	}
	
	private List<DetailDataCustom> getResourceList(HttpServletRequest request, ActionForm form) {
	    Detail result = ActionUtils.getDetails(request);
	    List<DetailDataCustom> resourceList =new ArrayList<DetailDataCustom>();
	    DownloadForm resourceForm = (DownloadForm)form;    	    	    
	    	    
	    if (result == null) {
		    try {
		    	String resourceType = resourceForm.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}
	    	
		    	EDTDelegate delegate = DelegateFactory.newDelegate(resourceForm.getServiceId());		    	
		    	result = delegate.list(resourceType, ResourceStatus.DOWNLOADABLE, resourceForm.getPageNoAsBigInt());								
				
		    	if(request.getSession().getAttribute("resourceTypeList")==null){
					resourceForm.setTypeListResult(ActionUtils.getTypeList(request, delegate));
					request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
				}
				else{
					resourceForm.setTypeListResult((TypeListResult)request.getSession().getAttribute("resourceTypeList"));
				}
		    	
				if(result!=null && result.getData()!=null && result.getResultSize() != null){ 
									
					DetailDataCustom detailDataK;
					for(DetailData detailData : result.getData()){
							detailDataK = new DetailDataCustom();														
							ResourceForm resourceForm2 = new ResourceForm();
							resourceForm2.setTypeListResult(resourceForm.getTypeListResult() );
							resourceForm2.setServiceIdSent(resourceForm.getServiceId());
							detailDataK = ActionUtils.mapDetailData(resourceForm2, detailDataK, detailData);							
							resourceList.add(detailDataK);
						
					}												

					if(resourceList.size()>0){						
						//Collections.sort(resourceList, DetailDataCustom.ResourceIdComparator);										
						request.getSession().setAttribute("resourceListDL",resourceList);
					}
				} else if (result.getResultSize() == null)	{
					request.getSession().removeAttribute("resourceListDL");
					// if a result is returned with no size, meaning you are accessing a list that is not permitted, one response will be returned holding the error message
					saveErrors(request,ActionUtils.addMessage("resourceAction.getResourceList.fault", result.getData().get(0).getResult().getMsg()));		
				}	
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			
			}
	    }	    
	    return resourceList;
    }
}
