/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.billing.CA.ON.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingONPaymentDao;
import org.oscarehr.common.dao.BillingOnItemPaymentDao;
import org.oscarehr.common.dao.BillingOnTransactionDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.common.model.BillingOnItemPayment;
import org.oscarehr.common.model.BillingOnTransaction;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.on.data.BillingItemData;
import oscar.oscarBilling.ca.on.data.JdbcBilling3rdPartImpl;


/**
 * 
 * @author rjonasz
 */
public class BillingONPaymentsAction extends DispatchAction {
	private static Logger logger = Logger
			.getLogger(BillingONPaymentsAction.class);

	private BillingONItemDao billingONItemDao;
	private BillingONPaymentDao billingONPaymentDao;
	private BillingPaymentTypeDao billingPaymentTypeDao;
	private BillingONCHeader1Dao billingClaimDAO;
	private BillingONExtDao billingONExtDao;
	private BillingOnItemPaymentDao billingOnItemPaymentDao;
	private BillingOnTransactionDao billingOnTransactionDao;
    
	public ActionForward listPayments(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Integer billingNo = Integer.parseInt(request.getParameter("billingNo"));
		
		List<BillingONPayment> paymentLists = billingONPaymentDao.find3rdPartyPaymentsByBillingNo(billingNo);
		if (paymentLists == null) {
			paymentLists = new ArrayList<BillingONPayment>();
		}
		
		BillingONCHeader1Dao ch1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
		BillingONCHeader1 cheader = ch1Dao.find(billingNo);
		BigDecimal total = cheader.getTotal();
		
		request.setAttribute("totalInvoiced", cheader.getTotal());
		
		BigDecimal payments = BigDecimal.ZERO;
		BigDecimal balances = BigDecimal.ZERO;
		BigDecimal refunds = BigDecimal.ZERO;
		BigDecimal discounts = BigDecimal.ZERO;
		BigDecimal credits = BigDecimal.ZERO;
		for(BillingONPayment pmt:paymentLists) {
			payments = new BigDecimal(pmt.getTotal_payment().intValue() + payments.intValue());
			discounts = new BigDecimal(pmt.getTotal_discount().intValue() + discounts.intValue());
			refunds = new BigDecimal (pmt.getTotal_refund().intValue() + refunds.intValue());
			credits = new BigDecimal(pmt.getTotal_credit().intValue() + credits.intValue());
		}
		BigDecimal balance = total.subtract(payments).subtract(discounts).add(credits);
		request.setAttribute("balance", balance);
	
		
		request.setAttribute("paymentsList", paymentLists);
		
		List<BillingONItem> items = billingONItemDao.getActiveBillingItemByCh1Id(billingNo);
		List<BillingItemData> itemDataList = new ArrayList<BillingItemData>();
		for (BillingONItem item : items) {
			List<BillingOnItemPayment> paymentList = billingOnItemPaymentDao.getAllByItemId(item.getId());
			BigDecimal payment = BigDecimal.ZERO;
			BigDecimal discount = BigDecimal.ZERO;
			BigDecimal refund = BigDecimal.ZERO;
			BigDecimal credit = BigDecimal.ZERO;
			for (BillingOnItemPayment payIter : paymentList) {
				payment = payment.add(payIter.getPaid());
				discount = discount.add(payIter.getDiscount());
				refund = refund.add(payIter.getRefund());
				credit = credit.add(payIter.getCredit());
			}
			
			BillingItemData itemData = new BillingItemData();
			itemData.setId(item.getId().toString());
			itemData.setService_code(item.getServiceCode());
			itemData.setFee(item.getFee());
			itemData.setPaid(payment.toString());
			itemData.setDiscount(discount.toString());
			itemData.setRefund(refund.toString());
			itemData.setCredit(credit.toString());
			
			itemDataList.add(itemData);
		}
		
		request.setAttribute("itemDataList", itemDataList);
		List<BillingPaymentType> paymentTypes = billingPaymentTypeDao.findAll();
		request.setAttribute("paymentTypeList", paymentTypes);

		
		BillingONCHeader1 cheader1 = billingClaimDAO.find(billingNo);
		Integer demographicNo = cheader1.getDemographicNo();
		BigDecimal payment = BigDecimal.ZERO;
		balance = BigDecimal.ZERO;
		total = BigDecimal.ZERO;
		BigDecimal discount = BigDecimal.ZERO;
		BigDecimal credit = BigDecimal.ZERO;
		BigDecimal refund = BigDecimal.ZERO;
		
		total = cheader1.getTotal();
		
		for (BillingONPayment bop : paymentLists) {
			credit = credit.add(bop.getTotal_credit());
			discount = discount.add(bop.getTotal_discount());
			payment = payment.add(bop.getTotal_payment());
			refund = refund.add(bop.getTotal_refund());		
		}
		return actionMapping.findForward("success");
	}
	
