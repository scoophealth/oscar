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

<%@ page import="oscar.form.*, java.util.*"%>
<%@ page import="java.io.FileInputStream"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Print Mental Health Assessment</title>
<html:base />
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="mhStyles.css">

<%
    Properties props = new Properties();
    StringBuffer temp = new StringBuffer("");
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    temp = new StringBuffer(e.nextElement().toString());
		props.setProperty(temp.toString(), request.getParameter(temp.toString()));
    }
    oscar.oscarEncounter.util.EctFileUtil list = new oscar.oscarEncounter.util.EctFileUtil();

    String projecthome = oscarVariables.getProperty("project_home");
    String path = "form/dataFiles";
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
        return true;
    }
    function onCancel() {
        window.close();
        return true;
    }
</script>
</head>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">

<table class="Head" class="hidePrint">
	<tr>
		<td align="left"><input type="button" value="Exit"
			onclick="javascript:onCancel();" /> <input type="button"
			value="Print" onclick="javascript:onPrint();" /></td>
	</tr>
</table>

<table cellpadding="5" cellspacing="0">
	<tr>
		<th align="left"><big>MENTAL HEALTH ASSESSMENT</big><br>
		<br>
		</th>
	</tr>
	<tr>
		<td>
		<table border="0" cellpadding="5" cellspacing="0"
			class="tableWithBorder">
			<tr>
				<td>Name:</td>
				<td align="left"><%= props.getProperty("c_pName", "") %>&nbsp;</td>
			</tr>
			<tr>
				<td>Sex:</td>
				<td align="left"><%= props.getProperty("c_sex", "") %>&nbsp;</td>
			</tr>
			</tr>
			<td>Address:</td>
			<td align="left"><%= props.getProperty("c_address", "") %>&nbsp;</td>
			</tr>
			<tr>
				<td>Home Phone:</td>
				<td align="left"><%= props.getProperty("c_homePhone", "") %>&nbsp;</td>
			</tr>
			<tr>
				<td>Birth Date <small>(yyyy/mm/dd)</small>:</td>
				<td align="left"><%= props.getProperty("c_birthDate", "") %>&nbsp;</td>
			</tr>
			<tr>
				<td>Referral Date<small>(yyyy/mm/dd)</small>:</td>
				<td align="left"><%= props.getProperty("c_referralDate", "") %>&nbsp;</td>
			</tr>
			<tr>
				<td>Referred By:</td>
				<td align="left"><%= props.getProperty("c_referredBy", "") %>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td class="mhSelect">Psychiatric Symptoms:<br>
				<% String[] aps = list.loadData("mhAssessment/PsychiatricSymptoms.txt", projecthome, path );%>
				1. <b><%=props.getProperty("a_aps1", "---").equals("")?"":aps[Integer.parseInt(props.getProperty("a_aps1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("a_aps2", "---").equals("")?"":aps[Integer.parseInt(props.getProperty("a_aps2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("a_aps3", "---").equals("")?"":aps[Integer.parseInt(props.getProperty("a_aps3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Psychosocial Issues:<br>
				<% String[] api = list.loadData("mhAssessment/PsychosocialIssues.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("a_api1", "---").equals("")?"":api[Integer.parseInt(props.getProperty("a_api1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("a_api2", "---").equals("")?"":api[Integer.parseInt(props.getProperty("a_api2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("a_api3", "---").equals("")?"":api[Integer.parseInt(props.getProperty("a_api3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Med/Phy Issues:<br>
				<% String[] ampi = list.loadData("mhAssessment/MedPhyIssues.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("a_ampi1", "---").equals("")?"":ampi[Integer.parseInt(props.getProperty("a_ampi1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("a_ampi2", "---").equals("")?"":ampi[Integer.parseInt(props.getProperty("a_ampi2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("a_ampi3", "---").equals("")?"":ampi[Integer.parseInt(props.getProperty("a_ampi3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Treatment Plan:<br>
				<% String[] tp = list.loadData("mhAssessment/TreatmentPlan.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("a_tp1", "---").equals("")?"":tp[Integer.parseInt(props.getProperty("a_tp1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("a_tp2", "---").equals("")?"":tp[Integer.parseInt(props.getProperty("a_tp2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("a_tp3", "---").equals("")?"":tp[Integer.parseInt(props.getProperty("a_tp3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Comments:<br>
				<%= props.getProperty("a_assComments", "")%></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table class="Head" class="hidePrint">
	<tr>
		<td align="left"><input type="button" value="Exit"
			onclick="javascript:onCancel();" /> <input type="button"
			value="Print" onclick="javascript:onPrint();" /></td>
	</tr>
</table>

</body>
</html:html>
