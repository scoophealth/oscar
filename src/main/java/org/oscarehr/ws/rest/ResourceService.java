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
package org.oscarehr.ws.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.dao.ResourceStorageDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.ResourceStorage;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.model.NotificationTo1;
import org.oscarehr.ws.rest.util.ClinicalConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.oscarPrevention.PreventionDS;
import oscar.oscarRx.util.LimitedUseLookup;


@Path("/resources")
public class ResourceService extends AbstractServiceImpl {
	private static final Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	@Autowired
	private AppDefinitionDao appDefinitionDao;
	
	@Autowired
	AppManager appManager;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	
	@Autowired
	private PreventionDS preventionDS;
	
	@GET
	@Path("/K2AActive/")
	@Produces("application/json")
	public GenericRESTResponse isK2AActive(@Context HttpServletRequest request){
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
    	if(!com.quatro.service.security.SecurityManager.hasPrivilege("_admin", roleName$)  && !com.quatro.service.security.SecurityManager.hasPrivilege("_report", roleName$)) {
    		throw new SecurityException("Insufficient Privileges");
    	}
		
		GenericRESTResponse response = null;
		AppDefinition appDef = appDefinitionDao.findByName("K2A");
		if(appDef == null){
			response = new GenericRESTResponse(false,"K2A active");
		}else{
			response = new GenericRESTResponse(true,"K2A not active");
		}
		return response;
	}
	
	private String getResource(LoggedInInfo loggedInInfo,String requestURI, String baseRequestURI) {
		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
		if(k2aApp != null) {
			AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
			
			if(k2aUser != null) {
				return OAuth1Utils.getOAuthGetResponse(loggedInInfo,k2aApp, k2aUser, requestURI, baseRequestURI);
			} else {
				return null;
			}
		}
		return null;
	}
	
	@GET
	@Path("/preventionRulesList")
	@Produces("application/json")
	public JSONArray getPreventionRulesListFromK2A(@Context HttpServletRequest request) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		JSONArray retArray = new JSONArray();
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/PREVENTION_RULES/list", "/ws/api/oscar/get/PREVENTION_RULES/list"); 
			JSONArray rulesArray = JSONArray.fromObject(resource);
			
