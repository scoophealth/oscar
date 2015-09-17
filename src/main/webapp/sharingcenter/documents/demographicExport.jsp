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
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.common.model.ProviderData"%>
<%@page import="org.marc.shic.core.FolderMetaData"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.PatientSharingNetworkDataObject"%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.oscarehr.common.model.Document"%>
<%@page import="oscar.oscarDemographic.pageUtil.DemographicExportAction4"%>
<%@page import="org.marc.shic.core.configuration.IheConfiguration"%>
<%@page import="org.marc.shic.core.configuration.IheAffinityDomainConfiguration"%>
<%@page import="org.oscarehr.sharingcenter.DocumentType"%>

<%
    Demographic demographic = null;

    if (request.getParameter("demographic_no") != null) {
        int demographicId = Integer.parseInt(request.getParameter("demographic_no"));
        DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        demographic = demoDao.getDemographicById(demographicId);
    }

    AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
    List<AffinityDomainDataObject> affinityDomains = affDao.getAllAffinityDomains();
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

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
              <script src="../../assets/js/html5shiv.js"></script>
              <script src="../../assets/js/respond.min.js"></script>
            <![endif]-->
    </head>

    <body>
        <!-- Begin page content -->
        <div class="container">

            <form id="demographicexportform" action="DemographicExport.do" method="POST">
                <input type="hidden" name="demographic_no" value="<%=demographic.getDemographicNo()%>" />

                <div class="page-header">
                    <h3>Document Export</h3>
                </div>

                <div class="well">
                    <table class="table">
                        <tr>
                            <th>Patient #</th>
                            <th>Name</th>
                            <th>Date of Birth</th>
                        </tr>
                        <tr>
                            <td><%=demographic.getDemographicNo()%></td>
                            <td><%=demographic.getDisplayName()%></td>
                            <td><%=String.format("%s / %s / %s", demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth())%></td>
                        </tr>
                    </table>
                </div>

                <div id="documentExportPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Document Export</h3>
                    </div>
                    <!-- panel heading -->

                    <div class="panel-body">

                        <div class="input-group">
                            <span class="input-group-addon"><span class="glyphicon glyphicon-folder-open"></span> Document Type:</span>
                            <select id="documentType" name="documentType" class="form-control">
                                <option value="<%=DocumentType.XPHR.name()%>"><%=DocumentType.XPHR.getValue()%></option>
                                <option value="<%=DocumentType.NEXJ.name()%>"><%=DocumentType.NEXJ.getValue()%></option>
                            </select>
                        </div><!-- /input-group -->

                        <br />

                        <div class="input-group">
                            <span class="input-group-addon"><span class="glyphicon glyphicon-folder-open"></span> Affinity Domain:</span>
                            <select id="affinityDomain" name="affinityDomain" class="form-control">
                                <% for (AffinityDomainDataObject domain : affinityDomains) { %>
                                    <option value="<%=domain.getId()%>"><%=domain.getName()%></option>
                                <% } %>
                            </select>
                        </div><!-- /input-group -->

                        <br />
                        <br />
                        <button id="sendButton" name="sendButton" class="btn btn-success pull-right" type="submit">Send</button>
                    </div>
                    <!-- panel body -->
                </div>
            </form>
        </div>
        <!-- container -->
    </body>
</html>
