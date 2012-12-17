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
<%
  String user_no = (String) session.getAttribute("user");
  int  nItems=0;
  String strLimit1="0";
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="../admin/dbconnection.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@page import="org.oscarehr.common.model.ReportProvider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao" %>
<%@page import="org.oscarehr.common.model.BillingONCHeader1" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingONCHeader1Dao billingOnCHeaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String clinic="";
  String clinicview = oscarVariables.getProperty("clinic_view");

  String visitLocation = clinicLocationDao.searchVisitLocation(clinicview);
  if(visitLocation!=null) {
 	 clinic = visitLocation;
  }

  int flag = 0, rowCount=0;
  String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
  String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
  String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarReport.oscarReportVisitControl.title" /></title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="5">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<div align="right"><a
			href="manageProvider.jsp?action=visitreport" target="_blank"><font
			face="Arial, Helvetica, sans-serif" size="1"><bean:message
			key="oscarReport.oscarReportVisitControl.btnManageProviderList" /></font></a></div>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"><input type='button' name='print'
			value='<bean:message key="global.btnPrint"/>'
			onClick='window.print()'></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">&nbsp;<bean:message
			key="oscarReport.oscarReportVisitControl.msgTitle" /></font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="get"
		action="oscarReportVisitControl.jsp">
	<tr>
		<td width="30%" align="left"><font size="1" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="radio" name="reportAction"
			onClick="document.serviceform.providerview.disabled=true" value="lk"
			<%=reportAction.equals("lk")?"checked":""%>> <bean:message
			key="oscarReport.oscarReportVisitControl.msgLarryKainReport" /> <input
			type="radio" name="reportAction"
			onClick="document.serviceform.providerview.disabled=false" value="vr"
			<%=reportAction.equals("vr")?"checked":""%>> <bean:message
			key="oscarReport.oscarReportVisitControl.msgVisitReport" /></font></td>
		<td width="60%" align="left"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#333333"><b><bean:message
			key="oscarReport.oscarReportVisitControl.msgSelectProvider" /> </b></font> <select
			name="providerview" <%=reportAction.equals("lk")?"disabled":""%>>
			<option value="%"><bean:message
				key="oscarReport.oscarReportVisitControl.msgSelectProviderAll" /></option>
			<% String proFirst="";
            String proLast="";
            String proOHIP="";
            for(ReportProvider rps : reportProviderDao.findByAction("visitreport")) {
            	Provider p = providerDao.getProvider(rps.getProviderNo());
          
                proFirst = p.getFirstName();
                proLast = p.getLastName();
                proOHIP = p.getProviderNo();
               %>
			<option value="<%=proOHIP%>"><%=proLast%>, <%=proFirst%></option>
			<% } %>
		</select></td>
		<td width="10%"><font color="#333333" size="1"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="submit" name="Submit"
			value="<bean:message key="oscarReport.oscarReportVisitControl.btnCreateReport"/>">
		</font></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="left"><font color="#003366"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>
		<font color="#333333"> <bean:message
			key="oscarReport.oscarReportVisitControl.msgServiceDateRange" /></font></b></font></font>
		&nbsp; &nbsp; <font size="1" face="Arial, Helvetica, sans-serif">
		<a href="#"
			onClick="MM_openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message
			key="oscarReport.oscarReportVisitControl.msgBeginDate" />:</a></font> <input
			type="text" name="xml_vdate" value="<%=xml_vdate%>"> <font
			size="1" face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="MM_openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message
			key="oscarReport.oscarReportVisitControl.msgEndDate" />:</a></font> <input
			type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"></div>
		</td>
	</tr>
	</form>
</table>
<% if (reportAction.compareTo("") == 0 || reportAction == null) { %>
<p>&nbsp;</p>
<% } else {
       if (reportAction.compareTo("lk") == 0) { %>
<%@ include file="oscarReportVisit_lk.jspf"%>
<%     }
   }
%>

<%@ include file="../demographic/zfooterbackclose.jsp"%>

</body>
</html>
