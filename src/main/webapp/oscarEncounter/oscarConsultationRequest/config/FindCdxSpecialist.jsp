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

<%@ taglib prefix="security" uri="/oscarSecuritytag" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="w" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search For CDX Capable Providers</title>
</head>
<body>
Enter some characters from the providers first, last or middle name and submit.<br>
<sl>If no characters are provided all CDX capable providers with an non-empty last name will be returned.</sl><br>
<b>That is very slow!!!</b>:
<form action="../../oscarConsultationRequest/config/AddCdxSpecialist.jsp">
    <label>
        First/Last name:
        <input type="text" name="searchstring">
    </label>
    <input type="submit" value="Find">
</form>

</body>
</html>
