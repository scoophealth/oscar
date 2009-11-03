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

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;


public final class EctInsertTemplateAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        String templateName = (String) request.getParameter("templateName");
            
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT encountertemplate_value FROM encountertemplate WHERE encountertemplate_name='" + templateName + "'";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()){
                String encounterTmpValue = db.getString(rs,"encountertemplate_value");                   
                encounterTmpValue = encounterTmpValue.replaceAll("\\\\", "\\\\u005C"); // replace \ with unicode equiv.
                encounterTmpValue = encounterTmpValue.replaceAll("\"", "\\\\u0022"); // replace " with unicode equiv.
                encounterTmpValue = encounterTmpValue.replaceAll("'", "\\\\u0027"); // replace ' with unicode equiv.
                encounterTmpValue = encounterTmpValue.replaceAll(">", "\\\\u003E");
                encounterTmpValue = encounterTmpValue.replaceAll("<", "\\\\u003C");
                encounterTmpValue = encounterTmpValue.replaceAll("\n", "\\\\u000A");
                encounterTmpValue = encounterTmpValue.replaceAll("\r", "\\\\u000D");
                request.setAttribute("templateValue", encounterTmpValue);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }            
        
        String version = (String)request.getParameter("version");
        if( version != null && version.equals("2") )
            return (mapping.findForward("success2"));
        else
            return (mapping.findForward("success"));
        
    }
}
