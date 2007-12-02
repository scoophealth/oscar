/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/


package org.caisi.core.web;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.model.CaisiEditor;
import org.caisi.service.CaisiRoleManager;
import org.caisi.service.CaisiEditorManager;

// use your IDE to handle imports
public class CaisiEditorAction extends DispatchAction {
    private static Log log = LogFactory.getLog(CaisiEditorAction.class);
    private CaisiEditorManager mgr = null;
    //private CaisiRoleManager caisiRoleMgr = null;

    public void setCaisiEditorManager(CaisiEditorManager CaisiEditorManager) {
        this.mgr = CaisiEditorManager;
    }
    /*public void setCaisiRoleManager(CaisiRoleManager caisiRoleManager) {
        this.caisiRoleMgr = caisiRoleManager;
    }*/
    public ActionForward cancel(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        return list(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        mgr.removeCaisiEditor(request.getParameter("CaisiEditor.id"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("CaisiEditor.deleted"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }
    public ActionForward edit(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'edit' method...");
        }
        DynaActionForm CaisiEditorForm = (DynaActionForm) form;
        String CaisiEditorId = request.getParameter("id");
        // null CaisiEditorId indicates an add
        if (CaisiEditorId != null) {
            CaisiEditor CaisiEditor = mgr.getCaisiEditor(CaisiEditorId);
            if (CaisiEditor == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("CaisiEditor.missing"));
                saveErrors(request, errors);
                return mapping.findForward("list");
            }
            //request.setAttribute("issueRole", CaisiEditor.getRole());
            CaisiEditorForm.set("caisiEditor", CaisiEditor);
        }
        //request.setAttribute("caisiRoles", caisiRoleMgr.getRoles());
        return mapping.findForward("edit");
    }
    public ActionForward list(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'list' method...");
        }
        request.setAttribute("caisiEditors", mgr.getCaisiEditors());
        return mapping.findForward("list");
    }
    public ActionForward save(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'save' method...");
        }
        
        // run validation rules on this form
        ActionMessages errors = form.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            //request.setAttribute("caisiRoles", caisiRoleMgr.getRoles());
            return mapping.findForward("edit");
        }
        
        DynaActionForm CaisiEditorForm = (DynaActionForm) form;

	//issue code cannot be duplicated
	/*String newCode = ((CaisiEditor)CaisiEditorForm.get("CaisiEditor")).getLabel();
	String newId = String.valueOf(((CaisiEditor)CaisiEditorForm.get("CaisiEditor")).getId());
	List CaisiEditors = mgr.getCaisiEditors();
	for(Iterator it = CaisiEditors.iterator(); it.hasNext();) {
	    CaisiEditor CaisiEditor = (CaisiEditor)it.next();
	    String existCode = CaisiEditor.getCode();
	    String existId = String.valueOf(CaisiEditor.getId());
	    if((existCode.equals(newCode)) && !(existId.equals(newId))) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("CaisiEditor.code.exist"));
		saveErrors(request,messages);
                request.setAttribute("caisiRoles", caisiRoleMgr.getRoles());
                return mapping.findForward("edit");
	    }
	}
	*/
        mgr.saveCaisiEditor((CaisiEditor)CaisiEditorForm.get("caisiEditor"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("CaisiEditor.saved"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }
}
