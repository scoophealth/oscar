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
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.bean.*;

public class EctSelectMeasurementGroupAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        EctSelectMeasurementGroupForm frm = (EctSelectMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctSelectMeasurementGroupForm", frm);
        
        String groupName = frm.getSelectedGroupName();
        String forward = frm.getForward();
        
        System.out.println("The forward message is: " + forward);
        
        HttpSession session = request.getSession();
        session.setAttribute( "groupName", groupName);
        
        if(forward.compareTo("style")==0){
            //get the current style
            EctValidation ectValidation = new EctValidation();             
            EctStyleSheetBeanHandler sshd = new EctStyleSheetBeanHandler();
            Collection allStyleSheets = sshd.getStyleSheetNameVector();                            
            String css = ectValidation.getCssName(groupName);
            request.setAttribute("css", css);
            request.setAttribute( "allStyleSheets", allStyleSheets);
            request.setAttribute( "groupName", groupName);
            return mapping.findForward("style");
        }
        else if(forward.compareTo("delete")==0){
            deleteGroup(groupName);
            return mapping.findForward("delete");
        }
        else{
            return mapping.findForward("type");
        }

    }
    
    /*****************************************************************************************
     * delete the selected group
     *
     * @return 
     ******************************************************************************************/
    private void deleteGroup(String inputGroupName){
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "DELETE FROM measurementGroupStyle WHERE groupName='" + inputGroupName + "'";
            System.out.println("Sql Statement: " + sql);
            db.RunSQL(sql);            
            sql = "DELETE FROM measurementGroup WHERE name='" + inputGroupName + "'";
            db.RunSQL(sql);
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }        
    }
}
    
