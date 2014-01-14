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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<% 
  if(session.getAttribute("user") == null)    response.sendRedirect("../logout.htm");
  
  String curProvider_no = request.getParameter("provider_no");
      
  String strLimit1="0";
  String strLimit2="10";
  StringBuilder bufChart = null, bufName = null, bufNo = null, bufDoctorNo = null;
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  boolean caisi = Boolean.valueOf(request.getParameter("caisi")).booleanValue();
%>

<%@ page import="java.util.*, java.sql.*,java.net.*, oscar.*,org.oscarehr.util.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="demographic.demographicsearch2apptresults.title" />
(demographicsearch2reportresults)</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
function checkTypeIn() {  
  var dob = document.titlesearch.keyword;
  
  if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){                  
     document.titlesearch.keyword.value = dob.value.substring(8,18);
     document.titlesearch.search_mode[4].checked = true;                  
  }
  
  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
    }
    if(dob.value.length != 10) {
      alert("<bean:message key="demographic.demographicsearch2apptresults.msgWrongDOB"/>");
      return false;
    } else {
      return true;
    }
  } else {
    return true;
  }
}

function searchInactive() {
    document.titlesearch.ptstatus.value="inactive"
    if (checkTypeIn()) document.forms[0].submit()
}

function searchAll() {
    document.titlesearch.ptstatus.value=""
    if (checkTypeIn()) document.forms[0].submit()
}


</SCRIPT>
</head>
<body bgcolor="white" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
	

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="subject">
		<th><bean:message
			key="demographic.demographicsearch2apptresults.patientsRecord" /></th>
	</tr>
</table>
<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#CCCCFF">
<!--  	
	<form method="post" name="titlesearch"
		action="../admin/admincontrol.jsp"
		onSubmit="return checkTypeIn()"><%--@ include file="zdemographictitlesearch.htm"--%>
	<tr>
		<td class="searchTitle" colspan="4"><bean:message
			key="demographic.demographicsearch2apptresults.btnSearch" /></td>
	</tr>
	<tr>
		<td class="blueText" width="10%" nowrap>
			<input type="radio" name="search_mode" value="search_name" <%=request.getParameter("search_mode").equals("search_name")?"checked":""%>>
			<bean:message key="demographic.demographicsearch2apptresults.optName" />
		</td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="search_phone"
			<%=request.getParameter("search_mode").equals("search_phone")?"checked":""%>>
		<bean:message key="demographic.demographicsearch2apptresults.optPhone" /></td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="search_dob"
			<%=request.getParameter("search_mode").equals("search_dob")?"checked":""%>>
		<bean:message key="demographic.demographicsearch2apptresults.optDOB" /></td>
		<td valign="middle" rowspan="2" ALIGN="left">
			<input type="text" NAME="keyword" VALUE="<%=request.getParameter("keyword")%>" SIZE="17" MAXLENGTH="100"> 
			<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name"> 
			<INPUT TYPE="hidden" NAME="dboperation" VALUE="demographic_admin_reports"> 
			<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> 
			<INPUT TYPE="hidden" NAME="limit2" VALUE="5"> 
			<input type="hidden" name="displaymode" value="Demographic_Admin_Reports"> 
			<INPUT TYPE="hidden" NAME="ptstatus" VALUE="active"> 
			<input type="SUBMIT" class="mbttn" name="displaymode" value='Search' size="17" title='<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchActive"/>'>
		&nbsp;&nbsp; <INPUT TYPE="button" class="mbttn"
			onclick="searchInactive();"
			TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchInactive"/>"
			VALUE="<bean:message key="demographic.search.Inactive"/>"> <INPUT
			TYPE="button" class="mbttn" onclick="searchAll();"
			TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchAll"/>"
			VALUE="<bean:message key="demographic.search.All"/>"></td>
	</tr>
	<tr>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="search_address"
			<%=request.getParameter("search_mode").equals("search_address")?"checked":""%>>
		<bean:message
			key="demographic.demographicsearch2apptresults.optAddress" /></td>
		<td class="blueText" nowrap><input type="radio"
			name="search_mode" value="search_hin"
			<%=request.getParameter("search_mode").equals("search_hin")?"checked":""%>>
		<bean:message key="demographic.demographicsearch2apptresults.optHIN" /></td>
		<td></td>
	</tr>
	<%
  String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")  ||temp.equals("ptstatus") ||temp.equals("submit")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
	%>
	</form>
