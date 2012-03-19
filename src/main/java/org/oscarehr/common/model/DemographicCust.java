package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="demographiccust")
public class DemographicCust extends AbstractModel<Integer>{

	@Id
	@Column(name="demographic_no")
	private Integer id;

	@Column(name="cust1")
	private String nurse;

	@Column(name="cust2")
	private String resident;

	@Column(name="cust3")
	private String alert;

	@Column(name="cust4")
	private String midwife;

	@Column(name="content")
	private String notes;

	public Integer getId() {
		return id;
	}

	public String getNurse() {
    	return nurse;
    }

	public void setNurse(String nurse) {
    	this.nurse = nurse;
    }

	public String getResident() {
    	return resident;
    }

	public void setResident(String resident) {
    	this.resident = resident;
    }

	public String getAlert() {
    	return alert;
    }

	public void setAlert(String alert) {
    	this.alert = alert;
    }

	public String getMidwife() {
    	return midwife;
    }

	public void setMidwife(String midwife) {
    	this.midwife = midwife;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }

	public void setId(Integer id) {
    	this.id = id;
    }




}
