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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.DocumentDao.DocumentType;
import org.oscarehr.common.dao.DocumentDao.Module;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
*This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
*/
public class RptConsultReportData {

	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao) SpringUtils.getBean("consultationServiceDao");

	public ArrayList demoList = null;
	public String days = null;

	public RptConsultReportData() {
	}

	public ArrayList<ArrayList<String>> providerList() {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
		for (Provider p : dao.getProvidersByType(ProviderDao.PR_TYPE_DOCTOR)) {
			ArrayList<String> a = new ArrayList<String>();
			a.add(p.getProviderNo());
			a.add(p.getFormattedName());
			arrayList.add(a);
		}
		return arrayList;
	}

	public void consultReportGenerate(String providerNo, String lastMonthCountInReport) {
		this.days = lastMonthCountInReport;

		Date timeLimit = convertMonthCountToAbsoluteDate(lastMonthCountInReport);

		ConsultationRequestDao dao = SpringUtils.getBean(ConsultationRequestDao.class);
		demoList = new ArrayList();
		for (Object[] o : dao.findRequests(timeLimit, providerNo)) {

			DemoConsultDataStruct d = new DemoConsultDataStruct();
			d.demoNo = String.valueOf(o[1]);
			demoList.add(d);
		}
	}

	private Date convertMonthCountToAbsoluteDate(String lastMonthCountInReport) {
		Calendar c = GregorianCalendar.getInstance();
		c.roll(Calendar.MONTH, ConversionUtils.fromIntString(lastMonthCountInReport) * (-1)); // roll back
		Date timeLimit = c.getTime();
		return timeLimit;
	}

	/**
	*This is a inner class that stores info on demographics.  It will get Consult letters that have been scanned and consults for the patient
	*/
	public class DemoConsultDataStruct {
		public String demoNo;
		ArrayList consultList;
		ArrayList conReplyList;

		public ArrayList getConsults() {
			ConsultationRequestDao dao = SpringUtils.getBean(ConsultationRequestDao.class);
			Date date = convertMonthCountToAbsoluteDate(days);

			consultList = new ArrayList();
			for (ConsultationRequest c : dao.findRequestsByDemoNo(ConversionUtils.fromIntString(demoNo), date)) {
				Consult con = new Consult();
				con.requestId = c.getId().toString();
				con.referalDate = ConversionUtils.toDateString(c.getReferralDate());
				con.serviceId = "" + c.getServiceId();
				con.specialist = "" + c.getSpecialistId();
				con.appDate = ConversionUtils.toDateString(c.getAppointmentDate());
				consultList.add(con);
			}

			return consultList;
		}

		public ArrayList getConReplys() {
			DocumentDao dao = SpringUtils.getBean(DocumentDao.class);
			ConLetter conLetter;
			conReplyList = new ArrayList();
			for (Object[] o : dao.getCtlDocsAndDocsByDemoId(ConversionUtils.fromIntString(demoNo), Module.DEMOGRAPHIC, DocumentType.CONSULT)) {
				Document d = (Document) o[1];

				conLetter = new ConLetter();
				conLetter.document_no = "" + d.getDocumentNo();
				conLetter.docdesc = d.getDocdesc();
				conLetter.docfileName = d.getDocfilename();
				conLetter.docDate = new java.sql.Date(d.getUpdatedatetime().getTime());
				conLetter.docStatus = "" + d.getStatus();
				conReplyList.add(conLetter);
			}

			return conReplyList;
		}

		public String getDemographicName() {
			DemographicDao dao = SpringUtils.getBean(DemographicDao.class);
			Demographic demo = dao.getDemographic(demoNo);
			if (demo != null) {
				return demo.getFormattedName();
			}
			return "&nbsp;";
		}

		public String getService(String serId) {
			String retval = "";
			ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(serId));
			if (cs != null) {
				retval = cs.getServiceDesc();
			}
			return retval;
		}

		public String getSpecialist(String specId) {
			ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
			ProfessionalSpecialist p = dao.find(ConversionUtils.fromIntString(specId));
			if (p != null) {
				return p.getFormattedName();
			}
			return "";
		}

		public final class Consult {
			public String requestId;
			public String referalDate;
			public String serviceId;
			public String specialist;
			public String appDate;

			public String getService(String serId) {
				String retval = "&nbsp;";
				ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(serId));
				if (cs != null) {
					retval = cs.getServiceDesc();
				}
				return retval;
			}

			public String getSpecialist(String specId) {
				ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
				ProfessionalSpecialist p = dao.find(ConversionUtils.fromIntString(specId));
				if (p != null) {
					return p.getFormattedName();
				}
				return "&nbsp;";
			}

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
