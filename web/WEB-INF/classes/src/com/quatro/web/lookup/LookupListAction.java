package com.quatro.web.lookup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupCodeValue;
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
        String grandParentCode =request.getParameter("grandParentCode");
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
