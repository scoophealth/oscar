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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="java.util.*,java.sql.*, java.net.*"%>
<%@ page import="org.oscarehr.common.web.ContactAction"%>
<%@ page import="org.oscarehr.common.model.ProfessionalContact"%>
<%@ page import="org.oscarehr.common.model.Contact"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.commons.lang.WordUtils"%>

<%@ include file="/taglibs.jsp"%>

<%

  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		 
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
	  String list = request.getParameter("list");
	  List<?> contacts;

	  String programNo = request.getParameter("programNo");
	  String relatedTo = request.getParameter("relatedTo");
	  
	  if( "all".equalsIgnoreCase(list) ) {
		  contacts = ContactAction.searchAllContacts(search_mode, orderBy, keyword,programNo,loggedInInfo.getLoggedInProviderNo(),relatedTo);
		  pageContext.setAttribute("toggleSearchTool", list);
	  } else {
		  contacts = ContactAction.searchProContacts(search_mode, orderBy, keyword,programNo, loggedInInfo.getLoggedInProviderNo(),relatedTo);
	  }
	  
	  nItems = contacts.size();
	  pageContext.setAttribute("contacts",contacts);
	}
	
	
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Search Professional Contacts</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript">

//<!--
		function setfocus() {
		  var toggleSearchTool = "<c:out value="${ toggleSearchTool }" />"; 
		  if( toggleSearchTool ) {
			  document.getElementById("procontactSearch_searchTools").style.display="none";  
		  }
		  
		  this.focus();
		  document.forms[0].keyword.focus();
		  document.forms[0].keyword.select();
		}
		
		function check() {
		  document.forms[0].submit.value="Search";
		  return true;
		}
		
		function selectResult(data1,data2) {

			try {
				serializePopupData(data1, data2);
			} catch(error) {				
				// split contact type [0] from contact id [1]
				data1 = data1.split("_")[1].trim();				
				opener.document.<%=form%>.elements['<%=elementId%>'].value = data1;
				opener.document.<%=form%>.elements['<%=elementName%>'].value = data2;
				self.close();
			}

		}
		
		function serializePopupData(data1, data2) {			
			
			var id1 = '<%= elementId %>';
			var id2 = '<%= elementName %>';
			var contactType = data1.split("_")[0].trim();
			var contactId = data1.split("_")[1].trim();
			
			var data = '{"' + 
				id1 + '":"' + contactId + 
				'","' + id2 + '":"' + data2 + 
				'","contactRole":"' +
				'","demographicContactId":"0' +
				'","method":"saveManage' +
				'","contactType":"' + contactType + '"}';
	
			opener.popUpData(data);
			self.close();
		}
		                
//-->

</script>
</head>
<body onload="setfocus()" class="BodyStyle">
	
<form method="post" name="titlesearch" action="procontactSearch.jsp" onSubmit="return check();">

<table class="MainTable" >

	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="20%">Search Professional Contacts</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="contact" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>

	<!--  tr>
		<td class="searchTitle" colspan="4">Search Professional Contacts</td>
	</tr-->
	<tr id="procontactSearch_searchTools" >
		<td class="blueText" >
			<input type="radio" name="search_mode" value="search_name" checked="checked"> <label for="search_mode">Name</label>
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

<!-- table width="95%" border="0">
	<tr>
		<td align="left">Results based on keyword(s): <%=keyword==null?"":keyword%></td>
	</tr>
</table  -->

<input type='hidden' name='form' value="<%=StringEscapeUtils.escapeHtml(form)%>"/>
<input type='hidden' name='elementName' value="<%=StringEscapeUtils.escapeHtml(elementName)%>"/>
<input type='hidden' name='elementId' value="<%=StringEscapeUtils.escapeHtml(elementId)%>"/>

</form>

<table width="100%" style="text-align:left;" 
	cellpadding="2" cellspacing="1" 
	bgcolor="#ABABAB" >
	
	<tr style="background-color:white;border:white;" >
		<td colspan="3"  >
			<strong>Results based on keyword(s):</strong> <%=keyword==null?"":keyword%>
		</td>
	</tr>
	
	<tr class="title">
		<th><strong>Last Name</strong></th>
		<th><strong>First Name</strong></th>		
		<th><strong>Phone</strong></th>
	</tr>
	
	<c:forEach var="contact" items="${contacts}" varStatus="i">
		<%
			ProfessionalContact contact = (ProfessionalContact)pageContext.getAttribute("contact");
			javax.servlet.jsp.jstl.core.LoopTagStatus i = (javax.servlet.jsp.jstl.core.LoopTagStatus) pageContext.getAttribute("i");
			String bgColor = i.getIndex()%2==0?"#EEEEFF":"ivory";	
			String strOnClick; 
            strOnClick = "selectResult('" + contact.getSystemId() + "_" + contact.getId() + "','"+StringEscapeUtils.escapeJavaScript(contact.getLastName()+ "," + contact.getFirstName()) + "')";
                        
		%>
		<tr bgcolor="<%=bgColor%>" 
		onMouseOver="this.style.cursor='pointer';this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';" onClick="<%=strOnClick%>">
			<td><c:out value="${contact.lastName}"/></td>
			<td><c:out value="${contact.firstName}"/></td>
			<td><c:out value="${contact.residencePhone}"/></td>
		</tr>
	</c:forEach>
	
	<%
	  int nLastPage=0,nNextPage=0;
	  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
	  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
	%> 
	<%
	  if(nItems==0 && nLastPage<=0) {
	%> 
	<tr>
		<td colspan="3"> <bean:message key="demographic.search.noResultsWereFound" /> </td> 
	</tr>
	<% } %> 
	
	<tr>	
		<form method="post" name="nextform" action="searchRefDoc.jsp">
			<%
			  if(nLastPage>=0) {
			%> 
			<td><input type="submit" class="mbttn" name="submit"
				value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
				onClick="last()"> </td><%
			  }
			  if(nItems==Integer.parseInt(strLimit2)) {
			%> <td><input type="submit" class="mbttn" name="submit"
				value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"
				onClick="next()"> </td><%
			}
			%>
		</form>	
	</tr>

</table>
<table>

	<tr>
		<td>
			<a href="Contact.do?method=addProContact&keyword=<%= keyword %>&contactRole=${ param.contactRole }" 
			style="font:inherit;display:block;margin:10px;">
				Add/Edit Professional Contact
			</a>
		</td>
	</tr>
	</table>


<script type="text/javascript">
//<!--
function last() {
  document.nextform.action="procontactSearch.jsp?form=<%=URLEncoder.encode(form,"UTF-8")%>&elementName=<%=URLEncoder.encode(elementName,"UTF-8")%>&elementId=<%=URLEncoder.encode(elementId,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ; 
  document.nextform.submit();
}
function next() {
  document.nextform.action="procontactSearch.jsp?form=<%=URLEncoder.encode(form,"UTF-8")%>&elementName=<%=URLEncoder.encode(elementName,"UTF-8")%>&elementId=<%=URLEncoder.encode(elementId,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ; 
  document.nextform.submit();
}
//-->
</script>

</body>
</html:html>
