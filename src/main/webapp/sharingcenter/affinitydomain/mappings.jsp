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
<%@page import="org.oscarehr.sharingcenter.model.MiscMapping"%>
<%@page import="org.oscarehr.sharingcenter.DocumentType"%>
<%@page import="org.oscarehr.sharingcenter.dao.MiscMappingDao"%>
<%@page import="org.oscarehr.sharingcenter.model.EFormMapping"%>
<%@page import="org.oscarehr.sharingcenter.dao.EFormMappingDao"%>
<%@page import="org.oscarehr.common.dao.EFormDao"%>
<%@page import="org.oscarehr.common.model.EForm"%>
<%@page import="org.marc.shic.core.MappingCodeType"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.sharingcenter.model.EDocMapping"%>
<%@page import="org.oscarehr.sharingcenter.dao.EDocMappingDao"%>
<%@page import="org.oscarehr.sharingcenter.dao.SiteMappingDao"%>
<%@page import="org.oscarehr.sharingcenter.model.SiteMapping"%>
<%@page import="org.oscarehr.sharingcenter.model.CodeValueDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.CodeMappingDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="oscar.dms.EDocUtil"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>

<%

    SiteMappingDao siteMappingDao = SpringUtils.getBean(SiteMappingDao.class);
    EDocMappingDao eDocMappingDao = SpringUtils.getBean(EDocMappingDao.class);
    EFormMappingDao eFormMappingDao = SpringUtils.getBean(EFormMappingDao.class);
    MiscMappingDao miscMappingDao = SpringUtils.getBean(MiscMappingDao.class);

    //Network
    AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
    AffinityDomainDataObject network = null;
    if (request.getParameter("id") != null) {
        network = affDao.getAffinityDomain(Integer.parseInt(request.getParameter("id")));
    }

    String mappingType = ""; // default: neither codes nor valuesets exist in the config file..

    // code sets
    Set<CodeValueDataObject> facilityCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> practiceSettingCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> typeCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> classCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> formatCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> contentTypeCodes = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> eventCodeList = new HashSet<CodeValueDataObject>();
    Set<CodeValueDataObject> folderCodeList = new HashSet<CodeValueDataObject>();

    // get svs collection (new requests against the svs repo)
    SiteMapping siteMapping = null;
    List<String> edocTypes = new ArrayList<String>();
    List<EForm> eforms = new ArrayList<EForm>();
    List<DocumentType> miscDocs = new ArrayList<DocumentType>();

    if (network != null) {

        // TODO: figure out if we are using codes or valuesets (use SharingCenterUtil)
        mappingType = "codes"; // or "svs"

        // prepare the code/svs sets
        if (mappingType.equals("codes")) {
            if (network.getCodeMapping(MappingCodeType.HealthcareFacilityTypeCode.toString()) != null) {
                facilityCodes = network.getCodeMapping(MappingCodeType.HealthcareFacilityTypeCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.PracticeSettingCode.toString()) != null) {
                practiceSettingCodes = network.getCodeMapping(MappingCodeType.PracticeSettingCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.TypeCode.toString()) != null) {
                typeCodes = network.getCodeMapping(MappingCodeType.TypeCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.ClassCode.toString()) != null) {
                classCodes = network.getCodeMapping(MappingCodeType.ClassCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.FormatCode.toString()) != null) {
                formatCodes = network.getCodeMapping(MappingCodeType.FormatCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.ContentTypeCode.toString()) != null) {
                contentTypeCodes = network.getCodeMapping(MappingCodeType.ContentTypeCode.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.EventCodeList.toString()) != null) {
                eventCodeList = network.getCodeMapping(MappingCodeType.EventCodeList.toString()).getCodeValues();
            }

            if (network.getCodeMapping(MappingCodeType.FolderCodeList.toString()) != null) {
                folderCodeList = network.getCodeMapping(MappingCodeType.FolderCodeList.toString()).getCodeValues();
            }

        } else if (mappingType.equals("svs")) {
            // TODO: populate svs code sets
        }

        // get current site mapping
        siteMapping = siteMappingDao.findSiteMapping(network.getId());

        // eDoc types
        edocTypes = EDocUtil.getDoctypes("demographic");

        // eForms
        EFormDao eFormDao = (EFormDao) SpringUtils.getBean("EFormDao");
        eforms = eFormDao.findAll(true);

        // other documents (MISC types)
        miscDocs = DocumentType.getAllDocumentTypes();

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

        <title>Code/SVS Mappings</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>

            $(document).ready(function() {
                // load nav bar
                $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");

                // click events
                $("#saveButton").on("click", function() {
                    $("#mappingform").submit();
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

            <form id="mappingform" action="${ctx}/sharingcenter/affinitydomain/DomainMappings.do" method="POST" role="form">

                <div class="page-header">
                    <h3>Code/SVS Mappings</h3>
                </div>

                <% if (network != null && !mappingType.isEmpty()) {%>

                <input id="network_id" name="network_id" type="hidden" value="<%= network.getId()%>" />
                <input id="source" name="source" type="hidden" value="<%= mappingType%>" />

                <div id="domainPanel" class="panel panel-default">
                    <div class="panel-body">
                        <h3 class="panel-title">Domain: <span class="label label-info"><%= network.getName()%></span></h3>
                    </div>
                </div>

                <div id="siteMappingsPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Site Mappings</h3>
                    </div><!-- panel heading -->

                    <div class="panel-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Healthcare Facility Type</th>
                                    <th>Practice Setting</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Clinic</td>
                                    <td>
                                        <% if (mappingType.equals("codes")) { %>

                                        <select id="site_facilityType" name="site_facilityType" class="form-control">
                                            <% for (CodeValueDataObject cv : facilityCodes) {%>
                                            <option value="<%= cv.getDisplayName()%>" <%= (siteMapping != null && siteMapping.getFacilityTypeCode() != null && cv.getDisplayName().equals(siteMapping.getFacilityTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                            <% } %>
                                        </select>

                                        <% } else if (mappingType.equals("svs")) { %>

                                        <select id="site_facilityType" name="site_facilityType" class="form-control">
                                        </select>

                                        <% } %>
                                    </td>
                                    <td>
                                        <% if (mappingType.equals("codes")) { %>

                                        <select id="site_practiceSetting" name="site_practiceSetting" class="form-control">
                                            <% for (CodeValueDataObject cv : practiceSettingCodes) {%>
                                            <option value="<%= cv.getDisplayName()%>" <%= (siteMapping != null && siteMapping.getPracticeSettingCode() != null && cv.getDisplayName().equals(siteMapping.getPracticeSettingCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                            <% } %>
                                        </select>

                                        <% } else if (mappingType.equals("svs")) { %>

                                        <select id="site_practiceSetting" name="site_practiceSetting" class="form-control">
                                        </select>

                                        <% } %>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div> <!-- panel body -->
                </div> <!-- panel -->

                <div id="edocMappingsPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">EDocument Mappings</h3>
                    </div><!-- panel heading -->

                    <div class="panel-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Type Code</th>
                                    <th>Class Code</th>
                                    <th>Format Code</th>
                                    <th>ContentType Code</th>
                                    <th>Folder Code List</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (String edocType : edocTypes) {
                                        EDocMapping eDocMapping = eDocMappingDao.findEDocMapping(network.getId(), edocType);
                                %>
                                <tr>
                                    <td><%= edocType%></td>
                            <input type="hidden" id="edoc_type" name="edoc_type" value="<%= edocType%>" />
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="edoc_typeCode" name="edoc_typeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : typeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eDocMapping != null && eDocMapping.getTypeCode() != null && cv.getDisplayName().equals(eDocMapping.getTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="edoc_typeCode" name="edoc_typeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="edoc_classCode" name="edoc_classCode" class="form-control">
                                    <% for (CodeValueDataObject cv : classCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eDocMapping != null && eDocMapping.getClassCode() != null && cv.getDisplayName().equals(eDocMapping.getClassCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="edoc_classCode" name="edoc_classCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="edoc_formatCode" name="edoc_formatCode" class="form-control">
                                    <% for (CodeValueDataObject cv : formatCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eDocMapping != null && eDocMapping.getFormatCode() != null && cv.getDisplayName().equals(eDocMapping.getFormatCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="edoc_formatCode" name="edoc_formatCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="edoc_contentTypeCode" name="edoc_contentTypeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : contentTypeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eDocMapping != null && eDocMapping.getContentTypeCode() != null && cv.getDisplayName().equals(eDocMapping.getContentTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="edoc_contentTypeCode" name="edoc_contentTypeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="edoc_folderCodeList" name="edoc_folderCodeList" class="form-control">
                                    <% for (CodeValueDataObject cv : folderCodeList) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eDocMapping != null && eDocMapping.getFolderCodeList() != null && cv.getDisplayName().equals(eDocMapping.getFolderCodeList().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="edoc_folderCodeList" name="edoc_folderCodeList" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            </tr>
                            <% } %>

                            </tbody>
                        </table>
                    </div> <!-- panel body -->
                </div> <!-- panel -->

                <div id="eformMappingsPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">EForm Mappings</h3>
                    </div><!-- panel heading -->

                    <div class="panel-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Type Code</th>
                                    <th>Class Code</th>
                                    <th>Format Code</th>
                                    <th>ContentType Code</th>
                                    <th>Folder Code List</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (EForm eform : eforms) {
                                        EFormMapping eFormMapping = eFormMappingDao.findEFormMapping(network.getId(), eform.getId());
                                %>
                                <tr>
                                    <td><%= eform.getFormName()%></td>
                            <input type="hidden" id="eform_id" name="eform_id" value="<%= eform.getId()%>" />
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="eform_typeCode" name="eform_typeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : typeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eFormMapping != null && eFormMapping.getTypeCode() != null && cv.getDisplayName().equals(eFormMapping.getTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="eform_typeCode" name="eform_typeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="eform_classCode" name="eform_classCode" class="form-control">
                                    <% for (CodeValueDataObject cv : classCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eFormMapping != null && eFormMapping.getClassCode() != null && cv.getDisplayName().equals(eFormMapping.getClassCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="eform_classCode" name="eform_classCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="eform_formatCode" name="eform_formatCode" class="form-control">
                                    <% for (CodeValueDataObject cv : formatCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eFormMapping != null && eFormMapping.getFormatCode() != null && cv.getDisplayName().equals(eFormMapping.getFormatCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="eform_formatCode" name="eform_formatCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="eform_contentTypeCode" name="eform_contentTypeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : contentTypeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eFormMapping != null && eFormMapping.getContentTypeCode() != null && cv.getDisplayName().equals(eFormMapping.getContentTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="eform_contentTypeCode" name="eform_contentTypeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="eform_folderCodeList" name="eform_folderCodeList" class="form-control">
                                    <% for (CodeValueDataObject cv : folderCodeList) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (eFormMapping != null && eFormMapping.getFolderCodeList() != null && cv.getDisplayName().equals(eFormMapping.getFolderCodeList().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="eform_folderCodeList" name="eform_folderCodeList" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            </tr>
                            <% } %>

                            </tbody>
                        </table>
                    </div> <!-- panel body -->
                </div> <!-- panel -->

                <div id="miscMappingsPanel" class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Other Document Mappings (Misc)</h3>
                    </div><!-- panel heading -->

                    <div class="panel-body">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Type Code</th>
                                    <th>Class Code</th>
                                    <th>Format Code</th>
                                    <th>ContentType Code</th>
                                    <th>Folder Code List</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (DocumentType miscDoc : miscDocs) {
                                        MiscMapping miscMapping = miscMappingDao.findMiscMapping(network.getId(), miscDoc.name());
                                %>
                                <tr>
                                    <td><%= miscDoc.getValue()%></td>
                            <input type="hidden" id="misc_type" name="misc_type" value="<%= miscDoc.name()%>" />
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="misc_typeCode" name="misc_typeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : typeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (miscMapping != null && miscMapping.getTypeCode() != null && cv.getDisplayName().equals(miscMapping.getTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="misc_typeCode" name="misc_typeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="misc_classCode" name="misc_classCode" class="form-control">
                                    <% for (CodeValueDataObject cv : classCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (miscMapping != null && miscMapping.getClassCode() != null && cv.getDisplayName().equals(miscMapping.getClassCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="misc_classCode" name="misc_classCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="misc_formatCode" name="misc_formatCode" class="form-control">
                                    <% for (CodeValueDataObject cv : formatCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (miscMapping != null && miscMapping.getFormatCode() != null && cv.getDisplayName().equals(miscMapping.getFormatCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="misc_formatCode" name="misc_formatCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="misc_contentTypeCode" name="misc_contentTypeCode" class="form-control">
                                    <% for (CodeValueDataObject cv : contentTypeCodes) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (miscMapping != null && miscMapping.getContentTypeCode() != null && cv.getDisplayName().equals(miscMapping.getContentTypeCode().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="misc_contentTypeCode" name="misc_contentTypeCode" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            <td>
                                <% if (mappingType.equals("codes")) { %>

                                <select id="misc_folderCodeList" name="misc_folderCodeList" class="form-control">
                                    <% for (CodeValueDataObject cv : folderCodeList) {%>
                                    <option value="<%= cv.getDisplayName()%>" <%= (miscMapping != null && miscMapping.getFolderCodeList() != null && cv.getDisplayName().equals(miscMapping.getFolderCodeList().getDisplayName())) ? "selected" : ""%>><%= cv.getDisplayName()%></option>
                                    <% } %>
                                </select>

                                <% } else if (mappingType.equals("svs")) { %>

                                <select id="misc_folderCodeList" name="misc_folderCodeList" class="form-control">
                                </select>

                                <% } %>
                            </td>
                            </tr>
                            <% } %>

                            </tbody>
                        </table>
                    </div> <!-- panel body -->
                </div> <!-- panel -->

                <button type="button" id="saveButton" name="saveButton" class="btn btn-success pull-right">Save</button>
                <br />
                <br />

                <% } else { // demographic == null or network == null  %>

                <div class="panel panel-default">
                    <div class="panel-body">
                        <h3 class="panel-title">Error: <span class="label label-danger">Problem with the affinity domain or there are no codes to display</span></h3>
                    </div>
                </div>

                <% } %>

            </form>

        </div> <!-- container -->

    </body>
</html>