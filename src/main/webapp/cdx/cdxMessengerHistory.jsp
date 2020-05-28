
<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxMessenger" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxMessengerDao" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="refresh" content="180">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>History</title>

    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">
        <%
          CdxMessengerDao cdxMessengerDao= SpringUtils.getBean(CdxMessengerDao.class);
          List<CdxMessenger> cdxMessengers= cdxMessengerDao.findHistory();
        %>
    <html>
<head>
    <title>History</title>

    <style>
        .ovalbutton{
            border-radius: 22px;
            background-color: #E8E8E8;
        }

    </style>
</head>
<body>
<div class="container">


      <center><h4> <a href="../cdx/cdxMessengerHistory.jsp" target="_blank" class="btn ovalbutton" style="text-align: center;" role="button">History</a></h4>
      </center>
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Author</th>
            <th>Patient</th>
            <th>Recipients</th>
            <th>Document Type</th>
            <th>Category</th>
            <th>Content</th>
            <th>Time</th>
            <th>Delivery Status</th>
        </tr>
        </thead>
        <tbody>
        <%

            if(cdxMessengers!=null && !cdxMessengers.isEmpty())
            {

                for(CdxMessenger n: cdxMessengers)
                {
        %>
        <tr>
            <td ><%=n.getAuthor()%>
            </td>

            <td> <%=n.getPatient()%>
            </td>
            <td> <%=n.getRecipients()%>
            </td>
            <td> <%=n.getDocumentType()%>
            </td>
            <td> <%=n.getCategory()%>
            </td>
            <td> <%=n.getContent()%>
            </td>
            <td> <%=n.getTimeStamp()%>
            </td>
            <td> <%=n.getDeliveryStatus()%>
            </td>

        </tr>
        <%
                }
            }

        %>
        </tbody>
    </table>
</div>
</body>
</html>
