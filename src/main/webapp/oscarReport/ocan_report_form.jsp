<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>


<%@include file="/layouts/caisi_html_top.jspf"%>


<h1>OCAN Export Report - v2.0.6</h1>

				
<form method="post" action="ocan_report_export.jsp">
	<table class="borderedTableAndCells">
		<tr>
			<td>OCAN Type </td>			
			<td >
				<select name="ocanType" id="ocanType">
					<option value="FULL">FULL</option>
					<option value="SELF">SELF+CORE</option>
					<option value="CORE">CORE</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">Date Selection  </td>
			<td></td>
		</tr>
		<tr>			
			<td>The Assessment Start Date From</td>
			<td>
				<select name="startYear">
				<%
					GregorianCalendar cal=new GregorianCalendar();
					int year=cal.get(GregorianCalendar.YEAR);
					year = year + 5;
					for(int i=0;i<5;i++)
					{	
						%>
						<option value="<%=year-i%>"><%=year-i %> </option>
						<%
					}
					year = year - 5;
					for (int i=0; i<10; i++)
					{
						%>
							<option value="<%=year-i%>"><%=year-i%></option>
						<%
					}
				%>
				</select>
				-
				<select name="startMonth">
				<%
					DateFormatSymbols dateFormatSymbols=DateFormatSymbols.getInstance();
					String[] months=dateFormatSymbols.getShortMonths();
					
					for (int i=1; i<13; i++)
					{
						%>
							<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>

		<tr>
			<td >To </td>
			<td>
				<select name="endYear">
				<%					
					int year2=cal.get(GregorianCalendar.YEAR);
					year2 = year2 + 5;
					for(int i=0;i<5;i++)
					{	
						%>
						<option value="<%=year2-i%>"><%=year2-i %> </option>
						<%
					}
					year2 = year2 - 5;
					for (int i=0; i<10; i++)
					{
						%>
							<option value="<%=year2-i%>"><%=year2-i%></option>
						<%
					}
				%>
				</select>
				-
				<select name="endMonth">
				<%					
					for (int i=1; i<13; i++)
					{
						%>
							<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>



		<tr>
			<td></td>
			<td><input type="submit" value="Download Report" /></td>
		</tr>
	</table>	
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>
