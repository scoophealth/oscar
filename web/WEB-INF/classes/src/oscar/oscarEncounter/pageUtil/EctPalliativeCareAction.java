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

import oscar.oscarEncounter.data.EctPalliativeCareRecord;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;
import java.util.*;
import java.io.*;



public final class EctPalliativeCareAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

        int newID=0;

        try
        {
            EctPalliativeCareRecord rec = new EctPalliativeCareRecord();

            Properties props = new Properties();

            Enumeration enum = request.getParameterNames();
            while (enum.hasMoreElements())
            {
                String name = (String)enum.nextElement();
                props.setProperty(name, request.getParameter(name));
            }

            newID = rec.savePalliativeCareRecord(props);
        } catch (java.sql.SQLException ex) {
            throw new ServletException(ex);
        }

        String where="";
        if(request.getParameter("submit").equalsIgnoreCase("print"))
        {
            ActionForward af = mapping.findForward("print");
            where = af.getPath();
            where = where + "?demoNo=" + request.getParameter("demographic_no")+"&formId=" + newID + "&provNo=" + request.getParameter("provNo");
        }
        else if(request.getParameter("submit").equalsIgnoreCase("save"))
        {
            ActionForward af = mapping.findForward("save");
            where = af.getPath();
            where = where + "?demographic_no=" + request.getParameter("demographic_no")+"&formId=" + newID + "&provNo=" + request.getParameter("provNo");
        }
        else if(request.getParameter("submit").equalsIgnoreCase("exit"))
        {
            ActionForward af = mapping.findForward("exit");
            where = af.getPath();
        }
        else
        {
            ActionForward af = mapping.findForward("failure");
            where = af.getPath();
        }
        return (new ActionForward(where));
    }
}
