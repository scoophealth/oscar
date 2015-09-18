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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.CodeValueDataObject"%>
<%@page import="org.oscarehr.sharingcenter.actions.DocumentPermissionStatus"%>
<%@page import="org.oscarehr.sharingcenter.DocumentConsentWrapper"%>
<%@page import="org.oscarehr.sharingcenter.AffinityDomainDocuments"%>
<%@page import="org.marc.shic.xds.utils.XdStarUtility"%>
<%@page import="org.oscarehr.sharingcenter.SharedDocumentsModel"%>
<%@page import="org.oscarehr.sharingcenter.AffinityDomainFolderMetaData"%>
<%@page import="org.marc.shic.core.FolderMetaData"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.marc.shic.core.exceptions.CommunicationsException"%>
<%@page import="org.marc.shic.core.DocumentMetaData"%>
<%@page import="org.marc.shic.xds.XdsCommunicator"%>
<%@page import="org.marc.shic.core.configuration.IheConfiguration"%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="org.marc.shic.pix.PixApplicationException"%>
<%@page import="org.oscarehr.sharingcenter.model.PatientDocument"%>
<%@page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic, org.oscarehr.util.SpringUtils"%>
<%
    SharedDocumentsModel model = (SharedDocumentsModel) request.getAttribute("model");

    String submitUrl = "SharedDocuments.do?demographic_no=" + request.getParameter("demographic_no");
