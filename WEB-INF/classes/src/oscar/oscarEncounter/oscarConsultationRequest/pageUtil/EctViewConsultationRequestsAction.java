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
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class EctViewConsultationRequestsAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String sendTo = null;
        EctViewConsultationRequestsForm frm = (EctViewConsultationRequestsForm) form;
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        if(bean == null)
            return mapping.findForward("eject");
        if(bean.isCurrentTeam() && frm.getSendTo() == null) {
            sendTo = frm.getSendTo();
        } else {
            sendTo = frm.getSendTo();
            frm.setCurrentTeam(sendTo);
            if(!sendTo.equals("-1"))
                bean.setCurrentTeam(sendTo);
        }
        request.setAttribute("teamVar", sendTo);
        return mapping.findForward("success");
    }
}
