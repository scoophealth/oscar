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

package org.oscarehr.PMmodule.streethealth;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.service.StreetHealthReportManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * 
 * @author Marc Dumontier (marc@mdumontier.com)
 *
 */
public class StreetHealthIntakeReportAction extends DispatchAction {
	
	private static Logger log = MiscUtils.getLogger();
	
	private static final String SDF_PATTERN = "yyyy-MM-dd";
	private static final String COHORT_CRITICAL_YM = "-03-31";
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	/*
	 * 	these values must be retrieved by node id because they are not mapped to a label
	 *	if the form were to change, these might need to be adjusted. Possibly a good idea
	 *	to move to the properties file
	*/
	private static int NODEID_BASELINE_RESIDENCE_TYPE;
	private static int NODEID_CURRENT_RESIDENCE_TYPE;
		
	private static int NODEID_HOSPITALIZATION1_DATE;
	private static int NODEID_HOSPITALIZATION1_LENGTH;
	private static int NODEID_HOSPITALIZATION1_PSYCH;
	private static int NODEID_HOSPITALIZATION1_PHYS;
	private static int NODEID_HOSPITALIZATION1_DECLINED;
	
	private static int NODEID_HOSPITALIZATION2_DATE;
	private static int NODEID_HOSPITALIZATION2_LENGTH;
	private static int NODEID_HOSPITALIZATION2_PSYCH;
	private static int NODEID_HOSPITALIZATION2_PHYS;
	private static int NODEID_HOSPITALIZATION2_DECLINED;
	
	private static int NODEID_HOSPITALIZATION3_DATE;
	private static int NODEID_HOSPITALIZATION3_LENGTH;
	private static int NODEID_HOSPITALIZATION3_PSYCH;
	private static int NODEID_HOSPITALIZATION3_PHYS;
	private static int NODEID_HOSPITALIZATION3_DECLINED;
	
	private static int NODEID_HOSPITALIZATION4_DATE;
	private static int NODEID_HOSPITALIZATION4_LENGTH;
	private static int NODEID_HOSPITALIZATION4_PSYCH;
	private static int NODEID_HOSPITALIZATION4_PHYS;
	private static int NODEID_HOSPITALIZATION4_DECLINED;
	
	private static int NODEID_HOSPITALIZATION5_DATE;
	private static int NODEID_HOSPITALIZATION5_LENGTH;
	private static int NODEID_HOSPITALIZATION5_PSYCH;
	private static int NODEID_HOSPITALIZATION5_PHYS;
	private static int NODEID_HOSPITALIZATION5_DECLINED;
	
	private static int NODEID_OTHER_ILLNESS_CONCURRENT;
	private static int NODEID_OTHER_ILLNESS_DUAL_DIAGNOSIS;
	private static int NODEID_OTHER_ILLNESS_MOHLTC_INITIATIVES;
	private static int NODEID_OTHER_ILLNESS_OTHER_CHRONIC;
	private static int NODEID_OTHER_ILLNESS_NOT_APPLICABLE;
	
	private static String LABLE_OTHER_ILLNESS_CONCURRENT;
	private static String LABLE_OTHER_ILLNESS_DUAL_DIAGNOSIS;
	private static String LABLE_OTHER_ILLNESS_MOHLTC_INITIATIVES;
	private static String LABLE_OTHER_ILLNESS_OTHER_CHRONIC;
	private static String LABLE_OTHER_ILLNESS_NOT_APPLICABLE;
	
	private static int NODEID_PRESENTING_ISSUES_1;
	private static int NODEID_PRESENTING_ISSUES_2;
	private static int NODEID_PRESENTING_ISSUES_3;
	private static int NODEID_PRESENTING_ISSUES_4;
	private static int NODEID_PRESENTING_ISSUES_5;
	private static int NODEID_PRESENTING_ISSUES_6;
	private static int NODEID_PRESENTING_ISSUES_7;
	private static int NODEID_PRESENTING_ISSUES_8;
	private static int NODEID_PRESENTING_ISSUES_9;
	private static int NODEID_PRESENTING_ISSUES_10;
	private static int NODEID_PRESENTING_ISSUES_11;
	private static int NODEID_PRESENTING_ISSUES_12;
	
	private static String LABEL_PRESENTING_ISSUES_1;
	private static String LABEL_PRESENTING_ISSUES_2;
	private static String LABEL_PRESENTING_ISSUES_3;
	private static String LABEL_PRESENTING_ISSUES_4;
	private static String LABEL_PRESENTING_ISSUES_5;
	private static String LABEL_PRESENTING_ISSUES_6;
	private static String LABEL_PRESENTING_ISSUES_7;
	private static String LABEL_PRESENTING_ISSUES_8;
	private static String LABEL_PRESENTING_ISSUES_9;
	private static String LABEL_PRESENTING_ISSUES_10;
	private static String LABEL_PRESENTING_ISSUES_11;
	private static String LABEL_PRESENTING_ISSUES_12;
	
	private static int NODEID_REFERRAL1;
	private static int NODEID_REFERRAL2;
	private static int NODEID_REFERRAL3;
	private static int NODEID_REFERRAL4;
	private static int NODEID_REFERRAL5;
	private static int NODEID_REFERRAL6;
	private static int NODEID_REFERRAL7;
	private static int NODEID_REFERRAL8;
	private static int NODEID_REFERRAL9;
	private static int NODEID_REFERRAL10;
	private static int NODEID_REFERRAL11;
	private static int NODEID_REFERRAL12;
	private static int NODEID_REFERRAL13;
	private static int NODEID_REFERRAL14;
	private static int NODEID_REFERRAL15;
	private static int NODEID_REFERRAL16;	
	
