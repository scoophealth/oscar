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
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MessageWs"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginResultTransfer3"%>
<%@page import="org.oscarehr.myoscar_server.myoscar_server_client_stubs2.LoginWs"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarServerWebServicesManager"%>
<html>
	<head></head>
	<body>
		This page tests the new embeded stub2's custom made for oscar's migration to newer stubs.
		<form method="post">
			user name : <input type="text" name="userName" />
			<br />
			password : <input type="password" name="password" />
			<br />
			<input type="submit" />
		</form>
		<hr />
		<%
			String userName=StringUtils.trimToNull(request.getParameter("userName"));
			String password=request.getParameter("password");
			
			if (userName!=null)
			{
				LoginWs loginWs=MyOscarServerWebServicesManager.getLoginWs(MyOscarLoggedInInfo.getMyOscarServerBaseUrl());
				try
				{
					LoginResultTransfer3 loginResult = loginWs.login4(userName, password);
					%>
						Login success userId : <%=loginResult.getPerson().getId()%>
						<br />
					<%
	
					MyOscarLoggedInInfo myOscarLoggedInInfo=new MyOscarLoggedInInfo(loginResult.getPerson().getId(), loginResult.getSecurityTokenKey(), request.getSession().getId(), request.getLocale());
					MyOscarLoggedInInfo.setLoggedInInfo(request.getSession(), myOscarLoggedInInfo);
					
				   	MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(myOscarLoggedInInfo);
				   	
				   	// THIS LINE will bomb with incorrect cipher/bouncy castle provider 
				   	int count=messageWs.getUnreadMessageCount(myOscarLoggedInInfo.getLoggedInPersonId());
					%>
						unread msg : <%=count%>
					<%
					
				}
				catch (Exception e)
				{
					%>
						error : <%=e.getMessage()%>
					<%
					MiscUtils.getLogger().error("Error logging into myoscar", e);
				}
			}
		%> 
	</body>	
</html>