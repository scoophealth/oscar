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

public class AnswerScalarTextHtmlAdapter extends AbstractAnswerScalarHtmlAdapter {
	
	private static final String CLASS_INTAKE_INPUT = "intakeInput";
	
	public AnswerScalarTextHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());

		//for use with repeating - this will be what's in the append() js call
		StringBuilder sb = new StringBuilder();
		indent(sb).
		append("<tr><td>").
		append("&nbsp;<a href=\"#\" class=\"repeat_remove\">-</a>&nbsp;").
		append(startLabel(true)).
		append(endLabel(false)).
		append("</td>").
		append(getRepeatTextInput(getId(), isParentQuestion())).
		append("</tr>");
		
		
		//the first row
		indent(preBuilder).
			append("<tr><td id=\"td_"+getId()+"\">");
		if(getRepeating()) {
			indent(preBuilder).append("<script type=\"text/javascript\">$('document').ready(function(){$('#repeat_add_"+getId()+"').click(function(e){e.preventDefault();$('#td_"+getId()+"').parent().parent().append('"+sb.toString()+"');});});</script>");
			indent(preBuilder).append("<a href=\"#\" class=\"repeat_add\" id=\"repeat_add_"+getId()+"\">+</a>&nbsp;");
		}
		indent(preBuilder).
			append(startLabel(true)).
			append(endLabel(false)).
			append("</td>");
			if(getRepeating()) {
				indent(preBuilder).append(getRepeatTextInput(getId(), getRepeatingAnswerValue(0), isParentQuestion()));
			} else {
				indent(preBuilder).append(getTextInput(getId(), getAnswerValue(), isParentQuestion()));
			}
			indent(preBuilder).append("</tr>").
			append(EOL);
		
		String mquest = "mquests";
		if (getNoOfSibling()>1) mquest = "mquestm";
		String pId = "_" + getParent().getId();
		if (getParent().getMandatory()) {
		    indent(preBuilder).append("<input type=\"hidden\" name=\""+mquest+getPos()+pId+"\" value=\"intake.answerMapped("+getId()+").value\">").append(EOL);
		}

		/* end of the first element, if repeating, we need to add the others here.
		 */
		if(getRepeating()) {
			int index=1;
			String value = null;
			while((value=getRepeatingAnswerValue(index++))!=null) {
				StringBuilder sb2 = new StringBuilder();
				indent(sb2).
				append("<tr><td>").
				append("&nbsp;<a href=\"#\" class=\"repeat_remove\">-</a>&nbsp;").
				append(startLabel(true)).
				append(endLabel(false)).
				append("</td>").
				append(getRepeatTextInput(getId(), value, isParentQuestion())).
				append("</tr>");
				
				preBuilder.append(sb2.toString());				
			}
		}
		
		return endAnswer(preBuilder);
	}

	private String getTextInput(String id, String value, boolean grabHorizontal) {
		if (grabHorizontal) {
			return String.format("<td><input type=\"text\" class=\"%s\" name=\"intake.answerMapped(%s).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\" "+this.getValidationsHtml()+"></input></td>", new Object[] { CLASS_INTAKE_INPUT, id, value });
		} else {
			return String.format("<td><input type=\"text\" name=\"intake.answerMapped(%s).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\" "+this.getValidationsHtml()+"></input></td>", new Object[] { id, value });
		}
		
	}
	
	private String getRepeatTextInput(String id, boolean grabHorizontal) {
		if (grabHorizontal) {
			return String.format("<td><input type=\"text\" class=\"%s\" name=\"intake.answerMapped(%s-0).value\" value=\"\" question_id=\"" +  getQuestionId()  + "\" repeat=\"true\" nodeId=\""+id+"\" "+this.getValidationsHtml()+"></input></td>", new Object[] { CLASS_INTAKE_INPUT, id });
		} else {
			return String.format("<td><input type=\"text\" name=\"intake.answerMapped(%s-0).value\" value=\"\" question_id=\"" +  getQuestionId()  + "\" repeat=\"true\" nodeId=\""+id+"\" "+this.getValidationsHtml()+"></input></td>", new Object[] { id });
		}
		
	}
	
	private String getRepeatTextInput(String id, String value, boolean grabHorizontal) {
		if (grabHorizontal) {
			return String.format("<td><input type=\"text\" class=\"%s\" name=\"intake.answerMapped(%s-0).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\" repeat=\"true\" nodeId=\""+id+"\" "+this.getValidationsHtml()+"></input></td>", new Object[] { CLASS_INTAKE_INPUT, id, value });
		} else {
			return String.format("<td><input type=\"text\" name=\"intake.answerMapped(%s-0).value\" value=\"%s\" question_id=\"" +  getQuestionId()  + "\" repeat=\"true\" nodeId=\""+id+"\" "+this.getValidationsHtml()+"></input></td>", new Object[] { id,value });
		}
		
	}
	
}
