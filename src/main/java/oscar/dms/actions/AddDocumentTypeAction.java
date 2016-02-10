/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.dms.actions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDocUtil;
import oscar.dms.data.AddDocumentTypeForm;

public class AddDocumentTypeAction extends DispatchAction {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
		AddDocumentTypeForm fm = (AddDocumentTypeForm) form;
		HashMap<String,String> errors = new HashMap<String,String>();
        
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin,_admin.document", "r", null)) {
			throw new SecurityException("missing required security object (_admin,_admin.document)");
		}
		
		boolean doctypeadded =false;
		
		if (fm.getDocType().length() == 0) {
            errors.put("typemissing", "dms.error.typeMissing");
            request.setAttribute("doctypeerrors", errors);
            return mapping.findForward("failed");
		} else if (fm.getFunction().length() == 0) {
			 errors.put("modulemissing", "dms.error.moduleMissing");
			 request.setAttribute("doctypeerrors", errors);
			 return mapping.findForward("failed");
		} 
		
		
		// If a new document type is added, include it in the database to create filters
         	
		if (!EDocUtil.getDoctypes(fm.getFunction()).contains(fm.getDocType())){
         	 EDocUtil.addDocTypeSQL(fm.getDocType(),fm.getFunction(),"A");
         	 
         	 doctypeadded = true;
         	 MiscUtils.getLogger().info("new Doc Type added " + doctypeadded);
         
             ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
             return redirect;
         } 
            
         ActionRedirect redirect = new ActionRedirect(mapping.findForward("failed"));
         return redirect;
            
     }
	
}
