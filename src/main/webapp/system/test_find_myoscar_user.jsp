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
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.AccountManager"%>
<html>
    <head>
        <title>test</title>
    </head>
    <body>
    	<h3>test find PHR user</h3>
    	<br />
		(you must already be logged into PHR for this page to work)
    	<br />
    	<form action="test_find_myoscar_user.jsp">
	    	userName : <input type="text" name="userName" />
	    	<br />
	    	<input type="submit" />
	    </form>
	    
	    <br />
	    
	    <%
	    	String userName=request.getParameter("userName");
	    	if (userName!=null)
	    	{
	    		MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
	    		Long userId=AccountManager.getUserId(myOscarLoggedInInfo, userName);
	    		
	    		%>
	    			userId = <%=userId %>
	    		<%
	    	}
	    %>
    </body>
</html>