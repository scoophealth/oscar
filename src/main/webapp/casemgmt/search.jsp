
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



<%-- Updated by Eugene Petruhin on 27 jan 2009 while fixing #2510693 --%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="org.oscarehr.casemgmt.model.*"%>

<script>
	function popupNotePage(varpage) {
        var page = "" + varpage;
        windowprops = "height=800,width=800,location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "", windowprops);
    }

   function popupHistoryPage(varpage) {
        var page = "" + varpage;
        windowprops = "location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "", windowprops);
    }
    
</script>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr class="title">
		<td colspan="2">Search Notes</td>
	</tr>
	<tr>
		<td bgcolor="#EEEEFF">Date Range:</td>
		<td bgcolor="#EEEEFF"><html:text property="searchStartDate" />
		to <html:text property="searchEndDate" /> (yyyy-mm-dd)</td>
	</tr>

	<tr>
		<td bgcolor="#EEEEFF">Program:</td>
		<td bgcolor="#EEEEFF"><html:select property="searchProgramId">
			<html:option value="0">&nbsp;</html:option>
			<html:options collection="program_domain" property="id"
				labelProperty="name" />
		</html:select></td>
	</tr>

	<tr>
		<td bgcolor="#EEEEFF">Role:</td>
		<td bgcolor="#EEEEFF"><html:select property="searchRoleId">
			<html:option value="0">&nbsp;</html:option>
			<html:options collection="roles" property="id" labelProperty="name" />
		</html:select></td>
	</tr>

	<tr>
		<td bgcolor="#EEEEFF" colspan="2"><html:submit
			onclick="this.form.method.value='search'" /></td>
	</tr>
</table>

