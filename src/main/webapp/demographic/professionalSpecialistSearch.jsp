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

<!--
/*
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 */
-->
<%@ page import="java.util.*,java.sql.*, java.net.*"%>
<%@ page import="org.oscarehr.common.web.ContactAction"%>
<%@ page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.commons.lang.WordUtils"%>

<%@ include file="/taglibs.jsp"%>

<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  int nItems = 0;
  Properties prop = null;
  String form = request.getParameter("form")==null?"":request.getParameter("form") ;
  String elementName = request.getParameter("elementName")==null?"":request.getParameter("elementName") ;
  String elementId = request.getParameter("elementId")==null?"":request.getParameter("elementId") ;
  String keyword = request.getParameter("keyword");

	if (request.getParameter("submit") != null 
		&& (request.getParameter("submit").equals("Search")
		|| request.getParameter("submit").equals("Next Page") 
		|| request.getParameter("submit").equals("Last Page")) ) {
			  
	  String search_mode = request.getParameter("search_mode")==null?"search_name":request.getParameter("search_mode");
	  String orderBy = request.getParameter("orderby")==null?"c.lastName,c.firstName":request.getParameter("orderby");
	  
	  List<ProfessionalSpecialist> contacts = ContactAction.searchProfessionalSpecialists(keyword);
	  nItems = contacts.size();
	  pageContext.setAttribute("contacts",contacts);
	}
	
	
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Search Professional Contacts</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">

<!--
		function setfocus() {
		  this.focus();
		  document.forms[0].keyword.focus();
		  document.forms[0].keyword.select();
		}
		function check() {
		  document.forms[0].submit.value="Search";
		  return true;
		}
		function selectResult(data1,data2) {			
			opener.document.<%=form%>.elements['<%=elementId%>'].value = data1;
			opener.document.<%=form%>.elements['<%=elementName%>'].value = data2;
			self.close();			
		}
		                
-->

      </script>
</head>
<body bgcolor="white" onload="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
	
<form method="post" name="titlesearch" action="professionalSpecialistSearch.jsp" onSubmit="return check();">
<table border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#CCCCFF">
	<tr>
		<td class="searchTitle" colspan="4">Search Professional Contacts</td>
	</tr>
	<tr>
		<td class="blueText" width="10%" nowrap>
			<input type="radio" name="search_mode" value="search_name" checked="checked"> Name
		</td>
		<td valign="middle" rowspan="2" align="left">
			<input type="text" name="keyword" value="" size="17" maxlength="100"> 
			<input type="hidden" name="orderby" value="c.lastName, c.firstName"> 
			<input type="hidden" name="limit1" value="0"> 
			<input type="hidden" name="limit2" value="10"> 
			<input type="hidden" name="submit" value='Search'> 
			<input type="submit" value='Search'>
		</td>
	</tr>	
</table>
<table width="95%" border="0">
	<tr>
		<td align="left">Results based on keyword(s): <%=keyword==null?"":keyword%></td>
	</tr>
</table>
<input type='hidden' name='form' value="<%=StringEscapeUtils.escapeHtml(form)%>"/>
<input type='hidden' name='elementName' value="<%=StringEscapeUtils.escapeHtml(elementName)%>"/>
<input type='hidden' name='elementId' value="<%=StringEscapeUtils.escapeHtml(elementId)%>"/>
</form>
	
<center>
<table width="100%" border="0" cellpadding="0" cellspacing="2"
	bgcolor="#C0C0C0">
	<tr class="title">
		<th width="25%"><b>Last Name</b></th>
		<th width="20%"><b>First Name</b></th>		
		<th width="20%"><b>Phone</b></th>
	</tr>
	
	<c:forEach var="contact" items="${contacts}" varStatus="i">
		<%
			ProfessionalSpecialist contact = (ProfessionalSpecialist)pageContext.getAttribute("contact");
			javax.servlet.jsp.jstl.core.LoopTagStatus i = (javax.servlet.jsp.jstl.core.LoopTagStatus) pageContext.getAttribute("i");
			String bgColor = i.getIndex()%2==0?"#EEEEFF":"ivory";	
			
			String strOnClick; 
            strOnClick = "selectResult('" + contact.getId() + "','"+StringEscapeUtils.escapeJavaScript(contact.getLastName()+ "," + contact.getFirstName()) + "')";
                        
		%>
		<tr align="center" bgcolor="<%=bgColor%>" align="center"
		onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';" onClick="<%=strOnClick%>">
			<td><c:out value="${contact.lastName}"/></td>
			<td><c:out value="${contact.firstName}"/></td>
			<td><c:out value="${contact.phoneNumber}"/></td>
		</tr>
	</c:forEach>
	
	
</table>

<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
%> <%
  if(nItems==0 && nLastPage<=0) {
%> <bean:message key="demographic.search.noResultsWereFound" /> <%
  }
%> <script language="JavaScript">
<!--
function last() {
  document.nextform.action="professionalSpecialistSearch.jsp?form=<%=URLEncoder.encode(form,"UTF-8")%>&elementName=<%=URLEncoder.encode(elementName,"UTF-8")%>&elementId=<%=URLEncoder.encode(elementId,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ; 
  document.nextform.submit();
}
function next() {
  document.nextform.action="professionalSpecialistSearch.jsp?form=<%=URLEncoder.encode(form,"UTF-8")%>&elementName=<%=URLEncoder.encode(elementName,"UTF-8")%>&elementId=<%=URLEncoder.encode(elementId,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ; 
  document.nextform.submit();
}
//-->
</SCRIPT>

<form method="post" name="nextform" action="searchRefDoc.jsp">
<%
  if(nLastPage>=0) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
	onClick="last()"> <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"
	onClick="next()"> <%
}
%>
</form>
<br>
<a href="<%=request.getContextPath() %>/oscarEncounter/oscarConsultationRequest/config/ShowAllServices.jsp">Add/Edit Professional Specialist</a></center>
</body>
</html:html>
