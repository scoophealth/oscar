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
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    if (c.get(Calendar.MONTH)<7) year--;
	String dateStartDefault = year+"-07-01";
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String dateEndDefault = df.format(new Date());
	
	boolean showData = true;
	String dateStart = request.getParameter("date_start");
	String dateEnd = request.getParameter("date_end");
	if (dateStart==null && dateEnd==null) showData = false;
	
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
	
	TreeSet<Integer> fieldNoteEforms = FieldNoteManager.getFieldNoteEforms();
	TreeMap<String, String> residentNameList = new TreeMap<String, String>();
	TreeMap<String, TreeMap<String, Integer>> supervisorResidentCountList = new TreeMap<String, TreeMap<String, Integer>>();
	TreeMap<String, Integer> supervisorCountList = new TreeMap<String, Integer>();
	int totalCount = 0;
	
	if (showData) {
		residentNameList = FieldNoteManager.getResidentNameList(fieldNoteEforms, startDate, endDate);
		supervisorResidentCountList = FieldNoteManager.getSupervisorResidentCountList();
		for (String supervisor : supervisorResidentCountList.keySet()) {
			int noteCount = 0;
			for (Integer count : supervisorResidentCountList.get(supervisor).values()) {
				noteCount += count;
			}
			supervisorCountList.put(supervisor, noteCount);
			totalCount += noteCount;
		}
	}
%>
<html:html locale="true">
<head>

<title><bean:message key="admin.fieldNote.report"/></title>
<link rel="stylesheet" href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../../share/css/eformStyle.css">
<link rel="stylesheet" type="text/css" media="all" href="../../share/calendar/calendar.css" title="win2k-cold-1"/> 
<style>
	td { font-size: small }
	.bordered {
		border: 1px solid #B0B0B0;
		border-collapse: collapse;
		padding-left: 5px;
		padding-right: 5px;
	}
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
		
		if (method=="view") document.fieldNoteReportForm.target = "_blank";
		document.fieldNoteReportForm.submit();
		document.fieldNoteReportForm.target = "_self";
	}
	
	function getFieldNotes() {
		document.fieldNoteReportForm.action = "";
		document.fieldNoteReportForm.submit();
	}

	function setDefaultDates() {
		document.fieldNoteReportForm.date_start.value = "<%=dateStartDefault%>";
		document.fieldNoteReportForm.date_end.value = "<%=dateEndDefault%>";
		document.fieldNoteReportForm.action = "";
		document.fieldNoteReportForm.submit();
	}
	
	function showSupervisorReport(show) {
		if (show) {
			document.getElementById("supervisorReport").style.display = "table";
			document.getElementById("supervisorReportButton").style.display = "none";
		} else {
			document.getElementById("supervisorReport").style.display = "none";
			document.getElementById("supervisorReportButton").style.display = "inline";
		}
	}
	
	function start() {
<%		if (invalidDate) {%>
			alert("Invalid Start/End dates");
<%		}
%>	}
</script>

</head>
<body onload="start();">

<form name="fieldNoteReportForm" action="fieldnotereport.jsp">
<input type="hidden" name="residentId"/>
<input type="hidden" name="residentName"/>
<input type="hidden" name="method"/>

<table>
	<tr>
		<th class="eformInputHeading"><bean:message key="admin.fieldNote.report"/></th>
	</tr>
    <tr style="background-color: <%=fieldNoteEforms.isEmpty()?"#FFFF00":"#FFFFFF"%>;">
    	<td align="<%=fieldNoteEforms.isEmpty()?"left":"right"%>">
