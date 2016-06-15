/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest.conversion.summary;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.managers.PreferenceManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class IssueNoteSummary implements Summary {
	
    
	@Autowired
	protected CaseManagementManager caseManagementMgr;
	
	@Autowired
	protected PreferenceManager preferenceManager;
    
	//protected static final String ELLIPSES = "...";
	//protected static final int MAX_LEN_TITLE = 48;
	//protected static final int CROP_LEN_TITLE = 45;
	//protected static final int MAX_LEN_KEY = 12;
	//protected static final int CROP_LEN_KEY = 9;
	
	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		SummaryTo1 summary = null;
		String[] issueIds = null;
		if("ongoingconcerns".equals(summaryCode)){
			summary = new SummaryTo1("Ongoing Concerns",0,SummaryTo1.ONGOINGCONCERNS_CODE);
			issueIds = new String[] {"Concerns"};
		}else if("medhx".equals(summaryCode)){
			summary = new SummaryTo1("Medical History",1,SummaryTo1.MEDICALHISTORY_CODE);
			issueIds = new String[] {"MedHistory"};
		}else if("socfamhx".equals(summaryCode)){
			summary = new SummaryTo1("Social/Family History",2,SummaryTo1.SOCIALFAMILYHISTORY_CODE);
			issueIds = new String[] {"SocHistory","FamHistory"};
		}else if("reminders".equals(summaryCode)){
			summary = new SummaryTo1("Reminders",4,SummaryTo1.REMINDERS_CODE);
			issueIds = new String[] {"Reminders"}; 
		}else if("othermeds".equals(summaryCode)){
			summary = new SummaryTo1("Other Meds",6,SummaryTo1.OTHERMEDS_CODE);
			issueIds = new String[] {"OMeds"};
		}else if("sochx".equals(summaryCode)){
			summary = new SummaryTo1("Social History",2,SummaryTo1.SOCIALHISTORY_CODE);
			issueIds = new String[] {"SocHistory"};
		}else if("famhx".equals(summaryCode)){
			summary = new SummaryTo1("Family History",3,SummaryTo1.FAMILYHISTORY_CODE);
			issueIds = new String[] {"FamHistory"};
		} else if("riskfactors".equals(summaryCode)){
			summary = new SummaryTo1("Risk Factors",7,SummaryTo1.RISK_FACTORS);
			issueIds = new String[] {"RiskFactors"};
		}
	
		
		List<SummaryItemTo1> list = summary.getSummaryItem();

	    getSummaryListForIssuedNotes( loggedInInfo,demographicNo, list,issueIds );

		return summary;
	}
	
	
	protected void getSummaryListForIssuedNotes(LoggedInInfo loggedInInfo,Integer demographicNo, List<SummaryItemTo1> list, String[] issueCodes){
		//String[] issueCodes = { "OMeds", "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory", "RiskFactors" };
		
		List<Issue> issueList = new ArrayList<Issue>();
		for (int j = 0; j < issueCodes.length; ++j) {
			//List<Issue> issues = caseManagementMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), issueCodes[j]);
			issueList.addAll(caseManagementMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), issueCodes[j]));

		}
		String[] issueIds = getIssueIds(issueList);
		
		Collection<CaseManagementNote> notes = caseManagementMgr.getActiveNotes(""+demographicNo, issueIds);
		
		String cppExts = "";
		int count = 0;
		for(CaseManagementNote note:notes){
			
			Set<CaseManagementIssue> issueSet = note.getIssues();
			StringBuilder issueBuilder = new StringBuilder();
			for (CaseManagementIssue s : issueSet) {
			    issueBuilder.append(s.getIssue().getCode());
			}
			String issueString = issueBuilder.toString();

			if( preferenceManager.isCppItem(issueString) && preferenceManager.isCustomSummaryEnabled(loggedInInfo) ){
				cppExts = preferenceManager.getCppExtsItem(loggedInInfo, caseManagementMgr.getExtByNote(note.getId()), issueString);
			}
			
			SummaryItemTo1 summaryItem = new SummaryItemTo1(count, note.getNote() + cppExts,"action","notes_"+issueString);
			summaryItem.setDate(note.getObservation_date());
			summaryItem.setEditor(note.getProviderName());
			summaryItem.setNoteId(note.getId());
			
			list.add(summaryItem);
			count++;
		}
	}
	
	public static String[] getIssueIds(List<Issue> issues) {
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue i : issues) {
			issueIds[idx] = String.valueOf(i.getId());
			++idx;
		}
		return issueIds;
	}
	
}
