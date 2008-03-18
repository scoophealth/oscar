package com.quatro.web.lookup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.quatro.model.LookupTableDefValue;
import com.quatro.service.LookupManager;

public class LookupCodeListAction extends DispatchAction {
    private LookupManager lookupManager=null;
    
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String tableId=request.getParameter("id");
		LookupTableDefValue tableDef = lookupManager.GetLookupTableDef(tableId); 

		List lst = lookupManager.LoadCodeList(tableId, false, null, null);
		
		DynaActionForm qform = (DynaActionForm) form;
		qform.set("codes",lst);
		qform.set("tableDef", tableDef);
		return mapping.findForward("list");
	}
}
