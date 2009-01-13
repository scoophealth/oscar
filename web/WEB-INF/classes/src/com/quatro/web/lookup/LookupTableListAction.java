package com.quatro.web.lookup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;

public class LookupTableListAction extends BaseAdminAction {
    private LookupManager lookupManager=null;
    
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_LOOKUP);
			return list(mapping,form,request,response);
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
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
