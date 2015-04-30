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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.model.AppDefinitionTo1;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/app")
public class AppService extends AbstractServiceImpl {
	protected Logger logger = MiscUtils.getLogger();
	
	@Autowired
	AppManager appManager;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	
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
	
	@POST
	@Path("/K2AInit/")
	public void initK2A(String clinicName){
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_appDefinition", "w", null)) {
			throw new RuntimeException("Access Denied");
		}

		if(appManager.getAppDefinition(getLoggedInInfo(), "K2A") != null){
			throw new RuntimeException("K2A Already Initialized");
		}
		
		URL url;
	    HttpURLConnection connection = null;  
					
	    try {
		      //Create connection
	      url = new URL("http://www.know2act.org:80/ws/rs/localoauth/new");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");   
	      connection.setRequestProperty("Content-Type", "application/json");
	      connection.setRequestProperty("Content-Length", "" +Integer.toString(clinicName.getBytes().length));
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (clinicName);
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
		
	}
	
}
