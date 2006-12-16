package org.oscarehr.casemgmt.web.formbeans;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.web.CheckBoxBean;
import org.oscarehr.casemgmt.web.CheckIssueBoxBean;

public class CaseManagementEntryFormBean extends ActionForm {
	private CaseManagementNote caseNote;
	private CaseManagementCPP cpp;
	private String demoNo;
	private String noteId;
	private CheckBoxBean[] issueCheckList;
	private CheckIssueBoxBean[] newIssueCheckList;
	private List newIssueList;
	private String sign; 
	private String includeIssue;
	private String method;
	private String showList;
	private String searString;
	private String deleteId;
	private String lineId;
	private String demographicNo;
	private String providerNo;
	private String programNo;
	private String demoName;
	private String caseNote_note;
	private String caseNote_history;
	
	

	public String getCaseNote_history() {
		return caseNote_history;
	}

	public void setCaseNote_history(String caseNote_history) {
		this.caseNote_history = caseNote_history;
	}

	public CaseManagementEntryFormBean() {
		caseNote = new CaseManagementNote();
	}
	
	public String getDeleteId()
	{
		return deleteId;
	}
	public void setDeleteId(String deleteId)
	{
		this.deleteId = deleteId;
	}
	
	public String getIncludeIssue()
	{
		return includeIssue;
	}
	public void setIncludeIssue(String includeIssue)
	{
		this.includeIssue = includeIssue;
	}
	public CheckBoxBean[] getIssueCheckList()
	{
		return issueCheckList;
	}
	public void setIssueCheckList(CheckBoxBean[] issueCheckList)
	{
		this.issueCheckList = issueCheckList;
	}
	public String getLineId()
	{
		return lineId;
	}
	public void setLineId(String lineId)
	{
		this.lineId = lineId;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public CheckIssueBoxBean[] getNewIssueCheckList()
	{
		return newIssueCheckList;
	}
	public void setNewIssueCheckList(CheckIssueBoxBean[] newIssueCheckList)
	{
		this.newIssueCheckList = newIssueCheckList;
	}
	public List getNewIssueList()
	{
		return newIssueList;
	}
	public void setNewIssueList(List newIssueList)
	{
		this.newIssueList = newIssueList;
	}
	public String getNoteId()
	{
		return noteId;
	}
	public void setNoteId(String noteId)
	{
		this.noteId = noteId;
	}
	public String getSearString()
	{
		return searString;
	}
	public void setSearString(String searString)
	{
		this.searString = searString;
	}
	public String getShowList()
	{
		return showList;
	}
	public void setShowList(String showList)
	{
		this.showList = showList;
	}
	public String getSign()
	{
		return sign;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public CaseManagementNote getCaseNote()
	{
		return caseNote;
	}

	public void setCaseNote(CaseManagementNote caseNote)
	{
		this.caseNote = caseNote;
	}

	public String getDemoNo()
	{
		return demoNo;
	}

	public void setDemoNo(String demoNo)
	{
		this.demoNo = demoNo;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getDemoName() {
		return demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getProgramNo() {
		return programNo;
	}

	public void setProgramNo(String programNo) {
		this.programNo = programNo;
	}
	
	
	
	public CaseManagementCPP getCpp() {
		return cpp;
	}

	public void setCpp(CaseManagementCPP cpp) {
		this.cpp = cpp;
	}

	public String getCaseNote_note() {
		this.caseNote_note=getCaseNote().getNote();
		return caseNote_note;
	}

	public void setCaseNote_note(String caseNote_note) {
		
		this.caseNote.setNote(caseNote_note);
		this.caseNote_note = caseNote_note;
	}
	
}
