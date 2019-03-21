/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.OLISResultsDao;
import org.oscarehr.common.model.OLISResults;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.indivica.olis.Driver;
import com.indivica.olis.queries.Query;
import com.indivica.olis.queries.QueryType;
import com.indivica.olis.queries.Z01Query;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class OLISResultsAction extends DispatchAction {

	public static HashMap<String, OLISHL7Handler> searchResultsMap = new HashMap<String, OLISHL7Handler>();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	protected OLISResultsDao olisResultsDao = SpringUtils.getBean(OLISResultsDao.class);
	protected ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "r", null)) {
        	throw new SecurityException("missing required security object (_lab)");
        }
		
		try {
			String olisResultString = (String) request.getAttribute("olisResponseContent");			
			olisResultString = (String)request.getSession().getAttribute("olisResponseContent");
			if(olisResultString == null && "no".equals(OscarProperties.getInstance().getProperty("olis_simulate","no"))) {
				olisResultString = oscar.Misc.getStr(request.getParameter("olisResponseContent"), "");
				request.setAttribute("olisResponseContent", olisResultString);
				
				String olisXmlResponse = oscar.Misc.getStr(request.getParameter("olisXmlResponse"), "");
				if (olisResultString.trim().equalsIgnoreCase("")) {
					if (!olisXmlResponse.trim().equalsIgnoreCase("")) {
						Driver.readResponseFromXML(LoggedInInfo.getLoggedInInfoFromSession(request), request, olisXmlResponse);
					}
					
					List<String> resultList = new LinkedList<String>();
					request.setAttribute("resultList", resultList);				
					return mapping.findForward("results");
				}
			}
			
			UUID uuid = UUID.randomUUID();
			
			Query query = (Query)request.getSession().getAttribute("olisResponseQuery");

			File tempFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + uuid.toString() + ".response");
			FileUtils.writeStringToFile(tempFile, olisResultString);

			
			@SuppressWarnings("unchecked")
            ArrayList<String> messages = Utilities.separateMessages(System.getProperty("java.io.tmpdir") + "/olis_" + uuid.toString() + ".response");
			
			List<String> resultList = new LinkedList<String>();
			
			if (messages != null) {
				for (String message : messages) {
					
					if(StringUtils.isEmpty(message)) {
						continue;
					}
					String resultUuid = UUID.randomUUID().toString();
										
					tempFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid.toString() + ".response");
					FileUtils.writeStringToFile(tempFile, message);
					
					
					//String messageNoMSH = message.replaceAll("^MSH.*[\\r\\n]+", "");
					//String hash = DigestUtils.md5Hex(messageNoMSH); //need to remove the MSH line i guess
					
					MessageHandler h = Factory.getHandler("OLIS_HL7", message);
					
					
					if(!olisResultsDao.hasExistingResult(query.getRequestingHICProviderNo() != null ? query.getRequestingHICProviderNo() : LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(), query.getQueryType().toString(), h.getAccessionNum())) {
						
						boolean dup2 = OLISUtils.isDuplicate(LoggedInInfo.getLoggedInInfoFromSession(request), new File(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid + ".response"));
						if(!dup2) {
							OLISResults result = new OLISResults();
							result.setHash(h.getAccessionNum());
							result.setProviderNo(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
							result.setQuery(query.getQueryHL7String());
							result.setQueryType(query.getQueryType().toString());
							result.setResults(message);
							//result.setStatus(status);
							result.setUuid(resultUuid);
							if(query.getRequestingHICProviderNo() != null)
								result.setRequestingHICProviderNo(query.getRequestingHICProviderNo());
							else
								result.setRequestingHICProviderNo(result.getProviderNo());
							
							
							Integer demId = MessageUploader.willOLISLabReportMatch(LoggedInInfo.getLoggedInInfoFromSession(request), h.getLastName(),h.getFirstName(), h.getSex(), h.getDOB(), h.getHealthNum());
							if(demId != null) {
								result.setDemographicNo(demId);
							}
							result.setQueryUuid(query.getUuid());
							olisResultsDao.persist(result);
						} else {
							//duplicate from community lab already in OSCAR
						//	LogAction.addLog(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(), "OLIS","DUPLICATE (Community Lab)", uuid.toString() , null);
							logOLISDuplicate(LoggedInInfo.getLoggedInInfoFromSession(request), query, message, uuid.toString());
						}
					} else {
						//duplicate from OLIS - system auto removes/rejects
						logOLISDuplicate(LoggedInInfo.getLoggedInInfoFromSession(request), query, message, uuid.toString());
					}
					 
				}
				
				//create a new result list based on whats in the DB
				List<OLISResults> results = olisResultsDao.getResultList(query.getRequestingHICProviderNo() != null ? query.getRequestingHICProviderNo() : LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(), query.getQueryType().toString());
				for(OLISResults result : results) {
					MessageHandler h = Factory.getHandler("OLIS_HL7", result.getResults());
					searchResultsMap.put(result.getUuid(), (OLISHL7Handler)h);
					resultList.add(result.getUuid());
				}
				
				request.getSession().setAttribute("olisResponseContent", null);
				request.setAttribute("resultList", resultList);
			}

		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't pull out messages from OLIS response.", e);
		}
		return mapping.findForward("results");
	}
	
	public void logOLISDuplicate(LoggedInInfo loggedInInfo, Query query, String message, String resultUuid) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		StringBuilder data = new StringBuilder();
		data.append("Query Date:" + formatter.format(query.getQueryExecutionDate()) + "\n");
		data.append("Query Type:" + query.getQueryType().toString() + "\n");
		
		if(!StringUtils.isEmpty(query.getRequestingHICProviderNo())) {
			Provider reqHic = providerDao.getProviderByPractitionerNo( query.getRequestingHICProviderNo());
			if(reqHic != null) {
				data.append("Requesting HIC:" + reqHic.getFormattedName() + "\n");
			}
		}
		data.append("Initiating Provider: " + providerDao.getProvider(query.getInitiatingProviderNo()).getFormattedName() + "\n");
		data.append("Rejecting User: System (automatic)" + "\n");
		data.append("Rejection Date: " + formatter.format(new Date()) + "\n");
		data.append("Rejection Reason: Duplicate\n");
		data.append("Rejection Type: System\n");
		
		
		OLISHL7Handler h = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", message);
		if(h != null) {
			data.append("Accession: " + h.getAccessionNum() + "\n");
			data.append("Test Request(s): " +  h.getTestList(",") + "\n");
			for(int x=0;x<h.getOBRCount();x++) {
				data.append("Collection Date:" + h.getTimeStamp(x, 1) + "\n");
			}
			for(int x=0;x<h.getOBRCount();x++) {
				data.append("LastUpdate Date:" + h.getLastUpdateDate(x, 1) + "\n");
			}
			
		}
		
		OscarLog oscarLog = new OscarLog();
		oscarLog.setAction("OLIS");
		oscarLog.setContent("DUPLICATE (OLIS)");
		oscarLog.setContentId(resultUuid);
		oscarLog.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		oscarLog.setData(data.toString());
		
		if(query.getQueryType() == QueryType.Z01) {
			String demographicNo = ((Z01Query)query).getDemographicNo();
			if(!StringUtils.isEmpty(demographicNo)) {
				oscarLog.setDemographicId( Integer.parseInt(demographicNo));
			}
		}
		
		LogAction.addLogSynchronous(oscarLog);
		
	}
}
