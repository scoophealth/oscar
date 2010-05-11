<%-- 
    Document   : PHRRedirectToPhrForm
    Created on : 27-Jul-2009, 12:36:58 PM
    Author     : apavel
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Redirecting</title>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <script type="text/javascript" language="JavaScript">
            function onloadd() {
                document.forms["autosubmit"].submit();
            }
        </script>
    </head>
    <body onload="onloadd()">
        <form action="<bean:write name="url"/>" name="autosubmit" method="POST">
            <input type="hidden" name="userid" value="<bean:write name="userid"/>">
            <input type="hidden" name="ticket" value="<bean:write name="ticket"/>">
            <input type="hidden" name="viewpatient" value="<bean:write name="viewpatient"/>">
        </form>
    </body>
</html>
