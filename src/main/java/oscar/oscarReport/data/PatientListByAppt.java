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

package oscar.oscarReport.data;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class PatientListByAppt extends HttpServlet {


    private static final long serialVersionUID = 1L;

	/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("plain/text");
		response.setHeader("Content-disposition", "attachment; filename=patientlist.txt");

		String drNo = request.getParameter("provider_no");
		// clear dr no value for all doc's
		if (drNo != null && drNo.equals("all")) {
			drNo = null;
		}
		String datefrom = request.getParameter("date_from");
		String dateto = request.getParameter("date_to");

		Date from = datefrom != null ? ConversionUtils.fromDateString(datefrom) : null;
		Date to = dateto != null ? ConversionUtils.fromDateString(dateto) : null;

		OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);

		PrintStream ps = new PrintStream(response.getOutputStream());

		for (Object[] o : dao.findPatientAppointments(drNo, from, to)) {
			Demographic d = (Demographic) o[0];
			Appointment a = (Appointment) o[1];
			Provider p = (Provider) o[2];

			ps.print(d.getLastName() + ",");
			ps.print(d.getFirstName() + ",");
			ps.print(d.getPhone() + ",");
			ps.print(d.getPhone2() + ",");
			ps.print(ConversionUtils.toTimeString(a.getStartTime()) + ",");
			ps.print(ConversionUtils.toDateString(a.getAppointmentDate()) + ",");
			ps.print(a.getType().replaceAll("\r\n", "") + ",");
			ps.print(p.getFirstName() + " ");
			ps.print(p.getLastName() + ",");
			ps.print(a.getLocation());
			ps.print("\n");
		}
		ps.println("");
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/** Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/** Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
