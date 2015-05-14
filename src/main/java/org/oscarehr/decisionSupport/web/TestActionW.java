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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.util.LoggedInInfo;

/**
 *
 * @author apavel
 */
public class TestActionW extends Action {
     private DSService dsService;
    
    public TestActionW() {
    }
    
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
        String demographic_no = request.getParameter("demographic_no");
        if (demographic_no == null) demographic_no = "1";
        response.getWriter().println(dsService.evaluateAndGetConsequences(loggedInInfo, demographic_no, (String) request.getSession().getAttribute("user")));
        return null;
    }

    public void setDsService(DSService dsService) {
        this.dsService = dsService;
    }
}
