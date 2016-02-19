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


package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class EctConAddSpecialistForm extends ActionForm {
	String fName;
	String lName;
	String proLetters;
	String address;
	String phone;
	String fax;
	String website;
	String email;
	String specType;
	String transType;
	String specId;
	int whichType;
	String eDataUrl;
	String eDataOscarKey;
	String eDataServiceKey;
	String eDataServiceName;
	String annotation;
	String institution;
	String department;
	String privatePhoneNumber;
	String cellPhoneNumber;
	String pagerNumber;
	String salutation;
	Boolean hideFromView;
	Integer eformId;

	private String referralNo;

	public EctConAddSpecialistForm() {
		whichType = 0;
	}

	public int getwhichType() {
		return whichType;
	}

	public String getTransType() {
		if (transType == null) transType = new String();
		return transType;
	}

	public void setTransType(String str) {
		transType = str;
	}

	public String getFirstName() {
		if (fName == null) fName = new String();
		return fName;
	}

	public void setFirstName(String str) {
		fName = str;
	}

	public String getLastName() {
		if (lName == null) lName = new String();
		return lName;
	}

	public void setLastName(String str) {
		lName = str;
	}

	public String getProLetters() {
		if (proLetters == null) proLetters = new String();
		return proLetters;
	}

	public void setProLetters(String str) {
		proLetters = str;
	}

	public String getAddress() {
		if (address == null) address = new String();
		return address;
	}

	public void setAddress(String str) {
		address = str;
	}

	public String getPhone() {
		if (phone == null) phone = new String();
		return phone;
	}

	public void setPhone(String str) {
		phone = str;
	}

	public String getFax() {
		if (fax == null) fax = new String();
		return fax;
	}

	public void setFax(String str) {
		fax = str;
	}

	public String getWebsite() {
		if (website == null) website = new String();
		return website;
	}

	public void setWebsite(String str) {
		website = str;
	}

	public String getEmail() {
		if (email == null) email = new String();
		return email;
	}

	public void setEmail(String str) {
		email = str;
	}

	public String getSpecType() {
		if (specType == null) specType = new String();
		return specType;
	}

	public void setSpecType(String str) {
		specType = str;
	}

	public String getSpecId() {
		if (specId == null) specId = new String();
		return specId;
	}

	public void setSpecId(String str) {
		specId = str;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (fName == null || fName.length() == 0) errors.add("fName", new ActionMessage("Errors.Firstname"));
		if (lName == null || lName.length() == 0) errors.add("lName", new ActionMessage("Errors.Lastname"));
		if (phone == null || phone.length() == 0) errors.add("phone", new ActionMessage("Errors.Phone"));
		if (address == null || address.length() == 0) errors.add("address", new ActionMessage("Errors.Address"));
		if (!errors.isEmpty() && whichType == 2) request.setAttribute("upd", "getterUpdating");
		return errors;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		resetForm();
	}

	public void resetForm() {
		fName = null;
		lName = null;
		proLetters = null;
		address = null;
		phone = null;
		fax = null;
		website = null;
		email = null;
		specType = null;
		transType = null;
		specId = null;
		eDataUrl = null;
		eDataOscarKey = null;
		eDataServiceKey = null;
		annotation = null;
		referralNo = null;
		privatePhoneNumber = null;
		cellPhoneNumber = null;
		pagerNumber = null;
		salutation = null;
		whichType = 0;
	}

	/**
	 * Returns the whichType.
	 *
	 * @return int
	 */
	public int getWhichType() {
		return whichType;
	}

	/**
	 * Sets the whichType.
	 *
	 * @param whichType The whichType to set
	 */
	public void setWhichType(int whichType) {
		this.whichType = whichType;
	}

	public String geteDataUrl() {
		return eDataUrl;
	}

	public void seteDataUrl(String eDataUrl) {
		this.eDataUrl = eDataUrl;
	}

	public String geteDataOscarKey() {
		return eDataOscarKey;
	}

	public void seteDataOscarKey(String eDataOscarKey) {
		this.eDataOscarKey = eDataOscarKey;
	}

	public String geteDataServiceKey() {
		return eDataServiceKey;
	}

	public void seteDataServiceKey(String eDataServiceKey) {
		this.eDataServiceKey = eDataServiceKey;
	}

	public String geteDataServiceName() {
		return eDataServiceName;
	}

	public void seteDataServiceName(String eDataServiceName) {
		this.eDataServiceName = eDataServiceName;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}

	public String getReferralNo() {
		return referralNo;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPrivatePhoneNumber() {
		return privatePhoneNumber;
	}

	public void setPrivatePhoneNumber(String privatePhoneNumber) {
		this.privatePhoneNumber = privatePhoneNumber;
	}

	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public String getPagerNumber() {
		return pagerNumber;
	}

	public void setPagerNumber(String pagerNumber) {
		this.pagerNumber = pagerNumber;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public Boolean getHideFromView() {
		return hideFromView;
	}

	public void setHideFromView(Boolean hideFromView) {
		this.hideFromView = hideFromView;
	}

	public Integer getEformId() {
		return eformId;
	}

	public void setEformId(Integer eformId) {
		this.eformId = eformId;
	}
	
	
	
}
