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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.util.LocaleUtils" %>
<%@page import="java.util.Locale" %>
<%@ page import="org.oscarehr.ws.rest.util.QuestimedUtil" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
    <%authed=false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
        if(!authed) {
                return;
        }
%>
<html:html locale="true">
    <head>
        <title>Questimed</title>
    </head>
    <script>
        function popupPagePost(varpage, target) {
            var p = varpage.split('?');
            var action = p[0];
            var params = p[1].split('&');
            var form = document.createElement("form");
            form.action = action;
            form.method = "post";
            form.target = target || "_self";
            for (var i in params) {
                var tmp = params[i].split('=');
                var key = tmp[0], value = tmp[1];
                var input = document.createElement("input");
                input.name = key;
                input.value = value;
                form.appendChild(input);
            }

            form.style.display = 'none';
            document.body.appendChild(form);
            form.submit();
        }
    </script>
    <%
            Locale locale = request.getLocale(); 
            LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
            String demographicNo= request.getParameter("demographic_no");
            String errorMsg="";
            String url;
            url=QuestimedUtil.getLaunchURL(loggedInInfo,demographicNo);
            if(url==null) {
                errorMsg = LocaleUtils.getMessage(locale, "questimed.connectionerror");
            }
            else if(url.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/questimed/createaccount.jsp?demographic_no="+demographicNo);
            }
            else {
    %>
    <script>
        popupPagePost("<%=url%>");
    </script>
    <%}%>    
     <body>
        <%=errorMsg%>
    </body>
</html:html>
