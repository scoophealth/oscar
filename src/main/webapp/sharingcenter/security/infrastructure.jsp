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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.model.InfrastructureDataObject" %>
<%@page import="org.oscarehr.sharingcenter.dao.InfrastructureDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%

    InfrastructureDao infDao = SpringUtils.getBean(InfrastructureDao.class);
    List<InfrastructureDataObject> infrastructureList = infDao.getAllInfrastructures();

%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="${ctx}/assets/ico/favicon.png">

        <title>Sharing Center - Security Infrastructures</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>
            $(document).ready(function() {

                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");

            <%                            if (request.getParameter("success") != null) {
            %>
                alert("Successful submission");
                window.location = window.location.pathname; //removes the get parameter by reloading
            <%
            } else if (request.getParameter("update") != null) {
            %>
                alert("Successful update");
                window.location = window.location.pathname; //removes the get parameter by reloading
            <%
            } else if (request.getParameter("delete") != null) {
            %>
                alert("Successful deletion");
                window.location = window.location.pathname;//removes the get parameter by reloading
            <%
                }
            %>

            });
        </script>

    </head>

    <body>

        <!-- Wrap all page content here -->
        <div id="wrap">

            <!-- Begin page content -->
            <div class="container">

                <div id="navBar">

                </div>

                <div id="infrastructuresPanel" class="panel panel-info">
                    <div class="panel-heading">
                        <span class="panel-title">Infrastuctures</span>
                    </div>
                    <div class="panel-body">
                        The following is a list of aliases representing health care networks (affinity domains) that configured their SSL Security.

                        <table class="table">
                            <tbody>
                                <tr>
                                    <th>#</th>
                                    <th>Alias</th>
                                </tr>

                                <%
                                    for (InfrastructureDataObject infr : infrastructureList) {
                                %>
                                <tr>
                                    <td><%=infr.getId()%></td>
                                    <td><a href="${ctx}/sharingcenter/security/import.jsp?id=<%=infr.getId()%>"><%=infr.getAlias()%></a></td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                        <a href="${ctx}/sharingcenter/security/new.jsp" class="btn btn-primary">Create</a>
                    </div>
                </div>
            </div> 
        </div>
    </body>
</html>
