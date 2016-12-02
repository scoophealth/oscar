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
package org.oscarehr.integration.dashboard.model;

public class User {
	
	public enum Province {
	    AB("AB"),
	    BC("BC"),
	    MB("MB"),
	    NB("NB"),
	    NL("NL"),
	    NS("NS"),
	    NT("NT"),
	    NU("NU"),
	    ON("ON"),
	    PE("PE"),
	    QC("QC"),
	    SK("SK"),
	    YT("YT"),
	    ;

	    private final String text;

	    /**
	     * @param text
	     */
	    private Province(final String text) {
	        this.text = text;
	    }

	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	private String username;
	private String firstName;
	private String lastName;
	private Province province;
	private String city;
	private String postalCode;
	private String uniqueIdentifier;
//	private List<String> authorizedUsernameList = new ArrayList<String>();
	private String role;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	/*
	public List<String> getAuthorizedUsernameList() {
		return authorizedUsernameList;
	}
	public void setAuthorizedUsernameList(List<String> authorizedUsernameList) {
		this.authorizedUsernameList = authorizedUsernameList;
	}
	*/
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