			//id  |  type  |       created_at       |       updated_at       | created_by | updated_by |       body        |      name      | private 
			logger.info("rules json"+rulesArray);
			for(int i = 0; i < rulesArray.size(); i++){
				JSONObject jobject = new JSONObject();
				JSONObject rule = (JSONObject) rulesArray.get(i);
				jobject.put("id", rule.getString("id"));
				jobject.put("name", rule.getString("name"));
				jobject.put("rulesXML", rule.getString("body"));
				jobject.put("created_at", rule.getString("created_at"));
				jobject.put("author", rule.getString("author"));
				
				retArray.add(jobject);
			}
			
		} catch(Exception e) {
			logger.error("Error retrieving prevention list",e);
			return null;
		}
		
		
		return retArray;
	}
	
	@GET
	@Path("/currentPreventionRulesVersion")
	@Produces("application/json")
	public String getCurrentPreventionRulesVersion(){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		ResourceBundle bundle = getResourceBundle();

		String preventionPath = OscarProperties.getInstance().getProperty("PREVENTION_FILE");
        if ( preventionPath != null){
        	return bundle.getString("prevention.currentrules.propertyfile");
        }else{
        	ResourceStorage resourceStorage = resourceStorageDao.findActive(ResourceStorage.PREVENTION_RULES);
        	if(resourceStorage != null){
        		return bundle.getString("prevention.currentrules.resourceStorage")+" "+resourceStorage.getResourceName();
        	}
        }
        return bundle.getString("prevention.currentrules.default");
	}
	
	@POST
	@Path("/loadPreventionRulesById/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public String addK2AReport(@PathParam("id") String id, @Context HttpServletRequest request,JSONObject jSONObject) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
    	
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			
			//Log agreement
			if(jSONObject.containsKey("agreement")){
				String action = "oauth1_AGREEMENT";
		    	String content = "PREVENTION_RULES_AGREEMENT";
		    	String contentId = id;
		    	String demographicNo = null;
		    	String data = jSONObject.getString("agreement");
		    	LogAction.addLog(loggedInInfo, action, content, contentId, demographicNo, data);
			}
			
			
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/PREVENTION_RULES/id/"+id, "/ws/api/oscar/get/PREVENTION_RULES/id/"+id); 
			
			if(resource !=null){
				//JSONObject jSONObject = JSONObject.fromObject(resource);
				ResourceStorage resourceStorage = new ResourceStorage();
				resourceStorage.setActive(true);
				resourceStorage.setResourceName(jSONObject.getString("name"));
				resourceStorage.setResourceType(ResourceStorage.PREVENTION_RULES);
				if(jSONObject.containsKey("uuid")){
					resourceStorage.setUuid(jSONObject.getString("uuid"));
				}
				resourceStorage.setUploadDate(new Date());
				resourceStorage.setFileContents(resource.getBytes());
				resourceStorage.setUuid(null);
				
				List<ResourceStorage> currActive=  resourceStorageDao.findActiveAll(ResourceStorage.PREVENTION_RULES);
				if(currActive != null){
					for(ResourceStorage rs: currActive){
						rs.setActive(false);
						resourceStorageDao.merge(rs);
					}
				}
				resourceStorageDao.persist(resourceStorage);
			}
			
		} catch(Exception e) {
			logger.error("Error saving Resource to Storage",e);
		}
		return null;
	}
	
	@GET
	@Path("/luCodesList")
	@Produces("application/json")
	public JSONArray getLUCodeFileListFromK2A(@Context HttpServletRequest request) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		JSONArray retArray = new JSONArray();
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/LU_CODES/list", "/ws/api/oscar/get/LU_CODES/list"); 
			JSONArray rulesArray = JSONArray.fromObject(resource);
			
			//id  |  type  |       created_at       |       updated_at       | created_by | updated_by |       body        |      name      | private 
			logger.info("rules json"+rulesArray);
			for(int i = 0; i < rulesArray.size(); i++){
				JSONObject jobject = new JSONObject();
				JSONObject rule = (JSONObject) rulesArray.get(i);
				jobject.put("id", rule.getString("id"));
				jobject.put("name", rule.getString("name"));
				jobject.put("rulesXML", rule.getString("body"));
				jobject.put("created_at", rule.getString("created_at"));
				jobject.put("author", rule.getString("author"));
				
				retArray.add(jobject);
			}
			
		} catch(Exception e) {
			logger.error("Error retrieving prevention list",e);
			return null;
		}
		
		
		return retArray;
	}
	
	@POST
	@Path("/loadLuCodesById/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public String addLuCodes(@PathParam("id") String id, @Context HttpServletRequest request,JSONObject jSONObject) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
    	
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			
			//Log agreement
			if(jSONObject.containsKey("agreement")){
				String action = "oauth1_AGREEMENT";
		    	String content = "LU_CODES_AGREEMENT";
		    	String contentId = id;
		    	String demographicNo = null;
		    	String data = jSONObject.getString("agreement");
		    	LogAction.addLog(loggedInInfo, action, content, contentId, demographicNo, data);
			}
			
			
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/LU_CODES/id/"+id, "/ws/api/oscar/get/LU_CODES/id/"+id); 
			
			if(resource !=null){
				//JSONObject jSONObject = JSONObject.fromObject(resource);
				ResourceStorage resourceStorage = new ResourceStorage();
				resourceStorage.setActive(true);
				resourceStorage.setResourceName(jSONObject.getString("name"));
				resourceStorage.setResourceType(ResourceStorage.LU_CODES);
				if(jSONObject.containsKey("uuid")){
					resourceStorage.setUuid(jSONObject.getString("uuid"));
				}
				resourceStorage.setUploadDate(new Date());
				resourceStorage.setFileContents(resource.getBytes());
				resourceStorage.setUuid(null);
				
				List<ResourceStorage> currActive=  resourceStorageDao.findActiveAll(ResourceStorage.LU_CODES);
				if(currActive != null){
					for(ResourceStorage rs: currActive){
						rs.setActive(false);
						resourceStorageDao.merge(rs);
					}
				}
				resourceStorageDao.persist(resourceStorage);
				LimitedUseLookup.reLoadLookupInformation();
			}
			
		} catch(Exception e) {
			logger.error("Error saving Resource to Storage",e);
		}
		return null;
	}
	
	
	@GET
	@Path("/currentLuCodesVersion")
	@Produces("application/json")
	public String getCurrentLuCodesVersion(){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		ResourceBundle bundle = getResourceBundle();
		
		String fileName = OscarProperties.getInstance().getProperty("odb_formulary_file");
		if (fileName != null && !fileName.isEmpty()) {
			return bundle.getString("lucodes.currentrules.propertyfile");
		}else{	
	    	ResourceStorage resourceStorage = resourceStorageDao.findActive(ResourceStorage.LU_CODES);
	    	if(resourceStorage != null){
	    		return bundle.getString("lucodes.currentrules.resourceStorage")+" "+resourceStorage.getResourceName();
	    	}
		}
        return bundle.getString("lucodes.currentrules.default");
	}
	
	
	@GET
	@Path("/notifications")
	@Produces("application/json")
	public List<NotificationTo1> getNotifications(@Context HttpServletRequest request) {
		List<NotificationTo1> list = new ArrayList<NotificationTo1>();
		try{
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			String notificationStr = getResource(loggedInInfo,"/ws/api/notification","/ws/api/notification");
			JSONObject notifyObject = JSONObject.fromObject(notificationStr);
			try{
				JSONObject notifyList = notifyObject.getJSONObject("notification");
				list.add(NotificationTo1.fromJSON(notifyList));
			}catch(Exception e){
				JSONArray notifyArrList = notifyObject.getJSONArray("notification");
				for(int i=0; i < notifyArrList.size();i++){
					list.add(NotificationTo1.fromJSON(notifyArrList.getJSONObject(i)));
				}
			}
		}catch(Exception e){
			logger.error("Error geting notifcations",e);
		}
		return list;
	}
	
	@GET
	@Path("/notifications/number")
	@Produces("application/json")
	public Response getNotificationsNumber(@Context HttpServletRequest request) {
		String k2aNoficationCount = "-";
		try{
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			String notificationStr = getResource(loggedInInfo,"/ws/api/notification","/ws/api/notification");
			JSONObject notifyObject = JSONObject.fromObject(notificationStr);
			k2aNoficationCount = notifyObject.getString("numberOfNotifications");
		}catch(Exception e){
			logger.error("Error geting notifcations",e);
		}
		return Response.ok(k2aNoficationCount).build();
	}
	
	@POST
	@Path("/notifications/readmore")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getMoreInfoNotificationURL(@Context HttpServletRequest request,JSONObject jSONObject) {
		String retval= "";
		try{
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			if(k2aApp != null) {
				AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
				
				if(k2aUser != null) {
					retval = OAuth1Utils.getOAuthPostResponse(loggedInInfo,k2aApp, k2aUser, "/ws/api/notification/readmore", "/ws/api/notification/readmore",OAuth1Utils.getProviderK2A(),NotificationTo1.fromJSON(jSONObject));
					logger.debug(retval);
				}
			}
		}catch(Exception e){
			logger.error("ERROR:",e);
		}
		return Response.ok(retval).build();
	}
	
	@POST
	@Path("/notifications/ack/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response markNotificationAsAck(@PathParam("id") String id, @Context HttpServletRequest request,JSONObject jSONObject) {
		String retval= "";
		try{
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			if(k2aApp != null) {
				AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
				
				if(k2aUser != null) {
					retval = OAuth1Utils.getOAuthPostResponse(loggedInInfo,k2aApp, k2aUser, "/ws/api/notification/ack", "/ws/api/notification/ack",OAuth1Utils.getProviderK2A(),NotificationTo1.fromJSON(jSONObject));
					logger.debug(retval);
				}
			}
		}catch(Exception e){
			logger.error("ERROR:",e);
		}
		return Response.ok(retval).build();
	}
	
	@GET
	@Path("/clinicalconnect")
	@Produces("text/plain")
	public String launchClinicalConnect(@Context HttpServletRequest request) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if (ClinicalConnectUtil.isReady(loggedInInfo.getLoggedInProviderNo()))
			return ClinicalConnectUtil.getLaunchURL(loggedInInfo, null);
		else
			return null;
	}
}
