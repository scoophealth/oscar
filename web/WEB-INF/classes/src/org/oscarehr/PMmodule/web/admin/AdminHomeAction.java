package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.web.BaseAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;

public class AdminHomeAction extends BaseAction {
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		super.setMenu(request,KeyConstants.MENU_ADMIN);
    		return mapping.findForward("admin");
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
	}
}
