<%-- 
    Document   : listGuidelines
    Created on : 29-Jun-2009, 1:14:43 AM
    Author     : apavel
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="org.oscarehr.decisionSupport.model.DSGuideline"%>

<%pageContext.setAttribute("demographic_no", request.getParameter("demographic_no"));
pageContext.setAttribute("provider_no", request.getParameter("provider_no"));
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>GuidelineList</title>
        <link rel="stylesheet" href="decisionSupport.css" type="text/css"></link>
    </head>
    <body>
        <div style="font-size: 16px; font-weight: bold;">You currently have the following tips:</div>
        <logic:present name="demographic_no"><div style="font-size: 10px;">Demographic No: <bean:write name="demographic_no"/></div></logic:present>
        <br>
        <table class="dsTable">
            <tr>
                <th>Version</th>
                <th>Title</th>
                <th>Author</th>
                <th>Date Imported</th>
                <th>Status</th>
                <logic:present name="demographic_no">
                    <th>Evaluate</th>
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
                <td><logic:equal name="guideline" property="status" value="A"><span class="good">Active</span></logic:equal>
                <logic:equal name="guideline" property="status" value="F"><span class="bad">Failed on <bean:write name="guideline" property="dateDecomissioned" format="MMM d, yyyy"/> - invalid definition</span></logic:equal>
                </td>
                <%
                if (request.getParameter("demographic_no") != null) {
                    DSGuideline dsGuideline = (DSGuideline) pageContext.getAttribute("guideline");
                    boolean passed = dsGuideline.evaluate(request.getParameter("demographic_no")) != null;
                    pageContext.setAttribute("passed", passed);
                %>
                <td>
                    <logic:equal name="passed" value="true"><span class="good">Passed</span></logic:equal>
                    <logic:equal name="passed" value="false"><span class="bad">Failed</span></logic:equal>
                    - <a href="<%=request.getContextPath()%>/oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=<bean:write name="guideline" property="id"/>&provider_no=<bean:write name="provider_no"/>&demographic_no=<bean:write name="demographic_no"/>">more info</a>
                </td>
                <%}%>
            </tr>
            </logic:iterate>
        </table>
    </body>
</html>
