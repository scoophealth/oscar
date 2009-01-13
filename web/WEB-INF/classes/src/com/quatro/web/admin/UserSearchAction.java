package com.quatro.web.admin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.model.security.Secuserrole;
import com.quatro.service.security.RolesManager;
import com.quatro.service.security.UsersManager;

public class UserSearchAction extends BaseAdminAction {

	private LogManager logManager;

	private RolesManager rolesManager;

	private UsersManager usersManager;


	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}

	public void setRolesManager(RolesManager rolesManager) {
		this.rolesManager = rolesManager;
	}

	public void setUsersManager(UsersManager usersManager) {
		this.usersManager = usersManager;
	}

	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}



	private ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_USER);
			
			ArrayList roleList = (ArrayList)rolesManager.getRoles();	
			request.getSession(true).setAttribute("roles", roleList);
			request.setAttribute("secuserroles",request.getSession(true).getAttribute("secuserroles"));
			return mapping.findForward("list");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_USER);
	
			DynaActionForm searchForm = (DynaActionForm)form;
			UserSearchFormBean formBean = (UserSearchFormBean)searchForm.get("criteria");
	/*
			String userName = formBean.getUserName();
			String roleName = formBean.getRoleName();
			String active = formBean.getActive();
			String fname = formBean.getFirstName();
			String lname = formBean.getLastName();
		*/	
						
			ArrayList surlist = new ArrayList();
	
			List userlist = usersManager.search(formBean);
			
			Hashtable ht = new Hashtable();
			if (userlist != null && userlist.size() > 0) {
				for (int i = 0; i < userlist.size(); i++) {
					Object[] tmp = (Object[]) userlist.get(i);
	
					Secuserrole sur = new Secuserrole();
					sur.setId((Integer) tmp[0]);  // provider No
					sur.setUserName((String) tmp[1]);
					sur.setProviderFName((String) tmp[3]);
					sur.setProviderLName((String) tmp[2]);
					sur.setFullName((String) tmp[2] + ", " + (String) tmp[3]);
					sur.setProviderNo((String) tmp[4]);
					sur.setActiveyn(Integer.valueOf((String) tmp[5]));
					surlist.add(sur);
				}
	
			}
			request.getSession(true).setAttribute("secuserroles", surlist); //session the list
			request.setAttribute("secuserroles", surlist);
			logManager.log("read", "full secuserroles list", "", request);
	
			return mapping.findForward("list");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

}