%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Shared Documents</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <style>
            .policy-group {
                padding-bottom: 5px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="page-header">
                    <h1>Shared Documents</h1>
                </div>
                <% if (model.getErrors().size() > 0) { %>
                <div class="alert alert-danger"><strong>The follow errors occurred:</strong>
                    <ul>
                        <% for (String error : model.getErrors()) {%>
                        <li><strong><%= error%></strong></li>              
                                <% } %>
                    </ul>
                </div>
                <% } %>           
                <% if (model.getDownloadedDocumentNames().size() > 0) { %>
                <div class="alert alert-success">
                    <strong>Documents Downloaded.</strong> The following documents have been successfully downloaded: 
                    <ul>
                        <% for (String docTitle : model.getDownloadedDocumentNames()) {%>
                        <li><strong><%= docTitle%></strong></li>
                                <% } %>
                    </ul>
                </div>
                <% } %>

                <% if (model.isConsentGiven()) { %>
                <div class="alert alert-success"><strong>Access elevated.</strong> Your access to documents has been elevated temporarily. Switching pages will reset your elevation.</div>
                <% } %>
            </div>
            <div class="row">
                <div class="col-md-2">
                    <h3>Folders</h3>
                    <div class="list-group">
                        <%
                            if (model != null) {
                                if (request.getParameter("folder") != null) {
                        %><a class="list-group-item" href="<%=submitUrl%>"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;Back</a><%
                            }
                            for (AffinityDomainFolderMetaData folder : model.getFolders()) {
                        %><a class="list-group-item<%= (folder.getFolder().getUniqueId().equals(request.getParameter("folder"))) ? " active" : ""%>" href="<%=String.format("%s&folder=%s&selectedAffinityDomain=%s&demographic_no=%s", submitUrl, folder.getFolder().getUniqueId(), folder.getAffinityDomainId(), request.getParameter("demographic_no"))%>"><%=folder.getFolder().getTitle()%></a><%
                                }
                            }
                        %> 
                    </div>
                </div>  
                <div class="col-md-10">
                    <form name="documentAction" action="SharedDocuments.do" method="POST">
                        <% if (model != null) {%>
                        <input id="selectedAffinityDomain" name="selectedAffinityDomain" type="hidden" value="<%= model.getSelectedAffinityDomain().getId()%>" />                        
                        <input id="demographic_no" name="demographic_no" type="hidden" value="<%= request.getParameter("demographic_no")%>" />  
                        <% if (request.getParameter("folder") != null) {%>
                        <input id="folder" name="folder" type="hidden" value="<%= request.getParameter("folder")%>" />  
                        <% } %>
                        <% }%>   
                        <div class="row">
                            <div class="panel panel-primary">
                                <div class="panel-heading">Shared Documents</div>
                                <div class="panel-body">
                                    <div>
                                        <span>You are currently viewing <strong><%= model.getSelectedAffinityDomain().getName()%>'s</strong>  network.</span>
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                                                Switch Network <span class="caret"></span>
                                            </button>                                         
                                            <ul class="dropdown-menu" role="menu">
                                                <% for (AffinityDomainDataObject domain : model.getSharedAffinityDomains()) {%>
                                                <li><a href="<%= String.format("%s&selectedAffinityDomain=%s", submitUrl, domain.getId())%>"><%= domain.getName()%></a></li>
                                                    <% } %>  
                                            </ul>
                                        </div>
                                    </div>
                                    <br />
                                    <table class="table">
                                        <tr>
                                            <th>Download</th>
                                            <th>Name</th>
                                            <th>Modified</th>
                                            <th>Author</th>
                                            <th>Document Type</th>
                                        </tr>
                                        <%
                                            if (model != null) {
                                                for (AffinityDomainDocuments affinityDomainDocuments : model.getAffinityDomainSharedDocuments()) {
                                                    for (DocumentConsentWrapper consentDoc : affinityDomainDocuments.getDocuments()) {
                                                        if (consentDoc.getDisclosePermission() != DocumentPermissionStatus.AccessDenied) {

                                                            PatientDocument doc = consentDoc.getDocument();
                                                            String author = XdStarUtility.parseAuthor(doc.getAuthor());

                                                            boolean documentDiscloseWarning = consentDoc.getDisclosePermission().equals(DocumentPermissionStatus.RequiresPolicyConsent)
                                                                    || (consentDoc.getDisclosePermission().equals(DocumentPermissionStatus.RequiresElevation) && !model.isConsentGiven());

                                                            boolean documentImportWarning = consentDoc.getImportPermission().equals(DocumentPermissionStatus.RequiresPolicyConsent)
                                                                    || (consentDoc.getImportPermission().equals(DocumentPermissionStatus.RequiresElevation) && !model.isConsentGiven());

                                                            boolean isDocumentImportDenied = consentDoc.getImportPermission().equals(DocumentPermissionStatus.AccessDenied);
                                        %> 
                                        <tr<%= (documentDiscloseWarning || documentImportWarning) ? " class=\"warning\"" : ""%>>
                                            <td>
                                                <% if (!isDocumentImportDenied && !documentImportWarning) {%>
                                                <input type="checkbox" name="docsToDownload" value="<%=doc.getId()%>"/>
                                                <% } %>                                                                
                                            </td>
                                            <td>
                                                <% if (documentDiscloseWarning) {
                                                        switch (consentDoc.getDisclosePermission()) {
                                                            case RequiresPolicyConsent: %>
                                                <em>Consent is required to view this document.</em><%
                                                        break;
                                                    case RequiresElevation: %>
                                                <em>Elevation is required to view this document.</em><%
                                                            break;
                                                    } %>
                                                <% } else {%>
                                                <%= doc.getTitle()%>
                                                <% } %>                                                                                                                      
                                            </td>
                                            <% if (!documentDiscloseWarning) {%>
                                            <td><%=doc.getCreationTime().toString()%></td>
                                            <td><%=author%></td>
                                            <td><%=doc.getMimetype()%></td>
                                            <% } else { %>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <% } %>
                                        </tr>
                                        <%
                                                        }
                                                    }
                                                }
                                            }
                                        %> 
                                    </table>
                                    <div>
                                        <input type="submit" class="btn btn-primary pull-left" name="downloadDocuments" value="Download" />
                                        <div class="pull-right">
                                            <% if (!model.getUnconsentedPolicies().isEmpty()) { %>
                                            <button type="button" id="consent" name="consent" class="btn btn-warning" data-toggle="modal" data-target="#consentModal">Consent</button>
                                            <% } %>
                                            <button type="button" id="elevate" name="elevate" class="btn btn-info" data-toggle="modal" data-target="#elevateModal">Elevate</button> 
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <% if (!model.getUnconsentedPolicies().isEmpty()) {%>
                        <!-- Consent Modal -->
                        <div class="modal fade" id="consentModal" tabindex="-1" role="dialog" aria-labelledby="elevateLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                        <h4 class="modal-title" id="myModalLabel">Consent</h4>
                                    </div>
                                    <div class="modal-body">                                  
                                        <h3>Policies</h3>
                                        <div class="input-group">
                                            <span class="input-group-addon">Authenticator: </span>
                                            <select id="authenticatorSelect" name="consentAuthenticatorSelect" class="form-control">
                                                <option value="<%= model.getDemographic().getDemographicNo()%>"><%= model.getDemographic().getDisplayName()%></option>
                                                <% for (Demographic decisionMaker : model.getDecisionMakers()) {%>
                                                <option value="<%=decisionMaker.getDemographicNo()%>"><%= decisionMaker.getFullName()%></option>
                                                <% } %>
                                            </select>
                                        </div>
                                        <br />
                                        <div class="input-group">
                                            <% for (PolicyDefinitionDataObject policy : model.getUnconsentedPolicies()) {%>
                                            <div class="input-group policy-group">
                                                <span class="input-group-addon"><%= policy.getDisplayName()%></span>
                                                <span class="input-group-addon"><a href="<%= policy.getPolicyDocUrl()%>" target="_blank">View Policy</a></span>                                      
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <% if (!model.getUnconsentedPolicies().isEmpty()) { %>                                    

                                        <div class="input-group policy-group">
                                            <span class="input-group-addon"><div class="pull-left">By confirming consent you agree to all the listed policies.</div></span>
                                            <span class="input-group-addon"><input type="submit" class="btn btn-primary pull-right" name="confirmConsent" value="Confirm Consent" /></span>
                                        </div>    
                                        <div class="input-group policy-group">
                                            <span class="input-group-addon"><div class="pull-left">Cancel consent. No information will be processed.</div></span>
                                            <span class="input-group-addon">
                                                <button type="button" id="dismissElevateModalButton" name="dismissConsentModalButton" class="btn btn-danger pull-right" data-dismiss="modal">Cancel</button>
                                            </span>
                                        </div>     
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% }%>
                        <!-- Elevate Modal -->
                        <div class="modal fade" id="elevateModal" tabindex="-1" role="dialog" aria-labelledby="elevateLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                        <h4 class="modal-title" id="myModalLabel">Elevate</h4>
                                    </div>
                                    <div class="modal-body">                                  
                                        <h3>Consent Codes</h3>
                                        <div class="input-group">
                                            <span class="input-group-addon">Authenticator: </span>
                                            <select id="authenticatorSelect" name="authenticatorSelect" class="form-control">
                                                <option value="<%= model.getDemographic().getDemographicNo()%>"><%= model.getDemographic().getDisplayName()%></option>
                                                <% for (Demographic decisionMaker : model.getDecisionMakers()) {%>
                                                <option value="<%=decisionMaker.getDemographicNo()%>"><%= decisionMaker.getFullName()%></option>
                                                <% } %>
                                            </select>
                                        </div>
                                        <br />
                                        <strong>You are overriding the following policies:</strong>
                                        <% for (PolicyDefinitionDataObject policy : model.getElevatePolicies()) {%>
                                        <div class="input-group policy-group">
                                            <span class="input-group-addon"><%= policy.getDisplayName()%></span>
                                            <span class="input-group-addon"><a href="<%= policy.getPolicyDocUrl()%>" target="_blank">View Policy</a></span>
                                        </div>
                                        <% } %>

                                        <br />
                                        <div class="input-group policy-group">
                                            <span class="input-group-addon"><strong>Purpose/Intent:</strong></span>
                                            <select id="purposeOfUse" name="purposeOfUse" class="form-control">
                                                <% for (CodeValueDataObject codeValue : model.getConsentCodeMappings().getCodeValues()) {%>
                                                <option value="<%= codeValue.getCode()%>"><%= codeValue.getDisplayName()%></option>
                                                <% }%>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" id="dismissElevateModalButton" name="dismissElevateModalButton" class="btn btn-default" data-dismiss="modal">Cancel</button>
                                        <input type="submit" class="btn btn-primary pull-left" name="confirmElevate" value="Elevate" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>                          
                </div>
            </div>
        </div>		
    </body>
</html>