package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="secObjectName")
public class SecObjectName extends AbstractModel<String> {

	@Id
	@Column(name="objectName")
	private String id;
	private String description;
	@Column(name="orgapplicable")
	private boolean orgApplicable;

	public String getId() {
    	return id;
    }
	public void setId(String id) {
    	this.id = id;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }
	public boolean isOrgApplicable() {
    	return orgApplicable;
    }
	public void setOrgApplicable(boolean orgApplicable) {
    	this.orgApplicable = orgApplicable;
    }


}
