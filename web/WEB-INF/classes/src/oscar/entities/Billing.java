/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package oscar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Encapsulates data from table billing
 *
 */
@Entity
@Table(name = "billing")
public class Billing {

    /**
     * auto_increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "billing_no")
    private int billingNo;
    @Column(name = "clinic_no")
    private int clinicNo;
    @Column(name = "demographic_no")
    private int demographicNo;
    @Column(name = "provider_no")
    private String providerNo;
    @Column(name = "appointment_no")
    private int appointmentNo;
    @Column(name = "organization_spec_code")
    private String organizationSpecCode;
    @Column(name = "demographic_name")
    private String demographicName;
    private String hin;
    @Column(name = "update_date")
    private String updateDate;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "billing_date")
    private String billingDate;
    @Column(name = "billing_time")
    private String billingTime;
    @Column(name = "clinic_ref_code")
    private String clinicRefCode;
    private String content;
    private String total;
    private String status;
    private String dob;
    private String visitdate;
    private String visittype;
    @Column(name = "provider_ohip_no")
    private String providerOhipNo;
    @Column(name = "provider_rma_no")
    private String providerRmaNo;
    @Column(name = "apptProvider_no")
    private String apptProviderNo;
    @Column(name = "asstProvider_no")
    private String asstProviderNo;
    @Column(name = "creator")
    private String creator;
    @Column(name = "billingtype")
    private String billingtype;

    /**
     * Class constructor with no arguments.
     */
    public Billing() {
    }

    /**
     * Full constructor
     *
     * @param billingNo int
     * @param clinicNo int
     * @param demographicNo int
     * @param providerNo String
     * @param appointmentNo int
     * @param organizationSpecCode String
     * @param demographicName String
     * @param hin String
     * @param updateDate String
     * @param updateTime String
     * @param billingDate String
     * @param billingTime String
     * @param clinicRefCode String
     * @param content String
     * @param total String
     * @param status String
     * @param dob String
     * @param visitdate String
     * @param visittype String
     * @param providerOhipNo String
     * @param providerRmaNo String
     * @param apptProviderNo String
     * @param asstProviderNo String
     * @param creator String
     * @param billingtype String
     */
    public Billing(int billingNo, int clinicNo, int demographicNo,
            String providerNo, int appointmentNo,
            String organizationSpecCode, String demographicName,
            String hin, String updateDate, String updateTime,
            String billingDate, String billingTime,
            String clinicRefCode, String content, String total,
            String status, String dob, String visitdate,
            String visittype, String providerOhipNo, String providerRmaNo,
            String apptProviderNo, String asstProviderNo, String creator,
            String billingtype) {
        this.billingNo = billingNo;
        this.clinicNo = clinicNo;
        this.demographicNo = demographicNo;
        this.providerNo = providerNo;
        this.appointmentNo = appointmentNo;
        this.organizationSpecCode = organizationSpecCode;
        this.demographicName = demographicName;
        this.hin = hin;
        this.updateDate = updateDate;
        this.updateTime = updateTime;
        this.billingDate = billingDate;
        this.billingTime = billingTime;
        this.clinicRefCode = clinicRefCode;
        this.content = content;
        this.total = total;
        this.status = status;
        this.dob = dob;
        this.visitdate = visitdate;
        this.visittype = visittype;
        this.providerOhipNo = providerOhipNo;
        this.providerRmaNo = providerRmaNo;
        this.apptProviderNo = apptProviderNo;
        this.asstProviderNo = asstProviderNo;
        this.creator = creator;
        this.billingtype = billingtype;
    }

    /**
     * Gets the billingNo
     * @return int billingNo
     */
    public int getBillingNo() {
        return billingNo;
    }

    /**
     * Gets the clinicNo
     * @return int clinicNo
     */
    public int getClinicNo() {
        return clinicNo;
    }

    /**
     * Gets the demographicNo
     * @return int demographicNo
     */
    public int getDemographicNo() {
        return demographicNo;
    }

    /**
     * Gets the providerNo
     * @return String providerNo
     */
    public String getProviderNo() {
        return (providerNo != null ? providerNo : "");
    }

    /**
     * Gets the appointmentNo
     * @return int appointmentNo
     */
    public int getAppointmentNo() {
        return appointmentNo;
    }

    /**
     * Gets the organizationSpecCode
     * @return String organizationSpecCode
     */
    public String getOrganizationSpecCode() {
        return (organizationSpecCode != null ? organizationSpecCode : "");
    }

    /**
     * Gets the demographicName
     * @return String demographicName
     */
    public String getDemographicName() {
        return (demographicName != null ? demographicName : "");
    }

    /**
     * Gets the hin
     * @return String hin
     */
    public String getHin() {
        return (hin != null ? hin : "");
    }

    /**
     * Gets the updateDate
     * @return String updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Gets the updateTime
     * @return String updateTime
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Gets the billingDate
     * @return String billingDate
     */
    public String getBillingDate() {
        return billingDate;
    }

    /**
     * Gets the billingTime
     * @return String billingTime
     */
    public String getBillingTime() {
        return billingTime;
    }

