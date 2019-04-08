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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) return;
%>

<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.ws.rest.util.QuestimedUtil" %>

<%
	String username = request.getParameter("serviceUsername");
	String password = request.getParameter("servicePassword");
	String location = request.getParameter("serviceLocation");
	
	boolean saved = false;
        Boolean connectionSuccess=false;
	if (username!=null) {
		saved = true;
		QuestimedUtil.saveServiceUsername(username);
	}
	else username = QuestimedUtil.getServiceUserName();
	if (password!=null) QuestimedUtil.saveServicePassword(password);
	else password = QuestimedUtil.getServicePassword();
	if (location!=null) QuestimedUtil.saveServiceLocation(location);
	else location = QuestimedUtil.getServiceLocation();
	
        if(username!=null && password!=null && location!=null) {
            connectionSuccess=QuestimedUtil.testServiceConnection();
        }
	if (username==null) username=new String();
	if (password==null) password=new String();
	if (location==null) location=new String();
%>

<html:html locale="true">
<head>
	<title>Questimed Config</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Questimed Configuration</h4>
	<form method="post">
	<fieldset>
		<div class="control-group">
			<label class="control-label">Service Username</label>
			<div class="controls">
				<input name="serviceUsername" type="text" value="<%=username%>"/><br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Service Password</label>
			<div class="controls">
				<input name="servicePassword" type="password" value="<%=password%>"/><br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Service Location (URL)</label>
			<div class="controls">
				<input name="serviceLocation" type="text" style="width:450px;" value="<%=location%>"/><br/>
			</div>
		</div>

		<div class="control-group">
                    <%=connectionSuccess==null?"":connectionSuccess?"Connection successful!" : "Connection failed!"%><br>
			<input type="submit" class="btn btn-primary" value="Save"/>
			<%=saved ? "Saved!" : ""%>
		</div>
	</fieldset>
	</form>
</html:html>
