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
import org.w3c.dom.NodeList;

import oscar.oscarEncounter.immunization.data.EctImmImmunizationData;
import oscar.util.UtilMisc;
import oscar.util.UtilXML;

public final class EctImmSaveScheduleAction extends Action {
   
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
	   
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
			throw new SecurityException("missing required security object (_demographic)");
		}
	   
      if(request.getParameter("hdnAction").equalsIgnoreCase("Configure"))
         return mapping.findForward("configure");
      
      if (request.getParameter("hdnAction").equalsIgnoreCase("ShowAll")){
         request.setAttribute("showDeleted","true"); 
         return mapping.findForward("reload");
      }
            
      try {
         String sDoc = UtilMisc.decode64(request.getParameter("xmlDoc"));
         Document doc = UtilXML.parseXML(sDoc);
         NodeList sets = doc.getElementsByTagName("immunizationSet");
         for(int i = 0; i < sets.getLength(); i++) {
            Element set = (Element)sets.item(i);
            NodeList rows = set.getElementsByTagName("row");
            for(int j = 0; j < rows.getLength(); j++) {
               Element row = (Element)rows.item(j);
               String sRow = String.valueOf(String.valueOf((new StringBuilder("tdSet")).append(i).append("_Row").append(j)));
               if(row.getAttribute("name").length() == 0 || row.getAttribute("name") == null)
                  row.setAttribute("name", request.getParameter(String.valueOf(String.valueOf(sRow)).concat("_name_text")));
               NodeList cells = row.getElementsByTagName("cell");
               for(int k = 0; k < cells.getLength(); k++) {
                  Element cell = (Element)cells.item(k);
                  String index = cell.getAttribute("index");
                  String sCell = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sRow)))).append("_Col").append(index)));
                  String givenDate = request.getParameter(String.valueOf(String.valueOf(sCell)).concat("_givenDate"));
                  String refusedDate = request.getParameter(String.valueOf(String.valueOf(sCell)).concat("_refusedDate"));
                  String lot = request.getParameter(String.valueOf(String.valueOf(sCell)).concat("_lot"));
                  String provider = request.getParameter(String.valueOf(String.valueOf(sCell)).concat("_provider"));
                  String comments = request.getParameter(String.valueOf(String.valueOf(sCell)).concat("_comments"));
                  cell.setAttribute("givenDate", givenDate);
                  cell.setAttribute("refusedDate", refusedDate);
                  cell.setAttribute("lot", lot);
                  cell.setAttribute("provider", provider);
                  cell.setAttribute("comments", comments);
               }
               
               String comments = request.getParameter(String.valueOf(String.valueOf(sRow)).concat("_comments_text"));
               NodeList cmnts = row.getElementsByTagName("comments");
               if(cmnts.getLength() > 0)
                  UtilXML.setText(cmnts.item(0), comments);
               else
                  UtilXML.addNode(row, "comments", comments);
            }
            
         }
         
         String sXML = UtilXML.toXML(doc);
         EctImmImmunizationData imm = new EctImmImmunizationData();
         String demographicNo = request.getParameter("demographic_no");
         String providerNo = (String)request.getSession().getAttribute("user");
         imm.saveImmunizations(demographicNo, providerNo, sXML);
      }
      catch(Exception ex) {MiscUtils.getLogger().error("Error", ex);
         throw new ServletException("Exception occurred in SaveScheduleAction", ex);
      }
      
      return mapping.findForward("reload");
   }
}
