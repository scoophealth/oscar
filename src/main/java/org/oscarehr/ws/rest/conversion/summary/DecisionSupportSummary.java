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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.renal.CkdScreener;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import oscar.OscarProperties;




@Component
public class DecisionSupportSummary implements Summary{
	private static Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private DSService  dsService =  null;

	@Autowired @Qualifier("DxresearchDAO")
    private  DxresearchDAO dxResearchDao;
    
	//protected static final String ELLIPSES = "...";
	//protected static final int MAX_LEN_TITLE = 48;
	//protected static final int CROP_LEN_TITLE = 45;
	//protected static final int MAX_LEN_KEY = 12;
	//protected static final int CROP_LEN_KEY = 9;
	
	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("Descion Support",0,SummaryTo1.DECISIONSUPPORT_CODE);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		int count = 0; 
	    
	    if(OscarProperties.getInstance().getProperty("ORN_PILOT", "yes").equalsIgnoreCase("yes") && (OscarProperties.getInstance().getProperty("ckd_notification_scheme","dsa").equals("dsa")||OscarProperties.getInstance().getProperty("ckd_notification_scheme","dsa").equals("all"))) {
		    fillCKD( loggedInInfo, list,demographicNo,count);
	    }
	    
	    
	    fillDSGuidelines( loggedInInfo, list,demographicNo,count);
		return summary;
	}
	
	private  void  fillCKD(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list,Integer demographicNo,int count){
		CkdScreener ckdScreener = new CkdScreener();
        List<String> reasons =new ArrayList<String>();
        boolean match = ckdScreener.screenDemographic(demographicNo,reasons, null);
        boolean notify=false;
        
        for(Dxresearch dr:dxResearchDao.find(demographicNo, "OscarCode", "CKDSCREEN")) {
        	//we have an active one, we should notify
        	if(dr.getStatus() == 'A') {
        		notify=true;
        	}
        }
        for(Dxresearch dr:dxResearchDao.find(demographicNo, "icd9", "585")) {
        	if(dr.getStatus() == 'A') {
        		notify=false;
        	}
        }
        if(!notify) {
        	//there's no active ones, but let's look at the latest one
        	List<Dxresearch> drs = dxResearchDao.find(demographicNo, "OscarCode", "CKDSCREEN");
        	if(drs.size()>0) {
        		Dxresearch dr = drs.get(0);
        		Calendar aYearAgo = Calendar.getInstance();
        		aYearAgo.add(Calendar.MONTH, -12);
        		if(dr.getUpdateDate().before(aYearAgo.getTime())) {
        			notify=true;
        			//reopen it
        			dr.setStatus('A');
        			dr.setUpdateDate(new Date());
        			dxResearchDao.merge(dr);
        			//need some way to notify that tab to reload
        			// This won't work anymore not sure of the effectsjavascript.append("jQuery(document).ready(function(){reloadNav('Dx');});");
        		}
        	}
        }
        if(match && notify) {
        	 SummaryItemTo1 summaryItem = new SummaryItemTo1(count, "Screen for CKD","action","ckd");
        	 summaryItem.setAction("../renal/CkdDSA.do?method=detail&demographic_no=" + demographicNo);
            
        }
	}
	
	
	private void fillDSGuidelines(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list,Integer demographicNo,int count){
		List<DSGuideline> dsGuidelines = dsService.getDsGuidelinesByProvider(loggedInInfo.getLoggedInProviderNo());
		for(DSGuideline dsGuideline: dsGuidelines) {
        	if(OscarProperties.getInstance().getProperty("dsa.skip."+dsGuideline.getTitle().replaceAll(" ", "_"),"false").equals("true")) {
        		continue;
        	}
            try {
                List<DSConsequence> dsConsequences = dsGuideline.evaluate(loggedInInfo, ""+demographicNo);
                if (dsConsequences == null) continue;
                for (DSConsequence dsConsequence: dsConsequences) {
                    if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.warning)
                        continue;
                    
                    SummaryItemTo1 summaryItem = new SummaryItemTo1(dsGuideline.getId(), dsGuideline.getTitle(),"action","dsguideline");
                    
                    String url = "../oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=" + dsGuideline.getId() + "&provider_no=" + loggedInInfo.getLoggedInProviderNo()+ "&demographic_no=" + demographicNo + "&parentAjaxId='); return false;";
                    
                    summaryItem.setDate(dsGuideline.getDateStart());
                    summaryItem.setAction(url);
                    list.add(summaryItem);
                    //if (dsConsequence.getConsequenceStrength() == DSConsequence.ConsequenceStrength.warning) {
                     //   item.setColour("#ff5409;");
                    //}
   
                }
            } catch (Exception e) {
                logger.error("Unable to evaluate patient against a DS guideline '" + dsGuideline.getTitle() + "' of UUID '" + dsGuideline.getUuid() + "'", e);
            }
        }

	}
	
}
