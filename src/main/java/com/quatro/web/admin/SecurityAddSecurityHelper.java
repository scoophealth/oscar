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
package com.quatro.web.admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.oscarehr.common.model.Security;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.MyDateFormat;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.login.PasswordHash;


/**
 * Helper class for securityaddsecurity.jsp page.
 */
public class SecurityAddSecurityHelper {
	

	private org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);

	/**
	 * Adds a security record (i.e. user login information) for the provider.
	 * <p/>
	 * Processing status is available as a "message" variable.
	 * 
	 * @param pageContext
	 * 		JSP page context
	 */
	public void addProvider(PageContext pageContext) {
		String message = process(pageContext);
		pageContext.setAttribute("message", message);
	}
	
	private String process(PageContext pageContext) {
		ServletRequest request = pageContext.getRequest();
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession((HttpServletRequest)request);
		
		StringBuilder sbTemp = new StringBuilder();
		MessageDigest md;
        try {
	        md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
        	MiscUtils.getLogger().error("Unable to get SHA message digest", e);
        	return "admin.securityaddsecurity.msgAdditionFailure";
        }
        
		byte[] btNewPasswd = md.digest(request.getParameter("password").getBytes());
		for (int i = 0; i < btNewPasswd.length; i++)
			sbTemp = sbTemp.append(btNewPasswd[i]);

		String hashedPassword = null;
		String hashedPin = null;
		
		try {
			hashedPassword = PasswordHash.createHash(request.getParameter("password"));
			hashedPin = PasswordHash.createHash(request.getParameter("pin"));
		} catch(Exception e) {
			MiscUtils.getLogger().error("Error with hashing passwords on this system!",e);
			return "admin.securityaddsecurity.msgAdditionFailure";
		}
		
		boolean isUserRecordAlreadyCreatedForProvider = securityManager.findByProviderNo(loggedInInfo, request.getParameter("provider_no"))!=null;
		if (isUserRecordAlreadyCreatedForProvider) return "admin.securityaddsecurity.msgLoginAlreadyExistsForProvider";

		boolean isUserAlreadyExists = securityManager.findByUserName(loggedInInfo, request.getParameter("user_name")).size() > 0;
		if (isUserAlreadyExists) return "admin.securityaddsecurity.msgAdditionFailureDuplicate";

		Security s = new Security();
		s.setUserName(request.getParameter("user_name"));
		s.setPassword(hashedPassword);
		s.setProviderNo(request.getParameter("provider_no"));
		s.setPin(hashedPin);
		s.setBExpireset(request.getParameter("b_ExpireSet") == null ? 0 : Integer.parseInt(request.getParameter("b_ExpireSet")));
		s.setDateExpiredate(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
		s.setBLocallockset(request.getParameter("b_LocalLockSet") == null ? 0 : Integer.parseInt(request.getParameter("b_LocalLockSet")));
		s.setBRemotelockset(request.getParameter("b_RemoteLockSet") == null ? 0 : Integer.parseInt(request.getParameter("b_RemoteLockSet")));
		s.setStorageVersion(Security.STORAGE_VERSION_2);
    	if (request.getParameter("forcePasswordReset") != null && request.getParameter("forcePasswordReset").equals("1")) {
    	    s.setForcePasswordReset(Boolean.TRUE);
    	} else {
    		s.setForcePasswordReset(Boolean.FALSE);  
        }
    	s.setPasswordUpdateDate(new java.util.Date());
    	s.setPinUpdateDate(new java.util.Date());
    	s.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		
    	securityManager.saveNewSecurityRecord(loggedInInfo, s);

		LogAction.addLog((String) pageContext.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_SECURITY, request.getParameter("user_name"), request.getRemoteAddr());

		// hurrah - it worked
		return "admin.securityaddsecurity.msgAdditionSuccess";
	}
}
