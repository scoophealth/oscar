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

package oscar.oscarEncounter.data;

import java.util.Date;

import org.oscarehr.common.dao.EChartDao;
import org.oscarehr.common.model.EChart;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class EctEChartBean {

	public Date eChartTimeStamp;
	public String providerNo;
	public String userName;
	public String demographicNo;
	public String socialHistory;
	public String familyHistory;
	public String medicalHistory;
	public String ongoingConcerns;
	public String reminders;
	public String encounter;
	public String subject;

	public EctEChartBean() {
	}

	public void setEChartBean(String demoNo) {
		demographicNo = demoNo;

		EChartDao dao = SpringUtils.getBean(EChartDao.class);
		EChart ec = dao.getLatestChart(ConversionUtils.fromIntString(demoNo));

		if (ec != null) {
			eChartTimeStamp = ec.getTimestamp();
			socialHistory = ec.getSocialHistory();
			familyHistory = ec.getFamilyHistory();
			medicalHistory = ec.getMedicalHistory();
			ongoingConcerns = ec.getOngoingConcerns();
			reminders = ec.getReminders();
			encounter = ec.getEncounter();
			subject = ec.getSubject();
			providerNo = ec.getProviderNo();
		} else {
			eChartTimeStamp = null;
			socialHistory = "";
			familyHistory = "";
			medicalHistory = "";
			ongoingConcerns = "";
			reminders = "";
			encounter = "";
			subject = "";
			providerNo = "";
		}
	}

}
