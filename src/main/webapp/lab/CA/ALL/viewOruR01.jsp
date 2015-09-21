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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.oscarLab.ca.all.pageUtil.ViewOruR01UIBean"%>

<%@include file="/layouts/html_top.jspf"%>

<%
	String segmentId=request.getParameter("segmentId");
	ViewOruR01UIBean viewOruR01UIBean=new ViewOruR01UIBean(segmentId);
%>


<h2 class="oscarBlueHeader">
	View eData 
	<span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message : segmentId <%=segmentId%>)</span>
</h2>

<table style="border-collapse:collapse;font-size:12px">
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader" style="width:10em">From Provider</td>
		<td><%=viewOruR01UIBean.getFromProviderDisplayString()%></td>
	</tr>
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader">To Provider</td>
		<td><%=viewOruR01UIBean.getToProviderDisplayString()%></td>
	</tr>
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">For Client</td>
		<td>
			<table style="border-collapse:collapse; width 100%">
				<tr style="border-bottom:solid silver 1px">
					<td style="font-weight:bold;text-align:right">Name: </td>
					<td><%=viewOruR01UIBean.getClientDisplayName()%></td>
				</tr>
				<tr style="border-bottom:solid silver 1px">
					<td style="font-weight:bold;text-align:right">Health Number: </td>
					<td><%=viewOruR01UIBean.getHinForDisplay()%></td>
				</tr>
				<tr>
					<td style="font-weight:bold;text-align:right">BirthDay: </td>
					<td><%=viewOruR01UIBean.getBirthDayForDisplay()%></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader">Data Name</td>
		<td><%=viewOruR01UIBean.getSubjectForDisplay()%></td>
	</tr>
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">Text Data</td>
		<td><textarea id="textMessage" readonly="readonly" style="width:40em;height:8em" ><%=viewOruR01UIBean.getTextMessageForDisplay()%></textarea></td>
	</tr>
	<tr style="border:solid silver 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">Uploaded File</td>
		<td>
			<%
				if (viewOruR01UIBean.hasBinaryFile())
				{
					%>
						<span style="font-weight:bold">File name:</span> <%=viewOruR01UIBean.getBinaryFilenameForDisplay()%>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<input type="button" value="open file" onclick="document.location='<%=viewOruR01UIBean.getContentRenderingUrl(request, false)%>'" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<input type="button" value="download file" onclick="document.location='<%=viewOruR01UIBean.getContentRenderingUrl(request, true)%>'" />
						<hr />
						<%=viewOruR01UIBean.getPreviewFileHtml(request)%>
					<%
				}
				else
				{
					 %>
					 	-- No File Uploaded --
					 <%
				}
			%>
		</td>
	</tr>
</table>

<%@include file="/layouts/html_bottom.jspf"%>
