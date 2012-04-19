/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.billing.CA.ON.model;

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

import org.oscarehr.common.model.AbstractModel;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name = "billing_on_item")
public class BillingItem  extends AbstractModel<Integer> implements Serializable {

    private Integer id;
    private Integer ch1_id;
    private String transc_id;
    private String rec_id;
    private String service_code;
    private String fee;
    private String ser_num;
    private Date service_date;
    private String dx;
    private String dx1;
    private String dx2;
    private String status;
    private Date timestamp;

    @Override
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the transc_id
     */
    @Column(length=2)
    public String getTransc_id() {
        return transc_id;
    }

    /**
     * @param transc_id the transc_id to set
     */
    public void setTransc_id(String transc_id) {
        this.transc_id = transc_id;
    }

    /**
     * @return the rec_id
     */
    @Column(length=1)
    public String getRec_id() {
        return rec_id;
    }

    /**
     * @param rec_id the rec_id to set
     */
    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    /**
     * @return the service_code
     */
    public String getService_code() {
        return service_code;
    }

    /**
     * @param service_code the service_code to set
     */
    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    /**
     * @return the fee
     */
    public String getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(String fee) {
        this.fee = fee;
    }

    /**
     * @return the ser_num
     */
    @Column(length=2)
    public String getSer_num() {
        return ser_num;
    }

    /**
     * @param ser_num the ser_num to set
     */
    public void setSer_num(String ser_num) {
        this.ser_num = ser_num;
    }

    /**
     * @return the service_date
     */
    @Temporal(TemporalType.DATE)
    public Date getService_date() {
        return service_date;
    }

    /**
     * @param service_date the service_date to set
     */
    public void setService_date(Date service_date) {
        this.service_date = service_date;
    }

    /**
     * @return the dx
     */
    @Column(length=4)
    public String getDx() {
        return dx;
    }

    /**
     * @param dx the dx to set
     */
    public void setDx(String dx) {
        this.dx = dx;
    }

    /**
     * @return the dx1
     */
    @Column(length=4)
    public String getDx1() {
        return dx1;
    }

    /**
     * @param dx1 the dx1 to set
     */
    public void setDx1(String dx1) {
        this.dx1 = dx1;
    }

    /**
     * @return the dx2
     */
    @Column(length=4)
    public String getDx2() {
        return dx2;
    }

    /**
     * @param dx2 the dx2 to set
     */
    public void setDx2(String dx2) {
        this.dx2 = dx2;
    }

    /**
     * @return the status
     */
    @Column(length=1)
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the timestamp
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the ch1_id
     */
    public Integer getCh1_id() {
        return ch1_id;
    }

    /**
     * @param ch1_id the ch1_id to set
     */
    public void setCh1_id(Integer ch1_id) {
        this.ch1_id = ch1_id;
    }

}
