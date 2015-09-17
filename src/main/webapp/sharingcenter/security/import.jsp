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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.model.InfrastructureDataObject"%>
<%@page import="org.oscarehr.sharingcenter.dao.InfrastructureDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%
    boolean validId = true;
    boolean found = false;
    InfrastructureDataObject infrastructure = new InfrastructureDataObject();
    if (request.getParameter("id") != null) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            InfrastructureDao infDao = SpringUtils.getBean(InfrastructureDao.class);
            infrastructure = infDao.getInfrastructure(id);
            if (infrastructure != null) {
                found = true;
            }
        } catch (NumberFormatException e) {
            validId = false;
        }
    }
%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="${ctx}/assets/ico/favicon.png">

        <title>Sharing Center - Infrastructure Details</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx}/js/FileSaver.js"></script>
        <script src="${ctx}/js/jqBootstrapValidation-1.3.7.min.js"></script>

        <script>

            function toggleReadOnly() {
                $("#addInfrastructuresPanel :input").each(function() {
                    $(this).attr("readonly", !$(this).attr("readonly"));
                });
            }

            function clearErrors() {
                $("#addInfrastructuresPanel .help-block").each(function() {
                    $(this).html("");
                });
            }

            function displayDeleteConfirmation() {
                var answer = confirm("Are you sure you want to delete \n this infrastructure?");
                if (answer) {
                    $("#action").val("delete");
                    $("#formsubmit").submit();
                }
            }

            function fillInDetails() {
            <%if (validId == false || found == false) {%>
                $(".panel").removeClass("panel-info").addClass("panel-danger");
                $(".panel-body").html("No infrastructure loaded (not found or id malformed)");
                $("#edit-button").attr("disabled", true);
                $("#save-button").attr("disabled", true);
                $("modal-container").remove();
                $("chainImportPanel").remove();
            <%} else {%>
                $("#form_alias").val("<%=infrastructure.getAlias()%>");
                $("#form_commonName").val("<%=infrastructure.getCommonName()%>");
                $("#form_organizationalUnit").val("<%=infrastructure.getOrganizationalUnit()%>");
                $("#form_organization").val("<%=infrastructure.getOrganization()%>");
                $("#form_locality").val("<%=infrastructure.getLocality()%>");
                $("#form_state").val("<%=infrastructure.getState()%>");
                $("#form_country").val("<%=infrastructure.getCountry()%>");
                $(".id").each(function() {
                    $(this).val("<%=infrastructure.getId()%>")
                });
            <%}%>
            }

            function toggleEdit() {
                if ($("#edit-button").html() == "Cancel") {
                    clearErrors();
                    fillInDetails();
                    toggleReadOnly();
                    $("#edit-button").html("Edit");
                    $("#edit-button").removeClass("btn-warning")
                            .addClass("btn-primary");
                } else {
                    toggleReadOnly();
                    $("#edit-button").html("Cancel");
                    $("#edit-button").removeClass("btn-primary")
                            .addClass("btn-warning");
                }
            }

            function ajaxGenerateCsrRequest() {

                var csrRequestObj = new Object();
                csrRequestObj.id = $(".id:first").val();
                csrRequestObj.action = "csr";

                $.ajax({
                    type: "GET",
                    url: "${ctx}/sharingcenter/security/Infrastructure.do",
                    dataType: "text",
                    data: csrRequestObj,
                    success: function(data) {
                        $("#csr_pre").text(data);
                        $("#download-button").click(function(event) {
                            var blob = new Blob([data], {
                                type: "text/plain;charset=utf-8"
                            });
                            saveAs(blob, "CSR.txt");
                        });
                    },
                    error: function(data) {
                        alert("There was an error!" + JSON.stringify(data));
                    }
                });

            }

            $(document).ready(function() {

                $("#importButton").submit(function() {
                    var answer = confirm("Are you sure you want to import \n this certificate?");
                    if (answer) {
                        $("#certform").append('<input type="hidden" name="id" value="<%=infrastructure.getId()%>" />');
                        $("#certform").submit();
                    }
                });

                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");
                fillInDetails();
                $("#edit-button").on("click", function() {
                    toggleEdit();
                });

                $(function() {
                    //$("input,select,textarea").not... for multiple types of input elements
                    $("#addInfrastructuresPanel :input").not("[type=submit]")
                            .jqBootstrapValidation();
                });

            <%if (request.getParameter("update") != null) {%>
                alert("Successful update");
                url = window.location.href.split("&")[0];
                window.location = url;//removes the get parameter by reloading
            <%} else if (request.getParameter("import") != null) {%>
                alert("Successful certificate import");
                url = window.location.href.split("&")[0];
                window.location = url;//removes the get parameter by reloading
            <%} else if (request.getParameter("fail") != null) {%>
                alert("There was a problem importing the certificate into the keystore/truststore. Check server logs.");
                url = window.location.href.split("&")[0];
                window.location = url;//removes the get parameter by reloading
            <%}%>
            });
        </script>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
