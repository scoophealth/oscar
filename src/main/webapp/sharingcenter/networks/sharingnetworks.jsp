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
<%@page import="org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>

<%

    // get all installed affinity domains
    AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
    List<AffinityDomainDataObject> affinityDomains = affDao.getAllAffinityDomains();

    // Patient information
    Demographic demographic = null;
    if (request.getParameter("demographic_no") != null) {
        int demographicId = Integer.parseInt(request.getParameter("demographic_no"));
        DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        demographic = demoDao.getDemographicById(demographicId);
    }

    //sharing enabled?
    PatientSharingNetworkDao patientSharingNetworkDao = SpringUtils.getBean(PatientSharingNetworkDao.class);

%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="${ctx}/assets/ico/favicon.png">

        <title>Patient Sharing Network</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
    </head>

    <body>

        <!-- Wrap all page content here -->
        <div class="container" width="500px">

            <div class="page-header">
                <h1><a href="#">Patient Sharing Networks</a></h1>
            </div>

            <% if (demographic != null) {%>

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

            <% } %>


            <div id="affinityDomainsPanel" class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Affinity Domains (Sharing Networks)</h3>
                </div>
                <div class="panel-body">
                    <p>The following is a list of healthcare networks (Affinity Domains) that the patient is able to share information with.</p>
                    <ul id="affinityDomainList" class="list-group">

                        <%
                            for (AffinityDomainDataObject domain : affinityDomains) {
                                boolean sharingEnabled = patientSharingNetworkDao.isSharingEnabled(domain.getId(), demographic.getDemographicNo());
                        %>    
                        <li class="list-group-item"><span class="glyphicon <%= (sharingEnabled) ? "glyphicon-ok" : "glyphicon-remove"%>"></span> <a href="domain.jsp?demographic_no=<%= demographic.getDemographicNo()%>&id=<%=domain.getId()%>"><%=domain.getName()%></a></li>

                        <% }%>

                    </ul>
                </div>
            </div>
        </div>
    </body>
</html>
