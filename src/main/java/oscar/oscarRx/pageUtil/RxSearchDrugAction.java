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
import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.util.RxDrugRef;

public final class RxSearchDrugAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	private RxDrugRef drugref;
	private static Logger logger = MiscUtils.getLogger(); 
	
	public RxSearchDrugAction() {
		this.drugref = new RxDrugRef();
	}
	
    @Override
    public ActionForward unspecified(ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    				throws IOException, ServletException {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}


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
    
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    public ActionForward searchAllCategories(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) {
    	logger.debug("Calling searchAllCategories");
    	Parameter.setParameters( request.getParameterMap() );
    	Vector<Hashtable<String,Object>> results = null;         

    	
    	try {
    		results = drugref.list_drug_element3(Parameter.SEARCH_STRING, wildCardRight(Parameter.WILDCARD));        
    		jsonify(results, response);
    	} catch (IOException e) {
    		logger.error("Exception while attempting to contact DrugRef", e);
    		return mapping.findForward("error");
    	} catch (Exception e) {
    		logger.error("Unknown Error", e);
    		return mapping.findForward("error");
    	} 
    	
    	return null;
    }
    
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    public ActionForward searchBrandName(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) {  	
    	logger.debug("Calling searchBrandName");
    	Parameter.setParameters( request.getParameterMap() );
    	Vector catVec = new Vector();
    	catVec.add(RxDrugRef.CAT_BRAND);
    	Vector<Hashtable<String,Object>> results = drugref.list_search_element_select_categories(
    			Parameter.SEARCH_STRING,
    			catVec,
    			wildCardRight(Parameter.WILDCARD));
    	try {
	        jsonify(results, response);
        } catch (IOException e) {
        	logger.error("Exception creating JSON Object for " + results, e);
        	return mapping.findForward("error");
        }
    	return null;
    }
    
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    public ActionForward searchGenericName(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) {
    	logger.debug("Calling searchGenericName");
    	Parameter.setParameters( request.getParameterMap() );
    	
    	Vector catVec = new Vector();
    	catVec.add(RxDrugRef.CAT_AI_COMPOSITE_GENERIC);
    	Vector<Hashtable<String,Object>> results = drugref.list_search_element_select_categories(
    			Parameter.SEARCH_STRING,
    			catVec,
    			wildCardRight(Parameter.WILDCARD));
    	try {
	        jsonify(results, response);
        } catch (IOException e) {
        	logger.error("Exception creating JSON Object for " + results, e);
        	return mapping.findForward("error");
        }
    	return null;
    }
    
    @SuppressWarnings({ "unused", "unchecked", "rawtypes" })
    public ActionForward searchActiveIngredient(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)  {
    	logger.debug("Calling searchActiveIngredient");
    	Parameter.setParameters( request.getParameterMap() );

    	Vector catVec = new Vector();
    	catVec.add(RxDrugRef.CAT_ACTIVE_INGREDIENT);
    	Vector<Hashtable<String,Object>> results = drugref.list_search_element_select_categories(
    			Parameter.SEARCH_STRING, 
        		catVec, 
        		wildCardRight(Parameter.WILDCARD) );
    	try {
	        jsonify(results, response );
        } catch (IOException e) {
        	logger.error("Exception creating JSON Object for " + results, e);
        	return mapping.findForward("error");
        }
    	
    	return null;
    }
    
    @SuppressWarnings({ "unused", "unchecked", "rawtypes" })
    public ActionForward searchNaturalRemedy(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)  {
    	
    	return null;
    }
    

    @SuppressWarnings({ "unchecked", "unused" })
    public ActionForward jsonSearch(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}

        String searchStr = request.getParameter("query");
        if (searchStr == null){
            searchStr = request.getParameter("name");
        }
        String wildcardRightOnly = OscarProperties.getInstance().getProperty("rx.search_right_wildcard_only", "false");                      
        Vector<Hashtable<String,Object>> vec = null;
 
        try {
	        vec = drugref.list_drug_element3(searchStr, wildCardRight(wildcardRightOnly));        
	        jsonify(vec, response);
        } catch (IOException e) {
	       logger.error("Exception while attempting to contact DrugRef", e);
	       return mapping.findForward("error");
        } catch (Exception e) {
	    	logger.error("Unknown Error", e);
	    	return mapping.findForward("error");
        }

        return null;
    }

    /**
     * Utilty methods - should be split into a class if they get any bigger.
     */
    
    private static final boolean wildCardRight(final String wildcard) {   	
    	if(!StringUtils.isBlank(wildcard)) {
    		return Boolean.valueOf(wildcard);
    	}
    	return Boolean.FALSE;
    }
    
    private static void jsonify(final Vector<Hashtable<String,Object>> data, 
    		final HttpServletResponse response) throws IOException {
    	 
		Hashtable<String, Vector<Hashtable<String,Object>>> d = new Hashtable<String, Vector<Hashtable<String,Object>>>();
		d.put("results", data);
		response.setContentType("text/x-json");
		
		JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
		Writer jsonWriter = jsonArray.write( response.getWriter() );
		
		jsonWriter.flush();
		jsonWriter.close();
		         
    }
    
    private static class Parameter {
    	
    	//public static String DRUG_STATUS;
    	public static String WILDCARD;
    	public static String SEARCH_STRING;
    	
    	private static void reset() {
    		//DRUG_STATUS = ""; 
        	WILDCARD = "";
        	SEARCH_STRING = "";
    	}

    	public static void setParameters(Map<String, String[]> parameters) {
    		reset();
    		
//    		if(parameters.containsKey("drugStatus")) {
//    			Parameter.DRUG_STATUS = parameters.get("drugStatus")[0];
//    		}
    		
    		if(parameters.containsKey("wildcard")) {
    			Parameter.WILDCARD = parameters.get("wildcard")[0];
    		}
    		
    		if(parameters.containsKey("searchString")) {
    			Parameter.SEARCH_STRING = parameters.get("searchString")[0];
    		}
    		
    	}
    	 
    }


}
