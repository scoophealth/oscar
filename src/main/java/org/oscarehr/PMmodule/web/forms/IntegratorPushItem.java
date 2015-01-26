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
package org.oscarehr.PMmodule.web.forms;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.model.IntegratorProgress;

public class IntegratorPushItem {

	private Integer id;
	
	private Date dateCreated;
	
	private String status;

	private String errorMessage;
	
	private int totalDemographics;
	
	private int totalOutstanding;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getTotalDemographics() {
		return totalDemographics;
	}

	public void setTotalDemographics(int totalDemographics) {
		this.totalDemographics = totalDemographics;
	}

	public int getTotalOutstanding() {
		return totalOutstanding;
	}

	public void setTotalOutstanding(int totalOutstanding) {
		this.totalOutstanding = totalOutstanding;
	}
	

	public String getDateCreatedAsString() {
		return DateFormatUtils.format(getDateCreated(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
	public String getEstimatedDateOfCompletionAsString() {
		if(IntegratorProgress.STATUS_COMPLETED.equals(getStatus())) {
			return "N/A";
		}
		return DateFormatUtils.format(getEstimatedDateOfCompletion(), "yyyy-MM-dd HH:mm:ss");
	}
	
	public String getProgressAsPercentageString() {
		Integer res =  getProgressAsPercentage();
		if(res == null) {
			return "N/A";
		}
		return res + "%";
	}
	
	public Integer getProgressAsPercentage() {
		if(totalDemographics == 0) {
			return null;
		}
		
		int perc =(int)(((float)(totalDemographics-totalOutstanding )/ totalDemographics)*100);
		
		return perc ;
	}
	
	public Date getEstimatedDateOfCompletion() {
		Date now = new Date();
		
		long timeElapsed = now.getTime() - getDateCreated().getTime();
		
		int recordsProccessd = getTotalDemographics() - getTotalOutstanding();
		
		long totalTime = (totalDemographics * timeElapsed) / recordsProccessd;
		
		long remainingTime = totalTime - timeElapsed;
		
		long endTime = now.getTime() + remainingTime;
		
		Date endDate = new Date(endTime);
		
		return endDate;
	}
}
 