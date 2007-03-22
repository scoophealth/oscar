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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.oscarehr.PMmodule.model.base.BaseIntakeNode;

public class IntakeNode extends BaseIntakeNode {

	private static final long serialVersionUID = 1L;

	private IntakeNodeWalker walker;

	public IntakeNode(IntakeNodeTemplate nodeTemplate) {
		super(null, nodeTemplate);
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */

	public IntakeNode() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNode(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNode(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate) {
		super(id, nodeTemplate);
	}

	/* [CONSTRUCTOR MARKER END] */

	@Override
	protected void initialize() {
		setChildren(new ArrayList<IntakeNode>());
		setIntakes(new TreeSet<Intake>());
		setAnswers(new TreeSet<IntakeAnswer>());

		walker = new IntakeNodeWalker(this);
	}

	@Override
	public void addTochildren(IntakeNode child) {
		child.setParent(this);
		super.addTochildren(child);
	}
	
	public boolean isIntake() {
		return getParent() == null && getNodeTemplate().isIntake();
	}

	public boolean isPage() {
		return getNodeTemplate().isPage();
	}

	public boolean isQuestion() {
		return getNodeTemplate().isQuestion();
	}

	public boolean isSection() {
		return getNodeTemplate().isSection();
	}

	public boolean isAnswerCompound() {
		return getNodeTemplate().isAnswerCompound();
	}

	public boolean isAnswerScalar() {
		return getNodeTemplate().isAnswerScalar();
	}
	
	public boolean isAnswerChoice() {
		return getNodeTemplate().isAnswerChoice();
	}

	public boolean isAnswerText() {
		return getNodeTemplate().isAnswerText();
	}

	public boolean isAnswerNote() {
		return getNodeTemplate().isAnswerNote();
	}

	public boolean isAnswerBoolean() {
		return getNodeTemplate().isAnswerBoolean();
	}

	public boolean hasPages() {
		for (IntakeNode child : getChildren()) {
			return child.isPage();
		}

		return false;
	}
	
	public IntakeNode getGrandParent() {
		return getParent() != null ? getParent().getParent() : null;
	}
	
	public String getType() {
		return getNodeTemplate().getType().getType();
    }
	
	public String getIdStr() {
		return getId() != null ? getId().toString() : "";
	}

	public String getLabelStr() {
		return getLabel() != null ? getLabel().getLabel() : getNodeTemplate().getLabelStr();
	}

	public Integer getIndex() {
		if (getParent() != null) {
			List<IntakeNode> siblings = getParent().getChildren();

			for (int i = 0; i < siblings.size(); i++) {
				IntakeNode sibling = siblings.get(i);

				if (sibling.equals(this)) {
					return i;
				}
			}
		}

		return 0;
	}

	public int getLevel() {
		return walker.getLevel();
	}

	public int getQuestionLevel() {
		return walker.getQuestionLevel();
	}

	public int getNumLevels() {
		return walker.getNumLevels();
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getLabel()).append(", ").append(getNodeTemplate()).append(")").toString();
	}

}