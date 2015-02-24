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
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONEAReportDao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingOnItemPaymentDao;
import org.oscarehr.common.dao.BillingOnTransactionDao;
import org.oscarehr.common.dao.RaDetailDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONEAReport;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingOnItemPayment;
import org.oscarehr.common.model.BillingOnTransaction;
import org.oscarehr.common.model.RaDetail;
import org.oscarehr.util.SpringUtils;


public class JdbcBillingCorrection {
	JdbcBillingLog dbLog = new JdbcBillingLog();
	
	private BillingONCHeader1Dao billingHeaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	//private BillingONRepoDao billingRepoDao = SpringUtils.getBean(BillingONRepoDao.class);
	private BillingONItemDao billingItemDao = SpringUtils.getBean(BillingONItemDao.class);
	private BillingONEAReportDao billingEaReportDao = SpringUtils.getBean(BillingONEAReportDao.class);
	private RaDetailDao raDetailDao = SpringUtils.getBean(RaDetailDao.class);
	private BillingOnItemPaymentDao itemPaymentDao = SpringUtils.getBean(BillingOnItemPaymentDao.class);
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat tsFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean updateBillingClaimHeader(BillingClaimHeader1Data ch1Obj) throws ParseException {
		BillingONCHeader1 c = billingHeaderDao.find(ch1Obj.getId());
		c.setTranscId(ch1Obj.getTransc_id());
		c.setRecId(ch1Obj.getRec_id());
		c.setHin(ch1Obj.getHin());
		c.setVer(ch1Obj.getVer());
		c.setDob(ch1Obj.getDob());
		c.setPayProgram(ch1Obj.getPay_program());
		c.setPayee(ch1Obj.getPayee());
		c.setRefNum(ch1Obj.getRef_num());
		c.setFaciltyNum(ch1Obj.getFacilty_num());
		c.setAdmissionDate(dateFormatter.parse(ch1Obj.getAdmission_date()));
		c.setRefLabNum(ch1Obj.getRef_lab_num());
		c.setManReview(ch1Obj.getMan_review());
		c.setLocation(ch1Obj.getLocation());
		c.setDemographicNo(Integer.parseInt(ch1Obj.getDemographic_no()));
		c.setProviderNo(ch1Obj.getProvider_no());
		c.setAppointmentNo(Integer.parseInt(ch1Obj.getAppointment_no()));
		c.setDemographicName(StringEscapeUtils.escapeSql(ch1Obj.getDemographic_name()));
		c.setSex(ch1Obj.getSex());
		c.setProvince(ch1Obj.getProvince());
		c.setBillingDate(dateFormatter.parse(ch1Obj.getBilling_date()));
		c.setBillingTime(dateFormatter.parse(ch1Obj.getBilling_time()));
		c.setTotal(new BigDecimal(ch1Obj.getTotal()));
		c.setPaid(new BigDecimal(ch1Obj.getPaid()));
		c.setStatus(ch1Obj.getStatus());
		c.setComment(ch1Obj.getComment());
		c.setVisitType(ch1Obj.getVisittype());
		c.setProviderOhipNo(ch1Obj.getProvider_ohip_no());
		c.setProviderRmaNo(ch1Obj.getProvider_rma_no());
		c.setApptProviderNo(ch1Obj.getApptProvider_no());
		c.setAsstProviderNo(ch1Obj.getAsstProvider_no());
		c.setCreator(ch1Obj.getCreator());
		c.setClinic(ch1Obj.getClinic()==null?"":ch1Obj.getClinic());
		
		billingHeaderDao.merge(c);			
		
		
	/*	String sql = "update billing_on_cheader1 set transc_id='" + ch1Obj.getTransc_id() + "'," + " rec_id='"
				+ ch1Obj.getRec_id() + "'," + " hin='" + ch1Obj.getHin() + "'," + " ver='" + ch1Obj.getVer() + "',"
				+ " dob='" + ch1Obj.getDob() + "'," + " pay_program='" + ch1Obj.getPay_program() + "'," + " payee='"
				+ ch1Obj.getPayee() + "'," + " ref_num='" + ch1Obj.getRef_num() + "'," + " facilty_num='"
				+ ch1Obj.getFacilty_num() + "'," + " admission_date='" + ch1Obj.getAdmission_date() + "',"
				+ " ref_lab_num='" + ch1Obj.getRef_lab_num() + "'," + " man_review='" + ch1Obj.getMan_review() + "',"
				+ " location='" + ch1Obj.getLocation()

				+ "'," + " demographic_no='" + ch1Obj.getDemographic_no() + "'," + " provider_no='"
				+ ch1Obj.getProviderNo() + "'," + " appointment_no='" + ch1Obj.getAppointment_no() + "',"
				+ " demographic_name='" + StringEscapeUtils.escapeSql(ch1Obj.getDemographic_name()) + "'," + " sex='"
				+ ch1Obj.getSex() + "'," + " province='" + ch1Obj.getProvince() + "'," + " billing_date='"
				+ ch1Obj.getBilling_date() + "'," + " billing_time='" + ch1Obj.getBilling_time() + "'," + " total='"
				+ ch1Obj.getTotal() + "'," + " paid='" + ch1Obj.getPaid() + "'," + " status='" + ch1Obj.getStatus()
				+ "'," + " comment1='" + ch1Obj.getComment() + "'," + " visittype='" + ch1Obj.getVisittype() + "',"
				+ " provider_ohip_no='" + ch1Obj.getProvider_ohip_no() + "'," + " provider_rma_no='"
				+ ch1Obj.getProvider_rma_no() + "'," + " apptProvider_no='" + ch1Obj.getApptProvider_no() + "',"
				+ " asstProvider_no='" + ch1Obj.getAsstProvider_no() + "'," + " creator='" + ch1Obj.getCreator()

				+ "', clinic=" + (ch1Obj.getClinic()==null?"null":"'"+ch1Obj.getClinic()+"'")

				+ " where id=" + ch1Obj.getId();
				*/
		
		return true;
	}

