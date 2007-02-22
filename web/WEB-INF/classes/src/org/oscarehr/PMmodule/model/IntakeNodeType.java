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

import org.apache.commons.collections.CollectionUtils;
import org.oscarehr.PMmodule.model.base.BaseIntakeNodeType;
import org.oscarehr.PMmodule.web.adapter.BooleanAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.DateAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.DefaultIntakeNodeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.EmailAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.IntegerAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.OneOfBooleanAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.PageTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.PhoneAnswerTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.QuestionTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.SectionTypeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.StringAnswerTypeHtmlAdapter;

public class IntakeNodeType extends BaseIntakeNodeType {

	private static final long serialVersionUID = 1L;

	private static final Integer QUICK_INTAKE_ID = 1;
	private static final Integer INDEPTH_INTAKE_ID = 2;
	private static final Integer PROGRAM_INTAKE_ID = 3;
	private static final Integer PAGE_ID = 4;
	private static final Integer SECTION_ID = 5;
	private static final Integer QUESTION_ID = 6;
	private static final Integer QUESTION_SINGLE_CHOICE_ID = 7;
	private static final Integer QUESTION_MULTIPLE_CHOICE_ID = 8;
	private static final Integer ANSWER_BOOLEAN_ID = 9;
	private static final Integer ANSWER_DATE_ID = 10;
	private static final Integer ANSWER_INTEGER_ID = 11;
	private static final Integer ANSWER_STRING_ID = 12;
	private static final Integer ANSWER_PHONE_ID = 13;
	private static final Integer ANSWER_EMAIL_ID = 14;

	private static final Set<Integer> INTAKE_IDS = new HashSet<Integer>();
	private static final Set<Integer> QUESTION_IDS = new HashSet<Integer>();
	private static final Set<Integer> ANSWER_IDS = new HashSet<Integer>();

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
	public IntakeNodeType(java.lang.Integer id, java.lang.String name) {
		super(id, name);
	}
	
	/* [CONSTRUCTOR MARKER END] */

	public IntakeNodeType(String name) {
		super(null, name);
	}
	
	@Override
	protected void initialize() {
		super.initialize();

		CollectionUtils.addAll(INTAKE_IDS, new Object[] { QUICK_INTAKE_ID, INDEPTH_INTAKE_ID, PROGRAM_INTAKE_ID });
		CollectionUtils.addAll(QUESTION_IDS, new Object[] { QUESTION_ID, QUESTION_SINGLE_CHOICE_ID, QUESTION_MULTIPLE_CHOICE_ID });
		CollectionUtils.addAll(ANSWER_IDS, new Object[] { ANSWER_BOOLEAN_ID, ANSWER_DATE_ID, ANSWER_INTEGER_ID, ANSWER_STRING_ID, ANSWER_PHONE_ID, ANSWER_EMAIL_ID });
	}

	public boolean isIntakeType() {
		return isType(INTAKE_IDS);
	}

	public boolean isPageType() {
		return isType(PAGE_ID);
	}

	public boolean isSectionType() {
		return isType(SECTION_ID);
	}

	public boolean isSingleChoiceType() {
		return isType(QUESTION_SINGLE_CHOICE_ID);
	}
	
	public boolean isAnswerType() {
		return isType(ANSWER_IDS);
	}
	
	public IntakeNodeHtmlAdapter getHtmlAdapter(int indent, IntakeInstance instance, IntakeNode node) {
		IntakeNodeHtmlAdapter htmlAdapter = null;

		if (isIntakeType()) {
			htmlAdapter = new DefaultIntakeNodeHtmlAdapter(indent);
		} else if (isPageType()) {
			htmlAdapter = new PageTypeHtmlAdapter(indent, instance, node);
		} else if (isSectionType()) {
			htmlAdapter = new SectionTypeHtmlAdapter(indent, instance, node);
		} else if (isType(QUESTION_IDS)) {
			htmlAdapter = new QuestionTypeHtmlAdapter(indent, instance, node);
		} else if (node.getParent().getType().isSingleChoiceType()) {
			htmlAdapter = new OneOfBooleanAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_BOOLEAN_ID)) {
			htmlAdapter = new BooleanAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_DATE_ID)) {
			htmlAdapter = new DateAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_INTEGER_ID)) {
			htmlAdapter = new IntegerAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_STRING_ID)) {
			htmlAdapter = new StringAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_PHONE_ID)) {
			htmlAdapter = new PhoneAnswerTypeHtmlAdapter(instance, node, indent);
		} else if (isType(ANSWER_EMAIL_ID)) {
			htmlAdapter = new EmailAnswerTypeHtmlAdapter(instance, node, indent);
		} else {
			throw new IllegalStateException("No html adapter for type: " + getName());
		}

		return htmlAdapter;
	}

	private boolean isType(Integer typeId) {
		if (getId() != null) {
			return typeId.equals(getId());
		}

		return false;
	}

	private boolean isType(Set<Integer> typeIds) {
		if (getId() != null) {
			return typeIds.contains(getId());
		}

		return false;
	}
	
	@Override
	public String toString() {
	    return getName();
	}

}