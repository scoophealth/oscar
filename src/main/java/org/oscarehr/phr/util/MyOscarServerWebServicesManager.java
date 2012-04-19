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


package org.oscarehr.phr.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.AccountWsService;
import org.oscarehr.myoscar_server.ws.LoginWs;
import org.oscarehr.myoscar_server.ws.LoginWsService;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.myoscar_server.ws.MedicalDataWsService;
import org.oscarehr.myoscar_server.ws.MessageWs;
import org.oscarehr.myoscar_server.ws.MessageWsService;
import org.oscarehr.myoscar_server.ws.SurveyWs;
import org.oscarehr.myoscar_server.ws.SurveyWsService;
import org.oscarehr.myoscar_server.ws.SystemInfoWs;
import org.oscarehr.myoscar_server.ws.SystemInfoWsService;
import org.oscarehr.util.CxfClientUtils;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public class MyOscarServerWebServicesManager
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final String myOscarServerBaseUrl = (String)OscarProperties.getInstance().get("myoscar_server_base_url");
	
	public static String getMyOscarServerBaseUrl()
	{
		return(myOscarServerBaseUrl);
	}
	
	private static URL buildURL(String servicePoint)
	{
		String urlString = myOscarServerBaseUrl + '/' + servicePoint + "?wsdl";

		logger.debug(urlString);

		try
		{
			return(new URL(urlString));
		}
		catch (MalformedURLException e)
		{
			logger.error("Invalid Url : " + urlString, e);
			return(null);
		}
	}

	public static SystemInfoWs getSystemPropertiesWs()
	{
		SystemInfoWsService service = new SystemInfoWsService(buildURL("SystemInfoService"));
		SystemInfoWs port = service.getSystemInfoWsPort();

		CxfClientUtils.configureClientConnection(port);

		return(port);
	}

	public static LoginWs getLoginWs()
	{
		LoginWsService service = new LoginWsService(buildURL("LoginService"));
		LoginWs port = service.getLoginWsPort();

		CxfClientUtils.configureClientConnection(port);

		return(port);
	}

	public static AccountWs getAccountWs(Long userId, String password)
	{
		AccountWsService service = new AccountWsService(buildURL("AccountService"));
		AccountWs port = service.getAccountWsPort();

		CxfClientUtils.configureClientConnection(port);
		CxfClientUtils.configureWSSecurity(port, userId.toString(), password);

		return(port);
	}

	public static MessageWs getMessageWs(Long userId, String password)
	{
		MessageWsService service = new MessageWsService(buildURL("MessageService"));
		MessageWs port = service.getMessageWsPort();

		CxfClientUtils.configureClientConnection(port);
		CxfClientUtils.configureWSSecurity(port, userId.toString(), password);

		return(port);
	}

	public static MedicalDataWs getMedicalDataWs(Long userId, String password)
	{
		MedicalDataWsService service = new MedicalDataWsService(buildURL("MedicalDataService"));
		MedicalDataWs port = service.getMedicalDataWsPort();

		CxfClientUtils.configureClientConnection(port);
		CxfClientUtils.configureWSSecurity(port, userId.toString(), password);

		return(port);
	}

	public static SurveyWs getSurveyWs(Long userId, String password)
	{
		SurveyWsService service = new SurveyWsService(buildURL("SurveyService"));
		SurveyWs port = service.getSurveyWsPort();

		CxfClientUtils.configureClientConnection(port);
		CxfClientUtils.configureWSSecurity(port, userId.toString(), password);

		return(port);
	}
}
