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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.billing.CA.ON.dao.BillingPercLimitDao;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.ClinicLocationDao;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class JdbcBillingReviewImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingReviewImpl.class);

	private ClinicLocationDao clinicLocationDao = (ClinicLocationDao) SpringUtils.getBean("clinicLocationDao");
	
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

	public List<BillingClaimHeader1Data> getBill(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, String visitLocation) {
		return getBillWithSorting(billType,statusType,providerNo,startDate,endDate,demoNo,visitLocation,null,null);
	}
	
	// invoice report
	public List<BillingClaimHeader1Data> getBillWithSorting(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, String visitLocation, String sortName, String sortOrder) {
		List<BillingClaimHeader1Data> retval = new ArrayList<BillingClaimHeader1Data>();
		BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONExtDao extDao = SpringUtils.getBean(BillingONExtDao.class);
		try {
			for (BillingONCHeader1 h : dao.findByMagic(Arrays.asList(billType), statusType, providerNo, ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), ConversionUtils.fromIntString(demoNo),visitLocation)) {
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
				ch1Obj.setTotal(h.getTotal());
				ch1Obj.setPay_program(h.getPayProgram());
				ch1Obj.setPaid(h.getPaid());
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
				d1 = Integer.parseInt(arg0.getDemographic_no());
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
	public List<BillingClaimHeader1Data> getBill(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation) {	
		return getBillWithSorting(billType,statusType,providerNo,startDate,endDate,demoNo,serviceCodes,dx,visitType,visitLocation,null,null);	
	}
	
	//invoice report
	public List<BillingClaimHeader1Data> getBillWithSorting(String[] billType, String statusType, String providerNo, String startDate, String endDate, String demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation, String sortName, String sortOrder) {
		List<BillingClaimHeader1Data> retval = new ArrayList<BillingClaimHeader1Data>();

		try {
			String prevId = null;
			String prevPaid = null;

			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			for (Object[] o : dao.findByMagic2(Arrays.asList(billType), statusType, providerNo, ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), ConversionUtils.fromIntString(demoNo), serviceCodes, dx, visitType, visitLocation)) {
				BillingONCHeader1 ch1 = (BillingONCHeader1) o[0];
				BillingONItem bi = (BillingONItem) o[1];

				BillingClaimHeader1Data ch1Obj = new BillingClaimHeader1Data();
				ch1Obj.setId("" + ch1.getId());
				ch1Obj.setDemographic_no("" + ch1.getDemographicNo());
				ch1Obj.setDemographic_name(ch1.getDemographicName());
				ch1Obj.setBilling_date(ConversionUtils.toDateString(ch1.getBillingDate()));
				ch1Obj.setBilling_time(ConversionUtils.toTimeString(ch1.getBillingTime()));
				ch1Obj.setStatus(ch1.getStatus());
				ch1Obj.setProviderNo(ch1.getProviderNo());
				ch1Obj.setProvider_ohip_no(ch1.getProviderOhipNo());
				ch1Obj.setApptProvider_no(ch1.getApptProviderNo());
				ch1Obj.setUpdate_datetime(ConversionUtils.toTimestampString(ch1.getTimestamp()));
				ch1Obj.setClinic(ch1.getClinic());
				ch1Obj.setPay_program(ch1.getPayProgram());
				if (!(ch1Obj.getId().equals(prevId) && ch1.getPaid().equals(prevPaid))) {
					ch1Obj.setPaid(ch1.getPaid());
				} else {
					ch1Obj.setPaid("0.00");
				}
				ch1Obj.setTotal(bi.getFee());
				ch1Obj.setRec_id(bi.getDx());
				ch1Obj.setTransc_id(bi.getServiceCode());

				retval.add(ch1Obj);
				prevId = ch1Obj.getId();
				prevPaid = ch1.getPaid();
				
				ch1Obj.setFacilty_num(clinicLocationDao.searchVisitLocation(ch1.getFaciltyNum()));

			}
		} catch (Exception e) {
			_logger.error("error", e);
		}

		applySort(retval,sortName,sortOrder);
		
		return retval;
	}

	// billing page
	public List<Object> getBillingHist(String demoNo, int iPageSize, int iOffSet, DateRange dateRange)  {
		List<Object> retval = new ArrayList<Object>();
		int iRow = 0;

		BillingClaimHeader1Data ch1Obj = null;

		BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);

		List<BillingONCHeader1> hs = null;
		if (dateRange == null) {
			hs = dao.findByDemoNo(ConversionUtils.fromIntString(demoNo), iOffSet, iPageSize);
		} else {
			hs = dao.findByDemoNoAndDates(ConversionUtils.fromIntString(demoNo), dateRange, iOffSet, iPageSize);
		}

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
				ch1Obj.setTotal(h.getTotal());

				retval.add(ch1Obj);

				String dx = "";
				String strService = "";
				String strServiceDate = "";
				for (BillingONItem i : itemDao.findByCh1IdAndStatusNotEqual(h.getId(), "D")) {
					strService += i.getServiceCode() + " x " + i.getServiceCount() + ", ";
					dx = i.getDx();
					strServiceDate = ConversionUtils.toDateString(i.getServiceDate());
				}

				BillingItemData itObj = new BillingItemData();
				itObj.setService_code(strService);
				itObj.setDx(dx);
				itObj.setService_date(strServiceDate);
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
		List<String> serviceCodeList = new ArrayList<String>();
		if (serviceCodes != null && serviceCodes.length() > 0) {
			String[] serviceArray = serviceCodes.split(",");
			for (int i = 0; i < serviceArray.length; i++) {
				serviceCodeList.add(serviceArray[i].trim());
			}
		} 
		if (billingForm != null && billingForm.length() > 0) {
			CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
			
			for(Object code : dao.findServiceCodesByType(billingForm)) {
				serviceCodeList.add(String.valueOf(code));
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
				ch1Obj.setTotal(h.getTotal());
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

}
