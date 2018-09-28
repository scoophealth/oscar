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
package oscar.oscarEncounter.pageUtil;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.struts.util.MessageResources;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.util.LoggedInInfo;

import oscar.OscarProperties;

public class EctDisplayEHRAction extends EctDisplayAction {

	private static final String cmd = "ehr";

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_ehr", "r", null)) {
			return true;
		} else {
			String winName = "ehr" + bean.demographicNo;
			String url = "javascript:void(0)";
			Dao.setLeftHeading("Provincial EHR Services");
			Dao.setLeftURL(url);

			url += ";return false;";
			Dao.setRightURL(url);
			Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action

			if(request.getSession().getAttribute("oneid_token") != null && tokenValid((String)request.getSession().getAttribute("oneid_token"))) {
				NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
				item.setTitle("Clinical Connect Viewer");
				item.setLinkTitle("Open the Clinical Connect EHR Viewer");
				item.setURL("openCCEHRWindow('"+request.getContextPath()+"/clinicalConnectEHRViewer.do?method=launch&demographicNo="+bean.demographicNo +"','"+bean.demographicNo+"');return false;");
				Dao.addItem(item);
			} else {
				NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
				item.setTitle("Not logged in");
				item.setLinkTitle("Sign in to OneID required");
				
				String backendEconsultUrl = OscarProperties.getInstance().getProperty("backendEconsultUrl");
				//String oscarUrl = request.getRequestURL().toString();
				String oUrl = getBaseUrl(request);
				String url2 = null;
				try {
					url2 = backendEconsultUrl + "/SAML2/login?oscarReturnURL=" + URLEncoder.encode(oUrl + "/econsultSSOLogin.do?operation=launch", "UTF-8") + "&loginStart=" + new Date().getTime() / 1000;
				} catch(Exception e) {
					
				}
				
				if(url2 != null) {
					item.setURL("javascript:location.href='"+url2+"';return false;");
					
				} else {
					item.setURL("javascript:void(0);return false;");
					
				}
				
				Dao.addItem(item);
			}

			return true;
		}
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
	    String scheme = request.getScheme() + "://";
	    String serverName = request.getServerName();
	    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
	    String contextPath = request.getContextPath();
	    return scheme + serverName + serverPort + contextPath;
	  }
	
	private boolean tokenValid(String token) {
		Date d = this.retrieveSessionExpiration(token);
		if(d == null) {
			return false;
		}
			
		if(d.after(new Date())) {
			return true;
		}
		return false;
	}

	protected Date retrieveSessionExpiration(String oneIdToken) {
		String sessionExpiry = null;
		
		
		try {
			//get the context session id
			String url = OscarProperties.getInstance().getProperty("backendEconsultUrl") + "/api/getTokenExpiry";
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("x-access-token", oneIdToken);
			HttpClient httpClient = getHttpClient2();
			HttpResponse httpResponse = httpClient.execute(httpGet);
	
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String entity = EntityUtils.toString(httpResponse.getEntity());
				JSONObject obj = new JSONObject(entity);
				sessionExpiry = (String) obj.get("sessionExpiration");
				MiscUtils.getLogger().debug("sessionExpiry = " + sessionExpiry);
				
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
				Date d  = fmt.parse(sessionExpiry + "+0000");
				
				return d;
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		return null;
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
	public String getCmd() {
		return cmd;
	}
}
