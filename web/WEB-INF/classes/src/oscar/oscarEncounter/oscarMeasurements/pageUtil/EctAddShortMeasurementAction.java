/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * EctAddShortMeasurementAction.java
 *
 * Created on September 27, 2006, 3:16 PM
 *
 */

package oscar.oscarEncounter.oscarMeasurements.pageUtil; 

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarPrevention.reports.FollowupManagement;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class EctAddShortMeasurementAction extends Action{
    
    /** Creates a new instance of EctAddShortMeasurementAction */
    public EctAddShortMeasurementAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException  {
     
        //MARK IN MEASUREMENTS????
       String followUpType =  request.getParameter("followupType");//"FLUF";
       String followUpValue = request.getParameter("followupValue"); //"L1";
       String[] demos = request.getParameterValues("demos");
       String id = request.getParameter("id");
       String providerNo = (String) request.getSession().getAttribute("user");
  
       String comment = request.getParameter("message");
       if ( followUpType != null && followUpValue != null){   
           FollowupManagement fup = new FollowupManagement();
           System.out.println("followUpType:"+followUpType+" followUpValue: "+followUpValue+" demos:"+demos+" providerNo:"+providerNo+" comment:"+comment);
           fup.markFollowupProcedure(followUpType,followUpValue,demos,providerNo,UtilDateUtilities.now(),comment);
           response.getWriter().print("id="+id+"&followupValue="+followUpValue+"&Date="+UtilDateUtilities.DateToString(UtilDateUtilities.now()));        
       }
       return null;
    }
    
}
