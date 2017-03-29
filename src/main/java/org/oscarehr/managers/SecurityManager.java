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

import java.util.Date;
import java.util.List;

import org.apache.cxf.common.util.StringUtils;
import org.oscarehr.common.dao.SecurityArchiveDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.login.PasswordHash;

@Service
public class SecurityManager {

	//private static Logger logger = MiscUtils.getLogger();

	@Autowired
	private SecurityDao securityDao;
	
	@Autowired
	private SecurityArchiveDao securityArchiveDao;
	
	
	public void saveNewSecurityRecord(LoggedInInfo loggedInInfo, Security security) {
		if(!isSecurityObjectValid(security)) {
			throw new IllegalArgumentException("Invalid Security object built");
		}
		security.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		security.setLastUpdateDate(new Date());
		
		securityDao.persist(security);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.saveNewSecurityRecord", "id=" + security.getId());
	}
	
	public void updateSecurityRecord(LoggedInInfo loggedInInfo, Security security) {
		if(!isSecurityObjectValid(security)) {
			throw new IllegalArgumentException("Invalid Security object built");
		}
		
		Security dbSecurity = securityDao.find(security.getId());
		
		securityArchiveDao.archiveRecord(dbSecurity);
		
		security.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		security.setLastUpdateDate(new Date());
		
		securityDao.merge(security);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.updateSecurityRecord", "id=" + security.getId());
	}
	
	public boolean checkPasswordAgainstPrevious(String newPassword, String providerNo) {
		//check previous passwords policy if the password is being changed
		String previousPasswordPolicy = OscarProperties.getInstance().getProperty("password.pastPasswordsToNotUse", "0");
		try {
			Security dbSecurity = securityDao.getByProviderNo(providerNo);
			
			if(!"0".equals(previousPasswordPolicy) && !PasswordHash.validatePassword(newPassword, dbSecurity.getPassword())) {
		
				int numToGoBack = Integer.parseInt(previousPasswordPolicy);
				List<String> archives = securityArchiveDao.findPreviousPasswordsByProviderNo(providerNo,numToGoBack);
				
				boolean foundItInPast=false;
				
				for(String a:archives) {
					if(PasswordHash.validatePassword(newPassword, a)) {
						foundItInPast = true;
						break;
					}
				}
				
				if(foundItInPast) {
					return true;
				}
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
			throw new RuntimeException(e);
		}
		return false;
	}
	
	
	public Security findByProviderNo(LoggedInInfo loggedInInfo, String providerNo) {
		
		List<Security> results = securityDao.findByProviderNo(providerNo);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findByProviderNo", "providerNo=" + providerNo);

		if(!results.isEmpty()) {
			return results.get(0);
		}
		
		return null;
	}
	
	public Security find(LoggedInInfo loggedInInfo, Integer id) {
		
		Security result = securityDao.find(id);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.find", "id=" + id);

		return result;
	}
	
	public List<Security> findByUserName(LoggedInInfo loggedInInfo, String userName) {
		
		List<Security> results = securityDao.findByUserName(userName);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findByUserName", "userName=" + userName);
		
		return results;
	}
	
	public boolean getPasswordResetFlag(String userName) {
		
		List<Security> results = securityDao.findByUserName(userName);
		
		if(results.isEmpty()) {
			return false;
		}
		
		return (results.get(0).isForcePasswordReset() != null && results.get(0).isForcePasswordReset().equals(Boolean.TRUE));
	}
	
	public boolean isRequireUpgradeToStorage(String userName) {
		
		List<Security> results = securityDao.findByUserName(userName);
		
		if(results.isEmpty()) {
			return false;
		}
		
		return (results.get(0).getStorageVersion() != Security.STORAGE_VERSION_2);
	}

	public List<Security> findByProviderSite(LoggedInInfo loggedInInfo, String providerNo) {
		
		List<Security> results = securityDao.findByProviderSite(providerNo);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findByProviderSite", "providerNo=" + providerNo);
		
		return results;
	}
	
	public List<Security> findAllOrderByUserName(LoggedInInfo loggedInInfo) {
		
		List<Security> results = securityDao.findAllOrderBy("user_name");
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findAllOrderByUserName", "");
		
		return results;
		
	}
	
	public List<Security> findByLikeProviderNo(LoggedInInfo loggedInInfo, String providerNo) {
		List<Security> results = securityDao.findByLikeProviderNo(providerNo);
		
		LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findByLikeProviderNo", "providerNo=" + providerNo);
		
		return results;
	}
	
	 public List<Security> findByLikeUserName(LoggedInInfo loggedInInfo, String userName) { 
		 List<Security> results = securityDao.findByLikeUserName(userName);
			
		 LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findByLikeUserName", "userName=" + userName);

		return results;
	 }
	 
	 public void remove(LoggedInInfo loggedInInfo, Integer id) { 
		
		 securityDao.remove(id);
		 
		 LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.remove", "id=" + id);
	 }
	 
	 public List<Object[]> findProviders(LoggedInInfo loggedInInfo) {
		 
		 List<Object[]> results = securityDao.findProviders();
		 
		 LogAction.addLogSynchronous(loggedInInfo, "SecurityManager.findProviders", "");
		 
		 return results;
	 }
	
	protected boolean isSecurityObjectValid(Security security) {
		if(security == null) {
			return false;
		}
		
		if(StringUtils.isEmpty(security.getPassword())) {
			return false;
		}
		
		if(StringUtils.isEmpty(security.getProviderNo())) {
			return false;
		}
		
		if(StringUtils.isEmpty(security.getUserName())) {
			return false;
		}
		
		return true;
	}
}
