package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

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