package org.oscarehr.PMmodule.caisi_integrator;

import java.net.ConnectException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
import javax.wsdl.WSDLException;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class CxfClientUtils {
	public static class TrustAllManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}
	};

	protected static void configureClientConnection(Object wsPort) {
		Client cxfClient = ClientProxy.getClient(wsPort);
		HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();

		configureSsl(httpConduit);
		configureTimeout(httpConduit);
	}

	private static void configureTimeout(HTTPConduit httpConduit) {
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		httpClientPolicy.setConnectionTimeout(4000);
		httpClientPolicy.setAllowChunking(false);
		httpClientPolicy.setReceiveTimeout(10000);
		
		httpConduit.setClient(httpClientPolicy);
	}

	private static void configureSsl(HTTPConduit httpConduit) {
		TLSClientParameters tslClientParameters = httpConduit.getTlsClientParameters();
		if (tslClientParameters == null) tslClientParameters = new TLSClientParameters();
		tslClientParameters.setDisableCNCheck(true);
		TrustAllManager[] tam = { new TrustAllManager() };
		tslClientParameters.setTrustManagers(tam);
		tslClientParameters.setSecureSocketProtocol("SSLv3");
		httpConduit.setTlsClientParameters(tslClientParameters);
	}

	protected static void addWSS4JAuthentication(String username, String password, Object wsPort)
	{
		Client cxfClient=ClientProxy.getClient(wsPort);
		cxfClient.getOutInterceptors().add(new AuthenticationOutWSS4JInterceptor(username, password));
	}
	
	public static boolean isConnectionException(Throwable t)
	{
		if (t!=null)
		{
			Throwable cause=t.getCause();
			if (cause!=null && cause instanceof ServiceConstructionException)
			{
				Throwable causeCause=cause.getCause();
				if (causeCause!=null && causeCause instanceof WSDLException)
				{
					Throwable causeCauseCaise=causeCause.getCause();
					if (causeCauseCaise!=null && causeCauseCaise instanceof ConnectException) return(true);
				}
			}
		}
		
		return(false);
	}
}
