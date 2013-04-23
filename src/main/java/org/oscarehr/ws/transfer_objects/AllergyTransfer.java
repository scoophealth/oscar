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

package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Allergy;
import org.springframework.beans.BeanUtils;

public final class AllergyTransfer {

	private Integer Id;
	private Integer demographicNo;
	private Date entryDate;
	private String description;
	private String reaction;
	private Integer hiclSeqno;
	private Integer hicSeqno;
	private Integer agcsp;
	private Integer agccs;
	private Integer typeCode;
	private Date startDate;
	private String ageOfOnset;
	private String severityOfReaction;
	private String onsetOfReaction;
	private String regionalIdentifier;
	private String lifeStage;
	private Date lastUpdateDate;
	private String providerNo;

	public Integer getDemographicNo() {
		return (demographicNo);
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public Integer getId() {
		return (Id);
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Date getEntryDate() {
		return (entryDate);
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReaction() {
		return (reaction);
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public Integer getHiclSeqno() {
		return (hiclSeqno);
	}

	public void setHiclSeqno(Integer hiclSeqno) {
		this.hiclSeqno = hiclSeqno;
	}

	public Integer getHicSeqno() {
		return (hicSeqno);
	}

	public void setHicSeqno(Integer hicSeqno) {
		this.hicSeqno = hicSeqno;
	}

	public Integer getAgcsp() {
		return (agcsp);
	}

	public void setAgcsp(Integer agcsp) {
		this.agcsp = agcsp;
	}

	public Integer getAgccs() {
		return (agccs);
	}

	public void setAgccs(Integer agccs) {
		this.agccs = agccs;
	}

	public Integer getTypeCode() {
		return (typeCode);
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Date getStartDate() {
		return (startDate);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getAgeOfOnset() {
		return (ageOfOnset);
	}

	public void setAgeOfOnset(String ageOfOnset) {
		this.ageOfOnset = ageOfOnset;
	}

	public String getSeverityOfReaction() {
		return (severityOfReaction);
	}

	public void setSeverityOfReaction(String severityOfReaction) {
		this.severityOfReaction = severityOfReaction;
	}

	public String getOnsetOfReaction() {
		return (onsetOfReaction);
	}

	public void setOnsetOfReaction(String onsetOfReaction) {
		this.onsetOfReaction = onsetOfReaction;
	}

	public String getRegionalIdentifier() {
		return (regionalIdentifier);
	}

	public void setRegionalIdentifier(String regionalIdentifier) {
		this.regionalIdentifier = regionalIdentifier;
	}

	public String getLifeStage() {
		return (lifeStage);
	}

	public void setLifeStage(String lifeStage) {
		this.lifeStage = lifeStage;
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
		this.providerNo = providerNo;
	}

	public static AllergyTransfer toTransfer(Allergy allergy) {
		if (allergy == null) return (null);

		AllergyTransfer allergyTransfer = new AllergyTransfer();

		BeanUtils.copyProperties(allergy, allergyTransfer);

		return (allergyTransfer);
	}

	public static AllergyTransfer[] toTransfers(List<Allergy> allergies) {
		ArrayList<AllergyTransfer> results = new ArrayList<AllergyTransfer>();

		for (Allergy allergy : allergies) {
			results.add(toTransfer(allergy));
		}

		return (results.toArray(new AllergyTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
