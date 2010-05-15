<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
    response.setHeader("Cache-Control","no-cache");
    //The oscarEncounter session manager, if the session bean is not in the context it looks for a
    //session cookie with the appropriate name and value, if the required cookie is not available
    //it dumps you out to an erros page.

    oscar.oscarSecurity.SessionBean bean = null;
    if((bean=(oscar.oscarSecurity.SessionBean)request.getSession().getAttribute("SessionBean"))==null)
    {response.sendRedirect("error.jsp");
    return;}
%>

<% String demoNo = (String)  request.getAttribute("demoNo");%>


<html:html locale="true">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Patient Options</title>
<html:base />
</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}

function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarPatientOptions", windowprops);

  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>


<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors />
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%"
			style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2"
			height="0%" colspan="2">
		<p class="HelpAboutLogout"><span class="FakeLink"><a
			href="Help.htm">Help</a></span> | <span class="FakeLink"><a
			href="About.htm">About</a></span> | <span class="FakeLink"> <a
			href="Disclaimer.htm">Disclaimer</a></span></p>
		</td>
	</tr>
	<tr>
		<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
		<td width="100%" bgcolor="#000000"
			style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
		<p class="ScreenTitle"><bean:message key="patientOptions.title" /></p>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><bean:message
					key="patientOptions.title" /></div>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript:popupOscarRx(768,1024,'/oscarEncounter/PatientOption.do?avant=CR&demoNo=<%=demoNo%>')">
				Create a Consultation Request form for a patient </a></td>
			</tr>
			<tr>
				<td><a
					href="javascript:popupOscarRx(768,1024,'/oscarEncounter/PatientOption.do?avant=VA&demoNo=<%=demoNo%>')">
				View All Consultations for this patient in a new window </a></td>
			</tr>
			<tr>
				<td><a
					href="/oscarEncounter/PatientOption.do?avant=VA&demoNo=<%=demoNo%>">
				View All Consultations for this patient in this window </a></td>
			</tr>



			<tr>
				<td><a
					href="/oscarEncounter/IncomingEncounter.do?avant=VA&demoNo=<%=demoNo%>">
				View Chart </a></td>
			</tr>
			<tr>
				<td><a> View Imunization records for this patient </a></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>
</body>
</html:html>
