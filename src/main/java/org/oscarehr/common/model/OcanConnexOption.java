package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;


@Entity
public class OcanConnexOption extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String LHINCode = null;
	private String orgLHIN = null;
	private String orgName = null;
	private String orgNumber = null;
	private String programName = null;
	private String programNumber = null;
	
	public Integer getId() {
		return id;
	}

	public String getLHINCode() {
		return LHINCode;
	}

	public void setLHINCode(String lHINCode) {
		LHINCode = lHINCode;
	}

	public String getOrgLHIN() {
		return orgLHIN;
	}

	public void setOrgLHIN(String orgLHIN) {
		this.orgLHIN = orgLHIN;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgNumber() {
		return orgNumber;
	}

	public void setOrgNumber(String orgNumber) {
		this.orgNumber = orgNumber;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramNumber() {
		return programNumber;
	}

	public void setProgramNumber(String programNumber) {
		this.programNumber = programNumber;
	}
	

	public boolean equals(OcanConnexOption o) {
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
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

}
