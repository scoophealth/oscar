/**
 * Copyright (c) 2008-2012 Indivica Inc.
 * 
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package oscar.eform.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * The purpose of this servlet is to allow a local process to access eform images.
 */
public final class EFormImageViewForPdfGenerationServlet extends HttpServlet {

	private static final Logger logger=MiscUtils.getLogger();
	
	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// ensure it's a local machine request... no one else should be calling this servlet.
		String remoteAddress=request.getRemoteAddr();
		logger.debug("EformPdfServlet request from : "+remoteAddress);
		
		if (!"127.0.0.1".equals(remoteAddress))
		{
			logger.warn("Unauthorised request made to EFormImageViewForPdfGenerationServlet from address : "+remoteAddress);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		
		request.setAttribute("prepareForFax", true);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/eform/displayImage.do");
		requestDispatcher.forward(request, response);
	}
}
