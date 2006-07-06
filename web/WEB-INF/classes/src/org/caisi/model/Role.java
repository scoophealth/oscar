package org.caisi.model;

import java.util.Date;

public class Role extends BaseObject {
	private Long id;
	private String name="";
	private String oscar_name;
	private Date update_date;
	
	public Role() {
		update_date = new Date();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getOscar_name() {
		return oscar_name;
	}
	public void setOscar_name(String oscar_name) {
		this.oscar_name = oscar_name;
	}
	
}
