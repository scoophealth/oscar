/*
 * Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class Cds4ReportUIBean {

	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	
	private static final char ROW_TERMINATOR='>';
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public static ArrayList<String> getAsciiExportData(int programId, int startYear, int startMonth, int endYear, int endMonth)
	{
		GregorianCalendar startDate=new GregorianCalendar(startYear, startMonth, 1);
		GregorianCalendar endDate=new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive
		
		ArrayList<String> asciiTextFileRows=new ArrayList<String>();
		asciiTextFileRows.add(getHeader(programId)+ROW_TERMINATOR);

		List<Admission> admissions=admissionDao.getAdmissionsByProgramAndDate(programId, startDate.getTime(), endDate.getTime());
		List<CdsClientForm> cdsForms=cdsClientFormDao.findLatestSignedCdsForms(admissions, "4");
		
		// put admissions into map so it's easier to retrieve by id.
		HashMap<Integer,Admission> admissionMap=new HashMap<Integer,Admission>();
		for (Admission admission : admissions) admissionMap.put(admission.getId().intValue(), admission);
		
		for (CdsFormOption cdsFormOption : cdsFormOptionDao.findByVersion("4"))
		{
			asciiTextFileRows.add(cdsFormOption.getCdsDataCategory()+getDataLine(cdsFormOption, cdsForms, admissionMap)+ROW_TERMINATOR);
		}
		
		return(asciiTextFileRows);
	}
	
	private static String getDataLine(CdsFormOption cdsFormOption, List<CdsClientForm> cdsForms, HashMap<Integer,Admission> admissionMap) {

		if (cdsFormOption.getCdsDataCategory().startsWith("007-"))
		{
			String result=getDataLine007(cdsFormOption, cdsForms, admissionMap);
			return(result);
		}
		else
		{
			StringBuilder sb=new StringBuilder();
			
			sb.append("*****INCOMPLETED*****");

			return(sb.toString());
		}
    }

	
	private static String getDataLine007(CdsFormOption cdsFormOption, List<CdsClientForm> cdsForms, HashMap<Integer,Admission> admissionMap) {
		StringBuilder sb=new StringBuilder();
		
		if ("007-01".equals(cdsFormOption.getCdsDataCategory()))
		{
			sb.append(getMultipleAdmissionCount(cdsForms));
			sb.append(getCohortCounts(cdsForms,admissionMap));
		}
		
		return(sb.toString());
    }

	private static String getCohortCounts(List<CdsClientForm> cdsForms, HashMap<Integer, Admission> admissionMap) {
		// okay stupid but we can't just look at the admissions, there might be
		// an admission which is missing a cds form at which point we ignore it so we have to iterate through the cdsForms
		
		// we will use Integer as the cohort year number from 0 to 10
		AccumulatorMap<Integer> cohortBuckets=new AccumulatorMap<Integer>();
		
		for (CdsClientForm form : cdsForms)
		{
			Admission admission=admissionMap.get(form.getAdmissionId());
			Date dischargeDate=new Date(); // default duration calculation to today if not discharged.
			if (admission.getDischargeDate()!=null) dischargeDate=admission.getDischargeDate();
			
			int years=MiscUtils.calculateYearDifference(dischargeDate, admission.getAdmissionDate());
			if (years>10) years=10; // limit everything above 10 years to the 10 year bucket.
			
			cohortBuckets.increment(years);
		}
		
		StringBuilder sb=new StringBuilder();
		
		for (int i=0; i<=10; i++)
		{
			Integer count=cohortBuckets.get(i);
			if (count==null) count=0;
			
			sb.append(padTo6(count));
		}
		
		return(sb.toString());
    }

	public static String getFilename(int programId)
	{
		// stubbed for now
	    // ooooopppppfff.Txt
		// Where:
		//      ooooo is the MOHLTC assigned Service Organization number
		//      ppppp is the MOHLTC assigned Program number
		//      fff is the CDS-MH Function Code

		return(getHeader(programId)+".Txt");
	}

	private static String getHeader(int programId)
	{
	    // ooooopppppfff
		// Where:
		//      ooooo is the MOHLTC assigned Service Organization number
		//      ppppp is the MOHLTC assigned Program number
		//      fff is the CDS-MH Function Code
		
		return("incomplete_"+programId+"_ooooopppppfff");
	}
	
	private static String getMultipleAdmissionCount(List<CdsClientForm> cdsForms)
	{
		AccumulatorMap<Integer> accumulatorMap=new AccumulatorMap<Integer>();
		
		for (CdsClientForm form : cdsForms)
		{
			accumulatorMap.increment(form.getClientId());
		}
		
		int multipleAdmissions=0;
		
		for (Integer value : accumulatorMap.values())
		{
			if (value>1) multipleAdmissions++;
		}
		
		return(padTo6(multipleAdmissions));
	}
	
	private static String padTo6(int i)
	{
		StringBuilder sb=new StringBuilder();
		
		if (i<9) sb.append("0");
		if (i<99) sb.append("0");
		if (i<999) sb.append("0");
		if (i<9999) sb.append("0");
		if (i<99999) sb.append("0");
		
		sb.append(i);
		
		return(sb.toString());
	}
}
