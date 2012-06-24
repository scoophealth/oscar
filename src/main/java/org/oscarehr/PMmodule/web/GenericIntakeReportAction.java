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

package org.oscarehr.PMmodule.web;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.util.MiscUtils;

public class GenericIntakeReportAction extends BaseGenericIntakeAction {

	private static Logger LOG = MiscUtils.getLogger();
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	// Forwards
	private static final String FORWARD_REPORT = "report";

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
		String intakeType = getType(request);
		Integer programId = getProgramId(request);
		Date startDate = getStartDate(request);
		Date endDate = getEndDate(request);
		boolean includePast = new Boolean(getIncludePast(request));

		String nodeId = getParameter(request,"nodeId");
		
		
		Map<String, SortedSet<ReportStatistic>> questionStatistics = genericIntakeManager.getQuestionStatistics(nodeId,intakeType, programId, startDate, endDate, includePast);
		
		request.setAttribute("intakeType", StringUtils.capitalize(intakeType));
		request.setAttribute("startDate", DateTimeFormatUtils.getStringFromDate(startDate, DATE_FORMAT));
		request.setAttribute("endDate", DateTimeFormatUtils.getStringFromDate(endDate, DATE_FORMAT));
		request.setAttribute("questionStatistics", questionStatistics);

		return mapping.findForward(FORWARD_REPORT);
	}

}