    /**
     * Gets the clinicRefCode
     * @return String clinicRefCode
     */
    public String getClinicRefCode() {
        return (clinicRefCode != null ? clinicRefCode : "");
    }

    /**
     * Gets the content
     * @return String content
     */
    public String getContent() {
        return (content != null ? content : "");
    }

    /**
     * Gets the total
     * @return String total
     */
    public String getTotal() {
        return (total != null ? total : "");
    }

    /**
     * Gets the status
     * @return String status
     */
    public String getStatus() {
        return (status != null ? status : "");
    }

    /**
     * Gets the dob
     * @return String dob
     */
    public String getDob() {
        return (dob != null ? dob : "");
    }

    /**
     * Gets the visitdate
     * @return String visitdate
     */
    public String getVisitdate() {
        return visitdate;
    }

    /**
     * Gets the visittype
     * @return String visittype
     */
    public String getVisittype() {
        return (visittype != null ? visittype : "");
    }

    /**
     * Gets the providerOhipNo
     * @return String providerOhipNo
     */
    public String getProviderOhipNo() {
        return (providerOhipNo != null ? providerOhipNo : "");
    }

    /**
     * Gets the providerRmaNo
     * @return String providerRmaNo
     */
    public String getProviderRmaNo() {
        return (providerRmaNo != null ? providerRmaNo : "");
    }

    /**
     * Gets the apptProviderNo
     * @return String apptProviderNo
     */
    public String getApptProviderNo() {
        return (apptProviderNo != null ? apptProviderNo : "");
    }

    /**
     * Gets the asstProviderNo
     * @return String asstProviderNo
     */
    public String getAsstProviderNo() {
        return (asstProviderNo != null ? asstProviderNo : "");
    }

    /**
     * Gets the creator
     * @return String creator
     */
    public String getCreator() {
        return (creator != null ? creator : "");
    }

    /**
     * Gets the billingtype
     * @return String billingtype
     */
    public String getBillingtype() {
        return (billingtype != null ? billingtype : "");
    }

    /**
     * Sets the billingNo
     * @param billingNo int
     */
    public void setBillingNo(int billingNo) {
        this.billingNo = billingNo;
    }

    /**
     * Sets the clinicNo
     * @param clinicNo int
     */
    public void setClinicNo(int clinicNo) {
        this.clinicNo = clinicNo;
    }

    /**
     * Sets the demographicNo
     * @param demographicNo int
     */
    public void setDemographicNo(int demographicNo) {
        this.demographicNo = demographicNo;
    }

    /**
     * Sets the providerNo
     * @param providerNo String
     */
    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    /**
     * Sets the appointmentNo
     * @param appointmentNo int
     */
    public void setAppointmentNo(int appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    /**
     * Sets the organizationSpecCode
     * @param organizationSpecCode String
     */
    public void setOrganizationSpecCode(String organizationSpecCode) {
        this.organizationSpecCode = organizationSpecCode;
    }

    /**
     * Sets the demographicName
     * @param demographicName String
     */
    public void setDemographicName(String demographicName) {
        this.demographicName = demographicName;
    }

    /**
     * Sets the hin
     * @param hin String
     */
    public void setHin(String hin) {
        this.hin = hin;
    }

    /**
     * Sets the updateDate
     * @param updateDate String
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Sets the updateTime
     * @param updateTime String
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Sets the billingDate
     * @param billingDate String
     */
    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    /**
     * Sets the billingTime
     * @param billingTime String
     */
    public void setBillingTime(String billingTime) {
        this.billingTime = billingTime;
    }

    /**
     * Sets the clinicRefCode
     * @param clinicRefCode String
     */
    public void setClinicRefCode(String clinicRefCode) {
        this.clinicRefCode = clinicRefCode;
    }

    /**
     * Sets the content
     * @param content String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets the total
     * @param total String
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * Sets the status
     * @param status String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the dob
     * @param dob String
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * Sets the visitdate
     * @param visitdate String
     */
    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    /**
     * Sets the visittype
     * @param visittype String
     */
    public void setVisittype(String visittype) {
        this.visittype = visittype;
    }

    /**
     * Sets the providerOhipNo
     * @param providerOhipNo String
     */
    public void setProviderOhipNo(String providerOhipNo) {
        this.providerOhipNo = providerOhipNo;
    }

    /**
     * Sets the providerRmaNo
     * @param providerRmaNo String
     */
    public void setProviderRmaNo(String providerRmaNo) {
        this.providerRmaNo = providerRmaNo;
    }

    /**
     * Sets the apptProviderNo
     * @param apptProviderNo String
     */
    public void setApptProviderNo(String apptProviderNo) {
        this.apptProviderNo = apptProviderNo;
    }

    /**
     * Sets the asstProviderNo
     * @param asstProviderNo String
     */
    public void setAsstProviderNo(String asstProviderNo) {
        this.asstProviderNo = asstProviderNo;
    }

    /**
     * Sets the creator
     * @param creator String
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Sets the billingtype
     * @param billingtype String
     */
    public void setBillingtype(String billingtype) {
        this.billingtype = billingtype;
    }
}


