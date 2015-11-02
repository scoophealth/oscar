<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@page import="org.oscarehr.common.dao.BillingONItemDao"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.MiscUtils" %>

<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao" %>

<%@page import="java.util.Calendar" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.List" %>

<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="org.oscarehr.common.model.BillingONCHeader1" %>

<%@page import="org.oscarehr.common.dao.BillingONPaymentDao" %>
<%@page import="org.oscarehr.common.model.BillingONPayment" %>

<%@page import="org.oscarehr.common.dao.BillingOnItemPaymentDao" %>
<%@page import="org.oscarehr.common.model.BillingOnItemPayment" %>

<%@page import="org.oscarehr.common.dao.BillingONItemDao" %>
<%@page import="org.oscarehr.common.model.BillingONItem" %>

<%@page import="org.oscarehr.common.dao.BillingONExtDao" %>
<%@page import="org.oscarehr.common.model.BillingONExt" %>

<%@page import="org.apache.log4j.Logger" %>

<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<title>Fix 3rd party billing payments</title>
</head>
<body>
</body>

<%
	BillingONCHeader1Dao billingOnCHeader1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	BillingONItemDao billingOnItemDao = SpringUtils.getBean(BillingONItemDao.class);
	BillingONPaymentDao billingOnPaymentDao = SpringUtils.getBean(BillingONPaymentDao.class);
	BillingONExtDao billingOnExtDao = SpringUtils.getBean(BillingONExtDao.class);
	BillingOnItemPaymentDao billingOnItemPaymentDao = SpringUtils.getBean(BillingOnItemPaymentDao.class);
	
	Logger logger = MiscUtils.getLogger();
	
	//basically, we go through each 3rd party billing
	//load the item(s)
	//load the payments / exts
	//create the item_payment rows
	//create the transaction?

	List<BillingONCHeader1> billings = null;
	boolean more = false;
	int start=0;
	int limit=100;
	int totalSeen = 0;
	do {
		
		billings = billingOnCHeader1Dao.findAllByPayProgram("PAT", start, limit);
		for(BillingONCHeader1 billing: billings) {
			int billingNo = billing.getId();
			List<BillingONItem> items = billing.getBillingItems();
			List<BillingONPayment> payments = billingOnPaymentDao.listPaymentsByBillingNo(billingNo);
			List<BillingONExt> exts = billingOnExtDao.getBillingExtItems(String.valueOf(billingNo));
			List<BillingOnItemPayment> itemPayments = billingOnItemPaymentDao.findByBillingNo(billingNo);
			
			/*
			logger.info("processing billing #"+billingNo);
			logger.info("- has "+ items.size() + " items.");
			logger.info("- has " + payments.size() + " payments");
			logger.info("- has " + exts.size() + " EXTs");
			logger.info("- has " + itemPayments.size() + " item payments");
			*/
			
			//make sure there is a billing_on_payment entry..even though it's zeros
			//for our data, we always had minimum 1 payment.
			if(payments.size() == 0) {
				logger.info(billingNo + " has no payments. but it should have a single entry. skipping");
				continue;
			}
			
			//can have multiple payments/refunds..lets make sure this is either 0 or fully paid. partially paid too hard since
			//we don't know which item(s) to apply the payment to.
			//This part basically figures out the total payments minus the total refunds.
			Double t = new Double(0);
			
			for(BillingONPayment payment:payments) {
				
				for(BillingONExt ext:payment.getBillingONExtItems()) {
					if(ext.getKeyVal().equals("payment")){
						Double d = new Double(ext.getValue());
						t = t.doubleValue() + d.doubleValue();
					}
					if(ext.getKeyVal().equals("refund")) {
						Double d = new Double(ext.getValue());
						t = t.doubleValue() - d.doubleValue();
					}
				}	
			}
			
			//Go through all the iems, and add up the fees
			//Not sure about how the GST part works..but this seems to work ok with HPH data
			Double it = new Double(0);
			for(BillingONItem item:items) {
				if("D".equals(item.getStatus())) {
					continue;
				}
				Double f = new Double(item.getFee());
				it = it.doubleValue() + f.doubleValue();
			}
			
			//skip partially paid invoices, and log it.
			//the other ones we know are 0.00 or the full amount.
			if(t.doubleValue() == 0 || (it.doubleValue()-t.doubleValue()) == 0) {
				//logger.info("can update");
			} else {
				logger.info(billing.getId() + " is only partially paid..can't proceed on this");
				continue;
			}
			
			//update the billing_on_cheader1.paid column to be all payments - refunds. (most currently set to 0.00)
			//logger.info("billing #" + billingNo + " has paid set to " + billing.getPaid().doubleValue());
			if(billing.getPaid().doubleValue() != t.doubleValue()) {
				//logger.info("I want to set billing.paid to " + t.doubleValue() + " from " + billing.getPaid().doubleValue());
				billing.setPaid(new BigDecimal(t));
				billingOnCHeader1Dao.merge(billing);
			} else {
				//logger.info("I don't need to set billing.paid (" + t.doubleValue() + ")");
			}
			
			//I checked that up to the update, each "payment" and "refund" have a payment_id..so i know there's no orphan ones.
			
			//for each payment, we want to make sure appropriate data is set (which is the billing_on_item_payment(s) that relate to this one. and set the total(s))
			for(BillingONPayment payment:payments) {
				List<BillingONExt> paymentExts = billingOnExtDao.findByBillingNoAndPaymentIdAndKey(billingNo,payment.getId(),"payment");
				List<BillingONExt> refundExts = billingOnExtDao.findByBillingNoAndPaymentIdAndKey(billingNo,payment.getId(), "refund");
				List<BillingONExt> payMethodExts = billingOnExtDao.findByBillingNoAndPaymentIdAndKey(billingNo,payment.getId(), "payMethod");
				
				
				//for this payment, we should have either 1 payment or 1 refund.
				BillingONExt paymentExt = paymentExts.size()==1?paymentExts.get(0):null;
				BillingONExt refundExt = refundExts.size()==1?refundExts.get(0):null;
				BillingONExt payMethodExt = payMethodExts.size()==1?payMethodExts.get(0):null;
				
				
				BigDecimal paymentValue = (paymentExt != null)?new BigDecimal(paymentExt.getValue()):new BigDecimal(0);
				BigDecimal refundValue = (refundExt != null)?new BigDecimal(refundExt.getValue()):new BigDecimal(0);
				
				double dPayment = paymentValue.doubleValue();
				double dRefund = refundValue.doubleValue();
				
				
				if(paymentExt == null && refundExt == null) {
					//this is probably a record made with the new system. it doesn't respect the payment_id column in the billing_on_ext it makes
					//not sure why it makes them at all.
					logger.info("skipping..billing #"+billingNo+ "/paymentId "+payment.getId()+" - has no payment / refund exts");
				}
				
				if(dPayment > 0 && dRefund > 0) {
					//never saw this..just checking the data is good
					logger.info("there's a payment and a refund? that's odd");
				}
				
				
				if(payMethodExt == null ) {
					logger.info("no payMethod found for billing #" + billingNo + ",payment_id="+payment.getId());
				} else {
					if(!payMethodExt.getValue().isEmpty()) {
						//logger.info("updating paymentTypeId on billing_on_payment " + payment.getId() );
						payment.setPaymentTypeId(Integer.parseInt(payMethodExt.getValue()));
						billingOnPaymentDao.merge(payment);
					}
					
				}
				
				//go through the payment, and apply it all to the items. Make sure the whole payment/refund is dispurst
				
				double remainingPayment = dPayment;
				double remainingRefund = dRefund;
				
				for(BillingONItem item:items) {
					boolean deleted=false;
					if("D".equals(item.getStatus())) {
						deleted=true;
					}
					BillingOnItemPayment boip = billingOnItemPaymentDao.findByPaymentIdAndItemId(payment.getId(), item.getId());
					if(boip == null) {
						//add it..need to know how to handle a refund in the new system.
						boip = new BillingOnItemPayment();
						boip.setBillingOnItemId(item.getId());
						boip.setBillingOnPaymentId(payment.getId());
						boip.setCh1Id(billingNo);
						boip.setCredit(new BigDecimal(0.00));
						boip.setDiscount(new BigDecimal(0.00));
						boip.setPaid(new BigDecimal(0.00));
						boip.setPaymentTimestamp(new java.sql.Timestamp(new java.util.Date().getTime()));
						boip.setRefund(new BigDecimal(0.00));
						
						//apply the payment/refund to this row..up to the item.getFee()
						BigDecimal fee = new BigDecimal(item.getFee());
						
						//only apply payment if not deleted..we still want to create missing BOIPs
						if(!deleted) {
							if(dPayment>0) {
								boip.setPaid(new BigDecimal(Math.min(remainingPayment, fee.doubleValue())));
								remainingPayment -= Math.min(remainingPayment, fee.doubleValue());
							}
							if(dRefund>0) {
								boip.setRefund(new BigDecimal(Math.min(remainingRefund, fee.doubleValue())));
								remainingRefund -= Math.min(remainingRefund, fee.doubleValue());
							}
						}
						
						billingOnItemPaymentDao.persist(boip);
						
						//logger.info("adding new billing_on_item_payment for billing #" + billingNo + ", paymentId " + payment.getId());
						
					} else {
						//this never happens on our data by the time the code gets here
						logger.info("should not already have this record. script already run?");
					}
					
				}
				
				//this wouldn't be good to see this output.
				if(remainingPayment >0 || remainingRefund > 0) {
					logger.info("remainingPayment="+remainingPayment);
					logger.info("remainingRefund="+remainingRefund);
				}
				
				payment.setTotal_payment(new BigDecimal(dPayment));
				payment.setTotal_refund(new BigDecimal(dRefund));
				billingOnPaymentDao.merge(payment);
			}
			
			//not sure how to change the exts. Annie's script gets them down to one
			
			totalSeen++;
		}
		if(billings.size() == limit) {
			more=true;
			start = start + limit;
		} else {
			more=false;
		}
	} while(more);
	

	logger.info("Processed a total of " + totalSeen + " billing records");
	
%>

</html:html>