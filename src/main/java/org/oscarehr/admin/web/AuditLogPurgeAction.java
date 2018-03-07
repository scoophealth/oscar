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
package org.oscarehr.admin.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.AuditLogManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class AuditLogPurgeAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.auditLogPurge", "r", null)) {
        	throw new SecurityException("missing required security object (_admin.auditLogPurge)");
        }
		
		
		String dt = request.getParameter("dateBegin");
		if(dt == null || dt.length() == 0) {
			request.setAttribute("msg", "No date parameter was sent to the server!");
		} 
		
		Date endDateToPurge = null;
		if(dt != null && dt.length() > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				endDateToPurge = formatter.parse(dt);	
			}catch(ParseException e) {
				request.setAttribute("msg", "Error parsing date. Should be in yyyy-MM-dd format");
			}
		}
		
		if(endDateToPurge != null) {
			AuditLogManager manager = SpringUtils.getBean(AuditLogManager.class);
			try  {
				int numRecords = manager.purgeAuditLog(LoggedInInfo.getLoggedInInfoFromSession(request), endDateToPurge);
				request.setAttribute("msg", "Success. Removed " + numRecords + " records from the audit log.");
			}catch(Exception e) {
				request.setAttribute("msg","Error. " + e.getCause() != null?e.getCause().getMessage():e.getMessage());
			}
		}
		
		
		return mapping.findForward("success");
	}
}
