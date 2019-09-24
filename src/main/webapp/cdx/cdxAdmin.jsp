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
<%@ page import="java.util.List" %>
<%@ page import="ca.uvic.leadlab.obibconnector.impl.receive.SearchDoc" %>
<%@ page import="ca.uvic.leadlab.obibconnector.impl.receive.ReceiveDoc" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDocument" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxPendingDoc" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxProvenanceDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxPendingDocsDao" %>
<%@ page import="java.text.SimpleDateFormat" %>

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
    ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);
//    ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
    UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

    //List<Provider> providers = providerDao.getActiveProviders();

//    String defaultProvider = "";
    String cdxOid = "";
    String pollEnabled;
    String pollDisabled;
    String pollInterval;
    String cdxUrl = "http://127.0.0.1:8081";
    //String privateKey = "";
    //String decryptionKey = "";

    Clinic clinic = clinicDAO.getClinic();
    if (clinic != null && clinic.getCdxOid() != null) cdxOid = clinic.getCdxOid();

    CDXConfiguration cdxConfiguration = new CDXConfiguration();
    if (cdxConfiguration.isPollingEnabled()) {
        pollEnabled = "checked";
        pollDisabled = "unchecked";
    } else {
        pollEnabled = "unchecked";
        pollDisabled = "checked";
    }
    pollInterval = cdxConfiguration.getPollingInterval();

    UserProperty up;

    up = userPropertyDao.getProp("cdx_url");
    if (up != null) cdxUrl = up.getValue();


    MiscUtils.getLogger().info("cdxOid: " + cdxOid);
    MiscUtils.getLogger().info("pollInterval: " + pollInterval);

    SearchDoc docSearcher = new SearchDoc(cdxConfiguration);
    ReceiveDoc receiveDoc = new ReceiveDoc(cdxConfiguration);

%>
<html>
<head>


    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>


    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">


    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>


    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>


    <script>

        function runFetch() {
            window.location = "<%=request.getContextPath() %>/cdx/cdxImportNewDocs.jsp";
        }

        function runFetchOld(rows) {

            var paramStr = "";

            var i;

            for (i = 0; i < rows.length; i++) {
                paramStr = paramStr + "docid=" + rows[i][2];
                if (i < rows.length + 1)
                    paramStr = paramStr + "&";
            }

            window.location.assign("<%=request.getContextPath() %>/cdx/cdxImportOldDocs.jsp?"
                + paramStr);

        }


        $(document).ready(function () {
            var table = $('#doctable').DataTable(
                {
                    "order": [[0, "desc"]]
                }
            );

            $('#doctable tbody').on('click', 'tr', function () {
                $(this).toggleClass('selected');
                if (table.rows('.selected').data().length > 0)
                    $('#downButton').show();
                else $('#downButton').hide();
            });

            $('#downButton').click(function () {
                var $this = $(this);
                $this.button('loading');
                runFetchOld(table.rows('.selected').data());
            });

            $('#selAll').click(function () {
                $('#doctable tbody tr').toggleClass('selected');
                if (table.rows('.selected').data().length > 0)
                    $('#downButton').show();
                else $('#downButton').hide();
            });

            $('#newButton').click(function () {
                runFetch();
            });
            $('#saveBtn').on('click', function () {
                var $this = $(this);
                $this.button('loading');
                setTimeout(function () {
                    $this.button('reset');
                }, 8000);
            });
        });


    </script>

    <title>CDX Configuration</title>


</head>
<body>

<div class="panel panel-default">
    <div class="panel-heading">


        <h3 class="panel-title">CDX Configuration 		<button type="button" class="btn btn-small" aria-label="CDX help" onClick="window.open('https://simbioses.github.io/cdxuserman/006_for_users/018_administration/cdx_03/');">
            <span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
        </button></h3>
    </div>

</div>
<div class="panel-body">
    <form action="<%=request.getContextPath()%>/cdx/CDXAdmin.do" method="post">

        <div class="form-group row">

            <div class="col-md-6">

                <label for="cdx_url">OBIB URL</label>
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1">http://&lt;ip_address&gt;:&lt;port&gt;</span>
                    <input type="text" class="form-control" id="cdx_url" name="cdx_url" value="<%=cdxUrl%>"
                           aria-describedby="basic-addon1">
                </div>
            </div>

            <div class="col-md-6">

                <label for="cdxOid">Clinic OID</label>
                <div class="input-group">
                    <input disabled type="text" class="form-control" id="cdxOid" name="cdxOid" value="<%=cdxOid%>">
                </div>

            </div>
        </div>


        <div class="form-group row">
            <div class="col-md-3">

                <div class="control-group">
                    <label class="control-label">Automatic Import:</label>
                    <div class="custom-control custom-radio custom-control-inline">
                        <input type="radio" class="custom-control-input" id="cdx_polling_enabled"
                               name="cdx_polling_enabled" value="true" <%=pollEnabled%>>
                        <label class="custom-control-label" for="cdx_polling_enabled">Enabled</label>
                    </div>

                    <div class="custom-control custom-radio custom-control-inline">
                        <input type="radio" class="custom-control-input" id="cdx_polling_disabled"
                               name="cdx_polling_enabled" <%=pollDisabled%> value="false">
                        <label class="custom-control-label" for="cdx_polling_disabled">Disabled</label>
                    </div>
                </div>
            </div>


            <div class="col-md-3">

                <label for="cdx_polling_interval">Polling Interval</label>
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon2">minutes</span>
                    <input type="text" class="form-control" id="cdx_polling_interval" name="cdx_polling_interval"
                           value="<%=pollInterval%>" aria-describedby="basic-addon2">
                </div>
            </div>

        </div>

        <div class="control-group">
            <button type="submit" id="saveBtn" class="btn btn-primary"
                    data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> Saving"> Save
            </button>
        </div>
    </form>
