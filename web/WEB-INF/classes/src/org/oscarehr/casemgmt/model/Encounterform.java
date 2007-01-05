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

package org.oscarehr.casemgmt.model;

import org.oscarehr.casemgmt.model.base.BaseEncounterform;

/**
 * This is the object class that relates to the encounterform table.
 * Any customizations belong here.
 */
public class Encounterform extends BaseEncounterform {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Encounterform () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Encounterform (java.lang.String _formValue) {
		super(_formValue);
	}

	/**
	 * Constructor for required fields
	 */
	public Encounterform (
		java.lang.String _formValue,
		java.lang.Integer _hidden,
		java.lang.String _formName,
		java.lang.String _formTable) {

		super (
			_formValue,
			_hidden,
			_formName,
			_formTable);
	}

/*[CONSTRUCTOR MARKER END]*/
}