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

public class AnswerScalarChoiceDropDownHtmlAdapter extends
	AnswerScalarChoiceHtmlAdapter {
	
	public AnswerScalarChoiceDropDownHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}
	
	public StringBuilder getPreBuilder() {			
		StringBuilder preBuilder = startAnswer(super.getPreBuilder());
	
		StringBuilder sb = new StringBuilder();
		indent(sb).
		append("<tr><td>").
		append("&nbsp;<a href=\"#\" class=\"repeat_remove\">-</a>&nbsp;").
		append(startLabel(true)).
		append(endLabel(false)).
		append("</td>").
		append(this.createSelect()).
		append("</tr>");
		
		
		indent(preBuilder)
			.append("<tr><td id=\"td_"+getId()+"\">");
		if(getRepeating()) {
			indent(preBuilder).append("<script type=\"text/javascript\">$('document').ready(function(){$('#repeat_add_"+getId()+"').click(function(e){e.preventDefault();$('#td_"+getId()+"').parent().parent().append('"+sb.toString()+"');});});</script>");
			indent(preBuilder).append("<a href=\"#\" class=\"repeat_add\" id=\"repeat_add_"+getId()+"\">+</a>&nbsp;");
		}
		
		//label
		preBuilder.append(startLabel(true)).append(endLabel(false)).append("</td>");
		
		preBuilder.append(createSelect());
		
		//close row
		preBuilder.append("</tr>").append(EOL);
		
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
				sb2.append("<tr><td>").
				append("&nbsp;<a href=\"#\" class=\"repeat_remove\">-</a>&nbsp;").
				append(startLabel(true)).
				append(endLabel(false)).
				append("</td>").
				append(this.createSelect(getId(), value)).
				append("</tr>");											
				preBuilder.append(sb2.toString());				
			}
		}
		
		return endAnswer(preBuilder);
	}
	
	private StringBuilder createSelect(String id, String answerValue) {
		StringBuilder preBuilder = new StringBuilder();
		
		preBuilder.append("<td>")
			.append(String.format("<select name=\"intake.answerMapped(%s-0).value\" question_id=\"" +  getQuestionId()  + "\" nodeId=\""+getId()+"\" repeat=\"true\" "+this.getValidationsHtml()+">", new Object[] { getId() }));
				
		//options
		preBuilder.append(createOption("", "Declined"));

		for (IntakeAnswerElement answerElement : getAnswerElements()) {
			String label = answerElement.getElement();
			if(answerElement.getLabel()!=null) {
				label=answerElement.getLabel();
			}
			String value = answerElement.getElement();
	
			preBuilder.append(createOptionR(label, value,answerValue));
		}

		endTag();
		indent(preBuilder).append("</select>").append("</td>" );
		
		return preBuilder;
	}

	private StringBuilder createSelect() {
		StringBuilder preBuilder = new StringBuilder();
		
		preBuilder.append("<td>")
			.append(String.format("<select name=\"intake.answerMapped(%s-0).value\" question_id=\"" +  getQuestionId()  + "\" nodeId=\""+getId()+"\" repeat=\"true\" "+this.getValidationsHtml()+">", new Object[] { getId() }));
				
		//options
		preBuilder.append(createOption("", "Declined"));

		for (IntakeAnswerElement answerElement : getAnswerElements()) {
			String label = answerElement.getElement();
			if(answerElement.getLabel()!=null) {
				label=answerElement.getLabel();
			}
			String value = answerElement.getElement();
	
			preBuilder.append(createOption(label, value));
		}

		endTag();
		indent(preBuilder).append("</select>").append("</td>" );
		
		return preBuilder;
	}
	
	private StringBuilder createOptionR(String label, String value, String answerValue) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("<option value=\"%s\"", new Object[] { value }));

		String av = answerValue;
		if(answerValue==null) {
			av=getAnswerValue();
		}
		if (av.equalsIgnoreCase(value)) {
			builder.append(" selected=\"selected\"");
		}

		builder.append(" >").append(String.format("%s</option>", new Object[] { label }));

		return builder;
	}
	
	private StringBuilder createOption(String label, String value) {
		return createOptionR(label,value,null);
	}
		

}
