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
<%@page import="org.oscarehr.sharingcenter.model.PatientSharingNetworkDataObject"%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao"%>
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

    if (request.getParameter("id") != null) {
        network = affDao.getAffinityDomain(Integer.parseInt(request.getParameter("id")));
    }

    // sharing enabled?
    PatientSharingNetworkDao patientSharingNetworkDao = SpringUtils.getBean(PatientSharingNetworkDao.class);
    boolean sharingEnabled = patientSharingNetworkDao.isSharingEnabled(network.getId(), demographic.getDemographicNo());

    // sharing key accepted?
    boolean useSharingKey = SharingCenterUtil.isSharingKeyAccepted(AffinityDomainDataObject.createIheAffinityDomainConfiguration(network));
    PatientSharingNetworkDataObject sharingDataObject = patientSharingNetworkDao.findPatientSharingNetworkDataObject(network.getId(), demographic.getDemographicNo());
    String sharingKey = "";
    if (sharingDataObject != null) {
        sharingKey = sharingDataObject.getSharingKey();
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

        <title>Patient Sharing Network</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>

            $(document).ready(function() {

                // click events
                // register button
                $("#enableViaRegisterButton").on("click", function() {
                    $("#sharing_action").val("register-patient");
                    $("#sharedomainform").submit();
                });

                // update button
                $("#updatePatientButton").on("click", function() {
                    $("#sharing_action").val("update-patient");
                    $("#sharedomainform").submit();
                });

                // disable sharing button
                $("#disableSharingButton").on("click", function() {
                    $("#sharing_action").val("disable-sharing");
                    $("#sharedomainform").submit();
                });

                // enable sharing (sharing key)
                $("#enableViaSharingKeyButton").on("click", function() {
                    // sharing key supplied?
                    if ($("#sharingKey").val() != "") {
                        // all is good
                        $("#sharing_action").val("register-sharing-key");
                        $("#sharedomainform").submit();

                    } else {
                        alert("You must type your Sharing Key to enable sharing");
                        return false;
                    }
                });

                // update sharing (sharing key)
                $("#updateSharingKeyButton").on("click", function() {
                    // sharing key supplied?
                    if ($("#sharingKey").val() != "") {
                        // all is good
                        $("#sharing_action").val("update-sharing-key");
                        $("#sharedomainform").submit();

                    } else {
                        alert("You must type your Sharing Key to enable sharing");
                        return false;
                    }
                });

            });

        </script>

    </head>

    <body>

        <!-- Wrap all page content here -->
        <div class="container" width="500px">

            <div class="page-header">
                <h1><a href="#">Patient Sharing Networks</a></h1>
            </div>

            <% if (demographic != null || network != null) {%>

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

            <div id="domainPanel" class="panel panel-default">
                <div class="panel-body">
                    <h3 class="panel-title">Domain: <span class="label label-info"><%= network.getName()%></span></h3>
                </div>
            </div>

            <div id="statusPanel" class="panel panel-default">
                <div class="panel-body">
                    <h3 class="panel-title">Status: <span class="label <%= (sharingEnabled) ? "label-success" : "label-danger"%>">Sharing is <%= (sharingEnabled) ? "enabled" : "disabled"%></span></h3>
                </div>
            </div>

            <form id="sharedomainform" action="ShareDomain.do" method="POST" role="form">
                <input id="network_id" name="network_id" type="hidden" value="<%= network.getId()%>" />
                <input id="demographic_no" name="demographic_no" type="hidden" value="<%= demographic.getDemographicNo()%>" />
                <input id="sharing_action" name="sharing_action" type="hidden" value="" />
                <input id="referring_page" name="referring_page" type="hidden" value="<%= request.getHeader("Referer")%>" />

                <% if (useSharingKey) { // display sharing key functionality (XDR) %>

                <div id="sharingInfoPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title"><%= (sharingEnabled) ? "Sharing Key" : String.format("Share With %s", network.getName())%></h3>
                    </div>
                    <div class="panel-body">
                            <p><%= (sharingEnabled) ? "The following number is your OSCAR sharing key. Provide this number to your physician to enable sharing of your OSCAR information."
                                    : String.format("To share patient information, enter your %s sharing key below (if available) and click 'Enable Sharing'", network.getName())%></p>

                        <div class="input-group">
                            <span class="input-group-addon">Sharing Key:</span>
                            <input id="sharingKey" name="sharingKey" type="text" class="form-control" value="<%= (sharingKey != null) ? sharingKey : ""%>">
                            <span class="input-group-btn">
                                <% if (sharingEnabled) { %>
                                <button id="updateSharingKeyButton" type="submit" onClick="return false;" class="btn btn-primary">Update</button>
                                <% } else { %>
                                <button id="enableViaSharingKeyButton" type="submit" onClick="return false;" class="btn btn-primary">Enable Sharing</button>
                                <% } %>
                            </span>
                        </div>

                    </div>
                </div>

                <% } else { // display registration functionality %>

                <% if (sharingEnabled == false) { %>
                <div id="registrationPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Register Patient</h3>
                    </div>
                    <div class="panel-body">
                        <p>Click the 'Enable Sharing' button below to register the patient in the network.</p>
                        <div class="input-group">
                            <button id="enableViaRegisterButton" type="submit" onClick="return false;" class="btn btn-primary pull-right">Enable Sharing</button>
                        </div>
                    </div>
                </div>
                <% } else { %>
                <div id="registrationPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Update Patient Demographic</h3>
                    </div>
                    <div class="panel-body">
                        <p>Click the 'Update Demographic' button below to update the patient's information in the network.</p>
                        <div class="input-group">
                            <button id="updatePatientButton" type="submit" onClick="return false;" class="btn btn-primary pull-right">Update Demographic</button>
                        </div>
                    </div>
                </div>
                <% } %>

                <% } %>

                <% if (sharingEnabled) { %>

                <a href="#"><span class="glyphicon glyphicon-print"></span> Print Page</a><button id="disableSharingButton" type="submit" onClick="return false;" class="btn btn-danger pull-right">Disable Sharing</button>

                <% } %>

            </form>

            <% } else { // demographic == null or network == null  %>

            <div class="panel panel-default">
                <div class="panel-body">
                    <h3 class="panel-title">Error: <span class="label label-danger">Unable to locate patient or network</span></h3>
                </div>
            </div>

            <% } %>

        </div>
        <br />

    </body>
</html>
