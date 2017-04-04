package org.oscarehr.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.login.PasswordHash;

public class CreateQuickUserAction extends Action {

	ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
	SecUserRoleDao secUserRoleDao = SpringUtils.getBean(SecUserRoleDao.class);
	ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		CreateQuickUserBean userForm = (CreateQuickUserBean)form;
		
		//check that the username exists
		boolean isUserAlreadyExists = securityManager.findByUserName(loggedInInfo, request.getParameter("user_name")).size() > 0;
		if (isUserAlreadyExists) {
			MiscUtils.getLogger().warn("admin.securityaddsecurity.msgAdditionFailureDuplicate");
			request.setAttribute("error", "Username already exists");
			return mapping.findForward("failure");
		}
		
		
		Provider p = new Provider();
		p.setProviderNo(providerManager.suggestProviderNo());
		p.setProviderType("doctor");
		p.setLastName(userForm.getLastName());
		p.setFirstName(userForm.getFirstName());
		p.setSpecialty("");
		p.setTeam("");
		p.setSex(userForm.getGender());
		p.setDob(null);
		p.setAddress("");
		p.setPhone("");
		p.setWorkPhone("");
		p.setOhipNo("");
		p.setRmaNo("");
		p.setBillingNo("");
		p.setHsoNo("");
		p.setStatus("1");
		p.setComments("");
		p.setProviderActivity("");
		p.setPractitionerNo("");
		p.setEmail("");
		p.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		p.setLastUpdateDate(new Date());
		
		try {
			providerManager.saveProvider(loggedInInfo,p);
			LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), LogConst.ADD, "adminAddQuickUser", p.getProviderNo(), request.getRemoteAddr());
		}catch(Exception e) {
			return mapping.findForward("failure");
		}

		
		StringBuilder sbTemp = new StringBuilder();
		MessageDigest md;
        try {
	        md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
        	MiscUtils.getLogger().error("Unable to get SHA message digest", e);
        	return mapping.findForward("failure");
        }
        
		byte[] btNewPasswd = md.digest(userForm.getPassword().getBytes());
		for (int i = 0; i < btNewPasswd.length; i++)
			sbTemp = sbTemp.append(btNewPasswd[i]);

		String hashedPassword = null;
		String hashedPin = null;
		
		try {
			hashedPassword = PasswordHash.createHash(userForm.getPassword());
			hashedPin = PasswordHash.createHash(userForm.getPin());
		} catch(Exception e) {
			MiscUtils.getLogger().error("Error with hashing passwords on this system!",e);
			return mapping.findForward("failure");
		}
		
		boolean isUserRecordAlreadyCreatedForProvider = securityManager.findByProviderNo(loggedInInfo, p.getProviderNo())!=null;
		if (isUserRecordAlreadyCreatedForProvider) {
			MiscUtils.getLogger().warn("admin.securityaddsecurity.msgLoginAlreadyExistsForProvider");
			return mapping.findForward("failure");
		}

		


		Security s = new Security();
		s.setUserName(userForm.getUsername());
		s.setPassword(hashedPassword);
		s.setProviderNo(p.getProviderNo());
		s.setPin(hashedPin);
		s.setBExpireset(0);
		s.setBLocallockset(1);
		s.setBRemotelockset(0);
		s.setForcePasswordReset(false);
		s.setStorageVersion(Security.STORAGE_VERSION_2);
		s.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		s.setLastUpdateDate(new Date());
		
		securityManager.saveNewSecurityRecord(loggedInInfo, s);
		
		LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_SECURITY, userForm.getUsername(), request.getRemoteAddr());

		//assign roles
		List<SecUserRole> secUserRoles = secUserRoleDao.getUserRoles(userForm.getCopyProviderNo());
		for(SecUserRole i:secUserRoles) {
			SecUserRole sur = new SecUserRole();
			sur.setActive(true);
			sur.setLastUpdateDate(new Date());
			sur.setProviderNo(p.getProviderNo());
			sur.setRoleName(i.getRoleName());
			secUserRoleDao.save(sur);
		}
		
		//assign program roles
		List<ProgramProvider> ppList = programManager.getProgramProvidersByProvider(userForm.getCopyProviderNo());
		for(ProgramProvider i:ppList) {
			ProgramProvider pp = new ProgramProvider();
			pp.setLastUpdateDate(new Date());
			pp.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
			pp.setProgramId(i.getProgramId());
			pp.setProviderNo(p.getProviderNo());
			pp.setRoleId(i.getRoleId());
			programManager.saveProgramProvider(loggedInInfo, pp);
		}
		
		
		
		return mapping.findForward("success");
	}
}
