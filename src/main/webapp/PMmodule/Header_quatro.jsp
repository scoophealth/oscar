
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
<table width="100%" border="1" bordercolor="#696969">
	<tr>
		<td align="left" width="180px" rowspan="2">
		<%if (oscar.OscarProperties.getInstance().isTorontoRFQ() && !oscar.OscarProperties.getInstance().getBooleanProperty("USE_CAISI_LOGO", "true")){%>
		&nbsp;&nbsp;<img
			src="<html:rewrite page="/images/QuatroShelter-Logo180.gif"   />"
			width="180px" alt="QuatroShelter" border="0" /> <%} else {%> <img
			src="<html:rewrite page="/images/caisi_1.jpg" />" alt="Caisi"
			id="caisilogo" border="0" /> <%}%>
		</td>
		<th rowspan="2">&nbsp;</th>
		<td valign="bottom" class="clsMenu">Shelter:<font color="red"
			size="2"><c:out
			value="${sessionScope.currentShelter.description}"></c:out></font></td>
		<td rowspan="2">&nbsp;</td>
		<td width="320px">
		<table width="100%">
			<tr>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuHome}">
						<td class="clsMenuCell2" nowrap="nowrap">

						<div><html:link action="/Home.do" styleClass="clsMenu">Home</html:link>
						</div>
						</td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuTask}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link action="/PMmodule/Task.do?method=filter"
							styleClass="clsMenu">My Tasks</html:link></div>
						</td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuReport}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link action="QuatroReport/ReportList.do"
							styleClass="clsMenu">Reports</html:link></div>
						</td>
					</c:when>
				</c:choose>
				<td class="clsMenuCell2" nowrap="nowrap"><a target="_blank"
					href='<%=request.getContextPath()%>/help/QuatroShelter.htm'
					class="clsMenu">Help</a></td>
				<td class="clsMenuCell2" nowrap="nowrap"><html:link
					action="/login.do?method=logout" styleClass="clsMenu">Logout</html:link>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="bottom" class="clsMenu" align="left">User: <font
			size="2"> <c:out
			value="${sessionScope.provider.formattedName}" /></font></td>
		<td>
		<table width="100%">
			<tr>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuClient}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link
							action="/PMmodule/ClientSearch2.do?client=true"
							styleClass="clsMenu">Client</html:link></div>
						</td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuProg}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link action="/PMmodule/ProgramManager.do"
							styleClass="clsMenu">Program</html:link></div>
						</td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuFacility}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link
							action="/PMmodule/FacilityManager.do?method=list"
							styleClass="clsMenu">Facility</html:link></div>
						</td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${'V' eq sessionScope.mnuAdmin}">
						<td class="clsMenuCell2" nowrap="nowrap">
						<div><html:link action="/PMmodule/Admin/SysAdmin.do"
							styleClass="clsMenu">Administration</html:link></div>
						</td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</td>
	</tr>
</table>
<!--</div>-->
