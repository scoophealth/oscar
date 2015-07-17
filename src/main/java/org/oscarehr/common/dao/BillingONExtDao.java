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
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONPayment;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Repository;
/**
 *
 * @author mweston4
 */
@Repository
@SuppressWarnings("unchecked")
public class BillingONExtDao extends AbstractDao<BillingONExt>{
	public final static String KEY_PAYMENT = "payment";
	public final static String KEY_REFUND = "refund";
	public final static String KEY_DISCOUNT = "discount";
	public final static String KEY_CREDIT = "credit";
	public final static String KEY_PAY_DATE = "payDate";
	public final static String KEY_PAY_METHOD = "payMethod";
	public final static String KEY_TOTAL = "total";
	public final static String KEY_GST = "gst";
	
    public BillingONExtDao() {
        super(BillingONExt.class);
    }
    
	public List<BillingONExt> find(String key, String value) {
		Query q = createQuery("q", "q.keyVal = :key AND q.value = :value");
		q.setParameter("key", key);
		q.setParameter("value", value);
		return q.getResultList();
	}

    
    public List<BillingONExt> findByBillingNoAndKey(Integer billingNo, String key) {
    	String sql = "select bExt from BillingONExt bExt where bExt.billingNo=? and bExt.keyVal=? order by bExt.id DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, billingNo);
        query.setParameter(2, key);       

        List<BillingONExt> results = query.getResultList();
        
        return results;
    }
    
    public List<BillingONExt> findByBillingNoAndPaymentIdAndKey(Integer billingNo, Integer paymentId, String key) {
    	String sql = "select bExt from BillingONExt bExt where bExt.billingNo=? and bExt.paymentId=? and bExt.keyVal=? order by bExt.id DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, billingNo);
        query.setParameter(2, paymentId);
        query.setParameter(3, key);
        List<BillingONExt> results = query.getResultList();
        
        return results;
    }
    
            
    public String getPayMethodDesc(BillingONExt bExt) {
        BillingPaymentTypeDao payMethod = SpringUtils.getBean(BillingPaymentTypeDao.class);
        Integer payMethodId = Integer.parseInt(bExt.getValue());
        BillingPaymentType payMethodDesc= payMethod.find(payMethodId);
        return payMethodDesc.getPaymentType();
    }
    
    public BigDecimal getPayment(BillingONPayment paymentRecord) {
        
        String sql = "select bExt from BillingONExt bExt where billingNo=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        
        query.setParameter(1, paymentRecord.getBillingNo());       
        query.setParameter(2, "payment");
         
        List<BillingONExt> results = query.getResultList();
        
        BigDecimal amtPaid = null;
        if (results.size() > 1) {
            MiscUtils.getLogger().warn("Multiple payments found for Payment Id:" + paymentRecord.getId());
        }
        
        if (results.isEmpty()) {
            amtPaid = new BigDecimal("0.00");
        } 
        else {
            BillingONExt payment = results.get(0);
            try {
                amtPaid = new BigDecimal(payment.getValue());
            } catch (NumberFormatException e) {
                MiscUtils.getLogger().warn("Payment not a valid currency amount (" + payment.getValue() + ") for Payment Id:" + paymentRecord.getId());
                amtPaid = new BigDecimal("0.00");
            }
        }
        return amtPaid;
    }
    
    public BigDecimal getRefund(BillingONPayment paymentRecord) {
        String sql = "select bExt from BillingONExt bExt where paymentId=? and billingNo=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, paymentRecord.getId());
        query.setParameter(2, paymentRecord.getBillingNo());       
        query.setParameter(3, "refund");
         
        List<BillingONExt> results = query.getResultList();
        
        BigDecimal amtRefunded = null;
        if (results.size() > 1) {
              MiscUtils.getLogger().warn("Multiple payments found for Payment Id:" + paymentRecord.getId());
        } 
        
        if (results.isEmpty()) {
            amtRefunded = new BigDecimal("0.00");
        } else {
            BillingONExt refund = results.get(0);
            try {
                amtRefunded = new BigDecimal(refund.getValue());
            } catch (NumberFormatException e) {
                MiscUtils.getLogger().warn("Refund not a valid currency amount (" + refund.getValue() + ") for Payment Id:" + paymentRecord.getId());
                amtRefunded = new BigDecimal("0.00");
            }
        }
        return amtRefunded;
    }
    
