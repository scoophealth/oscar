<%-- 
    Document   : registerIndivo
    Created on : 12-May-2008, 10:59:27 AM
    Author     : apavel
--%>

<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>
<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="oscar.oscarDemographic.data.DemographicExt"%>
<%@ page import="oscar.oscarProvider.data.ProviderMyOscarIdData"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="java.util.*"%>


<%
String demographicNo = request.getParameter("demographicNo");

DemographicData.Demographic demographic = new DemographicData().getDemographic(demographicNo);
request.setAttribute("demographic", demographic);

DemographicExt demographicExt = new DemographicExt();
String hPhoneExt = demographicExt.getValueForDemoKey(demographicNo, "hPhoneExt");
String wPhoneExt = demographicExt.getValueForDemoKey(demographicNo, "wPhoneExt");
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
        </style>
        <script type="text/javascript" language="JavaScript">
            function validateNSubmit() {
                if (document.getElementById("username").value == '') {
                    alert ("Username is required");
                    document.getElementById("username").focus();
                    return;
                } else if (document.getElementById("username").value.search('@') == -1) {
                    alert ("Username must be in format 'username@domain.org");
                    document.getElementById("username").focus();
                    return;
                } else if (document.getElementById("password").length < 8) {
                    alert ("Password must be at least 8 characters long");
                    document.getElementById("password").focus();
                    return;
                } else if (document.getElementById("password").value != document.getElementById("passwordRepeat").value) {
                    alert ("Passwords must match");
                    document.getElementById("password").value = "";
                    document.getElementById("passwordRepeat").value = "";
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
        <phr:IfNotPHRAuthenticated>
            <jsp:include page="../AuthInclude.jsp">
                <jsp:param name="forwardto" value="<%=\"/phr/indivo/RegisterIndivo.jsp?demographicNo=\" + demographicNo%>"/>
                <jsp:param name="pathtophr" value="<%=request.getContextPath() + \"/phr\"%>"/>
            </jsp:include>
        </phr:IfNotPHRAuthenticated>
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
                        <td><html-el:text styleId="username" property="username" value="" size="30"/></td>
                    </tr>
                    <tr>
                        <td>Password: </td>
                        <td><html-el:password styleId="password" property="password" value=""/></td>
                    </tr>
                    <tr>
                        <td>Repeat Password: <br/>(min 8 char) </td>
                        <td><html-el:password styleId="passwordRepeat" property="passwordRepeat" value=""/></td>
                    </tr>
                </table>
            </div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Personal Information
                </div>
                <table>
                    <html-el:hidden property="role" value="patient"/>
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
                        <td><html-el:text property="dob" value="<%=demographic.getDob(\"/\")%>" size="10"/></td>
                    </tr>
                </table>
            </div>
            <div class="objectBlue">
                <div class="objectBlueHeader">
                    Grant Access Rights:
                </div>
                    <%ArrayList<String> providerNums = (ArrayList<String>) ProviderMyOscarIdData.listMyOscarProviderNums();
                    for (int i=0; i<providerNums.size(); i++) {
                        String curProviderNo = providerNums.get(i);
                        ProviderData pd = new ProviderData(curProviderNo);
                        if (pd.getFirst_name() == null) continue; %>
                    <span style="white-space: nowrap; width: 40px;"><input type="checkbox" name="list:grantProviders" value="<%=curProviderNo%>"/><%=pd.getFirst_name()%> <%=pd.getLast_name()%></span>&nbsp; &nbsp;
                    <%}%>
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
       <phr:IfNotPHRAuthenticated>
           <script type="text/javascript" language="JavaScript">
               disableForm();
           </script>
       </phr:IfNotPHRAuthenticated>
    </body>
</html>
