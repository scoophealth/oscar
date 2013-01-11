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

import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.DocumentDao.DocumentType;
import org.oscarehr.common.dao.DocumentDao.Module;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.MdsMSH;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 * This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
 * Could use a rewrite
*/
public class RptLabReportData {

	public ArrayList demoList = null;
	public String days = null;

	public RptLabReportData() {
	}

	public ArrayList providerList() {
		ArrayList arrayList = new ArrayList();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> ps = dao.getProvidersByType(ProviderDao.PR_TYPE_DOCTOR);
		Collections.sort(ps, new BeanComparator("lastName"));
		for (Provider p : ps) {
			ArrayList a = new ArrayList();
			a.add(p.getProviderNo());
			a.add(p.getFormattedName());
			arrayList.add(a);
		}
		return arrayList;
	}

	public void labReportGenerate(String providerNo, String days) {
		this.days = days;
		try {
			ResultSet rs;
			// mysql function for dates = select date_sub(now(),interval 1 month); 
			String sql = "select distinct l.demographic_no from formLabReq l , demographic d where " 
					+ " ( to_days( now() ) - to_days(formCreated) ) <= " + " ( to_days( now() ) - to_days( date_sub(now(),interval " + days + " month) ) )" 
					+ " and l.demographic_no = d.demographic_no ";
			if (!providerNo.equals("-1")) {
				sql = sql + " and d.provider_no = '" + providerNo + "' ";
			}
			sql = sql + "  order by d.last_name ";
			
			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			demoList = new ArrayList();
			DemoLabDataStruct d;
			for (Object[] o : dao.runNativeQuery(sql)) {
				d = new DemoLabDataStruct();
				d.demoNo = String.valueOf(o[0]);
				demoList.add(d);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().debug("Problems");
			MiscUtils.getLogger().error("Error", e);
		}

	}

	/**
	*This is a inner class that stores info on demographics.  It will get Consult letters that have been scanned and consults for the patient
	*/
	public class DemoLabDataStruct {
		public String demoNo;
		ArrayList consultList;
		ArrayList conReplyList;

		public ArrayList getLabReqs() {
			try {
				String sql = " select ID, formCreated, provider_no from formLabReq where demographic_no = '" 
						+ demoNo + "' " + " and to_days(now()) - to_days(formCreated) <=  " + " (to_days( now() ) - to_days( date_sub( now(), interval " 
						+ days + " month ) ) )";
				Consult con;
				consultList = new ArrayList();
				FormsDao dao = SpringUtils.getBean(FormsDao.class);
				for (Object[] o : dao.runNativeQuery(sql)) {
					String id = String.valueOf(o[0]);
					String formCreated = String.valueOf(o[1]);
					String provider_no = String.valueOf(o[2]);

					con = new Consult();
					con.requestId = id;
					con.referalDate = formCreated;
					con.proNo = provider_no;
					consultList.add(con);
				}
			} catch (Exception e2) {
				MiscUtils.getLogger().debug(e2.getMessage());
			}
			return consultList;
		}

		public ArrayList getLabReplys() {
			DocumentDao dao = SpringUtils.getBean(DocumentDao.class);
			ConLetter conLetter;
			conReplyList = new ArrayList();
			for (Object[] o : dao.findCtlDocsAndDocsByModuleDocTypeAndModuleId(Module.DEMOGRAPHIC, DocumentType.LAB, ConversionUtils.fromIntString(demoNo))) {
				Document d = (Document) o[1];

				conLetter = new ConLetter();
				conLetter.document_no = "" + d.getDocumentNo();
				conLetter.docdesc = d.getDocdesc();
				conLetter.docfileName = d.getDocfilename();
				if (d.getUpdatedatetime() != null) {
					conLetter.docDate = new Date(d.getUpdatedatetime().getTime());
				}
				conLetter.docStatus = "" + d.getStatus();
				conReplyList.add(conLetter);
			}
			return conReplyList;
		}

		public ArrayList getLabReports(String demographic, java.util.Date startDate) {
			ArrayList list = new ArrayList();
			try {
				PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
				list = new ArrayList();
				for (Object[] o : dao.findRoutingAndPhysicianInfoByTypeAndDemoNo("CML", ConversionUtils.fromIntString(demographic))) {
					PatientLabRouting p = (PatientLabRouting) o[0];
					LabPatientPhysicianInfo l = (LabPatientPhysicianInfo) o[1];

					java.util.Date lab = getDateFromCML(l.getCollectionDate());
					MiscUtils.getLogger().debug(lab + " " + startDate + " " + lab.after(startDate));
					if (startDate != null && lab != null && lab.after(startDate)) {
						Hashtable h = new Hashtable();
						h.put("collectionDate", getCommonDate(lab));
						h.put("id", "" + p.getLabNo());
						h.put("labType", p.getLabType());
						list.add(h);
					}
				}

				for (Object[] o : dao.findRoutingsAndMdsMshByDemoNo(ConversionUtils.fromIntString(demographic))) {
					PatientLabRouting p = (PatientLabRouting) o[0];
					MdsMSH m = (MdsMSH) o[1];

					java.util.Date lab = getDateFromMDS(ConversionUtils.toTimestampString(m.getDateTime()));
					MiscUtils.getLogger().debug(lab + " " + startDate + " " + lab.after(startDate));
					if (startDate != null && lab != null && lab.after(startDate)) {
						Hashtable h = new Hashtable();
						h.put("collectionDate", getCommonDate(lab));
						h.put("id", "" + p.getLabNo());
						h.put("labType", p.getLabType());
						list.add(h);
					}
				}
			} catch (Exception e3) {
				MiscUtils.getLogger().debug(e3.getMessage());
				MiscUtils.getLogger().error("Error", e3);
			}
			return list;
		}

		private String getCommonDate(java.util.Date date) {
			String s = "unknown date";
			try {
				Format formatter = new SimpleDateFormat("yyyy-mm-dd");
				s = formatter.format(date);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to format date " + date, e);
			}
			return s;
		}

		private java.util.Date getDateFromCML(String d) {
			java.util.Date date = null;
			try {
				DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
				date = formatter.parse(d);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to format date " + date, e);
			}
			return date;
		}

		private java.util.Date getDateFromMDS(String d) { //2004-11-17 16:23:23
			java.util.Date date = null;
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				date = formatter.parse(d);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unable to format date " + date, e);
			}
			return date;
		}

		public String getDemographicName() {
			String retval = "&nbsp;";
			DemographicDao dao = SpringUtils.getBean(DemographicDao.class);
			Demographic demo = dao.getDemographic(demoNo);
			if (demo != null) {
				retval = demo.getFullName();
			}
			return retval;
		}

		public final class Consult {
			public String requestId;
			public String referalDate;
			public String serviceId;
			public String specialist;
			public String appDate;
			public String proNo;
		};

		public final class ConLetter {
			public String document_no;
			public String docdesc;
			public String docfileName;
			public String docStatus;
			public java.sql.Date docDate;
		};

	};
}
