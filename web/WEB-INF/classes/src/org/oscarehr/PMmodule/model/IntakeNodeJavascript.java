package org.oscarehr.PMmodule.model;

import java.io.Serializable;

public class IntakeNodeJavascript implements Serializable {

	private int hashCode = Integer.MIN_VALUE;// primary key	
	private Integer id;
	private int intakeNodeId;
	private String location;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getIntakeNodeId() {
		return intakeNodeId;
	}
	public void setIntakeNodeId(int intakeNodeId) {
		this.intakeNodeId = intakeNodeId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
