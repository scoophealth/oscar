package org.caisi.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class Issue extends BaseObject {
	private Long id;
	private String code;
	private String description;
	private String role;
	private Date update_date;
	
	public Issue() {
		update_date = new Date();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
}
