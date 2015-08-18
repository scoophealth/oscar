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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MeasurementGroupDao;
import org.oscarehr.common.model.MeasurementGroup;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public class EctEditMeasurementGroupAction extends Action {

	private MeasurementGroupDao dao = SpringUtils.getBean(MeasurementGroupDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null) )  {
    	
        EctEditMeasurementGroupForm frm = (EctEditMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctEditMeasurementGroupForm", frm);
        String groupName = frm.getGroupName();

        if(frm.getForward()!=null){
          
                                                                                        
                
            if (frm.getForward().compareTo("add")==0) {
                MiscUtils.getLogger().debug("the add button is pressed");
                String[] selectedAddTypes = frm.getSelectedAddTypes();  
                if(selectedAddTypes != null){
                    for(int i=0; i<selectedAddTypes.length; i++){
                        MiscUtils.getLogger().debug(selectedAddTypes[i]);
                        MeasurementGroup mg = new MeasurementGroup();
                        mg.setName(groupName);
                        mg.setTypeDisplayName(selectedAddTypes[i]);
                        dao.persist(mg);
                    }
                }
            }
            else if (frm.getForward().compareTo("delete")==0){
                MiscUtils.getLogger().debug("the delete button is pressed");
                String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();
                List<MeasurementGroup>mesList = null; 
                if(selectedDeleteTypes != null){
                    for(int i=0; i<selectedDeleteTypes.length; i++){
                        MiscUtils.getLogger().debug(selectedDeleteTypes[i]);

                        List<MeasurementGroup> mgList = dao.findByNameAndTypeDisplayName(groupName, selectedDeleteTypes[i]);
                        if(mgList != null && mgList.size()==1)
                        	dao.remove(mgList.get(0).getId());
                    }
                }
            }
            
                
            
        }                          
        
        return mapping.findForward("success");

		}else{
			throw new SecurityException("Access Denied!"); //missing required security object (_admin)
		}
    }
     
}