    public BillingONExt getRemitTo(BillingONCHeader1 bCh1) {
        BillingONExt bExt = null;
        
        String sql = "select bExt from BillingONExt bExt where billingNo=? and status=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());
        query.setParameter(2, '1');
        query.setParameter(3, "remitTo");
         
        List<BillingONExt> results = query.getResultList();
        
        if (results.size() > 1) {
            MiscUtils.getLogger().warn("More than one active remit to result for invoice number: " + bCh1.getId());
        }
        
        if (!results.isEmpty())
            bExt = results.get(0);
        return bExt;
    }
    
    public BillingONExt getBillTo(BillingONCHeader1 bCh1) {
        BillingONExt bExt = null;
        
        String sql = "select bExt from BillingONExt bExt where billingNo=? and status=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());
        query.setParameter(2, '1');
        query.setParameter(3, "billTo");
         
        List<BillingONExt> results = query.getResultList();
        
        if (results.size() > 1) {
            MiscUtils.getLogger().warn("More than one active bill to result for invoice number: " + bCh1.getId());
        }
        
        if (!results.isEmpty())
            bExt = results.get(0);
        return bExt;
    }
    
    public BillingONExt getBillToInactive(BillingONCHeader1 bCh1) {
        BillingONExt bExt = null;
        
        String sql = "select bExt from BillingONExt bExt where billingNo=? and status=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());
        query.setParameter(2, '0');
        query.setParameter(3, "billTo");
         
        List<BillingONExt> results = query.getResultList();
        
        if (results.size() > 1) {
            MiscUtils.getLogger().warn("More than one inactive bill to result for invoice number: " + bCh1.getId());
        }
        
        if (!results.isEmpty())
            bExt = results.get(0);
        return bExt;
    }
    
    public BillingONExt getDueDate(BillingONCHeader1 bCh1) {
	BillingONExt bExt = null;
	       
	String sql = "select bExt from BillingONExt bExt where billingNo=? and status=? and keyVal=?";
	Query query = entityManager.createQuery(sql);
	query.setParameter(1, bCh1.getId());
	query.setParameter(2, '1');
	query.setParameter(3, "dueDate");
	         
	List<BillingONExt> results = query.getResultList();
	       
	if (results.size() > 1) {
            MiscUtils.getLogger().warn("More than one active dueDate result for invoice number: " + bCh1.getId());
	}
	       
	if (!results.isEmpty()) {
            bExt = results.get(0);
	}
	       
        return bExt;
    }
    
    public BillingONExt getUseBillTo(BillingONCHeader1 bCh1) {
        BillingONExt bExt = null;
        
        String sql = "select bExt from BillingONExt bExt where billingNo=? and status=? and keyVal=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, bCh1.getId());
        query.setParameter(2, '1');
        query.setParameter(3, "useBillTo");
         
        List<BillingONExt> results = query.getResultList();
        
        if (results.size() > 1) {
            MiscUtils.getLogger().warn("More than one active useBillTo result for invoice number: " + bCh1.getId());
        }
        
        if (!results.isEmpty()) {
            bExt = results.get(0);
        }
        
        return bExt;
    }
 
    public List<BillingONExt> find(Integer billingNo, String key, Date start, Date end) {
		Query q = createQuery("b", "b.billingNo = :bNo AND b.keyVal = :key AND b.dateTime >= :start AND b.dateTime <= :end");
		q.setParameter("bNo", billingNo);
		q.setParameter("key", key);
		q.setParameter("start", start);
		q.setParameter("end", end);
	    return q.getResultList();
    }
    
   public List<BillingONExt> findByBillingNoAndPaymentNo(int billingNo, int paymentId) {
        
        String sql = "select bExt from BillingONExt bExt where bExt.paymentId=? and bExt.billingNo=?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, paymentId);
        query.setParameter(2, billingNo);       
         
        List<BillingONExt> results = query.getResultList();
        
        return results;
    }
    
    public List<BillingONExt> getClaimExtItems(int billingNo){
        Query query = entityManager.createQuery("select ext from BillingONExt ext where ext.billingNo = :billingNo");
        query.setParameter("billingNo", billingNo);
        return query.getResultList();
    }
    
