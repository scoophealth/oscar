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

public class Drug extends BaseObject {
	private Long id;
	private String provider_no;
	private String demographic_no;
	private Date rx_date;
	private Date end_date;
	private String BN;
	private Integer GCN_SEQNO;
	private String customName;
	private String special;
	private Long script_no;
	private Date create_date;
	private Boolean archived;
	
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProviderNo() {
		return provider_no;
	}
	public void setProviderNo(String provider_no) {
		this.provider_no = provider_no;
	}
	public Date getRx_date() {
		return rx_date;
	}
	public void setRx_date(Date rx_date) {
		this.rx_date = rx_date;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public Long getScript_no()
	{
		return script_no;
	}
	public void setScript_no(Long script_no)
	{
		this.script_no = script_no;
	}
	public Boolean getArchived() {
		return archived;
	}
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}
	public String getBN() {
		return BN;
	}
	public void setBN(String bn) {
		BN = bn;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public Integer getGCN_SEQNO() {
		return GCN_SEQNO;
	}
	public void setGCN_SEQNO(Integer gcn_seqno) {
		GCN_SEQNO = gcn_seqno;
	}
	
}
