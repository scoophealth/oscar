<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
</security:oscarSec>

<%
if(!authed) {
	return;
}
%>
<html:html locale="true">
    <head>
        <title>Facilities</title>
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/tigris.css" />' />
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/displaytag.css" />' />

        <script>
            function ConfirmDelete(name)
            {
                if(confirm("Are you sure you want to delete " + name + " ?")) {
                    return true;
                }
                return false;
            }
        </script>
    </head>
    <body>
        <h1>Facilities</h1>
        <html:form action="/FacilityManager.do">
            <display:table class="simple" cellspacing="2" cellpadding="3"
                id="facility" name="facilities" export="false" pagesize="0"
                requestURI="/FacilityManager.do">
                <display:setProperty name="paging.banner.placement" value="bottom" />
                <display:setProperty name="paging.banner.item_name" value="agency" />
                <display:setProperty name="paging.banner.items_name" value="facilities" />
                <display:setProperty name="basic.msg.empty_list" value="No facilities found." />

                <display:column property="name" sortable="true" title="Name" />
                <display:column property="description" sortable="true" title="Description" />
                
                <display:column sortable="false" title="">
                    <a href="<html:rewrite action="/FacilityManager.do"/>?method=edit&id=<c:out value="${facility.id}" />">
                    Edit </a>
                </display:column>
<!--
                < isplay:column sortable="false" title="">
                    <a href="< tml:rewrite action="/FacilityManager.do"/>?method=delete&id=< :out value="${facility.id}"/>&name=< :out value="${facility.name}"/>"
                       onclick="return ConfirmDelete('< :out value="${facility.name}"/>')">
                    Delete </a>
                </ isplay:column>
-->
            </display:table>
        </html:form>
<!--
        <p><a href="< tml:rewrite action="/FacilityManager.do"/>?method=add">
        Add new facility </a></p>
-->
    </body>
</html:html>
