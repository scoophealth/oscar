/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.OcanClientFormDao;
import org.oscarehr.common.dao.OcanClientFormDataDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.OcanClientForm;
import org.oscarehr.common.model.OcanClientFormData;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class OcanFormAction {
	
	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");

	private static OcanClientFormDao ocanClientFormDao = (OcanClientFormDao) SpringUtils.getBean("ocanClientFormDao");
	private static OcanClientFormDataDao ocanClientFormDataDao = (OcanClientFormDataDao) SpringUtils.getBean("ocanClientFormDataDao");

	
	public static OcanStaffForm createOcanStaffForm(LoggedInInfo loggedInInfo, Integer admissionId, Integer clientId, boolean signed)
	{
		OcanStaffForm ocanStaffForm=new OcanStaffForm();
		ocanStaffForm.setAdmissionId(admissionId);
		ocanStaffForm.setOcanFormVersion("1.2");		
		ocanStaffForm.setClientId(clientId);
		ocanStaffForm.setFacilityId(loggedInInfo.getCurrentFacility().getId());
		ocanStaffForm.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		ocanStaffForm.setSigned(signed);
		
		return(ocanStaffForm);
	}
	
	public static OcanStaffForm createOcanStaffForm(LoggedInInfo loggedInInfo,String ocanStaffFormId, Integer clientId, boolean signed)
	{
		OcanStaffForm ocanStaffForm=new OcanStaffForm();
		if(ocanStaffFormId==null || "".equals(ocanStaffFormId) || "null".equals(ocanStaffFormId) || "0".equals(ocanStaffFormId)) {
			
			//ocanStaffForm.setAdmissionId(admissionId);
			ocanStaffForm.setAssessmentId(ocanStaffForm.getId());
			ocanStaffForm.setOcanFormVersion("1.2");		
			ocanStaffForm.setClientId(clientId);
			ocanStaffForm.setFacilityId(loggedInInfo.getCurrentFacility().getId());			
			ocanStaffForm.setSigned(signed);
		} else {
			ocanStaffForm = OcanForm.getOcanStaffForm(Integer.valueOf(ocanStaffFormId));
		}
		return(ocanStaffForm);
	}
	
	public static void saveOcanStaffForm(OcanStaffForm ocanStaffForm) {			
		if(ocanStaffFormDao.findOcanStaffFormById(ocanStaffForm.getId())==null) {
			ocanStaffFormDao.persist(ocanStaffForm);
			ocanStaffForm.setAssessmentId(ocanStaffForm.getId());
			ocanStaffFormDao.merge(ocanStaffForm);
		} else {
			ocanStaffForm.setId(null);
			ocanStaffFormDao.persist(ocanStaffForm);
		}
		
	}
	
	public static void addOcanStaffFormData(Integer ocanStaffFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null || answer=="") return;
		OcanStaffFormData ocanStaffFormData;
		if(ocanStaffFormDataDao.findByQuestion(ocanStaffFormId, question).isEmpty()) {
			ocanStaffFormData=new OcanStaffFormData();
			ocanStaffFormData.setOcanStaffFormId(ocanStaffFormId);
			ocanStaffFormData.setQuestion(question);
			ocanStaffFormData.setAnswer(answer);			
			ocanStaffFormDataDao.persist(ocanStaffFormData); //create
		} else {
			ocanStaffFormData = ocanStaffFormDataDao.findLatestByQuestion(ocanStaffFormId, question);
			ocanStaffFormData.setOcanStaffFormId(ocanStaffFormId);
			ocanStaffFormData.setQuestion(question);
			ocanStaffFormData.setAnswer(answer);			
			ocanStaffFormDataDao.merge(ocanStaffFormData); //update
		}
	}
	
	public static void addOcanStaffFormDataMultipleAnswers(Integer ocanStaffFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null || answer=="") return;
		OcanStaffFormData ocanStaffFormData;
		
			ocanStaffFormData=new OcanStaffFormData();
			ocanStaffFormData.setOcanStaffFormId(ocanStaffFormId);
			ocanStaffFormData.setQuestion(question);
			ocanStaffFormData.setAnswer(answer);			
			ocanStaffFormDataDao.persist(ocanStaffFormData); //create
				
	}
	public static OcanClientForm createOcanClientForm(LoggedInInfo loggedInInfo, Integer clientId)
	{
		OcanClientForm ocanClientForm=new OcanClientForm();		
		ocanClientForm.setOcanFormVersion("1.2");		
		ocanClientForm.setClientId(clientId);
		ocanClientForm.setFacilityId(loggedInInfo.getCurrentFacility().getId());
		ocanClientForm.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		
		
		return(ocanClientForm);
	}
	
	public static void saveOcanClientForm(LoggedInInfo loggedInInfo, OcanClientForm ocanClientForm) {
		ocanClientForm.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		ocanClientForm.setProviderName(loggedInInfo.getLoggedInProvider().getFormattedName());
		
		ocanClientFormDao.persist(ocanClientForm);
	}
	
	public static void addOcanClientFormData(Integer ocanClientFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null) return;
		
		OcanClientFormData ocanClientFormData=new OcanClientFormData();
		
		ocanClientFormData.setOcanClientFormId(ocanClientFormId);
		ocanClientFormData.setQuestion(question);
		ocanClientFormData.setAnswer(answer);
		
		ocanClientFormDataDao.persist(ocanClientFormData);
	}
	

}
