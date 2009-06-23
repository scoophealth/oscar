/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportValue{

	private ReportTempValue reportTemp;
    private List filters;
    private Set options;
    private Date runTime;

    private Boolean print2Pdf;
    private int exportFormatType;

    //Hibernate values
    private int reportNo;
    private String title;
    private String description;
    private String accessType;
    private Boolean orgApplicable;
    private String reportType;
    private int reportGroupId;
    private String reportGroupDesc;
    private String dateOption;
    private String datePart;
    private String notes;
    private String tableName;
    private String author;   
    private Date updatedDate;
    private String providerNo;
    
    private String sptorun;
    
	public ReportTempValue getReportTemp() {
		return reportTemp;
	}

	public void setReportTemp(ReportTempValue reportTemp) {
		this.reportTemp = reportTemp;
	}

	public Set getOptions() {
		return options;
	}

	public void setOptions(Set options) {
		this.options = options;
	}

	public int getExportFormatType() {
		return exportFormatType;
	}

	public void setExportFormatType(int exportFormatType) {
		this.exportFormatType = exportFormatType;
	}

	public Boolean getPrint2Pdf() {
		return print2Pdf;
	}

	public void setPrint2Pdf(Boolean print2Pdf) {
		this.print2Pdf = print2Pdf;
	}

	public Date getRunTime() {
		return runTime;
	}

	public void setRunTime(Date runTime) {
		this.runTime = runTime;
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

	public int getReportGroupId() {
		return reportGroupId;
	}

	public void setReportGroupId(int reportGroup) {
		this.reportGroupId = reportGroup;
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

	public List getFilters() {
		return filters;
	}

	public void setFilters(List filters) {
		this.filters = filters;
	}

	public String getSptorun() {
		return sptorun;
	}

	public void setSptorun(String sptorun) {
		this.sptorun = sptorun;
	}
	
}
