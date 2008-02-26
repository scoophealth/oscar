package com.quatro.model;

import java.util.List;

//this class is used ONLY for Query purpose, don't do add/update/delete through ReportEXValue.hbm.xml  
public class ReportEXValue {
    private int reportNo;
    private String title;
    private String description;
    private String templateNo;
    private List childList;
    
    //for query from Report_Template table
    private String loginId;    
    private boolean privateTemplate;

    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	public String getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List getChildList() {
		return childList;
	}
	public void setChildList(List childList) {
		this.childList = childList;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public boolean isPrivateTemplate() {
		return privateTemplate;
	}
	public void setPrivateTemplate(boolean privateTemplate) {
		this.privateTemplate = privateTemplate;
	}
    
}
