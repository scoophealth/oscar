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
package org.oscarehr.myoscar.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.AccountWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.AccountWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.CalendarWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.CalendarWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.GroupWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.GroupWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.JournalWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.JournalWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonToPersonPermissionsWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.PersonToPersonPermissionsWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.ReportsWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.ReportsWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.RoleRelationshipWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.RoleRelationshipWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.SurveyWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.SurveyWsService;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.SystemInfoWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.SystemInfoWsService;
import org.oscarehr.util.ConfigXmlUtils;
import org.oscarehr.util.CxfClientUtils;
import org.oscarehr.util.CxfClientUtils.TrustAllManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

/**
 * Port Caching : ports are partially thread safe (see the CXF documentation), they are not when the configuration is altered.
 * We change the configuration on a per user bases (WSS configuration), therefore it's thread safe on a per-credential basis.
 * 
 * For the key we can use MyOscarLoggedInInfo, we won't mandate equals() & hashCode because it's more efficient to do a pointer compare.
 * If some one on the off chance has 2 credentials that are equivalent, it'll just make 2 entries in the cache, and everything works fine.
 * 
 * This class uses ConfigXmlUtils to controls some configuration parameters, they are all defaulted if not specified.
 * category=myoscar_client, property=ws_client_port_cache_size
 * category=myoscar_client, property=ws_client_port_cache_time_ms
 */
public class MyOscarServerWebServicesManager {
	private static Logger logger = MiscUtils.getLogger();
	private static int portCacheSize = ConfigXmlUtils.getPropertyInt("misc", "ws_client_port_cache_size");
	private static int portCacheTimeMs = ConfigXmlUtils.getPropertyInt("misc", "ws_client_port_cache_time_ms");

	private static QueueCache<MyOscarLoggedInInfo, AccountWs> accountWsCache = new QueueCache<MyOscarLoggedInInfo, AccountWs>(4, portCacheSize, portCacheTimeMs, null);
	private static QueueCache<MyOscarLoggedInInfo, RoleRelationshipWs> roleRelationshipWsCache = new QueueCache<MyOscarLoggedInInfo, RoleRelationshipWs>(4, portCacheSize, portCacheTimeMs, null);
	private static QueueCache<MyOscarLoggedInInfo, MessageWs> messageWsCache = new QueueCache<MyOscarLoggedInInfo, MessageWs>(4, portCacheSize, portCacheTimeMs, null);
	private static QueueCache<MyOscarLoggedInInfo, MedicalDataWs> medicalDataWsCache = new QueueCache<MyOscarLoggedInInfo, MedicalDataWs>(4, portCacheSize, portCacheTimeMs, null);

	static {
		CxfClientUtils.initSslFromConfig();
	}

	public static void configureClientConnection(Object wsPort)
	{
		Client cxfClient = ClientProxy.getClient(wsPort);
		HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();

		configureSsl(httpConduit);
		CxfClientUtils.configureTimeout(httpConduit);

		configureGzip(cxfClient);
	}

	public static void configureGzip(Client cxfClient)
	{
		cxfClient.getInInterceptors().add(new GZIPInInterceptor());
		cxfClient.getOutInterceptors().add(new GZIPOutInterceptor(0));
	}
	
	public static void configureSsl(HTTPConduit httpConduit)
	{
		TLSClientParameters tslClientParameters = httpConduit.getTlsClientParameters();
		if (tslClientParameters == null) tslClientParameters = new TLSClientParameters();
		tslClientParameters.setDisableCNCheck(true);
		TrustAllManager[] tam = { new TrustAllManager() };
		tslClientParameters.setTrustManagers(tam);
		tslClientParameters.setSecureSocketProtocol("TLS");
		httpConduit.setTlsClientParameters(tslClientParameters);
	}
	
	private static URL buildURL(String serverBaseUrl, String servicePoint) {
		String urlString = serverBaseUrl + "/ws/" + servicePoint + "?wsdl";

		logger.debug(urlString);

		try {
			return (new URL(urlString));
		} catch (MalformedURLException e) {
			logger.error("Invalid Url : " + urlString, e);
			return (null);
		}
	}

