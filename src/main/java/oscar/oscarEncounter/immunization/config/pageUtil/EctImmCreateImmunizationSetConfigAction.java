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


package oscar.oscarEncounter.immunization.config.pageUtil;

import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.immunization.config.data.EctImmHeading;
import oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetWriter;
import oscar.oscarEncounter.immunization.config.data.EctImmImmunizations;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class EctImmCreateImmunizationSetConfigAction extends Action {
   
   public String webizeNewLines(String str) {
      String temp = str.replaceAll("\n", "<br>");
      return temp;
   }
   
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
      EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
      String providerNo = bean.providerNo;
      Enumeration e = request.getParameterNames();
      Vector headingVec = new Vector();
      Vector immunizationVec = new Vector();
      Vector yearAgeVec = new Vector();
      String setName = ((EctImmCreateImmunizationSetConfigForm)form).getName();
      String headers = "true";
      String str;
      for(; e.hasMoreElements(); MiscUtils.getLogger().debug("str ".concat(String.valueOf(String.valueOf(str))))) {
         str = (String)e.nextElement();
         if(str.startsWith("heading")) {
            headingVec.add(str);
            continue;
         }
         if(str.startsWith("immunization")) {
            immunizationVec.add(str);
            continue;
         }
         boolean flag1;
         if(str.startsWith("yearAge"))
            yearAgeVec.add(str);
         else
            flag1 = false;
      }

      int flag = 0;
      Vector headingVector = new Vector();
      headingVector.setSize(headingVec.size());
      for(int u = 0; u < headingVec.size(); u++) {
         String headingString = (String)headingVec.elementAt(u);
         if(!headingString.equals(""))
            flag = 1;
         String gett = headingString.substring(9);
         StringTokenizer stringTokenizer = new StringTokenizer(gett, "D");
         String shouldBeCol = (String)stringTokenizer.nextElement();
         String headVal = request.getParameter(headingString);
         headVal = headVal.trim();
         headVal = webizeNewLines(headVal);
         EctImmHeading header = new EctImmHeading();
         header.headVal = headVal;
         int forTheCol = 0;
         try {
            forTheCol = Integer.parseInt(shouldBeCol);
         }
         catch(Exception colE) {
            MiscUtils.getLogger().debug("Kick'm out");
         }
         header.col = forTheCol;
         headingVector.setElementAt(header, header.col - 1);
      }
      
      Vector immunizationVector = new Vector();
      immunizationVector.setSize(immunizationVec.size());
      for(int i = 0; i < immunizationVec.size(); i++) {
         EctImmImmunizations immunization = new EctImmImmunizations();
         String colHead = (String)immunizationVec.elementAt(i);
         String immName = (String)immunizationVec.elementAt(i);
         String gett = immName.substring(12);
         StringTokenizer stringTokenizer = new StringTokenizer(gett, "D");
         String shouldBeRow = (String)stringTokenizer.nextElement();
         int forTheRow = 0;
         try {
            forTheRow = Integer.parseInt(shouldBeRow);
         }
         catch(Exception colE) {
            MiscUtils.getLogger().debug("Kick'm out");
         }
         String immVal = request.getParameter(immName);
         immVal = immVal.trim();
         immunization.immunizationName = immVal;
         immunization.row = forTheRow;
         immunization.indexAge = new Vector();
         immunizationVector.setElementAt(immunization, immunization.row - 1);
      }
      
      for(int q = 0; q < immunizationVector.size(); q++) {
         EctImmImmunizations immunization = (EctImmImmunizations)immunizationVector.elementAt(q);

      }
      
      Vector yearAgeVector = new Vector();
      yearAgeVector.setSize(yearAgeVec.size());
      for(int i = 0; i < yearAgeVec.size(); i++) {
         String yearAgeName = (String)yearAgeVec.elementAt(i);
         String subbedString = yearAgeName.substring(7);
         StringTokenizer stringTokenizer = new StringTokenizer(subbedString, "D");
         String rowToBeParsed = (String)stringTokenizer.nextElement();
         String colToBeParsed = (String)stringTokenizer.nextElement();
         int intRow = 0;
         int intCol = 0;
         try {
            intRow = Integer.parseInt(rowToBeParsed);
            intCol = Integer.parseInt(colToBeParsed);
         }
         catch(Exception rowAndCole) {
            MiscUtils.getLogger().debug("kickum out");
         }
         EctImmImmunizations immunizations = (EctImmImmunizations)immunizationVector.elementAt(intRow - 1);
         immunizations.indexAge.add(colToBeParsed);

      }
      
      EctImmImmunizationSetWriter immunizationSetWriter = new EctImmImmunizationSetWriter();
      immunizationSetWriter.doWrite(flag, headingVector, immunizationVector, yearAgeVec, setName, providerNo);
      request.setAttribute("cols", "2");
      request.setAttribute("rows", "2");
      return mapping.findForward("success");
   }
}
