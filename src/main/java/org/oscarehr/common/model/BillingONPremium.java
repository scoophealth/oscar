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
import javax.persistence.*;
import java.util.Date;
/**
 *
 * @author mweston4
 */
@Entity
@Table(name="billing_on_premium", 
       uniqueConstraints=@UniqueConstraint(columnNames={"raheader_no","providerohip_no"}))
public class BillingONPremium extends AbstractModel<Integer> implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "premium_id", nullable = false)
    private Integer id;

    @Column(name = "raheader_no", nullable = false)    
    private Integer raHeaderNo;
    
    @Column(name = "provider_no", nullable = true)
    private String providerNo;
    
    @Column(name = "providerohip_no", nullable = false)
    private String providerOHIPNo;
    
    @Column(name = "pay_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date payDate;
    
    @Column(name = "amount_pay", nullable = false)
    private String amountPay;
    
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column(name = "creator", nullable = false)
    private String creator;
    
    @Column(name = "status", nullable = false)
    private Boolean status = false;
    
    @Override
    public Integer getId() {
            return id;
    }
    
    public Integer getRAHeaderNo() { return this.raHeaderNo; }
    public void setRAHeaderNo(Integer raHeaderNo) { this.raHeaderNo = raHeaderNo; }
    
    public String getProviderNo() { return this.providerNo; }
    public void setProviderNo(String providerNo) { this.providerNo = providerNo; }
    
    public String getProviderOHIPNo() { return this.providerOHIPNo; }
    public void setProviderOHIPNo(String providerOHIPNo) { this.providerOHIPNo = providerOHIPNo; }
    
    public Date getPayDate() { return this.payDate; }
    public void setPayDate(Date payDate) { this.payDate = payDate; }
    
    public String getAmountPay() { return this.amountPay; }
    public void setAmountPay(String amountPay) { this.amountPay = amountPay; }      
    
    public Boolean getStatus() { return this.status; }
    public void setStatus(Boolean status) { this.status = status; }
    
    public Date getCreateDate() { return this.createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public String getCreator() { return this.creator; }
    public void setCreator(String creator) { this.creator = creator; }           
}
