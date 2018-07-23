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
package org.oscarehr.integration.born;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.helpers.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.tika.io.IOUtils;
import org.apache.xmlbeans.XmlOptions;
import org.hl7.fhir.dstu3.model.Communication;
import org.oscarehr.common.dao.BornTransmissionLogDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.BornTransmissionLog;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.integration.fhir.api.BIS;
import org.oscarehr.integration.fhir.builder.AbstractFhirMessageBuilder;
import org.oscarehr.integration.fhir.builder.FhirCommunicationBuilder;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oscar.OscarProperties;

public class BORNFhirJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;
	
	private EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	private ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private DxresearchDAO dxResearchDAO = SpringUtils.getBean(DxresearchDAO.class);
	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private ConsultationRequestDao consultationRequestDao = SpringUtils.getBean(ConsultationRequestDao.class);
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	private EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	
	protected void sendMessages(LoggedInInfo loggedInInfo, List<Integer> demographicsToSend,String type, Clinic clinic, String providerEmail, String oneIdToken) {	
		for(Integer demographicNo: demographicsToSend) {
			String data = getData(loggedInInfo,demographicNo,type);
			if(data == null) {
				MiscUtils.getLogger().warn("no payload for  " + demographicNo);
				continue;
			}
			FhirCommunicationBuilder fhirCommunicationBuilder = getFhirCommunicationBuilder(loggedInInfo, demographicNo, type, clinic, data);
			Communication communication = fhirCommunicationBuilder.getCommunication();         
        	String data2 = AbstractFhirMessageBuilder.getFhirContext().newJsonParser().setPrettyPrint(false).encodeResourceToString(communication);
            logger.debug(data2);
            sendMessage(providerEmail,oneIdToken,data2,demographicNo,type);
		}
	}
	
	
	
	@Override
	public void run() {
		
		try {
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(security);

			logger.info("BORN FHIR integration job started and running as " + x.getLoggedInProvider().getFormattedName());
			
			String providerEmail = security.getOneIdEmail();
			Clinic clinic = clinicDao.getClinic();
			String oneIdToken = OscarProperties.getInstance().getProperty("BORNFhirJob.access_token");
        	
			sendMessages(x, getARDemographicNos(), "ar", clinic, providerEmail, oneIdToken);
			sendMessages(x, getWBDemographicNos(), "wb", clinic, providerEmail, oneIdToken);
			sendMessages(x, getWBCSDDemographicNos(), "wbcsd", clinic, providerEmail, oneIdToken);
		
			logger.info("BORN FHIR integration job successfully completed");
			
		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}
	
	protected void markAsSentWB(Integer demographicNo) {
		BORNWbXmlGenerator xml = new BORNWbXmlGenerator();
		xml.init(demographicNo);
		
		for (String name : xml.getEformMap().keySet()) {
			Integer fdid = xml.getEformFdidMap().get(name);
			if (fdid != null) {
				EFormValue efv = eformValueDao.findByFormDataIdAndKey(fdid, "uploaded_to_BORN");
		
				if (efv == null) {
					efv = new EFormValue();
					efv.setDemographicId(demographicNo);
					efv.setFormDataId(fdid);
					efv.setFormId(xml.getEformMap().get(name).getId());
					efv.setVarName("uploaded_to_BORN");
					efv.setVarValue("Yes");
					eformValueDao.persist(efv);
				}
			}
		}
		
		//for each eform, set something to know that fdids before this one for this demographic are considered processed_by_Born
		//including this current one

		for (String name: xml.getEformMap().keySet()) {
			
			EForm eform = xml.getEformMap().get(name);
			Integer fdid = xml.getEformFdidMap().get(name);
			
			if(fdid == null) {
				continue;
			}
			
			List<EFormData> efdList = eformDataDao.findByDemographicIdAndFormId(demographicNo, eform.getId());
			
			boolean seen=false;
			//only set for the ones older than the one we sent.
			for(EFormData efd : efdList) {
				if(efd.getId().intValue() == fdid.intValue()) {
					seen=true;
				}
				if(seen) {
					//ADD the value if it doesn't exist processed_by_Born
					logger.debug("SET processed_by_BORN on fdid " + efd.getId());
					addValueIfMissing(efd.getId(),"processed_by_BORN","Yes",demographicNo,efd.getFormId());
				}
			}
		}

	}
	
	private void addValueIfMissing(Integer fdid, String varName, String varValue, Integer demographicNo, Integer formId) {
		EFormValue val = eformValueDao.findByFormDataIdAndKey(fdid, varName);
		if(val == null) {
			val = new EFormValue();
			val.setDemographicId(demographicNo);
			val.setFormDataId(fdid);
			val.setFormId(formId);
			val.setVarName(varName);
			val.setVarValue(varValue);
			eformValueDao.persist(val);
		}
		
	}

	private void markAsSentAR(Integer demographicNo) {
		ONAREnhancedBornConnector connector = new ONAREnhancedBornConnector();
		try {
			List<Map<String, Object>> arMetadata = connector.getMetadataForDemographic(demographicNo);
			for (Map<String, Object> metadata : arMetadata) {
				
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		for(Integer formId:formIdsSent) {
			try {
				connector.updateToSent(formId);
			}catch(Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
		}
	}

	private void markAsSentWBCSD(Integer demographicNo) {
		String val = formatter.format(new Date());

		DemographicExt de = demographicExtDao.getDemographicExt(demographicNo, "uploaded_to_BORN");
		if (de != null) {
			de.setValue(val);
			demographicExtDao.merge(de);
		} else {
			de = new DemographicExt();
			de.setDateCreated(new Date());
			de.setDemographicNo(demographicNo);
			de.setHidden(false);
			de.setKey("uploaded_to_BORN");
			de.setProviderNo(provider.getProviderNo());
			de.setValue(val);
			demographicExtDao.persist(de);
		}
	}
	
	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}


	protected boolean sendMessage(String providerEmail, String oneIdToken, String data, Integer demographicNo, String type) {
		HttpPost httpPost = new HttpPost(OscarProperties.getInstance().getProperty("backendEconsultUrl") + "/api/test3");
        httpPost.addHeader("x-oneid-email", providerEmail);
        httpPost.addHeader("x-access-token", oneIdToken);
        
        JSONObject obj = new JSONObject();
        obj.put("url",OscarProperties.getInstance().getProperty("BORNFhirJob.bisURL"));
        obj.put("service",OscarProperties.getInstance().getProperty("BORNFhirJob.service"));
        obj.put("body", Base64.encodeBase64String(data.getBytes()));
        
        try {
	       	 HttpEntity reqEntity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
	       	 httpPost.setEntity(reqEntity);
	       	 httpPost.setHeader("Content-type", "application/json+fhir");
	           
	       	 HttpClient httpClient = getHttpClient2();
	       	 HttpResponse httpResponse = httpClient.execute(httpPost);
	       	 String entity = EntityUtils.toString(httpResponse.getEntity());
	       	 logger.info("response=" + entity);
	       	 
	       	logSuccess(httpResponse, demographicNo, type, entity);
	       
	       	 
	       	

        } catch(Exception e) {
       	MiscUtils.getLogger().error("Error",e);
       }
       return false;
	}
	
	private void logSuccess(HttpResponse httpResponse, Integer demographicNo, String type, String responseBody) {
		
		String hialTransactionId =  null;
		org.codehaus.jettison.json.JSONObject headers = null;
		org.codehaus.jettison.json.JSONObject data = null;
		Integer code = null;
		try {
			org.codehaus.jettison.json.JSONObject o =new org.codehaus.jettison.json.JSONObject(responseBody);
			data = (org.codehaus.jettison.json.JSONObject)o.get("data");
			headers = (org.codehaus.jettison.json.JSONObject)o.get("headers");
			code = (Integer)o.get("code");
			
			if(headers != null) {
				hialTransactionId = headers.getString("hialTxId");
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		BornTransmissionLog log = new BornTransmissionLog();
		
		
		 if(code != null && code >= 200 && code < 300) {
	       	 if("ar".equals(type)) {
	       		markAsSentAR(demographicNo);
	       	 } else if("wb".equals(type)) {
	       		 markAsSentWB(demographicNo);
	       	 } else if("wbcsd".equals(type)) {
	       		 markAsSentWBCSD(demographicNo);
	       	 }
	       	 log.setSuccess(true);
       	 }
		 
		
	
		log.setDemographicNo(demographicNo);
		log.setHialTransactionId(hialTransactionId);
		log.setHttpCode(String.valueOf(code));
		log.setHttpHeaders(headers != null ? headers.toString() : null);
		log.setHttpResult(data != null ? data.toString() : null);
		log.setSubmitDateTime(new Date());
		log.setType(type);
		
		BornTransmissionLogDao bornTransmissionLogDao = SpringUtils.getBean(BornTransmissionLogDao.class);
		bornTransmissionLogDao.persist(log);
	}
	
	protected void logError(HttpResponse httpResponse, Integer demographicNo, String type, String reponseBody) {
		
		String hialTransactionId = httpResponse.getFirstHeader("hialTxId") != null ? httpResponse.getFirstHeader("hialTxId").getValue() : null;
		//String contentLocation = httpResponse.getFirstHeader("hialTxId") != null ? httpResponse.getFirstHeader("hialTxId").getValue() : null;
		
		JSONArray httpHeaders = new JSONArray();
		for(Header header:httpResponse.getAllHeaders() ) {
			JSONObject h = new JSONObject();
			h.put("name", header.getName());
			h.put("value", header.getValue());
			httpHeaders.add(h);
		}
		BornTransmissionLog log = new BornTransmissionLog();
	//	log.setContentLocation(contentLocation);
		log.setDemographicNo(demographicNo);
		log.setHialTransactionId(hialTransactionId);
		log.setHttpCode(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
		log.setHttpHeaders(httpHeaders.toString());
		//log.setHttpResult(responseBody);
		log.setSubmitDateTime(new Date());
		log.setType(type);
		
		BornTransmissionLogDao bornTransmissionLogDao = SpringUtils.getBean(BornTransmissionLogDao.class);
		bornTransmissionLogDao.persist(log);
	}

	private byte[] generateWBXml(BORNWbXmlGenerator xml, Integer demographicNo) {
		HashMap<String, String> suggestedPrefixes = new HashMap<String, String>();
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		opts.setSaveNoXmlDecl();
		opts.setUseDefaultNamespace();
		opts.setSaveNamespacesFirst();
		ByteArrayOutputStream os = null;
		PrintWriter pw = null;
		boolean xmlCreated = false;

		try {
			os = new ByteArrayOutputStream();
			pw = new PrintWriter(os, true);
			//pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlCreated = xml.addToStream(pw, opts, true);

			pw.close();
			if (xmlCreated) return os.toByteArray();
		} catch (Exception e) {
			logger.warn("Unable to add record", e);
		}

		return null;
	}
	
	private byte[] generateWBCSDXml(BORNWbCsdXmlGenerator xml, Integer demographicNo) {
		HashMap<String, String> suggestedPrefixes = new HashMap<String, String>();
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		opts.setSaveNoXmlDecl();
		opts.setUseDefaultNamespace();
		opts.setSaveNamespacesFirst();
		ByteArrayOutputStream os = null;
		PrintWriter pw = null;
		boolean xmlCreated = false;

		try {
			os = new ByteArrayOutputStream();
			pw = new PrintWriter(os, true);
			//pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlCreated = xml.addToStream(pw, opts, demographicNo, true);

			pw.close();
			if (xmlCreated) return os.toByteArray();
		} catch (Exception e) {
			logger.warn("Unable to add record", e);
		}

		return null;
	}

	
	private List<Integer> getWBCSDDemographicNos() {
		List<Integer> demoIds = measurementDao.findNewMeasurementsSinceDemoKey("uploaded_to_BORN");

		for(Integer i : demographicDao.getBORNKidsMissingExtKey("uploaded_to_BORN")) {
			if (!demoIds.contains(i)) {
				demoIds.add(i);
			}
		}
		
		for (Integer i : dxResearchDAO.findNewProblemsSinceDemokey("uploaded_to_BORN")) {
			if (!demoIds.contains(i)) {
				demoIds.add(i);
			}
		}
		for (Integer i : drugDao.findNewDrugsSinceDemoKey("uploaded_to_BORN")) {
			if (!demoIds.contains(i)) {
				demoIds.add(i);
			}
		}
		
		for (Integer i : preventionDao.findNewPreventionsSinceDemoKey("uploaded_to_BORN")) {
			if (!demoIds.contains(i)) {
				demoIds.add(i);
			}
		}
		
		for (Integer i : consultationRequestDao.findNewConsultationsSinceDemoKey("uploaded_to_BORN")) {
			if (!demoIds.contains(i)) {
				demoIds.add(i);
			}
		}	
		
		
		List<Integer> validatedIds = new ArrayList<Integer>();
		for(Integer i : demoIds) {
			Demographic d = demographicDao.getDemographicById(i);
			if(d != null && d.getAgeInYears() < 18 && d.getHin() != null && !d.getHin().isEmpty()) {
				validatedIds.add(i);
			}
		}
		
		return validatedIds;
	}
	
	private List<Integer> getWBDemographicNos() {
		//TODO: take into account drafts
		List<Integer> demographicsToSend = new ArrayList<Integer>();
		BORNWbXmlGenerator xmlGen = new BORNWbXmlGenerator();
		for(String name : xmlGen.getEformMap().keySet()) {
			EForm eform = xmlGen.getEformMap().get(name);
			
			if(eform != null) {
				List<Integer> tmp = eformDataDao.getDemographicNosMissingVarName(eform.getId(), "processed_by_BORN");
				
				for(Integer t:tmp) {
					if(!demographicsToSend.contains(t)) {
						demographicsToSend.add(t);
					}
				}
			}
		}
		
		return demographicsToSend;
	}
	
	
	private List<Integer> getARDemographicNos() {
		List<Integer> demographicsToSend = new ArrayList<Integer>();
		ONAREnhancedBornConnector arConnector = new ONAREnhancedBornConnector();
		
		try {
			List<Map<String, Object>> arMetadata = arConnector.getMetadata();
			for (Map<String, Object> metadata : arMetadata) {
				String strDemographicNo = String.valueOf(metadata.get("demographicNo"));
				demographicsToSend.add(Integer.parseInt(strDemographicNo));	
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		return demographicsToSend;
	}
	
	

	List<Integer> formIdsSent = new ArrayList<Integer>();
	
	private String getARData(LoggedInInfo loggedInInfo, int demographicNo) {
		formIdsSent = new ArrayList<Integer>();
		ONAREnhancedBornConnector arConnector = new ONAREnhancedBornConnector();
		String data = null;
		try {
			List<Map<String, Object>> arMetadata = arConnector.getMetadataForDemographic(demographicNo);
			for (Map<String, Object> metadata : arMetadata) {
				ONAREnhancedFormToXML xml = new ONAREnhancedFormToXML();
				XmlOptions opts = arConnector.getXmlOptions();

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				String result = null;
				sw.append("<ARRecordSet xmlns=\"http://www.oscarmcmaster.org/AR2005\">");
				if (xml.addXmlToStream(loggedInInfo,pw, opts, null, String.valueOf(metadata.get("demographicNo")), (Integer) metadata.get("id"), (Integer) metadata.get("episodeId"))) {
					sw.append("</ARRecordSet>");
					result = sw.toString();
					data = result;
					formIdsSent.add((Integer) metadata.get("id"));
				} else {
					MiscUtils.getLogger().warn("Failed to write record");
				}
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
			return null;
		}
		return data;
	}
	
	private String getWBData(int demographicNo) {
		String pathStr = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5";
		File path = new File(pathStr);
		if (!path.exists()) {
			FileUtils.mkDir(path);
		}
		
		logger.info("This is an early version..output can be found at " + path.toString());

		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		BORNWbXmlGenerator testGen = new BORNWbXmlGenerator();
		testGen.init(demographicNo);
		FileWriter fw = null;
		try {
			fw = new FileWriter(pathStr + File.separator + "born_wb_" + demographicNo + ".xml");
			if(!testGen.addToStream(fw, opts, true)) {
				logger.debug("no record to write");
			} else {
				BORNWbXmlGenerator xml = new BORNWbXmlGenerator();
				xml.init(demographicNo);
				byte[] wbXml = generateWBXml(xml, demographicNo);
				String wbData = new String(wbXml, Charset.defaultCharset());
				return wbData;
			}
		}catch (Exception e) {
			MiscUtils.getLogger().error("Error",e);
		} finally {
			IOUtils.closeQuietly(fw);
		}
		return null;
	}
	
	private String getWBCSDData(LoggedInInfo loggedInInfo, Integer demographicNo) {
		String pathStr = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5";

		try {

			File path = new File(OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + "born5");
			if (!path.exists()) {
				FileUtils.mkDir(path);
			}

			///////////////////////

			logger.info("This is an early version..output can be found at " + path.toString());

			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();

			BORNWbCsdXmlGenerator testGen2 = new BORNWbCsdXmlGenerator();
			FileWriter fw2 = null;
			try {
				fw2 = new FileWriter(pathStr + File.separator + "born_wbcsd_" + demographicNo + ".xml");
				testGen2.addToStream(fw2, opts, demographicNo, false);
			} finally {
				fw2.close();
			}
			
			BORNWbCsdXmlGenerator xml = new BORNWbCsdXmlGenerator();
			byte[] wbcsdXml = generateWBCSDXml(xml, demographicNo);
			if(wbcsdXml != null) {
				return new String(wbcsdXml);
			}
			
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return null;
	}
	
	   protected HttpClient getHttpClient2() throws KeyManagementException, NoSuchAlgorithmException {

	        //setup SSL
	        SSLContext sslcontext = SSLContexts.custom().useTLS().build();
	        sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
	        sslcontext.getDefaultSSLParameters().setWantClientAuth(true);
	        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);

	        //setup timeouts
	        int timeout = Integer.parseInt(OscarProperties.getInstance().getProperty("BORNFhirJob.timeout", "60"));
	        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).build();

	        CloseableHttpClient httpclient3 = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sf).build();

	        return httpclient3;

	    }
	   
	
	protected FhirCommunicationBuilder getFhirCommunicationBuilder(LoggedInInfo loggedInInfo, Integer demographicNo, String type, Clinic clinic, String data) {
		if("ar".equals(type)) {
			return  BIS.getFhirCommunicationBuilder(loggedInInfo, demographicNo, null, null, data, clinic);
		}
		if("wb".equals(type)) {
			return  BIS.getFhirCommunicationBuilder(loggedInInfo, demographicNo, data, null, null, clinic);
			
		}
		if("wbcsd".equals(type)) {
			return  BIS.getFhirCommunicationBuilder(loggedInInfo, demographicNo, null, data, null, clinic);
		}
		return null;
	}
	
	protected String getData(LoggedInInfo loggedInInfo, Integer demographicNo, String type) {
		if("ar".equals(type)) {
			return this.getARData(loggedInInfo, demographicNo);
		}
		if("wb".equals(type)) {
			return  this.getWBData(demographicNo);
		}
		if("wbcsd".equals(type)) {
			return this.getWBCSDData(loggedInInfo, demographicNo);
		}
		return null;
	}
}
