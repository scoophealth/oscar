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


public class EctAddMeasurementTypeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctAddMeasurementTypeForm frm = (EctAddMeasurementTypeForm) form;

        HttpSession session = request.getSession();
        request.getSession().setAttribute("EctAddMeasurementTypeForm", frm);
        
        MsgStringQuote str = new MsgStringQuote();     
        List messages = new LinkedList();
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String type = (String) frm.get("type");
            String typeUp = type.toUpperCase();
            String typeDesc = (String) frm.get("typeDesc");
            String typeDisplayName = (String) frm.get("typeDisplayName");
            String measuringInstrc = (String) frm.get("measuringInstrc");
            String validation = (String) frm.get("validation");

            String sql = "SELECT type FROM measurementType WHERE type='" + str.q(type) +"'";
            ResultSet rs = db.GetSQL(sql);
            rs.next();
            
            if(rs.getRow()>0){
                System.out.println("The specified type already exists");
                MessageResources mr = getResources(request);
                String msg = mr.getMessage("oscarEncounter.oscarMeasurements.AddMeasurementType.duplicateType");
                messages.add(msg);
                request.setAttribute("messages", messages);
                return mapping.findForward("failure");
            }
            
            //Write to database
            sql = "INSERT INTO measurementType"
                +"(type, typeDisplayName, typeDescription, measuringInstruction, validation)"
                +" VALUES ('"+str.q(typeUp)+"','"+str.q(typeDesc)+"','"+str.q(typeDisplayName)+"','"+str.q(measuringInstrc)+"','"
                + str.q(validation)+"')";
            System.out.println(" sql statement "+sql);
            db.RunSQL(sql);

            db.CloseConn();
                
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }            
        
        MessageResources mr = getResources(request);
        String msg = mr.getMessage("oscarEncounter.oscarMeasurements.AddMeasurementType.successful");
        messages.add(msg);
        request.setAttribute("messages", messages);
        return mapping.findForward("success");

    }
}
    
