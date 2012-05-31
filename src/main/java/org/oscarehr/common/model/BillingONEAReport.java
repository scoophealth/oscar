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
@Table(name="billing_on_eareport")
public class BillingONEAReport extends AbstractModel<Integer> implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "providerohip_no")    
    private String providerOHIPNo;
    
    @Column(name = "group_no")
    private String groupNo = "0000";
    
    @Column(name = "specialty")
    private String specialty = "00";
    
    @Column(name = "process_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processDate;
    
    @Column(name = "hin")
    private String hin;
    
    @Column(name = "ver")
    private String version;
    
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    
    @Column(name="billing_no")
    private Integer billingNo;
    
    @Column(name="ref_no")
    private String refNo;  
    
    @Column(name="facility")
    private String facility;
    
    @Column(name="admitted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date admittedDate;
    
    @Column(name="claim_error")
    private String claimError;
    
    @Column(name="code")
    private String code;
    
    @Column(name="fee")
    private String fee;
    
    @Column (name="unit")
    private String unit;
    
    @Column (name="code_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date codeDate;
    
    @Column(name="dx")
    private String dx;
    
    @Column(name="exp")
    private String exp;
    
    @Column(name="code_error")
    private String codeError;
    
    @Column(name="report_name")
    private String reportName="";
    
    @Column(name="status")
    private Character status='N';
    
    @Column(name="comment")
    private String comment;
            
    @Override
    public Integer getId() {
        return this.id;
    }
    
    public String getProviderOHIPNo() {
        return this.providerOHIPNo;
    }
    
    public void setProviderOHIPNo(String providerOHIPNo) {
        this.providerOHIPNo = providerOHIPNo;
    }
    
    public String getGroupNo() {
        return this.groupNo;
    }
    
    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
    
    public String getSpecialty() {
        return this.specialty;       
    }
    
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    
    public Date getProcessDate() {
        return this.processDate;
    }
    
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }
    
    public String getHin() {
        return this.hin;
    }
    
    public void setHin(String hin){
        this.hin = hin;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Date getDob() {
        return this.dob;
    }
    
    public void setDob(Date dob){
        this.dob = dob;
    }
    
    public Integer getBillingNo() {
        return this.billingNo;
    }
    
    public void setBillingNo(Integer billingNo) {
        this.billingNo = billingNo;
    }
    
    public String getRefNo() {
        return this.refNo;
    }
    
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
    
    public String getFacility() {
        return this.facility;
    }
    
    public void setFacility(String facility) {
        this.facility = facility;
    }
    
    public Date getAdmittedDate() {
        return this.admittedDate;
    }
    
    public void setAdmittedDate(Date admittedDate) {
        this.admittedDate = admittedDate;
    }
    
    public String getClaimError() {
        return this.claimError;
    }
    
    public void setClaimError(String claimError) {
        this.claimError = claimError;
    }
    
    public String getCode() {
        return this.code;        
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getFee() {
        return this.fee;
    }
    
    public void setFee(String fee) {
        this.fee = fee;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Date getCodeDate() {
        return this.codeDate;
    }
    
    public void setCodeDate(Date codeDate) {
        this.codeDate = codeDate;
    }
    
    public String getDx() {
        return this.dx;
    }
    
    public void setDx(String dx) {
        this.dx = dx;
    }
    
    public String getExp() {
        return this.exp;
    }
    
    public void setExp(String exp) {
        this.exp = exp;
    }
    
    public String getCodeError() {
        return this.codeError;
    }
    
    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }
    
    public String getReportName() {
        return this.reportName;
    }
    
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    
    public Character getStatus() {
        return this.status;
    }
    
    public void setStatus(Character status){
        this.status = status;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
                
}
