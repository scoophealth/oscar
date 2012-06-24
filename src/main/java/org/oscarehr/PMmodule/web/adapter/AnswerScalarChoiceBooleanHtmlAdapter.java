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
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;

public class AnswerScalarChoiceBooleanHtmlAdapter extends
AnswerScalarChoiceHtmlAdapter {
	
	public AnswerScalarChoiceBooleanHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}
	
	public StringBuilder getPreBuilder() {			
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());
		
		indent(preBuilder).append(startLabel(false)).append(createCheckBox(IntakeAnswerElement.TRUE)).append(endLabel(true)).append("<br/>").append(EOL);
		String mquest = "mquests";
		if (getNoOfSibling()>1) mquest = "mquestm";
		String pId = "_" + getParent().getId();
		if (getParent().getMandatory()) {
		    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
		}
	
		
		return endAnswer(preBuilder);
	}
	
	private StringBuilder createCheckBox(String on) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("<input type=\"checkbox\" name=\"intake.answerMapped(%s).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\"", new Object[] { getId(), on }));
		
		if (getAnswerValue().equalsIgnoreCase(on)) {
			builder.append(" checked=\"checked\"");
		}
			
		builder.append(" "+this.getValidationsHtml()+"/>");
		
		return builder;
	}

}
