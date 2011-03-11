package org.oscarehr.integration.hl7.model;

public class StaffId {
	private String id;
	private String typeCode;
	
	public StaffId() {
		//empty
	}
	
	public StaffId(String id, String typeCode) {
		this.id = id;
		this.typeCode = typeCode;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	
}
