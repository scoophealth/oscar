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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.billing.CA.ON.dao.BillingONDiskNameDao;
import org.oscarehr.billing.CA.ON.dao.BillingONFilenameDao;
import org.oscarehr.billing.CA.ON.dao.BillingONHeaderDao;
import org.oscarehr.billing.CA.ON.model.BillingONDiskName;
import org.oscarehr.billing.CA.ON.model.BillingONFilename;
import org.oscarehr.billing.CA.ON.model.BillingONHeader;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingONPaymentDao;
import org.oscarehr.common.dao.BillingONRepoDao;
import org.oscarehr.common.dao.BillingOnItemPaymentDao;
import org.oscarehr.common.dao.BillingOnTransactionDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.common.model.BillingONRepo;
import org.oscarehr.common.model.BillingOnItemPayment;
import org.oscarehr.common.model.BillingOnTransaction;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class JdbcBillingClaimImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingClaimImpl.class);
	private BillingONHeaderDao dao = SpringUtils.getBean(BillingONHeaderDao.class);
	private BillingONCHeader1Dao cheaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);
	private BillingONExtDao extDao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
	private BillingONDiskNameDao diskNameDao = SpringUtils.getBean(BillingONDiskNameDao.class);
	private BillingONFilenameDao filenameDao = SpringUtils.getBean(BillingONFilenameDao.class);
	private BillingONRepoDao repoDao = SpringUtils.getBean(BillingONRepoDao.class);
	private ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
	

	SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat tsFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public int addOneBatchHeaderRecord(BillingBatchHeaderData val) {
		BillingONHeader b = new BillingONHeader();
		b.setDiskId(Integer.parseInt(val.disk_id));
		b.setTransactionId(val.transc_id);
		b.setRecordId(val.rec_id);
		b.setSpecId(val.spec_id);
		b.setMohOffice(val.moh_office);
		b.setBatchId(val.batch_id);
		b.setOperator(val.operator);
		b.setGroupNum(val.group_num);
		b.setProviderRegNum(val.provider_reg_num);
		b.setSpecialty(val.specialty);
		b.sethCount(val.h_count);
		b.setrCount(val.r_count);
		b.settCount(val.t_count);
		b.setBatchDate(new Date());
		b.setCreateDateTime(new Date());
		b.setUpdateDateTime(new Date());
		b.setCreator(val.creator);
		b.setAction(val.action);
		b.setComment(val.comment);
		
		dao.persist(b);
		
		return b.getId();
	}

	public int addOneClaimHeaderRecord(LoggedInInfo loggedInInfo, BillingClaimHeader1Data val) {
		BillingONCHeader1 b = new BillingONCHeader1();
		b.setHeaderId(0);
		b.setTranscId(val.transc_id);
		b.setRecId(val.rec_id);
		b.setHin(val.hin);
		b.setVer(val.ver);
		b.setDob(val.dob);
		b.setPayProgram(val.pay_program);
		b.setPayee(val.payee);
		b.setRefNum(val.ref_num);
		b.setFaciltyNum(val.facilty_num);
		if(val.admission_date.length()>0)
			try{
				b.setAdmissionDate(dateformatter.parse(val.admission_date));
			}catch(ParseException e){/*empty*/}
		
		b.setRefLabNum(val.ref_lab_num);
		b.setManReview(val.man_review);
		b.setLocation(val.location);
		b.setDemographicNo(Integer.parseInt(val.demographic_no));
		b.setProviderNo(val.provider_no);
		String apptNo = StringUtils.trimToNull(val.appointment_no);
		
		if( apptNo != null ) {
			b.setAppointmentNo(Integer.parseInt(val.appointment_no));
		}
		else {
			b.setAppointmentNo(null);
		}
		
		b.setDemographicName(StringEscapeUtils.escapeSql(val.demographic_name));
		b.setSex(val.sex);
		b.setProvince(val.province);
		if(val.billing_date.length()>0)
			try {
				b.setBillingDate(dateformatter.parse(val.billing_date));
			}catch(ParseException e){/*empty*/}
		if(val.billing_time.length()>0)
			try {
				b.setBillingTime(timeFormatter.parse(val.billing_time));
			}catch(ParseException e){MiscUtils.getLogger().error("Invalid time", e);}

		
		b.setTotal(new BigDecimal(val.total==null?"0.00":val.total));
				
		if(val.paid == null || val.paid.isEmpty()){
			b.setPaid(new BigDecimal("0.00"));
		}else{
			b.setPaid(new BigDecimal(val.paid));
		}
		
		b.setStatus(val.status);
		b.setComment(StringEscapeUtils.escapeSql(val.comment));
		b.setVisitType(val.visittype);
		b.setProviderOhipNo(val.provider_ohip_no);
		b.setProviderRmaNo(val.provider_rma_no);
		b.setApptProviderNo(val.apptProvider_no);
		b.setAsstProviderNo(val.asstProvider_no);
		b.setCreator(val.creator);
		b.setClinic(val.clinic);
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
		
		if(pp != null) {
			b.setProgramNo(pp.getProgramId().intValue());
		}
		
		cheaderDao.persist(b);
		
		return b.getId();
	}
	
	public int addOneClaimHeaderRecord(BillingClaimHeader1Data val) {
		BillingONCHeader1 b = new BillingONCHeader1();
		b.setHeaderId(0);
		b.setTranscId(val.transc_id);
		b.setRecId(val.rec_id);
		b.setHin(val.hin);
		b.setVer(val.ver);
		b.setDob(val.dob);
		b.setPayProgram(val.pay_program);
		b.setPayee(val.payee);
		b.setRefNum(val.ref_num);
		b.setFaciltyNum(val.facilty_num);
		if(val.admission_date.length()>0)
			try{
				b.setAdmissionDate(dateformatter.parse(val.admission_date));
			}catch(ParseException e){/*empty*/}
		
		b.setRefLabNum(val.ref_lab_num);
		b.setManReview(val.man_review);
		b.setLocation(val.location);
		b.setDemographicNo(Integer.parseInt(val.demographic_no));
		b.setProviderNo(val.provider_no);
		String apptNo = StringUtils.trimToNull(val.appointment_no);
		
		if( apptNo != null ) {
			b.setAppointmentNo(Integer.parseInt(val.appointment_no));
		}
		else {
			b.setAppointmentNo(null);
		}
		
		b.setDemographicName(val.demographic_name);
		b.setSex(val.sex);
		b.setProvince(val.province);
		if(val.billing_date.length()>0)
			try {
				b.setBillingDate(dateformatter.parse(val.billing_date));
			}catch(ParseException e){/*empty*/}
		if(val.billing_time.length()>0)
			try {
				b.setBillingTime(timeFormatter.parse(val.billing_time));
			}catch(ParseException e){MiscUtils.getLogger().error("Invalid time", e);}

		
		b.setTotal(new BigDecimal(val.total==null?"0.00":val.total));
				
		if(val.paid == null || val.paid.isEmpty()){
			b.setPaid(new BigDecimal("0.00"));
		}else{
			b.setPaid(new BigDecimal(val.paid));
		}
		
		b.setStatus(val.status);
		b.setComment(val.comment);
		b.setVisitType(val.visittype);
		b.setProviderOhipNo(val.provider_ohip_no);
		b.setProviderRmaNo(val.provider_rma_no);
		b.setApptProviderNo(val.apptProvider_no);
		b.setAsstProviderNo(val.asstProvider_no);
		b.setCreator(val.creator);
		b.setClinic(val.clinic);
		
		cheaderDao.persist(b);
		
		return b.getId();
	}

	public boolean addItemRecord(List lVal, int id) {
	
		boolean retval = true;
		for (int i = 0; i < lVal.size(); i++) {
			BillingItemData val = (BillingItemData) lVal.get(i);
			
			BillingONItem b = new BillingONItem();
			b.setCh1Id(id);
			b.setTranscId(val.transc_id);
			b.setRecId(val.rec_id);
			b.setServiceCode(val.service_code);
			b.setFee(val.fee);
			b.setServiceCount(val.ser_num);
			if(val.service_date.length()>0)
				try {
					b.setServiceDate(dateformatter.parse(val.service_date));
				}catch(ParseException e){/*empty*/}
			b.setDx(val.dx);
			b.setDx1(val.dx1);
			b.setDx2(val.dx2);
			b.setStatus(val.status);
			
			itemDao.persist(b);
			val.setId(b.getId().toString());
		}
		return retval;
	}

	public boolean addItemPaymentRecord(List lVal, int id, int paymentId, int paymentType, Date paymentDate) {
		int retval = 0;
		BillingOnItemPayment billOnItemPayment = null;
		Timestamp ts = new Timestamp(paymentDate.getTime());
		BillingOnItemPaymentDao billOnItemPaymentDao = (BillingOnItemPaymentDao)SpringUtils.getBean(BillingOnItemPaymentDao.class);
		for (int i = 0; i < lVal.size(); i++) {
			BillingItemData val = (BillingItemData) lVal.get(i);
			billOnItemPayment = new BillingOnItemPayment();
			billOnItemPayment.setBillingOnItemId(Integer.parseInt(val.getId()));
			billOnItemPayment.setBillingOnPaymentId(paymentId);
			billOnItemPayment.setCh1Id(id);
			try {
				billOnItemPayment.setDiscount(new BigDecimal(val.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			} catch (Exception e) {
				billOnItemPayment.setDiscount(new BigDecimal("0.00"));
			}
			try {
				billOnItemPayment.setPaid(new BigDecimal(val.getPaid()).setScale(2, BigDecimal.ROUND_HALF_UP));
			} catch (Exception e) {
				billOnItemPayment.setPaid(new BigDecimal("0.00"));
			}
			billOnItemPayment.setPaymentTimestamp(ts);
			try {
				billOnItemPayment.setRefund(new BigDecimal(val.getRefund()).setScale(2, BigDecimal.ROUND_HALF_UP));
			} catch (Exception e) {
				billOnItemPayment.setRefund(new BigDecimal("0.00"));
			}
			billOnItemPaymentDao.persist(billOnItemPayment);
			val.setId(billOnItemPayment.getId().toString());
		}
		return (retval != 0);
	}

	private void addCreate3rdInvoiceTrans(BillingClaimHeader1Data billHeader, List<BillingItemData> billItemList, BillingONPayment billOnPayment) {
		if (billItemList.size() < 1) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp updateTs = new Timestamp(new Date().getTime());
		BillingOnTransaction billTrans = null;
		BillingOnTransactionDao billTransDao = (BillingOnTransactionDao)SpringUtils.getBean(BillingOnTransactionDao.class);
		for (BillingItemData billItem : billItemList) {
			billTrans = new BillingOnTransaction();
			billTrans.setActionType(BillingDataHlp.ACTION_TYPE.C.name());
			try {
				billTrans.setAdmissionDate(sdf.parse(billHeader.getAdmission_date()));
			} catch (Exception e) {
				billTrans.setAdmissionDate(null);
			}
			try {
				billTrans.setBillingDate(sdf.parse(billHeader.getBilling_date()));
			} catch (Exception e) {
				billTrans.setBillingDate(null);
			}
			
			billTrans.setBillingNotes(billHeader.getComment());
			billTrans.setBillingOnItemPaymentId(Integer.parseInt(billItem.getId()));
			billTrans.setCh1Id(Integer.parseInt(billHeader.getId()));
			billTrans.setClinic(billHeader.getClinic());
			billTrans.setCreator(billHeader.getCreator());
			billTrans.setDemographicNo(Integer.parseInt(billHeader.getDemographic_no()));
			billTrans.setDxCode(billItem.getDx());
			billTrans.setFacilityNum(billHeader.getFacilty_num());
			billTrans.setManReview(billHeader.getMan_review());
			billTrans.setPaymentDate(billOnPayment.getPaymentDate());
			billTrans.setPaymentId(billOnPayment.getId());
			billTrans.setPaymentType(billOnPayment.getPaymentTypeId());
			billTrans.setPayProgram(billHeader.getPay_program());
			billTrans.setProviderNo(billHeader.getProviderNo());
			billTrans.setProvince(billHeader.getProvince());
			billTrans.setRefNum(billHeader.getRef_num());
			billTrans.setServiceCode(billItem.getService_code());
			billTrans.setServiceCodeInvoiced(billItem.getFee());
			try {
				billTrans.setServiceCodeDiscount(new BigDecimal(billItem.getDiscount()));
			} catch (Exception e) {
				billTrans.setServiceCodeDiscount(BigDecimal.ZERO);
			}
			billTrans.setServiceCodeNum(billItem.getSer_num());
			try {
				billTrans.setServiceCodePaid(new BigDecimal(billItem.getPaid()));
			} catch (Exception e) {
				billTrans.setServiceCodePaid(BigDecimal.ZERO);
			}
			try {
				billTrans.setServiceCodeRefund(new BigDecimal(billItem.getRefund()));
			} catch (Exception e) {
				billTrans.setServiceCodeRefund(BigDecimal.ZERO);
			}
			billTrans.setStatus(billHeader.getStatus());
			billTrans.setSliCode(billHeader.getLocation());
			billTrans.setUpdateDatetime(updateTs);
			billTrans.setUpdateProviderNo(billHeader.getCreator());
			billTrans.setVisittype(billHeader.getVisittype());
			billTransDao.persist(billTrans);
		}
	}
	
	public void addCreateOhipInvoiceTrans(BillingClaimHeader1Data billHeader, List<BillingItemData> billItemList) {
		if (billItemList.size() < 1) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp updateTs = new Timestamp(new Date().getTime());
		BillingOnTransaction billTrans = null;
		BillingOnTransactionDao billTransDao = (BillingOnTransactionDao)SpringUtils.getBean(BillingOnTransactionDao.class);
		for (BillingItemData billItem : billItemList) {
			billTrans = new BillingOnTransaction();
			billTrans.setActionType(BillingDataHlp.ACTION_TYPE.C.name());
			try {
				billTrans.setAdmissionDate(sdf.parse(billHeader.getAdmission_date()));
			} catch (Exception e) {
				billTrans.setAdmissionDate(null);
			}
			try {
				billTrans.setBillingDate(sdf.parse(billHeader.getBilling_date()));
			} catch (Exception e) {
				billTrans.setBillingDate(null);
			}
			
			billTrans.setBillingNotes(billHeader.getComment());
			billTrans.setBillingOnItemPaymentId(Integer.parseInt(billItem.getId()));
			billTrans.setCh1Id(Integer.parseInt(billHeader.getId()));
			billTrans.setClinic(billHeader.getClinic());
			billTrans.setCreator(billHeader.getCreator());
			billTrans.setDemographicNo(Integer.parseInt(billHeader.getDemographic_no()));
			billTrans.setDxCode(billItem.getDx());
			billTrans.setFacilityNum(billHeader.getFacilty_num());
			billTrans.setManReview(billHeader.getMan_review());
			billTrans.setPaymentDate(null);
			billTrans.setPaymentId(0);
			billTrans.setPaymentType(0);
			billTrans.setPayProgram(billHeader.getPay_program());
			billTrans.setProviderNo(billHeader.getProviderNo());
			billTrans.setProvince(billHeader.getProvince());
			billTrans.setRefNum(billHeader.getRef_num());
			billTrans.setServiceCode(billItem.getService_code());
			billTrans.setServiceCodeInvoiced(billItem.getFee());
			billTrans.setServiceCodeDiscount(BigDecimal.ZERO);
			billTrans.setServiceCodePaid(BigDecimal.ZERO);
			billTrans.setServiceCodeRefund(BigDecimal.ZERO);
			billTrans.setServiceCodeNum(billItem.getSer_num());
			billTrans.setStatus(billHeader.getStatus());
			billTrans.setSliCode(billHeader.getLocation());
			billTrans.setUpdateDatetime(updateTs);
			billTrans.setUpdateProviderNo(billHeader.getCreator());
			billTrans.setVisittype(billHeader.getVisittype());
			billTransDao.persist(billTrans);
		}		
	}


	@SuppressWarnings("unchecked")
	public boolean add3rdBillExt(Map<String,String>mVal, int id, Vector vecObj) {
		BillingClaimHeader1Data claim1Obj = (BillingClaimHeader1Data) vecObj.get(0);
		boolean retval = true;
		String[] temp = { "billTo", "remitTo", "total", "payment", "discount", "provider_no", "gst", "payDate", "payMethod"};
		String demoNo = mVal.get("demographic_no");
		String dateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
                mVal.put("payDate", dateTime);
		String paymentSumParam = null;
		String paymentDateParam = null;
		String paymentTypeParam = null;
		String provider_no=mVal.get("provider_no");
		for (int i = 0; i < temp.length; i++) {
			String val = mVal.get(temp[i]);
			if ("discount".equals(temp[i])) {
				val = mVal.get("total_discount"); // 'refund' stands for write off, here totoal_discount is write off
			}
			if ("payment".equals(temp[i])) {
				val = mVal.get("total_payment");
			}
			BillingONExt billingONExt = new BillingONExt();
			billingONExt.setBillingNo(id);
			billingONExt.setDemographicNo(Integer.parseInt(demoNo));
			billingONExt.setKeyVal(StringEscapeUtils.escapeSql(temp[i]));
			billingONExt.setValue(StringEscapeUtils.escapeSql(val));
			billingONExt.setDateTime(new Date());
			billingONExt.setStatus('1');
			extDao.persist(billingONExt);			
			
			if(i == 3) paymentSumParam = mVal.get("total_payment"); // total_payment
			else if(i == 7) paymentDateParam = mVal.get(temp[i]); // paymentDate
			else if(i == 8) paymentTypeParam = mVal.get(temp[i]); // paymentMethod
			
		}
        
        if(paymentSumParam!=null) {
			BillingONPaymentDao billingONPaymentDao =(BillingONPaymentDao) SpringUtils.getBean("billingONPaymentDao");			
			BillingPaymentTypeDao billingPaymentTypeDao =(BillingPaymentTypeDao) SpringUtils.getBean("billingPaymentTypeDao");
			BillingONCHeader1 ch1 = cheaderDao.find(id);
			Date paymentDate = null;
			try {
	    		paymentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paymentDateParam);
	    	} catch(ParseException ex) {
				_logger.error("add3rdBillExt wrong date format " + paymentDateParam);
				return retval;
	    	}
			
			//allow user to override with the text box added
			String paymentDateOverride = mVal.get("payment_date");
			if(paymentDateOverride != null && paymentDateOverride.length()>0) {
				try {
		    		paymentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paymentDateOverride + " 00:00:00");
		    	} catch(ParseException ex) {
					_logger.error("add3rdBillExt wrong date format " + paymentDateOverride);
					return retval;
		    	}
			}
			
	    	if(paymentTypeParam==null || paymentTypeParam.equals("")) {
	    		paymentTypeParam="1";
	    	}
    		BillingPaymentType type = billingPaymentTypeDao.find(Integer.parseInt(paymentTypeParam));
    		BillingONPayment payment = null;
    		
    		if(paymentSumParam != null) {
		    	payment = new BillingONPayment();
	    		payment.setTotal_payment(BigDecimal.valueOf(Double.parseDouble(paymentSumParam)));
	    		payment.setTotal_discount(BigDecimal.valueOf(Double.parseDouble(mVal.get("total_discount"))));
	    		payment.setTotal_refund(new BigDecimal(0));
				payment.setPaymentDate(paymentDate);
		    	payment.setBillingOnCheader1(ch1);
				payment.setBillingNo(id);
		    	payment.setCreator(claim1Obj.getCreator());
		    	payment.setPaymentTypeId(Integer.parseInt(paymentTypeParam));
		    	
		    	//payment.setBillingPaymentType(type);
		    	billingONPaymentDao.persist(payment);
		    	addItemPaymentRecord((List) vecObj.get(1), id , payment.getId(), Integer.parseInt(paymentTypeParam), paymentDate);
		    	addCreate3rdInvoiceTrans((BillingClaimHeader1Data) vecObj.get(0), (List<BillingItemData>)vecObj.get(1), payment);
	    	}
        }
		return retval;
	}


	public int addOneItemRecord(BillingItemData val) throws ParseException {
		BillingONItem item = new BillingONItem();
		item.setCh1Id(Integer.parseInt(val.ch1_id));
		item.setTranscId(val.transc_id);
		item.setRecId(val.rec_id);
		item.setServiceCode(val.service_code);
		item.setFee(val.fee);
		item.setServiceCount(val.ser_num);
		item.setServiceDate(dateformatter.parse(val.service_date));
		item.setDx(val.dx);
		item.setDx1(val.dx1);
		item.setDx2(val.dx2);
		item.setStatus(val.status);	
		BillingONItem returnItem = itemDao.saveEntity(item); 
		return returnItem.getId(); //return ID
		
	}
		
	public boolean add3rdBillExt(Map<String,String>mVal, int id) {
		boolean retval = true;
		String[] temp = { "billTo", "remitTo", "total", "payment", "refund", "provider_no", "gst", "payDate", "payMethod"};
		String demoNo = mVal.get("demographic_no");
		String dateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
        mVal.put("payDate", dateTime);
        
        BillingONPaymentDao billingONPaymentDao = SpringUtils.getBean(BillingONPaymentDao.class);
        BillingONPayment newPayment = new BillingONPayment();
        BillingONCHeader1 ch1 = cheaderDao.find(id);
        newPayment.setBillingOnCheader1(ch1);
        newPayment.setPaymentDate(UtilDateUtilities.StringToDate(dateTime));
        
		for (int i = 0; i < temp.length; i++) {
				BillingONExt b = new BillingONExt();
				b.setBillingNo(id);
				b.setDemographicNo(Integer.valueOf(demoNo));
				b.setKeyVal(temp[i]);
				b.setValue(mVal.get(temp[i]));
				b.setDateTime(new Date());
				b.setStatus('1');
				b.setPaymentId(0);
				newPayment.getBillingONExtItems().add(b);
		}
		
		billingONPaymentDao.persist(newPayment);
		
		return retval;
	}

	// add disk file
	public int addBillingDiskName(BillingDiskNameData val) {
		BillingONDiskName b = new BillingONDiskName();
		b.setMonthCode(val.monthCode);
		b.setBatchCount(Integer.parseInt(val.batchcount));
		b.setOhipFilename(val.ohipfilename);
		b.setGroupNo(val.groupno);
		b.setCreator(val.creator);
		b.setClaimRecord(val.claimrecord);
		b.setCreateDateTime(new Date());
		b.setStatus(val.status);
		b.setTotal(val.total);
		
		diskNameDao.persist(b);
		
		int retval = b.getId();
		
		if (b.getId() > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				BillingONFilename f = new BillingONFilename();
				f.setDiskId(b.getId());
				f.setHtmlFilename((String)val.htmlfilename.get(i));
				f.setProviderOhipNo((String)val.providerohipno.get(i));
				f.setProviderNo((String)val.providerno.get(i));
				f.setClaimRecord((String)val.vecClaimrecord.get(0));
				f.setStatus((String)val.vecStatus.get(0));
				f.setTotal((String)val.vecTotal.get(0));
				filenameDao.persist(f);
			}

		} else {
			retval = 0;
		}

		return retval;
	}

	public String[] getLatestSoloMonthCodeBatchNum(String providerOhipNo) {
		String[] retval = null;
		
		BillingONDiskName b = diskNameDao.getLatestSoloMonthCodeBatchNum(providerOhipNo);
		if(b != null) {
			retval = new String[2];
			retval[0] = b.getMonthCode();
			retval[1] = "" + b.getBatchCount();
		} else {
			retval=null;
		}
			
		return retval;
	}

	public String[] getLatestGrpMonthCodeBatchNum(String groupNo) {
		String[] retval = null;
		BillingONDiskName b = diskNameDao.findByGroupNo(groupNo);
		if(b != null) {
			retval = new String[2];
			retval[0] = b.getMonthCode();
			retval[1] = "" +b.getBatchCount();
		}else {
			retval = null;
		}

		return retval;
	}

	public String getPrevDiskCreateDate(String diskId) {
		String retval = null;
		Date curDate =null;
		String groupNo = "";
		
		
		BillingONDiskName b= diskNameDao.find(Integer.valueOf(diskId));
		if(b != null) {
			curDate = b.getCreateDateTime();
			groupNo = b.getGroupNo();

			BillingONDiskName x = diskNameDao.getPrevDiskCreateDate(curDate,groupNo);
			if(x != null) {
				Date tmp = x.getCreateDateTime();
				retval = dateformatter.format(tmp);
			}
		}
		return retval;
	}
	

	public String getDiskCreateDate(String diskId) {
		String retval = null;
		
		BillingONDiskName b = diskNameDao.find(Integer.parseInt(diskId));
		if(b != null) {
			Date tmp = b.getCreateDateTime();
			retval = dateformatter.format(tmp);
		}
		return retval;
	}
	

	public List getMRIList(String sDate, String eDate, String status) {
		List retval = new Vector();
		BillingDiskNameData obj = null;
		
		try {
			List<BillingONDiskName> results = diskNameDao.findByCreateDateRangeAndStatus(dateformatter.parse(sDate),dateformatter.parse(eDate),status);
	
			for(BillingONDiskName b : results) {
				obj = new BillingDiskNameData();
				obj.setId("" + b.getId());
				obj.setMonthCode(b.getMonthCode());
				obj.setBatchcount("" + b.getBatchCount());
				obj.setOhipfilename(b.getOhipFilename());
				obj.setGroupno(b.getGroupNo());
				obj.setClaimrecord(b.getClaimRecord());
				obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
				obj.setUpdatedatetime(tsFormatter.format(b.getTimestamp()));
				obj.setStatus(b.getStatus());
				obj.setTotal(b.getTotal());
				
				List<BillingONFilename> ff = filenameDao.findByDiskIdAndStatus(b.getId(),status);
				Vector vecHtmlfilename = new Vector();
				Vector vecProviderohipno = new Vector();
				Vector vecProviderno = new Vector();
				Vector vecClaimrecord = new Vector();
				Vector vecStatus = new Vector();
				Vector vecTotal = new Vector();
				Vector vecFilenameId = new Vector();
					
				for(BillingONFilename f:ff) {	
					vecFilenameId.add("" + f.getId());
					vecHtmlfilename.add(f.getHtmlFilename());
					vecProviderohipno.add(f.getProviderOhipNo());
					vecProviderno.add(f.getProviderNo());
					vecClaimrecord.add(f.getClaimRecord());
					vecStatus.add(f.getStatus());
					vecTotal.add(f.getTotal());
				}
				obj.setVecFilenameId(vecFilenameId);
				obj.setHtmlfilename(vecHtmlfilename);
				obj.setProviderohipno(vecProviderohipno);
				obj.setProviderno(vecProviderno);
				obj.setVecClaimrecord(vecClaimrecord);
				obj.setVecStatus(vecStatus);
				obj.setVecTotal(vecTotal);
				retval.add(obj);	
			}
		} catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
			retval=null;
		}
		return retval;
	}

	public String getOhipfilename(int diskId) {
		BillingONDiskName b = diskNameDao.find(diskId);
		if(b != null) {
			return b.getOhipFilename();
		}
		return "";
	}

	public String getHtmlfilename(int diskId, String providerNo) {
		String obj="";
		List<BillingONFilename> results = filenameDao.findByDiskIdAndProvider(diskId, providerNo);
		for(BillingONFilename result:results) {
			obj = result.getHtmlFilename();
		}
		return obj;
	}

	public BillingDiskNameData getDisknameObj(String diskId) {
		BillingDiskNameData obj = new BillingDiskNameData();
		
		BillingONDiskName b = diskNameDao.find(Integer.valueOf(diskId));
		if(b != null) {
			obj.setId("" + b.getId());
			obj.setMonthCode(b.getMonthCode());
			obj.setBatchcount("" + b.getBatchCount());
			obj.setOhipfilename(b.getOhipFilename());
			obj.setGroupno(b.getGroupNo());
			obj.setClaimrecord(b.getCreator());
			obj.setClaimrecord(b.getClaimRecord());
			obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
			obj.setStatus(b.getStatus());
			obj.setTotal(b.getTotal());
			obj.setUpdatedatetime(tsFormatter.format(b.getTimestamp()));
			
			List<BillingONFilename> ff = filenameDao.findCurrentByDiskId(b.getId());
			Vector vecHtmlfilename = new Vector();
			Vector vecProviderohipno = new Vector();
			Vector vecProviderno = new Vector();
			Vector vecClaimrecord = new Vector();
			Vector vecStatus = new Vector();
			Vector vecTotal = new Vector();
			Vector vecFilenameId = new Vector();
			for(BillingONFilename f:ff) {
				vecFilenameId.add("" + f.getId());
				vecHtmlfilename.add(f.getHtmlFilename());
				vecProviderohipno.add(f.getProviderOhipNo());
				vecProviderno.add(f.getProviderNo());
				vecClaimrecord.add(f.getClaimRecord());
				vecStatus.add(f.getStatus());
				vecTotal.add(f.getTotal());
			}

			obj.setVecFilenameId(vecFilenameId);
			obj.setHtmlfilename(vecHtmlfilename);
			obj.setProviderohipno(vecProviderohipno);
			obj.setProviderno(vecProviderno);
			obj.setVecClaimrecord(vecClaimrecord);
			obj.setVecStatus(vecStatus);
			obj.setVecTotal(vecTotal);
		}

		return obj;
	}

	public int addRepoDiskName(BillingDiskNameData val) {
		int retval = 0;
		BillingONRepo b = new BillingONRepo();
		b.sethId(Integer.parseInt(val.id));
		b.setCategory("billing_on_diskname");
		b.setContent(val.monthCode + "|" + val.batchcount + "|" + val.ohipfilename + "|" + val.groupno + "|" + val.creator
				+ "|" + val.claimrecord + "|" + val.createdatetime + "|" + val.status + "|" + val.total + "|"
				+ val.updatedatetime);
		b.setCreateDateTime(new Date());
		
		repoDao.persist(b);
		retval = b.getId();
		
		if (b.getId() > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				BillingONRepo r = new BillingONRepo();
				r.sethId(Integer.valueOf((String)val.vecFilenameId.get(i)));
				r.setCategory("billing_on_filename");
				r.setContent(val.id + "|" + val.htmlfilename.get(i) + "|"
						+ val.providerohipno.get(i) + "|" + val.providerno.get(i) + "|" + val.vecClaimrecord.get(0)
						+ "|" + val.vecStatus.get(0) + "|" + val.vecTotal.get(0) + "|" + val.updatedatetime);
				
				r.setCreateDateTime(new Date());
				
				repoDao.persist(r);
			}
		} else {
			retval = 0;
		}
		return retval;
	}

	public boolean updateDiskName(BillingDiskNameData val) {
		BillingONDiskName b = diskNameDao.find(Integer.parseInt(val.getId()));
		if(b != null) {
			b.setCreator(val.creator);
			diskNameDao.merge(b);
		}
		return true;
	}

	public BillingBatchHeaderData getBatchHeaderObj(BillingProviderData providerData, String disk_id) {
		BillingBatchHeaderData obj = new BillingBatchHeaderData();
		
		List<BillingONHeader> bs = dao.findByDiskIdAndProviderRegNum(Integer.parseInt(disk_id),providerData.getOhipNo());
		
		for(BillingONHeader b:bs) {
			obj.setId("" +b.getId());
			obj.setDisk_id(disk_id);
			obj.setTransc_id(b.getTransactionId());
			obj.setRec_id(b.getRecordId());
			obj.setSpec_id(b.getSpecId());
			obj.setMoh_office(b.getMohOffice());

			obj.setBatch_id(b.getBatchId());
			obj.setOperator(b.getOperator());
			obj.setGroup_num(b.getGroupNum());
			obj.setProvider_reg_num(b.getProviderRegNum());
			obj.setSpecialty(b.getSpecialty());
			obj.setH_count(b.gethCount());
			obj.setR_count(b.getrCount());
			obj.setT_count(b.gettCount());
			obj.setBatch_date(dateformatter.format(b.getBatchDate()));

			obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
			obj.setUpdatedatetime(tsFormatter.format(b.getUpdateDateTime()));
			obj.setCreator(b.getCreator());
			obj.setAction(b.getAction());
			obj.setComment(b.getComment());
		}

		return obj;
	}

	public int addRepoBatchHeader(BillingBatchHeaderData val) {
		BillingONRepo b = new BillingONRepo();
		b.sethId(Integer.parseInt(val.id));
		b.setCategory("billing_on_header");
		b.setContent(val.disk_id + "|" + val.transc_id + "|" + val.rec_id + "|" + val.spec_id + "|" + val.moh_office + "|"
				+ val.batch_id + "|" + val.operator + "|" + val.group_num + "|" + val.provider_reg_num + "|"
				+ val.specialty + "|" + val.h_count + "|" + val.r_count + "|" + val.t_count + "|" + val.batch_date
				+ "|" + val.createdatetime + "|" + val.updatedatetime + "|" + val.creator + "|" + val.action + "|"
				+ val.comment);
		b.setCreateDateTime(new Date());
		repoDao.persist(b);
		
		return b.getId();
	}

	// TODO more update data
	public boolean updateBatchHeaderRecord(BillingBatchHeaderData val) {
		BillingONHeader b = dao.find(Integer.parseInt(val.getId()));
		if(b != null) {
			b.setMohOffice(val.moh_office);
			b.setBatchId(val.batch_id);
			b.setSpecialty(val.specialty);
			b.setCreator(val.creator);
			b.setUpdateDateTime(new Date());
			b.setAction(val.action);
			b.setComment(val.comment);
			dao.merge(b);
		}
		
		return true;
	}
        
}
