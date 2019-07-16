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
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>


<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.*" %>

<%@ page import="org.oscarehr.integration.cdx.dao.*" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxAttachment" %>
<%


    String docId = request.getParameter("ID");
    int docIdNo = Integer.parseInt(docId);
    CdxAttachmentDao cdxAttachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);
    CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);


    CdxProvenance provenanceDoc = provenanceDao.findByDocumentNo(docIdNo);



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
                    List<CdxProvenance> versions = provenanceDao.findReceivedVersionsOrderDesc(provenanceDoc.getDocumentId());
                    if (versions.size() > 1) {
                        if (provenanceDoc.getId().equals(versions.get(0).getId())) {
                %>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        Multiple versions of this document exist. You are looking at the <strong> latest</strong> version (<%=provenanceDoc.getVersion()%>).
                    </div>
                    <% } else { %>
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <strong> Warning! </strong> Multiple versions of this document exist. You are looking at an <strong> outdated </strong> version (<%=provenanceDoc.getVersion()%>).
                        </div>
                        <% } %>
                        <div class="panel panel-body">
                            <ul class="list-group">
                                <%
                                    for (CdxProvenance p : versions) {
                                %>
                                <a href="showCdxDocumentArchive.jsp?ID=<%=p.getDocumentNo()%>" class="list-group-item <%=(p.getId().equals(provenanceDoc.getId()) ? "list-group-item-info" : "")%> ">
                                    Version <%=p.getVersion()%>, Effective time: <%=p.getEffectiveTime()%>
                                </a>

                                <% }%>
                            </ul>
                        </div>
                    </div>
                    <%}%>


                    <div class="panel panel-default">
                        <div class="panel-heading">Status: <%=provenanceDoc.getStatus()%></div>

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
                            <li> <a href="#" onclick="javascript:popup(360, 680, '../dms/ManageDocument.do?method=viewCdxAttachment&attId=<%= a.getId() %>', 'Attachment: <%=a.getReference()%>')">

                                <%=a.getReference()%> </a> (<%=a.getAttachmentType()%>) </li>

                            <% }%>
                        </ul>
                    </div>

                    <% }%>


                    <div class="panel-footer">
                        <h3>Related documents:</h3>
                        <ul>
                            <%  boolean anyRelated = false;
                                String parentDocId = provenanceDoc.getParentDoc();
                                if(parentDocId != null) {
                                    List<CdxProvenance> parentDocs = provenanceDao.findReceivedVersionsOrderDesc(parentDocId);
                                    if (!parentDocs.isEmpty()) {
                                        CdxProvenance parentDoc = parentDocs.get(0);
                                        anyRelated = true;
                            %>
                            <li> <a href="showCdxDocumentArchive.jsp?ID=<%=parentDoc.getId()%>">

                                <%=parentDoc.getDocumentId()%> (parent document) </a>  </li>

                            <%
                                    }} %>

                            <%
                                String infulfillmentOfId = provenanceDoc.getInFulfillmentOfId();
                                if(infulfillmentOfId != null) {
                                    List<CdxProvenance> iffoDocs = provenanceDao.findVersionsOrderDesc(infulfillmentOfId);
                                    if (!iffoDocs.isEmpty()) {
                                        CdxProvenance iffoDoc = iffoDocs.get(0);
                                        anyRelated = true;
                            %>
                            <li> <a href="showCdxDocumentArchive.jsp?ID=<%=iffoDoc.getId()%>">

                                <%=iffoDoc.getDocumentId()%> (in fulfillment of) </a>  </li>

                            <%
                                    }} %>

                            <%

                                String setId = provenanceDoc.getSetId();
                                if(setId != null && (!setId.equals(""))) {
                                    List<CdxProvenance> setDocs = provenanceDao.findRelatedDocsBySetId(setId, provenanceDoc.getDocumentId());
                                    for (CdxProvenance d : setDocs) {
                                        anyRelated = true;
                            %>

                            <li> <a href="showCdxDocumentArchive.jsp?ID=<%=d.getId()%>">

                                <%=d.getDocumentId()%> (same document set)</a>  </li>
                            <%
                                    }}

                            %>

                        </ul>

                        <%
                            if (anyRelated == false)
                                out.print("none");
                        %>

                    </div>

                </div>

                <!--        ************************************ the code above is duplicated in showCdxDocument
                            ************************************ keep consistent upon changing
                            *********************END *********************************************************** -->

            </div>


        </div>
    </div>

</div>

</body>
</html>
