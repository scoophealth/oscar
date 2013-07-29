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
package org.oscarehr.research.eaaps;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;

import oscar.OscarProperties;

/**
 * Service client for querying the EAAPS web service.
 *
 */
public class EaapsServiceClient {

	private static Logger logger = Logger.getLogger(EaapsServiceClient.class);

	private String host;
	private String userName;
	private String password;

	// http://radu.cotescu.com/java-https-rest-services-apache-cxf/
	private static class TrustManager implements javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			// accept all
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			// accept all
		}
	}

	private static void setClientAuthentication(Object client, String userName, String password) {
		// Properties p = new Properties(); //PropertiesLoader.getPropertiesFromFile("config.properties");
		ClientConfiguration config = WebClient.getConfig(client);
		HTTPConduit httpConduit = (HTTPConduit) config.getConduit();

		AuthorizationPolicy authorization = new AuthorizationPolicy();
		authorization.setUserName(userName);
		authorization.setPassword(password);
		httpConduit.setAuthorization(authorization);

		TLSClientParameters tlsParams = new TLSClientParameters();
		TrustManager[] trustAllCerts = new TrustManager[] { new TrustManager() };
		tlsParams.setTrustManagers(trustAllCerts);

		// disables verification of the common name (the host for which the certificate has been issued)
		tlsParams.setDisableCNCheck(true);
		httpConduit.setTlsClientParameters(tlsParams);
	}

	public EaapsServiceClient() {
		this(null, null, null);
	}

	public EaapsServiceClient(String host, String user, String pass) {
		OscarProperties props = OscarProperties.getInstance();
		if (host == null) {
			host = props.getProperty("eaaps.ws.host", "https://secure.knowledgetranslation.ca:8081/eaaps/v0/");
		}
		setHost(host);
		
		if (user == null) {
			user = props.getProperty("eaaps.ws.user", "test");
		}
		setUserName(user);
		
		if (pass == null) {
			pass = props.getProperty("eaaps.ws.pass", "test");
		}
		setPassword(pass);
	}

	public EaapsPatientData getPatient(String patientHash) throws Exception {
		String path = "/patients/" + patientHash;

		WebClient client = WebClient.create(getHost());
		setClientAuthentication(client, getUserName(), getPassword());
		client = client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).path(path);

		Response response = client.get();
		Object entity = response.getEntity();

		InputStream is = new BufferedInputStream((InputStream) entity);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = new BufferedOutputStream(baos);

		String content = null;
		try {
			int i = 0;
			byte[] buf = new byte[4096];
			while ((i = is.read(buf)) != -1) {
				os.write(buf, 0, i);
			}
			os.flush();

			content = baos.toString();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				logger.error("Unable to close input stream", e);
			}

			try {
				os.close();
			} catch (Exception e) {
				logger.error("Unable to close output stream", e);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Received response: " + content);
		}

		JSONObject json = (JSONObject) JSONSerializer.toJSON(content);
		if (json == null) {
			throw new Exception("Unable to serialize eAAP response");
		}
		
		if (json.containsKey("code")) {
			String code = json.getString("code");
			if (code != null && code.equals("InternalError")) {
				String message = json.containsKey("message") ? json.getString("message") : "";
				throw new Exception("eAAPS Web Service Error: " + message);
			}
		}
		if (!json.containsKey("status")) {
			throw new Exception("Status information is not available in the service response");
		}
		return new EaapsPatientData(json);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EaapsPatientData getPatient(Demographic demographic) throws Exception {
	    EaapsHash hash = new EaapsHash(demographic);
	    return getPatient(hash.getHash());
    }
}
