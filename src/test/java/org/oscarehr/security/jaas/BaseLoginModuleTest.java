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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;

import org.junit.Test;

import oscar.login.jaas.BaseLoginModule;
import oscar.login.jaas.OscarCallbackHandler;
import oscar.login.jaas.OscarConfiguration;
import oscar.login.jaas.OscarPrincipal;

public class BaseLoginModuleTest {

	@Test
	public void testBaseLoginModule() throws Exception {
		// System.setProperty("java.security.auth.login.config", "loginConfig.jaas");
		Configuration.setConfiguration(new OscarConfiguration("dummyConfig", TestLoginModule.class.getName()));
		
		LoginContext loginContext = new LoginContext("dummyConfig", new OscarCallbackHandler("dummy", "pass"));
		try {
			loginContext.login();
		} catch (Exception e) {
			fail();
		}

		Subject subject = loginContext.getSubject();
		assertFalse(subject.getPrincipals().isEmpty());
		assertFalse(subject.getPrincipals(OscarPrincipal.class).isEmpty());

		try {
			loginContext.logout();
		} catch (Exception e) {
			fail();
		}

		loginContext = new LoginContext("dummyConfig", new OscarCallbackHandler("dummy2", "pass2"));
		try {
			loginContext.login();
			fail();
		} catch (Exception e) {
			// that's expected
		}

		assertNull(loginContext.getSubject());

		try {
			loginContext.logout();
			fail();
		} catch (Exception e) {
			// that's expected
		}

	}

	/**
	 * This is a dummy login module that authenticates only one subject with hardcoded credentials.
	 */
	public static final class TestLoginModule extends BaseLoginModule {
		@Override
		protected OscarPrincipal authenticate(String loginName, char[] password) {
			if ("dummy".equalsIgnoreCase(loginName) && Arrays.equals("pass".toCharArray(), password)) {
				return new OscarPrincipal();
			}
			return null;
		}

	}

}
