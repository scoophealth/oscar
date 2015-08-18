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
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MeasurementGroupDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.MeasurementTypeDeletedDao;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.MeasurementTypeDeleted;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.data.MeasurementTypes;


public class EctDeleteMeasurementTypesAction extends Action {

	private MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
	private MeasurementGroupDao measurementGroupDao = SpringUtils.getBean(MeasurementGroupDao.class);
	private MeasurementTypeDeletedDao measurementTypeDeletedDao = SpringUtils.getBean(MeasurementTypeDeletedDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null) )  {
    	
        EctDeleteMeasurementTypesForm frm = (EctDeleteMeasurementTypesForm) form;                
        request.getSession().setAttribute("EctDeleteMeasurementTypesForm", frm);
        String[] deleteCheckbox = frm.getDeleteCheckbox();
        GregorianCalendar now=new GregorianCalendar(); 
       
                                                                                        
            
        if(deleteCheckbox != null){
            for(int i=0; i<deleteCheckbox.length; i++){
                MiscUtils.getLogger().debug(deleteCheckbox[i]);
                
                MeasurementType mt = measurementTypeDao.find(Integer.parseInt(deleteCheckbox[i]));
                if(mt != null) {
                	MeasurementTypeDeleted mtd = new MeasurementTypeDeleted();
                	mtd.setType(mt.getType());
                	mtd.setTypeDisplayName(mt.getTypeDisplayName());
                	mtd.setTypeDescription(mt.getTypeDescription());
                	mtd.setMeasuringInstruction(mt.getMeasuringInstruction());
                	mtd.setValidation(mt.getValidation());
                	mtd.setDateDeleted(new Date());
                	measurementTypeDeletedDao.persist(mtd);
                	
                	measurementTypeDao.remove(mt.getId());
                	
                	measurementGroupDao.remove(measurementGroupDao.findByTypeDisplayName(mt.getTypeDisplayName()));
                }
                
                
            }
        }
        

         
      
        MeasurementTypes mt =  MeasurementTypes.getInstance();
        mt.reInit();
        return mapping.findForward("success");

		}else{
			throw new SecurityException("Access Denied!"); //missing required security object (_admin)
		}
    }
     
}
