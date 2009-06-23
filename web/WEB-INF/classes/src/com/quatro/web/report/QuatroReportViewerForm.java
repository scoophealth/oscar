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
package com.quatro.web.report;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import com.quatro.util.KeyValueBean;

public class QuatroReportViewerForm extends ActionForm {

    private ArrayList<KeyValueBean> exportFormatList;
    private ArrayList<KeyValueBean> reportOptionList;
	private String reportNo;
	private String startDate;
	private String endDate;
	private String exportFormat;
	private String lstOrg;
	private String reportOption;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getReportOption() {
		return reportOption;
	}

	public void setReportOption(String reportOption) {
		this.reportOption = reportOption;
	}

	public String getLstOrg() {
		return lstOrg;
	}

	public void setLstOrg(String lstOrg) {
		this.lstOrg = lstOrg;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public ArrayList<KeyValueBean> getExportFormatList() {
		if(exportFormatList==null) exportFormatList= new ArrayList<KeyValueBean>();
		return exportFormatList;
	}

	public void setExportFormatList(ArrayList<KeyValueBean> exportFormatList) {
		this.exportFormatList = exportFormatList;
	}

	public ArrayList<KeyValueBean> getReportOptionList() {
		if(reportOptionList==null) reportOptionList= new ArrayList<KeyValueBean>();
		return reportOptionList;
	}

	public void setReportOptionList(ArrayList<KeyValueBean> reportOptionList) {
		this.reportOptionList = reportOptionList;
	}


}
