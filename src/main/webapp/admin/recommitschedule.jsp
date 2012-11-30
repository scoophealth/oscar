<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested"
	prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<title>Schedule to resubmit hsfo xml</title>
</head>
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
function  setCopy(){
	document.inputForm.isCheck.value=document.inputForm.copyCheck.checked;
	
}

function confirmSch(){
	
	return confirm("Are you sure you want to schedule above time to resubmit the HSFO XML?");
}
</script>
<body>
<c:if test="${requestScope.schedule_message!=null}">
	<script type="text/javascript">
window.close();
</script>
</c:if>
<c:if test="${requestScope.schedule_message==null}">
	<span style="color: red"><c:out
		value="${requestScope.schedule_err}" /></span>
	<h3>Schedule to resubmit hsfo xml</h3>
	<nested:form action="/admin/RecommitHSFO2">
		<nested:hidden property="method" value="saveSchedule" />
		<nested:hidden property="isCheck" />
		<nested:hidden property="schedule_flag" />
		<nested:hidden property="schedule_id" />
		<nested:hidden property="lastlog_flag" />
		<nested:hidden property="lastlog" />
		<nested:hidden property="check_flag" />
		<table>
			<tr>
				<td colspan="2"><nested:equal property="schedule_flag"
					value="true">
Current schedule time(date hour:minute):
</nested:equal> <nested:equal property="schedule_flag" value="false">
New schedule time(date hour:minute):
</nested:equal></td>
			</tr>
			<tr>
				<td><nested:text property="schedule_date"
					styleId="schedule_date" onfocus="this.blur()">
				</nested:text> <img src="../images/cal.gif" id="schedule_vdate_cal"> <nested:select
					property="schedule_shour">
					<script type="text/javascript">
for (var i=0; i<24; i++){
	document.write("<option "+ (i==<nested:write property="schedule_shour" />?"selected":"") +" value='");
	document.write(i);
	if (i<10)document.write("'>0"); 
	else document.write("'>");
	document.write(i);
	document.write("</option>");
}
</script>
				</nested:select> :<nested:select property="schedule_min">
					<html:option value="0">00</html:option>
					<html:option value="15">15</html:option>
					<html:option value="30">30</html:option>
					<html:option value="45">45</html:option>
				</nested:select></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">Synchronize HSFO demographic info: <nested:equal
					property="check_flag" value="true">
					<input type="checkbox" name="copyCheck" onclick="setCopy();"
						checked>
				</nested:equal> <nested:equal property="check_flag" value="false">
					<input type="checkbox" name="copyCheck" onclick="setCopy();">
				</nested:equal></td>
			</tr>
			<tr>

				<td colspan="2">
				<CENTER><nested:submit value="save schedule"
					onclick="return confirmSch();" /></CENTER>
				</td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
		</table>
		<hr>
		<nested:equal property="lastlog_flag" value="true">

Last resubmit log at <nested:write property="lastlog_time" />
			<br>
			<nested:notEmpty property="lastlog">

				<nested:write property="lastlog" />
			</nested:notEmpty>
			<nested:empty property="lastlog">
Last resubmit didn't run.
</nested:empty>

		</nested:equal>
		<nested:equal property="lastlog_flag" value="false">
			<tr>
				<td colspan="2">No log in the system yet.</td>
			</tr>
		</nested:equal>

		<script type="text/javascript">
Calendar.setup( { inputField : "schedule_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "schedule_vdate_cal", singleClick : true, step : 1 } );
</script>

	</nested:form>
</c:if>
</body>

</html:html>
