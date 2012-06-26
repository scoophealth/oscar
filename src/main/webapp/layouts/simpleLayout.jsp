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

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><tiles:getAsString name="title" /></title>
<tiles:get name="meta" />
<tiles:get name="stylesheet" />
<tiles:get name="javascript" />
</head>
<body bgcolor="#FFFFFF" text="#000000" onLoad="BodyOnLoad();"
	onClick="BodyOnClick();">
<table border="0" cellpadding="0" cellspacing="0" width="100%"
	height="5%">
	<tr>
		<td align="center" valign="top" height="1%"><tiles:get
			name="header" /></td>
	</tr>
</table>
<br />
<tiles:get name="body" />
<br />
<table border="0" cellpadding="0" cellspacing="0" width="100%"
	height="5%">
	<tr>
		<td align="center" valign="bottom" height="1%"><tiles:get
			name="footer" /></td>
	</tr>
</table>
</body>
</html:html>
