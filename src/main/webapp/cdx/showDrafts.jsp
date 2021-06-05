
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxMessenger" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxMessengerDao" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxProvenanceDao" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxProvenance" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
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

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="refresh" content="180">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Drafts</title>

    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">



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
        $(document).ready(function () {
            var table = $('#doctable').DataTable(
                {
                    "order": [[0, "desc"]]
                }
            );

            $('body').on('click', '#selectAll', function () {
                if ($(this).hasClass('allChecked')) {
                    $('input[type="checkbox"]', '#drafts').prop('checked', false);
                } else {
                    $('input[type="checkbox"]', '#draftsForm').prop('checked', true);
                }
                $(this).toggleClass('allChecked');
            });

        });

    </script>

        <%
            CdxMessengerDao cdxMessengerDao= SpringUtils.getBean(CdxMessengerDao.class);
            CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);

            String[] selected = request.getParameterValues("Selected");
            if (selected != null && selected.length > 0 ) {
                for (String id : selected) {
                    try {
                        cdxMessengerDao.deleteDraftById(Integer.parseInt(id));
                    } catch (Exception ex) {
                        MiscUtils.getLogger().error("Got exception deleting draft messenger " + ex.getMessage());
                    }
                }
            }

            List<CdxMessenger> cdxMessengers= cdxMessengerDao.findDraft();
        %>


        <style>
            .ovalbutton{
                border-radius: 22px;
                background-color: #E8E8E8;
                width: 120px;
            }

        </style>
    </head>
<body>
<form action="showDrafts.jsp" method="post" id="draftsForm">
<div class="container">
    <center><h4> <a href="../cdx/showDrafts.jsp" class="btn ovalbutton" style="text-align: center;" role="button">Drafts</a></h4>
    </center>
    <table id="doctable" class="display" style="width:100%">
        <thead>
        <tr>
            <th>Select</th>
            <th>Edit</th>
            <th>Author</th>
            <th>Patient</th>
            <th>Recipients</th>
            <th>Document Type</th>
            <th>Category</th>
            <th>Content</th>
            <th>Time</th>
        </tr>
        </thead>
        <tbody>
        <%

            if(cdxMessengers!=null && !cdxMessengers.isEmpty())
            {

                for(CdxMessenger n: cdxMessengers)
                {
                    CdxProvenance dkind=null;
                    CdxProvenance d= provenanceDao.findByDocumentIdAndAction(n.getDocumentId(),"SEND");
                    if(!n.getCategory().equalsIgnoreCase("New")){
                        dkind=provenanceDao.getCdxProvenance(Integer.parseInt(n.getCategory().split(":")[1]));
                    }

        %>
        <tr>
            <td>
                <input type=checkbox name="Selected" value=<%=n.getId()%>>
            </td>
            <td>
                <a href="../cdx/cdxMessenger.jsp?draftId=<%=n.getId()%>" target="_blank" class="btn btn-primary" role="button" title="Edit Draft">
                    Edit</a>
            </td>
            <td ><%=n.getAuthor()%>
            </td>

            <td> <%=n.getPatient()%>
            </td>
            <td> <%=n.getRecipients()%>
            </td>
            <td> <%=n.getDocumentType()%>
            </td>
            <td><% if (n.getCategory() != null && !n.getCategory().equalsIgnoreCase("New")) { %>
                <span><%=n.getCategory().split(":")[0]%> </span>
                <a href="../dms/showCdxDocumentArchive.jsp?ID=<%=n.getCategory().split(":")[1]%>" target="_blank" title="Document Details">
                    <%=dkind.getKind()%></a>
                <% } else { %>
                <%=n.getCategory()%>
                <% } %>
            </td>
            <td> <%=n.getContent()%>
            </td>
            <td> <%=n.getTimeStamp()%>
            </td>
        </tr>
        <%
                }
            }

        %>
        </tbody>
    </table>
    <div class="container" style="text-align: center" >
        <input type="submit" class="btn btn-danger" value="Delete Selected" >
        <input type="button" class="btn btn-info" value="Select all" id="selectAll" >
    </div>
</div>
</form>
</body>
</html>
