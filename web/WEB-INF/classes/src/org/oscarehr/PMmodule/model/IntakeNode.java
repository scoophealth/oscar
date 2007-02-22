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
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.Closure;
import org.oscarehr.PMmodule.model.base.BaseIntakeNode;

public class IntakeNode extends BaseIntakeNode {

	private class GetLevelClosure implements Closure {
		private int level = 0;
		
		public void execute(Object o) {
	        level++;
        }

		public int getLevel() {
        	return level;
        }
	}
	
	private class GetNumLevelsClosure implements Closure {
		private SortedSet<Integer> levels = new TreeSet<Integer>();

		public void execute(Object o) {
			IntakeNode leaf = (IntakeNode) o;
			
	    	if (leaf.getChildren().isEmpty()) {
	    		levels.add(leaf.getLevel());
	    	}	        
        }
		
		public int getNumLevels() {
			return levels.isEmpty() ? 0 : levels.last();
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	private static int QUESTION_LEVEL = 3;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public IntakeNode () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNode (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNode(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeType type) {
		super(id, type);
	}

	/* [CONSTRUCTOR MARKER END] */

	public IntakeNode(IntakeNodeType type) {
		super();
		setType(type);
	}
	
	public boolean isIntake() {
		return getParent() == null && getType().isIntakeType();
	}

	public boolean isAnswer() {
		return getType().isAnswerType();
	}

	public String getIdStr() {
		return (getId() != null) ? getId().toString() : "";
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
		
		return null;
	}
	
	public Integer getNumSiblings() {
		if (getParent() != null) {
			return getParent().getChildren().size();
		}
		
		return null;
	}
	
	public String getLabel(int index) {
		return (getLabels().size() > index) ? getLabels().get(index).getLabel() : "";
	}
	
	public int getQuestionLevel() {
		return getLevel() - IntakeNode.QUESTION_LEVEL;
	}

	public int getMaxPathLength() {
		return getNumLevels() - getLevel() + 1;
	}
	
	@Override
	public void addTochildren(IntakeNode child) {
		super.addTochildren(child);
		child.setParent(this);
	}
	
	@Override
	public void addTolabels(IntakeLabel label) {
		super.addTolabels(label);
		label.setNode(this);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(REF).append("(");
		builder.append(getId()).append(", ").append(getType());
		
		for (Iterator i = getLabels().iterator(); i.hasNext();) {
	        builder.append(", ").append(i.next());
        }

		builder.append(")");

		return builder.toString();
	}
	
	@Override
	protected void initialize() {
		setChildren(new ArrayList<IntakeNode>());
		setLabels(new ArrayList<IntakeLabel>());
		setAnswers(new TreeSet<IntakeAnswer>());
		setInstances(new TreeSet<IntakeInstance>());
	}
	
	int getLevel() {
		GetLevelClosure closure = new GetLevelClosure();		
		getRoot(closure);
		
		return closure.getLevel();
	}

	int getNumLevels() {
		GetNumLevelsClosure closure = new GetNumLevelsClosure();
		walkTree(closure, getRoot().getChildren());
		
		return closure.getNumLevels();
	}

	private IntakeNode getRoot() {
		return getRoot(new Closure() { public void execute(Object o) {} });
	}
	
	private IntakeNode getRoot(Closure closure) {
		IntakeNode root = this;
		
		while (root.getParent() != null) {
			closure.execute(root);
			root = root.getParent();
		}
		
		return root;
	}

	private void walkTree(Closure closure, List<IntakeNode> children) {
	    for (IntakeNode child : children) {
			closure.execute(child);
	        walkTree(closure, child.getChildren());
        }
    }
		
}