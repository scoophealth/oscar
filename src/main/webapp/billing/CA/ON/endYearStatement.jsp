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
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/oscar/js/global.js"></script>
<title>End Year Statement</title>
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="../../../web.css">
<link rel="stylesheet" type="text/css" media="all" href="../../../share/css/extractedFromPages.css"  />
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../../../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
	  window.location = page;
}
function demographicSearch() {
	var search_param = document.forms[0].lastNameParam.value;
	var url = '../../../admin/admincontrol.jsp';
	url += '?originalpage='+escape('../billing/CA/ON/endYearStatement.do?demosearch=true');
	url += '&displaymode=Demographic_Admin_Reports';
	url += '&search_mode=search_name';
	url += '&orderby=last_name, first_name';
	url += '&dboperation=demographic_admin_reports';
	url += '&limit1=0&limit2=5';
	url += '&keyword='+search_param;
	popupPage(700,1000,url,'master');
	return false;	
}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}
//-->
</script>

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
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<html:form action="billing/CA/ON/endYearStatement">
	<tr>
		<td width="550px" align="center"><font size="3"> 
			First Name: <html:text property="firstNameParam" size="16" style="font-size:16px" ></html:text> &nbsp;&nbsp;
		</font></td>
		<td width="50px" align="left" nowrap>Date From:</td>
		<td width="200px" align="left" nowrap>
				<input type="text" name="fromDateParam" id="fromDateParam" onDblClick="calToday(this)" size="10" >
				 <img src="../../../images/cal.gif" id="fromDateCal"> 
		</td>
		<td align="left" width="200px"><font size="3"><input type="submit" name="search" value="Create Statement"> &nbsp;&nbsp;</font></td>
	</tr>
	<tr>
		<td width="550px" align="center"><font size="3"> 
			Last Name: <html:text property="lastNameParam" size="16" style="font-size:16px" ></html:text> &nbsp;&nbsp;
			<input type="button" value="Search" onclick="demographicSearch()"></input>
		</font></td>
		<td width="50px" align="left" nowrap>Date To:</td>
		<td width="200px" align="left" nowrap>
				<input type="text" name="toDateParam" id="toDateParam" onDblClick="calToday(this)" size="10" > 
				<img src="../../../images/cal.gif" id="toDateCal">
		</td>
		<td align="left"><input type="submit" name="pdf" value="Print PDF" <logic:empty name="result">disabled="disabled"</logic:empty> ></td>
	</tr>
	</html:form>
</table>
<div style="color"red"><html:errors/></div>
<logic:notEmpty name="summary">
<table border="0" width="100%">
<tr>
<td width="15px"/>
<td>
<table style="border:collapse" width="100%">
	<tr>
		<td width="50px">&nbsp;</td>
		<td align="left" colspan="2">Patient Information</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Patient:</td>
		<td>
			<bean:write name="summary" property="patientNo" />&nbsp;&nbsp;
			<bean:write name="summary" property="patientName" />&nbsp;&nbsp;
			<bean:write name="summary" property="hin" />&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Address :</td>
		<td>
			<bean:write name="summary" property="address" />
		</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Phone :</td>
		<td>
			<bean:write name="summary" property="phone" />
		</td>
	</tr>
</table>
</logic:notEmpty>
<br/>
<logic:notEmpty name="result">
<table border="0" width="100%">
	<tr>
		<td width="10px"/>
		<td>
			<table border="1" cellspacing="0" cellpadding="0" width="100%"
	bordercolorlight="#99FF99" bordercolordark="#FFFFFF" bgcolor="#FFFFFF">
				<tr bgcolor=#ccffcc>
					<th>INVOICE NUMBER</th>
					<th>INVOICE DATE</th>
					<th>SERVICE CODE</th>
					<th>INVOICED</th>
					<th>PAID</th>
				</tr>
<logic:iterate id="row" name="result" indexId="counter"> 
				<tr bgcolor="#CEF6CE" >
					<td><bean:write name="row" property="invoiceNo" /></td>
					<td><bean:write name="row" property="invoiceDate" /></td>
					<td>&nbsp;</td>
					<td><bean:write name="row" property="invoiced" /></td>
					<td><bean:write name="row" property="paid" /></td>
				</tr>
<logic:iterate id="service" name="row" property="services" indexId="counterService"> 
				<tr bgcolor="<%= counterService.intValue()%2 == 0 ? "ivory" : "#EEEEFF" %>" >
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><bean:write name="service" property="code" /></td>
					<td><bean:write name="service" property="fee" /></td>
					<td>&nbsp;</td>
				</tr>
</logic:iterate>
</logic:iterate>
				<tr height="10">
					<td colspan="5" style="border:collapse">&nbsp;</td>
				</tr>
				<tr bgcolor="#99FF66" >
					<td>Count: &nbsp;&nbsp;&nbsp; <logic:notEmpty name="summary"><bean:write name="summary" property="count" /></logic:notEmpty></td>
					<td>&nbsp;</td>
					<td align="center">Total:</td>
					<td><logic:notEmpty name="summary"><bean:write name="summary" property="invoiced" /></logic:notEmpty></td>
					<td><logic:notEmpty name="summary"><bean:write name="summary" property="paid" /></logic:notEmpty></td>
				</tr>
			</table>
</logic:notEmpty>
		</td>
		<td width="10px"/>
	</tr>
</table>			
</td>
<td width="15px"/>
</tr>
</body>
<script type="text/javascript">
Calendar.setup( { inputField : "fromDateParam", ifFormat : "%Y/%m/%d", showsTime :false, button : "fromDateCal", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "toDateParam", ifFormat : "%Y/%m/%d", showsTime :false, button : "toDateCal", singleClick : true, step : 1 } );
document.getElementById('fromDateParam').value = '<%= request.getAttribute("fromDateParam") != null ? request.getAttribute("fromDateParam") : "" %>';
document.getElementById('toDateParam').value = '<%= request.getAttribute("toDateParam") != null ? request.getAttribute("toDateParam") : "" %>';
</script>
</html>
