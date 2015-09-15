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
            
            <!--Family History-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.familyHistoryTitle"/></h2>  
            <hr/>
            <table> 
                <tr>
                    <td class="title" colspan="4"><bean:message key="oscarEncounter.formIntakeHx.familyHistory.familyConditions"/>:</td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.migraine"/>:</td>
                    <td><input type="text" name="biologicalmigraine" value="<%=props.getProperty("biologicalmigraine","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescmigraine" value="<%=props.getProperty("biologicalDescmigraine","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.neuroDisorder"/>:</td>
                    <td><input type="text" name="biologicalneurologic" value="<%=props.getProperty("biologicalneurologic","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescneurologic" value="<%=props.getProperty("biologicalDescneurologic","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.asthma"/>:</td>
                    <td><input type="text" name="biologicalasthma" value="<%=props.getProperty("biologicalasthma","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescasthma" value="<%=props.getProperty("biologicalDescasthma","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.pheumonia"/>:</td>
                    <td><input type="text" name="biologicalpneumonia" value="<%=props.getProperty("biologicalpneumonia","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescpneumonia" value="<%=props.getProperty("biologicalDescpneumonia","")%>"/></td>
                </tr>                  
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.lungDisease"/>:</td>
                    <td><input type="text" name="biologicallungdisease" value="<%=props.getProperty("biologicallungdisease","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDesclungdisease" value="<%=props.getProperty("biologicalDesclungdisease","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.heartDisease"/>:</td>
                    <td><input type="text" name="biologicalheartdisease" value="<%=props.getProperty("biologicalheartdisease","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescheartdisease" value="<%=props.getProperty("biologicalDescheartdisease","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.ulcer"/>:</td>
                    <td><input type="text" name="biologicalulcer" value="<%=props.getProperty("biologicalulcer","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDesculcer" value="<%=props.getProperty("biologicalDesculcer","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.bowelDisease"/>:</td>
                    <td><input type="text" name="biologicalboweldisease" value="<%=props.getProperty("biologicalboweldisease","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescboweldisease" value="<%=props.getProperty("biologicalDescboweldisease","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.hepatitis"/>:</td>
                    <td><input type="text" name="biologicalhepatitis" value="<%=props.getProperty("biologicalhepatitis","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDeschepatitis" value="<%=props.getProperty("biologicalDeschepatitis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.thyroid"/>:</td>
                    <td><input type="text" name="biologicalthyroid" value="<%=props.getProperty("biologicalthyroid","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescthyroid" value="<%=props.getProperty("biologicalDescthyroid","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.bloodDisorder"/>:</td>
                    <td><input type="text" name="biologicalblooddisorder" value="<%=props.getProperty("biologicalblooddisorder","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescblooddisorder" value="<%=props.getProperty("biologicalDescblooddisorder","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.diabetes"/>:</td>
                    <td><input type="text" name="biologicaldiabetes" value="<%=props.getProperty("biologicaldiabetes","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescdiabetes" value="<%=props.getProperty("biologicalDescdiabetes","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.bloodTransfusion"/>:</td>
                    <td><input type="text" name="biologicalbloodtransfusion" value="<%=props.getProperty("biologicalbloodtransfusion","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescbloodtransfusion" value="<%=props.getProperty("biologicalDescbloodtransfusion","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.cancer"/>:</td>
                    <td><input type="text" name="biologicalcancerorleukemia" value="<%=props.getProperty("biologicalcancerorleukemia","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDesccancerorleukemia" value="<%=props.getProperty("biologicalDesccancerorleukemia","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.uri"/>:</td>
                    <td><input type="text" name="biologicalURI" value="<%=props.getProperty("biologicalURI","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescURI" value="<%=props.getProperty("biologicalDescURI","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.emotional"/>:</td>
                    <td><input type="text" name="biologicalemotional" value="<%=props.getProperty("biologicalemotional","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescemotional" value="<%=props.getProperty("biologicalDescemotional","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.arthritis"/>:</td>
                    <td><input type="text" name="biologicalarthritis" value="<%=props.getProperty("biologicalarthritis","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescarthritis" value="<%=props.getProperty("biologicalDescarthritis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.osteoporosis"/>:</td>
                    <td><input type="text" name="biologicalosteoporosis" value="<%=props.getProperty("biologicalosteoporosis","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescosteoporosis" value="<%=props.getProperty("biologicalDescosteoporosis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.skinProblems"/>:</td>
                    <td><input type="text" name="biologicalskin" value="<%=props.getProperty("biologicalskin","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescskin" value="<%=props.getProperty("biologicalDescskin","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.highBP"/>:</td>
                    <td><input type="text" name="biologicalHBP" value="<%=props.getProperty("biologicalHBP","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescHBP" value="<%=props.getProperty("biologicalDescHBP","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.learningDisability"/>:</td>
                    <td><input type="text" name="biologicallearningdisability" value="<%=props.getProperty("biologicallearningdisability","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDesclearningdisability" value="<%=props.getProperty("biologicalDesclearningdisability","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.schizophrenia"/>:</td>
                    <td><input type="text" name="biologicalschizophrenia" value="<%=props.getProperty("biologicalschizophrenia","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescschizophrenia" value="<%=props.getProperty("biologicalDescschizophrenia","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.alcoholDependency"/>:</td>
                    <td><input type="text" name="biologicalalcohol" value="<%=props.getProperty("biologicalalcohol","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescalcohol" value="<%=props.getProperty("biologicalDescalcohol","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.multipleSclerosis"/>:</td>
                    <td><input type="text" name="biologicalMS" value="<%=props.getProperty("biologicalMS","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescMS" value="<%=props.getProperty("biologicalDescMS","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.stroke"/>:</td>
                    <td><input type="text" name="biologicalstroke" value="<%=props.getProperty("biologicalstroke","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescstroke" value="<%=props.getProperty("biologicalDescstroke","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.cholesterol"/>:</td>
                    <td><input type="text" name="biologicalhighcholesterol" value="<%=props.getProperty("biologicalhighcholesterol","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDeschighcholesterol" value="<%=props.getProperty("biologicalDeschighcholesterol","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.depression"/>:</td>
                    <td><input type="text" name="biologicaldepression" value="<%=props.getProperty("biologicaldepression","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescdepression" value="<%=props.getProperty("biologicalDescdepression","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.familyHistory.drugDependency"/>:</td>
                    <td><input type="text" name="biologicaldrug" value="<%=props.getProperty("biologicaldrug","")%>"/></td>

                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="biologicalDescdrug" value="<%=props.getProperty("biologicalDescdrug","")%>"/></td>
                </tr>
            </table>
         </html:form>
    </body>   
</html:html>