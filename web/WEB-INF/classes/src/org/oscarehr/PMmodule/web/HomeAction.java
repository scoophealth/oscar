package org.oscarehr.PMmodule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;

public class HomeAction extends BaseAction {
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		super.setMenu(request,KeyConstants.MENU_HOME);
    		return mapping.findForward("home");
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
	}
}
