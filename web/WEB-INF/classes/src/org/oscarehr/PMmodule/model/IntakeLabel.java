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

import org.oscarehr.PMmodule.model.base.BaseIntakeLabel;

public class IntakeLabel extends BaseIntakeLabel {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public IntakeLabel () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeLabel (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeLabel(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.String label) {
		super(id, node, label);
	}

	/* [CONSTRUCTOR MARKER END] */
	
	public IntakeLabel(String label) {
		super();
		setLabel(label);
	}
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("(");
		builder.append(getId()).append(", ");
		builder.append(getLabel());
		builder.append(")");
		
		return builder.toString();
	}

}