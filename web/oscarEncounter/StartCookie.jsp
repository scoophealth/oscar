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
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.StartCookie.title" /></title>
<script type="text/javascript" language=javascript>
//tilde operator function variables
    var handlePressState = 0;
    var handlePressFocus = "none";

function popupTwo(page){
popupStart1(700,980,page);
//popupStart2(200,200,'Help.jsp');
}

function popupStart1(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "1", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
}


}
function popupStart2(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "2", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}


</script>

<html:base />
<%
    Cookie cookie = new Cookie("myCookie","myValue");
    cookie.setPath("/");
    response.addCookie(cookie);
%>

<body>
<%
String provider_no      ="xyz";
String appointment_no   ="8";
String demographic_no   ="10000040";
String curProvider_no   ="174";
String reason           ="101112";
String username         ="131415";
String appointment_date ="2002-6-12";
String start_time       ="192021";
String todaysDate       ="eggs";
String status           ="222324";
%>
<a name="Bob"
	href="javascript:popupStart1(700,980,'/oscarEncounter/IncomingEncounter.do?appointmentNo=5&demographicNo=10000048&curProvideNo=174&reason=&userName=David+H+Chan&appointmentDate=2002-6-14&startTime=10:0&status=t')"><bean:message
	key="oscarEncounter.StartCookie.msgLaunch" /></a>

<a name="Bob"
	href="javascript:popupStart1(700,980,'/oscarEncounter/IncomingEncounter.do?providerNo=<%=provider_no%>&appointmentNo=<%=appointment_no%>&demographicNo=<%=demographic_no%>&curProviderNo=<%=curProvider_no%>&reason=<%=reason%>&userName=<%=username%>&appointmentDate=<%=appointment_date%>&startTime=<%=start_time%>&status=<%=status%>')"><bean:message
	key="oscarEncounter.StartCookie.msgLaunch" /></a>
<table bgcolor="#555555">
	<tr>
		<td>#555555</td>
	</tr>
</table>
<table bgcolor="#6699cc">
	<tr>
		<td>#6699cc</td>
	</tr>
</table>
<table bgcolor="#f1f1f1">
	<tr>
		<td>#f1f1f1</td>
	</tr>
</table>
<table bgcolor="#003399">
	<tr>
		<td>#003399</td>
	</tr>
</table>
<table bgcolor="#ff3300">
	<tr>
		<td>#ff3300</td>
	</tr>
</table>

<table bgcolor="#fbe249">
	<tr>
		<td>#fbe249</td>
	</tr>
</table>
<table bgcolor="#666699">
	<tr>
		<td>#666699</td>
	</tr>
</table>
<table bgcolor="#555555">
	<tr>
		<td>#555555</td>
	</tr>
</table>
<table bgcolor="#ff3300">
	<tr>
		<td>#ff3300</td>
	</tr>
</table>
<table bgcolor="#ff3300">
	<tr>
		<td>#ff3300</td>
	</tr>
</table>
<table bgcolor="#ff3300">
	<tr>
		<td>#ff3300</td>
	</tr>
</table>
<table bgcolor="#ff3300">
	<tr>
		<td>#ff3300</td>
	</tr>
</table>
</body>
</html:html>

