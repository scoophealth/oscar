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

package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;

/**
 * This class is used for (at least) 2 purposes, 1) as a PrimaryKey for JPA objects,
 * 2) as a primary key for integrator processing (the reason being the pk's passed
 * by the integrator doesn't have equals/hashcode so we just use this one when
 * we need to have the keys that support equals/hashcode).  
 */
@Embeddable
public class FacilityDemographicPrimaryKey implements Serializable {
	private Integer facilityId = null;
	private Integer demographicId = null;

	public FacilityDemographicPrimaryKey()
	{
		// do nothing, required by jpa
	}
	
	public FacilityDemographicPrimaryKey(Integer facilityId, Integer demographicId)
	{
		this.facilityId=facilityId;
		this.demographicId=demographicId;
	}
	
	public FacilityDemographicPrimaryKey(FacilityIdIntegerCompositePk pk)
	{
		this.facilityId=pk.getIntegratorFacilityId();
		this.demographicId=pk.getCaisiItemId();
	}
	
	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	@Override
	public String toString() {
		return ("facilityId=" + facilityId + ", demographicId=" + demographicId);
	}

	@Override
	public int hashCode() {
		return (demographicId);
	}

	@Override
	public boolean equals(Object o) {
		try {
			FacilityDemographicPrimaryKey o1 = (FacilityDemographicPrimaryKey) o;
			return ((facilityId.equals(o1.facilityId)) && (demographicId .equals(o1.demographicId)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
