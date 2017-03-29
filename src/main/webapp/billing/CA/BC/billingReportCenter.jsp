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
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%    
  String user_no = (String) session.getAttribute("user");
  int  nItems=0;
     String strLimit1="0";
    String strLimit2="5";
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>

<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>

<%
GregorianCalendar now=new GregorianCalendar(); 
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  
  
  %>
<% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" href="../web.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<div align="right"><a href=#
			onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')"><font
			face="Arial, Helvetica, sans-serif" size="1">Manage Provider
		List </font></a></div>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing</font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="get" action="billingReportControl.jsp">
	<tr>
		<td width="30%" align="right"><font size="2" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="radio" name="reportAction" value="unbilled"> Unbilled <input
			type="radio" name="reportAction" value="billed"> Billed <!--<input type="radio" name="reportAction" value="unsettled">
        Unsettled
        <input type="radio" name="reportAction" value="billob">
        OB
         <input type="radio" name="reportAction" value="flu">
        FLU</font>--></td>
		<td width="50%">
		<div align="right"></div>
		<div align="center"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b>Select
		provider </b></font> <select name="providerview">
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no;
           int Count = 0;
           for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
				ReportProvider rp = (ReportProvider)result[0];
				Provider p = (Provider)result[1];
				
				 proFirst = p.getFirstName();
				 proLast = p.getLastName();
				 proOHIP = p.getProviderNo();

%>
			<option value="<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%
      }      
  
  %>
		</select></div>
		</td>
		<td width="20%"><font color="#333333" size="2"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="submit" name="Submit" value="Create Report"> </font></td>
	</tr>
	<tr>
		<td width="19%">
		<div align="right"><font color="#003366"><font
			face="Arial, Helvetica, sans-serif" size="2"><b> <font
			color="#333333">Service Date-Range</font></b></font></font></div>
		</td>
		<td width="41%">
		<div align="center"><input type="text" name="xml_vdate"
			value="<%=xml_vdate%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_vdate&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">Begin:</a></font>
		</div>
		</td>
		<td width="40%"><input type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_appointment_date&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">End:</a></font>
		</td>
	</tr>
	</form>
</table>
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
<p>&nbsp;</p>
<%@ include file="../../../demographic/zfooterbackclose.jsp"%>

</body>
</html>
