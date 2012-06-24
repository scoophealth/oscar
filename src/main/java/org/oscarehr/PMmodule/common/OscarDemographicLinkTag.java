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

package org.oscarehr.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class OscarDemographicLinkTag implements Tag {

	private PageContext pc = null;
	private Tag parent = null;
	private String name = null;
	private String demographicNo;

	public void setPageContext(PageContext p) {
		pc = p;
	}

	public void setParent(Tag t) {
		parent = t;
	}

	public Tag getParent() {
		return parent;
	}

	public void setName(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

	public int doStartTag() throws JspException {
		try {
			HttpSession se = ((HttpServletRequest) pc.getRequest()).getSession();
			String p = (String) se.getAttribute("OscarPageURL");

			if (p == null || p.equals("")) {
				pc.getOut().print("");
			} else {
				p = p.substring(0, p.indexOf("/provider"));
				p += "/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=" + demographicNo;
				String temps = "<a href=\"javascript.void(0);\" onclick=\"window.open('" + p + "','demographic');return false;\">OSCAR Master File</a>";
				pc.getOut().print(temps);
			}

		} catch (Exception e) {
			throw new JspTagException("An IOException occurred.");
		}
		
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {
		pc = null;
		parent = null;
		name = null;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

}
