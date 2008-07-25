package org.oscarehr.casemgmt.web.formbeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts.action.ActionForm;

public class OnCallQuestionnaireFormBean extends ActionForm {
	private String type_health;
	private String nurse;
	private String course_action;
	private String physician_consult;
	private String date;
	private String time;
	
	public OnCallQuestionnaireFormBean() {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat t = new SimpleDateFormat("kk:mm");
		
		date = d.format(new Date());
		time = t.format(new Date());
	}
	
	public Date getDateObject() throws ParseException {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return d.parse(getDate().trim() + " " + getTime().trim());
	}

	public String getType_health() {
		return type_health;
	}

	public void setType_health(String type_health) {
		this.type_health = type_health;
	}

	public String getNurse() {
		return nurse;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

	public String getCourse_action() {
		return course_action;
	}

	public void setCourse_action(String course_action) {
		this.course_action = course_action;
	}

	public String getPhysician_consult() {
		return physician_consult;
	}

	public void setPhysician_consult(String physician_consult) {
		this.physician_consult = physician_consult;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
