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
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.CdsHospitalisationDays"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>

<%
	Integer clientId=Integer.parseInt(request.getParameter("clientId"));
	List<CdsHospitalisationDays> hospitalisationDays=CdsForm4.getHospitalisationDays(clientId);
%>

<table style="border-collapse:collapse;text-align:center;font-family:monospace">
	<tr>
		<td class="genericTableHeader" style="border:solid black 1px">Admission</td>
		<td class="genericTableHeader" style="border:solid black 1px">Discharge</td>
		<td class="genericTableHeader" style="border:solid black 1px"></td>
	</tr>
	<%
		for (CdsHospitalisationDays entry : hospitalisationDays)
		{
			%>
				<tr>
					<td id="admissionDate<%=entry.getId() %>" style="border:solid black 1px"><%=DateFormatUtils.ISO_DATE_FORMAT.format(entry.getAdmitted())%></td>
					<td id="dischargeDate<%=entry.getId() %>" style="border:solid black 1px"><%=entry.getDischarged()!=null?DateFormatUtils.ISO_DATE_FORMAT.format(entry.getDischarged()):"-"%></td>
					<td style="border:solid black 1px"><image src="<%=request.getContextPath()%>/images/delete.png" onclick="deleteHospitalisationDay(<%=entry.getId()%>)" /></td>
				</tr>
			<%
		}
	%>
</table>
