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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.DocumentType"%>
<%
    String demographicId = "";
    String affinityDomain = request.getParameter("affinityDomain");
    String documentType = request.getParameter("type");

    String[] documents = {};

    if (documentType != null && (documentType.equalsIgnoreCase("edocs") || documentType.equalsIgnoreCase(DocumentType.CDS.name()))) {
        demographicId = request.getParameter("demoId");
        documents = request.getParameterValues("docNo");

    } else if (documentType != null && documentType.equalsIgnoreCase("eforms")) {
        demographicId = request.getParameter("clientId");
        documents = request.getParameterValues("sendToPhr");
    }

    StringBuilder actionForward = new StringBuilder();

    actionForward.append(request.getContextPath() + "/sharingcenter/documents/export.jsp?demographic_no=");
    actionForward.append(demographicId);

    actionForward.append("&domain=");
    actionForward.append(affinityDomain);

    actionForward.append("&type=");
    actionForward.append(documentType);

    for (String docId : documents) {
        actionForward.append("&documents=");
        actionForward.append(docId);
    }

    response.sendRedirect(actionForward.toString());
%>