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
<bean:message key="oscarEncounter.Measurements.msgEditMeasurementGroup"/>
</title>
<html:base/>

<script language="javascript">
function set(target) {
     document.forms[0].forward.value=target;
};
</script>

</head>

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<html:form action="/oscarEncounter/oscarMeasurements/EditMeasurementGroup.do">
<table>
    <tr>
        <td>
            <table>
                <tr>
                    <th align="left" class="td.tite">
                        <bean:message key="oscarEncounter.oscarMeasurements.MeasurementGroup.allTypes"/>
                    </th>

                    <th align="left" class="td.tite">
                        <bean:write name="groupName"/>    
                    </th>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.oscarMeasurements.MeasurementGroup.add2Group"/><bean:write name="groupName"/></td>
                    <td><bean:message key="oscarEncounter.oscarMeasurements.MeasurementGroup.deleteTypes"/><bean:write name="groupName"/></td>
                <tr>
                    <td>
                        <html:select multiple="true" property="selectedAddTypes" size="10">
                            <html:options collection="allTypeDisplayNames" property="typeDisplayName" labelProperty="typeDisplayName"/>
                        </html:select>
                    </td>
                    <td>
                        <html:select multiple="true" property="selectedDeleteTypes" size="10">
                            <html:options collection="existingTypeDisplayNames" property="typeDisplayName" labelProperty="typeDisplayName"/>
                        </html:select>
                    </td>
                </tr>
                <tr>     
                        <input type="hidden" name="forward" value="error"/>
                    <td><input type="button" name="button" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/>" onclick="set('add');submit();"/></td>
                    <td><input type="button" name="button" value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementAction.deleteBtn"/>" onclick="set('delete');submit();"/></td>
                </tr>                        
                <tr>
                    <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>
                    <td></td>
                </tr>
                <input type="hidden" name="groupName" value="<bean:write name="groupName"/>"/>    
            </table>
        </td>   
    </tr>
</table>
</html:form>
</body>
</html:html>
                             
                                   
                                
