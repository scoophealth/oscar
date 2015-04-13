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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author mweston4
 */
public class BillingONReviewAction extends DispatchAction {
    
    private ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean("clinicDAO");
    private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    
    public ActionForward getDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String demographicNo = request.getParameter("demographicNo");  
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }

        Demographic demographic = demographicDao.getDemographic(demographicNo);
        
        
        JsonConfig config = new JsonConfig();
	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());        
        
        JSONObject json = JSONObject.fromObject(demographic,config);
        response.getOutputStream().write(json.toString().getBytes());
        return null;
    }
    public ActionForward getClinic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {   
        Clinic clinic = clinicDao.getClinic();
        JSONObject json = JSONObject.fromObject(clinic);
        response.getOutputStream().write(json.toString().getBytes());
        return null;
    }
}
