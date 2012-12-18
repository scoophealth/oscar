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

<%--
    Document   : registerIndivo
    Created on : 12-May-2008, 10:59:27 AM
    Author     : apavel
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.phr.RegistrationHelper"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>
<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.oscarehr.common.model.DemographicExt"%>
<%@ page import="org.oscarehr.common.dao.DemographicExtDao"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="oscar.oscarProvider.data.ProviderMyOscarIdData"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="java.util.*"%>


<%
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

String demographicNo = request.getParameter("demographicNo");
int demographicId=Integer.parseInt(demographicNo);

org.oscarehr.common.model.Demographic demographic = new DemographicData().getDemographic(demographicNo);
request.setAttribute("demographic", demographic);

String defaultNewUserName=RegistrationHelper.getDefaultUserName(demographicId);

String hPhoneExt = demographicExtDao.getValueForDemoKey(Integer.parseInt(demographicNo), "hPhoneExt");
String wPhoneExt = demographicExtDao.getValueForDemoKey(Integer.parseInt(demographicNo), "wPhoneExt");
if (hPhoneExt != null)
    request.setAttribute("demographicHomeExt", " " + hPhoneExt);
if (wPhoneExt != null)
    request.setAttribute("demographicWorkExt", " " + wPhoneExt);
%>

