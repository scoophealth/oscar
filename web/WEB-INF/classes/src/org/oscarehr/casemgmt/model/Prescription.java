/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class Prescription extends BaseObject {
	private Long id;
	private String providerNo;
	private String demographic_no;
	private Date date_prescribed;
	private Date date_printed;
	private String dates_reprinted;
	private String textView;
	
	
	public Date getDate_prescribed() {
		return date_prescribed;
	}
	public void setDate_prescribed(Date date_prescribed) {
		this.date_prescribed = date_prescribed;
	}
	public Date getDate_printed() {
		return date_printed;
	}
	public void setDate_printed(Date date_printed) {
		this.date_printed = date_printed;
	}
	public String getDates_reprinted() {
		return dates_reprinted;
	}
	public void setDates_reprinted(String dates_reprinted) {
		this.dates_reprinted = dates_reprinted;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String provider_no) {
		this.providerNo = provider_no;
	}
	public String getTextView() {
		return textView;
	}
	public void setTextView(String textView) {
		this.textView = textView;
	}
}
