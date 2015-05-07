<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@page import="org.oscarehr.common.dao.Billing3rdPartyAddressDao"%>
<%@page import="org.oscarehr.billing.CA.ON.model.Billing3rdPartyAddress"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%//
			if (session.getAttribute("user") == null) {
				response.sendRedirect("../logout.jsp");
			}
			String strLimit1 = request.getParameter("limit1") == null ? "1" : request.getParameter("limit1");
			String strLimit2 = request.getParameter("limit2") == null ? "25" : request.getParameter("limit2");

			int nItems = 0;
			Vector vec = new Vector();
			Properties prop = null;
			String param = request.getParameter("param") == null ? "" : request.getParameter("param");
			String param2 = request.getParameter("param2") == null ? "" : request.getParameter("param2");
			String keyword = request.getParameter("keyword");
			
			if (request.getParameter("submit") != null
					&& (request.getParameter("submit").equals("Search")
							|| request.getParameter("submit").equals("Next Page") || request.getParameter("submit")
							.equals("Last Page"))) {
				String searchModeParam = request.getParameter("search_mode"); 
				String orderByParam = request.getParameter("orderby");
				
				Billing3rdPartyAddressDao dao = SpringUtils.getBean(Billing3rdPartyAddressDao.class);
				for(Billing3rdPartyAddress ba : dao.findAddresses(searchModeParam, orderByParam, keyword, strLimit1, strLimit2)) {
					prop = new Properties();
					prop.setProperty("id", "" + ba.getId());
					prop.setProperty("attention", ba.getAttention());
					prop.setProperty("company_name", ba.getCompanyName());
					prop.setProperty("address", ba.getAddress());
					prop.setProperty("city", ba.getCity());
					prop.setProperty("province", ba.getProvince());
					prop.setProperty("postcode", ba.getPostalCode());
					prop.setProperty("telephone", ba.getTelephone());
					prop.setProperty("fax", ba.getFax());
					vec.add(prop);
				}
			}
%>
<%@ page errorPage="../../../appointment/errorpage.jsp"
	import="java.util.*,java.sql.*,java.net.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.commons.lang.WordUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit 3rd Bill Address</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<script language="JavaScript">

		function setfocus() {
		  this.focus();
		  document.forms[0].keyword.focus();
		  document.forms[0].keyword.select();
		}
		function check() {
		  document.forms[0].submit.value="Search";
		  return true;
		}
		<%if(param.length()>0) {%>
		function typeInData1(data) {
			if( opener.updateElement != undefined ) {
				opener.updateElement("<%=param%>", data);
			}
			else {
	   	  		opener.<%=param%> = data;
			}
	   	  
		  	self.close();		 
		}
		<%if(param2.length()>0) {%>
		function typeInData2(data1, data2) {
		  opener.<%=param%> = data1;
		  opener.<%=param2%> = data2;
		  self.close();
		}
		<%}}%>


      </script>
</head>
<body bgcolor="white" bgproperties="fixed" onload="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
	
<form method="post" name="titlesearch" action="onSearch3rdBillAddr.jsp" onSubmit="return check();">	
<table border="0" cellpadding="1" cellspacing="0" width="100%" class="myDarkGreen">
	<tr>
		<td class="searchTitle" colspan="4"><font color="white">Search
		Address</font></td>
	</tr>
	<tr class="myYellow">
		<td class="blueText" width="10%" nowrap><input type="radio"
			name="search_mode" value="search_name" checked> Name</td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="postcode"> Postcode</td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="telephone"> Tel.</td>
		<td valign="middle" rowspan="2" align="left"><input type="text"
			name="keyword" value="" size="17" maxlength="100"> <input
			type="hidden" name="orderby" value="company_name"> <input
			type="hidden" name="limit1" value="0"> <input type="hidden"
			name="limit2" value="20"> <input type="hidden" name="submit"
			value='Search'> <input type="submit" value='Search'>
		</td>
	</tr>
</table>
<input type='hidden' name='param'
	value="<%=StringEscapeUtils.escapeHtml(param)%>">
