<%-- Wanted to use this, but never got around to it --%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<%@ page import="org.oscarehr.phr.PHRAuthentication"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>

<%
String providerName = request.getSession().getAttribute("userfirstname") + " " + 
        request.getSession().getAttribute("userlastname");
String providerNo = (String) request.getSession().getAttribute("user");
ProviderData providerData = new ProviderData();
providerData.setProvider_no(providerNo);
String providerPhrId = providerData.getMyOscarId();
PHRAuthentication phrAuth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
   <style type="text/css">
        .myoscarLoginElementNoAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #f3e9e9;
        }
        
        .myoscarLoginElementAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #d9ecd8;
        }
        .moreInfoBoxoverBody{
            border: 1px solid #9fbbe8;
            padding: 1px;
            padding-left: 3px;
            padding-right: 3px;
            border-top: 0px;
            font-size: 10px;
            background-color: white;
        }
        .moreInfoBoxoverHeader{
            border: 1px solid #9fbbe8;
            background-color: #e8ecf3;
            padding: 2px;
            padding-left: 3px;
            padding-right: 3px;
            border-bottom: 0px;
            font-size: 10px;
            color: red;
        }
   </style>
   <logic:present name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
       <span class="myoscarLoginElementAuth">
           Status: <b>Logged in as <%=providerName%></b> (<%=phrAuth.getUserId()%>)<br/>
           <form action="../phr/Logout.do" name="phrLogout" method="POST">
               <input type="hidden" name="forwardto" value="<%=(String) request.getAttribute("forwardto")%>">
               <center><a href="javascript:;" onclick="document.forms['phrLogout'].submit();">Logout</a></center>
           </form>
       </span>
       <!--<p style="background-color: #E00000"  title="fade=[on] requireclick=[on] header=[Diabetes Med Changes] body=[<span style='color:red'>no DM Med changes have been recorded</span> </br>]">dsfsdfsdfsdfgsdgsdg</p>-->
   </logic:present>
   <logic:notPresent name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
        <span class="myoscarLoginElementNoAuth">
            <form action="../phr/Login.do" name="phrLogin" method="POST">
                <%request.setAttribute("phrUserLoginErrorMsg", request.getParameter("phrUserLoginErrorMsg"));
                request.setAttribute("phrTechLoginErrorMsg", request.getParameter("phrTechLoginErrorMsg"));%>
                <logic:present name="phrUserLoginErrorMsg">
                    <div class="phrLoginErrorMsg"><font color="red"><bean:write name="phrUserLoginErrorMsg"/></font>  
                    <logic:present name="phrTechLoginErrorMsg">
                        <a href="javascript:;" title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[<bean:write name="phrTechLoginErrorMsg"/> </br>]">More Info</a></div>
                    </logic:present>
                </logic:present>
                Status: <b>Not logged in</b><br/>
                <%=providerName%> password: <input type="password" id="phrPassword" name="phrPassword" style="font-size: 8px; width: 40px;"> <a href="javascript: document.forms['phrLogin'].submit()">Login</a>
                <input type="hidden" name="forwardto" value="<%=(String) request.getAttribute("forwardto")%>">
            </form>
        </span>
   </logic:notPresent>
