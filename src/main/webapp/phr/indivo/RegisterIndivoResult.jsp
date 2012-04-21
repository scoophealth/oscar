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
    Document   : RegisterIndivoSuccess
    Created on : 9-Jun-2008, 11:03:24 AM
    Author     : apavel
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
   <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%
String dispDocNo = request.getParameter("DocId");
String user = (String) session.getAttribute("user");
%>
<html>
    <head>
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
                	The user and corresponding permissions have been added. 
                    <br/>
                    <input type="button" onclick="closeWindow()" name="closeButton" value="Close Window">
                    <a href="../../dms/ManageDocument.do?method=display&doc_no=<%=dispDocNo%>&providerNo=<%=user%>" >Registration Letter</a>
            </div>
        <%}%>
    </body>
</html>
