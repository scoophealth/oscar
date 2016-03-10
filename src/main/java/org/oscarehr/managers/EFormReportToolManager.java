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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormReportToolDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormReportTool;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Marc Dumontier
 *
 */
@Service
//@Transactional
public class EFormReportToolManager {

	private Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private EFormReportToolDao eformReportToolDao;
	@Autowired
	private EFormDao eformDao;
	@Autowired
	private EFormValueDao eformValueDao;
	@Autowired
	private EFormDataDao eformDataDao;
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	//@PersistenceContext
	//protected EntityManager entityManager = null;

	
	public List<EFormReportTool> findAll(LoggedInInfo loggedInInfo, Integer offset, Integer limit) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "r", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		List<EFormReportTool> results = eformReportToolDao.findAll(offset, limit);

		return results;
	}

	/*
	 * Updates the eft_latest column to 1 for the latest form from each demographic. This is calculated by latest form date/form time, and in the 
	 * case that there's 2 results, the highest fdid will be marked. 
	 */
	public void markLatest(LoggedInInfo loggedInInfo, Integer eformReportToolId) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "w", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		eformReportToolDao.markLatest(eformReportToolId);
	}
	
	public void addNew(LoggedInInfo loggedInInfo, EFormReportTool eformReportTool, boolean useNameAsTableName) {

		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "w", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		EForm eform = eformDao.findById(eformReportTool.getEformId());
		
		if (eform != null) {
			//get list of possible var_name
			List<String> fields = eformValueDao.findAllVarNamesForEForm(eform.getId());
			eformReportToolDao.addNew(eformReportTool,eform, fields,loggedInInfo.getLoggedInProviderNo(), useNameAsTableName);
		} else {
			logger.info("the eform id passed did not match an existing eform");
		}	
		
	}

	
	public void populateReportTable(LoggedInInfo loggedInInfo, Integer eformReportToolId) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "r", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		EFormReportTool eft = eformReportToolDao.find(eformReportToolId);
		if (eft != null) {

			deleteAllData(eft);
			
			//get all fdid for this fid
			List<Object[]> fdidList = eformDataDao.findMetaFieldsByFormId(eft.getEformId());

			Date dateStarted = new Date();

			
			for (Object[] data : fdidList) {
				Integer fdid = (Integer) data[0];
				Integer demographicNo = (Integer) data[1];
				Date dateFormCreated = createDateFromDateAndTime((Date) data[2], (Date) data[3]);
				String providerNo = (String)data[4];
				List<EFormValue> values = eformValueDao.findByFormDataId(fdid);

				if (values.isEmpty()) {
					continue;
				}

				if(eft.getStartDate() != null && !dateFormCreated.after(eft.getStartDate())) {
					continue;
				}
				
				if(eft.getEndDate() != null && !dateFormCreated.before(eft.getEndDate())) {
					continue;
				}
				eformReportToolDao.populateReportTableItem(eft,values, fdid, demographicNo, dateFormCreated, providerNo);

				logger.debug("Added fdid " + fdid + " to table " + eft.getTableName());
			}

			eft.setDateLastPopulated(dateStarted);
			eft.setLatestMarked(false);
			eformReportToolDao.merge(eft);
		}

	}

	public void deleteAllData(LoggedInInfo loggedInInfo, Integer eformReportToolId) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "w", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		EFormReportTool eft = eformReportToolDao.find(eformReportToolId);
		if(eft != null) {
			deleteAllData(eft);
			eft.setDateLastPopulated(null);
			eft.setLatestMarked(false);
			eformReportToolDao.merge(eft);
		}

	}
	
	private void deleteAllData(EFormReportTool eft) {
		eformReportToolDao.deleteAllData(eft);
	}

	public void remove(LoggedInInfo loggedInInfo, Integer eformReportToolId) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "w", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		EFormReportTool eft = eformReportToolDao.find(eformReportToolId);
		remove(eft);

	}
	
	private void remove(EFormReportTool eft) {
		eformReportToolDao.drop(eft);
		eformReportToolDao.remove(eft.getId());
	}

	
	private Date createDateFromDateAndTime(Date date, Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		Calendar tCal = Calendar.getInstance();
		tCal.setTime(time);

		cal.add(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
		cal.add(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}
	
	public int getNumRecords(LoggedInInfo loggedInInfo,Integer eformReportToolId) {
		return getNumRecords(loggedInInfo, eformReportToolDao.find(eformReportToolId));
	}
	
	@SuppressWarnings("unchecked")
	public Integer getNumRecords(LoggedInInfo loggedInInfo, EFormReportTool eformReportTool) {
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin.eformreporttool", "r", null)) {
			throw new RuntimeException("Access Denied [_admin.eformreporttool]");
		}
		
		return eformReportToolDao.getNumRecords(eformReportTool);
	}
}
