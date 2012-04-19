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


package oscar.dms.actions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.dms.EDocUtil;
import oscar.dms.data.AddDocumentTypeForm;

public class AddDocumentTypeAction extends DispatchAction {
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
		AddDocumentTypeForm fm = (AddDocumentTypeForm) form;
		HashMap<String,String> errors = new HashMap<String,String>();
        
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
