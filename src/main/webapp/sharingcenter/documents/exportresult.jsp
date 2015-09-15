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
<security:oscarSec roleName="<%=roleName2$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>

<%

// Patient information
    Demographic demographic = null;
    if (request.getParameter("demographic_no") != null) {
        int demographicId = Integer.parseInt(request.getParameter("demographic_no"));
        DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        demographic = demoDao.getDemographicById(demographicId);
    }

// Network
    AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
    AffinityDomainDataObject network = null;

    if (request.getParameter("domain") != null) {
        network = affDao.getAffinityDomain(Integer.parseInt(request.getParameter("domain")));
    }

    String bppcStatus = null;
    if (request.getParameter("bppc") != null) {
        bppcStatus = request.getParameter("bppc");
    }

    String submissionStatus = null;
    if (request.getParameter("submission") != null) {
        submissionStatus = request.getParameter("submission");
    }

%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="${ctx}/assets/ico/favicon.png">

        <title>Document Export</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>

            $(document).ready(function() {

            });

        </script>

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="../../assets/js/html5shiv.js"></script>
          <script src="../../assets/js/respond.min.js"></script>
        <![endif]-->

    </head>

    <body>

        <!-- Begin page content -->
        <div class="container">

            <div class="page-header">
                <h3>Document Export Result</h3>
            </div>

            <div class="well">
                <table class="table">
                    <tr>
                        <th>Patient #</th>
                        <th>Name</th>
                        <th>Date of Birth</th>
                    </tr>
                    <tr>
                        <td><%= demographic.getDemographicNo()%></td>
                        <td><%= demographic.getDisplayName()%></td>
                        <td><%= String.format("%s / %s / %s", demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth())%></td>
                    </tr>
                </table>
            </div>
            <% if (bppcStatus != null) {%>
            <div class="panel panel-default">
                <div class="panel-body">
                    <h4><span class="label label-<%= (bppcStatus.equals("1")) ? "success" : "danger"%>"><span class="glyphicon glyphicon-eye-close"></span> <%= (bppcStatus.equals("1")) ? "Consent Policy Acknowledgement was successfully sent." : "An error occured while sending Consent Policy Acknowledgement"%></span></h4>
                </div>
            </div>
            <% } %>

            <% if (submissionStatus != null) {%>
            <div class="panel panel-default">
                <div class="panel-body">
                    <h4><span class="label label-<%= (submissionStatus.equals("1")) ? "success" : "danger"%>"><span class="glyphicon glyphicon-file"></span> <%= (submissionStatus.equals("1")) ? "Document(s) were successfully exported." : "An error occurred while exporting document(s)."%></span></h4>
                </div>
            </div>
            <% }%>

        </div>

    </body>
</html>
