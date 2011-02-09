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
			return ((facilityId == o1.facilityId) && (demographicId == o1.demographicId));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}