<html>
    <head>
                <title>Register Indivo</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/phr/phr.css">
        <style type="text/css" language="JavaScript">
            .headingTop {
                width: 100%;
                text-align: center;
                font-weight: bold;
            }
            td.error {
            	color: red;
            }
            tr.userrow td {
            	border-bottom:1px solid black;

            }
        </style>
        <script type="text/javascript" language="JavaScript">
            function validateNSubmit() {
                if (document.getElementById("username").value == '') {
                    alert ("Username is required");
                    document.getElementById("username").focus();
                    return;
                } else if (document.getElementById("password").length < 8) {
                    alert ("Password must be at least 8 characters long");
                    document.getElementById("password").focus();
                    return;
                }

                //if everything is cool:
                document.getElementById("submitButton").disabled = true;
                document.getElementById("closeButton").disabled = true;
                document.getElementById("submitButton").value = "Creating user...";
                document.getElementById("submittedMessage").style.display = 'block';
                document.getElementById("registrationForm").submit();
            }
            function enableSubmit() {
                document.getElementById("submitButton").disabled = false;
                document.getElementById("closeButton").disabled = false;
            }
            function disableForm() {
                var formElements = document.getElementById("registrationForm").elements;
                for (var i=0; i<formElements.length; i++) {
                    formElements[i].disabled = true;
                }
                document.getElementById("submitButton").disabled = true;
                document.getElementById("closeButton").disabled = true;
            }

        </script>
    </head>
    <body>
	    <%
	    	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
	    	if (myOscarLoggedInInfo == null || !myOscarLoggedInInfo.isLoggedIn())
	    	{
	    		%>
		            <jsp:include page="../AuthInclude.jsp">
		                <jsp:param name="forwardto" value="<%=\"/phr/indivo/RegisterIndivo.jsp?demographicNo=\" + demographicNo%>"/>
		                <jsp:param name="pathtophr" value="<%=request.getContextPath() + \"/phr\"%>"/>
		            </jsp:include>
		        <%
	    	}
		%>
        <html-el:form action="/phr/UserManagement" styleId="registrationForm" method="POST">
                <html-el:hidden property="method" value="registerUser"/>
                <html-el:hidden property="demographicNo" value="<%=demographicNo%>"/>
            <div class="headingTop">MyOSCAR Patient Registration</div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Login Information
                </div>
                <table>
                    <tr>
                        <td>Username: </td>
                        <td><html-el:text styleId="username" property="username" value="<%=defaultNewUserName%>" size="30"/></td>
                    </tr>
                    <tr>
                        <td>Password (default generated): </td>
                        <td><html-el:text styleId="password" property="password" value="<%=RegistrationHelper.getNewRandomPassword()%>"/></td>
                    </tr>
                </table>
            </div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Personal Information
                </div>
                <table>
                    <tr>
                        <td>Name (First, Last):</td>
                        <td>
                            <html-el:text property="firstName" value="${demographic.firstName}" size="20"/>
                            <html-el:text property="lastName" value="${demographic.lastName}" size="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Street Address</td>
                        <td><html-el:text property="address" value="${demographic.address}" size="40"/></td>
                    </tr>
                    <tr>
                        <td>City</td>
                        <td><html-el:text property="city" value="${demographic.city}" size="20"/></td>
                    </tr>
                    <tr>
                        <td>Province</td>
                        <td><html-el:text property="province" value="${demographic.province}" size="20"/></td>
                    </tr>
                    <tr>
                        <td>Postal Code</td>
                        <td><html-el:text property="postal" value="${demographic.postal}" size="10"/></td>
                    </tr>
                    <tr>
                        <td>Home Phone Number:</td>
                        <td><html-el:text property="phone" value="${demographic.phone}${demographicHomeExt}" size="20"/></td>
                    </tr>
                    <tr>
                        <td>Work Phone Number:</td>
                        <td><html-el:text property="phone2" value="${demographic.phone2}${demographicWorkExt}" size="20"/></td>
                    </tr>
                    <tr>
                        <td>E-mail</td>
                        <td><html-el:text property="email" value="${demographic.email}" size="30"/></td>
                    </tr>
                    <tr>
                        <td>Date of Birth</td>
                        <td><html-el:text property="dob" value="<%=DemographicData.getDob(demographic,\"/\")%>" size="10"/> (YYYY-MM-DD)</td>
                    </tr>
                </table>
            </div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Relationships
                </div>
                <table>
                    		<tr>
                    			<th>&nbsp;</th>
                    			<th>Provider</th>
                    			<th>Relationship</th>
                    			<th>Allow patients to send messages</th>
                    		</tr>
                	<%
                		TreeMap<String, Provider> myOscarProviders=RegistrationHelper.getMyOscarProviders();
                		
                		for (Map.Entry<String, Provider> entry : myOscarProviders.entrySet())
                		{
                			Long providerMyOscarId=MyOscarUtils.getMyOscarUserIdFromOscarProviderNo(myOscarLoggedInInfo, entry.getValue().getProviderNo());
		                	%>
		                	<tr class="userrow">
		                		<%if (providerMyOscarId != null){ %>
		                		<td><input type="checkbox" name="enable_primary_relation_<%=providerMyOscarId%>" <%=RegistrationHelper.getCheckedString(session, "enable_primary_relation_"+providerMyOscarId)%> /></td>
		                        <td><%=StringEscapeUtils.escapeHtml(entry.getKey()+" ("+entry.getValue().getFormattedName()+')')%></td>
		                		<td><%=RegistrationHelper.renderRelationshipSelect(session, "primary_relation_"+providerMyOscarId)%></td>
		                        <td align="center"><input type="checkbox" name="reverse_relation_<%=providerMyOscarId%>" value="PATIENT" <%=RegistrationHelper.getCheckedStringWithValueString(session, "reverse_relation_"+providerMyOscarId,"PATIENT")%> ></td>
		                        <%}else{ %>
		                		<td>&nbsp</td>
		                        <td class="error" ><%=StringEscapeUtils.escapeHtml(entry.getKey()+" ("+entry.getValue().getFormattedName()+')')%></td>
		                		<td colspan="2" class="error">Not Found on Server</td>
		                		<%} %>
		                	</tr>
		                    <%
                		}
                    %>
                </table>
            </div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Submit
                </div>
                <center>
                    <html-el:button onclick="validateNSubmit();" property="submitButton" styleId="submitButton" value="Submit"/><html-el:button property="closeButton" styleId="closeButton" value="Close" onclick="window.close();"/>
                </center>
           </div>
       </html-el:form>
       <div id="submittedMessage" class="objectGreen" style="position: absolute; width: 50%; height: 20%; display: none; top: 40%; left: 25%; text-align: center; font-size: 12px;">
           <div class="objectGreenHeader">
               Creating a new user
           </div>
           <br/>
           Creating a new user......  <br/>
           <b>Do not close this window.</b>
       </div>
       <script type="text/javascript" language="JavaScript">
           enableSubmit();
       </script>
	    <%
	    	if (myOscarLoggedInInfo == null || !myOscarLoggedInInfo.isLoggedIn())
	    	{
	    		%>
		           <script type="text/javascript" language="JavaScript">
		               disableForm();
		           </script>
		        <%
	    	}
		%>
    </body>
</html>
