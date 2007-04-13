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

import java.text.SimpleDateFormat;

import org.oscarehr.PMmodule.model.base.BaseAdmission;

/**
 * This is the object class that relates to the admission table. Any customizations belong here.
 */
public class Admission extends BaseAdmission {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public Admission () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Admission (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission(java.lang.Long id, java.lang.Long agencyId, java.lang.Long providerNo, java.lang.Integer clientId, java.lang.Integer programId) {
		super(id, agencyId, providerNo, clientId, programId);
	}

	/* [CONSTRUCTOR MARKER END] */

	private Program program;
	
	public void setProgram(Program p) {
		this.program = p;
	}

	public Program getProgram() {
		return program;
	}
	
	public String getAdmissionDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(this.getAdmissionDate());		
	}

}