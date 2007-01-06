/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class CaseManagementLinkTag extends TagSupport {

	private String name;

	private String demographicNo;

	private String providerNo;

	private String providerName;
	
	private Tag parent;

	public CaseManagementLinkTag() {
		providerName = "";
	}

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(String demographicNo) {
    	this.demographicNo = demographicNo;
    }
	
	public String getProviderNo() {
		return providerNo;
	}
	
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getProviderName() {
    	return providerName;
    }

	public void setProviderName(String providerName) {
    	this.providerName = providerName;
    }

	public Tag getParent() {
    	return parent;
    }

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	public int doStartTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		
		try {
			StringBuilder builder = new StringBuilder();
			
			builder.append(req.getScheme()).append("://");
			builder.append(req.getServerName()).append(":");
			builder.append(req.getServerPort()).append("/");
			builder.append("oscar").append("/");
			
			builder.append("CaseManagementView.do").append("?");
			builder.append("demographicNo=").append(demographicNo).append("&");
			builder.append("providerNo=").append(providerNo).append("&");
			builder.append("providerName=").append(providerName);
			
			pageContext.getOut().write(builder.toString());
		} catch (IOException e) {
			throw new JspTagException("An IOException occurred.");
		}
		
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		
		parent = null;
		name = null;
	}

}