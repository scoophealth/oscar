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

<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<div class="page-header">
	<h4>OCAN Export Report - v2.0.6</h4>
</div>


<form method="post" action="${ctx}/oscarReport/ocan_report_export.jsp"
	class="well form-horizontal">

	<fieldset>
		<h4>
			<bean:message key="admin.admin.ocanRpt" />
			<br> <small>Please select the OCAN type and assessment
				start date from &amp; to.</small>
		</h4>
		<div class="row-fluid">
			<div class="control-group">
				<label class="control-label">OCAN Type</label>
				<div class="controls">

					<select name="ocanType" id="ocanType" class="span3">
						<option value="FULL">FULL</option>
						<option value="SELF">SELF+CORE</option>
						<option value="CORE">CORE</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">From</label>
				<div class="controls">
					<select name="startYear" class="input-small">
						<%
							GregorianCalendar cal = new GregorianCalendar();
							int year = cal.get(GregorianCalendar.YEAR);
							year = year + 5;
							for (int i = 0; i < 5; i++) {
						%>
						<option value="<%=year - i%>"><%=year - i%>
						</option>
						<%
							}
							year = year - 5;
							for (int i = 0; i < 10; i++) {
						%>
						<option value="<%=year - i%>"><%=year - i%></option>
						<%
							}
						%>
					</select> <select name="startMonth" class="input-mini">
						<%
							DateFormatSymbols dateFormatSymbols = DateFormatSymbols
									.getInstance();
							String[] months = dateFormatSymbols.getShortMonths();

							for (int i = 1; i < 13; i++) {
						%>
						<option value="<%=i%>" title="<%=months[i - 1]%>"><%=i%></option>
						<%
							}
						%>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">To</label>
				<div class="controls">
					<select name="endYear" class="input-small">
						<%
							int year2 = cal.get(GregorianCalendar.YEAR);
							year2 = year2 + 5;
							for (int i = 0; i < 5; i++) {
						%>
						<option value="<%=year2 - i%>"><%=year2 - i%>
						</option>
						<%
							}
							year2 = year2 - 5;
							for (int i = 0; i < 10; i++) {
						%>
						<option value="<%=year2 - i%>"><%=year2 - i%></option>
						<%
							}
						%>
					</select> <select name="endMonth" class="input-mini">
						<%
							for (int i = 1; i < 13; i++) {
						%>
						<option value="<%=i%>" title="<%=months[i - 1]%>"><%=i%></option>
						<%
							}
						%>
					</select>
				</div>
			</div>

		</div>
			<div class="control-group">
				<div class="controls">
					<input type="submit" value="Download Report"
						class="btn btn-primary" />
				</div>
			</div>
	</fieldset>
</form>
