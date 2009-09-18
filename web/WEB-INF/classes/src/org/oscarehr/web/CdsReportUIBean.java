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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.util.SpringUtils;

public class CdsReportUIBean {

	private static Logger logger = LogManager.getLogger(CdsReportUIBean.class);

	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");

	/**
	 * End dates should be teated as inclusive.
	 */
	public static ArrayList<String> getAsciiExportData(int programId, int startYear, int startMonth, int endYear, int endMonth)
	{
		GregorianCalendar startDate=new GregorianCalendar(startYear, startMonth, 1);
		GregorianCalendar endDate=new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive
		
		ArrayList<String> results=new ArrayList<String>();
		
		String temp="blah : "+programId+" : "+startDate+" : "+endDate;
		results.add(temp);
		
		return(results);
	}
}
