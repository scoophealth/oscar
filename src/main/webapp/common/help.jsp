
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


<% 
	String topic = request.getParameter("topic"); 
%>

<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>CAISI Help</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<c:choose>
	<c:when test="${param.topic == null}">
				No Topic Chosen
			</c:when>
	<c:otherwise>
		<h3><c:out value="${param.topic}" /></h3>
		<br />
		<bean:message key="<%=topic%>" bundle="help" />
	</c:otherwise>
</c:choose>
<br />
<center><input type="button" value="Close"
	onclick="self.close();" /></center>
</body>
</html:html>
