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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DATISExporterServlet extends HttpServlet {

	private static final long serialVersionUID = -3152671093057808424L;

	private static final Logger log = Logger.getLogger(DATISExporterServlet.class);

	private static final String RESPONSE_MIME_TYPE = "application/x-zip-compressed";
	private static final String EXPORT_PATH = "WEB-INF/datisexport";
	private static final String OUTFILE_ZIP = "outfile.zip";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		FileInputStream fis = null;

		int facilityId;
		String[] filenames = new String[6];
		int fileIndex = 0;
		File dir = null;

		try {
			facilityId = Integer.parseInt(request.getParameter("facilityId"));
			String sessionId = request.getSession().getId();

			log.debug("Exporting for facility ID: " + facilityId);

			String dirLocation = request.getSession().getServletContext().getRealPath(EXPORT_PATH);
			dir = new File(dirLocation + File.separatorChar + sessionId);
			dir.mkdir();
			dirLocation = dirLocation + File.separatorChar + sessionId;


			AbstractIntakeExporter[] exporter = new AbstractIntakeExporter[6];

			if(request.getParameter("ai") != null) {
				exporter[0] = (DATISAgencyInformation)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterAgencyInformation");
				exportCSVFile(exporter[0], "agencyinformation", dirLocation, facilityId, filenames, fileIndex++);
			}
			if(request.getParameter("lp") != null) {
				exporter[1] = (DATISListOfPrograms)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterListOfPrograms");
				exportCSVFile(exporter[1], "listofprograms", dirLocation, facilityId, filenames, fileIndex++);
			}
			if(request.getParameter("mn") != null) {
				exporter[2] = (DATISMain)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterMain");
				exportCSVFile(exporter[2], "main", dirLocation, facilityId, filenames, fileIndex++);
			}
			if(request.getParameter("pi") != null) {
				exporter[3] = (DATISProgramInformation)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterProgramInformation");
				exportCSVFile(exporter[3], "programinformation", dirLocation, facilityId, filenames, fileIndex++);
			}
			if(request.getParameter("gf") != null) {
				exporter[4] = (DATISGamingForm)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterGamblingForm");
				exportCSVFile(exporter[4], "gamblingform", dirLocation, facilityId, filenames, fileIndex++);
			}
			if(request.getParameter("nc") != null) {
				exporter[5] = (DATISNonClientService)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("intakeExporterNonClientService");
				exportCSVFile(exporter[5], "nonclientservice", dirLocation, facilityId, filenames, fileIndex++);
			}

			// Reset clients list for subsequent requests
			for(int i = 0; i < exporter.length; i++) {
				if(exporter[i] != null) {
					exporter[i].setClients(null);
				}
			}

			response.setContentType(RESPONSE_MIME_TYPE);
			response.setHeader("Content-Disposition", "attachment;filename=DATISexport.zip");

			fis = getZipFile(dirLocation, filenames);
			byte[] buf = new byte[1024];
			while(fis.read(buf) != -1) {
				response.getOutputStream().write(buf);
			}

		} catch(Exception t) {
			log.error("Error", t);
			try {
				response.getWriter().print("An Error Occured during DATIS Export operation. Please check server log for details.");
			} catch (IOException e) {
				MiscUtils.getLogger().error("Error", e);
			}
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}
			if(dir != null) {
				removeFiles(dir);
			}
		}
	}

	private void removeFiles(File dir) {
		if(dir.exists() && dir.isDirectory()) {
			for(File f :dir.listFiles()) {
				f.delete();
			}

			dir.delete();
		}
	}

	private FileInputStream getZipFile(String dirLocation, String[] filenames) throws IOException {
		byte[] buf = new byte[1024];

		String outFilename = dirLocation + File.separatorChar + OUTFILE_ZIP;
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
		for (int i = 0; i < filenames.length; i++) {
			if(StringUtils.isBlank(filenames[i])) {
				continue;
			}

			FileInputStream in = new FileInputStream(filenames[i]);
			out.putNextEntry(new ZipEntry(filenames[i].substring(1 + filenames[i].lastIndexOf(File.separatorChar))));

			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			out.closeEntry();
			in.close();
		}

		out.close();

		return new FileInputStream(outFilename);
	}

	private void exportCSVFile(AbstractIntakeExporter exporter, String filename, String dirLocation, int facilityId, String[] filenames, int fileIndex) throws Exception {
		exporter.setFacilityId(facilityId);
		String data = exporter.export();

		log.debug(filename + " data exported.");

		String fullName = dirLocation + File.separatorChar + filename + "Facility" + facilityId + ".csv";
		PrintWriter writer = new PrintWriter(fullName);
		writer.write(data);

		writer.flush();
		writer.close();

		filenames[fileIndex] = fullName;

		log.debug("File " + filename + " exported.");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request, response);
	}
}
