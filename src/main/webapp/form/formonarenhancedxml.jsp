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
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.integration.born.ONAREnhancedFormToXML" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%
	String demographicNo = request.getParameter("demographic_no");
	String formId = request.getParameter("formId");
    String episodeId = request.getParameter("episodeId");

    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();
		
	response.setContentType ("text/xml");
	response.setHeader ("Content-Disposition", "attachment; filename=\"ar.xml\"");

	ONAREnhancedFormToXML gen = new ONAREnhancedFormToXML();
	gen.generateXMLAndValidate(loggedInInfo, response.getOutputStream(),providerNo,demographicNo, Integer.parseInt(formId),Integer.parseInt(episodeId));
	response.getOutputStream().flush();
	response.getOutputStream().close();
	%>