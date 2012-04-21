<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<%@ include file="/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Generic Intake Report</title>
<link rel="stylesheet" type="text/css" media="all" href="../../share/css/extractedFromPages.css"  />
</head>
<body>
<table class="header">
	<thead>
		<tr>
			<td class="header"><c:out value="${intakeType}"></c:out> Intake
			Report ( <c:out value="${startDate}"></c:out> / <c:out
				value="${endDate}"></c:out> )</td>
		</tr>
	</thead>
</table>
<br />
<br />
<c:forEach var="question" items="${questionStatistics}">
	<table>
		<caption><c:out value="${question.key}"></c:out></caption>
		<thead>
			<tr>
				<th>&nbsp;</th>
				<td scope="col">Count / Total</td>
				<td scope="col">Percent</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="statistic" items="${question.value}">
				<tr>
					<th><c:out value="${statistic.label}"></c:out></th>
					<td><c:out value="${statistic.count}"></c:out> / <c:out
						value="${statistic.size}"></c:out></td>
					<td><c:out value="${statistic.percent}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br />
</c:forEach>
</body>
</html>
