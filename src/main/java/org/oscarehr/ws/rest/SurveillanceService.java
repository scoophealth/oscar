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


import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.dao.ResourceStorageDao;
import org.oscarehr.common.dao.SurveillanceDataDao;
import org.oscarehr.common.jobs.OscarJobUtils;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.common.model.ResourceStorage;
import org.oscarehr.common.model.SurveillanceData;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.OscarJobManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import org.springframework.beans.factory.annotation.Autowired;


import oscar.log.LogAction;
import oscar.oscarPrevention.PreventionDS;

import oscar.oscarSurveillance.ProcessSurveyFile;
import oscar.oscarSurveillance.SurveillanceMaster;
import oscar.oscarSurveillance.Survey;


@Path("/surveillance")
public class SurveillanceService extends AbstractServiceImpl {
	private static final Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	@Autowired
	private AppDefinitionDao appDefinitionDao;
	
	@Autowired
	AppManager appManager;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	
	@Autowired
	private PreventionDS preventionDS;
	
	@Autowired
	SurveillanceDataDao surveillanceDataDao;
	
	@Autowired
	OscarJobManager oscarJobManager;
	

	
	private String getResource(LoggedInInfo loggedInInfo,String requestURI, String baseRequestURI) {
		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
		if(k2aApp != null) {
			AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
			
			if(k2aUser != null) {
				String retString = OAuth1Utils.getOAuthGetResponse(loggedInInfo,k2aApp, k2aUser, requestURI, baseRequestURI);
				logger.debug("returned string from "+requestURI+": "+retString);
				return retString;
			} else {
				return null;
			}
		}
		return null;
	}
	

	@GET
	@Path("/allLoadedSurveillanceConfigs")
	@Produces("application/json")
	public JSONArray getSurveillanceConfigs(){
		JSONArray retArr = new JSONArray();
		List<ResourceStorage> loadedResources=  resourceStorageDao.findAll(ResourceStorage.SURVEILLANCE_CONFIGURATION);
		
		for(ResourceStorage res:loadedResources) {
			JSONObject jobject = new JSONObject();
			jobject.put("name",res.getResourceName());
			if(res.getUploadDate() != null) {
				jobject.put("date",res.getUploadDate().getTime());
			}else {
				jobject.put("date","");
			}
			jobject.put("id",res.getId());
			jobject.put("active",res.isActive());
			jobject.put("uuid",res.getUuid());
			retArr.add(jobject);
		}
		return retArr;
	}
	
	@POST
	@Path("/getSurvey/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Survey getSurvey(@PathParam("id") Integer id) {
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		Survey survey = null;
		try {
			survey = Survey.createSurvey(resourceStorage.getFileContents());
		} catch (Exception e) {
			logger.error("getSurvey",e);
		} 
		return survey;
	}	
	
	@POST
	@Path("/updateSurvey/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Survey updateSurvey(@PathParam("id") Integer id,Survey newProviderList) {
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		Survey survey = null;
		try {
			survey = Survey.createSurvey(resourceStorage.getFileContents());
			survey.setProviderList(newProviderList.getProviderList());
			byte[] surveyByteArr = Survey.toBytes(survey);
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.updateSurvey", "ResourceStorage",""+id,loggedInInfo.getIp(), null,Survey.toXmlString(survey) );
			resourceStorage.setFileContents(surveyByteArr);
			resourceStorageDao.merge(resourceStorage);
			if(resourceStorage.isActive()){  // Only need to reload if storage is active.
				SurveillanceMaster.reInit();
			}
		} catch (Exception e) {
			logger.error("updateSurvey",e);
		} 
		return survey;
	}	
	
	@POST
	@Path("/enableResource/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response enableResource(@PathParam("id") Integer id) {
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		if(!resourceStorage.isActive()){
			resourceStorage.setActive(true);
			resourceStorageDao.merge(resourceStorage);
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.enableResource", "ResourceStorage",""+resourceStorage.getId(),loggedInInfo.getIp(), null,null);
			SurveillanceMaster.reInit();
			return Response.ok(true).build();
		}
		return Response.ok(false).build();
	}
	
