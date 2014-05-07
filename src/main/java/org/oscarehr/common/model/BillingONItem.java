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
import javax.persistence.*;

/**
 *
 * @author mweston4
 */
@Entity
@Table(name = "billing_on_item")
public class BillingONItem extends AbstractModel<Integer> implements Serializable{
    
    public static final String DELETED = "D";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "ch1_id")
    private Integer ch1Id;
    
    @Column(name = "transc_id")
    private String transcId;
    
    @Column(name = "rec_id")
    private String recId;
    
    @Column(name = "service_code")
    private String serviceCode;
     
    @Column(name = "fee") 
    private String fee;
    
    @Column(name = "ser_num")
    private String serviceCount;
    
    @Temporal(TemporalType.DATE)
    @Column(name="service_date")
    private Date serviceDate;
    
    @Column(name = "dx")
    private String dx;
    
    @Column(name = "dx1")
    private String dx1;
    
    @Column(name = "dx2")
    private String dx2;
    
    @Column(name = "status")
    private String status;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date lastEditDT;

    @Override
    public Integer getId() {
        return id;
    }
    
    public String getTranscId() {
        return transcId;
    }

    public void setTranscId(String transcId) {
        this.transcId = transcId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getDx1() {
        return dx1;
    }

    public void setDx1(String dx1) {
        this.dx1 = dx1;
    }

    public String getDx2() {
        return dx2;
    }

    public void setDx2(String dx2) {
        this.dx2 = dx2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastEditDT() {
        return lastEditDT;
    }

    public void setLastEditDT(Date lastEditDT) {
        this.lastEditDT = lastEditDT;
    }

    public Integer getCh1Id() {
        return ch1Id;
    }

    public void setCh1Id(Integer ch1Id) {
        this.ch1Id = ch1Id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillingONItem item = (BillingONItem) o;

        if ((id != null) && (item.getId() != null))
            return id.equals(item.getId()); 
        else
            return ch1Id.equals(item.getCh1Id()) && this.serviceCode.equals(item.getServiceCode());
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }        
    
    @PrePersist
    @PreUpdate
    protected void jpa_updateTimestamp() {
    	this.lastEditDT = new Date();
    }
}
