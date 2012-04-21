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
    Document   : PHRRedirectToPhrForm
    Created on : 27-Jul-2009, 12:36:58 PM
    Author     : apavel
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <title>Redirecting</title>
        <script type="text/javascript" language="JavaScript">
            function onloadd() {
                document.forms["autosubmit"].submit();
            }
        </script>
    </head>
    <body onload="onloadd()">
        <form action="<bean:write name="url"/>" name="autosubmit" method="POST">
            <input type="hidden" name="userName" value="<bean:write name="userName"/>">
            <input type="hidden" name="password" value="<bean:write name="password"/>">
            <input type="hidden" name="viewpatient" value="<bean:write name="viewpatient"/>">
        </form>
    </body>
</html>
