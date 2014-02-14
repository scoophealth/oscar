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

package oscar.oscarReport.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class RptFluReportData {

	public ArrayList<DemoFluDataStruct> demoList;
	public String years;

	public RptFluReportData() {
		demoList = null;
		years = null;
	}

	@SuppressWarnings("unchecked")
	public List<Provider> providerList() {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> pList = dao.getActiveProviders();
		
		return pList;
	}

	public void fluReportGenerate(String s, String s1) {
		years = s1;
		
		DemographicDao dao = SpringUtils.getBean(DemographicDao.class);
		
		demoList = new ArrayList<DemoFluDataStruct>();
		DemoFluDataStruct demofludatastruct;
		for(Object[] o : dao.findDemographicsForFluReport(s)) {
			String demographic_no = String.valueOf(o[0]);
			String demoname = String.valueOf(o[0]);
			String phone = String.valueOf(o[0]);
			String roster_status = String.valueOf(o[0]);
			String patient_status = String.valueOf(o[0]);
			String dob = String.valueOf(o[0]);
			String age = String.valueOf(o[0]);
			
			demofludatastruct = new DemoFluDataStruct();
			demofludatastruct.demoNo = demographic_no;
			demofludatastruct.demoName = demoname;
			demofludatastruct.demoPhone = phone;
			demofludatastruct.demoRosterStatus = roster_status;
			demofludatastruct.demoPatientStatus = patient_status;
			demofludatastruct.demoDOB = dob;
			demofludatastruct.demoAge = age;
			
			demoList.add(demofludatastruct);
		}
	}

	public class DemoFluDataStruct {

		public String demoNo;
		public String demoName;
		public String demoPhone;
		public String demoRosterStatus;
		public String demoAge;
		public String demoDOB;
		public String demoPatientStatus;

		public String getDemoNo() {
			return demoNo;
		}

		public String getDemoName() {
			return demoName;
		}

		public String getDemoPhone() {
			return demoPhone;
		}

		public String getDemoAge() {
			return demoAge;
		}

		public String getDemoDOB() {
			return demoDOB;
		}

		public String getBillingDate() {
			String s = "&nbsp;";

			BillingDao dao = SpringUtils.getBean(BillingDao.class);
			for (Billing b : dao.findBillingsByDemoNoServiceCodeAndDate(ConversionUtils.fromIntString(demoNo), ConversionUtils.fromDateString("2003-04-01"), Arrays.asList(new String[] { "G590A", "G591A" }))) {
				s = ConversionUtils.toDateString(b.getBillingDate());
			}
			return s;
		}

		public String getBillingDate(String reportYear) {
			String s = "&nbsp;";

			String sDate = reportYear + "-01-01";
			String eDate = reportYear + "-12-31";

			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			for (BillingONCHeader1 b : dao.findBillingsByDemoNoCh1HeaderServiceCodeAndDate(ConversionUtils.fromIntString(demoNo), Arrays.asList(new String[] { "G590A", "G591A" }), ConversionUtils.fromDateString(sDate), ConversionUtils.fromDateString(eDate))) {
				s = ConversionUtils.toDateString(b.getBillingDate());
				break;
			}

			return s;
		}
	}
}
