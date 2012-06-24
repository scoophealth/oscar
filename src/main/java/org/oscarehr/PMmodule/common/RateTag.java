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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class RateTag implements Tag {

	private PageContext pc = null;
	private Tag parent = null;
	private String name = null;

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
			String pageName = ((HttpServletRequest) pc.getRequest()).getServletPath();
			pageName = pageName.substring(pageName.lastIndexOf('/') + 1);
			pc.getOut().print(
		        "<script>" +
		        	"function popup(form) {" +
		        		"var w = open('','_RatePage','toolbar=no,resizable=yes,scrollbars=yes,width=600,height=400');" +
		        		"w.document.write('Please wait...');form.target='_RatePage'" +
		        	"}" +
		        "</script>" +
		        "<div id='projecttools' class='toolgroup'>" +
		        	"<div class='label'>" +
		        		"<Strong>Rate this page</Strong>" +
		        	"</div>" +
		        	"<div class='body' align='center'>" +
		        		"<form target='_blank' action='" + ((HttpServletRequest) pc.getRequest()).getContextPath() + "/ratePage.do' method='post' onsubmit='popup(this)'>" +
		        			"<select name='rate' style='width:80px; margin:4px;'>" +
						        "<option value='10' selected>10</option>" +
						        "<option vaue='9'>9</option>" +
						        "<option vaue='8'>8</option>" +
						        "<option vaue='7'>7</option>" +
						        "<option vaue='6'>6</option>" +
						        "<option vaue='5'>5</option>" +
						        "<option vaue='4'>4</option>" +
						        "<option vaue='3'>3</option>" +
						        "<option vaue='2'>2</option>" +
						        "<option vaue='1'>1</option>" +
		        			"</select>" +
		        			"<input type='hidden' name='rateURL' value='" + pageName + "'>" +
		        			"<input type='submit' value='go' style='margin:4px;'>" +
		        		"</form>" +
		        	"</div>" +
		        "</div>");
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
	
}
