/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.wlmatch;

import java.util.Date;

public class VacancyDisplayBO {

	int vacancyID=0;
	Date created=null;
	String vacancyName = null;
	String vacancyTemplateName=null;
	String criteriaSummary=null;
	boolean active=true;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    String programName;

	int pendingCount=0;
	int rejectedCount=0;
	int acceptedCount=0;
	
	private Integer programId;
	private Integer noOfVacancy;
	
	public int getVacancyID() {
		return vacancyID;
	}
	public void setVacancyID(int vacancyID) {
		this.vacancyID = vacancyID;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getVacancyTemplateName() {
		return vacancyTemplateName;
	}
	public void setVacancyTemplateName(String vacancyTemplateName) {
		this.vacancyTemplateName = vacancyTemplateName;
	}
	public String getCriteriaSummary() {
		return criteriaSummary;
	}
	public void setCriteriaSummary(String criteriaSummary) {
		this.criteriaSummary = criteriaSummary;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getVacancyName() {
    	return vacancyName;
    }
	public void setVacancyName(String vacancyName) {
    	this.vacancyName = vacancyName;
    }

	
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
	public int getRejectedCount() {
		return rejectedCount;
	}
	public void setRejectedCount(int rejectedCount) {
		this.rejectedCount = rejectedCount;
	}
	public int getAcceptedCount() {
		return acceptedCount;
	}
	public void setAcceptedCount(int acceptedCount) {
		this.acceptedCount = acceptedCount;
	}

	public Integer getProgramId() {
    	return programId;
    }
	public void setProgramId(Integer programId) {
    	this.programId = programId;
    }
	public Integer getNoOfVacancy() {
    	return noOfVacancy;
    }
	public void setNoOfVacancy(Integer noOfVacancy) {
    	this.noOfVacancy = noOfVacancy;
    }
	
	
}
