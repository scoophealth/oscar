package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsPID")
public class MdsPID extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="intPatientID")
	private String internalPatientId;

	@Column(name="altPatientID")
	private String alternatePatientId;

	private String patientName;

	@Column(name="dOB")
	private String dob;

	private String sex;

	private String homePhone;

	private String healthNumber;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getInternalPatientId() {
    	return internalPatientId;
    }

	public void setInternalPatientId(String internalPatientId) {
    	this.internalPatientId = internalPatientId;
    }

	public String getAlternatePatientId() {
    	return alternatePatientId;
    }

	public void setAlternatePatientId(String alternatePatientId) {
    	this.alternatePatientId = alternatePatientId;
    }

	public String getPatientName() {
    	return patientName;
    }

	public void setPatientName(String patientName) {
    	this.patientName = patientName;
    }

	public String getDob() {
    	return dob;
    }

	public void setDob(String dob) {
    	this.dob = dob;
    }

	public String getSex() {
    	return sex;
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public String getHomePhone() {
    	return homePhone;
    }

	public void setHomePhone(String homePhone) {
    	this.homePhone = homePhone;
    }

	public String getHealthNumber() {
    	return healthNumber;
    }

	public void setHealthNumber(String healthNumber) {
    	this.healthNumber = healthNumber;
    }


}
