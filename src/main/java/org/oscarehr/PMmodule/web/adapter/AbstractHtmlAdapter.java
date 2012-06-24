/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.adapter;

import java.util.Set;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;

abstract public class AbstractHtmlAdapter implements IntakeNodeHtmlAdapter {

	protected static final String TAB = "\t";
	protected static final String SPACE = " ";
	protected static final String EOL = System.getProperty("line.separator");

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

	protected boolean hasSections() {
		return node.hasSections();
	}

	protected String getId() {
		return node.getIdStr();
	}
	
	protected String getQuestionId() {
		String qid = node.getQuestionId();
		return (qid!=null)?qid:"";
	}
	
	protected String getLabel() {
		return node.getLabelStr();
	}
	
	protected String getLabelOcanClientXML() {
		String label = 'C' + node.getLabelStr().
			replace(' ', '_').
			replace('.', '_').
			replace(',', '_').
			replace(':', '_').
			replace(';', '_').
			replace('\'', '_').
			replace('"', '_').
			replace('(', '_').
			replace(')', '_').
			replace('<', '_').
			replace('>', '_').
			replace('`', '_').
			replace('~', '_').
			replace('@', '_').
			replace('#', '_').
			replace('$', '_').
			replace('%', '_').
			replace('^', '_').
			replace('&', '_').
			replace('*', '_').
			replace('-', '_').
			replace('+', '_').
			replace('=', '_').
			replace('[', '_').
			replace(']', '_').
			replace('{', '_').
			replace('}', '_').
			replace('/', '_').
			replace('\\', '_').
			replace('|', '_').
			replace('!', '_').
			replace('?', '_');
		return label.substring(0, Math.min(label.length(), 96));
	}
	
	protected String getLabelOcanXML() {
		String label = 'C' + node.getLabelStr().trim().
			replace(' ', '_').
			replace('.', '_').
			replace(',', '_').
			replace(':', '_').
			replace(';', '_').
			replace('\'', '_').
			replace('"', '_').
			replace('(', '_').
			replace(')', '_').
			replace('<', '_').
			replace('>', '_').
			replace('`', '_').
			replace('~', '_').
			replace('@', '_').
			replace('#', '_').
			replace('$', '_').
			replace('%', '_').
			replace('^', '_').
			replace('&', '_').
			replace('*', '_').
			replace('-', '_').
			replace('+', '_').
			replace('=', '_').
			replace('[', '_').
			replace(']', '_').
			replace('{', '_').
			replace('}', '_').
			replace('/', '_').
			replace('\\', '_').
			replace('|', '_').
			replace('!', '_').
			replace('?', '_');
		return label.substring(0, Math.min(label.length(), 96));
	}
	
	protected boolean getMandatory() {
		return node.getMandatory();
	}
	
	protected boolean getRepeating() {
		return node.getRepeating();
	}
	
	protected String getValidations() {
		return node.getValidations();
	}
	
	protected Integer getPos() {
		return node.getPos();
	}
	
	protected Integer getNoOfSibling() {
		return node.getParent().getChildren().size();
	}
	
	protected IntakeNode getParent() {
		return node.getParent();
	}
	
	protected int getDistanceToQuestionLevel() {
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
	
	protected String getRepeatingAnswerValue(int index) {
		IntakeAnswer answer = intake.getRepeatingAnswerMapped(getId(),index,false);
		if(answer != null) {
			return answer.getValue();
		}
		return null;
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
