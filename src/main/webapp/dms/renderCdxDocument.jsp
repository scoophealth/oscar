<%--
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 *
--%>

<%-- Note: this file is included in showCdxDocument.jsp and showCdxDocumentArchive.jsp --%>

<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxAttachment" %>

<%
    List<CdxProvenance> versions = provenanceDao.findReceivedVersions(provenanceDoc.getSetId(), provenanceDoc.getInFulfillmentOfId());
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
</div> <%}%>


<% // was this document amended? (does this document have "child" documents?)

    List<CdxProvenance> childDocs = provenanceDao.findChildDocuments(provenanceDoc.getDocumentId());
    if(!childDocs.isEmpty()) {
%>

<div class="alert alert-danger alert-dismissable" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <p class="text-danger">
        Note: The displayed document has <strong> amendments</strong>:
    <ul>
        <%for (CdxProvenance child : childDocs) {%>
            <li>
                <a class="alert-link alert-danger" href="showCdxDocumentArchive.jsp?ID=<%=child.getId()%>">
                    <%=child.getKind()%> (<%=child.getStatus()%>)</a>
            </li>
        <%}%>
    </ul>
    </p>
</div>

<% } %>


<% // is this document a child document to a parent?
    String parentDocId = provenanceDoc.getParentDoc();
    if(parentDocId != null) {
        CdxProvenance parentDoc = provenanceDao.findByDocumentId(parentDocId);
        if (!(parentDoc==null)) {
%>

<div class="alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <p class="text-warning">
        Note: The displayed document has a <strong> parent document</strong>:
        <a class="alert-link alert-warning" href="showCdxDocumentArchive.jsp?ID=<%=parentDoc.getId()%>">

            <%=parentDoc.getKind()%> </a>
    </p>
</div>

<%}} %>


<% // was this document created in fulfillment of another document?
    String infulfillmentOfId = provenanceDoc.getInFulfillmentOfId();
    if(infulfillmentOfId != null) {
        CdxProvenance iffoDoc = provenanceDao.findByDocumentId(infulfillmentOfId);
        if (!(iffoDoc==null)) {
%>
<div class="alert alert-success alert-dismissable" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <p class="text-success">
        Note: The displayed document was created <strong>in fulfillment of</strong>:
        <a class="alert-link alert-success" href="showCdxDocumentArchive.jsp?ID=<%=iffoDoc.getId()%>" >

            <%=iffoDoc.getKind()%> </a>
    </p>
</div>

<%}} %>

<% // was this document fulfilled by other documents?

    childDocs = provenanceDao.findFulfillingDocuments(provenanceDoc.getDocumentId());
    if(!childDocs.isEmpty()) {
%>

<div class="alert alert-success alert-dismissable" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <p class="text-success">
        Note: This document was fulfilled by other documents:
    <ul>
        <%for (CdxProvenance child : childDocs) {%>
        <li>
            <a class="alert-link alert-success" href="showCdxDocumentArchive.jsp?ID=<%=child.getId()%>">
                <%=child.getKind()%> (<%=child.getStatus()%>)</a>
        </li>
        <%}%>
    </ul>
    </p>
</div>

<% } %>

<% if (provenanceDoc.isStatusWarning() ) { %>
<div class="panel panel-warning">
        <% } else if (provenanceDoc.isStatusDanger() ) { %>
    <div class="panel panel-danger">
            <% } else { %>
        <div class="panel panel-default">
            <%}%>
            <div class="panel-heading"> <strong>
                Status: <%=provenanceDoc.getStatus()%><%= provenanceDoc.getReceivedTime() != null ? ", Received at: " + provenanceDoc.getReceivedTime() : "" %>
            </strong></div>

            <div class="panel-body">
                <c:import url="/share/xslt/CDA_to_HTML.xsl" var="xslt"/>
                <x:transform xml="<%=provenanceDoc.getPayload()%>" xslt="${xslt}"/>
            </div>
        </div>

    <%
    CdxAttachmentDao cdxAttachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);
    List<CdxAttachment> atts = cdxAttachmentDao.findByDocNo(provenanceDoc.getId());
    if (!atts.isEmpty()) {%>

        <div class="panel-footer">
            <h3>Attachments (<%=atts.size()%>):</h3>
            <ul class="list-group">
                <%
                    for (CdxAttachment a : atts) { %>
                <li class="list-group-item">
                    <span class="badge"><%=a.getAttachmentType()%></span>
                    <a href="#" class="alert-link" onclick="popup(360, 680, '../dms/ManageDocument.do?method=viewCdxAttachment&attId=<%= a.getId() %>', 'Attachment: <%=a.getReference()%>')">

                        <%=a.getReference()%> </a> </li>

                <% }%>
            </ul>
        </div>

        <% }%>




