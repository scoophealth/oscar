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
package org.oscarehr.security.jaas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;

import oscar.OscarProperties;
import oscar.login.jaas.LdapLoginModule;
import oscar.login.jaas.LoginModuleFactory;
import oscar.login.jaas.OscarCallbackHandler;
import oscar.login.jaas.OscarConfiguration;
import oscar.login.jaas.OscarPrincipal;

public class LdapLoginModuleTest extends DaoTestFixtures {

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("security", "provider", "secUserRole");
	}

	@Test
	public void runTests() throws Exception {
		if (!OscarProperties.isLdapAuthenticationEnabled()) return;

		testLogin();
		testLoginModule();
		testAuthMethods();
	}

	/**
	 * Tests just the login module
	 */
	public void testLogin() throws Exception {
		// test direct auth first 
		LdapLoginModule lm = new LdapLoginModule();
		Map<String, Object> config = new HashMap<String, Object>();
		config.put(LdapLoginModule.OPTION_BASE_DN, OscarProperties.getInstance().getProperty("ldap.baseDn"));
		config.put(LdapLoginModule.OPTION_LDAP_URL, OscarProperties.getInstance().getProperty("ldap.url"));

		lm.initialize(new Subject(), new OscarCallbackHandler("oscardoc", "mac2002"), new HashMap<String, Object>(), config);
		boolean isSuccess = lm.login();
		assertTrue(isSuccess);
	}

	/**
	 * Tests end-to-end auth process
	 */
	public void testLoginModule() throws LoginException {
		// now test the end-to-end login
		LoginContext loginContext = LoginModuleFactory.createContext(new OscarCallbackHandler("oscardoc", "mac2002"));

		try {
			loginContext.login();
		} catch (LoginException e) {
			fail();
		}

		Subject subject = loginContext.getSubject();
		assertFalse(subject.getPrincipals().isEmpty());
		assertFalse(subject.getPrincipals(OscarPrincipal.class).isEmpty());

		OscarPrincipal provider = subject.getPrincipals(OscarPrincipal.class).iterator().next();
		assertEquals("oscardoc", provider.getName());

		try {
			loginContext.logout();
		} catch (LoginException e) {
			fail();
		}

		subject = loginContext.getSubject();
		assertTrue(subject.getPrincipals().isEmpty());
		assertTrue(subject.getPrincipals(OscarPrincipal.class).isEmpty());

		loginContext = LoginModuleFactory.createContext(new OscarCallbackHandler("non_existent_user_name", "password"));
		try {
			loginContext.login();
			fail();
		} catch (LoginException e) {
			// invalid user name!
		}

	}

	/**
	 * Tests various authentication methods
	 */
	public void testAuthMethods() {
		// null should the same as "simple"
		for (String authMethod : new String[] { null, "simple", "DIGEST-MD5" }) {
			OscarConfiguration config = (OscarConfiguration) Configuration.getConfiguration();
			config.setOption(LdapLoginModule.OPTION_AUTH_METHOD, authMethod);

			// now test the end-to-end login	
			try {
				LoginContext loginContext = LoginModuleFactory.createContext(new OscarCallbackHandler("oscardoc", "mac2002"));
				loginContext.login();
				loginContext.logout();
			} catch (LoginException e) {
				fail();
			}
		}
	}

}
