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
package org.oscarehr.PMmodule.web.adapter;

import java.util.Set;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;

abstract class AbstractHtmlAdapter implements IntakeNodeHtmlAdapter {

	protected static final String EOL = System.getProperty("line.separator");
	protected static final String TAB = "\t";

	private int indent;
	private IntakeNode node;
	private Intake intake;
	
	protected AbstractHtmlAdapter(int indent, IntakeNode node) {
		this(indent, node, null);
	}
	
	protected AbstractHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		this.indent = indent;
		this.intake = intake;
		this.node = node;
	}
	
	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getIndent()
	 */
	public int getIndent() {
		return indent;
	}
	
	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		return new StringBuilder();
	}
	
	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		return new StringBuilder();
	}
	
	// Node
	
	protected boolean isFirstChild() {
		return node.getIndex() == 0;
	}

	protected boolean isParentIntake() {
		return node.getParent().isIntake();
	}

	protected boolean isParentQuestion() {
		return node.getParent().isQuestion();
	}

	protected boolean isAnswerBoolean() {
		return node.isAnswerBoolean();
	}
	
	protected boolean hasPages() {
		return node.hasPages();
	}

	protected String getId() {
		return node.getIdStr();
	}
	
	protected String getLabel() {
		return node.getLabelStr();
	}
	
	protected int getNestedQuestionLevel() {
		return node.getLevel() - node.getQuestionLevel();
	}
	
	protected int getDistanceToMaxLevel() {
		return node.getNumLevels() - node.getLevel() + 1;
	}
	
	protected String getValidationType() {
		return !getAnswerElements().isEmpty() ? getAnswerElements().iterator().next().getValidationStr() : "";
	}
	
	protected Set<IntakeAnswerElement> getAnswerElements() {
		return node.getNodeTemplate().getAnswerElements();
	}
	
	// Intake
	
	protected String getAnswerValue() {
		return intake.getAnswerMapped(getId()).getValue();
	}
	
	// Indent
	
	protected void beginTag() {
		indent += 1;
	}

	protected void endTag() {
		indent -= 1;
	}

	protected StringBuilder indent(StringBuilder builder) {
		for (int i = 0; i < indent; i++) {
			builder.append(TAB);
		}

		return builder;
	}

}