	@POST
	@Path("/disableResource/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response disableResource(@PathParam("id") Integer id) {
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		if(resourceStorage.isActive()){
			resourceStorage.setActive(false);
			resourceStorageDao.merge(resourceStorage);
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.disableResource", "ResourceStorage",""+resourceStorage.getId(),loggedInInfo.getIp(), null,null);
			SurveillanceMaster.reInit();
			//SurveillanceMaster.displaySurveys();
			return Response.ok(true).build();
		}
		return Response.ok(false).build();
	}

	
	@POST
	@Path("/generateExport/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response generateExport(@PathParam("id") Integer id) throws Exception{
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		Survey survey = Survey.createSurvey(resourceStorage.getFileContents());
		ProcessSurveyFile.processSurveyFile(survey.getSurveyId());
		
		if(resourceStorage.isActive()){
			
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.generateExport", "ResourceStorage",""+resourceStorage.getId(),loggedInInfo.getIp(), null,null);

			return Response.ok(true).build();
		}
		return Response.ok(false).build();
	}
	
	@POST
	@Path("/setExportAsSent/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response setExportAsSent(@PathParam("id") Integer id) throws Exception{
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		SurveillanceData surveyData = surveillanceDataDao.find(id);
		
		surveyData.setSent(true);
		surveyData.setTransmissionDate(new Date());
		
		surveillanceDataDao.merge(surveyData);
		LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.setExportAsSent", "SurveillanceData",""+surveyData.getId(),loggedInInfo.getIp(), null,null);

		return Response.ok(surveyData).build();
	}
	
	
	
