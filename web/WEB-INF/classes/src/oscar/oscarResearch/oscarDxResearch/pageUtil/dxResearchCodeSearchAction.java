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
package oscar.oscarResearch.oscarDxResearch.pageUtil;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarResearch.oscarDxResearch.bean.*;

public final class dxResearchCodeSearchAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
                
        //String demographicNo = request.getParameter("demographicNo");
        String[] xml_research = new String[5];
        xml_research[0] = request.getParameter("xml_research1");
        xml_research[1] = request.getParameter("xml_research2");
        xml_research[2] = request.getParameter("xml_research3");
        xml_research[3] = request.getParameter("xml_research4");
        xml_research[4] = request.getParameter("xml_research5");
                
        dxCodeSearchBeanHandler hd = new dxCodeSearchBeanHandler(xml_research);
        HttpSession session = request.getSession();
        session.setAttribute("allMatchedCodes", hd);                
        
        return mapping.findForward("success");
    }
}
