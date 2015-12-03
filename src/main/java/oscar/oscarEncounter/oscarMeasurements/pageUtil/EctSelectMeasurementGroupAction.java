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
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MeasurementGroupDao;
import org.oscarehr.common.dao.MeasurementGroupStyleDao;
import org.oscarehr.common.model.MeasurementGroup;
import org.oscarehr.common.model.MeasurementGroupStyle;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.managers.MeasurementManager;

import oscar.oscarEncounter.oscarMeasurements.bean.EctStyleSheetBeanHandler;

public class EctSelectMeasurementGroupAction extends Action {
	
	private MeasurementGroupStyleDao styleDao = SpringUtils.getBean(MeasurementGroupStyleDao.class);
	private MeasurementGroupDao groupDao = SpringUtils.getBean(MeasurementGroupDao.class);
	private MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        EctSelectMeasurementGroupForm frm = (EctSelectMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctSelectMeasurementGroupForm", frm);
        
        String groupName = frm.getSelectedGroupName();
        String forward = frm.getForward();
        
        MiscUtils.getLogger().debug("The forward message is: " + forward);
        
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
        if(forward.compareTo("dsHTML")==0){
        	
        	String state = "addDSHTML";
        	String groupId = null;
        	boolean propExists = false;
        	String propKey = null;
        	
        	groupId = measurementManager.findGroupId(groupName);
        	propKey = "mgroup.ds.html."+groupId;
        	propExists = measurementManager.isProperty(propKey);
        	
        	if(propExists){
        		state = "removeDSHTML";
        	}
        	//set here if if should be addDSHTML or removeDSHTML or completeDSHTML
        	return mapping.findForward(state);
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
    	
    	//MeasurementGroupStyle
    	for(MeasurementGroupStyle ms:styleDao.findByGroupName(inputGroupName)) {
    		styleDao.remove(ms.getId());
        } 
    	
        //MeasurementGroup
        for(MeasurementGroup m:groupDao.findByName(inputGroupName)) {
        	groupDao.remove(m.getId());
        }
        
    }
}
