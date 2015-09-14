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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>"> 
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar,org.oscarehr.util.MiscUtils" %>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	List<CachedFacility> facilities=CaisiIntegratorManager.getRemoteFacilities(loggedInInfo, loggedInInfo.getCurrentFacility(),false);

	int secondsTillConsideredStale = -1;
	try{
		secondsTillConsideredStale = Integer.parseInt(oscar.OscarProperties.getInstance().getProperty("seconds_till_considered_stale"));
	}catch(Exception e){
		MiscUtils.getLogger().error("OSCAR Property: seconds_till_considered_stale did not parse to an int",e);
		secondsTillConsideredStale = -1;
	}


	Calendar timeConsideredStale = Calendar.getInstance();
	//TODO: 
	timeConsideredStale.add(Calendar.SECOND, -secondsTillConsideredStale);
%>

<h3>Community of integrated facilities considered old if facility hasn't sync since <%=DateFormatUtils.ISO_DATETIME_FORMAT.format(timeConsideredStale) %></h3>

<table style="border-collapse: collapse">
	<tr style="border: solid black 2px; background: silver">
		<td style="border: solid black 1px">Name</td>
		<td style="border: solid black 1px">Description</td>
		<td style="border: solid black 1px">Contact Name</td>
		<td style="border: solid black 1px">Contact Email</td>
		<td style="border: solid black 1px">Contact Phone</td>
		<td style="border: solid black 1px">Last Updated</td>
	</tr>
	<%
		for (CachedFacility x : facilities)
		{
			Calendar lastDataUpdate = x.getLastDataUpdate();
			String background = "white";
			if( lastDataUpdate == null || timeConsideredStale.after(lastDataUpdate)){
				background = "orange";
			}
			
			%>
	<tr style="border: solid black 2px; background: <%=background %>; color: gray">
		<td style="border: solid black 1px"><%=x.getName()%></td>
		<td style="border: solid black 1px"><%=x.getDescription()%></td>
		<td style="border: solid black 1px"><%=x.getContactName()%></td>
		<td style="border: solid black 1px"><%=x.getContactEmail()%></td>
		<td style="border: solid black 1px"><%=x.getContactPhone()%></td>
		<td style="border: solid black 1px"><%=(x.getLastDataUpdate()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(x.getLastDataUpdate()):"")%></td>
	</tr>
	<%
		}
	%>
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
