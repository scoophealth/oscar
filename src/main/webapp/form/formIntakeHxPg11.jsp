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
            
            <!--Sexual Health-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.sexualHealthTitle"/></h2>  
            <hr/>
            <table> 
                <tr><td class="title" colspan="3"><bean:message key="oscarEncounter.formIntakeHx.sexualHistory"/>:</td></tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.intercourse"/>?</td>
                         <td><input type="text" name="hadSexualIntercourse" value="<%=props.getProperty("hadSexualIntercourse","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.maleSex"/>?</td>
                         <td><input type="text" name="sexWithMale" value="<%=props.getProperty("sexWithMale","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.femaleSex"/>?</td>
                         <td><input type="text" name="sexWithFemale" value="<%=props.getProperty("sexWithFemale","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.firstIntercourse"/>?</td>
                         <td><input type="text" name="ageHadSex" value="<%=props.getProperty("ageHadSex","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.numberOfPartners"/>?</td>
                         <td><input type="text" name="partnersLastYear" value="<%=props.getProperty("partnersLastYear","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.condomFrequency"/>?</td>
                         <td><input type="text" name="HowOftenUseCondoms" value="<%=props.getProperty("HowOftenUseCondoms","")%>"/></td>
                </tr>
                <tr>
                         <td class="title" colspan="3"><bean:message key="oscarEncounter.formIntakeHx.stds"/>:</td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.hadSTD"/>?</td>
                         <td><input type="text" name="hadSTD" value="<%=props.getProperty("hadSTD","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.hpv"/></td>
                         <td><input type="text" name="hadHPV" value="<%=props.getProperty("hadHPV","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.chlamydia"/></td>
                         <td><input type="text" name="hadchlamydia" value="<%=props.getProperty("hadchlamydia","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.gonorrhea"/></td>
                         <td><input type="text" name="hadgonorrhea" value="<%=props.getProperty("hadgonorrhea","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.genitalHerpes"/></td>
                         <td><input type="text" name="hadHSV2" value="<%=props.getProperty("hadHSV2","")%>"/></td>
                </tr>
                <tr>
                         <td colspan="2"><bean:message key="oscarEncounter.formIntakeHx.syphilis"/></td>
                         <td><input type="text" name="hadsyphilis" value="<%=props.getProperty("hadsyphilis","")%>"/></td>
                </tr>
            </table>
        </html:form>
    </body>   
</html:html>