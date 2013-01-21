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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.quatro.model.LookupCodeValue;
import com.quatro.service.LookupManager;

public class LookupTableListAction extends DispatchAction {
    private LookupManager lookupManager=null;
    
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
			String tableId="FCT";
			List lst = lookupManager.LoadCodeList(tableId, true, null, null);
			
			for(int i=0; i<lst.size(); i++)
			{
				LookupCodeValue lkv = (LookupCodeValue) lst.get(i);
				List l1 = lookupManager.LoadCodeList("LKT",true,lkv.getCode(),null,null);
				lkv.setAssociates(l1);
			}
			
			DynaActionForm qform = (DynaActionForm) form;
			qform.set("modules",lst);
			return mapping.findForward("list");
	}
}
