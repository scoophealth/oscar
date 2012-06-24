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

package org.oscarehr.common.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.Mortalities;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.common.model.ShelterPopulation;
import org.oscarehr.common.model.ShelterUsage;
import org.oscarehr.common.service.PopulationReportManager;

public class PopulationReportAction extends DispatchAction {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	// Forwards
	private static final String REPORT = "report";

	private PopulationReportManager populationReportManager;

	private static long lastDataRetrievedTime=0;
	private static Date currentDateTime = null;
	private static ShelterPopulation shelterPopulation = null;
	private static ShelterUsage shelterUsage = null;
	private static Mortalities mortalities = null;
	private static Map<String, ReportStatistic> majorMedicalConditions = null;
	private static Map<String, ReportStatistic> majorMentalIllnesses = null;
	private static Map<String, ReportStatistic> seriousMedicalConditions = null;
	private static Map<String, Map<String, String>> categoryCodeDescriptions = null;

	public void setPopulationReportManager(PopulationReportManager populationReportManager) {
	    this.populationReportManager = populationReportManager;
    }

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return report(mapping, form, request, response);
	}

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// simple caching mechanism (because this report is open to the public and we don't want the public smacking our server around)
		if (System.currentTimeMillis()-lastDataRetrievedTime>DateUtils.MILLIS_PER_HOUR)
		{
			lastDataRetrievedTime=System.currentTimeMillis();

			// get attributes
			currentDateTime = Calendar.getInstance().getTime();
			shelterPopulation = populationReportManager.getShelterPopulation();
			shelterUsage = populationReportManager.getShelterUsage();
			mortalities = populationReportManager.getMortalities();
			majorMedicalConditions = populationReportManager.getMajorMedicalConditions();
			majorMentalIllnesses = populationReportManager.getMajorMentalIllnesses();
			seriousMedicalConditions = populationReportManager.getSeriousMedicalConditions();
			categoryCodeDescriptions = populationReportManager.getCategoryCodeDescriptions();
		}

		// set attributes
		request.setAttribute("date", DateTimeFormatUtils.getStringFromDate(currentDateTime, DATE_FORMAT));
		request.setAttribute("time", DateTimeFormatUtils.getStringFromTime(currentDateTime));
		request.setAttribute("shelterPopulation", shelterPopulation);
		request.setAttribute("shelterUsage", shelterUsage);
		request.setAttribute("mortalities", mortalities);
		request.setAttribute("majorMedicalConditions", majorMedicalConditions);
		request.setAttribute("majorMentalIllnesses", majorMentalIllnesses);
		request.setAttribute("seriousMedicalConditions", seriousMedicalConditions);
		request.setAttribute("categoryCodeDescriptions", categoryCodeDescriptions);

		// forward to view page
		return mapping.findForward(REPORT);
	}

}
