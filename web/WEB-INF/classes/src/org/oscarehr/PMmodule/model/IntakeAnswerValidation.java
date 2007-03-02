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

import org.oscarehr.PMmodule.model.base.BaseIntakeAnswerValidation;

public class IntakeAnswerValidation extends BaseIntakeAnswerValidation {

	private static final long serialVersionUID = 1L;

	public IntakeAnswerValidation(String type) {
		super(null, type);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public IntakeAnswerValidation() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeAnswerValidation(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeAnswerValidation(java.lang.Integer id, java.lang.String type) {
		super(id, type);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(")").toString();
	}

}