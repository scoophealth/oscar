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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarResearch.oscarDxResearch.pageUtil;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;


public class dxResearchUpdateAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String status = request.getParameter("status");
        String did = request.getParameter("did");
        String demographicNo = request.getParameter("demographicNo");        
        String providerNo = request.getParameter("providerNo");
        String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"); 
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            String sql = "";
            if (status.equals("C")){
                sql = "update dxresearch set update_date='"+nowDate + "', status='C' where dxresearch_no='"+did+"'";
                db.RunSQL(sql);
            }
            else if (status.equals("D")){
                sql = "update dxresearch set update_date='"+nowDate + "', status='D' where dxresearch_no='"+did+"'";
                db.RunSQL(sql);
            }
        }

        catch(SQLException e){
            System.out.println(e.getMessage());
        }                                    
        
        ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
        forward.addParameter("demographicNo", demographicNo);
        forward.addParameter("providerNo", providerNo);
        forward.addParameter("quickList", "");        
        
        return forward;
    }
     
}
