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

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProviderData;
import org.oscarehr.common.dao.ProviderDataDao;
/**
 *
 * @author jackson
 */
public class SearchProviderAutoCompleteAction extends DispatchAction{
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
        String searchStr=request.getParameter("providerKeyword");
        if(searchStr==null){
            searchStr=request.getParameter("query");
        }
        if(searchStr==null){
            searchStr=request.getParameter("name");
        }        
       
        
        MiscUtils.getLogger().info("Search Provider " + searchStr);
        List provList=ProviderData.searchProvider(searchStr,true);
        Hashtable d=new Hashtable();
        d.put("results", provList);

        response.setContentType("text/x-json");
        JSONObject jsonArray=(JSONObject) JSONSerializer.toJSON(d);
        jsonArray.write(response.getWriter());
        return null;

    }
    
    public ActionForward labSearch(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	String searchStr = request.getParameter("term");
    	String firstName, lastName;
    	
    	if( searchStr.indexOf(",") != -1 ) {
    		String[] searchParams = searchStr.split(",");
    		lastName = searchParams[0].trim();
    		firstName = searchParams[1].trim();
    	}
    	else {
    		lastName = searchStr;
    		firstName = null;
    	}
    	
    	ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
    	List<org.oscarehr.common.model.ProviderData> provList = providerDataDao.findByName(firstName, lastName, true);
    	StringBuilder searchResults = new StringBuilder("[");
    	int idx = 0;
    	
    	for( org.oscarehr.common.model.ProviderData provData : provList ) {
    		searchResults.append("{\"label\":\"" + provData.getLastName() + ", " + provData.getFirstName() + "\",\"value\":\"" + provData.getId() + "\"}");
    		if( idx < provList.size() - 1 ) {
    			searchResults.append(",");
    		}
    		++idx;
    	}
    	
    	searchResults.append("]");
    	
    	response.setContentType("text/x-json");
    	MiscUtils.getLogger().info(searchResults.toString());
    	response.getWriter().write(searchResults.toString());
    	/*HashMap<String,String>searchResults = new HashMap<String,String>();
    	
    	for( org.oscarehr.common.model.ProviderData provData : provList ) {
    		searchResults.put(provData.getId(), provData.getLastName() + ", " + provData.getFirstName());
    	}
    	
    	JSONObject json = (JSONObject) JSONSerializer.toJSON(searchResults);
    	
    	response.setContentType("text/x-json");
        json.write(response.getWriter());
        
    	MiscUtils.getLogger().info(json);
    	*/
    	return null;
    }
}
