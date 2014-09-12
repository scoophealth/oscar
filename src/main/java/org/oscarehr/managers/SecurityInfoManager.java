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

import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quatro.dao.security.SecobjprivilegeDao;
import com.quatro.dao.security.SecuserroleDao;
import com.quatro.model.security.Secobjprivilege;
import com.quatro.model.security.Secuserrole;

@Service
public class SecurityInfoManager {

	@Autowired 
	private SecRoleDao roleDao;
	
	@Autowired
	private SecuserroleDao secUserRoleDao;
	
	@Autowired
	private SecobjprivilegeDao secobjprivilegeDao;
    
	
	public List<Secuserrole> getRoles(LoggedInInfo loggedInInfo) {
		List<Secuserrole> results =  secUserRoleDao.findByProviderNo(loggedInInfo.getLoggedInProviderNo());
		
	//	LogAction.addLogSynchronous(loggedInInfo, "SecurityInfoManager.getRoles", loggedInInfo.getLoggedInProviderNo());
			
		return results;
	}
	
	public List<Secobjprivilege> getSecurityObjects(LoggedInInfo loggedInInfo) {
		
		List<String> roleNames = new ArrayList<String>();
		for(Secuserrole role:getRoles(loggedInInfo)) {
			roleNames.add(role.getRoleName());
		}
		roleNames.add(loggedInInfo.getLoggedInProviderNo());
		
		List<Secobjprivilege> results = secobjprivilegeDao.getByRoles(roleNames);
		
	//	LogAction.addLogSynchronous(loggedInInfo, "SecurityInfoManager.getSecurityObjects", loggedInInfo.getLoggedInProviderNo());
			
		return results;
	}
	
	/**
	 * Checks to see if this provider has the privilege to the security object being requested.
	 * 
	 * The way it's coded now
	 * 
	 * get all the roles associated with the logged in provider, including the roleName=providerNo.
	 * find the privileges using the roles list.
	 * 
	 * Loop through all the rights, if we find one that can evaluate to true , we exit..else we keep checking
	 * 
	 * if r then an entry with r | w | x  is required
	 * if w then an entry with w | x is required
	 * if u then an entry with u | x is required
	 * if d then an entry with d | x is required
	 * 
	 * @param loggedInInfo
	 * @param objectName
	 * @param privilege
	 * @return
	 */
	public boolean hasPrivilege(LoggedInInfo loggedInInfo, String objectName, String privilege) {
		
		List<String> roleNames = new ArrayList<String>();
		for(Secuserrole role:getRoles(loggedInInfo)) {
			roleNames.add(role.getRoleName());
		}
		roleNames.add(loggedInInfo.getLoggedInProviderNo());
		
		
		List<Secobjprivilege> results = secobjprivilegeDao.getByObjectNameAndRoles(objectName, roleNames);
		
		for(Secobjprivilege result:results) {
			
			if("r".equals(privilege) && 
					("r".equals(result.getPrivilege_code()) || "w".equals(result.getPrivilege_code()) || "x".equals(result.getPrivilege_code())) ) {
				return true;
			}
			if("w".equals(privilege) && 
					("w".equals(result.getPrivilege_code()) || "x".equals(result.getPrivilege_code()) ) ) {
				return true;
			}
			if("u".equals(privilege) && 
					("u".equals(result.getPrivilege_code()) || "x".equals(result.getPrivilege_code()) ) ) {
				return true;
			}
			if("d".equals(privilege) && 
					("d".equals(result.getPrivilege_code()) || "x".equals(result.getPrivilege_code()) ) ) {
				return true;
			}
		}
		
		return false;
	}
	
}
