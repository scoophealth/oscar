/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
package com.quatro.web.lookup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupTableDefValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;
import com.quatro.util.Utility;

public class LookupListAction extends BaseAdminAction {
    private LookupManager lookupManager=null;
     
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NoAccessException {
		return list(mapping,form,request,response);
	}
	
	private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NoAccessException{
        String tableId=request.getParameter("tableId");
		if("PRP,SIT,LKT,QGV,RPG".indexOf(tableId)> 0) throw new NoAccessException();
		if(tableId.equals("FUN"))
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
		if(tableId.equals("ROL"))
			super.getAccess(request,KeyConstants.FUN_ADMIN_USER);
		if(tableId.equals("USR"))
			super.getAccess(request, KeyConstants.FUN_PROGRAM_STAFF);
		if(tableId.equals("CLN"))
			super.getAccess(request, KeyConstants.FUN_CLIENT);
        String parentCode =request.getParameter("parentCode");
        request.setAttribute("parentCode",parentCode);
       
        LookupTableDefValue tableDef = lookupManager.GetLookupTableDef(tableId);
		List lst = lookupManager.LoadCodeList(tableId, true,parentCode, null, null);
		LookupListForm qform = (LookupListForm) form;
		qform.setLookups(lst);
		qform.setTableDef(tableDef);
		
		request.setAttribute("notoken", "Y");
		return mapping.findForward("list");
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NoAccessException{
		super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP);
		LookupListForm qform = (LookupListForm) form;
        String tableId=request.getParameter("tableId");
        String parentCode =request.getParameter("parentCode");
        	if(Utility.IsEmpty(parentCode)) parentCode=qform.getParentCode();
		List lst = lookupManager.LoadCodeList(tableId, true,parentCode, null, qform.getKeywordName());
		 LookupTableDefValue tableDef = lookupManager.GetLookupTableDef(tableId);
		qform.setLookups(lst);
		qform.setTableDef(tableDef);
//		qform.setOpenerFormName(request.getParameter("openerFormName"));
//		qform.setOpenerCodeElementName(request.getParameter("openerCodeElementName"));
//		qform.setOpenerDescElementName(request.getParameter("openerDescElementName"));
		request.setAttribute("notoken", "Y");
		return mapping.findForward("list");
	}
	
}
