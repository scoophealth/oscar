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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public abstract class SecurityTokenManager {
	
	static SecurityTokenManager instance = null;
	
	public static SecurityTokenManager getInstance() {
		if(instance != null) {
			return instance;
		}
		
		String managerName = OscarProperties.getInstance().getProperty("security.token.manager");
		if(managerName != null) {
			try {
				instance = (SecurityTokenManager)Class.forName(managerName).newInstance();
			}catch(Exception e) {
				MiscUtils.getLogger().error("Unable to load token manager");
			}
		}
		
		return instance;
	}
	
	/**
	 * Set the "token" attribute in the request if successful.
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void requestToken(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
	
	/**
	 * Check token, do the login if successful.
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract boolean handleToken(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
		
}
