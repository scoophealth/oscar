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

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.Closure;

public class IntakeNodeWalker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Closure NULL_CLOSURE = new Closure() { public void execute(Object o) {} };
	
	private class GetLevelClosure implements Closure {
		private int level = 0;
		
		public void execute(Object o) {
	        level += 1;
        }

		public int getLevel() {
        	return level;
        }
	}
	
	private class GetQuestionLevelClosure implements Closure {
		private int questionLevel = 0;
		
		public void execute(Object o) {
			IntakeNode leaf = (IntakeNode) o;
			
			if (leaf.isQuestion()) {
	    		IntakeNodeWalker leafWalker = new IntakeNodeWalker(leaf);
				questionLevel = leafWalker.getLevel();
			}
		}

		public int getQuestionLevel() {
			return questionLevel;
		}
	}
	
	private class GetNumLevelsClosure implements Closure {
		private SortedSet<Integer> levels = new TreeSet<Integer>();

		public void execute(Object o) {
			IntakeNode leaf = (IntakeNode) o;
			
	    	if (leaf.getChildren().isEmpty()) {
	    		IntakeNodeWalker leafWalker = new IntakeNodeWalker(leaf);
	    		levels.add(leafWalker.getLevel());
	    	}	        
        }
		
		public int getNumLevels() {
			return levels.isEmpty() ? 0 : levels.last();
		}
	}
	
	private IntakeNode node;
	
	public IntakeNodeWalker(IntakeNode node) {
		this.node = node;
	}
	
	public IntakeNode getRoot() {
		return up(node, NULL_CLOSURE);
	}
	
	public int getLevel() {
		GetLevelClosure closure = new GetLevelClosure();		
		up(node, closure);
		
		return closure.getLevel();
	}
	
	public int getQuestionLevel() {
		GetQuestionLevelClosure closure = new GetQuestionLevelClosure();
		down(getRoot().getChildren(), closure);
		
		return closure.getQuestionLevel();
	}
	
	public int getNumLevels() {
		GetNumLevelsClosure closure = new GetNumLevelsClosure();
		down(getRoot().getChildren(), closure);
		
		return closure.getNumLevels();
	}
	
	private IntakeNode up(IntakeNode node, Closure closure) {
		IntakeNode root = node;
		
		while (root.getParent() != null) {
			closure.execute(root);
			root = root.getParent();
		}
		
		return root;
	}

	private void down(List<IntakeNode> children, Closure closure) {
	    for (IntakeNode child : children) {
			closure.execute(child);
	        down(child.getChildren(), closure);
        }
    }
	
}
