package com.quatro.web.admin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;
import org.apache.struts.actions.DispatchAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.DataViews;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.ReportFilterValue;
import com.quatro.model.ReportTempCriValue;
import com.quatro.model.ReportValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.model.security.Secobjprivilege;

import com.quatro.model.security.Secrole;
import com.quatro.service.LookupManager;
import com.quatro.service.security.RolesManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.util.Utility;

public class RoleManagerAction extends BaseAdminAction {

	private LogManager logManager;

	private RolesManager rolesManager;

	private LookupManager lookupManager;

	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}

	public void setRolesManager(RolesManager rolesManager) {
		this.rolesManager = rolesManager;
	}

	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			return list(mapping, form, request, response);
	}

	private ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
			List list = null;
			list = rolesManager.getRoles();
	
			request.setAttribute("roles", list);
			logManager.log("read", "full roles list", "", request);
	
			String scrollPosition = (String) request.getParameter("scrollPosition");
			if(null != scrollPosition) {
				request.setAttribute("scrPos", scrollPosition);
			}else{
				request.setAttribute("scrPos", "0");
			}
			
			return mapping.findForward("list");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}


	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
	
			DynaActionForm secroleForm = (DynaActionForm) form;
	
			String roleName = request.getParameter("roleName");
	
			if (isCancelled(request)) {
				return list(mapping, form, request, response);
			}
	
			if (!Utility.IsEmpty(roleName)) {
				super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
				Secrole secrole = rolesManager.getRoleByRolename(roleName);
	
				if (secrole == null) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"role.missing"));
					saveMessages(request, messages);
	
					return list(mapping, form, request, response);
				}
	
				//secroleForm.set("roleNo", secrole.getRoleNo());
				secroleForm.set("roleName", secrole.getRoleName());
				secroleForm.set("description", secrole.getDescription());
				
				if (secrole.isActive())
					secroleForm.set("active", "on");
				else secroleForm.set("active", "off");
				request.setAttribute("secroleForEdit", secrole);		
	
				ArrayList secobjprivilegeLst = new ArrayList();
				ArrayList funLst = new ArrayList();
				funLst = (ArrayList)rolesManager.getFunctions(secrole.getRoleName());
				
				
				
				int lineno = funLst.size();
						
				for (int i = 0; i < lineno; i++) {
									
					Secobjprivilege objNew = (Secobjprivilege) funLst.get(i);
	
					String accessType_code = objNew.getPrivilege_code();
					String accessType_description = rolesManager.getAccessDesc(accessType_code );
					String function_code = objNew.getObjectname_code();
					String function_description = rolesManager.getFunctionDesc(function_code );
	
					if (accessType_code != null)
						objNew.setPrivilege_code(accessType_code);
					if (accessType_description != null)
						objNew.setPrivilege_desc(accessType_description);
					if (function_code != null)
						objNew.setObjectname_code(function_code);
					if (function_description != null)
						objNew.setObjectname_desc(function_description);
					else
						objNew.setObjectname_desc(function_code);
	
					secobjprivilegeLst.add(objNew);
		
						
				
				}
				secroleForm.set("secobjprivilegeLst", secobjprivilegeLst);
				
			}
			else
			{
				super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE,KeyConstants.ACCESS_WRITE);
			}
	
			String scrollPosition = (String) request.getParameter("scrollPosition");
			if(null != scrollPosition) {
				request.setAttribute("scrPos", scrollPosition);
			}else{
				request.setAttribute("scrPos", "0");
			}
			boolean isReadOnly =false;		
			SecurityManager sec = (SecurityManager) request.getSession()
			.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);	
			if (sec.GetAccess(KeyConstants.FUN_ADMIN_ROLE, null).compareTo(KeyConstants.ACCESS_READ) <= 0) 
				isReadOnly=true;
			if(isReadOnly) request.setAttribute("isReadOnly", Boolean.valueOf(isReadOnly));
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}

	public ActionForward saveNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE,KeyConstants.ACCESS_WRITE);
	
			ActionMessages messages = new ActionMessages();
	        boolean isError = false;
	        boolean isWarning = false;
	        
			DynaActionForm secroleForm = (DynaActionForm) form;
	
			Secrole secrole = new Secrole();
			//secrole.setRoleNo((Integer) secroleForm.get("roleNo"));
			String roleName = (String) secroleForm.get("roleName");
			secrole.setRoleName(roleName);
			secrole.setDescription((String) secroleForm.get("description"));
			if("on".equals(secroleForm.get("active"))) secrole.setActive(true) ;
			// check rolename, should be unique
			Secrole existRole = rolesManager.getRoleByRolename(roleName);
	
			if (existRole == null) {
				rolesManager.save(secrole);
	
				LookupCodeValue functions = new LookupCodeValue();
				secroleForm.set("functions", functions);
	
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.role.created",
	           			request.getContextPath(), roleName));
				saveMessages(request,messages);
				
				return edit(mapping, form, request, response);
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.role.exist",
	           			request.getContextPath(), roleName));
				saveMessages(request,messages);
				
				return mapping.findForward("edit");
	
			}
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}

	public ActionForward saveChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE, KeyConstants.ACCESS_UPDATE);
	
			ActionMessages messages = new ActionMessages();
			DynaActionForm secroleForm = (DynaActionForm) form;
	
			LookupCodeValue functions = new LookupCodeValue();
			secroleForm.set("functions", functions);
	
	
			try{
				if (!saveRoleAndFunctions(mapping, form, request, response, true)) {
					return mapping.findForward("edit");
				}
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.save.success",
				request.getContextPath()));
				saveMessages(request,messages);			
				request.setAttribute("pageChanged","");
			}catch(Exception e){
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed",
				request.getContextPath()));
				saveMessages(request,messages);				
			}
	
			return edit(mapping, form, request, response);
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}

	public ActionForward preNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)  {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

	/*
	public ActionForward addFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
	
			DynaActionForm secroleForm = (DynaActionForm) form;
			ChangeFunLstTable(2, secroleForm, request);
	
			return mapping.findForward("functions");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}
	*/
	public ActionForward addFunctionInEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
	
			DynaActionForm secroleForm = (DynaActionForm) form;
			ChangeFunLstTable(2, secroleForm, request);
			request.setAttribute("secroleForEdit", "flag");
	
			String scrollPosition = (String) request.getParameter("scrollPosition");
			if(!Utility.IsEmpty(scrollPosition)) {
				request.setAttribute("scrPos", String.valueOf(Integer.valueOf(scrollPosition).intValue()+ 50));
			}else{
				request.setAttribute("scrPos", "0");
			}
			request.setAttribute("pageChanged","1");
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}
/*
	public ActionForward removeFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
			DynaActionForm secroleForm = (DynaActionForm) form;
			ChangeFunLstTable(1, secroleForm, request);
	
			return mapping.findForward("functions");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
			
	}
*/
	public ActionForward removeFunctionInEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
			DynaActionForm secroleForm = (DynaActionForm) form;
			ChangeFunLstTable(1, secroleForm, request);
			request.setAttribute("secroleForEdit", "flag");
			
			String scrollPosition = (String) request.getParameter("scrollPosition");
			if(null != scrollPosition) {
				request.setAttribute("scrPos", scrollPosition);
			}else{
				request.setAttribute("scrPos", "0");
			}
			request.setAttribute("pageChanged","1");
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}

	private void ChangeFunLstTable(int operationType, DynaActionForm myForm,
			HttpServletRequest request) {
		
		ActionMessages messages = new ActionMessages();
		
		ArrayList secobjprivilegeLst = new ArrayList();

		

		Map map = request.getParameterMap();
		String[] arr_lineno = (String[]) map.get("lineno");
		int lineno = 0;
		if (arr_lineno != null)
			lineno = arr_lineno.length;

		switch (operationType) {
		case 1: // remove
			for (int i = 0; i < lineno; i++) {
				String[] isChecked = (String[]) map.get("p" + i);

				if (isChecked == null) {
					Secobjprivilege objNew = new Secobjprivilege();
					
					String[] accessType_code = (String[]) map
							.get("accessTypes_code" + i);
					String[] accessType_description = (String[]) map
							.get("accessTypes_description" + i);
					String[] function_code = (String[]) map.get("function_code"
							+ i);
					String[] function_description = (String[]) map
							.get("function_description" + i);

					if (accessType_code != null)
						objNew.setPrivilege_code(accessType_code[0]);
					if (accessType_description != null)
						objNew.setPrivilege_desc(accessType_description[0]);
					if (function_code != null)
						objNew.setObjectname_code(function_code[0]);
					if (function_description != null)
						objNew.setObjectname_desc(function_description[0]);

					secobjprivilegeLst.add(objNew);

				}

			}
			break;
		case 2: // add
			for (int i = 0; i < lineno; i++) {
				Secobjprivilege objNew = new Secobjprivilege();

				String[] accessType_code = (String[]) map
						.get("accessTypes_code" + i);
				String[] accessType_description = (String[]) map
						.get("accessTypes_description" + i);
				String[] function_code = (String[]) map
						.get("function_code" + i);
				String[] function_description = (String[]) map
						.get("function_description" + i);

				if (accessType_code != null)
					objNew.setPrivilege_code(accessType_code[0]);
				if (accessType_description != null)
					objNew.setPrivilege_desc(accessType_description[0]);
				if (function_code != null)
					objNew.setObjectname_code(function_code[0]);
				if (function_description != null)
					objNew.setObjectname_desc(function_description[0]);

				secobjprivilegeLst.add(objNew);
			}
			Secobjprivilege objNew2 = new Secobjprivilege();
			secobjprivilegeLst.add(objNew2);
			break;

		}
		myForm.set("secobjprivilegeLst", secobjprivilegeLst);
//		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.role.function.add",
//       			request.getContextPath()));
//		saveMessages(request,messages);
	}
