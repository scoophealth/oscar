package org.oscarehr.survey.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MockOscarFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)baseRequest;
		
		HttpSession session = request.getSession();
		
		//for testing
		if(session.getAttribute("user") == null) {
			//log.warn("using test user");
			session.setAttribute("user","999998");
			session.setAttribute("userrole","doctor admin");
		}
		
    	chain.doFilter(baseRequest,baseResponse);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
