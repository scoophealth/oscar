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


package oscar.oscarResearch.oscarDxResearch.pageUtil;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarResearch.oscarDxResearch.bean.dxQuickListItemsHandler;
import oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem;

public class dxResearchLoadQuickListItemsAction extends Action {
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {        
    	if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dxresearch", "r", null)) {
    		throw new RuntimeException("missing required security object (_dxresearch)");
    	}
    	
        dxResearchLoadQuickListItemsForm frm = (dxResearchLoadQuickListItemsForm) form;                        
        request.getSession().setAttribute("dxResearchLoadQuickListItemsFrm", frm);
        String quickListName = frm.getQuickListName();
        
        dxResearchCodingSystem codingSys = new dxResearchCodingSystem();        

        dxQuickListItemsHandler quicklistItemsHd = new dxQuickListItemsHandler(quickListName);
        
        HttpSession session = request.getSession();        
        session.setAttribute("codingSystem", codingSys);
        session.setAttribute("allQuickListItems", quicklistItemsHd);
        session.setAttribute("quickListName", quickListName);
        
        return mapping.findForward("success");
    }     
}
