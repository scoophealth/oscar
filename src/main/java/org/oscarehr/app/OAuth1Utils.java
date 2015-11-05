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
package org.oscarehr.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.MiscUtils;

public class OAuth1Utils {
	private static final Logger logger = MiscUtils.getLogger();
	
	public static String getOAuthResponse(AppDefinition app, AppUser user,String requestURI) {
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		BufferedReader bufferedReader = null;
		try {
			AppOAuth1Config appAuthConfig = AppOAuth1Config.fromDocument(app.getConfig());
			Map<String,String> keySecret = AppOAuth1Config.getKeySecret(user.getAuthenticationData());
			
			OAuthClientUtils.Consumer consumer = new OAuthClientUtils.Consumer(appAuthConfig.getConsumerKey(),appAuthConfig.getConsumerSecret());
			OAuthClientUtils.Token accessToken = new OAuthClientUtils.Token(keySecret.get("key"),keySecret.get("secret"));
			
			String method = "GET";
			requestURI = appAuthConfig.getBaseURL() + requestURI;
			WebClient webclient = WebClient.create(requestURI);
			
			webclient = webclient.replaceHeader("Authorization",OAuthClientUtils.createAuthorizationHeader(consumer, accessToken, method, requestURI));
			
			javax.ws.rs.core.Response reps = webclient.get();
			
			in = (InputStream) reps.getEntity();
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch(Exception e) {
			logger.error("Error getting information from OAuth Service", e);
			return null;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(bufferedReader);
		}
	}
}
