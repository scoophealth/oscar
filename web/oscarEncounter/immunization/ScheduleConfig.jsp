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
<%@ page import="oscar.oscarEncounter.immunization.data.*, oscar.util.*" %>
<%@ page import="oscar.oscarEncounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">

<html:html locale="true">
<head>
<title>
<bean:message key="oscarEncounter.immunization.ScheduleConfig.title"/>
</title>
<%
oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean");
%>

</head>
<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarEncounter.immunization.ScheduleConfig.msgImm"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td class="Header" style="padding-left:2px;padding-right:2px;border-right:2px solid #003399;text-align:left;font-size:80%;font-weight:bold;width:100%;" NOWRAP >
                            <%=bean.patientLastName %>, <%=bean.patientFirstName%> <%=bean.patientSex%> <%=bean.patientAge%>
                        </td>
                        <td>
                        </td>
                        <td style="text-align:right;width:650;">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help"/></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about"/></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license"/></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
            </td>
            <td class="MainTableRightColumn">
<%
String sCfg = new EctImmConfigData().getImmunizationConfig();
Document cfgDoc = UtilXML.parseXML(sCfg);
Element cfgRoot = cfgDoc.getDocumentElement();
NodeList cfgSets = cfgRoot.getElementsByTagName("immunizationSet");
%>
<html:form action="/oscarEncounter/immunization/saveConfig">
<input type="hidden" name="xmlDoc" value="<%= UtilMisc.encode64(UtilXML.toXML(cfgDoc)) %>" />
<%
for(int i=0; i<cfgSets.getLength(); i++)
{
    Element cfgSet = (Element)cfgSets.item(i);
    %>
    <div style="font-weight: bold">
        <input type="checkbox" name="chkSet<%=i%>" />
        <%= cfgSet.getAttribute("name") %>
    </div>
    <%
}

%>
<input type="hidden" name="submit_form" value="Save Configuration" />
<input type="button" value="<bean:message key="oscarEncounter.immunization.ScheduleConfig.btnSaveConfig"/>" onclick="document.forms['EctImmPassThruForm'].submit.value='Save Configuration';document.forms['EctImmPassThruForm'].submit();" />
<input type="button" value="<bean:message key="global.btnCancel"/>" onclick="javascript:location.href='loadSchedule.do';" />
<input type="button" value="<bean:message key="oscarEncounter.immunization.ScheduleConfig.btnConfig"/>" onclick="javascript:location.href='config/initConfig.do';" />
</html:form>

            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>