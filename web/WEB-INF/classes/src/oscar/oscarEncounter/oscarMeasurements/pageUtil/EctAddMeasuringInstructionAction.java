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


public class EctAddMeasuringInstructionAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctAddMeasuringInstructionForm frm = (EctAddMeasuringInstructionForm) form;

        HttpSession session = request.getSession();
        request.getSession().setAttribute("EctAddMeasuringInstructionForm", frm);
        
        MsgStringQuote str = new MsgStringQuote();
        String requestId = "";
        List messages = new LinkedList();
        
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String typeDisplayName = frm.getTypeDisplayName();
            String measuringInstrc = frm.getMeasuringInstrc();
            String validation = frm.getValidation();
            boolean isValid = true;
            
            ActionMessages errors = new ActionMessages();  
            EctValidation validate = new EctValidation();
            String regExp = validate.getRegCharacterExp();
            String errorField = "The measuring instruction " + measuringInstrc;
            if(!validate.matchRegExp(regExp, measuringInstrc)){
                errors.add(measuringInstrc,
                new ActionMessage("errors.invalid", errorField));
                saveErrors(request, errors);
                isValid = false;                
            }
            if(!validate.maxLength(255, measuringInstrc)){
                errors.add(measuringInstrc,
                new ActionMessage("errors.maxlength", errorField, "255"));
                saveErrors(request, errors);
                isValid = false;
            } 
            if(!isValid)
                return (new ActionForward(mapping.getInput()));
            
            String sql = "SELECT measuringInstruction FROM measurementType WHERE measuringInstruction='" + str.q(measuringInstrc) +"' AND typeDisplayName='" + str.q(typeDisplayName) + "'";
            ResultSet rs = db.GetSQL(sql);
            rs.next();
            
            if(rs.getRow()>0){
                errors.add(measuringInstrc,
                new ActionMessage("error.oscarEncounter.Measurements.duplicateTypeName"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));                
            }
            
            rs.close();
            sql = "SELECT * FROM measurementType WHERE typeDisplayName='" + str.q(typeDisplayName) +"'";
            rs = db.GetSQL(sql);
            rs.next();
            
            String type = db.getString(rs,"type");
            String typeDesc = db.getString(rs,"typeDescription");            
            
            //Write to database
            sql = "INSERT INTO measurementType"
                +"(type, typeDisplayName, typeDescription, measuringInstruction, validation)"
                +" VALUES ('"+str.q(type)+"','"+str.q(typeDisplayName)+"','"+str.q(typeDesc)+"','"+str.q(measuringInstrc)+"','"
                + str.q(validation)+"')";
            db.RunSQL(sql);


            /* select the correct db specific command */
            String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
            String dbSpecificCommand;
            if (db_type.equalsIgnoreCase("mysql")) {
                dbSpecificCommand = "SELECT LAST_INSERT_ID()";
            } 
            else if (db_type.equalsIgnoreCase("postgresql")){
                dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
            }
            else
                throw new SQLException("ERROR: Database " + db_type + " unrecognized.");

            rs.close();
            rs = db.GetSQL(dbSpecificCommand);
            if(rs.next())
                requestId = Integer.toString(rs.getInt(1));
            db.CloseConn();
                
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }            
        
        MessageResources mr = getResources(request);
        String msg = mr.getMessage("oscarEncounter.oscarMeasurements.AddMeasuringInstruction.successful", "!");
        //String msg = "Measuring Instruction has been added successfully!";
        messages.add(msg);
        request.setAttribute("messages", messages);                
        return mapping.findForward("success");

    }
    
    

}
    