<script>
$(function() {
    $( document ).tooltip();
  });
</script>

    </head>

    <body>

        <!-- Wrap all page content here -->
        <div id="wrap">

            <!-- Begin page content -->
            <div class="container">

                <div id="navBar">
                    <a href="NavBar.jsp">click</a>
                </div>

                <div id="chainImportPanel" class="panel panel-info">
                    <div class="panel-heading">
                        <span class="panel-title">Certificate Chain Import</span>
                    </div>
                    <div class="panel-body">

                        <form id="certform" action="Infrastructure.do" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">PKCS7 Certificate:</span> <input
                                        id="file" type="file" style="display: none" name="file"/>
                                        <span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
        
                                        <input
                                        id="photoCover" class="form-control" type="text" />
                                    <input class="id" name="id" type="hidden" value="<%=infrastructure.getId()%>" />
                                    <div class="input-group-btn btn-group">
                                        <span class="input-group-btn"> <a
                                                class="btn btn-default"
                                                onclick="$('input[id=file]').click();">Browse</a>
                                        </span>
                                        <span class="input-group-btn">
                                            <button id="importButton" type="submit" class="btn btn-primary">Import</button>
                                        </span>
                                    </div>
                                    <script type="text/javascript">
                                        $('input[id=file]').change(function() {
                                            $('#photoCover').val($(this).val());
                                        });
                                    </script>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>


                <div id="addInfrastructuresPanel" class="panel panel-info">
                    <div class="panel-heading">
                        <span class="panel-title">Infrastructure Details</span>
                    </div>
                    <form id="formsubmit" action="Infrastructure.do" novalidate>
                        <div class="panel-body">
                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">Alias:</span> <input type="text"
                                                                                         class="form-control" name="form_alias" id="form_alias" readonly
                                                                                         required />
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">CN:</span> <input type="text"
                                                                                      class="form-control" name="form_commonName"
                                                                                      id="form_commonName" readonly required /> <span
                                                                                      class="input-group-addon">Common Name</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">OU:</span> <input type="text"
                                                                                      class="form-control" name="form_organizationalUnit"
                                                                                      id="form_organizationalUnit" readonly required /> <span
                                                                                      class="input-group-addon">Organizational Unit</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">O:</span> <input type="text"
                                                                                     class="form-control" name="form_organization"
                                                                                     id="form_organization" readonly required /> <span
                                                                                     class="input-group-addon">Organization</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">L:</span> <input type="text"
                                                                                     class="form-control" name="form_locality" id="form_locality"
                                                                                     readonly required /> <span class="input-group-addon">Locality</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">ST:</span> <input type="text"
                                                                                      class="form-control" name="form_state" id="form_state" readonly
                                                                                      required /> <span class="input-group-addon">State/Province</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br />

                            <div class="control-group">
                                <div class="input-group">
                                    <span class="input-group-addon">C:</span> <input type="text"
                                                                                     class="form-control" name="form_country" id="form_country"
                                                                                     readonly required /> <span class="input-group-addon">Country</span>
                                </div>
                                <p class="help-block"></p>
                            </div>
                            <br /> <input type="hidden" class="id" name="id" value="" /> <input
                                type="hidden" id="action" name="action" value="update" />

                            <button class="btn btn-primary pull-left" type="button"
                                    id="edit-button">Edit</button>
                            <button class="btn btn-primary pull-right" id="save-button">Save</button>

                        </div>
                    </form>
                </div>


                <div id="modal-container">

                    <!--  Modal -->
                    <div id="myModal" class="modal fade" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">Certificate Signing Request (CSR)</h4>
                                </div>
                                <div class="modal-body">
                                    <div class="alert alert-info">
                                        <p>Here is your Certificate Signing Request. Copy this to
                                            your clipboard.</p>
                                        <textarea id="csr_pre" class="form-control" rows="30"
                                                  style="resize: none;" readonly></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-default pull-left" id="download-button">
                                        <span class="glyphicon glyphicon-download-alt"></span> Download
                                    </button>
                                    <button type="button" class="btn btn-default"
                                            data-dismiss="modal">Close</button>
                                </div>
                            </div>
                            <!--  /.modal-content -->
                        </div>
                        <!--  /.modal-dialog -->
                    </div>
                    <!-- /.modal -->

                    <button data-target="#myModal" class="btn btn-primary"
                            data-toggle="modal" onclick="ajaxGenerateCsrRequest()">Generate
                        CSR</button>
                    <button onclick="displayDeleteConfirmation()"
                            class="btn btn-danger pull-right">Remove Security
                        Configuration</button>

                </div>
                <br />
                <br />
            </div>
    </body>
</html>
