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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_search" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_search");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%  
  String curProvider_no = request.getParameter("provider_no");
      
  String strOffset="0";
  String strLimit="10";
  StringBuilder bufChart = null, bufName = null, bufNo = null, bufDoctorNo = null;
  
  String keyword = request.getParameter("keyword");
  if(request.getParameter("limit1")!=null) strOffset = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit = request.getParameter("limit2");
  
  int limit = Integer.parseInt(strLimit);
  int offset = Integer.parseInt(strOffset);
  
  boolean caisi = Boolean.valueOf(request.getParameter("caisi")).booleanValue();
  
   
%>

<%@ page import="java.util.*, java.sql.*,java.net.*, oscar.*" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>

<jsp:useBean id="providerBean" class="java.util.Properties"	scope="session" />

<%
List<Demographic> demoList = null;  
DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");

String[] statusArray = { "IN","DE","IC","ID","MO","FI" };
List<String> statusList = Arrays.asList(statusArray);
String statusString = "'IN','DE','IC','ID','MO','FI'";

%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="demographic.demographicsearch2apptresults.title" />(demographicsearch2reportresults)</title>

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
<body bgcolor="white" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="subject">
		<th><bean:message key="demographic.demographicsearch2apptresults.patientsRecord" /></th>
	</tr>
</table>
<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#CCCCFF">
</table>

<table width="95%" border="0">
	<tr>
		<td align="left"><bean:message key="demographic.demographicsearch2apptresults.msgKeywords" /> <%=request.getParameter("keyword")%></td>
	</tr>
</table>

<script language="JavaScript">

var fullname="";
<%-- RJ 07/10/2006 Need to pass doctor of patient back to referrer --%>
function addName(demographic_no, lastname, firstname, chartno, messageID, doctorNo) {  
  fullname=lastname+","+firstname;
  document.addform.action="<%=request.getParameter("originalpage")%>&demographicNoParam="+demographic_no+"&demographic_no="+demographic_no+"&firstNameParam="+firstname+"&lastNameParam="+lastname+"&chart_no="+chartno;
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

<%

boolean toggleLine = false;
int nItems=0;

demoList = demographicDao.searchDemographicByNameAndNotStatus(keyword, statusList, limit, offset, curProvider_no, true);
int dSize = demoList.size();


if(demoList == null) {
    out.println("failed!!!");
} 
else {

	for(Demographic demo : demoList) {
		
		toggleLine = !toggleLine;
        nItems++; //to calculate if it is the end of records


String bgColor = toggleLine?"#EEEEFF":"white";
%>

	<tr bgcolor="<%=bgColor%>" align="center" 
			onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
			onMouseout="this.style.backgroundColor='<%=bgColor%>';"
			onClick="<% if(caisi) { out.print("addNameCaisi");} 
						else { out.print("addName");} %>('<%=demo.getDemographicNo()%>','<%=URLEncoder.encode(demo.getLastName())%>','<%=URLEncoder.encode(demo.getFirstName())%>','<%=URLEncoder.encode(demo.getChartNo())%>','<%=request.getParameter("messageId")%>','<%=demo.getProviderNo()%>')">
		
		<td><input type="submit" class="mbttn" name="demographic_no" value="<%=demo.getDemographicNo()%>"
			onClick="<% if(caisi) {out.print("addNameCaisi");} 
					else { out.print("addName");} %>('<%=demo.getDemographicNo()%>','<%=URLEncoder.encode(demo.getLastName())%>','<%=URLEncoder.encode(demo.getFirstName())%>','<%=URLEncoder.encode(demo.getChartNo())%>','<%=request.getParameter("messageId")%>','<%=demo.getProviderNo()%>')">
			</td>
		<td><%=Misc.toUpperLowerCase(demo.getLastName())%></td>
		<td><%=Misc.toUpperLowerCase(demo.getFirstName())%></td>
		<td><%=demo.getAge()%></td>
		<td><%=demo.getRosterStatus()%></td>
		<td><%=demo.getSex()%></td>
		<td><%=demo.getFormattedDob()%></td>
		<td><%=providerBean.getProperty(demo.getProviderNo())==null?"":providerBean.getProperty(demo.getProviderNo())%></td>
	</tr>
	<%
      bufName = new StringBuilder(demo.getLastName()+ ","+ demo.getFirstName());
      bufNo = new StringBuilder(demo.getDemographicNo());
      bufChart = new StringBuilder(demo.getChartNo());
      bufDoctorNo = new StringBuilder(demo.getProviderNo()); 
    }
  }
%>
	<%
  String temp=null;
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
  nNextPage=Integer.parseInt(strLimit)+Integer.parseInt(strOffset);
  nLastPage=Integer.parseInt(strOffset)-Integer.parseInt(strLimit);
%> 

<%
if(nItems==0 && nLastPage<=0) {
%> <caisi:isModuleLoad moduleName="caisi" reverse="true">
	<bean:message key="demographic.search.noResultsWereFound" />
	<a href="../demographic/demographicaddarecordhtm.jsp?search_mode=<%=request.getParameter("search_mode")%>&keyword=<%=request.getParameter("keyword")%>"><bean:message
		key="demographic.search.btnCreateNew" /></a>
</caisi:isModuleLoad> <%
    }
%> <script language="JavaScript">
<!--
function last() {		
  document.nextform.action="demographicsearch2reportresults.jsp?originalpage=<%=request.getParameter("originalpage")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit%>" ;
  //document.nextform.submit();  
}
function next() {
  document.nextform.action="demographicsearch2reportresults.jsp?originalpage=<%=request.getParameter("originalpage")%>&keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit%>" ;
  //document.nextform.submit();  
}
//-->
</SCRIPT>

<form method="post" name="nextform"	action="demographicsearch2reportresults.jsp">
<%
  if(nLastPage>=0) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"
	onClick="last()"> <%
  }
  if(nItems==Integer.parseInt(strLimit)) {
%> <input type="submit" class="mbttn" name="submit"
	value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"
	onClick="next()"> <%
}
%> <%
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("submit")  ||temp.equals("chart_no")) continue;
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
