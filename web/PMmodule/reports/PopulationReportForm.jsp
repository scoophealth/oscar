<!-- 
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
 -->
<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>

<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	ProgramDao programDao = (ProgramDao) applicationContext.getBean("programDao");
	
	List<Program> allPrograms=programDao.getAllPrograms();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

	<h1>Population Report Form</h1>
	
	<form method="post" action="PopulationReport.jsp" >
		<table>
			<tr>
				<td>Program</td>
				<td>StartDate</td>
				<td>EndDate</td>
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
				<td><input type="submit"></td>
				<td></td>
			</tr>
		</table>
	</form>
	

	
<%@include file="/layouts/caisi_html_bottom.jspf"%>
