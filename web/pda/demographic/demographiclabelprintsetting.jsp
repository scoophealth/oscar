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
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
  String curProvider_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<%//operation available to the client - dboperation
  String [][] dbQueries=new String[][] {
    {"search_detail", "select * from demographic where demographic_no=?"},
  };
  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">

<script language="JavaScript">
<!--

function onNewPatient() {
  document.labelprint.label1no.value="1";
  document.labelprint.label2no.value="6";
  document.labelprint.label3no.value="0";
}
function checkTotal() {
  var total = 0+ document.labelprint.label1no.value + document.labelprint.label2no.value + document.labelprint.label3no.value;
  if(total>7) return false;
  return true;
}
//-->
</script>
</head>
<body background="../images/gray_bg.jpg" bgcolor="white"
	bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		LABELS</font></th>
	</tr>
</table>

<%
	GregorianCalendar now=new GregorianCalendar();  int curYear = now.get(Calendar.YEAR);  int curMonth = (now.get(Calendar.MONTH)+1);  int curDay = now.get(Calendar.DAY_OF_MONTH);
	int age=0, dob_year=0, dob_month=0, dob_date=0;
  String first_name="",last_name="",chart_no="",address="",city="",province="",postal="",phone="",phone2="",dob="",sex="",hin="";
  int param = Integer.parseInt(request.getParameter("demographic_no"));
  // System.out.println("from editcpp : "+ param);
 
  ResultSet rs = apptMainBean.queryResults(param, "search_detail");
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      dob_year = Integer.parseInt(rs.getString("year_of_birth"));
      dob_month = Integer.parseInt(rs.getString("month_of_birth"));
      dob_date = Integer.parseInt(rs.getString("date_of_birth"));
      if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
      
      first_name = rs.getString("first_name");
      last_name = rs.getString("last_name");
      chart_no = rs.getString("chart_no");
      address = rs.getString("address");
      city = rs.getString("city");
      province = rs.getString("province");
      postal = rs.getString("postal");
      phone = rs.getString("phone");
      phone2 = rs.getString("phone2");
      dob=dob_year+"/"+dob_month+"/"+dob_date;
      sex = rs.getString("sex");
      hin = "HN "+ rs.getString("hc_type") +" "+rs.getString("hin")+ " " +rs.getString("ver");
    }
  }
  String label1 = "<font face=\"Courier New, Courier, mono\" size=\"2\"><b>" +last_name+ ", " +first_name+ "</b><br>&nbsp;&nbsp;&nbsp;&nbsp;" +hin+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;" +dob+ " " +sex+ "<br><br><b>" +last_name+ ", " +first_name+ "</b><br>&nbsp;&nbsp;&nbsp;&nbsp;" +hin+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;" +dob+ " " +sex+ "<br></font>";
  String label2 = "<font face=\"Courier New, Courier, mono\" size=\"2\"><b>" +last_name+ ", " +first_name+ "  &nbsp;" +chart_no+ "</b><br>" +address+ "<br>" +city+ ", " +province+ ", " +postal+ "<br>Home: " +phone+ "<br>" +dob+ " " +sex+ "<br>" +hin+ "<br>Bus: " +phone2+ "<br></font>";
  String label3 = "<font face=\"Courier New, Courier, mono\" size=\"2\">" +last_name+ ", " +first_name+ "<br>" +address+ "<br>" +city+ ", " +province+ ", " +postal+ "<br></font>";
%>

<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="labelprint"
		action="demographicprintdemographic.jsp">
	<tr bgcolor="gold" align="center">
		<td>Label</td>
		<td>Number of Label
		<td>Location</td>
	</tr>
	<tr>
		<td align="center"><br>
		<table width="90%" border="1" cellspacing="0" cellpadding="0"
			bgcolor="#FFFFFF">
			<tr>
				<td><%=label1%></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC"><a href="#"
			onClick="onNewPatient()">New Patient Label</a><br>
		<input type="checkbox" name="label1checkbox" value="checked" checked>
		<input type="text" name="label1no" size="2" maxlength="2" value="1">
		</td>
		<td bgcolor="#999999" rowspan="3" valign="middle" align="right">
		<p>left: <input type="text" name="left" size="3" maxlength="3"
			value="155"> px</p>
		<p>top: <input type="text" name="top" size="3" maxlength="3"
			value="0"> px</p>
		<p>height: <input type="text" name="height" size="3" maxlength="3"
			value="145"> px</p>
		<p>gap: <input type="text" name="gap" size="3" maxlength="3"
			value="0"> px</p>
		</td>
	</tr>
	<tr>
		<td align="center"><br>
		<table width="90%" border="1" cellspacing="0" cellpadding="0"
			bgcolor="#FFFFFF">
			<tr>
				<td><%=label2%></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC"><input type="checkbox"
			name="label2checkbox" value="checked" checked> <input
			type="text" name="label2no" size="2" maxlength="2" value="1">
		</td>
	</tr>
	<tr>
		<td align="center"><br>
		<table width="90%" border="1" cellspacing="0" cellpadding="0"
			bgcolor="#FFFFFF">
			<tr>
				<td><%=label3%></td>
			</tr>
		</table>
		<br>
		</td>
		<td align="center" bgcolor="#CCCCCC"><input type="checkbox"
			name="label3checkbox" value="checked" checked> <input
			type="text" name="label3no" size="2" maxlength="2" value="1">
		</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td align="center" colspan="3"><input type="submit" name="Submit"
			value="Print Preview/Print"> <input type="button"
			name="button" value="Back"
			onClick="javascript:history.go(-1);return false;"></td>
	</tr>
	<input type="hidden" name="label1" value='<%=label1%>'>
	<input type="hidden" name="label2" value='<%=label2%>'>
	<input type="hidden" name="label3" value='<%=label3%>'>
	</form>
</table>

</body>
</html>