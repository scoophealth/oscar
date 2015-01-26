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

package org.oscarehr.ws;

import java.lang.management.ManagementFactory;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public final class WsUtils {
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

	/**
	 * This method will check to see if the person is allowed to login, i.e. it will check username/expiry/password.
	 * If the person is allowed it will setup the loggedInInfo data.
	 * 
	 * @param security can be null, it will return false for null. 
	 * @param securityToken can be the SecurityId's password, or a valid securityToken.
	 */
	public static boolean checkAuthenticationAndSetLoggedInInfo(HttpServletRequest request, Security security, String securityToken) {
		if (security != null) {
			if (security.getDateExpiredate() != null && security.getDateExpiredate().before(new Date())) return (false);

			if (checkToken(security, securityToken) || security.checkPassword(securityToken)) {
				LoggedInInfo loggedInInfo = new LoggedInInfo();
				loggedInInfo.setLoggedInSecurity(security);
				if (security.getProviderNo() != null) {
					loggedInInfo.setLoggedInProvider(providerDao.getProvider(security.getProviderNo()));
				}

				LoggedInInfo.setLoggedInInfoIntoRequest(request, loggedInInfo);
				return (true);
			}
		}

		return (false);
	}

	private static boolean checkToken(Security security, String securityToken) {
		return (securityToken.equals(generateSecurityToken(security)));
	}

	public static String generateSecurityToken(Security security) {
		// we'll add a randomish seed to a deterministic value unique to the user.
		// the randomish seed can be the jvm start time
		// the deterministic value unique to the user is a mostly unique rendition of their 
		// password.
		
		long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		long mangling = getStringAsLongRendition(security.getPassword());
		return (String.valueOf(jvmStartTime + mangling));
	}

	/**
	 * The purpose of this method is to generate a long from the given string.
	 * It doesn't have to be anything in particular other than being deterministic 
	 * and relatively unique (no uniqueness guarantees required). 
	 */
	private static long getStringAsLongRendition(String s) {
		long result = 0;
		int modMax = 6;
		int mod = 0;

		for (char c : s.toCharArray()) {
			result = result + c * (long) Math.pow(100, mod);

			mod = (mod + 1) % modMax;
		}

		return (result);
	}
}
