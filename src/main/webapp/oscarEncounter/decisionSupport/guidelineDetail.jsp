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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

<%@ page import="org.oscarehr.decisionSupport.model.DSGuideline"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.decisionSupport.model.DSCondition"%>

<%pageContext.setAttribute("demographic_no", request.getParameter("demographic_no"));
pageContext.setAttribute("provider_no", request.getParameter("provider_no"));

%>

<html>
    <head>
        <title>GuidelineList</title>
        <link rel="stylesheet" href="decisionSupport.css" type="text/css"></link>
    </head>
    <body>
        <div style="font-size: 16px; font-weight: bold;"><bean:message key="oscarencounter.guidelinedetail.guidelineassessment" /> <c:out value="${patientName}"/></div>
        <br>
            <logic:present name="consequences">
                <c:forEach items="${consequences}" var="consequence">
                    <span class="good" style="font-size: 12px; font-weight: bold;">
                    GUIDELINE PASSED: <c:out value="${consequence.text}"/><br/>
                    </span>
                </c:forEach>
            </logic:present>
            <table style="font-size: 10px;  border-top: 1px solid black; border-collapse: collapse; margin-top: 15px;">
                <tr><th><bean:message key="oscarencounter.guidelinelist.title" /></th><td><c:out value="${guideline.title}"/></td></tr>
                <tr><th><bean:message key="oscarencounter.guidelinelist.author" /></th><td><c:out value="${guideline.author}"/></td></tr>
                <tr><th><bean:message key="oscarrx.showallergies.startdate" /></th><td><c:out value="${guideline.dateStart}"/></td></tr>
            </table>
            Conditions:
        <table class="dsTable">
            <tr>
                <th>Type</th>
                <th><bean:message key="oscarrx.showallergies.operator" /></th>
                <th><bean:message key="oscarrx.showallergies.expected" /></th>
                <th><bean:message key="oscarrx.showallergies.actual" /></th>
                <th><bean:message key="oscarrx.showallergies.evaluate" /></th>
            </tr>
            <logic:iterate name="conditionResults" id="conditionResult" type="org.oscarehr.decisionSupport.web.DSGuidelineAction.ConditionResult" indexId="index">
            <bean:define name="conditionResult" property="condition" id="condition"/>
            <%
            String cssClass = "even";
            if (index%2 == 1)  cssClass = "odd";%>
            <tr class="<%=cssClass%>">
                <td><c:out value="${conditionResult.condition.conditionType}"/></td>
                <td><c:out value="${conditionResult.condition.listOperator}"/></td>
                <td><c:out value="${conditionResult.condition.values}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${conditionResult.actualValues == null}"><span class="bad"><bean:message key="oscarencounter.guidelinedetail.error" /></span></c:when>
                        <c:otherwise><c:out value="${conditionResult.actualValues}"/></c:otherwise>
                    </c:choose>
                </td>
                <td style="text-align: center;">
                    <c:choose>
                        <c:when test="${conditionResult.result == true}">
                            <span class="good"><bean:message key="oscarencounter.guidelinelist.passed" /></span>
                        </c:when>
                        <c:otherwise>
                            <span class="bad"><bean:message key="oscarencounter.guidelinelist.fail" /></span>
                        </c:otherwise>
                    </c:choose>
               </td>
            </tr>
            </logic:iterate>
        </table>
        <input type="button" value="<bean:message key="oscarencounter.guidelinedetail.btnlistguideline" />" onclick="document.location='guidelineAction.do?method=list&demographic_no=<c:out value="${demographic_no}"/>&provider_no=<c:out value="${provider_no}"/>'">
    </body>
</html>
