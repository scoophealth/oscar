package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="immunizations")
public class Immunizations extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="provider_no")
	private String providerNo;

	private String immunizations;

	@Column(name="save_date")
	@Temporal(TemporalType.DATE)
	private Date saveDate;

	private int archived;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getImmunizations() {
    	return immunizations;
    }

	public void setImmunizations(String immunizations) {
    	this.immunizations = immunizations;
    }

	public Date getSaveDate() {
    	return saveDate;
    }

	public void setSaveDate(Date saveDate) {
    	this.saveDate = saveDate;
    }

	public int getArchived() {
    	return archived;
    }

	public void setArchived(int archived) {
    	this.archived = archived;
    }


}
