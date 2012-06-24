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
