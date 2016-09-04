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
package org.oscarehr.dashboard.admin;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ExportResultsAction extends Action  {

	private static Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_tickler", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
		}
		
		String indicatorId = request.getParameter("indicatorId");
		String indicatorName = request.getParameter("indicatorName");		
		OutputStream outputStream = null;
		String csvFile = dashboardManager.exportDrilldownQueryResultsToCSV( loggedInInfo, Integer.parseInt( indicatorId ) );
		
		if( indicatorName == null || indicatorName.isEmpty() ) {
			indicatorName = "indicator_data-" + System.currentTimeMillis() + ".csv";
		} else {
			indicatorName = indicatorName + System.currentTimeMillis() + ".csv";
		}
		
		if( csvFile != null ) {
			
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition","attachment;filename=" + indicatorName );
	
			try {
				outputStream = response.getOutputStream();
				outputStream.write( csvFile.getBytes() );
			} catch (IOException e) {
				logger.error("Failed to export CSV file: " + indicatorName, e );
			} finally {
				if( outputStream != null ) {
					try {
						outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						logger.error("Failed to close output stream", e );
					}
				}
			}
		}
		
		return null;
	}

}
