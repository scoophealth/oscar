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
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.Date"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.common.model.CdsClientForm"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4Action"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());

	// for these values get them and pop them from map so subsequent iterating through map doesn't process these parameters again.
	Integer admissionId=Integer.valueOf(parameters.get("admissionId")[0]);	
	parameters.remove("admissionId");

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");

	Date initialContactDate=null;
	try
	{
		initialContactDate=DateUtils.toDate(parameters.get("initialContactDate")[0]);
	}
	catch(Exception e)
	{
		// do nothing, no date specified
	}
	parameters.remove("initialContactDate");

	Date assessmentDate=null;
	try
	{
		assessmentDate=DateUtils.toDate(parameters.get("assessmentDate")[0]);
	}
	catch(Exception e)
	{
		// do nothing, no date specified
	}
	parameters.remove("assessmentDate");

	Date serviceInitiationDate=null;
	try
	{
		serviceInitiationDate=DateUtils.toDate(parameters.get("serviceInitiationDate")[0]);
	}
	catch(Exception e)
	{
		// do nothing, no date specified
	}
	parameters.remove("serviceInitiationDate");
	
	boolean signed=WebUtils.isChecked(request, "signed");	
	parameters.remove("signed");

	CdsClientForm cdsClientForm=CdsForm4Action.createCdsClientForm(loggedInInfo, admissionId, clientId, initialContactDate, assessmentDate, serviceInitiationDate, signed);
	
	for (Map.Entry<String, String[]> entry : parameters.entrySet())
	{
		if (entry.getValue()!=null)
		{
			for (String value : entry.getValue())
			{
				CdsForm4Action.addCdsClientFormData(cdsClientForm.getId(), entry.getKey(), value);				
			}
		}
	}
	
	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>
