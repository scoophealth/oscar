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
package org.oscarehr.hospitalReportManager.v2018;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.common.dao.HrmLogDao;
import org.oscarehr.common.dao.HrmLogEntryDao;
import org.oscarehr.common.dao.OscarJobDao;
import org.oscarehr.common.dao.OscarJobTypeDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarJobUtils;
import org.oscarehr.common.model.HrmLog;
import org.oscarehr.common.model.HrmLogEntry;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.hospitalReportManager.HRMReport;
import org.oscarehr.hospitalReportManager.HRMReportParser;
import org.oscarehr.hospitalReportManager.SFTPConnector;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMProviderConfidentialityStatement;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class HRMAction extends DispatchAction {

	static int draw = 0;
	
	Logger logger = MiscUtils.getLogger();

	private HRMDocumentDao hrmDocumentDao = SpringUtils.getBean(HRMDocumentDao.class);
	private UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	private HRMCategoryDao hrmCategoryDao = SpringUtils.getBean(HRMCategoryDao.class);
	private HrmLogDao hrmLogDao = SpringUtils.getBean(HrmLogDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = SpringUtils.getBean(HRMDocumentToDemographicDao.class);
	
	
	public ActionForward uploadReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		
		if(!isHrm) {
			return null;
		}
		
		String downloadDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		
		JSONObject obj = new JSONObject();

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		try {

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			ServletContext servletContext = getServlet().getServletContext();
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			for (FileItem item : items) {
				if ("hrm_file".equals(item.getFieldName())) {
					
					File file = new File(downloadDirectory, item.getName());
					item.write(file);

					HRMReport report = HRMReportParser.parseReport(loggedInInfo,file.getAbsolutePath());
			        if (report != null) {
			        	HRMReportParser.addReportToInbox(loggedInInfo,report);
			        } else {
			        	throw new RuntimeException();
			        }
			        
					
					obj.put("message", item.getName() + " successfully saved");
				}
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			MiscUtils.getLogger().error("Error", e);
			obj.put("message", "Error:" + e.getMessage());
			throw new RuntimeException(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			MiscUtils.getLogger().error("Error", e);
			obj.put("message", "Error:" + e.getMessage());
			throw new RuntimeException(e);
		}


		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}
	
	public ActionForward uploadPrivateKey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		
		if(!isAdmin) {
			return null;
		}
		String privateKeyDirectory = OscarProperties.getInstance().getProperty("OMD_DIRECTORY");
		if (privateKeyDirectory == null) {
			privateKeyDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + ".." + File.separator + "hrm" + File.separator + "OMD";
		}
		
		JSONObject obj = new JSONObject();

		try {

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			ServletContext servletContext = getServlet().getServletContext();
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			for (FileItem item : items) {
				if ("privateKeyFile".equals(item.getFieldName())) {
					File file = new File(privateKeyDirectory, item.getName());
					item.write(file);

					//update props
					saveUserProperty("hrm_private_key_file", item.getName());
					
					obj.put("message", item.getName() + " successfully saved");
				}
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			MiscUtils.getLogger().error("Error", e);
			obj.put("message", "Error:" + e.getMessage());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			MiscUtils.getLogger().error("Error", e);
			obj.put("message", "Error:" + e.getMessage());
		}


		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}
	
	private String getUserPropertyValueOrNull(String propName) {
		UserProperty up = userPropertyDao.getProp(propName);
		if (up != null && !StringUtils.isEmpty(up.getValue())) {
			return up.getValue();
		}
		return null;
	}

	//new SFTPConnector().startAutoFetch(loggedInInfo);
	public ActionForward fetch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		
		if(!isHrm) {
			return null;
		}
		SFTPConnector connector = null;
		String error = null;
		try {
			
			String hostname = getUserPropertyValueOrNull("hrm_hostname");
			String port = getUserPropertyValueOrNull("hrm_port");
			String username = getUserPropertyValueOrNull("hrm_username");
			String remoteDir = getUserPropertyValueOrNull("hrm_location");
			String decryptionKey = getUserPropertyValueOrNull("hrm_decryption_key");
			String privateKeyFile = getUserPropertyValueOrNull("hrm_private_key_file");
			
			String privateKeyDirectory = OscarProperties.getInstance().getProperty("OMD_DIRECTORY");
			if (privateKeyDirectory == null) {
				privateKeyDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + ".." + File.separator + "hrm" + File.separator + "OMD" + File.separator;
			}
			
			
			connector = new SFTPConnector(LoggedInInfo.getLoggedInInfoFromSession(request), hostname,Integer.parseInt(port),username,privateKeyDirectory + privateKeyFile,"Manual");
			SFTPConnector.setDecryptionKey(decryptionKey);
			connector.startAutoFetch(LoggedInInfo.getLoggedInInfoFromSession(request),remoteDir);

		} catch (Exception e) {
			error = e.getMessage();
		}

		JSONObject obj = new JSONObject();
		obj.put("error", error);

		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}

	public ActionForward saveConfigurationDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		
		if(!isAdmin) {
			return null;
		}
		String hostname = request.getParameter("hostname");
		String port = request.getParameter("port");
		String username = request.getParameter("username");
		String location = request.getParameter("remoteDirectory");
		String key = request.getParameter("decryptionKey");

		String pollingEnabled = request.getParameter("polling_enabled");
		String pollingInterval = request.getParameter("polling_interval");

		saveUserProperty("hrm_hostname", hostname);
		saveUserProperty("hrm_port", port);
		saveUserProperty("hrm_username", username);
		saveUserProperty("hrm_location", location);
		saveUserProperty("hrm_decryption_key", key);

		int pInterval = 30;
		
		try {
			pInterval = Integer.parseInt(pollingInterval);
			if(pInterval<0)
				pInterval=30;
		} catch(NumberFormatException e) {
			pInterval=30;
		}
		
		savePolling(LoggedInInfo.getLoggedInInfoFromSession(request), Boolean.valueOf(pollingEnabled), pInterval);
		
		JSONObject obj = new JSONObject();

		response.setContentType("application/json");
		obj.write(response.getWriter());
		return null;
	}

	/*
	 * auto polling - enabled/disabled
	 * auto-polling interval
	 */
	public ActionForward getConfigurationDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		
		if(!isAdmin) {
			return null;
		}
		
		JSONObject obj = new JSONObject();
		obj.put("hostname", getUserPropertyValueOrEmpty("hrm_hostname"));
		obj.put("port", getUserPropertyValueOrEmpty("hrm_port"));
		obj.put("username", getUserPropertyValueOrEmpty("hrm_username"));
		obj.put("remoteDirectory", getUserPropertyValueOrEmpty("hrm_location"));
		obj.put("decryptionKey", getUserPropertyValueOrEmpty("hrm_decryption_key"));
		obj.put("privateKeyFile", getUserPropertyValueOrEmpty("hrm_private_key_file"));

		obj.put("polling_enabled",isPollingEnabled());
		obj.put("polling_interval", getPollingInterval());
		
		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}
	
	private void savePolling(LoggedInInfo loggedInInfo, boolean enabled, int frequencyInMinutes) {
		OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
		OscarJobTypeDao oscarJobTypeDao = SpringUtils.getBean(OscarJobTypeDao.class);
		
		OscarJobType oscarJobType = null;
		OscarJob oscarJob = null;
		List<OscarJobType> jobTypes = oscarJobTypeDao.findByClassName("org.oscarehr.hospitalReportManager.v2018.HRMDownloadJob");
		if(jobTypes.isEmpty()) {
			oscarJobType = new OscarJobType();
			oscarJobType.setClassName("org.oscarehr.hospitalReportManager.v2018.HRMDownloadJob");
			oscarJobType.setDescription("HRM Downloader");
			oscarJobType.setEnabled(true);
			oscarJobType.setName("HRMDownloadJobType");
			oscarJobType.setUpdated(new Date());
			oscarJobTypeDao.persist(oscarJobType);
		} else {
			oscarJobType = jobTypes.get(0);
		}
		
		List<OscarJob> jobs = oscarJobDao.findByType(oscarJobType);
		if(jobs.isEmpty()) {
			oscarJob = new OscarJob();
			oscarJob.setCronExpression("0 0/"+frequencyInMinutes+" * 1/1 * ?");
			oscarJob.setDescription("HRM Downloader");
			oscarJob.setEnabled(enabled);
			oscarJob.setName("HRMDownloadJob");
			oscarJob.setOscarJobTypeId(oscarJobType.getId());
			oscarJob.setOscarJobType(oscarJobType);
			oscarJob.setProviderNo(loggedInInfo.getLoggedInProviderNo());
			oscarJob.setUpdated(new Date());
			oscarJobDao.persist(oscarJob);
		} else {
			oscarJob = jobs.get(0);
			oscarJob.setCronExpression("0 0/"+frequencyInMinutes+" * 1/1 * ?");
			oscarJob.setEnabled(enabled);
			oscarJobDao.merge(oscarJob);
		}

		try {
			OscarJobUtils.resetJobExecutionFramework();
		}catch(Exception e) {
			logger.error("Error",e);
		}
	}
	
	private boolean isPollingEnabled() {
		
		OscarJob job = getOscarJobForPolling();
		
		if(job != null &&  job.isEnabled()  && job.getOscarJobType().isEnabled() && !StringUtils.isEmpty(job.getCronExpression())) {
			return true;
		}
			
		return false;
	}
	
	private String getPollingInterval() {
		
		OscarJob job = getOscarJobForPolling();
		
		if(job != null && !StringUtils.isEmpty(job.getCronExpression())) {
			String[] expr = job.getCronExpression().split(" ");
			String val = expr[1];
			String v = val.split("/")[1];
			return v;
		}
			
		return "N/A";
	}
	
	private OscarJob getOscarJobForPolling() {
		OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
		OscarJobTypeDao oscarJobTypeDao = SpringUtils.getBean(OscarJobTypeDao.class);
		
		List<OscarJobType> jobTypes = oscarJobTypeDao.findByClassName("org.oscarehr.hospitalReportManager.v2018.HRMDownloadJob");
		
		for(OscarJobType jobType:jobTypes) {
			List<OscarJob> jobs = oscarJobDao.findByType(jobType);
			for(OscarJob job:jobs) {
				return job;
			}
		}
		
		return null;
	}

	private String getUserPropertyValueOrEmpty(String propName) {
		UserProperty up = userPropertyDao.getProp(propName);
		if (up != null && !StringUtils.isEmpty(up.getValue())) {
			return up.getValue();
		}
		return "";
	}

	private Integer saveUserProperty(String name, String value) {
		UserProperty up = userPropertyDao.getProp(name);
		if (up != null) {
			up.setValue(value);
			userPropertyDao.merge(up);
		} else {
			up = new UserProperty();
			up.setName(name);
			up.setValue(value);
			userPropertyDao.persist(up);
		}

		return up.getId();
	}

	public ActionForward getConfidentialityStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");

		String data = hrmProviderConfidentialityStatementDao.getConfidentialityStatementForProvider(loggedInInfo.getLoggedInProviderNo());
		JSONObject res = new JSONObject();
		res.put("value", data != null ? data : "");
		
		response.setContentType("application/json");
		res.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward saveConfidentialityStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");

		String value = request.getParameter("value");
		
		HRMProviderConfidentialityStatement stmt = hrmProviderConfidentialityStatementDao.findByProvider(loggedInInfo.getLoggedInProviderNo());
		
		if(stmt == null) {
			stmt = new HRMProviderConfidentialityStatement();
			stmt.setId(loggedInInfo.getLoggedInProviderNo());
		}
		stmt.setStatement(value);
		hrmProviderConfidentialityStatementDao.merge(stmt);
		
		return null;
	}
	
	public ActionForward addToOutageList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		SFTPConnector.addMeToDoNotSendList(loggedInInfo);
		
		
		return null;
	}
	
	public ActionForward getHrmStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject obj = new JSONObject();

		response.setContentType("application/json");
		obj.put("running", SFTPConnector.isFetchRunning());
		obj.write(response.getWriter());
		
		
		return null;
	}
	
	public ActionForward getDetailedLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isHrmAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm.administrator", "r", null);
		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		
		if(!isHrm && !isHrmAdmin && !isAdmin) {
			return null;
		}
		String id = request.getParameter("id");
		
		HrmLogEntryDao hrmLogEntryDao = SpringUtils.getBean(HrmLogEntryDao.class);
		
		List<HrmLogEntry> hrmLogEntries = hrmLogEntryDao.findByHrmLogId(Integer.parseInt(id));
		
		JSONObject res = new JSONObject();

		JSONArray data = new JSONArray();

		
		for(HrmLogEntry l : hrmLogEntries) {
			JSONObject obj = new JSONObject();
			obj.put("id", l.getId() );
			obj.put("decrypted_filename", l.getDecryptedFileName() != null ? l.getDecryptedFileName().substring(l.getDecryptedFileName().lastIndexOf("/")+1) : "");
			obj.put("encrypted_filename", l.getEncryptedFileName() != null ? l.getEncryptedFileName().substring(l.getEncryptedFileName().lastIndexOf("/")+1):"" );
			obj.put("error", l.getError() != null ? l.getError() : "" );
			obj.put("filename", l.getFilename() != null ? l.getFilename().substring(l.getFilename().lastIndexOf("/")+1) : "" );
			obj.put("recipient", l.getRecipientName()!= null? l.getRecipientName() + (l.getRecipientId()!=null?"("+l.getRecipientId()+")":"") : "" );
			
			data.put(obj);
		}
		
		res.put("items", data);
		
		response.setContentType("application/json");
		res.write(response.getWriter());

		
		return null;
	}
	
	public ActionForward viewLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isHrmAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm.administrator", "r", null);
		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		
		if(!isHrm && !isHrmAdmin && !isAdmin) {
			return null;
		}
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");

		String orderingColumnIndex = request.getParameter("order[0][column]"); //idx (eg 0)
		String orderingColumnDirection = request.getParameter("order[0][dir]"); //asc,desc

		
		//setup a column map from request parameters
		Map<Integer, ColumnInfo> columnMap = new HashMap<Integer, ColumnInfo>();
		int idx = 0;
		while (true) {
			if (request.getParameter("columns[" + idx + "][data]") == null) {
				break;
			}
			columnMap.put(idx, new ColumnInfo(idx, request.getParameter("columns[" + idx + "][data]")));
			idx++;
		}

		String orderBy = null;

		if (!StringUtils.isEmpty(orderingColumnIndex)) {
			ColumnInfo columnInfo = columnMap.get(Integer.parseInt(orderingColumnIndex));
			if ("transaction_date".equals(columnInfo.getData())) {
				orderBy = "started";
			} else if ("transaction_type".equals(columnInfo.getData())) {
				orderBy = "transactionType";
			}
		}
		
		String limitToProvider=null;
		if(!isHrmAdmin && !isAdmin) {
			limitToProvider = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
		}

		List<HrmLog> logs = hrmLogDao.query(Integer.parseInt(start), Integer.parseInt(length), StringEscapeUtils.escapeSql(orderBy), StringEscapeUtils.escapeSql(orderingColumnDirection), limitToProvider);

		JSONArray data = new JSONArray();

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		for (HrmLog l : logs) {
			Provider p = null;
			if(!StringUtils.isEmpty(l.getInitiatingProviderNo())) {
				p = providerDao.getProvider(l.getInitiatingProviderNo());
			}
			
			JSONObject data1 = new JSONObject();
			data1.put("id", l.getId());
			data1.put("transaction_date", fmt.format(l.getStarted()));
			data1.put("transaction_type", l.getTransactionType());
			data1.put("external_system", l.getExternalSystem());
			data1.put("initiating_provider", p != null ? p.getFormattedName() : "");
			data1.put("connected", l.getConnected() != null && l.getConnected()?"Yes":"No");
			data1.put("downloaded", l.getDownloadedFiles() != null &&  l.getDownloadedFiles()?"Yes":"No");
			data1.put("num_files_downloaded", l.getNumFilesDownloaded() != null ? l.getNumFilesDownloaded() : "N/A");
			data1.put("deleted", l.getDeleted() != null && l.getDeleted()?"Yes":"No");
			
			data.put(data1);
		}

		JSONObject obj = new JSONObject();
		obj.put("draw", ++draw);
		obj.put("recordsTotal", data.length());
		obj.put("recordsFiltered", data.length());
		obj.put("data", data);
		//obj.put("error", "error occurred");

		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}

	public ActionForward searchCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String query = request.getParameter("query");
		
		List<HRMCategory> categoryList = hrmCategoryDao.search(query);
		JSONArray data = new JSONArray();
		for(HRMCategory category:categoryList) {
			JSONObject obj = new JSONObject();
			obj.put("id", category.getId());
			obj.put("mnemonic", category.getSubClassNameMnemonic());
			obj.put("name", category.getCategoryName());
			data.put(obj);
		}
		JSONObject d = new JSONObject();
		d.put("results", data);
	        
		response.setContentType("text/x-json");
	    d.write(response.getWriter());
	     
		return null;
	}
	
	public ActionForward saveCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String hrmDocumentId = request.getParameter("hrmDocumentId");
		String categoryId = request.getParameter("categoryId");
		
		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		
		HRMCategory category = null;
		HRMDocument doc = null;
		
		if(isHrm) {

			category = hrmCategoryDao.find(Integer.parseInt(categoryId));
			if(category != null) {
				doc = hrmDocumentDao.find(Integer.parseInt(hrmDocumentId));
				
				if(doc != null) {
					doc.setHrmCategoryId(Integer.parseInt(categoryId));
					hrmCategoryDao.merge(doc);
				}
			}
		}
		
		
		
		JSONObject d = new JSONObject();
		
		if(doc != null && doc.getHrmCategoryId() != null) {
			HRMCategory setCat = hrmCategoryDao.find(doc.getHrmCategoryId());
			if(setCat != null) {
				d.put("value", setCat.getSubClassNameMnemonic() + ":" + setCat.getCategoryName());
			}
		}
		
		response.setContentType("text/x-json");
	    d.write(response.getWriter());
	    
		return null;
	}
	
	

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isHrmAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm.administrator", "r", null);
		boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
		boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
		

		String start = request.getParameter("start");
		String length = request.getParameter("length");

		String orderingColumnIndex = request.getParameter("order[0][column]"); //idx (eg 0)
		String orderingColumnDirection = request.getParameter("order[0][dir]"); //asc,desc

		
		String providerNo = request.getParameter("providerNo");
		String providerUnmatched = StringUtils.trimToEmpty(request.getParameter("providerUnmatched"));
		
		String noSignOff = StringUtils.trimToEmpty(request.getParameter("noSignOff"));
		String demographicUnmatched = StringUtils.trimToEmpty(request.getParameter("demographicUnmatched"));
			
		if(isHrmAdmin &&  "ALL".equals(providerNo)) {
			providerNo = null;
		}
		if(!isHrmAdmin && !isAdmin) {
			providerNo = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
		}

		//setup a column map from request parameters
		Map<Integer, ColumnInfo> columnMap = new HashMap<Integer, ColumnInfo>();
		int idx = 0;
		while (true) {
			if (request.getParameter("columns[" + idx + "][data]") == null) {
				break;
			}
			columnMap.put(idx, new ColumnInfo(idx, request.getParameter("columns[" + idx + "][data]")));
			idx++;
		}

		String orderBy = null;

		if (!StringUtils.isEmpty(orderingColumnIndex)) {
			ColumnInfo columnInfo = columnMap.get(Integer.parseInt(orderingColumnIndex));
			if ("patient_name".equals(columnInfo.getData())) {
				orderBy = "formattedName";
			} else if ("patient_dob".equals(columnInfo.getData())) {
				orderBy = "dob";
			} else if ("report_date".equals(columnInfo.getData())) {
				orderBy = "reportDate";
			}  else if ("received_date".equals(columnInfo.getData())) {
				orderBy = "timeReceived";
			} else if ("sending_facility".equals(columnInfo.getData())) {
				orderBy = "sourceFacility";
			}
		}
		
		JSONArray data = new JSONArray();

		long total = 0L;
		
		
		if(isHrm) {
			List<HRMDocument> docs = hrmDocumentDao.query(providerNo,"true".equals(providerUnmatched), "true".equals(noSignOff),  "true".equals(demographicUnmatched), Integer.parseInt(start), Integer.parseInt(length), orderBy, orderingColumnDirection);
	
			total = hrmDocumentDao.queryForCount(providerNo,"true".equals(providerUnmatched), "true".equals(noSignOff),  "true".equals(demographicUnmatched), Integer.parseInt(start), Integer.parseInt(length), orderBy, orderingColumnDirection);
			
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
			for (HRMDocument d : docs) {
				HRMCategory category = null;
				//Provider provider = null;
				if(d.getHrmCategoryId()!= null) {
					 category = hrmCategoryDao.find(d.getHrmCategoryId());
				}
				//if(d.getRecipientProviderNo() != null) {
				//	provider = providerDao.getProvider(d.getRecipientProviderNo());
				//}
				
				List<HRMDocumentToDemographic> ptList = hrmDocumentToDemographicDao.findByHrmDocumentId(d.getId().toString());
				Integer demographicNo = null;
				if(!ptList.isEmpty()) {
					demographicNo = Integer.parseInt(ptList.get(0).getDemographicNo());
				}
				
				JSONObject data1 = new JSONObject();
				data1.put("id", d.getId());
				data1.put("provider_no", d.getRecipientProviderNo() != null ?  d.getRecipientProviderNo() : "");
				data1.put("demographic_no", demographicNo != null ? demographicNo.toString() : "");
				data1.put("recipient_name", d.getRecipientName() != null ? d.getRecipientName() : "");
				data1.put("patient_name", d.getFormattedName());
				data1.put("patient_dob", d.getDob());
				data1.put("patient_hcn", d.getHcn());
				data1.put("patient_gender", d.getGender());
				
				String reportDate = "";
				if(d.getReportDate() != null) {
					reportDate = fmt.format(d.getReportDate());
				} else if(!d.getAccompanyingSubClasses().isEmpty()) {
					for(HRMDocumentSubClass hdsc: d.getAccompanyingSubClasses()) {
						if(hdsc.isActive() && hdsc.getSubClassDateTime() != null) {
							reportDate = fmt.format(hdsc.getSubClassDateTime());
						}
					}
				}
				
				data1.put("report_date", reportDate != null ? reportDate : "");
				
				
				data1.put("sending_facility", d.getSourceFacility() != null ? d.getSourceFacility() : "");
				if(!StringUtils.isEmpty(d.getClassName()) && !StringUtils.isEmpty(d.getSubClassName())) {
					String className = d.getClassName();
					String subClassName = d.getSubClassName();
					String displaySubClass = "";
					if(subClassName != null) {
						if(subClassName.indexOf("^") != -1) {
							displaySubClass = subClassName.split("\\^")[1];
						} else {
							displaySubClass = subClassName;
						}
					}
					data1.put("class_subclass", className + (displaySubClass.length()>0?":"+displaySubClass:""));
				}
				if(!StringUtils.isEmpty(d.getClassName()) && !d.getAccompanyingSubClasses().isEmpty()) {
					for(HRMDocumentSubClass sc: d.getAccompanyingSubClasses()) {
						if(sc.isActive()) {
							data1.put("class_subclass", d.getClassName() + " " + sc.getSubClass() + ":" + sc.getSubClassMnemonic() + ":" + sc.getSubClassDescription());
						}
					}
				}
				
				data1.put("received_date", fmt.format(d.getTimeReceived()));
				//data1.put("reviewed", "N/A");
				data1.put("category", category!=null?category.getCategoryName():"");
				data1.put("description", d.getDescription());
				
				data.put(data1);
			}
		}

		JSONObject obj = new JSONObject();
		obj.put("draw", ++draw);
		obj.put("recordsTotal", total);
		obj.put("recordsFiltered", total);
		obj.put("data", data);
		//obj.put("error", "error occurred");

		response.setContentType("application/json");
		obj.write(response.getWriter());

		return null;
	}

}

class ColumnInfo {
	private int index;
	private String data;

	public ColumnInfo() {
	}

	public ColumnInfo(int index, String data) {
		this.index = index;
		this.data = data;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}