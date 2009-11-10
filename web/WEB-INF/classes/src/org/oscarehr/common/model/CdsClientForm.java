package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents every time a provider fills out or updates a CDS form.
 * Note that every row / entry represents a change, so as an example if 
 * one provider answers the first 4 questions, then saves it. It will make a entry.
 * If another provider then updates it to answer another 2 questions, this should
 * make a 2nd row. This allows us to track who changed what on the form and when. 
 * As a result, these entities are non delete/update able, the expectation is to
 * make a new entity instead of updating an existing one.
 */
@Entity
public class CdsClientForm extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String cdsFormVersion=null;
	private String providerNo = null;
	private boolean signed=false;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	
	private Integer facilityId=null;
	private Integer clientId=null;
	private Integer admissionId=null;
	private Integer clientAge=null;
	
	public Integer getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public String getCdsFormVersion() {
    	return cdsFormVersion;
    }

	public void setCdsFormVersion(String cdsFormVersion) {
    	this.cdsFormVersion = cdsFormVersion;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public boolean isSigned() {
    	return signed;
    }

	public void setSigned(boolean signed) {
    	this.signed = signed;
    }

	public Integer getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public Integer getClientId() {
    	return clientId;
    }

	public void setClientId(Integer clientId) {
    	this.clientId = clientId;
    }

	public Integer getAdmissionId() {
    	return admissionId;
    }

	public void setAdmissionId(Integer admissionId) {
    	this.admissionId = admissionId;
    }

	public Integer getClientAge() {
    	return clientAge;
    }

	public void setClientAge(Integer clientAge) {
    	this.clientAge = clientAge;
    }

	public boolean equals(CdsClientForm o) {
		try {
			return (id != null && id.intValue() == o.id.intValue());
		} catch (Exception e) {
			return (false);
		}
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}
	
	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

}
