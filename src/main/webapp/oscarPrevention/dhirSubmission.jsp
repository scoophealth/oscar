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

<%@page import="org.apache.http.impl.client.HttpClients"%>
<%@page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@page import="org.apache.http.client.config.RequestConfig"%>
<%@page import="org.apache.http.conn.ssl.SSLConnectionSocketFactory"%>
<%@page import="org.apache.http.conn.ssl.SSLContexts"%>
<%@page import="java.util.UUID"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.common.model.DHIRSubmissionLog"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.DHIRSubmissionManager"%>
<%@page import="org.hl7.fhir.dstu3.model.Immunization"%>
<%@page import="org.hl7.fhir.dstu3.model.Patient"%>
<%@page import="org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.integration.fhir.builder.AbstractFhirMessageBuilder"%>
<%@page import="org.hl7.fhir.dstu3.model.Bundle"%>
<%@page import="java.util.Map"%>
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
	DHIRSubmissionManager submissionManager = SpringUtils.getBean(DHIRSubmissionManager.class);
    		
    
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
	//logger.debug("providerEmail is " + providerEmail);
	
	String backendEconsultUrl = OscarProperties.getInstance().getProperty("backendEconsultUrl");
	
	
	String url = backendEconsultUrl + "/api/test";
	
	String uuid = request.getParameter("uuid");
	Map<String,Bundle> bundles = (Map<String,Bundle> )session.getAttribute("bundles");
	Bundle bundle = bundles.get(uuid);
	
	String demographicNo = null;
	
	List<DHIRSubmissionLog> logs= new ArrayList<DHIRSubmissionLog>();
	
	for(BundleEntryComponent bec : bundle.getEntry()) {
		if(bec.getResource().fhirType().equals("Patient")) {
	Patient patient  = (Patient)bec.getResource();
	demographicNo = patient.getId();
		}	
	}
	
	for(BundleEntryComponent bec : bundle.getEntry()) {
		if(bec.getResource().fhirType().equals("Immunization")) {
	Immunization i = (Immunization)bec.getResource();
	
	DHIRSubmissionLog log = new DHIRSubmissionLog();
	log.setDateCreated(new java.util.Date());
	log.setDemographicNo(Integer.parseInt(demographicNo));
	log.setPreventionId(Integer.parseInt(i.getId()));
	log.setStatus("Error");
	log.setSubmitterProviderNo(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
	log.setBundleId(bundle.getId());
	submissionManager.save(log);
	
	logs.add(log);
		}
	}
%>



<html:html locale="true">

<head>
<title>OSCAR Prevention Review Screen</title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>

<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}


  ////////
  div.prevention {  background-color: #999999; }
  div.prevention fieldset {width:35em; font-weight:bold; }
  div.prevention legend {font-weight:bold; }

  ////////
</style>



<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;
	   border-right: 1pt solid #888888;
           text-align: center;
	}


label{
float: left;
width: 120px;
font-weight: bold;
}

label.checkbox{
float: left;
width: 116px;
font-weight: bold;
}

label.fields{
float: left;
width: 80px;
font-weight: bold;
}

span.labelLook{
font-weight:bold;

}

input, textarea,select{

//margin-bottom: 5px;
}

textarea{
width: 450px;
height: 100px;
}


.boxes{
width: 1em;
}

#submitbutton{
margin-left: 120px;
margin-top: 5px;
width: 90px;
}

br{
clear: left;
}
</style>


</head>

<body class="BodyStyle" vlink="#0000FF" onload="disableifchecked(document.getElementById('neverWarn'),'nextDate');">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
              DHIR Submission
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            
                        </td>
                        <td  >&nbsp;

                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="prevention" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
               &nbsp;
            </td>
            <td valign="top" class="MainTableRightColumn">
            
