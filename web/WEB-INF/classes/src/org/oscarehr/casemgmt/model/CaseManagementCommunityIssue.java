package org.oscarehr.casemgmt.model;

/**
 * An extension of org.oscarehr.casemgmt.model.CaseManagementIssue, specifically for use in CAISI as part of a distributed system connected through Caisi Integrator.
 * This object includes extra properties for denoting the location of an Issue (the local, current facility or a remote facility, whether it be from another 
 * physical installation of CAISI, or another facility on the same CAISI installation), the name/id of the owning facility and a unique value used for checkboxing. 
 * @author dakers
 *
 */
public class CaseManagementCommunityIssue extends CaseManagementIssue {
	
	protected boolean remote = false;
	protected Integer facilityId;
	protected String facilityName;
	protected String checkboxID;
	
	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getCheckboxID() {
		return checkboxID;
	}

	public void setCheckboxID(String checkboxID) {
		this.checkboxID = checkboxID;
	}
	
	

	
}
