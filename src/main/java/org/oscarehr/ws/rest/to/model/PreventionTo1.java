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
package org.oscarehr.ws.rest.to.model;

import java.util.Date;

public class PreventionTo1 {

	private Integer id;
	private Integer demographicId;

	private Date creationDate = new Date();
	private Date preventionDate;

	private String providerNo;
	private String preventionType;

	private char deleted = '0';
	private char refused = '0';
	private char never = '0';

	private Date nextDate;

	private String creatorProviderNo;
	
	private Date lastUpdateDate;
	
	private Boolean restrictToProgram = false;
	private Integer programNo;

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getPreventionDate() {
		return preventionDate;
	}

	public void setPreventionDate(Date preventionDate) {
		this.preventionDate = preventionDate;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPreventionType() {
		return preventionType;
	}

	public void setPreventionType(String preventionType) {
		this.preventionType = preventionType;
	}

	public boolean isDeleted() {
		return deleted=='1';
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted ? '1' : '0';
	}

	public boolean isRefused() {
		return refused=='1';
	}
	
	public boolean isIneligible(){
		return refused == '2';
	}

	public void setRefused(boolean refused) {
		this.refused = refused ? '1' : '0';
	}
	
	public void setIneligible(boolean ineligible){
		this.refused = ineligible ? '2' : '0';
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public boolean isNever() {
		return never=='1';
	}

	public void setNever(boolean never) {
		this.never = never ? '1' : '0';
	}

	public String getCreatorProviderNo() {
		return creatorProviderNo;
	}

	public void setCreatorProviderNo(String creatorProviderNo) {
		this.creatorProviderNo = creatorProviderNo;
	}
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


   	public Integer getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}
	
	public String getDeletedRawValue() {
		return String.valueOf(deleted);
	}

	public Boolean getRestrictToProgram() {
		return restrictToProgram;
	}

	public void setRestrictToProgram(Boolean restrictToProgram) {
		this.restrictToProgram = restrictToProgram;
	}

	public Integer getProgramNo() {
		return programNo;
	}

	public void setProgramNo(Integer programNo) {
		this.programNo = programNo;
	}
	
	
}
