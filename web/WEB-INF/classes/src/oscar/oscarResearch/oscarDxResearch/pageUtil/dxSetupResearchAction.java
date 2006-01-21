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
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarResearch.oscarDxResearch.bean.*;
import oscar.oscarResearch.oscarDxResearch.util.*;

public final class dxSetupResearchAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
            
        dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
        String codingSystem = codingSys.getCodingSystem();          
        
        String demographicNo = request.getParameter("demographicNo");
        String providerNo = request.getParameter("providerNo");
        String selectedQuickList = request.getParameter("quickList");          
        dxResearchBeanHandler hd = new dxResearchBeanHandler(demographicNo);
        
        dxQuickListBeanHandler quicklistHd = null;
        dxQuickListItemsHandler quicklistItemsHd = null;
        
        //System.out.println("Quick List: " + selectedQuickList);
        if (selectedQuickList.equals("")||selectedQuickList==null){
            quicklistHd = new dxQuickListBeanHandler(providerNo, codingSystem);
            quicklistItemsHd = new dxQuickListItemsHandler(quicklistHd.getLastUsedQuickList(),providerNo);
        }
        else{
            quicklistItemsHd = new dxQuickListItemsHandler(selectedQuickList,providerNo);
            quicklistHd = new dxQuickListBeanHandler(providerNo,codingSystem);
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("codingSystem", codingSystem);
        session.setAttribute("allQuickLists", quicklistHd);
        session.setAttribute("allQuickListItems", quicklistItemsHd);
        session.setAttribute("allDiagnostics", hd );
        session.setAttribute("demographicNo", demographicNo ); 
        session.setAttribute("providerNo", providerNo ); 
       
        return (mapping.findForward("success"));
    }
}
