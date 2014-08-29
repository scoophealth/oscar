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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.MessageManager"%>
<%
	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	if (myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn())
	{
		try
		{
			int count=MessageManager.getUnreadActiveMessageCount(myOscarLoggedInInfo, myOscarLoggedInInfo.getLoggedInPersonId());
			
			if (count>0)
			{
				%>
					<div style="color:white;vertical-align:top;font-size:smaller;background-color:red;border-radius:4px;padding-left:2px;padding-right:1px"><%=count%></div>
				<%
			}
		}
		catch (Exception e)
		{
			// we'll force a re-login if this ever fails for any reason what so ever.
			MyOscarUtils.attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously(loggedInInfo, true);
		}
	}
%>

