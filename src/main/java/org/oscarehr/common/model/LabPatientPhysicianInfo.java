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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="labPatientPhysicianInfo")
public class LabPatientPhysicianInfo extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="labReportInfo_id")
	private int labReportInfoId;

	@Column(name="accession_num")
	private String accessionNum;

	@Column(name="physician_account_num")
	private String physicianAccountNum;

	@Column(name="service_date")
	private String serviceDate;

	@Column(name="patient_first_name")
	private String patientFirstName;

	@Column(name="patient_last_name")
	private String patientLastName;

	@Column(name="patient_sex")
	private String patientSex;

	@Column(name="patient_health_num")
	private String patientHin;

	@Column(name="patient_dob")
	private String patientDob;

	@Column(name="lab_status")
	private String labStatus;

	@Column(name="doc_num")
	private String docNum;

	@Column(name="doc_name")
	private String docName;

	@Column(name="doc_addr1")
	private String docAddress1;

	@Column(name="doc_addr2")
	private String docAddress2;

	@Column(name="doc_addr3")
	private String docAddress3;

	@Column(name="doc_postal")
	private String docPostal;

	@Column(name="doc_route")
	private String docRoute;

	private String comment1;

	private String comment2;

	@Column(name="patient_phone")
	private String patientPhone;

	@Column(name="doc_phone")
	private String docPhone;

	@Column(name="collection_date")
	private String collectionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getLabReportInfoId() {
    	return labReportInfoId;
    }

	public void setLabReportInfoId(int labReportInfoId) {
    	this.labReportInfoId = labReportInfoId;
    }

	public String getAccessionNum() {
    	return accessionNum;
    }

	public void setAccessionNum(String accessionNum) {
    	this.accessionNum = accessionNum;
    }

	public String getPhysicianAccountNum() {
    	return physicianAccountNum;
    }

	public void setPhysicianAccountNum(String physicianAccountNum) {
    	this.physicianAccountNum = physicianAccountNum;
    }

	public String getServiceDate() {
    	return serviceDate;
    }

	public void setServiceDate(String serviceDate) {
    	this.serviceDate = serviceDate;
    }

	public String getPatientFirstName() {
    	return patientFirstName;
    }

	public void setPatientFirstName(String patientFirstName) {
    	this.patientFirstName = patientFirstName;
    }

	public String getPatientLastName() {
    	return patientLastName;
    }

	public void setPatientLastName(String patientLastName) {
    	this.patientLastName = patientLastName;
    }

	public String getPatientSex() {
    	return patientSex;
    }

	public void setPatientSex(String patientSex) {
    	this.patientSex = patientSex;
    }

	public String getPatientHin() {
    	return patientHin;
    }

	public void setPatientHin(String patientHin) {
    	this.patientHin = patientHin;
    }

	public String getPatientDob() {
    	return patientDob;
    }

	public void setPatientDob(String patientDob) {
    	this.patientDob = patientDob;
    }

	public String getLabStatus() {
    	return labStatus;
    }

	public void setLabStatus(String labStatus) {
    	this.labStatus = labStatus;
    }

	public String getDocNum() {
    	return docNum;
    }

	public void setDocNum(String docNum) {
    	this.docNum = docNum;
    }

	public String getDocName() {
    	return docName;
    }

	public void setDocName(String docName) {
    	this.docName = docName;
    }

	public String getDocAddress1() {
    	return docAddress1;
    }

	public void setDocAddress1(String docAddress1) {
    	this.docAddress1 = docAddress1;
    }

	public String getDocAddress2() {
    	return docAddress2;
    }

	public void setDocAddress2(String docAddress2) {
    	this.docAddress2 = docAddress2;
    }

	public String getDocAddress3() {
    	return docAddress3;
    }

	public void setDocAddress3(String docAddress3) {
    	this.docAddress3 = docAddress3;
    }

	public String getDocPostal() {
    	return docPostal;
    }

	public void setDocPostal(String docPostal) {
    	this.docPostal = docPostal;
    }

	public String getDocRoute() {
    	return docRoute;
    }

	public void setDocRoute(String docRoute) {
    	this.docRoute = docRoute;
    }

	public String getComment1() {
    	return comment1;
    }

	public void setComment1(String comment1) {
    	this.comment1 = comment1;
    }

	public String getComment2() {
    	return comment2;
    }

	public void setComment2(String comment2) {
    	this.comment2 = comment2;
    }

	public String getPatientPhone() {
    	return patientPhone;
    }

	public void setPatientPhone(String patientPhone) {
    	this.patientPhone = patientPhone;
    }

	public String getDocPhone() {
    	return docPhone;
    }

	public void setDocPhone(String docPhone) {
    	this.docPhone = docPhone;
    }

	public String getCollectionDate() {
    	return collectionDate;
    }

	public void setCollectionDate(String collectionDate) {
    	this.collectionDate = collectionDate;
    }

	public String getPatientFullName() {
		return getPatientLastName() + ", " + getPatientFirstName();
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@PrePersist
	@PreUpdate
	protected void jpaUpdateDate() {
		this.lastUpdateDate = new Date();
	}
}
