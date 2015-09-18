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
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="${ctx}/assets/ico/favicon.png">

        <title>Sharing Center - Create New Infrastructure</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <script src="${ctx}/js/jqBootstrapValidation-1.3.7.min.js"></script>
        
        <script>

            $(document).ready(function() {

                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");

                $(function() {
                    $("#addInfrastructuresPanel :input").not("[type=submit]")
                            .jqBootstrapValidation();
                });

            <%if (request.getParameter("exists") != null) {%>
                alert("Alias already exists");
                url = window.location.href.split("?")[0];
                window.location = url;//removes the get parameter by reloading but keeps the id
            <%}%>


            });


        </script>


    </head>

    <body>

        <!-- Wrap all page content here -->
        <div id="wrap">

            <!-- Begin page content -->
            <div class="container">

                <div id="navBar">
                    <a href="NavBar.jsp" >click</a>
                </div>  


                <div id="addInfrastructuresPanel" class="panel panel-info">
                    <div class="panel-heading">
                        <span class="panel-title">Add New Infrastructure</span>
                    </div>
                    <div class="panel-body">
                        <form action="Infrastructure.do" method="GET" role="form" novalidate>
                            <div class="form-group">
                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">Alias:</span> <input
                                            type="text" class="form-control"
                                            placeholder="Alias for Infrastructure" name="form_alias"
                                            required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">CN:</span> <input type="text"
                                                                                          class="form-control" placeholder="Common Name"
                                                                                          name="form_commonName" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">OU:</span> <input type="text"
                                                                                          class="form-control" placeholder="Organizational Unit"
                                                                                          name="form_organizationalUnit" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">O:</span> <input type="text"
                                                                                         class="form-control" placeholder="Organization"
                                                                                         name="form_organization" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">L:</span> <input type="text"
                                                                                         class="form-control" placeholder="Locality/City"
                                                                                         name="form_locality" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">ST:</span> <input type="text"
                                                                                          class="form-control" placeholder="State/Province"
                                                                                          name="form_state" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <div class="control-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">C:</span> <input type="text"
                                                                                         class="form-control" placeholder="Country (CA)"
                                                                                         name="form_country" required />
                                    </div>
                                    <p class="help-block"></p>
                                </div>
                                <br />

                                <button type="submit" class="btn btn-primary pull-right">
                                    Create</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div> 
        </div>
    </body>
</html>
