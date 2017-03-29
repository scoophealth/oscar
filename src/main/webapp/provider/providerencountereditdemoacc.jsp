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

<%
  String apptProvider_no, user_no, username;
  user_no = (String) session.getAttribute("user");
  //username =  request.getParameter("username").toUpperCase();
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,java.net.*"
	errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>DEMO ACCS</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  document.demoacce.xml_Problem_List.focus();
  //document.demoacce.year.select();
}

//-->
</script>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><%=request.getParameter("demographic_name")%></font></th>
	</tr>
</table>
<%
   boolean bNewDemoAcc=true;
	DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(request.getParameter("demographic_no")));
	if(da != null) {
		String content = da.getContent();
		bNewDemoAcc=false;

%>
<xml id="xml_list">
<encounteraccessory>
<%=content%>
</encounteraccessory>
</xml>
<%
   }
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!--for form use-->
	<form name="demoacce" method="post"
		action="providersavedemographicaccessory.jsp">
	<tr>
		<td>

		<table border="0" cellpadding="2" cellspacing="0" bgcolor="#aabbcc"
			width=100%>
			<tr>
				<td>
				<table bgcolor="#eeeeee" border="0" cellpadding="3" cellspacing="0"
					width="100%">
					<tr>
						<td bgcolor="#ffffff" align="center">

						<table width="100%" border="0" cellpadding="2" cellspacing="0"
							bgcolor="#aabbcc" <%=bNewDemoAcc?"":"datasrc='#xml_list'"%>>
							<tr>
								<td width="50%" align="center">Problem List:<br>
								<textarea name="xml_Problem_List" style="width: 100%" cols="30"
									rows="18" <%=bNewDemoAcc?"":"datafld='xml_Problem_List'"%>></textarea>

								</td>
								<td width="50%" align="center">Medication:<br>
								<textarea name="xml_Medication" style="width: 100%" cols="30"
									rows="18" <%=bNewDemoAcc?"":"datafld='xml_Medication'"%>></textarea>
								</td>
							</tr>
							<tr>
								<td>
								<div align="center">Allergy/Alert:<br>
								<textarea name="xml_Alert" style="width: 100%" cols="30"
									rows="15" <%=bNewDemoAcc?"":"datafld='xml_Alert'"%>></textarea>
								</div>
								</td>
								<td>
								<div align="center">Family Social History:<br>
								<textarea name="xml_Family_Social_History" style="width: 100%"
									cols="30" rows="15"
									<%=bNewDemoAcc?"":"datafld='xml_Family_Social_History'"%>></textarea>
								</div>
								</td>
							</tr>
						</table>

						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		</td>
	</tr>
	<tr>
		<td align="center"><br>
		<input type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="submit" name="submit" value=" Save "> <input
			type="button" name="Button" value="Cancel" onClick="window.close();">
		</td>
	</tr>
	</form>
</table>

</body>
</html>
