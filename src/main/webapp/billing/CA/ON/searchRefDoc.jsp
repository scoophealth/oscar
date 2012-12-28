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
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
%>
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  int nItems = 0;
  Vector vec = new Vector();
  Properties prop = null;
  String param = request.getParameter("param")==null?"":request.getParameter("param") ;
  String param2 = request.getParameter("param2")==null?"":request.getParameter("param2") ;
  String toname = request.getParameter("toname")==null?"":request.getParameter("toname") ;
  String toaddress1 = request.getParameter("toaddress1")==null?"":request.getParameter("toaddress1") ;
  String tophone = request.getParameter("tophone")==null?"":request.getParameter("tophone") ;
  String tofax = request.getParameter("tofax")==null?"":request.getParameter("tofax") ;
  String keyword = request.getParameter("keyword");

	if (request.getParameter("submit") != null && (request.getParameter("submit").equals("Search")
		|| request.getParameter("submit").equals("Next Page") || request.getParameter("submit").equals("Last Page")) ) {

	  
	  String search_mode = request.getParameter("search_mode")==null?"search_name":request.getParameter("search_mode");
	  String orderBy = request.getParameter("orderby")==null?"last_name,first_name":request.getParameter("orderby");
	  String where = "";

	  List<ProfessionalSpecialist> professionalSpecialists = null;

	  if ("search_name".equals(search_mode)) {
	    String[] temp = keyword.split("\\,\\p{Space}*");
	    
	    if (temp.length>1) {		
	      professionalSpecialists = professionalSpecialistDao.findByFullName(temp[0], temp[1]);
	    } else {		
	    	professionalSpecialists = professionalSpecialistDao.findByLastName(temp[0]);
	    }
	  } else if("specialty".equals(search_mode)){
		  professionalSpecialists = professionalSpecialistDao.findBySpecialty(keyword);
	  } else if("referral_no".equals(search_mode)) {
		  professionalSpecialists = professionalSpecialistDao.findByReferralNo(keyword);
	  }

	  if (professionalSpecialists != null) {
		 for (ProfessionalSpecialist professionalSpecialist : professionalSpecialists) {
		  	prop = new Properties();
		  	prop.setProperty("referral_no", (professionalSpecialist.getReferralNo() != null ? professionalSpecialist.getReferralNo() : ""));
		  	prop.setProperty("last_name", (professionalSpecialist.getLastName() != null ? professionalSpecialist.getLastName() : ""));
		  	prop.setProperty("first_name", (professionalSpecialist.getFirstName() != null ? professionalSpecialist.getFirstName() : ""));
		  	prop.setProperty("specialty", (professionalSpecialist.getSpecialtyType() != null ? professionalSpecialist.getSpecialtyType() : ""));
		  	prop.setProperty("phone", (professionalSpecialist.getPhoneNumber() != null ? professionalSpecialist.getPhoneNumber() : ""));
            prop.setProperty("to_fax", (professionalSpecialist.getFaxNumber() != null ? professionalSpecialist.getFaxNumber() : ""));
            prop.setProperty("to_name", "Dr. " + professionalSpecialist.getFirstName() + " " + professionalSpecialist.getLastName());
            prop.setProperty("to_address", (professionalSpecialist.getStreetAddress() != null ? professionalSpecialist.getStreetAddress() : ""));
		  	vec.add(prop);
		 }
	  }

	}
%>
<%@ page import="java.util.*, java.sql.*, java.net.*"%>

<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.commons.lang.WordUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Service Code</title>
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
		<%if(param.length()>0) {%>
		function typeInData1(data) {
		  self.close();
		  opener.<%=param%> = data;
		}
		<%if(param2.length()>0) {%>
		function typeInData2(data1, data2) {
		  opener.<%=param%> = data1;
		  opener.<%=param2%> = data2;
		  self.close();
		}
		<%}}%>
                
         function typeInData3(billno, toname, toaddress, tophone, tofax){
         	self.close();
         	<%if( param.length() > 0 ) {%>
            	opener.<%=param%> = billno;
            <%}
              if( toname.length() > 0 ) {%>
            	opener.<%=toname%> = toname;
            <%}
              if( toaddress1.length() > 0 ) {%>
            	opener.<%=toaddress1%> = toaddress;
            <%}
              if( tophone.length() > 0 ) {%>
            	opener.<%=tophone%> = tophone;
            <%}
              if( tofax.length() > 0 ) {%>
            	opener.<%=tofax%> = tofax;
            <%}%>
         }
                
-->

      </script>
