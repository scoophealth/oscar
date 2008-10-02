<%-- 
/*
* Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	
	List<Program> allPrograms=programDao.getAllActivePrograms();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

	
<h1>Activity Report Form</h1>
	
<form method="post" action="activity_report.jsp" >
	<table>
		<tr>
			<td>Program</td>
			<td>Start Date</td>
			<td>End Date</td>
		</tr>
		
		<tr>
			<td>
				<select name="programId">
					<%
						for (Program program : allPrograms)
						{
							%>
								<option value="<%=program.getId() %>"><%=program.getName()%></option>
							<%
						}
					%>
				</select>
			</td>
			
			<td>
				<input type="text" name="startDate" />
			</td>
			
			<td>
				<input type="text" name="endDate" />
			</td>
		</tr>	
					
		<tr>
			<td></td>
			<td>(YYYY-MM-DD)</td>
			<td>(YYYY-MM-DD)</td>
		</tr>

		<tr>
			<td></td>
			<td></td>
			<td><input type="submit" /></td>
		</tr>
	</table>
</form>
	
<hr />

<h2>Export to csv</h2>
(This will export all bed/service programs to a csv broken down by month.)

<form method="post" action="activity_report_export.jsp" >
	<table>
		<tr>
			<td>Start Date</td>
			<td>EndDate (inclusive)</td>
		</tr>
		<tr>
			<td>
				<input type="text" name="startDate" />
			</td>
			
			<td>
				<input type="text" name="endDate" />
			</td>
		</tr>	
					
		<tr>
			<td>(YYYY-MM)</td>
			<td>(YYYY-MM)</td>
		</tr>

		<tr>
			<td></td>
			<td><input type="submit" value="export" /></td>
		</tr>
	</table>
</form>
	
<%@include file="/layouts/caisi_html_bottom.jspf"%>
