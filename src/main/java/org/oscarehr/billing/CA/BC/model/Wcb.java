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
package org.oscarehr.billing.CA.BC.model;

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

@Entity
@Table(name="wcb")
public class Wcb extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="billing_no")
	private int billingNo;
	
	@Column(name="demographic_no")
	private int demographicNo;
	
	//TODO: change to string
	@Column(name="provider_no")
	private int providerNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date formCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date formEdited;
	
	@Column(name="w_reporttype")
	private String reportType;
	
	@Column(name="bill_amount")
	private String billAmount;
	
	@Column(name="w_fname")
	private String fName;
	
	@Column(name="w_lname")
	private String lName;
	
	@Column(name="w_mname")
	private String mName;
	
	@Column(name="w_gender")
	private String gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name="w_dob")
	private Date dob;
	
	@Column(name="w_address")
	private String address;
	
	@Column(name="w_city")
	private String city;
	
	@Column(name="w_postal")
	private String postal;
	
	@Column(name="w_area")
	private String area;
	
	@Column(name="w_phone")
	private String phone;
	
	@Column(name="w_phn")
	private String phn;
	
	@Column(name="w_empname")
	private String empName;
	
	@Column(name="w_emparea")
	private String empArea;
	
	@Column(name="w_empphone")
	private String empPhone;
	
	@Column(name="w_wcbno")
	private String wcbNo;
	
	@Column(name="w_opaddress")
	private String opAddress;
	
	@Column(name="w_opcity")
	private String opCity;
	
	@Column(name="w_rphysician")
	private String rPhysician;
	
	@Column(name="w_duration")
	private int duration;
	
	@Column(name="w_problem")
	private String problem;
	
	@Temporal(TemporalType.DATE)
	@Column(name="w_servicedate")
	private Date serviceDate;
	
	@Column(name="w_diagnosis")
	private String diagnosis;
	
	@Column(name="w_icd9")
	private String icd9;
	
	@Column(name="w_bp")
	private String bp;
	
	@Column(name="w_side")
	private String side;
	
	@Column(name="w_noi")
	private String noi;
	
	@Column(name="w_work")
	private String work;
	
	@Temporal(TemporalType.DATE)
	@Column(name="w_workdate")
	private Date workDate;
	
	@Column(name="w_clinicinfo")
	private String clinicInfo;
	
	@Column(name="w_capability")
	private String capability;
	
	@Column(name="w_capreason")
	private String capReason;
	
	@Column(name="w_estimate")
	private String estimate;
	
	@Column(name="w_rehab")
	private String rehab;
	
	@Column(name="w_rehabtype")
	private String rehabType;
	
	@Column(name="w_wcbadvisor")
	private String wcbAdbvisor;
	
	@Column(name="w_ftreatment")
	private String fTreatment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="w_estimatedate")
	private Date estimateDate;
	
	@Column(name="w_tofollow")
	private String toFollow;
	
	@Column(name="w_payeeno")
	private String payeeNo;
	
	@Column(name="w_pracno")
	private String pracNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="w_doi")
	private Date doi;
	
	private String status;
	
	@Column(name="w_feeitem")
	private String feeItem;
	
	@Column(name="w_extrafeeitem")
	private String extraFeeItem;
	
	@Column(name="w_servicelocation")
	private String serviceLocation;
	
	private int formNeeded;

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

	public int getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(int providerNo) {
		this.providerNo = providerNo;
	}

	public Date getFormCreated() {
		return formCreated;
	}

	public void setFormCreated(Date formCreated) {
		this.formCreated = formCreated;
	}

	public Date getFormEdited() {
		return formEdited;
	}

	public void setFormEdited(Date formEdited) {
		this.formEdited = formEdited;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpArea() {
		return empArea;
	}

	public void setEmpArea(String empArea) {
		this.empArea = empArea;
	}

	public String getEmpPhone() {
		return empPhone;
	}

	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}

	public String getWcbNo() {
		return wcbNo;
	}

	public void setWcbNo(String wcbNo) {
		this.wcbNo = wcbNo;
	}

	public String getOpAddress() {
		return opAddress;
	}

	public void setOpAddress(String opAddress) {
		this.opAddress = opAddress;
	}

	public String getOpCity() {
		return opCity;
	}

	public void setOpCity(String opCity) {
		this.opCity = opCity;
	}

	public String getrPhysician() {
		return rPhysician;
	}

	public void setrPhysician(String rPhysician) {
		this.rPhysician = rPhysician;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getIcd9() {
		return icd9;
	}

	public void setIcd9(String icd9) {
		this.icd9 = icd9;
	}

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getNoi() {
		return noi;
	}

	public void setNoi(String noi) {
		this.noi = noi;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getClinicInfo() {
		return clinicInfo;
	}

	public void setClinicInfo(String clinicInfo) {
		this.clinicInfo = clinicInfo;
	}

	public String getCapability() {
		return capability;
	}

	public void setCapability(String capability) {
		this.capability = capability;
	}

	public String getCapReason() {
		return capReason;
	}

	public void setCapReason(String capReason) {
		this.capReason = capReason;
	}

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getRehab() {
		return rehab;
	}

	public void setRehab(String rehab) {
		this.rehab = rehab;
	}

	public String getRehabType() {
		return rehabType;
	}

	public void setRehabType(String rehabType) {
		this.rehabType = rehabType;
	}

	public String getWcbAdbvisor() {
		return wcbAdbvisor;
	}

	public void setWcbAdbvisor(String wcbAdbvisor) {
		this.wcbAdbvisor = wcbAdbvisor;
	}

	public String getfTreatment() {
		return fTreatment;
	}

	public void setfTreatment(String fTreatment) {
		this.fTreatment = fTreatment;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getToFollow() {
		return toFollow;
	}

	public void setToFollow(String toFollow) {
		this.toFollow = toFollow;
	}

	public String getPayeeNo() {
		return payeeNo;
	}

	public void setPayeeNo(String payeeNo) {
		this.payeeNo = payeeNo;
	}

	public String getPracNo() {
		return pracNo;
	}

	public void setPracNo(String pracNo) {
		this.pracNo = pracNo;
	}

	public Date getDoi() {
		return doi;
	}

	public void setDoi(Date doi) {
		this.doi = doi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(String feeItem) {
		this.feeItem = feeItem;
	}

	public String getExtraFeeItem() {
		return extraFeeItem;
	}

	public void setExtraFeeItem(String extraFeeItem) {
		this.extraFeeItem = extraFeeItem;
	}

	public String getServiceLocation() {
		return serviceLocation;
	}

	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	public int getFormNeeded() {
		return formNeeded;
	}

	public void setFormNeeded(int formNeeded) {
		this.formNeeded = formNeeded;
	}
	
	
	
	
}
