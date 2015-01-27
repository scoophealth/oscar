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

<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="oscar.oscarRx.data.RxProviderData"%>
<%@page import="oscar.oscarRx.data.RxProviderData.Provider"%>
<%@page import="oscar.oscarClinic.ClinicData"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@ include file="/taglibs.jsp"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.eyeform.model.EyeformConsultationReport"%>

<html:html>
  <head>
    <html:base />
    <title>Generate Consultation Report</title>
<link rel="stylesheet" href="css/displaytag.css" type="text/css">
<style type="text/css">
.boldRow {
	color:red;
}
.commonRow{
	color:black;
}
span.h5 {
  margin-top: 1px;
  border-bottom: 1px solid #000;
  width: 90%;
  font-weight: bold;
  list-style-type: none;
  padding: 2px 2px 2px 2px;
  color: black;
  background-color: #69c;
  font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
  font-size: 10pt;
  text-decoration: none;
  display: block;
  clear: both;
  white-space: nowrap;

}
</style>
	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
     <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">

  <!-- main calendar program -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>

<script type="text/javascript" language=javascript>
function popupPage(varpage) {
        var page = "" + varpage;
        windowprops = "height=600,width=800,location=no,"
          +"left=50,top=50,scrollbars=yes,menubar=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "_blank", windowprops);
    }

function updTklrList() {
    clearInterval(check_demo_no);
   // createReport();
}

function search_demographic() {
    var url = '<c:out value="${ctx}"/>/ticklerPlus/demographicSearch2.jsp?form=consultationReportForm&elementName=dmname&elementId=cr.demographicNo';
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.consultationReportForm.elements['cr.demographicNo'].value;
    check_demo_no = setInterval("if (demo_no_orig != document.consultationReportForm.elements['cr.demographicNo'].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}

function clear_demographic() {
	document.consultationReportForm.elements['cr.demographicNo'].value = '';
	document.consultationReportForm.elements['dmname'].value='';
}

function doSubmit() {
	document.consultationReportForm.method.value='list';
	document.consultationReportForm.elements['cr.demographicName'].value=document.consultationReportForm.elements['dmname'].value
	return true;
}
</script>
</head>

<body onload="setLetterhead();">

<html:form action="/eyeform/ConsultationReportList">
	<input type="hidden" name="method" value="list"/>
	<html:hidden property="cr.demographicNo"/>
	<html:hidden property="cr.demographicName"/>

	<table style="border:0;">
	<tr>
	<td style="text-align: right;">
	<a style="color:red;" href="#" onclick="window.close();">Close</a>
	</td>
	</tr>
	</table>
	<table bgcolor="#ddeeff">
		<tr>

			<td>Status:</td>
			<td><html:select property="cr.status">
				<html:option value="">All</html:option>
				<html:option value="Incomplete">Incomplete</html:option>
				<html:option value="Completed,not sent">Completed,not sent</html:option>
				<html:option value="Completed,and sent">Completed,and sent</html:option>
			</html:select></td>
			<td>Demographic:</td>
			<td style="float: left;" colspan="2">
			<%
				String dmname = (String)request.getAttribute("dmname");
				if(dmname==null) {
					dmname = request.getParameter("dmname");
					if(dmname == null) {
						dmname=new String();
					}
				}
			%>
				<input style="float: left; width: 140px;" type="text" name="dmname" disabled="disabled" value="<%=dmname%>"/>
	  			<input style="float: left;" type="button" value="Clear" onclick="clear_demographic();"/>
	  			<input style="float: left;  vertical-align: top; width: 140px;" width="140px" type="button" value="Search Demographic" onclick="search_demographic();"/>
  			</td>
		</tr>
		<tr>
		<td>Report Start Date:</td>
		<td>
		 <html:text styleClass="plain" property="cr.startDate" size="12" onfocus="this.blur()" readonly="readonly" styleId="sdate"/><img src="<%=request.getContextPath()%>/images/cal.gif" id="sdate_cal">
	    </td>
	<td>Report End Date:</td>
	<td>
	<html:text styleClass="plain" property="cr.endDate" size="12" onfocus="this.blur()" readonly="readonly" styleId="edate"/><img src="<%=request.getContextPath()%>/images/cal.gif" id="edate_cal">

	</td>

	 <script type="text/javascript">
				Calendar.setup({ inputField : "sdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "sdate_cal", singleClick : true, step : 1 });
				Calendar.setup({ inputField : "edate", ifFormat : "%Y-%m-%d", showsTime :false, button : "edate_cal", singleClick : true, step : 1 });
	   </script>
	
		</tr>
		<tr>
		<%if(bMultisites){ %>		
		<%@include file="conreportlist_multisite.jsp" %>
		<%} %>		
		<td>Internal Doctor:</td>
		<td colspan="1"><html:select property="cr.providerNo">
			<html:option value="">All</html:option>
			<html:optionsCollection property="cr.providerList" label="formattedName" value="providerNo"/>
		</html:select></td>
		<td colspan="1">
			<html:submit onclick="return doSubmit();">List Consultation Reports</html:submit>

			</td>

		</tr>
	</table>

	<c:if test="${not empty demoName}">

		Consultation reports for <c:out value="demoName"/>:

	</c:if>


	<display:table name="conReportList" requestURI="/eyeform/ConsultationReportList.do" defaultsort="2" sort="list" defaultorder="descending"
		id="conreport" pagesize="15">

		<c:url var="thisURL" value="/eyeform/Eyeform.do">
			<c:param name="conReportNo" value="${conreport.id}"/>
			<c:param name="demographicNo" value="${conreport.demographicNo}"/>
			<c:param name="method" value="prepareConReport"/>
			<c:param name="from" value="out"/>
		</c:url>

		<c:url var="arURL" value="/ArConReport.do">
			<c:param name="conReportNo" value="${conreport.id}"/>
			<c:param name="demographicNo" value="${conreport.demographicNo}"/>
			<c:param name="method" value="newReport"/>
			<c:param name="newreport" value="false"/>
		</c:url>

		<display:column title="Patient name" sortable="true" headerClass="sortable" style="width:210px;">
			<c:if test="${conreport.type=='AR2'}" >
			<a href="javascript:popupPage('<c:out value="${arURL}"/>')"><c:out value="${conreport.demographic.lastName}"/>, <c:out value="${conreport.demographic.firstName}"/></a>

			</c:if>
			<c:if test="${conreport.type!='AR2'}" >
		    <a href="javascript:popupPage('<c:out value="${thisURL}"/>')"><c:out value="${conreport.demographic.lastName}"/>, <c:out value="${conreport.demographic.firstName}"/></a>
		    </c:if>
		</display:column>

		<display:column property="date" title="Report Date" sortable="true" headerClass="sortable" style="width:160px;"></display:column>
		<display:column title="Doctor" property="provider.formattedName" sortable="true" headerClass="sortable" style="width:165px;" />
		<display:column title="Status" property="status" sortable="true" headerClass="sortable"/>

	</display:table>
	<table style="border:0;">
	<tr>
	<td style="text-align: center;">
	<a href="#" onclick="window.close();" >Close</a>
	</td>
	</tr>
	</table>
</html:form>

 </body>
 </html:html>
