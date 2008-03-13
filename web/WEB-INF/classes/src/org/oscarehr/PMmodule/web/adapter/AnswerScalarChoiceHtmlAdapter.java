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
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;

public class AnswerScalarChoiceHtmlAdapter extends AbstractAnswerScalarHtmlAdapter {

	public AnswerScalarChoiceHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());
		
		if (isAnswerBoolean()) {
			indent(preBuilder).append(startLabel(false)).append(createCheckBox(IntakeAnswerElement.TRUE)).append(endLabel(true)).append(EOL);
			String mquest = "mquests";
			if (getNoOfSibling()>1) mquest = "mquestm";
			String pId = "_" + getParent().getId();
			if (getParent().getMandatory()) {
			    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
			}
		} else {
			indent(preBuilder).append(startLabel(true)).append(String.format("<select name=\"intake.answerMapped(%s).value\">", new Object[] { getId() })).append(EOL);
			beginTag();

			indent(preBuilder).append(createOption("", "Declined")).append(EOL);

			for (IntakeAnswerElement answerElement : getAnswerElements()) {
				String label = answerElement.getElement();
				String value = answerElement.getElement();

				indent(preBuilder).append(createOption(label, value)).append(EOL);
			}

			endTag();
			indent(preBuilder).append("</select>").append(endLabel(false)).append(EOL);
			String mquest = "mquests";
			if (getNoOfSibling()>1) mquest = "mquestm";
			String pId = "_" + getParent().getId();
			if (getParent().getMandatory()) {
			    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
			}
		}
		
		return endAnswer(preBuilder);
	}
	
	private StringBuilder createCheckBox(String on) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("<input type=\"checkbox\" name=\"intake.answerMapped(%s).value\" value=\"%s\"", new Object[] { getId(), on }));
		
		if (getAnswerValue().equalsIgnoreCase(on)) {
			builder.append(" checked=\"checked\"");
		}
		
		builder.append(" />");
		
		return builder;
	}

	private StringBuilder createOption(String label, String value) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("<option value=\"%s\"", new Object[] { value }));

		if (getAnswerValue().equalsIgnoreCase(value)) {
			builder.append(" selected=\"selected\"");
		}

		builder.append(" >").append(String.format("%s</option>", new Object[] { label }));

		return builder;
	}
		
}