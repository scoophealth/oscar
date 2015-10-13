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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>

<%
	String user_no = (String) session.getAttribute("user");
	String providerview = request.getParameter("providerview") == null ? 
		"all":request.getParameter("providerview") ;
%>

<%@ page import="java.util.*, oscar.login.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>


<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
	GregorianCalendar now=new GregorianCalendar(); 
	int curYear = now.get(Calendar.YEAR);
	int curMonth = (now.get(Calendar.MONTH)+1);
	int curDay = now.get(Calendar.DAY_OF_MONTH);

	String xml_vdate = request.getParameter("xml_vdate") == null ? 
		"" : request.getParameter("xml_vdate");

	String xml_appointment_date = request.getParameter("xml_appointment_date") == null ? 
		"" : request.getParameter("xml_appointment_date");
%>

<%
	List<String> header_values = new ArrayList<String>();
	List<Properties> column_values = new ArrayList<Properties>();
	List<String> total_values = new ArrayList<String>();
	Properties prop = null;

	String action = request.getParameter("reportAction") == null ? 
		"unbilled" : request.getParameter("reportAction");

	// Set default start and end dates
	Date current_date = new Date();
	Date start_date = null;
	Date end_date = null;
	if(xml_vdate == "")
	{
		start_date = new Date(
				current_date.getYear(),
				current_date.getMonth(),
				0
			);
	}
	else
	{
		start_date = new Date(xml_vdate);
	}

	if(xml_appointment_date == "")
	{
		end_date = current_date;
	}
	else
	{
		end_date = new Date(xml_appointment_date);
	}

	if("unbilled".equals(action)) 
	{
		header_values.add("SERVICE DATE");
		header_values.add("TIME");
		header_values.add("PATIENT");
		header_values.add("DESCRIPTION");
		header_values.add("COMMENTS");

		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
		List<Appointment> unBilledAppointments = 
			appointmentDao.findPatientUnbilledAppointmentsByProviderAndAppointmentDate(
				providerview,
				start_date,
				end_date
			);

		for( Appointment unBilledAppointment:unBilledAppointments)
		{
			String service_start_date = unBilledAppointment.getAppointmentDate().toString();
			String appointment_start_time = unBilledAppointment.getStartTime().toString();
			String demographic_no = Integer.toString(unBilledAppointment.getDemographicNo());
			String provider_no = unBilledAppointment.getProviderNo();
			String appointment_no = Integer.toString(unBilledAppointment.getId());
			Demographic demographic = demographicDao.getDemographic(demographic_no);
			String demographic_name = demographic.getFullName();

			prop = new Properties();
			prop.setProperty("SERVICE DATE", service_start_date);
			prop.setProperty("TIME", appointment_start_time);
			prop.setProperty("PATIENT", demographic_name);
			
			String status = unBilledAppointment.getStatus();
			String reason = unBilledAppointment.getReason();
			String note = unBilledAppointment.getNotes();

			prop.setProperty("DESCRIPTION", reason);

			prop.setProperty("COMMENTS", note);
			column_values.add(prop);
		}
	}
	else if("billed".equals(action)) 
	{
		header_values.add("SERVICE DATE");
		header_values.add("TIME");
		header_values.add("PATIENT");
		header_values.add("DESCRIPTION");
		header_values.add("ACCOUNT");


		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
		List<Appointment> billedAppointments = 
			appointmentDao.findPatientBilledAppointmentsByProviderAndAppointmentDate(
			providerview,
			start_date,
			end_date
			);

		for( Appointment billedAppointment:billedAppointments)
		{
			String service_start_date = billedAppointment.getAppointmentDate().toString();
			String appointment_start_time = billedAppointment.getStartTime().toString();
			String demographic_no = Integer.toString(billedAppointment.getDemographicNo());
			String provider_no = billedAppointment.getProviderNo();
			String appointment_no = Integer.toString(billedAppointment.getId());
			Demographic demographic = demographicDao.getDemographic(demographic_no);
			String demographic_name = demographic.getFullName();

			prop = new Properties();
			prop.setProperty("SERVICE DATE", service_start_date);
			prop.setProperty("TIME", appointment_start_time);
			prop.setProperty("PATIENT", demographic_name);
			
			String status = billedAppointment.getStatus();
			String reason = billedAppointment.getReason();
			String note = billedAppointment.getNotes();

			prop.setProperty("DESCRIPTION", reason);

			String clinicaid_link = "../../billing/billingClinicAid.jsp?" + 
				"billing_action=create_invoice&" +
				"demographic_no=" + demographic_no + "&service_start_date=" + 
				URLEncoder.encode(service_start_date, "UTF-8") +
				"&appointment_no=" + appointment_no +
				"&appointment_provider_no=" + provider_no +
				"&chart_no=" +
				"&appointment_start_time=" + appointment_start_time;

			String billing_url = "<a href=# onClick='popupPage(700,720, \"" +
				clinicaid_link + "\"); return false;'>Bill</a>";

			prop.setProperty("ACCOUNT", billing_url);
			column_values.add(prop);
		}
	}
