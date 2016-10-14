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
<%@ page isErrorPage="true"%><!-- only true can access exception object -->

<%@ page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">
<%@ page import="org.apache.cxf.interceptor.Fault"%>
<%@ page import="java.lang.RuntimeException"%>
<%@ page import="org.oscarehr.common.exception.NotFoundException"%>
<%@ page import="org.oscarehr.common.exception.ValidationException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.lang.Integer"%>

	<body>
		<h1><bean:message key="error.description" /></h1>
		<hr>
		<p>
		<center>Something is wrong with your input. <br>
		<a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a> &amp; Try it again</center>
		<p>
		<h2>
			<bean:message key="error.msgException" />:<br>
			<font color=red>
				<% out.println("Exception:" + exception);%><br>
				<% out.println("Exception Message:" + exception.getMessage());%><br>
			</font>
		</h2>
	</body>
</html:html>
<%MiscUtils.getLogger().error("Error", exception);%>
