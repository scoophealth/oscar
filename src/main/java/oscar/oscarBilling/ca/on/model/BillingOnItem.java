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


package oscar.oscarBilling.ca.on.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class BillingOnItem implements Serializable {
    private Integer id;
    private Integer ch1_id;
    private String  transc_id;
    private String  rec_id;
    private String  service_code;
    private String  fee;
    private String  ser_num;
    private Date    service_date;
    private String  dx;
    private String  dx1;
    private String  dx2;
    private String  status;
    private Timestamp timestamp;

    public Integer getId() {
        return this.id;
    }

    public Integer getCh1_id() {
        return this.ch1_id;
    }

    public String getTransc_id() {
        return this.transc_id;
    }

    public String getRec_id() {
        return this.rec_id;
    }

    public String getService_code() {
        return this.service_code;
    }

    public String getFee() {
        return this.fee;
    }

    public String getSer_num() {
        return this.ser_num;
    }

    public Date getService_date() {
        return this.service_date;
    }

    public String getDx() {
        return this.dx;
    }

    public String getDx1() {
        return this.dx1;
    }

    public String getDx2() {
        return this.dx2;
    }

    public String getStatus() {
        return this.status;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setId(Integer i) {
        this.id = i;
    }

    public void setCh1_id(Integer i) {
        this.ch1_id = i;
    }

    public void setTransc_id(String s) {
        this.transc_id = s;
    }

    public void setRec_id(String s) {
        this.rec_id = s;
    }

    public void setService_code(String s) {
        this.service_code = s;
    }

    public void setFee(String s) {
        this.fee = s;
    }

    public void setSer_num(String s) {
        this.ser_num = s;
    }

    public void setService_date(Date d) {
        this.service_date = d;
    }

    public void setDx(String s) {
        this.dx = s;
    }

    public void setDx1(String s) {
        this.dx1 = s;
    }

    public void setDx2(String s) {
        this.dx2 = s;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public void setTimestamp(Timestamp t) {
        this.timestamp = t;
    }
}
