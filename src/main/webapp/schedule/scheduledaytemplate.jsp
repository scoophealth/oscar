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

%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*"
	errorPage="../appointment/errorpage.jsp"%>

<jsp:useBean id="providerNameBean" class="oscar.Dict" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%
	ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
%>

<% //save or delete the settings
  int rowsAffected = 0;
  if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").compareTo(" Save ")==0 ) {
    ScheduleTemplateCode code = scheduleTemplateCodeDao.getByCode(request.getParameter("code").toCharArray()[0]);
    if(code != null) {
    	scheduleTemplateCodeDao.remove(code.getId());
    }

    code = new ScheduleTemplateCode();
    code.setCode(request.getParameter("code").toCharArray()[0]);
    code.setDescription(request.getParameter("description"));
    code.setDuration(request.getParameter("duration"));
	code.setConfirm("N");
	code.setBookinglimit(1);
	scheduleTemplateCodeDao.persist(code);
  }
  if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals("Delete") ) {
    ScheduleTemplateCode code = scheduleTemplateCodeDao.getByCode(request.getParameter("delcode").toCharArray()[0]);
    if(code != null) {
    	scheduleTemplateCodeDao.remove(code.getId());
    }
  }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>DAY TEMPLATE SETTING</title>
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.addtemplatecode.code.focus();
  document.addtemplatecode.code.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkInput() {
	if(document.schedule.holiday_name.value == "") {
	  alert("Please check your input!!!");
	  return false;
	} else {
	  return true;
	}
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" width="100%">
	<tr>
		<td width="50" bgcolor="#009966">&nbsp;</td>
		<td align="center">

		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr bgcolor="#CCFFCC">
				<td>
				<p align="right">Provider:</p>
				</td>
				<td><input type="text" name="templatename" value="Delete">
				<select name="delcode">
					<%
					List<ScheduleTemplateCode> stcs = scheduleTemplateCodeDao.findAll();
					Collections.sort(stcs,ScheduleTemplateCode.CodeComparator);
					for(ScheduleTemplateCode stc: stcs) {
  
	%>
					<option value="<%=String.valueOf(stc.getCode())%>"><%=String.valueOf(stc.getCode())+" |"+stc.getDescription()%></option>
					<%
     }
	%>
				</select>
				<td></td>
			</tr>
			</form>
		</table>

		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
			<tr>
				<td width="50%" align="center">&nbsp;</td>
			</TR>
		</table>

		<table width="95%" border="1" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<form name="addtemplatecode" method="post"
				action="scheduletemplatecodesetting.jsp">
			<tr bgcolor="#FOFOFO" align="center">
				<td colspan=2><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="red">Appt Template Code</font></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red">Code:</font></td>
				<td><input type="text" name="code" size="1" maxlength="1"></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red">Description:</font></td>
				<td><input type="text" name="description" size="25"></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red">Duration:</font></td>
				<td><input type="text" name="duration" size="3" maxlength="3">
				mins.</td>
			</tr>
		</table>


		<table width="95%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO">
				<td></td>
				<td align="right"><input type="submit" name="dboperation"
					value=" Save "> <input type="button" name="Button"
					value=" Exit " onClick="window.close()"></td>
			</tr>
			</form>
		</table>
		<p align='left'>&nbsp; Code: only one char.<br>
		&nbsp; Description: less than 40 chars.<br>
		&nbsp; Duration: unit is minute.</p>

		</td>
	</tr>
</table>

</form>
</body>
</html>
