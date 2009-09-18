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
import java.util.GregorianCalendar;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.util.SpringUtils;

public class Cds4ReportUIBean {

	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	
	private static final char ROW_TERMINATOR='>';
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public static ArrayList<String> getAsciiExportData(int programId, int startYear, int startMonth, int endYear, int endMonth)
	{
		GregorianCalendar startDate=new GregorianCalendar(startYear, startMonth, 1);
		GregorianCalendar endDate=new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive
		
		ArrayList<String> results=new ArrayList<String>();
		
		results.add(getHeader(programId)+ROW_TERMINATOR);
		
		for (CdsFormOption cdsFormOption : cdsFormOptionDao.findByVersion("4"))
		{
			results.add(dataLine(programId, startDate, endDate, cdsFormOption)+ROW_TERMINATOR);
		}
		
		return(results);
	}
	
	private static String dataLine(int programId, GregorianCalendar startDate, GregorianCalendar endDate, CdsFormOption cdsFormOption) {

		StringBuilder sb=new StringBuilder();
		
		sb.append("incomplete_");
		
		sb.append(cdsFormOption.getCdsDataCategory());
		
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
		
		return("incomplete_ooooopppppfff");
	}
}