-->	
</table>

<table width="95%" border="0">
	<tr>
		<td align="left"><bean:message
			key="demographic.demographicsearch2apptresults.msgKeywords" /> <%=request.getParameter("keyword")%></td>
	</tr>
</table>


<script language="JavaScript">

var fullname="";
<%-- RJ 07/10/2006 Need to pass doctor of patient back to referrer --%>
function addName(demographic_no, lastname, firstname, chartno, messageID, doctorNo) {  
  fullname=lastname+","+firstname;
  document.addform.action="<%=request.getParameter("originalpage")%>?demographic_no="+demographic_no+"&firstNameParam="+firstname+"&lastNameParam="+lastname+"&chart_no="+chartno;
  document.addform.submit();
  return true;
}

<%if(caisi) {%>
function addNameCaisi(demographic_no,lastname,firstname,chartno,messageID) {
  	fullname=lastname+","+firstname;
  	if(opener.document.<%=request.getParameter("formName")%>!=null){
      if(opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementName")%>']!=null)
    	 opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementName")%>'].value=fullname;
	  if(opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementId")%>']!=null)
  	     opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("elementId")%>'].value=demographic_no;
	}
	self.close();
}
<%}%>
</SCRIPT>

<CENTER>
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<form method="post" name="addform"
		action="../appointment/addappointment.jsp">
	<tr class="title">
		<TH width="20%"><b><bean:message
			key="demographic.demographicsearch2apptresults.demographicId" /></b></TH>
		<TH width="20%"><b><bean:message
			key="demographic.demographicsearch2apptresults.lastName" /></b></TH>
		<TH width="20%"><b><bean:message
			key="demographic.demographicsearch2apptresults.firstName" /></b></TH>
		<TH width="5%"><b><bean:message
			key="demographic.demographicsearch2apptresults.age" /></b></TH>
		<TH width="10%"><b><bean:message
			key="demographic.demographicsearch2apptresults.rosterStatus" /></b></TH>
		<TH width="5%"><b><bean:message
			key="demographic.demographicsearch2apptresults.sex" /></B></TH>
		<TH width="10%"><b><bean:message
			key="demographic.demographicsearch2apptresults.DOB" /></B></TH>
		<TH width="10%"><b><bean:message
			key="demographic.demographicsearch2apptresults.doctor" /></B></TH>
	</tr>

	<%@ include file="../demographic/zzdemographicsearchresult.jspf"%>
	<%
String bgColor = bodd?"#EEEEFF":"white";
%>

	<tr bgcolor="<%=bgColor%>" align="center"
		<%-- 07/10/2006 RJ Added doctor provider_no to url --%>
onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';"
		onClick="<% if(caisi) { out.print("addNameCaisi");} else { out.print("addName");} %>('<%=apptMainBean.getString(rs,"demographic_no")%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"chart_no"))%>','<%=request.getParameter("messageId")%>','<%=apptMainBean.getString(rs,"provider_no")%>')">
		<%-- 07/10/2006 RJ Added doctor provider_no to url --%>
		<td><input type="submit" class="mbttn" name="demographic_no"
			value="<%=apptMainBean.getString(rs,"demographic_no")%>"
			onClick="<% if(caisi) {out.print("addNameCaisi");} else {out.print("addName");} %>('<%=apptMainBean.getString(rs,"demographic_no")%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>','<%=URLEncoder.encode(apptMainBean.getString(rs,"chart_no"))%>','<%=request.getParameter("messageId")%>','<%=apptMainBean.getString(rs,"provider_no")%>')"></td>
		<td><%=Misc.toUpperLowerCase(apptMainBean.getString(rs,"last_name"))%></td>
		<td><%=Misc.toUpperLowerCase(apptMainBean.getString(rs,"first_name"))%></td>
		<td><%=age%></td>
		<td><%=apptMainBean.getString(rs,"roster_status")%></td>
		<td><%=apptMainBean.getString(rs,"sex")%></td>
		<td><%=apptMainBean.getString(rs,"year_of_birth")+"-"+apptMainBean.getString(rs,"month_of_birth")+"-"+apptMainBean.getString(rs,"date_of_birth")%></td>
		<td><%=providerBean.getProperty(apptMainBean.getString(rs,"provider_no"))==null?"":providerBean.getProperty(apptMainBean.getString(rs,"provider_no"))%></td>
	</tr>
	<%
      bufName = new StringBuilder( (apptMainBean.getString(rs,"last_name")+ ","+ apptMainBean.getString(rs,"first_name")) );
      bufNo = new StringBuilder( (apptMainBean.getString(rs,"demographic_no")) );
      bufChart = new StringBuilder(apptMainBean.getString(rs,"chart_no"));
      bufDoctorNo = new StringBuilder( apptMainBean.getString(rs,"provider_no") ); 
    }
  }
