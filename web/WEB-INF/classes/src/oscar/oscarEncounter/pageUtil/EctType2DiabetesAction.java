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
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarEncounter.data.EctType2DiabetesRecord;

public final class EctType2DiabetesAction extends Action {
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException   {
        int newID = 0;
        try {
            EctType2DiabetesRecord rec = new EctType2DiabetesRecord();
            Properties props = new Properties();
            String name;
            for(Enumeration enum = request.getParameterNames(); enum.hasMoreElements(); props.setProperty(name, request.getParameter(name)))
                name = (String)enum.nextElement();

            newID = rec.saveType2DiabetesRecord(props);
        } catch(SQLException ex) {
            throw new ServletException(ex);
        }
        String where = "";
        if(request.getParameter("submit").equalsIgnoreCase("print")) {
            ActionForward af = mapping.findForward("print");
            where = af.getPath();
            where = where+"?demoNo="+request.getParameter("demographic_no")+"&formId="+newID;
        } else if(request.getParameter("submit").equalsIgnoreCase("save")) {
            ActionForward af = mapping.findForward("save");
            where = af.getPath();
            where = where+"?demographic_no="+request.getParameter("demographic_no")+"&formId="+newID;
        } else if(request.getParameter("submit").equalsIgnoreCase("exit")) {
            ActionForward af = mapping.findForward("exit");
            where = af.getPath();
        } else {
            ActionForward af = mapping.findForward("failure");
            where = af.getPath();
        }
        return new ActionForward(where);
    }
}
