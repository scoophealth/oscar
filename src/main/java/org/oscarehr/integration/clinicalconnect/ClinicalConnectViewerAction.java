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
package org.oscarehr.integration.clinicalconnect;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;

public class ClinicalConnectViewerAction extends DispatchAction {

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	Logger logger = MiscUtils.getLogger();
	
	private void addError(String demographicNo, String errorMessage, List<String> errorList, HttpServletRequest request, String contextSessionID) {
		errorList.add(errorMessage);
		request.setAttribute("errors", errorList);	
		String prefix = contextSessionID;
		if(prefix == null) {
			prefix = "";
		}
		LogAction.addLog(LoggedInInfo.getLoggedInInfoFromSession(request),"Launch CMS EHR Viewer","launch","error",demographicNo,prefix + ":" + errorMessage);
	}
	
	public ActionForward launch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNoStr = request.getParameter("demographicNo");
		
		List<String> errors = new ArrayList<String>();
		
		Demographic demographic = demographicDao.getDemographic(demographicNoStr);
		
		if(demographic == null) {
			addError(demographicNoStr,"Unable to find patient record",errors,request,null);
			return mapping.findForward("error");
		}
		
		String oneIdToken = (String) request.getSession().getAttribute("oneid_token");	
		if(StringUtils.isEmpty(oneIdToken)) {
			addError(demographicNoStr,"Unable to retrieve OneId Token",errors,request,null);
			return mapping.findForward("error");
		}
		
		//get ContextSessionID
		String contextSessionID = retrieveContextSessionId(oneIdToken);
		if(contextSessionID == null) {
			addError(demographicNoStr,"Unable to retrieve contextSessionID",errors,request,null);
			return mapping.findForward("error");
		}
		
		//UPDATE CMS		
		try {
					
			String hcn = demographic.getHin();
			
			if(StringUtils.isEmpty(hcn)) {
				addError(demographicNoStr,"Patient does not have a Health Card #",errors,request,contextSessionID);
				return mapping.findForward("error");
			}
			
			if(!Check(hcn)) {
				addError(demographicNoStr,"Patient does not have a valid Health Card #",errors,request,contextSessionID);
				return mapping.findForward("error");
			}
			
			String cmsURL = OscarProperties.getInstance().getProperty("clinicalConnect.CMS.url");
			String redirectURL = OscarProperties.getInstance().getProperty("clinicalConnect.redirectUrl");
			
			logger.debug("CMS URL = " + cmsURL);
			HttpPut httpPut = new HttpPut(cmsURL);

			httpPut.addHeader("User-Agent", "Java/OSCAR");
			httpPut.addHeader("Content-Type", "application/json");

			JSONObject cms = new JSONObject();
			cms.put("contextSessionID", contextSessionID);
			cms.put("contextTopic", "patientContext");
			cms.put("patientContext.Identifier1.type", "JHN");
			cms.put("patientContext.Identifier1.value", hcn);
			cms.put("patientContext.Identifier1.system",
					"http://ehealthontario.ca/fhir/NamingSystem/id-registration-and-claims-branch-def-source");
			String theString = cms.toString();
			HttpEntity reqEntity = new ByteArrayEntity(theString.getBytes("UTF-8"));
			httpPut.setEntity(reqEntity);

			HttpClient httpClient2 = getHttpClient2();
			HttpResponse httpResponse2 = httpClient2.execute(httpPut);
			
			if( httpResponse2.getStatusLine().getStatusCode() >= 200 &&  httpResponse2.getStatusLine().getStatusCode() < 300) {
				String entity2 = EntityUtils.toString(httpResponse2.getEntity());
				JSONObject resp = new JSONObject(entity2);
				
				if(resp.getString("contextSessionID").equals(contextSessionID)) {
					logger.info("successfully set contextSessionID in CMS");
		
					request.getSession().setAttribute("CC_EHR_LOADED", true);
					
					LogAction.addLog(LoggedInInfo.getLoggedInInfoFromSession(request),"Launch CMS EHR Viewer","launch","success",demographicNoStr,entity2);
			
					response.sendRedirect(redirectURL);
					
					return null;

				} else {
					addError(demographicNoStr,"Could not update Patient Context for EHR viewer",errors,request,contextSessionID);
					LogAction.addLog(LoggedInInfo.getLoggedInInfoFromSession(request),"Launch CMS EHR Viewer","launch","error",demographicNoStr,entity2);
					logger.warn("response didn't have the proper contextSessionID");	
					return mapping.findForward("error");	
					
					
				}
				
			} else {
				addError(demographicNoStr,"Could not update Patient Context for EHR viewer",errors,request,contextSessionID);
				logger.warn("Did not get a success response from CMS : " + httpResponse2.getStatusLine().getStatusCode());
				return mapping.findForward("error");	
			}
			

		} catch (Exception e) {
			addError(demographicNoStr,"Error launching EHR viewer: " + e.getMessage(),errors,request,contextSessionID);
			logger.error("Error", e);
			return mapping.findForward("error");
		}

		
		
	}

	protected String retrieveContextSessionId(String oneIdToken) {
		String contextSessionID = null;
		
		
		try {
			//get the context session id
			String url = OscarProperties.getInstance().getProperty("backendEconsultUrl") + "/api/contextSessionId";
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("x-access-token", oneIdToken);
			HttpClient httpClient = getHttpClient2();
			HttpResponse httpResponse = httpClient.execute(httpGet);
	
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String entity = EntityUtils.toString(httpResponse.getEntity());
				JSONObject obj = new JSONObject(entity);
				contextSessionID = (String) obj.get("contextSessionID");
				logger.debug("contextSessionID = " + contextSessionID);
			}
		}catch(Exception e) {
			logger.error("Error",e);
		}
		
		return contextSessionID;
	}
	protected HttpClient getHttpClient2() throws Exception {

		String cmsKeystoreFile = OscarProperties.getInstance().getProperty("clinicalConnect.CMS.keystore");
		String cmsKeystorePassword = OscarProperties.getInstance().getProperty("clinicalConnect.CMS.keystore.password");

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(cmsKeystoreFile), cmsKeystorePassword.toCharArray());

		//setup SSL
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(ks, cmsKeystorePassword.toCharArray()).build();
		sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
		sslcontext.getDefaultSSLParameters().setWantClientAuth(true);
		SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);

		//setup timeouts
		int timeout = Integer.parseInt(OscarProperties.getInstance().getProperty("clinicalConnect.CMS.timeout", "60"));
		RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).build();

		CloseableHttpClient httpclient3 = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sf).build();

		return httpclient3;

	}

	boolean Check(String ccNumber) {
		int sum = 0;
		boolean alternate = false;
		for (int i = ccNumber.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(ccNumber.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
	}

}