	private static String LABEL_REFERRAL1;
	private static String LABEL_REFERRAL2;
	private static String LABEL_REFERRAL3;
	private static String LABEL_REFERRAL4;
	private static String LABEL_REFERRAL5;
	private static String LABEL_REFERRAL6;
	private static String LABEL_REFERRAL7;
	private static String LABEL_REFERRAL8;
	private static String LABEL_REFERRAL9;
	private static String LABEL_REFERRAL10;
	private static String LABEL_REFERRAL11;
	private static String LABEL_REFERRAL12;
	private static String LABEL_REFERRAL13;
	private static String LABEL_REFERRAL14;
	private static String LABEL_REFERRAL15;
	private static String LABEL_REFERRAL16;
	
	private static int NODEID_EXIT_DISPOSITION1;
	private static int NODEID_EXIT_DISPOSITION2;
	private static int NODEID_EXIT_DISPOSITION3;
	private static int NODEID_EXIT_DISPOSITION4;
	private static int NODEID_EXIT_DISPOSITION5;
	private static int NODEID_EXIT_DISPOSITION6;
	private static int NODEID_EXIT_DISPOSITION7;
	
	private static String LABEL_EXIT_DISPOSITION1;
	private static String LABEL_EXIT_DISPOSITION2;
	private static String LABEL_EXIT_DISPOSITION3;
	private static String LABEL_EXIT_DISPOSITION4;
	private static String LABEL_EXIT_DISPOSITION5;
	private static String LABEL_EXIT_DISPOSITION6;
	private static String LABEL_EXIT_DISPOSITION7;
	
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT1;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT2;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT3;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT4;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT5;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT6;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT7;
	private static int NODEID_BASELINE_LIVING_ARRANGEMENT8;
	
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT1;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT2;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT3;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT4;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT5;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT6;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT7;
	private static String LABEL_BASELINE_LIVING_ARRANGEMENT8;
	
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT1;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT2;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT3;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT4;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT5;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT6;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT7;
	private static int NODEID_CURRENT_LIVING_ARRANGEMENT8;
	
	
	
	
	private StreetHealthReportManager mgr;	
	
