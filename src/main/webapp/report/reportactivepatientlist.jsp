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
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%@page import="oscar.util.ConversionUtils"%>
<%
  
  String strLimit1="0";
  String strLimit2="50";  
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerNameBean" class="oscar.Dict" scope="page" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="reportMainBeanConn.jspf"%>
<% } %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportactivepatientlist.title" />
</title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css">
<script language="JavaScript">
<!--
function setfocus() {
//  document.titlesearch.keyword.focus();
//  document.titlesearch.keyword.select();
}
//-->
</SCRIPT>

</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER><font face="Helvetica" color="#FFFFFF"><bean:message
			key="report.reportactivepatientlist.msgTitle" /></font></th>
		<th align="right" width="10%" NOWRAP><input type="button"
			name="Button" value="<bean:message key="global.btnPrint" />"
			onClick="window.print()"> <input type="button" name="Button"
			value="<bean:message key="global.btnCancel" />"
			onClick="window.close()"></th>
	</tr>
</table>

<CENTER>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="0"
	cellpadding="1">
	<tr bgcolor="silver">
		<TH align="center" width="12%" nowrap><b><bean:message
			key="report.reportactivepatientlist.msgLastName" /></b></TH>
		<TH align="center" width="12%"><b><bean:message
			key="report.reportactivepatientlist.msgFirstName" /> </b></TH>
		<TH align="center" width="5%"><b><bean:message
			key="report.reportactivepatientlist.msgChart" /></b></TH>
		<TH align="center" width="5%"><b><bean:message
			key="report.reportactivepatientlist.msgAge" /></b></TH>
		<TH align="center" width="5%"><b><bean:message
			key="report.reportactivepatientlist.msgSex" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="report.reportactivepatientlist.msgHIN" /></b></TH>
		<TH align="center" width="5%"><b><bean:message
			key="report.reportactivepatientlist.msgVer" /></b></TH>
		<TH align="center" width="16%"><b><bean:message
			key="report.reportactivepatientlist.msgMCDoc" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="report.reportactivepatientlist.msgDateJoined" /></b></TH>
		<TH align="center" width="15%"><b><bean:message
			key="report.reportactivepatientlist.msgPhone" /></b></TH>
	</tr>
	<%
  int age=0;
  ResultSet rs=null ;
  int[] itemp1 = new int[2];
  itemp1[1] = Integer.parseInt(strLimit1);
  itemp1[0] = Integer.parseInt(strLimit2);
  rs = reportMainBean.queryResults(itemp1, "search_demo_active");

  boolean bodd=false;
  int nItems=0;
  
  while (rs.next()) {
    bodd=bodd?false:true; //for the color of rows
    nItems++; 
   	int yearOfBirthInt = ConversionUtils.fromIntString(reportMainBean.getString(rs,"year_of_birth"));
   	int monthOfBirthInt = ConversionUtils.fromIntString(reportMainBean.getString(rs,"month_of_birth"));
   	int dateOfBirthInt = ConversionUtils.fromIntString(reportMainBean.getString(rs,"date_of_birth"));
    if(yearOfBirthInt != 0 && monthOfBirthInt != 0 && dateOfBirthInt != 0) {
    	age=MyDateFormat.getAge(yearOfBirthInt, monthOfBirthInt, dateOfBirthInt);
    }
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<td nowrap><%=reportMainBean.getString(rs,"last_name")%></td>
		<td nowrap><%=reportMainBean.getString(rs,"first_name")%></td>
		<td align="center"><%=reportMainBean.getString(rs,"chart_no")%>
		</td>
		<td align="center"><%=age%></td>
		<td align="center"><%=reportMainBean.getString(rs,"sex")%></td>
		<td><%=reportMainBean.getString(rs,"hin")%></td>
		<td align="center"><%=reportMainBean.getString(rs,"ver")%></td>
		<td><%=reportMainBean.getString(rs,"provider_no").length()>11?reportMainBean.getString(rs,"provider_no").substring(0,11):reportMainBean.getString(rs,"provider_no")%></td>
		<td><%=reportMainBean.getString(rs,"date_joined")%></td>
		<td><%=reportMainBean.getString(rs,"phone")%></td>
	</tr>
	<%
  }

if(reportMainBean.getBDoConfigure()) reportMainBean.setBDoConfigure();
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportactivepatientlist.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportactivepatientlist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportactivepatientlist.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportactivepatientlist.msgNextPage" /></a> <%
  }
%>

</body>
</html:html>
