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
package oscar.oscarEncounter.oscarMeasurements.oscarForm.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
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
import oscar.oscarEncounter.oscarMeasurements.oscarForm.bean.*;

public final class EctSetupVTFormAction extends Action {
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws Exception {
        
        /*****************************************************
         *Create a new group named VTForm in the measurementGroup table
         *Create new setVTFormAction
         *Map the new action in strut-config file and foward to formVT.jsp
         *
         *****************************************************/
        HttpSession session = request.getSession();
        
        String groupName = "VT";
        EctVTFormForm frm = (EctVTFormForm) form;
        //EctValidation ectValidation = new EctValidation();
        //String css = ectValidation.getCssPath(groupName);
        java.util.Calendar calender = java.util.Calendar.getInstance();
        String day =  Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
        String month =  Integer.toString(calender.get(java.util.Calendar.MONTH)+1);
        String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
        String today = year+"-"+month+"-"+day;
        
        //request.setAttribute("groupName", groupName);
        //request.setAttribute("css", css);
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        
        if (bean!=null){
            request.getSession().setAttribute("EctSessionBean", bean);
            String demo = (String) bean.getDemographicNo();            
            oscar.oscarEncounter.oscarMeasurements.oscarForm.bean.EctVTFormMeasurementTypesBeanHandler hd = new oscar.oscarEncounter.oscarMeasurements.oscarForm.bean.EctVTFormMeasurementTypesBeanHandler(demo);            
            session.setAttribute("measurementTypes", hd);
            for(int i=0; i<hd.getMeasurementTypeVector().size(); i++){
                frm.setValue("date-" + i, today);
            }            
            
        }
        return (mapping.findForward("continue"));
    }
    
    
}