	public void loadNodeIdsWithNoLabels() {
		Properties p = new Properties();
		try {
			p.load(getClass().getClassLoader().getResourceAsStream("streethealth_report.properties"));
		}catch(IOException e) {log.warn(e);}
		
		try {
			NODEID_BASELINE_RESIDENCE_TYPE = Integer.parseInt((String)p.get("NODEID_BASELINE_RESIDENCE_TYPE"));
			NODEID_CURRENT_RESIDENCE_TYPE = Integer.parseInt((String)p.get("NODEID_CURRENT_RESIDENCE_TYPE"));
			
			
			//NODEID_HOSPITALIZATION_START = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION_START"));
			
			NODEID_HOSPITALIZATION1_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_DATE"));
			NODEID_HOSPITALIZATION1_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_LENGTH"));
			NODEID_HOSPITALIZATION1_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_PSYCH"));
			NODEID_HOSPITALIZATION1_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_PHYS"));
			NODEID_HOSPITALIZATION1_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_DECLINED"));
			
			NODEID_HOSPITALIZATION2_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_DATE"));
			NODEID_HOSPITALIZATION2_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_LENGTH"));
			NODEID_HOSPITALIZATION2_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_PSYCH"));
			NODEID_HOSPITALIZATION2_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_PHYS"));
			NODEID_HOSPITALIZATION2_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_DECLINED"));
			
			NODEID_HOSPITALIZATION3_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_DATE"));
			NODEID_HOSPITALIZATION3_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_LENGTH"));
			NODEID_HOSPITALIZATION3_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_PSYCH"));
			NODEID_HOSPITALIZATION3_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_PHYS"));
			NODEID_HOSPITALIZATION3_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_DECLINED"));
			
			NODEID_HOSPITALIZATION4_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_DATE"));
			NODEID_HOSPITALIZATION4_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_LENGTH"));
			NODEID_HOSPITALIZATION4_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_PSYCH"));
			NODEID_HOSPITALIZATION4_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_PHYS"));
			NODEID_HOSPITALIZATION4_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_DECLINED"));
			
			NODEID_HOSPITALIZATION5_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_DATE"));
			NODEID_HOSPITALIZATION5_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_LENGTH"));
			NODEID_HOSPITALIZATION5_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_PSYCH"));
			NODEID_HOSPITALIZATION5_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_PHYS"));
			NODEID_HOSPITALIZATION5_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_DECLINED"));
			
			NODEID_OTHER_ILLNESS_CONCURRENT = Integer.parseInt((String)p.get("NODEID_OTHER_ILLNESS_CONCURRENT"));
			NODEID_OTHER_ILLNESS_DUAL_DIAGNOSIS = Integer.parseInt((String)p.get("NODEID_OTHER_ILLNESS_DUAL_DIAGNOSIS"));
			NODEID_OTHER_ILLNESS_MOHLTC_INITIATIVES = Integer.parseInt((String)p.get("NODEID_OTHER_ILLNESS_MOHLTC_INITIATIVES"));
			NODEID_OTHER_ILLNESS_OTHER_CHRONIC = Integer.parseInt((String)p.get("NODEID_OTHER_ILLNESS_OTHER_CHRONIC"));
			NODEID_OTHER_ILLNESS_NOT_APPLICABLE = Integer.parseInt((String)p.get("NODEID_OTHER_ILLNESS_NOT_APPLICABLE"));
			
			LABLE_OTHER_ILLNESS_CONCURRENT = (String)p.get("illness1");
			LABLE_OTHER_ILLNESS_DUAL_DIAGNOSIS = (String)p.get("illness2");
			LABLE_OTHER_ILLNESS_MOHLTC_INITIATIVES = (String)p.get("illness3");
			LABLE_OTHER_ILLNESS_OTHER_CHRONIC = (String)p.get("illness4");
			LABLE_OTHER_ILLNESS_NOT_APPLICABLE = (String)p.get("illness5");
			
			NODEID_PRESENTING_ISSUES_1 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_1"));
			NODEID_PRESENTING_ISSUES_2 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_2"));
			NODEID_PRESENTING_ISSUES_3 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_3"));
			NODEID_PRESENTING_ISSUES_4 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_4"));
			NODEID_PRESENTING_ISSUES_5 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_5"));
			NODEID_PRESENTING_ISSUES_6 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_6"));
			NODEID_PRESENTING_ISSUES_7 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_7"));
			NODEID_PRESENTING_ISSUES_8 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_8"));
			NODEID_PRESENTING_ISSUES_9 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_9"));
			NODEID_PRESENTING_ISSUES_10 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_10"));
			NODEID_PRESENTING_ISSUES_11 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_11"));
			NODEID_PRESENTING_ISSUES_12 = Integer.parseInt((String)p.get("NODEID_PRESENTING_ISSUES_12"));
			
			LABEL_PRESENTING_ISSUES_1 = (String)p.get("presenting_issues1");
			LABEL_PRESENTING_ISSUES_2 = (String)p.get("presenting_issues2");
			LABEL_PRESENTING_ISSUES_3 = (String)p.get("presenting_issues3");
			LABEL_PRESENTING_ISSUES_4 = (String)p.get("presenting_issues4");
			LABEL_PRESENTING_ISSUES_5 = (String)p.get("presenting_issues5");
			LABEL_PRESENTING_ISSUES_6 = (String)p.get("presenting_issues6");
			LABEL_PRESENTING_ISSUES_7 = (String)p.get("presenting_issues7");
			LABEL_PRESENTING_ISSUES_8 = (String)p.get("presenting_issues8");
			LABEL_PRESENTING_ISSUES_9 = (String)p.get("presenting_issues9");
			LABEL_PRESENTING_ISSUES_10 = (String)p.get("presenting_issues10");
			LABEL_PRESENTING_ISSUES_11 = (String)p.get("presenting_issues11");
			LABEL_PRESENTING_ISSUES_12 = (String)p.get("presenting_issues12");
			
			NODEID_REFERRAL1 = Integer.parseInt((String)p.get("NODEID_REFERRAL1"));	
			NODEID_REFERRAL2 = Integer.parseInt((String)p.get("NODEID_REFERRAL2"));	
			NODEID_REFERRAL3 = Integer.parseInt((String)p.get("NODEID_REFERRAL3"));	
			NODEID_REFERRAL4 = Integer.parseInt((String)p.get("NODEID_REFERRAL4"));	
			NODEID_REFERRAL5 = Integer.parseInt((String)p.get("NODEID_REFERRAL5"));	
			NODEID_REFERRAL6 = Integer.parseInt((String)p.get("NODEID_REFERRAL6"));	
			NODEID_REFERRAL7 = Integer.parseInt((String)p.get("NODEID_REFERRAL7"));	
			NODEID_REFERRAL8 = Integer.parseInt((String)p.get("NODEID_REFERRAL8"));	
			NODEID_REFERRAL9 = Integer.parseInt((String)p.get("NODEID_REFERRAL9"));	
			NODEID_REFERRAL10 = Integer.parseInt((String)p.get("NODEID_REFERRAL10"));	
			NODEID_REFERRAL11 = Integer.parseInt((String)p.get("NODEID_REFERRAL11"));	
			NODEID_REFERRAL12 = Integer.parseInt((String)p.get("NODEID_REFERRAL12"));	
			NODEID_REFERRAL13 = Integer.parseInt((String)p.get("NODEID_REFERRAL13"));	
			NODEID_REFERRAL14 = Integer.parseInt((String)p.get("NODEID_REFERRAL14"));	
			NODEID_REFERRAL15 = Integer.parseInt((String)p.get("NODEID_REFERRAL15"));	
			NODEID_REFERRAL16 = Integer.parseInt((String)p.get("NODEID_REFERRAL16"));				
			
			LABEL_REFERRAL1 = (String)p.get("referral1");
			LABEL_REFERRAL2 = (String)p.get("referral2");
			LABEL_REFERRAL3 = (String)p.get("referral3");
			LABEL_REFERRAL4 = (String)p.get("referral4");
			LABEL_REFERRAL5 = (String)p.get("referral5");
			LABEL_REFERRAL6 = (String)p.get("referral6");
			LABEL_REFERRAL7 = (String)p.get("referral7");
			LABEL_REFERRAL8 = (String)p.get("referral8");
			LABEL_REFERRAL9 = (String)p.get("referral9");
			LABEL_REFERRAL10 = (String)p.get("referral10");
			LABEL_REFERRAL11 = (String)p.get("referral11");
			LABEL_REFERRAL12 = (String)p.get("referral12");
			LABEL_REFERRAL13 = (String)p.get("referral13");
			LABEL_REFERRAL14 = (String)p.get("referral14");
			LABEL_REFERRAL15 = (String)p.get("referral15");
			LABEL_REFERRAL16 = (String)p.get("referral16");
			
			NODEID_EXIT_DISPOSITION1 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION1"));
			NODEID_EXIT_DISPOSITION2 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION2"));
			NODEID_EXIT_DISPOSITION3 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION3"));
			NODEID_EXIT_DISPOSITION4 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION4"));
			NODEID_EXIT_DISPOSITION5 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION5"));
			NODEID_EXIT_DISPOSITION6 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION6"));
			NODEID_EXIT_DISPOSITION7 = Integer.parseInt((String)p.get("NODEID_EXIT_DISPOSITION7"));
			
			LABEL_EXIT_DISPOSITION1 = (String)p.get("exit1");
			LABEL_EXIT_DISPOSITION2 = (String)p.get("exit2");
			LABEL_EXIT_DISPOSITION3 = (String)p.get("exit3");
			LABEL_EXIT_DISPOSITION4 = (String)p.get("exit4");
			LABEL_EXIT_DISPOSITION5 = (String)p.get("exit5");
			LABEL_EXIT_DISPOSITION6 = (String)p.get("exit6");
			LABEL_EXIT_DISPOSITION7 = (String)p.get("exit7");			
			
			
			NODEID_BASELINE_LIVING_ARRANGEMENT1 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT1"));
			NODEID_BASELINE_LIVING_ARRANGEMENT2 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT2"));
			NODEID_BASELINE_LIVING_ARRANGEMENT3 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT3"));
			NODEID_BASELINE_LIVING_ARRANGEMENT4 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT4"));
			NODEID_BASELINE_LIVING_ARRANGEMENT5 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT5"));
			NODEID_BASELINE_LIVING_ARRANGEMENT6 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT6"));
			NODEID_BASELINE_LIVING_ARRANGEMENT7 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT7"));
			NODEID_BASELINE_LIVING_ARRANGEMENT8 = Integer.parseInt((String)p.get("NODEID_BASELINE_LIVING_ARRANGEMENT8"));
			
			LABEL_BASELINE_LIVING_ARRANGEMENT1 = (String)p.get("living_arrangement1");
			LABEL_BASELINE_LIVING_ARRANGEMENT2 = (String)p.get("living_arrangement2");
			LABEL_BASELINE_LIVING_ARRANGEMENT3 = (String)p.get("living_arrangement3");
			LABEL_BASELINE_LIVING_ARRANGEMENT4 = (String)p.get("living_arrangement4");
			LABEL_BASELINE_LIVING_ARRANGEMENT5 = (String)p.get("living_arrangement5");
			LABEL_BASELINE_LIVING_ARRANGEMENT6 = (String)p.get("living_arrangement6");
			LABEL_BASELINE_LIVING_ARRANGEMENT7 = (String)p.get("living_arrangement7");
			LABEL_BASELINE_LIVING_ARRANGEMENT8 = (String)p.get("living_arrangement8");
			
			NODEID_CURRENT_LIVING_ARRANGEMENT1 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT1"));
			NODEID_CURRENT_LIVING_ARRANGEMENT2 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT2"));
			NODEID_CURRENT_LIVING_ARRANGEMENT3 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT3"));
			NODEID_CURRENT_LIVING_ARRANGEMENT4 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT4"));
			NODEID_CURRENT_LIVING_ARRANGEMENT5 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT5"));
			NODEID_CURRENT_LIVING_ARRANGEMENT6 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT6"));
			NODEID_CURRENT_LIVING_ARRANGEMENT7 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT7"));
			NODEID_CURRENT_LIVING_ARRANGEMENT8 = Integer.parseInt((String)p.get("NODEID_CURRENT_LIVING_ARRANGEMENT8"));
			
			
			
			
			
		}catch(NumberFormatException e) {log.warn(e);}
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
        	throw new SecurityException("missing required security object (_report)");
        }
		
