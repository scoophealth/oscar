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

package org.oscarehr.casemgmt.web;

import java.util.Date;

public class PrescriptDrug {
	private Integer localDrugId=null;
	private Date date_prescribed;
	private String drug_special;
	private Date end_date;
	private boolean drug_achived;
	private String BN;
	private Integer GCN_SEQNO;
	private String customName;
	private String regionalIdentifier = null;
	private String remoteFacilityName = null;
	private Date createDate=null;
	private boolean longTerm=false;
        private boolean nonAuthoritative=false;

        public boolean isNonAuthoritative() {
		return this.nonAuthoritative;
	}

	public void setNonAuthoritative(boolean nonAuthoritative) {
		this.nonAuthoritative = nonAuthoritative;
	}

        public boolean isLongTerm(){
            return this.longTerm;
        }
        public void setLongTerm(boolean longTerm){
            this.longTerm=longTerm;
        }
	public Integer getLocalDrugId() {
    	return localDrugId;
    }

	public void setLocalDrugId(Integer localDrugId) {
    	this.localDrugId = localDrugId;
    }

	public String getRegionalIdentifier() {
		return regionalIdentifier;
	}

	public void setRegionalIdentifier(String regionalIdentifier) {
		this.regionalIdentifier = regionalIdentifier;
	}

	public String getRemoteFacilityName() {
		return remoteFacilityName;
	}

	public void setRemoteFacilityName(String remoteFacilityName) {
		this.remoteFacilityName = remoteFacilityName;
	}

	public Date getDate_prescribed() {
		return date_prescribed;
	}

	public void setDate_prescribed(Date date_prescribed) {
		this.date_prescribed = date_prescribed;
	}

	public String getDrug_special() {
		return drug_special;
	}

	public void setDrug_special(String drug_special) {
		this.drug_special = drug_special;
	}

	public boolean isExpired() {
		if (end_date == null) return (false);

		return ((new Date()).after(end_date));
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public boolean isDrug_achived() {
		return drug_achived;
	}

	public void setDrug_achived(boolean drug_achived) {
		this.drug_achived = drug_achived;
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

	public Date getCreateDate() {
    	return createDate;
    }

	public void setCreateDate(Date createDate) {
    	this.createDate = createDate;
    }

}
