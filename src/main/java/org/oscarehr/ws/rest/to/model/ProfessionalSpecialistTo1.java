/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;

public class ProfessionalSpecialistTo1 implements Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private String firstName;
	private String lastName;
	private String name;
	private String professionalLetters;
	private String streetAddress;
	private String phoneNumber;
	private String faxNumber;
	private String webSite;
	private String emailAddress;
	private String specialtyType;
	private String eDataUrl;
	private String eDataOscarKey;
	private String eDataServiceKey;
	private String eDataServiceName;
    private String annotation;
    private String referralNo;
	private Integer institutionId;
    private Integer departmentId;
    private Integer eformId;
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfessionalLetters() {
		return professionalLetters;
	}
	public void setProfessionalLetters(String professionalLetters) {
		this.professionalLetters = professionalLetters;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getSpecialtyType() {
		return specialtyType;
	}
	public void setSpecialtyType(String specialtyType) {
		this.specialtyType = specialtyType;
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
	public String getReferralNo() {
		return referralNo;
	}
	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}
	public Integer getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getEformId() {
		return eformId;
	}
	public void setEformId(Integer eformId) {
		this.eformId = eformId;
	}
	
}