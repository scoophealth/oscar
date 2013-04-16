/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;


public class HRMPreferences {
	
	
	private String userName ;
	private String location;
	private String privateKey;
	private String decryptionKey;
	private String interval;
	
	public HRMPreferences(){
		
	}
	
	public String getUserName() {
		
		return userName;
    }
	
	public void setUserName(String userName) {
    	this.userName = userName;
    }
	
	public String getLocation() {
    	return location;
    }
	public void setLocation(String location) {
    	this.location = location;
    }
	public String getPrivateKey() {
    	return privateKey;
    }
	public void setPrivateKey(String privateKey) {
    	this.privateKey = privateKey;
    }
	public String getDecryptionKey() {
    	return decryptionKey;
    }
	public void setDecryptionKey(String decryptionKey) {
    	this.decryptionKey = decryptionKey;
    }

	public void setInterval(String interval) {
	    this.interval = interval;
    }
	
	public String getInterval() {
		return interval;
	}
	
	
}
