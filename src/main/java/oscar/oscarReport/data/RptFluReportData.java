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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
public class RptFluReportData {
	public class DemoFluDataStruct {
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
			try {
				
				String s1 = "select b.billing_no, b.billing_date from billing b, billingdetail bd where bd.billing_no=b.billing_no and bd.status<>'D' and b.status<>'D' and (bd.service_code='G590A' or bd.service_code='G591A') and b.billing_date <= '2003-04-01' and b.demographic_no='"
						+ demoNo + "' limit 0,1";
				ResultSet resultset = DBHandler.GetSQL(s1);
				if (resultset.next())
					s = resultset.getString("billing_date");
				resultset.close();
			} catch (SQLException sqlexception) {
				MiscUtils.getLogger().debug(sqlexception.getMessage());
			}
			return s;
		}
		public String getBillingDate(String reportYear) {
			String s = "&nbsp;";
			String sDate = reportYear + "-01-01";
			String eDate = reportYear + "-12-31";
			try {
				
				String s1 = "select b.id, b.billing_date from billing_on_cheader1 b, billing_on_item bd where b.demographic_no='"
						+ demoNo
						+ "' and bd.ch1_id=b.id and (bd.service_code='G590A' or bd.service_code='G591A') and b.billing_date >= '"
						+ sDate
						+ "' and b.billing_date <= '"
						+ eDate
						+ "' and  bd.status<>'D' and b.status<>'D' order by b.billing_date desc limit 0,1";
				ResultSet resultset = DBHandler.GetSQL(s1);
				if (resultset.next())
					s = resultset.getString("billing_date");
				resultset.close();
			} catch (SQLException sqlexception) {
				MiscUtils.getLogger().debug(sqlexception.getMessage());
			}
			return s;
		}
		public String demoNo;
		public String demoName;
		public String demoPhone;
		public String demoRosterStatus;
		public String demoAge;
		public String demoDOB;
		public String demoPatientStatus;
		public DemoFluDataStruct() {
		}
	}
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
		try {
			
			String s2 = "select demographic_no, CONCAT(last_name,',',first_name) as demoname, phone, roster_status, patient_status, DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth), '-',(date_of_birth)),'%Y-%m-%d') as dob, (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d')))-(RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) as age from demographic  where (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((year_of_birth),'-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d')))-(RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >= 65 and (patient_status = 'AC' or patient_status = 'UHIP') and (roster_status='RO' or roster_status='NR' or roster_status='FS' or roster_status='RF' or roster_status='PL')";
			if (!s.equals("-1"))
				s2 = s2 + " and provider_no = '" + s + "' ";
			s2 = s2 + "  order by last_name ";
			ResultSet resultset = DBHandler.GetSQL(s2);
			demoList = new ArrayList();
			DemoFluDataStruct demofludatastruct;
			for (; resultset.next(); demoList.add(demofludatastruct)) {
				demofludatastruct = new DemoFluDataStruct();
				demofludatastruct.demoNo = resultset
						.getString("demographic_no");
				demofludatastruct.demoName = resultset.getString("demoname");
				demofludatastruct.demoPhone = resultset.getString("phone");
				demofludatastruct.demoRosterStatus = resultset
						.getString("roster_status");
				demofludatastruct.demoPatientStatus = resultset
						.getString("patient_status");
				demofludatastruct.demoDOB = resultset.getString("dob");
				demofludatastruct.demoAge = resultset.getString("age");
			}
			resultset.close();
		} catch (SQLException sqlexception) {
			MiscUtils.getLogger().debug("Problems");
			MiscUtils.getLogger().debug(sqlexception.getMessage());
		}
	}
	public ArrayList demoList;
	public String years;
}