%>
	<%
  //String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode")||temp.equals("submit") ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
  
  //should close the pipe connected to the database here!!!
	%>
	</form>

</table>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
%> <%	
  if(nItems==1 && nLastPage<=0 && !caisi) { //if there is only one search result, it should be added to the appoint page directly.
%> <script language="JavaScript">
<!--
  document.addform.action="<%=request.getParameter("originalpage")%>?name=<%=URLEncoder.encode(bufName.toString())%>&chart_no=<%=URLEncoder.encode(bufChart.toString())%>&bFirstDisp=false&demographic_no=<%=bufNo.toString()%>&messageID=<%=request.getParameter("messageId")%>&doctor_no=<%=bufDoctorNo.toString()%>";
  document.addform.submit();  
//-->
</SCRIPT> <%
  } else if (nItems==1 && nLastPage<=0 && caisi) {
	  //caisi version
%> <script language="JavaScript">
<!--

	fullname='<%=bufName.toString()%>';
	demographic_no='<%=bufNo.toString()%>';
	if(opener.document.ticklerForm.elements['tickler.demographic_webName']!=null)
	   opener.document.ticklerForm.elements['tickler.demographic_webName'].value=fullname;
	if(opener.document.ticklerForm.elements['tickler.demographic_no']!=null)   
  	   opener.document.ticklerForm.elements['tickler.demographic_no'].value=demographic_no;
	self.close();
//-->
</SCRIPT> <%
  }
  if(nItems==0 && nLastPage<=0) {
%> <caisi:isModuleLoad moduleName="caisi" reverse="true">
	<bean:message key="demographic.search.noResultsWereFound" />
	<a href="../demographic/demographicaddarecordhtm.jsp"><bean:message
		key="demographic.search.btnCreateNew" /></a>
</caisi:isModuleLoad> <%
    }
%> <script language="JavaScript">
<!--
function last() {
  document.nextform.action="../admin/admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();  
}
function next() {
  document.nextform.action="../admin/admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();  
}
//-->
</SCRIPT>

<form method="post" name="nextform"
	action="../admin/admincontrol.jsp">
<%
  if(nLastPage>=0) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
	onClick="last()"> <%
  }
//  if(nItems==Integer.parseInt(strLimit2)) {
  if(nItems<iRow) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"
	onClick="next()"> <%
}
%> <%
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("submit")  ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
        
  }
	%>
</form>
</CENTER>
<a href="#" onClick="history.go(-1);return false;"> 
	<img src="../images/leftarrow.gif" border="0" width="25" height="20" align="absmiddle"> Back 
</a>
</body>
</html>
