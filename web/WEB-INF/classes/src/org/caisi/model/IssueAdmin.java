package org.caisi.model;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public class IssueAdmin extends BaseObject {
  private Long id;
  private String code;
  private String description;
  private String role;
  private Date update_date;
  private static java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-DD");
    /*
     Generate your getters and setters using your favorite IDE:
     In Eclipse:
     Right-click -> Source -> Generate Getters and Setters
    */

    public IssueAdmin() {
    	this.update_date = new Date();
    }

    public Long getId() {
	return id;
    }
    public void setId(Long id) {
	this.id = id;
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
    public String getRole() {
	return role;
    }
    public void setRole(String role) {
	this.role = role;
    } 
    public Date getUpdate_date() {
        return this.update_date;
    }
    public void setUpdate_date(Date update_date) {
    	this.update_date = update_date;       
    }
    public String getUpdate_date_web() {	
	if(update_date==null)
		return null;
	else
        	return formatter.format(update_date);
    }
    public void setUpdate_date_web(String update_date_s) {
	//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//sdf.setLenient(false);
	//SimpleDateFormat sdf = new SimpleDateFormat();
    formatter.setLenient(false);
	try{
			if(update_date_s!=null)
				this.update_date = formatter.parse(update_date_s);
			else
				this.update_date = new Date();
	    }catch(Exception e){ System.out.println("Invalid issue update date"); e.printStackTrace();}
	}
}
