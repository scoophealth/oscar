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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class CdsManualLineEntry {
	private static final Logger logger=MiscUtils.getLogger();
	
	public int multipleAdmissions;
	public int cohort0;
	public int cohort1;
	public int cohort2;
	public int cohort3;
	public int cohort4;
	public int cohort5;
	public int cohort6;
	public int cohort7;
	public int cohort8;
	public int cohort9;
	public int cohort10;
	
	/**
	 * expects the request parameters to be of the form
	 * 7-02.ma, 7-02.c0, 7-02.c1 where 7-02 is the section
	 * 
	 * to be used with outputCdsManualLineEntryTable()
	 */
	public static CdsManualLineEntry getCdsManualLineEntry(HttpServletRequest request, String section)
	{
		CdsManualLineEntry cdsManualLineEntry=new CdsManualLineEntry();
		
		cdsManualLineEntry.multipleAdmissions=getParameterDefault0(request, section+".ma");
		cdsManualLineEntry.cohort0=getParameterDefault0(request, section+".c0");
		cdsManualLineEntry.cohort1=getParameterDefault0(request, section+".c1");
		cdsManualLineEntry.cohort2=getParameterDefault0(request, section+".c2");
		cdsManualLineEntry.cohort3=getParameterDefault0(request, section+".c3");
		cdsManualLineEntry.cohort4=getParameterDefault0(request, section+".c4");
		cdsManualLineEntry.cohort5=getParameterDefault0(request, section+".c5");
		cdsManualLineEntry.cohort6=getParameterDefault0(request, section+".c6");
		cdsManualLineEntry.cohort7=getParameterDefault0(request, section+".c7");
		cdsManualLineEntry.cohort8=getParameterDefault0(request, section+".c8");
		cdsManualLineEntry.cohort9=getParameterDefault0(request, section+".c9");
		cdsManualLineEntry.cohort10=getParameterDefault0(request, section+".c10");
		
		return(cdsManualLineEntry);
	}
	
	private static int getParameterDefault0(HttpServletRequest request, String parameterName)
	{
		String temp=request.getParameter(parameterName);
		temp=StringUtils.trimToNull(temp);
		
		try
		{
			return(Integer.parseInt(temp));
		}
		catch (Exception e){
			logger.warn("Error in cds parameter entry, defaulting to 0", e);
			return(0);
		}
	}
	
	public static String outputCdsManualLineEntryTable(String section)
	{
		//	<table>
		//		<tr>
		//			<td>multiple admissions</td>
		//			<td>cohort 0</td>
		//			<td>1</td>
		//			<td>2</td>
		//			<td>3</td>
		//			<td>4</td>
		//			<td>5</td>
		//			<td>6</td>
		//			<td>7</td>
		//			<td>8</td>
		//			<td>9</td>
		//			<td>10</td>
		//		</tr>
		//		<tr>
		//			<td><input type="text" name="7-02.ma" /></td>
		//			<td><input type="text" name="7-02.c0" /></td>
		//			<td><input type="text" name="7-02.c1" /></td>
		//			<td><input type="text" name="7-02.c2" /></td>
		//			<td><input type="text" name="7-02.c3" /></td>
		//			<td><input type="text" name="7-02.c4" /></td>
		//			<td><input type="text" name="7-02.c5" /></td>
		//			<td><input type="text" name="7-02.c6" /></td>
		//			<td><input type="text" name="7-02.c7" /></td>
		//			<td><input type="text" name="7-02.c8" /></td>
		//			<td><input type="text" name="7-02.c9" /></td>
		//			<td><input type="text" name="7-02.c10" /></td>
		//		</tr>
		//	</table>
	
		StringBuilder sb=new StringBuilder();
		
		sb.append("<table>");
		sb.append("<tr>");

		sb.append("<td>Multiple Admissions</td>");
		for (int i=0; i<=10; i++)
		{
			sb.append("<td>Cohort "+i+"</td>");			
		}
		
		sb.append("</tr>");
		sb.append("<tr>");

		sb.append("<td><input type=\"text\" name=\""+section+".ma\" /></td>");
		for (int i=0; i<=10; i++)
		{
			sb.append("<td><input type=\"text\" name=\""+section+".c"+i+"\" /></td>");
		}
		
		sb.append("</tr>");
		sb.append("</table>");
		
		return(sb.toString());
	}
}
