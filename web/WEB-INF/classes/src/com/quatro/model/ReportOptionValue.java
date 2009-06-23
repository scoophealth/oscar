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

import org.caisi.model.BaseObject;

public class ReportOptionValue extends BaseObject{
    private int reportNo;
    private int optionNo;
    private String optionTitle;
    private String optionDescription;
    private Boolean active;
    private Boolean bdefault;

    private String dateFieldName;
    private String dateFieldDesc;

    private String rptFileName;
    private int rptFileNo;
    private Date rptVersion;
    private String dateFieldType;

    private String dbsqlWhere;
    private String dbsqlOrderBy;
    
	public ReportOptionValue() {
	}

    public String getDbsqlOrderBy() {
		return dbsqlOrderBy;
	}
	public void setDbsqlOrderBy(String dbsqlOrderBy) {
		this.dbsqlOrderBy = dbsqlOrderBy;
	}
	public String getDbsqlWhere() {
		return dbsqlWhere;
	}
	public void setDbsqlWhere(String dbsqlWhere) {
		this.dbsqlWhere = dbsqlWhere;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean isDefault() {
		return bdefault;
	}
	public void setDefault(Boolean bdefault) {
		this.bdefault = bdefault;
	}
	public String getDateFieldDesc() {
		return dateFieldDesc;
	}
	public void setDateFieldDesc(String dateFieldDesc) {
		this.dateFieldDesc = dateFieldDesc;
	}
	public String getDateFieldName() {
		return dateFieldName;
	}
	public void setDateFieldName(String dateFieldName) {
		this.dateFieldName = dateFieldName;
	}
	public String getDateFieldType() {
		return dateFieldType;
	}
	public void setDateFieldType(String dateFieldType) {
		this.dateFieldType = dateFieldType;
	}
	public String getOptionDescription() {
		return optionDescription;
	}
	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}
	public int getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	public String getRptFileName() {
		return rptFileName;
	}
	public void setRptFileName(String rptFileName) {
		this.rptFileName = rptFileName;
	}
	public int getRptFileNo() {
		return rptFileNo;
	}
	public void setRptFileNo(int rptFileNo) {
		this.rptFileNo = rptFileNo;
	}
	public Date getRptVersion() {
		return rptVersion;
	}
	public void setRptVersion(Date rptVersion) {
		this.rptVersion = rptVersion;
	}

}
