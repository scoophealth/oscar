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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.billing.CA.ON.dao.BillingPercLimitDao;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingONPaymentDao;
import org.oscarehr.common.dao.BillingOnItemPaymentDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.ClinicLocationDao;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.common.model.BillingOnItemPayment;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class JdbcBillingReviewImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingReviewImpl.class);

	private ClinicLocationDao clinicLocationDao = (ClinicLocationDao) SpringUtils.getBean("clinicLocationDao");
	private BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private BillingONExtDao extDao = SpringUtils.getBean(BillingONExtDao.class);
	private BillingONPaymentDao payDao = SpringUtils.getBean(BillingONPaymentDao.class);
	private BillingServiceDao serviceDao = SpringUtils.getBean(BillingServiceDao.class);
	private BillingOnItemPaymentDao billOnItemPaymentDao = (BillingOnItemPaymentDao)SpringUtils.getBean(BillingOnItemPaymentDao.class);
	
	private ProgramProviderDAO programProviderDAO = SpringUtils.getBean(ProgramProviderDAO.class);
	
	public String getCodeFee(String val, String billReferalDate) {
		String retval = null;
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);

		try {
			for (BillingService bs : dao.findByServiceCodeAndLatestDate(val, ConversionUtils.fromDateString(billReferalDate))) {
				retval = bs.getValue();

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date serviceDate = df.parse(billReferalDate);
				if (bs.getTerminationDate().before(serviceDate)) {
					retval = "defunct";
				}
			}

		} catch (Exception e) {
			_logger.error("error", e);
		}

		return retval;
	}

	public String getPercFee(String val, String billReferalDate) {
		String retval = null;
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		try {
			for (BillingService bs : dao.findByServiceCodeAndLatestDate(val, ConversionUtils.fromDateString(billReferalDate))) {
				retval = bs.getPercentage();
			}
		} catch (Exception e) {
			_logger.error("error", e);
		}
		return retval;
	}

	public String[] getPercMinMaxFee(String val, String billReferalDate) {
		String[] retval = { "", "" };

		BillingPercLimitDao dao = SpringUtils.getBean(BillingPercLimitDao.class);
		try {
			for (BillingPercLimit b : dao.findByServiceCodeAndLatestDate(val, ConversionUtils.fromDateString(billReferalDate))) {
				retval[0] = b.getMin();
				retval[1] = b.getMax();
			}
		} catch (Exception e) {
			_logger.error("error", e);
		}
		return retval;
	}

	// invoice report
	public List getBill(String billType, String statusType, String providerNo,
			String startDate, String endDate, String demoNo) {
		
		return getBill(billType, statusType, providerNo, startDate, endDate, demoNo, "", "", "");	
		
	}

	// invoice report
	public List getBill(String billType, String statusType, String providerNo,
			String startDate, String endDate, String demoNo,
			String serviceCodes, String dx, String visitType) {
		
		List<BillingClaimHeader1Data> retval = new ArrayList<BillingClaimHeader1Data>();
		BillingClaimHeader1Data ch1Obj = null ;
		
		// For filtering invoice report based on dx code
		String temp = demoNo + " " + providerNo + " " + statusType + " "
				+ startDate + " " + endDate + " " + billType + " " + dx + " "
				+ visitType + " " + serviceCodes;
		temp = temp.trim().startsWith("and") ? temp.trim().substring(3) : temp;
		
		/*String sql = "SELECT ch1.id,ch1.pay_program,ch1.demographic_no,ch1.demographic_name,ch1.billing_date,ch1.billing_time,"
		+ "ch1.status,ch1.provider_no,ch1.provider_ohip_no,ch1.apptProvider_no,ch1.timestamp1,ch1.total,ch1.paid,ch1.clinic,"
		+ "bi.fee, bi.service_code, bi.ser_num, bi.dx, bi.id as billing_on_item_id "
		+ "FROM billing_on_item bi LEFT JOIN billing_on_cheader1 ch1 ON ch1.id=bi.ch1_id "
		+ "WHERE "
		+ temp				
		+ " ORDER BY ch1.billing_date, ch1.billing_time";
		 */		
		List<String[]> bills = dao.findBillingData(temp);
		if(bills!=null) {
			for(String[] b : bills) {
				String prevId = null;
				String prevPaid = null;
				
				boolean bSameBillCh1 = false;
				ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId(b[0]);
				ch1Obj.setPay_program(b[1]);
				ch1Obj.setDemographic_no(b[2]);
				ch1Obj.setDemographic_name(b[3]);
				ch1Obj.setBilling_date(b[4]);
				ch1Obj.setBilling_time(b[5]);
				ch1Obj.setStatus(b[6]);
				ch1Obj.setProvider_no(b[7]);
				ch1Obj.setProvider_ohip_no(b[8]);
				ch1Obj.setUpdate_datetime(b[9]);
				ch1Obj.setTotal(b[10]);
				//ch1Obj.setPaid(b[11]);
				ch1Obj.setClinic(b[12]);
				//ch1Obj.setTotal(b[13]);//fee is not total?
				ch1Obj.setSer_num(b[15]); //14 is service code
				ch1Obj.setBilling_on_item_id(b[17]); //16 is dx
				
				List<BillingONExt> exts = extDao.findByBillingNoAndKey(Integer.parseInt(b[0]), "payDate");
				for(BillingONExt e : exts ) {
					if(e.getStatus()=='1') {
						ch1Obj.setSettle_date(e.getValue());
					}
				}
				
				if("PAT".equals(ch1Obj.getPay_program())){
					BigDecimal amountPaid = billOnItemPaymentDao.getAmountPaidByItemId(Integer.parseInt(b[17]));
					ch1Obj.setPaid(amountPaid.toString());					
				} else {
					if( prevId==null && prevPaid==null) {
						ch1Obj.setPaid(b[11]);
					} else if(prevId!=null && prevPaid!=null && !ch1Obj.getId().equals(prevId) ) {
						ch1Obj.setPaid(b[11]);
					} else {
						ch1Obj.setPaid("0.00");			
					}
				}
				retval.add(ch1Obj);
				
				prevId = ch1Obj.getId();
				prevPaid = b[11];
			}

		}
				
		return retval;
	}


	public List<BillingClaimHeader1Data> getBill(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, String visitLocation, String paymentStartDate, String paymentEndDate) {
		return getBillWithSorting(billType,statusType,providerNo,startDate,endDate,demoNo,visitLocation,null,null, paymentStartDate,paymentEndDate);
	}
	
	// invoice report
	public List<BillingClaimHeader1Data> getBillWithSorting(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, String visitLocation, String sortName, String sortOrder,  String paymentStartDate, String paymentEndDate) {
		List<BillingClaimHeader1Data> retval = new ArrayList<BillingClaimHeader1Data>();		
		try {
			for (BillingONCHeader1 h : dao.findByMagic(Arrays.asList(billType), statusType, providerNo, ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), ConversionUtils.fromIntString(demoNo),visitLocation, ConversionUtils.fromDateString(paymentStartDate), ConversionUtils.fromDateString(paymentEndDate))) {
				String prevId = null;
				String prevPaid = null;
				
				BillingClaimHeader1Data ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + h.getId());
				ch1Obj.setDemographic_no("" + h.getDemographicNo());
				ch1Obj.setDemographic_name(h.getDemographicName());
				ch1Obj.setBilling_date(ConversionUtils.toDateString(h.getBillingDate()));
				ch1Obj.setBilling_time(ConversionUtils.toDateString(h.getBillingTime()));
				ch1Obj.setStatus(h.getStatus());
				ch1Obj.setProviderNo(h.getProviderNo());
				ch1Obj.setProvider_ohip_no(h.getProviderOhipNo());
				ch1Obj.setApptProvider_no(h.getApptProviderNo());
				ch1Obj.setUpdate_datetime(ConversionUtils.toDateString(h.getTimestamp()));
				ch1Obj.setTotal(String.valueOf(h.getTotal().doubleValue()));
				ch1Obj.setPay_program(h.getPayProgram());
				ch1Obj.setPaid(String.valueOf(h.getPaid().doubleValue()));
				ch1Obj.setClinic(h.getClinic());
				for (BillingONExt b : extDao.findByBillingNoAndKey(h.getId(), "payDate")) {
					ch1Obj.setSettle_date(b.getValue());
				}
				
				ch1Obj.setFacilty_num(clinicLocationDao.searchVisitLocation(h.getFaciltyNum()));

				retval.add(ch1Obj);
			}
		} catch (Exception e) {
			_logger.error("error", e);
		}
		
		applySort(retval,sortName,sortOrder);
		return retval;
	}
	
	private void applySort(List<BillingClaimHeader1Data> retval, String sortName, String sortOrder) {
		if(sortOrder == null) {
			sortOrder = "asc";
		}
		
		if(sortName != null && "ServiceDate".equals(sortName)) {
			Collections.sort(retval, SERVICE_DATE_COMPARATOR);
		}
		if(sortName != null && "DemographicNo".equals(sortName)) {
			Collections.sort(retval, DEMOGRAPHIC_NO_COMPARATOR);
		}
		if(sortName != null && "VisitLocation".equals(sortName)) {
			Collections.sort(retval, VISIT_LOCATION_COMPARATOR);
		}
		if(sortOrder.equals("desc")) {
			Collections.reverse(retval);
		}
	}
	
	public static final Comparator<BillingClaimHeader1Data> SERVICE_DATE_COMPARATOR =new Comparator<BillingClaimHeader1Data>() {
		public int compare(BillingClaimHeader1Data arg0, BillingClaimHeader1Data arg1) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date0=null,date1=null;
			try {
				date0 = formatter.parse(arg0.getBilling_date());
				date1 = formatter.parse(arg1.getBilling_date());
			}catch(ParseException e) {
				return 0;
			}
			return(date0.compareTo(date1));
			
		}
	};
	
	public static final Comparator<BillingClaimHeader1Data> DEMOGRAPHIC_NO_COMPARATOR =new Comparator<BillingClaimHeader1Data>() {
		public int compare(BillingClaimHeader1Data arg0, BillingClaimHeader1Data arg1) {
			Integer d0,d1;
			try {
				d0 = Integer.parseInt(arg0.getDemographic_no());
				d1 = Integer.parseInt(arg1.getDemographic_no());
			}catch(Exception e) {
				return 0;
			}
			return(d0.compareTo(d1));
			
		}
	};
	
	public static final Comparator<BillingClaimHeader1Data> VISIT_LOCATION_COMPARATOR =new Comparator<BillingClaimHeader1Data>() {
		public int compare(BillingClaimHeader1Data arg0, BillingClaimHeader1Data arg1) {
			return arg0.getFacilty_num().compareTo(arg1.getFacilty_num());
		}
	};
	

	//invoice report	
	public List<BillingClaimHeader1Data> getBill(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation, String paymentStartDate, String paymentEndDate) {	
		return getBillWithSorting(billType,statusType,providerNo,startDate,endDate,demoNo,serviceCodes,dx,visitType, visitLocation,null,null,paymentStartDate,paymentEndDate);	
	}
	
	//invoice report
	public List<BillingClaimHeader1Data> getBillWithSorting(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation, String sortName, String sortOrder, String paymentStartDate, String paymentEndDate) {
		List<BillingClaimHeader1Data> retval = new ArrayList<BillingClaimHeader1Data>();

		try {
			String prevId = null;
			String prevPaid = null;

			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			BillingONPaymentDao billingOnPaymentDao = SpringUtils.getBean(BillingONPaymentDao.class);
			BillingONExtDao billingOnExtDao = SpringUtils.getBean(BillingONExtDao.class);
			BillingPaymentTypeDao billingPaymentTypeDao = SpringUtils.getBean(BillingPaymentTypeDao.class);
			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			
			Integer CASH_PAYMENT_ID = billingPaymentTypeDao.findIdByName("CASH");
			Integer DEBIT_PAYMENT_ID = billingPaymentTypeDao.findIdByName("DEBIT");
			
			for (Object[] o : dao.findByMagic2(Arrays.asList(billType), statusType, providerNo, ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), ConversionUtils.fromIntString(demoNo), serviceCodes, dx, visitType, visitLocation, ConversionUtils.fromDateString(paymentStartDate),  ConversionUtils.fromDateString(paymentEndDate))) {
				BillingONCHeader1 ch1 = (BillingONCHeader1) o[0];
				BillingONItem bi = (BillingONItem) o[1];

				BillingClaimHeader1Data ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + ch1.getId());
				ch1Obj.setDemographic_no("" + ch1.getDemographicNo());
				ch1Obj.setDemographic_name(ch1.getDemographicName());
				ch1Obj.setSex(ch1.getSex());
				ch1Obj.setBilling_date(ConversionUtils.toDateString(ch1.getBillingDate()));
				ch1Obj.setBilling_time(ConversionUtils.toTimeString(ch1.getBillingTime()));
				ch1Obj.setStatus(ch1.getStatus());
				ch1Obj.setProviderNo(ch1.getProviderNo());
				ch1Obj.setProvider_ohip_no(ch1.getProviderOhipNo());
				ch1Obj.setApptProvider_no(ch1.getApptProviderNo());
				ch1Obj.setUpdate_datetime(ConversionUtils.toTimestampString(ch1.getTimestamp()));
				ch1Obj.setClinic(ch1.getClinic());
				ch1Obj.setPay_program(ch1.getPayProgram());
				
				if("PAT".equals(ch1.getPayProgram()) ){ 
					BigDecimal amountPaid = billOnItemPaymentDao.getAmountPaidByItemId(bi.getId());
					ch1Obj.setPaid(amountPaid.toString());
					ch1Obj.setBilling_on_item_id(bi.getId().toString());
				} else {
					if( prevId==null && prevPaid==null) {
						ch1Obj.setPaid(ch1.getPaid().toString());
					} else if(prevId!=null && prevPaid!=null && !ch1Obj.getId().equals(prevId) ) {
						ch1Obj.setPaid(ch1.getPaid().toString());
					} else
						ch1Obj.setPaid("0.00");				
				}
				ch1Obj.setTotal(bi.getFee());
				ch1Obj.setRec_id(bi.getDx());
				ch1Obj.setTransc_id(bi.getServiceCode());

				retval.add(ch1Obj);
				prevId = ch1Obj.getId();
				prevPaid = ch1.getPaid().toString();
				
				ch1Obj.setFacilty_num(clinicLocationDao.searchVisitLocation(ch1.getFaciltyNum()));
				
				double cashTotal = 0.00;
				double debitTotal = 0.00;

				ch1Obj.setNumItems(Integer.parseInt(bi.getServiceCount()));
				
				for(Integer paymentId:billingOnPaymentDao.find3rdPartyPayments(Integer.parseInt(ch1Obj.getId()))) {
					//because private billing changed, we'll check via paymentTypeId in billing_on_payment
					BillingONPayment paymentObj = billingOnPaymentDao.find(paymentId);
					BillingOnItemPayment boip = billOnItemPaymentDao.findByPaymentIdAndItemId(paymentId, bi.getId());
					
					if(boip == null) {
						MiscUtils.getLogger().warn("boip is null - " + paymentId + "," + bi.getId());
						//probably means that no payment was applied to this item.
						continue;
					}
					
					if(paymentObj.getPaymentTypeId() == CASH_PAYMENT_ID) {
						cashTotal += boip.getPaid().intValue();
					} else if(paymentObj.getPaymentTypeId() == DEBIT_PAYMENT_ID) {
						debitTotal += boip.getPaid().intValue();
					}
					
				}
				
				
				ch1Obj.setCashTotal(cashTotal);
				ch1Obj.setDebitTotal(debitTotal);
				
				Provider provider = providerDao.getProvider(ch1Obj.getProvider_no());
				if(provider!=null) {
					ch1Obj.setProviderName(provider.getFormattedName());
				}

			}
		} catch (Exception e) {
			_logger.error("error", e);
		}

		applySort(retval,sortName,sortOrder);
		
		return retval;
	}

	public List<BillingONCHeader1> filterOutOtherPrograms(LoggedInInfo loggedInInfo, List<BillingONCHeader1> hs) {
		List<BillingONCHeader1> filtered = new ArrayList<BillingONCHeader1>();
		
		List<ProgramProvider> ppList = programProviderDAO.getProgramDomain(loggedInInfo.getLoggedInProviderNo());
		List<Integer> programIdsUserCanAccess = new ArrayList<Integer>();
		for(ProgramProvider pp:ppList) {
			programIdsUserCanAccess.add(pp.getProgramId().intValue());
		}
		
		for(BillingONCHeader1 h:hs) {
			Integer programNo = h.getProgramNo();
			if(programNo != null) {
				if(programIdsUserCanAccess.contains(programNo)) {
					filtered.add(h);
				} else {
					continue;
				}
			} else {
				filtered.add(h);
			}
			
		}
		return filtered;
	}
	
	// billing page
	public List<Object> getBillingHist(LoggedInInfo loggedInInfo, String demoNo, int iPageSize, int iOffSet, DateRange dateRange)  {
		List<Object> retval = new ArrayList<Object>();
		int iRow = 0;

		BillingClaimHeader1Data ch1Obj = null;
		ProviderDao providerdao = (ProviderDao)SpringUtils.getBean(ProviderDao.class);

		BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);

		List<BillingONCHeader1> hs = null;
		if (dateRange == null) {
			hs = dao.findByDemoNo(ConversionUtils.fromIntString(demoNo), iOffSet, iPageSize);
		} else {
			hs = dao.findByDemoNoAndDates(ConversionUtils.fromIntString(demoNo), dateRange, iOffSet, iPageSize);
		}

		//filter out the ones with programNos not in my domain
		hs = filterOutOtherPrograms(loggedInInfo, hs);
		
		try {
			for (BillingONCHeader1 h : hs) {
				iRow++;
				if (iRow > iPageSize) {
					break;
				}
				ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + h.getId());
				ch1Obj.setBilling_date(ConversionUtils.toDateString(h.getBillingDate()));
				ch1Obj.setBilling_time(ConversionUtils.toDateString(h.getBillingTime()));
				ch1Obj.setStatus(h.getStatus());
				ch1Obj.setProviderNo(h.getProviderNo());
				ch1Obj.setApptProvider_no(h.getApptProviderNo());
				ch1Obj.setUpdate_datetime(ConversionUtils.toDateString(h.getTimestamp()));

				ch1Obj.setClinic(h.getClinic());
				ch1Obj.setAppointment_no("" + h.getAppointmentNo());
				ch1Obj.setPay_program(h.getPayProgram());
				ch1Obj.setVisittype(h.getVisitType());
				ch1Obj.setAdmission_date(ConversionUtils.toDateString(h.getAdmissionDate()));
				ch1Obj.setFacilty_num(h.getFaciltyNum());
				ch1Obj.setTotal(h.getTotal().toString());
				
				Provider provider = providerdao.getProvider(h.getProviderNo());
				ch1Obj.setLast_name(provider.getLastName());
				ch1Obj.setFirst_name(provider.getFirstName());
				
	
				retval.add(ch1Obj);

				String dx = "";
				Set<String> serviceCodeSet = new HashSet<String>();
			
				String strServiceDate = "";
				BigDecimal paid = new BigDecimal("0.00");
				BigDecimal refund = new BigDecimal("0.00");
				BigDecimal discount = new BigDecimal("0.00");


				for (BillingONItem i : itemDao.findByCh1IdAndStatusNotEqual(h.getId(), "D")) {
					String strService = i.getServiceCode() + " x " + i.getServiceCount() + ", ";
					dx = i.getDx();
					strServiceDate = ConversionUtils.toDateString(i.getServiceDate());
					
					serviceCodeSet.add(strService);
				}
								
				BillingItemData itObj = new BillingItemData();
				StringBuffer codeBuf = new StringBuffer();
				for (String codeStr : serviceCodeSet) {
					codeBuf.append(codeStr + ",");
				}
				if (codeBuf.length() > 0) {
					codeBuf.deleteCharAt(codeBuf.length() - 1);
				}
				itObj.setService_code(codeBuf.toString());
				itObj.setDx(dx);
				itObj.setService_date(strServiceDate);
				
				List<BillingONPayment> payment = payDao.find3rdPartyPaymentsByBillingNo(h.getId());
				itObj.setPaid(payDao.getTotalSumByBillingNoWeb(h.getId().toString()));
				itObj.setRefund(payDao.getPaymentsRefundByBillingNoWeb(h.getId().toString()));
				BigDecimal discount_total = payDao.getPaymentsDiscountByBillingNo(h.getId());
				if(discount_total == null) {
					discount_total = new BigDecimal(0);
				}
				NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);		        
				itObj.setDiscount(currency.format(discount_total));
				
				retval.add(itObj);
			}
		} catch (Exception e) {
			_logger.error("error", e);
		}

		return retval;
	}

	public List<LabelValueBean> listBillingForms() {
		List<LabelValueBean> res = new ArrayList<LabelValueBean>();

		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		try {
			for (Object[] o : dao.findServiceTypes()) {
				String servicetype = String.valueOf(o[0]);
				String servicetypeName = String.valueOf(o[1]);
				res.add(new LabelValueBean(servicetypeName, servicetype));
			}
		} catch (Exception ex) {
			_logger.error("Error getting billing forms list", ex);
		}
		return res;
	}

	public List<String> mergeServiceCodes(String serviceCodes, String billingForm) {
		
		List<String> serviceCodeList = null;		
		if( (serviceCodes != null && serviceCodes.length() > 0) ||  (billingForm != null && billingForm.length() > 0)){
			serviceCodeList = new ArrayList<String>();
		}
		
		if (serviceCodes != null && serviceCodes.length() > 0) {
			String[] serviceArray = serviceCodes.split(",");
			for (int i = 0; i < serviceArray.length; i++) {
				serviceCodeList.add(serviceArray[i].trim());
			}
		}
		
		if (billingForm != null && billingForm.length() > 0) {
			CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
			for(Object code : dao.findServiceCodesByType(billingForm)) {
					serviceCodeList.add(code.toString());
			}		
		}
		
		return serviceCodeList;
	}

	// billing edit page
	public List<Object> getBillingByApptNo(String apptNo)  {
		List<Object> retval = new ArrayList<Object>();
		
		BillingClaimHeader1Data ch1Obj = null;

		BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1.class);
		BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);
		
		try {
			for(BillingONCHeader1 h : dao.findByAppointmentNo(ConversionUtils.fromIntString(apptNo))) {
				ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + h.getId());
				ch1Obj.setBilling_date(ConversionUtils.toDateString(h.getBillingDate()));
				ch1Obj.setBilling_time(ConversionUtils.toTimeString(h.getBillingTime()));
				ch1Obj.setStatus(h.getStatus());
				ch1Obj.setProviderNo(h.getProviderNo());
				ch1Obj.setAppointment_no("" + h.getAppointmentNo());
				ch1Obj.setApptProvider_no(h.getApptProviderNo());
				ch1Obj.setAsstProvider_no(h.getAsstProviderNo());
				ch1Obj.setMan_review(h.getManReview());
				ch1Obj.setUpdate_datetime(ConversionUtils.toTimestampString(h.getTimestamp()));
				ch1Obj.setClinic(h.getClinic());
				ch1Obj.setPay_program(h.getPayProgram());
				ch1Obj.setVisittype(h.getVisitType());
				ch1Obj.setAdmission_date(ConversionUtils.toDateString(h.getAdmissionDate()));
				ch1Obj.setFacilty_num(h.getFaciltyNum());
				ch1Obj.setHin(h.getHin());
				ch1Obj.setVer(h.getVer());
				ch1Obj.setProvince(h.getProvince());
				ch1Obj.setDob(h.getDob());
				ch1Obj.setDemographic_name(h.getDemographicName());
				ch1Obj.setDemographic_no("" + h.getDemographicNo());
				ch1Obj.setTotal(String.valueOf(h.getTotal().doubleValue()));
				retval.add(ch1Obj);
				
				String dx = null;
				String dx1 = null;
				String dx2 = null;
				String strService = null;
				String strServiceDate = null;

				for(BillingONItem i : itemDao.findByCh1Id(h.getId())) {
					strService += i.getServiceCode() + " x " + i.getServiceCount() + ", ";
					dx = i.getDx();
					strServiceDate = ConversionUtils.toDateString(i.getServiceDate());
					dx1 = i.getDx1();
					dx2 = i.getDx2();
				}
				
				BillingItemData itObj = new BillingItemData();
				itObj.setService_code(strService);
				itObj.setDx(dx);
				itObj.setDx1(dx1);
				itObj.setDx2(dx2);
				itObj.setService_date(strServiceDate);
				retval.add(itObj);

			}
		} catch (Exception e) {
			_logger.error("error", e);
		}

		return retval;
	}
	
	
	public String getCodeDescription(String val, String billReferalDate){
		return serviceDao.getCodeDescription(val, billReferalDate);
		
	}

}
