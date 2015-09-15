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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%
    AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
    List<AffinityDomainDataObject> affList = affDao.getAllAffinityDomains();
%>

<html>
    <head>
        <meta charset="utf-8">
        <title>Oscar Sharing Center</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>
            $(document).ready(function() {
                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");
            <%if (request.getParameter("deleteSuccess") != null) {%>
                alert("Successful deletion");
                url = window.location.href.split("?")[0];
                window.location = url;//removes the get parameter by reloading
            <%} else if (request.getParameter("deleteFail") != null) {%>
                alert("Failed deletion");
                url = window.location.href.split("?")[0];
                window.location = url;//removes the get parameter by reloading
            <%}%>
            });

            function confirmUpload() {
                var answer = confirm("Are you sure you want to upload \n this Affinity Domain Configuration?");
                if (answer) {
                    $("#configform").submit();
                }
            }
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
        <div class="container" width="500px">

            <div id="navBar"></div>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">Upload Configuration</h2>
                </div>

                <form id="configform" action="${ctx}/sharingcenter/affinitydomain/AffinityDomainParser.do"
                      method="POST" enctype="multipart/form-data" role="form">
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon">Upload Configuration:</span> <input
                                id="file" name="file" type="file" style="display: none">
                                <span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
        
                            <input id="photoCover" class="form-control" type="text" />

                            <div class="input-group-btn btn-group">
                                <span class="input-group-btn"> <a class="btn btn-default"
                                                                  onclick="$('input[id=file]').click();">Browse</a>
                                </span> <span class="input-group-btn"> <a class="btn btn-primary"
                                                                          onclick="confirmUpload()">Upload</a>
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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">Affinity Domains</h2>
                </div>
                <div class="panel-body">

                    <table class="table">
                        <p>The following is a list of healthcare networks (affinity
                            domains) registered in Oscar.</p>
                        <tr>
                            <th>OID</th>
                            <th>Name</th>
                        </tr>

                        <% for (AffinityDomainDataObject domain : affList) {%>

                        <tr>
                            <td><%=domain.getOid()%></td>
                            <td><a href="${ctx}/sharingcenter/affinitydomain/network.jsp?id=<%=domain.getId()%>"><%=domain.getName()%></a></td>
                        </tr>

                        <% }%>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
<!--  
<script>
registerFormSubmit('configform', 'dynamic-content');
</script>-->
</html>