package org.oscarehr.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class CaseManagementLinkTag extends TagSupport {

	private Tag parent = null;
	private String name = null;
	
	private String demographicNo;
	private String providerNo;
	private String providerName;
	
	public CaseManagementLinkTag() {
		providerName="";
	}

	public void setParent(Tag t) {
		parent=t;
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
	
	public int doStartTag() throws JspException 
	{
		 HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		 HttpSession session = req.getSession();
		try 
		{
			if(true) {
               String contextPath = req.getContextPath();
               if(contextPath.indexOf("/mod/") != -1) {
            	   contextPath = contextPath.substring(0,contextPath.indexOf("/mod/"));
               }
                StringBuffer buf = new StringBuffer().append(req.getScheme())
                .append("://").append(req.getServerName()).append(":").append(
                        req.getServerPort()).append(contextPath).append(
                        "/mod/").append("caisi").append("/").append("CaseManagementView.do?").append(
                        		"demographicNo=").append(demographicNo).append("&providerNo=").append(
                        				providerNo).append("&providerName=").append(providerName);
                pageContext.getOut().write(buf.toString());
			} else if(session.getAttribute("OscarPageURL") != null) {
				String p=(String) session.getAttribute("OscarPageURL");
				p = p.substring(0,p.indexOf("/provider"));
				p += "/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=" + demographicNo;
				pageContext.getOut().print(p);
			} else {
				pageContext.getOut().write("javascript:alert('No module found');");
			}
		} 
		catch (Exception e) 
		{
			throw new JspTagException("An IOException occurred.");
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException 
	{
		return EVAL_PAGE;
	}

	public void release() 
	{
		super.release();
		parent = null;
		name = null;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

}
