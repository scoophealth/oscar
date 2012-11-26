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

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="validations")
public class Validations extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(nullable=true)
	private String regularExp;
	
	@Column(nullable=true)
	private Double minValue;

	@Column(name="maxValue1",nullable=true)
	private Double maxValue;

	@Column(nullable=true)
	private Integer minLength;

	@Column(nullable=true)
	private Integer maxLength = 0;

	@Column(nullable=true)
	private Boolean isNumeric;

	@Column(nullable=true)
	private Boolean isTrue;

	@Column(nullable=true)
	private Boolean isDate;

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

	public Double getMinValue() {
    	return minValue;
    }

	public void setMinValue(Double minValue) {
    	this.minValue = minValue;
    }

	public Double getMaxValue() {
    	return maxValue;
    }

	public void setMaxValue(Double maxValue) {
    	this.maxValue = maxValue;
    }

	public Integer getMinLength() {
    	return minLength;
    }

	public void setMinLength(Integer minLength) {
    	this.minLength = minLength;
    }

	public Integer getMaxLength() {
    	return maxLength;
    }

	public void setMaxLength(Integer maxLength) {
    	this.maxLength = maxLength;
    }

	public Boolean isNumeric() {
    	return isNumeric;
    }

	public void setNumeric(Boolean isNumeric) {
    	this.isNumeric = isNumeric;
    }

	public Boolean isTrue() {
    	return isTrue;
    }

	public void setTrue(Boolean isTrue) {
    	this.isTrue = isTrue;
    }

	public Boolean isDate() {
    	return isDate;
    }

	public void setDate(Boolean isDate) {
    	this.isDate = isDate;
    }

    public static final Comparator<Validations> NameComparator = new Comparator<Validations>() {
        public int compare(Validations o1, Validations o2) {
                return o1.getName().compareTo(o2.getName());
        }
    }; 


}
