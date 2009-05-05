/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * <OSCAR TEAM>
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarSecurity;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.cookierevolver.CRFactory;

/**
 * @author Dennis Langdeau
 */
public class LoginFilter implements Filter {

	private static final String[] EXEMPT_URLS = { 
		"/images/", 
		"/signature_pad/", 
		"/lab/CMLlabUpload.do", 
		"/lab/newLabUpload.do", 
		"/lab/CA/ON/uploadComplete.jsp",
		"/PopulationReport.do",
		"/login.do",
		"/logout.jsp",
		"/index.jsp",
		"/loginfailed.jsp",
		"/index.html"
	};

	/*
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		if (!CRHelper.isCRFrameworkEnabled()) {
			CRFactory.getConfig().setProperty("cr.disabled", "true");
		}
	}

	/*
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!CRHelper.isCRFrameworkEnabled()) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			if (httpRequest.getSession().getAttribute("user") == null) {
				String requestURI = httpRequest.getRequestURI();
				String contextPath = httpRequest.getContextPath();

				/*
				 * If the requested resource is npt exempt then redirect to the logout page.
				 * 
				 * bug fix: removed root directory auto-exemption. If you want to have a resource
				 * be an exemption, you must explicitely add to EXEMPT_URLS array.
				 */
				if (!inListOfExemptions(requestURI, contextPath)) {
					httpResponse.sendRedirect(contextPath + "/logout.jsp");
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	boolean inListOfExemptions(String requestURI, String contextPath) {
		for (String exemptUrl : EXEMPT_URLS) {
	        if (requestURI.startsWith(contextPath + exemptUrl)) {
	        	return true;
	        }
        }
		
		return false;
	}

	/*
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {}

}