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


package oscar.oscarEncounter.immunization.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import oscar.oscarEncounter.immunization.data.EctImmImmunizationData;
import oscar.util.UtilXML;

/**
 *
 * @author Jay Gallagher
 */
public class EctImmDeleteImmScheduleAction extends Action {
   
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   /**
   
    Creates a new instance of EctImmDeleteImmScheduleAction
    */
   public EctImmDeleteImmScheduleAction() {
   }
   
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
      
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
			throw new SecurityException("missing required security object (_demographic)");
		}
	   
      EctImmImmunizationData immData = new EctImmImmunizationData();

      String demographicNo = request.getParameter("demoNo");
      String providerNo = (String) request.getSession().getAttribute("user");
      String method = request.getParameter("method");
      String id = request.getParameter("tblSet");
      int setnum = Integer.parseInt(id);
      
           
      try {
                  
         String imm = immData.getImmunizations(demographicNo);           
         ///String sDoc = UtilMisc.decode64(imm);
         Document doc = UtilXML.parseXML(imm);
         NodeList sets = doc.getElementsByTagName("immunizationSet");
         Element set = (Element) sets.item(setnum);

         if (method.equals("delete")){
            set.setAttribute("status", "deleted");
         }else{
            set.removeAttribute("status");
         }
         
         
         
         String sXML = UtilXML.toXML(doc);

         //EctImmImmunizationData imm = new EctImmImmunizationData();
         immData.saveImmunizations(demographicNo, providerNo, sXML);
      }
      catch(Exception ex) {
         throw new ServletException("Exception occurred in EctImmDeleteImmScheduleAction", ex);
      }           
           
      return mapping.findForward("success");
   }
   
   
}
