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
package org.oscarehr.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.oscarehr.common.model.BillingONItem;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author mweston4
 */
@Service
public class BillingONService {
    
    @Autowired
    BillingONCHeader1Dao billingONCHeader1Dao;
    
    public List<BillingONItem> getNonDeletedInvoices(Integer invoiceNo) {
        BillingONCHeader1 billingONCHeader1 = billingONCHeader1Dao.find(invoiceNo);
        List<BillingONItem> billingItems = billingONCHeader1.getBillingItems();
        List<BillingONItem>tempItems = new ArrayList<BillingONItem>();
        
        for (BillingONItem b : billingItems) {
                if( !b.getStatus().equals("D")) {
                        tempItems.add(b);
                }    
        }

        return tempItems;
    }
    
    public BigDecimal calculateBalanceOwing(Integer invoiceNo, BigDecimal paidTotal, BigDecimal refundTotal) {
        BillingONCHeader1 billingONCHeader1 = billingONCHeader1Dao.find(invoiceNo);
        BigDecimal billTotal = new BigDecimal(billingONCHeader1.getTotal());
        billTotal = billTotal.movePointLeft(2);

        return billTotal.subtract(paidTotal).add(refundTotal);
    }
    
     public boolean updateTotal(Integer invoiceNo) {           
            BillingONCHeader1 billingONCHeader1 = billingONCHeader1Dao.find(invoiceNo);
            boolean isUpdated = true;
            
            // Update bill total to equal the sum of the billing item fees.             
            BigDecimal feeTotal = new BigDecimal("0.00");
            List<BillingONItem> billingONItems = billingONCHeader1.getBillingItems();
            if (billingONItems != null) {
                for (BillingONItem bItem : billingONItems) {
                    if (!bItem.getStatus().equals(BillingONItem.DELETED)) {
                        String feeStr = bItem.getFee();
                        try {
                            BigDecimal fee = new BigDecimal(feeStr);
                            feeTotal = feeTotal.add(fee);
                        } catch (NumberFormatException e) {
                            isUpdated = false;
                            MiscUtils.getLogger().error("Fee not valid amount:" + feeStr, e);
                        }                    
                    }
                }
            }
            else {
                isUpdated = false;
            }

            BigDecimal currentTotal = new BigDecimal("0.00");
            try {    
                Long total = billingONCHeader1.getTotal();                
                if (total != null) {
                    currentTotal = new BigDecimal(total);
                    currentTotal = currentTotal.movePointLeft(2);
                }                
            }
            catch (NumberFormatException e) {
                isUpdated = false;
                MiscUtils.getLogger().error("Invalid bill total:", e);
            } 

            if (isUpdated && (currentTotal.compareTo(feeTotal) != 0)) {
                    feeTotal = feeTotal.movePointRight(2);
                    billingONCHeader1.setTotal(feeTotal.longValue());
            } 
            
            return isUpdated;
        }
}
