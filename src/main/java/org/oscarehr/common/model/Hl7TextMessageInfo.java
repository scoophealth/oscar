/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hl7TextMessageInfo")
public class Hl7TextMessageInfo extends AbstractModel<Integer> implements Serializable {
	
	
	public String message;
	
	public int lab_no_A;
	public int lab_no_B;
	public String labDate_A;
	public String labDate_B;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public void setId(Integer id) {
	    this.id = id;
    }
	public Integer getId() {
	    return id;
    }
	public String getMessage() {
    	return message;
    }
	public void setMessage(String message) {
    	this.message = message;
    }
	public int getLab_no_A() {
    	return lab_no_A;
    }
	public void setLab_no_A(int lab_no_A) {
    	this.lab_no_A = lab_no_A;
    }
	public int getLab_no_B() {
    	return lab_no_B;
    }
	public void setLab_no_B(int lab_no_B) {
    	this.lab_no_B = lab_no_B;
    }
	public String getLabDate_A() {
    	return labDate_A;
    }
	public void setLabDate_A(String labDate_A) {
    	this.labDate_A = labDate_A;
    }
	public String getLabDate_B() {
    	return labDate_B;
    }
	public void setLabDate_B(String labDate_B) {
    	this.labDate_B = labDate_B;
    }
	
	
}
