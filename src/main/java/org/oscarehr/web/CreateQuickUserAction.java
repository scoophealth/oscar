/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.web;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.dao.FavoritesDao;
import org.oscarehr.common.model.Favorites;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.login.PasswordHash;

public class CreateQuickUserAction extends DispatchAction {

	ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
	SecUserRoleDao secUserRoleDao = SpringUtils.getBean(SecUserRoleDao.class);
	ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
	SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	FavoritesDao favoritesDao = SpringUtils.getBean(FavoritesDao.class);
	
	private String generateBillingNo() {
		
		String val = null;
			
		do {
			Random rand = new Random();
			long  n = rand.nextInt(9999999) + 1000000;
			val = String.valueOf(n);
		} while(providerDao.getProviderByBillingNo(val).size()>0);
		return val;
	}
	
	private String generateThirdPartyBillingNo() {
		String val = null;
		
		do {
			Random rand = new Random();
			long  n = rand.nextInt(9999999) + 1000000;
			val = String.valueOf(n);
		} while(providerDao.getProviderByThirdPartyBillingNo(val).size()>0);
		return val;
	}
	
	
	public ActionForward checkUsername(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
		
		String userName = request.getParameter("user_name");
		
		JSONObject resp = new JSONObject();
		
		if(userName != null) {
			boolean isUserAlreadyExists = securityManager.findByUserName(LoggedInInfo.getLoggedInInfoFromSession(request), userName).size() > 0;
			if (isUserAlreadyExists) {
				resp.put("usernameExists", true);
			} else {
				resp.put("usernameExists", false);
			}
			
		} else {
			resp.put("error", true);
			resp.put("error.message", "Must pass user_name as a parameter");
		}
		
		resp.write(response.getWriter());
		
		return null;
	}
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
		
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
		p.setBillingNo(userForm.getBillingNo());
		p.setRmaNo(userForm.getThirdPartyBillingNo());
		
		if(userForm.isGenerateBillingNo()) {
			p.setBillingNo(generateBillingNo());
		}
		if(userForm.isGenerateThirdPartyBillingNo()) {
			p.setRmaNo(generateThirdPartyBillingNo());
		}
		
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
		if(userForm.getForcePasswordReset() != null && "1".equals(userForm.getForcePasswordReset())) {
			s.setForcePasswordReset(true);
		}
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
		
		
		//copy drug favorites
		if(userForm.isIncludeDrugFavourites()) {
			List<Favorites> favorites = favoritesDao.findByProviderNo(userForm.getCopyProviderNo());
			for(Favorites from: favorites) {
				Favorites to = new Favorites();
				try {
					BeanUtils.copyProperties(to, from);
					to.setId(null);
					to.setProviderNo(p.getProviderNo());
					favoritesDao.persist(to);
				}catch(Exception e) {
					MiscUtils.getLogger().warn("Error copying favorite " + from.getId());
				}
			}
		}
		
		return mapping.findForward("success");
	}
}
