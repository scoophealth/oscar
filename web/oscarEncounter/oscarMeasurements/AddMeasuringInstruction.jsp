
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
<bean:message key="oscarEncounter.Measurements.msgAddMeasurementInstruction"/>
</title>
<html:base/>
</head>

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<html:form action="/oscarEncounter/oscarMeasurements/AddMeasuringInstruction.do">
<table>
    <tr>
        <td>
            <table>
                <tr>
                    <td>
                        <tr>
                            <td colspan="2">
                                <logic:present name="messages">
                                    <logic:iterate id="msg" name="messages">
                                    <bean:write name="msg"/><br>
                                    </logic:iterate>
                                </logic:present>              
                            </td>
                        </tr>
                        <tr>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingType"/>
                            </th>
                            <td>
                             <html:select property="typeDisplayName">
                                <html:options collection="typeDisplayNames" property="typeDisplayName" labelProperty="typeDisplayName"/>
                            </html:select>
                            </td>
                        </tr>                     
                        <tr>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc"/>
                            </th>
                            <td><html:text property="measuringInstrc" /></td> 
                        </tr>
                        <tr>
                            <th align="left" class="td.tite">
                                <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingValidation"/>
                            </th>
                            <td>
                             <html:select property="validation">
                                <html:options collection="validations" property="id" labelProperty="name"/>
                            </html:select>
                            </td>
                        </tr>                            
                        <tr>                            
                            <td>
                                <table>
                                    <tr>
                                        <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>
                                        <td><input type="button" name="Button" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/>" onclick="submit();"/></td>
                                    <tr>
                                </table>
                            </td>
                            <td></td>
                        </tr>   
                    </td>
                </tr>
            </table>
        </td>   
    </tr>
</table>
<input type="hidden" name="msgBetween" value="<bean:message key="oscarEncounter.oscarMeasurements.AddMeasurementType.duplicateType"/>    
</html:form>




</body>
</html:html>
                             
                                   
                                
