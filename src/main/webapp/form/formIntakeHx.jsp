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
    boolean authed3=true;
%>
<security:oscarSec roleName="<%=roleName3$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed3=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed3) {
		return;
	}
%>

<!DOCTYPE html>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

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
            
            <!--Demographic Information-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.sectionDemographicTitle"/></h2>
            <hr/> 
            
            <table>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.chartNo"/>:</td>
                    <td><input type="text" name="student_number" value="<%=props.getProperty("student_number","")%>"/></td> 
                </tr>   
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.firstName"/>:</td>
                    <td><input type="text" name="student_firstname" value="<%=props.getProperty("student_firstname","")%>" /></td>                   
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.lastName"/>:</td>
                    <td><input type="text" name="student_surname" value="<%=props.getProperty("student_surname","")%>" /></td>                    
                </tr>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.dob"/>:</td>
                    <td><input type="text" name="student_dob" value="<%=props.getProperty("student_dob","")%>"/></td> 

                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.sex"/>:</td>
                    <td><input type="text" name="student_sex" value="<%=props.getProperty("student_sex","")%>"/></td> 
                </tr>           
            </table>
            
            <!--Emergency Contact-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.sectionEmergencyContact"/></h2>              
            <hr/> 
            <table>
                 <tr>
                     <td class="title"><bean:message key="oscarEncounter.formIntakeHx.emergencyName"/>:</td>
                     <td><input type="text" name="student_ercontact_name" value="<%=props.getProperty("student_ercontact_name","")%>"/></td> 
                 </tr>
                 <tr>
                     <td class="title"><bean:message key="oscarEncounter.formIntakeHx.emergencyPhone"/>:</td>
                     <td><input type="text" name="student_ercontact_phone" value="<%=props.getProperty("student_ercontact_phone","")%>"/></td>
                 </tr>
                 <tr>
                     <td class="title"><bean:message key="oscarEncounter.formIntakeHx.emergencyAddress"/>:</td>
                     <td><input type="text" name="student_ercontact_address" value="<%=props.getProperty("student_ercontact_address","")%>"/></td>
                 </tr>
                 <tr>   
                     <td></td>
                     <td><input type="text" name="student_ercontact_address2" value="<%=props.getProperty("student_ercontact_address2","")%>"/></td>
                 </tr>
            </table>
            
            <!--Home Physician-->     
            <h2><bean:message key="oscarEncounter.formIntakeHx.sectionHomePhysician"/></h2>              
            <hr/>
            <table>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.physicianName"/>:</td>
                    <td><input type="text" name="student_physician_name" value="<%=props.getProperty("student_physician_name","")%>"/></td>
                </tr>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.physicianTelephone"/>:</td>
                    <td><input type="text" name="student_physician_phone" value="<%=props.getProperty("student_physician_phone","")%>"/></td>  
                </tr>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.physicianAddress"/>:</td>
                    <td><input type="text" name="student_physician_address" value="<%=props.getProperty("student_physician_address","")%>"/></td>  
                </tr>
                <tr>
                    <td></td>
                    <td><input type="text" name="student_physician_address2" value="<%=props.getProperty("student_physician_address2","")%>"/></td>  
                </tr>                                
            </table>
            
            <!--Academic Information-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.sectionAcademicInfo"/></h2>              
            <hr/>
            <table>               
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.academicFaculty"/>:</td>
                    <td><input type="text" name="student_faculty_phone" value="<%=props.getProperty("student_faculty_phone","")%>"/></td> 
                </tr>
<%
                String enrollment = props.getProperty("pt_ft",LocaleUtils.getMessage(request, "oscarEncounter.formIntakeHx.notSpecified"));
                if (enrollment.equals("F")) {
                    enrollment=LocaleUtils.getMessage(request, "oscarEncounter.formIntakeHx.academicFullTime");
                } else if (enrollment.equals("P")) {
                    enrollment=LocaleUtils.getMessage(request, "oscarEncounter.formIntakeHx.academicPartTime");;
                } 
%>                               
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.academicEnrollment"/>:</td>
                    <td><input type="text" name="pt_ft" value="<%=enrollment%>"/></td> 
                </tr>
                <tr>
                    <td class="title"><bean:message key="oscarEncounter.formIntakeHx.academicYear"/>:</td>
                    <td><input type="text" name="academic_year" value="<%=props.getProperty("academic_year","")%>"/></td> 
                </tr>                
            </table>
                
        </html:form>
    </body>   
</html:html>
