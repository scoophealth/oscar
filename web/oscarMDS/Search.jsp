<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList" %>

<%
if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">

<html>
<head>
<title>
<bean:message key="oscarMDS.search.title"/>
</title>
</head>

<body>
    <form method="post" action="Index.jsp">
        <table width="100%" height="100%" border="0">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRow" colspan="9" align="left">                       
                    <table width="100%">
                        <tr> 
                            <td align="left">
                                <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                            </td>
                            <td align="right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"><bean:message key="global.help"/></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about"/></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license"/></a>
                            </td>
                        </tr>
                    </table>                        
                </td>
            </tr>
            <tr>
                <td valign="middle">
                    <center>
                        <table border="0" cellpadding="5" cellspacing="5">
                            <tr>
                                <td>
                                    <bean:message key="oscarMDS.search.formPatientLastName"/>:
                                </td>
                                <td>
                                    <input type="text" name="lname" size="20">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <bean:message key="oscarMDS.search.formPatientFirstName"/>:
                                </td>
                                <td>
                                    <input type="text" name="fname" size="20">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <bean:message key="oscarMDS.search.formPatientHealthNumber"/>:
                                </td>
                                <td>
                                    <input type="text" name="hnum" size="15">
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <bean:message key="oscarMDS.search.formPhysician"/>:
                                </td>
                                <td>
                                    <select name="searchProviderNo" size="10">
                                        <option value=""><bean:message key="oscarMDS.search.formPhysicianAll"/></option>
                                        <option value="0"><bean:message key="oscarMDS.search.formPhysicianUnclaimed"/></option>
                                        <% ArrayList providers = ProviderData.getProviderList();
                                               for (int i=0; i < providers.size(); i++) { %>
                                                   <option value="<%= (String) ((ArrayList) providers.get(i)).get(0) %>"<%= ( ((String) ((ArrayList) providers.get(i)).get(0)).equals(request.getParameter("providerNo")) ? " selected" : "" ) %>><%= (String) ((ArrayList) providers.get(i)).get(1) %> <%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
                                            <% } %>
                                    </select>
                                    <input type="hidden" name="providerNo" value="<%= request.getParameter("providerNo") %>">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <center>
                                        <bean:message key="oscarMDS.search.formReportStatus"/>: 
                                        <input type="radio" name="status" value=""><bean:message key="oscarMDS.search.formReportStatusAll"/>
                                        <input type="radio" name="status" value="N" checked><bean:message key="oscarMDS.search.formReportStatusNew"/>
                                        <input type="radio" name="status" value="A"><bean:message key="oscarMDS.search.formReportStatusAcknowledged"/>
                                    </center>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <center>
                                        <input type="submit" value=" <bean:message key="oscarMDS.search.btnSearch"/> ">
                                    </center>
                                </td>
                            </tr>
                        </table>
                    </center>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>