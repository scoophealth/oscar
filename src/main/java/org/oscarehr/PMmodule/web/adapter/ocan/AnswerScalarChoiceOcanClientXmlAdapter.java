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
import org.oscarehr.PMmodule.web.adapter.AbstractAnswerScalarHtmlAdapter;

public class AnswerScalarChoiceOcanClientXmlAdapter extends AbstractAnswerScalarHtmlAdapter {

	public AnswerScalarChoiceOcanClientXmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = super.getPreBuilder();
		
		if (isAnswerBoolean()) {
			String labelXML = getLabelOcanClientXML();
			if ("CNo_Need".equals(labelXML)) {
				preBuilder.
				append("<AnswerGroup>");
			}
			preBuilder.
			append("<").
			append(labelXML).
			append(">").
			append(getAnswerValue()).
			append("</").
			append(labelXML).
			append(">").
			append(EOL);
		} else {
			String labelXML = getLabelOcanClientXML();
			preBuilder.
			append("<").
			append(labelXML).
			append(">").
			append(getAnswerValue()).
			append("</").
			append(labelXML).
			append(">").
			append(EOL);

//			for (IntakeAnswerElement answerElement : getAnswerElements()) {
//				String label = answerElement.getElement();
//				String value = answerElement.getElement();
//
//				preBuilder.
//				append("<").
//				append(label).
//				append(">").
//				append(value).
//				append("</").
//				append(label).
//				append(">").
//				append(EOL);
//			}

/*
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
*/
		}
		
		return preBuilder;
	}
}
