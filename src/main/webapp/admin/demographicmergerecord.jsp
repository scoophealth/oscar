<!DOCTYPE html>
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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%

// Defaults    		
String strOffset="0";
String strLimit="10";

//OFFSET
if(request.getParameter("limit1")!=null) 
	strOffset = request.getParameter("limit1");
//LIMIT
if(request.getParameter("limit2")!=null) 
	strLimit = request.getParameter("limit2");

int offset = Integer.parseInt(strOffset);
int limit = Integer.parseInt(strLimit);

String outcome = request.getParameter("outcome");
boolean mergedSearch = false;
if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals("demographic_search_merged")) mergedSearch = true;
if( outcome !=null){
    if (outcome.equals("success")){
%>
<script language="JavaScript">
	alert("Records merged successfully");
</script>
<%
	}else if (outcome.equals("failure")){
%>
<script language="JavaScript">
	alert("Failed to merge records");
</script>
<%
	}else if (outcome.equals("successUnMerge")){
%>
<script language="JavaScript">
	alert("Record(s) unmerged successfully");
</script>
<%
	}else if (outcome.equals("failureUnMerge")){
%>
<script language="JavaScript">
	alert("Failed to unmerge records");
</script>
<%
	}
	}
%>

<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.oscarDemographic.data.DemographicMerged"%>
<%@ page import="java.lang.System" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%
	List<Demographic> demoList = null;  //demographicDao.getDemographicByProvider( "55");
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");

	String dboperation = request.getParameter("dboperation");
	String keyword=request.getParameter("keyword");
	String orderBy = request.getParameter("orderby");
	String searchMode = request.getParameter("search_mode");
	if(searchMode == null)
		searchMode = "search_name";
	
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo = loggedInInfo.getLoggedInProviderNo();
	boolean outOfDomain = true;
	if(OscarProperties.getInstance().getProperty("ModuleNames","").indexOf("Caisi") != -1) {
		if(!"true".equals(OscarProperties.getInstance().getProperty("pmm.client.search.outside.of.domain.enabled","true"))) {
			outOfDomain=false;
		}
		if(request.getParameter("outofdomain")!=null && request.getParameter("outofdomain").equals("true")) {
			outOfDomain=true;
		}
	}
	
%>

<html>
<head>
<title><bean:message key="admin.admin.mergeRec"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<script language="JavaScript">
	function setfocus() {
		document.titlesearch.keyword.focus();
		document.titlesearch.keyword.select();
	}

	function checkTypeIn() {
		var dob = document.titlesearch.keyword;
		typeInOK = true;
		if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18) {
			document.titlesearch.keyword.value = dob.value.substring(8, 18);
			document.titlesearch.search_mode[4].checked = true;
		}

		if (document.titlesearch.search_mode[2].checked) {
			if (dob.value.length == 8) {
				dob.value = dob.value.substring(0, 4) + "-"
						+ dob.value.substring(4, 6) + "-"
						+ dob.value.substring(6, 8);
			}
			if (dob.value.length != 10 || dob.value.indexOf(' ') > 0) {
				alert("Please format the date as yyyy-mm-dd");
				typeInOK = false;
			}
			return typeInOK;
		} else {
			return true;
		}
	}

	function UnMerge() {
		document.mergeform.mergeAction.value = "unmerge";
	}

	function searchMerged() {
		document.titlesearch.dboperation.value = "demographic_search_merged";
	}

	function popupWindow(page) {
		windowprops = "height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
		var popup = window.open(page, "labreport", windowprops);
		popup.focus();
	}
</SCRIPT>
<!--base target="pt_srch_main"-->

<style>
input[type="radio"]{
margin-left:8px;
}
</style>


</head>
<body onLoad="setfocus()">
<div class="container-fluid well">
<h3><bean:message key="admin.admin.mergeRec"/></h3>

<form method="post" name="titlesearch" action="demographicmergerecord.jsp" class="form-inline" onSubmit="return checkTypeIn()">

Search:

<input type="radio" name="search_mode" value="search_name" <%=searchMode.equals("search_name")?"checked":""%> > Name
<input type="radio" name="search_mode" value="search_phone" <%=searchMode.equals("search_phone")?"checked":""%>	> Phone
<input type="radio" name="search_mode" value="search_dob" <%=searchMode.equals("search_dob")?"checked":""%> > DOB
<input type="radio" name="search_mode" value="search_address" <%=searchMode.equals("search_address")?"checked":""%>> Address
<input type="radio" name="search_mode" value="search_hin" <%=searchMode.equals("search_hin")?"checked":""%>> HIN

<input type="text" NAME="keyword" class="span6" MAXLENGTH="100" value="<%=(keyword != null)?keyword:""%>"> 
			<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name"> 
			<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> 
			<INPUT TYPE="hidden" NAME="limit2" VALUE="10"> 

<INPUT class="btn" TYPE="SUBMIT" NAME="button" VALUE="Search"> 
<input class="btn" type="submit" name="mergebutton" value="Search Merged Records" onclick="searchMerged()">
</form>
</div><!--well-->

