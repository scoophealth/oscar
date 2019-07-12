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
package org.oscarehr.ws.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.dao.AppointmentSearchDao;
import org.oscarehr.common.dao.ConsentDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.common.model.AppointmentSearch;
import org.oscarehr.common.model.Consent;
import org.oscarehr.common.model.ConsentType;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.PatientConsentManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.phr.RegistrationHelper;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.Creds;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.RSSResponse;
import org.oscarehr.ws.rest.to.model.AppDefinitionTo1;
import org.oscarehr.ws.rest.to.model.PHRAccount;
import org.oscarehr.ws.rest.to.model.PHRInviteTo1;
import org.oscarehr.ws.rest.to.model.ProviderTo1;
import org.oscarehr.ws.rest.to.model.RssItem;
import org.springframework.beans.factory.annotation.Autowired;

import com.quatro.dao.security.SecuserroleDao;

import com.quatro.model.security.Secuserrole;
import com.quatro.web.admin.SecurityAddSecurityHelper;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

import org.oscarehr.PMmodule.dao.ProviderDao;


@Path("/app")
public class AppService extends AbstractServiceImpl {
	protected Logger logger = MiscUtils.getLogger();
	
	@Autowired
	AppManager appManager;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	@Autowired
	AppDefinitionDao appDefinitionDao;
	
	@Autowired
	private DemographicManager demographicManager;
	
	@Autowired
	ConsentDao consentDao;
	
	@Autowired
	ProviderDao providerDao;
	
	@Autowired
	SecurityDao securityDao;
	
	@Autowired
	PatientConsentManager patientConsentManager;
	
	@Autowired
	SecuserroleDao secUserRoleDao;
	
	@Autowired
	AppointmentSearchDao appointmentSearchDao;
	
	@GET
	@Path("/getApps/")
	@Produces("application/json")
	public List<AppDefinitionTo1> getApps() {
		return appManager.getAppDefinitions(getLoggedInInfo());		
	}
	
	@GET
	@Path("/K2AActive/")
	@Produces("application/json")
	public GenericRESTResponse isK2AActive(){
		GenericRESTResponse response = null;
		if( appManager.getAppDefinition(getLoggedInInfo(), "K2A") == null){
			response = new GenericRESTResponse(false,"K2A active");
		}else{
			response = new GenericRESTResponse(true,"K2A not active");
		}
		return response;
	}

	
	@GET
	@Path("/PHRActive/")
	@Produces("application/json")
	public GenericRESTResponse isPHRActive(@Context HttpServletRequest request){
		GenericRESTResponse response = null;
		if( appManager.hasAppDefinition(getLoggedInInfo(), "PHR")){
			response = new GenericRESTResponse(true,"PHR active");			
		}else{
			response = new GenericRESTResponse(false,"PHR not active");
		}
		return response;
	}
	
	
	@GET
	@Path("/PHRActiveAndConsentConfigured/")
	@Produces("application/json")
	public GenericRESTResponse isPHRActiveAndConsentConfigured(@Context HttpServletRequest request){
		Integer id = appManager.getAppDefinitionConsentId(getLoggedInInfo(), "PHR");
		if (id != null) {
			return new GenericRESTResponse(true,""+id);
		}
		return new GenericRESTResponse(false,"Consent Not Configured");
	}
	
	
	/*States returned:                  Boolean    Message 
		PHR inactive                    FALSE      INACTIVE
		PHR active & Consent Needed     TRUE       NEED_CONSENT
		PHR Active & Consent exists.    TRUE       CONSENTED
	*/
	@GET
	@Path("/PHRActive/consentGiven/{demographicNo}")
	@Produces("application/json")
	public GenericRESTResponse isPHRActiveAndConsentGiven(@Context HttpServletRequest request,@PathParam("demographicNo") Integer demographicNo){
		String roleName$ = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
    	
		GenericRESTResponse response = null;
		Integer id = appManager.getAppDefinitionConsentId(getLoggedInInfo(), "PHR");
		if(id == null){
			response = new GenericRESTResponse(false,"INACTIVE");
		}else{
			//look up consent identifier here and check if patient has given consent.
			Consent consent = consentDao.findByDemographicAndConsentTypeId( demographicNo,id) ;
			if(consent != null && consent.getPatientConsented()) {
				response = new GenericRESTResponse(true,"CONSENTED");
			}else {
				response = new GenericRESTResponse(true,"NEED_CONSENT");
			}
		
		}
		return response;
	}
	
