package org.oscarehr.integration.hl7.model;

//typeIds
//MR=ADT MRN
//TMR=PHS Temporary MRN
//PI = ADT CPI
//PE = Enterprise EPN
//TPN = PHS Temporary EPN
//AN = ADT Patient Account Number
//TAN = PHS temporary patient account number
//NH = National health card #
//JHN = regional health card #

public class PatientId {
	private String id;
	private String authority;
	private String typeId;
	
	public PatientId() {
		
	}
	
	public PatientId(String id, String authority, String typeId) {
		this.id = id;
		this.authority = authority;
		this.typeId = typeId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String toString() {
		return "Patient Identifier: " + id + " " + authority + " " + typeId;
	}
}
