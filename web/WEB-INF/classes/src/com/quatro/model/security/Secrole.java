package com.quatro.model.security;

import java.util.Calendar;


public class Secrole implements java.io.Serializable {

	// Fields

	
	private String roleName;
	private String description;
	private boolean active;   
	private String lastUpdateUser;
	private Calendar lastUpdateDate;
	
    private int orderByIndex;

	// Constructors

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Calendar getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Calendar lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public int getOrderByIndex() {
		return orderByIndex;
	}

	public void setOrderByIndex(int orderByIndex) {
		this.orderByIndex = orderByIndex;
	}

	/** default constructor */
	public Secrole() {
	}

	/** full constructor */
	public Secrole(String roleName, String description) {
		this.roleName = roleName;
		this.description = description;
	}

	// Property accessors

	

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}