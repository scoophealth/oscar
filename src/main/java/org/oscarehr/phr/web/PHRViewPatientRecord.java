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

package org.oscarehr.phr.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.PHRVerificationDao;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.UtilDateUtilities;
import oscar.log.LogAction;
import oscar.log.LogConst;

/**
 *
 * @author apavel
 */
public class PHRViewPatientRecord extends DispatchAction {
    private static final Logger log2 = MiscUtils.getLogger();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           return viewMyOscarRecord(mapping,form,request,response);
    }
    

    
    public ActionForward viewMyOscarRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String demographicNo = request.getParameter("demographic_no");
        if (demographicNo == null) { //shouldn't really happy, but just in case
            response.getWriter().println("Error: Lost demographic number.  Please try again");
            return null;
        }
        PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        
        if (auth == null || auth.getMyOscarUserId() == null) {
            request.setAttribute("forwardToOnSuccess", request.getContextPath() + "/demographic/viewPhrRecord.do?demographic_no=" + demographicNo);
            return mapping.findForward("login");
        } else {
            String phrPath = OscarProperties.getInstance().getProperty("myOSCAR.url"); //http://130.113.106.96:8080/myoscar_client/
            if (phrPath == null) {
                response.getWriter().println("Error: 'myOSCAR.url' property is not configured... please contact support to have this feature enabled");
                return null;
            }
            DemographicData demographicData = new DemographicData();
            Long myOscarUserId = MyOscarUtils.getMyOscarUserId(auth, demographicData.getDemographic(demographicNo).getMyOscarUserName());

            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_PHR, String.valueOf(myOscarUserId), request.getRemoteAddr(), demographicNo, "");
//            request.setAttribute("userid", auth.getUserId());
//            request.setAttribute("ticket", auth.getToken());
            request.setAttribute("userName", auth.getMyOscarUserName());
            request.setAttribute("password", auth.getMyOscarPassword());
            request.setAttribute("viewpatient", myOscarUserId);
            request.setAttribute("url", phrPath + (phrPath.endsWith("/")?"":"/") + "patient_view_action.jsp");
            return mapping.findForward("phr");
        }

    }
    
    
    public ActionForward saveNewVerification(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String demographicNo = request.getParameter("demographic_no");
        PHRVerificationDao phrVerificationDao = (PHRVerificationDao)SpringUtils.getBean("PHRVerificationDao");
        
        org.oscarehr.common.model.PHRVerification phrA = new org.oscarehr.common.model.PHRVerification();
        
        boolean photoId = false;
        try{
        	photoId = Boolean.parseBoolean(request.getParameter("photoId"));
        }catch(Exception e){/*anything but true should be considered false*/}
        
        boolean parentGuardian = false;
        try{
        	parentGuardian = Boolean.parseBoolean(request.getParameter("parentGuardian"));
        }catch(Exception e2){/*Anything but true should be considered false*/}
        
        
        
        Date verificationDate = null;
        try{
        	verificationDate = UtilDateUtilities.getDateFromString(request.getParameter("verificationDate"),"yyyy-MM-dd");
        }catch(Exception e){
        	verificationDate = new Date();
        }
        
        DemographicData demographicData = new DemographicData();
        phrA.setPhrUserName(demographicData.getDemographic(demographicNo).getMyOscarUserName());
        phrA.setVerificationLevel(request.getParameter("verificationLevel"));
        phrA.setVerificationDate(verificationDate);// fix me
        phrA.setPhotoId(photoId);
        phrA.setParentGuardian(parentGuardian);
        phrA.setComments(request.getParameter("comments"));
        
        phrA.setDemographicNo(Integer.parseInt(demographicNo));
        phrA.setVerificationBy((String) request.getSession().getAttribute("user"));
        phrA.setArchived(false);
        phrA.setCreatedDate(new java.util.Date());
        phrVerificationDao.persist(phrA);
        
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.VERIFY, LogConst.CON_PHR, phrA.getPhrUserName(), request.getRemoteAddr(), demographicNo, "");
        
        ActionRedirect ar = new ActionRedirect(mapping.findForward("viewVerification"));
        ar.addParameter("demographic_no", demographicNo);
        return ar;
    }
    
}
