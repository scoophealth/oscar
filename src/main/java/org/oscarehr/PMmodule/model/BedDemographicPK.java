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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BedDemographicPK implements Serializable {
	
    protected int hashCode = Integer.MIN_VALUE;
    private Integer demographicNo;
    private Integer bedId;


    public BedDemographicPK () {}

	public BedDemographicPK (
		Integer demographicNo,
		Integer bedId) {

		this.setDemographicNo(demographicNo);
		this.setBedId(bedId);
	}

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    /**
	 * Return the value associated with the column: demographic_no
     */
    public Integer getDemographicNo () {
        return demographicNo;
    }

    /**
	 * Set the value related to the column: demographic_no
     * @param demographicNo the demographic_no value
     */
    public void setDemographicNo (Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    /**
	 * Return the value associated with the column: bed_id
     */
    public Integer getBedId () {
        return bedId;
    }

    /**
	 * Set the value related to the column: bed_id
     * @param bedId the bed_id value
     */
    public void setBedId (Integer bedId) {
        this.bedId = bedId;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof BedDemographicPK)) return false;
        else {
            BedDemographicPK mObj = (BedDemographicPK) obj;
            if (null != this.getDemographicNo() && null != mObj.getDemographicNo()) {
                if (!this.getDemographicNo().equals(mObj.getDemographicNo())) {
                    return false;
                }
            }
            else {
                return false;
            }
            if (null != this.getBedId() && null != mObj.getBedId()) {
                if (!this.getBedId().equals(mObj.getBedId())) {
                    return false;
                }
            }
            else {
                return false;
            }
            return true;
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            StringBuilder sb = new StringBuilder();
            if (null != this.getDemographicNo()) {
                sb.append(this.getDemographicNo().hashCode());
                sb.append(":");
            }
            else {
                return super.hashCode();
            }
            if (null != this.getBedId()) {
                sb.append(this.getBedId().hashCode());
                sb.append(":");
            }
            else {
                return super.hashCode();
            }
            this.hashCode = sb.toString().hashCode();
        }
        return this.hashCode;
    }
}
