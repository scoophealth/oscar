package org.oscarehr.PMmodule.model;

import java.io.Serializable;

public class IntakeNodeJavascript implements Serializable {

	private int hashCode = Integer.MIN_VALUE;// primary key	
	private Integer id;
	private String questionId;
	private String location;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
