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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName3$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName3$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<!DOCTYPE html>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

<html:html locale="true">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="oscarEncounter.formIntakeHx.title"/></title>
        <link rel="stylesheet" type="text/css" href="westernuStyle.css">
    </head>
    <body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,768)" bgcolor="#eeeeee">
        <html:form action="/form/formname">    
            <h1><bean:message key="oscarEncounter.formIntakeHx.title"/></h1>
            
            <%@include file="formIntakeHxTitleBar.jsp"%>
            
            <!--Womens' Health-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.womensHealthTitle"/></h2>  
            <hr/>
            <table>
                <tr><td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.femalePt"/>:</td></tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.firstPeriod"/>?</td>
                         <td><input type="text" name="firstPeriod" value="<%=props.getProperty("firstPeriod","")%>"/></td>
                </tr>                
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.periodFrequency"/>?</td>
                         <td><input type="text" name="monthlyPeriod" value="<%=props.getProperty("monthlyPeriod","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.periodLength"/>?</td>
                         <td><input type="text" name="periodLength" value="<%=props.getProperty("periodLength","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.periodCramps"/>?</td>
                         <td><input type="text" name="severeCramps" value="<%=props.getProperty("severeCramps","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.midcycleBleeding"/>?</td>
                         <td><input type="text" name="unusualBleeding" value="<%=props.getProperty("unusualBleeding","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.pid"/>?</td>
                         <td><input type="text" name="PID" value="<%=props.getProperty("PID","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.ovaryCyst"/>?</td>
                         <td><input type="text" name="ovarianCyst" value="<%=props.getProperty("ovarianCyst","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.breastCancer"/>?</td>
                         <td><input type="text" name="breastCancer" value="<%=props.getProperty("breastCancer","")%>"/></td>
                </tr>                
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.breastMass"/>?</td>
                         <td><input type="text" name="hadBreastLump" value="<%=props.getProperty("hadBreastLump","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.pregnant"/>?</td>
                         <td><input type="text" name="BeenPregnant" value="<%=props.getProperty("BeenPregnant","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.abortion"/>?</td>
                         <td><input type="text" name="TherapeuticAbortion" value="<%=props.getProperty("TherapeuticAbortion","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.abortionAge"/>?</td>
                         <td><input type="text" name="AgeHadAbortion" value="<%=props.getProperty("AgeHadAbortion","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.papTest"/>?</td>
                         <td><input type="text" name="HadPap" value="<%=props.getProperty("HadPap","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.abnormalPap"/>?</td>
                         <td><input type="text" name="AbnormalPap" value="<%=props.getProperty("AbnormalPap","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.lastPap"/>?</td>
                         <td><input type="text" name="LastPap" value="<%=props.getProperty("LastPap","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.birthControl"/>?</td>
                         <td><input type="text" name="usedBirthControl" value="<%=props.getProperty("usedBirthControl","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.whichBC"/>:</td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bc1"/></td>
                         <td><input type="text" name="birthcontrolUsed1" value="<%=props.getProperty("birthcontrolUsed1","")%>"/></td>
                </tr> 
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bc2"/></td>
                         <td><input type="text" name="birthcontrolUsed2" value="<%=props.getProperty("birthcontrolUsed2","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bc3"/></td>
                         <td><input type="text" name="birthcontrolUsed3" value="<%=props.getProperty("birthcontrolUsed3","")%>"/></td>
                </tr> 
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bc4"/></td>
                         <td><input type="text" name="birthcontrolUsed4" value="<%=props.getProperty("birthcontrolUsed4","")%>"/></td>
                </tr> 
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.whichBCNow"/>?</td>
                         <td><input type="text" name="onbirthcontrol" value="<%=props.getProperty("onbirthcontrol","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bcProblems"/>?</td>
                         <td><input type="text" name="problemsBirthControl" value="<%=props.getProperty("problemsBirthControl","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="2"><bean:message key="oscarEncounter.formIntakeHx.bse"/>?</td>
                         <td><input type="text" name="monthlyBreastSelfExam" value="<%=props.getProperty("monthlyBreastSelfExam","")%>"/></td>
                </tr>
                
            </table>
         </html:form>
    </body>   
</html:html>