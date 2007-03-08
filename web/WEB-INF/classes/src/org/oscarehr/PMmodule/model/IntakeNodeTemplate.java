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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.oscarehr.PMmodule.model.base.BaseIntakeNodeTemplate;

public class IntakeNodeTemplate extends BaseIntakeNodeTemplate {

	private static final long serialVersionUID = 1L;

	public IntakeNodeTemplate(IntakeNodeType type) {
		super(null, type);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public IntakeNodeTemplate() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeTemplate(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeTemplate(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeType type) {
		super(id, type);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	protected void initialize() {
		setAnswerElements(new TreeSet<IntakeAnswerElement>());
	}
	
	@Override
	public void addToanswerElements(IntakeAnswerElement intakeAnswerElement) {
		intakeAnswerElement.setNodeTemplate(this);
		super.addToanswerElements(intakeAnswerElement);
	}
	
	public boolean isIntake() {
		return getType().isIntakeType();
	}

	public boolean isPage() {
		return getType().isPageType();
	}

	public boolean isQuestion() {
		return getType().isQuestionType();
	}

	public boolean isAnswerCompound() {
		return getType().isCompoundAnswerType();
	}

	public boolean isAnswerScalar() {
		return getType().isScalarAnswerType();
	}

	public boolean isAnswerBoolean() {
		Set<String> elements = new HashSet<String>();
		
		if (getAnswerElements().size() == 2) {
			for (IntakeAnswerElement answerElement : getAnswerElements()) {
				elements.add(answerElement.getElement());
			}
		}
		
		return elements.contains(IntakeAnswerElement.TRUE) && elements.contains(IntakeAnswerElement.FALSE);
	}

	public String getLabelStr() {
		return getLabel() != null ? getLabel().getLabel() : "";
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(", ").append(getLabel()).append(")").toString();
	}

}