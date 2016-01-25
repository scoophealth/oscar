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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class HRMManager {

	@Autowired
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao;
	
	@Autowired
	private HRMDocumentDao hrmDocumentDao;
	
	@Autowired
	private ConsultDocsDao consultDocsDao;
	
	public List<HRMDocument> findByDemographicNo(LoggedInInfo loggedInInfo, Integer demographicNo) {
		
		List<HRMDocument> results = new ArrayList<HRMDocument>();
		
		LogAction.addLogSynchronous(loggedInInfo,"HRMManager.findByDemographicNo", "demographicNo="+demographicNo);

		List<HRMDocumentToDemographic> linkedResults = hrmDocumentToDemographicDao.findByDemographicNo(demographicNo.toString());
				
		for(HRMDocumentToDemographic linkedResult:linkedResults) {
			results.add(hrmDocumentDao.find(Integer.parseInt(linkedResult.getHrmDocumentId())));
		}
		return results;
	}
	
	public List<HRMDocument> findAttached(LoggedInInfo loggedInInfo, Integer demographicNo, Integer consultationRequestId) {
		
		List<HRMDocument> results = new ArrayList<HRMDocument>();
		
		LogAction.addLogSynchronous(loggedInInfo,"HRMManager.findAttached", "demographicNo="+demographicNo + ",requestId="+consultationRequestId);

		if(consultationRequestId == null) {
			return results;
		}
		
		List<ConsultDocs> hrmConsultDocs = consultDocsDao.findByRequestIdAndDocType(consultationRequestId,ConsultDocs.DOCTYPE_HRM);
		
		for(ConsultDocs consultDocs: hrmConsultDocs) {
			results.add(hrmDocumentDao.find(consultDocs.getDocumentNo()));
		}
		
		
		return results;
	}
	
	public List<HRMDocument> findUnattached(LoggedInInfo loggedInInfo, Integer demographicNo, Integer consultationRequestId) {
		
		List<HRMDocument> results = new ArrayList<HRMDocument>();
		
		LogAction.addLogSynchronous(loggedInInfo,"HRMManager.findUnattached", "demographicNo="+demographicNo + ",requestId=" + consultationRequestId);

		//our full set
		List<HRMDocumentToDemographic> linkedResults = hrmDocumentToDemographicDao.findByDemographicNo(demographicNo.toString());
			
		//the ones already attached
		List<ConsultDocs> attachedDocs = consultDocsDao.findByRequestIdAndDocType(consultationRequestId,ConsultDocs.DOCTYPE_HRM);
		
		
		for(HRMDocumentToDemographic linkedResult:linkedResults) {
			boolean found=false;
			for(ConsultDocs cd:attachedDocs) {
				if(cd.getDocumentNo() == Integer.parseInt(linkedResult.getHrmDocumentId())) {
					found=true;
					break;
				}
			}
			if(!found) {
				results.add(hrmDocumentDao.find(Integer.parseInt(linkedResult.getHrmDocumentId())));
			}
		}
		return results;
	}
	

	
}
