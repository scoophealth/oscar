/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.model.security;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.MiscUtils;

public class Security implements java.io.Serializable {
	private static Logger logger = MiscUtils.getLogger();

	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FAILED = 1;
	public static final int USER_NOT_EXISTS = 2;
	public static final int PASSWORD_EXPIRED = 3;
	public static final int ACCOUNT_BLOCKED = 4;

	private Integer securityNo;
	private String userName;
	private String password;
	private String providerNo;
	private String pin;
	private Integer BRemotelockset;
	private Integer BLocallockset;
	private Date dateExpiredate;
	private Integer BExpireset;
	private String lastUpdateUser;
	private Calendar lastUpdateDate;
	private String loginIP = "";
	private Date loginDate;
	private int loginStatus = 0;

	public Calendar getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Calendar lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	/** default constructor */
	public Security() {
	}

	/** full constructor */
	public Security(String userName, String password, String providerNo, String pin, Integer BRemotelockset, Integer BLocallockset, Date dateExpiredate, Integer BExpireset) {
		this.userName = userName;
		this.password = password;
		this.providerNo = providerNo;
		this.pin = pin;
		this.BRemotelockset = BRemotelockset;
		this.BLocallockset = BLocallockset;
		this.dateExpiredate = dateExpiredate;
		this.BExpireset = BExpireset;
	}

	// Property accessors

	public Integer getSecurityNo() {
		return this.securityNo;
	}

	public void setSecurityNo(Integer securityNo) {
		this.securityNo = securityNo;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProviderNo() {
		return this.providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Integer getBRemotelockset() {
		return this.BRemotelockset;
	}

	public void setBRemotelockset(Integer BRemotelockset) {
		this.BRemotelockset = BRemotelockset;
	}

	public Integer getBLocallockset() {
		return this.BLocallockset;
	}

	public void setBLocallockset(Integer BLocallockset) {
		this.BLocallockset = BLocallockset;
	}

	public Date getDateExpiredate() {
		return this.dateExpiredate;
	}

	public void setDateExpiredate(Date dateExpiredate) {
		this.dateExpiredate = dateExpiredate;
	}

	public Integer getBExpireset() {
		return this.BExpireset;
	}

	public void setBExpireset(Integer BExpireset) {
		this.BExpireset = BExpireset;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public int getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * @return true if inputed password equals password in the DB, false otherwise.
	 */
	public boolean checkPassword(String inputedPassword) {
		if (password == null) return (false);

		byte[] sha1Bytes = EncryptionUtils.getSha1(inputedPassword);
		StringBuilder sb = new StringBuilder();
		for (byte b : sha1Bytes) {
			sb.append(b);
		}

		if (password.equals(sb.toString())) {
			return (true);
		} else {
			try {
				// sleep to throttle anyone trying to brute force hack passwords
				Thread.sleep(250);
			} catch (InterruptedException e) {
				logger.error("Error", e);
			}
			return (false);
		}
	}
}
