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
//import org.apache.regexp.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;


public class EctDefineNewMeasurementGroupAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        EctDefineNewMeasurementGroupForm frm = (EctDefineNewMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctDefineNewMeasurementGroupForm", frm);
        
        String groupName = (String) frm.getGroupName();
        String styleSheet = (String) frm.getStyleSheet();
        
        ActionMessages errors = new ActionMessages();  
        EctValidation validate = new EctValidation();
        String regExp = validate.getRegCharacterExp();
        
        if(!validate.matchRegExp(regExp, groupName)){
            errors.add(groupName,
            new ActionMessage("errors.invalid", groupName));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        //Write the new groupName to the database if there's no duplication
        if(!write2Database(groupName, styleSheet)){
            errors.add(groupName,
            new ActionMessage("error.oscarEncounter.addNewMeasurementGroup.duplicateGroupName", groupName));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
                             
        HttpSession session = request.getSession();
        session.setAttribute( "groupName", groupName);
        
        return mapping.findForward("continue");

    }
    
    /*****************************************************************************************
     * Write the new groupName to the database if there's no duplication
     *
     * @return boolean
     ******************************************************************************************/
    private boolean write2Database(String inputGroupName, String styleSheet){
        boolean isWrite2Database = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT groupName from measurementGroupStyle ORDER BY groupName";
            System.out.println("Sql Statement: " + sql);
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); )
            {
                String groupName = db.getString(rs,"groupName");
                if (inputGroupName.compareTo(groupName)==0){
                    isWrite2Database = false;
                    break;
                }
            }
            rs.close();
            
            if (isWrite2Database){
                sql = "INSERT INTO measurementGroupStyle(groupName, cssID) VALUES ('" + inputGroupName + "','" + styleSheet + "')";
                db.RunSQL(sql);
            }
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }
        return isWrite2Database;
    }
}
    
