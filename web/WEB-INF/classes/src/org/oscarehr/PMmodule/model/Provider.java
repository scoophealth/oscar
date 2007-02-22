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

import org.oscarehr.PMmodule.model.base.BaseProvider;

/**
 * This is the object class that relates to the provider table. Any customizations belong here.
 */
public class Provider extends BaseProvider {

	private static final long serialVersionUID = 1L;

	public static final String SYSTEM_PROVIDER_NO = "-1";

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Provider() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Provider(java.lang.String _providerNo) {
		super(_providerNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Provider(java.lang.String _providerNo, java.lang.String _lastName, java.lang.String _providerType, java.lang.String _sex, java.lang.String _specialty, java.lang.String _firstName) {
		super(_providerNo, _lastName, _providerType, _sex, _specialty, _firstName);
	}
	/* [CONSTRUCTOR MARKER END] */

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public String getProvider_no() {
		return getProviderNo();
	}
}