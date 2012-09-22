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


package org.oscarehr.web.eform;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * The purpose of this servlet is to allow a local process to convert an html page into a pdf file in a manner similar to viewing a pdf with a browser and selecting print to file
 */
public final class EformViewForPdfGenerationServlet extends HttpServlet {

	private static final Logger logger=MiscUtils.getLogger();
	
	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// ensure it's a local machine request... no one else should be calling this servlet.
		String remoteAddress=request.getRemoteAddr();
		logger.debug("EformPdfServlet request from : "+remoteAddress);
		if (!"127.0.0.1".equals(remoteAddress))
		{
			logger.warn("Unauthorised request made to EformPdfServlet from address : "+remoteAddress);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		// https://127.0.0.1:8443/oscar/eform/efmshowform_data.jsp?fdid=2&parentAjaxId=eforms
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/eform/efmshowform_data.jsp");
		requestDispatcher.forward(request, response);
	}
}
