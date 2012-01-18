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

package org.oscarehr.common.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "allergies")
public class Allergy extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "allergyid")
	private Integer id;
	
	@Column(name = "demographic_no")
	private Integer demographicNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "entry_date")
	private Date entryDate;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	private String reaction;
	private String archived;

	@Column(name = "HICL_SEQNO")
	private Integer hiclSeqno;
	
	@Column(name = "HIC_SEQNO")
	private Integer hicSeqno;
	
	@Column(name = "AGCSP")
	private Integer agcsp;
	
	@Column(name = "AGCCS")
	private Integer agccs;
	
	@Column(name = "TYPECODE")
	private Integer typeCode;
	
	@Column(name = "drugref_id")
	private String drugrefId;
	
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "age_of_onset")
	private String ageOfOnset;
	
	@Column(name = "severity_of_reaction")
	private String severityOfReaction;
	
	@Column(name = "onset_of_reaction")
	private String onsetOfReaction;
	
	@Column(name = "regional_identifier")
	private String regionalIdentifier;
	
	@Column(name = "life_stage")
	private String lifeStage;

	private int position=0;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;

	/**
	 * This string is currently nullable because this field never use to exist, therefore all previous entries are null, all new entries should be populated though.
	 */
	private String providerNo;
	
	public String getArchived() {
		return archived;
	}

	public void setArchived(String archived) {
		this.archived = archived;
	}

	public Integer getAllergyId() {
		return id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographic_no) {
		this.demographicNo = demographic_no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = StringUtils.trimToNull(description);
	}

	public Date getEntryDate() {
    	return (entryDate);
    }

	public void setEntryDate(Date entryDate) {
    	this.entryDate = entryDate;
    }

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = StringUtils.trimToNull(reaction);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getHiclSeqno() {
		return hiclSeqno;
	}

	public void setHiclSeqno(Integer hiclSeqno) {
		this.hiclSeqno = hiclSeqno;
	}

	public Integer getHicSeqno() {
		return hicSeqno;
	}

	public void setHicSeqno(Integer hicSeqno) {
		this.hicSeqno = hicSeqno;
	}

	public Integer getAgcsp() {
		return agcsp;
	}

	public void setAgcsp(Integer agcsp) {
		this.agcsp = agcsp;
	}

	public Integer getAgccs() {
		return agccs;
	}

	public void setAgccs(Integer agccs) {
		this.agccs = agccs;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getDrugrefId() {
		return drugrefId;
	}

	public void setDrugrefId(String drugrefId) {
		this.drugrefId = StringUtils.trimToNull(drugrefId);
	}

	public String getAgeOfOnset() {
		return ageOfOnset;
	}

	public void setAgeOfOnset(String ageOfOnset) {
		this.ageOfOnset = StringUtils.trimToNull(ageOfOnset);
	}

	public String getSeverityOfReaction() {
		return severityOfReaction;
	}

	public void setSeverityOfReaction(String severityOfReaction) {
		this.severityOfReaction = StringUtils.trimToNull(severityOfReaction);
	}

	public String getOnsetOfReaction() {
		return onsetOfReaction;
	}

	public void setOnsetOfReaction(String onsetOfReaction) {
		this.onsetOfReaction = StringUtils.trimToNull(onsetOfReaction);
	}

	public String getRegionalIdentifier() {
		return regionalIdentifier;
	}

	public void setRegionalIdentifier(String regionalIdentifier) {
		this.regionalIdentifier = StringUtils.trimToNull(regionalIdentifier);
	}

	public String getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(String lifeStage) {
		this.lifeStage = StringUtils.trimToNull(lifeStage);
	}

	public int getPosition() {
    	return (position);
    }

	public void setPosition(int position) {
    	this.position = position;
    }

	public Date getLastUpdateDate() {
    	return (lastUpdateDate);
    }

	public void setLastUpdateDate(Date lastUpdateDate) {
    	this.lastUpdateDate = lastUpdateDate;
    }
	
	public String getProviderNo() {
    	return (providerNo);
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = StringUtils.trimToNull(providerNo);
    }

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}
}
