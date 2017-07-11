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
package oscar.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth.data.AccessToken;
import org.apache.cxf.rs.security.oauth.data.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth.data.AuthorizationInput;
import org.apache.cxf.rs.security.oauth.data.Client;
import org.apache.cxf.rs.security.oauth.data.OAuthPermission;
import org.apache.cxf.rs.security.oauth.data.RequestToken;
import org.apache.cxf.rs.security.oauth.data.RequestTokenRegistration;
import org.apache.cxf.rs.security.oauth.data.Token;
import org.apache.cxf.rs.security.oauth.data.UserSubject;
import org.apache.cxf.rs.security.oauth.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth.provider.OAuthServiceException;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ServiceAccessTokenDao;
import org.oscarehr.common.dao.ServiceClientDao;
import org.oscarehr.common.dao.ServiceRequestTokenDao;
import org.oscarehr.common.model.ServiceAccessToken;
import org.oscarehr.common.model.ServiceClient;
import org.oscarehr.common.model.ServiceRequestToken;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class OscarOAuthDataProvider implements OAuthDataProvider {

	Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private ServiceClientDao serviceClientDao;
	@Autowired
	private ServiceRequestTokenDao serviceRequestTokenDao;
	@Autowired
	private ServiceAccessTokenDao serviceAccessTokenDao;
	
	
	@Override
	public Client getClient(String clientId) throws OAuthServiceException {
		logger.debug("getClient() called");
		ServiceClient sc = serviceClientDao.findByKey(clientId);
		if(sc != null) {
			Client newClient = new Client(sc.getKey(), sc.getSecret(), sc.getName(), sc.getUri());
			return newClient;
		}
		
		return null;
	}
	
	/*
	 * scope is first available here
	 */
	@Override
	public RequestToken createRequestToken(RequestTokenRegistration reg) throws OAuthServiceException {
		logger.debug("createRequestToken() called");
        String tokenId = UUID.randomUUID().toString();
        String tokenSecret = UUID.randomUUID().toString();
        RequestToken rt = new RequestToken(reg.getClient(), tokenId, tokenSecret);
        
        StringBuilder sb = new StringBuilder();
        List<OAuthPermission> perms = new ArrayList<OAuthPermission>();
        for(String scope:reg.getScopes()) {
        	//TODO: load permissions from DB
        	OAuthPermission p = new OAuthPermission(scope, scope);
        	 perms.add(p);
        	 sb.append(" " + scope);
        }
        rt.setScopes(perms);
        
        rt.setCallback(reg.getCallback());
       

        ServiceRequestToken srt = new ServiceRequestToken();
        srt.setCallback(rt.getCallback());
        srt.setClientId(serviceClientDao.findByKey(rt.getClient().getConsumerKey()).getId());
        srt.setDateCreated(new Date());
        srt.setTokenId(rt.getTokenKey());
        srt.setTokenSecret(rt.getTokenSecret());
        srt.setScopes(sb.toString().trim());
        serviceRequestTokenDao.persist(srt);
        
        return rt;

	}

	@Override
	public RequestToken getRequestToken(String requestToken) throws OAuthServiceException {
		logger.debug("getRequestToken() called");
		ServiceRequestToken serviceToken = serviceRequestTokenDao.findByTokenId(requestToken);
		if(serviceToken != null) {

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, -1);
			Date oneHourAgo = cal.getTime();
			
			if(serviceToken.getDateCreated().before(oneHourAgo)) {
				//lifetime of 1 hour
				serviceRequestTokenDao.remove(serviceToken.getId());
				return null;
			}
				
			ServiceClient sc = serviceClientDao.find(serviceToken.getClientId());
			Client newClient = new Client(sc.getKey(), sc.getSecret(), sc.getName(), sc.getUri());
			RequestToken rt = new RequestToken(newClient, serviceToken.getTokenId(), serviceToken.getTokenSecret());
			
			List<OAuthPermission> perms = new ArrayList<OAuthPermission>();
			String[] scopes =  serviceToken.getScopes().split(" ");
	        for(String scope:scopes) {
	        	//TODO: load permissions from DB
	        	OAuthPermission p = new OAuthPermission(scope, scope);
	        	 perms.add(p);
	        }
	        rt.setScopes(perms);
	        
	        rt.setCallback(serviceToken.getCallback());
	        rt.setVerifier(serviceToken.getVerifier());
	        return rt;
		}
		return null;
	}

	@Override
	public String finalizeAuthorization(AuthorizationInput data) throws OAuthServiceException {
		logger.debug("finalizeAuthorization() called");
		RequestToken requestToken = data.getToken();
        requestToken.setVerifier(UUID.randomUUID().toString());
        
        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(data.getToken().getTokenKey());
        if(srt != null) {
        	srt.setVerifier(requestToken.getVerifier());
        	serviceRequestTokenDao.merge(srt);
        }
        return requestToken.getVerifier();
		
	}

	@Override
	public AccessToken createAccessToken(AccessTokenRegistration reg) throws OAuthServiceException {
		logger.debug("createAccessToken() called");
        RequestToken requestToken = reg.getRequestToken();

        Client client = requestToken.getClient();
        requestToken = getRequestToken(requestToken.getTokenKey());

        ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(requestToken.getTokenKey());
        
        String accessTokenString = UUID.randomUUID().toString();
        String tokenSecretString = UUID.randomUUID().toString();

        long issuedAt = System.currentTimeMillis() / 1000;
        AccessToken accessToken = new AccessToken(client, accessTokenString,
            tokenSecretString, 3600, issuedAt);
        UserSubject subject = new UserSubject(srt.getProviderNo(), new ArrayList<String>());
        accessToken.setSubject(subject);
        accessToken.getClient().setLoginName(srt.getProviderNo());
        
        accessToken.setScopes(requestToken.getScopes());
 
        ServiceAccessToken sat = new ServiceAccessToken();
        ServiceClient sc = serviceClientDao.findByKey(client.getConsumerKey());
        sat.setClientId(sc.getId());
        sat.setDateCreated(new Date());
        sat.setIssued(issuedAt);
        sat.setLifetime(-1);
        sat.setTokenId(accessTokenString);
        sat.setTokenSecret(tokenSecretString);
        sat.setProviderNo(srt.getProviderNo());
        sat.setScopes(srt.getScopes());
        serviceAccessTokenDao.persist(sat);
       
        serviceRequestTokenDao.remove(srt.getId());

        return accessToken;
	}

	@Override
	public AccessToken getAccessToken(String accessToken) throws OAuthServiceException {
		ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(accessToken);
		
		ServiceClient sc = serviceClientDao.find(sat.getClientId());
		Client c = getClient(sc.getKey());
		
		AccessToken accessToken1 = new AccessToken(c, sat.getTokenId(),
	            sat.getTokenSecret(), sat.getLifetime(), sat.getIssued());
		
		UserSubject subject = new UserSubject(sat.getProviderNo(), new ArrayList<String>());
        accessToken1.setSubject(subject);
        
        accessToken1.getClient().setLoginName(sat.getProviderNo());
        
		List<OAuthPermission> perms = new ArrayList<OAuthPermission>();
		String[] scopes =  sat.getScopes().split(" ");
        for(String scope:scopes) {
        	//TODO: load permissions from DB
        	OAuthPermission p = new OAuthPermission(scope, scope);
        	perms.add(p);
        }
        accessToken1.setScopes(perms);
        
		return accessToken1;
	}

	@Override
	public void removeToken(Token token) throws OAuthServiceException {
		ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(token.getTokenKey());
		if(srt != null) {
			serviceRequestTokenDao.remove(srt.getId());
		}
		ServiceAccessToken sat = serviceAccessTokenDao.findByTokenId(token.getTokenKey());
		if(sat != null) {
			serviceAccessTokenDao.remove(sat.getId());
		}

	}

}
