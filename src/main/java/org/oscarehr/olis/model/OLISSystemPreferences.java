/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package org.oscarehr.olis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;
@Entity
public class OLISSystemPreferences extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String startTime;
	private String endTime;
	private Integer pollFrequency;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastRun;

	private boolean filterPatients;
	
	
	@Override
	public Integer getId() {
		return id;
	}	
	
	public String getStartTime() {
    	return startTime;
    }

	public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }

	public String getEndTime() {
    	return endTime;
    }

	public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }
	
	public Integer getPollFrequency() {
    	return pollFrequency;
    }

	public void setPollFrequency(Integer pollFrequency) {
    	this.pollFrequency = pollFrequency;
    }

	public Date getLastRun() {
		return lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}	
	
	

	public boolean isFilterPatients() {
    	return filterPatients;
    }

	public void setFilterPatients(boolean filterPatients) {
    	this.filterPatients = filterPatients;
    }

	public OLISSystemPreferences(){
		super();
	}	
}
