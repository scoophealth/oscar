package com.quatro.model.security;

/**
 * Secobjectname entity.
 * 
 * @author JZhang
 */

public class Secobjectname implements java.io.Serializable {

	// Fields

	private String objectname;
	private String description;
	private Integer orgapplicable;

	// Constructors

	/** default constructor */
	public Secobjectname() {
	}

	/** minimal constructor */
	public Secobjectname(String objectname) {
		this.objectname = objectname;
	}

	/** full constructor */
	public Secobjectname(String objectname, String description,
			Integer orgapplicable) {
		this.objectname = objectname;
		this.description = description;
		this.orgapplicable = orgapplicable;
	}

	// Property accessors

	public String getObjectname() {
		return this.objectname;
	}

	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrgapplicable() {
		return this.orgapplicable;
	}

	public void setOrgapplicable(Integer orgapplicable) {
		this.orgapplicable = orgapplicable;
	}

}