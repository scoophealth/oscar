/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.BillingONEAReportDao;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class JdbcBillingErrorRepImpl {
	
	private BillingONEAReportDao billingONEARReportDao = (BillingONEAReportDao) SpringUtils.getBean(BillingONEAReportDao.class);
	
	JdbcBillingLog dbLog = new JdbcBillingLog();

	public List<BillingErrorRepData> getErrorRecords(BillingProviderData val, String fromDate, String toDate, String filename) {
		List<BillingErrorRepData> retval = new ArrayList<BillingErrorRepData>();
		BillingONEAReportDao dao = SpringUtils.getBean(BillingONEAReportDao.class);
		for (BillingONEAReport r : dao.findByMagic(val.getOhipNo(), val.getBillingGroupNo(), val.getSpecialtyCode(), ConversionUtils.fromDateString(fromDate), ConversionUtils.fromDateString(toDate), filename)) {
			toReportData(retval, r);
		}
		return retval;
	}

	private void toReportData(List<BillingErrorRepData> retval, BillingONEAReport r) {
	    BillingErrorRepData obj = null;
	    obj = new BillingErrorRepData();
	    obj.setId("" + r.getId());
	    obj.setBilling_no("" + r.getBillingNo());
	    obj.setProviderohip_no(r.getProviderOHIPNo());
	    obj.setGroup_no(r.getGroupNo());
	    obj.setSpecialty(r.getSpecialty());
	    obj.setProcess_date(ConversionUtils.toDateString(r.getProcessDate()));
	    obj.setHin(r.getHin());
	    obj.setVer(r.getVersion());
	    obj.setDob(ConversionUtils.toDateString(r.getDob()));
	    obj.setRef_no(r.getRefNo());
	    obj.setFacility(r.getFacility());
	    obj.setAdmitted_date(ConversionUtils.toDateString(r.getAdmittedDate()));
	    obj.setClaim_error(r.getClaimError());
	    obj.setCode(r.getCode());
	    obj.setFee(r.getFee());
	    obj.setUnit(r.getUnit());
	    obj.setCode_date(ConversionUtils.toDateString(r.getCodeDate()));
	    obj.setDx(r.getDx());
	    obj.setExp(r.getExp());
	    obj.setCode_error(r.getCodeError());
	    obj.setReport_name(r.getReportName());
	    obj.setStatus("" + r.getStatus());
	    obj.setComment(r.getComment());
	    retval.add(obj);
    }

	public List<BillingErrorRepData> getErrorRecords(List<BillingProviderData> list, String fromDate, String toDate, String filename) {
		List<BillingErrorRepData> retval = new ArrayList<BillingErrorRepData>();
		if (list == null) {
			return retval;
		}
		
		BillingONEAReportDao dao = SpringUtils.getBean(BillingONEAReportDao.class);
		for(BillingONEAReport r : dao.findByMagic(list, ConversionUtils.fromDateString(fromDate), ConversionUtils.fromDateString(toDate), filename)) {
			toReportData(retval, r);
		}

		return retval;
	}

	public boolean deleteErrorReport(BillingErrorRepData val) {
		List<BillingONEAReport> bs = billingONEARReportDao.findByProviderOhipNoAndGroupNoAndSpecialtyAndProcessDateAndBillingNo(val.getProviderohip_no(), val.getGroup_no(), val.getSpecialty(), ConversionUtils.fromDateString(val.getProcess_date()),  Integer.parseInt(val.getBilling_no()));
		for (BillingONEAReport b : bs) {
			billingONEARReportDao.remove(b.getId());
		}
		return true;
	}

	public int addErrorReportRecord(BillingErrorRepData val) {
		BillingONEAReport b = new BillingONEAReport();
		b.setProviderOHIPNo(val.providerohip_no);
		b.setGroupNo(val.group_no);
		b.setSpecialty(val.specialty);
		b.setProcessDate(ConversionUtils.fromDateString(val.process_date,"yyyyMMdd"));
		b.setHin(val.hin);
		b.setVersion(val.ver);
		b.setDob(ConversionUtils.fromDateString(val.dob));
		b.setBillingNo(Integer.parseInt(val.billing_no));
		b.setRefNo(val.ref_no);
		b.setFacility(val.facility);
		b.setAdmittedDate(ConversionUtils.fromDateString(val.admitted_date,"yyyyMMdd"));
		b.setClaimError(val.claim_error);
		b.setCode(val.code);
		b.setFee(val.fee);
		b.setUnit(val.unit);
		b.setCodeDate(ConversionUtils.fromDateString(val.code_date,"yyyyMMdd"));
		b.setDx(val.dx);
		b.setExp(val.exp);
		b.setCodeError(val.code_error);
		b.setReportName(val.report_name);
		b.setStatus(val.status.toCharArray()[0]);
		b.setComment(val.comment);

		billingONEARReportDao.persist(b);

		return b.getId();

	}

	public boolean updateErrorReportStatus(String id, String val) {
		BillingONEAReport b = billingONEARReportDao.find(Integer.valueOf(id));
		if (b != null) {
			b.setStatus(val.toCharArray()[0]);
			billingONEARReportDao.merge(b);
		}
		return true;
	}

}
