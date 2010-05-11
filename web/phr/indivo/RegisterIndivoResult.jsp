<%-- 
    Document   : RegisterIndivoSuccess
    Created on : 9-Jun-2008, 11:03:24 AM
    Author     : apavel
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Indivo Registration Result</title>
        <link rel="stylesheet" type="text/css" href="../phr.css">
        <style type="text/css">
            div.messageDiv {
                position: absolute; 
                top: 30%;
                width: 90%;
                left: 5%;
                text-align: center;
                font-size: 12px;
            }
        </style>
        <script type="text/javascript" language="javascript">
        function closeWindow() {
           window.close();
           if (!window.opener.closed) {
               window.opener.location.reload();
               window.opener.focus();
           }
        }
        </script>
    </head>
    <body>
        <%if (request.getParameter("failmessage") != null) {%>
            <div class="objectRed messageDiv">
                <div class="objectRedHeader">
                    Error: Failed to add user:
                </div>
                Failed to add a user:<br/>
                <%=request.getParameter("failmessage")%>
                <br/>
                <input type="button" onclick="closeWindow()" name="closeButton" value="Close Window">
            </div>
        <%} else {%>
            <div class="objectGreen messageDiv">
                <div class="objectGreenHeader">
                    User was successfully added.
                </div>
                The user and corresponding permissions have been added.  All checked providers
                    must authorize this demographic as "patient" from the OSCAR Personal Health Record
                    message screen before the patient can communicate with them.
                    <br/>
                    <input type="button" onclick="closeWindow()" name="closeButton" value="Close Window">
            </div>
        <%}%>
    </body>
</html>
