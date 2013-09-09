<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>

<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.billingCalendarPopup.title" /></title>
<script language="JavaScript">
<!--

function typeInDate(year1,month1,day1) {
  self.close();
  opener.document.serviceform.xml_vdate.value=year1+"-"+month1+"-"+day1;
}
function typeSrvDate(year1,month1,day1) {
  self.close();
  opener.document.serviceform.xml_appointment_date.value=year1+"-"+month1+"-"+day1;
}
//-->
</script>
</head>

<body bgcolor="ivory" onLoad="setfocus()" leftmargin="0" rightmargin="0">

<%
//to prepare calendar display  
String type = request.getParameter("type");
if (type == null) {
	type = "";
}

int year = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : 0;
int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : 0;
int delta = request.getParameter("delta")==null ? 0 : Integer.parseInt(request.getParameter("delta")); //add or minus month

GregorianCalendar now = new GregorianCalendar(year,month-1,1);
now.add(now.MONTH, delta);
year = now.get(Calendar.YEAR);
month = now.get(Calendar.MONTH)+1;

now.add(now.DATE, -1); 
DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
int [][] dateGrid = aDate.getMonthDateGrid();

String locationPrefix = request.getContextPath() + "/billing/CA/ON/";

%>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#FFD7C4">
		<td width="5%" nowrap><a
			href="billingCalendarPopup.jsp?year=<%=year-1%>&month=<%=month%>&delta=0&type=<%=type%>">
		<img src="<%= locationPrefix %>images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="Last Year" vspace="2"> <img src="<%= locationPrefix %>images/previous.gif"
			WIDTH="10" HEIGHT="9" BORDER="0" ALT="Last Year" vspace="2"></a></td>
		<td align="center"><a
			href="billingCalendarPopup.jsp?year=<%=year%>&month=<%=month%>&delta=-1&type=<%=type%>">
		<img src="<%= locationPrefix %>images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="View Last Month" vspace="2"> <bean:message
			key="billing.billingCalendarPopup.btnLast" /></a> <b><span
			CLASS=title><%=year%>-<%=month%></span></b> <a
			href="billingCalendarPopup.jsp?year=<%=year%>&month=<%=month%>&delta=1&type=<%=type%>">
		<bean:message key="billing.billingCalendarPopup.btnNext" /> <img
			src="<%= locationPrefix %>images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="View Next Month" vspace="2">&nbsp;&nbsp;</a></td>
		<td width="5%" align="right" nowrap><a
			href="billingCalendarPopup.jsp?year=<%=year+1%>&month=<%=month%>&delta=0&type=<%=type%>">
		<img src="<%= locationPrefix %>images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="Next Year" vspace="2"> <img src="<%= locationPrefix %>images/next.gif"
			WIDTH="10" HEIGHT="9" BORDER="0" ALT="Next Year" vspace="2"></a></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="1">
	<tr align="center" bgcolor="#FFFFFF">
		<th>
		<%
String[] arrayMonth = new String[] { "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" };
for(int i=0; i<12; i++) {
%> <a
			href="billingCalendarPopup.jsp?year=<%=year%>&month=<%=i+1%>&delta=0&type=<%=type%>">
		<font SIZE="2" <%=(i+1)==month?"color='red'":"color='blue'"%>><%=arrayMonth[i]%></a>
		<%}%>
		</th>
	</tr>
</table>


<table width="100%" border="1" cellspacing="0" cellpadding="2"
	bgcolor="silver">
	<tr bgcolor="#FOFOFO" align="center">
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
			color="red"><bean:message
			key="billing.billingCalendarPopup.msgSun" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="billing.billingCalendarPopup.msgMon" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="billing.billingCalendarPopup.msgTue" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="billing.billingCalendarPopup.msgWed" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="billing.billingCalendarPopup.msgThu" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
			key="billing.billingCalendarPopup.msgFri" /></font></td>
		<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
			color="green"><bean:message
			key="billing.billingCalendarPopup.msgSat" /></font></td>
	</tr>

	<%
for (int i=0; i<dateGrid.length; i++) {
	%> <tr> <%
	for (int j=0; j<7; j++) {
		if(dateGrid[i][j]==0) { 
			%><td></td><%
		}
		else {
			now.add(now.DATE, 1);

			if (type.compareTo("admission") == 0) {
%>
	<td align="center" bgcolor='#FBECF3'><a href="#"
		onClick="typeInDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">
	<%= dateGrid[i][j] %> </a></td>
	<%
			}  else {
%>
	<td align="center" bgcolor='#FBECF3'><a href="#"
		onClick="typeSrvDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">
	<%= dateGrid[i][j] %> </a></td>
	<%
			}
		}
	}
	%> </tr> <%
}
%>

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#FFD7C4" align="center"><input type="button"
			name="Cancel"
			value=" <bean:message key="billing.billingCalendarPopup.btnExit"/> "
			onClick="window.close()"></td>
	</tr>
</table>

</body>
</html:html>
