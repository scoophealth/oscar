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


package oscar.oscarSecurity.token;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.SecurityTokenDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.SecurityToken;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarSecurity.SecurityTokenManager;

public class StJoesTokenManager extends SecurityTokenManager {

	private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
    private SecUserRoleDao secUserRoleDao=(SecUserRoleDao)SpringUtils.getBean("secUserRoleDao");
    private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    
    
	@Override
	public void requestToken(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String key = request.getParameter("key");
		
		String storedKey = OscarProperties.getInstance().getProperty("security.token.key");
		
		if(key == null || storedKey == null || key.length()==0 || storedKey.length()==0) {
			response.getWriter().println("ERROR: no valid keys found");
			return;
		}
		
		if(!key.equals(storedKey)) {
			response.getWriter().println("ERROR: key does not match");
			return;
		}
		
		String expiryMins = OscarProperties.getInstance().getProperty("security.token.expiry","60");
		int expiry = 60;
		try {
			expiry = Integer.parseInt(expiryMins);
		} catch(NumberFormatException e) {
			MiscUtils.getLogger().error("error",e);
		}
		
		//create token
		SecurityToken st = new SecurityToken();
		st.setCreated(new Date());		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, expiry);
		st.setExpiry(cal.getTime());
		st.setToken(UUID.randomUUID().toString());
		st.setData("");
		st.setProviderNo(request.getParameter("providerNo"));
		
		//store the token
		SecurityTokenDao std = (SecurityTokenDao)SpringUtils.getBean("securityTokenDao");
		std.persist(st);
		
		//set the redirect to display the token back
		response.getWriter().println(st.getToken());
	}

	@Override
	public boolean handleToken(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		String token = request.getParameter("token");
		if(token == null) {
			token = (String)request.getAttribute("token");
		}
		
		SecurityTokenDao std = (SecurityTokenDao)SpringUtils.getBean("securityTokenDao");
		SecurityToken st = std.getByTokenAndExpiry(token,new Date());

		//token not valid
		if(st == null) {			
			MiscUtils.getLogger().warn("Invalid token - " + token);
			((HttpServletResponse)response).sendRedirect(httpRequest.getContextPath() + "/oscarFacesheet/token_error.jsp");
			return false;
		}
		//token is now valid.
		
		//check if this is an existing session
		if(httpRequest.getSession() != null && httpRequest.getSession().getAttribute("user") != null) {
			return true;
		}
		
		//create the session and log the provider in
		HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
            session = httpRequest.getSession(); // Create a new session for this user
        }
        
        //don't really need all this for what we're using this for, but tried to get everything in there.
        //be nice if this was in a common class.
        Integer facilityId = null;
        List<Integer> facilityIds = providerDao.getFacilityIds(st.getProviderNo());
        if(facilityIds.size()==0) {
        	List<Facility> facility = facilityDao.findAll(null);
        	facilityId = facility.get(0).getId();
        } else {
        	facilityId = facilityIds.get(0);
        }      
        Provider provider = providerManager.getProvider(st.getProviderNo());
        List<SecUserRole> userRoles = secUserRoleDao.getUserRoles(provider.getProviderNo());
        StringBuilder sb = new StringBuilder();
        for(SecUserRole role:userRoles) {
        	if(sb.length()>0)
        		sb.append(",");
        	sb.append(role);
        }
        
        session.setAttribute("oscarVariables", OscarProperties.getInstance());
        session.setAttribute("user", provider.getProviderNo());
        session.setAttribute("userfirstname", provider.getFirstName());
        session.setAttribute("userlastname", provider.getLastName());
        session.setAttribute("userrole", sb.toString());
        session.setAttribute("oscar_context_path", httpRequest.getContextPath());
        //session.setAttribute("expired_days", strAuth[5]);
        Facility facility=facilityDao.find(facilityId);
        httpRequest.getSession().setAttribute("currentFacility", facility);

        LoggedInInfo loggedInInfo=new LoggedInInfo();
		loggedInInfo.setCurrentFacility((Facility) session.getAttribute(SessionConstants.CURRENT_FACILITY));
		loggedInInfo.setLoggedInProvider((Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER));
		loggedInInfo.setLoggedInSecurity((Security) session.getAttribute(SessionConstants.LOGGED_IN_SECURITY));
		loggedInInfo.setInitiatingCode(httpRequest.getRequestURI());
		loggedInInfo.setLocale(httpRequest.getLocale());
		LoggedInInfo.setLoggedInInfoIntoSession(session, loggedInInfo);
		
		return true;
	}

}
