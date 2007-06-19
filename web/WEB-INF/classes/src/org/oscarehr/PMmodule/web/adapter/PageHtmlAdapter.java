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

public class PageHtmlAdapter extends AbstractHtmlAdapter {

	public PageHtmlAdapter(int indent, IntakeNode node) {
		super(indent, node);
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = super.getPreBuilder();

		indent(preBuilder).append("<div dojoType=\"ContentPane\" label=\"").append(getLabel()).append("\" class=\"intakePage\" >").append(EOL);
		beginTag();
		
		if (!hasSections()) {
			indent(preBuilder).append("<table class=\"intakeTable\">").append(EOL);
			beginTag();
		}

		return preBuilder;
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		StringBuilder postBuilder = super.getPostBuilder();

		if (!hasSections()) {
			endTag();
			indent(postBuilder).append("</table> <!-- End Question Table -->").append(EOL);
		}

		endTag();
		indent(postBuilder).append("</div> <!-- End Page -->").append(EOL);

		return postBuilder;
	}

}