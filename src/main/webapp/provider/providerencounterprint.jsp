<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*, java.sql.*, oscar.*,java.net.*" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.EncounterDao" %>
<%@page import="org.oscarehr.common.model.Encounter" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	EncounterDao encounterDao = SpringUtils.getBean(EncounterDao.class);	
%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ENCOUNTER PRINT</title>
<script language="JavaScript">
<!--


//-->
</script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<center>
<table BORDER=0 width="100%">
	<tr>
		<td>

		<table BORDER=0 NOSAVE width="100%">
			<TR>
				<%@ include file="../share/letterheader.htm"%>
				<TD WIDTH="20%" ALIGN="right" nowrap valign="top"><input
					type="button" name="Submit" value="Print" onClick="window.print()">
				<input type="button" name="Submit2" value="Cancel"
					onClick="window.close()"></TD>
			</TR>
		</TABLE>

<%
	Encounter enc = encounterDao.find(Integer.parseInt(request.getParameter("encounter_no")));
   String encounter_date=null,encounter_time=null,subject=null,content=null,provider_no=null;
   if (enc != null) {
     encounter_date=ConversionUtils.toDateString(enc.getEncounterDate());
     encounter_time=ConversionUtils.toTimeString(enc.getEncounterTime());
     provider_no=enc.getProviderNo();
     subject=enc.getSubject();
     content=enc.getContent();
   }
%> <xml id="xml_list"> <encounter> <%=content%> </encounter> </xml>
		<table width="100%" border="1" datasrc='#xml_list'>
			<tr>
				<td width="65%"><b>Name: </b><span datafld='xml_name'></td>
				<td><b>Phone: </b><span datafld='xml_hp'></td>
			</tr>
			<tr>
				<td colspan='2'><b>Address: </b><span datafld='xml_address'></td>
			</tr>
			<tr>
				<td width="50%"><b>DOB</b>(yyyy/mm/dd): <span
					datafld='xml_dob'></td>
				<td><b>Age: </b><span datafld='xml_age'> <span
					datafld='xml_sex'></td>
			</tr>
			<tr>
				<td width="50%"><b>PCN Roster Status: </b><span
					datafld='xml_roster'></td>
				<td><b>HIN: </b><span datafld='xml_hin'> <span
					datafld='xml_ver'></td>
			</tr>
			<tr>
				<td colspan='2'><b>Family Doctor: </b><span datafld='xml_fd'></td>
			</tr>
		</table>


		<br>
		<p>
		<table width="100%" cellspacing="0" cellpadding="1" border="1"
			datasrc='#xml_list'>
			<tr>
				<td width="50%" valign="top"><b>Problem List:</b><br>
				<div datafld='xml_Problem_List'>
				</td>
				<td valign="top"><b>Medication:</b><br>
				<div datafld='xml_Medication'>
				</td>
			</tr>
			<tr>
				<td valign="top"><b>Allergy/Alert:</b><br>
				<div datafld='xml_Alert'>
				</td>
				<td valign="top"><b>Family Social History:</b><br>
				<div datafld='xml_Family_Social_History'>
				</td>
			</tr>
		</table>
		<br>
		<p>
		<%
%>
		
		<table width="100%" cellspacing="0" cellpadding="2" border="1"
			datasrc='#xml_list'>
			<tr>
				<td><%=encounter_date%> <%=encounter_time%><br>
				<b>Reason:</b><%=subject.substring(2).replace('|',' ')%><br>
				<b>Content:</b>
				<div datafld='xml_content'>
				</td>
			</tr>
		</table>
		<br>
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			datasrc='#xml_list'>
			<tr>
				<td><b>By: </b><span datafld='xml_username'><br></td>
			</tr>
		</table>

		</td>
	</tr>
</table>
</center>
</body>
</html>
