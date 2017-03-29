<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the multi-site admin page --%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
	String provider_no=(String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.Site"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Clinic</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage Satellite Site Details</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
		
<nested:form action="/admin/ManageSites?method=add&function=provider&functionId=<%=provider_no %>">
<nested:submit style="border:1px solid #666666;">Add New Site</nested:submit>
</nested:form>

  <display-el:table name="sites" id="site" class="its" style="border:1px solid #666666; width:99%;margin-top:2px;">
     <display-el:column title="Active" ><c:choose ><c:when test="${site.status==0}">No</c:when><c:otherwise>Yes</c:otherwise></c:choose></display-el:column>
     <display-el:column title="Site Name">
     <a href="<%= request.getContextPath() %>/admin/ManageSites.do?method=update&function=provider&functionId=<%=provider_no %>&siteId=<c:out value='${site.siteId}'/>" ><c:out value="${site.name}" /></a></display-el:column>
     <display-el:column property="shortName" title="Short Name" />
     <display-el:column property="bgColor" title="Color" style="background-color:${site.bgColor}" />
     <display-el:column property="phone" title="Telephone" />
     <display-el:column property="fax" title="FAX" />
     <display-el:column property="address" title="Address" style="width: 200px;" />
     <display-el:column property="city" title="City" />
     <display-el:column property="province" title="Province" />
     <display-el:column property="postal" title="Postal Code" />
   <% if (org.oscarehr.common.IsPropertiesOn.isProviderFormalizeEnable()) { %>  
     <display-el:column property="providerIdFrom" title="ProviderID From" />
     <display-el:column property="providerIdTo" title="ProviderID To" />  
   <% } %>   
   <display-el:column title="SiteLogo">
   <a href="<%= request.getContextPath() %>/dms/ManageDocument.do?method=display&doc_no=<c:out value='${site.siteLogoId}'/>&providerNo=<%=provider_no%>"><c:out value='${site.siteLogoDesc}'/></a></display-el:column>
   <display-el:column property="siteUrl" title="Site URL" />
  </display-el:table>
		

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>

</html:html>
