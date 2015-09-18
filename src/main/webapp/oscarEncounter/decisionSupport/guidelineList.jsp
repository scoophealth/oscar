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

<%-- 
    Document   : listGuidelines
    Created on : 29-Jun-2009, 1:14:43 AM
    Author     : apavel
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="org.oscarehr.decisionSupport.model.DSGuideline"%>

<%pageContext.setAttribute("demographic_no", request.getParameter("demographic_no"));
pageContext.setAttribute("provider_no", request.getParameter("provider_no"));
LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<html>
    <head>
        <title>GuidelineList</title>
        <link rel="stylesheet" href="decisionSupport.css" type="text/css"></link>
    </head>
    <body>
        <div style="font-size: 16px; font-weight: bold;"><bean:message key="oscarencounter.guidelinelist.youcurrently" /></div>
        <logic:present name="demographic_no"><div style="font-size: 10px;"><bean:message key="oscarencounter.guidelinelist.demographicno" /> <bean:write name="demographic_no"/></div></logic:present>
        <br>
        <table class="dsTable">
            <tr>
                <th>Version</th>
                <th><bean:message key="oscarencounter.guidelinelist.title" /></th>
                <th><bean:message key="oscarencounter.guidelinelist.author" /></th>
                <th><bean:message key="oscarencounter.guidelinelist.dateimported" /></th>
                <th><bean:message key="oscarrx.showallergies.status" /></th>
                <logic:present name="demographic_no">
                    <th><bean:message key="oscarencounter.guidelinelist.evaluated" /></th>
                </logic:present>

            </tr>
            <logic:iterate name="guidelines" id="guideline" type="org.oscarehr.decisionSupport.model.DSGuideline" indexId="index">
            <%
            String cssClass = "even";
            if (index%2 == 1)  cssClass = "odd";%>
            <tr class="<%=cssClass%>">
                <td><bean:write name="guideline" property="version"/></td>
                <td><bean:write name="guideline" property="title"/></td>
                <td><bean:write name="guideline" property="author"/></td>
                <td><bean:write name="guideline" property="dateStart" format="MMM d, yyyy"/></td>
                <td><logic:equal name="guideline" property="status" value="A"><span class="good"><bean:message key="oscarencounter.guidelinelist.active" /> </span></logic:equal>
                <logic:equal name="guideline" property="status" value="F"><span class="bad"><bean:message key="oscarencounter.guidelinelist.failedon" /> <bean:write name="guideline" property="dateDecomissioned" format="MMM d, yyyy"/> <bean:message key="oscarencounter.guidelinelist.invalid" /></span></logic:equal>
                </td>
                <%
                if (request.getParameter("demographic_no") != null) {
                    DSGuideline dsGuideline = (DSGuideline) pageContext.getAttribute("guideline");
                    boolean passed = dsGuideline.evaluate(loggedInInfo, request.getParameter("demographic_no")) != null;
                    pageContext.setAttribute("passed", passed);
                %>
                <td>
                    <logic:equal name="passed" value="true"><span class="good"><bean:message key="oscarencounter.guidelinelist.passed" /></span></logic:equal>
                    <logic:equal name="passed" value="false"><span class="bad"><bean:message key="oscarencounter.guidelinelist.failed" /></span></logic:equal>
                    - <a href="<%=request.getContextPath()%>/oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=<bean:write name="guideline" property="id"/>&provider_no=<bean:write name="provider_no"/>&demographic_no=<bean:write name="demographic_no"/>"><bean:message key="oscarencounter.guidelinelist.moreinfo" /></a>
                </td>
                <%}%>
            </tr>
            </logic:iterate>
        </table>
    </body>
</html>
