package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsPV1")
public class MdsPV1 extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String patientClass;

	private String patientLocation;

	private String refDoctor;

	private String conDoctor;

	private String admDoctor;

	private String vNumber;

	private String accStatus;

	private String admDateTime;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getPatientClass() {
    	return patientClass;
    }

	public void setPatientClass(String patientClass) {
    	this.patientClass = patientClass;
    }

	public String getPatientLocation() {
    	return patientLocation;
    }

	public void setPatientLocation(String patientLocation) {
    	this.patientLocation = patientLocation;
    }

	public String getRefDoctor() {
    	return refDoctor;
    }

	public void setRefDoctor(String refDoctor) {
    	this.refDoctor = refDoctor;
    }

	public String getConDoctor() {
    	return conDoctor;
    }

	public void setConDoctor(String conDoctor) {
    	this.conDoctor = conDoctor;
    }

	public String getAdmDoctor() {
    	return admDoctor;
    }

	public void setAdmDoctor(String admDoctor) {
    	this.admDoctor = admDoctor;
    }

	public String getvNumber() {
    	return vNumber;
    }

	public void setvNumber(String vNumber) {
    	this.vNumber = vNumber;
    }

	public String getAccStatus() {
    	return accStatus;
    }

	public void setAccStatus(String accStatus) {
    	this.accStatus = accStatus;
    }

	public String getAdmDateTime() {
    	return admDateTime;
    }

	public void setAdmDateTime(String admDateTime) {
    	this.admDateTime = admDateTime;
    }


}
