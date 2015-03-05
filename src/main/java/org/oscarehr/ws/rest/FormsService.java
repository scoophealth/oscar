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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils;
import org.oscarehr.app.AppOAuth1Config;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.EFormDao.EFormSortOrder;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.conversion.EFormConverter;
import org.oscarehr.ws.rest.conversion.EncounterFormConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.model.EFormTo1;
import org.oscarehr.ws.rest.to.model.EncounterFormTo1;
import org.oscarehr.ws.rest.to.model.FormListTo1;
import org.oscarehr.ws.rest.to.model.FormTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import oscar.eform.EFormExportZip;
import oscar.oscarEncounter.data.EctFormData;


/**
 * Service for interacting with forms (eforms and sql table forms in oscar) 
 */
@Path("/forms")
@Component("formsService")
public class FormsService extends AbstractServiceImpl {
	@Autowired
	private FormsManager formsManager;
	
	@Autowired
	private AppDefinitionDao appDefinitionDao;
	
	@GET
	@Path("/{demographicNo}/all")
	@Produces("application/json")
	public FormListTo1 getFormsForHeading(@PathParam("demographicNo") Integer demographicNo ,@QueryParam("heading") String heading){
		FormListTo1 formListTo1 = new FormListTo1();
		if(heading.equals("Completed")){
			List<EFormData> completedEforms = formsManager.findByDemographicId(getLoggedInInfo(),demographicNo);
			Collections.sort(completedEforms, Collections.reverseOrder(EFormData.FORM_DATE_COMPARATOR));
			
			for(EFormData eformData: completedEforms){	
				int id = eformData.getId();
				int formId = eformData.getFormId();
				String name = eformData.getFormName();
				String subject = eformData.getSubject();
				String status = eformData.getSubject();
				Date date = eformData.getFormDate();
				Boolean showLatestFormOnly = eformData.isShowLatestFormOnly();
				formListTo1.add(FormTo1.create(id, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
			}
			
		}else{  // Only two options right now.  Need to change this anyways
			List<EForm> eforms =  formsManager.findByStatus(getLoggedInInfo(),true, null);  //This will have to change to accommodate forms too.
			Collections.sort(eforms,EForm.FORM_NAME_COMPARATOR);
			for(EForm eform :eforms){
				int formId = eform.getId();
				String name = eform.getFormName();
				String subject = eform.getSubject();
				String status = null;
				Date date = null;
				Boolean showLatestFormOnly = eform.isShowLatestFormOnly();
				formListTo1.add(FormTo1.create(null, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
			}
		}
		return formListTo1;
	}

	@GET
	@Path("/allEForms")
	@Produces("application/json")
	public AbstractSearchResponse<EFormTo1> getAllEFormNames(){
		AbstractSearchResponse<EFormTo1> response = new AbstractSearchResponse<EFormTo1>();
		response.setContent(new EFormConverter(true).getAllAsTransferObjects(getLoggedInInfo(),formsManager.findByStatus(getLoggedInInfo(), true, EFormSortOrder.NAME)));
		response.setTotal(response.getContent().size());
		return response;
		
	}
	
	@GET
	@Path("/allEncounterForms")
	@Produces("application/json")
	public AbstractSearchResponse<EncounterFormTo1> getAllFormNames(){
		AbstractSearchResponse<EncounterFormTo1> response = new AbstractSearchResponse<EncounterFormTo1>();
		response.setContent(new EncounterFormConverter().getAllAsTransferObjects(getLoggedInInfo(),formsManager.getAllEncounterForms()));
		response.setTotal(response.getContent().size());
		return response;
		
	}
	
	@GET
	@Path("/selectedEncounterForms")
	@Produces("application/json")
	public AbstractSearchResponse<EncounterFormTo1> getSelectedFormNames(){
		AbstractSearchResponse<EncounterFormTo1> response = new AbstractSearchResponse<EncounterFormTo1>();
		response.setContent(new EncounterFormConverter().getAllAsTransferObjects(getLoggedInInfo(),formsManager.getSelectedEncounterForms()));
		response.setTotal(response.getContent().size());
		return response;
		
	}
	
		
	@GET
	@Path("/{demographicNo}/completedEncounterForms")
	@Produces("application/json")
	public FormListTo1 getCompletedFormNames(@PathParam("demographicNo") String demographicNo){
		FormListTo1 formListTo1 = new FormListTo1();

		List<EncounterForm> encounterForms = formsManager.getAllEncounterForms();
		Collections.sort(encounterForms, EncounterForm.BC_FIRST_COMPARATOR);

		for (EncounterForm encounterForm : encounterForms) {
			String table = StringUtils.trimToNull(encounterForm.getFormTable());
			if (table != null) {
			
				EctFormData.PatientForm[] pforms = EctFormData.getPatientFormsFromLocalAndRemote(getLoggedInInfo(), demographicNo, table);
				int formId = 0;
				String name = encounterForm.getFormName();
				
				if (pforms.length > 0) {
				
					EctFormData.PatientForm pfrm = pforms[0];
					formId = Integer.parseInt(pfrm.getFormId());
					Date date;
					
					//d-MMM-y
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
					String dateStr = pfrm.getCreated();
					try {
						date = formatter.parse(dateStr);
					} catch (ParseException ex) {
						date = null;
					}
                                   
					formListTo1.add(FormTo1.create(null, Integer.parseInt(demographicNo), formId, FormsManager.FORM, name, null, null, date, false ));

				}

			}
		}
		
		return formListTo1;
	}
	
	@GET
	@Path("/groupNames")
	@Produces("application/json")
	public AbstractSearchResponse<String> getGroupNames(){
		AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();

		response.setContent(formsManager.getGroupNames());
		response.setTotal(response.getContent().size());
		return response;
		
	}
	
	@POST
	@Path("/getK2AEForm")
	@Consumes("application/json")
	@Produces("application/json")
	public AbstractSearchResponse<String> getK2AEForm(String id) {
		AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();
		MiscUtils.getLogger().info("EForm id is: " + id);
		
        try {
        	AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
        	AppOAuth1Config appAuthConfig = AppOAuth1Config.fromDocument(k2aApp.getConfig());
		    URL url = new URL(appAuthConfig.getBaseURL() + "/download/eform/" + id);
		    url.openStream();
		    EFormExportZip eFormExportZip = new EFormExportZip();
		    List<String> errors = eFormExportZip.importForm(url.openStream());
		    if(errors != null) {
			    response.setContent(errors);
			}
			response.setTotal(1);
        } catch (Exception e) {
        	MiscUtils.getLogger().error("Error parsing data - " + e);
	        return null;
        }
        return response;
	}
	
	@POST
	@Path("/getAllK2AEForms")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public AbstractSearchResponse<String> getAllK2AEForms(String jsonString) {
		try {
			AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();
			JSONArray jsonArray = JSONArray.fromObject(jsonString);
			List<String> errors = new ArrayList<String>();
			int totalEFormsProcessed = 0;
			
			AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			AppOAuth1Config appAuthConfig = AppOAuth1Config.fromDocument(k2aApp.getConfig());
			
	        for (int i = 0; i < jsonArray.size(); i++) {
	        		JSONObject eform = jsonArray.getJSONObject(i);
				    URL url = new URL(appAuthConfig.getBaseURL() + "/download/eform/" + eform.getString("id"));
			        
					url.openStream();
					EFormExportZip eFormExportZip = new EFormExportZip();
					List<String> eformErrors = eFormExportZip.importForm(url.openStream());
					if(eformErrors != null) {
					    errors.add("failed to upload eform: " + eform.getString("name") + ", eform may already exist in OSCAR.");
					}
					totalEFormsProcessed++;
	        }
			response.setTotal(totalEFormsProcessed);
	        response.setContent(errors);
	        return response;
		} catch (Exception e) {
            MiscUtils.getLogger().error("Error parsing data - " + e);
    	    return null;
        }
	}
	
	public static String getK2AEFormsList(AppDefinition k2aApp, AppUser k2aUser) {
		try {
			AppOAuth1Config appAuthConfig = AppOAuth1Config.fromDocument(k2aApp.getConfig());
			Map<String,String> keySecret = AppOAuth1Config.getKeySecret(k2aUser.getAuthenticationData());
			
			OAuthClientUtils.Consumer consumer = new OAuthClientUtils.Consumer(appAuthConfig.getConsumerKey(),appAuthConfig.getConsumerSecret());
			OAuthClientUtils.Token accessToken = new OAuthClientUtils.Token(keySecret.get("key"),keySecret.get("secret"));
			String method = "GET";
			String requestURI = appAuthConfig.getBaseURL() + "/ws/api/eforms/getEForms";
			
			WebClient webclient = WebClient.create(requestURI);
			
			webclient = webclient.replaceHeader("Authorization",OAuthClientUtils.createAuthorizationHeader(consumer, accessToken, method, requestURI));
			
			javax.ws.rs.core.Response reps = webclient.get();
			
			InputStream in = (InputStream) reps.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch(Exception e) {
			return null;
		}
	}
}
