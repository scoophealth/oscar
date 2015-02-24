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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="billing_on_ext")
public class BillingONExt extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="key_val")
	private String keyVal;

	private String value;

	@Column(name="date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

    @Column(name="status")
    private char status = '1';
        
    @Column(name="payment_id")
    private Integer paymentId;     	     
        
        @Override
	public Integer getId() {
            return id;
        }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getBillingNo() {
            return billingNo;
        }

	public void setBillingNo(int billingNo) {
            this.billingNo = billingNo;
        }

	public int getDemographicNo() {
            return demographicNo;
        }
        
	public void setDemographicNo(int demographicNo) {
            this.demographicNo = demographicNo;
        }

	public String getKeyVal() {
            return keyVal;
        }

	public void setKeyVal(String keyVal) {
            this.keyVal = keyVal;
        }

	public String getValue() {
            return value;
        }

	public void setValue(String value) {
            this.value = value;
        }

	public Date getDateTime() {
            return dateTime;
        }

	public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

	public char getStatus() {
            return status;
        }

	public void setStatus(char status) {
            this.status = status;
        }
        
	 public Integer getPaymentId() {
         return this.paymentId;
     }
     
     public void setPaymentId(Integer paymentId) {
         this.paymentId = paymentId;
     }  
}
