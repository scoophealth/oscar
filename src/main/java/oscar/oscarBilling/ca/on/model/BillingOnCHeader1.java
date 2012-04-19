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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class BillingOnCHeader1 implements Serializable {
    private Integer id;
    private Integer header_id;
    private String transc_id;
    private String rec_id;
    private String hin;
    private String ver;
    private String dob;
    private String pay_program;
    private String payee;
    private String ref_num;
    private String facilty_num;
    private String admission_date;
    private String ref_lab_num;
    private String man_review;
    private String location;
    private Integer demographic_no;
    private String provider_no;
    private Integer appointment_no;
    private String demographic_name;
    private String sex;
    private String province;
    private Date billing_date;
    private Time billing_time;
    private String total;
    private String paid;
    private String status;
    private String comment1;
    private String visittype;
    private String provider_ohip_no;
    private String provider_rma_no;
    private String apptProvider_no;
    private String asstProvider_no;
    private String creator;
    private Timestamp timestamp1;
    private String clinic;

    public Integer getId() {
        return this.id;
    }
    public Integer getHeader_id() {
        return this.header_id;
    }
    public String getTransc_id() {
        return this.transc_id;
    }
    public String getRec_id() {
        return this.rec_id;
    }
    public String getHin() {
        return this.hin;
    }
    public String getVer() {
        return this.ver;
    }
    public String getDob() {
        return this.dob;
    }
    public String getPay_program() {
        return this.pay_program;
    }
    public String getPayee() {
        return this.payee;
    }
    public String getRef_num() {
        return this.ref_num;
    }
    public String getFacilty_num() {
        return this.facilty_num;
    }
    public String getAdmission_date() {
        return this.admission_date;
    }
    public String getRef_lab_num() {
        return this.ref_lab_num;
    }
    public String getMan_review() {
        return this.man_review;
    }
    public String getLocation() {
        return this.location;
    }
    public Integer getDemographic_no() {
        return this.demographic_no;
    }
    public String getProvider_no() {
        return this.provider_no;
    }
    public Integer getAppointment_no() {
        return this.appointment_no;
    }
    public String getDemographic_name() {
        return this.demographic_name;
    }
    public String getSex() {
        return this.sex;
    }
    public String getProvince() {
        return this.province;
    }
    public Date getBilling_date() {
        return this.billing_date;
    }
    public Time getBilling_time() {
        return this.billing_time;
    }
    public String getTotal() {
        return this.total;
    }
    public String getPaid() {
        return this.paid;
    }
    public String getStatus() {
        return this.status;
    }
    public String getComment1() {
        return this.comment1;
    }
    public String getVisittype() {
        return this.visittype;
    }
    public String getProvider_ohip_no() {
        return this.provider_ohip_no;
    }
    public String getProvider_rma_no() {
        return this.provider_rma_no;
    }
    public String getApptProvider_no() {
        return this.apptProvider_no;
    }
    public String getAsstProvider_no() {
        return this.asstProvider_no;
    }
    public String getCreator() {
        return this.creator;
    }
    public Timestamp getTimestamp1() {
        return this.timestamp1;
    }
    public String getClinic() {
        return this.clinic;
    }

    public void setId(Integer i) {
        this.id = i;
    }
    public void setHeader_id(Integer i) {
        this.header_id = i;
    }
    public void setTransc_id(String s) {
        this.transc_id = s;
    }
    public void setRec_id(String s) {
        this.rec_id = s;
    }
    public void setHin(String s) {
        this.hin = s;
    }
    public void setVer(String s) {
        this.ver = s;
    }
    public void setDob(String s) {
        this.dob = s;
    }
    public void setPay_program(String s) {
        this.pay_program = s;
    }
    public void setPayee(String s) {
        this.payee = s;
    }
    public void setRef_num(String s) {
        this.ref_num = s;
    }
    public void setFacilty_num(String s) {
        this.facilty_num = s;
    }
    public void setAdmission_date(String s) {
        this.admission_date = s;
    }
    public void setRef_lab_num(String s) {
        this.ref_lab_num = s;
    }
    public void setMan_review(String s) {
        this.man_review = s;
    }
    public void setLocation(String s) {
        this.location = s;
    }
    public void setDemographic_no(Integer i) {
        this.demographic_no = i;
    }
    public void setProvider_no(String s) {
        this.provider_no = s;
    }
    public void setAppointment_no(Integer i) {
        this.appointment_no = i;
    }
    public void setDemographic_name(String s) {
        this.demographic_name = s;
    }
    public void setSex(String s) {
        this.sex = s;
    }
    public void setProvince(String s) {
        this.province = s;
    }
    public void setBilling_date(Date d) {
        this.billing_date = d;
    }
    public void setBilling_time(Time t) {
        this.billing_time = t;
    }
    public void setTotal(String s) {
        this.total = s;
    }
    public void setPaid(String s) {
        this.paid = s;
    }
    public void setStatus(String s) {
        this.status = s;
    }
    public void setComment1(String s) {
        this.comment1 = s;
    }
    public void setVisittype(String s) {
        this.visittype = s;
    }
    public void setProvider_ohip_no(String s) {
        this.provider_ohip_no = s;
    }
    public void setProvider_rma_no(String s) {
        this.provider_rma_no = s;
    }
    public void setApptProvider_no(String s) {
        this.apptProvider_no = s;
    }
    public void setAsstProvider_no(String s) {
        this.asstProvider_no = s;
    }
    public void setCreator(String s) {
        this.creator = s;
    }
    public void setTimestamp1(Timestamp t) {
        this.timestamp1 = t;
    }
    public void setClinic(String s) {
        this.clinic = s;
    }
}
