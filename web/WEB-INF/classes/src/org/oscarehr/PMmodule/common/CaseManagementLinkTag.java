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