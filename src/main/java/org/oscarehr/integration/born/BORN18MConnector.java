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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.BornTransmissionLogDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.BornTransmissionLog;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;

public class BORN18MConnector {
	private final String UPLOADED_TO_BORN = "uploaded_to_BORN";
	private final String VALUE_YES = "Yes";
	
	private final BornTransmissionLogDao logDao = SpringUtils.getBean(BornTransmissionLogDao.class);
	private final DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private final EFormDao eformDao = SpringUtils.getBean(EFormDao.class);
	private final EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	private final EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);
	private final Logger logger = MiscUtils.getLogger();
	
	private final OscarProperties oscarProperties = OscarProperties.getInstance();
	private final String filenameStart = "BORN_" + oscarProperties.getProperty("born18m_orgcode", "") + "_18MEWBV_" + oscarProperties.getProperty("born18m_env", "T");

	public List<Integer> getDemographicIdsOfUnsentRecords()  {		
		logger.debug("getDemographicIdsOfUnsentRecords() starting");
		List<Integer> results = new ArrayList<Integer>();
    	String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
    	logger.debug("form names retrieved from properties");
    			
		EForm rourkeForm = eformDao.findByName(rourkeFormName);
		EForm nddsForm = eformDao.findByName(nddsFormName);
		EForm rpt18mForm = eformDao.findByName(rpt18mFormName);

		logger.debug("loaded eforms");
		
		List<Integer> rourkeFormDemoList = new ArrayList<Integer>();
		List<Integer> nddsFormDemoList = new ArrayList<Integer>();
		List<Integer> rpt18mFormDemoList = new ArrayList<Integer>();
		
		logger.debug("intialized lists");
		
		if (rourkeForm==null) {
			logger.error(rourkeFormName+" form not found!");
		} else {
			logger.debug("build demographic id list for rourke");
			buildDemoNos(rourkeForm, rourkeFormDemoList);
		}
		if (nddsForm==null) {
			logger.error(nddsFormName+" form not found!");
		} else {
			logger.debug("build demographic id list for ndds");
			buildDemoNos(nddsForm, nddsFormDemoList);
		}
		if (rpt18mForm==null) {
			logger.error(rpt18mFormName+" form not found!");
		} else {
			logger.debug("build demographic id list for 18m report");
			buildDemoNos(rpt18mForm, rpt18mFormDemoList);
		}
    	
		logger.debug("rourke demo list size " + rourkeFormDemoList.size());
		logger.debug("ndds demo list size " + nddsFormDemoList.size());
		logger.debug("18m report demo list size " + rpt18mFormDemoList.size());
		
		for (Integer demoNo : rourkeFormDemoList) {
			Integer fdid = checkRourkeDone(rourkeFormName, demoNo);
			if (fdid!=null) {
				if(!results.contains(demoNo))
					results.add(demoNo);
			}
			
		}
		for (Integer demoNo : nddsFormDemoList) {
			Integer fdid = checkNddsDone(nddsFormName, demoNo);
			if (fdid!=null) {
				if(!results.contains(demoNo))
					results.add(demoNo);
			}
			
		}
		for (Integer demoNo : rpt18mFormDemoList) {
			Integer fdid = checkReport18mDone(rpt18mFormName, demoNo);
			if (fdid!=null) {
				if(!results.contains(demoNo))
					results.add(demoNo);
			}
		}
		
		logger.debug("results list size " + results.size());
		
		return results;
	}
	
	public String getIDForCDA(Integer demoNo) {
		String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
		Integer rourkeFdid = checkRourkeDone(rourkeFormName, demoNo);
		Integer nddsFdid = checkNddsDone(nddsFormName, demoNo);
		Integer report18mFdid = checkReport18mDone(rpt18mFormName, demoNo);
		
		
		return ((rourkeFdid!=null)?rourkeFdid:0) + "-" + ((nddsFdid!=null)?nddsFdid:0) +  "-" + ((report18mFdid!=null)?report18mFdid:0);
	}
	
	public Object[] getXmlForDemographic(LoggedInInfo loggedInInfo, Integer demoNo) {
	   	String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
		Integer rourkeFdid = checkRourkeDone(rourkeFormName, demoNo);
		Integer nddsFdid = checkNddsDone(nddsFormName, demoNo);
		Integer report18mFdid = checkReport18mDone(rpt18mFormName, demoNo);
		
		byte[] born18mXml = generateXml(loggedInInfo, demoNo, rourkeFdid, nddsFdid, report18mFdid, true);
		if(born18mXml == null) {
			return null;
		}
		String decoded = null;
		try {
			decoded = new String(born18mXml, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			logger.error("error",e);
		}
		return new Object[]{rourkeFdid,nddsFdid,report18mFdid,decoded};
	}
	
	public void updateBorn(LoggedInInfo loggedInInfo) {
    	String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
		EForm rourkeForm = eformDao.findByName(rourkeFormName);
		EForm nddsForm = eformDao.findByName(nddsFormName);
		EForm rpt18mForm = eformDao.findByName(rpt18mFormName);

		List<Integer> rourkeFormDemoList = new ArrayList<Integer>();
		List<Integer> nddsFormDemoList = new ArrayList<Integer>();
		List<Integer> rpt18mFormDemoList = new ArrayList<Integer>();
		
		if (rourkeForm==null) logger.error(rourkeFormName+" form not found!");
		else buildDemoNos(rourkeForm, rourkeFormDemoList);
		if (nddsForm==null) logger.error(nddsFormName+" form not found!");
		else buildDemoNos(nddsForm, nddsFormDemoList);
		if (rpt18mForm==null) logger.error(rpt18mFormName+" form not found!");
		else buildDemoNos(rpt18mForm, rpt18mFormDemoList);
    	
		HashMap<Integer,Integer> rourkeFormDemoFdids = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> nddsFormDemoFdids = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> rpt18mFormDemoFdids = new HashMap<Integer,Integer>();
		
		for (Integer demoNo : rourkeFormDemoList) {
			Integer fdid = checkRourkeDone(rourkeFormName, demoNo);
			if (fdid!=null) rourkeFormDemoFdids.put(demoNo, fdid);
		}
		for (Integer demoNo : nddsFormDemoList) {
			Integer fdid = checkNddsDone(nddsFormName, demoNo);
			if (fdid!=null) nddsFormDemoFdids.put(demoNo, fdid);
		}
		for (Integer demoNo : rpt18mFormDemoList) {
			Integer fdid = checkReport18mDone(rpt18mFormName, demoNo);
			if (fdid!=null) rpt18mFormDemoFdids.put(demoNo, fdid);
		}

		//Upload to BORN repository
		for (Integer demoNo : rourkeFormDemoFdids.keySet()) {
			uploadToBorn(loggedInInfo, demoNo, rourkeFormDemoFdids.get(demoNo), nddsFormDemoFdids.get(demoNo), rpt18mFormDemoFdids.get(demoNo));
			nddsFormDemoFdids.remove(demoNo);
			rpt18mFormDemoFdids.remove(demoNo);
		}
		for (Integer demoNo : nddsFormDemoFdids.keySet()) {
			uploadToBorn(loggedInInfo, demoNo, null, nddsFormDemoFdids.get(demoNo), rpt18mFormDemoFdids.get(demoNo));
			rpt18mFormDemoFdids.remove(demoNo);
		}
		for (Integer demoNo : rpt18mFormDemoFdids.keySet()) {
			if (hasFormUploaded(rourkeFormName, demoNo) && hasFormUploaded(nddsFormName, demoNo)) {
				uploadToBorn(loggedInInfo, demoNo, null, null, rpt18mFormDemoFdids.get(demoNo));
			}
		}
	}
	

	
	private void uploadToBorn(LoggedInInfo loggedInInfo, Integer demographicNo, Integer rourkeFdid, Integer nddsFdid, Integer report18mFdid) {
		byte[] born18mXml = generateXml(loggedInInfo, demographicNo, rourkeFdid, nddsFdid, report18mFdid,false);
		if (born18mXml == null) return;
		
		BornTransmissionLog log = prepareLog();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(new Date());
		String filename = filenameStart + "_" + dt + "_" + getFileSuffix(log.getId()) + ".xml";

		boolean uploadOk = uploadToBORN(born18mXml, filename);
		if (uploadOk) recordFormSent(demographicNo, rourkeFdid, nddsFdid, report18mFdid);

		//update log filename and status (success=true/false)
		log.setFilename(filename);
		log.setSuccess(uploadOk);
		logDao.merge(log);
		
		logger.info("Uploaded ["+filename+"]");
		return;
	}
	
	private void buildDemoNos(EForm eform, List<Integer> demoList) {
		logger.debug("buildDemoNos() running for eform " + eform.getFormName());
		List<Integer> eformDataList = eformDataDao.findDemographicNosByFormId(eform.getId());
		logger.debug("loaded eformData list for " + eform.getFormName());
		for (Integer eformData : eformDataList) {
			if (!demoList.contains(eformData)) demoList.add(eformData);
		}
		logger.debug("completed building demoList for eform " + eform.getFormName());
	}
	
	private Integer checkRourkeDone(String rourkeFormName, Integer demographicNo) {
		Integer fdid = getLatestUnsentFdids(rourkeFormName, demographicNo);
		if (fdid<0) return null; //no un-uploaded form data
		
		EFormValue eformValue = eformValueDao.findByFormDataIdAndKey(fdid, "visit_date_18m");
		if (eformValue==null) return null;
		
		Date visitDate = UtilDateUtilities.StringToDate(eformValue.getVarValue(), "yyyy-MM-dd");
		if (!checkDate18m(visitDate, demographicNo)) return null;
		
/* Commented by Ronnie 2015-4-16: An unsent Rourke should be uploaded no matter how long 
 * 		
		//check if the form is for 2-3y or 4-5y visit -> not uploading
		eformValue = eformValueDao.findByFormDataIdAndKey(fdid, "visit_date_2y");
		if (eformValue!=null && eformValue.getVarValue()!=null && !eformValue.getVarValue().trim().isEmpty()) {
			return null;
		}
		
		eformValue = eformValueDao.findByFormDataIdAndKey(fdid, "visit_date_4y");
		if (eformValue!=null && eformValue.getVarValue()!=null && !eformValue.getVarValue().trim().isEmpty()) {
			return null;
		}
*/		
		return fdid;
	}
	
	private Integer checkNddsDone(String nddsFormName, Integer demographicNo) {
		Integer fdid = getLatestUnsentFdids(nddsFormName, demographicNo);
		if (fdid<0) return null; //no un-uploaded form data
		
		return fdid;
	}
	
	private Integer checkReport18mDone(String report18mFormName, Integer demographicNo) {
		Integer fdid = getLatestUnsentFdids(report18mFormName, demographicNo);
		if (fdid<0) return null; //no un-uploaded form data
		
		return fdid;
	}
	
	public List<String> getAuthors(Integer demoNo) {
		List<String> results = new ArrayList<String>();
		
		String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
		Integer rourkeFdid = checkRourkeDone(rourkeFormName, demoNo);
		Integer nddsFdid = checkNddsDone(nddsFormName, demoNo);
		Integer report18mFdid = checkReport18mDone(rpt18mFormName, demoNo);
		
		EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);
		EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
		
		EFormValue rourkeProvider = null, nddsProvider = null, report18mProvider = null;
		
		if(rourkeFdid != null) {
			rourkeProvider = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "efmprovider_no");	
		}
		
		if(nddsFdid != null) {
			nddsProvider = eformValueDao.findByFormDataIdAndKey(nddsFdid, "efmprovider_no");	
		}
		if(report18mFdid != null) {
			report18mProvider = eformValueDao.findByFormDataIdAndKey(report18mFdid, "efmprovider_no");
		}
		
		
		if(rourkeProvider != null) {
			results.add(rourkeProvider.getVarValue());
		}
		if(nddsProvider != null) {
			results.add(nddsProvider.getVarValue());
		}
		if(report18mProvider != null) {
			results.add(report18mProvider.getVarValue());
		}
		return results;	
	}
	
	/**
	 * Only goes to formDate level, not time.
	 * 
	 * @param demoNo
	 * @return
	 */
	public Date getLatestDateForTrio(Integer demoNo) {
		String rourkeFormName = oscarProperties.getProperty("born18m_eform_rourke", "Rourke Baby Record");
    	String nddsFormName = oscarProperties.getProperty("born18m_eform_ndds", "Nipissing District Developmental Screen");
    	String rpt18mFormName = oscarProperties.getProperty("born18m_eform_report18m", "Summary Report: 18-month Well Baby Visit");
    	
		Integer rourkeFdid = checkRourkeDone(rourkeFormName, demoNo);
		Integer nddsFdid = checkNddsDone(nddsFormName, demoNo);
		Integer report18mFdid = checkReport18mDone(rpt18mFormName, demoNo);
		
		EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
		
		
		List<EFormData> forms = eformDataDao.findByFdids(Arrays.asList(new Integer[]{rourkeFdid,nddsFdid,report18mFdid}));
		Date latestDate = null;
		for(EFormData form:forms) {
			if(latestDate ==null) {
				latestDate = form.getFormDate();
				continue;
			}
			if(latestDate.before(form.getFormDate())) {
				latestDate = form.getFormDate();
			}
		}
		
		return latestDate;	
	}
	
	private byte[] generateXml(LoggedInInfo loggedInInfo, Integer demographicNo, Integer rourkeFdid, Integer nddsFdid, Integer report18mFdid, boolean useClinicInfoForOrganizationId) {
		HashMap<String,String> suggestedPrefixes = new HashMap<String,String>();
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		opts.setSaveNoXmlDecl();
		opts.setUseDefaultNamespace();
		opts.setSaveNamespacesFirst();
		ByteArrayOutputStream os = null;
		PrintWriter pw = null;
		boolean xmlCreated = false;
		
		BORN18MFormToXML xml = new BORN18MFormToXML(demographicNo);
		try {
			os = new ByteArrayOutputStream();
			pw = new PrintWriter(os, true);
			//pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlCreated = xml.addXmlToStream(loggedInInfo, pw, opts, rourkeFdid, nddsFdid, report18mFdid, useClinicInfoForOrganizationId);
			
			pw.close();
			if (xmlCreated) return os.toByteArray();
		}
		catch(Exception e) {
			logger.warn("Unable to add record",e);
		}
		
		return null;
	}

	private BornTransmissionLog prepareLog() {
		BornTransmissionLog log = new BornTransmissionLog();
		log.setFilename(filenameStart);
		log.setSubmitDateTime(new Date());
		logDao.persist(log);
		
		return log;
	}

	private boolean uploadToBORN(byte[] xmlFile, String filename) {
		String documentDir = oscarProperties.getProperty("DOCUMENT_DIR");
		
		boolean success = false;
		if(documentDir != null && new File(documentDir).exists()) {
			FileOutputStream fos = null;
			try {
				File f = new File(documentDir + File.separator + filename);
				fos = new FileOutputStream(f);
	            fos.write(xmlFile);
				
				success = BornFtpManager.upload18MEWBVDataToRepository(xmlFile, filename);
            }
			catch (IOException e) {
				logger.warn("Unabled to backup file to document dir",e);
			}
			finally {
				try {
	                if (fos!=null) fos.close();
                } catch (IOException e) {
                	logger.warn("Fail to close file output stream",e);
            	}
			}
		} else {
			logger.warn("Cannot find DOCUMENT_DIR");
		}
		return success;
	}

	public void recordFormSent(Integer demographicNo, Integer rourkeFdid, Integer nddsFdid, Integer report18mFdid) {
		List<Integer> fdids = new ArrayList<Integer>();
		if (rourkeFdid!=null) fdids.add(rourkeFdid);
		if (nddsFdid!=null) fdids.add(nddsFdid);
		if (report18mFdid!=null) fdids.add(report18mFdid);
		
		for (Integer fdid : fdids) {
			Integer fid = eformDataDao.find(fdid).getFormId();
			EFormValue eformValue = new EFormValue();
			eformValue.setDemographicId(Integer.valueOf(demographicNo));
			eformValue.setFormDataId(fdid);
			eformValue.setFormId(fid);
			eformValue.setVarName(UPLOADED_TO_BORN);
			eformValue.setVarValue(VALUE_YES);
			eformValueDao.persist(eformValue);
		}
	}
	
	private boolean checkDate18m(Date formDate, Integer demographicNo) {
		Calendar babyBirthday = demographicDao.getDemographic(demographicNo.toString()).getBirthDay();
		
		if (UtilDateUtilities.getNumMonths(babyBirthday.getTime(), formDate)<18) {
			return false;
		}
		return true;
	}
	
	private int getLatestUnsentFdids(String formName, Integer demographicNo) {
		List<EFormData> eformDatas = eformDataDao.findByDemographicIdAndFormName(demographicNo, formName);
		
		Integer latestFdid = -1;
		if (eformDatas==null || eformDatas.isEmpty()) {
			logger.warn(formName+" form data not found for patient #"+demographicNo);
			return latestFdid;
		}
		
		for (EFormData eformData : eformDatas) {
			if (eformData.getId()<latestFdid) continue;
			if (eformData.getSubject()==null || eformData.getSubject().trim().isEmpty()) continue;
			if (eformData.getSubject().toLowerCase().contains("draft")) continue;
			if (checkUploadedToBorn(eformData.getId())) continue;
			
			if (eformData.getId()>latestFdid) latestFdid = eformData.getId();
		}
		return latestFdid;
	}
	
	private boolean hasFormUploaded(String formName, Integer demographicNo) {
		List<EFormData> eformDatas = eformDataDao.findByDemographicIdAndFormName(demographicNo, formName);
		if (eformDatas==null || eformDatas.isEmpty()) {
			return false;
		}
		
		for (EFormData eformData : eformDatas) {
			if (checkUploadedToBorn(eformData.getId())) return true;
		}
		return false;
	}
	
	private boolean checkUploadedToBorn(Integer fdid) {
		EFormValue value = eformValueDao.findByFormDataIdAndKey(fdid, UPLOADED_TO_BORN);
		return (value!=null && value.getVarValue().equals(VALUE_YES));
	}
	
	private String getFileSuffix(Integer logId) {
		long num = logDao.getSeqNoToday(filenameStart, logId);
		String tmp = String.valueOf(num);
		while(tmp.length() <3) {tmp = "0"+tmp;}
		return tmp;
	}
}
