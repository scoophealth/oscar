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
package org.oscarehr.ws.rest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
//import org.oscarehr.ws.rest.to.model.FormTo1;
//import org.oscarehr.ws.rest.to.model.MenuItem;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/recordUX/")
@Component("recordUxService")
public class RecordUxService extends AbstractServiceImpl {
	private static final Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private CaseManagementManager caseManagementMgr;
	
	@Autowired
	private FormsManager formsManager;

	/**
	$scope.recordtabs2 = [ 
	 {id : 0,name : 'Master',url : 'partials/master.html'},
	 {id : 1,name : 'Summary',url : 'partials/summary.html'},
	 {id : 2,name : 'Rx',url : 'partials/rx.jsp'},
	 {id : 3,name : 'Msg',url : 'partials/summary.html'},
	 {id : 4,name : 'Trackers',url : 'partials/tracker.jsp'},
	 {id : 5,name : 'Consults',url : 'partials/summary.html'},
	 {id : 6,name : 'Forms',url : 'partials/formview.html'},
	 {id : 7,name : 'Prevs/Measurements',url : 'partials/summary.html'},
	 {id : 8,name : 'Ticklers',url : 'partials/summary.html'},
	 {id : 9,name : 'MyOscar',url : 'partials/blank.jsp'},
	 {id : 10,name : 'Allergies',url : 'partials/summary.html'},
	 {id : 11,name : 'CPP',url : 'partials/cpp.html'},
	 {id : 12,name : 'Labs/Docs',url : 'partials/labview.html'},
	 {id : 13,name : 'Billing',url : 'partials/billing.jsp'}
	 ];
	...
	
	@GET
	@Path("/recordMenu")
	@Produces("application/json")
	public MenuItem[] getRecordMenu(@QueryParam(value="demo") String demographic){
		logger.error("getRecordMenu getting called for demo "+demographic);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		MenuItem[] menulist = new MenuItem[5];
		menulist[0] = new MenuItem(0, "Master", "partials/master.html");
		menulist[1] = new MenuItem(1, "Summary", "partials/summary.html");
		menulist[2] = new MenuItem(2, "Rx", "partials/rx.jsp");
		menulist[3] = new MenuItem(3, "Forms", "partials/formview.html");
		menulist[4] = new MenuItem(4, "Labs/Docs", "partials/labview.html");
		return menulist;
	}
	**/  
	
	@GET
	@Path("/{demographicNo}/summary/{summaryName}") //@Path("/leftsideSummary")
	@Produces("application/json")
	public SummaryTo1[] getSummary(@PathParam("demographicNo") Integer demographicNo,@PathParam("summaryName") String summaryName){
		LoggedInInfo loggedInInfo = getLoggedInInfo();// LoggedInInfo.loggedInInfo.get();
		logger.debug("getting summary:"+summaryName+" for demo "+demographicNo+"  loggedInInfo "+loggedInInfo);
		SummaryTo1[] summaryList = new SummaryTo1[8];
		summaryList[0] = new SummaryTo1("Ongoing Concerns",0,"ongoingconcerns");
		summaryList[1] = new SummaryTo1("Medical History",1,"medhx"); 
		//summaryList[2] = new SummaryTo1("Social/Family History",2,"socfamhx");
		summaryList[2] = new SummaryTo1("Social History",2,"sochx");
		summaryList[3] = new SummaryTo1("Family History",3,"famhx");

		summaryList[4] = new SummaryTo1("Reminders",4,"reminders");
		summaryList[5] = new SummaryTo1("Medications",5,"meds");
		summaryList[6] = new SummaryTo1("Other Meds",6,"othermeds");
		summaryList[7] = new SummaryTo1("Assessments",7,"assessments");
		//summaryList[9] = new SummaryTo1("Outgoing",7,"outgoing");
		return summaryList;
	}
	

