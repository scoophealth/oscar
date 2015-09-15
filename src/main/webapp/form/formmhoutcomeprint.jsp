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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Print Mental Health Outcome</title>
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
		<th align="left"><big>MENTAL HEALTH OUTCOME</big><br>
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
				<td class="mhSelect">Services Provided:<br>
				<% String[] sp = list.loadData("mhOutcome/ServicesProvided.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("o_sp1", "---").equals("")?"":sp[Integer.parseInt(props.getProperty("o_sp1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("o_sp2", "---").equals("")?"":sp[Integer.parseInt(props.getProperty("o_sp2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("o_sp3", "---").equals("")?"":sp[Integer.parseInt(props.getProperty("o_sp3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Problems Encountered:<br>
				<% String[] pe = list.loadData("mhOutcome/ProblemsEncountered.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("o_pe1", "---").equals("")?"":pe[Integer.parseInt(props.getProperty("o_pe1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("o_pe2", "---").equals("")?"":pe[Integer.parseInt(props.getProperty("o_pe2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("o_pe3", "---").equals("")?"":pe[Integer.parseInt(props.getProperty("o_pe3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Dispostion:<br>
				<% String[] d = list.loadData("mhOutcome/Disposition.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("o_d1", "---").equals("")?"":d[Integer.parseInt(props.getProperty("o_d1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("o_d2", "---").equals("")?"":d[Integer.parseInt(props.getProperty("o_d2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("o_d3", "---").equals("")?"":d[Integer.parseInt(props.getProperty("o_d3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Patient Not Seen:<br>
				<% String[] pns = list.loadData("mhOutcome/PatientNotSeen.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("o_pns1", "---").equals("")?"":pns[Integer.parseInt(props.getProperty("o_pns1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("o_pns2", "---").equals("")?"":pns[Integer.parseInt(props.getProperty("o_pns2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("o_pns3", "---").equals("")?"":pns[Integer.parseInt(props.getProperty("o_pns3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Comments:<br>
				<%= props.getProperty("o_outComments", "")%></td>
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
