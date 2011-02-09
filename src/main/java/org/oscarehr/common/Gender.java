package org.oscarehr.common;

public enum Gender {
	M("Male"),
	F("Female"),
	T("Transgendered"),
	O("Other"),
	U("Undefined");
	
	private final String text;
		
	Gender(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
		
}