	@POST
	@Path("/PHRActive/consentGiven/{demographicNo}")
	@Produces("application/json")
	public GenericRESTResponse recordConsentGiven(@Context HttpServletRequest request,@PathParam("demographicNo") Integer demographicNo){
		
		GenericRESTResponse response = null;
		Integer id = appManager.getAppDefinitionConsentId(getLoggedInInfo(), "PHR");
		if(id == null){
			response = new GenericRESTResponse(false,"INACTIVE");
		}else{
			//look up consent identifier here and check if patient has given consent.
				Consent consent = new Consent();
				consent.setConsentDate(new Date());
				consent.setConsentTypeId(id);
				consent.setDemographicNo(demographicNo);
				consent.setExplicit(true);
				consent.setOptout(false);
				consent.setLastEnteredBy(getLoggedInInfo().getLoggedInProviderNo());
				consentDao.persist(consent);
				//Consent consent = consentDao.findByDemographicAndConsentTypeId( demographicNo,  appDef.getConsentTypeId()  ) ;
				if(consent != null && consent.getPatientConsented()) {
					response = new GenericRESTResponse(true,"CONSENTED");
				}else {
					response = new GenericRESTResponse(true,"NEED_CONSENT");
				}
		}
		return response;
	}
	
	
	
	//$scope.createPHRUser = function(){
 	//-Just do it and then show report of what has happened
	@POST
	@Path("/PHRCreateUser/")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse createPHRUser(@Context HttpServletRequest request,ProviderTo1 provider){
	
		//<li>create a provider record with the name self-Book</li>
		
		Provider p = new Provider();
		p.setProviderNo(provider.getProviderNo());
		p.setLastName(provider.getLastName());
		p.setFirstName(provider.getFirstName());
		p.setProviderType("doctor");
		p.setSex("F"); //has to be set to something
		p.setSpecialty("Integration");
		p.setStatus("1");
		
		if(providerDao.providerExists(p.getProviderNo())) {
			return new GenericRESTResponse(false,"Provider # already in use");
		} else {
		  	providerDao.saveProvider(p);
		}
		
		//<li>creating a security record with a strong random user/password</li>
		String username = RegistrationHelper.getNewRandomPassword();
		String password = RegistrationHelper.getNewRandomPassword();
		
		String encodedPassword = null;
		try {
			encodedPassword = SecurityAddSecurityHelper.digestPassword(password);
		}catch(NoSuchAlgorithmException e) {
			return new GenericRESTResponse(false,"Failed encoding password");
		}
		
		Security s = new Security();
			s.setUserName(username);
			s.setPassword(encodedPassword);
			s.setProviderNo(p.getProviderNo());
			s.setPin(null);
			s.setBExpireset(0);
			s.setDateExpiredate(null);
			s.setBLocallockset(0);
			s.setBRemotelockset(0);
	    		s.setForcePasswordReset(Boolean.FALSE);  
	    	securityDao.persist(s);

		LogAction.addLog(getLoggedInInfo().getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_SECURITY, request.getParameter("user_name"), request.getRemoteAddr());
		
	    //<li>creating consent type to record which patients are participating with using the PHR</li>
		ConsentType consentTypeToAdd = new ConsentType();
		
		consentTypeToAdd.setActive(true);
		consentTypeToAdd.setDescription("Consent to handle PHR data");
		consentTypeToAdd.setName("PHR");
		consentTypeToAdd.setProviderNo(p.getProviderNo());
		consentTypeToAdd.setRemoteEnabled(true);
		consentTypeToAdd.setType(ConsentType.PROVIDER_CONSENT_FILTER);
		
		patientConsentManager.addConsentType(getLoggedInInfo(), consentTypeToAdd);
		
	    //<li>And communicate this user to the PHR server for integration.</li>
		logger.error("send this to connector "+s.getId()+"  password "+password);
		
		AppDefinition appDef = appManager.getAppDefinition(getLoggedInInfo(), "PHR");
		appDef.setConsentTypeId(consentTypeToAdd.getId());
		appManager.updateAppDefinition(getLoggedInInfo(), appDef);
		
		Secuserrole secUserRole = new Secuserrole();
	    secUserRole.setProviderNo(p.getProviderNo());
	    secUserRole.setRoleName("doctor");
	    secUserRole.setActiveyn(1);
	    secUserRoleDao.save(secUserRole);
	    LogAction.addLog(getLoggedInInfo().getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_ROLE, p.getProviderNo() +"|doctor",request.getRemoteAddr());

	    
	    AppointmentSearch appointmentSearch = new AppointmentSearch();
		appointmentSearch.setProviderNo(p.getProviderNo());
		appointmentSearch.setSearchName("PHR");
		appointmentSearch.setUuid(UUID.randomUUID().toString());
		appointmentSearch.setSearchType(AppointmentSearch.ONLINE);
			
		appointmentSearchDao.persist(appointmentSearch);
		
		JSONObject jojb = new JSONObject();
		jojb.element("username",""+s.getId());
		jojb.element("password", password);
		jojb.element("url",provider.getComments());
		
		try {
			Response reps = callPHR("configurEMRConnection",getLoggedInInfo().getLoggedInProviderNo(),jojb.toString());
			//InputStream in = (InputStream) reps.getEntity();
			//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			//String response = IOUtils.toString(bufferedReader);
			String response = (String)  reps.getEntity();
			//bufferedReader.close();
		}catch(Exception e) {
			logger.error("error",e);
			return new GenericRESTResponse(false,"ERROR connecting to PHR");
		}
		
		return new GenericRESTResponse(true,"Registered Sucessfully");
	}
 
