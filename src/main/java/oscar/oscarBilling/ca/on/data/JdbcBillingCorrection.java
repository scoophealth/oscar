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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONEAReportDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingONRepoDao;
import org.oscarehr.common.dao.RaDetailDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.RaDetail;
import org.oscarehr.util.SpringUtils;

public class JdbcBillingCorrection {
	private static final Logger _logger = Logger.getLogger(JdbcBillingCorrection.class);
	
	JdbcBillingLog dbLog = new JdbcBillingLog();
	
	private BillingONCHeader1Dao billingHeaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private BillingONRepoDao billingRepoDao = SpringUtils.getBean(BillingONRepoDao.class);
	private BillingONItemDao billingItemDao = SpringUtils.getBean(BillingONItemDao.class);
	private BillingONEAReportDao billingEaReportDao = SpringUtils.getBean(BillingONEAReportDao.class);
	private RaDetailDao raDetailDao = SpringUtils.getBean(RaDetailDao.class);
	
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat tsFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public boolean updateBillingStatus(String id, String status, String providerNo) {
		BillingONCHeader1 h = billingHeaderDao.find(Integer.valueOf(id));
		if(h != null) {
			h.setStatus(status);
			billingHeaderDao.merge(h);
			dbLog.addBillingLog(providerNo, "updateBillingStatus", "", id);
			
			List<BillingONItem> items = billingItemDao.getBillingItemByCh1Id(Integer.valueOf(id));
			for(BillingONItem i:items) {
				i.setStatus(status);
			}
			 dbLog.addBillingLog(providerNo, "updateBillingStatus-items", "", id);
			return true;
		} else {
			return false;
		}
	}


	public List<String> getBillingCH1NoStatusByAppt(String id) {
		List<String> obj = new ArrayList<String>();		
		List<BillingONCHeader1> headers = billingHeaderDao.findByAppointmentNo(Integer.parseInt(id));
		for(BillingONCHeader1 h:headers) {
			obj.add(h.getId().toString());
			obj.add(h.getStatus());
		}
		
		return obj;
	}

	
	public List<String> getBillingCH1NoStatusByBillNo(String id) {
		List<String> obj = new ArrayList<String>();
		BillingONCHeader1 header = billingHeaderDao.find(Integer.parseInt(id));
		obj.add(header.getId().toString());
		obj.add(header.getStatus());
		
		return obj;
	}
	
