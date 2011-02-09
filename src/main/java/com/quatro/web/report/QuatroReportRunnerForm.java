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

import com.quatro.model.ReportFilterValue;
import com.quatro.model.ReportTempCriValue;
import com.quatro.model.ReportValue;
import com.quatro.util.HTMLPropertyBean;
import com.quatro.util.KeyValueBean;

public class QuatroReportRunnerForm extends ActionForm {

    private ArrayList<KeyValueBean> exportFormatList= new ArrayList();
    private ArrayList<KeyValueBean> reportOptionList=new ArrayList();
    private ArrayList<KeyValueBean> orgSelectionList=new ArrayList();
    private ArrayList<ReportTempCriValue> templateCriteriaList=new ArrayList();
	private String reportNo;
	private String startField;
	private String endField;
	private HTMLPropertyBean startDateProperty;
	private HTMLPropertyBean endDateProperty;
	private HTMLPropertyBean startTxtProperty;
	private HTMLPropertyBean endTxtProperty;
	private HTMLPropertyBean orgSelectionProperty;
	private String exportFormat;
	private String lstOrg;
	private String txtOrgKey;
	private String txtOrgValue;
	private String reportOption;
	private String strClientJavascript;
	private String [] relations = new String [] {"", "AND", "OR", "(", ")" ,")AND", ") OR",")AND(", ")OR(","AND(", "OR("};
	private ArrayList<ReportFilterValue> filterFields= new ArrayList();
	private ArrayList<KeyValueBean> operatorList= new ArrayList();

	private String reportTitle;
	private ReportValue rptVal;
	private String lblError;
	private String lblDateRange;
	private String lblStartDate;
	private String lblEndDate;

	public ReportValue getRptVal() {
		return rptVal;
	}

	public void setRptVal(ReportValue rptVal) {
		this.rptVal = rptVal;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
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

	public String getStrClientJavascript() {
		return strClientJavascript;
	}

	public void setStrClientJavascript(String strClientJavascript) {
		this.strClientJavascript = strClientJavascript;
	}

	public ArrayList<ReportTempCriValue> getTemplateCriteriaList() {
		if(templateCriteriaList==null) templateCriteriaList= new ArrayList<ReportTempCriValue>(); 
		return templateCriteriaList;
	}

	public void setTemplateCriteriaList(ArrayList<ReportTempCriValue> templateCriteriaList) {
		this.templateCriteriaList = templateCriteriaList;
	}

	public String[] getRelations() {
		return relations;
	}

	public ArrayList<ReportFilterValue> getFilterFields() {
		return filterFields;
	}

	public void setFilterFields(ArrayList<ReportFilterValue> filterFields) {
		this.filterFields = filterFields;
	}

	public ArrayList<KeyValueBean> getOperatorList() {
		if(operatorList==null) operatorList = new ArrayList<KeyValueBean>(); 
		return operatorList;
	}

	public void setOperatorList(ArrayList<KeyValueBean> operatorList) {
		this.operatorList = operatorList;
	}

	public String getLblError() {
		return lblError;
	}

	public void setLblError(String lblError) {
		this.lblError = lblError;
	}

	public String getLblDateRange() {
		return lblDateRange;
	}

	public void setLblDateRange(String lblDateRange) {
		this.lblDateRange = lblDateRange;
	}

	public String getLblEndDate() {
		return lblEndDate;
	}

	public void setLblEndDate(String lblEndDate) {
		this.lblEndDate = lblEndDate;
	}

	public String getLblStartDate() {
		return lblStartDate;
	}

	public void setLblStartDate(String lblStartDate) {
		this.lblStartDate = lblStartDate;
	}

	public HTMLPropertyBean getEndDateProperty() {
		return endDateProperty;
	}

	public void setEndDateProperty(HTMLPropertyBean endDateProperty) {
		this.endDateProperty = endDateProperty;
	}

	public HTMLPropertyBean getEndTxtProperty() {
		return endTxtProperty;
	}

	public void setEndTxtProperty(HTMLPropertyBean endTxtProperty) {
		this.endTxtProperty = endTxtProperty;
	}

	public HTMLPropertyBean getStartDateProperty() {
		return startDateProperty;
	}

	public void setStartDateProperty(HTMLPropertyBean startDateProperty) {
		this.startDateProperty = startDateProperty;
	}

	public HTMLPropertyBean getStartTxtProperty() {
		return startTxtProperty;
	}

	public void setStartTxtProperty(HTMLPropertyBean startTxtProperty) {
		this.startTxtProperty = startTxtProperty;
	}

	public HTMLPropertyBean getOrgSelectionProperty() {
		return orgSelectionProperty;
	}

	public void setOrgSelectionProperty(HTMLPropertyBean orgSelectionProperty) {
		this.orgSelectionProperty = orgSelectionProperty;
	}

	public ArrayList<KeyValueBean> getOrgSelectionList() {
		if(orgSelectionList==null) orgSelectionList= new ArrayList<KeyValueBean>();
		return orgSelectionList;
	}

	public void setOrgSelectionList(ArrayList<KeyValueBean> orgSelectionList) {
		this.orgSelectionList = orgSelectionList;
	}

	public String getTxtOrgKey() {
		return txtOrgKey;
	}

	public void setTxtOrgKey(String txtOrgKey) {
		this.txtOrgKey = txtOrgKey;
	}

	public String getTxtOrgValue() {
		return txtOrgValue;
	}

	public void setTxtOrgValue(String txtOrgValue) {
		this.txtOrgValue = txtOrgValue;
	}

	public String getEndField() {
		if(endField==null)
		   return "";
		else
  		   return endField;
	}

	public void setEndField(String endField) {
		this.endField = endField;
	}

	public String getStartField() {
		if(startField==null)
			return "";
		else
		  return startField;
	}

	public void setStartField(String startField) {
		this.startField = startField;
	}

}