	//$scope.linkExistingUser = function(){
	//Show drop list to select user.
	@POST
	@Path("/PHRLinkUser/")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse linkPHRUser(@Context HttpServletRequest request,ProviderTo1 provider){
		//<p>This will create the consentType to record which patients are participating with using the PHR. Communicating the user information to the PHR will need to be done manually.</p>
		
		List<Security> secList = securityDao.findByProviderNo(provider.getProviderNo());
		Security s = null;
		String password = provider.getFirstName();
		if(secList.size() == 0) {
			//<li>creating a security record with a strong random user/password</li>
			String username = RegistrationHelper.getNewRandomPassword();
			//password = RegistrationHelper.getNewRandomPassword();
			
			String encodedPassword = null;
			try {
				encodedPassword = SecurityAddSecurityHelper.digestPassword(password);
			}catch(NoSuchAlgorithmException e) {
				return new GenericRESTResponse(false,"Failed encoding password");
			}
			
			s = new Security();
				s.setUserName(username);
				s.setPassword(encodedPassword);
				s.setProviderNo(provider.getProviderNo());
				s.setPin(null);
				s.setBExpireset(0);
				s.setDateExpiredate(null);
				s.setBLocallockset(0);
				s.setBRemotelockset(0);
		    		s.setForcePasswordReset(Boolean.FALSE);  
		    	securityDao.persist(s);

			LogAction.addLog(getLoggedInInfo().getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_SECURITY, request.getParameter("user_name"), request.getRemoteAddr());

		}else{
			s = secList.get(0);
		}
				
				
	    //<li>creating consent type to record which patients are participating with using the PHR</li>
		ConsentType consentTypeToAdd = new ConsentType();
		
		consentTypeToAdd.setActive(true);
		consentTypeToAdd.setDescription("Consent to handle PHR data");
		consentTypeToAdd.setName("PHR");
		consentTypeToAdd.setProviderNo(provider.getProviderNo());
		consentTypeToAdd.setRemoteEnabled(true);
		consentTypeToAdd.setType(ConsentType.PROVIDER_CONSENT_FILTER);
		
		patientConsentManager.addConsentType(getLoggedInInfo(), consentTypeToAdd);
		
	    //<li>And communicate this user to the PHR server for integration.</li>
		
		AppDefinition appDef = appManager.getAppDefinition(getLoggedInInfo(), "PHR");
		appDef.setConsentTypeId(consentTypeToAdd.getId());
		appManager.updateAppDefinition(getLoggedInInfo(), appDef);
		
		if(appointmentSearchDao.findForProvider(provider.getProviderNo()) == null) {
		    AppointmentSearch appointmentSearch = new AppointmentSearch();
			appointmentSearch.setProviderNo(provider.getProviderNo());
			appointmentSearch.setSearchName("PHR");
			appointmentSearch.setUuid(UUID.randomUUID().toString());
			appointmentSearch.setSearchType(AppointmentSearch.ONLINE);
			appointmentSearchDao.persist(appointmentSearch);
		}
		JSONObject jojb = new JSONObject();
		jojb.element("username",""+s.getId());
		jojb.element("password", password);
		jojb.element("url",provider.getComments());
		
		try {
			Response reps = callPHR("configurEMRConnection",getLoggedInInfo().getLoggedInProviderNo(),jojb.toString());
			//InputStream in = (InputStream) reps.getEntity();
			//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			//String response = IOUtils.toString(bufferedReader);
			String response = (String)  reps.getEntity();
			//bufferedReader.close();
		}catch(Exception e) {
			logger.error("error",e);
			return new GenericRESTResponse(false,"ERROR connecting to PHR");
		}
		
		return new GenericRESTResponse(true,"Registered Sucessfully");

	}
 
	
	
