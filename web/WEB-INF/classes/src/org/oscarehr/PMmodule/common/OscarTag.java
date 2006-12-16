package org.oscarehr.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class OscarTag implements Tag {

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
			String q=(String) se.getAttribute("OscarPageQuery");
			
			if(p==null)
			{
				pc.getOut().print("");
			}
			else if (p.equals(""))
			{
				pc.getOut().print("");
			}
			else
			{
				/*
				p='"'+p+'"';
				String temps="<a href='javascript:returnToOscarPage("+p+");'>Oscar Medical</a>";
				*/
				p=p.substring(0,p.lastIndexOf("/")+1)+"providercontrol.jsp?"+q;
				String temps="<a href='"+p+"'>Oscar Medical</a>";
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
