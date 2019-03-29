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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed=false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if(!authed) {
        return;
    }
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<%@ page import="org.oscarehr.integration.cdx.dao.CdxDocumentDao" %>

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.integration.cdx.model.CdxDocument" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxPerson" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxPersonDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxAttachment" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxAttachmentDao" %>
<%@ page import="java.util.Iterator" %>
<%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

    String demoName=request.getParameter("demoName");
    String documentNo = request.getParameter("segmentID");
    Integer docNo = Integer.parseInt(documentNo);
    DocumentDao docDao = SpringUtils.getBean(DocumentDao.class);
    CdxDocumentDao cdxDocDao = SpringUtils.getBean(CdxDocumentDao.class);
    CdxPersonDao cdxPersonDao = SpringUtils.getBean(CdxPersonDao.class);
    CdxAttachmentDao cdxAttachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);

    Document    doc = docDao.findActiveByDocumentNo(docNo).get(0);
    CdxDocument cdxDoc = cdxDocDao.getCdxDocument(documentNo);

%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>CDX Document</title>

    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>


</head>
<body>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6">
                    <h3 >
                        <%out.print(cdxDoc.getTitle());%>
                    </h3>
                    <table class="table table-condensed">
                        <tbody>
                        <tr >
                            <td class="active col-md-1">Document type:</td>
                            <td class="col-md-3"><% out.print(cdxDoc.getTemplateName()
                                                            + "/"
                                                            + cdxDoc.getLoincName()); %></td>
                        </tr>
                        <tr>
                            <td class="active">Author, Date:</td>
                            <td><%
                                out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleAuthor).get(0) + ",");
                                out.print(cdxDoc.getAuthoringTimeAsString());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Status:</td>
                            <td><%
                                out.print(cdxDoc.getStatusCode());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Custodian:</td>
                            <td><%
                                out.print(cdxDoc.getCustodian());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Device, Time:</td>
                            <td><%
                              out.print(cdxDoc.getDevice() + ", ");
                              out.print(cdxDoc.getEffectiveTimeAsString());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Parent document:</td>
                            <td><%
                              out.print(cdxDoc.getParentDocId());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Procedure, Date:</td>
                            <td><%
                                out.print(cdxDoc.getProcedureName() + ", ");
                                out.print(cdxDoc.getObservationDateAsString());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Procedure Performer:</td>
                            <td><%
                                out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleProcedurePerformer).get(0));
                            %></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-4">
                    <h3 >
                        Patient: <%
                        out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.rolePatient).get(0));
                        %>
                    </h3>

                    <table class="table table-condensed">
                        <tbody>
                        <tr >
                            <td class="active col-md-1">Primary Recipient:</td>
                            <td class="col-md-3">
                                <%
                                    out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.rolePrimaryRecipient).get(0));
                                %></td>
                        </tr>
                        <tr>
                            <td class="active">Secondary Recipients:</td>
                            <td>
                                <%
                                    for (String p : cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleSecondaryRecipient)) {
                                        out.println(p);
                                    }
                                %>
                            </td>
                        </tr>
                        <tr>
                            <td class="active">Ordering Provider:</td>
                            <td><%
                                out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleOrderingProvider).get(0));
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Family Provider:</td>
                            <td><%
                                out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleFamilyProvider).get(0));
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Participating Providers:</td>
                            <td><%
                                List<String> names = cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleParticipatingProvider);
                                for (int i = 0; i < names.size(); i++) {
                                    out.print(names.get(i));
                                    if (i < names.size())
                                        out.print("<br>");
                                }
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Admission, Discharge:</td>
                            <td><%
                                out.print(cdxDoc.getAdmissionDateAsString() + ", ");
                                out.print(cdxDoc.getDischargeDateAsString());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Disposition:</td>
                            <td><%
                                out.print(cdxDoc.getDisposition());
                            %></td>
                        </tr>
                        <tr>
                            <td class="active">Attachments:</td>
                            <td><%
                                for (CdxAttachment a : cdxAttachmentDao.findByDocNo(cdxDoc.getId())) {
                                    out.print(a.getReference());
                                    out.println(" (" + a.getAttachmentType() + ")");
                                }
                            %></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="jumbotron">
                <p><%
                    out.print(cdxDoc.getContents());
                %>
                </p>

            </div>

        </div>
    </div>
</div>


</body>
</html>