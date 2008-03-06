package com.quatro.web.lookup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.service.TicklerManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.service.LookupManager;

public class LookupListAction extends DispatchAction {
    private LookupManager lookupManager=null;
    
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String tableId=request.getParameter("tableId");
		List lst = lookupManager.LoadCodeList(tableId, true, null, null);
		LookupListForm qform = (LookupListForm) form;
		qform.setLookups(lst);
		qform.setOpenerFormName(request.getParameter("openerForm"));
		qform.setOpenerCodeElementName(request.getParameter("codeName"));
		qform.setOpenerDescElementName(request.getParameter("descName"));
		return mapping.findForward("list");
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LookupListForm qform = (LookupListForm) form;
        String tableId=request.getParameter("tableId");
		List lst = lookupManager.LoadCodeList(tableId, true, null, qform.getKeywordName());
		qform.setLookups(lst);
		qform.setOpenerFormName(request.getParameter("openerForm"));
		qform.setOpenerCodeElementName(request.getParameter("codeName"));
		qform.setOpenerDescElementName(request.getParameter("descName"));
		return mapping.findForward("list");
	}
	
}
