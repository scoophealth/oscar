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


package org.oscarehr.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.AppointmentUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicMerged;
import oscar.oscarRx.data.RxProviderData;
import oscar.oscarRx.data.RxProviderData.Provider;

/**
 *
 * @author jaygallagher
 */
public class SearchDemographicAutoCompleteAction extends Action {
    

	
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String providerNo = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
    	
    	boolean outOfDomain = false;
    	if(request.getParameter("outofdomain")!=null && request.getParameter("outofdomain").equals("true")) {
    		outOfDomain=true;
    	}
    	
        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao"); 
        String searchStr = request.getParameter("demographicKeyword");
        
        if (searchStr == null){
           searchStr = request.getParameter("query");
        }
        
        if (searchStr == null){
           searchStr = request.getParameter("name");
        }
        
        if(searchStr == null){
        	searchStr = request.getParameter("term");
        }
        
        boolean activeOnly = false;
        activeOnly = request.getParameter("activeOnly") != null && request.getParameter("activeOnly").equalsIgnoreCase("true");
        boolean jqueryJSON = request.getParameter("jqueryJSON") != null && request.getParameter("jqueryJSON").equalsIgnoreCase("true");
        RxProviderData rx = new RxProviderData();
        

        List<Demographic> list = null;
        
        List<Demographic> finalList = new ArrayList<Demographic>();
        

        String relatedTo = request.getParameter("relatedTo");
        
        
    	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
    	
    	List<String> searchTypes = new ArrayList<String>(); searchTypes.add("search_name");
    	List<String> searchTerms = new ArrayList<String>(); searchTerms.add(searchStr);
    	
    	if (searchStr.length() == 8 && searchStr.matches("([0-9]*)")) {
            list = demographicDao.searchMergedDemographicByDOB(searchStr.substring(0,4)+"-"+searchStr.substring(4,6)+"-"+searchStr.substring(6,8), 100, 0,providerNo,outOfDomain,relatedTo);
        } else {
        	list = demographicManager.doMultiSearch(LoggedInInfo.getLoggedInInfoFromSession(request), searchTypes, searchTerms, 100, 0, providerNo, outOfDomain, activeOnly, !activeOnly,relatedTo);
        }
    	
		DemographicMerged dmDAO = new DemographicMerged();

		for(Demographic demo : list) {
			String dem_no = demo.getDemographicNo().toString();
			String head = dmDAO.getHead(dem_no);

			if (head != null && !head.equals(dem_no)) {
				//skip non head records
				continue;
			}
			finalList.add(demo);
		}
		
/*
    	if (searchStr.length() == 8 && searchStr.matches("([0-9]*)")) {
            list = demographicDao.searchMergedDemographicByDOB(searchStr.substring(0,4)+"-"+searchStr.substring(4,6)+"-"+searchStr.substring(6,8), 100, 0,providerNo,outOfDomain);
        } 
        else if( activeOnly ) {
        	OscarProperties props = OscarProperties.getInstance();
        	String pstatus = props.getProperty("inactive_statuses", "IN, DE, IC, ID, MO, FI");
            pstatus = pstatus.replaceAll("'","").replaceAll("\\s", "");
            List<String>stati = Arrays.asList(pstatus.split(","));

        	list = demographicDao.searchDemographicByNameAndNotStatus(searchStr, stati, 100, 0, providerNo, outOfDomain,true);
        	if(list.size() == 100) {
        		MiscUtils.getLogger().warn("More results exists than returned");
        	}
        }
        else {
        	list = demographicDao.searchMergedDemographicByName(searchStr, 100, 0, providerNo, outOfDomain);
        	if(list.size() == 100) {
        		MiscUtils.getLogger().warn("More results exists than returned");
        	}
        }
*/
        
        
        List<HashMap<String, String>> secondList= new ArrayList<HashMap<String,String>>();
        for(Demographic demo :list){
            HashMap<String,String> h = new HashMap<String,String>();
             h.put("fomattedDob",demo.getFormattedDob());
             h.put("formattedName",StringEscapeUtils.escapeJava(demo.getFormattedName().replaceAll("\"", "\\\"")));
             h.put("demographicNo",String.valueOf(demo.getDemographicNo()));
             h.put("status",demo.getPatientStatus());
             

            Provider p = rx.getProvider(demo.getProviderNo());
            if ( demo.getProviderNo() != null ) {
                h.put("providerNo", demo.getProviderNo());
            }
            if ( p.getSurname() != null && p.getFirstName() != null ) {
                h.put("providerName", p.getSurname() + ", " + p.getFirstName());
            }
            
            if (OscarProperties.getInstance().isPropertyActive("workflow_enhance")) {            	 
            	 h.put("nextAppointment", AppointmentUtil.getNextAppointment(demo.getDemographicNo() + ""));
            	 DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
            	 DemographicCust demographicCust = demographicCustDao.find(demo.getDemographicNo());
            	 
            	 if (demographicCust!=null) {
            		 String cust1 = StringUtils.trimToNull(demographicCust.getNurse());
            		 String cust2 = StringUtils.trimToNull(demographicCust.getResident());
            		 String cust4 = StringUtils.trimToNull(demographicCust.getMidwife())
            				 ;
					if (cust1 != null) {
						h.put("cust1", cust1);
						p = rx.getProvider(cust1);
						h.put("cust1Name", p.getSurname() + ", " + p.getFirstName());
					}
					if (cust2 != null) {
						h.put("cust2", cust2);
						p = rx.getProvider(cust2);
						h.put("cust2Name", p.getSurname() + ", " + p.getFirstName());
					}
					if (cust4 != null) {
						h.put("cust4", cust4);
						p = rx.getProvider(cust4);
						h.put("cust4Name", p.getSurname() + ", " + p.getFirstName());
					}
 				}
 			}
             
             
             secondList.add(h);
        }

        HashMap<String,List<HashMap<String, String>>> d = new HashMap<String,List<HashMap<String, String>>>();
        d.put("results",secondList);
        response.setContentType("text/x-json");
        if( jqueryJSON ) {
        	response.getWriter().print(formatJSON(secondList));
        	response.getWriter().flush();
        }
        else {
        	JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        	jsonArray.write(response.getWriter());        	
        }
        return null;

    }
    
    private String formatJSON(List<HashMap<String, String>>info) {
    	StringBuilder json = new StringBuilder("[");
    	
    	HashMap<String, String>record;
    	int size = info.size();
    	for( int idx = 0; idx < size; ++idx) {
    		record = info.get(idx);
    		json.append("{\"label\":\"" + record.get("formattedName") + " " + record.get("fomattedDob") + " (" + record.get("status") + ")\",\"value\":\"" + record.get("demographicNo") + "\"");
    		json.append(",\"providerNo\":\"" + record.get("providerNo") + "\",\"provider\":\"" + record.get("providerName") + "\",\"nextAppt\":\"" + record.get("nextAppointment")+"\",");
    		json.append("\"formattedName\":\"" + record.get("formattedName") + "\"}");
    		
    		if( idx < size-1) {
    			json.append(",");
    		}
    	}    	
    		json.append("]");

    	//MiscUtils.getLogger().info(json.toString());
    	return json.toString();
    }

}
