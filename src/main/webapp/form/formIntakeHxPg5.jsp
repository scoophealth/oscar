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
            
            <!--Medical Conditions-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.medicalConditionsTitle"/></h2>  
            <hr/>
            <table>
                <tr>
                    <td class="title" colspan="4"><bean:message key="oscarEncounter.formIntakeHx.hadConditions"/>:</td>
                </tr>
                
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.brokenBones"/>:</td>
                    <td><input type="text" name="Conditionsbrokenbones" value="<%=props.getProperty("Conditionsbrokenbones","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescbrokenbones" value="<%=props.getProperty("ConditionsDescbrokenbones","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.migraine"/>:</td>
                    <td><input type="text" name="Conditionsmigraine" value="<%=props.getProperty("Conditionsmigraine","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescmigraine" value="<%=props.getProperty("ConditionsDescmigraine","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.neuroDisorder"/>:</td>
                    <td><input type="text" name="Conditionsneurologicdisorder" value="<%=props.getProperty("Conditionsneurologicdisorder","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescneurologicdisorder" value="<%=props.getProperty("ConditionsDescneurologicdisorder","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.asthma"/>:</td>
                    <td><input type="text" name="Conditionsasthma" value="<%=props.getProperty("Conditionsasthma","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescasthma" value="<%=props.getProperty("ConditionsDescasthma","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pneumonia"/>:</td>
                    <td><input type="text" name="Conditionspneumonia" value="<%=props.getProperty("Conditionspneumonia","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescpneumonia" value="<%=props.getProperty("ConditionsDescpneumonia","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.lungDisease"/>:</td>
                    <td><input type="text" name="Conditionslungdisease" value="<%=props.getProperty("Conditionslungdisease","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDesclungdisease" value="<%=props.getProperty("ConditionsDesclungdisease","")%>"/></td>
                </tr>
                 <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.heartDisease"/>:</td>
                    <td><input type="text" name="Conditionsheartdisease" value="<%=props.getProperty("Conditionsheartdisease","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescheartdisease" value="<%=props.getProperty("ConditionsDescheartdisease","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.ulcer"/>:</td>
                    <td><input type="text" name="Conditionsulcer" value="<%=props.getProperty("Conditionsulcer","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDesculcer" value="<%=props.getProperty("ConditionsDesculcer","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.bowelDisease"/>:</td>
                    <td><input type="text" name="Conditionsboweldisease" value="<%=props.getProperty("Conditionsboweldisease","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescboweldisease" value="<%=props.getProperty("ConditionsDescboweldisease","")%>"/></td>
                </tr>
                 <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.hepatitis"/>:</td>
                    <td><input type="text" name="Conditionshepatitis" value="<%=props.getProperty("Conditionshepatitis","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDeschepatitis" value="<%=props.getProperty("ConditionsDeschepatitis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.hivPositive"/>:</td>
                    <td><input type="text" name="ConditionsHIV" value="<%=props.getProperty("ConditionsHIV","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescHIV" value="<%=props.getProperty("ConditionsDescHIV","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.thyroidProblem"/>:</td>
                    <td><input type="text" name="Conditionsthyroid" value="<%=props.getProperty("Conditionsthyroid","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescthyroid" value="<%=props.getProperty("ConditionsDescthyroid","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.bloodDisorder"/>:</td>
                    <td><input type="text" name="Conditionsblooddisorder" value="<%=props.getProperty("Conditionsblooddisorder","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescblooddisorder" value="<%=props.getProperty("ConditionsDescblooddisorder","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.diabetes"/>:</td>
                    <td><input type="text" name="Conditionsdiabetes" value="<%=props.getProperty("Conditionsdiabetes","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescdiabetes" value="<%=props.getProperty("ConditionsDescdiabetes","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.bloodTransfusion"/>:</td>
                    <td><input type="text" name="Conditionsbloodtransfusion" value="<%=props.getProperty("Conditionsbloodtransfusion","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescbloodtransfusion" value="<%=props.getProperty("ConditionsDescbloodtransfusion","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.cancer"/>:</td>
                    <td><input type="text" name="Conditionscancerorleukemia" value="<%=props.getProperty("Conditionscancerorleukemia","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDesccancerorleukemia" value="<%=props.getProperty("ConditionsDesccancerorleukemia","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.sexualDisease"/>:</td>
                    <td><input type="text" name="Conditionssexualdisease" value="<%=props.getProperty("Conditionssexualdisease","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescsexualdisease" value="<%=props.getProperty("ConditionsDescsexualdisease","")%>"/></td>
                </tr>  
                
                <tr>
                    <td class="title" colspan="4"><bean:message key="oscarEncounter.formIntakeHx.hadConditions"/>:</td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.urinary"/>:</td>
                    <td><input type="text" name="ConditionsURI" value="<%=props.getProperty("ConditionsURI","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescURI" value="<%=props.getProperty("ConditionsDescURI","")%>"/></td>
                </tr> 
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.emotional"/>:</td>
                    <td><input type="text" name="Conditionsemotional" value="<%=props.getProperty("Conditionsemotional","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescemotional" value="<%=props.getProperty("ConditionsDescemotional","")%>"/></td>
                </tr> 
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.arthritis"/>:</td>
                    <td><input type="text" name="Conditionsarthritis" value="<%=props.getProperty("Conditionsarthritis","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescarthritis" value="<%=props.getProperty("ConditionsDescarthritis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.eatingDisorder"/>:</td>
                    <td><input type="text" name="Conditionseatingdisorder" value="<%=props.getProperty("Conditionseatingdisorder","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDesceatingdisorder" value="<%=props.getProperty("ConditionsDesceatingdisorder","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.osteoporosis"/>:</td>
                    <td><input type="text" name="Conditionsosteoporosis" value="<%=props.getProperty("Conditionsosteoporosis","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescosteoporosis" value="<%=props.getProperty("ConditionsDescosteoporosis","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.skinProblems"/>:</td>
                    <td><input type="text" name="Conditionsskin" value="<%=props.getProperty("Conditionsskin","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescskin" value="<%=props.getProperty("ConditionsDescskin","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.highBP"/>:</td>
                    <td><input type="text" name="ConditionsHighbloodpressure" value="<%=props.getProperty("ConditionsHighbloodpressure","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescHighbloodpressure" value="<%=props.getProperty("ConditionsDescHighbloodpressure","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.learningDisability"/>:</td>
                    <td><input type="text" name="Conditionslearningdisability" value="<%=props.getProperty("Conditionslearningdisability","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDesclearningdisability" value="<%=props.getProperty("ConditionsDesclearningdisability","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.schizophrenia"/>:</td>
                    <td><input type="text" name="Conditionsschizophrenia" value="<%=props.getProperty("Conditionsschizophrenia","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescschizophrenia" value="<%=props.getProperty("ConditionsDescschizophrenia","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.alcoholDependency"/>:</td>
                    <td><input type="text" name="Conditionsalcohol" value="<%=props.getProperty("Conditionsalcohol","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescalcohol" value="<%=props.getProperty("ConditionsDescalcohol","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.multipleSclerosis"/>:</td>
                    <td><input type="text" name="ConditionsMS" value="<%=props.getProperty("ConditionsMS","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescMS" value="<%=props.getProperty("ConditionsDescMS","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.stroke"/>:</td>
                    <td><input type="text" name="Conditionsstroke" value="<%=props.getProperty("Conditionsstroke","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescstroke" value="<%=props.getProperty("ConditionsDescstroke","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.highCholesterol"/>:</td>
                    <td><input type="text" name="ConditionsHighcholesterol" value="<%=props.getProperty("ConditionsHighcholesterol","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescHighcholesterol" value="<%=props.getProperty("ConditionsDescHighcholesterol","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.depression"/>:</td>
                    <td><input type="text" name="Conditionsdepression" value="<%=props.getProperty("Conditionsdepression","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescdepression" value="<%=props.getProperty("ConditionsDescdepression","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.drugDependancy"/>:</td>
                    <td><input type="text" name="ConditionsDrugdependency" value="<%=props.getProperty("ConditionsDrugdependency","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescDrugdependency" value="<%=props.getProperty("ConditionsDescDrugdependency","")%>"/></td>
                </tr>
                 <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.otherDisease"/>:</td>
                    <td><input type="text" name="ConditionsOtherdisease" value="<%=props.getProperty("ConditionsOtherdisease","")%>"/></td>
                    
                    <td><bean:message key="oscarEncounter.formIntakeHx.describe"/>:</td>
                    <td><input type="text" name="ConditionsDescOtherdisease" value="<%=props.getProperty("ConditionsDescOtherdisease","")%>"/></td>
                </tr>
            </table>
        </html:form>
    </body>   
</html:html>
