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
import java.util.Collection;
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

public final class EctSetupDisplayHistoryAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {

        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        request.getSession().setAttribute("EctSessionBean", bean);
        String type = (String) request.getParameter("type");
        System.out.println("Type: " + type);
        oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler hd = null;
        if (bean!=null){
            String demo = (String) bean.getDemographicNo();
            request.setAttribute("demographicNo",demo);
            if(type!=null){
                hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo, type);
                request.setAttribute("type", type);
            }            
            HttpSession session = request.getSession();
            session.setAttribute( "measurementsData", hd );                        
        }
        else{
            System.out.println("cannot get the EctSessionBean");
        }
        return (mapping.findForward("continue"));
    }
}
