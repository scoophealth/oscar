
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Provider Search</title>
<link href="<html:rewrite page='/css/tigris.css'/>" rel="stylesheet"
	type="text/css" />
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet"
	type="text/css" />
</head>

<script type="text/javascript">
		function selectProvider(id,name) {
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementName")%>'].value=name;
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=id;
			self.close();
		}
	</script>

<body marginwidth="0" marginheight="0">
<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Search Results</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="provider" name="providers" export="false" pagesize="10"
	requestURI="/PMmodule/ProviderSearch.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:column sortable="true" title="Name">
		<a href="#javascript:void(0);"
			onclick="selectProvider('<c:out value="${provider.providerNo}"/>','<c:out value="${provider.formattedName}"/>');">
		<c:out value="${provider.providerNo}" /> </a>
	</display:column>
	<display:column property="lastName" sortable="true" title="Last Name" />
	<display:column property="firstName" sortable="true" title="First Name" />
	<display:column property="providerType" sortable="true" title="Type" />
</display:table>
</body>
</html:html>
