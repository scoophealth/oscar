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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.Closure;

public class IntakeNodeWalker implements Serializable {

	private static final Closure NULL_CLOSURE = new Closure() {
		public void execute(Object o) {
			// why is this all of a sudden crapping out the compiler....
		}
	};

	private class LevelClosure implements Closure {
		private int level = 0;

		public void execute(Object o) {
			level += 1;
		}

		public int getLevel() {
			return level;
		}
	}

	private class QuestionLevelClosure implements Closure {
		private SortedSet<Integer> questionLevels = new TreeSet<Integer>();

		public void execute(Object o) {
			IntakeNode leaf = (IntakeNode) o;

			if (leaf.isQuestion()) {
				IntakeNodeWalker leafWalker = new IntakeNodeWalker(leaf);
				questionLevels.add(leafWalker.getLevel());
			}
		}

		public int getQuestionLevel() {
			return questionLevels.isEmpty() ? 0 : questionLevels.first();
		}
	}

	private class NumLevelsClosure implements Closure {
		private SortedSet<Integer> levels = new TreeSet<Integer>();

		public void execute(Object o) {
			IntakeNode leaf = (IntakeNode) o;

			if (leaf != null && leaf.getChildren().isEmpty()) {
				IntakeNodeWalker leafWalker = new IntakeNodeWalker(leaf);
				levels.add(leafWalker.getLevel());
			}
		}

		public int getNumLevels() {
			return levels.isEmpty() ? 0 : levels.last();
		}
	}

	private class QuestionsWithChoiceAnswersClosure implements Closure {
		private Set<IntakeNode> questionsWithChoiceAnswers = new LinkedHashSet<IntakeNode>();

		public void execute(Object o) {
			IntakeNode parent = (IntakeNode) o;

			for (IntakeNode child : parent.getChildren()) {
				if (child.isAnswerChoice()) {
					questionsWithChoiceAnswers.add(parent);
					break;
				}
			}
		}

		public Set<IntakeNode> getQuestionsWithChoiceAnswers() {
			return questionsWithChoiceAnswers;
		}
	}

	private class ChoiceAnswerIdsClosure implements Closure {
		private SortedSet<Integer> choiceAnswerIds = new TreeSet<Integer>();

		public void execute(Object o) {
			IntakeNode node = (IntakeNode) o;

			if (node.isAnswerChoice()) {
				choiceAnswerIds.add(node.getId());
			}
		}

		public SortedSet<Integer> getChoiceAnswerIds() {
			return choiceAnswerIds;
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
		LevelClosure closure = new LevelClosure();
		up(node, closure);

		return closure.getLevel();
	}

	public int getQuestionLevel() {
		QuestionLevelClosure closure = new QuestionLevelClosure();
		up(node, closure);

		return closure.getQuestionLevel();
	}

	public int getNumLevels() {
		NumLevelsClosure closure = new NumLevelsClosure();
		down(getRoot().getChildren(), closure);

		return closure.getNumLevels();
	}

	public Set<IntakeNode> getQuestionsWithChoiceAnswers() {
		QuestionsWithChoiceAnswersClosure closure = new QuestionsWithChoiceAnswersClosure();
		down(getRoot().getChildren(), closure);

		return closure.getQuestionsWithChoiceAnswers();
	}

	public SortedSet<Integer> getChoiceAnswerIds() {
		ChoiceAnswerIdsClosure closure = new ChoiceAnswerIdsClosure();
		down(getRoot().getChildren(), closure);

		return closure.getChoiceAnswerIds();
	}

	private IntakeNode up(IntakeNode node, Closure closure) {
		//if(node == null)
		
		IntakeNode root = node;
		
		while (root.getParent() != null) {
			closure.execute(root);
			root = root.getParent();
		}

		return root;
	}

	private void down(List<IntakeNode> children, Closure closure) {
		for (IntakeNode child : children) {
			if (child != null) {
				closure.execute(child);
				down(child.getChildren(), closure);
			}
		}
	}

}
