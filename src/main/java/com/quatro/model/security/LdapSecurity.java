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
package com.quatro.model.security;

import java.util.Date;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.oscarehr.common.model.Security;

import oscar.login.jaas.LoginModuleFactory;
import oscar.login.jaas.OscarCallbackHandler;

/**
 * Security class for integration with LDAP.
 *
 */
public class LdapSecurity extends Security {

	public LdapSecurity() {
		super();
	}

	public LdapSecurity(String userName, String password, String providerNo, String pin, Integer BRemotelockset, Integer BLocallockset, Date dateExpiredate, Integer BExpireset, Boolean forcePasswordReset) {
		super(userName, password, providerNo, pin, BRemotelockset, BLocallockset, dateExpiredate, BExpireset, forcePasswordReset,1);
	}

	public LdapSecurity(Security security) {
		super(security);
	}

	/**
	 * Clones this instance as a {@link Security} class.
	 * 
	 * @return
	 * 		Returns a deep copy of this instance as {@link Security}.
	 */
	public Security toSecurity() {
		return new Security(this);
	}

	/**
	 * Checks password and {@link #getUserName()} agains LDAP database.
	 * 
	 * @return
	 * 		Returns trues if password validation agains the AD is successful and false otherwise
	 * 
	 * @see org.oscarehr.common.model.Security#checkPassword(java.lang.String)
	 * 
	 */
	@Override
	public boolean checkPassword(String inputedPassword) {
		LoginContext ctx = null;
		try {
			ctx = LoginModuleFactory.createContext(new OscarCallbackHandler(getUserName(), inputedPassword));
			ctx.login();
			return true;
		} catch (LoginException e) {
			throttleOnFailedLogin();
			return false;
		}
	}

}
