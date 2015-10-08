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
import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.BillingOnItemPayment;
import org.springframework.stereotype.Repository;

@Repository
public class BillingOnItemPaymentDao extends AbstractDao<BillingOnItemPayment>{
	public BillingOnItemPaymentDao() {
		super(BillingOnItemPayment.class);
	}
	
	public BillingOnItemPayment findByPaymentIdAndItemId(int paymentId, int itemId) {
		Query query = entityManager.createQuery("select boip from BillingOnItemPayment boip where boip.billingOnPaymentId = ?1 and boip.billingOnItemId = ?2");
		query.setParameter(1, paymentId);
		query.setParameter(2, itemId);
		return getSingleResultOrNull(query);
	}
	
	@SuppressWarnings("unchecked")
	public List<BillingOnItemPayment> getAllByItemId(int itemId) {
		Query query = entityManager.createQuery("select boip from BillingOnItemPayment boip where boip.billingOnItemId =?1");
		query.setParameter(1, itemId);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<BillingOnItemPayment> getItemsByPaymentId(int paymentId) {
		Query query = entityManager.createQuery("select boip from BillingOnItemPayment boip where boip.billingOnPaymentId = ?1");
		query.setParameter(1, paymentId);
		return query.getResultList();
	}
	
	public BigDecimal getAmountPaidByItemId(int itemId) {
		Query query = entityManager.createQuery("select sum(boip.paid) from BillingOnItemPayment boip where boip.billingOnItemId = ?1");
		query.setParameter(1, itemId);
		BigDecimal paid = null;
		try {
			paid = (BigDecimal) query.getSingleResult();
		} catch (Exception e) {}
		
		if (paid == null) {
			paid = new BigDecimal("0.00");
		}
		
		return paid;
	}
	
    public List<BillingOnItemPayment> getItemPaymentByInvoiceNoItemId(Integer ch1_id, Integer item_id) {
        String sql = "select bPay from BillingOnItemPayment bPay where bPay.ch1Id= ?1 and bPay.billingOnItemId = ?2 ";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, ch1_id);    
        query.setParameter(2, item_id); 
        
        @SuppressWarnings("unchecked")
        List<BillingOnItemPayment> results = query.getResultList();
                              
        return results;
    }
    
	public static BigDecimal calculateItemPaymentTotal(List<BillingOnItemPayment> paymentRecords) {
	        
	     BigDecimal paidTotal = new BigDecimal("0.00");	        
	     for (BillingOnItemPayment bPay : paymentRecords) {
	         BigDecimal amtPaid = bPay.getPaid();
	         paidTotal = paidTotal.add(amtPaid);                                   
	     }
	         
	     return paidTotal;
	}
	
	public static BigDecimal calculateItemRefundTotal(List<BillingOnItemPayment> paymentRecords) {
        
        BigDecimal refundTotal = new BigDecimal("0.00");
        for (BillingOnItemPayment bPay : paymentRecords) {
       	 	BigDecimal amtRefunded = bPay.getRefund();
            refundTotal = refundTotal.add(amtRefunded);                                   
        }
        
        return refundTotal;
   }   
	
	public List<BillingOnItemPayment> findByBillingNo(int billingNo) {
		Query query = entityManager.createQuery("select boip from BillingOnItemPayment boip where boip.ch1Id = ?1");
		query.setParameter(1, billingNo);
		return query.getResultList();
	}
	
}
