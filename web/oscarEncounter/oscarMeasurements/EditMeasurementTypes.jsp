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
<bean:message key="oscarEncounter.Measurements.msgEditMeasurementTypes"/>
</title>
<style type="text/css">
body{
    FONT-SIZE: 12px;
    FONT-FAMILY: Verdana, Tahoma, Arial, sans-serif;
}
</style>
<html:base/>
<script type="text/javascript">
function popupOscarConS(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.oscarConS"/>", windowprops);
  window.close();
}
</script>
</head>

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<table>
    <tr>
    <td class=Title colspan="2"><bean:message key="oscarEncounter.Measurements.msgGroup"/></td>
    </tr>
    <tr>
        <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="150">
            <a href=# onClick="popupOscarConS(200,1000,'DefineNewMeasurementGroup.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.Index.measurements.addMeasurementGroup"/></a>
            </td></tr></table>
        </td>
        <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="150">
            <a href=# onClick="popupOscarConS(200,1000,'ProcessGroupListAction.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.Index.measurements.editMeasurementGroup"/></a>
            </td></tr></table>
        </td>
    </tr>
    <tr>
    <td class=Title colspan="2"><bean:message key="oscarEncounter.Measurements.msgType"/></td>
    </tr>
    <tr>
         <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="150">
            <a href=# onClick="popupOscarConS(600,1000,'ProcessDisplayMeasurementTypesAction.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.Index.measurements.viewMeasurementType"/></a>
            </td></tr></table>
        </td>
         <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="150">
            <a href=# onClick="popupOscarConS(200,1000,'ProcessAddMeasurementTypeAction.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.Index.measurements.addMeasurementType"/></a>
            </td></tr></table>
        </td>
    </tr>
    <tr>
    <td class=Title colspan="2"><bean:message key="oscarEncounter.Measurements.msgMeasuringInstruction"/></td>
    </tr>
    <tr>
        <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="150">
            <a href=# onClick="popupOscarConS(200,1000,'ProcessAddMeasuringInstructionAction.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.Index.measurements.addMeasuringInstruction"/></a>
            </td></tr></table>
        </td>
    </tr>
    <tr><td></td></tr>
    <tr>
       <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
            <a href="javascript:window.close()" class="messengerButtons"><bean:message key="global.btnCancel"/></a>
            </td></tr></table>
        </td>
    </tr>
</table>
</body>
</html:html>
                             
                                   
                                
