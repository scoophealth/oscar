package com.quatro.model;

import org.caisi.model.BaseObject;
import java.util.ArrayList;
import java.util.Date;

import org.caisi.model.BaseObject;

public class ReportTValue extends BaseObject{
    private int reportNo;
    private String title;
    private String description;
    private String accessType;
    private Boolean orgApplicable;
    private String reportType;
    private String reportGroup;
    private String reportGroupDesc;
    private String dateOption;
    private String datePart;
    private String notes;

    private String tableName;
    private String author;   
    private Date updatedDate;
    private String providerNo;

	public ReportTValue() {
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDateOption() {
		return dateOption;
	}

	public void setDateOption(String dateOption) {
		this.dateOption = dateOption;
	}

	public String getDatePart() {
		return datePart;
	}

	public void setDatePart(String datePart) {
		this.datePart = datePart;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean isOrgApplicable() {
		return orgApplicable;
	}

	public void setOrgApplicable(Boolean orgApplicable) {
		this.orgApplicable = orgApplicable;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getReportGroup() {
		return reportGroup;
	}

	public void setReportGroup(String reportGroup) {
		this.reportGroup = reportGroup;
	}

	public String getReportGroupDesc() {
		return reportGroupDesc;
	}

	public void setReportGroupDesc(String reportGroupDesc) {
		this.reportGroupDesc = reportGroupDesc;
	}

	public int getReportNo() {
		return reportNo;
	}

	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}