<br />
<br />
<c:if test="${not empty search_results}">
	<table border="0" width="100%">
		<tr>
			<td align="left"><span
				style="text-decoration: underline; cursor: pointer; color: blue"
				onclick="document.caseManagementViewForm.note_view.value='summary';document.caseManagementViewForm.method.value='search';document.caseManagementViewForm.submit(); return false;">Summary</span>
			&nbsp;|&nbsp; <span
				style="text-decoration: underline; cursor: pointer; color: blue"
				onclick="document.caseManagementViewForm.note_view.value='detailed';document.caseManagementViewForm.method.value='search';document.caseManagementViewForm.submit();return false;">Detailed</span>
			</td>
			<td align="right">Sort: <html-el:select property="note_sort"
				onchange="document.caseManagementViewForm.method.value='search';document.caseManagementViewForm.note_view.value='${param.note_view}';document.caseManagementViewForm.submit()">
				<html:option value="update_date">Date</html:option>
				<html:option value="providerName">Provider</html:option>
				<html:option value="programName">Program</html:option>
				<html:option value="roleName">Role</html:option>
			</html-el:select></td>
		</tr>
	</table>
	<c:choose>
		<c:when
			test="${param.note_view!='detailed'}">
			<table id="test" width="100%" border="0" cellpadding="0"
				cellspacing="1" bgcolor="#C0C0C0">
				<tr class="title">
					<td></td>
					<td>Date</td>
					<td>Provider</td>
					<td>Status</td>
					<td>Program</td>
					<td>Role</td>
				</tr>

		<%int index=0; String bgcolor="white"; %>
				<c:forEach var="note" items="${search_results}">
		<%
			if(index++%2!=0) {
				bgcolor="white";
			} else {
				bgcolor="#EEEEFF";
			}		
		%>
					<tr bgcolor="<%=bgcolor %>" align="center">
						<td><c:choose>
							<c:when
								test="${(!note.signed) and (sessionScope.readonly=='false')}">
								<c:url
									value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
									var="notesURL" />
								<img src="<c:out value="${ctx}"/>/images/edit_white.png"
									title="Edit/Sign Note" style="cursor: pointer"
									onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')" />
							</c:when>
							<c:when
								test="${note.signed and param.providerNo eq note.providerNo}">
								<c:url
									value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
									var="notesURL" />
								<img src="<c:out value="${ctx}"/>/images/edit_white.png"
									title="Edit Note" style="cursor: pointer"
									onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')" />
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif"
									title="" />
							</c:otherwise>
						</c:choose> <c:choose>
							<c:when test="${note.hasHistory == true}">
								<c:url
									value="/CaseManagementEntry.do?method=history&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
									var="historyURL" />
								<img src="<c:out value="${ctx}"/>/images/history.gif"
									title="Note History" style="cursor: pointer"
									onclick="popupHistoryPage('<c:out value="${historyURL}" escapeXml="false"/>')">
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif"
									title="" />
							</c:otherwise>
						</c:choose> <c:choose>
							<c:when test="${note.locked}">
								<c:url
									value="/CaseManagementView.do?method=unlock&noteId=${note.id}"
									var="lockedURL" />
								<img src="<c:out value="${ctx}"/>/images/ulock.gif"
									title="Unlock" style="cursor: pointer"
									onclick="popupPage('<c:out value="${lockedURL}" escapeXml="false"/>')" />
							</c:when>
							<c:otherwise>
								<img src="<c:out value="${ctx}"/>/images/transparent_icon.gif"
									title="" />
							</c:otherwise>
						</c:choose></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd hh:mm a"
							value="${note.update_date}" /></td>
						<td><c:out value="${note.providerName}" /></td>
						<td><c:out value="${note.status}" /></td>
						<td><c:out value="${note.programName}" /></td>
						<td><c:out value="${note.roleName}" /></td>
					</tr>
				</c:forEach>
			</table>


		</c:when>
		<c:otherwise>


			<table id="test" width="100%" border="0" cellpadding="0"
				cellspacing="1" bgcolor="#C0C0C0">
		<%int index1=0; String bgcolor1="white"; %>
				<c:forEach var="note" items="${search_results}">
		<%
			if(index1++%2!=0) {
				bgcolor1="white";
			} else {
				bgcolor1="#EEEEFF";
			}
		%>
				<tr>
					<td>
					<table width="100%" border="0" style="margin-bottom: 5px">
					<tr bgcolor="<%=bgcolor1 %>">
						<td width="7%">Provider</td>
						<td width="93%"><c:out
							value="${note.provider.formattedName }" /></td>
					</tr>
					<tr bgcolor="<%=bgcolor1 %>">
						<td width="7%">Date</td>
						<td width="93%"><fmt:formatDate pattern="yyyy-MM-dd hh:mm a"
							value="${note.update_date}" /></td>
					</tr>
					<tr bgcolor="<%=bgcolor1 %>">
						<td width="7%">Status</td>
						<td width="93%"><c:out value="${note.status}" /></td>
					</tr>
					<tr bgcolor="<%=bgcolor1 %>">
						<td width="7%">Action</td>
						<td width="93%"><c:if
							test="${(!note.signed) and (sessionScope.readonly=='false')}">
							<c:url
								value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
								var="notesURL" />
							<input type="button" value="Edit and Sign"
								onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')">
						</c:if> <c:if
							test="${note.signed and param.providerNo eq note.providerNo}">
							<c:url
								value="/CaseManagementEntry.do?method=edit&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
								var="notesURL" />
							<input type="button" value="Edit This Note"
								onclick="popupNotePage('<c:out value="${notesURL}" escapeXml="false"/>')">
						</c:if> <c:if test="${note.hasHistory == true}">
							<c:url
								value="/CaseManagementEntry.do?method=history&from=casemgmt&noteId=${note.id}&demographicNo=${param.demographicNo}&providerNo=${param.providerNo}"
								var="historyURL" />
							<input type="button" value="Note History"
								onclick="popupHistoryPage('<c:out value="${historyURL}" escapeXml="false"/>')">
						</c:if> <c:if test="${note.locked}">
							<c:url
								value="/CaseManagementView.do?method=unlock&noteId=${note.id}"
								var="lockedURL" />
							<input type="button" value="Unlock"
								onclick="popupPage('<c:out value="${lockedURL}" escapeXml="false"/>')">
						</c:if></td>
					</tr>
					<tr bgcolor="<%=bgcolor1 %>">
						<td width="7%">Note</td>

						<td width="93%"><c:choose>
							<c:when test="${note.locked}">
								<span style="color: red"><i>Contents Hidden</i></span>
							</c:when>
							<c:otherwise>
								<pre><c:out value="${note.note }" /></pre>
							</c:otherwise>
						</c:choose></td>
					</tr>
					</table>
					</td>
				</tr>
				</c:forEach>
			</table>

		</c:otherwise>
	</c:choose>

	<span style="text-decoration: underline; cursor: pointer; color: blue"
		onclick="document.caseManagementViewForm.note_view.value='summary';document.caseManagementViewForm.method.value='search';document.caseManagementViewForm.submit(); return false;">Summary</span>
&nbsp;|&nbsp;
<span style="text-decoration: underline; cursor: pointer; color: blue"
		onclick="document.caseManagementViewForm.note_view.value='detailed';document.caseManagementViewForm.method.value='search';document.caseManagementViewForm.submit();return false;">Detailed</span>

</c:if>
