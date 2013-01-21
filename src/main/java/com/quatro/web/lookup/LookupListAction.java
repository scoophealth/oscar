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
import org.apache.struts.actions.DispatchAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupTableDefValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.util.Utility;

public class LookupListAction extends DispatchAction {
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
			getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
		if(tableId.equals("ROL"))
			getAccess(request,KeyConstants.FUN_ADMIN_USER);
		if(tableId.equals("USR"))
			getAccess(request, KeyConstants.FUN_PROGRAM_STAFF);
		if(tableId.equals("CLN"))
			getAccess(request, KeyConstants.FUN_CLIENT);
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
		getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP);
		LookupListForm qform = (LookupListForm) form;
        String tableId=request.getParameter("tableId");
        String parentCode =request.getParameter("parentCode");
        	if(Utility.IsEmpty(parentCode)) parentCode=qform.getParentCode();
		List lst = lookupManager.LoadCodeList(tableId, true,parentCode, null, qform.getKeywordName());
		 LookupTableDefValue tableDef = lookupManager.GetLookupTableDef(tableId);
		qform.setLookups(lst);
		qform.setTableDef(tableDef);
		request.setAttribute("notoken", "Y");
		return mapping.findForward("list");
	}
	
	protected String getAccess(HttpServletRequest request,String functionName) throws NoAccessException
	{
		SecurityManager sec = (SecurityManager) request.getSession()
				.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);
		String acc = sec.GetAccess(functionName, "");
		if (acc.equals(KeyConstants.ACCESS_NONE)) throw new NoAccessException();
		return acc;
	}
	protected String getAccess(HttpServletRequest request,String functionName, String rights) throws NoAccessException
	{
		SecurityManager sec = (SecurityManager) request.getSession()
				.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);
		String acc = sec.GetAccess(functionName, "");
		if (acc.compareTo(rights) < 0) throw new NoAccessException();
		return acc;
	}
	public boolean isReadOnly(HttpServletRequest request,String funName) throws NoAccessException{
		boolean readOnly =false;
		
		SecurityManager sec = (SecurityManager) request.getSession()
				.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);
		String r = sec.GetAccess(funName, null); 
		if (r.compareTo(KeyConstants.ACCESS_READ) < 0) throw new NoAccessException(); 
		if (r.compareTo(KeyConstants.ACCESS_READ) == 0) readOnly=true;
		return readOnly;
	}
	
}
