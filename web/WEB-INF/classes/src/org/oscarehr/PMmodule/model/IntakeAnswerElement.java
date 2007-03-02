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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.oscarehr.PMmodule.model.base.BaseIntakeAnswerElement;

public class IntakeAnswerElement extends BaseIntakeAnswerElement implements Comparable<IntakeAnswerElement> {

	private static final long serialVersionUID = 1L;

	public IntakeAnswerElement(String element) {
		super(null, null, element);
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */

	public IntakeAnswerElement() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeAnswerElement(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeAnswerElement(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate, java.lang.String element) {
		super(id, nodeTemplate, element);
	}

	/* [CONSTRUCTOR MARKER END] */

	public String getValidationStr() {
		return getValidation() != null ? getValidation().getType() : "";
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntakeAnswerElement answerElement) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getId(), answerElement.getId());
		compareToBuilder.append(getElement(), answerElement.getElement());

		return compareToBuilder.toComparison();
	}
	
	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getElement()).append(", ").append(getValidation()).append(")").toString();
	}

}