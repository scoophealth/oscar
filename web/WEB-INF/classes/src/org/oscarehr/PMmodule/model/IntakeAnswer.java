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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseIntakeAnswer;

public class IntakeAnswer extends BaseIntakeAnswer implements Comparable<IntakeAnswer> {

	private static final long serialVersionUID = 1L;

	public static IntakeAnswer create(IntakeInstance instance, IntakeNode node) {
		IntakeAnswer answer = new IntakeAnswer();
		answer.setNode(node);
		answer.setInstance(instance);

		return answer;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public IntakeAnswer() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeAnswer(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeAnswer(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeInstance instance, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.String value) {
		super(id, instance, node, value);
	}
	
	/* [CONSTRUCTOR MARKER END] */
	
	public boolean isEqual(String value) {
		return (getValue() != null) ? getValue().equalsIgnoreCase(value) : false;
	}
	
	@Override
	public void setInstance(IntakeInstance instance) {
	    super.setInstance(instance);
	    instance.addToanswers(this);
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntakeAnswer answer) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getId(), answer.getId());
		compareToBuilder.append(getInstance().getId(), answer.getInstance().getId());
		compareToBuilder.append(getNode().getId(), answer.getNode().getId());

		return compareToBuilder.toComparison();
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		toStringBuilder.append(PROP_ID, getId());
		toStringBuilder.append(PROP_VALUE, getValue());

		return toStringBuilder.toString();
	}

}