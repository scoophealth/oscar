<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>


<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("logout.jsp");%>
</security:oscarSec>

<html:html locale="true">
    <head>
        <meta http-equiv="Cache-Control" content="no-cache" />
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