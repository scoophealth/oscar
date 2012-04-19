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


package oscar.oscarRx.pageUtil;

import org.oscarehr.common.model.Allergy;

public final class AllergyDisplay {
	private Integer id;
	private Integer remoteFacilityId;
	private String entryDate;
	private String description;
	private int typeCode;
	private String severityCode;
	private String onSetCode;
	private String reaction;
	private String startDate;
	private String archived;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRemoteFacilityId() {
		return (remoteFacilityId);
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}

	public String getEntryDate() {
		return (entryDate);
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTypeCode() {
		return (typeCode);
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public String getSeverityCode() {
		return (severityCode);
	}

	public void setSeverityCode(String severityCode) {
		this.severityCode = severityCode;
	}

	public String getOnSetCode() {
		return (onSetCode);
	}

	public void setOnSetCode(String onSetCode) {
		this.onSetCode = onSetCode;
	}

	public String getReaction() {
		return (reaction);
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public String getStartDate() {
		return (startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getOnSetDesc() {
		return (Allergy.getOnSetOfReactionDesc(onSetCode));
	}

	public String getSeverityDesc() {
		return (Allergy.getSeverityOfReactionDesc(severityCode));
	}

	public String getTypeDesc() {
		return (Allergy.getTypeDesc(typeCode));
	}

	public String getArchived() {
    	return archived;
    }

	public void setArchived(String archived) {
    	this.archived = archived;
    }

}
