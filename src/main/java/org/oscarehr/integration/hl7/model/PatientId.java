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


package org.oscarehr.integration.hl7.model;

//typeIds
//MR=ADT MRN
//TMR=PHS Temporary MRN
//PI = ADT CPI
//PE = Enterprise EPN
//TPN = PHS Temporary EPN
//AN = ADT Patient Account Number
//TAN = PHS temporary patient account number
//NH = National health card #
//JHN = regional health card #

public class PatientId {
	private String id;
	private String authority;
	private String typeId;
	
	public PatientId() {
		
	}
	
	public PatientId(String id, String authority, String typeId) {
		this.id = id;
		this.authority = authority;
		this.typeId = typeId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String toString() {
		return "Patient Identifier: " + id + " " + authority + " " + typeId;
	}
}
