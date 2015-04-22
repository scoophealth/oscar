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
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetData;
import oscar.oscarEncounter.immunization.data.EctImmImmunizationData;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.util.UtilXML;

public final class EctImmSaveConfigAction extends Action {
   
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {

	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
			throw new SecurityException("missing required security object (_demographic)");
		}
	   
      EctSessionBean bean = null;
      bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
      try {
         //String sCfgDoc = UtilMisc.decode64(request.getParameter("xmlDoc"));
         //Document cfgDoc = UtilXML.parseXML(sCfgDoc);
         //NodeList cfgSets = cfgDoc.getElementsByTagName("immunizationSet");
         
         EctImmImmunizationData imm = new EctImmImmunizationData();
         String sDoc = imm.getImmunizations(bean.demographicNo);
         Document doc;
         Element root;
         try {
            doc = UtilXML.parseXML(sDoc);
            root = doc.getDocumentElement();
         } catch(Exception ex) {
            doc = UtilXML.newDocument();
            root = UtilXML.addNode(doc, "immunizations");
         }
         //add new src
         String[] setId = request.getParameterValues("chkSet");
         if (setId == null) return mapping.findForward("success");
         
         for (int i=0; i<setId.length; i++) {            
            String sCfgDoc = (new EctImmImmunizationSetData()).getSetXMLDoc(setId[i]);            
            Document cfgDoc = UtilXML.parseXML(sCfgDoc);            
            NodeList cfgSets = cfgDoc.getElementsByTagName("immunizationSet");            
            Element cfgSet = (Element)cfgSets.item(0);            
            Node nod = doc.importNode(cfgSet, true);            
            root.appendChild(nod);                         
         }
         
/*
            for(int i = 0; i < cfgSets.getLength(); i++) {
                if(request.getParameter("chkSet".concat(String.valueOf(String.valueOf(i)))) != null && request.getParameter("chkSet".concat(String.valueOf(String.valueOf(i)))).equalsIgnoreCase("on"))
                {
                    Element cfgSet = (Element)cfgSets.item(i);
                    Node nod = doc.importNode(cfgSet, true);
                    root.appendChild(nod);
                }
            }
 */
         if (doc != null) {
            String sXML = UtilXML.toXML(doc);
            imm.saveImmunizations(bean.demographicNo, bean.providerNo, sXML);
         }

      } catch(Exception ex)  {MiscUtils.getLogger().error("Error", ex);
         throw new ServletException("Exception occurred in SaveConfigAction", ex);
      }
      return mapping.findForward("success");
   }
}
