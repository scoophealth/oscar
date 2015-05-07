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

package oscar.oscarBilling.ca.on.pageUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONPaymentDao;
import org.oscarehr.common.dao.BillingONRepoDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.service.BillingONService;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

/**
 *
 * @author mweston4
 */
public class BillingCorrectionAction extends DispatchAction{
    
    private BillingONPaymentDao bPaymentDao = (BillingONPaymentDao) SpringUtils.getBean("billingONPaymentDao");        
    private BillingONCHeader1Dao bCh1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");     
    private  BillingONExtDao billExtDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");
        
    public ActionForward add3rdPartyPayment(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		String invoiceNo = request.getParameter("billing_no"); 
        
        BillingONCHeader1 bCh1 = bCh1Dao.find(Integer.parseInt(invoiceNo));
        
        //If we have a bill
        if (bCh1 != null) {
            
            //Validate pay amount
            BigDecimal paidAmt = null;
            try {
                  String amtPaid = request.getParameter("amtPaid");
                  paidAmt = new BigDecimal(amtPaid);                  
            } catch (NumberFormatException e) {
                MiscUtils.getLogger().error("3rd party pay amount not a valid number", e);   
                return mapping.findForward("closeReload");
            }
                        
            //Validate pay Method
            String payMethod = request.getParameter("payMethod");
            if (  (payMethod == null) ||
                  (!payMethod.equals("1")//Cash
                 &&!payMethod.equals("2")//Cheque
                 &&!payMethod.equals("3")//Visa
                 &&!payMethod.equals("4")//Mastercard
                 &&!payMethod.equals("5")//Amex
                 &&!payMethod.equals("6")//Electronic
                 &&!payMethod.equals("7")//Debit                
                 &&!payMethod.equals("8")//Alternate
                  )
               ) 
            {
                MiscUtils.getLogger().error("3rd party pay method not valid");  
                return mapping.findForward("closeReload");
            }
            
            //Validate pay type
            String payType = request.getParameter("payType");
            if ((payType == null) ||
                ( !payType.equals("P")//Payment
                &&!payType.equals("R")//Refund                
                ) 
               )
            {
                MiscUtils.getLogger().error("3rd party pay type not valid");
                return mapping.findForward("closeReload");
            }
            
            //Add new payment amount to third party bill
            bPaymentDao.createPayment(bCh1, request.getLocale(), payType, paidAmt, payMethod,providerNo);
                                                           
            return mapping.findForward("success");            
        }
        else {
            MiscUtils.getLogger().error("Invalid billing invoice:"+ invoiceNo);
            return mapping.findForward("closeReload");  
        }
        
    }
    
