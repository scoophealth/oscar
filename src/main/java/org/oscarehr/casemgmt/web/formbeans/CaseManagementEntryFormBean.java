/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.web.formbeans;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.web.CheckBoxBean;
import org.oscarehr.casemgmt.web.CheckIssueBoxBean;

public class CaseManagementEntryFormBean extends ActionForm  implements java.io.Serializable {
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
	private String chain;
        private String appointmentNo;
        private String appointmentDate;
        private String startTime;
        private String billRegion;
        private String apptProvider;
        private String providerview;
	
        private String observation_date;
	
        private boolean groupNote;
        private String[] groupNoteClientIds;
        private int groupNoteTotalAnonymous;
   
        private Integer hourOfEncounterTime;
        private Integer minuteOfEncounterTime;
        private Integer hourOfEncTransportationTime;
        private Integer minuteOfEncTransportationTime;
        private Integer OscarMsgType;   
        private Integer OscarMsgTypeLink;
        
        public CaseManagementEntryFormBean() {
            super();           
            this.caseNote = new CaseManagementNote();
        }               
        
        public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
            String strDemo;
            if( (strDemo = request.getParameter("demographicNo")) != null) {                
                String sessionName = "caseManagementEntryForm" + strDemo;
                CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean)request.getSession().getAttribute(sessionName);
                if( sessionFrm != null ) {
                    this.issueCheckList = sessionFrm.getIssueCheckList();
                    this.newIssueCheckList = sessionFrm.newIssueCheckList;
                }
            }
        }
        
        public String getObservation_date() {
            return this.observation_date;
        }
        
        public void setObservation_date(String date) {
            this.observation_date = date;
        }
        
	public String getCaseNote_history() {
		return caseNote_history;
	}

	public void setCaseNote_history(String caseNote_history) {
		this.caseNote_history = caseNote_history;
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

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}
	
        public String getAppointmentNo() {
            return appointmentNo;
        }
        
        public void setAppointmentNo(String appointmentNo) {
            this.appointmentNo = appointmentNo;
        }
        
        public String getAppointmentDate() {
            return this.appointmentDate;
        }
        
        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }
        
        public String getStart_time() {
            return this.startTime;
        }
        
        public void setStart_time(String startTime) {
            this.startTime = startTime;
        }
        
        public String getBillRegion() {
            return this.billRegion;
        }
        
        public void setBillRegion(String billRegion) {
            this.billRegion = billRegion;
        }
        
        public String getApptProvider() {
            return this.apptProvider;
        }
        
        public void setApptProvider(String apptProvider) {
            this.apptProvider = apptProvider;
        }
        
        public String getProviderview() {
            return this.providerview;
        }
        
        public void setProviderview(String providerview) {
            this.providerview = providerview;
        }

		public boolean isGroupNote() {
			return groupNote;
		}

		public void setGroupNote(boolean groupNote) {
			this.groupNote = groupNote;
		}

		public String[] getGroupNoteClientIds() {
			return groupNoteClientIds;
		}

		public void setGroupNoteClientIds(String[] groupNoteClientIds) {
			this.groupNoteClientIds = groupNoteClientIds;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public int getGroupNoteTotalAnonymous() {
			return groupNoteTotalAnonymous;
		}

		public void setGroupNoteTotalAnonymous(int groupNoteTotalAnonymous) {
			this.groupNoteTotalAnonymous = groupNoteTotalAnonymous;
		}
		
		public String getTrimmedNoteText() {
			return org.apache.commons.lang.StringUtils.trimToNull(this.getCaseNote_note());	
		}

		public Integer getHourOfEncounterTime() {
        	return hourOfEncounterTime;
        }

		public void setHourOfEncounterTime(Integer hourOfEncounterTime) {
        	this.hourOfEncounterTime = hourOfEncounterTime;
        }

		public Integer getMinuteOfEncounterTime() {
        	return minuteOfEncounterTime;
        }

		public void setMinuteOfEncounterTime(Integer minuteOfEncounterTime) {
        	this.minuteOfEncounterTime = minuteOfEncounterTime;
        }

		public Integer getHourOfEncTransportationTime() {
        	return hourOfEncTransportationTime;
        }

		public void setHourOfEncTransportationTime(Integer hourOfEncTransportationTime) {
        	this.hourOfEncTransportationTime = hourOfEncTransportationTime;
        }

		public Integer getMinuteOfEncTransportationTime() {
        	return minuteOfEncTransportationTime;
        }

		public void setMinuteOfEncTransportationTime(Integer minuteOfEncTransportationTime) {
        	this.minuteOfEncTransportationTime = minuteOfEncTransportationTime;
        }

    /**
     * @return the OscarMsgType
     */
    public Integer getOscarMsgType() {
        return OscarMsgType;
    }

    /**
     * @param OscarMsgType the OscarMsgType to set
     */
    public void setOscarMsgType(Integer OscarMsgType) {
        this.OscarMsgType = OscarMsgType;
    }

    
    /**
     * @return the OscarMsgTypeLink
     */
    public Integer getOscarMsgTypeLink() {
        return OscarMsgTypeLink;
    }

    /**
     * @param OscarMsgTypeLink the OscarMsgTypeLink to set
     */
    public void setOscarMsgTypeLink(Integer OscarMsgTypeLink) {
        this.OscarMsgTypeLink = OscarMsgTypeLink;
    }
		
		
}
