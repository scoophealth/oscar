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

<%@ page import="org.oscarehr.integration.cdx.dao.*" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.datatypes.DocumentType" %>
<%

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

    <title>CDX Document Viewer</title>

    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js" ></script>

    <link rel="stylesheet" href="<%= request.getContextPath() %>/share/css/bootstrap.min.css">

    <script src="<%= request.getContextPath() %>/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/fonts-min.css"/>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/autocomplete.css"/>


    <script type="text/javascript">
        jQuery.noConflict();

    </script>

</head>
<body>

<div class="container-fluid">

    <div class="row">
        <br>
      <div>

          <%         String status = "";
                          if (provenanceDoc.getAction().equals("SEND")) {
                          status=provenanceDoc.getDistributionStatus();
                              out.print("<strong><font size=\"3px\">Distribution Status:</font> </strong>");
                          if (status.equals("DELIVERED"))
                              out.print("<strong><font size=\"2px\">DELIVERED </font> </strong>");
                          else if (status.equals("QUEUED"))
                              out.print("<strong><font size=\"2px\">QUEUED </font> </strong>");
                          else if (status.startsWith("UNKNOWN"))
                              out.print("<strong><font size=\"2px\">UNKNOWN </font> </strong>");
                          else if (status.equals("LOST"))
                              out.print("<strong><font size=\"2px\">LOST </font> </strong>");
                          else out.print("<strong><font size=\"2px\">UNDELIVERABLE </font> </strong>");
                              out.print(" <a href=\""+request.getContextPath()+"/dms/displayDistributionStatus.jsp?documentId=" + provenanceDoc.getDocumentId() + "\"" + " class =\"btn btn-info\" role=\"button\"> Show Detailed Distribution Status</a> ");
                      }
          %>
      </div> <hr>
        <div class="col-md-12">



            <div class="row">

                <%@include  file = "renderCdxDocument.jsp" %>

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
</div>

</body>
</html>
