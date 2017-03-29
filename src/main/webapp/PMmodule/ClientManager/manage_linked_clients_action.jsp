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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClientsAction"%><%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.oscarehr.common.model.FacilityDemographicPrimaryKey"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Facility currentFacility=(Facility)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	ManageLinkedClientsAction manageLinkedClientsAction=new ManageLinkedClientsAction();

	@SuppressWarnings("unchecked")
	Enumeration<String> e=request.getParameterNames();
	while (e.hasMoreElements())
	{
		try
		{
			String key=e.nextElement();
			// must check for "on" because some versions of IE submit "off" or ""
			if (key.startsWith("linked.") && "on".equals(request.getParameter(key))) manageLinkedClientsAction.addLinkedId(key);
		}
		catch (Exception ex)
		{
			MiscUtils.getLogger().error("Error", ex);
		}
	}

	Provider provider=(Provider)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	manageLinkedClientsAction.saveLinkedIds(loggedInInfo, currentFacility, provider, currentDemographicId);

	response.sendRedirect("../ClientManager.do?id="+currentDemographicId);
%>
