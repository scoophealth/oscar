package com.quatro.model;

import java.util.ArrayList;
import java.sql.Date;

public class ReportTempValue {
    private int templateNo = 0;
    private int reportNo;
    private int reportOptionID;
    private String desc;
    private Date startDate;
    private Date endDate;
    private String startPayPeriod;
    private String endPayPeriod;
    private Date updateDate;
    private String loginId;
    private boolean privateTemplate;
    private ArrayList templateCriteria;
    private String criteriaDis;
    private ArrayList orgCodes;
    private int index;
    private String userName;
    public String ErrMsg = "";
    
	public String getCriteriaDis() {
		return criteriaDis;
	}
	public void setCriteriaDis(String criteriaDis) {
		this.criteriaDis = criteriaDis;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getEndPayPeriod() {
		return endPayPeriod;
	}
	public void setEndPayPeriod(String endPayPeriod) {
		this.endPayPeriod = endPayPeriod;
	}
	public String getErrMsg() {
		return ErrMsg;
	}
	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isPrivateTemplate() {
		return privateTemplate;
	}
	public void setPrivateTemplate(boolean privateTemplate) {
		this.privateTemplate = privateTemplate;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public ArrayList getOrgCodes() {
		return orgCodes;
	}
	public void setOrgCodes(ArrayList orgCodes) {
		this.orgCodes = orgCodes;
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	public int getReportOptionID() {
		return reportOptionID;
	}
	public void setReportOptionID(int reportOptionID) {
		this.reportOptionID = reportOptionID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getStartPayPeriod() {
		return startPayPeriod;
	}
	public void setStartPayPeriod(String startPayPeriod) {
		this.startPayPeriod = startPayPeriod;
	}
	public ArrayList getTemplateCriteria() {
		return templateCriteria;
	}
	public void setTemplateCriteria(ArrayList templateCriteria) {
		this.templateCriteria = templateCriteria;
	}
	public int getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(int templateNo) {
		this.templateNo = templateNo;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