	public boolean updateBillingOneItem(BillingItemData val) throws ParseException {
		BillingONItem b = billingItemDao.find(val.id);
		if(b!=null) {
			b.setTranscId(val.transc_id);
			b.setRecId(val.rec_id);
			b.setServiceCode(val.service_code);		
			b.setFee(val.fee);
			b.setServiceCount(val.ser_num);
			b.setServiceDate(dateFormatter.parse(val.service_date));	
			b.setDx(val.dx);
			b.setDx1(val.dx1);
			b.setDx2(val.dx2);
			b.setStatus(val.status);
			
			billingItemDao.merge(b);
		}
		return true;		
	}
	
	
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
	
	@SuppressWarnings("rawtypes")
	public List getPayprogramByBillNo(String id){
		BillingONCHeader1 b = billingHeaderDao.find(id);		
		List obj = new Vector();
		if(b!=null) {
			obj.add(b.getPayProgram());
		}
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
                        			
			if( h.getAppointmentNo() != null ) {
				ch1Obj.setAppointment_no(h.getAppointmentNo().toString());
			}
			else {
				ch1Obj.setAppointment_no(null);
			}
			
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
			
			ch1Obj.setTotal(String.valueOf(h.getTotal().doubleValue()));
			ch1Obj.setPaid(String.valueOf(h.getPaid().doubleValue()));
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

			// get billTo from billing_on_ext
			BillingONExtDao billExtDao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
			BillingONExt ext = billExtDao.getClaimExtItem(Integer.parseInt(ch1Obj.getId()), Integer.parseInt(ch1Obj.getDemographic_no()), "billTo");
			if (ext != null) {
				ch1Obj.setBillto(ext.getValue());
			}

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

				List<BillingOnItemPayment> itemPayList = itemPaymentDao.getAllByItemId(Integer.parseInt(itemObj.getId()));
				BigDecimal sumPaid = BigDecimal.ZERO;
				BigDecimal sumDiscount = BigDecimal.ZERO;
				BigDecimal sumRefund = BigDecimal.ZERO;
				if (itemPayList != null) {
					for (BillingOnItemPayment itemPay : itemPayList) {
						sumPaid = sumPaid.add(itemPay.getPaid());
						sumDiscount = sumDiscount.add(itemPay.getDiscount());
						sumRefund = sumRefund.add(itemPay.getRefund());
					}
				}
				itemObj.setPaid(sumPaid.toString());
				itemObj.setDiscount(sumDiscount.toString());
				itemObj.setRefund(sumRefund.toString());

		
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

	public String getBillingTotal(String id) {
		BillingONCHeader1 b = billingHeaderDao.find(id);
		if(b!=null) {
			return String.valueOf(b.getTotal().doubleValue());			
		}
		return "";
		
	}
	
	
	public String getBillingPaid(String id) {
		BillingONCHeader1 b = billingHeaderDao.find(id);
		if(b!=null) {
			return String.valueOf(b.getPaid().doubleValue());			
		}
		return "";
		
	}

	public boolean updateBillingTotal(String fee, String id) {
		BillingONCHeader1 b = billingHeaderDao.find(id);
		if(b!=null) {
			b.setTotal(new BigDecimal(fee));
			billingHeaderDao.merge(b);
			return true;
		}
		return false;
		
	}
	
	public boolean updateBillingPaid(String fee, String id) {
		BillingONCHeader1 b = billingHeaderDao.find(id);
		if(b!=null) {
			b.setPaid(new BigDecimal(fee));
			billingHeaderDao.merge(b);
			return true;
		}
		return false;		
	}

	public void addInsertOneBillItemTrans(BillingClaimHeader1Data billHeader, BillingItemData billItem, String updateProviderNo) {
		BillingOnTransactionDao billOnTransDao = (BillingOnTransactionDao)SpringUtils.getBean(BillingOnTransactionDao.class);
		if (billOnTransDao == null) {
			return;
		}
		
		BillingOnTransaction billTrans = new BillingOnTransaction();
		billTrans.setActionType(BillingDataHlp.ACTION_TYPE.C.name());
		try {
			billTrans.setAdmissionDate(new SimpleDateFormat("yyyy-MM-dd").parse(billHeader.getAdmission_date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			billTrans.setAdmissionDate(null);
		}
		try {
			billTrans.setBillingDate(new SimpleDateFormat("yyyy-MM-dd").parse(billHeader.getBilling_date()));
		} catch (Exception e) {
			billTrans.setBillingDate(null);
		}
		billTrans.setBillingNotes(billHeader.getComment());
		billTrans.setCh1Id(Integer.parseInt(billHeader.getId()));
		billTrans.setClinic(billHeader.getClinic());
		billTrans.setCreator(billHeader.getCreator());
		billTrans.setDemographicNo(Integer.parseInt(billHeader.getDemographic_no()));
		billTrans.setDxCode(billItem.getDx());
		billTrans.setFacilityNum(billHeader.getFacilty_num());
		billTrans.setManReview(billHeader.getMan_review());
		billTrans.setProviderNo(billHeader.getProviderNo());
		billTrans.setProvince(billHeader.getProvince());
		billTrans.setPayProgram(billHeader.getPay_program());
		billTrans.setRefNum(billHeader.getRef_num());
		
		billTrans.setPaymentDate(null);
		billTrans.setPaymentId(0);
		billTrans.setPaymentType(0);
		
		billTrans.setServiceCode(billItem.getService_code());
		billTrans.setServiceCodeInvoiced(billItem.getFee());
		billTrans.setServiceCodePaid(BigDecimal.ZERO);
		billTrans.setServiceCodeDiscount(BigDecimal.ZERO);
		billTrans.setServiceCodeRefund(BigDecimal.ZERO);
		billTrans.setServiceCodeNum(billItem.getSer_num());
		
		billTrans.setSliCode(billHeader.getLocation());
		billTrans.setUpdateProviderNo(updateProviderNo);
		billTrans.setVisittype(billHeader.getVisittype());
		billTrans.setUpdateDatetime(new Timestamp(new Date().getTime()));
		billTrans.setStatus(billItem.getStatus());
		
		billOnTransDao.persist(billTrans);
	}
	
	public void addUpdateOneBillItemTrans(BillingClaimHeader1Data billHeader, BillingItemData billItem, String updateProviderNo) {
		BillingOnTransactionDao billOnTransDao = (BillingOnTransactionDao)SpringUtils.getBean(BillingOnTransactionDao.class);
		if (billOnTransDao == null) {
			return;
		}
		
		BillingOnTransaction billTrans = new BillingOnTransaction();
		billTrans.setActionType(BillingDataHlp.ACTION_TYPE.U.name());
		try {
			billTrans.setAdmissionDate(new SimpleDateFormat("yyyy-MM-dd").parse(billHeader.getAdmission_date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			billTrans.setAdmissionDate(null);
		}
		try {
			billTrans.setBillingDate(new SimpleDateFormat("yyyy-MM-dd").parse(billHeader.getBilling_date()));
		} catch (Exception e) {
			billTrans.setBillingDate(null);
		}
		billTrans.setBillingNotes(billHeader.getComment());
		billTrans.setCh1Id(Integer.parseInt(billHeader.getId()));
		billTrans.setClinic(billHeader.getClinic());
		billTrans.setCreator(billHeader.getCreator());
		billTrans.setDemographicNo(Integer.parseInt(billHeader.getDemographic_no()));
		billTrans.setFacilityNum(billHeader.getFacilty_num());
		billTrans.setManReview(billHeader.getMan_review());
		billTrans.setProviderNo(billHeader.getProviderNo());
		billTrans.setProvince(billHeader.getProvince());
		billTrans.setPayProgram(billHeader.getPay_program());
		billTrans.setRefNum(billHeader.getRef_num());
		
		billTrans.setPaymentDate(null);
		billTrans.setPaymentId(0);
		billTrans.setPaymentType(0);
		billTrans.setServiceCode(billItem.getService_code());
		billTrans.setServiceCodeInvoiced(billItem.getFee());
		billTrans.setServiceCodeNum(billItem.getSer_num());
		billTrans.setDxCode(billItem.getDx());
		billTrans.setStatus(billItem.getStatus());
		
		billTrans.setServiceCodePaid(BigDecimal.ZERO);
		billTrans.setServiceCodePaid(BigDecimal.ZERO);
		billTrans.setServiceCodePaid(BigDecimal.ZERO);
		
		billTrans.setSliCode(billHeader.getLocation());
		billTrans.setUpdateProviderNo(updateProviderNo);
		billTrans.setVisittype(billHeader.getVisittype());
		billTrans.setUpdateDatetime(new Timestamp(new Date().getTime()));
		
		billOnTransDao.persist(billTrans);
	}

}
