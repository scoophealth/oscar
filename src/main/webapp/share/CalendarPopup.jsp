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
<%--
  /*
    input: urlfrom and param
	output: urlfrom + "?year-day" + param
	or
	output: opener.param.substring("&formdatebox=".length()) = year1 + "-" + month1 + "-" + day1
  */
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
  
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%
String urlfrom = request.getParameter("urlfrom")==null?"":request.getParameter("urlfrom") ;
String param = request.getParameter("param")==null?"":request.getParameter("param") ;
//to prepare calendar display  
int year = Integer.parseInt(request.getParameter("year"));
int month = Integer.parseInt(request.getParameter("month"));
int delta = request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
GregorianCalendar now = new GregorianCalendar(year,month-1,1);

now.add(now.MONTH, delta);
year = now.get(Calendar.YEAR);
month = now.get(Calendar.MONTH)+1;

//the date of today
GregorianCalendar cal = new GregorianCalendar();
int todayDate = cal.get(Calendar.DATE);
boolean bTodayDate = false;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>CALENDAR</title>
<% if (session.getAttribute("mobileOptimized") != null) { %>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
<% } %>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<style type="text/css">
    td, th { font-size: 14px; }
</style>
<script language="JavaScript">
<!--

function typeInDate(year1,month1,day1) {

<%
    if (param.startsWith("&formdatebox=")) {
%>
  opener.<%=param.substring("&formdatebox=".length())%> = year1 + "-" + month1 + "-" + day1; 
<%
    } else {
%>  
  opener.location.href="<%=urlfrom%>"+"?year=" + year1 + "&month=" + month1 + "&day=" + day1 +"<%=param%>"; 
<%  }  %>  
  self.close();
}
//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" leftmargin="0" rightmargin="0">
<%
ResourceBundle oscarRec = ResourceBundle.getBundle("oscarResources");
String jan = oscarRec.getString("share.CalendarPopUp.msgJan");
String feb = oscarRec.getString("share.CalendarPopUp.msgFeb");
String mar = oscarRec.getString("share.CalendarPopUp.msgMar");
String apr = oscarRec.getString("share.CalendarPopUp.msgApr");
String may = oscarRec.getString("share.CalendarPopUp.msgMay");
String jun = oscarRec.getString("share.CalendarPopUp.msgJun");
String jul = oscarRec.getString("share.CalendarPopUp.msgJul");
String aug = oscarRec.getString("share.CalendarPopUp.msgAug");
String sep = oscarRec.getString("share.CalendarPopUp.msgSep");
String oct = oscarRec.getString("share.CalendarPopUp.msgOct");
String nov = oscarRec.getString("share.CalendarPopUp.msgNov");
String dec = oscarRec.getString("share.CalendarPopUp.msgDec");


String[] arrayMonth = new String[] { jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec };


now.add(now.DATE, -1); 
DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
int [][] dateGrid = aDate.getMonthDateGrid();
%>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<td width="5%" align="center" nowrap><a
			href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=-12">
		<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="<bean:message key="share.CalendarPopUp.msgNextYear"/>"
			vspace="2"> <img src="../images/previous.gif" WIDTH="10"
			HEIGHT="9" BORDER="0"
			ALT="<bean:message key="share.CalendarPopUp.msgLastYear"/>"
			vspace="2"> </a></td>
		<td align="center" nowrap><a
			href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=-1">
		<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="<bean:message key="share.CalendarPopUp.msgViewLastMonth"/>"
			vspace="2"> <bean:message
			key="share.CalendarPopUp.msgLastMonth" /> </a> <b><span CLASS=title><%=year%>-<%=month%></span></b>
		<a
			href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=1">
		<bean:message key="share.CalendarPopUp.msgNextMonth" /> <img
			src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="<bean:message key="share.CalendarPopUp.msgNextMonth"/>"
			vspace="2"></a></td>
		<td align='right'><a
			href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=12">
		<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
			ALT="Next Year" vspace="2"> <img src="../images/next.gif"
			WIDTH="10" HEIGHT="9" BORDER="0" ALT="Next Year" vspace="2"></a></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="2">
	<tr align="center" bgcolor="#FFFFFF">
		<th>
		<%
  for(int i=0; i<12; i++) {
%> <a
			href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=i+1%>&param=<%=URLEncoder.encode(param)%>"><font
			SIZE="2" <%=(i+1)==month?"color='red'":"color='blue'"%>><%=arrayMonth[i]%></a>
		<% } %>
		</th>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="2"
	bgcolor="silver">
	<tr bgcolor="#CCCCFF" align="center">
		<th width="14%"><font color="red"><bean:message
			key="share.CalendarPopUp.msgSun" /></font>
		</td>
		<th width="14%"><bean:message key="share.CalendarPopUp.msgMon" /></font>
		</td>
		<th width="14%"><bean:message key="share.CalendarPopUp.msgTue" /></font>
		</td>
		<th width="14%"><bean:message key="share.CalendarPopUp.msgWed" /></font>
		</td>
		<th width="14%"><bean:message key="share.CalendarPopUp.msgThu" /></font>
		</td>
		<th width="14%"><bean:message key="share.CalendarPopUp.msgFri" />
		</td>
		<th width="14%"><font color="green"><bean:message
			key="share.CalendarPopUp.msgSat" /></font>
		</td>
	</tr>

	<%
for (int i=0; i<dateGrid.length; i++) {
	out.println("<tr>");
	for (int j=0; j<7; j++) {
		if(dateGrid[i][j]==0) out.println("<td></td>");
		else {
			now.add(now.DATE, 1);
			if(todayDate == now.get(Calendar.DATE)) bTodayDate = true;
			else bTodayDate = false;
%>
	<td align="center" bgcolor='<%=bTodayDate?"gold":"#EEEEFF"%>'><a
		href="#"
		onClick="typeInDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">
	<%= dateGrid[i][j] %></a></td>
	<%
		}  
	}
	out.println("</tr>");
}
%>

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#CCCCFF" align="center"><input type="button"
			name="Cancel" value=" Exit " onClick="window.close()"></td>
	</tr>
</table>

</body>
</html>
