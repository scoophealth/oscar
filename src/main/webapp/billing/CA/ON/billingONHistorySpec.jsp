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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.DateRange"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
 
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
 
  String strDay="0";
  if(request.getParameter("day")!=null) strDay = request.getParameter("day");
  
  Calendar calendar = Calendar.getInstance();
  String strToday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE);
  calendar.add(Calendar.DATE, Integer.parseInt(strDay)*(-1));
  String strStartDay = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE);;

  DateRange pDateRange= new DateRange(MyDateFormat.getSysDate(strStartDay), MyDateFormat.getSysDate(strToday));
  
  String serviceCode = request.getParameter("serviceCode")!=null? request.getParameter("serviceCode") : "";
  
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>BILLING HISTORY</title>
<link rel="stylesheet" href="billingON.css">
<script language="JavaScript">
<!--

function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}

//-->
</SCRIPT>
</head>
<body topmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="myDarkGreen">
		<th><font color="#FFFFFF">BILLING HISTORY </font></th>
	</tr>
</table>

<form method="post" name="titlesearch" action="billingONHistorySpec.jsp">
<table width="95%" border="0">
	<tr>
		<td align="left"><%=request.getParameter("demo_name")%> (<%=request.getParameter("demographic_no")%>)
		<%=strToday + " - " + strStartDay %></td>
		<td align="right">Service Code <input type="text"
			name="serviceCode" value="<%=serviceCode %>" size="4"
			onBlur="upCaseCtrl(this)" /> <input type="hidden" name="day"
			value="<%=strDay %>" /> <input type="hidden" name="demo_name"
			value="<%=request.getParameter("demo_name") %>" /> <input
			type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no") %>" /> <input
			type="submit" name="submit" value="Search" /></td>
	</tr>
</table>
</form>

<CENTER>
<table width="100%" border="0" bgcolor="#ffffff">
	<tr class="myYellow">
		<TH width="12%"><b>Invoice No.</b></TH>
		<TH width="20%"><b>Appt. Date</b></TH>
		<TH width="15%"><b>Bill Type</b></TH>
		<TH width="35%"><b>Service Code</b></TH>
		<TH width="5%"><b>Dx</b></TH>
		<TH><b>Fee</b></TH>
	</tr>
	<% // new billing records
JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
String limit = "";

List aL = dbObj.getBillingHist(loggedInInfo, request.getParameter("demographic_no"), 10000000, 0, pDateRange);
int nItems=0;
for(int i=0; i<aL.size(); i=i+2) {
	BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(i);
	BillingItemData itObj = (BillingItemData) aL.get(i+1);
	String strServiceCode = itObj.getService_code();
	if(!serviceCode.equals("")) {
		if(strServiceCode.indexOf(serviceCode) < 0) {
			continue;
		}
	}
%>
	<tr bgcolor="<%=i%2==0?"#CCFF99":"white"%>">
		<td width="5%" align="center" height="25"><%=obj.getId()%></td>
		<td align="center"><%=obj.getBilling_date()%> <%--=obj.getBilling_time()--%></td>
		<td align="center"><%=BillingDataHlp.getPropBillingType().getProperty(obj.getStatus(),"")%></td>
		<td align="center"><%=strServiceCode%></td>
		<td align="center"><%=itObj.getDx()%></td>
		<td align="center"><%=obj.getTotal()%></td>
	</tr>
	<% 
	nItems++;
}
%>
	<tr class="myYellow">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td align="center"><%=nItems %></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>
<br>
<p>
<hr width="100%">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td align="right"><a href="" onClick="self.close();">Close
		the Window<img src="images/rightarrow.gif" border="0" width="25"
			height="20" align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html>
