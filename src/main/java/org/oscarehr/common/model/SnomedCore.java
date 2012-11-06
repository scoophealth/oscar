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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SnomedCore extends AbstractCodeSystemModel<Integer> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
	@Column(name="SnomedCore")
     private String snomedCore;
     private String description;
     private String conceptStatus;
     private String umlsCui;
     private int occurance;
     private double usagePercentage;
     private String firstInSubset;
     private String isRetiredFromSubset;
     private String lastInSubset;
     private String replacedBySnomedCid;

    public SnomedCore() {
    }

    public SnomedCore(String snomedCore, String description) {
       this.snomedCore = snomedCore;
       this.description = description;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getSnomedCore() {
        return this.snomedCore;
    }

    public void setSnomedCore(String snomedCore) {
        this.snomedCore = snomedCore;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCode() {
    	return snomedCore;
    }

    public String getCodingSystem() {
    	return "SnomedCore";
    }

	public String getConceptStatus() {
    	return conceptStatus;
    }

	public void setConceptStatus(String conceptStatus) {
    	this.conceptStatus = conceptStatus;
    }

	public String getUmlsCui() {
    	return umlsCui;
    }

	public void setUmlsCui(String umlsCui) {
    	this.umlsCui = umlsCui;
    }

	public int getOccurance() {
    	return occurance;
    }

	public void setOccurance(int occurance) {
    	this.occurance = occurance;
    }

	public double getUsagePercentage() {
    	return usagePercentage;
    }

	public void setUsagePercentage(double usagePercentage) {
    	this.usagePercentage = usagePercentage;
    }

	public String getFirstInSubset() {
    	return firstInSubset;
    }

	public void setFirstInSubset(String firstInSubset) {
    	this.firstInSubset = firstInSubset;
    }

	public String getIsRetiredFromSubset() {
    	return isRetiredFromSubset;
    }

	public void setIsRetiredFromSubset(String isRetiredFromSubset) {
    	this.isRetiredFromSubset = isRetiredFromSubset;
    }

	public String getLastInSubset() {
    	return lastInSubset;
    }

	public void setLastInSubset(String lastInSubset) {
    	this.lastInSubset = lastInSubset;
    }

	public String getReplacedBySnomedCid() {
    	return replacedBySnomedCid;
    }

	public void setReplacedBySnomedCid(String replacedBySnomedCid) {
    	this.replacedBySnomedCid = replacedBySnomedCid;
    }

	@Override
	public void setCode(String code) {
		this.setSnomedCore(code);
	}

}
