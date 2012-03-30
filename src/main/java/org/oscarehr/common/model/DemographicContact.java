package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class DemographicContact extends AbstractModel<Integer> {

	//link to the provider table
	public static final int TYPE_PROVIDER = 0;
	//link to the demographic table
	public static final int TYPE_DEMOGRAPHIC = 1;
	//link to the contact table
	public static final int TYPE_CONTACT = 2;

	public static final String CATEGORY_PERSONAL = "personal";
	public static final String CATEGORY_PROFESSIONAL = "professional";


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	private boolean deleted;
	private int demographicNo;
	private String contactId;
	private String role;
	private int type;
	private String category;
	private String sdm;
	private String ec;
	private String note;

	private int facilityId;
	private String creator;


	@Transient
	private String contactName;

	@Override
	public Integer getId() {
		return this.id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}


	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCategory() {
    	return category;
    }

	public void setCategory(String category) {
    	this.category = category;
    }

	public void setId(Integer id) {
    	this.id = id;
    }


	public String getContactName() {
    	return contactName;
    }

	public void setContactName(String contactName) {
    	this.contactName = contactName;
    }

	public String getSdm() {
    	return sdm;
    }

	public void setSdm(String sdm) {
    	this.sdm = sdm;
    }

	public String getEc() {
    	return ec;
    }

	public void setEc(String ec) {
    	this.ec = ec;
    }

	public String getNote() {
    	return note;
    }

	public void setNote(String note) {
    	this.note = note;
    }



	public int getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(int facilityId) {
    	this.facilityId = facilityId;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PrePersist
	@PreUpdate
	protected void jpa_updateTimestamp() {
		this.setUpdateDate(new Date());
	}
}