    public ActionForward updateInvoice(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
        Integer billingNo = null;
        try {
            billingNo = Integer.parseInt(request.getParameter("xml_billing_no"));
        }                       
        catch (NumberFormatException e) {
            MiscUtils.getLogger().error("Billing number invalid for Ch1 Id: " + request.getParameter("xml_billing_no"));           
            return mapping.findForward("closeReload");
        }
        BillingONCHeader1 bCh1 = bCh1Dao.find(billingNo);
                        
        if (bCh1 == null) {
            MiscUtils.getLogger().error("No billing object found for Ch1 Id: " + request.getParameter("xml_billing_no"));
            return mapping.findForward("closeReload");            
        }        
        
        if (!updateBillingONCHeader1(bCh1, request))
            return mapping.findForward("failure");
                           
        if (!bCh1.getBillingItems().isEmpty()) {
            updateBillingItems(bCh1, request);
            BillingONService billingONService = (BillingONService) SpringUtils.getBean("billingONService");
            if (!billingONService.updateTotal(bCh1))
                return mapping.findForward("failure");
        }
        
        bCh1Dao.merge(bCh1);
         
        String newStatus = request.getParameter("status").substring(0,1);
        String oldStatus = bCh1.getStatus();
        
        //Add payment audit if bill has just been settled.
        if (newStatus.equals(BillingONCHeader1.SETTLED) && !oldStatus.equals(newStatus)) {  
                BillingONPayment billPayment = new BillingONPayment();
                billPayment.setBillingOnCheader1(bCh1);
                billPayment.setPaymentDate(new java.util.Date());
                bPaymentDao.persist(billPayment);
        }
        
        //Update Bill To if changed.
        if (request.getParameter("billTo") != null) {           
            BillingONExt billExt = billExtDao.getBillTo(bCh1);
            if (billExt != null) {
                billExt.setValue(request.getParameter("billTo"));
                
                billExtDao.merge(billExt);
            } else {
                billExt = new BillingONExt();
                billExt.setBillingNo(bCh1.getId());
                billExt.setDateTime(new Date());
                billExt.setDemographicNo(bCh1.getDemographicNo());
                billExt.setKeyVal("billTo");
                billExt.setPaymentId(new Integer(0));
                billExt.setStatus('1');
                billExt.setValue(request.getParameter("billTo"));
                
                billExtDao.persist(billExt);
            }            
        }
        
        //Update Due Date if changed.
	if (request.getParameter("invoiceDueDate") != null) {
	           
            BillingONExt billExt = billExtDao.getDueDate(bCh1);
	           
            if (billExt != null) {
                billExt.setValue(request.getParameter("invoiceDueDate"));                
                billExtDao.merge(billExt);
            } else {
                billExt = new BillingONExt();
                billExt.setBillingNo(bCh1.getId());
                billExt.setDateTime(new Date());
                billExt.setDemographicNo(bCh1.getDemographicNo());
                billExt.setKeyVal("dueDate");
                billExt.setPaymentId(new Integer(0));
                billExt.setStatus('1');
                billExt.setValue(request.getParameter("invoiceDueDate"));
	               
                billExtDao.persist(billExt);
            }            
        }        
        
        //Update Use Bill To for Reprint if changed                    
        BillingONExt billExt = billExtDao.getUseBillTo(bCh1);            
        if (billExt != null) {
            if (request.getParameter("overrideUseDemoContact") != null) {
                billExt.setValue(request.getParameter("overrideUseDemoContact")); 
                billExt.setStatus('1');
                billExtDao.merge(billExt);
            }
            else {
                billExt.setStatus('0');
                billExtDao.merge(billExt);
            }
        } else if (request.getParameter("overrideUseDemoContact") != null) {           
            billExt = new BillingONExt();
            billExt.setBillingNo(bCh1.getId());
            billExt.setDateTime(new Date());
            billExt.setDemographicNo(bCh1.getDemographicNo());
            billExt.setKeyVal("useBillTo");
            billExt.setPaymentId(new Integer(0));
            billExt.setValue(request.getParameter("overrideUseDemoContact"));             
            billExt.setStatus('1');
            
            billExtDao.persist(billExt);
        }            
    
 
        if(request.getParameter("submit").equals("Save&Correct Another")){
            return mapping.findForward("closeReload");
        } else if(request.getParameter("adminSubmit")!=null){
        	return mapping.findForward("adminReload");
        } else {
            return mapping.findForward("submitClose"); 
        }
        
        
                    
    }
        
