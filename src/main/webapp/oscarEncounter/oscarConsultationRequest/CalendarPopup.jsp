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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"%>
<%
  //to prepare calendar display
  String type = "admissfdfion";//request.getParameter("type");
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  //int day = now.get(Calendar.DATE);
  int delta = request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
  GregorianCalendar now = new GregorianCalendar(year,month-1,1);

 	now.add(now.MONTH, delta);
  year = now.get(Calendar.YEAR);
  month = now.get(Calendar.MONTH)+1;
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="OscarEncounter.oscarConsultationRequest.CalendarPopUp.title" /></title>
<script language="JavaScript">
<!--

function typeInDate(year1,month1,day1) {
  opener.serviceform.xml_vdate.value=year1+"-"+month1+"-"+day1;
  close();
}
function typeSrvDate(year1,month1,day1) {
opener.document.EctConsultationFormRequestForm.appointmentYear.value=year1;
opener.document.EctConsultationFormRequestForm.appointmentMonth.options.selectedIndex = (month1-1);
opener.document.EctConsultationFormRequestForm.appointmentDay.options.selectedIndex = (day1-1); close();
}
//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()">
<%
  now.add(now.DATE, -1);
  DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
  int [][] dateGrid = aDate.getMonthDateGrid();
%>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td BGCOLOR="#bbbbff" width="50%" align="center">
			<a href="CalendarPopup.jsp?year=<%=year%>&month=<%=month%>&delta=-1&type=<%=type%>">
				<img src="<%= request.getContextPath() %>/images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" 
					ALT="<bean:message key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgVLastMonth"/>"
					vspace="2"> <bean:message key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgLastMonth" />&nbsp;&nbsp;</a> 
			<b><span CLASS=title><%=year%>-<%=month%></span></b> 
			<a href="CalendarPopup.jsp?year=<%=year%>&month=<%=month%>&delta=1&type=<%=type%>">	&nbsp;&nbsp;<bean:message key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgNextMonth" />
				<img src="<%= request.getContextPath() %>/images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="<bean:message key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgVNextMonth"/>"
					vspace="2"></a>
		</td>
	</tr>
</table>
<p>
<table width="100%" border="1" cellspacing="0" cellpadding="2"
	bgcolor="silver">
	<tr bgcolor="#ccccff" align="center">
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
			color="red"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgSun" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgMon" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgTue" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgWed" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgThu" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgFri" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
			color="green"><bean:message
			key="oscarEncounter.oscarConsultationRequest.CalendarPopUp.msgSat" /></font></td>
	</tr>

	<%
              for (int i=0; i<dateGrid.length; i++) {
                out.println("<tr>");
                for (int j=0; j<7; j++) {
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    now.add(now.DATE, 1);

                      if (type.compareTo("admission") == 0) {
            %>
	<td align="center" bgcolor='#eeeeff'><a href="#"
		onClick="typeInDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">
	<%= dateGrid[i][j] %> </a></td>
	<%    }
                 else {
                 %>
	<td align="center" bgcolor='#eeeeff'><a href="#"
		onClick="typeSrvDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">
	<%= dateGrid[i][j] %> </a></td>
	<%
                 }

                 }

                }
                out.println("</tr>");
              }
            %>

</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#bbbbff">
		<div align="center"><input type="button" name="Cancel"
			value="<bean:message key="global.btnClose"/>"
			onClick="window.close()"></div>
		</td>
	</tr>
</table>

</body>
</html:html>
