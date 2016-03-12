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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.form.pharmaForms.formBPMH.util.JsonUtil;
import oscar.oscarRx.data.RxAllergyData;
import oscar.oscarRx.util.RxDrugRef;

public final class RxSearchAllergyAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);


    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_allergy", "r", null)) {
			throw new RuntimeException("missing required security object (_allergy)");
		}


        // Setup variables

        // execute search

        RxSearchAllergyForm frm = (RxSearchAllergyForm)form;
        
        // JSON overrides the form.
 		String jsondata = request.getParameter("jsonData");
 		if( jsondata != null ) {			
 			frm = (RxSearchAllergyForm) JsonUtil.jsonToPojo(jsondata, RxSearchAllergyForm.class);
 		}
 		
        RxAllergyData aData = new RxAllergyData();
        //RxAllergyData.Allergy[] arr =
        //    aData.AllergySearch(frm.getSearchString(), frm.getType5(),
        //    frm.getType4(), frm.getType3(), frm.getType2(), frm.getType1());


        ///Search a drug like another one

        RxDrugRef drugRef = new RxDrugRef();

        java.util.Vector vec    = new java.util.Vector();
        java.util.Vector catVec = new java.util.Vector();

        /**
         * <html:checkbox property="type4" /> Drug Classes   9 |  4 | ther_class
         * <html:checkbox property="type3" /> Ingredients   13 |  6 | ingredient
         * <html:checkbox property="type2" /> Generic Names  6 |  1 | generic
         * <html:checkbox property="type1" /> Brand Names    8 |  3 | brandname
         *
         *|  8 | anatomical class
         *|  9 | chemical class
         *| 10 | therapeutic class
         *| 11 | generic
         *| 12 | composite generic
         *| 13 | branded product
         *| 14 | ingredient

         *
         *
         **/
        if(frm.getType1()){
            catVec.add("13");
        }

        if(frm.getType2()){
            catVec.add("11");
            catVec.add("12");
        }

        if(frm.getType3()){
            catVec.add("14");
        }

        if(frm.getType4()){
            catVec.add("8");
            catVec.add("10");

        }

        boolean itemsFound = true;

        String wildcardRightOnly = OscarProperties.getInstance().getProperty("allergies.search_right_wildcard_only", "false");
        vec  = drugRef.list_search_element_select_categories(frm.getSearchString(),catVec,Boolean.valueOf(wildcardRightOnly));

        //  'id':'0','category':'','name'
        Allergy[] arr = new Allergy[vec==null?0:vec.size()];

        String includeClassesStr = OscarProperties.getInstance().getProperty("allergies.include_ahfs_class_in_results", "true");
        boolean includeClasses = Boolean.valueOf(includeClassesStr);

        boolean flatResults = Boolean.valueOf(OscarProperties.getInstance().getProperty("allergies.flat_results", "false"));
        TreeMap<String,Allergy> flatList = new TreeMap<String,Allergy>();

        //we want to categorize the search results.
        Map<Integer,List<Allergy>> allergyResults = new HashMap<Integer,List<Allergy>>();
        allergyResults.put(8, new ArrayList<Allergy>());
        allergyResults.put(10, new ArrayList<Allergy>());
        allergyResults.put(11, new ArrayList<Allergy>());
        allergyResults.put(12, new ArrayList<Allergy>());
        allergyResults.put(13, new ArrayList<Allergy>());
        allergyResults.put(14, new ArrayList<Allergy>());

        Map<Integer,Allergy> classResults = new HashMap<Integer,Allergy>();

        Vector classVec = new Vector() ;
        for (int i = 0; i < vec.size(); i++){
        	java.util.Hashtable hash = (java.util.Hashtable) vec.get(i);
            if (!hash.get("name").equals("None found")){
                arr[i] = new Allergy();

                arr[i].setTypeCode(((Integer) hash.get("category")).intValue());
                arr[i].setDrugrefId( String.valueOf(hash.get("id")));
                arr[i].setDescription(((String) hash.get("name")));

                if( arr[i].getTypeCode() == 13){
                    classVec.add(""+arr[i].getDrugrefId());
                }

                allergyResults.get(hash.get("category")).add(arr[i]);
                if(flatList.get(arr[i].getDescription()) == null) {
                	flatList.put(arr[i].getDescription(),arr[i]);
                }
            }else{
                MiscUtils.getLogger().debug("IM FLAGGING IT AS NOT FOUND");
                itemsFound = false;
                arr= null;
            }

        }

        Hashtable returnHash = new Hashtable();

        if (itemsFound && includeClasses){

            if (classVec.size() > 0){
                Vector classVec2 = drugRef.list_drug_class(classVec);


                Hashtable hash ;
                String compString = null;
                Vector strVec = null;

                if(classVec2!=null) {
                	for(int j = 0; j < classVec2.size(); j++){

                    MiscUtils.getLogger().debug("LOOPING");
                    hash = (java.util.Hashtable) classVec2.get(j);
                    //'id_drug'     'id_class'      'name'
                    String idDrug  = String.valueOf(hash.get("id_drug"));
                    String idClass = String.valueOf(hash.get("id_class"));
                    String name    = String.valueOf(hash.get("name"));
                    String[] strArr = new String[2];
                    strArr[0] = idClass;
                    strArr[1] = name;
                    MiscUtils.getLogger().debug(j+" idDrug "+idDrug+" idClass "+idClass+" name "+name);
                    if(returnHash.containsKey(idDrug)){
                        strVec = (Vector) returnHash.get(idDrug);
                        strVec.add(strArr);
                        returnHash.put(idDrug,strVec);
                    }else{
                        strVec = new Vector();
                        strVec.add(strArr);
                        returnHash.put(idDrug,strVec);
                    }
                }
                }
            }
          	MiscUtils.getLogger().debug("Sending this many vector of this size back "+returnHash.values().size());

        }

        if(arr != null && arr.length>0) {
        	request.setAttribute("allergyResults", allergyResults);
            request.setAttribute("allergies", arr);
            request.setAttribute("drugClasses", returnHash);
            request.setAttribute("flatMap", flatList);
        }

        return (mapping.findForward("success"));
    }
}