    private boolean updateBillingONCHeader1(BillingONCHeader1 bCh1, HttpServletRequest request) {
        
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		Locale locale = request.getLocale();
        
        String status = request.getParameter("status").substring(0,1);
        
        boolean statusChangedToSettled = status.equals("S") && !bCh1.getStatus().equals(status);
        
        String payProgram = "";
            
        if (status.equals("N"))
            payProgram = "NOT";	
        else
            payProgram = request.getParameter("payProgram");
                    
        if (hasInvoiceChanged(bCh1, request)){
                                                                         
            //Add Existing state of Invoice to Billing Repository
            BillingONRepoDao billRepoDao = (BillingONRepoDao) SpringUtils.getBean("billingONRepoDao");                                  
            billRepoDao.createBillingONCHeader1Entry(bCh1, locale); 
                                    
            Date billingDate = null;
            try {
                billingDate = DateUtils.parseDate(request.getParameter("xml_appointment_date"), locale);
            } catch (java.text.ParseException e) {
                MiscUtils.getLogger().error("Invalid billing date:" + request.getParameter("xml_appointment_date"), e);
                return false;
            }                     
            
            Date visitDate = null;
            try {            	
            	visitDate = DateUtils.parseDate(request.getParameter("xml_vdate"), locale);
            }
            catch( java.text.ParseException e) {
            	MiscUtils.getLogger().warn("Could not parse visit date: " + request.getParameter("xml_vdate"), e);
            }
                                                                                   
            String manualReview = "";
            
            if (request.getParameter("m_review") != null) {
                manualReview = "Y";
            }
            
            ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
            Provider provider = providerDao.getProvider(bCh1.getProviderNo());
                                                           
            bCh1.setStatus(status);
            bCh1.setPayProgram(payProgram);
            bCh1.setRefNum(request.getParameter("rdohip"));
            bCh1.setVisitType(request.getParameter("visittype"));            
            bCh1.setFaciltyNum(request.getParameter("clinic_ref_code"));                        
            bCh1.setManReview(manualReview);                        
            bCh1.setBillingDate(billingDate); 
            if(visitDate!=null) 
            	bCh1.setAdmissionDate(visitDate);
            bCh1.setProviderNo(request.getParameter("provider_no"));
            bCh1.setComment(request.getParameter("comment"));           
            bCh1.setProviderOhipNo(provider.getOhipNo());
            bCh1.setProviderRmaNo(provider.getRmaNo());                        
            bCh1.setCreator(providerNo);
            bCh1.setClinic(request.getParameter("site"));			
            bCh1.setProvince(request.getParameter("hc_type"));
            bCh1.setLocation(request.getParameter("xml_slicode"));                        
        }
        
        boolean mohPayProgram = ("HCP".equals(payProgram) || "RMB".equals(payProgram) || "WCB".equals(payProgram));
        
        if( request.getParameter("oldStatus").equals("thirdParty") && mohPayProgram) {
            /* 
             * If status has been changed from 3rd Party Pay Program to Ministry of Health Pay Program, 
             * AND there has been 3rd party payments already received, refund an amount equal to the
             * total amount paid by the 3rd party.
             */
            List<BillingONPayment> paymentRecords = bPaymentDao.find3rdPartyPayRecordsByBill(bCh1);
            BigDecimal payments = BillingONPaymentDao.calculatePaymentTotal(paymentRecords);
            BigDecimal refunds = BillingONPaymentDao.calculateRefundTotal(paymentRecords);
            BigDecimal reversedFunds = payments.subtract(refunds);

            int doReverse = reversedFunds.compareTo(new BigDecimal("0.00"));

            if (doReverse < 0) {
                MiscUtils.getLogger().warn("Amount owing on the account is less than zero. Cannot return payment to third party.");
                return false;
            }

            if (doReverse > 0) {
                bPaymentDao.createPayment(bCh1, locale, BillingONPayment.REFUND, reversedFunds, "",providerNo);                                        
            }
        } else if (statusChangedToSettled && !mohPayProgram) {
            /*
             * If the invoice has just been settled for a 3rd party invoice,
             * Then any amount outstanding is now paid in full.
             */
            List<BillingONPayment> paymentRecords = bPaymentDao.find3rdPartyPayRecordsByBill(bCh1);
                   
            BigDecimal totalOwing =  bCh1.getTotal();
            BigDecimal totalPaid = BillingONPaymentDao.calculatePaymentTotal(paymentRecords);
            BigDecimal totalRefund = BillingONPaymentDao.calculateRefundTotal(paymentRecords);
            BigDecimal amtOutstanding = totalOwing.subtract(totalPaid).add(totalRefund);

            int doSettlePayment = amtOutstanding.compareTo(new BigDecimal("0.00"));

            if (doSettlePayment < 0) {
                MiscUtils.getLogger().warn("Amount to settle on the account is already less than zero. No additional third party payment required.");
                return false;
            }

            if (doSettlePayment > 0) {
                bPaymentDao.createPayment(bCh1, locale, BillingONPayment.PAYMENT, amtOutstanding, "",providerNo);
            }
        }
        
        return true;
    }
    