	public ActionForward savePayment(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {

		Date curDate = new Date();
		String paymentdate1=request.getParameter("paymentDate");
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
	    Date paymentdate=sim.parse(paymentdate1);
		
		int itemSize = Integer.parseInt(request.getParameter("size"));
		int billNo = Integer.parseInt(request.getParameter("billingNo"));
		String curProviderNo = (String) request.getSession().getAttribute("user");
		String paymentTypeId = request.getParameter("paymentType");
		if (paymentTypeId == null || paymentTypeId.isEmpty()) {
			paymentTypeId = "0";
		}

		// get all paid, discount and refund list
		BigDecimal sumPaid = BigDecimal.ZERO;
		BigDecimal sumRefund = BigDecimal.ZERO;
		BigDecimal sumCredit = BigDecimal.ZERO;
		BigDecimal sumDiscount = BigDecimal.ZERO;
		for (int i = 0; i < itemSize; i++) {
			String payment = request.getParameter("payment" + i);
			String discount = request.getParameter("discount" + i);
			String itemId = request.getParameter("itemId" + i);
			if (billingONItemDao.find(Integer.parseInt(itemId))!=null) {
				if ("payment".equals(request.getParameter("sel" + i))) {
					BigDecimal pay = BigDecimal.ZERO;
					BigDecimal dicnt = BigDecimal.ZERO;
					try {
						pay = new BigDecimal(payment);
					} catch (Exception e) {}
					if (pay.compareTo(BigDecimal.ZERO) == 1) {
						sumPaid = sumPaid.add(pay);
					}
					try {
						dicnt = new BigDecimal(discount);
					} catch (Exception e) {}
					if (dicnt.compareTo(BigDecimal.ZERO) == 1) {
						sumDiscount = sumDiscount.add(dicnt);
					}
				} else if ("refund".equals(request.getParameter("sel" + i))) {
					BigDecimal refundTmp = BigDecimal.ZERO;
					try {
						refundTmp = new BigDecimal(payment);
					} catch (Exception e) {}
					if (refundTmp.compareTo(BigDecimal.ZERO) == 1) {
						sumRefund = sumRefund.add(refundTmp);
					}
				} else if ("credit".equals(request.getParameter("sel" + i))) {
					BigDecimal creditTmp = BigDecimal.ZERO;
					try {
						creditTmp = new BigDecimal(payment);
					} catch (Exception e) {	}
					if (creditTmp.compareTo(BigDecimal.ZERO) == 1) {
						sumCredit = sumCredit.add(creditTmp);
					}
				}
			}
		}
		
		BillingONCHeader1 cheader1 = billingClaimDAO.find(billNo);
		if (cheader1 == null) {
			return actionMapping.findForward("failure");
		}
		String status = request.getParameter("status");
		boolean toUpdateChl = false;
		if (status != null && !status.equals(cheader1.getStatus())) {
			cheader1.setStatus(status);
			toUpdateChl = true;
		}
		
		JSONObject ret = new JSONObject();
		if (sumPaid.compareTo(BigDecimal.ZERO) == 0
				&& sumDiscount.compareTo(BigDecimal.ZERO) == 0
				&& sumRefund.compareTo(BigDecimal.ZERO) == 0
				&& sumCredit.compareTo(BigDecimal.ZERO) == 0) {
			
			if (toUpdateChl) {
				billingClaimDAO.merge(cheader1);
				ret.put("ret", 0);
			} else {
				ret.put("ret", 1);
				ret.put("reason", "Payments, discounts and refunds can't be all zeros!!");
			}
			response.setCharacterEncoding("utf-8");
			response.setContentType("html/text");
			try {
				response.getWriter().print(ret.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				logger.info(e.toString());
				return actionMapping.findForward("failure");
			}
			return null;
		}
		
		// count sum of paid,refund,discount
		String demographicNo = cheader1.getDemographicNo().toString();
		
		// 1.billing_on_ext table: payment		
		JdbcBilling3rdPartImpl tExtObj = new JdbcBilling3rdPartImpl();
		if (sumPaid.compareTo(BigDecimal.ZERO) == 1) {
			toUpdateChl = true;
			BigDecimal sumPaidTmp = sumPaid.add(cheader1.getPaid());
			cheader1.setPaid(sumPaidTmp);
			if (tExtObj.keyExists(Integer.toString(billNo), BillingONExtDao.KEY_PAYMENT)) {
				tExtObj.updateKeyValue(Integer.toString(billNo), BillingONExtDao.KEY_PAYMENT, sumPaidTmp.toString());
			} else {
				tExtObj.add3rdBillExt(Integer.toString(billNo), demographicNo, BillingONExtDao.KEY_PAYMENT, sumPaidTmp.toString());
			}
		}
		if (toUpdateChl) {
			billingClaimDAO.merge(cheader1);
		}
		
		// 2.update billing_on_ext table: discount
		if (sumDiscount.compareTo(BigDecimal.ZERO) == 1) {
			BigDecimal extDiscount = billingONExtDao.getAccountVal(billNo, BillingONExtDao.KEY_DISCOUNT);
			BigDecimal sumDiscountTmp = sumDiscount.add(extDiscount);
			if (tExtObj.keyExists(Integer.toString(billNo), BillingONExtDao.KEY_DISCOUNT)) {
				tExtObj.updateKeyValue(Integer.toString(billNo), BillingONExtDao.KEY_DISCOUNT, sumDiscountTmp.toString());
			} else {
				tExtObj.add3rdBillExt(Integer.toString(billNo), demographicNo, BillingONExtDao.KEY_DISCOUNT, sumDiscountTmp.toString());
			}
		}
		
		// 3.update billing_on_ext table: refund
		if (sumRefund.compareTo(BigDecimal.ZERO) == 1) {
			BigDecimal extRefund = billingONExtDao.getAccountVal(billNo, BillingONExtDao.KEY_REFUND);
			BigDecimal sumRefundTmp = sumRefund.add(extRefund);
			if (tExtObj.keyExists(Integer.toString(billNo), BillingONExtDao.KEY_REFUND)) {
				tExtObj.updateKeyValue(Integer.toString(billNo), BillingONExtDao.KEY_REFUND, sumRefundTmp.toString());
			} else {
				tExtObj.add3rdBillExt(Integer.toString(billNo), demographicNo, BillingONExtDao.KEY_REFUND, sumRefundTmp.toString());
			}
		}
		
		// 3.update billing_on_ext table: credit
		if (sumCredit.compareTo(BigDecimal.ZERO) == 1) {
			BigDecimal extCredit = billingONExtDao.getAccountVal(billNo, BillingONExtDao.KEY_CREDIT);
			BigDecimal sumCreditTmp = sumCredit.add(extCredit);
			if (tExtObj.keyExists(Integer.toString(billNo), BillingONExtDao.KEY_CREDIT)) {
				tExtObj.updateKeyValue(Integer.toString(billNo), BillingONExtDao.KEY_CREDIT, sumCreditTmp.toString());
			} else {
				tExtObj.add3rdBillExt(Integer.toString(billNo), demographicNo, BillingONExtDao.KEY_CREDIT, sumCreditTmp.toString());
			}
		}

		// update billing_on_ext table: KEY_PAY_METHOD
		if (paymentTypeId!=null) {
			BillingONExt extCredit = billingONExtDao.getClaimExtItem(Integer.valueOf(billNo), Integer.valueOf(demographicNo), BillingONExtDao.KEY_PAY_METHOD);			
			if (tExtObj.keyExists(Integer.toString(billNo), BillingONExtDao.KEY_PAY_METHOD)) {
				tExtObj.updateKeyValue(Integer.toString(billNo), BillingONExtDao.KEY_PAY_METHOD, paymentTypeId);
			} else {
				tExtObj.add3rdBillExt(Integer.toString(billNo), demographicNo, BillingONExtDao.KEY_PAY_METHOD, paymentTypeId);
			}
		}
				
		// 4.update billing_on_payment
		BillingONPayment billPayment = new BillingONPayment();
		billPayment.setBillingOnCheader1(cheader1);
		billPayment.setBillingNo(billNo);
		billPayment.setCreator(curProviderNo);
		billPayment.setPaymentDate(paymentdate);
		billPayment.setPaymentTypeId(Integer.parseInt(paymentTypeId));
		billPayment.setTotal_payment(sumPaid);
		billPayment.setTotal_discount(sumDiscount);
		billPayment.setTotal_refund(sumRefund);
		billPayment.setTotal_credit(sumCredit);
		billingONPaymentDao.persist(billPayment);
		
		// 5.update biling_on_item_payment
		for (int i = 0; i < itemSize; i++) {
			String payment = request.getParameter("payment" + i);
			String discount = request.getParameter("discount" + i);
			String itemId = request.getParameter("itemId" + i);
			BillingONItem billItem = billingONItemDao.find(Integer.parseInt(itemId));
			if(billItem == null) continue;
            
			String str=paymentdate1+" 00:00:00";
     		SimpleDateFormat sim1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    Date paymentdatetmp=sim1.parse(str);
			BillingOnItemPayment billItemPayment = new BillingOnItemPayment();
			billItemPayment.setBillingOnItemId(Integer.parseInt(itemId));
			billItemPayment.setBillingOnPaymentId(billPayment.getId());
			billItemPayment.setCh1Id(billNo);
			billItemPayment.setPaymentTimestamp(new Timestamp(paymentdatetmp.getTime()));
			
			if ("payment".equals(request.getParameter("sel" + i))) {
				BigDecimal itemPayment = BigDecimal.ZERO;
				BigDecimal itemDiscnt = BigDecimal.ZERO;
				try {
					itemPayment = new BigDecimal(payment);
				} catch (Exception e) {}
				try {
					itemDiscnt = new BigDecimal(discount);
				} catch (Exception e) {}
				
				if (itemPayment.compareTo(BigDecimal.ZERO) == 0 && itemDiscnt.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				billItemPayment.setPaid(itemPayment);
				billItemPayment.setDiscount(itemDiscnt);
				billingOnItemPaymentDao.persist(billItemPayment);
				BillingOnTransaction billTrans = billingOnTransactionDao.getTransTemplate(cheader1, billItem, billPayment, curProviderNo,billItemPayment.getId());
				billTrans.setServiceCodePaid(itemPayment);
				billTrans.setServiceCodeDiscount(itemDiscnt);
				billingOnTransactionDao.persist(billTrans);
			} else if ("refund".equals(request.getParameter("sel" + i))) {
				BigDecimal itemRefund = BigDecimal.ZERO;
				try {
					itemRefund = new BigDecimal(payment);
				} catch (Exception e) {}
				if (itemRefund.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				billItemPayment.setRefund(itemRefund);
				billingOnItemPaymentDao.persist(billItemPayment);
				BillingOnTransaction billTrans = billingOnTransactionDao.getTransTemplate(cheader1, billItem, billPayment, curProviderNo,billItemPayment.getId());
				billTrans.setServiceCodeRefund(itemRefund);
				billingOnTransactionDao.persist(billTrans);
			} else if ("credit".equals(request.getParameter("sel" + i))) {
				BigDecimal itemCredit = BigDecimal.ZERO;
				try {
					itemCredit = new BigDecimal(payment);
				} catch (Exception e) {}
				if (itemCredit.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				billItemPayment.setCredit(itemCredit);
				billingOnItemPaymentDao.persist(billItemPayment);
				BillingOnTransaction billTrans = billingOnTransactionDao.getTransTemplate(cheader1, billItem, billPayment, curProviderNo,billItemPayment.getId());
				billTrans.setServiceCodeCredit(itemCredit);
				billingOnTransactionDao.persist(billTrans);
			}
		}
		ret.put("ret", 0);
		response.setCharacterEncoding("utf-8");
		response.setContentType("html/text");
		try {
			response.getWriter().print(ret.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			logger.info(e.toString());
			return actionMapping.findForward("failure");
		}
		return null;

	}

	public ActionForward deletePayment(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Date curDate = new Date();
		try {
			Integer paymentId = Integer.parseInt(request.getParameter("id"));
			BillingONPayment payment = billingONPaymentDao.find(paymentId);
			BillingONCHeader1 ch1 = payment.getBillingONCheader1();
			Integer billingNo = payment.getBillingONCheader1().getId();

			billingONPaymentDao.remove(paymentId);

			BigDecimal paid = billingONPaymentDao.getPaymentsSumByBillingNo(billingNo);
			BigDecimal refund = billingONPaymentDao.getPaymentsRefundByBillingNo(billingNo).negate();
			NumberFormat currency = NumberFormat.getCurrencyInstance();
			ch1.setPaid(paid.subtract(refund));
			billingClaimDAO.merge(ch1);

			billingONExtDao.setExtItem(billingNo, ch1.getDemographicNo(),
					BillingONExtDao.KEY_PAYMENT,
					currency.format(paid).replace("$", ""), curDate, '1');
			billingONExtDao.setExtItem(billingNo, ch1.getDemographicNo(),
					BillingONExtDao.KEY_REFUND, currency.format(refund)
							.replace("$", ""), curDate, '1');

		} catch (Exception ex) {
			logger.error(
					"Failed to delete payment: " + request.getParameter("id"),
					ex);
			return actionMapping.findForward("failure");
		}

		return listPayments(actionMapping, actionForm, request, response);

	}
	
	public ActionForward viewPayment(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("paymentId");
		int paymentId = 0;
		try {
			paymentId = Integer.parseInt(id);
			if (paymentId == 0) {
				return actionMapping.findForward("failure");
			}
		} catch (Exception e) {
			logger.info(e.toString());
			return actionMapping.findForward("failure");
		}
		BillingONPayment billPayment = billingONPaymentDao.find(paymentId);
		if (billPayment == null) {
			return actionMapping.findForward("failure");
		}
		List<BillingOnItemPayment> itemPaymentList = billingOnItemPaymentDao.getItemsByPaymentId(paymentId);
		if (itemPaymentList == null) {
			return actionMapping.findForward("failure");
		}
		JSONArray payDetail = new JSONArray();

		// payment date object
		JSONObject paymentDateObj = new JSONObject();
		paymentDateObj.put("paymentDate", new SimpleDateFormat("yyyy-MM-dd").format(billPayment.getPaymentDate()));
		payDetail.add(paymentDateObj);
		
		// payment type object
		JSONObject typeObj = new JSONObject();
		typeObj.put("paymentType", billPayment.getPaymentTypeId());
		payDetail.add(typeObj);
		
		for (BillingOnItemPayment itemPayment : itemPaymentList) {
			JSONObject itemObj = new JSONObject();
			itemObj.put("id", itemPayment.getBillingOnItemId());
			if (itemPayment.getRefund().compareTo(BigDecimal.ZERO) == 0) {
				itemObj.put("type", "payment");
				itemObj.put("payment", itemPayment.getPaid());
				itemObj.put("discount", itemPayment.getDiscount());
			} else {
				itemObj.put("type", "refund");
				itemObj.put("refund", itemPayment.getRefund());
			}
			payDetail.add(itemObj);
		}
		response.setCharacterEncoding("utf-8"); 
        response.setContentType("html/text");
		try {
			response.getWriter().print(payDetail.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			logger.info(e.toString());
			return actionMapping.findForward("failure");
		}
		
		return null;
	}
	
	public ActionForward viewPayment_ext(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		// 1.get payment details according to billing_on_item_payment
		int billPaymentId = 0;
		try {
			billPaymentId = Integer.parseInt(request.getParameter("billPaymentId"));
		} catch (Exception e) {
			MiscUtils.getLogger().info(e.toString());
			return null;
		}
		BillingONPayment billPayment = billingONPaymentDao.find(billPaymentId);
		if (billPayment == null) {
			return null;
		}
		request.setAttribute("billPayment", billPayment);
		List<BillingItemData> itemDataList = new ArrayList<BillingItemData>();
		List<BillingOnItemPayment> itemPaymentList = billingOnItemPaymentDao.getItemsByPaymentId(billPaymentId);
		for (BillingOnItemPayment itemPayment : itemPaymentList) {
			BillingONItem billItemList = billingONItemDao.find(itemPayment.getBillingOnItemId());
			if (billItemList == null ) {
				continue;
			}
			BillingItemData itemData = new BillingItemData();
			itemData.setId(Integer.toString(itemPayment.getBillingOnItemId()));
			itemData.setService_code(billItemList.getServiceCode());
			itemData.setFee(billItemList.getFee());
			itemData.setPaid(itemPayment.getPaid().toString());
			itemData.setDiscount(itemPayment.getDiscount().toString());
			itemData.setRefund(itemPayment.getRefund().toString());
			itemData.setCredit(itemPayment.getCredit().toString());
			itemData.setCh1_id(String.valueOf(itemPayment.getCh1Id()));
			String ptName = "";
			Integer ch1_id = itemPayment.getCh1Id();
			BillingONCHeader1 ch1 = billingClaimDAO.find(ch1_id);
			if(ch1!=null) {
				ptName = ch1.getDemographicName();
				if(ptName==null)
					ptName = "";
			}
			itemData.setPatientName(ptName);
			itemDataList.add(itemData);
		}
		
		request.setAttribute("itemDataList", itemDataList);

		return actionMapping.findForward("viewPayment");
	}

	public void setBillingONPaymentDao(BillingONPaymentDao paymentDao) {
		this.billingONPaymentDao = paymentDao;
	}

	public void setBillingPaymentTypeDao(BillingPaymentTypeDao paymentTypeDao) {
		this.billingPaymentTypeDao = paymentTypeDao;
	}

	public void setBillingONCHeader1Dao(BillingONCHeader1Dao billingDao) {
		this.billingClaimDAO = billingDao;
	}

	public void setBillingONExtDao(BillingONExtDao billingExtDao) {
		this.billingONExtDao = billingExtDao;
	}

	public void setBillingONItemDao(BillingONItemDao billingOnItemDao) {
		this.billingONItemDao = billingOnItemDao;
	}
	
	public void setBillingOnItemPaymentDao(BillingOnItemPaymentDao billingOnItemPaymentDao) {
		this.billingOnItemPaymentDao = billingOnItemPaymentDao;
	}

	public void setBillingOnTransactionDao(
			BillingOnTransactionDao billingOnTransactionDao) {
		this.billingOnTransactionDao = billingOnTransactionDao;
	}
}
