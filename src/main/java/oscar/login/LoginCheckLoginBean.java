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


package oscar.login;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;

import com.quatro.model.security.LdapSecurity;

public final class LoginCheckLoginBean {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String LOG_PRE = "Login!@#$: ";

	private String username = "";
	private String password = "";
	private String pin = "";
	private String ip = "";

	private String userpassword = null; // your password in the table

	private String firstname = null;
	private String lastname = null;
	private String profession = null;
	private String rolename = null;

	private Security security = null;

	public void ini(String user_name, String password, String pin1, String ip1) {
		setUsername(user_name);
		setPassword(password);
		setPin(pin1);
		setIp(ip1);
	}

	public String[] authenticate() {
		security = getUserID();

		// the user is not in security table
		if (security == null) {
			return cleanNullObj(LOG_PRE + "No Such User: " + username);
		}
		boolean auth = false;
		
		if(security.getStorageVersion() == Security.STORAGE_VERSION_1) {
			// check pin if needed
	
			String sPin = pin;
			if (oscar.OscarProperties.getInstance().isPINEncripted()) sPin = oscar.Misc.encryptPIN(sPin);
	
			if (isWAN() && security.getBRemotelockset() != null && security.getBRemotelockset().intValue() == 1 && (!sPin.equals(security.getPin()) || pin.length() < 3)) {
				return cleanNullObj(LOG_PRE + "Pin-remote needed: " + username);
			} else if (!isWAN() && security.getBLocallockset() != null && security.getBLocallockset().intValue() == 1 && (!sPin.equals(security.getPin()) || pin.length() < 3)) {
				return cleanNullObj(LOG_PRE + "Pin-local needed: " + username);
			}
	
			if (security.getBExpireset() != null && security.getBExpireset().intValue() == 1 && (security.getDateExpiredate() == null || security.getDateExpiredate().before(new Date()))) {
				return cleanNullObjExpire(LOG_PRE + "Expired: " + username);
			}
			
	
			userpassword = security.getPassword();
			if (userpassword.length() < 20) {
				auth = password.equals(userpassword);
			} else {
				auth = security.checkPassword(password);
			}
		} else if(security.getStorageVersion() == Security.STORAGE_VERSION_2) {
			
			try {
				//password check
				if(!PasswordHash.validatePassword(this.password, security.getPassword())) {
					return cleanNullObj(LOG_PRE + "Password failed: " + username);
				}
				
				//remote pin check
				if (isWAN() && security.getBRemotelockset() != null && security.getBRemotelockset().intValue() == 1 && (!PasswordHash.validatePassword(pin, security.getPin()))) {
					return cleanNullObj(LOG_PRE + "Pin-remote needed: " + username);
				}
				
				//local pin check
				if (!isWAN() && security.getBLocallockset() != null && security.getBLocallockset().intValue() == 1 && (!PasswordHash.validatePassword(pin, security.getPin()))) {
					return cleanNullObj(LOG_PRE + "Pin-local needed: " + username);
				}
				
			}catch(Exception e) {
				MiscUtils.getLogger().error("Unable to check the password",e);
				return cleanNullObj(LOG_PRE + "Password failed: " + username);
			}
			
			auth=true;
		}
		
		
		//expiry
		String expired_days = "";
		if (security.getBExpireset() != null && security.getBExpireset().intValue() == 1) {
			// Give warning if the password will be expired in 10 days.

			long date_expireDate = security.getDateExpiredate().getTime();
			long date_now = new Date().getTime();
			long date_diff = (date_expireDate - date_now) / (24 * 3600 * 1000);

			if (security.getBExpireset().intValue() == 1 && date_diff < 11) {
				expired_days = String.valueOf(date_diff);
			}
		}

		if (auth) { // login successfully
			String[] strAuth = new String[6];
			strAuth[0] = security.getProviderNo();
			strAuth[1] = firstname;
			strAuth[2] = lastname;
			strAuth[3] = profession;
			strAuth[4] = rolename;
			strAuth[5] = expired_days;
			return strAuth;
		} else { // login failed
			return cleanNullObj(LOG_PRE + "password failed: " + username);
		}
	}

	private String[] cleanNullObj(String errorMsg) {
		logger.warn(errorMsg);
		LogAction.addLogSynchronous("", "failed", LogConst.CON_LOGIN, username, ip);
		userpassword = null;
		password = null;
		return null;
	}

	private String[] cleanNullObjExpire(String errorMsg) {
		logger.warn(errorMsg);
		LogAction.addLogSynchronous("", "expired", LogConst.CON_LOGIN, username, ip);
		userpassword = null;
		password = null;
		return new String[] { "expired" };
	}

	private Security getUserID() {
		//this gets a pass on not being used by a manager because the user is not authenticated
		//I didn't want to add a non loggedInInfo method for getByUserName()
		SecurityDao securityDao = (SecurityDao) SpringUtils.getBean("securityDao");
		List<Security> results = securityDao.findByUserName(username);
		Security security = null;
		if (results.size() > 0) security = results.get(0);

		if (security == null) {
			return null;
		} else if (OscarProperties.isLdapAuthenticationEnabled()) {
			security = new LdapSecurity(security);
		}

		// find the detail of the user
		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
		Provider provider = providerDao.getProvider(security.getProviderNo());

		if (provider != null) {
			firstname = provider.getFirstName();
			lastname = provider.getLastName();
			profession = provider.getProviderType();
		}

		// retrieve the oscar roles for this Provider as a comma separated list
		SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
		List<SecUserRole> roles = secUserRoleDao.getUserRoles(security.getProviderNo());
		for (SecUserRole role : roles) {
			if (rolename == null) {
				rolename = role.getRoleName();
			} else {
				rolename += "," + role.getRoleName();
			}
		}

		return security;
	}

	public boolean isWAN() {
		boolean bWAN = true;
		Properties p = OscarProperties.getInstance();
		if (ip.startsWith(p.getProperty("login_local_ip"))) bWAN = false;
		return bWAN;
	}

	public void setUsername(String user_name) {
		this.username = user_name;
	}

	public void setPassword(String password) {
		this.password = password.replace(' ', '\b'); // no white space to be allowed in the password
	}

	public void setPin(String pin1) {
		this.pin = pin1.replace(' ', '\b');
	}

	public void setIp(String ip1) {
		this.ip = ip1;
	}

	public Security getSecurity() {
		return (security);
	}

}
