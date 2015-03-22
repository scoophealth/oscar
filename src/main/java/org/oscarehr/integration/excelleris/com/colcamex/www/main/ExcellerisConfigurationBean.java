/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.excelleris.com.colcamex.www.main;

import java.io.File;

/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class ExcellerisConfigurationBean  {

	protected String userName;
	protected String password;
	protected String secretKey;
	
	private String serviceName;
	private String servicePath;
	private String loginPath;
	private String acknowledgePath;
	private String logoutPath;
	private String fetchPath;
	private File certPath;
	private File keyPath;
	private String userLogPath;
	
	private boolean certificateInstalled;
	
	public ExcellerisConfigurationBean() {
		// default constructor
	}
	
	public void initialize(
			String URI,
			String REQUEST,
			String LOGIN,
			String LOGOUT,
			String ACKNOWLEDGE) {
		
		setServicePath(URI);
		setLoginPath(LOGIN);
		setLogoutPath(LOGOUT);
		setFetchPath(REQUEST);
		setAcknowledgePath(ACKNOWLEDGE);
		
	}
	
	
	public String getSecretKey() {
		return new String(secretKey);
	}

	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getPassword () {	
		
		if(password == null) {
			return "";
		} 
		
		return new String(password);
		
	}
	
	public void setPassword (String password) {		
		this.password = password;				
	}

	public String getUserName() {
		
		if(userName == null) {
			return "";
		}
		return userName;
	}

	public void setUserName (String userName) {
		this.userName = userName;

	}
	

	public String getServicePath() {
		return servicePath;
	}


	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}


	public String getLoginPath() {
		return loginPath;
	}


	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}


	public String getAcknowledgePath() {
		return acknowledgePath;
	}


	public void setAcknowledgePath(String acknowledgePath) {
		this.acknowledgePath = acknowledgePath;
	}

	public String getLogoutPath() {
		return logoutPath;
	}


	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}


	public String getFetchPath() {
		return fetchPath;
	}

	public void setFetchPath(String fetchPath) {
		this.fetchPath = fetchPath;
	}


	public boolean isCertificateInstalled() {
		certificateInstalled = true;
		
		File certPath = getCertPath();
		File keyPath = getKeyPath();
		
		if(certPath != null) {
			if(! certPath.isFile()) {
				certificateInstalled = false;
			}
		} else {
			certificateInstalled = false;
		}
		if(keyPath != null) {
			if(! keyPath.isFile()) {
				certificateInstalled = false;
			}
		} else {
			certificateInstalled = false;
		}
		return certificateInstalled;
	}
	

	public File getCertPath() {
		return certPath;
	}


	public void setCertPath(File certPath) {
		this.certPath = certPath;
	}

	public File getKeyPath() {
		return keyPath;
	}


	public void setKeyPath(File keyPath) {
		this.keyPath = keyPath;
	}

	public void setCertificateInstalled(boolean certificateInstalled) {
		this.certificateInstalled = certificateInstalled;
	}


	public boolean isLoginInfoSet() {
		String password = getPassword();
		String login = getUserName();

		if((password != null)&&(login != null)) {
			if( (! password.isEmpty())&&(! login.isEmpty()) ){
				return true;
			}		
		}
		return false;
	}

	public String getUserLogPath() {
		if(this.userLogPath == null) {
			return new String(getServiceName() + "Log" + ".html");
		}
		
		return userLogPath;
	}

	public void setUserLogPath(String userLogPath) {
		this.userLogPath = userLogPath;		
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;		
	}


	public String getServiceName() {
		return this.serviceName;
	}

}
