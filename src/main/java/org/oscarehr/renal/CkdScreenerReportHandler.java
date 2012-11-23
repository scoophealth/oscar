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
package org.oscarehr.renal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class CkdScreenerReportHandler {

	private DxresearchDAO dxResearchDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	private CkdScreener ckdScreener = new CkdScreener();
	private OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat apptFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public List<CKDReportContainer> generateReport() {
		List<CKDReportContainer> results = new ArrayList<CKDReportContainer>();
		
		//get list of patients with an active dx of OscarCode:CKDSCREEN
		for(Dxresearch dr:dxResearchDao.findActive("OscarCode","CKDSCREEN")) {
			CKDReportContainer result = new CKDReportContainer();
			
			int demographicNo = dr.getDemographicNo();
			Demographic demographic = demographicDao.getDemographicById(demographicNo);
			result.setDemographic(demographic);
			result.setDiabetic(dxResearchDao.activeEntryExists(demographicNo, "icd9", "250"));
			result.setHypertensive(dxResearchDao.activeEntryExists(demographicNo, "icd9", "401"));
			
			result.setMedication(ckdScreener.checkMedication(demographicNo));
			
			DemographicExt ab = demographicExtDao.getLatestDemographicExt(demographicNo, "aboriginal");
			if(ab != null && ab.getValue().equals("Yes")) {
				result.setAboriginalStr("Yes");
			} else if(ab != null && ab.getValue().equals("No")) {
				result.setAboriginalStr("No");
			}
			else {
				result.setAboriginalStr("Unknown");
			}
			
			result.setBp(ckdScreener.checkBP(demographicNo));
			
			result.setHx(ckdScreener.checkCpp(demographicNo));
			
			result.setLabs(ckdScreener.checkLabs(demographicNo));
			
			List<OscarLog> logs = oscarLogDao.findByActionContentAndDemographicId("create","CkdPatientLetter",demographicNo);
			if(logs.size()>0) {
				Provider p = providerDao.getProvider(logs.get(0).getProviderNo());
				result.setLastPatientLetter("Last created by " + p.getFormattedName() + " on " + formatter.format(logs.get(0).getCreated()));
			}
			
			logs = oscarLogDao.findByActionContentAndDemographicId("create","CkdLabReq",demographicNo);
			if(logs.size()>0) {
				Provider p = providerDao.getProvider(logs.get(0).getProviderNo());
				result.setLastLabReq("Last created by " + p.getFormattedName() + " on " + formatter.format(logs.get(0).getCreated()));
			}
			
			List<Appointment> appts = appointmentDao.getAllByDemographicNo(demographicNo);
			Collections.sort(appts,Appointment.APPT_DATE_COMPARATOR);
			for(Appointment appt:appts) {
				if(appt.getAppointmentDate().before(new Date())) {
					Calendar time = Calendar.getInstance();
					time.setTime(appt.getStartTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(appt.getAppointmentDate());
					cal.set(Calendar.HOUR_OF_DAY,time.get(Calendar.HOUR_OF_DAY));
					cal.set(Calendar.MINUTE,time.get(Calendar.MINUTE));
					Date d = cal.getTime();
					
					result.setLastVisit(apptFormatter.format(d));
					break;
				}
			}
			results.add(result);
		}
		return results;
	}
}
