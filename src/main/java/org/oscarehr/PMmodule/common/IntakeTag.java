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
package org.oscarehr.PMmodule.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.HtmlAdapterFactory;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;

public class IntakeTag extends SimpleTagSupport {

	private int base;
	private Intake intake;
	private HtmlAdapterFactory adapterFactory;
	
	public IntakeTag() {
		adapterFactory = new HtmlAdapterFactory();
	}

	public void setBase(int base) {
		this.base = base;
	}
		
	public void setIntake(Intake intake) {
		this.intake = intake;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(toHtml());
	}
	
	String toHtml() {
		StringBuilder html = new StringBuilder();
		toHtml(html, base, intake.getNode());
		
		return html.toString();
	}

	void toHtml(StringBuilder builder, int indent, IntakeNode node) {
		if(node == null)
			return;
		IntakeNodeHtmlAdapter htmlAdapter = adapterFactory.getHtmlAdapter(indent, node, intake);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toHtml(builder, htmlAdapter.getIndent(), child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

}