<%
		if (fieldNoteEforms.isEmpty()) {
%>  	  	<bean:message key="admin.fieldNote.noEformAssigned"/>
<%		}
%>    		<input type="button" value="<bean:message key="admin.fieldNote.selectEformsButton"/>" title="<bean:message key="admin.fieldNote.selectEforms"/>" onclick="window.location.href='fieldnoteselect.jsp'"/>
    	</td>
    </tr>
    <tr style="background-color: #F2F2F2;">
    	<td>
			<bean:message key="admin.fieldNote.startDate"/>:<input type="text" name="date_start" size="8" value="<%=dateStart%>" id="startDate"><a id="SCal"><img title="Calendar" src="../../images/cal.gif" alt="Calendar" border="0"/></a>
			&nbsp;
			<bean:message key="admin.fieldNote.endDate"/>:<input type="text" name="date_end" size="8" value="<%=dateEnd%>" id="endDate"><a id="ECal"><img title="Calendar" src="../../images/cal.gif" alt="Calendar" border="0"/></a>
			&nbsp;
   			<input type="submit" value="<bean:message key="admin.fieldNote.getFieldNotes"/>" style="font-weight:bold; font-size:large" onclick="getFieldNotes();"/>
    		<input type="button" value="<bean:message key="admin.fieldNote.reset"/>" title="<bean:message key="admin.fieldNote.resetDates"/>" onclick="setDefaultDates();"/>
			<script language='javascript'>
				Calendar.setup({inputField:"startDate",ifFormat:"%Y-%m-%d",showsTime:false,button:"SCal",singleClick:true,step:1});
				Calendar.setup({inputField:"endDate",ifFormat:"%Y-%m-%d",showsTime:false,button:"ECal",singleClick:true,step:1});
			</script>
    	</td>
    </tr>
</table>
<br/>
<%
	if (!showData) return;
%>
<input type="button" id="supervisorReportButton" value="<bean:message key="admin.fieldNote.observerNoteCount"/> (<%=totalCount%>)" onclick="showSupervisorReport(true);"/>
<table id="supervisorReport" style="display:none">
	<tr>
		<td valign="top">
			<input type="button" value="X" onclick="showSupervisorReport(false);" style="font-size:x-small; padding:0"/>
		</td>
		<td>
			<table class="bordered">
				<tr>
					<th class="bordered"><bean:message key="admin.fieldNote.observerSupervisor"/></th>
					<th class="bordered"><bean:message key="admin.fieldNote.count"/></th>
					<th class="bordered"><bean:message key="admin.fieldNote.resident"/></th>
					<th class="bordered"><bean:message key="admin.fieldNote.count"/></th>
				</tr>
<%
				for (String supervisor : supervisorResidentCountList.keySet()) {
					TreeMap<String, Integer> residentCountList = supervisorResidentCountList.get(supervisor);
%>					<tr>
					<td class="bordered" rowspan="<%=residentCountList.size()%>" valign="top">
						<%=supervisor%>
					</td>
<%					boolean first = true;
					for (String resident : residentCountList.keySet()) {
						if (first) {
							first = false;
%>							<td class="bordered" rowspan="<%=residentCountList.size()%>" valign="top" align="center">
								<%=supervisorCountList.get(supervisor)%>
							</td>
<%						} else {
%>							<tr>
<%						}
%>						<td class="bordered"><%=resident%></td>
						<td class="bordered" align="center"><%=residentCountList.get(resident)%></td>
						</tr>
<%					}
				}
%>				<tr><td colspan="3" align="right"><bean:message key="admin.fieldNote.total"/>:</td><td align="center"><%=totalCount%></td></tr>
			</table>
		</td>
	</tr>
</table>
<br/><br/>
<div style="text-decoration:underline; font-size:large"><bean:message key="admin.fieldNote.residentReports"/></div>
<table>
<%
	for (String residentName : residentNameList.keySet()) {
		String residentId = residentNameList.get(residentName);
		String resNameSend = residentName.replace("'", "\\'");
%>
		<tr>
		   	<td><%=residentName%></td>
		   	<td>
		   		<input type="button" value="<bean:message key="admin.fieldNote.view"/>" title="<bean:message key="admin.fieldNote.viewReport"/>" onclick="send('<%=residentId%>','<%=resNameSend%>','view');"/>
				<input type="button" value="<bean:message key="admin.fieldNote.download"/>" title="<bean:message key="admin.fieldNote.downloadReport"/>" onclick="send('<%=residentId%>','<%=resNameSend%>','download');"/>
			</td>
		</tr>
<%	}
%>
</table>

</form>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
$( document ).ready(function() {
parent.parent.resizeIframe($('html').height());      

});
</script>

</body>
</html:html>
