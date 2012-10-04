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

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="study")
public class StudyDetails extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="study_no")
	private Integer id;

	@Column(name="study_name")
	private String studyName;

	@Column(name="study_link")
	private String studyLink;

	private String description;

	@Column(name="form_name")
	private String formName;

	private int current1;

	@Column(name="remote_serverurl")
	private String remoteServerUrl;

	@Column(name="provider_no")
	private String providerNo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="providerstudy", joinColumns= @JoinColumn(name="study_no"),
									 inverseJoinColumns= @JoinColumn(name="provider_no"))
	Set<Provider>providers;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="demographicstudy", joinColumns= @JoinColumn(name="study_no"),
										inverseJoinColumns= @JoinColumn(name="demographic_no"))
	Set<Demographic>demographics;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getStudyName() {
    	return studyName;
    }

	public void setStudyName(String studyName) {
    	this.studyName = studyName;
    }

	public String getStudyLink() {
    	return studyLink;
    }

	public void setStudyLink(String studyLink) {
    	this.studyLink = studyLink;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getFormName() {
    	return formName;
    }

	public void setFormName(String formName) {
    	this.formName = formName;
    }

	public int getCurrent1() {
    	return current1;
    }

	public void setCurrent1(int current1) {
    	this.current1 = current1;
    }

	public String getRemoteServerUrl() {
    	return remoteServerUrl;
    }

	public void setRemoteServerUrl(String remoteServerUrl) {
    	this.remoteServerUrl = remoteServerUrl;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }

	public Set<Provider> getProviders() {
    	return providers;
    }

	public void setProviders(Set<Provider> providers) {
    	this.providers = providers;
    }

	public Set<Demographic> getDemographics() {
    	return demographics;
    }

	public void setDemographics(Set<Demographic> demographics) {
    	this.demographics = demographics;
    }


}
