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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.BornTransmissionLogDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.BornTransmissionLog;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;

public class BORN18MConnector {
	private final String UPLOADED_TO_BORN = "uploaded_to_BORN";
	private final String VALUE_YES = "Yes";

	private Integer rourkeFdid;
	private Integer nddsFdid;
	private Integer report18mFdid;
	private Integer demographicNo;
	
	private BornTransmissionLogDao logDao = SpringUtils.getBean(BornTransmissionLogDao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	private EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);
	private Logger logger = MiscUtils.getLogger();
	
	private OscarProperties oscarProperties = OscarProperties.getInstance();
	private String filenameStart = "BORN_" + oscarProperties.getProperty("born18m_orgcode", "") + "_18MWB_" + oscarProperties.getProperty("born18m_env", "T");
	
	public BORN18MConnector(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public void setRourkeFdid(Integer fdid) {
		this.rourkeFdid = fdid;
	}
	
	public void setNddsFdid(Integer fdid) {
		this.nddsFdid = fdid;
	}
	
	public void setReport18mFdid(Integer fdid) {
		this.report18mFdid = fdid;
	}
	
	
	
	
	public void updateBorn() {
		if (rourkeFdid==null || nddsFdid==null || report18mFdid==null) return; //cannot upload data if not all 3 forms are completed

		byte[] born18mXml = generateXml();
		if (born18mXml == null) return;
		
		BornTransmissionLog log = prepareLog();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(new Date());
		String filename = filenameStart + "_" + dt + "_" + getFileSuffix(log.getId()) + ".xml";

		boolean uploadOk = uploadToBORN(born18mXml, filename);
		if (uploadOk) recordFormSent();

		//update log filename and status (success=true/false)
		log.setFilename(filename);
		log.setSuccess(uploadOk);
		logDao.merge(log);
		
		return;
	}

	
	public boolean checkBabyNotYet18m() {
		Calendar babyBirthday = demographicDao.getDemographic(demographicNo.toString()).getBirthDay();
		Calendar today = new GregorianCalendar();
		
		if (UtilDateUtilities.getNumMonths(babyBirthday, today)<18) {
			return true;
		}
		return false;
	}
	
	public Integer checkRourkeDone(String rourkeFormName) {
		if (rourkeFdid==null) {
			rourkeFdid = getFdid18m(rourkeFormName);
			if (rourkeFdid==null) return null; //no 18M form data
		}
		
		EFormValue eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "visit_date_18m");
		if (eformValue==null) return null;
		
		Date visitDate = UtilDateUtilities.StringToDate(eformValue.getVarValue(), "yyyy-MM-dd");
		if (!checkDate18m(visitDate)) return null;
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "height_18m");
		if (eformValue==null) return null;
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "weight_18m");
		if (eformValue==null) return null;
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "headcirc_18m");
		if (eformValue==null) return null;
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "subject");
		if (eformValue==null) return null;
		
		if (eformValue.getVarValue()!=null && eformValue.getVarValue().toLowerCase().contains("draft")) {
			return null;
		}
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "visit_date_2y");
		if (eformValue!=null && eformValue.getVarValue()!=null && !eformValue.getVarValue().trim().isEmpty()) {
			return null;
		}
		
		eformValue = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "visit_date_4y");
		if (eformValue!=null && eformValue.getVarValue()!=null && !eformValue.getVarValue().trim().isEmpty()) {
			return null;
		}
		
		return rourkeFdid;
	}
	
	public Integer checkNddsDone(String nddsFormName) {
		if (nddsFdid==null) {
			nddsFdid = getFdid18m(nddsFormName);
			if (nddsFdid==null) return null; //no 18M form data
		}
		
		EFormValue eformValue = eformValueDao.findByFormDataIdAndKey(nddsFdid, "subject");
		if (eformValue==null) return null;
		
		if (eformValue.getVarValue()!=null && eformValue.getVarValue().toLowerCase().contains("draft")) {
			return null;
		}
		
		return nddsFdid;
	}
	
	public Integer checkReport18mDone(String report18mFormName) {
		if (report18mFdid==null) {
			report18mFdid = getFdid18m(report18mFormName);
			if (report18mFdid==null) return null; //no 18M form data
		}
		
		return report18mFdid;
	}
	
	
	
	
	private byte[] generateXml() {
		BORN18MFormToXML xml = new BORN18MFormToXML();
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
		
		try {
			os = new ByteArrayOutputStream();
			pw = new PrintWriter(os, true);
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xmlCreated = xml.addXmlToStream(pw, opts, demographicNo, rourkeFdid, nddsFdid, report18mFdid);
			
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

	private void recordFormSent() {
		Integer[] fdids = {rourkeFdid, nddsFdid, report18mFdid};
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
	
	private boolean checkDate18m(Date date) {
		Calendar babyBirthday = demographicDao.getDemographic(demographicNo.toString()).getBirthDay();
		
		if (UtilDateUtilities.getNumMonths(babyBirthday.getTime(), date)<18) {
			return false;
		}
		return true;
	}
	
	private Integer getFdid18m(String formName) {
		List<EFormData> eformDatas = eformDataDao.findByDemographicIdAndFormName(demographicNo, formName);
		if (eformDatas==null || eformDatas.isEmpty()) return null; //no form data
		
		Integer fdid = null;
		for (EFormData eformData : eformDatas) {
			if (fdid==null || fdid < eformData.getId()) {
				if (checkDate18m(eformData.getFormDate())) {
					fdid = eformData.getId();
				}
			}
		} //fdid = max fdid with form date > 18smonths
		
		if (!checkUploadedToBorn(fdid)) return fdid;
		else return null;
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