	@POST
	@Path("/PHRInit/")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse initPHR(@Context HttpServletRequest request,Creds clinicCreds){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		List<Object> providers = new ArrayList<Object>();
		JSONProvider jsonProvider = new JSONProvider();
		jsonProvider.setDropRootElement(true);
	    providers.add(jsonProvider);
	    String requestURI = OscarProperties.getInstance().getProperty("PHR_CONNECTOR_URL");	  
		try {
		WebClient webclient = WebClient.create(requestURI+"emr/register", providers);
		HTTPConduit conduit = WebClient.getConfig(webclient).getHttpConduit();

		    TLSClientParameters params = conduit.getTlsClientParameters();

		    if (params == null) {
		        params = new TLSClientParameters();
		        conduit.setTlsClientParameters(params);
		    }
		    if(OscarProperties.getInstance().isPropertyActive("PHR_TEST_CONNECTION")){
		    		params.setTrustManagers(new TrustManager[] { TrustManagerUtils.getAcceptAllTrustManager() });
		    		params.setDisableCNCheck(true);
		    }
		////////
   		javax.ws.rs.core.Response reps = webclient.accept("application/json, text/plain, */*").acceptEncoding("gzip, deflate").type("application/json;charset=utf-8").post(clinicCreds);
		logger.info("response code "+reps.getStatus());
   		if(reps.getStatus() == 200) {
   		
	   		InputStream in = (InputStream) reps.getEntity();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();	
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		
			bufferedReader.close();
			
		    String response  = sb.toString();
			 
		      
		    AppDefinition phrNew = new AppDefinition();
			      
		    phrNew.setActive(true);
		    phrNew.setAdded(new Date());
		    phrNew.setAppType(AppDefinition.OAUTH2_TYPE);
		    phrNew.setName("PHR");
		    phrNew.setConfig(response);
		    phrNew.setAddedBy(getLoggedInInfo().getLoggedInProviderNo());
		    appManager.saveAppDefinition(getLoggedInInfo(),  phrNew);
		    
		    return new GenericRESTResponse(true,"completed");
   		}else if(reps.getStatus() == 404){
   			return new GenericRESTResponse(false,"Invalid Username/Password"); 
		}
		}catch(Exception e) {
			logger.error("error initializing phr",e);
		}
		return new GenericRESTResponse(false,"failed"); 
	}
	
	private String getAccessToken(AppDefinition phrApp,String providerNo, String type) {
		try {
			org.codehaus.jettison.json.JSONObject configObject = new org.codehaus.jettison.json.JSONObject(phrApp.getConfig());
			String requestURL = OscarProperties.getInstance().getProperty("PHR_CONNECTOR_URL");   
			String requestURI = "/oauth/token";

			WebClient webclient = WebClient.create(requestURL+requestURI,configObject.getString("clientId"),configObject.getString("clientSecret"),null);//, providers);
			HTTPConduit conduit = WebClient.getConfig(webclient).getHttpConduit();

		    TLSClientParameters params = conduit.getTlsClientParameters();

		    if (params == null) {
		        params = new TLSClientParameters();
		        conduit.setTlsClientParameters(params);
		    }

		    if(OscarProperties.getInstance().isPropertyActive("PHR_TEST_CONNECTION")){
		    		params.setTrustManagers(new TrustManager[] { TrustManagerUtils.getAcceptAllTrustManager() });
		    		params.setDisableCNCheck(true);
		    }
			
	   		javax.ws.rs.core.Response reps = webclient.accept("application/json, text/plain, */*")
	   												 .acceptEncoding("gzip, deflate")
	   												 .type("application/x-www-form-urlencoded")
	   												 .post("grant_type=client_credentials&userId="+providerNo);
	   		
	   		InputStream in = (InputStream) reps.getEntity();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String response = IOUtils.toString(bufferedReader);
			bufferedReader.close();
			logger.debug("oauth2 json :"+response);
			
			org.codehaus.jettison.json.JSONObject responseObject = new org.codehaus.jettison.json.JSONObject(response);
			String access_token = responseObject.getString("access_token");
			return access_token;
			
		}catch(Exception e) {
			logger.error("Error with access token ",e);
		}
		
		return null;
	}
	
