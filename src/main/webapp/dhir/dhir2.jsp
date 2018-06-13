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
<%@page import="org.apache.http.impl.conn.PoolingClientConnectionManager"%>
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
<%@page import="org.apache.http.client.methods.HttpGet" %>
<%@page import="javax.servlet.http.Cookie" %>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.apache.http.client.HttpClient" %>
<%@page import="org.apache.http.HttpResponse" %>
<%@page import="org.codehaus.jettison.json.*" %>


<%
	Logger logger = MiscUtils.getLogger();

	OscarProperties oscarProperties = OscarProperties.getInstance();

	String oneIdEmail = session.getAttribute("oneIdEmail") != null ? session.getAttribute("oneIdEmail").toString() : "";

	String delegateOneIdEmail = session.getAttribute("delegateOneIdEmail") != null
			? session.getAttribute("delegateOneIdEmail").toString() : "";
	String providerEmail = oneIdEmail;

	//If there is a delegateOneIdEmail then it is used as the normal oneId email and the current user is the delegate as they are delegating for that person
	if (!delegateOneIdEmail.equals("")) {
		providerEmail = delegateOneIdEmail;
	}
	String backendEconsultUrl = "https://138.197.167.236";
	
	String url = backendEconsultUrl + "/api/test3";
	
	
	try {
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("bis4.json");
		String theString = IOUtils.toString(inputStream); 
	//	logger.info("theString=" + theString);
		JSONObject bundle = new JSONObject(theString);
		
				
        HttpPost httpPost = new HttpPost(url);
        
        String oneIdToken = (String)session.getAttribute("oneid_token");
        
        logger.info("oneid_token is " + oneIdToken);
        
        httpPost.addHeader("x-oneid-email", providerEmail);
        httpPost.addHeader("x-access-token", oneIdToken);
        
        JSONObject obj = new JSONObject();
        obj.put("url","https://76.75.149.140:9443/API/FHIR/BORNDenominator/v1/Communication");
        obj.put("service","DHIR");
        obj.put("body",theString);
        
        HttpEntity reqEntity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
        httpPost.setEntity(reqEntity);
        httpPost.setHeader("Content-type", "application/json+fhir");
        
        HttpClient httpClient = getHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        //Gets the entity from the response and stores it as a JSONObject
        String entity = EntityUtils.toString(httpResponse.getEntity());
        //logger.info("entity=" + entity);
        
        
        JSONObject object = new JSONObject(entity);
       
        logger.info(object.toString());
        
        Integer code = (Integer)object.get("code");
        logger.info("code="+code);
        
        out.println("<h2>Code=" + code + "</h2>");
         
        if(object.get("data") instanceof String) {
        //Gets the data and then entry sections of the response entity
        JSONObject data = new JSONObject((String)object.get("data"));
        
        
       
        
        if(data != null) {
        
        %><%=((JSONObject)data.get("text")).get("div")%><%
        
        }
        }
        
    }
    catch (IOException e) {
        logger.error("Failed to retrieve eConsults for the OneID account " + providerEmail, e);
    }
    catch (NoSuchAlgorithmException e) {
        logger.error("Failed to create an HttpClient that allows all SSL", e);
    }
    catch (KeyManagementException e) {
    	 logger.error("Failed to create an HttpClient that allows all SSL", e);
    }
   

%>

<%!

private HttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
    //Gets the SSLContext instance for SSL and initializes it
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, new TrustManager[] {new CxfClientUtils.TrustAllManager()}, new SecureRandom());
    //Creates a new SocketFactory to bypass the SSL verifiers
    SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    //Makes a new SchemeRegistry using the SocketFactory so that HTTPS ssl is bypassed
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("https", 443, sf));
    //Creates a new ClientConnectionManager with the registry and creates the httpClient to use
    ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

    return new DefaultHttpClient(ccm);
}
%>
