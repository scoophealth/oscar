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

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;

public class HtmlAdapterFactory {

	public IntakeNodeHtmlAdapter getHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter htmlAdapter = null;

		if (node.isIntake()) {
			htmlAdapter = new IntakeHtmlAdapter(indent, node);
		} else if (node.isPage()) {
			htmlAdapter = new PageHtmlAdapter(indent, node);
		} else if (node.isSection()) {
			htmlAdapter = new SectionHtmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			htmlAdapter = new QuestionHtmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			htmlAdapter = new AnswerCompoundHtmlAdapter(indent, node);
		} else if (node.isAnswerChoice() && node.isAnswerBoolean()) {
			htmlAdapter = new AnswerScalarChoiceBooleanHtmlAdapter(indent, node, intake);
		} else if(node.isAnswerChoice() && !node.isAnswerBoolean()) {
			htmlAdapter = new AnswerScalarChoiceDropDownHtmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			htmlAdapter = new AnswerScalarTextHtmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			htmlAdapter = new AnswerScalarNoteHtmlAdapter(indent, node, intake);
		} else if (node.isAnswerDate()) {
			htmlAdapter = new AnswerScalarDateHtmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No html adapter for type: " + node.getType());
		}

		return htmlAdapter;
	}
	
}