    public List<BillingONExt> getBillingExtItems(String billingNo){
        Query query = entityManager.createQuery("select ext from BillingONExt ext where ext.billingNo = :billingNo and ext.status='1' ");
        try {
        	query.setParameter("billingNo", Integer.parseInt(billingNo));
        	return query.getResultList();
        } catch (Exception e) {
        	return null;
        }
    }
    
    public List<BillingONExt> getInactiveBillingExtItems(String billingNo){
        Query query = entityManager.createQuery("select ext from BillingONExt ext where ext.billingNo = :billingNo and ext.status='0' ");
        try {
        	query.setParameter("billingNo", Integer.parseInt(billingNo));
        	return query.getResultList();
        } catch (Exception e) {
        	return null;
        }
    }
    
    public BigDecimal getAccountVal(int billingNo, String key) {
    	BigDecimal val = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
    	if (!KEY_TOTAL.equals(key) && !KEY_PAYMENT.equals(key) && !KEY_DISCOUNT.equals(key) && !KEY_REFUND.equals(key) && !KEY_CREDIT.equals(key)) {
    		return val;
    	}
    	Query query = entityManager.createQuery("select ext from BillingONExt ext where ext.billingNo = ?1 and ext.keyVal = ?2");
    	query.setParameter(1, billingNo);
    	query.setParameter(2, key);
    	BillingONExt ext = null;
    	try {
    		ext = (BillingONExt) query.getSingleResult();
    	} catch (Exception e) {}
    	if (ext != null) {
    		val = new BigDecimal(ext.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    	}
    	return val;
    }
    public BillingONExt getClaimExtItem(Integer billingNo, Integer demographicNo, String keyVal) throws NonUniqueResultException {
    	String filter1 = (billingNo == null ? "" : "ext.billingNo = :billingNo");
    	String filter2 = (demographicNo == null ? "" : "ext.demographicNo = :demographicNo");
    	String filter3 = (keyVal == null ? "" : "ext.keyVal = :keyVal");
    	String sql = "select ext from BillingONExt ext";
    	boolean isWhere = false;
    	if(filter1 != null) {
    		sql += " where ext.billingNo = :billingNo";
    		isWhere = true;
    	}
    	if(filter2 != null) {
    		if(isWhere) sql += " and demographicNo = :demographicNo";
    		else {
    			sql += "where demographicNo = :demographicNo";
    			isWhere = true;
    		}
    	}
    	if(filter3 != null) {
    		if(isWhere) sql += " and keyVal = :keyVal";
    		else {
    			sql += "where keyVal = :keyVal";
    			isWhere = true;
    		}
    	}
    	Query query = entityManager.createQuery(sql);
        if(filter1 != null) query.setParameter("billingNo", billingNo);
        if(filter1 != null) query.setParameter("demographicNo", demographicNo);
        if(filter3 != null) query.setParameter("keyVal", keyVal);
        BillingONExt res = null;
        try {
        	res = (BillingONExt)query.getSingleResult();
        } catch (NoResultException ex) {
        	return null;
        } 
        return res;
    }

    public void setExtItem(int billingNo, int demographicNo, String keyVal, String value, Date dateTime, char status) throws NonUniqueResultException {
    	BillingONExt ext = getClaimExtItem(billingNo, demographicNo, keyVal);
    	if(ext != null) {
    		ext.setValue(value);
    		ext.setDateTime(dateTime);
    		ext.setStatus(status);
    		this.merge(ext);
    	} else {
    		BillingONExt res = new BillingONExt();
    		res.setBillingNo(billingNo);
    		res.setDemographicNo(demographicNo);
    		res.setKeyVal(keyVal);
    		res.setValue(value);
    		res.setDateTime(dateTime);
    		res.setStatus(status);
    		this.persist(res);
    	}
    }
    public static boolean isNumberKey(String key) {
    	if (KEY_PAYMENT.equalsIgnoreCase(key) 
    			|| KEY_DISCOUNT.equalsIgnoreCase(key) 
    			|| KEY_TOTAL.equalsIgnoreCase(key) 
    			|| KEY_REFUND.equalsIgnoreCase(key)
    			|| KEY_CREDIT.equals(key)) {
    		return true;
    	}
    	return false;
    }
}