		String target = "success";   
        
        if(request.getParameter("action") != null && request.getParameter("action").equalsIgnoreCase("download")) {
        	target="download";
        }
        
        Map<StreetHealthReportKey,Integer> results = new HashMap<StreetHealthReportKey,Integer>();
        
        
        loadNodeIdsWithNoLabels();
        
        //initialize business manager
        this.mgr = (StreetHealthReportManager) SpringUtils.getBean("streetHealthReportManager");
        
      
        //check to make sure use is logged in
        HttpSession session = request.getSession(false);
        if (session == null) {
            target = "timeout";
            return(mapping.findForward(target));
        }
        
        //get start date parameter
        String strStartDate = request.getParameter("startDate");
        if(strStartDate==null) {
        	request.setAttribute("ERROR_MSG", "No start date found.");
        	return(mapping.findForward(target));
        }
        Date startDate = this.stringToDate(strStartDate, SDF_PATTERN);
        if(startDate ==null) {
        	request.setAttribute("ERROR_MSG", "Invalid start date.");
        	return(mapping.findForward(target));
        }
        
        //based on the start date, return the "cohorts".
        List<DateRange> dates = getDates(startDate);        
        request.setAttribute("dates",dates);
                  
        //for each cohort, extract the values from the intakes
        for(int x=0;x<dates.size();x++) {
        	DateRange dr = dates.get(x);       
        	List cohort = mgr.getCohort(dr.getStartDate(), dr.getEndDate(), loggedInInfo.getCurrentFacility().getId());
        	if (cohort != null) {          
        		getCohortCount(results,cohort,x);
            }
        }
        
