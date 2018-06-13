<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@page import="org.apache.http.client.methods.HttpPut"%>
<%@page import="org.apache.http.impl.client.HttpClients"%>
<%@page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@page import="org.apache.http.conn.ssl.SSLConnectionSocketFactory"%>
<%@page import="org.apache.http.conn.ssl.SSLContexts"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.security.KeyStore"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="com.sun.codemodel.fmt.JSerializedObject"%>
<%@page import="org.apache.http.entity.ByteArrayEntity"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="java.io.IOException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.http.util.EntityUtils"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page
	import="org.apache.http.impl.conn.PoolingClientConnectionManager"%>
<%@page import="org.apache.http.conn.scheme.Scheme"%>
<%@page import="org.apache.http.conn.ssl.SSLSocketFactory"%>
<%@page import="javax.net.ssl.TrustManager"%>
<%@page import="org.apache.http.conn.ClientConnectionManager"%>
<%@page import="org.apache.http.conn.scheme.SchemeRegistry"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="org.oscarehr.util.CxfClientUtils"%>
<%@page import="javax.net.ssl.SSLContext"%>
<%@page import="java.security.KeyManagementException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="javax.servlet.http.Cookie"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.apache.http.client.HttpClient"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.codehaus.jettison.json.*"%>


<%
	Logger logger = MiscUtils.getLogger();

	OscarProperties oscarProperties = OscarProperties.getInstance();

	String oneIdEmail = session.getAttribute("oneIdEmail") != null
			? session.getAttribute("oneIdEmail").toString() : "";

	String delegateOneIdEmail = session.getAttribute("delegateOneIdEmail") != null
			? session.getAttribute("delegateOneIdEmail").toString() : "";
	String providerEmail = oneIdEmail;

	//If there is a delegateOneIdEmail then it is used as the normal oneId email and the current user is the delegate as they are delegating for that person
	if (!delegateOneIdEmail.equals("")) {
		providerEmail = delegateOneIdEmail;
	}
	String backendEconsultUrl = OscarProperties.getInstance().getProperty("backendEconsultUrl");

	String url = backendEconsultUrl + "/api/contextSessionId";

	//https://federationbroker.ehealthontario.ca/fed/idp/initiatesso?providerid=OpenSSOFedletSPForCCInDEV

	HttpGet httpGet = new HttpGet(url);
	String oneIdToken = (String) session.getAttribute("oneid_token");

	logger.info("oneid_token is " + oneIdToken);

	httpGet.addHeader("x-oneid-email", providerEmail);
	httpGet.addHeader("x-access-token", oneIdToken);

	HttpClient httpClient = getHttpClient();
	HttpResponse httpResponse = httpClient.execute(httpGet);

	String contextSessionID = null;

	if (httpResponse.getStatusLine().getStatusCode() == 200) {
		String entity = EntityUtils.toString(httpResponse.getEntity());
		JSONObject obj = new JSONObject(entity);
		contextSessionID = (String) obj.get("contextSessionID");
		logger.info("contextSessionID = " + contextSessionID);
	}

	//UPDATE CMS
	if (contextSessionID != null) {

		logger.info("contextSessionID=" + contextSessionID);

		try {
										
			HttpPut httpPut = new HttpPut("https://cmsdev.hhsc.ca/ContextMGMT/ContextMGMT.svc/json/ContextInfo");

			httpPut.addHeader("User-Agent", "Java/OSCAR");
			httpPut.addHeader("Content-Type", "application/json");

			JSONObject cms = new JSONObject();
			cms.put("contextSessionID", contextSessionID);
			cms.put("contextTopic", "patientContext");
			cms.put("patientContext.Identifier1.type", "JHN");
			cms.put("patientContext.Identifier1.value", "1111666663");
			cms.put("patientContext.Identifier1.system",
					"http://ehealthontario.ca/fhir/NamingSystem/id-registration-and-claims-branch-def-source");

			//	cms.put("creationTime","");
			//	cms.put("version","");
			//	cms.put("status","");
			//cms.put("patientContext.birthDate","");
			//cms.put("patientContext.gender","");

			String theString = cms.toString();

			HttpEntity reqEntity = new ByteArrayEntity(theString.getBytes("UTF-8"));
			httpPut.setEntity(reqEntity);

			HttpClient httpClient2 = getHttpClient2();
			HttpResponse httpResponse2 = httpClient2.execute(httpPut);
			//Gets the entity from the response and stores it as a JSONObject
			String entity2 = EntityUtils.toString(httpResponse2.getEntity());

			logger.info("statusCode=" + httpResponse2.getStatusLine().getStatusCode());
			logger.info("entity=" + entity2);
			
			JSONObject resp = new JSONObject(entity2);
			//contextSessionID
			//creationTime="5/15/2018 4:39:52 PM"
			//status=Success
			
			//(String)resp.get("contextSessionID");
			
			//redirect user to https://federationbroker.ehealthontario.ca/fed/idp/initiatesso?providerid=OpenSSOFedletSPForCCInDEV
					
			response.sendRedirect("https://federationbroker.ehealthontario.ca/fed/idp/initiatesso?providerid=OpenSSOFedletSPForCCInDEV");

		} catch (IOException e) {
			logger.error("Failed to retrieve eConsults for the OneID account ", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Failed to create an HttpClient that allows all SSL", e);
		} catch (KeyManagementException e) {
			logger.error("Failed to create an HttpClient that allows all SSL", e);
		} catch (Exception e) {
			logger.error("Boom ", e);
		}

	}

%>

<%!private HttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new TrustManager[] { new CxfClientUtils.TrustAllManager() }, new SecureRandom());
		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("https", 443, sf));
		ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

		return new DefaultHttpClient(ccm);
	}

	private HttpClient getHttpClient2() throws Exception {

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("/home/marc/Downloads/ClinicalConnect/ClinicalConnect.jks"),
				"Oscar123".toCharArray());

		//setup SSL
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(ks, "Oscar123".toCharArray()).build();
		sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
		sslcontext.getDefaultSSLParameters().setWantClientAuth(true);

		SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext, null, null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		CloseableHttpClient httpclient3 = HttpClients.custom().setSSLSocketFactory(sf).build();

		return httpclient3;

	}%>
