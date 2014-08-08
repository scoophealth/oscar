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
package org.oscarehr.research.eaaps;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicStudyDao;
import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicStudy;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.pageUtil.DemographicExportHelper;
import oscar.util.ConversionUtils;
import cds.OmdCdsDocument;
import cds.PatientRecordDocument.PatientRecord;
import cds.PersonalHistoryDocument.PersonalHistory;
import cdsDt.ResidualInformation;
import cdsDt.ResidualInformation.DataElement;

/**
 * This action is responsible for exporting a study to eAAP server
 * for further processing.
 */
public class EaapsExportAction extends Action {

	private static Logger logger = Logger.getLogger(EaapsExportAction.class);

	private boolean debugEnabled = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("eaaps.debug", "false"));
	
	private DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
	
	private DemographicStudyDao demoStudyDao = SpringUtils.getBean(DemographicStudyDao.class);
	
	private OscarAuditLogger auditLogger = OscarAuditLogger.getInstance();
	
	private StudyDataDao studyDataDao = SpringUtils.getBean(StudyDataDao.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		EaapsExportForm eaapsForm = (EaapsExportForm) form;
		int studyId = eaapsForm.getStudyId();
		List<DemographicStudy> studies = demoStudyDao.findByStudyNo(studyId);
		
		// build meds exports for all records in the study
		List<ExportEntry> docs = new ArrayList<ExportEntry>();
		for(DemographicStudy s : studies) {
			if (s == null || s.getId() == null) {
				logger.warn("Invalid study data " + s);
				continue;
			}
			
			String demoId = "" + s.getId().getDemographicNo();
			if (logger.isInfoEnabled()) {
				logger.info("Preparing export data for demographic Id " + demoId);
			}
			
			Demographic demo = demoDao.getDemographic(demoId);
			if (demo == null) {
				logger.warn("Unable to find demographic instance for " + demoId);
				continue;
			}
			
			EaapsHash hash = new EaapsHash(demo); 
			
			OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
			OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
			PatientRecord patientRec = omdCds.addNewPatientRecord();
			PersonalHistory ph = patientRec.addNewPersonalHistory();
			ResidualInformation ri = ph.addNewResidualInfo();
			DataElement de = ri.addNewDataElement();
			de.setName("HashedId");
			de.setContent(hash.getHash());
			de.setDataType("text");
			
			if (debugEnabled) {
				de = ri.addNewDataElement();
				de.setName("RepresentativeId");
				de.setContent(hash.getKey());
				de.setDataType("text");
			}
			
			try {
				DemographicExportHelper exportManager = SpringUtils.getBean(DemographicExportHelper.class);
				exportManager.addMedications(demoId, patientRec);
			} catch (Exception e) {
				// at this point, we still want to continue, ignoring the meds export errors
				logger.error("Unable to export medications for demographic ID " + demoId 
						+ ". Skipping export for this demographic number.", e);
			}
			
			String providerNo = demo.getProviderNo();
			if (providerNo == null) {
				providerNo = "";
			}
			docs.add(new ExportEntry(omdCdsDoc, s.getId().getDemographicNo(), demoId, hash.getHash(), providerNo));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Prepared export for push to eaaps server. There is a total of " + docs.size() + " records to be pushed.");
		}
		
		String clinicName = OscarProperties.getInstance().getProperty("eaaps.clinic", "");
		if (clinicName == null) {
			clinicName = "";
		}
		if (!clinicName.isEmpty()) {
			clinicName = clinicName.concat("_");
		}
		
		// push records 
		SshHandler handler = null;
		try {
			handler = new SshHandler();
			handler.connect();
			for(ExportEntry e : docs) {
				// make sure we persist hash for further lookup
				StudyData studyData = getStudyData(e.getDemographicId(), studyId);
				if (studyData != null) {
					studyData.setProviderNo(e.getProviderNo());
					studyData.setContent(e.getHash());
					studyDataDao.saveEntity(studyData);
				} else {
					logger.warn("Unable to find study data for " + e.getDemographicId() + " and study Id " + studyId);
				}
				
				//  now save the record to the server
				String demographicData = e.getDocContent();
				String fileName = clinicName.concat(String.format("%08d", ConversionUtils.fromIntString(e.getEntryId())) + ".xml");
				try {
					handler.put(demographicData, fileName);
				} catch (Exception ee) {
					logger.error("Unable to export " + fileName, ee);
				}
				
				// finally log the update
				auditLogger.log(loggedInInfo, "eaaps export", "demographic", e.getDemographicId(), demographicData);
				
				if (logger.isInfoEnabled()) {
					logger.info("Pushed " + e + " to eaaps server successfully.");
				}
			}
			
			request.setAttribute("message", "Exported study successfully.");
		} catch (Exception e) {
			logger.error("Unable to upload demographic entry", e);
			
			request.setAttribute("message", "Unable to export study: " + e.getMessage());
		} finally {
			if (handler != null) {
				handler.close();
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Completed EAAPS export successfully.");
		}
		
		return mapping.findForward("status");
	}

	private StudyData getStudyData(Integer demographicId, int studyId) {
	    List<StudyData> datum = studyDataDao.findByDemoAndStudy(demographicId, studyId);
	    if (datum.isEmpty()) {
	    	StudyData data = new StudyData();
	    	data.setDemographicNo(demographicId);
	    	data.setStudyNo(studyId);
	    	return data;
	    } else {
	    	if (datum.size() > 1) {
	    		logger.warn("Multiple data entries are found for " + demographicId + " in study " + studyId + ". Expected at most one.");
	    	}
	    	
	    	return datum.get(0);
	    }
    }
}
