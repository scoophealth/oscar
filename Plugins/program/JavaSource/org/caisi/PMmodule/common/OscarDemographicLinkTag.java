package org.caisi.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class OscarDemographicLinkTag implements Tag{

	private PageContext pc = null;
	private Tag parent = null;
	private String name = null;
	
	private String demographicNo;
	
	
	public void setPageContext(PageContext p) {
		pc=p;
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
		try 
		{
			HttpSession se = ((HttpServletRequest) pc.getRequest())
					.getSession();
			String p=(String) se.getAttribute("OscarPageURL");
			
			if(p==null || p.equals("")) {
				pc.getOut().print("");
			} else {
				p = p.substring(0,p.indexOf("/provider"));
				p += "/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=" + demographicNo;
				String temps="<a href=\"javascript.void(0);\" onclick=\"window.open('"+p+"','demographic');return false;\">OSCAR Master File</a>";
				pc.getOut().print(temps);
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