<%
            	try {	
            		String theString = AbstractFhirMessageBuilder.getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
            		JSONObject jbundle = new JSONObject(theString);
            		
            		String clientRequestId = UUID.randomUUID().toString();

          		
                    HttpPost httpPost = new HttpPost(url);
                    
                    String oneIdToken = (String)session.getAttribute("oneid_token");
                    logger.debug("oneid_token is " + oneIdToken);
                    
                    httpPost.addHeader("x-oneid-email", providerEmail);
                    httpPost.addHeader("x-access-token", oneIdToken);
                    
                    JSONObject obj = new JSONObject();
                    obj.put("url",OscarProperties.getInstance().getProperty("dhir.url"));
                    obj.put("service","DHIR");
                    obj.put("body",jbundle);
                    obj.put("client-request-id",clientRequestId);
                    obj.put("client-app-desc","EMR");
                    
                    HttpEntity reqEntity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
                    httpPost.setEntity(reqEntity);
                    httpPost.setHeader("Content-type", "application/json");
                    
                    HttpClient httpClient = getHttpClient2();
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    String entity = EntityUtils.toString(httpResponse.getEntity());
                    
                    JSONObject object = new JSONObject(entity);
                    logger.info("object="+object.toString());
                    
                    Integer code = (Integer)object.get("code");
                    
                    if(code >= 200 && code < 300) { 
                    	String val = null;
                    	String clientId = null;
                    	if(object != null) {
                    		JSONObject headers = (JSONObject)object.get("headers");
                    		try {
                    			val = (String)headers.get("hialTxId");
                    		}catch(JSONException je) {
                    			
                    		}
                    		try {
                    			clientId = (String)headers.get("client-response-id");
                    		}catch(JSONException je) {
                    			
                    		}
                    	}
                    	
                    	for(DHIRSubmissionLog log : logs) {
                    		log.setStatus("Submitted");
                    		log.setTransactionId(val);
                    		log.setClientResponseId(clientId);
                    		log.setClientRequestId(clientRequestId);
                    		submissionManager.update(log);
                    	}
            %>
        		<h2>Submission send for review. You may find the reference number in the prevention's Summary field. </h2>
        		<input type="button" value="Close Window" onClick="window.close()"/>
        	<%
        }
        
        
        if((code >=300 && code < 400) || code == 500) {
        	String val = null;
        	String clientId = null;
        	
        	if(object != null) {
        		JSONObject headers = (JSONObject)object.get("headers");
        		try {
        			val = (String)headers.get("hialTxId");
        		}catch(JSONException je) {
        			
        		}
        		try {
        			clientId = (String)headers.get("client-response-id");
        		}catch(JSONException je) {
        			
        		}
        	}
        	
        	
        	for(DHIRSubmissionLog log : logs) {
        		log.setStatus("Error");
        		log.setTransactionId(val);
        		log.setResponse(entity != null ? entity : "");
        		log.setClientResponseId(clientId);
        		log.setClientRequestId(clientRequestId);
        		submissionManager.update(log);
        	}
        	
        	%>
        		<h2>There was an error sending the message. You can try resubmitting.</h2>
        		<h3>HTTP Code <%=code%></h3>
        		<h3>Check logs for more information</h3>
        		<input type="button" value="Retry" onClick="window.location.reload()"/>
        		&nbsp;
        		<input type="button" value="Close Window" onClick="window.close()"/>
        	<%
        }
        
        if(code >=400 && code < 500) {
        	String val = null;
        	String clientId = null;
        	if(object != null) {
        		JSONObject headers = (JSONObject)object.get("headers");
        		try {
        			val = (String)headers.get("hialTxId");
        		}catch(JSONException je) {
        			
        		}
        		try {
        			clientId = (String)headers.get("client-response-id");
        		}catch(JSONException je) {
        			
        		}
        	}
        	
        	for(DHIRSubmissionLog log : logs) {
        		log.setStatus("Error");
        		log.setTransactionId(val);
        		log.setResponse(entity != null ? entity : "");
        		log.setClientResponseId(clientId);
        		log.setClientRequestId(clientRequestId);
        		submissionManager.update(log);
        	}
        	
        	%>
        		<h2>There was an error sending the message. DHIR will not accept this message.</h2>
        		<h3>HTTP Code <%=code%></h3>
        		<h3>Check logs for more information</h3>
        		
        		<input type="button" value="Close Window" onClick="window.close()"/>
        	<%
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
   
	bundles.put(uuid,null);


%>

 </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>

</body>
</html:html>



<%!

protected HttpClient getHttpClient2() throws Exception {

    //setup SSL
    SSLContext sslcontext = SSLContexts.custom().useTLS().build();
    sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
    sslcontext.getDefaultSSLParameters().setWantClientAuth(true);
    SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);

    //setup timeouts
    int timeout = Integer.parseInt(OscarProperties.getInstance().getProperty("dhir.timeout", "60"));
    RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).build();

    CloseableHttpClient httpclient3 = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sf).build();

    return httpclient3;

}


%>