        request.setAttribute("results", results);
        request.setAttribute("nCohorts", dates.size());              
        return(mapping.findForward(target));
    }
    

    
    /*
     * For each intake in the cohort, pull out the values, and add to the results map 
     * 
     */
	public void getCohortCount(Map<StreetHealthReportKey,Integer> results, List cohortList, int idx) {
        int minAge=0;
        int maxAge=0;
        int avgAgeTotal=0;
        int avgAgeSize=0;
        
        for (int x = 0; x < cohortList.size(); x++) {
        	Intake intake = new Intake();
            Demographic demographic = new Demographic();
            Object[] cohort = (Object[]) cohortList.get(x);            
            boolean preAdmission = false;

            demographic = (Demographic) cohort[1];           
            intake = (Intake) cohort[0];
            
            //pre-admission
            String strPreAdmission =  intake.getAnswerKeyValues().get("Pre-Admission");
            if(strPreAdmission!=null && strPreAdmission.equals("T")) {
            	preAdmission=true;
            	addToResults(results,idx,"Total Service Recipients","Unique individuals - pre-admission");
            }  else {
            	addToResults(results,idx,"Total Service Recipients","Unique individuals - admitted");
            }
            
            
            //gender
            String gender = demographic.getSex();
            if(gender.equalsIgnoreCase("m")) {
            	addToResults(results,idx,"Gender","Male");
            } else if(gender.equalsIgnoreCase("f")) {
            	addToResults(results,idx,"Gender","Female");
            } else {
            	addToResults(results,idx,"Gender","Other");
            }
            
            
            //age
            int age = demographic.getAgeInYears();
            if(age <= 15) {
            	addToResults(results,idx,"Age","0-15");
            } else if(age >= 16 && age <= 17) {
            	addToResults(results,idx,"Age","16-17");
            } else if(age >= 18 && age <= 24) {
            	addToResults(results,idx,"Age","18-24");
            } else if(age >= 25 && age <= 34) {
            	addToResults(results,idx,"Age","25-34");
            } else if(age >= 35 && age <= 44) {
            	addToResults(results,idx,"Age","35-44");
            } else if(age >= 45 && age <= 54) {
            	addToResults(results,idx,"Age","45-54");
            } else if(age > 55 && age <= 64) {
            	addToResults(results,idx,"Age","55-64");
            } else if(age > 65 && age <= 74) {
            	addToResults(results,idx,"Age","65-74");
            } else if(age > 75 && age <= 84) {
            	addToResults(results,idx,"Age","75-84");
            } else {
            	addToResults(results,idx,"Age","85 and over");
            }
                   
            //extra age stats
            if(age < minAge || idx==0) {
            	minAge=age;
            }
            
            if(age > maxAge || idx==0) {
            	maxAge=age;
            }
            
            if(age != 0 ) {
            	avgAgeTotal+=age;
            	avgAgeSize++;
            }
            
            //service recipient - hard coded to toronto
            addToResults(results,idx,"Service Recipient Location","Toronto");
            
            //Aboriginal Origin
            String aboriginal = getIntakeAnswer(intake,"Ethno-racial Background");            
            addToResults(results,idx,"Aboriginal Origin",aboriginal);
            
            
            //language
            String languageEnglish = getIntakeAnswer(intake,"First Language English");
            String languageOther = getIntakeAnswer(intake,"If No, Service Recipient Preferred Language");
            String language="";
            if(languageEnglish!=null && languageEnglish.equalsIgnoreCase("yes")) {
            	language="English";
            }
            if(languageOther!=null && languageOther.indexOf("french") != -1) {
            	language="French";
            } else {
            	language="Other";
            }            
            addToResults(results,idx,"Service Recipient Preferred Language",language);
            
            
            //baseline legal
            if(!preAdmission) {
            	String bLegal = getIntakeAnswer(intake,"Baseline Legal Status");           
            	addToResults(results,idx,"Baseline Legal Status",bLegal);
            }
            
            //current legal
            if(!preAdmission) {
            	String cLegal = getIntakeAnswer(intake,"Current Legal Status");            
            	addToResults(results,idx,"Current Legal Status",cLegal);
            }
            
            //CTO
            if(!preAdmission) {
            	String cto = getIntakeAnswer(intake,"Community Treatment Orders");            
            	addToResults(results,idx,"Community Treatment Orders",cto);
            }
            
            //primary diagnosis
            if(!preAdmission) {
	            String primaryDiagnosis = getIntakeAnswer(intake,"Primary Diagnosis");
	            addToResults(results,idx,"Diagnostic Categories",primaryDiagnosis);
            }
            
            //other illness
            if(!preAdmission) {
	            String otherIllness_concurrent = getIntakeAnswerByNodeId(intake, NODEID_OTHER_ILLNESS_CONCURRENT);
	            String otherIllness_dual = getIntakeAnswerByNodeId(intake, NODEID_OTHER_ILLNESS_DUAL_DIAGNOSIS);
	            String otherIllness_mohltc = getIntakeAnswerByNodeId(intake, NODEID_OTHER_ILLNESS_MOHLTC_INITIATIVES);
	            String otherIllness_other = getIntakeAnswerByNodeId(intake, NODEID_OTHER_ILLNESS_OTHER_CHRONIC);
	            String otherIllness_not = getIntakeAnswerByNodeId(intake, NODEID_OTHER_ILLNESS_NOT_APPLICABLE);
	                
	            if("T".equalsIgnoreCase(otherIllness_concurrent)) {
	            	addToResults(results,idx,"Other Illness Information",LABLE_OTHER_ILLNESS_CONCURRENT);
	            }
	            if("T".equalsIgnoreCase(otherIllness_dual)) {
	            	addToResults(results,idx,"Other Illness Information",LABLE_OTHER_ILLNESS_DUAL_DIAGNOSIS);
	            }
	            if("T".equalsIgnoreCase(otherIllness_mohltc)) {
	            	addToResults(results,idx,"Other Illness Information",LABLE_OTHER_ILLNESS_MOHLTC_INITIATIVES);
	            }
	            if("T".equalsIgnoreCase(otherIllness_other)) {
	            	addToResults(results,idx,"Other Illness Information",LABLE_OTHER_ILLNESS_OTHER_CHRONIC);
	            }
	            if("T".equalsIgnoreCase(otherIllness_not)) {
	            	addToResults(results,idx,"Other Illness Information",LABLE_OTHER_ILLNESS_NOT_APPLICABLE);
	            }
        	}
            //presenting issues
            
            //String presentingIssues = getIntakeAnswer(intake,"Presenting Issues");
            //addToResults(results,idx,"Presenting Issues",presentingIssues);
            if(!preAdmission) {
	            String presentingIssues_1 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_1);
	            String presentingIssues_2 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_2);
	            String presentingIssues_3 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_3);
	            String presentingIssues_4 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_4);
	            String presentingIssues_5 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_5);
	            String presentingIssues_6 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_6);
	            String presentingIssues_7 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_7);
	            String presentingIssues_8 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_8);
	            String presentingIssues_9 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_9);
	            String presentingIssues_10 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_10);
	            String presentingIssues_11 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_11);
	            String presentingIssues_12 = getIntakeAnswerByNodeId(intake,NODEID_PRESENTING_ISSUES_12);
	            
	            if("T".equalsIgnoreCase(presentingIssues_1)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_1);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_2)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_2);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_3)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_3);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_4)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_4);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_5)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_5);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_6)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_6);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_7)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_7);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_8)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_8);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_9)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_9);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_10)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_10);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_11)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_11);
	            }
	            if("T".equalsIgnoreCase(presentingIssues_12)) {
	            	addToResults(results,idx,"Presenting Issues",LABEL_PRESENTING_ISSUES_12);
	            }
        	}            
            
          	//source of referral
            if(!preAdmission) {
            	//String srcOfReferral = getIntakeAnswer(intake,"Source of Referral");
            	//if(srcOfReferral!=null && srcOfReferral.equals("Criminal Justice System")) {
            	//	srcOfReferral = getIntakeAnswer(intake,"If Criminal Justice System");
            	//}
            	//addToResults(results,idx,"Source of Referral",srcOfReferral);
            	String srcOfReferral_1 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL1);
            	String srcOfReferral_2 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL2);
            	String srcOfReferral_3 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL3);
            	String srcOfReferral_4 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL4);
            	String srcOfReferral_5 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL5);
            	String srcOfReferral_6 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL6);
            	String srcOfReferral_7 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL7);
            	String srcOfReferral_8 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL8);
            	String srcOfReferral_9 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL9);
            	String srcOfReferral_10 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL10);
            	String srcOfReferral_11 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL11);
            	String srcOfReferral_12 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL12);
            	String srcOfReferral_13 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL13);
            	String srcOfReferral_14 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL14);
            	String srcOfReferral_15 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL15);
            	String srcOfReferral_16 = getIntakeAnswerByNodeId(intake,NODEID_REFERRAL16);
            	
            	if("T".equalsIgnoreCase(srcOfReferral_1)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL1);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_2)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL2);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_3)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL3);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_4)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL4);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_5)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL5);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_6)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL6);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_7)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL7);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_8)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL8);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_9)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL9);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_10)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL10);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_11)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL11);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_12)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL12);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_13)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL13);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_14)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL14);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_15)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL15);
                }
            	if("T".equalsIgnoreCase(srcOfReferral_16)) {
                	addToResults(results,idx,"Source of Referral",LABEL_REFERRAL16);
                }            	    			
            }
            
            //exit disposition
            if(!preAdmission) {
            	//String exitDisposition = getIntakeAnswer(intake,"Exit Disposition");
            	//addToResults(results,idx,"Exit Disposition",exitDisposition);
            	String exitDisposition_1 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION1);
            	String exitDisposition_2 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION2);
            	String exitDisposition_3 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION3);
            	String exitDisposition_4 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION4);
            	String exitDisposition_5 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION5);
            	String exitDisposition_6 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION6);
            	String exitDisposition_7 = getIntakeAnswerByNodeId(intake,NODEID_EXIT_DISPOSITION7);
            	
            	if("T".equalsIgnoreCase(exitDisposition_1)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION1);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_2)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION2);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_3)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION3);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_4)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION4);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_5)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION5);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_6)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION6);
                }   
            	if("T".equalsIgnoreCase(exitDisposition_7)) {
                	addToResults(results,idx,"Exit Disposition",LABEL_EXIT_DISPOSITION7);
                }   
            	
            }
            
            //hospitalizations
            if(!preAdmission) {
	            List<HospitalizationBean> hospitalizations = new ArrayList<HospitalizationBean>();
	            hospitalizations.add(getHospitalizationInfo(intake,"h1",NODEID_HOSPITALIZATION1_DATE,NODEID_HOSPITALIZATION1_LENGTH,NODEID_HOSPITALIZATION1_PSYCH,NODEID_HOSPITALIZATION1_PHYS,NODEID_HOSPITALIZATION1_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h2",NODEID_HOSPITALIZATION2_DATE,NODEID_HOSPITALIZATION2_LENGTH,NODEID_HOSPITALIZATION2_PSYCH,NODEID_HOSPITALIZATION2_PHYS,NODEID_HOSPITALIZATION2_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h3",NODEID_HOSPITALIZATION3_DATE,NODEID_HOSPITALIZATION3_LENGTH,NODEID_HOSPITALIZATION3_PSYCH,NODEID_HOSPITALIZATION3_PHYS,NODEID_HOSPITALIZATION3_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h4",NODEID_HOSPITALIZATION4_DATE,NODEID_HOSPITALIZATION4_LENGTH,NODEID_HOSPITALIZATION4_PSYCH,NODEID_HOSPITALIZATION4_PHYS,NODEID_HOSPITALIZATION4_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h5",NODEID_HOSPITALIZATION5_DATE,NODEID_HOSPITALIZATION5_LENGTH,NODEID_HOSPITALIZATION5_PSYCH,NODEID_HOSPITALIZATION5_PHYS,NODEID_HOSPITALIZATION5_DECLINED));

	            
	            //current psychiatric hospitalizations
	            int numDaysHospitalized=0;
	            int numPsychHospitalizations=0;
	            int numDeclined = 0;
	            
	            
	            for(HospitalizationBean hospitalization:hospitalizations) {
	            	try {
	            		if(hospitalization.getPsychiatric().equals("T")) {
	            			numPsychHospitalizations++;
	            			int length = Integer.parseInt(hospitalization.getLength());
	            			numDaysHospitalized += length;
	            		}
	            		if(hospitalization.getDeclined().equalsIgnoreCase("T")) {
	            			numDeclined++;
	            		}
	            	} catch(NumberFormatException e) {
	            		MiscUtils.getLogger().warn("warning",e);
	            	}
	            }
	           
	            addToResults(results,idx,"Current Psychiatric Hospitalizations","Total Number of Episodes",numPsychHospitalizations);
	            addToResults(results,idx,"Current Psychiatric Hospitalizations","Total Number of Hospitalization Days",numDaysHospitalized);
	            addToResults(results,idx,"Current Psychiatric Hospitalizations","Unknown or Service Recipient Declined",numDeclined);
	            
            }
            
            //baseline living arrangements
            if(!preAdmission) {
            	//String bLivingArrangements = getIntakeAnswer(intake,"Baseline Living Arrangement");
            	//addToResults(results,idx,"Baseline Living Arrangement",bLivingArrangements);
            	String bLivingArrangements_1 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT1);
            	String bLivingArrangements_2 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT2);
            	String bLivingArrangements_3 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT3);
            	String bLivingArrangements_4 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT4);
            	String bLivingArrangements_5 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT5);
            	String bLivingArrangements_6 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT6);
            	String bLivingArrangements_7 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT7);
            	String bLivingArrangements_8 = getIntakeAnswerByNodeId(intake,NODEID_BASELINE_LIVING_ARRANGEMENT8);
            	
            	if("T".equalsIgnoreCase(bLivingArrangements_1)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT1);
                }   
            	if("T".equalsIgnoreCase(bLivingArrangements_2)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT2);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_3)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT3);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_4)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT4);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_5)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT5);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_6)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT6);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_7)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT7);
                }  
            	if("T".equalsIgnoreCase(bLivingArrangements_8)) {
                	addToResults(results,idx,"Baseline Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT8);
                }  
            }
            
          	//current living arrangements
            if(!preAdmission) {
            	//String cLivingArrangements = getIntakeAnswer(intake,"Current Living Arrangement");
            	//addToResults(results,idx,"Current Living Arrangement",cLivingArrangements);
            	
            	String cLivingArrangements_1 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT1);
            	String cLivingArrangements_2 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT2);
            	String cLivingArrangements_3 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT3);
            	String cLivingArrangements_4 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT4);
            	String cLivingArrangements_5 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT5);
            	String cLivingArrangements_6 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT6);
            	String cLivingArrangements_7 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT7);
            	String cLivingArrangements_8 = getIntakeAnswerByNodeId(intake,NODEID_CURRENT_LIVING_ARRANGEMENT8);
            	
            	if("T".equalsIgnoreCase(cLivingArrangements_1)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT1);
                }   
            	if("T".equalsIgnoreCase(cLivingArrangements_2)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT2);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_3)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT3);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_4)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT4);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_5)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT5);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_6)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT6);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_7)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT7);
                }  
            	if("T".equalsIgnoreCase(cLivingArrangements_8)) {
                	addToResults(results,idx,"Current Living Arrangement",LABEL_BASELINE_LIVING_ARRANGEMENT8);
                }  
            }
            
            //baseline residence type
            if(!preAdmission) {
            	String bResidenceType = getIntakeAnswerByNodeId(intake,"Baseline Primary Residence Type",NODEID_BASELINE_RESIDENCE_TYPE);            	
            	addToResults(results,idx,"Baseline Primary Residence Type",bResidenceType);
            }
            
            //baseline residence status
            if(!preAdmission) {
            	String bResidenceStatus = getIntakeAnswer(intake,"Baseline Residence Status");
            	addToResults(results,idx,"Baseline Residence Status",bResidenceStatus);
            }
            
            //current residence type
            if(!preAdmission) {
            	String cResidenceType = getIntakeAnswerByNodeId(intake,"Current Primary Residence Type",NODEID_CURRENT_RESIDENCE_TYPE);            	
            	addToResults(results,idx,"Current Primary Residence Type",cResidenceType);
            }
            
            //current residence status
            if(!preAdmission) {
            	String cResidenceStatus = getIntakeAnswer(intake,"Current Residence Status");
            	addToResults(results,idx,"Current Residence Status",cResidenceStatus);
            }
            
            //baseline employment status
            if(!preAdmission) {
            	String bEmploymentStatus = getIntakeAnswer(intake,"Baseline Employment Status");
            	addToResults(results,idx,"Baseline Employment Status",bEmploymentStatus);
            }
            
          	//current employment status
            if(!preAdmission) {
            	String cEmploymentStatus = getIntakeAnswer(intake,"Current Employment Status");
            	addToResults(results,idx,"Current Employment Status",cEmploymentStatus);
            }
                     
         	//baseline educational status
            if(!preAdmission) {
            	String bEducationalStatus = getIntakeAnswer(intake,"Baseline Educational Status (participating in education at intake)");            	
            	addToResults(results,idx,"Baseline Educational Status",bEducationalStatus);
            }
            
          	//current educational status
            if(!preAdmission) {
            	String cEducationalStatus = getIntakeAnswer(intake,"Current Educational Status (participating in education at intake)");
            	addToResults(results,idx,"Current Educational Status",cEducationalStatus);
            }
                       
            //current highest level of education
            if(!preAdmission) {
            	String highestLevelOfEducation = getIntakeAnswer(intake,"Current Highest Level of Education at Intake");
            	addToResults(results,idx,"Current Highest Level of Education",highestLevelOfEducation);
            }
            
           
            //baseline primary income source
            if(!preAdmission) {
            	String bPrimaryIncomeSource = getIntakeAnswer(intake,"Baseline Primary Income Source");
            	addToResults(results,idx,"Baseline Primary Income Source",bPrimaryIncomeSource);
            }
            
            //current primary income source
            if(!preAdmission) {
            	String cPrimaryIncomeSource = getIntakeAnswer(intake,"Current Primary Income Source");
            	addToResults(results,idx,"Current Primary Income Source",cPrimaryIncomeSource);
            }
            
        }
        
        addToResults(results,idx,"Age","Minimum Age (ACT only)",minAge);
        addToResults(results,idx,"Age","Maximum Age (ACT only)",maxAge);
        addToResults(results,idx,"Age","Average Age (ACT only)",(int)((double)avgAgeTotal/(double)avgAgeSize));
    }
    
    public HospitalizationBean getHospitalizationInfo(Intake intake, String label, int date, int length, int psych, int phys, int declined) {
    	HospitalizationBean bean = new HospitalizationBean();
    	
    	bean.setDate(getIntakeAnswerByNodeId(intake,date));
    	bean.setLength(getIntakeAnswerByNodeId(intake,length));
    	bean.setPsychiatric(getIntakeAnswerByNodeId(intake,psych));
    	bean.setPhysical(getIntakeAnswerByNodeId(intake,phys));
    	bean.setDeclined(getIntakeAnswerByNodeId(intake,declined));
    	
    	
    	return bean;
    }
     
    /*
     * Creates a set of 10 date ranges.
     * The first one will be yyyy-03-01 to startdate.
     * The next ones will each by 1 yr in length
     */
    List<DateRange> getDates(Date startDate) {
    	List<DateRange> ranges = new ArrayList<DateRange>();
    	
    	//first one is special
    	Date cohort0StartDate=null;
    	Date cohort0EndDate=startDate;
    	String strDate = dateToString(startDate, SDF_PATTERN);

        if (strDate.substring(4).compareTo(COHORT_CRITICAL_YM) > 0) {
            cohort0StartDate = stringToDate(strDate.substring(0, 4) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }
        else {
        	cohort0StartDate = stringToDate(Integer.toString(Integer.parseInt(strDate.substring(0, 4)) - 1) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }
        ranges.add(new DateRange(cohort0StartDate,cohort0EndDate));
        
        
        for(int x=0;x<9;x++) {
        	DateRange lastDateRange = ranges.get(ranges.size()-1);
        	
        	Date eDate = lastDateRange.getStartDate();        	
        	Date sDate = this.addYears(eDate,-1);
        	
        	ranges.add(new DateRange(sDate,eDate));        	
        }
        
    	return ranges;
    	
    }
    
    public void addToResults(Map<StreetHealthReportKey,Integer> results,int cohortIdx, String label, String answer) {
    	log.info("adding to result=" + cohortIdx + "," + label  + "," + answer);
    	StreetHealthReportKey key = new StreetHealthReportKey(cohortIdx,label,answer);
    	if(results.get(key) == null) {
    		results.put(key,1);
    	} else {
    		int x = results.get(key);
    		x++;
    		results.put(key,x);
    	}
    }
    
    public void addToResults(Map<StreetHealthReportKey,Integer> results, int cohortIdx, String label, String answer, int increment) {
    	log.info("adding to result=" + cohortIdx + "," + label  + "," + answer);
    	StreetHealthReportKey key = new StreetHealthReportKey(cohortIdx,label,answer);
    	if(results.get(key) == null) {
    		results.put(key,increment);
    	} else {
    		int x = results.get(key);
    		x+=increment;
    		results.put(key,x);
    	}
    }

    /* DATE FUNCTIONS */
    private Date stringToDate(String date, String pattern){
    	try {
    		return new SimpleDateFormat(pattern).parse(date);
    	}catch(ParseException e) {MiscUtils.getLogger().error("Error", e);}
    	return null;
    }
    
    private String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date).toString();
    }
    
    private Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }
    
    
    /* INTAKE FUNCTIONS */
    
    public String getIntakeAnswer(Intake intake, String key) {
    	String value = intake.getAnswerKeyValues().get(key);
    	if(value!=null && value.equals("Declined")) {
    		value="Unknown or Service Recipient Declined";
    	}
    	log.info(key + "=" + value);
    	return value;
    }
    
    public String getIntakeAnswerByNodeId(Intake intake, String key, int nodeId) {
    	IntakeAnswer answer = intake.getAnswerMapped(String.valueOf(nodeId));
    	String value = answer.getValue();
    	log.info(key + "=" + value);
    	return value;
    }
    
    public String getIntakeAnswerByNodeId(Intake intake, int nodeId) {
    	IntakeAnswer answer = intake.getAnswerMapped(String.valueOf(nodeId));
    	String value = answer.getValue();
    	log.info(nodeId + " answer =" + value);
    	return value;
    }
}
