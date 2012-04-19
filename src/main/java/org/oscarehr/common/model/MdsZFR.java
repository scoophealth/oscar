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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZFR")
public class MdsZFR extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String reportForm;

	private String reportFormStatus;

	private String testingLab;

	private String medicalDirector;

	private String editFlag;

	private String abnormalFlag;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getReportForm() {
    	return reportForm;
    }

	public void setReportForm(String reportForm) {
    	this.reportForm = reportForm;
    }

	public String getReportFormStatus() {
    	return reportFormStatus;
    }

	public void setReportFormStatus(String reportFormStatus) {
    	this.reportFormStatus = reportFormStatus;
    }

	public String getTestingLab() {
    	return testingLab;
    }

	public void setTestingLab(String testingLab) {
    	this.testingLab = testingLab;
    }

	public String getMedicalDirector() {
    	return medicalDirector;
    }

	public void setMedicalDirector(String medicalDirector) {
    	this.medicalDirector = medicalDirector;
    }

	public String getEditFlag() {
    	return editFlag;
    }

	public void setEditFlag(String editFlag) {
    	this.editFlag = editFlag;
    }

	public String getAbnormalFlag() {
    	return abnormalFlag;
    }

	public void setAbnormalFlag(String abnormalFlag) {
    	this.abnormalFlag = abnormalFlag;
    }


}
