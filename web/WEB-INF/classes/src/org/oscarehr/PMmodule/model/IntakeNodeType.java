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
import org.oscarehr.PMmodule.web.adapter.AnswerCompoundHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.AnswerScalarChoiceHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.AnswerScalarTextHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.IntakeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.PageHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.QuestionHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.SectionHtmlAdapter;

public class IntakeNodeType extends BaseIntakeNodeType {

	private static final long serialVersionUID = 1L;

	private static final Integer INTAKE_ID = 1;
	private static final Integer PAGE_ID = 2;
	private static final Integer SECTION_ID = 3;
	private static final Integer QUESTION_ID = 4;
	private static final Integer ANSWER_COMPOUND_ID = 5;
	private static final Integer ANSWER_SCALAR_CHOICE_ID = 6;
	private static final Integer ANSWER_SCALAR_TEXT_ID = 7;

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

	public boolean isQuestionType() {
		return isType(QUESTION_ID);
	}

	public boolean isScalarAnswerType() {
		return isType(ANSWER_SCALAR_CHOICE_ID) || isType(ANSWER_SCALAR_TEXT_ID);
	}

	public IntakeNodeHtmlAdapter getHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter htmlAdapter = null;

		if (isType(INTAKE_ID)) {
			htmlAdapter = new IntakeHtmlAdapter(indent, node);
		} else if (isType(PAGE_ID)) {
			htmlAdapter = new PageHtmlAdapter(indent, node);
		} else if (isType(SECTION_ID)) {
			htmlAdapter = new SectionHtmlAdapter(indent, node);
		} else if (isType(QUESTION_ID)) {
			htmlAdapter = new QuestionHtmlAdapter(indent, node);
		} else if (isType(ANSWER_COMPOUND_ID)) {
			htmlAdapter = new AnswerCompoundHtmlAdapter(indent, node);
		} else if (isType(ANSWER_SCALAR_CHOICE_ID)) {
			htmlAdapter = new AnswerScalarChoiceHtmlAdapter(indent, node, intake);
		} else if (isType(ANSWER_SCALAR_TEXT_ID)) {
			htmlAdapter = new AnswerScalarTextHtmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No html adapter for type: " + getType());
		}

		return htmlAdapter;
	}

	private boolean isType(Integer id) {
		return getId() != null ? getId().equals(id) : false;
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(")").toString();
	}

}