</head>
<body bgcolor="white" bgproperties="fixed" onload="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#CCCCFF">
	<form method="post" name="titlesearch" action="searchRefDoc.jsp"
		onSubmit="return check();">
	<tr>
		<td class="searchTitle" colspan="4">Search Referral Doctor</td>
	</tr>
	<tr>
		<td class="blueText" width="10%" nowrap><input type="radio"
			name="search_mode" value="search_name" checked> Name</td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="specialty"> Specialty</td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="referral_no"> Ref. no.</td>
		<td valign="middle" rowspan="2" align="left"><input type="text"
			name="keyword" value="" size="17" maxlength="100"> <input
			type="hidden" name="orderby" value="last_name, first_name"> <input
			type="hidden" name="limit1" value="0"> <input type="hidden"
			name="limit2" value="10"> <input type="hidden" name="submit"
			value='Search'> <input type="submit" value='Search'>
		</td>
	</tr>
	<input type='hidden' name='param'
		value="<%=StringEscapeUtils.escapeHtml(param)%>">
	<input type='hidden' name='param2'
		value="<%=StringEscapeUtils.escapeHtml(param2)%>">
	<input type='hidden' name='toname'
		value="<%=StringEscapeUtils.escapeHtml(toname)%>">
	<input type='hidden' name='toaddress1'
		value="<%=StringEscapeUtils.escapeHtml(toaddress1)%>">
	<input type='hidden' name='tophone'
		value="<%=StringEscapeUtils.escapeHtml(tophone)%>">
	<input type='hidden' name='tofax'
		value="<%=StringEscapeUtils.escapeHtml(tofax)%>">
</table>
<table width="95%" border="0">
	<tr>
		<td align="left">Results based on keyword(s): <%=keyword==null?"":keyword%></td>
	</tr>
	</form>
</table>
<center>
<table width="100%" border="0" cellpadding="0" cellspacing="2"
	bgcolor="#C0C0C0">
	<tr class="title">
		<th width="10%"><b>Ref. No.</b></th>
		<th width="25%">Last Name</b></th>
		<th width="20%">First Name</b></th>
		<th width="20%">Specialty</b></th>
		<th width="20%">Phone</b></th>
	</tr>
	<%for(int i=0; i<vec.size(); i++) {
        	prop = (Properties) vec.get(i);
			String bgColor = i%2==0?"#EEEEFF":"ivory";
			String strOnClick;
                        if ( param2.length() <= 0){
                            strOnClick = "typeInData3('" + StringEscapeUtils.escapeJavaScript(prop.getProperty("referral_no","")) + "', '" + StringEscapeUtils.escapeJavaScript(prop.getProperty("to_name", "")) + "', '" + StringEscapeUtils.escapeJavaScript(prop.getProperty("to_address", "")) + "', '" + StringEscapeUtils.escapeJavaScript(prop.getProperty("phone", "")) + "', '" + StringEscapeUtils.escapeJavaScript(prop.getProperty("to_fax", "")) + "')" ;
                        } else {
                            strOnClick = param2.length()>0? "typeInData2('" + StringEscapeUtils.escapeJavaScript(prop.getProperty("referral_no", "")) + "','"+StringEscapeUtils.escapeJavaScript(prop.getProperty("last_name", "")+ "," + prop.getProperty("first_name", "")) + "')"
				: "typeInData1('" + prop.getProperty("referral_no", "") + "')";
                                }
        %>
	<tr align="center" bgcolor="<%=bgColor%>" align="center"
		onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';"
		onClick="<%=strOnClick%>">
		<td><%=prop.getProperty("referral_no", "")%></td>
		<td><%=WordUtils.capitalize(prop.getProperty("last_name", "").toLowerCase())%></td>
		<td><%=WordUtils.capitalize(prop.getProperty("first_name", "").toLowerCase())%></td>
		<td><%=prop.getProperty("specialty", "")%></td>
		<td><%=prop.getProperty("phone", "")%></td>
	</tr>
	<% } %>
</table>

<%
  nItems=vec.size();
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
  document.nextform.action="searchRefDoc.jsp?param=<%=URLEncoder.encode(param,"UTF-8")%>&param2=<%=URLEncoder.encode(param2,"UTF-8")%>&toname=<%=URLEncoder.encode(toname,"UTF-8")%>&toaddress1=<%=URLEncoder.encode(toaddress1,"UTF-8")%>&tophone=<%=URLEncoder.encode(tophone,"UTF-8")%>&tofax=<%=URLEncoder.encode(tofax,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
  document.nextform.submit();
}
function next() {
  document.nextform.action="searchRefDoc.jsp?param=<%=URLEncoder.encode(param,"UTF-8")%>&param2=<%=URLEncoder.encode(param2,"UTF-8")%>&toname=<%=URLEncoder.encode(toname,"UTF-8")%>&toaddress1=<%=URLEncoder.encode(toaddress1,"UTF-8")%>&tophone=<%=URLEncoder.encode(tophone,"UTF-8")%>&tofax=<%=URLEncoder.encode(tofax,"UTF-8")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
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
<a href="<%=request.getContextPath() %>/oscarEncounter/oscarConsultationRequest/config/EditSpecialists.jsp">Edit Professional Specialists</a></center>
</body>
</html:html>
