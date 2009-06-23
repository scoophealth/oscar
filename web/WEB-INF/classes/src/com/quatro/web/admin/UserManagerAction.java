/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.web.admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
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
import com.quatro.model.security.NoAccessException;
import com.quatro.model.security.SecProvider;
import com.quatro.model.security.Security;
import com.quatro.model.security.Secuserrole;
import com.quatro.service.LookupManager;
import com.quatro.service.ORGManager;
import com.quatro.service.security.RolesManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.service.security.UsersManager;
import com.quatro.model.LookupCodeValue;
import com.quatro.service.LookupManager;
import com.quatro.util.*;
public class UserManagerAction extends BaseAdminAction {

	private LogManager logManager;


	private UsersManager usersManager;
	private LookupManager lookupManager;

	private MessageDigest md;
	private String PWD = "**********";
	private String PIN = "****";

	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}


	public void setUsersManager(UsersManager usersManager) {
		this.usersManager = usersManager;
	}
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}


	public ActionForward profile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_USER);
	
			String providerNo = request.getParameter("providerNo");
			org.apache.struts.validator.DynaValidatorForm secForm = (org.apache.struts.validator.DynaValidatorForm) form;
			ArrayList profilelist = new ArrayList();
			
			DynaActionForm secuserForm = (DynaActionForm) form;
			
			SecProvider provider = usersManager
				.getProviderByProviderNo(providerNo);
	
			if (provider != null) {
				secuserForm.set("providerNo", providerNo);
				secuserForm.set("firstName", provider.getFirstName());
				secuserForm.set("lastName", provider.getLastName());
				secuserForm.set("init", provider.getInit());
				secuserForm.set("title", provider.getTitle());
				secuserForm.set("jobTitle", provider.getJobTitle());
				secuserForm.set("email", provider.getEmail());
			}
			
			Security user;
			List ulist = usersManager.getUserByProviderNo(providerNo);
			if (ulist != null && ulist.size() > 0) {
				user = (Security) ulist.get(0);
	
				secuserForm.set("securityNo", user.getSecurityNo());
				secuserForm.set("userName", user.getUserName());		
				request.setAttribute("userForEdit", user);
			}
			
			List list = usersManager.getProfile(providerNo);
	
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] tmp = (Object[]) list.get(i);
	
					Secuserrole sur = new Secuserrole();
					if (tmp != null) {
						sur.setProviderNo((String) tmp[0]);
						sur.setRoleName((String) tmp[1]);
						sur.setOrgcd_desc((String) tmp[2]);
						sur.setUserName((String) tmp[3]);
						sur.setOrgcd((String) tmp[4]);
						profilelist.add(sur);
					}
	
				}
			}
	
			request.setAttribute("profilelist", profilelist);
			secForm.set("secUserRoleLst", profilelist);
	//		logManager.log("read", "full secuserroles list", "", request);
	
			String scrollPosition = (String) request.getParameter("scrollPosition");
			if(null != scrollPosition) {
				request.setAttribute("scrPos", scrollPosition);
			}else{
				request.setAttribute("scrPos", "0");
			}
			boolean isReadOnly =isReadOnly(request, KeyConstants.FUN_ADMIN_USER);
			if(isReadOnly) request.setAttribute("isReadOnly", Boolean.valueOf(isReadOnly));
			return mapping.findForward("addRoles");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}	

	public ActionForward preNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			super.getAccess(request, KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_WRITE);
			List titleLst = lookupManager.LoadCodeList("TLT", true, null, null);
	        request.setAttribute("titleLst", titleLst);
	        
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
		
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_USER);
			DynaActionForm secuserForm = (DynaActionForm) form;
			String providerNo = request.getParameter("providerNo");
	
			if (isCancelled(request)) {
				return mapping.findForward("list");
			}
	
			if (providerNo != null) {
	
				SecProvider provider = usersManager.getProviderByProviderNo(providerNo);
	
				if (provider != null) {
					secuserForm.set("providerNo", providerNo);
					secuserForm.set("firstName", provider.getFirstName());
					secuserForm.set("lastName", provider.getLastName());
					secuserForm.set("init", provider.getInit());
					secuserForm.set("title", provider.getTitle());
					secuserForm.set("jobTitle", provider.getJobTitle());
					secuserForm.set("email", provider.getEmail());
					secuserForm.set("password", PWD);
					secuserForm.set("confirmPassword", PWD);
					secuserForm.set("pin", PIN);
					secuserForm.set("confirmPin", PIN);
					
					
					String isChecked = provider.getStatus();
					if (isChecked != null && isChecked.equals("1"))
						secuserForm.set("status", "on");
	
					request.setAttribute("userForEdit", provider);
	
					Security user;
					List list = usersManager.getUserByProviderNo(providerNo);
					if (list != null && list.size() > 0) {
						user = (Security) list.get(0);
	
						secuserForm.set("securityNo", user.getSecurityNo());
						secuserForm.set("userName", user.getUserName());
						boolean isReadOnly =isReadOnly(request, KeyConstants.FUN_ADMIN_USER);
						if(isReadOnly) request.setAttribute("isReadOnly", Boolean.valueOf(isReadOnly));
						request.setAttribute("userForEdit", user);
					}				
				}
	
			} else {
				return mapping.findForward("list");
			}
			
			List titleLst = lookupManager.LoadCodeList("TLT", true, null, null);
	        request.setAttribute("titleLst", titleLst);
	        
			return mapping.findForward("edit");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}

	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException {
		List titleLst = lookupManager.LoadCodeList("TLT", true, null, null);
        request.setAttribute("titleLst", titleLst);
        request.setAttribute("scrPos", request.getParameter("scrollPosition"));
		ActionMessages messages = new ActionMessages();

		DynaActionForm secuserForm = (DynaActionForm) form;

		SecProvider provider = null;
		Security user = null;
		String providerNo = (String) secuserForm.get("providerNo");
		if (providerNo != null && providerNo.length() > 0) {
			super.getAccess(request,KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_UPDATE);
			provider = usersManager.getProviderByProviderNo(providerNo);
			List userList = usersManager.getUserByProviderNo(providerNo);
			if (userList != null && userList.size() > 0)
				user = (Security) userList.get(0);
		} else
		{
			super.getAccess(request,KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_WRITE);
			provider = new SecProvider();
		}
		provider.setFirstName((String) secuserForm.get("firstName"));
		provider.setLastName((String) secuserForm.get("lastName"));
		provider.setInit((String) secuserForm.get("init"));
		provider.setTitle((String) secuserForm.get("title"));
		provider.setJobTitle((String) secuserForm.get("jobTitle"));
		provider.setEmail((String) secuserForm.get("email"));
		
		/* added by marc - this can't be null */
		provider.setProviderType("quatro");
		provider.setSpecialty("");
		provider.setSex("");
		
		Map map = request.getParameterMap();
		String[] isChecked = (String[]) map.get("status");
		if (isChecked != null)
			provider.setStatus("1");
		else
			provider.setStatus("0");

		if (user == null) {
			user = new Security();
			user.setBLocallockset(new Integer(1));
			user.setBRemotelockset(new Integer(1));
			user.setBExpireset(new Integer(1));
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date aDate = sdf.parse("01/01/2999");
				user.setDateExpiredate(aDate);
			} catch (Exception e) {

			}

		}
		user.setUserName(((String) secuserForm.get("userName")).toLowerCase());
		secuserForm.set("userName",user.getUserName());
		String password = (String) secuserForm.get("password");
		String cpass = (String) secuserForm.get("confirmPassword");
		String pin = (String) secuserForm.get("pin");
		String cpin = (String) secuserForm.get("confirmPin");
		//String pin = "123456";
		//String cpin = "123456";

		if (password.equals(cpass) && pin.equals(cpin)) {
			if (!password.equals(PWD)) {
				try {
					password = encryptPassword(password);
				} catch (NoSuchAlgorithmException foo) {
					logManager.log("new user",
							"NoSuchAlgorithmException - SHA", "", request);
				}
				user.setPassword(password);
			}
			if (!pin.equals(PIN))
				user.setPin(/*oscar.Misc.encryptPIN(pin)*/pin);
		} else {
			messages
					.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"error.newuser.passwordNotMatch", request
									.getContextPath()));
			saveMessages(request, messages);
			return mapping.findForward("edit");
		}

		try {
			usersManager.save(provider, user);
			request.setAttribute("userForEdit", user);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"message.save.success", request.getContextPath()));
			saveMessages(request, messages);
			secuserForm.set("providerNo", provider.getProviderNo());
			//return addRole(mapping, form, request, response);
			//return mapping.findForward("edit");
		} catch (Exception e) {
			String msg = e.getMessage();
			int i = msg
					.indexOf("org.hibernate.exception.ConstraintViolationException");// unique constraint
			if (i >= 0) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.user.exist", request.getContextPath(), user
								.getUserName()));
				saveMessages(request, messages);
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.newuser.failed", request.getContextPath()));
				saveMessages(request, messages);

			}
			//return mapping.findForward("edit");
		}
		
        return mapping.findForward("edit");
        
	}
	// all users can change their own password
	public ActionForward changePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException {

		DynaActionForm secuserForm = (DynaActionForm) form;

		String providerNo =(String)request.getSession(true).getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
		secuserForm.set("providerNo", providerNo);
		
        return mapping.findForward("changePassword");
        
	}
	
	public ActionForward savePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException {
		
		ActionMessages messages = new ActionMessages();

		DynaActionForm secuserForm = (DynaActionForm) form;

		//SecProvider provider = null;
		
		String providerNo = (String) secuserForm.get("providerNo");
		//provider = usersManager.getProviderByProviderNo(providerNo);
		List userList = usersManager.getUserByProviderNo(providerNo);
		Security user = (Security) userList.get(0);
		
				
		String oldPassword = (String) secuserForm.get("oldPassword");
		String password = (String) secuserForm.get("password");
		String cpass = (String) secuserForm.get("confirmPassword");
		
		try {
			oldPassword = encryptPassword(oldPassword);
		} catch (NoSuchAlgorithmException foo) {
			logManager.log("new user",
					"NoSuchAlgorithmException - SHA", "", request);
		}
		String existPwd = user.getPassword();
		
		if(!oldPassword.equals(existPwd)){
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.notMatch", request.getContextPath()));
			saveMessages(request, messages);
			return mapping.findForward("changePassword");
		}
		if (password.equals(cpass)) {
			if (!password.equals(PWD)) {
				try {
					password = encryptPassword(password);
				} catch (NoSuchAlgorithmException foo) {
					logManager.log("new user",
							"NoSuchAlgorithmException - SHA", "", request);
				}
		
				user.setPassword(password);
			}else{
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.matchMask", request.getContextPath(), PWD));
				saveMessages(request, messages);
				return mapping.findForward("changePassword");
			}
			
		} else {
			messages
					.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"error.newuser.passwordNotMatch", request
									.getContextPath()));
			saveMessages(request, messages);
			return mapping.findForward("changePassword");
		}

		try {
			usersManager.updateUser(user);
			
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"message.save.success", request.getContextPath()));
			saveMessages(request, messages);
			secuserForm.set("providerNo", providerNo);
			
		} catch (Exception e) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.save.failed", request.getContextPath()));
				saveMessages(request, messages);
		}
		
        return mapping.findForward("changePassword");
        
	}
	
	private String encryptPassword(String password) throws NoSuchAlgorithmException{
		StringBuffer sbTemp = new StringBuffer();
		byte[] pwd = password.getBytes();
		
		md = MessageDigest.getInstance("SHA");
		
		byte[] btTypeInPasswd = md.digest(pwd);
		for (int i = 0; i < btTypeInPasswd.length; i++)
			sbTemp = sbTemp.append(btTypeInPasswd[i]);
		password = sbTemp.toString();

		return password;
	}
	public ActionForward addRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException{
		
		super.getAccess(request,KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_WRITE);

		DynaActionForm secuserForm = (DynaActionForm) form;
		String providerNo = request.getParameter("providerNo");

		if (isCancelled(request)) {
			return mapping.findForward("list");
		}

		SecProvider provider = usersManager
				.getProviderByProviderNo(providerNo);

		if (provider != null) {
			secuserForm.set("providerNo", providerNo);
			secuserForm.set("firstName", provider.getFirstName());
			secuserForm.set("lastName", provider.getLastName());
			secuserForm.set("init", provider.getInit());
			secuserForm.set("title", provider.getTitle());
			secuserForm.set("jobTitle", provider.getJobTitle());
			secuserForm.set("email", provider.getEmail());

			String isChecked = provider.getStatus();
			if (isChecked != null)
				secuserForm.set("status", "on");

			
			Security user;
			List list = usersManager.getUserByProviderNo(providerNo);
			if (list != null && list.size() > 0) {
				user = (Security) list.get(0);

				secuserForm.set("securityNo", user.getSecurityNo());
				secuserForm.set("userName", user.getUserName());

				request.setAttribute("userForEdit", user);
			}
		}

		
		changeRoleLstTable(2, secuserForm, request);

		String scrollPosition = (String) request.getParameter("scrollPosition");
		if(!"".equals(scrollPosition)) {
			request.setAttribute("scrPos", String.valueOf(Integer.valueOf(scrollPosition).intValue()+ 50));
		}else{
			request.setAttribute("scrPos", "0");
		}
		request.setAttribute("pageChanged", "1");
		return mapping.findForward("addRoles");

	}

	public ActionForward removeRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException {

		super.getAccess(request,KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_WRITE);
		
		DynaActionForm secroleForm = (DynaActionForm) form;
		changeRoleLstTable(1, secroleForm, request);

		String scrollPosition = (String) request.getParameter("scrollPosition");
		if(null != scrollPosition) {
			request.setAttribute("scrPos", scrollPosition);
		}else{
			request.setAttribute("scrPos", "0");
		}
		request.setAttribute("pageChanged", "1");
		return mapping.findForward("addRoles");

	}

	private void changeRoleLstTable(int operationType, DynaActionForm myForm,
			HttpServletRequest request) {
		
		ArrayList secUserRoleLst = new ArrayList();
		
		switch (operationType) {
		
		case 1: // remove
			secUserRoleLst = (ArrayList)getRowList(request, myForm, 1);
			break;
			
		case 2: // add
			secUserRoleLst = (ArrayList)getRowList(request, myForm, 2);
			
			Secuserrole objNew2 = new Secuserrole();
			
			secUserRoleLst.add(objNew2);
			
			break;

		}
		myForm.set("secUserRoleLst", secUserRoleLst);
		
		request.getSession(true).setAttribute("secUserRoleLst",	secUserRoleLst);
	}

	private List getRowList(HttpServletRequest request, ActionForm form, int operationType){
		ArrayList secUserRoleLst = new ArrayList();
		
		DynaActionForm secuserForm = (DynaActionForm) form;
		String providerNo = (String) secuserForm.get("providerNo");

		Map map = request.getParameterMap();
		String[] arr_lineno = (String[]) map.get("lineno");
		int lineno = 0;
		if (arr_lineno != null)
			lineno = arr_lineno.length;
		
		for (int i = 0; i < lineno; i++) {
			String[] isChecked = (String[]) map.get("p" + i);
			if ((operationType == 1 && isChecked == null) || operationType != 1) {
				Secuserrole objNew = new Secuserrole();
				
				String[] org_code = (String[]) map.get("org_code" + i);
				String[] org_description = (String[]) map.get("org_description" + i);
				String[] role_code = (String[]) map.get("role_code" + i);
				String[] role_description = (String[]) map.get("role_description" + i);
		
				if (org_code != null)
					objNew.setOrgcd(org_code[0]);
				if (org_description != null)
					objNew.setOrgcd_desc(org_description[0]);
				if (role_code != null)
					objNew.setRoleName(role_code[0]);
				if (role_description != null)
					objNew.setRoleName_desc(role_description[0]);
				
				objNew.setProviderNo(providerNo);
				objNew.setActiveyn(new Integer("1"));
		
				secUserRoleLst.add(objNew);
			}
			
		}
		return secUserRoleLst;
	}
	
	public ActionForward saveRoles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws NoAccessException {

		super.getAccess(request,KeyConstants.FUN_ADMIN_USER,KeyConstants.ACCESS_WRITE);

		DynaActionForm secuserForm = (DynaActionForm) form;
		String providerNo = (String) secuserForm.get("providerNo");
		ActionMessages messages = new ActionMessages();
		List secUserRoleLst = getRowList(request, form, 0);
		Hashtable hsforSave = new Hashtable();
        request.setAttribute("scrPos", request.getParameter("scrollPosition"));
		
		Iterator it = secUserRoleLst.iterator();
		boolean hasDuplicates = false;
		boolean hasBlanks = false;
		String duplicateItem = "";
		try {
			while(it.hasNext()){
				Secuserrole tmp = (Secuserrole)it.next();
				if(Utility.IsEmpty(tmp.getOrgcd()) || Utility.IsEmpty(tmp.getRoleName()))
				{
					hasBlanks = true;
					break;
				}
				if(hsforSave.containsKey(tmp.getOrgcd() + tmp.getRoleName()))
				{
					hasDuplicates = true;
					duplicateItem="Org: " + tmp.getOrgcd() + " Role: " + tmp.getRoleName(); 
					break;
				}
				else
				{
					hsforSave.put(tmp.getOrgcd() + tmp.getRoleName(), "duplicate detector");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			// duplicate detected;
		}
		if(!(hasBlanks || hasDuplicates)){
			try{
				usersManager.saveRolesToUser(secUserRoleLst, providerNo);
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.save.success",
				request.getContextPath()));
				saveMessages(request,messages);			
				request.setAttribute("pageChanged", "");
			}catch(Exception e){
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed",
				request.getContextPath()));
				saveMessages(request,messages);				
			}
		}else{
			if(hasBlanks) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed.emptyField",
						request.getContextPath()));
			}
			else
			{
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.save.failed.duplicateItem",
						request.getContextPath(),duplicateItem));
			}
			saveMessages(request,messages);	
			secuserForm.set("secUserRoleLst", secUserRoleLst);
			return mapping.findForward("profile");
		}
		
		return profile(mapping,form,request,response);

	}

}
