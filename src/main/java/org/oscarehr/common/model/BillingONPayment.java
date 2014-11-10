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

package org.oscarehr.common.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.*;
import java.util.Date;
/**
 *
 * @author mweston4
 */

@Entity
@Table(name = "billing_on_payment")
public class BillingONPayment extends AbstractModel<Integer> implements Serializable {
        public static String PAYMENT = "P";
        public static String REFUND = "R";
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "payment_id", nullable = false)
        private Integer id;
        
        @Column(name = "billing_no", nullable = false)
        private Integer billingNo;
        
        @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
        @JoinColumn(name="payment_id", referencedColumnName="payment_id")
        private List<BillingONExt> billingONExtItems = new ArrayList<BillingONExt>();
        
        @Column(name = "pay_date", insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
        private Date payDate; 
        
        @Column(name="payment_id", updatable=false, insertable=false)
        private Integer paymentId;
        
        @Override
	public Integer getId() {
		return id;
	}
        
        public Integer getBillingNo() {
		return billingNo;
	}
        
        public void setBillingNo(Integer billingNo) {
            this.billingNo = billingNo;
        }
                        
        public List<BillingONExt> getBillingONExtItems() {
            return this.billingONExtItems;
        }
               
        public Date getPayDate() {
            return this.payDate;
        }
        
        public void setPayDate(Date payDate) {
            this.payDate = payDate;
        }
        
        
        public Integer getPaymentId() {
			return paymentId;
		}

		public void setPaymentId(Integer paymentId) {
			this.paymentId = paymentId;
		}
		
		public void setId(Integer id) {
			this.id = id;
		}

		@PostPersist
        public void postPersist() {            
            for (BillingONExt bExt : this.billingONExtItems) {
                bExt.setPaymentId(this.id);
            }            
        }                
        
        /**
	 * This comparator sorts EncounterForm ascending based on the formName
	 */
	public static final Comparator<BillingONPayment> BILLING_ON_PAYMENT_COMPARATOR = new Comparator<BillingONPayment>() {
		public int compare(BillingONPayment p1, BillingONPayment p2) {
			return (p1.getPayDate().compareTo(p2.getPayDate()));
		}
	};      
}
