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


package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.util.RxDrugRef;

public final class RxSearchDrugAction extends DispatchAction {


    @Override
    public ActionForward unspecified(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

            // Setup variables
            RxSearchDrugForm reqForm = (RxSearchDrugForm) form;
            String genericSearch = reqForm.getGenericSearch();
            String searchString = reqForm.getSearchString();
	    String searchRoute = reqForm.getSearchRoute();
	    if (searchRoute==null) searchRoute = "";

            RxDrugData drugData = new RxDrugData();             
                                   
            RxDrugData.DrugSearch drugSearch = null;
    
            try{
                if (genericSearch != null ){                    
                    drugSearch = drugData.listDrugFromElement(genericSearch);
                }
		else if (!searchRoute.equals("")){
		    drugSearch = drugData.listDrugByRoute(searchString, searchRoute);
		} else {
                    drugSearch = drugData.listDrug(searchString);
                }
            }catch(Exception connEx){
            	MiscUtils.getLogger().error("Error", connEx);
            }
            request.setAttribute("drugSearch", drugSearch);
            request.setAttribute("demoNo", reqForm.getDemographicNo());

            return (mapping.findForward("success"));
    }

    public ActionForward jsonSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {

        String searchStr = request.getParameter("query");
        if (searchStr == null){
            searchStr = request.getParameter("name");
        }

        String wildcardRightOnly = OscarProperties.getInstance().getProperty("rx.search_right_wildcard_only", "false");       
               
        RxDrugRef drugref = new RxDrugRef();
        Vector<Hashtable> vec = drugref.list_drug_element3(searchStr,Boolean.valueOf(wildcardRightOnly));
        
        Hashtable d = new Hashtable();
        d.put("results",vec);
        response.setContentType("text/x-json");
        
        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }
    public ActionForward jsonSearchVerbose(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {

        String searchStr = request.getParameter("query");
        if (searchStr == null){
            searchStr = request.getParameter("name");
        }

        String wildcardRightOnly = OscarProperties.getInstance().getProperty("rx.search_right_wildcard_only", "false");       
               
        RxDrugRef drugref = new RxDrugRef();
        Vector<Hashtable> vec = drugref.list_drug_element3(searchStr,Boolean.valueOf(wildcardRightOnly));
        
        Hashtable d = new Hashtable();
        d.put("results",vec);
        response.setContentType("text/x-json");
        
        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }


}
