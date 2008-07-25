package org.oscarehr.PMmodule.caisi_integrator;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

public class SslUtils
{
	public static class TrustAllManager implements X509TrustManager
	{
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[0];
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
		}
	};
	
	public static void configureSsl(Object wsPort)
    {
	    Client cxfClient = ClientProxy.getClient(wsPort);
		HTTPConduit httpConduit = (HTTPConduit)cxfClient.getConduit();

		TLSClientParameters tslClientParameters = httpConduit.getTlsClientParameters();
		if (tslClientParameters == null) tslClientParameters = new TLSClientParameters();
		tslClientParameters.setDisableCNCheck(true);
		TrustAllManager[] tam = { new TrustAllManager() };
		tslClientParameters.setTrustManagers(tam);
		tslClientParameters.setSecureSocketProtocol("SSL");
		httpConduit.setTlsClientParameters(tslClientParameters);
    }
}
