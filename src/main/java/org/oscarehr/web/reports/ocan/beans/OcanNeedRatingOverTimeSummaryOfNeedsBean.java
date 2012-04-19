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


package org.oscarehr.web.reports.ocan.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OcanNeedRatingOverTimeSummaryOfNeedsBean {

	private String ocanName;
	private Date ocanDate;

	//for summary table
	private Map<String,Integer> consumerNeedMap = new HashMap<String,Integer>();
	private Map<String,Integer>staffNeedMap = new HashMap<String,Integer>();

	public OcanNeedRatingOverTimeSummaryOfNeedsBean() {
		getConsumerNeedMap().put("unknown",0);
		getConsumerNeedMap().put("no",0);
		getConsumerNeedMap().put("met",0);
		getConsumerNeedMap().put("unmet",0);
		getStaffNeedMap().put("unknown",0);
		getStaffNeedMap().put("no",0);
		getStaffNeedMap().put("met",0);
		getStaffNeedMap().put("unmet",0);
	}
	public String getOcanName() {
		return ocanName;
	}
	public void setOcanName(String ocanName) {
		this.ocanName = ocanName;
	}
	public Date getOcanDate() {
		return ocanDate;
	}
	public void setOcanDate(Date ocanDate) {
		this.ocanDate = ocanDate;
	}
	public Map<String, Integer> getConsumerNeedMap() {
		return consumerNeedMap;
	}
	public void setConsumerNeedMap(Map<String, Integer> consumerNeedMap) {
		this.consumerNeedMap = consumerNeedMap;
	}
	public Map<String, Integer> getStaffNeedMap() {
		return staffNeedMap;
	}
	public void setStaffNeedMap(Map<String, Integer> staffNeedMap) {
		this.staffNeedMap = staffNeedMap;
	}


}
