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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="packageNA.title" /></title>
</head>
<% String pkg = request.getParameter("pkg")==null?"":request.getParameter("pkg"); 
   String pkgname = pkg.compareTo("oscarRx")==0?"packageNA.msg.oscarRx":pkg.compareTo("oscarComm")==0?"packageNA.msg.oscarComm":"packageNA.msg.oscarComm"; %>
<body bgcolor="#FFFFFF" text="#000000">
<table width="100%" border="0" cellspacing="1" cellpadding="0"
	bgcolor="#000000">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="1" cellpadding="0"
			height="100%" bgcolor="#FFFFFF">
			<tr>
				<td colspan="3">
				<h2><bean:message key="packageNA.msgNA1" /> <%=pkg%> <bean:message
					key="packageNA.msgNA2" /></h2>
				</td>
			</tr>
			<tr>
				<td width="37%">&nbsp;</td>
				<td width="42%">
				<p><img src="images/OSCAR-LOGO.gif" width="523" height="325"></p>
				<p>&nbsp;</p>
				</td>
				<td width="21%">&nbsp;</td>
			</tr>
			<tr>
				<td width="37%">&nbsp;</td>
				<td width="42%"><font face="Tahoma" size="2"><bean:message
					key="<%=pkgname%>" /></font></td>
				<td width="21%">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html:html>
