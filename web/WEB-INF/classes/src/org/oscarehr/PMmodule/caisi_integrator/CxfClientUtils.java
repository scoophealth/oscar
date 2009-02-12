package org.oscarehr.PMmodule.caisi_integrator;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.net.ssl.X509TrustManager;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
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

		httpClientPolicy.setConnectionTimeout(10000);
		httpClientPolicy.setAllowChunking(false);
		httpClientPolicy.setReceiveTimeout(15000);
		
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
	
	public static Date toDate(XMLGregorianCalendar cal)
	{
		if (cal!=null) return(cal.toGregorianCalendar().getTime());
		else return(null);
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date d) throws DatatypeConfigurationException
	{
		if (d==null) return(null);
		
		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(d);
		XMLGregorianCalendar soapCal=DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		
		return(soapCal);
	}

}
