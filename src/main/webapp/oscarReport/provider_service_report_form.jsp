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
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%
%>

<%@include file="/layouts/caisi_html_top.jspf"%>


<h1>Provider Service Report Form</h1>

<h2>Export to csv</h2>
(This will provide a break down of all unique encounters of a
demographic to a provider, broken down by month and for the entire
interval as well. This only does the numbers for a program of type bed
or service)

<form method="post" action="provider_service_report_export.jsp">
<table>
	<tr>
		<td>Start Date</td>
		<td>EndDate (inclusive)</td>
	</tr>
	<tr>
		<td><input type="text" name="startDate" /></td>

		<td><input type="text" name="endDate" /></td>
	</tr>

	<tr>
		<td>(YYYY-MM)</td>
		<td>(YYYY-MM)</td>
	</tr>

	<tr>
		<td></td>
		<td><input type="submit" value="export" /></td>
	</tr>
</table>
</form>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
