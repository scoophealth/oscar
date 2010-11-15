/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.web;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author jaygallagher
 */
public class SearchDemographicAutoCompleteAction extends Action {
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao"); 
        String searchStr = request.getParameter("demographicKeyword");
        
        if (searchStr == null){
           searchStr = request.getParameter("query");
        }
        
        if (searchStr == null){
           searchStr = request.getParameter("name");
        }
        
        List<Demographic> list = demographicDao.searchDemographic(searchStr);
        List secondList= new ArrayList();
        for(Demographic demo :list){
            Hashtable h = new Hashtable();
             h.put("fomattedDob",demo.getFormattedDob());
             h.put("formattedName",demo.getFormattedName());
             h.put("demographicNo",demo.getDemographicNo());
             secondList.add(h);
        }

        Hashtable d = new Hashtable();
        d.put("results",secondList);
        response.setContentType("text/x-json");
        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;

    }

}