%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Clincaid Billed Appointment Report</title>
<link rel="stylesheet" href="../../../web.css">
<link rel="stylesheet" type="text/css" media="all" href="../../../share/css/extractedFromPages.css"  />
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../../../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
	   adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CCCCFF">
		<td width="5%"></td>
		<td width="80%" align="left">
		<p><b><font face="Verdana, Arial" color="#FFFFFF" size="3"><a
			href="billingReportCenter.jsp">OSCARbilling</a></font></b></p>
		</td>
		<td align="right"><a href=#
			onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">
		<font size="1">Manage Provider List </font></a></td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="post" action="billingClinicaidReport.jsp">
	<tr>
		<td width="30%" align="center"><font size="2"> <input
			type="radio" name="reportAction" value="unbilled"
			<%="unbilled".equals(action)? "checked" : "" %>>Unbilled <input
			type="radio" name="reportAction" value="billed"
			<%="billed".equals(action)? "checked" : "" %>>Billed <!--  input type="radio" name="reportAction" value="paid" <%="paid".equals(action)? "checked" : "" %>>Paid 
	<input type="radio" name="reportAction" value="unpaid" <%="unpaid".equals(action)? "checked" : "" %>>Unpaid -->
			</font></td>
			<td width="20%" align="right" nowrap><b>Provider </b></font> 
			<select
				name="providerview">
				<% 
					String proFirst="";
					String proLast="";
					String proOHIP="";
					String specialty_code; 
					String billinggroup_no;
					int Count = 0;


					for(Object[] res:reportProviderDao.search_reportprovider("billingreport"))
					{
						ReportProvider rp = (ReportProvider)res[0];
						Provider p =  (Provider)res[1];
						proFirst = p.getFirstName();
						proLast = p.getLastName();
						proOHIP = p.getProviderNo();

	  
				%>
				<option value="<%=proOHIP%>"
					<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
					<%=proFirst%>
				</option>
				<%
					}      
				%>
			</select>
			
			</td>
			<td align="center" nowrap><font size="1"> From:</font> <input
				type="text" name="xml_vdate" id="xml_vdate" size="10"
				value="<%=xml_vdate%>"> <font size="1"> <img
				src="../../../images/cal.gif" id="xml_vdate_cal"> To:</font> <input
				type="text" name="xml_appointment_date" id="xml_appointment_date"
				onDblClick="calToday(this)" size="10"
				value="<%=xml_appointment_date%>"> <img
				src="../../../images/cal.gif" id="xml_appointment_date_cal"></td>
			<td align="right"><input type="submit" name="Submit"
				value="Create Report"> </font></td>
		</tr>
		<tr>
			</form>
	</table>

	<table border="1" cellspacing="0" cellpadding="0" width="100%"
		bordercolorlight="#99A005" bordercolordark="#FFFFFF" bgcolor="#FFFFFF">
		<tr bgcolor=<%="#ccffcc" %>>
			<% for (int i=0; i<header_values.size(); i++) {%>
			<th><%=header_values.get(i) %></th>
			<% } %>
			<% for (int i=0; i<column_values.size(); i++) {%>
		
		<tr bgcolor="<%=i%2==0? "ivory" : "#EEEEFF" %>">
			<% for (int j=0; j<header_values.size(); j++) {
			prop = (Properties)column_values.get(i);
		%>
			<td align="center"><%=prop.getProperty((String)header_values.get(j), "&nbsp;") %>&nbsp;</td>
			<% } %>
		</tr>
		<% } %>

		<% if(total_values.size() > 0) { %>
		<tr bgcolor="silver">
			<% for (int i=0; i < total_values.size(); i++) {%>
			<th><%=total_values.get(i) %>&nbsp;</th>
			<% } %>
		</tr>
		<% } %>

	</table>

	<br>

	<hr width="100%">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td><a href=# onClick="javascript:history.go(-1);return false;">
			<img src="../../../images/leftarrow.gif" border="0" width="25" height="20"
				align="absmiddle"> Back </a></td>
			<td align="right"><a href="" onClick="self.close();">Close
			the Window<img src="../../../images/rightarrow.gif" border="0" width="25"
				height="20" align="absmiddle"></a></td>
		</tr>
	</table>

	</body>
	<script type="text/javascript">
	Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );
	Calendar.setup( { inputField : "xml_appointment_date", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 } );
	</script>
	</html>
	<%! 
	String getFormatDateStr(String str) {
		String ret = str;
		if(str.length() == 8) {
			ret = str.substring(0,4) + "/" + str.substring(4,6) + "/" + str.substring(6);
		}
		return ret;
	}
%>
