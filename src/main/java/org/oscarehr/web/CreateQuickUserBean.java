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
package org.oscarehr.web;

import org.apache.struts.action.ActionForm;

public class CreateQuickUserBean extends ActionForm {
	private String firstName;
	private String lastName;
	private String gender;
	private String username;
	private String password;
	private String confirmPassword;
	private String copyProviderNo;
	private String pin;
	private String forcePasswordReset;
	private String billingNo;
	private String thirdPartyBillingNo;
	private boolean generateBillingNo;
	private boolean generateThirdPartyBillingNo;
	private boolean includeDrugFavourites;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCopyProviderNo() {
		return copyProviderNo;
	}

	public void setCopyProviderNo(String copyProviderNo) {
		this.copyProviderNo = copyProviderNo;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getForcePasswordReset() {
		return forcePasswordReset;
	}

	public void setForcePasswordReset(String forcePasswordReset) {
		this.forcePasswordReset = forcePasswordReset;
	}

	public String getBillingNo() {
		return billingNo;
	}

	public void setBillingNo(String billingNo) {
		this.billingNo = billingNo;
	}

	public String getThirdPartyBillingNo() {
		return thirdPartyBillingNo;
	}

	public void setThirdPartyBillingNo(String thirdPartyBillingNo) {
		this.thirdPartyBillingNo = thirdPartyBillingNo;
	}

	public boolean isGenerateBillingNo() {
		return generateBillingNo;
	}

	public void setGenerateBillingNo(boolean generateBillingNo) {
		this.generateBillingNo = generateBillingNo;
	}

	public boolean isGenerateThirdPartyBillingNo() {
		return generateThirdPartyBillingNo;
	}

	public void setGenerateThirdPartyBillingNo(boolean generateThirdPartyBillingNo) {
		this.generateThirdPartyBillingNo = generateThirdPartyBillingNo;
	}

	public boolean isIncludeDrugFavourites() {
		return includeDrugFavourites;
	}

	public void setIncludeDrugFavourites(boolean includeDrugFavourites) {
		this.includeDrugFavourites = includeDrugFavourites;
	}
	
	
}
