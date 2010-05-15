<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="java.sql.*, java.util.*, oscar.*" buffer="none"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.preferencesearchresults.title" /></title>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
    <!--
		function setfocus() {
		  document.searchprovider.keyword.focus();
		  document.searchprovider.keyword.select();
		}

    function onsub() {
      if(document.searchprovider.keyword.value=="") {
        alert("<bean:message key="global.msgInputKeyword"/>");
        return false;
      } else return true;
      // do nothing at the moment
      // check input data in the future 
    }

    //-->
    </script>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.preferencesearchresults.description" /></font></th>
	</tr>
</table>

<%--@ include file="zprovidertitlesearch.htm" --%>
<table cellspacing="0" cellpadding="0" width="100%" border="0"
	BGCOLOR="#C4D9E7">

	<form method="post" action="admincontrol.jsp" name="searchprovider">
	<tr valign="top">
		<td rowspan="2" align="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i><bean:message
			key="admin.search.formSearchCriteria" /></i></b></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_preferenceno"><bean:message
			key="admin.preference.formPreferenceNo" /></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" checked name="search_mode"
			value="search_providerno"><bean:message
			key="admin.provider.formProviderNo" /></font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="provider_no"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="preference_search_titlename">
		<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
			TYPE="hidden" NAME="limit2" VALUE="10"> <INPUT TYPE="hidden"
			NAME="displaymode" VALUE="Preference_Search"> <INPUT
			TYPE="SUBMIT" NAME="button"
			VALUE="<bean:message key="admin.search.btnSubmit"/>" SIZE="17"></td>
	</tr>
	<tr>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">Reserved
		</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		</font></td>
	</tr>
	</form>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><i><bean:message key="admin.search.keywords" /></i>
		: <%=request.getParameter("keyword")%></td>
	</tr>
</table>
<CENTER>
<table width="100%" cellspacing="0" cellpadding="1" border="1"
	bgcolor="#ffffff">
	<tr bgcolor="#339999">
		<TH align="center" width="10%"><b><bean:message
			key="admin.provider.formProviderNo" /></b></TH>
		<TH align="center" width="20%"><b><bean:message
			key="admin.preference.formStartHour" /></b></TH>
		<TH align="center" width="20%"><b><bean:message
			key="admin.preference.formEndHour" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="admin.preference.formPeriod" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="admin.preference.formGroupNo" /></b></TH>
		<!--TH align="center" width="10%"><b>Action</B></TH-->
	</tr>

	<%
  //if action is good, then give me the result
  ResultSet rs = null;
  String dboperation = request.getParameter("dboperation");
  String keyword=request.getParameter("keyword").trim();
  //keyword.replace('*', '%').trim();
  if(request.getParameter("search_mode").equals("search_name")) {
      keyword=request.getParameter("keyword")+"%";
      if(keyword.indexOf(",")==-1)  rs = apptMainBean.queryResults(keyword, dboperation) ; //lastname
      else if(keyword.indexOf(",")==(keyword.length()-1))  rs = apptMainBean.queryResults(keyword.substring(0,(keyword.length()-1)), dboperation);//lastname
      else { //lastname,firstname
    		String[] param =new String[2];
    		int index = keyword.indexOf(",");
	  		param[0]=keyword.substring(0,index).trim()+"%";//(",");
	  		param[1]=keyword.substring(index+1).trim()+"%";
	  		//System.out.println("from -------- :"+ param[0]+ ": next :"+param[1]);
    		rs = apptMainBean.queryResults(param, dboperation);
   		}
  } else if(request.getParameter("search_mode").equals("search_dob")) {
    		String[] param =new String[3];
	  		param[0]=""+MyDateFormat.getYearFromStandardDate(keyword)+"%";//(",");
	  		param[1]=""+MyDateFormat.getMonthFromStandardDate(keyword)+"%";
	  		param[2]=""+MyDateFormat.getDayFromStandardDate(keyword)+"%";  
	      //System.out.println("1111111111111111111 "+param[0]+param[1]+param[2]);
    		rs = apptMainBean.queryResults(param, dboperation);
  } else {
    keyword=request.getParameter("keyword")+"%";
    rs = apptMainBean.queryResults(keyword, dboperation);
	  // System.out.println("1111111111111111111 "+keyword);
  }
  
  boolean bodd=false;
  int nItems=0;
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      bodd=bodd?false:true;
      nItems++; //to calculate if it is the end of records
    // the cursor of ResultSet only goes through once from top
%>

	<tr bgcolor="<%=bodd?"ivory":"white"%>">

		<td align="center"><a
			href='admincontrol.jsp?keyword=<%=apptMainBean.getString(rs,"preference_no")%>&displaymode=Preference_Update&dboperation=preference_search_detail'><%= apptMainBean.getString(rs,"provider_no") %></a></td>
		<td align="center"><%= apptMainBean.getString(rs,"start_hour") %></td>
		<td align="center"><%= apptMainBean.getString(rs,"end_hour") %></td>
		<td align="center"><%= apptMainBean.getString(rs,"every_min") %></td>
		<td align="center"><%= apptMainBean.getString(rs,"mygroup_no") %></td>

		<!--td align="center" valign="middle" -->
		<!--img src="../images/buttondetail.gif" width="75" height="30" border="0" valign="middle"-->
	</tr>
	<%
    }
  }
  apptMainBean.closePstmtConn();
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  String strLimit1=request.getParameter("limit1");
  String strLimit2=request.getParameter("limit2");
  
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="admin.search.btnLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"><bean:message
	key="admin.search.btnNextPage" /></a> <%
}
%>
<p><bean:message
	key="admin.preferencesearchresults.msgClickForDetail" /></p>
</center>
<%@ include file="footerhtm.jsp"%></center>
</body>
</html:html>
