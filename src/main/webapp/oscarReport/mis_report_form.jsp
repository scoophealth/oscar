<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.web.MisReportUIBean"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.getCurrentFacility().getId());
	List<Program> programs=programDao.getProgramsByFacilityId(loggedInInfo.getCurrentFacility().getId());
	List<Provider> providers = providerDao.getActiveProviders();
%>

<%@ include file="/taglibs.jsp"%>

<html>
<head>	
<title><bean:message key="admin.admin.misRpt" /></title>
</head>

<body>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

	<h3><bean:message key="admin.admin.misRpt" /></h3>

<form action="${ctx}/oscarReport/mis_report_form.jsp" class="well form-horizontal" id="misForm">

	<div class="control-group">
			<label class="control-label">Report By</label> 
			<div class="controls">
			<label class="radio inline"> 
				<input
				type="radio" id="reportByFn" name="reportBy" value="functionalCentre"
				checked="checked" onclick="toggleDivs()">
				Functional Centre
			</label> 
			<label class="radio inline"> 
				<input type="radio" id="reportByPr" name="reportByPr"
				value="programs" onclick="toggleDivs()">
				Programs
			</label>
			<label class="radio inline"> 
				<input type="radio" id="reportByProvider" name="reportByProvider"
				value="provider" onclick="toggleDivs()">
				Provider
			</label>
			</div>
		</div>
	<div class="control-group">
		<label class="control-label"></label> 
		<div class="controls">
				<div class="toggleDiv">
				
				<%if(functionalCentres.size()>0){%>
					<select id="functionalCentreId" name="functionalCentreId" class="input-medium">
						<%
							for (FunctionalCentre functionalCentre : functionalCentres)
							{
						%>
						<option value="<%=functionalCentre.getAccountId()%>"><%=StringEscapeUtils.escapeHtml(functionalCentre.getAccountId()+", "+functionalCentre.getDescription())%></option>
						<%
							}
						%>
					</select>
				<%}else{%>
					    <div class="alert span6">
							No function centre in use by facility
					    </div>
					
				<%}%>

				</div>
				<div class="toggleDiv" style="display: none;">
					<select id="programIds" name="programIds" multiple='multiple' class="input">
						<%
							for (Program program : programs)
							{
								if (program.isBed() || program.isService())
								{
						%>
						<option value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName() + " ("+program.getType()+')')%></option>
						<%
								}
							}
						%>
					</select>
					<label class="checkbox"> 
					<input type="checkbox"	name="reportProgramsIndividually"> Report Programs Separately
					</label>

				</div>
				<div class="toggleDiv" style="display: none;">
					<select name="providerId">
						<%
							for (Provider provider : providers)
							{
								%>
									<option value="<%=provider.getProviderNo()%>"><%=StringEscapeUtils.escapeHtml(provider.getFormattedName())%></option>
								<%
							}
						%>
					</select>
				</div>		
		</div>
	</div>
	<hr style="border-bottom:1px solid #e5e5e5; width:100%;">
	<div class="control-group">
		<label class="control-label">Date Range Start</label>
			<div class="controls">
				<select name="startYear" class="input-medium">
					<%
						GregorianCalendar cal=new GregorianCalendar();
						int year=cal.get(GregorianCalendar.YEAR);
						for (int i=0; i<10; i++)
						{
					%>
					<option value="<%=year-i%>"><%=year-i%></option>
					<%
						}
					%>
				</select>
				<select name="startMonth" class="input-mini">
					<%
						DateFormatSymbols dateFormatSymbols=DateFormatSymbols.getInstance();
						String[] months=dateFormatSymbols.getShortMonths();
						
						for (int i=1; i<13; i++)
						{
					%>
					<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
					<%
						}
					%>
				</select>
			</div>
	</div>
	<div class="control-group">
			<label class="control-label">Date Range End (inclusive)</label>
			<div class="controls">
				<select name="endYear" class="input-medium">
					<%
						for (int i=0; i<10; i++)
						{
					%>
					<option value="<%=year-i%>"><%=year-i%></option>
					<%
						}
					%>
				</select> <select name="endMonth" class="input-mini">
					<%
						for (int i=1; i<13; i++)
						{
					%>
					<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
					<%
						}
					%>
				</select>
			</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<input type="submit" value="View Report" class="btn btn-primary" />
		</div>
	</div>
</form>	
	<%
		if(request.getParameter("reportBy")!=null){
			int startYear = Integer.parseInt(request.getParameter("startYear"));
			int startMonth = Integer.parseInt(request.getParameter("startMonth"));
			int endYear = Integer.parseInt(request.getParameter("endYear"));
			int endMonth = Integer.parseInt(request.getParameter("endMonth"));

			GregorianCalendar startDate=new GregorianCalendar(startYear, startMonth, 1);
			GregorianCalendar endDate=new GregorianCalendar(endYear, endMonth, 1);
			GregorianCalendar actualEndDate=(GregorianCalendar)endDate.clone();
			actualEndDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive of the 30/31 days of the month

			MisReportUIBean misReportUIBean=null;	
			String reportBy=request.getParameter("reportBy");
			
			if ("functionalCentre".equals(reportBy))
			{
				String functionalCentreId=request.getParameter("functionalCentreId");
				misReportUIBean=new MisReportUIBean(loggedInInfo,functionalCentreId, startDate, actualEndDate);
			}
			else if ("programs".equals(reportBy))
			{
				String[] programIds=request.getParameterValues("programIds");
				boolean reportProgramsIndividually=WebUtils.isChecked(request, "reportProgramsIndividually");

				if (!reportProgramsIndividually)
				{
					misReportUIBean=new MisReportUIBean(programIds, startDate, actualEndDate);
				}
				else
				{
					misReportUIBean=MisReportUIBean.getSplitProgramReports(programIds, startDate, actualEndDate);
				}
			}
			else
			{
				throw(new IllegalStateException("missed a case : reportBy="+reportBy));
			}	
	%>
	
<table class="table table-bordered table-striped table-condensed table-hover">
	<thead>
		<tr>
		<%
			for (String header : misReportUIBean.getHeaderRow())
			{
				%>
					<th><%=header%></th>
				<%
			}
		%>
	</tr>
	</thead>
	<tbody>
	<%
		for (MisReportUIBean.DataRow dataRow : misReportUIBean.getDataRows())
		{
			%>
				<tr>
					<td><%=dataRow.dataReportId%></td>
					<td><%=dataRow.dataReportDescription%></td>
					<%
						for (Integer tempResult : dataRow.dataReportResult)
						{
							%>
								<td><%=tempResult==null?"-":tempResult%></td>
							<%
						}
					%>
				</tr>
			<%
		}
	%>
	</tbody>
</table>
	
	<%
		}
	%>

	<script type="text/javascript">
		var oldRadioVal = $("input:radio[name=reportBy]:checked").val();
		
		function toggleDivs() {
			var newVal = $("input:radio[name=reportBy]:checked").val();

			if(oldRadioVal!=newVal)
				$(".toggleDiv").toggle();
			
			oldRadioVal = newVal;
		}

		$(document).ready(function() {
			 $('#misForm').validate(
			 {
			  rules: { 
				  		functionalCentreId: { 
				  			required: function(element){ return $('#reportByFn').prop('checked'); } 
			 			},
			 			programIds: { 
			 				required: function(element){ return $('#reportByPr').prop('checked'); }
			 			}   
			  }
			 });
		});
		registerFormSubmit('misForm', 'dynamic-content');
	</script>
	
</body>
</html>