	// 0-cheader1 obj, 1 - item1obj, 2 - item2obj, ...
	public List getBillingRecordObj(String id) {
		List obj = new Vector();
		BillingClaimHeader1Data ch1Obj = null;
		BillingItemData itemObj = null;

		BillingONCHeader1 h = billingHeaderDao.find(Integer.parseInt(id));
		if(h != null) {
			ch1Obj = new BillingClaimHeader1Data();
			ch1Obj.setId(h.getId().toString());
			ch1Obj.setTransc_id(h.getTranscId());
			ch1Obj.setRec_id(h.getRecId());
			ch1Obj.setHin(h.getHin());
			ch1Obj.setVer(h.getVer());
			ch1Obj.setDob(h.getDob());

			ch1Obj.setPay_program(h.getPayProgram());
			ch1Obj.setPayee(h.getPayee());
			ch1Obj.setRef_num(h.getRefNum());
			ch1Obj.setFacilty_num(h.getFaciltyNum());
			try {
				if(h.getAdmissionDate() != null)
					ch1Obj.setAdmission_date(dateFormatter.format(h.getAdmissionDate()));
				else
					ch1Obj.setAdmission_date("");
			}catch(ParseException e) {
				ch1Obj.setAdmission_date("");
			}
			ch1Obj.setRef_lab_num(h.getRefLabNum());
			ch1Obj.setMan_review(h.getManReview());
			ch1Obj.setLocation(h.getLocation());
			ch1Obj.setClinic(h.getClinic());

			ch1Obj.setDemographic_no(h.getDemographicNo().toString());
			ch1Obj.setProviderNo(h.getProviderNo());
			ch1Obj.setAppointment_no(h.getAppointmentNo().toString());
			ch1Obj.setDemographic_name(h.getDemographicName());
			ch1Obj.setSex(h.getSex());
			ch1Obj.setProvince(h.getProvince());

			if(h.getBillingDate() != null)
				ch1Obj.setBilling_date(dateFormatter.format(h.getBillingDate()));
			else
				ch1Obj.setBilling_date("");
			
			if(h.getBillingTime() != null)
				ch1Obj.setBilling_time(timeFormatter.format(h.getBillingTime()));
			else
				ch1Obj.setBilling_time("");
			
			ch1Obj.setTotal(h.getTotal());
			ch1Obj.setPaid(h.getPaid());
			ch1Obj.setStatus(h.getStatus());
			ch1Obj.setComment(h.getComment());
			ch1Obj.setVisittype(h.getVisitType());
			ch1Obj.setProvider_ohip_no(h.getProviderOhipNo());
			ch1Obj.setProvider_rma_no(h.getProviderRmaNo());
			ch1Obj.setAsstProvider_no(h.getAsstProviderNo());
			ch1Obj.setCreator(h.getCreator());			
			if(h.getTimestamp() != null)
				ch1Obj.setUpdate_datetime(tsFormatter.format(h.getTimestamp()));
			else
				ch1Obj.setUpdate_datetime("");

			ch1Obj.setClinic(h.getClinic());
							
			obj.add(ch1Obj);
		}
		
		List<BillingONItem> items = billingItemDao.getActiveBillingItemByCh1Id(Integer.parseInt(id));
		for(BillingONItem i:items) {
			itemObj = new BillingItemData();
			itemObj.setId(i.getId().toString());
			itemObj.setCh1_id(i.getCh1Id().toString());
			itemObj.setTransc_id(i.getTranscId());
			itemObj.setRec_id(i.getRecId());
			itemObj.setService_code(i.getServiceCode());
			itemObj.setFee(i.getFee());
			itemObj.setSer_num(i.getServiceCount());
			if(i.getServiceDate() != null)
				itemObj.setService_date(dateFormatter.format(i.getServiceDate()));
			else
				itemObj.setService_date("");
			
			String diagcode = i.getDx();
			diagcode = ":::".equals(diagcode) ? "   " : diagcode;
			itemObj.setDx(diagcode);
			itemObj.setDx1(i.getDx1());
			itemObj.setDx2(i.getDx2());
			itemObj.setStatus(i.getStatus());
			if(i.getLastEditDT() != null)
				itemObj.setTimestamp(tsFormatter.format(i.getLastEditDT()));
			else
				itemObj.setTimestamp("");
		
			obj.add(itemObj);
		}
		
	
		return obj;
	}

	public List<String> getBillingRejectList(String id) {
		List<String> obj = new ArrayList<String>();
		List<BillingONEAReport> reports = billingEaReportDao.findByBillingNo(Integer.parseInt(id));
		for(BillingONEAReport report:reports) {
			String error = report.getClaimError().trim();
			if (error.length() > 2) {
				String temp[] = error.split("\\s");
				for (int i = 0; i < temp.length; i++) {
					obj.add(temp[i]);
				}
			}
			error = report.getCodeError().trim();
			if (error.length() > 1) {
				String temp[] = error.split("\\s");
				for (int i = 0; i < temp.length; i++) {
					obj.add(temp[i]);
				}
			}
		}
		
		return obj;
	}

	public List<String> getBillingExplanatoryList(String id) {
		List<String> obj = new ArrayList<String>();
		List<RaDetail> rds = raDetailDao.findByBillingNo(Integer.parseInt(id));
		
		String tHeaderNo = "";

		for(RaDetail rad:rds) {
			if ("".equals(tHeaderNo)) {
				tHeaderNo = String.valueOf(rad.getId());
			} else if (!tHeaderNo.equals(rad.getId().toString())) {
				break;
			}
			obj.add(rad.getErrorCode());
		}
		
		return obj;
	}

}
