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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class CdsForm4Action {

	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");

	public static CdsClientForm createCdsClientForm(LoggedInInfo loggedInInfo, Integer admissionId, Integer clientId, Date initialContactDate, Date assessmentDate, Date serviceInitiationDate, boolean signed)
	{
		CdsClientForm cdsClientForm=new CdsClientForm();
		cdsClientForm.setAdmissionId(admissionId);
		cdsClientForm.setCdsFormVersion("4");
		cdsClientForm.setClientId(clientId);
		cdsClientForm.setFacilityId(loggedInInfo.getCurrentFacility().getId());
		cdsClientForm.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		cdsClientForm.setInitialContactDate(initialContactDate);
		cdsClientForm.setAssessmentDate(assessmentDate);
		cdsClientForm.setServiceInitiationDate(serviceInitiationDate);
		cdsClientForm.setSigned(signed);
		cdsClientFormDao.persist(cdsClientForm);
		
		return(cdsClientForm);
	}
	
	public static void addCdsClientFormData(Integer cdsClientFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null) return;
		
		CdsClientFormData cdsClientFormData=new CdsClientFormData();
		
		cdsClientFormData.setCdsClientFormId(cdsClientFormId);
		cdsClientFormData.setQuestion(question);
		cdsClientFormData.setAnswer(answer);
		
		cdsClientFormDataDao.persist(cdsClientFormData);
	}
}
