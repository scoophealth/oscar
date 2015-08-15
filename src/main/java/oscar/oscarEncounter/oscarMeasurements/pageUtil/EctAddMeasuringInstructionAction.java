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
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.util.MsgStringQuote;


public class EctAddMeasuringInstructionAction extends Action {

	private MeasurementTypeDao dao = SpringUtils.getBean(MeasurementTypeDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctAddMeasuringInstructionForm frm = (EctAddMeasuringInstructionForm) form;

        
        if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null) )  {
        
        request.getSession().setAttribute("EctAddMeasuringInstructionForm", frm);
        
        MsgStringQuote str = new MsgStringQuote();
        String requestId = "";
        List messages = new LinkedList();
        
       
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
        
        List<MeasurementType> mts = dao.findByMeasuringInstructionAndTypeDisplayName(measuringInstrc,typeDisplayName);
        if(mts.size()>0) {
        	errors.add(measuringInstrc,
            new ActionMessage("error.oscarEncounter.Measurements.duplicateTypeName"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));       
        }
        
        mts = dao.findByTypeDisplayName(typeDisplayName);
        if(mts.size()>0) {
        	MeasurementType mt = mts.get(0);
        	String type = mt.getType();
        	String typeDesc = mt.getTypeDescription();
        	
        	MeasurementType m = new MeasurementType();
        	m.setType(typeDesc);
        	m.setTypeDisplayName(typeDisplayName);
        	m.setMeasuringInstruction(measuringInstrc);
        	m.setValidation(validation);
        	dao.persist(m);
        	
        	requestId = m.getId().toString();
        }
               
        MessageResources mr = getResources(request);
        String msg = mr.getMessage("oscarEncounter.oscarMeasurements.AddMeasuringInstruction.successful", "!");
        messages.add(msg);
        request.setAttribute("messages", messages);                
        return mapping.findForward("success");
        
		}else{
			throw new SecurityException("Access Denied!"); //missing required security object (_admin)
		}

    }
    
    

}
