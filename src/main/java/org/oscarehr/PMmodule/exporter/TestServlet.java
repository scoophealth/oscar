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

package org.oscarehr.PMmodule.exporter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * For Test
 */
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1471337270123160719L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
		
			AbstractIntakeExporter exporter = null;
			
			int fileno = Integer.parseInt(request.getParameter("file"));
			int clientId = 0;
			
			if(null != request.getParameter("c"))
				clientId = Integer.parseInt(request.getParameter("c"));
			
			int facilityId = Integer.parseInt(request.getParameter("f"));
			
			switch(fileno) {
			case 1:
				exporter = (DATISAgencyInformation)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterAgencyInformation");
				break;
			case 2:
				exporter = (DATISListOfPrograms)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterListOfPrograms");
				break;
			case 3:
				exporter = (DATISMain)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterMain");
				break;
			case 4:
				exporter = (DATISProgramInformation)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterProgramInformation");
				break;
			case 5:
				exporter = (DATISGamingForm)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterGamblingForm");
				break;
			case 6:
				exporter = (DATISNonClientService)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterNonClientService");
				break;
			default:
				exporter = (DATISAgencyInformation)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterAgencyInformation");
				break;
			}
			
			if(clientId != 0) {
				List<Integer> clients = new ArrayList<Integer>();
				clients.add(clientId);
				exporter.setClients(clients);
			}
			
			exporter.setFacilityId(facilityId);
			response.getWriter().print(exporter.export());
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request, response);
	}
}
