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
import javax.persistence.Table;

@Entity
@Table(name="ctl_frequency")
public class CtlFrequency extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="freqid")
	private Integer id;

	@Column(name="freqcode")
	private String freqCode;

	@Column(name="dailymin")
	private String dailyMin;

	@Column(name="dailymax")
	private String dailyMax;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getFreqCode() {
    	return freqCode;
    }

	public void setFreqCode(String freqCode) {
    	this.freqCode = freqCode;
    }

	public String getDailyMin() {
    	return dailyMin;
    }

	public void setDailyMin(String dailyMin) {
    	this.dailyMin = dailyMin;
    }

	public String getDailyMax() {
    	return dailyMax;
    }

	public void setDailyMax(String dailyMax) {
    	this.dailyMax = dailyMax;
    }


}
