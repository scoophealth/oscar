package org.caisi.service.impl;

import org.caisi.service.Version;

public class VersionImpl implements Version {

	private String version;
	
	public VersionImpl() {
		
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVersion() {
		return version;
	}

}
