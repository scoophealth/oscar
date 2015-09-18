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
<%@ page import="oscar.oscarEncounter.data.EctPatientData"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
    String demoNo = request.getParameter("demographic_no");
    int formId = Integer.parseInt(request.getParameter("formId"));
    //int provNo = Integer.parseInt(request.getParameter("provNo"));

	EctPatientData.Patient p = new EctPatientData().getPatient(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
    String s = p.getSex();

    if(true) {
        out.clear();
		if (s.equals("F")) 
			pageContext.forward("formannualfemaleV2.jsp?demographic_no=" + demoNo + "&formId=" + formId) ; // request.getParameter("form_link") + "?demographic_no=" + request.getParameter("demographic_no") ); //+ "&study_no=" + request.getParameter("study_no") ); //forward request&response to the target page "&formId=" + request.getParameter("formId") + 
		else 
			pageContext.forward("formannualmaleV2.jsp?demographic_no=" + demoNo + "&formId=" + formId) ;

		return;
    }
%>
