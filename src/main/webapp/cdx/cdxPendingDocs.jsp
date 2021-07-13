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

<%@ page import="org.oscarehr.common.dao.ClinicDAO" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.Clinic" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.integration.cdx.CDXConfiguration" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.uvic.leadlab.obibconnector.impl.receive.SearchDoc" %>
<%@ page import="ca.uvic.leadlab.obibconnector.impl.receive.ReceiveDoc" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDocument" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxPendingDoc" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxProvenanceDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxPendingDocsDao" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    CdxPendingDocsDao cdxPendingDocsDao = SpringUtils.getBean(CdxPendingDocsDao.class);

    String reason = request.getParameter("reason");
    String title = "";
    List<CdxPendingDoc> pendingDocs = new ArrayList<CdxPendingDoc>();
    if ("ERR".equalsIgnoreCase(reason)) {
        pendingDocs = cdxPendingDocsDao.getPendingErrorDocs();
        title = "Errors";
    } else if ("DEL".equalsIgnoreCase(reason)) {
        pendingDocs = cdxPendingDocsDao.getDeletedDocs();
        title = "Deleted";
    }
%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />

    <!-- Optional theme -->
    <link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap-theme.min.css" />

    <!-- Latest compiled and minified JavaScript -->
    <script src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

    <script>
        $(document).ready(function () {
            var table = $('#doctable').DataTable(
                {
                    "order": [[0, "desc"]]
                }
            );
        });
    </script>

    <title>CDX Pending Documents - <%=title%></title>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading">
        <div class="row">
            <div class="col-md-6">
                <h5 class="panel-title">CDX Pending Documents - <%=title%></h5>
            </div>
        </div>
    </div>
    <div class="panel-body">

        <table id="doctable" class="display" style="width:100%">
            <thead>
            <tr>
                <th>Time</th>
                <th>Message ID</th>
                <th>Explanation</th>
            </tr>
            </thead>
            <tbody>
            <% for (CdxPendingDoc d : pendingDocs) { %>
                <tr>
                    <td>
                        <%=format.format(d.getTimestamp())%>
                    </td>
                    <td>
                        <%=d.getDocId()%>
                    </td>
                    <td>
                        <%
                            String explanation = d.getExplanation();
                            if (explanation != null && explanation.contains("Exception:")) {
                                int index = explanation.indexOf("Exception:") + 10;
                                explanation = explanation.substring(index);
                            }
                            out.print(explanation);
                        %>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
