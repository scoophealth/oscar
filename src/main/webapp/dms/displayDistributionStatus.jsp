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
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDocument" %>
<%@ page import="org.oscarehr.integration.cdx.CDXDistribution" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDistributionStatus" %>

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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Distribution Status</title>

    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">


    <script type="text/javascript">
        jQuery.noConflict();

    </script>

</head>
<body>

<div class="container">
<table class="table table-bordered">
    <h3>Distribution Status of the document:</h3>
    <thead>
    <tr>
        <th>Clinic Id</th>
        <th>Clinic Name</th>
        <th>Status Code</th>
        <th>Status Name</th>
        <th>Status Time</th>
    </tr>
    </thead>
    <tbody>
<%
    CDXDistribution cdxDistribution=new CDXDistribution();
    List<IDocument> doc=cdxDistribution.getDocumentDistributionStatus(request.getParameter("documentId"));
    if(doc!=null && !doc.isEmpty())
    {
        List<IDistributionStatus> distribution= doc.get(0).getDistributionStatus();
        if(distribution!=null && !distribution.isEmpty())
        {
            for(IDistributionStatus d: distribution)
            {
                %>
<tr >
   <td ><%=d.getReceivedOrganization().getID()%>
    </td>
   <td ><%=d.getReceivedOrganization().getName()%>
    </td>

    <td> <%=d.getStatusCode()%>
    </td>
    <td > <%=d.getStatusName()%>
    </td>

    <td><%=d.getStatusTime()%>
    </td>
</tr>
    <%

            }
        }
    }
    %>
    </tbody>
</table>
</div>
</body>
</html>