/*
	public ActionForward saveFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ROLE);
	
			if (saveRoleAndFunctions(mapping, form, request, response, false))
				return list(mapping, form, request, response);
			else
				return mapping.findForward("list");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}
*/
	private boolean saveRoleAndFunctions(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, boolean saveRole) {

		ActionMessages messages = new ActionMessages();
		DynaActionForm secroleForm = (DynaActionForm) form;
		
		String roleName = (String) secroleForm.get("roleName");
		String providerNo = (String) request.getSession(true).getAttribute("user");

		Secrole secrole=null;
		if(saveRole){
		  secrole = new Secrole();
		  secrole.setRoleName(roleName);
		  secrole.setDescription((String) secroleForm.get("description"));
		  if("on".equals(secroleForm.get("active"))) secrole.setActive(true);
		  request.setAttribute("secroleForEdit", secrole);		
		}
		
		Map map = request.getParameterMap();
		String[] arr_lineno = (String[]) map.get("lineno");
		int lineno = 0;
		if (arr_lineno != null)
			lineno = arr_lineno.length;
		boolean hasBlank = false;
		boolean hasDuplicates = false;
		String duplicateItem = "";
		ArrayList listForSave = new ArrayList();
		Hashtable hashDup = new Hashtable(); 
		for (int i = 0; i < lineno; i++) {
			String[] function_code = (String[]) map.get("function_code" + i);
			String[] accessType_code = (String[]) map.get("accessTypes_code" + i);
			String[] accessType_desc = (String[]) map.get("accessTypes_description" + i);
			String[] function_desc = (String[]) map.get("function_description"+i);
				Secobjprivilege objNew = new Secobjprivilege();
				objNew.setObjectname_code(function_code[0]);
				objNew.setObjectname_desc(function_desc[0]);
				objNew.setRoleusergroup(roleName);
				//String[] accessType_code = (String[]) map.get("accessTypes_code" + i);
				if (accessType_code != null) {
					objNew.setPrivilege_code(accessType_code[0]);
					objNew.setPrivilege_desc(accessType_desc[0]);
				}
				objNew.setProviderNo(providerNo);
				objNew.setPriority(new Integer("0"));

				listForSave.add(objNew);
				if (function_code == null || function_code[0].length() == 0 || accessType_code ==null || accessType_code[0].length()==0)
				{
					hasBlank = true;
				}
				else if(hashDup.containsKey(objNew.getObjectname_code())) {
					hasDuplicates = true;
					duplicateItem = "Function: " + objNew.getObjectname_desc();
				}
				else
				{
					hashDup.put(objNew.getObjectname_code(), "duplicate detector");
				}
		}
		if(!(hasBlank || hasDuplicates)) {
			rolesManager.saveFunctions(secrole, listForSave, roleName);
			return true;
		}
		else
		{
			if(hasBlank) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed.emptyField",
						request.getContextPath()));
			}
			else
			{
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed.duplicateItem",
						request.getContextPath(),duplicateItem));
			}
			saveMessages(request,messages);	
			secroleForm.set("secobjprivilegeLst", listForSave);
			return false;
		}
	}
}