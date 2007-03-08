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
import org.oscarehr.PMmodule.model.base.BaseIntakeAnswer;

public class IntakeAnswer extends BaseIntakeAnswer implements Comparable<IntakeAnswer> {

	private static final long serialVersionUID = 1L;
	
	public static IntakeAnswer create(IntakeNode node) {
		IntakeAnswer answer = new IntakeAnswer();
		answer.setNode(node);

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
	public IntakeAnswer(java.lang.Integer id, org.oscarehr.PMmodule.model.Intake intake, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.String value) {
		super(id, intake, node, value);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	public String getValue() {
		return super.getValue() != null ? super.getValue() : "";
	}
	
	public boolean isAnswerScalar() {
		return getNode().isAnswerScalar();
	}
	
	public boolean isAnswerCompound() {
		return getNode().isAnswerCompound();
	}
	
	public boolean isParentQuestion() {
		return getNode().getParent().isQuestion();
	}
	
	public IntakeNode getQuestion() {
		IntakeNode question = null;
		
		IntakeNode parent = getNode().getParent();
		IntakeNode grandParent = getNode().getGrandParent();
		
		if (parent.isQuestion()) {
			question = parent;
		} else if (grandParent.isQuestion()) {
			question = grandParent;
		}
		
		return question;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntakeAnswer answer) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getId(), answer.getId());
		compareToBuilder.append(getNode().getId(), answer.getNode().getId());
		
		return compareToBuilder.toComparison();
	}
	
	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getValue()).append(")").toString();
	}

}