	public static SystemInfoWs getSystemInfoWs(String serverBaseUrl) {
		SystemInfoWsService service = new SystemInfoWsService(buildURL(serverBaseUrl, "SystemInfoService"));
		SystemInfoWs port = service.getSystemInfoWsPort();

		configureClientConnection(port);

		return (port);
	}

	public static LoginWs getLoginWs(String serverBaseUrl) {
		LoginWsService service = new LoginWsService(buildURL(serverBaseUrl, "LoginService"));
		LoginWs port = service.getLoginWsPort();

		configureClientConnection(port);

		return (port);
	}

	public static AccountWs getAccountWs(MyOscarLoggedInInfo credentials) {
		AccountWs port = accountWsCache.get(credentials);

		if (port == null) {
			AccountWsService service = new AccountWsService(buildURL(credentials.getServerBaseUrl(), "AccountService"));
			port = service.getAccountWsPort();

			configureClientConnection(port);
			CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);
			accountWsCache.put(credentials, port);
		}

		return (port);
	}

	public static RoleRelationshipWs getRoleRelationshipWs(MyOscarLoggedInInfo credentials) {
		RoleRelationshipWs port = roleRelationshipWsCache.get(credentials);

		if (port == null) {
			RoleRelationshipWsService service = new RoleRelationshipWsService(buildURL(credentials.getServerBaseUrl(), "RoleRelationshipService"));
			port = service.getRoleRelationshipWsPort();

			configureClientConnection(port);
			CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);
			roleRelationshipWsCache.put(credentials, port);
		}

		return (port);
	}

	public static MessageWs getMessageWs(MyOscarLoggedInInfo credentials) {
		MessageWs port = messageWsCache.get(credentials);

		if (port == null) {
			MessageWsService service = new MessageWsService(buildURL(credentials.getServerBaseUrl(), "MessageService"));
			port = service.getMessageWsPort();

			configureClientConnection(port);
			CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);
			messageWsCache.put(credentials, port);
		}

		return (port);
	}

	public static MedicalDataWs getMedicalDataWs(MyOscarLoggedInInfo credentials) {
		MedicalDataWs port = medicalDataWsCache.get(credentials);

		if (port == null) {
			MedicalDataWsService service = new MedicalDataWsService(buildURL(credentials.getServerBaseUrl(), "MedicalDataService"));
			port = service.getMedicalDataWsPort();

			configureClientConnection(port);
			CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);
			medicalDataWsCache.put(credentials, port);
		}

		return (port);
	}

	public static CalendarWs getCalendarWs(MyOscarLoggedInInfo credentials) {
		CalendarWsService service = new CalendarWsService(buildURL(credentials.getServerBaseUrl(), "CalendarService"));
		CalendarWs port = service.getCalendarWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}

	public static JournalWs getJournalWs(MyOscarLoggedInInfo credentials) {
		JournalWsService service = new JournalWsService(buildURL(credentials.getServerBaseUrl(), "JournalService"));
		JournalWs port = service.getJournalWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}

	public static SurveyWs getSurveyWs(MyOscarLoggedInInfo credentials) {
		SurveyWsService service = new SurveyWsService(buildURL(credentials.getServerBaseUrl(), "SurveyService"));
		SurveyWs port = service.getSurveyWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}

	public static GroupWs getGroupWs(MyOscarLoggedInInfo credentials) {
		GroupWsService service = new GroupWsService(buildURL(credentials.getServerBaseUrl(), "GroupService"));
		GroupWs port = service.getGroupWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}

	public static ReportsWs getReportsWs(MyOscarLoggedInInfo credentials) {
		ReportsWsService service = new ReportsWsService(buildURL(credentials.getServerBaseUrl(), "ReportsService"));
		ReportsWs port = service.getReportsWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}

	public static PersonToPersonPermissionsWs getPersonToPersonPermissionWs(MyOscarLoggedInInfo credentials) {
		PersonToPersonPermissionsWsService service = new PersonToPersonPermissionsWsService(buildURL(credentials.getServerBaseUrl(), "PersonToPersonPermissionsService"));
		PersonToPersonPermissionsWs port = service.getPersonToPersonPermissionsWsPort();

		configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(credentials.getLoggedInPersonId(), credentials.getLoggedInPersonSecurityToken(), port);

		return (port);
	}
}
