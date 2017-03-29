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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
	public static final String PAYMENT = "P";
	public static final String REFUND = "R";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "payment_id", nullable = false)
	private Integer id;
    
        @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
        @JoinColumn(name="payment_id", referencedColumnName="payment_id")
        private List<BillingONExt> billingONExtItems = new ArrayList<BillingONExt>();

	private BigDecimal total_payment = new BigDecimal("0.00");
	
	@Column(name="pay_date", nullable=false)
	private Date paymentdate;
	
	private BigDecimal total_refund = new BigDecimal("0.00");
	
	private BigDecimal total_credit = new BigDecimal("0.00");
	
	private BigDecimal total_discount = new BigDecimal("0.00");
	
	private static final long serialVersionUID = 1L;
      
	@Column(name="billing_no")
	private Integer billingNo;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="billing_no", nullable=false, insertable=false, updatable=false)	
	private BillingONCHeader1 billingONCheader1;	
	
	private String creator;
	private Integer paymentTypeId; 

	public int getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public BillingONPayment() {
		super();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return this.paymentdate;
	}

	public void setPaymentDate(Date paymentdate) {
		this.paymentdate = paymentdate;
	}
 
	public String getPaymentDateFormatted() {
		return this.paymentdate != null ? new SimpleDateFormat("yyyy-MM-dd").format(this.paymentdate) : "---";
	}
  	
	public BigDecimal getTotal_payment() {
		return total_payment;
	}

	public void setTotal_payment(BigDecimal total_payment) {
		this.total_payment = total_payment;
	}

	public BigDecimal getTotal_refund() {
		return total_refund;
	}

	public void setTotal_refund(BigDecimal total_refund) {
		this.total_refund = total_refund;
	}

	public BigDecimal getTotal_discount() {
		return total_discount;
	}

	public void setTotal_discount(BigDecimal total_discount) {
		this.total_discount = total_discount;
	}

	public BigDecimal getTotal_credit() {
		return total_credit;
	}

	public void setTotal_credit(BigDecimal total_credit) {
		this.total_credit = total_credit;
	}

        

                        
        public List<BillingONExt> getBillingONExtItems() {
            return this.billingONExtItems;
        }
               

		@PostPersist
        public void postPersist() {            
            for (BillingONExt bExt : this.billingONExtItems) {
                bExt.setPaymentId(this.id);
            }            
        }                
	
		public BillingONCHeader1 getBillingONCheader1() {
	        return billingONCheader1;
        }

		public void setBillingOnCheader1(BillingONCHeader1 billingOnCheader1) {
	        this.billingONCheader1 = billingOnCheader1;
	       
        }

		public Integer getBillingNo() {
	        return billingNo;
        }

		public void setBillingNo(Integer billingNo) {
	        this.billingNo = billingNo;
        }

		/**
	 * This comparator sorts EncounterForm ascending based on the formName
	 */
	public static final Comparator<BillingONPayment> BILLING_ON_PAYMENT_COMPARATOR = new Comparator<BillingONPayment>() {
		public int compare(BillingONPayment p1, BillingONPayment p2) {
			return (p1.getPaymentDate().compareTo(p2.getPaymentDate()));
		}
	};      
}
