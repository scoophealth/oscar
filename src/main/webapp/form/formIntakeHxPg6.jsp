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
            
            <!--Immunizations-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.immunizationsTitle"/></h2>  
            <hr/>
            <table>
                <tr>
                    <td class="title" colspan="4"><bean:message key="oscarEncounter.formIntakeHx.whenImmunized"/>:</td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.hepatitisBSerum"/>:</td>
                    <td><input type="text" name="ImmunizationHepatitisB" value="<%=props.getProperty("ImmunizationHepatitisB","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearHepatitisB" value="<%=props.getProperty("ImmunizationYearHepatitisB","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.tetanusLockjaw"/>:</td>
                    <td><input type="text" name="ImmunizationHadTetanus" value="<%=props.getProperty("ImmunizationHadTetanus","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearTetanus" value="<%=props.getProperty("ImmunizationYearTetanus","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.polio"/>:</td>
                    <td><input type="text" name="ImmunizationHadPolio" value="<%=props.getProperty("ImmunizationHadPolio","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearPolio" value="<%=props.getProperty("ImmunizationYearPolio","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.mmr"/>:</td>
                    <td><input type="text" name="ImmunizationHadMMR" value="<%=props.getProperty("ImmunizationHadMMR","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearMMR" value="<%=props.getProperty("ImmunizationYearMMR","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.lastTBTest"/>:</td>
                    <td><input type="text" name="ImmunizationHadTB" value="<%=props.getProperty("ImmunizationHadTB","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearTB" value="<%=props.getProperty("ImmunizationYearTB","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.rubella"/>:</td>
                    <td><input type="text" name="ImmunizationHadRubella" value="<%=props.getProperty("ImmunizationHadRubella","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearRubella" value="<%=props.getProperty("ImmunizationYearRubella","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.varicella"/>:</td>
                    <td><input type="text" name="ImmunizationHadVaricella" value="<%=props.getProperty("ImmunizationHadVaricella","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearVaricella" value="<%=props.getProperty("ImmunizationYearVaricella","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.meningitis"/>:</td>
                    <td><input type="text" name="ImmunizationHadMeningitis" value="<%=props.getProperty("ImmunizationHadMeningitis","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearMeningitis" value="<%=props.getProperty("ImmunizationYearMeningitis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pneumococcus"/>:</td>
                    <td><input type="text" name="ImmunizationHadPneumococcus" value="<%=props.getProperty("ImmunizationHadPneumococcus","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="ImmunizationYearPneumococcus" value="<%=props.getProperty("ImmunizationYearPneumococcus","")%>"/></td>
                </tr>
                <tr>
                    <td class="title" colspan="4"><bean:message key="oscarEncounter.formIntakeHx.hadDisease"/>:</td>
                </tr>
                                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.hepatitisBSerum"/>:</td>
                    <td><input type="text" name="immunizationDiseaseHepatitisB" value="<%=props.getProperty("immunizationDiseaseHepatitisB","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearHepatitisB" value="<%=props.getProperty("immunizationDiseaseYearHepatitisB","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.tetanusLockjaw"/>:</td>
                    <td><input type="text" name="immunizationDiseaseTetanus" value="<%=props.getProperty("immunizationDiseaseTetanus","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearTetanus" value="<%=props.getProperty("immunizationDiseaseYearTetanus","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.polio"/>:</td>
                    <td><input type="text" name="immunizationDiseasePolio" value="<%=props.getProperty("immunizationDiseasePolio","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearPolio" value="<%=props.getProperty("immunizationDiseaseYearPolio","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.mmr"/>:</td>
                    <td><input type="text" name="immunizationDiseaseMMR" value="<%=props.getProperty("immunizationDiseaseMMR","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearMMR" value="<%=props.getProperty("immunizationDiseaseYearMMR","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.lastTBTest"/>:</td>
                    <td><input type="text" name="immunizationDiseaseTb" value="<%=props.getProperty("immunizationDiseaseTb","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearTb" value="<%=props.getProperty("immunizationDiseaseYearTb","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.rubella"/>:</td>
                    <td><input type="text" name="immunizationDiseaseRubella" value="<%=props.getProperty("immunizationDiseaseRubella","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearRubella" value="<%=props.getProperty("immunizationDiseaseYearRubella","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.varicella"/>:</td>
                    <td><input type="text" name="immunizationDiseaseVaricella" value="<%=props.getProperty("immunizationDiseaseVaricella","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearVaricella" value="<%=props.getProperty("immunizationDiseaseYearVaricella","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.meningitis"/>:</td>
                    <td><input type="text" name="immunizationDiseaseMeningitis" value="<%=props.getProperty("immunizationDiseaseMeningitis","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearMeningitis" value="<%=props.getProperty("immunizationDiseaseYearMeningitis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pneumococcus"/>:</td>
                    <td><input type="text" name="immunizationDiseasePneumococcus" value="<%=props.getProperty("immunizationDiseasePneumococcus","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.year"/>:</td>
                    <td><input type="text" name="immunizationDiseaseYearPneumococcus" value="<%=props.getProperty("immunizationDiseaseYearPneumococcus","")%>"/></td>
                </tr>
                <tr>
                    <td class="title" colspan="3"><bean:message key="oscarEncounter.formIntakeHx.immunizationCardOnPerson"/>:</td>
                    <td><input type="text" name="HaveImmunizationCard" value="<%=props.getProperty("HaveImmunizationCard","")%>"/></td>
                </tr>
            </table>
            
        </html:form>
    </body>   
</html:html>