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


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementTypes;
import oscar.oscarMessenger.util.MsgStringQuote;

public class EctAddMeasurementTypeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctAddMeasurementTypeForm frm = (EctAddMeasurementTypeForm) form;

       
        request.getSession().setAttribute("EctAddMeasurementTypeForm", frm);
        
        MsgStringQuote str = new MsgStringQuote();     
        List messages = new LinkedList();
        
        try{
            

            String type = frm.getType();
            String typeUp = type.toUpperCase();
            String typeDesc = frm.getTypeDesc();
            String typeDisplayName = frm.getTypeDisplayName();
            String measuringInstrc = frm.getMeasuringInstrc();
            String validation = frm.getValidation();
            if (!allInputIsValid(request, type, typeDesc, typeDisplayName, measuringInstrc)){
                return (new ActionForward(mapping.getInput()));
            }

            //Write to database
            String sql = "INSERT INTO measurementType"
                +"(type, typeDescription, typeDisplayName, measuringInstruction, validation)"
                +" VALUES ('"+str.q(typeUp)+"','"+str.q(typeDesc)+"','"+str.q(typeDisplayName)+"','"+str.q(measuringInstrc)+"','"
                + str.q(validation)+"')";
            MiscUtils.getLogger().debug(" sql statement "+sql);
            DBHandler.RunSQL(sql);
                
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }            
        
        MessageResources mr = getResources(request);
        String msg = mr.getMessage("oscarEncounter.oscarMeasurements.AddMeasurementType.successful", "!");
        //String msg = "Measurement Type has been added successfully!";
        messages.add(msg);
        request.setAttribute("messages", messages);
        MeasurementTypes mt =  MeasurementTypes.getInstance();
        mt.reInit();
        return mapping.findForward("success");

    }
    
    private boolean allInputIsValid(HttpServletRequest request, String type, String typeDesc, String typeDisplayName, String measuringInstrc){
        
        ActionMessages errors = new ActionMessages();  
        EctValidation validate = new EctValidation();
        String regExp = validate.getRegCharacterExp();
        boolean isValid = true;
        try{
            
            String sql = "SELECT type FROM measurementType WHERE type='" + type +"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            rs.next();
            if(rs.getRow()>0){
                errors.add(type,
                new ActionMessage("error.oscarEncounter.Measurements.duplicateTypeName"));
                saveErrors(request, errors);
                isValid = false;                
            }
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }     
        
        String errorField = "The type " + type;
        if(!validate.matchRegExp(regExp, type)){
            errors.add(type,
            new ActionMessage("errors.invalid", errorField));
            saveErrors(request, errors);
            isValid = false;
        }
        if(!validate.maxLength(4, type)){
            errors.add(type,
            new ActionMessage("errors.maxlength", errorField, "4"));
            saveErrors(request, errors);
            isValid = false;
        }

        errorField = "The type description " + typeDesc;
        if(!validate.matchRegExp(regExp, typeDesc)){
            errors.add(typeDesc,
            new ActionMessage("errors.invalid", errorField));
            saveErrors(request, errors);
            isValid = false;
        }
        if(!validate.maxLength(255, type)){
            errors.add(type,
            new ActionMessage("errors.maxlength", errorField, "255"));
            saveErrors(request, errors);
            isValid = false;
        }
        
        errorField = "The type description " + typeDisplayName;
        if(!validate.matchRegExp(regExp, typeDisplayName)){
            errors.add(typeDisplayName,
            new ActionMessage("errors.invalid", errorField));
            saveErrors(request, errors);
            isValid = false;
        }
        if(!validate.maxLength(255, type)){
            errors.add(type,
            new ActionMessage("errors.maxlength", errorField, "255"));
            saveErrors(request, errors);
            isValid = false;
        }
        
        errorField = "The measuring instruction " + measuringInstrc;
        if(!validate.matchRegExp(regExp, measuringInstrc)){
            errors.add(measuringInstrc,
            new ActionMessage("errors.invalid", errorField));
            saveErrors(request, errors);
            isValid = false;
        }
        if(!validate.maxLength(255, type)){
            errors.add(type,
            new ActionMessage("errors.maxlength", errorField, "255"));
            saveErrors(request, errors);
            isValid = false;
        }
       
        return isValid;
        
    }
}
