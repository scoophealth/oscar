package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="demographicPharmacy")
public class DemographicPharmacy extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="pharmacyID")
	private int pharmacyId;

	@Column(name="demographic_no")
	private int demographicNo;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date addDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getPharmacyId() {
    	return pharmacyId;
    }

	public void setPharmacyId(int pharmacyId) {
    	this.pharmacyId = pharmacyId;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public Date getAddDate() {
    	return addDate;
    }

	public void setAddDate(Date addDate) {
    	this.addDate = addDate;
    }

	@PrePersist
	public void beforePersist() {
		addDate = new Date();
	}


}
