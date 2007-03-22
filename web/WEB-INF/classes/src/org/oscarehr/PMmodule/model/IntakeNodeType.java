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

import org.oscarehr.PMmodule.model.base.BaseIntakeNodeType;

public class IntakeNodeType extends BaseIntakeNodeType {

	private static final long serialVersionUID = 1L;

	public static final Integer INTAKE_ID = 1;
	public static final Integer PAGE_ID = 2;
	public static final Integer SECTION_ID = 3;
	public static final Integer QUESTION_ID = 4;
	public static final Integer ANSWER_COMPOUND_ID = 5;
	public static final Integer ANSWER_SCALAR_CHOICE_ID = 6;
	public static final Integer ANSWER_SCALAR_TEXT_ID = 7;
	public static final Integer ANSWER_SCALAR_NOTE_ID = 8;

	public IntakeNodeType(String type) {
		super(null, type);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public IntakeNodeType() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeType(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeType(java.lang.Integer id, java.lang.String type) {
		super(id, type);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean isIntakeType() {
		return isType(INTAKE_ID);
	}

	public boolean isPageType() {
		return isType(PAGE_ID);
	}

	public boolean isSectionType() {
		return isType(SECTION_ID);
	}

	public boolean isQuestionType() {
		return isType(QUESTION_ID);
	}

	public boolean isCompoundAnswerType() {
		return isType(ANSWER_COMPOUND_ID);
	}

	public boolean isScalarAnswerType() {
		return isChoiceAnswerType() || isTextAnswerType() || isNoteAnswerType();
	}

	public boolean isChoiceAnswerType() {
		return isType(ANSWER_SCALAR_CHOICE_ID);
	}

	public boolean isTextAnswerType() {
		return isType(ANSWER_SCALAR_TEXT_ID);
	}

	public boolean isNoteAnswerType() {
		return isType(ANSWER_SCALAR_NOTE_ID);
	}

	private boolean isType(Integer id) {
		return getId() != null ? getId().equals(id) : false;
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(")").toString();
	}

}