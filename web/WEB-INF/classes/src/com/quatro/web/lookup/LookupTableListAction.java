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
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
