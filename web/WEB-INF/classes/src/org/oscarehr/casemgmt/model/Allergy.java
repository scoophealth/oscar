package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class Allergy extends BaseObject {
	private Long allergyid;
	private String demographic_no;
	private Date entry_date;
	private String description;
	private String reaction;
	
	
	public Long getAllergyid() {
		return allergyid;
	}
	public void setAllergyid(Long allergyid) {
		this.allergyid = allergyid;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public String getReaction()
	{
		return reaction;
	}
	public void setReaction(String reaction)
	{
		this.reaction = reaction;
	}
	
}
