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

package org.oscarehr.common.dao;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Repository;

import oscar.util.DateUtils;

/**
 *
 * @author mweston4
 */

@Repository
public class BillingONPaymentDao extends AbstractDao<BillingONPayment>{
    
   
    private BillingONExtDao billingONExtDao;
    
     public BillingONPaymentDao() {
        super(BillingONPayment.class);    
     }
     
    public void setBillingONExtDao(BillingONExtDao billingONExtDao) {
        this.billingONExtDao = billingONExtDao;
    }

    public BillingONExtDao getBillingONExtDao() {
        return this.billingONExtDao;
    }
    
    public List<BillingONPayment> find3rdPartyPayRecordsByBill(BillingONCHeader1 bCh1) {
        String sql = "select bPay from BillingONPayment bPay where billingNo=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());    
                 
        @SuppressWarnings("unchecked")
        List<BillingONPayment> results = query.getResultList();
                      
        Collections.sort(results, BillingONPayment.BILLING_ON_PAYMENT_COMPARATOR);
        return results;
    }
    
    public List<Integer> find3rdPartyPayments(Integer billingNo) {
    	String sql = "select bPay.paymentId from BillingONPayment bPay where bPay.billingNo=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, billingNo);    
        
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();
       
        return results;
    }
    
    
    public List<BillingONPayment> find3rdPartyPayRecordsByBill(BillingONCHeader1 bCh1, Date startDate, Date endDate) {
        String sql = "select bPay from BillingONPayment bPay where billingNo=? and payDate >= ? and payDate < ? order by payDate";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());    
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);    
         
        @SuppressWarnings("unchecked")
        List<BillingONPayment> results = query.getResultList();
                      
        Collections.sort(results, BillingONPayment.BILLING_ON_PAYMENT_COMPARATOR);
        return results;
     }
    
     public void createPayment(BillingONCHeader1 bCh1,Locale locale, String payType, BigDecimal paidAmt, String payMethod, String providerNo) {
         //add new payment
         BillingONPayment newPayment = new BillingONPayment();
         Date now = new Date();
         
         newPayment.setBillingNo(bCh1.getId());
         newPayment.setPayDate(now);
         addPaymentItems(newPayment, bCh1, locale, now, payType, paidAmt, payMethod, providerNo);         
         this.persist(newPayment);
         
         //update billing claim header's total paid amount to reflect new payment
         BigDecimal amtPaid = new BigDecimal(bCh1.getPaid());
         
        if (payType.equals("P"))
            amtPaid = amtPaid.add(paidAmt);
        else
           amtPaid = amtPaid.subtract(paidAmt);

        bCh1.setPaid(amtPaid.toPlainString());
        
        BillingONCHeader1Dao bCh1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");
        bCh1Dao.merge(bCh1);                  
     }
     
     private void addPaymentItems(BillingONPayment billingPayment, BillingONCHeader1 bCh1, Locale locale, Date payDate, String payType, BigDecimal paidAmt, String payMethod, String providerNo) {
         //add creator's providerNo
        
        BillingONExt bExt = new BillingONExt();
                
        bExt.setBillingNo(bCh1.getId());
        bExt.setDateTime(payDate);
        bExt.setDemographicNo(bCh1.getDemographicNo());
        bExt.setStatus('1');            
        bExt.setKeyVal("provider_no");
        bExt.setValue(providerNo);
        
        billingPayment.getBillingONExtItems().add(bExt);
        
        //add pay date   
        bExt = new BillingONExt();
        bExt.setBillingNo(bCh1.getId());
        bExt.setDateTime(payDate);
        bExt.setDemographicNo(bCh1.getDemographicNo());
        bExt.setStatus('1');
        bExt.setKeyVal("payDate");
        bExt.setValue(DateUtils.formatDateTime(payDate,locale));
        
        billingPayment.getBillingONExtItems().add(bExt);
        
        //add payment amount     
        bExt = new BillingONExt();
        bExt.setBillingNo(bCh1.getId());
        bExt.setDateTime(payDate);
        bExt.setDemographicNo(bCh1.getDemographicNo());
        bExt.setStatus('1');
           
        if (payType.equals("P")) {
            bExt.setKeyVal("payment");
            bExt.setValue(paidAmt.toPlainString());
        } else {
            bExt.setKeyVal("refund");
            bExt.setValue(paidAmt.toPlainString());
        }
        billingPayment.getBillingONExtItems().add(bExt);
        
        //add pay method     
        bExt = new BillingONExt();
        bExt.setBillingNo(bCh1.getId());
        bExt.setDateTime(payDate);
        bExt.setDemographicNo(bCh1.getDemographicNo());
        bExt.setStatus('1');
        bExt.setKeyVal("payMethod");
        bExt.setValue(payMethod);
        
        billingPayment.getBillingONExtItems().add(bExt);                            
     }
          
     public static BigDecimal calculatePaymentTotal(List<BillingONPayment> paymentRecords) {
        
         BigDecimal paidTotal = new BigDecimal("0.00");
         BillingONExtDao bExtDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");    
         for (BillingONPayment bPay : paymentRecords) {
                          
             BigDecimal amtPaid = bExtDao.getPayment(bPay);
             paidTotal = paidTotal.add(amtPaid);                                   
         }
         
         return paidTotal;
    }
    
    public static BigDecimal calculateRefundTotal(List<BillingONPayment> paymentRecords) {
        
         BigDecimal refundTotal = new BigDecimal("0.00");
         BillingONExtDao bExtDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");       
         for (BillingONPayment bPay : paymentRecords) {
                          
             BigDecimal amtRefunded = bExtDao.getRefund(bPay);
             refundTotal = refundTotal.add(amtRefunded);                                   
         }
         
         return refundTotal;
    }       
}
