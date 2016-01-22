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
<%@page import="org.apache.cxf.rs.security.oauth.client.OAuthClientUtils,org.apache.cxf.jaxrs.client.WebClient,java.util.*,java.net.*,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.*,org.oscarehr.app.*"%><%

AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);

Integer appId = null;
if (request.getParameter("id") != null){
	try{
	appId = Integer.parseInt(request.getParameter("id"));
	}catch(Exception e){
		if("K2A".equals(request.getParameter("id"))){
			AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			if(k2aApp != null){
				appId = k2aApp.getId();
			}
		}
	}
}else if(session.getAttribute("appId") != null ){
	appId = (Integer) session.getAttribute("appId");
}

if(appId == null){
	response.sendRedirect("close.jsp");
}

AppDefinition appDef = appDefinitionDao.find(appId);

AppOAuth1Config oauth1config = (AppOAuth1Config) AppOAuth1Config.fromDocument(appDef.getConfig());
OAuthClientUtils.Consumer consumer = new OAuthClientUtils.Consumer(oauth1config.getConsumerKey(),oauth1config.getConsumerSecret());

if(request.getParameter("oauth_verifier") == null){	  //need to request a token
	try{
		WebClient requestTokenService = WebClient.create(oauth1config.getRequestTokenService()).encoding("text/plain;charset=UTF-8"); // /oauth/request_token
		requestTokenService = requestTokenService.accept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		URI callback = new URI(""+request.getRequestURL());
		MiscUtils.getLogger().error(""+request.getRequestURL());
		Map<String,String> extraParams  = null;
		String authorizationServiceURI = oauth1config.getAuthorizationServiceURI();
	
		OAuthClientUtils.Token requestToken = OAuthClientUtils.getRequestToken(requestTokenService,consumer,callback,extraParams);
		session.setAttribute("requestToken",requestToken);
		session.setAttribute("appId",appId);
		URI authUrl = OAuthClientUtils.getAuthorizationURI(authorizationServiceURI, requestToken.getToken());
		response.sendRedirect(authUrl.toString());
	}catch(Exception e){
		MiscUtils.getLogger().error("Error getting Request Token from app "+appId+" for user "+(String)session.getAttribute("user"),e);
		session.setAttribute("oauthMessage","Error requesting token from app");		
		response.sendRedirect("close.jsp");
	}
}else{
	try{
	WebClient accessTokenService  = WebClient.create(oauth1config.getAccessTokenService()).encoding("text/plain;charset=UTF-8"); // /oauth/request_token
	accessTokenService = accessTokenService.accept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	String oauthVerifier = request.getParameter("oauth_verifier");
	OAuthClientUtils.Token requestToken = (OAuthClientUtils.Token) session.getAttribute("requestToken");
	OAuthClientUtils.Token accessToken = OAuthClientUtils.getAccessToken(accessTokenService, consumer, requestToken, oauthVerifier);
	
	//appUserDao
	AppUser appuser = new AppUser();
	appuser.setAppId(appId); 
	appuser.setProviderNo((String)session.getAttribute("user"));

	String authenticationData = AppOAuth1Config.getTokenXML(accessToken.getToken(),accessToken.getSecret());
	appuser.setAuthenticationData(authenticationData);
	
	appuser.setAdded(new Date());
	appUserDao.saveEntity(appuser);
	}catch(Exception e){
		session.setAttribute("oauthMessage","Error with verifing authentication");
		MiscUtils.getLogger().error("Error returning from app "+appId+" for user "+(String)session.getAttribute("user"),e);
		response.sendRedirect("close.jsp");
	}
	
	session.setAttribute("oauthMessage","Success");
	session.removeAttribute("requestToken");
	session.removeAttribute("appId");
	response.sendRedirect("close.jsp");
}
%>