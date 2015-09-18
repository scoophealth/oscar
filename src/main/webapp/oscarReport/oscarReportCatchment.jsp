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

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
	String user_no = (String) session.getAttribute("user");
	int nItems = 0;
	String strLimit1 = "0";
	String strLimit2 = "30";
	if (request.getParameter("limit1") != null)
		strLimit1 = request.getParameter("limit1");
	if (request.getParameter("limit2") != null)
		strLimit2 = request.getParameter("limit2");
	String providerview = request.getParameter("providerview") == null ? "all"
			: request.getParameter("providerview");
%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"%>
<%@ include file="../admin/dbconnection.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao"%>
<%
	DemographicDao demographicDao = SpringUtils
			.getBean(DemographicDao.class);
%>
<%
	GregorianCalendar now = new GregorianCalendar();
	int curYear = now.get(Calendar.YEAR);
	int curMonth = (now.get(Calendar.MONTH) + 1);
	int curDay = now.get(Calendar.DAY_OF_MONTH);
	String clinic = "";
	String clinicview = oscarVariables.getProperty("clinic_view");

	int[] itemp1 = new int[2];
	itemp1[0] = Integer.parseInt(strLimit1);
	itemp1[1] = Integer.parseInt(strLimit2);
%>
<%
	int flag = 0, rowCount = 0;
	String reportAction = request.getParameter("reportAction") == null ? ""
			: request.getParameter("reportAction");
	String xml_vdate = request.getParameter("xml_vdate") == null ? ""
			: request.getParameter("xml_vdate");
	String xml_appointment_date = request
			.getParameter("xml_appointment_date") == null ? ""
			: request.getParameter("xml_appointment_date");
%>

<div class="page-header">
	<h4>
		<bean:message key="oscarReport.oscarReportCatchment.title" />
		<div class="pull-right">
			<button name='print' onClick='window.print()' class="btn">
				<i class="icon-print icon-black"></i>
				<bean:message key="global.btnPrint" />
			</button>
		</div>
	</h4>
</div>

<table class="table table-bordered table-striped table-condensed table-hover">
	<thead>
		<tr>
			<th><bean:message
					key="oscarReport.oscarReportCatchment.msgDemographic" /></th>
			<th><bean:message key="oscarReport.oscarReportCatchment.msgSex" />
			</th>
			<th><bean:message key="oscarReport.oscarReportCatchment.msgDOB" />
			</th>
			<th><bean:message key="oscarReport.oscarReportCatchment.msgCity" />
			</th>
			<th><bean:message
					key="oscarReport.oscarReportCatchment.msgProvince" /></th>
			<th><bean:message
					key="oscarReport.oscarReportCatchment.msgPostal" /></th>
			<th><bean:message
					key="oscarReport.oscarReportCatchment.msgStatus" /></th>
		</tr>
	</thead>
	<%
		for (Demographic d : demographicDao.search_catchment("RO",
				Integer.parseInt(strLimit1), Integer.parseInt(strLimit2))) {
			nItems++;
	%>
	<tr>
		<td><%=d.getLastName()%>,<%=d.getFirstName()%></td>
		<td><%=d.getSex()%></td>
		<td><%=d.getDateOfBirth()%>-<%=d.getMonthOfBirth()%>-<%=d.getYearOfBirth()%></td>
		<td><%=d.getCity()%></td>
		<td><%=d.getProvince()%></td>
		<td><%=d.getPostal()%></td>
		<td><%=d.getPatientStatus()%></td>
	</tr>
	<%
		}
	%>
</table>

<%
	int nLastPage = 0, nNextPage = 0;
	nNextPage = Integer.parseInt(strLimit2)
			+ Integer.parseInt(strLimit1);
	nLastPage = Integer.parseInt(strLimit1)
			- Integer.parseInt(strLimit2);
%>

<ul class="pager">
	<li class="previous <%=nLastPage >= 0 ? "" : "disabled"%>"><a
		href="${ctx}/oscarReport/oscarReportCatchment.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"
		class="contentLink"> &larr; Previous Page
	</a></li>
	<li
		class="next <%=nItems == Integer.parseInt(strLimit2) ? "" : "disabled"%>">
		<a
		href="${ctx}/oscarReport/oscarReportCatchment.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"
		class="contentLink"> <bean:message
				key="oscarReport.oscarReportCatchment.msgNextPage" /> &rarr;
	</a>
	</li>
</ul>

<script>
	$(document).ready(function() {
		$("a.contentLink").click(function(e) {
			//alert('link click')
			e.preventDefault();
			//alert("You clicked the link");
			$("#dynamic-content").load($(this).attr("href"), 
				function(response, status, xhr) {
			  		if (status == "error") {
				    	var msg = "Sorry but there was an error: ";
				    	$("#dynamic-content").html(msg + xhr.status + " " + xhr.statusText);
					}
				}
			);
		});
	});
</script>