	@GET
	@Path("/getExportFiles/{id}")
	@Produces("application/json")
	public JSONArray getExportFiles(@Context HttpServletRequest request,@PathParam("id") Integer id) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		JSONArray retArray = new JSONArray();
		try {
			ResourceStorage resourceStorage=  resourceStorageDao.find(id);
			
			Survey survey = Survey.createSurvey(resourceStorage.getFileContents());
			List<SurveillanceData> surveyData = surveillanceDataDao.findExportDataBySurveyId(survey.getSurveyId());
			
			for(SurveillanceData sd: surveyData){
				JSONObject jobject = new JSONObject();
				jobject.put("id", sd.getId());
				jobject.put("name", sd.getId()+"-"+sd.getCreateDate().getTime());
				jobject.put("createDate", sd.getCreateDate().getTime());
				if(sd.getTransmissionDate() != null) {
				jobject.put("transmissionDate", sd.getTransmissionDate().getTime());
				}
				jobject.put("sent", sd.isSent());
				retArray.add(jobject);
			}
		}catch(Exception e) {
			logger.error("error getting expoprt files",e);
			JSONObject jobject = new JSONObject();
			jobject.put("id", 0);
			jobject.put("name", "Failed to retrieve export files");
			jobject.put("createDate", new Date().getTime());
			
			jobject.put("sent", false);
			retArray.add(jobject);
		}
		return retArray;
	}
	
	
	
	@POST
	@Path("/createJob/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response createJob(@PathParam("id") Integer id,JSONObject uandp) throws Exception{
		ResourceStorage resourceStorage=  resourceStorageDao.find(id);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		Survey survey = Survey.createSurvey(resourceStorage.getFileContents());
		
	    uandp.accumulate("domain", survey.getExportDomain());
	    uandp.accumulate("port", survey.getExportPort());
	    uandp.accumulate("surveyId",survey.getSurveyId());
		
		String config = uandp.toString();
		
		if(resourceStorage.isActive()){
			
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.generateExport", "ResourceStorage",""+resourceStorage.getId(),loggedInInfo.getIp(), null,null);
			
			if("FTPS".equals(survey.getExportMethod())) {
				
				List<OscarJob> jobList =  oscarJobManager.getJobByName(loggedInInfo,"Surveillance "+survey.getSurveyId());
				OscarJob job = null;
				if(jobList.isEmpty()) {					
					OscarJobType oscarJobType = oscarJobManager.addIfNotLoaded(loggedInInfo,OscarJobManager.getFTPSJob());
					job = new OscarJob();
					job.setOscarJobTypeId(oscarJobType.getId());
					job.setEnabled(true);
					job.setProviderNo(loggedInInfo.getLoggedInProviderNo());;
					job.setName("Surveillance "+survey.getSurveyId());
					job.setDescription("Data submission :"+survey.getSurveyTitle());
					job.setUpdated(new Date());
					job.setOscarJobType(null);
					job.setConfig(config);
					job.setCronExpression("0 "+getRandomMinute()+" 6 ? * MON");
					oscarJobManager.saveJob(getLoggedInInfo(),job);
				}else {
					job = jobList.get(0);
					logger.debug("OscarJob found :"+job.getId());
					job.setConfig(config);
					job.setUpdated(new Date());
					job.setEnabled(true);
					job.setCronExpression("0 "+getRandomMinute()+" 6 ? * MON");
					oscarJobManager.updateJob(loggedInInfo, job);

				}
				OscarJobUtils.scheduleJob(job);
				
			}

			return Response.ok(true).build();
		}
		return Response.ok(false).build();
	}
	
	
	private int getRandomMinute() {
		Random r = new Random();
		int low = 1;
		int high = 59;
		int result = r.nextInt(high-low) + low;
		return result;
	}
	
	private Date getReferenceDate(String s) {
		Date referenceDate = null;
		SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		try {
			referenceDate = parser.parse(s);
			
		}catch(Exception e) {
			SimpleDateFormat parser2=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
			try {
				referenceDate = parser2.parse(s);
			}catch(Exception e2) {
				logger.error("error parsing "+s,e2);
			}
		}
		return referenceDate;
	}
	
	@GET
	@Path("/surveillanceConfigList")
	@Produces("application/json")
	public JSONArray getSURVEILLANCECONFIGFileListFromK2A(@Context HttpServletRequest request) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		JSONArray retArray = new JSONArray();
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/SURVEILLANCE_CONFIG/list", "/ws/api/oscar/get/SURVEILLANCE_CONFIG/list"); 
			JSONArray rulesArray = JSONArray.fromObject(resource);
			 
			for(int i = 0; i < rulesArray.size(); i++){
				JSONObject jobject = new JSONObject();
				JSONObject rule = (JSONObject) rulesArray.get(i);
				jobject.put("id", rule.getString("id"));
				jobject.put("name", rule.getString("name"));
				jobject.put("xml", rule.getString("body"));
				jobject.put("created_at", rule.getString("created_at"));
				jobject.put("updated_at", rule.getString("updatedAt"));
				jobject.put("author", rule.getString("author"));
				jobject.put("uuid", rule.optString("uuid"));
				String uuid = rule.optString("uuid",null);
				if(uuid != null) {
				//look if uuid is active
					List<ResourceStorage> resList = resourceStorageDao.findByUUID(uuid);
					if(resList.size() > 0) {
						jobject.put("resourceId",resList.get(0).getId());
					
						
						
						Date referenceDate = getReferenceDate(rule.getString("updatedAt"));
						java.util.Calendar referenceCal = java.util.Calendar.getInstance();
						referenceCal.setTime(referenceDate);
						referenceCal.set(java.util.Calendar.MILLISECOND,0);
						if(resList.get(0).getReferenceDate() == null || referenceCal.getTime().after(resList.get(0).getReferenceDate())) {
							jobject.put("resourceNew",true);
						}else {
							jobject.put("resourceNew",false);
						}
						
					}
				}
				
				retArray.add(jobject);
			}
			
		} catch(Exception e) {
			logger.error("Error retrieving prevention list",e);
			return null;
		}
		
		
		return retArray;
	}
	
	
	
	
	@POST
	@Path("/addSurveyFromK2A/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addSurveyFromK2A(@PathParam("id") String id,JSONObject jSONObject) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}

		String newId = "0";
		try {
			LoggedInInfo loggedInInfo = getLoggedInInfo();
			
			//Log agreement
			if(jSONObject.containsKey("agreement")){
				String action = "oauth1_AGREEMENT";
		    	String content = "SURVEILLANCE_CONFIG_AGREEMENT";
		    	String contentId = id;
		    	String demographicNo = null;
		    	String data = jSONObject.getString("agreement");
		    	LogAction.addLog(loggedInInfo, action, content, contentId, demographicNo, data);
			}
			
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/SURVEILLANCE_CONFIG/id/"+id, "/ws/api/oscar/get/SURVEILLANCE_CONFIG/id/"+id); 
			
			if(resource !=null){
				ResourceStorage resourceStorage = new ResourceStorage();
				resourceStorage.setActive(false);  // Don't active surveys by default, users still need to select which users will participate
				resourceStorage.setResourceName(jSONObject.getString("name"));
				resourceStorage.setResourceType(ResourceStorage.SURVEILLANCE_CONFIGURATION);
				resourceStorage.setUuid(null);
				if(jSONObject.containsKey("uuid")){
					resourceStorage.setUuid(jSONObject.getString("uuid"));
				}
				Date date = new Date();
				resourceStorage.setUploadDate(date);
				resourceStorage.setUpdateDate(date);
				
				Date referenceDate = getReferenceDate(jSONObject.getString("updated_at"));
				resourceStorage.setReferenceDate(referenceDate);
				
				Survey newSurvey = Survey.createSurvey(jSONObject.getString("xml"));
				resourceStorage.setFileContents(Survey.toBytes(newSurvey));
				resourceStorageDao.persist(resourceStorage);
				newId = ""+resourceStorage.getId();
				LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.addSurveyFromK2A", "ResourceStorage",newId,loggedInInfo.getIp(), null,new String(resource.getBytes()) );
			}
			
		} catch(Exception e) {
			logger.error("Error saving Resource to Storage",e);
		}
		return Response.ok().entity(newId).build();
	}
		
	@POST
	@Path("/updateSurveyFromK2A/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateSurveyFromK2A(@PathParam("id") String id,JSONObject jSONObject) {
		if (!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_admin", "r", null) && !securityInfoManager.hasPrivilege(getLoggedInInfo(), "_report", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
    		/// THIS WAS COPY AND PASTED FROM ABOVE/   NEEDS TO BE MODIFIED TO FOR SURVEYS
		String newId = "0";
		try {
			LoggedInInfo loggedInInfo = getLoggedInInfo();
			
			//Log agreement
			if(jSONObject.containsKey("agreement")){
				String action = "oauth1_AGREEMENT";
		    	String content = "SURVEILLANCE_CONFIG_AGREEMENT";
		    	String contentId = id;
		    	String demographicNo = null;
		    	String data = jSONObject.getString("agreement");
		    	LogAction.addLog(loggedInInfo, action, content, contentId, demographicNo, data);
			}
			
			String resource = getResource(loggedInInfo,"/ws/api/oscar/get/SURVEILLANCE_CONFIG/id/"+id, "/ws/api/oscar/get/SURVEILLANCE_CONFIG/id/"+id); 
			if(resource !=null){
				ResourceStorage resourceStorage = resourceStorageDao.find(jSONObject.getInt("resourceId"));
				//resourceStorage.setActive(false);  // Don't active surveys by default, users still need to select which users will participate
				resourceStorage.setResourceName(jSONObject.getString("name"));
				resourceStorage.setResourceType(ResourceStorage.SURVEILLANCE_CONFIGURATION);
				resourceStorage.setUuid(null);
				if(jSONObject.containsKey("uuid")){
					resourceStorage.setUuid(jSONObject.getString("uuid"));
				}
				Date date = new Date();
				// resourceStorage.setUploadDate(date); keep upload date as the original date
				resourceStorage.setUpdateDate(date);
				
				Date referenceDate = getReferenceDate(jSONObject.getString("updated_at"));
				resourceStorage.setReferenceDate(referenceDate);
				
				//Need to migrate participants from the running surviellance
				
				Survey currentSurvey = Survey.createSurvey(resourceStorage.getFileContents());
				Survey newSurvey = Survey.createSurvey(jSONObject.getString("xml"));//resource.getBytes());
				newSurvey.setProviderList(currentSurvey.getProviderList());
				

				resourceStorage.setFileContents(Survey.toBytes(newSurvey));
				resourceStorageDao.merge(resourceStorage);
				SurveillanceMaster.reInit();
				SurveillanceMaster.displaySurveys();
				newId = ""+resourceStorage.getId();

				LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.updateSurveyFromK2A", "ResourceStorage",newId,loggedInInfo.getIp(), null,new String(resource.getBytes()) );

			}
			
		} catch(Exception e) {
			logger.error("Error saving Resource to Storage",e);
		}
		return Response.ok().entity(newId).build();
	}
	
	
	@GET
	@Path("/download/{id}")
	@Produces("text/plain")
	public StreamingOutput download(@PathParam("id") Integer id,@Context HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		SurveillanceData surveyData = surveillanceDataDao.find(id);
		LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), "SurveillanceService.download", "SurveillanceData",""+surveyData.getId(),loggedInInfo.getIp(), null,null);
			
		final byte[] arr = surveyData.getData();
		String filename = surveyData.getId()+"-"+surveyData.getCreateDate().getTime();
		response.setHeader("Content-Disposition", "attachment; filename=\""+filename+".csv\"");
		
		return new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream os)
					throws IOException, WebApplicationException {
				try{
					IOUtils.write(arr,os);
		        }catch(Exception e){
		        		logger.error("error streaming",e);
		        }finally{
		        	IOUtils.closeQuietly(os);
		        }
				
			}  
	    };
	}
		

	
}