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
  if(session.getValue("patient") == null)    response.sendRedirect("../logout.jsp");
  String demographic_no = (String) session.getAttribute("demo_no");
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*" errorPage="../appointment/errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="oscar.util.ConversionUtils" %>

<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>	

<%
 
  int age=0, dob_year=0, dob_month=0, dob_date=0;
  Demographic d = demographicDao.getDemographic(demographic_no);
  if (d != null) { 
      dob_year = Integer.parseInt(d.getYearOfBirth());
      dob_month = Integer.parseInt(d.getMonthOfBirth());
      dob_date = Integer.parseInt(d.getDateOfBirth());
      if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);

%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>YOUR DEMOGRAPHIC RECORD</title>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">Hello
		<%=d.getFirstName()%> <%=d.getLastName()%>
		</font></th>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="1"
	bgcolor="#0066CC" bordercolor="#0066CC">
	<tr>
		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="er.jsp">Emergency Record</a></td>

		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="sa.jsp">Self Assessment</a></td>

		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="sc.jsp">Smart Checkup</a></td>

		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="ma.jsp">Make Appointment</a></td>

		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="mu.jsp">Mail Us</a></td>

		<td bgcolor="#CCCCCC" bordercolor="#669966" align="center"><a
			href="logout.jsp">Finish & Exit</a></td>

	</tr>
</table>

<p>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="updatedelete" id="updatedelete"
		action="demographiccontrol.jsp"><input type="hidden"
		name="demographic_no" value="10000046">
	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=d.getLastName()%>"></td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=d.getFirstName()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Address: </b></td>
		<td align="left"><input type="text" name="address"
			value="<%=d.getAddress()%>"></td>
		<td align="right"><b>City: </b></td>
		<td align="left"><input type="text" name="city"
			value="<%=d.getCity()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Province: </b></td>
		<td align="left"><input type="text" name="province"
			value="<%=d.getProvince()%>"></td>
		<td align="right"><b>Postal: </b></td>
		<td align="left"><input type="text" name="postal"
			value="<%=d.getPostal()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Phone(H): </b></td>
		<td align="left"><input type="text" name="phone"
			value="<%=d.getPhone()%>"></td>
		<td align="right"><b>Phone(O):</b></td>
		<td align="left"><input type="text" name="phone2"
			value="<%=d.getPhone2()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>DOB</b><font size="-2">(yyyy-mm-dd)</font><b>:</b>
		</td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="year_of_birth"
					value="<%=d.getYearOfBirth()%>" size="4"
					maxlength="4"></td>
				<td>-</td>
				<td><input type="text" name="month_of_birth"
					value="<%=d.getMonthOfBirth()%>" size="2"
					maxlength="2"></td>
				<td>-</td>
				<td><input type="text" name="date_of_birth"
					value="<%=d.getDateOfBirth()%>" size="2"
					maxlength="2"></td>
			</tr>
		</table>
		</td>
		<td align="right"><b>Age: </b></td>
		<td align="left"><input type="text" name="age" readonly
			value="<%=age%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>HIN: </b></td>
		<td align="left"><input type="text" name="hin"
			value="<%=d.getHin()%>"></td>
		<td align="right"><b>Ver.</b></td>
		<td align="left"><input type="text" name="ver"
			value="<%=d.getVer()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Roster Status: </b></td>
		<td align="left"><input type="text" name="roster_status"
			value="<%=d.getRosterStatus()%>"></td>
		<td align="right" nowrap><b>Patient Status:</b> <b> </b></td>
		<td align="left"><input type="text" name="patient_status"
			value="<%=d.getPatientStatus()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Date Joined: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="date_joined_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(ConversionUtils.toDateString(d.getDateJoined()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="date_joined_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(ConversionUtils.toDateString(d.getDateJoined()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="date_joined_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(ConversionUtils.toDateString(d.getDateJoined()))%>">
				</td>
			</tr>
		</table>
		</td>
		<td align="right"><b>HC Type:</b></td>
		<td align="left"><input type="text" name="hc_type"
			value="<%=d.getHcType()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>MC Doctor: </b></td>
		<td align="left"><input type="text" name="provider_no"
			value="<%=d.getProviderNo()%>"></td>
		<td align="right"><b>Family Doctor: </b></td>
		<td align="left"><input type="text" name="family_doctor"
			value="<%=d.getFamilyDoctor()%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>End Date: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="end_date_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(ConversionUtils.toDateString(d.getEndDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="end_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(ConversionUtils.toDateString(d.getEndDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="end_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(ConversionUtils.toDateString(d.getEndDate()))%>">
				</td>
			</tr>
		</table>
		</td>
		<td align="right"><b>EFF Date: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="eff_date_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(ConversionUtils.toDateString(d.getEffDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="eff_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(ConversionUtils.toDateString(d.getEffDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="eff_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(ConversionUtils.toDateString(d.getEffDate()))%>">
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr valign="top">
		<td align="right" nowrap><b>PCN Indicator: </b></td>
		<td align="left"><input type="text" name="pcn_indicator"
			value="<%=d.getPcnIndicator()%>"></td>
		<td align="right"><b>Your PIN No.:</b></td>
		<td align="left"><input type="text" name="chart_no"
			value="<%=d.getChartNo()%>"></td>
	<tr valign="top">
		<td align="right" nowrap><b>HC Renew Date: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="hc_renew_date_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(ConversionUtils.toDateString(d.getHcRenewDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="hc_renew_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(ConversionUtils.toDateString(d.getHcRenewDate()))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="hc_renew_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(ConversionUtils.toDateString(d.getHcRenewDate()))%>">
				</td>
			</tr>
		</table>
		</td>
		<td align="right"><b>Sex: </b></td>
		<td align="left"><input type="text" name="sex"
			value="<%=d.getSex()%>"></td>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	</form>
</table>
<%
  }
%>

<p>
</body>
</html>
