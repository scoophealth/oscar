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

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="oscar.OscarProperties"%>
<%
	String myOscarExternalLinkUrl = OscarProperties.getInstance().getProperty("myOSCAR.url")+"external_page_view_action.jsp";
	String redirectPage=request.getParameter("redirectPage");
   	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
%>
<%@include file="/layouts/html_top.jspf" %>
	
	<form name="theForm" method="post" action="<%=myOscarExternalLinkUrl%>">
		<input type="hidden" name="userId" value="<%=myOscarLoggedInInfo.getLoggedInPersonId()%>" />
		<input type="hidden" name="password" value="<%=StringEscapeUtils.escapeHtml(myOscarLoggedInInfo.getLoggedInPersonSecurityToken())%>" />
		<input type="hidden" name="redirectPage" value="<%=StringEscapeUtils.escapeHtml(redirectPage)%>" />
		<script type="text/javascript">
			document.theForm.submit();
		</script>
		<input type="submit" value="Click here if you are not automatically redirected." />
	</form>

<%@include file="/layouts/html_bottom.jspf" %>
