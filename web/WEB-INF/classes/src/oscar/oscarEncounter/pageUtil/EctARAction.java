// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarEncounter.data.EctARRecord;

public final class EctARAction extends Action {

    public ActionForward execute(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse httpservletresponse)
        throws IOException, ServletException    {
        int newID = 0;
        try   {
            EctARRecord arrecord = new EctARRecord();
            Properties properties = new Properties();
            String formId = "0";

            Enumeration enum = request.getParameterNames();
            while (enum.hasMoreElements()) {
                String name = (String)enum.nextElement();
                if(name.equalsIgnoreCase("formId")) {
                    formId = request.getParameter(name);
                }
                properties.setProperty(name, request.getParameter(name));
            }
            newID = arrecord.saveARRecord(properties, formId);
        } catch(SQLException sqlexception) {
            throw new ServletException(sqlexception);
        }
        String s = ""; //s=where
        if(request.getParameter("submit").equalsIgnoreCase("printAR1")) {
            ActionForward actionforward = actionmapping.findForward("printAR1");
            s = actionforward.getPath();
            s = s + "?demoNo=" + request.getParameter("demographic_no") + "&formId=" +newID+ "&provNo=" + request.getParameter("provNo");
        } else if(request.getParameter("submit").equalsIgnoreCase("printAR2")) {
            ActionForward actionforward1 = actionmapping.findForward("printAR2");
            s = actionforward1.getPath();
            s = s + "?demoNo=" + request.getParameter("demographic_no") + "&formId=" +newID+ "&provNo=" + request.getParameter("provNo");
        } else if(request.getParameter("submit").equalsIgnoreCase("save")) {
            ActionForward actionforward2 = actionmapping.findForward("save");
            s = actionforward2.getPath();
            s = s + "?demographic_no=" + request.getParameter("demographic_no") + "&formId=" +newID+ "&provNo=" + request.getParameter("provNo");
        } else if(request.getParameter("submit").equalsIgnoreCase("exit")) {
            ActionForward actionforward3 = actionmapping.findForward("exit");
            s = actionforward3.getPath();
        } else {
            ActionForward actionforward4 = actionmapping.findForward("failure");
            s = actionforward4.getPath();
        }
        return new ActionForward(s);
    }
}
