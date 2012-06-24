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

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;

abstract public class AbstractAnswerScalarHtmlAdapter extends AbstractAnswerHtmlAdapter {

	protected AbstractAnswerScalarHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		super(indent, node, intake);
	}

	protected StringBuilder startAnswer(StringBuilder builder) {
		return isParentQuestion() ? startCell(startRow(builder)) : builder;
	}

	protected StringBuilder endAnswer(StringBuilder builder) {
		return isParentQuestion() ? endRow(endCell(builder)) : builder;
	}

	protected StringBuilder startLabel(boolean printLabelBefore) {
		StringBuilder labelBuilder = new StringBuilder();
		
		//labelBuilder.append("<td>");
		labelBuilder.append("<label>");
		
		if (printLabelBefore) {
			printLabel(labelBuilder);
			labelBuilder.append(SPACE);
		}
		
		return labelBuilder;
	}
	
	protected StringBuilder endLabel(boolean printLabelAfter) {
		StringBuilder labelBuilder = new StringBuilder();
		
		if (printLabelAfter) {
			labelBuilder.append(SPACE);
			printLabel(labelBuilder);
		}
		
		labelBuilder.append("</label>");
		//labelBuilder.append("</td>");
		return labelBuilder;
	}
	
	private StringBuilder printLabel(StringBuilder builder) {
		// builder.append(" [").append(getId()).append("] ");
		
		String label = getLabel(); 
		
		if (StringUtils.isNotBlank(label)) {
			builder.append(label);
		}
		
		return builder;
	}
	
	protected String getValidationsHtml() {
		String validations = this.getValidations();
		if(validations==null) { return "";}
		return "class=\"{validate: {" + validations + "}}\"";	
	}
}
