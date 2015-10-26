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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.oscarehr.util.*"%>
<%@page import="java.text.*"%>
<%@page import="org.oscarehr.common.model.CaisiForm"%>
<%@page import="org.oscarehr.common.dao.CaisiFormDao"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.survey.service.OscarFormManager" %>

<%
	Long formId=Long.parseLong(request.getParameter("formId"));
	String startDateString=request.getParameter("startDate");
	String endDateString=request.getParameter("endDate");
	if(startDateString==null || "".equals(startDateString))
		startDateString = "1900-01-01";
	if(endDateString==null || "".equals(endDateString)) 
		endDateString = "9998-12-31";
	SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd");
	Date startDate=null;
	Date endDate=null;
	try
	{
		startDate=dateFormatter.parse(startDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	try
	{
		endDate=dateFormatter.parse(endDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	
	CaisiFormDao caisiFormDao = SpringUtils.getBean(CaisiFormDao.class);	
	
	OscarFormManager oscarFormManager = (OscarFormManager)SpringUtils.getBean("oscarFormManager");
	
	Map data = oscarFormManager.getFormReport(formId.intValue(),startDate,endDate);
	CaisiForm form = caisiFormDao.find(formId.intValue());	
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h1>User Created Report : <%=form.getDescription()%> from <%=startDateString%>
to <%=endDateString%></h1>

<input type="button" value="Back"
	onclick="document.location='<%=request.getContextPath()%>/PMmodule/ProviderInfo.do'" />

<table class="genericTable">
	<tr class="genericTableRow">
		<td class="genericTableHeader">Questions</td>
		<td class="genericTableHeader">Answer</td>
		<td class="genericTableHeader">Total</td>
	</tr>
	<% 
		Iterator keyValues = data.entrySet().iterator();
		for(int i=0; i<data.size();i++) {
			Map.Entry entry = (Map.Entry)keyValues.next();
			String[] questionAnswer = (String[])entry.getKey();
			String count = (String)entry.getValue();
			String question = questionAnswer[0];
			String answer = questionAnswer[1];
		
		/*
		for (Map.Entry<String, String[]> dd : data.entrySet().iterator())
		{			
			String question = dd.getKey();
			String[] values = dd.getValue();
			String answer = values[0];
			String count = values[1];	
			*/
	%>
	<tr class="genericTableRow">
		<td class="genericTableHeader"><%=question%></td>
		<td class="genericTableData"><%=answer%></td>

		<td class="genericTableData"><%=count%></td>

	</tr>
	<%
	}
	%>

</table>

<input type="button" value="Back"
	onclick="document.location='<%=request.getContextPath()%>/PMmodule/ProviderInfo.do'" />


<%@include file="/layouts/caisi_html_bottom.jspf"%>
