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

import org.oscarehr.PMmodule.model.base.BaseConsentInterview;

/**
 * This is the object class that relates to the consent_interview table.
 * Any customizations belong here.
 */
public class ConsentInterview extends BaseConsentInterview {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ConsentInterview () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ConsentInterview (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ConsentInterview (
		java.lang.Long _id,
		long _consentId,
		java.lang.Long _demographicNo,
		java.lang.String _providerNo) {

		super (
			_id,
			_consentId,
			_demographicNo,
			_providerNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}