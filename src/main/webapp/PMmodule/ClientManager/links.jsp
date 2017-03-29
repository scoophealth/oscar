
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


<%@ taglib uri="http://displaytag.sf.net/el" prefix="display-el"%>

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

<c:choose>
	<c:when test="${not empty requestScope.client.demographics }">
		<display-el:table class="simple"
			name="requestScope.client.demographics" id="demographic">
			<display-el:column property="agency.name" sortable="true"
				title="Agency" />
			<display-el:column sortable="true" title="Name">
				<c:out value="${demographic.lastName}" />, <c:out
					value="${demographic.firstName}" />
			</display-el:column>

			<display-el:column sortable="true" title="Date of Birth">
				<c:out value="${demographic.yearOfBirth}" />-<c:out
					value="${demographic.monthOfBirth}" />-<c:out
					value="${demographic.dateOfBirth}" />
			</display-el:column>
		</display-el:table>
	</c:when>
	<c:otherwise>
        Unable to complete request
    </c:otherwise>
</c:choose>
