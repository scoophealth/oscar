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
<%@page import="org.oscarehr.sharingcenter.DocumentType"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.common.model.ProviderData"%>
<%@page import="org.marc.shic.core.FolderMetaData"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.PatientSharingNetworkDataObject"%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="org.oscarehr.sharingcenter.dao.PatientSharingNetworkDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.oscarehr.common.model.Document"%>

<%@ page import="oscar.dms.EDocUtil"%>

<%
    String user_no = (String) session.getAttribute("user");

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

    List<FolderMetaData> folders = null;
    boolean useXDR = false;
    if (demographic != null && network != null) {
        // sharing enabled?
        PatientSharingNetworkDao patientSharingNetworkDao = SpringUtils.getBean(PatientSharingNetworkDao.class);
        boolean sharingEnabled = patientSharingNetworkDao.isSharingEnabled(network.getId(), demographic.getDemographicNo());

        // redirect to sharing networks page if the patient is not sharing with this network
        if (!sharingEnabled) {
            response.sendRedirect(String.format(request.getContextPath() + "/sharingcenter/networks/domain.jsp?demographic_no=%s&id=%s", demographic.getDemographicNo(), network.getId()));
        }

        // XDR accepted?
        useXDR = SharingCenterUtil.isXDREnabled(AffinityDomainDataObject.createIheAffinityDomainConfiguration(network));
        PatientSharingNetworkDataObject sharingDataObject = patientSharingNetworkDao.findPatientSharingNetworkDataObject(network.getId(), demographic.getDemographicNo());
        String sharingKey = "";
        if (sharingDataObject != null) {
            sharingKey = sharingDataObject.getSharingKey();
        }

        // get folder list for patient
        if (useXDR) {
            folders = new ArrayList<FolderMetaData>();
        } else {
            folders = SharingCenterUtil.findFolders(demographic.getDemographicNo(), network);
        }
    }

    // document submission type
    String docSubType = null;
    if (!request.getParameter("type").isEmpty()) {
        docSubType = request.getParameter("type");
    }

    // documents
    String[] documentsArray = {};
    if (request.getParameterValues("documents").length > 0) {
        documentsArray = request.getParameterValues("documents");
    }

    // logged in user
    String providerId = (String) session.getAttribute("user");
    ProviderData provider = SharingCenterUtil.retrieveProvider(Integer.parseInt(providerId));

    // authenticator (ie. guardian)
    List<Demographic> decisionMakers = SharingCenterUtil.getRelationships(demographic.getDemographicNo());

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
        <link rel="stylesheet" href="${ctx}/css/bootstrap-multiselect.css" type="text/css"/>

        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx}/js/bootstrap-multiselect.js"></script>

        <script>

            var selectedPolicies = [];

            var popupWidth = 640;
            var popupHeight = 360;
            var popupLeft = (screen.width / 2) - (popupWidth / 2);
            var popupTop = (screen.height / 2) - (popupHeight / 2);

            $(document).ready(function() {

                // initialize the multiselect plugin
                $(".multiselect").multiselect();

                // click events
                // add folder link
                $("#newFolderLink").on("click", function() {
                    $("#newFolderModal").modal();
                });

                // add folder button
                $("#addFolderButton").on("click", function() {
                    if ($("#newFolderTitle").val() != "") {
                        // remove old unsaved folder
                        $("#folderSelect option[value='new']").each(function() {
                            $(this).remove();
                        });
                        // create
                        $("#folderSelect").append(
                                $("<option></option>").attr("value", "new").text($("#newFolderTitle").val())
                                );
                        // select new folder
                        $("#folderSelect").val("new");
                        // hide modal
                        $("#newFolderModal").modal('hide');
                    }
                });

                // send button
                $("#sendButton").on("click", function() {
                    // make sure the user selected a policy
                    if ($("#policySelect option:selected").size() == 0) {
                        alert("You must select at least 1 consent policy to continue.");
                        return false;
                    }

                    // clear previous policies
                    selectedPolicies = [];
                    $("#policyViewAndConfirmation").empty();

                    // get more details about the selected policies
                    $("#policySelect option:selected").each(function() {
                        // push all selected policies to a buffer
                        selectedPolicies.push($(this).attr("value"));
                    });

                    // create the policy html
                    $("#consentGiven").attr("checked", "checked");
                    $.each(selectedPolicies, function(index, value) {
                        // use ajax to decide whether to automatically check the consentGiven checkbox
                        var consentData = {"domain": $("#network_id").val(), "patient": $("#demographic_no").val(), "policy": value};

                        $.ajax({
                            type: "POST",
                            url: "../affinitydomain/ConsentChecker.do",
                            dataType: "text",
                            data: consentData,
                            success: function(data) {
                                // grab the policy info and create the dynamic policies section in the consent confirmation modal
                                var policy = jQuery.parseJSON(data);

                                var policyLink = "<a href=\"#\" onclick=\"window.open('" + policy.url + "','View Policy: " + policy.name + "','width='+popupWidth+', height='+popupHeight+', top='+popupTop+', left='+popupLeft); return false;\">" + policy.name + "</a>";
                                $("#policyViewAndConfirmation").append('<span class="glyphicon glyphicon-eye-close"></span>');
                                $("#policyViewAndConfirmation").append('<strong class="text-primary"> ' + policyLink + '</strong>');
                                $("#policyViewAndConfirmation").append('<br />');

                                // make the user consent to the policy?
                                if (policy.consent == false) {
                                    $("#consentGiven").removeAttr('checked');
                                }
                            }
                        });

                    });

                    // always show the modal regardless of policies selected..
                    $("#confirmationModal").modal('show');
                });

                // confirm button
                $("#confirmButton").on("click", function() {
                    if ($("#consentGiven").is(':checked')) {
                        $("#documentexportform").submit();
                    } else {
                        alert("You must give consent to all consent/privacy policies to continue");
                        return false;
                    }
                });
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

            <form id="documentexportform" action="DocumentExport.do" method="POST" role="form">

                <div class="page-header">
                    <h3>Document Export</h3>
                </div>

                <% if (demographic != null && network != null && docSubType != null && documentsArray.length > 0) {%>

                <input id="network_id" name="network_id" type="hidden" value="<%= network.getId()%>" />
                <input id="demographic_no" name="demographic_no" type="hidden" value="<%= demographic.getDemographicNo()%>" />
                <input id="provider_no" name="provider_no" type="hidden" value="<%= providerId%>" />
                <input id="documentsType" name="documentsType" type="hidden" value="<%= docSubType%>" />
                <% for (int i = 0; i < documentsArray.length; i++) {%>
                <input type="hidden" name="documents" value="<%= documentsArray[i]%>" />
                <%}%>

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

                <div id="documentExportPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Document Export</h3>
                    </div><!-- panel heading -->

                    <div class="panel-body">
                        <p>You are about to share the following documents with the "<%= network.getName()%>" affinity domain.</p>
                        <table class="table">
                            <tbody>

                                <% for (int i = 0; i < documentsArray.length; i++) {

                                        if (docSubType.equalsIgnoreCase(DocumentType.NEXJ.name()) || docSubType.equalsIgnoreCase(DocumentType.XPHR.name())) {%>
                                <tr>
                                    <td><span class="glyphicon glyphicon-file"></span> Document:</td>
                                    <td><strong class="text-primary"><a href="#" onclick="window.open('DocumentPreview.do?doc_type=<%=docSubType%>&doc_no=<%=documentsArray[i]%>&prov_no=<%=user_no%>', 'View Document: <%= documentsArray[i]%>', 'width=' + popupWidth + ', scrollbars=yes, height=' + popupHeight + ', top=' + popupTop + ', left=' + popupLeft);
                                            return false;">CDA Document</a></strong></td>
                                </tr>
                                <%} else if (docSubType.equalsIgnoreCase("edocs")) {%>
                                <tr>
                                    <td><span class="glyphicon glyphicon-file"></span> Document:</td>
                                    <td><strong class="text-primary"><a href="#" onclick="window.open('DocumentPreview.do?doc_type=<%=docSubType%>&doc_no=<%=documentsArray[i]%>&prov_no=<%=user_no%>', 'View Document', 'width=' + popupWidth + ', scrollbars=yes, height=' + popupHeight + ', top=' + popupTop + ', left=' + popupLeft);
                                            return false;" ><%= SharingCenterUtil.retrieveEdoc(documentsArray[i]).getDocdesc()%></a></strong></td>
                                </tr>
                                <%} else if (docSubType.equalsIgnoreCase("eforms")) {%>
                                <tr>
                                    <td><span class="glyphicon glyphicon-file"></span> Document:</td>
                                    <td><strong class="text-primary"><a href="#" onclick="window.open('DocumentPreview.do?doc_type=<%=docSubType%>&doc_no=<%=documentsArray[i]%>&prov_no=<%=user_no%>', 'View Document', 'width=' + popupWidth + ', scrollbars=yes, height=' + popupHeight + ', top=' + popupTop + ', left=' + popupLeft);
                                            return false;" ><%= SharingCenterUtil.retrieveEform(documentsArray[i]).getFormName()%></a></strong></td>
                                </tr>
                                <%} else if (docSubType.equalsIgnoreCase(DocumentType.CDS.name())) {%>
                                <tr>
                                    <td><span class="glyphicon glyphicon-file"></span> Document:</td>
                                    <td><strong class="text-primary"><a href="#" onclick="window.open('DocumentPreview.do?doc_type=<%=docSubType%>&doc_no=<%=documentsArray[i]%>&prov_no=<%=user_no%>', 'View Document', 'width=' + popupWidth + ', scrollbars=yes, height=' + popupHeight + ', top=' + popupTop + ', left=' + popupLeft);
                                            return false;" >CDS Export</a></strong></td>
                                </tr>
                                <%}
                                    }%>

                            </tbody>
                        </table>

                        <% if (useXDR == false) { // no folder support in XDR %>

                        <p>Add document(s) to folder: <a href="#" id="newFolderLink" onclick="return false;" class="pull-right"><span class="glyphicon glyphicon-plus"></span> new</a></p>

                        <div class="input-group">
                            <span class="input-group-addon"><span class="glyphicon glyphicon-folder-open"></span> Folder:</span>
                            <select id="folderSelect" name="folderSelect" class="form-control">
                                <option value="">---</option>
                                <% for (FolderMetaData folder : folders) { %>
                                <option value="<%= folder.getUniqueId()%>"><%= folder.getTitle()%></option>
                                <% } %>
                            </select>
                        </div><!-- /input-group -->

                        <% } %>

                        <br />

                        <p>Select the Consent Policy(ies) appropriate for the document(s):</p>

                        <div id="policyGroup" class="input-group">
                            <span class="input-group-addon"><span class="glyphicon glyphicon-eye-close"></span> Policy:</span>
                            <select id="policySelect" name="policySelect" class="form-control multiselect" multiple="multiple">
                                <% for (PolicyDefinitionDataObject policy : network.getPolicyDefinitions()) {%>
                                <option value="<%= policy.getId()%>"><%= policy.getDisplayName()%></option>
                                <% }%>
                            </select>     				     	
                        </div><!-- init group -->

                        <br />

                        <button type="button" id="cancelButton" name="cancelButton" class="btn btn-default">Cancel</button>
                        <button type="button" id="sendButton" name="sendButton" class="btn btn-success pull-right">Send</button>
                    </div> <!-- panel body -->
                </div>


                <!-- New Folder Modal -->
                <div class="modal fade" id="newFolderModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title" id="myModalLabel">New folder</h4>
                            </div>
                            <div class="modal-body">
                                <div class="input-group">
                                    <span class="input-group-addon">Folder: </span>
                                    <input type="text" id="newFolderTitle" name="newFolderTitle" class="form-control" placeholder="Folder name" />
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" id="dismissFolderModalButton" name="dismissFolderModalButton" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button type="button" id="addFolderButton" name="addFolderButton" class="btn btn-primary">Add</button>
                            </div>
                        </div><!-- /.modal-content -->
                    </div><!-- /.modal-dialog -->
                </div><!-- /.modal -->


                <!-- Confirmation Modal -->
                <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Confirm Operation</h4>
                            </div>
                            <div class="modal-body">
                                <p>You are about to perform an operation that requires additional consent,
                                    security permission, or auditing:</p>

                                <p>Please review the following policy(ies) with the patient (if applicable).</p>
                                <br />

                                <div id="policyViewAndConfirmation" class="container well well-sm">
                                    <%-- for (PolicyDefinitionDataObject policy : network.getPolicyDefinitions()) { %>
                                            <span class="glyphicon glyphicon-eye-close"></span> 
                                            <strong class="text-primary">
                                                    <a href="#" onclick="window.open('<%= policy.getPolicyDocUrl() %>','View Policy: <%= policy.getDisplayName() %>','width='+popupWidth+', height='+popupHeight+', top='+popupTop+', left='+popupLeft); return false;"><%= policy.getDisplayName() %></a>
                                            </strong>
                                            <br />
                                    <% } --%>
                                </div>

                                <div class="checkbox">
                                    <label><input type="checkbox" id="consentGiven" name="consentGiven" value="yes"> I give consent to the policy(ies) above</label>
                                </div>
                                <br />


                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon">Patient: </span>
                                        <input type="text" class="form-control" placeholder="<%= demographic.getDisplayName()%>" disabled />
                                    </div>
                                    <br />

                                    <div class="input-group">
                                        <span class="input-group-addon">Physician: </span>
                                        <input type="text" class="form-control" placeholder="<%= String.format("%s, %s", provider.getLastName(), provider.getFirstName())%>" disabled />
                                    </div>
                                    <br />

                                    <div class="input-group">
                                        <span class="input-group-addon">Authenticator: </span>
                                        <select id="authenticatorSelect" name="authenticatorSelect" class="form-control">
                                            <option value="<%= demographic.getDemographicNo()%>"><%= demographic.getDisplayName()%></option>
                                            <% for (Demographic decisionMaker : decisionMakers) {%>
                                            <option value="<%=decisionMaker.getDemographicNo()%>"><%= decisionMaker.getFullName()%></option>
                                            <% } %>
                                        </select>
                                    </div>

                                    <!--
                                    <br />
                                    <div class="input-group">
                                            <span class="input-group-addon">Purpose/Intent: </span>
                                            <select class="form-control">
                                                    <option value="">Some Purpose</option>
                                            </select>
                                    </div>-->
                                </div>
                            </div>
                            <br />

                            <div class="modal-footer">
                                <button type="button" id="dismissConfirmationModalButton" name="dismissConfirmationModalButton" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                <button type="button" id="confirmButton" name="confirmButton" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                            </div>
                            <br />

                        </div>
                    </div>	
                </div><!-- end Consent Modal -->

                <% }

                    
                    
                else { // demographic == null or network == null  %>

                <div class="panel panel-default">
                    <div class="panel-body">
                        <h3 class="panel-title">Error: <span class="label label-danger">Unable to locate patient or network</span></h3>
                    </div>
                </div>

                <% } %>

            </form>

        </div> <!-- container -->

    </body>
</html>