    private void updateBillingItems(BillingONCHeader1 bCh1, HttpServletRequest request) {
        
        String dx = request.getParameter("xml_diagnostic_detail");
	
        if (dx.length() > 2) {
            dx = dx.substring(0, 3);
        }
        
        String serviceDateStr = request.getParameter("xml_appointment_date");
        
        Date serviceDate = null;
        try {
            serviceDate = DateUtils.parseDate(serviceDateStr,request.getLocale());
        } catch (java.text.ParseException e) {
            MiscUtils.getLogger().error("Invalid date", e);
        }        

        /*
         * Create list of billing items in current state
         */
        List<BillingONItem> bItemsCurrent = new ArrayList<BillingONItem>();
        
        for (int i = 0; i < BillingDataHlp.FIELD_MAX_SERVICE_NUM; i++) {
            String serviceCodeId = request.getParameter("servicecode"+ i);
            if ((serviceCodeId != null) && (serviceCodeId.length() > 0)) { // == 5
                
                String itemStatus = "O";
                if (request.getParameter("itemStatus" + i) != null)
                    itemStatus = "S";
                
                //Determine Unit
                String unit = request.getParameter("billingunit" + i);
                MiscUtils.getLogger().info("("+ serviceCodeId + ") Unit Amount:" + unit);
                if (!unit.matches("\\d+")) {
                    unit = "1";
                }
                BigDecimal unitAmt = new BigDecimal(unit);
                
                 //Determine fee
                String fee = request.getParameter("billingamount" + i);                
                if (fee == null || fee.isEmpty() || fee.trim().isEmpty()) {
                    BillingServiceDao bServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");
                    BillingService bService = bServiceDao.searchBillingCode(serviceCodeId, "ON", serviceDate);
                    
                    if( bService == null ) {
                    	bService = bServiceDao.searchPrivateBillingCode(serviceCodeId, serviceDate);
                    }
                    if( bService != null ) {
                    	                    
                    	if (bService.getTerminationDate().before(serviceDate)) {
                    		fee = "defunct";
                    	} else { 
                    		fee = bService.getValue();      
                    		BigDecimal feeAmt = new BigDecimal(fee);
                    		feeAmt = feeAmt.multiply(unitAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
                    		fee = feeAmt.toPlainString();
                    	}
                    }
                }
                                                    
                BillingONItem bItem = new BillingONItem();
                bItem.setServiceCode(serviceCodeId);
                bItem.setServiceCount(unit);
                bItem.setFee(fee);   
                bItem.setServiceDate(serviceDate);
                bItem.setDx(dx);
                bItem.setStatus(itemStatus);                                               
                bItem.setCh1Id(bCh1.getId());
                bItem.setTranscId(bCh1.getTranscId());
                bItem.setRecId(BillingDataHlp.ITEM_REORDIDENTIFICATION);
                bItemsCurrent.add(bItem);              			
            }
        }
               
        List<BillingONItem> bItemsExisting = bCh1.getBillingItems();
               
        for (BillingONItem bItemCurrent : bItemsCurrent) {

            if (bItemsExisting.contains(bItemCurrent)) {  
                
                // Update an existing billing items  that is now modified, not deleted.               
                
                int index = bItemsExisting.indexOf(bItemCurrent); 
                BillingONItem bItemExisting = bItemsExisting.get(index);

                boolean statusChanged = false;
                if ((!bItemExisting.getStatus().equals("S") && bItemCurrent.getStatus().equals("S"))
                  ||(bItemExisting.getStatus().equals("S") && !bItemCurrent.getStatus().equals("S"))) {
                                statusChanged = true;
                }

                String fee = bItemCurrent.getFee();
                String unit = bItemCurrent.getServiceCount();
                
                if (!bItemExisting.getServiceCount().equals(unit)
                 || !bItemExisting.getFee().equals(fee)
                 || !bItemExisting.getDx().equals(dx)
                 || (bItemExisting.getServiceDate().compareTo(serviceDate) != 0)
                 || statusChanged) {

                    BillingONRepoDao billRepoDao = (BillingONRepoDao) SpringUtils.getBean("billingONRepoDao");
                    billRepoDao.createBillingONItemEntry(bItemExisting, request.getLocale());				
                }

                
                if (!fee.equals("defunct") && !bItemExisting.getServiceCount().equals(unit)) {
                    BigDecimal feeAmt = new BigDecimal(fee);
                    BigDecimal unitAmt = new BigDecimal(unit);
                    feeAmt = feeAmt.multiply(unitAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
                    fee = feeAmt.toPlainString();
                }
                
                bItemExisting.setServiceCount(unit);
                bItemExisting.setFee(fee);
                bItemExisting.setServiceDate(bItemCurrent.getServiceDate());
                bItemExisting.setDx(dx);
                bItemExisting.setStatus(bItemCurrent.getStatus());                                             
            } 
            else {                
                // This is a new billing item that isn't already persisted.                
                bCh1.getBillingItems().add(bItemCurrent);                
            }        
        }
                
         // Update status on existing billing items now removed
         for (BillingONItem bItemExisting : bItemsExisting) {        
             
            if (!bItemsCurrent.contains(bItemExisting)){                
                bItemExisting.setStatus("D");                               
            }          
         }                        
    }
    
    private boolean hasInvoiceChanged(BillingONCHeader1 bCh1, HttpServletRequest request) {
        
        boolean isChanged = false;
  
        Locale locale = request.getLocale();
        
        String admissionDateStr = "Invalid Date";
        String billingDateStr = "Invalid Date";
      
        try {
            admissionDateStr = DateUtils.formatDate(bCh1.getAdmissionDate(), locale);
            billingDateStr = DateUtils.formatDate(bCh1.getBillingDate(), locale);         
        } catch (java.text.ParseException e) {
            MiscUtils.getLogger().warn("Invalid Date or Time",e);
        }
            
	String manualReview = request.getParameter("m_review");
        if (manualReview != null)
            manualReview = "Y";
        else
            manualReview="";                
        
        if (   !bCh1.getStatus().equals(request.getParameter("status").substring(0, 1))
            || !bCh1.getPayProgram().equals(request.getParameter("payProgram"))
            || !bCh1.getRefNum().equals(request.getParameter("rdohip"))
            || !bCh1.getVisitType().equals(request.getParameter("visittype"))            
            || !admissionDateStr.equals(request.getParameter("xml_vdate"))
            || !bCh1.getFaciltyNum().equals(request.getParameter("clinic_ref_code"))
            || !bCh1.getManReview().equals(manualReview)
            || !billingDateStr.equals(request.getParameter("xml_appointment_date"))
            || !bCh1.getComment().equals(request.getParameter("comment"))
            || !bCh1.getProviderNo().equals(request.getParameter("provider_no"))
            || !bCh1.getLocation().equals(request.getParameter("xml_slicode"))
            || !StringUtils.nullSafeEquals(bCh1.getClinic(), request.getParameter("site"))
            || !bCh1.getProvince().equals(request.getParameter("hc_type"))) {
            
            isChanged = true;
        }
        
        return isChanged;
    }
}
