<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*,org.oscarehr.olis.*,org.oscarehr.olis.model.*, org.oscarehr.olis.dao.*, org.oscarehr.util.SpringUtils, org.joda.time.DateTime, org.joda.time.format.DateTimeFormat, org.joda.time.format.DateTimeFormatter"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.misc&type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
    String curProvider = (String) session.getAttribute("userlastname") + ","+ (String) session.getAttribute("userfirstname");
    OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao)SpringUtils.getBean("OLISSystemPreferencesDao");
    OLISSystemPreferences olisPreferences =  olisPrefDao.getPreferences();
    
    String startTime = oscar.Misc.getStr(olisPreferences.getStartTime(), "");
    String endTime = oscar.Misc.getStr(olisPreferences.getEndTime(), "");
    DateTimeFormatter output = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss Z");
 	DateTimeFormatter input = DateTimeFormat.forPattern("YYYYMMddHHmmssZ");
 	
    if (!startTime.equals("")) {
    	DateTime date = input.parseDateTime(startTime);
     	startTime = date.toString(output);	
    }
    if (!endTime.equals("")) {
    	DateTime date = input.parseDateTime(endTime);
    	endTime = date.toString(output);
    }
 	Integer pollFrequency = oscar.Misc.getInt(request.getParameter("pollFrequency"), 30);
%>


<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OLIS Preferences</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
    jQuery.noConflict();
</script>
<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>
<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>

<script type="text/javascript" language="JavaScript">
function checkForm() {
	var datePattern = /^\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}\s[+|-]\d{4}$/;
	var frequencyPattern = /^\d+$/;

	var startDate = jQuery("input[name='startTime']").val().trim();
	var endDate = jQuery("input[name='endTime']").val().trim();
	var frequency = jQuery("input[name='pollFrequency']").val().trim();

	var error = false;

	// Hide all errors 
	var startErrorEl = jQuery("#startTimeError");
	var endErrorEl = jQuery("#endTimeError");
	var frequencyErrorEl = jQuery("#frequencyError");
   
	startErrorEl.hide();
	endErrorEl.hide();
	frequencyErrorEl.hide();

	if (!datePattern.test(startDate)) {
		error = true;
		startErrorEl.show();
	}

	if (endDate != "" && !datePattern.test(endDate)) {
		error = true;
		endErrorEl.show();
	}

	if (!frequencyPattern.test(frequency)) { 
		error = true;
		frequencyErrorEl.show();
    }

	return !error;
}

</script>

</head>
<body vlink="#0000FF" class="BodyStyle">
<form action="<%=request.getContextPath()%>/olis/Preferences.do" onsubmit="return checkForm();">
<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>OLIS Preferences</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<table>
			<tr>
				<td>Start Time:</td>
				<td><input type="text" name="startTime" value="<%=startTime%>"> (YYYY-MM-DD hh:mm:ss [-/+]ZZZZ)</td>				
			</tr>
			<tr id="startTimeError" style="display:none;">
				<td> &nbsp; </td>
				<td style="color:red"> The start time does not match the provided format (e.g., ""1970-01-01 00:00:00 - 400".)</td>
			</tr>
			<tr>
				<td>End Time (optional):</td>
				<td><input type="text" name="endTime" value="<%=endTime%>"> (YYYY-MM-DD hh:mm:ss [-/+]ZZZZ)</td>
			</tr>
			<tr id="endTimeError" style="display:none;">
				<td> &nbsp; </td>
				<td style="color:red"> The end time does not match the provided format (e.g., ""1970-01-01 00:00:00 - 400".)</td>
			</tr>
			<tr>
				<td>Frequency (in minutes):</td>
				<td><input type="text" name="pollFrequency" value="<%=pollFrequency%>"></td>
			</tr>
			<tr id="frequencyError" style="display:none;">
				<td> &nbsp; </td>
				<td style="color:red"> The frequency must be an integer</td>
			</tr>
			<tr>
				<td>Filter out patients not in system</td>
				<td >					
					<input type="checkbox" name="filter_patients" <%=(olisPreferences.isFilterPatients()?"checked=\"checked\"":"") %>/>
				</td>
			</tr>				
			<tr>
				<td> 
					<input type="submit" value="Submit" />
				</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</form>
</html:html>
