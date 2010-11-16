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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

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
        <div style="font-size: 16px; font-weight: bold;">Guideline assessment: <c:out value="${patientName}"/></div>
        <br>
            <logic:present name="consequences">
                <c:forEach items="${consequences}" var="consequence">
                    <span class="good" style="font-size: 12px; font-weight: bold;">
                    GUIDELINE PASSED: <c:out value="${consequence.text}"/><br/>
                    </span>
                </c:forEach>
            </logic:present>
            <table style="font-size: 10px;  border-top: 1px solid black; border-collapse: collapse; margin-top: 15px;">
                <tr><th>Title: </th><td><c:out value="${guideline.title}"/></td></tr>
                <tr><th>Author: </th><td><c:out value="${guideline.author}"/></td></tr>
                <tr><th>Start Date: </th><td><c:out value="${guideline.dateStart}"/></td></tr>
            </table>
            Conditions:
        <table class="dsTable">
            <tr>
                <th>Type</th>
                <th>Operator</th>
                <th>Expected</th>
                <th>Actual</th>
                <th>Evaluate</th>
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
                        <c:when test="${conditionResult.actualValues == null}"><span class="bad">Error: Cannot get patient data</span></c:when>
                        <c:otherwise><c:out value="${conditionResult.actualValues}"/></c:otherwise>
                    </c:choose>
                </td>
                <td style="text-align: center;">
                    <c:choose>
                        <c:when test="${conditionResult.result == true}">
                            <span class="good">PASSED</span>
                        </c:when>
                        <c:otherwise>
                            <span class="bad">FAIL</span>
                        </c:otherwise>
                    </c:choose>
               </td>
            </tr>
            </logic:iterate>
        </table>
        <input type="button" value="List Guidelines" onclick="document.location='guidelineAction.do?method=list&demographic_no=<c:out value="${demographic_no}"/>&provider_no=<c:out value="${provider_no}"/>'">
    </body>
</html>
