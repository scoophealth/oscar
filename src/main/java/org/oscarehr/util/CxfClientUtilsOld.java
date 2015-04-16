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


package org.oscarehr.util;

import java.io.IOException;
import java.net.ConnectException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.X509TrustManager;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.wsdl.WSDLException;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

import oscar.OscarProperties;

/**
 * This file has been renamed to "Old" because this file should no longer be enhanced. A common version of this class
 * is made available from the Utils package. There maybe some methods left here which don't entirely make sense
 * or don't make sense in the context of a general purpose project agnostic utility class. This class still exists as "Old" so
 * we can slowly refactor the non sensical code to use the new common utilities. Any remaining methods which do make sense
 * should then me moved to a generic Oscar Utility class or similar. If the method makes sense in a project
 * agnostic fashion, then it should be moved to the util project itself. 
 */
public class CxfClientUtilsOld
{
	private static long connectionTimeout=Long.parseLong(OscarProperties.getInstance().getProperty("web_service_client.connection_timeout_ms"));
	private static long receiveTimeout=Long.parseLong(OscarProperties.getInstance().getProperty("web_service_client.received_timeout_ms"));
	
	public static class TrustAllManager implements X509TrustManager
	{
		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
			// trust all no work required
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
			// trust all no work required
		}
	}

	public static class GenericPasswordCallbackHandler implements CallbackHandler
	{
		private String password;

		public GenericPasswordCallbackHandler(String password)
		{
			this.password = password;
		}

		@Override
		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
		{
			for (Callback callback : callbacks)
			{
				if (callback instanceof WSPasswordCallback)
				{
					WSPasswordCallback wsPasswordCallback = (WSPasswordCallback)callback;
					wsPasswordCallback.setPassword(password);

					break;
				}
			}
		}
	}

	public static void configureClientConnection(Object wsPort)
	{
		Client cxfClient = ClientProxy.getClient(wsPort);
		HTTPConduit httpConduit = (HTTPConduit)cxfClient.getConduit();

		configureSsl(httpConduit);
		configureTimeout(httpConduit);
		
		if(OscarProperties.getInstance().getProperty("INTEGRATOR_COMPRESSION_ENABLED","false").equals("true")) {
			configureGZIP(cxfClient);
		}
	
		if(OscarProperties.getInstance().getProperty("INTEGRATOR_LOGGING_ENABLED","false").equals("true")) {
			configureLogging(cxfClient,true);
		}

	}
	
	public static void configureLogging(Client cxfClient,boolean x)
	{
		cxfClient.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		cxfClient.getEndpoint().getInFaultInterceptors().add(new LoggingInInterceptor());
	}

	private static void configureGZIP(Client client)
	{
		client.getInInterceptors().add(new GZIPInInterceptor());
		client.getOutInterceptors().add(new GZIPOutInterceptor());
		client.getBus().getFeatures().add(new GZIPFeature());
	}

	public static void configureLogging(Object wsPort)
	{
		Client cxfClient = ClientProxy.getClient(wsPort);
		cxfClient.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		cxfClient.getEndpoint().getInFaultInterceptors().add(new LoggingInInterceptor());
	}

	public static void configureWSSecurity(Object wsPort, String user, String password)
	{
		configureWSSecurity(wsPort, user, new GenericPasswordCallbackHandler(password));
	}

	public static <T extends CallbackHandler> void configureWSSecurity(Object wsPort, String user, T passwordCallbackInstance)
	{
		Client cxfClient = ClientProxy.getClient(wsPort);
		Endpoint cxfEndpoint = cxfClient.getEndpoint();

		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);

		outProps.put(WSHandlerConstants.USER, user);
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, passwordCallbackInstance);

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);
	}

	private static void configureTimeout(HTTPConduit httpConduit)
	{
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		httpClientPolicy.setConnectionTimeout(connectionTimeout);
		httpClientPolicy.setAllowChunking(false);
		httpClientPolicy.setReceiveTimeout(receiveTimeout);

		httpConduit.setClient(httpClientPolicy);
	}

	private static void configureSsl(HTTPConduit httpConduit)
	{
		TLSClientParameters tslClientParameters = httpConduit.getTlsClientParameters();
		if (tslClientParameters == null) tslClientParameters = new TLSClientParameters();
		tslClientParameters.setDisableCNCheck(true);
		TrustAllManager[] tam = {new TrustAllManager()};
		tslClientParameters.setTrustManagers(tam);

		String sslProtocol = ConfigXmlUtils.getPropertyString("misc", "ws_client_ssl_protocol");
		tslClientParameters.setSecureSocketProtocol(sslProtocol);
		
		httpConduit.setTlsClientParameters(tslClientParameters);
	}

	public static boolean isConnectionException(Throwable t)
	{
		if (t != null)
		{
			Throwable cause = t.getCause();
			if (cause != null && cause instanceof ServiceConstructionException)
			{
				Throwable causeCause = cause.getCause();
				if (causeCause != null && causeCause instanceof WSDLException)
				{
					Throwable causeCauseCaise = causeCause.getCause();
					if (causeCauseCaise != null && causeCauseCaise instanceof ConnectException) return(true);
				}
			}
		}

		return(false);
	}
}
