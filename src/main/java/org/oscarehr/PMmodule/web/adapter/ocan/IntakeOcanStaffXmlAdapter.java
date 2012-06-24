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

import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.AbstractHtmlAdapter;

public class IntakeOcanStaffXmlAdapter extends AbstractHtmlAdapter {

	public IntakeOcanStaffXmlAdapter(int indent, IntakeNode node) {
		super(indent, node);
	}
	
	@Override
	public StringBuilder getPreBuilder() {
		StringBuilder preBuilder = super.getPreBuilder();

		String labelOcanXML = getLabelOcanXML();
		preBuilder.
		append("<").
		append((labelOcanXML.indexOf("OCAN_Staff_Assessment") > -1)?"OCAN_Staff_Assessment":labelOcanXML).
		append(">").
		append(EOL);

		/*		
		if (hasPages()) {
			preBuilder.append("<div dojoType=\"TabContainer\" class=\"intakePageContainer\" >").append(EOL);
			beginTag();
		}
*/		
		return preBuilder;
	}

	public StringBuilder getPostBuilder() {
		StringBuilder postBuilder = super.getPostBuilder();

		String labelOcanXML = getLabelOcanXML();
		postBuilder.
		append("</").
		append((labelOcanXML.indexOf("OCAN_Staff_Assessment") > -1)?"OCAN_Staff_Assessment":labelOcanXML).
		append(">").
		append(EOL);
/*		
		if (hasPages()) {
			endTag();
			indent(postBuilder).append("</div> <!-- End Page Container -->").append(EOL);
		}
*/
		return postBuilder;
	}
}
