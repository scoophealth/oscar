
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



<%@ page errorPage="error.jsp"%>
<%@ include file="/survey/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Oscar Forms</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>

<table border="0" cellspacing="0" cellpadding="18" width="100%">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" bundle="survey">
			<tr>
				<td colspan="3" class="message"><c:out value="${message}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>
	<logic:messagesPresent>
		<html:messages id="error" bundle="survey">
			<tr>
				<td colspan="3" class="error"><c:out value="${error}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>
	<tr>
		<td><a href="javascript:history.go(-1);">back</a></td>
	</tr>
</table>

</body>
</html:html>
