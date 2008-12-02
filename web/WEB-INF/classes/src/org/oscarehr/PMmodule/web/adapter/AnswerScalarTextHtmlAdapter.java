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

public class AnswerScalarTextHtmlAdapter extends AbstractAnswerScalarHtmlAdapter {
	
	private static final String CLASS_INTAKE_INPUT = "intakeInput";
	
	public AnswerScalarTextHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());

		indent(preBuilder).
			append("<tr><td>").
			append(startLabel(true)).
			append(endLabel(false)).
			append("</td>").
			append(getTextInput(getId(), getAnswerValue(), isParentQuestion())).
			append("</tr>").
			append(EOL);
		
		String mquest = "mquests";
		if (getNoOfSibling()>1) mquest = "mquestm";
		String pId = "_" + getParent().getId();
		if (getParent().getMandatory()) {
		    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
		}

		return endAnswer(preBuilder);
	}

	private String getTextInput(String id, String value, boolean grabHorizontal) {
		if (grabHorizontal) {
			return String.format("<td><input type=\"text\" class=\"%s\" name=\"intake.answerMapped(%s).value\" value=\"%s\"></input></td>", new Object[] { CLASS_INTAKE_INPUT, id, value });
		} else {
			return String.format("<td><input type=\"text\" name=\"intake.answerMapped(%s).value\" value=\"%s\"></input></td>", new Object[] { id, value });
		}
		
	}
	
}