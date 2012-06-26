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
<%@ page
	import="oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Invoice</title>
</head>
<body>
<table>
	<tr>
		<td>INVOICE - <%=DateUtils.sumDate("yyyy-MM-dd HH:mm","0") %></td>
		<td align="right"><%=DateUtils.sumDate("yyyy-MM-dd HH:mm","0") %></td>
	</tr>
</table>
</body>
</html>