<% if (request.getParameter("keyword") != null) {%>

<i>Results based on keyword(s)</i> : <%=request.getParameter("keyword")%>

<CENTER>
<form method="post" name="mergeform" action="MergeRecords.do" onSubmit="return checkTypeIn()">
	<input type="hidden" name="mergeAction" value="merge" /> 
	<input type="hidden" name="provider_no" value="<%= session.getAttribute("user") %>" />
	
<table class="table table-striped  table-condensed">
	<tr>
		<TH align="CENTER" width="5%"></th>
		<% if (!mergedSearch){%>
		<th align="center" width="5%">Main Record</th>
		<%}%>
		<TH align="center" width="10%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit%>">Demographic</a></b></font></TH>
		<TH align="center" width="20%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=last_name&limit1=0&limit2=<%=strLimit%>">Last Name</a> </b></font></TH>
		<TH align="center" width="20%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=first_name&limit1=0&limit2=<%=strLimit%>">First Name</a> </b></font></TH>
		<TH align="center" width="10%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=age&limit1=0&limit2=<%=strLimit%>">Age</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit%>">Roster Status</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=sex&limit1=0&limit2=<%=strLimit%>">Sex</a></B></font></TH>
		<TH align="center" width="10%"><b><a
			href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=date_of_birth&limit1=0&limit2=<%=strLimit%>">DOB(yy/mm/dd)</a></B></Font></TH>
	</tr>
<%

if(!mergedSearch) {
	if(searchMode.equals("search_name")) {
		demoList = demographicDao.searchDemographicByName(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_dob")) {
		demoList = demographicDao.searchDemographicByDOB(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_phone")) {
		demoList = demographicDao.searchDemographicByPhone(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_hin")) {
		demoList = demographicDao.searchDemographicByHIN(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_address")) {
		demoList = demographicDao.searchDemographicByAddress(keyword, limit, offset, providerNo, outOfDomain);
	}
} else {
	if(searchMode.equals("search_name")) {
		demoList = demographicDao.searchMergedDemographicByName(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_dob")) {
		demoList = demographicDao.searchMergedDemographicByDOB(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_phone")) {
		demoList = demographicDao.searchMergedDemographicByPhone(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_hin")) {
		demoList = demographicDao.searchMergedDemographicByHIN(keyword, limit, offset, providerNo, outOfDomain);
	}
	else if(searchMode.equals("search_address")) {
		demoList = demographicDao.searchMergedDemographicByAddress(keyword, limit, offset, providerNo, outOfDomain);
	}
	
}

if(orderBy.equals("last_name")) {
	Collections.sort(demoList, Demographic.LastNameComparator);
}
else if(orderBy.equals("first_name")) {
	Collections.sort(demoList, Demographic.FirstNameComparator);
}
else if(orderBy.equals("demographic_no")) {
	Collections.sort(demoList, Demographic.DemographicNoComparator);
}
else if(orderBy.equals("sex")) {
	Collections.sort(demoList, Demographic.SexComparator);
}
else if(orderBy.equals("age")) {
	Collections.sort(demoList, Demographic.AgeComparator);
}
else if(orderBy.equals("date_of_birth")) {
	Collections.sort(demoList, Demographic.DateOfBirthComparator);
}
else if(orderBy.equals("roster_status")) {
	Collections.sort(demoList, Demographic.RosterStatusComparator);
}


boolean toggleLine = false;
int nItems=0;

if(demoList == null) {
    out.println("failed!!!");
} 
else {

	for(Demographic demo : demoList) {
		
		toggleLine = !toggleLine;
        nItems++; //to calculate if it is the end of records
%>
	<tr>
		<%
        DemographicMerged dmDAO = new DemographicMerged();
		String demographicNo = demo.getDemographicNo().toString() ;
		String head = dmDAO.getHead(demographicNo);
        
		// default to head record
        boolean isHeadRecord = true;

		// if record has a head record, then it is not the head record
        if(demo.getHeadRecord() != null)
        	isHeadRecord = false;
    
        if(mergedSearch || isHeadRecord  ){%>
			<td align="center" width="5%" height="25"><input type="checkbox" name="records" value="<%= demographicNo%>"></td>
		<%}
        else{%>
		<td align="center" width="5%" height="25">&nbsp;</td>
		<%}
		if (!mergedSearch ){
       		if(isHeadRecord){%>
				<td align="center" width="5%" height="25"><input type="radio" name="head" value="<%= demographicNo %>"></td>
			<%}else{%>
				<td align="center" width="5%" height="25">&nbsp;</td>
			<%}
		}%>
		<td width="15%" align="center" height="25">
		<caisi:isModuleLoad	moduleName="TORONTO_RFQ" reverse="true">
			<a href="javascript:popupWindow('../demographic/demographiccontrol.jsp?demographic_no=<%= head %>&displaymode=edit&dboperation=search_detail')"><%=demographicNo%></a>
		</caisi:isModuleLoad></td>
		<td align="center" width="20%" height="25"><%=demo.getLastName()%></td>
		<td align="center" width="20%" height="25"><%=demo.getFirstName()%></td>
		<td align="center" width="10%" height="25"><%=demo.getAge()%></td>
		<td align="center" width="10%" height="25"><%=demo.getRosterStatus()%></td>
		<td align="center" width="10%" height="25"><%=demo.getSex()%></td>
		<td align="center" width="10%" height="25"><%=demo.getFormattedDob()%></td>
	</tr>
	<%
    }
}
%>

</table>

<br>
<% if (mergedSearch){%> 

<input type="submit" class="btn btn-warning btn-large" value="UnMerge Selected Records" onclick="UnMerge()" /> 

<%}else{%> 

<input type="submit" class="btn btn-primary btn-large" value="Merge Selected Records" /> 

<%}%> <br />

</form>
<%
int nLastPage=0,nNextPage=0;
nNextPage=Integer.parseInt(strLimit)+Integer.parseInt(strOffset);
nLastPage=Integer.parseInt(strOffset)-Integer.parseInt(strLimit);
if(nLastPage>=0) {
%> <a
	href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit%>">Last
Page</a> | <%
}
if(nItems==Integer.parseInt(strLimit)) {
%> <a
	href="demographicmergerecord.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit%>">
Next Page</a> <%
}


}else{// end if (request.getParameter("keyword") != null)
%>
</center>

<h3 align="center">Please search for the records you wish to merge.</h3>
<% } %>


</body>
</html>
