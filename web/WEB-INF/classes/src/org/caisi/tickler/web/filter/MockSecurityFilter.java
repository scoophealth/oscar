package org.caisi.tickler.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MockSecurityFilter implements Filter {

	private static Log log = LogFactory.getLog(MockSecurityFilter.class);

	   public void init(FilterConfig config) throws ServletException {
	        // TODO Auto-generated method stub
	    }
	    
	    public void destroy() {
	        // TODO Auto-generated method stub
	    }

	    public void doFilter(ServletRequest request, ServletResponse response,
	            FilterChain chain) throws IOException, ServletException {
	
	        HttpSession session =
	            ((HttpServletRequest)request).getSession(true);
	        if(session != null) {
	        	session.setAttribute("user","999998");
	        } else {
	        	log.error("session was null");
	        }
	        chain.doFilter(request,response);
	    }
}
