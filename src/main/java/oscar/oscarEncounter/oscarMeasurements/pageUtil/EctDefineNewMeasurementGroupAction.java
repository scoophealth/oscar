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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.MeasurementGroupStyleDao;
import org.oscarehr.common.model.MeasurementGroupStyle;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;


public class EctDefineNewMeasurementGroupAction extends Action {

	private MeasurementGroupStyleDao dao = SpringUtils.getBean(MeasurementGroupStyleDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
    	if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null) )  {
    	
        EctDefineNewMeasurementGroupForm frm = (EctDefineNewMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctDefineNewMeasurementGroupForm", frm);
        
        String groupName = frm.getGroupName();
        String styleSheet = frm.getStyleSheet();
        
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
        
		}else{
			throw new SecurityException("Access Denied!"); //missing required security object (_admin)
		} 
    }
    
    /*****************************************************************************************
     * Write the new groupName to the database if there's no duplication
     *
     * @return boolean
     ******************************************************************************************/
    private boolean write2Database(String inputGroupName, String styleSheet){
        boolean isWrite2Database = true;
        
        for(MeasurementGroupStyle mgs:dao.findAll()) {
        	String groupName = mgs.getGroupName();
            if (inputGroupName.compareTo(groupName)==0){
                isWrite2Database = false;
                break;
            }
        }
        
        if (isWrite2Database){
        	MeasurementGroupStyle mgs = new MeasurementGroupStyle();
        	mgs.setGroupName(inputGroupName);
        	try {
        		mgs.setCssId(Integer.parseInt(styleSheet));
        	}catch(NumberFormatException e) {
        		//nothing
        	}
        	dao.persist(mgs);
           
        }
       
        return isWrite2Database;
    }
}
