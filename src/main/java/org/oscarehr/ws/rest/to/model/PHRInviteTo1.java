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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@XmlRootElement
public class PHRInviteTo1 implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long demographicNo;
	private String title;
	private String firstName;
	private String lastName;
	private String email;
	private String language;
	private String phrPatientUsername;

	private Long clinicId;
	private String status;
	private String token;
	private Long phrPatientId;
	private String newphrPatientUsername;

	public Long getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Long demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPhrPatientUsername() {
		return phrPatientUsername;
	}

	public void setPhrPatientUsername(String phrPatientUsername) {
		this.phrPatientUsername = phrPatientUsername;
	}

	public Long getClinicId() {
		return clinicId;
	}

	public void setClinicId(Long clinicId) {
		this.clinicId = clinicId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getPhrPatientId() {
		return phrPatientId;
	}

	public void setPhrPatientId(Long phrPatientId) {
		this.phrPatientId = phrPatientId;
	}

	public String getNewPhrPatientUsername() {
		return newphrPatientUsername;
	}

	public void setNewPhrPatientUsername(String newphrPatientUsername) {
		this.newphrPatientUsername = newphrPatientUsername;
	}
	
	public JSONObject toJson() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("demographicNo", demographicNo);
		json.put("title", title);
		json.put("firstName", firstName);
		json.put("lastName", lastName);
		json.put("email", email);
		json.put("language", language);;
		json.put("phrPatientUsername", phrPatientUsername);
		json.put("clinicId", clinicId);
		json.put("status", status);
		json.put("token", token);
		json.put("phrPatientId", phrPatientId);
		json.put("newphrPatientUsername", newphrPatientUsername);
		return json;
	}
}
