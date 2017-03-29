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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%--This JSP displays the result of the report query--%>


<%@ page
	import="java.util.*,oscar.oscarReport.reportByTemplate.*,java.sql.*, org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report by Template</title>
<link rel="stylesheet" type="text/css"
	href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="reportByTemplate.css">
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/Oscar.js"></script>
	<link rel="stylesheet" type="text/css" media="all"
	href="../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript"
	src="../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" language="JavaScript">
    function checkform(formobj) {
        if (!validDateFieldsByClass('datefield', formobj)) {
            alert("Invalid Date: Must be in the format YYYY/MM/DD");
            return false;
        }
        return true;
    }
</script>
<script type="text/javascript" language="javascript">
function clearSession(){
    new Ajax.Request('clearSession.jsp','{asynchronous:true}');

}
</script>
<style type="text/css" media="print">
.MainTableTopRow,.MainTableLeftColumn,.noprint,.showhidequery,.sqlBorderDiv,.MainTableBottomRow
	{
	display: none;
}

.MainTableRightColumn {
	border: 0;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle" onunload="clearSession();">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarReport.CDMReport.msgReport" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Report by Template</td>
			</tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<%
		
		ReportObjectGeneric curreport = (ReportObjectGeneric) request.getAttribute("reportobject");
        
		Integer sequenceLength = (Integer)request.getAttribute("sequenceLength");
		
		
		List<String> sqlList = new ArrayList<String>();
		List<String> htmlList = new ArrayList<String>();
		List<String> csvList = new ArrayList<String>();
		
		if(curreport.isSequence()) {
			for(int x=0;x<sequenceLength;x++) {
				sqlList.add((String) request.getAttribute("sql-" + x));
				htmlList.add((String) request.getAttribute("resultsethtml-" + x));
				csvList.add((String) request.getAttribute("csv-" + x));
			}
		} else {
			sqlList.add((String) request.getAttribute("sql"));
			htmlList.add((String) request.getAttribute("resultsethtml"));
			csvList.add((String) request.getAttribute("csv"));
		}
            
          %>
		<td class="MainTableLeftColumn" valign="top" width="160px;"><jsp:include
			page="listTemplates.jsp">
			<jsp:param name="templateviewid"
				value="<%=curreport.getTemplateId()%>" />
		</jsp:include></td>
		<td class="MainTableRightColumn" valign="top">
		
		<%
		String templateid = request.getParameter("templateId");
		ReportObject curreport1 = (new ReportManager()).getReportTemplate(templateid);
		  		 String xml = (new ReportManager()).getTemplateXml(templateid);
                 ArrayList parameters = curreport1.getParameters();
                 int step = 0;
                 if (request.getAttribute("errormsg") != null) {
                    String errormsg = (String) request.getAttribute("errormsg");%>
		<div class="warning"><%=errormsg%></div>
		<%}%>
		<div class="reportTitle"
			<%if (curreport1.getTitle().indexOf("Error") != -1) {%>
			style="color: red;" <%}%>><%=curreport1.getTitle()%></div>
		<div class="reportDescription"><%=curreport1.getDescription()%></div>
		<html:form action="/oscarReport/reportByTemplate/GenerateReportAction"
			onsubmit="return checkform(this);">
			<input type="hidden" name="templateId"
				value="<%=curreport1.getTemplateId()%>">
			<input type="hidden" name="type" value="<%=curreport1.getType()%>">
			<div class="configDiv">
			<table class="configTable">
				<%for (int i=0; i<parameters.size(); i++) {
                             step++;
                             Parameter curparam = (Parameter) parameters.get(i);
                     %>
				<tr>
					<th class="stepRC">Step <%=step%>:</th>
					<td class="descriptionRC" style="max-width: 550px"><%=curparam.getParamDescription()%>
					</td>
					<td id="enclosingCol<%=i%>"><%-- If LIST field --%> 
					<%if (curparam.getParamType().equals(curparam.LIST)) {%>
					<select name="<%=curparam.getParamId()%>">
						<%ArrayList paramChoices = curparam.getParamChoices();
                          for (int i2=0; i2<paramChoices.size(); i2++) { 
                          	Choice curchoice = (Choice) paramChoices.get(i2);%>
							<option value="<%=curchoice.getChoiceId()%>" <%=(request.getParameter(curparam.getParamId()) != null && request.getParameter(curparam.getParamId()).equals(curchoice.getChoiceId()))?" selected=\"selected\" ":"" %>><%=curchoice.getChoiceText()%></option>
						<%}%>
					</select> 
					<%--If TEXT field --%> <% } else if (curparam.getParamType().equals(curparam.TEXT)) {%>
					<input type="text" size="20" name="<%=curparam.getParamId()%>" value="<%=StringUtils.trimToEmpty(request.getParameter(curparam.getParamId()))%>">
					<%--If DATE field --%> <% } else if (curparam.getParamType().equals(curparam.DATE)) {%>
					<input type="text" class="datefield" id="datefield<%=i%>" name="<%=curparam.getParamId()%>" value="<%=StringUtils.trimToEmpty(request.getParameter(curparam.getParamId()))%>">
						<a id="obsdate<%=i%>"><img title="Calendar" src="../../images/cal.gif" alt="Calendar" border="0" /></a> 
						<script type="text/javascript">
                                    Calendar.setup( { inputField : "datefield<%=i%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "obsdate<%=i%>", singleClick : true, step : 1 } );
                                 </script>
                    <%--If CHECK field --%> <% } else if (curparam.getParamType().equals(curparam.CHECK)) {%>
					<input type="hidden" name="<%=curparam.getParamId()%>:check" value="<%=StringUtils.trimToEmpty(request.getParameter(curparam.getParamId()+":check"))%>"> 
					<input type="checkbox" name="mastercheck" onclick="checkAll(this, 'enclosingCol<%=i%>', 'checkclass<%=i%>')">
					<br />
					<%ArrayList paramChoices = curparam.getParamChoices();
                      for (int i2=0; i2<paramChoices.size(); i2++) {
                      	Choice curchoice = (Choice) paramChoices.get(i2);%>
						<input type="checkbox" name="<%=curparam.getParamId()%>" class="checkclass<%=i%>" value="<%=curchoice.getChoiceId()%>" <%=(request.getParameter(curparam.getParamId()) != null && request.getParameter(curparam.getParamId()).equals(curchoice.getChoiceId()))?" checked=\"checked\" ":"" %>>
						<%=curchoice.getChoiceText()%>
						<br />
					  <%}%> 
					<% } else if (curparam.getParamType().equals(curparam.TEXTLIST)) {%>
					<input type="text" size="20" name="<%=curparam.getParamId()%>:list" value="<%=StringUtils.trimToEmpty(request.getParameter(curparam.getParamId()+":list"))%>">
					<font style="font-size: 10px;">(Comma Separated)</font> <% }%>
					</td>
				</tr>
				<%} %>
				<tr>
					<th>Step <%=step+1%>:</th>
					<td>Generate Query</td>
					<td><input type="submit" name="submitButton" value="Run Query"></td>
				</tr>
			</table>
			<div><a
				href="viewTemplate.jsp?templateid=<%=curreport1.getTemplateId()%>"
				class="link">View Template XML</a> <a
				href="addEditTemplate.jsp?templateid=<%=curreport1.getTemplateId()%>&opentext=1"
				class="link">Edit Template</a> <a
				href="addEditTemplatesAction.do?templateid=<%=curreport1.getTemplateId()%>&action=delete"
				onclick="return confirm('Are you sure you want to delete this report template?')"
				class="link">Delete Template</a> <a
				href="exportTemplateAction.do?templateid=<%=templateid%>&name=<%=curreport1.getTitle()%>"
				class="link">Export Template to K2A</a>
			</div>
			<%  if (request.getAttribute("message") != null) {
				String message = (String) request.getAttribute("message"); %>
			<%=message%>
			<%}%>
			</div>
		</html:form>
		
		<br/>
		<br/><br/><br/><br/><br/><br/><br/>
		<div class="reportTitle"><%=curreport.getTitle()%></div>
		<div class="reportDescription"><%=curreport.getDescription()%></div>
		<a href="#" style="font-size: 10px; text-decoration: none;"
			class="showhidequery" onclick="showHideItem('sqlDiv')">Hide/Show
		Query</a>
		<div class="sqlBorderDiv" id="sqlDiv" style="display: none;"><b>Query:</b><br />
		<code style="font-size: 11px;">
			<%
			for(int x=0;x<sqlList.size();x++) {
				out.println((x+1) + ")" + org.apache.commons.lang.StringEscapeUtils.escapeHtml(sqlList.get(x).trim()) + "<br/>");
			}
			%>
		</code>
		</div>
		<div class="reportBorderDiv">
		<%
		
			for(int x=0;x<htmlList.size();x++) {
				 out.println(htmlList.get(x));
				 out.println("<br/>");
				 
			}
			
        %>
		</div>
		<div class="noprint"
			style="clear: left; float: left; margin-top: 15px;">
			
			<input type="button" value="<-- Back"
				onclick="javascript: history.go(-1);return false;">
			<input type="button" value="Print"
				onclick="javascript: window.print();">
			<br/><br/>
	
	<%
		for(int x=0;x<csvList.size();x++) {
	%>			

			<html:form action="/oscarReport/reportByTemplate/generateOutFilesAction">
			<%if(x>1){ %>
			<label><%=(x+1)%></label>
			<%}%>
			<input type="hidden" name="csv"
				value="<%=StringEscapeUtils.escapeHtml(csvList.get(x))%>">

			<input type="submit" name="getCSV" value="Export to CSV">
			<input type="submit" name="getXLS" value="Export to XLS">
		</html:form>
		
	<% } %>	
		</div>
		</td>
	</tr>
	<tr class="MainTableBottomRow">
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