<input type='hidden' name='param2'
	value="<%=StringEscapeUtils.escapeHtml(param2)%>">
<table width="95%" border="0">
	<tr>
		<td align="left">Results based on keyword(s): <%=keyword == null ? "" : keyword%></td>
	</tr>
</table>
</form>
<center>
<table width="100%" border="0" cellpadding="0" cellspacing="2" class="myYellow">
	<tr class="title">
		<th width="20%">Attention</th>
		<th width="20%">Company name</th>
		<th width="25%">Address</th>
		<th width="10%">City</th>
		<th width="10%">Postcode</th>
		<th>Phone</th>
		<!--  >th width="20%">Fax</b></th-->
	</tr>
	
	<%for (int i = 0; i < vec.size(); i++) {
					prop = (Properties) vec.get(i);
					String bgColor = i % 2 == 0 ? "#EEEEFF" : "ivory";
					String strOnClick = param.length() > 0 ? "typeInData1('"
							+ StringEscapeUtils.escapeJavaScript((prop.getProperty("attention", "").equals("")?"":(prop.getProperty("attention")+"\n")))
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("company_name", "").equals("")?"":(prop.getProperty("company_name")+"\n")) 
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("address", "").equals("")?"":(prop.getProperty("address")+"\n")) 
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("city", "").equals("")?"":(prop.getProperty("city")+" ")) 
                                                        + StringEscapeUtils.escapeJavaScript(prop.getProperty("province", "").equals("")?"":(prop.getProperty("province")+" ")) 
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("postcode", "").equals("")?"":(prop.getProperty("postcode")+"\n")) 
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("telephone", "").equals("")?"":(prop.getProperty("telephone")+"\n")) 
							+ StringEscapeUtils.escapeJavaScript(prop.getProperty("fax", "").equals("")?"":(prop.getProperty("fax")+"\n")) 
							+ "')" : "typeInData1('"
							+ prop.getProperty("city", "") + "')";

					%>
	<tr align="center" bgcolor="<%=bgColor%>"
		onMouseOver="this.style.cursor='pointer';this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';"
		onClick="<%=StringEscapeUtils.escapeHtml(strOnClick)%>">
		<td><%=prop.getProperty("attention", "")%></td>
		<td><%=WordUtils.capitalize(prop.getProperty("company_name", "").toLowerCase())%></td>
		<td><%=WordUtils.capitalize(prop.getProperty("address", "").toLowerCase())%></td>
		<td><%=prop.getProperty("city", "")%></td>
		<td><%=prop.getProperty("postcode", "")%></td>
		<td><%=prop.getProperty("telephone", "")%></td>
		<!--td><%=prop.getProperty("fax", "")%></td-->
	</tr>
	<%}

				%>
</table>

<%nItems = vec.size();
				int nLastPage = 0, nNextPage = 0;
				nNextPage = Integer.parseInt(strLimit2) + Integer.parseInt(strLimit1);
				nLastPage = Integer.parseInt(strLimit1) - Integer.parseInt(strLimit2);

				%> <%if (nItems == 0 && nLastPage <= 0) {

				%> <bean:message key="demographic.search.noResultsWereFound" /> <%}
%> <script language="JavaScript">
<!--
function last() {
  document.nextform.action="onSearch3rdBillAddr.jsp?param=<%=URLEncoder.encode(param,"UTF-8")%>&param2=<%=URLEncoder.encode(param2,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
  document.nextform.submit();
}
function next() {
  document.nextform.action="onSearch3rdBillAddr.jsp?param=<%=URLEncoder.encode(param,"UTF-8")%>&param2=<%=URLEncoder.encode(param2,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
  document.nextform.submit();
}
//-->
</SCRIPT>

<form method="post" name="nextform" action="onSearch3rdBillAddr.jsp">
<%if (nLastPage >= 0) {

				%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
	onClick="last()"> <%}
				if (nItems == Integer.parseInt(strLimit2)) {

				%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"
	onClick="next()"> <%}
%>
</form>
<br>
<a href="onAddEdit3rdAddr.jsp">Add/Edit Address</a></center>
</body>
</html:html>
