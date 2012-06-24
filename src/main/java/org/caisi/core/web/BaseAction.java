/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.core.web;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.caisi.service.InfirmBedProgramManager;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.DispatchActionSupport;

public class BaseAction extends DispatchActionSupport
{
    public void addError(HttpServletRequest req, String message)
    {
        ActionMessages msgs = getErrors(req);
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "errors.detail", message));
        addErrors(req, msgs);
    }

    public void addMessage(HttpServletRequest req, String message)
    {
        ActionMessages msgs = getMessages(req);
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "errors.detail", message));
        addMessages(req, msgs);
    }

    public ApplicationContext getAppContext()
    {
        return WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext());
    }

	public InfirmBedProgramManager getInfirmBedProgramManager() {
		InfirmBedProgramManager bpm = (InfirmBedProgramManager) getAppContext()
				.getBean("infirmBedProgramManager");
		return bpm;
	}
    
	public ProgramManager getProgramManager(){
		ProgramManager pm = (ProgramManager) getAppContext().getBean("programManager");
		return pm;
	}
	
	public AdmissionManager getAdmissionManager(){
		AdmissionManager mgr = (AdmissionManager) getAppContext().getBean("admissionManager");
		return mgr;
	}
}
