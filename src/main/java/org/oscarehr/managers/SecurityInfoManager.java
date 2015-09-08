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
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.exception.PatientDirectiveException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.util.OscarRoleObjectPrivilege;

import com.quatro.dao.security.SecobjprivilegeDao;
import com.quatro.dao.security.SecuserroleDao;
import com.quatro.model.security.Secobjprivilege;
import com.quatro.model.security.Secuserrole;

@Service
public class SecurityInfoManager {
	public static final String READ = "r";
	public static final String WRITE = "w";
	public static final String UPDATE = "u";
	public static final String DELETE = "d";
	public static final String NORIGHTS = "o";
	
	
	@Autowired
	private SecuserroleDao secUserRoleDao;
	
	@Autowired
	private SecobjprivilegeDao secobjprivilegeDao;
    
	
	public List<Secuserrole> getRoles(LoggedInInfo loggedInInfo) {
		@SuppressWarnings("unchecked")
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
	
	public boolean hasPrivilege(LoggedInInfo loggedInInfo, String objectName, String privilege, int demographicNo) {
		return hasPrivilege(loggedInInfo, objectName, privilege, String.valueOf(demographicNo));
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
	 * if r then an entry with r | u |w | x  is required
	 * if u then an entry with u | w | x is required
	 * if w then an entry with w | x is required
	 * if d then an entry with d | x is required
	 * 
	 * Privileges priority is taken care of by OscarRoleObjectPrivilege.checkPrivilege()
	 *
	 * If patient-specific privileges are present, it takes priority over the general privileges.
	 * For checking non-patient-specific object privileges, call with demographicNo==null.
	 * 
	 * @param loggedInInfo
	 * @param objectName
	 * @param privilege
	 * @param demographicNo
	 * @return boolean
	 */
	public boolean hasPrivilege(LoggedInInfo loggedInInfo, String objectName, String privilege, String demographicNo) {
		try {
			List<String> roleNameLs = new ArrayList<String>();
			for(Secuserrole role:getRoles(loggedInInfo)) {
				roleNameLs.add(role.getRoleName());
			}
			roleNameLs.add(loggedInInfo.getLoggedInProviderNo());
			String roleNames = StringUtils.join(roleNameLs, ",");
			
			boolean noMatchingRoleToSpecificPatient = true;
			List v = null;
			if (demographicNo!=null) {
				v = OscarRoleObjectPrivilege.getPrivilegeProp(objectName+"$"+demographicNo);
				List<String> roleInObj = (List<String>)v.get(1);
				
				for (String objRole : roleInObj) {
					if (roleNames.toLowerCase().contains(objRole.toLowerCase().trim())) {
						noMatchingRoleToSpecificPatient = false;
						break;
					}
				}
			}
			if (noMatchingRoleToSpecificPatient) v = OscarRoleObjectPrivilege.getPrivilegeProp(objectName);
			
			if (!noMatchingRoleToSpecificPatient && OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), NORIGHTS)) {
					throw new PatientDirectiveException("Patient has requested user not access record");
			} else  if (OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), "x")) {
				return true;
			}
			else if (OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), WRITE)) {
				return ((READ+UPDATE+WRITE).contains(privilege));
			}
			else if (OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), UPDATE)) {
				return ((READ+UPDATE).contains(privilege));
			}
			else if (OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), READ)) {
				return (READ.equals(privilege));
			}
			else if (OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), DELETE)) {
				return (DELETE.equals(privilege));
			}
	
		} catch (PatientDirectiveException ex) {
			throw(ex);
		} catch (Exception ex) {
			MiscUtils.getLogger().error("Error checking privileges", ex);
		}
		
		return false;
	}
	
	public boolean isAllowedAccessToPatientRecord(LoggedInInfo loggedInInfo, Integer demographicNo) {
		
		List<String> roleNameLs = new ArrayList<String>();
		for(Secuserrole role:getRoles(loggedInInfo)) {
			roleNameLs.add(role.getRoleName());
		}
		roleNameLs.add(loggedInInfo.getLoggedInProviderNo());
		String roleNames = StringUtils.join(roleNameLs, ",");
		
		
		Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_demographic$"+demographicNo);
		if(OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), "o")) {
			return false;
		}
		
		v = OscarRoleObjectPrivilege.getPrivilegeProp("_eChart$"+demographicNo);
		if(OscarRoleObjectPrivilege.checkPrivilege(roleNames, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), "o")) {
			return false;
		}
		
		return true;
	}
}
