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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.service.IssueAdminManager;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

// use your IDE to handle imports
public class IssueAdminAction extends DispatchAction {
    private static Logger log = MiscUtils.getLogger();
    
    private IssueAdminManager mgr = SpringUtils.getBean(IssueAdminManager.class);
   
    private SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
   
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        return list(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
        
        mgr.removeIssueAdmin(request.getParameter("issueAdmin.id"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("issueAdmin.deleted"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }
   
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("entering 'edit' method...");
        }
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

        DynaActionForm issueAdminForm = (DynaActionForm) form;
        String issueAdminId = request.getParameter("id");
        // null issueAdminId indicates an add
        if (issueAdminId != null) {
            Issue issueAdmin = mgr.getIssueAdmin(issueAdminId);
            if (issueAdmin == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("issueAdmin.missing"));
                saveErrors(request, errors);
                return mapping.findForward("list");
            }
            request.setAttribute("issueRole", issueAdmin.getRole());
            issueAdminForm.set("issueAdmin", issueAdmin);
        }
        
        request.setAttribute("caisiRoles", secRoleDao.findAll());
        return mapping.findForward("edit");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return list(mapping,form,request,response);
    }
    
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("entering 'list' method...");
        }
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "r", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
        
        request.setAttribute("issueAdmins", mgr.getIssueAdmins());
        return mapping.findForward("list");
    }
   
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("entering 'save' method...");
        }

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

        // run validation rules on this form
        ActionMessages errors = form.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
           // request.setAttribute("caisiRoles", caisiRoleMgr.getRoles());
            return mapping.findForward("edit");
        }

        DynaActionForm issueAdminForm = (DynaActionForm) form;

		//issue code cannot be duplicated
		String newCode = ((Issue)issueAdminForm.get("issueAdmin")).getCode();
		String newId = String.valueOf(((Issue)issueAdminForm.get("issueAdmin")).getId());
		List<Issue> issueAdmins = mgr.getIssueAdmins();
		for(Iterator<Issue> it = issueAdmins.iterator(); it.hasNext();) {
		    Issue issueAdmin = it.next();
		    String existCode = issueAdmin.getCode();
		    String existId = String.valueOf(issueAdmin.getId());
		    if((existCode.equals(newCode)) && !(existId.equals(newId))) {
		    	ActionMessages messages = new ActionMessages();
		    	messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("issueAdmin.code.exist"));
		    	saveErrors(request,messages);
                //request.setAttribute("caisiRoles", caisiRoleMgr.getRoles());
                return mapping.findForward("edit");
		    }
		}

        mgr.saveIssueAdmin((Issue)issueAdminForm.get("issueAdmin"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("issueAdmin.saved"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }
    
    public ActionForward archiveIssues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("entering 'archive' method...");
        }
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
        
        log.info("entering 'archive' method...");
       
        List<Integer> ids = new ArrayList<Integer>();
        
        String idList = request.getParameter("ids");
        
        if(idList != null && idList.length()>0) {
        	String[] vals = idList.split(",");
        	for(String v:vals) {
        		ids.add(Integer.parseInt(v));
        	}
        }
       
        mgr.archiveIssues(ids);
        
      
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("issueAdmin.archived"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }
    
}
