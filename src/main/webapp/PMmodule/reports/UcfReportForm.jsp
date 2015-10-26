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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.common.model.CaisiForm"%>
<%@page import="org.oscarehr.common.dao.CaisiFormDao"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.survey.service.OscarFormManager" %>
<%
CaisiFormDao caisiFormDao = SpringUtils.getBean(CaisiFormDao.class);	

%>
<script>
	function getReport() {
		var formId = document.ucfForm.formId.value;	 
		if(formId != "") {alert("formid = "+formId);
			//alert('<html:rewrite action="/SurveyManager"/>?method=export_csv&id=' + formId);	
			location.href='<html:rewrite action="/SurveyManager"/>?method=getUcfReport&forId=' + formId;		
			return true;
		} else {
			alert("Please select a form from the form list.");
			return false;
		}	
	}
</script>
<%
	
	List<CaisiForm> forms = caisiFormDao.getCaisiForms();
	
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h1>User Created Form Report</h1>

<form method="post" name="ucfForm"
	action="<%=request.getContextPath()%>/PMmodule/reports/UcfReport.jsp">
<table>
	<tr>
		<td>User Created Forms</td>
		<td>StartDate</td>
		<td>EndDate</td>
	</tr>

	<tr>
		<td><select name="formId">
			<% 
					for(CaisiForm form : forms) {
						%>
			<option value="<%=form.getId() %>"><%=form.getDescription() %></option>
			<% 
					}
					%>

		</select></td>

		<td><input type="text" name="startDate" /></td>

		<td><input type="text" name="endDate" /></td>
	</tr>

	<tr>
		<td></td>
		<td>(YYYY-MM-DD)</td>
		<td>(YYYY-MM-DD)</td>
	</tr>

	<tr>
		<td></td>
		<td><input type="submit" /></td>
		<td><input type="button" value="Back"
			onclick="document.location='<%=request.getContextPath()%>/PMmodule/ProviderInfo.do'" /></td>
	</tr>
</table>
</form>



<%@include file="/layouts/caisi_html_bottom.jspf"%>
