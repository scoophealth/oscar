/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="validations")
public class Validations extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String regularExp;

	private double minValue;

	private double maxValue;

	private int minLength;

	private int maxLength;

	private boolean isNumeric;

	private boolean isTrue;

	private boolean isDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getRegularExp() {
    	return regularExp;
    }

	public void setRegularExp(String regularExp) {
    	this.regularExp = regularExp;
    }

	public double getMinValue() {
    	return minValue;
    }

	public void setMinValue(double minValue) {
    	this.minValue = minValue;
    }

	public double getMaxValue() {
    	return maxValue;
    }

	public void setMaxValue(double maxValue) {
    	this.maxValue = maxValue;
    }

	public int getMinLength() {
    	return minLength;
    }

	public void setMinLength(int minLength) {
    	this.minLength = minLength;
    }

	public int getMaxLength() {
    	return maxLength;
    }

	public void setMaxLength(int maxLength) {
    	this.maxLength = maxLength;
    }

	public boolean isNumeric() {
    	return isNumeric;
    }

	public void setNumeric(boolean isNumeric) {
    	this.isNumeric = isNumeric;
    }

	public boolean isTrue() {
    	return isTrue;
    }

	public void setTrue(boolean isTrue) {
    	this.isTrue = isTrue;
    }

	public boolean isDate() {
    	return isDate;
    }

	public void setDate(boolean isDate) {
    	this.isDate = isDate;
    }


}
