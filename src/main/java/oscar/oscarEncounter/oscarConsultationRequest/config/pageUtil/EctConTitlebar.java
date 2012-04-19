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


package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class EctConTitlebar
{

    public EctConTitlebar()
    {
    	ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources");
    	
        jspVect = new Vector();
        displayNameVect = new Vector();
        jspVect.add("AddSpecialist.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnAddSpecialist"));
        jspVect.add("AddService.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnAddService"));
        jspVect.add("EditSpecialists.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnEditSpecialists"));
        jspVect.add("ShowAllServices.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnShowAllServices"));
        jspVect.add("DeleteServices.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnDeleteServices"));
    }

    public EctConTitlebar(HttpServletRequest request)
    {
    	ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources",request.getLocale());
    	
        jspVect = new Vector();
        displayNameVect = new Vector();
        jspVect.add("AddSpecialist.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnAddSpecialist"));
        jspVect.add("AddService.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnAddService"));
        jspVect.add("EditSpecialists.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnEditSpecialists"));
        jspVect.add("ShowAllServices.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnShowAllServices"));
        jspVect.add("DeleteServices.jsp");
        displayNameVect.add(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.btnDeleteServices"));
    }


    public String estBar(HttpServletRequest request)
    {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("<table bgcolor=\"#ffffff\" cellspacing=\"2\">\n");
        strBuf.append("   <tr>\n");
        String uri = request.getRequestURI();
        int ind = uri.lastIndexOf("/");
        uri = uri.substring(ind + 1);

        for(int i = 0; i < jspVect.size(); i++){
            if(uri.equals(jspVect.elementAt(i)) && request.getAttribute("upd") == null)
            {
                strBuf.append("      <td bgcolor=\"#ccccff\">\n");
                strBuf.append("         <a href="+(String)jspVect.elementAt(i)+" class=\"consultButtonsDormant\">"+(String)displayNameVect.elementAt(i)+"</a>\n" );
                strBuf.append("      </td>\n");
            } else
            {
                strBuf.append("      <td bgcolor=\"#9999ff\">\n");
                strBuf.append(String.valueOf(String.valueOf((new StringBuilder("         <a href=")).append((String)jspVect.elementAt(i)).append(" class=\"consultButtonsActive\">").append((String)displayNameVect.elementAt(i)).append("</a>\n"))));
                strBuf.append("      </td>\n");
            }
            strBuf.append("   </tr>\n");
        }
        strBuf.append("</table>\n");
        return strBuf.toString();
    }

    Vector jspVect;
    Vector displayNameVect;
}
