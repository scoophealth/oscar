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


package oscar.oscarMessenger.pageUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.util.ParameterActionForward;

public class MsgWriteToEncounterAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
            GregorianCalendar now=new GregorianCalendar();
            int curYear = now.get(Calendar.YEAR);
            int curMonth = (now.get(Calendar.MONTH)+1);
            int curDay = now.get(Calendar.DAY_OF_MONTH);
            String dateString = curYear+"-"+curMonth+"-"+curDay;
            String provider = (String) request.getSession().getAttribute("user");

            

            //../oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=
            ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
            forward.addParameter("providerNo", provider);
            forward.addParameter("appointmentNo", "");
            forward.addParameter("demographicNo", request.getParameter("demographic_no"));
            forward.addParameter("curProviderNo", provider);
            forward.addParameter("reason", "oscarMessenger");
            forward.addParameter("userName", request.getSession().getAttribute("userfirstname")+" "+request.getSession().getAttribute("userlastname"));
            forward.addParameter("curDate", dateString);
            forward.addParameter("appointmentDate", "");
            forward.addParameter("startTime", "");
            forward.addParameter("status", "");
            forward.addParameter("msgId", request.getParameter("msgId"));
            String encType = request.getParameter("encType");
            
            if( encType != null )
                forward.addParameter("encType", encType);
            return forward;       
            //if(request.getParameter("name")!=null)               
    }

}
