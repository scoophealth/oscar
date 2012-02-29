package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="clinic_location")
public class ClinicLocation extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="clinic_location_no")
	private String clinicLocationNo;

	@Column(name="clinic_no")
	private int clinicNo;

	@Column(name="clinic_location_name")
	private String clinicLocationName;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getClinicLocationNo() {
    	return clinicLocationNo;
    }

	public void setClinicLocationNo(String clinicLocationNo) {
    	this.clinicLocationNo = clinicLocationNo;
    }

	public int getClinicNo() {
    	return clinicNo;
    }

	public void setClinicNo(int clinicNo) {
    	this.clinicNo = clinicNo;
    }

	public String getClinicLocationName() {
    	return clinicLocationName;
    }

	public void setClinicLocationName(String clinicLocationName) {
    	this.clinicLocationName = clinicLocationName;
    }


}
