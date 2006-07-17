package org.caisi.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class CaisiRoleLinkTag implements Tag{

	private PageContext pc = null;
	private Tag parent = null;
	private String name = null;
	
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
				p += "/CaisiRole.do";
				String temps="<a href=\"javascript.void(0);\" onclick=\"window.open('"+p+"','caisi_role','width=500,height=500');return false;\">Caisi Roles</a>";
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

}
