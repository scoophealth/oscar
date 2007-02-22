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

import org.oscarehr.PMmodule.model.base.BaseConsent;

/**
 * This is the object class that relates to the consent table.
 * Any customizations belong here.
 */
public class Consent extends BaseConsent {

	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Consent() {
		super();
		this.setHardcopy(false);
		this.setSignatureDeclaration(false);
		this.setRefusedToSign(false);
	}

	/**
	 * Constructor for primary key
	 */
	public Consent(java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Consent(java.lang.Long _id, java.lang.Long _demographicNo, java.lang.String _providerNo) {
		super(_id, _demographicNo, _providerNo);
	}
	/*[CONSTRUCTOR MARKER END]*/

	private String optout;
	private String exclusionString;

	public String getOptout() {
		return optout;
	}

	public void setOptout(String optout) {
		this.optout = optout;
	}

	public String getExclusionString() {
		return exclusionString;
	}

	public void setExclusionString(String exclusionString) {
		this.exclusionString = exclusionString;
	}

}