package org.caisi.tickler.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityFilter  implements Filter {

	private static Log log = LogFactory.getLog(SecurityFilter.class);

    public void init(FilterConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }
    
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

    	HttpSession session =
            ((HttpServletRequest)request).getSession(false);

    	if (((HttpServletRequest) request).getServletPath().endsWith("Auth.jsp")) {
    		   chain.doFilter(request, response);
    		   return;
    		}
    	
    	if(session == null) {
    		log.debug("session has expired..");
    		RequestDispatcher rd = request.getRequestDispatcher("/Auth.jsp");
        	rd.forward(request,response);
        	return;
    	}
    	
        String providerNo = (String)session.getAttribute("user");
        
        if(providerNo == null) {
        	log.debug("not logged in");
        	RequestDispatcher rd = request.getRequestDispatcher("/Auth.jsp");
        	rd.forward(request,response);
        } else {
        	chain.doFilter(request,response);
        }
    }
}
