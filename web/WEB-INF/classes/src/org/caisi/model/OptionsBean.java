package org.caisi.model;

public class OptionsBean {
	private String property;
	private String labelProperty;
	
	public OptionsBean(String property) {
		setLabelProperty(property);
		setProperty(property);
	}
	
	public OptionsBean(String labelProperty,String property) {
		setLabelProperty(labelProperty);
		setProperty(property);
	}
	
	public String getLabelProperty() {
		return labelProperty;
	}
	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
}
