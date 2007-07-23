/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseProgram;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class Program extends BaseProgram {

	private static final long serialVersionUID = 1L;

	public static final Integer DEFAULT_COMMUNITY_PROGRAM_ID = new Integer(10010);
	
	public static final String BED_TYPE = "Bed";
	public static final String COMMUNITY_TYPE = "community";
	public static final String SERVICE_TYPE = "Service";
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public Program() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Program(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Program(java.lang.Integer id, java.lang.Integer maxAllowed, java.lang.String address, java.lang.String phone, java.lang.String fax, java.lang.String url, java.lang.String email, java.lang.String emergencyNumber, java.lang.String name, java.lang.Long agencyId, boolean holdingTank, java.lang.String programStatus) {
		super(id, maxAllowed, address, phone, fax, url, email, emergencyNumber, name, agencyId, holdingTank, programStatus);
	}

	/* [CONSTRUCTOR MARKER END] */
	
	public boolean isActive() {
		return getProgramStatus().equalsIgnoreCase("active");
	}
	
	public boolean isFull() {
		return getNumOfMembers() >= getMaxAllowed();
	}
	
	public boolean isBed() {
		return BED_TYPE.equalsIgnoreCase(getType());
	}
	
	public boolean isCommunity() {
		return COMMUNITY_TYPE.equalsIgnoreCase(getType());
	}
	
	public boolean isService() {
		return SERVICE_TYPE.equalsIgnoreCase(getType());
	}

	public boolean getHoldingTank() {
		return isHoldingTank();
	}
}