	@GET
	@Path("/{demographicNo}/fullSummary/{summaryCode}")
	@Produces("application/json")
	public SummaryTo1 getFullSummmary(@PathParam("demographicNo") Integer demographicNo,@PathParam(value="summaryCode") String summaryCode){
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		SummaryTo1 summary = null;
		
		
		if("ongoingconcerns".equals(summaryCode)){
			summary = new SummaryTo1("Ongoing Concerns",0,"ongoingconcerns");
			//String[] issueIds = new String[] {"62"};
			String[] issueIds = new String[] {"Concerns"};
			getSummaryListForIssuedNotes(demographicNo, summary.getSummaryItem(),  issueIds);
		}else if("medhx".equals(summaryCode)){
			summary = new SummaryTo1("Medical History",1,"medhx");
			//String[] issueIds = new String[] {"61"};
			String[] issueIds = new String[] {"MedHistory"};
			getSummaryListForIssuedNotes(demographicNo, summary.getSummaryItem(),  issueIds);
		}else if("socfamhx".equals(summaryCode)){
			summary = new SummaryTo1("Social/Family History",2,"socfamhx");
			//String[] codes = new String[] {"60","64"};
			String[] codes = new String[] {"SocHistory","FamHistory"};
			
			getSummaryListForIssuedNotes(demographicNo,summary.getSummaryItem(),codes);
		}else if("reminders".equals(summaryCode)){
			summary = new SummaryTo1("Reminders",4,"reminders");
			//String[] issueIds = new String[] {"63"}; //|       63 | Reminders  
			String[] issueIds = new String[] {"Reminders"}; //|       63 | Reminders  
			getSummaryListForIssuedNotes(demographicNo, summary.getSummaryItem(),  issueIds);
		}else if("meds".equals(summaryCode)){
			summary = new SummaryTo1("Medications",5,"meds");
			//STILL NEED TO DO THIS
		}else if("othermeds".equals(summaryCode)){
			summary = new SummaryTo1("Other Meds",6,"othermeds");
			//String[] issueIds = new String[] {"59"}; //|      
			String[] issueIds = new String[] {"OMeds"}; //|       
			getSummaryListForIssuedNotes(demographicNo, summary.getSummaryItem(),  issueIds);
		}else if("assessments".equals(summaryCode)){
			summary = new SummaryTo1("Assessments",7,"assessments");
			
			List<EFormData> completedEforms = formsManager.findByDemographicId(loggedInInfo,demographicNo);
			Collections.sort(completedEforms, Collections.reverseOrder(EFormData.FORM_DATE_COMPARATOR));
			List<SummaryItemTo1> list =   summary.getSummaryItem();
			int count = 0;
			for(EFormData eformData: completedEforms){	
				int id = eformData.getId();
				
				//list.add(new SummaryItemTo1(id, eformData.getFormName(),"#/record/"+demographicNo+"/forms/eform/id/"+id));
				list.add(new SummaryItemTo1(id, eformData.getFormName(),"record.forms.existing","eform"));
				count++;
				/*int formId = eformData.getFormId();
				String name = eformData.getFormName();
				String subject = eformData.getSubject();
				String status = eformData.getSubject();
				Date date = eformData.getFormDate();
				Boolean showLatestFormOnly = eformData.isShowLatestFormOnly();
				formListTo1.add(FormTo1.create(id, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
				*/
			}
			
			
			//STILL NEED TO DO THIS
		}else if("sochx".equals(summaryCode)){
			summary = new SummaryTo1("Social History",2,"sochx");
			//String[] codes = new String[] {"60","64"};
			String[] codes = new String[] {"SocHistory"};
			
			getSummaryListForIssuedNotes(demographicNo,summary.getSummaryItem(),codes);
		}else if("famhx".equals(summaryCode)){
			summary = new SummaryTo1("Family History",3,"famhx");
			//String[] codes = new String[] {"60","64"};
			String[] codes = new String[] {"FamHistory"};
			
			getSummaryListForIssuedNotes(demographicNo,summary.getSummaryItem(),codes);
		}
		
		
		
		
		/* Removing outgoing for the demo
		else if("outgoing".equals(summaryCode)){
			summary = new SummaryTo1("Outgoing",7,"outgoing");
			
			//STILL NEED TO DO THIS
		}
		*/		
	
		logger.debug("outgoing summary object:"+summary);
		return summary;
	}
	
	public String[] getIssueIds(List<Issue> issues) {
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue i : issues) {
			issueIds[idx] = String.valueOf(i.getId());
			++idx;
		}
		return issueIds;
	}
	
	private void getSummaryListForIssuedNotes(Integer demographicNo, List<SummaryItemTo1> list, String[] issueCodes){
		//String[] issueCodes = { "OMeds", "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory", "RiskFactors" };
		LoggedInInfo loggedInInfo = getLoggedInInfo(); // LoggedInInfo.loggedInInfo.get();
		List<Issue> issueList = new ArrayList<Issue>();
		for (int j = 0; j < issueCodes.length; ++j) {
			List<Issue> issues = caseManagementMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), issueCodes[j]);
			issueList.addAll(caseManagementMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), issueCodes[j]));

		}
		String[] issueIds = getIssueIds(issueList);
	
		Collection<CaseManagementNote> notes = caseManagementMgr.getActiveNotes(""+demographicNo, issueIds);
		int count = 0;
		for(CaseManagementNote note:notes){
			logger.debug("adding "+note.getNote()+" for issues "+issueIds);
			list.add(new SummaryItemTo1(count, note.getNote(),"action","notes"));
			count++;
		}
	}
	
	/*
|       *59 | OMeds       | Other Meds as part of cpp                                      | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       *60 | SocHistory  | Social History as part of cpp                                  | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       *61 | MedHistory  | Medical History as part of cpp                                 | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       *62 | Concerns    | Ongoing Concerns as part of cpp                                | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       *63 | Reminders   | Reminders as part of cpp                                       | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       *64 | FamHistory  | Family History as part of cpp                                  | nurse  | 2011-06-23 12:49:15 | NULL     | system      |        NULL |
|       65 | RiskFactors | Risk Factors as part of cpp           
	
	public List<SummaryItemTo1> getSummaryListForIssuedNotes(Integer demographicNo,<List>SummaryItemTo1 list,String[] issueIds){
		Collection<CaseManagementNote> notes = caseManagementMgr.getActiveNotes(demographicNo, issueIds);
		Integer count = 0;
		for(CaseManagementNote note:notes){
			list.add(new SummaryItemTo1(count, note.getNote(),"action"));
			count++;
		}
		return list;
	}
	 */
	
	
	
	
	
}
