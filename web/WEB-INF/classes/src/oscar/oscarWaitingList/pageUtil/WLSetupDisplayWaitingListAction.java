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
package oscar.oscarWaitingList.pageUtil;

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
import oscar.oscarDB.DBHandler;
import oscar.OscarProperties;
import oscar.oscarWaitingList.bean.*;
import oscar.oscarProvider.bean.*;
import oscar.util.*;

public final class WLSetupDisplayWaitingListAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        
        String waitingListId = (String) request.getParameter("waitingListId");
        System.out.println("waitingListId: "+ waitingListId);
        WLWaitingListBeanHandler hd = new WLWaitingListBeanHandler(waitingListId);
        ProviderNameBeanHandler phd = new ProviderNameBeanHandler();
        WLWaitingListNameBeanHandler wlNameHd = new WLWaitingListNameBeanHandler();
        Collection allWaitingListName = wlNameHd.getWaitingListNameVector();
        Collection allProviders = phd.getProviderNameVector();
        String nbPatients = Integer.toString(hd.getWaitingListVector().size());
        String today = UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd");
        HttpSession session = request.getSession();
        session.setAttribute( "waitingList", hd );            
        session.setAttribute("waitingListName", hd.getWaitingListName());
        session.setAttribute("allProviders", allProviders);
        session.setAttribute("nbPatients", nbPatients);
        session.setAttribute("allWaitingListName", allWaitingListName);
        session.setAttribute("today", today);
        return (mapping.findForward("continue"));
    }
}
