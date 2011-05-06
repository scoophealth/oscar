package org.oscarehr.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.oscarehr.provider.web.CppPreferencesUIBean;

import oscar.OscarProperties;

public class CustomInterfaceTag extends TagSupport {

	Logger logger = MiscUtils.getLogger();
	private String name;
	private String section;
	
	@Override
	public int doStartTag() throws JspException {
		OscarProperties props = OscarProperties.getInstance();
		String customJs = props.getProperty("cme_js");
		
		if(customJs == null || customJs.length() == 0) {
			customJs="default";
		}
		
		if(customJs.equals("default") && getSection().equals("cme")) {
			//check preferences
			CppPreferencesUIBean bean = new CppPreferencesUIBean(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			bean.loadValues();
			if(bean.getEnable()!=null&&bean.getEnable().equals("on")) {
				logger.info("Use preference based echart");
				try {
					JspWriter out = super.pageContext.getOut();
					out.println(this.getPreferenceBasedEChart(bean));
				}catch(IOException e) {
					logger.error("Error:",e);
				}
				return SKIP_BODY;
			}
		}
		
		if(customJs != null && customJs.length()>0) {
			JspWriter out = super.pageContext.getOut();
			String contextPath = this.pageContext.getServletContext().getContextPath();
			try {
				out.println("<link rel=\"stylesheet\" href=\""+contextPath+"js/custom/global.css\" type=\"text/css\">");
				out.println("<script src=\""+contextPath+"/js/custom/"+customJs+"/global.js\"></script>");
				if(getSection()!=null && getSection().length()>0) {
					out.println("<script src=\""+contextPath+"/js/custom/"+customJs+"/"+getSection()+".js\"></script>");
				}
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
	
	
	public String getPreferenceBasedEChart(CppPreferencesUIBean bean) {		
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("jQuery(document).ready(function(){");
		sb.append("issueNoteUrls = {");
		boolean flag=false, row1=false, row2=false;
		if(!bean.getSocialHxPosition().equals("")) {
			sb.append("div"+bean.getSocialHxPosition()+":    ctx + \"/CaseManagementView.do?hc=996633&method=listNotes&providerNo=\" + providerNo + \"&demographicNo=\" + demographicNo + \"&issue_code=SocHistory&title=\" + socHistoryLabel + \"&cmd=div"+bean.getSocialHxPosition()+"\"");
			flag=true;
			if(bean.getSocialHxPosition().startsWith("R1")) {row1=true;}
			if(bean.getSocialHxPosition().startsWith("R2")) {row2=true;}
		}
		if(!bean.getMedicalHxPosition().equals("")) {
			if(flag) {sb.append(",");}
			sb.append("div"+bean.getMedicalHxPosition()+":    ctx + \"/CaseManagementView.do?hc=996633&method=listNotes&providerNo=\" + providerNo + \"&demographicNo=\" + demographicNo + \"&issue_code=MedHistory&title=\" + medHistoryLabel + \"&cmd=div"+bean.getMedicalHxPosition()+"\"");
			flag=true;
			if(bean.getMedicalHxPosition().startsWith("R1")) {row1=true;}
			if(bean.getMedicalHxPosition().startsWith("R2")) {row2=true;}
		}
		if(!bean.getOngoingConcernsPosition().equals("")) {
			if(flag) {sb.append(",");}
			sb.append("div"+bean.getOngoingConcernsPosition()+":    ctx + \"/CaseManagementView.do?hc=996633&method=listNotes&providerNo=\" + providerNo + \"&demographicNo=\" + demographicNo + \"&issue_code=Concerns&title=\" + onGoingLabel + \"&cmd=div"+bean.getOngoingConcernsPosition()+"\"");
			flag=true;
			if(bean.getOngoingConcernsPosition().startsWith("R1")) {row1=true;}
			if(bean.getOngoingConcernsPosition().startsWith("R2")) {row2=true;}
		}
		if(!bean.getRemindersPosition().equals("")) {
			if(flag) {sb.append(",");}
			sb.append("div"+bean.getRemindersPosition()+":    ctx + \"/CaseManagementView.do?hc=996633&method=listNotes&providerNo=\" + providerNo + \"&demographicNo=\" + demographicNo + \"&issue_code=Reminders&title=\" + remindersLabel + \"&cmd=div"+bean.getRemindersPosition()+"\"");
			flag=true;
			if(bean.getRemindersPosition().startsWith("R1")) {row1=true;}
			if(bean.getRemindersPosition().startsWith("R2")) {row2=true;}
		}
		sb.append("};");
		
		//can we delete a row?
		if(!row1) {	sb.append("removeCppRow(1);");}
		if(!row2) {	sb.append("removeCppRow(2);");}
		
		sb.append("init();");
		sb.append("});");
		sb.append("function notifyIssueUpdate() {}");
		sb.append("function notifyDivLoaded(divId) {}");
		sb.append("</script>");
		return sb.toString();
	}
}
