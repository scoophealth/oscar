package com.quatro.model;

import java.util.Calendar;

import com.quatro.common.KeyConstants;

import oscar.MyDateFormat;

/**
 * Incident entity.
 * 
 * @author Jzhang
 */

public class IncidentValue implements java.io.Serializable {

	// Fields

	private Integer id;
	private Calendar createdDate;
	private String providerNo;
	private Calendar incidentDate;
	private String incidentTime;// hh:mmA
	private String clients;
	private String staff;
	private String witnesses;
	private String otherInvolved;
	private String nature;
	private String location;
	private String clientIssues;
	private String description;
	private String disposition;
	private String restriction;
	private String chargesLaid;
	private String policeReportNo;
	private String badgeNo;
	private String investigationRcmd;
	private String investigationConductedby;
	private Calendar investigationDate;
	private String followupInfo;
	private String followupCompletedby;
	private Calendar followupDate;
	private String reportCompleted;
	private Integer programId;
	
	private String clientsNames;

	// Constructors

	/** default constructor */
	public IncidentValue() {
	}

	public String getBadgeNo() {
		return badgeNo;
	}

	public void setBadgeNo(String badgeNo) {
		this.badgeNo = badgeNo;
	}

	public String getChargesLaid() {
		return chargesLaid;
	}

	public void setChargesLaid(String chargesLaid) {
		this.chargesLaid = chargesLaid;
	}

	public String getClientIssues() {
		return clientIssues;
	}

	public void setClientIssues(String clientIssues) {
		this.clientIssues = clientIssues;
	}

	public String getClients() {
		return clients;
	}

	public void setClients(String clients) {
		this.clients = clients;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getFollowupCompletedby() {
		return followupCompletedby;
	}

	public void setFollowupCompletedby(String followupCompletedby) {
		this.followupCompletedby = followupCompletedby;
	}

	public Calendar getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Calendar followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupInfo() {
		return followupInfo;
	}

	public void setFollowupInfo(String followupInfo) {
		this.followupInfo = followupInfo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Calendar incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getInvestigationConductedby() {
		return investigationConductedby;
	}

	public void setInvestigationConductedby(String investigationConductedby) {
		this.investigationConductedby = investigationConductedby;
	}

	public Calendar getInvestigationDate() {
		return investigationDate;
	}

	public void setInvestigationDate(Calendar investigationDate) {
		this.investigationDate = investigationDate;
	}

	public String getInvestigationRcmd() {
		return investigationRcmd;
	}

	public void setInvestigationRcmd(String investigationRcmd) {
		this.investigationRcmd = investigationRcmd;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getOtherInvolved() {
		return otherInvolved;
	}

	public void setOtherInvolved(String otherInvolved) {
		this.otherInvolved = otherInvolved;
	}

	public String getPoliceReportNo() {
		return policeReportNo;
	}

	public void setPoliceReportNo(String policeReportNo) {
		this.policeReportNo = policeReportNo;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getReportCompleted() {
		return reportCompleted;
	}

	public void setReportCompleted(String reportCompleted) {
		this.reportCompleted = reportCompleted;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getWitnesses() {
		return witnesses;
	}

	public void setWitnesses(String witnesses) {
		this.witnesses = witnesses;
	}

	public String getIncidentDatex() {
		String str = "";
    	if(incidentDate != null)
    		str = MyDateFormat.getStandardDate(incidentDate);
    		
		return str;
		
	}
	 public String getStatus(){
	    	String status =KeyConstants.STATUS_ACTIVE;
	    	if("1".equals(getReportCompleted())){
	    		status=KeyConstants.STATUS_READONLY;
	    	}
	    	return status;
	    }

	public String getClientsNames() {
		int i = clients.indexOf("/");
		if(i > 0){
			String tmp = clients.substring(i+1);
			//clientsNames = tmp.replace(":", "<br />");// since java5
			clientsNames = com.quatro.util.Utility.replace(tmp,":", "<br />");
		}
		return clientsNames;
	}

	public void setClientsNames(String clientsNames) {
		this.clientsNames = clientsNames;
	}
}