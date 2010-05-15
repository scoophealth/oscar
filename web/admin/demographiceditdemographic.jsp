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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT DETAIL INFO - demographiceditdemographic</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
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

<%@ include file="zdemographicfulltitlesearch.jsp"%>

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  
  int param = Integer.parseInt(request.getParameter("demographic_no"));
  // System.out.println("from editcpp : "+ param);
 
  ResultSet rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
     if(!(apptMainBean.getString(rs,"month_of_birth").equals(""))) {//   ||apptMainBean.getString(rs,"year_of_birth")||apptMainBean.getString(rs,"date_of_birth")) {
    	if(curMonth>Integer.parseInt(apptMainBean.getString(rs,"month_of_birth"))) {
    		age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
    	} else {
    		if(curMonth==Integer.parseInt(apptMainBean.getString(rs,"month_of_birth")) &&
    			curDay>Integer.parseInt(apptMainBean.getString(rs,"date_of_birth"))) {
    			age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
    		} else {
    			age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"))-1; 
    		}
    	}	
     }
        
%>
<table width="100%" border="1" cellspacing="1" cellpadding="1">
	<tr>
		<td>
		<div align="center"><a href="">Problem list</a></div>
		</td>
		<td>
		<div align="center"><a href="">Lab Reports</a></div>
		</td>
		<td>
		<div align="center"><a
			href='admincontrol.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'>Appt.History</a></div>
		</td>
		<td>
		<div align="center"><a href="">Enc. Hist.</a></div>
		</td>
	</tr>
</table>

<div align="center"><b><font color="navy">DEMOGRAPHIC
NO : <%=apptMainBean.getString(rs,"demographic_no")%></font></b></div>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="updatedelete" action="admincontrol.jsp">
	<input type="hidden" name="demographic_no"
		value="<%=apptMainBean.getString(rs,"demographic_no")%>">
	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=apptMainBean.getString(rs,"last_name")%>"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=apptMainBean.getString(rs,"first_name")%>"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Address: </b></td>
		<td align="left"><input type="text" name="address"
			value="<%=apptMainBean.getString(rs,"address")%>"></td>
		<td align="right"><b>City: </b></td>
		<td align="left"><input type="text" name="city"
			value="<%=apptMainBean.getString(rs,"city")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Province: </b></td>
		<td align="left"><input type="text" name="province"
			value="<%=apptMainBean.getString(rs,"province")%>"></td>
		<td align="right"><b>Postal: </b></td>
		<td align="left"><input type="text" name="postal"
			value="<%=apptMainBean.getString(rs,"postal")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Phone(H): </b></td>
		<td align="left"><input type="text" name="phone"
			value="<%=apptMainBean.getString(rs,"phone")%>"></td>
		<td align="right"><b>Phone(O):</b></td>
		<td align="left"><input type="text" name="phone2"
			value="<%=apptMainBean.getString(rs,"phone2")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>DOB</b><font size="-2">(yyyy-mm-dd)</font><b>:</b>
		</td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="year_of_birth"
					value="<%=apptMainBean.getString(rs,"year_of_birth")%>" size="4"
					maxlength="4"></td>
				<td>-</td>
				<td><input type="text" name="month_of_birth"
					value="<%=apptMainBean.getString(rs,"month_of_birth")%>" size="2"
					maxlength="2"></td>
				<td>-</td>
				<td><input type="text" name="date_of_birth"
					value="<%=apptMainBean.getString(rs,"date_of_birth")%>" size="2"
					maxlength="2"></td>
			</tr>
		</table>
		</td>
		<td align="right"><b>Age: </b></td>
		<td align="left"><input type="text" name="age" value="<%=age%>">
		</td>
	</tr>
	<tr valign="top">
		<td align="right"><b>HIN: </b></td>
		<td align="left"><input type="text" name="hin"
			value="<%=apptMainBean.getString(rs,"hin")%>"></td>
		<td align="right"><b>Ver.</b></td>
		<td align="left"><input type="text" name="ver"
			value="<%=apptMainBean.getString(rs,"ver")%>"></td>
	</tr>
	<!--tr valign="top"> 
      <td align="right"><b>Roster Status: </b> </td>
      <td align="left" > 
        <input type="text" name="roster_status" value="<%=apptMainBean.getString(rs,"roster_status")%>">
      </td>
      <td align="right"> <b>Patient Status:</b> <b> </b></td>
      <td align="left"> 
        <input type="text" name="patient_status" value="<%=apptMainBean.getString(rs,"patient_status")%>">
      </td>
    </tr-->
	<tr valign="top">
		<td align="right"><b>Date Joined: </b></td>
		<td align="left"><input type="text" name="date_joined"
			value="<%=apptMainBean.getString(rs,"date_joined")%>"></td>
		<td align="right"><b>HC Type: </b></td>
		<td align="left"><input type="text" name="hc_type"
			value="<%=apptMainBean.getString(rs,"hc_type")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Doctor: </b></td>
		<td align="left"><input type="text" name="provider_no"
			value="<%=apptMainBean.getString(rs,"provider_no")%>"></td>
		<td align="right"><b>Sex: </b></td>
		<td align="left"><input type="text" name="sex"
			value="<%=apptMainBean.getString(rs,"sex")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>End Date: </b></td>
		<td align="left"><input type="text" name="end_date"
			value="<%=apptMainBean.getString(rs,"end_date")%>"></td>
		<td align="right"><b>EFF Date: </b></td>
		<td align="left"><input type="text" name="eff_date"
			value="<%=apptMainBean.getString(rs,"eff_date")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>PCN Indicator: </b></td>
		<td align="left"><input type="text" name="pcn_indicator"
			value="<%=apptMainBean.getString(rs,"pcn_indicator")%>"></td>
		<td align="right"><b>Chart No.:</b></td>
		<td align="left"><input type="text" name="chart_no"
			value="<%=apptMainBean.getString(rs,"chart_no")%>"></td>
	<tr valign="top">
		<td align="right"><b>HC Renew Date: </b></td>
		<td align="left"><input type="text" name="hc_renew_date"
			value="<%=apptMainBean.getString(rs,"hc_renew_date")%>"></td>
		<td align="right"><b>Family Doctor: </b></td>
		<td align="left"><input type="text" name="family_doctor"
			value="<%=apptMainBean.getString(rs,"family_doctor")%>"></td>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td colspan="4">
		<div align="center"><input type="hidden" name="dboperation"
			value="demographic_update_record"> <input type="hidden"
			name="displaymode" value="Demographic_Update"> <input
			type="submit" name="subbutton" value="Update Record"> <a
			href='admincontrol.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&displaymode=Demographic_Delete&dboperation=demographic_delete'>
		<img src="../images/buttondelete.gif" width="73" height="28"
			border="0" align="absmiddle" alt="Delete the Record"></a> <!--input type="button" name="Button" value="Cancel" onclick=self.close();-->

		<%--
	     if (apptMainBean.getString(rs,"sex").compareTo("F") == 0) {
         out.println("<input type=\"button\" name=\"Button2\" value=\"Antenatal Care Planner\" onClick=\"window.location='../ob/risks.jsp?demographic_no="+apptMainBean.getString(rs,"demographic_no")+"'\">");
	     }
	  --%></div>
		</td>
	</tr>
	</form>
</table>
<%
    }
  }
%>
<%@ include file="footer.jsp"%>

</body>
</html>
