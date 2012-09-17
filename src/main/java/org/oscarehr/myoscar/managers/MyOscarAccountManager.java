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

package org.oscarehr.myoscar.managers;

import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.ws.LoginResultTransfer;
import org.oscarehr.myoscar_server.ws.LoginWs;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.util.MiscUtils;

public final class MyOscarAccountManager
{
	private static final Logger logger = MiscUtils.getLogger();

	public static final String MYOSCAR_CLIENT_PREFERENCE_PREFIX = "MyOscarClient.";

	/**
	 * @return the logged in person or null if invalid login.
	 */
	public static LoginResultTransfer login(String userName, String password) throws NotAuthorisedException_Exception
	{
		try
		{
			LoginWs loginWs = MyOscarServerWebServicesManager.getLoginWs();
			LoginResultTransfer loginResultTransfer = loginWs.login2(userName, password);
			return(loginResultTransfer);
		}
		catch (NotAuthorisedException_Exception e)
		{
			logger.warn("Invalid Login Request userName=" + userName);
			throw(e);
		}
	}	
}
