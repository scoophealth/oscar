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
<title>Print Mental Health Referral</title>
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
		<th align="left"><big>MENTAL HEALTH REFERRAL</big><br>
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
				<%String[] rps = list.loadData("mhReferral/PsychiatricSymptoms.txt", projecthome, path );%>
				1. <b><%=props.getProperty("r_rps1", "---").equals("")?"":rps[Integer.parseInt(props.getProperty("r_rps1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("r_rps2", "---").equals("")?"":rps[Integer.parseInt(props.getProperty("r_rps2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("r_rps3", "---").equals("")?"":rps[Integer.parseInt(props.getProperty("r_rps3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Psychosocial Issues:<br>
				<% String[] rpi = list.loadData("mhReferral/PsychosocialIssues.txt", projecthome, path );%>
				1. <b><%=props.getProperty("r_rpi1", "---").equals("")?"":rpi[Integer.parseInt(props.getProperty("r_rpi1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("r_rpi2", "---").equals("")?"":rpi[Integer.parseInt(props.getProperty("r_rpi2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("r_rpi3", "---").equals("")?"":rpi[Integer.parseInt(props.getProperty("r_rpi3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Med/Phy Issues:<br>
				<% String[] rmpi = list.loadData("mhReferral/MedPhyIssues.txt", projecthome, path );%>
				1. <b><%=props.getProperty("r_rmpi1", "---").equals("")?"":rmpi[Integer.parseInt(props.getProperty("r_rmpi1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("r_rmpi2", "---").equals("")?"":rmpi[Integer.parseInt(props.getProperty("r_rmpi2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("r_rmpi3", "---").equals("")?"":rmpi[Integer.parseInt(props.getProperty("r_rmpi3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Interventions Requested:<br>
				<% String[] ir = list.loadData("mhReferral/InterventionsRequested.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("r_ir1", "---").equals("")?"":ir[Integer.parseInt(props.getProperty("r_ir1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("r_ir2", "---").equals("")?"":ir[Integer.parseInt(props.getProperty("r_ir2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("r_ir3", "---").equals("")?"":ir[Integer.parseInt(props.getProperty("r_ir3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Advice Regarding Management:<br>
				<% String[] arm = list.loadData("mhReferral/AdviceRegardingManagement.txt", projecthome, path ); %>
				1. <b><%=props.getProperty("r_arm1", "---").equals("")?"":arm[Integer.parseInt(props.getProperty("r_arm1", "---"))-1] %></b><br>
				2. <b><%=props.getProperty("r_arm2", "---").equals("")?"":arm[Integer.parseInt(props.getProperty("r_arm2", "---"))-1] %></b><br>
				3. <b><%=props.getProperty("r_arm3", "---").equals("")?"":arm[Integer.parseInt(props.getProperty("r_arm3", "---"))-1] %></b>
				</td>
			</tr>
			<tr>
				<td class="mhSelect">Comments:<br>
				<%= props.getProperty("r_refComments", "")%></td>
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
