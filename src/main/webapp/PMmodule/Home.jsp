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
<%
String _appPath = request.getContextPath();
%>

<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>



<table width="100%" height="100%" cellpadding="0px" cellspacing="0px"
	style="border-width: 1px; border-style: solid; bordercolor: black">
	<tr>
		<th class="pageTitle">Home</th>
	</tr>
	<tr>
		<td>
		<table width="100%">
			<tr>
				<td style="vertical-align: middle" align="center">
				<table align="center" border="0" width="100%" cellpadding="5"
					cellspacing="5">
					<tbody>
						<tr>
							<td align="left"></td>
						</tr>
						<tr>
							<td width="100%" style="vertical-align: middle">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td width="10%" style="vertical-align: middle">&nbsp;&nbsp;<img
										border="0" width="60px" height="60px"
										src="<%=_appPath %>/images/Home.gif" alt="" /></td>
									<td width="80%" align="left" class="clsPageHeader" colspan="5"
										style="font-style: italic; vertical-align: middle">
									<h1 style="color: #1E90FF" style="vertical-align: middle">Home</h1>
									</td>
									<td width="10%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td colspan="5">
									<hr />
									</td>
									<td></td>
								</tr>

							</table>
							<table width="100%">
								<tr>
									<td width="10%"></td>
									<td width="10%"></td>
									<td width="27%"></td>
									<td width="5%"></td>
									<td width="10%"></td>
									<td width="28%"></td>
									<td width="10%"></td>
								</tr>

								<tr>
									<th></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuClient}">
											<html:link action="/PMmodule/ClientSearch2.do">
												<img id="lnkClient" src="<%=_appPath%>/images/Client-60.gif"
													height="60" width="60" border="0"
													style="vertical-align: middle">
											</html:link>
										</c:when>
									</c:choose></th>
									<th align="left" style="vertical-align: middle">
									<table>
										<tr align="left">
											<th align="left" style="vertical-align: middle"
												class="clsHomePageLabels"><c:choose>
												<c:when test="${'V' eq sessionScope.mnuClient}">
													<html:link action="/PMmodule/ClientSearch2.do">Client Management</html:link>
												</c:when>
											</c:choose></th>
										</tr>
									</table>
									</th>
									<th></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuProg}">
											<html:link action="/PMmodule/ProgramManager.do">
												<img id="lnkCare1" src="<%=_appPath%>/images/Program-60.gif"
													height="60" width="60" border="0"
													style="vertical-align: middle">
											</html:link>
										</c:when>
									</c:choose></th>
									<th style="vertical-align: middle">

									<table align="left">
										<tr>
											<th align="left" style="vertical-align: middle"
												class="clsHomePageLabels"><c:choose>
												<c:when test="${'V' eq sessionScope.mnuProg}">
													<html:link action="/PMmodule/ProgramManager.do">Program Management</html:link>
												</c:when>
											</c:choose></th>
										</tr>
									</table>
									</th>
									<th></th>
								</tr>
								<tr>
									<th></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuReport}">
											<html:link action="QuatroReport/ReportList.do">
												<img src="<%=_appPath%>/images/Reports-60.gif" height="60"
													width="60" border="0" alt="" style="vertical-align: middle">
											</html:link>
										</c:when>
									</c:choose></th>
									<th style="vertical-align: middle">
									<table width="100%" align="left">
										<tr>
											<th align="left" style="vertical-align: middle"
												class="clsHomePageLabels"><c:choose>
												<c:when test="${'V' eq sessionScope.mnuReport}">
													<html:link action="QuatroReport/ReportList.do">Reports</html:link>
												</c:when>
											</c:choose></th>
										</tr>
									</table>
									</th>
									<th></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuFacility}">
											<html:link action="/PMmodule/FacilityManager.do?method=list">
												<img id="lnkLTD1" src="<%=_appPath%>/images/Shelter-60.gif"
													height="60" width="60" border="0"
													style="vertical-align: middle">
											</html:link>
										</c:when>
									</c:choose></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuFacility}">
											<table align="left">
												<tr>
													<th align="left" style="vertical-align: middle"
														class="clsHomePageLabels"><html:link
														action="/PMmodule/FacilityManager.do?method=list">Facility Management</html:link>
													</th>
												</tr>
											</table>
										</c:when>
									</c:choose></th>
									<th></th>
								</tr>
								<tr>
									<th></th>
									<th style="vertical-align: middle"><html:link
										action="/PMmodule/Task.do?method=filter">
										<img src="<%=_appPath%>/images/mytasks60.gif" height="60"
											width="60" border="0" style="vertical-align: middle">
									</html:link></th>
									<th style="vertical-align: middle">
									<table>
										<tr align="left">
											<th style="vertical-align: middle" align="left"
												class="clsHomePageLabels">My Tasks
											</th>
										</tr>
									</table>
									</th>


									<th></th>
									<th style="vertical-align: middle"><c:choose>
										<c:when test="${'V' eq sessionScope.mnuAdmin}">
											<a href="<%=_appPath%>/PMmodule/Admin/SysAdmin.do"><img
												src="<%=_appPath%>/images/Admin-60.gif" height="60"
												width="60" border="0" onclick="lnkResource1_Click"></a>
										</c:when>
									</c:choose></th>
									<th align="left" style="vertical-align: middle">
									<table>
										<tr align="left">
											<th style="vertical-align: middle" align="left"
												class="clsHomePageLabels"><c:choose>
												<c:when test="${'V' eq sessionScope.mnuAdmin}">
													<a href="<%=_appPath%>/PMmodule/Admin/SysAdmin.do">System
													Administration</a> &nbsp;
												</c:when>
											</c:choose></th>
										</tr>
									</table>
									</th>
									<th>&nbsp;</th>
								</tr>


								<tr>
									<th></th>
									<th style="vertical-align: middle"><a
										href="<%=_appPath%>/PMmodule/Admin/UserManager.do?method=changePassword"><img
										src="<%=_appPath%>/images/Password.gif" height="60" width="60"
										border="0" onclick="lnkResource1_Click"></a></th>
									<th align="left" style="vertical-align: middle">
									<table>
										<tr align="left">

											<th style="vertical-align: middle" align="left"
												class="clsHomePageLabels"><a
												href="<%=_appPath%>/PMmodule/Admin/UserManager.do?method=changePassword">Change
											Password</a> &nbsp;</th>
										</tr>
									</table>
									</th>
									<th>&nbsp;</th>

									<th></th>
									<th valign="middle"></th>
									<th align="left" valign="middle"></th>
									<th>&nbsp;</th>
								</tr>
								<!-- Jim added end -->

							</table>
							</td>
						</tr>
					</tbody>
				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>

</table>
