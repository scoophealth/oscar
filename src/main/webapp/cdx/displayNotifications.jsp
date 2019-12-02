
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
<%@ page import="org.oscarehr.integration.cdx.dao.NotificationDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.integration.cdx.model.Notification" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Notification</title>

    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">


    <script type="text/javascript">

        $(document).ready(function () {
            $('body').on('click', '#selectAll', function () {
                if ($(this).hasClass('allChecked')) {
                    $('input[type="checkbox"]', '#myform').prop('checked', false);
                } else {
                    $('input[type="checkbox"]', '#myform').prop('checked', true);
                }
                $(this).toggleClass('allChecked');
            })
        });
    </script>

</head>
<%
    NotificationDao notificationDao= SpringUtils.getBean(NotificationDao.class);
%>
<%
    String error[]=request.getParameterValues("Errors");
    Notification not;
    if(error!=null && error.length!=0 )
    {
        for(String e: error)
        {
           not=notificationDao.findById(e);
           not.setSeenBy(session.getAttribute("userfirstname")+","+session.getAttribute("userlastname"));
           not.setSeenAt(new Timestamp(new Date().getTime()));
           try
           {
               notificationDao.merge(not);
           }
           catch(Exception ex)
           {
               MiscUtils.getLogger().error("Got exception saving notification Information " + ex.getMessage());
           }
        }
        out.print("<br><div><center><strong><font size=\"2px\">*All the SEEN warnings/Errors are logged and ignored!. </font> </strong></center></div>");
    }



%>

<body>
<form action="displayNotifications.jsp" method="post" id="myform">
<div class="container">
    <table class="table table-bordered">
      <h3>System Errors </h3>
        <thead>
        <tr>
            <th>Check to ignore</th>
            <th>Message</th>
            <th>Generated At</th>
        </tr>
        </thead>
        <tbody>
        <%

           List<Notification> notifications=notificationDao.findNotificationsError();

            if(notifications!=null && !notifications.isEmpty())
            {

                    for(Notification n: notifications)
                    {
        %>
        <tr >
            <td>

                <input type=checkbox name="Errors" value=<%=n.getId()%>>

            </td>
            <td ><%=n.getMessage()%>
            </td>

            <td> <%=n.getGeneratedAt()%>
            </td>

        </tr>
        <%
                }
            }

        %>
        </tbody>
    </table>
</div>
<hr>
<div class="container">
    <table class="table table-bordered">
       <h3>System Warnings</h3>
        <thead>
        <tr>
            <th>Check to ignore</th>
            <th>Message</th>
            <th>Generated At</th>
        </tr>
        </thead>
        <tbody>
        <%

            List<Notification> notificationsw=notificationDao.findNotificationsWarning();

            if(notificationsw!=null && !notificationsw.isEmpty() )
            {


                for(Notification w: notificationsw)
                {
        %>
        <tr >
            <td>

                <input type=checkbox name="Errors" value=<%=w.getId()%>>

            </td>
            <td ><%=w.getMessage()%>
            </td>

            <td> <%=w.getGeneratedAt()%>
            </td>

        </tr>
        <%
                }
            }

        %>
        </tbody>
    </table>
<div class="container" style="text-align: center" >
    <input type="submit" class="btn btn-success" value="Dismiss">
    <input type="button" class="btn btn-info" value="Select all" id="selectAll" >
</div>

</div>
</form>
</body>
</html>
