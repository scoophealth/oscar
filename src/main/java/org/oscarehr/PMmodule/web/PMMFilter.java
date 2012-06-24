/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.OscarSecurityManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 */
public class PMMFilter implements Filter {

	private static Logger logger = MiscUtils.getLogger();

	private AgencyManager agencyManager;
	private OscarSecurityManager oscarSecurityManager;
	private ProviderManager providerManager;
	private FilterConfig config;

	private void setProviderManager() {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

		agencyManager = (AgencyManager) wac.getBean("agencyManager");
		oscarSecurityManager = (OscarSecurityManager) wac.getBean("oscarSecurityManager");
		providerManager = (ProviderManager) wac.getBean("providerManager");
	}

	public void init(FilterConfig config) throws ServletException {
		logger.info("Starting Filter : "+getClass().getSimpleName());
		this.config = config;
	}

	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) baseRequest;
		HttpSession session = request.getSession();
		
		setProviderManager();

		String oscarUser = (String) session.getAttribute("user");
		if (oscarUser == null || oscarUser.length() == 0) {
			logger.info("Not logged in!");
			chain.doFilter(baseRequest, baseResponse);

            return;
        }
		
		session.setAttribute("program_domain", providerManager.getProgramDomain(oscarUser));

		if (session.getAttribute("pmm_admin") == null) {
			logger.debug("setting session variable: pmm_admin");
			session.setAttribute("pmm_admin", new Boolean(oscarSecurityManager.hasAdminRole(oscarUser)));
		}
		
/* If the provider didn't have the role 'admin', he can still have the access to the administration links(eg. Add Program) on PMM.
 * Each link should be separately configurable under the role rights object screen.
 *

		if (request.getRequestURI().indexOf("ProgramManager.do") != -1 && ((String) session.getAttribute("userrole")).indexOf("admin") == -1) {
			RequestDispatcher rd = baseRequest.getRequestDispatcher("/common/auth.jsp");
			rd.forward(baseRequest, baseResponse);
			return;
		}
*/
		// set local agency
		if (request.getSession().getServletContext().getAttribute("agency") == null) {
			Agency agency = agencyManager.getLocalAgency();
			request.getSession().getServletContext().setAttribute("agency", agency);
			Agency.setLocalAgency(agency);
		}


		chain.doFilter(baseRequest, baseResponse);
	}

	public void destroy() {}

}
