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
 * PHRLoginAction.java
 *
 * Created on April 16, 2007, 3:28 PM
 *
 */

package org.oscarehr.phr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author jay
 */
public class PHRLogoutAction extends DispatchAction {
     private static Logger log = MiscUtils.getLogger();
    
    /**
     * Creates a new instance of PHRLoginAction
     */
    public PHRLogoutAction() {
    }
    
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       HttpSession session = request.getSession();
       session.removeAttribute(PHRAuthentication.SESSION_PHR_AUTH);
       session.removeAttribute(PHRService.SESSION_PHR_EXCHANGE_TIME);
       String forwardTo = (String) request.getParameter("forwardto");
       ActionRedirect ar = new ActionRedirect(forwardTo);
       return ar;
    }
}
