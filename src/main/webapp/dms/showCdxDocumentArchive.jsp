<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "x" uri = "http://java.sun.com/jsp/jstl/xml" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>


<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.*" %>

<%@ page import="org.oscarehr.integration.cdx.dao.*" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxAttachment" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDocument" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.datatypes.DocumentType" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.datatypes.DocumentStatus" %>
<%

    CdxAttachmentDao cdxAttachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);
    CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
    CdxProvenance provenanceDoc;

    String cdxDocId = request.getParameter("ID");

    if (cdxDocId != null) {
        int cdxDocIdNo = Integer.parseInt(cdxDocId);
        provenanceDoc = provenanceDao.getCdxProvenance(cdxDocIdNo);

    } else {
        String eDocId = request.getParameter("EDID");
        provenanceDoc = provenanceDao.findByDocumentNo(Integer.parseInt(eDocId));
    }

%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>CDX Document Archive Viewer</title>

    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js" ></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/fonts-min.css"/>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/autocomplete.css"/>


    <script type="text/javascript">
        jQuery.noConflict();

    </script>

</head>
<body>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">
            <div class="row">

                <!--  ************************************ this code below duplicated in showCdxDocument
                       ************************************ keep consistent upon changing
                       *********************BEGIN *********************************************************** -->

                <%
                    List<CdxProvenance> versions = provenanceDao.findVersionsOrderDesc(provenanceDoc.getSetId());
                    if (versions.size() > 1) {
                        if (provenanceDoc.getId().equals(versions.get(0).getId())) {
                %>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        Multiple versions of this document exist. You are looking at the <strong> latest</strong> version (<%=provenanceDoc.getVersion()%>).
                    </div>
                    <% } else { %>
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <strong> Warning! </strong> Multiple versions of this document exist. You are looking at an <strong> outdated </strong> version (<%=provenanceDoc.getVersion()%>).
                        </div>
                        <% } %>
                        <div class="panel panel-body">
                            <ul class="list-group">
                                <%
                                    for (CdxProvenance p : versions) {
                                %>
                                <a href="showCdxDocumentArchive.jsp?ID=<%=p.getId()%>" class="list-group-item <%=(p.getId().equals(provenanceDoc.getId()) ? "disabled" : "")%> ">
                                    Version <%=p.getVersion()%>, Effective time: <%=p.getEffectiveTime()%>
                                </a>

                                <% }%>
                            </ul>
                        </div>
                    </div>
                    <%}%>


                    <%
                        if (provenanceDoc.isStatusWarning() ) {
                            out.println("<div class=\"panel panel-warning\">");
                        } else if (provenanceDoc.isStatusDanger() ) {
                                out.println("<div class=\"panel panel-danger\">");
                        } else {
                                out.println("<div class=\"panel panel-default\">");
                        }
                    %>
                        <div class="panel-heading">Status: <%=provenanceDoc.getStatus()%><%= provenanceDoc.getReceivedTime() != null ? ", Received at: " + provenanceDoc.getReceivedTime() : "" %></div>

                        <div class="panel-body">

                            <c:import url="/share/xslt/CDA_to_HTML.xsl" var="xslt"/>
                            <x:transform xml="<%=provenanceDoc.getPayload()%>" xslt="${xslt}"/>
                        </div>
                    </div>

                    <%
                        List<CdxAttachment> atts = cdxAttachmentDao.findByDocNo(provenanceDoc.getId());
                        if (!atts.isEmpty()) {
                    %>

                    <div class="panel-footer">
                        <h3>Attachments (<%=atts.size()%>):</h3>
                        <ul>
                            <%
                                for (CdxAttachment a : atts) { %>
                            <li> <a href="#" onclick="popup(360, 680, '../dms/ManageDocument.do?method=viewCdxAttachment&attId=<%= a.getId() %>', 'Attachment: <%=a.getReference()%>')">

                                <%=a.getReference()%> </a> (<%=a.getAttachmentType()%>) </li>

                            <% }%>
                        </ul>
                    </div>

                    <% }%>


                <% // was this document amended?

                    List<CdxProvenance> childDocs = provenanceDao.findChildDocuments(provenanceDoc.getDocumentId());
                    if(!childDocs.isEmpty()) {
                %>

                <div class="alert alert-danger">
                    This document has <strong> ammendments</strong>:
                    <ul>
                        <%
                            for (CdxProvenance child : childDocs) {
                        %>
                        <li>
                            <a href="showCdxDocumentArchive.jsp?ID=<%=child.getId()%>">
                                <%=child.getKind()%> (<%=child.getStatus()%>)</a>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </div>

                <%
                    } %>


                <% // is this document a child document to a parent?
                    String parentDocId = provenanceDoc.getParentDoc();
                    if(parentDocId != null) {
                        CdxProvenance parentDoc = provenanceDao.findByDocumentId(parentDocId);
                        if (!(parentDoc==null)) {
                %>

                <div class="alert alert-warning">
                    This document has a <strong> parent document</strong>:
                    <a href="showCdxDocumentArchive.jsp?ID=<%=parentDoc.getId()%>">

                        <%=parentDoc.getKind()%> </a>
                </div>

                <%
                        }} %>


                <% // was this document created in fulfillment of another document?
                    String infulfillmentOfId = provenanceDoc.getInFulfillmentOfId();
                    if(infulfillmentOfId != null) {
                        CdxProvenance iffoDoc = provenanceDao.findByDocumentId(infulfillmentOfId);
                        if (!(iffoDoc==null)) {
                %>
                <div class="alert alert-success">
                    This document was created <strong>in fulfillment of</strong>:
                    <a href="showCdxDocumentArchive.jsp?ID=<%=iffoDoc.getId()%>">

                        <%=iffoDoc.getKind()%> </a>
                </div>

                <%
                        }} %>
            </div>

        </div>

                <!--        ************************************ the code above is duplicated in showCdxDocument
                            ************************************ keep consistent upon changing
                            *********************END *********************************************************** -->

            </div>

            <%
                if (provenanceDoc.getAction().equals("SEND")) {

                    if (provenanceDoc.getKind().equals(DocumentType.REFERRAL_NOTE.label)) {
                        out.print("<div> <a href=\"/oscar/oscarEncounter/ViewRequest.do?requestId=" + provenanceDoc.getInFulfillmentOfId() + "\"" + " class =\"btn btn-info\" role=\"button\">  Open corresponding consultation </a> </div>");
                    }
                }
            %>

        </div>
    </div>

</body>
</html>
