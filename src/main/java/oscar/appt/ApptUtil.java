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

package oscar.appt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.common.model.Site;

/**
 * This class contains Appointment related presentation layer helper methods.
 * 
 * @author Eugene Petruhin
 * 
 */
public class ApptUtil {

	private static final String SESSION_APPT_BEAN = "apptBean";

	public static void copyAppointmentIntoSession(HttpServletRequest request) {
		ApptData obj = new ApptData();
		obj.setAppointment_date(request.getParameter("appointment_date"));
		obj.setStart_time(request.getParameter("start_time"));
		obj.setEnd_time(request.getParameter("end_time"));
		obj.setName(request.getParameter("keyword"));
		obj.setDemographic_no(request.getParameter("demographic_no"));
		obj.setNotes(request.getParameter("notes"));
		obj.setReasonCode(request.getParameter("reasonCode"));
		obj.setReason(request.getParameter("reason"));
		obj.setLocation(request.getParameter("location"));
		obj.setResources(request.getParameter("resources"));
		obj.setType(request.getParameter("type"));
		obj.setStyle(request.getParameter("style"));
		obj.setBilling(request.getParameter("billing"));
		obj.setStatus(request.getParameter("status"));
		obj.setRemarks(request.getParameter("remarks"));
		obj.setDuration(request.getParameter("duration"));
		obj.setChart_no(request.getParameter("chart_no"));
		obj.setUrgency(request.getParameter("urgency"));
		obj.setProgram(request.getParameter("program"));
		// set up session bean
		request.getSession().setAttribute(SESSION_APPT_BEAN, obj);
	}

	public static ApptData getAppointmentFromSession(HttpServletRequest request) {
		return (ApptData) request.getSession().getAttribute(SESSION_APPT_BEAN);
	}

	public static String getColorFromLocation(String site, String colo, String loca) {
		String ret = "white";
		String[] s = site.split("\\|");
		String[] c = colo.split("\\|");
		for (int i = 0; i < s.length; i++) {
			if (s[i].startsWith(loca)) {
				ret = c[i];
				break;
			}
		}
		return ret;
	}
	
	public static String getColorFromLocation(List<Site> sites, String siteName) {
		for (Site s:sites) {
			if (s.getName().equals(siteName))
				return s.getBgColor();
		}
		return "white";
	}
	
	public static String getShortNameFromLocation(List<Site> sites, String siteName) {
		for (Site s:sites) {
			if (s.getName().equals(siteName))
				return s.getShortName();
		}
		return "";
	}
	
	public static Site getSiteFromName(List<Site> sites, String siteName) {
		for (Site s:sites) {
			if (s.getName().equals(siteName))
				return s;
		}
		return null;
	}
}
