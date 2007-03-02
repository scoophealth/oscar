/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseIntakeNodeLabel;

public class IntakeNodeLabel extends BaseIntakeNodeLabel {

	private static final long serialVersionUID = 1L;

	public IntakeNodeLabel(String label) {
		super(null, label);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public IntakeNodeLabel() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeLabel(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeLabel(java.lang.Integer id, java.lang.String label) {
		super(id, label);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getLabel()).append(")").toString();
	}

}