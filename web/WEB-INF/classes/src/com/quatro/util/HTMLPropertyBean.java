package com.quatro.util;

public class HTMLPropertyBean {
	String readOnly;
	String visible;
	String enable;

	public String getEnable() {
		if(enable==null) enable="";
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getReadOnly() {
		if(readOnly==null) readOnly="";
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public String getVisible() {
		if(visible==null) visible="";
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
}
