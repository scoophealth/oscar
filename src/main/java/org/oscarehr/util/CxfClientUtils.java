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
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

public class CxfClientUtils
{
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

		httpClientPolicy.setConnectionTimeout(4000);
		httpClientPolicy.setAllowChunking(false);
		httpClientPolicy.setReceiveTimeout(10000);

		httpConduit.setClient(httpClientPolicy);
	}

	private static void configureSsl(HTTPConduit httpConduit)
	{
		TLSClientParameters tslClientParameters = httpConduit.getTlsClientParameters();
		if (tslClientParameters == null) tslClientParameters = new TLSClientParameters();
		tslClientParameters.setDisableCNCheck(true);
		TrustAllManager[] tam = {new TrustAllManager()};
		tslClientParameters.setTrustManagers(tam);
		tslClientParameters.setSecureSocketProtocol("SSLv3");
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
