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

package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.AppDefinitionTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class AppManager {
	protected Logger logger = MiscUtils.getLogger();
	 
	@Autowired
	private AppDefinitionDao appDefinitionDao;

	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	

	public List<AppDefinitionTo1> getAppDefinitions(LoggedInInfo loggedInInfo){
		List<AppDefinition> appList = appDefinitionDao.findAll();
		List<AppDefinitionTo1> returningAppList = new ArrayList<AppDefinitionTo1>(appList.size());
		for(AppDefinition app: appList) {
			AppDefinitionTo1 appTo = new AppDefinitionTo1();
			appTo.setId(app.getId());
			appTo.setAdded(app.getAdded());
			appTo.setAppType(app.getAppType());
			appTo.setName(app.getName());
			appTo.setActive(app.getActive());
			appTo.setAddedBy(app.getAddedBy());
			AppUser appuser = appUserDao.findForProvider(app.getId(),loggedInInfo.getLoggedInProviderNo());
			if (appuser != null){
				appTo.setAuthenticated(true);
			}else{
				appTo.setAuthenticated(false);
			}
			returningAppList.add(appTo);
		}
		
		//--- log action ---
		if (returningAppList!=null && returningAppList.size()>0) {
			String resultIds=AppDefinition.getIdsAsStringList(appList);
			LogAction.addLogSynchronous(loggedInInfo, "AppManager.getAppDefinitions", "ids returned=" + resultIds);
		}
		
		return returningAppList;
	}
	
	public AppDefinition saveAppDefinition(LoggedInInfo loggedInInfo,  AppDefinition appDef){
		//Can user create new AppDefinitions?
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		appDefinitionDao.persist(appDef);
		
		//--- log action ---
		if (appDef!=null) {
			LogAction.addLogSynchronous(loggedInInfo, "AppManager.saveAppDefinition", "id=" + appDef.getId());
		}
		return appDef;
	}
	
	public AppDefinition getAppDefinition(LoggedInInfo loggedInInfo,  String appName){
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_appDefinition", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		AppDefinition appDef = appDefinitionDao.findByName(appName);
		
		//--- log action ---
		if (appDef!=null) {
			LogAction.addLogSynchronous(loggedInInfo, "AppManager.getAppDefinition", "id=" + appDef.getId());
		}
		return appDef;
	}

	
	public boolean isK2AUser(LoggedInInfo loggedInInfo){
		return isK2AUser(loggedInInfo.getLoggedInProviderNo());
	}
	
	public boolean isK2AUser(String providerNo){
		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
		if(k2aApp != null) {
			AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),providerNo);
			if(k2aUser != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isK2AEnabled(){
		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
		if(k2aApp != null){
			return true;
		}
		return false;
	}
	
	public String getK2ANotificationNumber(LoggedInInfo loggedInInfo){
		String retval = "-";
		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
		if(k2aApp != null) {
			AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProviderNo());
				
			if(k2aUser != null) {
				String notificationStr = null;
				try{
					notificationStr = OAuth1Utils.getOAuthGetResponse(loggedInInfo,k2aApp, k2aUser, "/ws/api/notification", "/ws/api/notification");
					JSONObject notifyObject = JSONObject.fromObject(notificationStr);
					retval = notifyObject.getString("numberOfNotifications");
				}catch(Exception e){
					logger.error("User is not logged in "+notificationStr);
				}
			}
		}
		return retval;
	}
	
}
