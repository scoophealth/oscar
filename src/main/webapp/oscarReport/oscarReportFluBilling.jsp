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

<%@ page import="java.util.*,oscar.oscarReport.data.*"%>
<%@ page import="org.oscarehr.common.model.Provider" %>


<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
	String curUser_no, userfirstname, userlastname;
	curUser_no = (String) session.getAttribute("user");
	int count = 0;

	String years = null;
	int curYear = 0;

	String pros = "";
	if (request.getParameter("numMonth") != null) {
		years = request.getParameter("numMonth");
		curYear = Integer.parseInt(years);
	} else {
		GregorianCalendar cal = new GregorianCalendar();
		curYear = cal.get(Calendar.YEAR);
		years = "" + curYear; //"2003";
	}

	if (request.getParameter("proNo") != null) {
		pros = request.getParameter("proNo");
	}

	oscar.oscarReport.data.RptFluReportData fluData = new oscar.oscarReport.data.RptFluReportData();
	fluData.fluReportGenerate(pros, years);
	List<Provider> providers = fluData.providerList();
%>
<%!String selled(String i, String years) {
		String retval = "";
		if (i.equals(years)) {
			retval = "selected";
		}
		return retval;
	}%>



<div class="page-header">
	<h4>
		<bean:message key="oscarReport.oscarReportFluBilling.title" />
		<%=years%>
	</h4>
</div>

<form action="${ctx}/oscarReport/oscarReportFluBilling.jsp" class="well form-inline" id="fluForm">
	<select name="numMonth" class="input-small">
		<%
			for (int i = curYear - 2; i <= curYear + 2; i++) {
		%>
		<option value="<%=i%>" <%=selled(("" + i), years)%>><%=i%></option>
		<%
			}
		%>

	</select> <select name="proNo" class="input-large">
		<option value="-1" <%=selled("-1", pros)%>>
			<bean:message key="oscarReport.oscarReportFluBilling.msgAllProviders" />
		</option>
		<%
			for (Provider p : providers) {
		%>
		<option value="<%=p.getProviderNo()%>" <%=selled(p.getProviderNo(), pros)%>><%=p.getFormattedName()%></option>
		<%
			}
		%>
	</select>
	<button type="submit" class="btn btn-primary">
		<bean:message key="oscarReport.oscarReportFluBilling.btnUpdate" />
	</button>
</form>

<table class="table table-bordered table-striped table-condensed table-hover">
	<thead>
		<tr>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgName" /></th>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgDOB" /></th>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgAge" /></th>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgRoster" /></th>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgPatientStatus" /></th>
			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgPhone" /></th>

			<th><bean:message
					key="oscarReport.oscarReportFluBilling.msgBillingDate" /></th>
		</tr>
	</thead>
	<tbody>
		<%
			RptFluReportData.DemoFluDataStruct demoData;
			for (int i = 0; i < fluData.demoList.size(); i++) {
				demoData = (RptFluReportData.DemoFluDataStruct) fluData.demoList
				.get(i);
				count = count + 1;
		%>
		<tr>
			<td><%=demoData.demoName%></td>
			<td><%=demoData.getDemoDOB()%></td>
			<td><%=demoData.getDemoAge()%></td>
			<td><%=demoData.demoRosterStatus%></td>
			<td><%=demoData.demoPatientStatus%></td>
			<td><%=demoData.getDemoPhone()%></td>
			<td><%=demoData.getBillingDate(fluData.years)%></td>
		</tr>
		<%
			}
		%>
	</tbody>
</table>

<script>
	registerFormSubmit('fluForm', 'dynamic-content');
</script>