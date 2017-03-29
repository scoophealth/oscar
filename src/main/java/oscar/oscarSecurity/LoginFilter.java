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

package oscar.oscarSecurity;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.cookierevolver.CRFactory;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * @author Dennis Langdeau
 */
public class LoginFilter implements Filter {

	private static final Logger logger=MiscUtils.getLogger();

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
		"/forcepasswordreset.jsp",
		"/loginfailed.jsp",
		"/index.html",
		"/eformViewForPdfGenerationServlet",
		"/LabViewForPdfGenerationServlet",
		"/oscarFacesheet/token_error.jsp",
		"/ws/",
		"/EFormViewForPdfGenerationServlet",
		"/EFormSignatureViewForPdfGenerationServlet",
		"/EFormImageViewForPdfGenerationServlet",
		"/ProxyEformNotification",
		"/js/jquery-1.7.1.min.js",
		"/js/bootstap",
		"/js/global.js",
		"/css/bootstrap",
		"/myoscar_login_tester.jsp",
		"/myoscar_login_tester2.jsp"
	};
	
	private static final String[] EXEMPT_URLS_FOR_REQUEST_TIMEOUT = {
		"/images/",  
		"/login.do",
		"/logout.jsp",
		"/index.jsp",
		"/loginfailed.jsp",
		"/index.html",
		"/eformViewForPdfGenerationServlet",
		"/LabViewForPdfGenerationServlet",
		"/oscarFacesheet/token_error.jsp",
		"/ws/",
		"/EFormViewForPdfGenerationServlet",
		"/EFormSignatureViewForPdfGenerationServlet",
		"/EFormImageViewForPdfGenerationServlet",
		"/oscar/provider/providercontrol.jsp",
		"/oscar/js",
		"/oscar/provider/tabAlertsRefresh.jsp",
		"/oscar/SystemMessage.do",
		"/oscar/FacilityMessage.do",
		"/ProxyEformNotification",
		"/js/jquery-1.7.1.min.js",
		"/js/bootstrap",
		"/css/bootstrap"
	};
	
	private static final String[] EXEMPT_URLS_FOR_REQUEST_TIMEOUT_REDIRECT = {
		"/logout.jsp",
		"/index.jsp",
		"/loginfailed.jsp",
		"/index.html"
	};

	/*
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		logger.info("Starting Filter : "+getClass().getSimpleName());
		
		if (!CRHelper.isCRFrameworkEnabled()) {
			CRFactory.getConfig().setProperty("cr.disabled", "true");
		}
	}

	/*
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.debug("Entering LoginFilter.doFilter()");
		
		if (!CRHelper.isCRFrameworkEnabled()) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			String contextPath = httpRequest.getContextPath();
			String requestURI = httpRequest.getRequestURI();
			String InActivityLimitInMins = OscarProperties.getInstance().getProperty("INACTIVITY_LIMIT_MINS");
			
			SecurityTokenManager stm = SecurityTokenManager.getInstance();
			if(stm != null) {
				//token is being requested
				if(request.getParameter("request_token")!=null && request.getParameter("request_token").equals("true")) {
					stm.requestToken(httpRequest, httpResponse, chain);
					return;
				}
				
				//token is being sent..check if it's valid..this will set the "user" attribute in the session.
				if(request.getParameter("token")!=null || request.getAttribute("token")!=null) {
					boolean success = stm.handleToken(httpRequest, httpResponse, chain);
					if(!success) {
						return;
					}
				}
			}
			
			HttpSession session = httpRequest.getSession(false);
			if (session==null || session.getAttribute("user") == null) {
				
				

				/*
				 * If the requested resource is npt exempt then redirect to the logout page.
				 * 
				 * bug fix: removed root directory auto-exemption. If you want to have a resource
				 * be an exemption, you must explicitely add to EXEMPT_URLS array.
				 */
				if (!inListOfExemptions(requestURI, contextPath,EXEMPT_URLS)) {
					httpResponse.sendRedirect(contextPath + "/logout.jsp");
					return;
				}
			}else if(session!=null && InActivityLimitInMins != null){ //Tracking for last request time
				try{
					long minLimit = Long.parseLong(InActivityLimitInMins);
				
					Date lastRequestDate = (Date) session.getAttribute("last_request_time");
					Date thisRequestDate = new Date();	
					long timeSinceLastRequest = -1;
					if (lastRequestDate != null){ 
						long timeBeforeExpire = 60 * 1000 * minLimit; //TODO need to use this Also need to get it from a property
						long lastRequest = lastRequestDate.getTime();
						long thisRequest = thisRequestDate.getTime();
						timeSinceLastRequest = thisRequest - lastRequest;
						logger.debug("lastRequestDate.getTime() "+lastRequestDate.getTime()+" thisRequestDate.getTime() "+thisRequestDate.getTime()+" -- "+timeSinceLastRequest);
						if (timeSinceLastRequest > timeBeforeExpire && !inListOfExemptions(requestURI,contextPath,EXEMPT_URLS_FOR_REQUEST_TIMEOUT_REDIRECT )){
							httpResponse.sendRedirect(contextPath + "/logout.jsp");
							return;
						}
					}
					
				if(!inListOfExemptions(requestURI, contextPath,EXEMPT_URLS_FOR_REQUEST_TIMEOUT)) { 
					logger.debug("reseting timer list uri "+httpRequest.getRequestURI());
					session.setAttribute("last_request_time",thisRequestDate);
				}
				}catch(Exception e){
					logger.error("ERROR checking for last activity. Limit Activity :"+InActivityLimitInMins, e);
				}
			}
		}

		logger.debug("LoginFilter chainning");
		chain.doFilter(request, response);
	}

	boolean inListOfExemptions(String requestURI, String contextPath,String[] EXEMPT_URLS) {
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
