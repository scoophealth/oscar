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
  String apptProvider_no, user_no, username;
  user_no = (String) session.getAttribute("user");
  apptProvider_no = request.getParameter("curProvider_no");
  username =  request.getParameter("username").toUpperCase();
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PRESCRIBE</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--

//-->
</SCRIPT>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PRESCRIBE</font></th>
	</tr>
</table>
<%
   String demoname=null,dob=null,gender=null,hin=null,roster=null;
   int dob_year = 0, dob_month = 0, dob_date = 0;
   //dboperation=search_demograph
   List<Map> resultList = oscarSuperManager.find("providerDao", request.getParameter("dboperation"), new Object[] {request.getParameter("demographic_no")});
   for (Map demo : resultList) {
     demoname=demo.get("last_name")+", "+demo.get("first_name");
     dob_year = Integer.parseInt(String.valueOf(demo.get("year_of_birth")));
     dob_month = Integer.parseInt(String.valueOf(demo.get("month_of_birth")));
     dob_date = Integer.parseInt(String.valueOf(demo.get("date_of_birth")));
     dob=dob_year+"-"+dob_month+"-"+dob_date;
     gender=String.valueOf(demo.get("sex"));
     hin=String.valueOf(demo.get("hin"));
     roster=String.valueOf(demo.get("roster_status"));
   }
 
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
  boolean bNewDemoAcc=true;
  resultList = oscarSuperManager.find("providerDao", "search_demographicaccessory", new Object[] {request.getParameter("demographic_no")});
  for (Map demo : resultList) {
    String content=String.valueOf(demo.get("content"));
    bNewDemoAcc=false;
%>
<xml id="xml_list">
<encounteraccessory>
<%=content%>
</encounteraccessory>
</xml>
<%
  } 
%>
<table width="100%" cellspacing="0" cellpadding="0" border="0">
	<form name="encounter" method="post" action="providercontrol.jsp">
	<tr>
		<td>


		<table width="100%" border="0">
			<tr>
				<td align="left"><font color="blue"><%=demoname%> <i><%=age%></i>
				<%=gender%> <i>RS: <%=roster==null?"NONE":roster%></i></font></td>
				<td align="right">
			</tr>
		</table>
		<table width="100%" border="0" bgcolor="silver" datasrc='#xml_list'>
			<tr>
				<td width="50%" align="center">Problem List:<br>
				<textarea name="xml_Problem_List" style="width: 100%" cols="30"
					rows="2" <%=bNewDemoAcc?"":"datafld='xml_Problem_List'"%>></textarea>

				</td>
				<td width="50%" align="center">Medication:<br>
				<textarea name="xml_dup_Medication" style="width: 100%" cols="30"
					rows="2" <%=bNewDemoAcc?"":"datafld='xml_Medication'"%>></textarea>
				<!--duplicate the value just the same as the following by xml data island-->
				</td>
			</tr>
			<tr>
				<td>
				<div align="center">Alert:<br>
				<textarea name="xml_Alert" style="width: 100%" cols="30" rows="2"
					<%=bNewDemoAcc?"":"datafld='xml_Alert'"%>></textarea></div>
				</td>
				<td>
				<div align="center">Family Social History:<br>
				<textarea name="xml_Family_Social_History" style="width: 100%"
					cols="30" rows="2"
					<%=bNewDemoAcc?"":"datafld='xml_Family_Social_History'"%>></textarea>
				</div>
				</td>
			</tr>
		</table>

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<td><font color="#FFFFFF"><%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>
				| <%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)%> <%=username%>
				</font></td>
				<td>This Prescribe</td>
			</tr>
		</table>

		<table border="0" width="100%" datasrc='#xml_list'>
			<tr>
				<td>
				<div align="center">Medication<br>
				<textarea name='xml_Medication' style="width: 100%" cols='60'
					rows='10' <%=bNewDemoAcc?"":"datafld='xml_Medication'"%>></textarea>
				</div>
				<input type='hidden' name='xml_subjectprefix' value='.'></td>
			</tr>
			</talbe>
			<table border="0" width="100%">
				<tr>
					<td nowrap align="center" colspan="2">
					<p><input type="hidden" tabindex="5" name="demographic_no"
						value="<%=request.getParameter("demographic_no")%>"> <input
						type="hidden" tabindex="6" name="prescribe_date"
						value='<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>'>
					<input type="hidden" tabindex="7" name="prescribe_time"
						value='<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)%>'>
					<input type="hidden" tabindex="8" name="user_no"
						value='<%=user_no%>'> <!--input type="hidden" tabindex="9" name="template" value='<%=request.getParameter("template")%>'-->
					<input type="hidden" tabindex="10" name="dboperation"
						value="save_prescribe"> <input type="hidden" tabindex="11"
						name="displaymode" value="saveprescribe"> <input
						type="submit" tabindex="12" name="submit" value=" Save ">
					<input type="button" tabindex="13" name="Button" value="Cancel"
						onClick="window.close();"></p>
					</td>
				</tr>
			</table>

			</td>
		</tr>
	</form>
</table>
</body>
</html>