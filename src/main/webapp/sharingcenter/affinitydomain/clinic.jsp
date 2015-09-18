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
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.model.ClinicInfoDataObject"%>
<%@page import="org.oscarehr.sharingcenter.dao.ClinicInfoDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%
    boolean found = false;

    ClinicInfoDao clinicDao = SpringUtils.getBean(ClinicInfoDao.class);
    ClinicInfoDataObject clinicInfo = clinicDao.getClinic();

    if (clinicInfo.getId() != null) {
        found = true;
    }
%>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8">
        <title>Oscar Sharing Center - Clinic Info</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <script src="${ctx}/js/jqBootstrapValidation-1.3.7.min.js"></script>


        <script>

            $(document).ready(function() {


                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");

                $(function() {
                    //$("input,select,textarea").not... for multiple types of input elements
                    $("input").not("[type=submit]").jqBootstrapValidation();
                });

            <%if (found) {%>
                $("#form_name").val("<%=clinicInfo.getName()%>");
                $("#form_appname").val("<%=clinicInfo.getLocalAppName()%>");
                $("#form_facilityname").val("<%=clinicInfo.getFacilityName()%>");
                $("#form_universal").val("<%=clinicInfo.getUniversalId()%>");
                $("#form_namespace").val("<%=clinicInfo.getNamespaceId()%>");
                $("#form_oid").val("<%=clinicInfo.getOid()%>");
                $("#form_id").val("<%=clinicInfo.getId()%>");
                $("#form_source").val("<%=clinicInfo.getSourceId()%>");
                $("#submit-button").html("Update");
            <%}%>

            <%if (request.getParameter("success") != null) {%>
                alert("Successful submission");
                window.location = window.location.pathname;//removes the get parameter by reloading
            <%} else if (request.getParameter("update") != null) {%>
                alert("Successful update");
                window.location = window.location.pathname;//removes the get parameter by reloading
            <%}%>
            });
        </script>

    </head>
    <body>
        <form action="${ctx}/sharingcenter/affinitydomain/ClinicInfo.do" method="POST" novalidate>

            <div class="container" width="500px">

                <div id="navBar"></div>

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h2 class="panel-title">Clinic Info</h2>
                    </div>

                    <div class="panel-body" id="clinicInfoForm">
                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Clinic Name:</span>
                                <input type="text" class="form-control" id="form_name" name="form_name" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Organization OID:</span>
                                <input type="text" class="form-control" id="form_oid" name="form_oid" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>

                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h2 class="panel-title">HL7v2 Info</h2>
                    </div>
                    <div class="panel-body">

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Application Name:</span>
                                <input type="text" class="form-control" id="form_appname" name="form_appname" required data-validation-required-message="This field is
                                       required" />
                            </div>
                            <p class="help-block"></p>
                        </div>
                        <br />
                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Facility Name:</span>
                                <input type="text" class="form-control" id="form_facilityname" name="form_facilityname" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>
                        <br />
                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Universal ID:</span>
                                <input type="text" class="form-control" id="form_universal" name="form_universal" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>
                        <br />
                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Namespace ID:</span>
                                <input type="text" class="form-control" id="form_namespace" name="form_namespace" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>

                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h2 class="panel-title">XDS Info</h2>
                    </div>
                    <div class="panel-body">

                        <div class="control-group">
                            <div class="input-group">
                                <span class="input-group-addon">Source ID:</span>
                                <input type="text" class="form-control" id="form_source" name="form_source" required data-validation-required-message="This field is required" />
                            </div>
                            <p class="help-block"></p>
                        </div>

                    </div>
                </div>

                <br />
                <input type="hidden" id="form_id" name="form_id" value="" />
                <button class="btn btn-primary pull-right" id="submit-button" type="submit">Save</button>

            </div>
        </form>
    </body>
</html>