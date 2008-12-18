<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT DETAIL INFO</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}

var awnd=null;
function ScriptAttach() {

  awnd=rs('swipe','zdemographicswipe.htm',600,600,1);
  awnd.focus();
}

function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}


//-->
</script>



</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		DETAIL RECORD</font></th>
	</tr>
</table>

<%@ include file="zdemographicfulltitlesearch.htm"%>

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0, dob_year=0, dob_month=0, dob_date=0;
  
  int param = Integer.parseInt(request.getParameter("demographic_no"));
  // System.out.println("from editcpp : "+ param);
 
  ResultSet rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      dob_year = Integer.parseInt(rs.getString("year_of_birth"));
      dob_month = Integer.parseInt(rs.getString("month_of_birth"));
      dob_date = Integer.parseInt(rs.getString("date_of_birth"));
      if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
/*  if(!(rs.getString("month_of_birth").equals(""))) {//   ||rs.getString("year_of_birth")||rs.getString("date_of_birth")) {    	if(curMonth>Integer.parseInt(rs.getString("month_of_birth"))) {    		age=curYear-Integer.parseInt(rs.getString("year_of_birth"));    	} else {    		if(curMonth==Integer.parseInt(rs.getString("month_of_birth")) &&    			curDay>Integer.parseInt(rs.getString("date_of_birth"))) {    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"));    		} else {    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"))-1;     		}    	}	     }
*/   
%>
<table width="100%" border="0" cellspacing="1" cellpadding="1"
	bgcolor="#0066CC" bordercolor="#0066CC">
	<tr>
		<td bgcolor="#CCCCCC" bordercolor="#669966">
		<div align="center"><a href="#"
			onClick="popupPage(500,600,'demographicsummary.jsp?demographic_no=<%=rs.getString("demographic_no")%>')">Patient
		Summary</a></div>
		</td>
		<td bgcolor="#CCCCCC">
		<div align="center"><a href="">Lab Reports</a></div>
		</td>
		<td bgcolor="#CCCCCC">
		<div align="center"><a
			href='demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=rs.getString("last_name")%>&first_name=<%=rs.getString("first_name")%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'>Appt.History</a></div>
		</td>
		<td bgcolor="#CCCCCC" align="center"><!--a href="#" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
		<a
			href='../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'>Billing
		History</a></td>
	</tr>
</table>

<p>
<div align="center"><b><font color="navy">DEMOGRAPHIC
NO : <%=rs.getString("demographic_no")%></font></b></div>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="updatedelete" id="updatedelete"
		action="demographiccontrol.jsp"><input type="hidden"
		name="demographic_no" value="<%=rs.getString("demographic_no")%>">
	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=rs.getString("last_name")%>" onBlur="upCaseCtrl(this)">
		</td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=rs.getString("first_name")%>" onBlur="upCaseCtrl(this)">
		</td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Address: </b></td>
		<td align="left"><input type="text" name="address"
			value="<%=rs.getString("address")%>"></td>
		<td align="right"><b>City: </b></td>
		<td align="left"><input type="text" name="city"
			value="<%=rs.getString("city")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Province: </b></td>
		<td align="left"><input type="text" name="province"
			value="<%=rs.getString("province")%>"></td>
		<td align="right"><b>Postal: </b></td>
		<td align="left"><input type="text" name="postal"
			value="<%=rs.getString("postal")%>" onBlur="upCaseCtrl(this)">
		</td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Phone(H): </b></td>
		<td align="left"><input type="text" name="phone"
			value="<%=rs.getString("phone")%>"></td>
		<td align="right"><b>Phone(O):</b></td>
		<td align="left"><input type="text" name="phone2"
			value="<%=rs.getString("phone2")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>DOB</b><font size="-2">(yyyy-mm-dd)</font><b>:</b>
		</td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="year_of_birth"
					value="<%=rs.getString("year_of_birth")%>" size="4" maxlength="4">
				</td>
				<td>-</td>
				<td><input type="text" name="month_of_birth"
					value="<%=rs.getString("month_of_birth")%>" size="2" maxlength="2">
				</td>
				<td>-</td>
				<td><input type="text" name="date_of_birth"
					value="<%=rs.getString("date_of_birth")%>" size="2" maxlength="2">
				</td>
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
			value="<%=rs.getString("hin")%>"></td>
		<td align="right"><b>Ver.</b></td>
		<td align="left"><input type="text" name="ver"
			value="<%=rs.getString("ver")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Roster Status: </b></td>
		<td align="left"><input type="text" name="roster_status"
			value="<%=rs.getString("roster_status")%>" onBlur="upCaseCtrl(this)">
		</td>
		<td align="right" nowrap><b>Patient Status:</b> <b> </b></td>
		<td align="left"><input type="text" name="patient_status"
			value="<%=rs.getString("patient_status")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Date Joined: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="date_joined_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("date_joined"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="date_joined_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("date_joined"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="date_joined_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("date_joined"))%>">
				</td>
			</tr>
		</table>
		</td>
		<td align="right"><b>HC Type:</b></td>
		<td align="left"><input type="text" name="hc_type"
			value="<%=rs.getString("hc_type")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>MC Doctor: </b></td>
		<td align="left"><input type="text" name="provider_no"
			value="<%=rs.getString("provider_no")%>"></td>
		<td align="right"><b>Family Doctor: </b></td>
		<td align="left"><input type="text" name="family_doctor"
			value="<%=rs.getString("family_doctor")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>End Date: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="end_date_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("end_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="end_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("end_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="end_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("end_date"))%>">
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
					value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("eff_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="eff_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("eff_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="eff_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("eff_date"))%>">
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr valign="top">
		<td align="right" nowrap><b>PCN Indicator: </b></td>
		<td align="left"><input type="text" name="pcn_indicator"
			value="<%=rs.getString("pcn_indicator")%>"></td>
		<td align="right"><b>Chart No.:</b></td>
		<td align="left"><input type="text" name="chart_no"
			value="<%=rs.getString("chart_no")%>"></td>
	<tr valign="top">
		<td align="right" nowrap><b>HC Renew Date: </b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="hc_renew_date_year" size="4"
					maxlength="4"
					value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("hc_renew_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="hc_renew_date_month" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("hc_renew_date"))%>">
				</td>
				<td>-</td>
				<td><input type="text" name="hc_renew_date_date" size="2"
					maxlength="2"
					value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("hc_renew_date"))%>">
				</td>
			</tr>
		</table>
		</td>
		<td align="right"><b>Sex: </b></td>
		<td align="left"><input type="text" name="sex"
			value="<%=rs.getString("sex")%>" onBlur="upCaseCtrl(this)"></td>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td colspan="4">
		<table border=0 width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="20%"><input type="hidden" name="dboperation"
					value="update_record"> &nbsp;<input type="button"
					name="Button" value=" Back " onclick="history.go(-1);return false;">
				</td>
				<td width="40%" align='center'><input type="submit"
					name="displaymode" value="Update Record"> <input
					type="button" name="Button" value="Cancel" onclick=self.close();>
				</td>
				<td width="40%" align='right'><input type="button"
					name="Button" value="Swipe Card"
					onclick="window.open('zdemographicswipe.htm','', 'scrollbars=yes,resizable=yes,width=600,height=300')";>
				<input type="button" name="Button" value="Print Label"
					onclick="window.location='demographiclabelprintsetting.jsp?demographic_no=<%=rs.getString("demographic_no")%>'";>
				</td>
		</table>
	</tr>
	</form>
</table>
<%
    }
  }
%>
<p>
</body>
</html>