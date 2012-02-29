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
