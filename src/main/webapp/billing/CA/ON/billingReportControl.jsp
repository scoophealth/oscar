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
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed2) {
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
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>

<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

int flag = 0, rowCount=0;
String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="right"><a href=#
			onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">
		<font size="1">Manage Provider List </font></a></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><b><font face="Verdana, Arial" color="#FFFFFF" size="3">oscarBilling</font></b></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="post"
		action="billingReportControl.jsp">
	<tr>
		<td width="50%" align="right"><font size="2" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="radio" name="reportAction" value="unbilled"
			<%=reportAction.equals("unbilled")?"checked":""%>>Unbilled <input
			type="radio" name="reportAction" value="billed"
			<%=reportAction.equals("billed")?"checked":""%>>Billed <input
			type="radio" name="reportAction" value="unsettled"
			<%=reportAction.equals("unsettled")?"checked":""%>>Unsettled
		<input type="radio" name="reportAction" value="billob"
			<%=reportAction.equals("billob")?"checked":""%>>OB <input
			type="radio" name="reportAction" value="flu"
			<%=reportAction.equals("flu")?"checked":""%>>FLU</font></td>
		<td width="30%" align="right" nowrap><font
			face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333">
		<b>Select provider </b></font> <select name="providerview">

			<% 
String proFirst="";
String proLast="";
String proOHIP="";
String specialty_code; 
String billinggroup_no;
int Count = 0;

for(Object[] res:reportProviderDao.search_reportprovider("billingreport")) {
	ReportProvider rp = (ReportProvider)res[0];
	Provider p = (Provider)res[1];
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
		</select></td>
		<td align="center"><font color="#333333" size="2"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="submit" name="Submit" value="Create Report"> </font></td>
	</tr>
	<tr>
		<td></td>
		<td align="right"><B>Date</B> &nbsp; <font size="1"
			face="Arial, Helvetica, sans-serif"> <a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">From:</a></font>
		<input type="text" name="xml_vdate" size="10" value="<%=xml_vdate%>">
		<font size="1" face="Arial, Helvetica, sans-serif"> <a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">
		To:</a></font> <input type="text" name="xml_appointment_date" size="10"
			value="<%=xml_appointment_date%>"></td>
		<td></td>
	</tr>
	</form>
</table>

<%
if (reportAction.compareTo("") == 0 || reportAction == null) {
%>
<p>&nbsp;</p>
<% 
} else if (reportAction.compareTo("unbilled") == 0) {  
%>
<%@ include file="billingReport_unbilled.jspf"%>
<%
} else if (reportAction.compareTo("billed") == 0) {
%>
<%@ include file="billingReport_billed.jspf"%>
<%
} else if (reportAction.compareTo("unsettled") == 0) {
%>
<%@ include file="billingReport_unsettled.jspf"%>
<%
} else if (reportAction.compareTo("billob") == 0){
%>
<%@ include file="billingReport_billob.jspf"%>
<%
} else if (reportAction.compareTo("flu") == 0) {
%>
<%@ include file="billingReport_flu.jspf"%>
<%
}
%>

<br>

<hr width="100%">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href=# onClick="javascript:history.go(-1);return false;">
		<img src="images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"> Back </a></td>
		<td align="right"><a href="" onClick="self.close();">Close
		the Window<img src="images/rightarrow.gif" border="0" width="25"
			height="20" align="absmiddle"></a></td>
	</tr>
</table>

</body>
</html>
