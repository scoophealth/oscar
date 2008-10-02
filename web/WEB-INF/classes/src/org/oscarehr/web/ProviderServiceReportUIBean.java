/*
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.EncounterUtil.EncounterType;

public class ProviderServiceReportUIBean {

	private ProviderDao providerDao=(ProviderDao)SpringUtils.getBean("providerDao");
	private ProgramDao programDao=(ProgramDao)SpringUtils.getBean("programDao");
	
	private Date startDate = null;
	private Date endDate = null;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");

	public ProviderServiceReportUIBean(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static class DataRow
	{
		public String programName=null;
		public String programType=null;
		public String date=null;
		public String providerName=null;
		public int uniqueFaceToFaceEncounters=0;
		public int uniqueAllEncounters=0;
	}
	
	public List<DataRow> getDataRows()
	{
		Calendar startCal=Calendar.getInstance();
		startCal.setTimeInMillis(startDate.getTime());
		MiscUtils.setToBeginningOfMonth(startCal);
		
		Calendar endCal=Calendar.getInstance();
		endCal.setTimeInMillis(endDate.getTime());
		endCal.add(Calendar.MONTH, 1);
		MiscUtils.setToBeginningOfMonth(endCal);
		
		List<Program> activePrograms=programDao.getAllActivePrograms();
		
		List<Provider> providers=providerDao.getActiveProvidersByType("doctor");
		ArrayList<DataRow> results=new ArrayList<DataRow>();
		
		for (Provider provider : providers)
		{
			Calendar tempStart=(Calendar)startCal.clone();
			
			for (Program program : activePrograms)
			{
				if (!Program.BED_TYPE.equals(program.getType()) && !Program.SERVICE_TYPE.equals(program.getType())) continue;

				while (tempStart.compareTo(endCal) < 0)
				{
					Calendar tempEnd=(Calendar)tempStart.clone();
					tempEnd.add(Calendar.MONTH, 1);

					DataRow dataRow=new DataRow();
					dataRow.programName=program.getName();
					dataRow.programType=program.getType();
					dataRow.date=dateFormatter.format(tempStart.getTime());
					dataRow.providerName=provider.getLastName() + ", "+provider.getFirstName();
					dataRow.uniqueFaceToFaceEncounters=CaseManagementNoteDAO.getUniqueDemographicCountByProviderProgramAndEncounterType(provider.getProviderNo(), program.getId(), EncounterType.FACE_TO_FACE_WITH_CLIENT, tempStart.getTime(), tempEnd.getTime());
					dataRow.uniqueAllEncounters=CaseManagementNoteDAO.getUniqueDemographicCountByProviderProgramAndEncounterType(provider.getProviderNo(), program.getId(), null, tempStart.getTime(), tempEnd.getTime());
					
					results.add(dataRow);
					
					tempStart.add(Calendar.MONTH, 1);
				}

				DataRow dataRow=new DataRow();
				dataRow.programName=program.getName();
				dataRow.programType=program.getType();
				dataRow.date=dateFormatter.format(startCal.getTime()) + " to " + dateFormatter.format(endCal.getTime());
				dataRow.providerName=provider.getLastName() + ", "+provider.getFirstName();
				dataRow.uniqueFaceToFaceEncounters=CaseManagementNoteDAO.getUniqueDemographicCountByProviderProgramAndEncounterType(provider.getProviderNo(), program.getId(), EncounterType.FACE_TO_FACE_WITH_CLIENT, startCal.getTime(), endCal.getTime());
				dataRow.uniqueAllEncounters=CaseManagementNoteDAO.getUniqueDemographicCountByProviderProgramAndEncounterType(provider.getProviderNo(), program.getId(), null, startCal.getTime(), endCal.getTime());
				
				results.add(dataRow);
			}
		}
		
		return(results);
	}
}
