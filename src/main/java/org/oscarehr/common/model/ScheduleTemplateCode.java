package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="scheduletemplatecode")
public class ScheduleTemplateCode extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Character code;
	private String description;
	private String duration;
	private String color;
	private String confirm;
	private int bookinglimit;
	
	@Override
	public Integer getId() {
		return id;
	}

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getDuration() {
    	return duration;
    }

	public void setDuration(String duration) {
    	this.duration = duration;
    }

	public String getColor() {
    	return color;
    }

	public void setColor(String color) {
    	this.color = color;
    }

	public String getConfirm() {
    	return confirm;
    }

	public void setConfirm(String confirm) {
    	this.confirm = confirm;
    }

	public int getBookinglimit() {
    	return bookinglimit;
    }

	public void setBookinglimit(int bookinglimit) {
    	this.bookinglimit = bookinglimit;
    }

	public Character getCode() {
    	return code;
    }

	public void setCode(Character code) {
    	this.code = code;
    }
	
	
}
