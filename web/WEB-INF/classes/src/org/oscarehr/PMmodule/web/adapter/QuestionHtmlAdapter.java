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

import org.oscarehr.PMmodule.model.IntakeNode;

public class QuestionHtmlAdapter extends AbstractHtmlAdapter {
	
	public QuestionHtmlAdapter(int indent, IntakeNode node) {
	    super(indent, node);
    }

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = super.getPreBuilder();
				
		indent(preBuilder).append("<tr>").append(EOL);
		beginTag();
		
		for (int i = 0; i < getDistanceToQuestionLevel(); i++) {
			indent(preBuilder).append("<td class=\"intakeEmptyCell\"></td>").append(EOL);
        }
		
		indent(preBuilder).append("<td class=\"intakeQuestionCell\" colspan=\"").append(getDistanceToMaxLevel()).append("\">").append(EOL);
		beginTag();
		
		indent(preBuilder).append(getLabel()).append(getMandatory()?"<font color=red>*</font>":"").append(EOL);
		
		endTag();
		indent(preBuilder).append("</td>").append(EOL);
		
		endTag();
		indent(preBuilder).append("</tr>").append(EOL);

	    return preBuilder;
    }
	
}