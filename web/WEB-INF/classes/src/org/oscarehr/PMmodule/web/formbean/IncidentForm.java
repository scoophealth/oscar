package org.oscarehr.PMmodule.web.formbean;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import oscar.MyDateFormat;

import com.quatro.model.IncidentValue;
import com.quatro.util.KeyValueBean;

public class IncidentForm extends ValidatorForm{
	
	private IncidentValue incident;
	private String providerName;
	
    private List othersLst;
	private List natureLst;
	private List clientIssuesLst;
	private List dispositionLst;
	
	private String createdDateStr;
	private String incidentDateStr;
	private String investigationDateStr;
	private String followupDateStr;
	
	private String[] othersArr = {};
	private String[] naturesArr = {};
	private String[] issuesArr = {};
	private String[] dispositionArr = {};
	private String hour;
	private String minute;
	private String ampm;
	
	// for client/staff list
	private String txtClientKeys;
	private String txtClientValues;
	private String lstClient;
	private ArrayList clientSelectionList;
	
	private String txtStaffKeys;
	private String txtStaffValues;
	private String lstStaff;
	private ArrayList staffSelectionList;
	
	// for searching
	private String clientId;
	private String clientName;
	private String startDateStr;
	private String endDateStr;
	private String incDateStr;
	private String programId;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public ArrayList getStaffSelectionList() {
		if(staffSelectionList == null)
			staffSelectionList = new ArrayList();
		return staffSelectionList;
	}
	public void setStaffSelectionList(ArrayList staffSelectionList) {
		this.staffSelectionList = staffSelectionList;
	}
	public List getClientIssuesLst() {
		return clientIssuesLst;
	}
	public void setClientIssuesLst(List clientIssuesLst) {
		this.clientIssuesLst = clientIssuesLst;
	}
	public List getDispositionLst() {
		return dispositionLst;
	}
	public void setDispositionLst(List dispositionLst) {
		this.dispositionLst = dispositionLst;
	}
	public IncidentValue getIncident() {
		if(incident == null)
			incident = new IncidentValue();
		return incident;
	}
	public void setIncident(IncidentValue incident) {
		this.incident = incident;
	}
	public List getNatureLst() {
		return natureLst;
	}
	public void setNatureLst(List natureLst) {
		this.natureLst = natureLst;
	}
	public List getOthersLst() {
		return othersLst;
	}
	public void setOthersLst(List othersLst) {
		this.othersLst = othersLst;
	}
	public String getCreatedDateStr() {
		if(incident.getCreatedDate() != null)
			createdDateStr = MyDateFormat.getStandardDate(incident.getCreatedDate());
		return createdDateStr;
	}
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}
	public String getFollowupDateStr() {
		if(incident.getFollowupDate() != null)
			followupDateStr = MyDateFormat.getStandardDate(incident.getFollowupDate());
		return followupDateStr;
	}
	public void setFollowupDateStr(String followupDateStr) {
		this.followupDateStr = followupDateStr;
	}
	public String getIncidentDateStr() {
		if(incident.getIncidentDate() != null)
			incidentDateStr = MyDateFormat.getStandardDate(incident.getIncidentDate());
		return incidentDateStr;
	}
	public void setIncidentDateStr(String incidentDateStr) {
		this.incidentDateStr = incidentDateStr;
	}
	public String getInvestigationDateStr() {
		if(incident.getInvestigationDate() != null)
			investigationDateStr = MyDateFormat.getStandardDate(incident.getInvestigationDate());
		return investigationDateStr;
	}
	public void setInvestigationDateStr(String investigationDateStr) {
		this.investigationDateStr = investigationDateStr;
	}
	public String[] getDispositionArr() {
		return dispositionArr;
	}
	public void setDispositionArr(String[] dispositionArr) {
		this.dispositionArr = dispositionArr;
	}
	public String[] getIssuesArr() {
		return issuesArr;
	}
	public void setIssuesArr(String[] issuesArr) {
		this.issuesArr = issuesArr;
	}
	public String[] getNaturesArr() {
		return naturesArr;
	}
	public void setNaturesArr(String[] naturesArr) {
		this.naturesArr = naturesArr;
	}
	public String[] getOthersArr() {
		return othersArr;
	}
	public void setOthersArr(String[] othersArr) {
		this.othersArr = othersArr;
	}
	public String getAmpm() {
		
		return ampm;
	}
	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}
	public String getHour() {
		
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getLstStaff() {
		return lstStaff;
	}
	public void setLstStaff(String lstStaff) {
		this.lstStaff = lstStaff;
	}
	public String getTxtStaffKeys() {
		return txtStaffKeys;
	}
	public void setTxtStaffKeys(String txtStaffKeys) {
		this.txtStaffKeys = txtStaffKeys;
	}
	public String getTxtStaffValues() {
		return txtStaffValues;
	}
	public void setTxtStaffValues(String txtStaffValues) {
		this.txtStaffValues = txtStaffValues;
	}
	public ArrayList getClientSelectionList() {
		if(clientSelectionList == null)
			clientSelectionList = new ArrayList();
		return clientSelectionList;
	}
	public void setClientSelectionList(ArrayList clientSelectionList) {
		this.clientSelectionList = clientSelectionList;
	}
	public String getLstClient() {
		return lstClient;
	}
	public void setLstClient(String lstClient) {
		this.lstClient = lstClient;
	}
	public String getTxtClientKeys() {
		return txtClientKeys;
	}
	public void setTxtClientKeys(String txtClientKeys) {
		this.txtClientKeys = txtClientKeys;
	}
	public String getTxtClientValues() {
		return txtClientValues;
	}
	public void setTxtClientValues(String txtClientValues) {
		this.txtClientValues = txtClientValues;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getIncDateStr() {
		return incDateStr;
	}
	public void setIncDateStr(String incDateStr) {
		this.incDateStr = incDateStr;
	}

	
}
