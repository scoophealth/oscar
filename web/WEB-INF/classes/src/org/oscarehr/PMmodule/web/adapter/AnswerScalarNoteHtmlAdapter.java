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

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;

public class AnswerScalarNoteHtmlAdapter extends AbstractAnswerScalarHtmlAdapter {
	
	private static final Integer COLS = 50;
	private static final Integer ROWS = 7;
	
	public AnswerScalarNoteHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());

		indent(preBuilder).append(getTextInput(getId(), COLS, ROWS, getAnswerValue())).append(EOL);
		indent(preBuilder).append(getLabel()).append(EOL);

		return endAnswer(preBuilder);
	}

	private String getTextInput(String id, Integer cols, Integer rows, String value) {
		return String.format("<textarea name=\"intake.answerMapped(%s).value\" cols=\"%s\" rows=\"%s\">%s</textarea>", new Object[] { id, cols, rows, value });
		
	}

}