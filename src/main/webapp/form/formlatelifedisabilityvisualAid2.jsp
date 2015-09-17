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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Late Life FDI: Disability component</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(640,768)">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<table border="0" cellspacing="1" cellpadding="0" width="600px"
	height="95%">
	<tr>
		<td valign="top" colspan="2">
		<table border="0" cellspacing="0" cellpadding="0" width="600px"
			height="10%">
			<tr>
				<th class="subject">Late Life FDI: Disability Component</th>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" height="100%"
			width="100%">
			<tr>
				<td valign="top">
				<table border="0" cellspacing="0" cellpadding="0" height="85%"
					width="100%" id="visualAid1">
					<tr>
						<td valign="top" colspan="2">
						<table width="100%" height="650px" border="0" cellspacing="1"
							cellpadding="2">
							<tr class="title">
								<th colspan="5">Disability Visual Aid #2</th>
							</tr>
							<tr>
								<td class="question" colspan="5">To what extent do you feel
								limited in...? <br>
								<br>
								</td>
							</tr>
							<tr bgcolor="white" height="150px">
								<td width="11%" align="center" align="center"><img
									src="graphics/disabilityVisualAid/notAtAll.jpg" align="center"
									border='0' /></td>
								<td width="20%" align="center"><img
									src="graphics/disabilityVisualAid/little.jpg" align="center"
									border='0' /></td>
								<td width="25%" align="center"><img
									src="graphics/disabilityVisualAid/somewhat.jpg" align="center"
									border='0' /></td>
								<td width="30%" align="center"><img
									src="graphics/disabilityVisualAid/aLot.jpg" align="center"
									border='0' /></td>
								<td width="34%" align="center"><img
									src="graphics/disabilityVisualAid/completely.jpg"
									align="center" border='0' /></td>
							</tr>
							<tr bgcolor="white" height="80px">
								<td align="center" valign="center" width="10%">No
								Limitations</td>
								<td align="center" valign="center" width="20%">Slight
								Limitations</td>
								<td align="center" valign="center" width="25%">Moderate
								Limitations</td>
								<td align="center" valign="center" width="31%">Heavy
								Limitations</td>
								<td align="center" valign="center" width="34%">Total
								Limitation <br>
								Cannot do</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td style="border: 1px solid #000000" colspan="4">
								<table>
									<tr>
										<td width="3%"></td>
										<td><font style="text-decoration: underline">Examples
										of limiting factors that may restrict you:</font> <br>
										<li>Mental or Physical Energy</li>
										<li>Too much effort</li>
										<li>Social and economic circumstances</li>
										<li>Transportation problems</li>
										<li>Accessibility issues</li>
										<li>Health</li>
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td colspan="3">
								<table height="8px">
									<tr>
										<td></td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
			<tr>
				<td valign="top">
				<table class="Head" valign="bottom" width="100%" height="15%"
					id="copyRight">
					<tr>
						<td><font style="font-size: 70%">&copy; Copyright 2002
						Trustees of Boston University, All Right Reserved</font></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html:html>