	private javax.ws.rs.core.Response callPHRWebClient(String url,String object,String bearerToken) {
		List<Object> providers = new ArrayList<Object>();
		JSONProvider jsonProvider = new JSONProvider();
		jsonProvider.setDropRootElement(true);
	    providers.add(jsonProvider);
		WebClient webclient = WebClient.create(url, providers);
		HTTPConduit conduit = WebClient.getConfig(webclient).getHttpConduit();

	    TLSClientParameters params = conduit.getTlsClientParameters();

	    if (params == null) {
	        params = new TLSClientParameters();
	        conduit.setTlsClientParameters(params);
	    }
	    if(OscarProperties.getInstance().isPropertyActive("PHR_TEST_CONNECTION")){
	    		params.setTrustManagers(new TrustManager[] { TrustManagerUtils.getAcceptAllTrustManager() });
	    		params.setDisableCNCheck(true);
	    }
	    javax.ws.rs.core.Response reps = webclient.accept("application/json, text/plain, */*")
			 .acceptEncoding("gzip, deflate")
			 .header("Authorization", "Bearer "+bearerToken)
			 .type("application/json;charset=utf-8")
			 .post(object);
	   
	    return reps;
	}
	
	private Response callPHR(String requestURI,String providerNo) {
		return callPHR(requestURI, providerNo,"True");
	}

	private Response callPHR(String requestURI,String providerNo,String object) {
		//////////
	    String requestURL = OscarProperties.getInstance().getProperty("PHR_CONNECTOR_URL");    
	    AppDefinition phrApp = appDefinitionDao.findByName("PHR");
		    
		try {
			String bearerToken = getAccessToken(phrApp,providerNo,"PHR");
		    logger.debug("bearerToken: "+bearerToken );
	   		javax.ws.rs.core.Response reps = callPHRWebClient(requestURL+requestURI,object,bearerToken);
	   		
	   		InputStream in = (InputStream) reps.getEntity();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String response = IOUtils.toString(bufferedReader);
			bufferedReader.close();
	   		
			logger.info("response code "+reps.getStatus());
	   		if(reps.getStatus() >= 200 && reps.getStatus() < 300) {
	   			return Response.ok(response).status(reps.getStatus()).build();
	   		}
	   		
			}catch(Exception e) {
				
				Throwable rootException = ExceptionUtils.getRootCause(e);
				logger.debug("Exception: "+e.getClass().getName()+" --- "+rootException.getClass().getName());
						
				if (rootException instanceof java.net.ConnectException || rootException instanceof java.net.SocketTimeoutException){
					logger.error("ERROR CONNECTING ",rootException);
					return Response.status(268).entity("{\"ERROR\":\"Connection Refused\"}").build();
				}
					
				logger.error("ERROR getting abilities",e);
			}
	   		return Response.status(401).entity("ERROR").build();
	}
	
	private String  callPHRWindowOpen(String requestURI,String providerNo,String windowName ,String role) {
		//////////
		JSONObject jojb = new JSONObject();
		jojb.element("windowName",windowName);
		jojb.element("providerNo", providerNo);
		jojb.element("role",role);
		
		String requestURL = OscarProperties.getInstance().getProperty("PHR_CONNECTOR_URL");    
	    AppDefinition phrApp = appDefinitionDao.findByName("PHR");
		    
		try {
			String bearerToken = getAccessToken(phrApp,providerNo,"PHR");
		    logger.debug("bearerToken: "+bearerToken );
	   		javax.ws.rs.core.Response reps = callPHRWebClient(requestURL+requestURI,jojb.toString(),bearerToken);
	   		
	   		InputStream in = (InputStream) reps.getEntity();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String response = IOUtils.toString(bufferedReader);
			bufferedReader.close();
	   		logger.error("url launching "+response+"?access_token="+bearerToken);
			return response+"?access_token="+bearerToken;
	   		
			}catch(Exception e) {
				
				Throwable rootException = ExceptionUtils.getRootCause(e);
				logger.debug("Exception: "+e.getClass().getName()+" --- "+rootException.getClass().getName());
						
				if (rootException instanceof java.net.ConnectException || rootException instanceof java.net.SocketTimeoutException){
					logger.error("ERROR CONNECTING ",rootException);
					return null;
				}
					
				logger.error("ERROR getting abilities",e);
			}
	   		return null;
	}

	
	/////
	@POST
	@Path("/PHRAuditSetup/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response phrEMRAudit(@Context HttpServletRequest request){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		return callPHR("auditSetup",getLoggedInInfo().getLoggedInProviderNo());
	}
	
