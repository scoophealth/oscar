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
package org.oscarehr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO.EncounterCounts;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;

public class ProviderServiceReportUIBean {

	private static Logger logger = MiscUtils.getLogger();

	private ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);

	private Date startDate = null;
	private Date endDate = null;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");

	public ProviderServiceReportUIBean(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static class DataRow {
		public String programName = null;
		public String programType = null;
		public String date = null;
		public EncounterCounts encounterCounts = null;
	}

	public List<DataRow> getDataRows() {
		Calendar startCal = Calendar.getInstance();
		startCal.setTimeInMillis(startDate.getTime());
		DateUtils.setToBeginningOfMonth(startCal);


		Calendar endCal = Calendar.getInstance();
		endCal.setTimeInMillis(endDate.getTime());
		endCal.add(Calendar.MONTH, 1);
		DateUtils.setToBeginningOfMonth(endCal);

		List<Program> activePrograms = programDao.getAllActivePrograms();
		SecRole doctorRole = null;
		for (SecRole role : secRoleDao.findAll()) if ("doctor".equals(role.getName())) doctorRole = role;
		if (doctorRole == null) 
		{
			logger.error("Error, no caisi role named 'doctor' found in database.");
			return(null);
		}

		ArrayList<DataRow> results = new ArrayList<DataRow>();

		for (Program program : activePrograms) {
			// we only want bed and service programs
			if (!Program.BED_TYPE.equals(program.getType()) && !Program.SERVICE_TYPE.equals(program.getType())) continue;

			results.addAll(getProgramNumbers(startCal, endCal, doctorRole, program));
		}

		results.addAll(getEntireAgencyNumbers(startCal, endCal, doctorRole));

		return (results);
	}

	private Collection<? extends DataRow> getEntireAgencyNumbers(Calendar startCal, Calendar endCal, SecRole doctorRole) {
		ArrayList<DataRow> results = new ArrayList<DataRow>();

		Calendar tempStart = (Calendar) startCal.clone();
		while (tempStart.compareTo(endCal) < 0) {
			Calendar tempEnd = (Calendar) tempStart.clone();
			tempEnd.add(Calendar.MONTH, 1);

			DataRow dataRow = new DataRow();
			dataRow.programName = "all programs";
			dataRow.programType = "all program types";
			dataRow.date = dateFormatter.format(tempStart.getTime());
			dataRow.encounterCounts = CaseManagementNoteDAO.getDemographicEncounterCountsByProgramAndRoleId(null, doctorRole.getId().intValue(),
					tempStart.getTime(), tempEnd.getTime());

			results.add(dataRow);

			tempStart.add(Calendar.MONTH, 1);
		}

		DataRow dataRow = new DataRow();
		dataRow.programName = "all programs";
		dataRow.programType = "all program types";
		dataRow.date = dateFormatter.format(startCal.getTime()) + " to " + dateFormatter.format(endCal.getTime());
		dataRow.encounterCounts = CaseManagementNoteDAO.getDemographicEncounterCountsByProgramAndRoleId(null, doctorRole.getId().intValue(), startCal.getTime(),
				endCal.getTime());

		results.add(dataRow);
		
		return(results);
	}

	private ArrayList<DataRow> getProgramNumbers(Calendar startCal, Calendar endCal, SecRole doctorRole, Program program) {
		ArrayList<DataRow> results = new ArrayList<DataRow>();

		Calendar tempStart = (Calendar) startCal.clone();
		while (tempStart.compareTo(endCal) < 0) {
			Calendar tempEnd = (Calendar) tempStart.clone();
			tempEnd.add(Calendar.MONTH, 1);

			DataRow dataRow = new DataRow();
			dataRow.programName = program.getName();
			dataRow.programType = program.getType();
			dataRow.date = dateFormatter.format(tempStart.getTime());
			dataRow.encounterCounts = CaseManagementNoteDAO.getDemographicEncounterCountsByProgramAndRoleId(program.getId(), doctorRole.getId().intValue(),
					tempStart.getTime(), tempEnd.getTime());

			results.add(dataRow);

			tempStart.add(Calendar.MONTH, 1);
		}

		DataRow dataRow = new DataRow();
		dataRow.programName = program.getName();
		dataRow.programType = program.getType();
		dataRow.date = dateFormatter.format(startCal.getTime()) + " to " + dateFormatter.format(endCal.getTime());
		dataRow.encounterCounts = CaseManagementNoteDAO.getDemographicEncounterCountsByProgramAndRoleId(program.getId(), doctorRole.getId().intValue(), startCal.getTime(),
				endCal.getTime());

		results.add(dataRow);
		
		return(results);
	}
}
