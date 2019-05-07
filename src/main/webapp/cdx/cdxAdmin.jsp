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

//    up = userPropertyDao.getProp("cdx_default_provider");
//    if (up != null) defaultProvider = up.getValue();

//    up = userPropertyDao.getProp("cdx_privateKey");
//    if (up != null) privateKey = up.getValue();
//
//    up = userPropertyDao.getProp("cdx_decryptionKey");
//    if (up != null) decryptionKey = up.getValue();

//    MiscUtils.getLogger().info("defaultProvider: " + defaultProvider);
    MiscUtils.getLogger().info("cdxOid: " + cdxOid);
    MiscUtils.getLogger().info("pollInterval: " + pollInterval);

    SearchDoc docSearcher = new SearchDoc(cdxConfiguration);
    ReceiveDoc receiveDoc = new ReceiveDoc(cdxConfiguration);

%>
<html>
<head>



    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>








    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.js">   </script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js">   </script>



    <script>

        function runFetch() {
            window.location = "<%=request.getContextPath() %>/cdx/cdxImportNewDocs.jsp";
        }
        function runFetchOld(rows) {

            var paramStr="";

            var i;

            for (i = 0; i<rows.length; i++) {
                paramStr = paramStr + "docid=" + rows[i][2];
                if (i < rows.length +1)
                    paramStr = paramStr + "&";
            }

            window.location.assign("<%=request.getContextPath() %>/cdx/cdxImportOldDocs.jsp?"
                + paramStr);

        }


        $(document).ready(function() {
            var table = $('#doctable').DataTable(
                {
                    "order": [[ 0, "desc" ]]
                }

            );

            $('#doctable tbody').on( 'click', 'tr', function () {
                $(this).toggleClass('selected');
                if (table.rows('.selected').data().length >0 )
                    $('#downButton').show();
                else $('#downButton').hide();
            } );

            $('#downButton').click( function() {
                runFetchOld(table.rows('.selected').data());
            });
            $('#newButton').click( function() {
                runFetch();
            });
        } );
    </script>

    <title>CDX Configuration</title>


</head>
<body>
<h4>CDX Configuration</h4>
<form action="<%=request.getContextPath()%>/cdx/CDXAdmin.do" method="post">
    <div class="control-group">
        <label class="control-label">CDX URL: Format is http://&lt;ip_address&gt;:&lt;port&gt;</label>
        <div class="controls">
            <label>
                <input type="text" name="cdx_url" value="<%=cdxUrl%>"/>
            </label>
        </div>
    </div>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label">Default Provider:</label>--%>
    <%--        <div class="controls">--%>
    <%--            <select name="defaultProvider">--%>
    <%--                <%--%>
    <%--                    for (Provider provider : providers) {--%>
    <%--                        String selected = "";--%>
    <%--                        if (!defaultProvider.equals("")--%>
    <%--                                && defaultProvider.equals(provider.getProviderNo())) {--%>
    <%--                            selected = " selected=\"selected\" ";--%>
    <%--                        }--%>
    <%--                %>--%>
    <%--                <option value="<%=provider.getProviderNo()%>" <%=selected%>><%=provider.getFormattedName()%>--%>
    <%--                </option>--%>
    <%--                <%--%>
    <%--                    }--%>
    <%--                %>--%>
    <%--            </select>--%>
    <%--        </div>--%>
    <%--    </div>--%>

    <div class="control-group">
        <label class="control-label">Clinic OID:</label>
        <div class="controls">
            <label>
                <input type="text" name="cdxOid" value="<%=cdxOid%>"/>
            </label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">Automated Polling:</label>
        <label class="radio-inline"><input type="radio" name="cdx_polling_enabled" value="true" <%=pollEnabled%>
                                           class="form-control">Enabled</label>
        <label class="radio-inline"><input type="radio" name="cdx_polling_enabled" <%=pollDisabled%> value="false"
                                           class="form-control">Disabled</label>
    </div>
    <div class="control-group">
        <label class="control-label">Polling Interval (minutes):</label>
        <div class="controls">
            <label>
                <input type="text" name="cdx_polling_interval" value="<%=pollInterval %>"/>
            </label>
        </div>
    </div>
    <div class="control-group">
        <input type="submit" class="btn btn-primary" value="Submit"/>
    </div>
</form>

<hr>
<div>
    <h4>CDX Status</h4>
    <%
        CdxPendingDocsDao cdxPendingDocsDao = SpringUtils.getBean(CdxPendingDocsDao.class);
        SimpleDateFormat format=new SimpleDateFormat ("yyyy-MM-dd HH:mm");

        List<String> newDocs = null;

        try {
            newDocs=receiveDoc.pollNewDocIDs();
        } catch (Exception e) {
            MiscUtils.getLogger().info("OBIB pollNewDocIDs failed", e);
        }

        if (newDocs == null) {
    %>
    <p style="color:#FF0000";>The OBIB is <strong>not</strong> connected</p>
    <% } else { %>

    <p style="color:#229B36";>The OBIB is connected</p>

    <p>There are <%=newDocs.size()%> new documents waiting.
        <input id="newButton" type="button" class="btn" value="Import New Documents"
                <%=(newDocs.size()==0 ? "disabled" : "")%>/></p>

    <p>There are <%=cdxPendingDocsDao.getPendingErrorDocs().size()%> messages that could not be imported due to an internal error.</p>

    <p>There are <%=cdxPendingDocsDao.getDeletedDocs().size()%> documents that were deleted by a user. (They can be re-imported.) </p>

    <%


        List<IDocument> availableDocs = null;
        try {
            availableDocs=docSearcher.searchDocumentsByClinic(cdxConfiguration.getClinicId());
        } catch (Exception e) {
            MiscUtils.getLogger().info("OBIB searchDocumentsByClinic failed", e);
        }

        if (availableDocs == null) {
    %>
    <p style="color:#FF0000";>Search Documents by Clinic ID failed </p>
    <% } else {

        CdxProvenanceDao provDao = SpringUtils.getBean(CdxProvenanceDao.class);
        List<IDocument> notImportedDocs = new ArrayList<IDocument>();

        for (IDocument d : availableDocs) {
            if (provDao.findByMsgId(d.getDocumentID()).isEmpty())
                notImportedDocs.add(d);
        }

    %>

    <p>There is a total of <%=notImportedDocs.size()%> documents retrievable from the CDX system for this clinic, which are not imported in the EMR (or were deleted after import).</p>

    <%
        if (!notImportedDocs.isEmpty()){
    %>

    <p> Select all documents you want to (re)import below <button id="downButton" type="button" hidden="hidden" >Import selected documents </button> </p>
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

                        if (pd.getReasonCode().equals(CdxPendingDoc.error))
                            msg = "unsuccessful import";
                        else msg = "deleted after import";

                        out.print(msg + "\n at " + pd.getTimestamp());
                    }
                    else out.print("never imported");
                %>
            </td>

        </tr>
            <%
                }
            %>
        </tfoot>
    </table>

    <%}%>

    <%}%>

    <%}%>
</div>
</body>
</html>
