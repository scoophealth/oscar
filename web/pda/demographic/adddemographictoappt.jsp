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
<title>PATIENT SEARCH INFO</title>
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
function goback(){
 history.go(-1);return false;
}
//-->
</script>
</head>
<!--base target="pt_srch_main"-->
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		DETAIL RECORD</font></th>
	</tr>
</table>

<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#C4D9E7">
	<form method="post" name="titlesearch" action="../cpp/cppcontrol.jsp">
	<%--@ include file="zcpptitlesearch.htm"--%>
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i>Search </i></b></font></td>

		<td width="10%" nowrap><font size="1" face="Verdana"
			color="#0000FF"> <input type="radio" checked
			name="search_mode" value="search_name"> Name </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_phone">
		Phone</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_dob"> DOB</font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="demographic_no"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="search_titlename"> <INPUT
			TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT TYPE="hidden"
			NAME="limit2" VALUE="10"> <input type="hidden"
			name="displaymode" value="Search "> <input type="SUBMIT"
			name="displaymode" value="Search " size="17">
	</tr>
	<tr>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_address">
		Address </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_hin"> HIN</font></td>
		<td></td>
	</tr>
	<%
  String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("demographic_no") || temp.equals("dboperation") ||temp.equals("displaymode")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
         //System.out.println();
  }
	%>
	</form>
</table>
<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  
  if(request.getParameter("demographic_no").equals("")) {
%>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="addrec" action="../cpp/cppcontrol.jsp">
	<%@ include file="zcppaddarecord.htm"%> <%
  //String temp=null; store appt data
	for (Enumeration e = request.getParameterNames() ;  e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("demographic_no") || temp.equals("dboperation") ||temp.equals("displaymode")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
         //System.out.println();
  }
	%>
	
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td colspan="4" align="center"><input type="hidden"
			name="dboperation" value="add_record"> <input type="submit"
			name="displaymode" value="Add Record & Back to Appointment">
		<input type="button" name="Button" value="Cancel"
			onclick="self.close();"></td>
	</tr>
	</form>
</table>

<%
  } else {
  int param = Integer.parseInt(request.getParameter("demographic_no"));
  System.out.println("from editcpp : "+ param);
  String dboperation="search_detail";
 
  ResultSet rs = apptMainBean.queryResults(param, dboperation);
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
     if(!(rs.getString("month_of_birth").equals(""))) {//   ||rs.getString("year_of_birth")||rs.getString("date_of_birth")) {
    	if(curMonth>Integer.parseInt(rs.getString("month_of_birth"))) {
    		age=curYear-Integer.parseInt(rs.getString("year_of_birth"));
    	} else {
    		if(curMonth==Integer.parseInt(rs.getString("month_of_birth")) &&
    			curDay>Integer.parseInt(rs.getString("date_of_birth"))) {
    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"));
    		} else {
    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"))-1; 
    		}
    	}	
     }
%>
<div align="center"><b><font color="navy">Demographic
No : <%=rs.getString("demographic_no")%></font></b></div>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="updatedelete" action="../cpp/cppcontrol.jsp">
	<input type="hidden" name="demographic_no"
		value="<%=rs.getString("demographic_no")%>">
	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=rs.getString("last_name")%>"></td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=rs.getString("first_name")%>"></td>
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
			value="<%=rs.getString("postal")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Phone: </b></td>
		<td align="left"><input type="text" name="phone"
			value="<%=rs.getString("phone")%>"></td>
		<td align="right"><b>Ext.:</b></td>
		<td align="left"><input type="text" name="ext"
			value="<%=rs.getString("ext")%>"></td>
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
		<td align="left"><input type="text" name="age" value="<%=age%>">
		</td>
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
			value="<%=rs.getString("roster_status")%>"></td>
		<td align="right"><b>Patient Status:</b> <b> </b></td>
		<td align="left"><input type="text" name="patient_status"
			value="<%=rs.getString("patient_status")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Date Joined: </b></td>
		<td align="left"><input type="text" name="date_joined"
			value="<%=rs.getString("date_joined")%>"></td>
		<td align="right"><b>Chart No.:</b></td>
		<td align="left"><input type="text" name="chart_no"
			value="<%=rs.getString("chart_no")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>Doctor: </b></td>
		<td align="left"><input type="text" name="provider_no"
			value="<%=rs.getString("provider_no")%>"></td>
		<td align="right"><b>Sex: </b></td>
		<td align="left"><input type="text" name="sex"
			value="<%=rs.getString("sex")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>End Date: </b></td>
		<td align="left"><input type="text" name="end_date"
			value="<%=rs.getString("end_date")%>"></td>
		<td align="right"><b>EFF Date: </b></td>
		<td align="left"><input type="text" name="eff_date"
			value="<%=rs.getString("eff_date")%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>PCN Indicator: </b></td>
		<td align="left"><input type="text" name="pcn_indicator"
			value="<%=rs.getString("pcn_indicator")%>"></td>
		<td align="right"><b>HC Type: </b></td>
		<td align="left"><input type="text" name="hc_type"
			value="<%=rs.getString("hc_type")%>"></td>
	<tr valign="top">
		<td align="right"><b>HC Renew Date: </b></td>
		<td align="left"><input type="text" name="hc_renew_date"
			value="<%=rs.getString("hc_renew_date")%>"></td>
		<td align="right">&nbsp;</td>
		<td align="left">&nbsp;</td>
	<tr>
		<td colspan="4">&nbsp;</td>
		<%
  //String temp=null; store appt data
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("demographic_no") || temp.equals("dboperation") ||temp.equals("displaymode")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
         //System.out.println();
  }
	%>
	</tr>
	<tr bgcolor="#486ebd">
		<td colspan="4">
		<div align="center"><input type="hidden" name="name"
			value="<%=rs.getString("last_name")+","+rs.getString("first_name")%>">
		<input type="hidden" name="dboperation" value="update_record">
		<input type="submit" name="displaymode"
			value="Update Record & Back to Appointment"> <!--a href='cppcontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&displaymode=delete&dboperation=delete'> 
          <img src="../images/buttondelete.gif" width="73" height="28" border="0" align="absmiddle" alt="Delete the Record"></a-->
		<input type="button" name="Button" value="Cancel" onclick="goback()">
		</div>
		</td>
	</tr>
	</form>
</table>
<%
    }
  }
  }
%>

</body>
</html>