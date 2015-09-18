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
            
            <!--Medical History-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.sectionMedHistoryTitle"/></h2>  
            <hr/>
            <table>
                <tr>
                    <td colspan="3" class="title"><bean:message key="oscarEncounter.formIntakeHx.hospitalization"/>?</td>
                    <td><input type="text" name="SeriousIllness" value="<%=props.getProperty("SeriousIllness","")%>"/></td>
                </tr>
                <tr>
                    <td colspan="4" class="title"><bean:message key="oscarEncounter.formIntakeHx.pastIllness"/>:</td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness1"/>:</td>
                    <td><input type="text" name="PastIllness1" value="<%=props.getProperty("PastIllness1","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge1" value="<%=props.getProperty("IllnessAge1","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness2"/>:</td>
                    <td><input type="text" name="PastIllness2" value="<%=props.getProperty("PastIllness2","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge2" value="<%=props.getProperty("IllnessAge2","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness3"/>:</td>
                    <td><input type="text" name="PastIllness3" value="<%=props.getProperty("PastIllness3","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge3" value="<%=props.getProperty("IllnessAge3","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness4"/>:</td>
                    <td><input type="text" name="PastIllness4" value="<%=props.getProperty("PastIllness4","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge4" value="<%=props.getProperty("IllnessAge4","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness5"/>:</td>
                    <td><input type="text" name="PastIllness5" value="<%=props.getProperty("PastIllness5","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge5" value="<%=props.getProperty("IllnessAge5","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.pastIllness6"/>:</td>
                    <td><input type="text" name="PastIllness6" value="<%=props.getProperty("PastIllness6","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="IllnessAge6" value="<%=props.getProperty("IllnessAge6","")%>"/></td>
                </tr> 
                <tr>
                    <td colspan="3" class="title"><bean:message key="oscarEncounter.formIntakeHx.operation"/>?</td>
                    <td><input type="text" name="operations" value="<%=props.getProperty("operations","")%>"/></td>
                </tr>
                <tr>
                    <td colspan="4"class="title"><bean:message key="oscarEncounter.formIntakeHx.operationAndAge"/>:</td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation1"/>:</td>
                    <td><input type="text" name="NameofOperation1" value="<%=props.getProperty("NameofOperation1","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge1" value="<%=props.getProperty("NameofOperationAge1","")%>"/></td>
                </tr>                      
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation2"/>:</td>
                    <td><input type="text" name="NameofOperation2" value="<%=props.getProperty("NameofOperation2","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge2" value="<%=props.getProperty("NameofOperationAge2","")%>"/></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation3"/>:</td>
                    <td><input type="text" name="NameofOperation3" value="<%=props.getProperty("NameofOperation3","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge3" value="<%=props.getProperty("NameofOperationAge3","")%>"/></td>
                </tr> 
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation4"/>:</td>
                    <td><input type="text" name="NameofOperation4" value="<%=props.getProperty("NameofOperation4","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge4" value="<%=props.getProperty("NameofOperationAge4","")%>"/></td>
                </tr> 
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation5"/>:</td>
                    <td><input type="text" name="NameofOperation5" value="<%=props.getProperty("NameofOperation5","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge5" value="<%=props.getProperty("NameofOperationAge5","")%>"/></td>
                </tr> 
                <tr>
                    <td><bean:message key="oscarEncounter.formIntakeHx.operation6"/>:</td>
                    <td><input type="text" name="NameofOperation6" value="<%=props.getProperty("NameofOperation6","")%>"/></td>
                    <td><bean:message key="oscarEncounter.formIntakeHx.age"/>:</td>
                    <td><input type="text" name="NameofOperationAge6" value="<%=props.getProperty("NameofOperationAge6","")%>"/></td>
                </tr> 
            </table>
         </html:form>
    </body>   
</html:html>
