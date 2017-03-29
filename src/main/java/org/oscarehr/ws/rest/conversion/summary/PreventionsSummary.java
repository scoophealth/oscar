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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.managers.PreventionManager;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.oscarPrevention.PreventionDS;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;


@Component
public class PreventionsSummary implements Summary {

	@Autowired
	private PreventionManager preventionManager;
	
	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("Preventions",0,SummaryTo1.PREVENTIONS);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		
		preventionsList(loggedInInfo, list, demographicNo);
		
		return summary;
	}
	

	@SuppressWarnings("unchecked")
	private void preventionsList(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list, Integer demographicNo){
		
		String url = "../oscarPrevention/index.jsp?demographic_no="+demographicNo;
		
		//saved preventions for demographicNo
		List<Prevention> preventions = preventionManager.getPreventionsByDemographicNo(loggedInInfo, demographicNo);
		
		SummaryItemTo1 summaryItem;
				
		oscar.oscarPrevention.Prevention p = PreventionData.getPrevention(loggedInInfo, demographicNo);

        PreventionDS pf = SpringUtils.getBean(PreventionDS.class);

        try{
            pf.getMessages(p);
        }catch(Exception dsException){
            //do nothing for now
        }
        
        //create list of preventions
        PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
        ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo); 
        
        //get warnings from list of preventions for demographicNo
		@SuppressWarnings("rawtypes")
		Map warningTable = p.getWarningMsgs();
				
		List<String> items = new ArrayList<String>();

		for (int i = 0 ; i < prevList.size(); i++){
			
			HashMap<String,String> h = prevList.get(i);
            String prevName = h.get("name");
            ArrayList<Map<String,Object>> alist = PreventionData.getPreventionData(loggedInInfo, prevName, demographicNo);
            Date demographicDateOfBirth=PreventionData.getDemographicDateOfBirth(loggedInInfo, demographicNo);
            PreventionData.addRemotePreventions(loggedInInfo, alist, demographicNo,prevName,demographicDateOfBirth);
            boolean show = pdc.display(loggedInInfo, h, demographicNo.toString(),alist.size());
            if( show ) {
              //add warnings right away so they display first
        	  if( warningTable.containsKey(prevName) ){
					summaryItem = new SummaryItemTo1(0, prevName, url, "prevention");
					summaryItem.setIndicatorClass("highlight");
					summaryItem.setWarning(warningTable.get(prevName).toString());
					list.add(summaryItem);
              } else {
            	  
            	  if(!isPreventionExist(preventions, prevName)){//don't add to items if data entered already
                  	items.add(prevName);
            	  }
              }

            }
		}
		
		
		//add the rest of the items
        for(int idx = 0; idx < items.size(); ++idx ){
	        	summaryItem = new SummaryItemTo1(0, items.get(idx),url,"prevention");
	        	list.add(summaryItem);
        }

		for(Prevention prevention:preventions) {
			summaryItem = new SummaryItemTo1(prevention.getId(), prevention.getPreventionType(),url,"prevention");
			summaryItem.setDate(prevention.getPreventionDate());
		    if( prevention.isRefused() ) {
                summaryItem.setIndicatorClass("refused");
            }else if( prevention.isIneligible()) {
            	summaryItem.setIndicatorClass("ineligible");
            }else if(PreventionData.getExtValue(String.valueOf(prevention.getId()), "result").equals("abnormal")){
            	summaryItem.setIndicatorClass("abnormal-prev");
            }else if(PreventionData.getExtValue(String.valueOf(prevention.getId()), "result").equals("pending")){
                	summaryItem.setIndicatorClass("pending");
            }
			
			list.add(summaryItem);
		}
		
		//sort items
		//Collections.sort(list, new ChronologicAsc());
	        
	}	
	
	private boolean isPreventionExist(List<Prevention> preventions, String prevName){
		
		for(Prevention prevention:preventions) {
			if(prevention.getPreventionType().equals(prevName)){
				return true;
			}
		}
		
		return false;
	}
	
	/* @SuppressWarnings("rawtypes")
	 public class ChronologicAsc implements Comparator {
		 public int compare( Object o1, Object o2 ) {
			 Item i1 = (Item)o1;
			 Item i2 = (Item)o2;
			 Date d1 = i1.getDate();
			 Date d2 = i2.getDate();

			 if( d1 == null && d2 != null )
				 return -1;
			 else if( d1 != null && d2 == null )
				 return 1;
			 else if( d1 == null && d2 == null )
				 return 0;
			 else
				 return -(i1.getDate().compareTo(i2.getDate()));
		 }
	 }*/
	
}