	@GET
	@Path("/PHREmailInvite/{demographicId}")
	public GenericRESTResponse phrEmailInvite(@Context HttpServletRequest request, @PathParam("demographicId") String demographicId) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		Demographic demographic = demographicManager.getDemographic(getLoggedInInfo(), demographicId);
		if (demographic!=null && demographic.getEmail()!=null && (demographic.getMyOscarUserName()==null || demographic.getMyOscarUserName().trim().isEmpty())) {
			PHRInviteTo1 invite = new PHRInviteTo1();
			invite.setDemographicNo(Long.valueOf(demographic.getDemographicNo()));
			invite.setEmail(demographic.getEmail());
			invite.setFirstName(demographic.getFirstName());
			invite.setLastName(demographic.getLastName());
			invite.setLanguage(demographic.getOfficialLanguage());
			invite.setTitle(demographic.getTitle());
			
			try {
				Response response = callPHR("/clinics/phr/invite", getLoggedInInfo().getLoggedInProviderNo(), invite.toJson().toString());
				
				if (response.getStatus()==Response.Status.CREATED.getStatusCode()) {
					return new GenericRESTResponse(true, "Email invite sent");
				} else {
					return new GenericRESTResponse(false, "Connect PHR error");
				}
			} catch (JSONException e) {
				logger.error("Convert PHRInvite to JSON error", e);
				return new GenericRESTResponse(false, "Convert PHRInvite to JSON error");
			}
		}
		return new GenericRESTResponse(false, "Demographic/email/PHRUsername error");
	}
	
	@POST
	@Path("/updatePHRPW")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse createProviderPHRAccount(@Context HttpServletRequest request){
		try {
			String password = RegistrationHelper.getNewRandomPassword();
			String providerNo = getLoggedInInfo().getLoggedInProviderNo();
			
			PHRAccount phrAccount = new PHRAccount();
			phrAccount.setPassword(password);
			phrAccount.setProviderNo(ProviderMyOscarIdData.getMyOscarId(providerNo));
			
			Response response = callPHR("/updatePW", providerNo, phrAccount.toJson().toString());
			logger.error("stat "+response.getStatus()+" == "+Response.Status.OK.getStatusCode()+" ==== "+(response.getStatus()==Response.Status.CREATED.getStatusCode()));
			
			
			if (response.getStatus()==Response.Status.OK.getStatusCode()) {
				try {
				String username = (String) response.getEntity(); 
				//Save username to properties
				ProviderMyOscarIdData.setId(providerNo, username);
				
				//Save Password
				SecretKeySpec key=MyOscarUtils.getDeterministicallyMangledPasswordSecretKeyFromSession(request.getSession());
		        byte[] encryptedMyOscarPassword=EncryptionUtils.encrypt(key, password.getBytes("UTF-8"));

		        ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
		        ProviderPreference providerPreference=providerPreferenceDao.find(providerNo);
		        providerPreference.setEncryptedMyOscarPassword(encryptedMyOscarPassword);
		        providerPreferenceDao.merge(providerPreference);
		        
		        MyOscarLoggedInInfo.setLoggedInInfo(request.getSession(), null);
				
		        MyOscarUtils.attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously(getLoggedInInfo(), false);
				
				return new GenericRESTResponse(true, "Password Updated");	
				}catch(Exception e) {
					logger.error("Error creating account ",e);
				}
				
			} 
			return new GenericRESTResponse(false, "Error connecting to PHR, try again later");
			
		} catch (JSONException e) {
			logger.error("Convert PHRInvite to JSON error", e);
			return new GenericRESTResponse(false, "Error connecting to PHR, try again later");
		}
		
	}
	
	@POST
	@Path("/createPHRAccount")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse createProviderPHRAccount(@Context HttpServletRequest request,PHRAccount phrAccount){
		
		String password = RegistrationHelper.getNewRandomPassword();
		String providerNo = getLoggedInInfo().getLoggedInProviderNo();
		phrAccount.setPassword(password);
		phrAccount.setRole("PROVIDER");
		phrAccount.setProviderNo(getLoggedInInfo().getLoggedInProviderNo());

		try {
			Response response = callPHR("/createUser", providerNo, phrAccount.toJson().toString());
			logger.error("stat "+response.getStatus()+" == "+Response.Status.CREATED.getStatusCode()+" ==== "+(response.getStatus()==Response.Status.CREATED.getStatusCode()));
			
			
			if (response.getStatus()==Response.Status.CREATED.getStatusCode()) {
				try {
				String username = (String) response.getEntity(); 
				//Save username to properties
				ProviderMyOscarIdData.setId(providerNo, username);
				
				//Save Password
				SecretKeySpec key=MyOscarUtils.getDeterministicallyMangledPasswordSecretKeyFromSession(request.getSession());
		        byte[] encryptedMyOscarPassword=EncryptionUtils.encrypt(key, password.getBytes("UTF-8"));

		        ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
		        ProviderPreference providerPreference=providerPreferenceDao.find(providerNo);
		        providerPreference.setEncryptedMyOscarPassword(encryptedMyOscarPassword);
		        providerPreferenceDao.merge(providerPreference);
				
				
				return new GenericRESTResponse(true, "Account Registered");	
				}catch(Exception e) {
					logger.error("Error creating account ",e);
				}
				
			} 
			return new GenericRESTResponse(false, "Error connecting to PHR, try again later");
			
		} catch (JSONException e) {
			logger.error("Convert PHRInvite to JSON error", e);
			return new GenericRESTResponse(false, "Error connecting to PHR, try again later");
		}
		
	}
	
	
	@GET
	@Path("/providerLaunchItems")
	public Response providerLaunchItems(@Context HttpServletRequest request,@Context HttpServletResponse resp){
		String providerNo = getLoggedInInfo().getLoggedInProviderNo();
		Response response = callPHR("/getProviderLaunchItems", providerNo,providerNo);
		if(response.getStatus() == Response.Status.OK.getStatusCode()) {
			return Response.ok(response.getEntity()).build();
		}
		return  Response.noContent().build();
	}
	
	Object chartLaunchItems  = null;
	@GET
	@Path("/providerChartLaunchItems")
	@Produces("application/json")
	public Response providerChartLaunchItems(@Context HttpServletRequest request,@Context HttpServletResponse resp){
		String providerNo = getLoggedInInfo().getLoggedInProviderNo();
		if( appManager.hasAppDefinition(getLoggedInInfo(), "PHR")){
			if(chartLaunchItems!= null) {
				return Response.ok(chartLaunchItems).build();
			}
			Response response = callPHR("/providerChartLaunchItems", providerNo,providerNo);
			
			if(response.getStatus() == Response.Status.OK.getStatusCode()) {
				chartLaunchItems = response.getEntity();
				return Response.ok(chartLaunchItems).build();
			}
		}
		return  Response.noContent().build();
	}
	
	@GET
	@Path("/openPHRWindow/{windowName}")
	public Response openPHRWindow(@Context HttpServletRequest request,@Context HttpServletResponse response,@PathParam("windowName") String windowName) throws IOException{
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		String redirectUrl = callPHRWindowOpen("openWindow",getLoggedInInfo().getLoggedInProviderNo(), windowName ,"admin");
		
		logger.debug("URL for open window " +windowName+" url-- "+redirectUrl );
		
		response.sendRedirect(redirectUrl);
		return Response.status(Status.ACCEPTED).build();
	}
	
	@GET
	@Path("/openProviderPHRWindow/{windowName}")
	public Response openProviderPHRWindow(@Context HttpServletRequest request,@Context HttpServletResponse response,@PathParam("windowName") String windowName) throws IOException{
		
		String redirectUrl = callPHRWindowOpen("openWindow",getLoggedInInfo().getLoggedInProviderNo(), windowName ,"provider");
		
		logger.debug("URL for open window " +windowName+" url-- "+redirectUrl );
		
		response.sendRedirect(redirectUrl);
		return Response.status(Status.ACCEPTED).build();
	}
	
	@POST
	@Path("/K2AInit/")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse initK2A(JSONObject clinicName,@Context HttpServletRequest request){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}

		if(appManager.getAppDefinition(getLoggedInInfo(), "K2A") != null){
			throw new RuntimeException("K2A Already Initialized");
		}
		
		String name = (String) clinicName.get("name");
		if (name==null || name.trim().isEmpty()){
			throw new RuntimeException("Invalid clinic name ["+name+"]");
		}
		
		URL url;
	    HttpURLConnection connection = null;  
	    
	    clinicName.accumulate("url", request.getRequestURL().toString());
					
	    try {
		      //Create connection
	      url = new URL(OscarProperties.getInstance().getProperty("K2A_URL","https://www.know2act.org/ws/rs/localoauth/new"));
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");   
	      connection.setRequestProperty("Content-Type", "application/json");
	      connection.setRequestProperty("Content-Length", "" +Integer.toString(clinicName.toString().getBytes().length));
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	      logger.info("k2a json "+clinicName.toString());
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (clinicName.toString());
	      wr.flush ();
	      wr.close ();

		      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuilder responseBuffer = new StringBuilder(); 
	      while((line = bufferedReader.readLine()) != null) {
	    	  responseBuffer.append(line);
	      }
	      bufferedReader.close();
	      String response  = responseBuffer.toString();
		  
	      //Check if response is valid
	      
	      AppDefinition k2aNew = new AppDefinition();
		      
	      k2aNew.setActive(true);
	      k2aNew.setAdded(new Date());
	      k2aNew.setAppType(AppDefinition.OAUTH1_TYPE);
	      k2aNew.setName("K2A");
	      k2aNew.setConfig(response);
	      k2aNew.setAddedBy(getLoggedInInfo().getLoggedInProviderNo());
	      appManager.saveAppDefinition(getLoggedInInfo(),  k2aNew);

	    } catch (Exception e) {
	    	logger.error("Error processing K2A init",e);
	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
		return  new GenericRESTResponse(false,"K2A active");
	}
	
	@POST
	@Path("/comment")
	@Consumes("application/json")
	@Produces("application/json")
	public org.oscarehr.ws.rest.to.RSSResponse postK2AComment(RssItem comment) {
		RSSResponse response = new RSSResponse();
		response.setTimestamp(new Date());
		try {
			AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
	    	AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
	    		
	    	AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			
	    	if(k2aApp != null) {
		    	AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),getLoggedInInfo().getLoggedInProviderNo());
		    		
		    	if(k2aUser != null) {
		    		String jsonString = OAuth1Utils.getOAuthPostResponse(getLoggedInInfo(),k2aApp, k2aUser, "/ws/api/posts/comment", "/ws/api/posts/comment", OAuth1Utils.getProviderK2A(), comment);
			    		
			    	if(jsonString != null && !jsonString.isEmpty()) {
			    		org.codehaus.jettison.json.JSONObject post = new org.codehaus.jettison.json.JSONObject(jsonString);
	    	        	
	    	        	RssItem commentItem = new RssItem();
	    	        	commentItem.setId(Long.parseLong(post.getString("id")));
	    	        	commentItem.setAuthor(post.getString("author"));
	    				Date date = null;
	    				if(post.has("createdAt")) {
		    				try {
			    				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	    	        			date = formatter.parse(post.getString("createdAt"));
		    				} catch(ParseException e) {
		    					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	    	        			date = formatter.parse(post.getString("createdAt"));
		    				}
	    				}
	    				commentItem.setPublishedDate(date);
	    				commentItem.setBody(post.getString("body"));
	    				if(post.has("agree")) {
	    					commentItem.setAgree(post.getBoolean("agree"));
	    				}
	    				if(post.has("agreeId")) {
	    					commentItem.setAgreeId(Long.parseLong(post.getString("agreeId")));
	    				}
	    				response.getContent().add(commentItem);
			    	}
			    	response.setTotal(response.getContent().size());
		    	}
	    	}
		} catch(Exception e) {
			logger.error("error",e);
			return null;
		}
		return response;
	}
	
	@DELETE
	@Path("/comment/{commentId}")
	@Consumes("application/json")
	@Produces("application/json")
	public org.oscarehr.ws.rest.to.RSSResponse removeK2AComment(@PathParam("commentId") String commentId) {
		RSSResponse response = new RSSResponse();
		response.setTimestamp(new Date());
		try {
			AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
	    	AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
	    		
	    	AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			
	    	if(k2aApp != null) {
		    	AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),getLoggedInInfo().getLoggedInProviderNo());
		    		
		    	if(k2aUser != null) {
		    		OAuth1Utils.getOAuthDeleteResponse(k2aApp, k2aUser, "/ws/api/posts/comment/" + commentId, "/ws/api/posts/comment/" + commentId);
		    	}
	    	}
		} catch(Exception e) {
			logger.error("error",e);
			return null;
		}
		return response;
	}
}
