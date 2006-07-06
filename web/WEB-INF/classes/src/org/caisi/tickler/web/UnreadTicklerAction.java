package org.caisi.tickler.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.model.CustomFilter;
import org.caisi.service.TicklerManager;

import oscar.login.LoginCheckLogin;

public class UnreadTicklerAction extends DispatchAction {

	private TicklerManager ticklerMgr = null;
	
	public void setTicklerManager(TicklerManager ticklerManager) {
		this.ticklerMgr = ticklerManager;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String providerNo = (String) request.getSession().getAttribute("user");
		if(providerNo == null) {
			return mapping.findForward("login");
		}
		return refresh(mapping,form,request,response);
	}

	public ActionForward refresh(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String providerNo = (String) request.getSession().getAttribute("user");
		if(providerNo == null) {
			return mapping.findForward("login");
		}
		
		int oldNum = -1;
		if(request.getSession().getAttribute("num_ticklers") != null) {
			oldNum = ((Integer)request.getSession().getAttribute("num_ticklers")).intValue();
		}
        CustomFilter filter = new CustomFilter();
        filter.setAssignee(providerNo);
        Collection coll = ticklerMgr.getTicklers(filter);
        if(oldNum != -1 && (coll.size() > oldNum)) {
        	request.setAttribute("difference",new Integer(coll.size() - oldNum));
        }
        request.setAttribute("ticklers",coll);
        request.getSession().setAttribute("num_ticklers",new Integer(coll.size()));    
		return mapping.findForward("view");
		
	}
	
	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LazyValidatorForm loginForm = (LazyValidatorForm)form;
		
		String propName = request.getContextPath().substring(1) + ".properties";
		String ip = request.getRemoteAddr();

		String userName = (String)loginForm.get("username");
	    String password = (String)loginForm.get("password");
	    String pin = (String)loginForm.get("pin");
	    
	    if(userName == null || password == null || pin == null) {
	    	String errorMsg = "Need username/password/ping";
	        request.setAttribute("errorMsg",errorMsg);
	        return mapping.findForward("login");
	    }
	    
	    LoginCheckLogin cl = new LoginCheckLogin(propName);
	    if (!cl.isPropFileFound()) {
	      String errorMsg = "Unable to open the properties file " +
	          cl.getPropFileName() + ".";
	      request.setAttribute("errormsg",errorMsg);
	      return (mapping.findForward("login"));
	    }
	    
	    if (cl.isBlock(ip, userName)) {
	    	String errorMsg = "Your account is locked. Please contact your administrator to unlock.";
	        request.setAttribute("errormsg",errorMsg);
	        return mapping.findForward("login");
	    }
	    
	    String[] strAuth;
	    try {
	      strAuth = cl.auth(userName, password, pin, ip);
	    }
	    catch (Exception e) {
	    	String errorMsg = "DB Connection error";
	        request.setAttribute("errormsg",errorMsg);
	        return mapping.findForward("login");
	    }
	    
	    if(strAuth == null || strAuth.length == 1) {
	    	String errorMsg = "Login Failed";
	        request.setAttribute("errormsg",errorMsg);
	        return mapping.findForward("login");
	    }
	    
        //invalidate the existing sesson
        HttpSession session = request.getSession(false);
        if (session != null) {
          session.invalidate();
          session = request.getSession(); // Create a new session for this user
        }
        session.setMaxInactiveInterval(3200);

        request.getSession().setAttribute("user",strAuth[0]);
 
        return refresh(mapping,form,request,response);
	}
	
	public ActionForward logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate();
		return mapping.findForward("login");
	}
}
