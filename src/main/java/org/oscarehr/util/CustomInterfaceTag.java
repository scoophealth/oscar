package org.oscarehr.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import oscar.OscarProperties;

public class CustomInterfaceTag extends TagSupport {

	Logger logger = MiscUtils.getLogger();
	private String name;
	private String section;
	
	@Override
	public int doStartTag() throws JspException {
		OscarProperties props = OscarProperties.getInstance();
		String customJs = props.getProperty("cme_js");
		
		if(customJs != null && customJs.length()>0) {
			JspWriter out = super.pageContext.getOut();
			String contextPath = this.pageContext.getServletContext().getContextPath();
			try {
				out.println("<script src=\""+contextPath+"/js/custom/"+customJs+".js\"></script>");
				out.println("<script src=\""+contextPath+"/js/custom/"+customJs+"-"+getSection()+".js\"></script>");
			}catch(IOException e) {
				logger.error("Error",e);
			}
		}
		return SKIP_BODY;
	}
	/*
	@Override
	public int doEndTag() throws JspException {		        
		return EVAL_PAGE;	   
	}
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	
}
