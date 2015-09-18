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
            
            <!--Social History-->    
            <h2><bean:message key="oscarEncounter.formIntakeHx.socialHistoryTitle"/></h2>  
            <hr/>
            <table> 
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.wearSeatbelt"/></td>
                    <td><input type="text" name="SeatBelt" value="<%=props.getProperty("SeatBelt","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.smoker"/></td>
                    <td><input type="text" name="smoker" value="<%=props.getProperty("smoker","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.smokingAmount"/></td>
                    <td><input type="text" name="HowMuchSmoke" value="<%=props.getProperty("HowMuchSmoke","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.pastSmoking"/></td>
                    <td><input type="text" name="smokeInPast" value="<%=props.getProperty("smokeInPast","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.streetDrugs"/></td>
                    <td><input type="text" name="UseDrugs" value="<%=props.getProperty("UseDrugs","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.drinkAlcohol"/></td>
                    <td><input type="text" name="Alcohol" value="<%=props.getProperty("Alcohol","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.numberOfDrinks"/></td>
                    <td><input type="text" name="HowManyDrinks" value="<%=props.getProperty("HowManyDrinks","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.drinksPerWeek"/></td>
                    <td><input type="text" name="HowManyDrinksWeek" value="<%=props.getProperty("HowManyDrinksWeek","")%>"/></td>
                </tr>
                <tr>
                    <td  class="title"><bean:message key="oscarEncounter.formIntakeHx.hoursOfExercise"/></td>
                    <td><input type="text" name="exercise" value="<%=props.getProperty("exercise","")%>"/></td>
                </tr>
            </table>
            
        </html:form>
    </body>   
</html:html>