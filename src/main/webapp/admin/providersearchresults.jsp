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

<%@ page import="java.sql.*, java.util.*, oscar.*" buffer="none" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="*" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

	
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providersearchresults.title" /></title>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
	function setfocus() {
		document.searchprovider.keyword.focus();
		document.searchprovider.keyword.select();
	}
	function onsub() {
		var keyword = document.searchprovider.keyword.value;
		var keywordLowerCase = keyword.toLowerCase();
		document.searchprovider.keyword.value = keywordLowerCase;

	}
</script>
</head>

<%
		String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";

		//Defaults    		
		String strOffset = "0";
		String strLimit = "10";

		//OFFSET
		if (request.getParameter("limit1") != null)
			strOffset = request.getParameter("limit1");
		//LIMIT
		if (request.getParameter("limit2") != null)
			strLimit = request.getParameter("limit2");

		String keyword = request.getParameter("keyword");
		String orderBy = request.getParameter("orderby");
		String searchMode = request.getParameter("search_mode");
		if (searchMode == null)
			searchMode = "search_name";
		if (orderBy == null)
			orderBy = "last_name";
		String[] searchStatuses = request
				.getParameterValues("search_status");
		String searchStatus = null;

		if (searchStatuses != null) {
			if (searchStatuses.length == 1) {
				searchStatus = searchStatuses[0];
			}
		}
		
		int offset = Integer.parseInt(strOffset);
		int limit = Integer.parseInt(strLimit);
%>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><bean:message key="admin.providersearchresults.description" /></th>
	</tr>
</table>
<table cellspacing="0" cellpadding="0" width="100%" border="0" BGCOLOR="<%=weakcolor%>">
	<form method="post" action="providersearchresults.jsp" name="searchprovider" onsubmit="return onsub()">
	<tr valign="top">
		<td rowspan="2" align="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i><bean:message key="admin.search.formSearchCriteria" /></i></b></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" <%=searchMode.equals("search_name")?"checked":""%>
			name="search_mode" value="search_name"
			onclick="document.forms['searchprovider'].keyword.focus();">
			<bean:message key="admin.providersearch.formLastName" /></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio"	<%=searchMode.equals("search_providerno")?"checked":""%>
			name="search_mode" value="search_providerno"
			onclick="document.forms['searchprovider'].keyword.focus();">
			<bean:message key="admin.provider.formProviderNo" /></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="checkbox" name="search_status" value="1"
			<%=request.getParameter("search_status")!=null?(request.getParameter("search_status").equals("1")?"checked":""):""%>>
			<bean:message key="admin.providersearch.formActiveStatus" /><br />
		<input type="checkbox" name="search_status" value="0"
			<%=request.getParameter("search_status")!=null?(request.getParameter("search_status").equals("0")?"checked":""):""%>>
			<bean:message key="admin.providersearch.formInactiveStatus" /> </font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="last_name"> <INPUT

		<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
			TYPE="hidden" NAME="limit2" VALUE="10">  <INPUT
			TYPE="SUBMIT" NAME="button"
			VALUE=<bean:message key="admin.providersearchresults.btnSubmit"/>
			SIZE="17"></td>
	</tr>
	</form>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><i><bean:message key="admin.search.keywords" /></i> : <%=keyword%></td>
	</tr>
</table>

<CENTER>
<table width="100%" cellspacing="2" cellpadding="2" border="0"
	bgcolor="ivory">
	<tr bgcolor="<%=deepcolor%>">
		<TH align="center" width="10%"><b><a
			href="providersearchresults.jsp?keyword=<%=keyword%>&search_mode=<%=searchMode%>&orderby=provider_no&limit1=0&limit2=10">
			<bean:message key="admin.providersearchresults.ID" /></a></b></TH>
		<TH align="center" width="19%"><b><a
			href="providersearchresults.jsp?keyword=<%=keyword%>&search_mode=<%=searchMode%>&orderby=first_name&limit1=0&limit2=10">
			<bean:message key="admin.provider.formFirstName" /></a> </b></TH>
		<TH align="center" width="19%"><b><a
			href="providersearchresults.jsp?keyword=<%=keyword%>&search_mode=<%=searchMode%>&orderby=last_name&limit1=0&limit2=10">
			<bean:message key="admin.provider.formLastName" /></a></b></TH>
		<TH align="center" width="16%"><b>
			<bean:message key="admin.provider.formSpecialty" /></b></TH>
		<TH align="center" width="9%"><b>
			<bean:message key="admin.provider.formTeam" /></b></TH>
		<TH align="center" width="2%"><b>
			<bean:message key="admin.provider.formSex" /></B></TH>
		<TH align="center" width="15%"><b>
			<bean:message key="admin.providersearchresults.phone" /></B></TH>
		<TH align="center" width="15%"><b>
			<bean:message key="admin.provider.formStatus" /></B></TH>
	</tr>
<%
	List<ProviderData> providerList = null;
	ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
	
	if(searchMode.equals("search_name")) {
		providerList = providerDao.findByProviderName(keyword, searchStatus, limit, offset);
	}
	else if(searchMode.equals("search_providerno")) {
		providerList = providerDao.findByProviderNo(keyword, searchStatus, limit, offset);
	}
	
	if(orderBy.equals("last_name")) {
		Collections.sort(providerList, ProviderData.LastNameComparator);
	}
	else if(orderBy.equals("first_name")) {
		Collections.sort(providerList, ProviderData.FirstNameComparator);
	}
	else if(orderBy.equals("provider_no")) {
		Collections.sort(providerList, ProviderData.ProviderNoComparator);
	}
  
  boolean toggleLine=false;
  int nItems=0;
  
  if(providerList == null) {
    out.println("failed!!!");
  } 
  else {
    
	  for(ProviderData provider : providerList) {
		  toggleLine = !toggleLine;
		  nItems++;
%>

	<tr bgcolor="<%=toggleLine?"white":weakcolor%>">
		<td align="center"><a href='providerupdateprovider.jsp?keyword=<%=provider.getId()%>'><%= provider.getId() %></a></td>
		<td><%= provider.getFirstName() %></td>
		<td><%= provider.getLastName() %></td>
		<td><%= provider.getSpecialty() %></td>
		<td><%= provider.getTeam() %></td>
		<td align="center"><%= provider.getSex() %></td>
		<td><%= provider.getPhone() %></td>
		<td><%= provider.getStatus().equals("1")?"Active":"Inactive" %></td>
	</tr>
	<%
    }
  }
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  
  nNextPage=Integer.parseInt(strLimit)+Integer.parseInt(strOffset);
  nLastPage=Integer.parseInt(strOffset)-Integer.parseInt(strLimit);
  String searchStatusQ = (searchStatus!=null)?"&search_status="+searchStatus:"";
  if(nLastPage>=0) {
%> <a
	href="providersearchresults.jsp?keyword=<%= keyword %>&search_mode=<%= searchMode %><%= searchStatusQ %>&orderby=<%=orderBy%>&limit1=<%=nLastPage%>&limit2=<%=strLimit%>"><bean:message
	key="admin.providersearchresults.btnLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit)) {
%> <a
	href="providersearchresults.jsp?keyword=<%= keyword %>&search_mode=<%= searchMode %><%= searchStatusQ %>&orderby=<%= orderBy %>&limit1=<%=nNextPage%>&limit2=<%=strLimit%>"><bean:message
	key="admin.providersearchresults.btnNextPage" /></a> <%
}
%>
<p><bean:message key="admin.providersearchresults.msgClickForEditing" /></p>
</center>
</body>
</html:html>