</div>
</div>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">CDX Status</h3>
    </div>
    <div class="panel-body">

        <%
            CdxPendingDocsDao cdxPendingDocsDao = SpringUtils.getBean(CdxPendingDocsDao.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            List<String> newDocs = null;

            try {
                newDocs = receiveDoc.pollNewDocIDs();
            } catch (Exception e) {
                MiscUtils.getLogger().info("OBIB pollNewDocIDs failed", e);
            }

            if (newDocs == null) {
        %>
        <div class="alert alert-danger" role="alert">The OBIB is not connected</div>
        <% } else { %>

        <div class="alert alert-success" role="alert">The OBIB is connected</div>

        There are <%=newDocs.size()%> new documents waiting.
        <% if (newDocs.size() > 0) { %>
        <button id="newButton" class="btn btn-default btn-xs" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> Importing">Import</button>
        <%}%>

        <%
            int noOfErrDocs = cdxPendingDocsDao.getPendingErrorDocs().size();
            if (noOfErrDocs > 0) {
        %>

        <div class="alert alert-danger" role="alert">There are <%=noOfErrDocs%> messages that could not be imported due
            to an internal error.
        </div>
        <%
            }
            int noOfDelDocs = cdxPendingDocsDao.getDeletedDocs().size();
            if (noOfDelDocs > 0) {
        %>

        <div class="alert alert-warning" role="alert">There are <%=noOfDelDocs%> documents that were deleted by a user
            (after import)
        </div>


        <%
            }


            List<IDocument> availableDocs = null;
            try {
                availableDocs = docSearcher.searchDocuments();
            } catch (Exception e) {
                MiscUtils.getLogger().info("OBIB searchDocumentsByClinic failed", e);
            }

            if (availableDocs == null) {
        %>
        <p style="color:#FF0000" ;>Search Documents by Clinic ID failed </p>
        <% } else {

            CdxProvenanceDao provDao = SpringUtils.getBean(CdxProvenanceDao.class);
            List<IDocument> notImportedDocs = new ArrayList<IDocument>();

            for (IDocument d : availableDocs) {
                if (provDao.findByMsgId(d.getDocumentID()).isEmpty()) notImportedDocs.add(d);
            }

        %>

        <p>There is a total of <%=notImportedDocs.size()%> documents retrievable from the CDX system for this clinic,
            which are not in the EMR database.</p>

        <%
            if (!notImportedDocs.isEmpty()) {
        %>

        <div class="panel panel-default">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-6">
                        <h5 class="panel-title">Select the documents you want to (re)import (click on rows)</h5>
                    </div>
                    <div class="col-md-6">
                        <button id="selAll" class="btn btn-default btn-xs" type="button" >Toggle Selection
                        </button>
                        <button id="downButton" class="btn btn-primary btn-xs" type="button" style="display:none" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> Importing">Import
                            selected documents
                        </button>
                    </div>
                </div>
            </div>
            <div class="panel-body">


                <table id="doctable" class="display" style="width:100%">
                    <thead>
                    <tr>
                        <th>Time</th>
                        <th>Title</th>
                        <th>Message ID</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>

                        <%
                for (IDocument d : notImportedDocs) {
                    %>
                    <tr>
                        <td>
                            <%=format.format(d.getEffectiveTime())%>
                        </td>
                        <td>
                            <%=d.getLoincCodeDisplayName()%>
                        </td>
                        <td>
                            <%=d.getDocumentID()%>
                        </td>
                        <td>
                            <%
                                List<CdxPendingDoc> pendingDocs = cdxPendingDocsDao.findPendingDocs(d.getDocumentID());
                                if (!pendingDocs.isEmpty()) {
                                    CdxPendingDoc pd = pendingDocs.get(0);
                                    String msg = "";

                                    if (pd.getReasonCode().equals(CdxPendingDoc.error)) msg = "unsuccessful import";
                                    else msg = "deleted after import";

                                    out.print(msg + "\n at " + pd.getTimestamp());
                                } else if (newDocs.contains(d.getDocumentID())) out.print("new document");
                                else out.print("not in EMR");
                            %>
                        </td>

                    </tr>
                        <%
                }
            %>
                    </tfoot>
                </table>
            </div>

        </div>

        <%}%>

        <%}%>

        <%}%>
    </div>
</div>
</body>
</html>
