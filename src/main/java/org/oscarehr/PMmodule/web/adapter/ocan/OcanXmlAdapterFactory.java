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

package org.oscarehr.PMmodule.web.adapter.ocan;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;

public class OcanXmlAdapterFactory {

	public IntakeNodeHtmlAdapter getOcanClientXmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter xmlAdapter = null;

		if (node.isIntake()) {
			xmlAdapter = new IntakeOcanClientXmlAdapter(indent, node);
		} else if (node.isPage()) {
			xmlAdapter = new PageOcanClientXmlAdapter(indent, node);
		} else if (node.isSection()) {
			xmlAdapter = new SectionOcanClientXmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			xmlAdapter = new QuestionOcanClientXmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			xmlAdapter = new AnswerCompoundOcanXmlAdapter(indent, node);
		} else if (node.isAnswerChoice()) {
			xmlAdapter = new AnswerScalarChoiceOcanClientXmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			xmlAdapter = new AnswerScalarTextOcanClientXmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			xmlAdapter = new AnswerScalarNoteOcanClientXmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No ocan xml adapter for type: " + node.getType());
		}

		return xmlAdapter;
	}
	
	public IntakeNodeHtmlAdapter getOcanStaffXmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter xmlAdapter = null;

		if (node.isIntake()) {
			xmlAdapter = new IntakeOcanStaffXmlAdapter(indent, node);
		} else if (node.isPage()) {
			xmlAdapter = new PageOcanStaffXmlAdapter(indent, node);
		} else if (node.isSection()) {
			xmlAdapter = new SectionOcanStaffXmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			xmlAdapter = new QuestionOcanStaffXmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			xmlAdapter = new AnswerCompoundOcanXmlAdapter(indent, node);
		} else if (node.isAnswerChoice()) {
			xmlAdapter = new AnswerScalarChoiceOcanStaffXmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			xmlAdapter = new AnswerScalarTextOcanStaffXmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			xmlAdapter = new AnswerScalarNoteOcanStaffXmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No ocan xml adapter for type: " + node.getType());
		}

		return xmlAdapter;
	}
}
