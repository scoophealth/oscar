
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.Vector;"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy   
%>

<html:html locale="true">

<head>
<title>
<bean:message key="oscarEncounter.Measurements.msgDisplayHistory"/>
</title>
<html:base/>

</head>


<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<html:form action="/oscarEncounter/oscarMeasurements/DeleteData">
<table>
    <tr>
        <td>
            <table>
                <tr>
                <logic:present name="messages">
                    <logic:iterate id="msg" name="messages">
                    <bean:write name="msg"/><br>
                    </logic:iterate>
                </logic:present>
                </tr>
                <tr>
                    <td>               
                        <tr>
                            <th align="left" class="td.tite" width="5">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingType"/>
                            </th>
                            <th align="left" class="td.tite" width="20">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingProvider"/>
                            </th>
                            <th align="left" class="td.tite" width="10">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingData"/>
                            </th>
                            <th align="left" class="td.tite" width="300">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingMeasuringInstruction"/>
                            </th>
                            <th align="left" class="td.tite" width="300">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingComments"/>
                            </th>
                            <th align="left" class="td.tite" width="150">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingObservationDate"/>
                            </th>
                            <th align="left" class="td.tite" width="150">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingDateEntered"/>
                            </th>
                            <th align="left" class="td.tite" width="10">
                                <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingDelete"/>
                            </th>
                         </tr>
                        <logic:iterate id="data" name="measurementsData" property="measurementsDataVector" indexId = "ctr" >
                        <tr>
                            <td width="5"><bean:write name="data" property="type" /></td>
                            <td width="20"><bean:write name="data" property="providerFirstName" /> <bean:write name="data" property="providerLastName" /></td>                            
                            <td width="10"><bean:write name="data" property="dataField" /></td>
                            <td width="300"><bean:write name="data" property="measuringInstrc" /></td>
                            <td width="300"><bean:write name="data" property="comments" /></td>
                            <td width="150"><bean:write name="data" property="dateObserved" /></td>
                            <td width="150"><bean:write name="data" property="dateEntered" /></td>
                            <td width="10"><input type="checkbox" name="deleteCheckbox" value="<bean:write name="data" property="id" />"</td>                            
                        </tr>                        
                        </logic:iterate>
                        <tr>
                            <td><input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onClick="window.close()"></td>
                            <td><input type="button" name="Button" value="<bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingDelete"/>" onclick="submit();"/></td>
                        </tr>
                        
                    </td>
                </tr>
            </table>
        </td>   
    </tr>
</table>
</html:form>
</body>
</html:html>
                             
                                   
                                
