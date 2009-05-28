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
package org.oscarehr.PMmodule.web.adapter.ocan;

import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.AbstractHtmlAdapter;

public class QuestionOcanClientXmlAdapter extends AbstractHtmlAdapter {
	
	@Override
	public StringBuilder getPostBuilder() {
		StringBuilder postBuilder = super.getPostBuilder();
		return postBuilder.append("</").append(getLabelOcanClientXML()).append(">").append(EOL);
	}

	public QuestionOcanClientXmlAdapter(int indent, IntakeNode node) {
	    super(indent, node);
    }

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = super.getPreBuilder();
		return preBuilder.append("<").append(getLabelOcanClientXML()).append(">").append(EOL);
    }
	
}