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

<%@ page import="org.oscarehr.common.service.FieldNoteManager"%>
<%@ page import="java.util.*, java.text.*"%>
<%@ page import="oscar.util.StringUtils"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	String dateStart = request.getParameter("date_start");
	String dateEnd = request.getParameter("date_end");
	String dateStartDefault = "2011-07-01";
	String dateEndDefault = df.format(new Date());
	
	if (StringUtils.empty(dateStart)) dateStart = dateStartDefault;
	if (StringUtils.empty(dateEnd)) dateEnd = dateEndDefault;

	//prepare start/end dates
	Date startDate = null;
	Date endDate = null;
	boolean invalidDate = false;
	try
	{
		startDate = df.parse(dateStart);
		endDate = df.parse(dateEnd);
		
		//add one to endDate
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(endDate);
	    cal.add(Calendar.DATE, 1);
	    endDate = cal.getTime();
	}
	catch (ParseException pex)
	{
		invalidDate = true;
	}
	
	//get resident names
	TreeSet<Integer> fieldNoteEforms = FieldNoteManager.getFieldNoteEforms();
	TreeMap<String, String> residentNameList = FieldNoteManager.getResidentNameList(fieldNoteEforms, startDate, endDate);
%>
<html:html locale="true">
<head>
<title><bean:message key="admin.fieldNote.report"/></title>
<link rel="stylesheet" href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../../share/css/eformStyle.css">
<link rel="stylesheet" type="text/css" media="all" href="../../share/calendar/calendar.css" title="win2k-cold-1"/> 
<style>
	td { font-size: small; }
</style>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="../../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../../share/calendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
	function send(residentId, residentName, method) {
		document.fieldNoteReportForm.residentId.value = residentId;
		document.fieldNoteReportForm.residentName.value = residentName;
		document.fieldNoteReportForm.method.value = method;
		document.fieldNoteReportForm.action = "fieldnotereportdetail.jsp";
		document.fieldNoteReportForm.submit();
	}

	function setDefaultDates()
	{
		document.fieldNoteReportForm.date_start.value = "<%=dateStartDefault%>";
		document.fieldNoteReportForm.date_end.value = "<%=dateEndDefault%>";
		document.fieldNoteReportForm.submit();
	}
	
	function checkDates()
	{
<%
	if (invalidDate) {
%>
		alert("Invalid Start/End dates");
<%
	}
%>
	}
</script>
</head>
<body onload="checkDates();">

<div class="eformInputHeading" align="center">
	<bean:message key="admin.fieldNote.report"/>
</div>

<form name="fieldNoteReportForm" action="fieldnotereport.jsp">
<input type="hidden" name="residentId"/>
<input type="hidden" name="residentName"/>
<input type="hidden" name="method"/>

<table width="100%">
    <tr style="background-color: <%=fieldNoteEforms.isEmpty()?"#FFFF00":"#FFFFFF"%>;">
    	<td align="<%=fieldNoteEforms.isEmpty()?"left":"right"%>">
<%
	if (fieldNoteEforms.isEmpty()) {
%>
    		No eForm assigned as Field Note. Press [Select eForms] to assign.
<%
	}
%>
    		<input type="button" value="Select eForms" title="<bean:message key="admin.fieldNote.selectEforms"/>" onclick="window.location.href='fieldnoteselect.jsp'"/>
    	</td>
    </tr>
    <tr style="background-color: #F2F2F2;">
    	<td>
			Report start date:<input type="text" name="date_start" size="8" value="<%=dateStart%>" id="startDate"><a id="SCal"><img title="Calendar" src="../../images/cal.gif" alt="Calendar" border="0"/></a>
			&nbsp;
			Report end date:<input type="text" name="date_end" size="8" value="<%=dateEnd%>" id="endDate"><a id="ECal"><img title="Calendar" src="../../images/cal.gif" alt="Calendar" border="0"/></a>
			&nbsp;
    		<input type="submit" title="Change reporting dates" value="<bean:message key="admin.fieldNote.change"/>"/>
    		<input type="button" value="Reset" title="Reset dates to defaults" onclick="setDefaultDates();"/>
			<script language='javascript'>
				Calendar.setup({inputField:"startDate",ifFormat:"%Y-%m-%d",showsTime:false,button:"SCal",singleClick:true,step:1});
				Calendar.setup({inputField:"endDate",ifFormat:"%Y-%m-%d",showsTime:false,button:"ECal",singleClick:true,step:1});
			</script>
    	</td>
    </tr>
</table>

<p>&nbsp;</p>

<table>
<%
	for (String residentName : residentNameList.keySet()) {
		String residentId = residentNameList.get(residentName);
%>
    <tr>
    	<td><%=residentName%></td>
    	<td>
    		<input type="button" value="View" title="View report online" onclick="send('<%=residentId%>','<%=residentName%>','view');"/>
    		<input type="button" value="Download" title="Download report as Word document" onclick="send('<%=residentId%>','<%=residentName%>','download');"/>
    	</td>
    </tr>
<%
	}
%>
</table>

</form>
</body>
</html:html>
