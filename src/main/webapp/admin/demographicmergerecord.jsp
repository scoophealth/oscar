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
<%
if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
//String curProvider_no;
//curProvider_no = (String) session.getAttribute("user");
//curProvider_no =  request.getParameter("provider_no");

//display the main provider page
//includeing the provider name and a month calendar
String strLimit1="0";
String strLimit2="18";
if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
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

<%@ page
	import="java.util.*, java.sql.*, oscar.*, oscar.oscarDemographic.data.DemographicMerged"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>DEMOGRAPHIC - MERGE RECORDS</title>
<script language="JavaScript">

        function setfocus() {
            document.titlesearch.keyword.focus();
            document.titlesearch.keyword.select();
        }
        
        function checkTypeIn() {
            var dob = document.titlesearch.keyword; typeInOK = true;          
            if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){
               document.titlesearch.keyword.value = dob.value.substring(8,18);
               document.titlesearch.search_mode[4].checked = true;             
            }

            if(document.titlesearch.search_mode[2].checked) {
              if(dob.value.length==8) {
                dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
              }
              if(dob.value.length != 10 || dob.value.indexOf(' ') > 0 ) {
                alert("Please format the date as yyyy-mm-dd");
                typeInOK = false;
              }
              return typeInOK;
            } else {
              return true;
            }
        }
        
        function UnMerge(){
            document.mergeform.mergeAction.value = "unmerge";
        }
        
        function searchMerged(){
            document.titlesearch.dboperation.value="demographic_search_merged";
        }
        
        function popupWindow(page) {
            windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
            var popup = window.open(page, "labreport", windowprops);
            popup.focus();
        }
        

    </SCRIPT>
<!--base target="pt_srch_main"-->
<link rel="stylesheet" href="../web.css">
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">MERGE
		CLIENT RECORDS</font></th>
	</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<form method="post" name="titlesearch" action="admincontrol.jsp"
		onSubmit="return checkTypeIn()">
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i>Search </i></b></font></td>

		<td width="10%" nowrap><font size="1" face="Verdana"
			color="#0000FF"> <input type="radio" checked
			name="search_mode" value="search_name"> Name </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_phone">
		Phone</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_dob"> DOB</font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="last_name"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="demographic_search_titlename">
		<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
			TYPE="hidden" NAME="limit2" VALUE="10"> <INPUT TYPE="hidden"
			NAME="displaymode" VALUE="Demographic_Merge"> <INPUT
			TYPE="SUBMIT" NAME="button" VALUE="Search" SIZE="17"> <input
			type="submit" name="mergebutton" value="Search Merged Records"
			onclick="searchMerged()"></td>
	</tr>
	<tr>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_address">
		Address </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_hin"> HIN</font></td>
		<td></td>
	</tr>
	</form>
</table>
<br />
<CENTER>
<% if (request.getParameter("keyword") != null) {%>
<table width="95%" border="0">
	<tr>
		<td align="left"><i>Results based on keyword(s)</i> : <%=request.getParameter("keyword")%></td>
	</tr>
</table>
<hr>
<CENTER>
<form method="post" name="mergeform" action="MergeRecords.do"
	onSubmit="return checkTypeIn()"><input type="hidden"
	name="mergeAction" value="merge" /> <input type="hidden"
	name="provider_no" value="<%= session.getAttribute("user") %>" />
<table width="100%" border="2" bgcolor="#ffffff">
	<tr bgcolor="silver">
		<TH align="CENTER" width="5%"></th>
		<% if (!mergedSearch){%>
		<th align="center" width="5%">Main Record</th>
		<%}%>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit2%>">DEMOGP'
		NO</a></b></font></TH>
		<TH align="center" width="20%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=last_name&limit1=0&limit2=<%=strLimit2%>">LAST
		NAME</a> </b></font></TH>
		<TH align="center" width="20%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=first_name&limit1=0&limit2=<%=strLimit2%>">FIRST
		NAME</a> </b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=year_of_birth,month_of_birth,date_of_birth&limit1=0&limit2=<%=strLimit2%>">AGE</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit2%>">ROSTER
		STATUS</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=sex&limit1=0&limit2=<%=strLimit2%>">SEX</a></B></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=year_of_birth,month_of_birth,date_of_birth&limit1=0&limit2=<%=strLimit2%>">DOB(yy/mm/dd)</a></B></Font></TH>
	</tr>
	<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);
int age=0;
ResultSet rs=null ;

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
        rs = apptMainBean.queryResults(param, dboperation);
    }
} else if(request.getParameter("search_mode").equals("search_dob")) {
    String[] param =new String[3];
    param[0] = keyword.substring(0,4);//+"%";//(",");
    param[1] = keyword.substring(keyword.indexOf("-")+1, keyword.lastIndexOf("-"));
//param[1] = param[1].startsWith("0") ? param[1].substring(1) : param[1];
    param[2] = keyword.substring(keyword.lastIndexOf("-")+1);
//param[2] = param[2].startsWith("0") ? param[2].substring(1) : param[2];
    rs = apptMainBean.queryResults(param, dboperation);
} else {
    keyword=request.getParameter("keyword")+"%";
    rs = apptMainBean.queryResults(keyword, dboperation);
}

boolean bodd=false;
int nItems=0;

if(rs==null) {
    out.println("failed!!!");
} else {
    while (rs.next()) {
        bodd=bodd?false:true; //for the color of rows
        nItems++; //to calculate if it is the end of records
        
        if(!(apptMainBean.getString(rs,"month_of_birth").equals(""))) {//   ||apptMainBean.getString(rs,"year_of_birth")||apptMainBean.getString(rs,"date_of_birth")) {
            if(curMonth>Integer.parseInt(apptMainBean.getString(rs,"month_of_birth"))) {
                age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
            } else {
                if(curMonth==Integer.parseInt(apptMainBean.getString(rs,"month_of_birth")) &&
                        curDay>Integer.parseInt(apptMainBean.getString(rs,"date_of_birth"))) {
                    age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
                } else {
                    age=curYear-Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"))-1;
                }
            }
        }	

%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<%
        DemographicMerged dmDAO = new DemographicMerged();
        String dbop = "demographic_search_detail";
        String head = dmDAO.getHead(apptMainBean.getString(rs,"demographic_no"));
        String demographic = apptMainBean.getString(rs,"demographic_no") ;
        boolean headRecord = head.equals(demographic);
        java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
        if (vLocale.getCountry().equals("BR"))
            dbop = "demographic_search_detail_ptbr";
    if(mergedSearch || headRecord  ){%>
		<td align="center" width="5%" height="25"><input type="checkbox"
			name="records"
			value="<%= apptMainBean.getString(rs,"demographic_no") %>"></td>
		<%}else{%>
		<td align="center" width="5%" height="25">&nbsp;</td>
		<%}%>
		<% if (!mergedSearch ){
       if(headRecord){%>
		<td align="center" width="5%" height="25"><input type="radio"
			name="head"
			value="<%= apptMainBean.getString(rs,"demographic_no") %>"></td>
		<%}else{%>
		<td align="center" width="5%" height="25">&nbsp;</td>
		<%}%>

		<%}%>
		<td width="15%" align="center" height="25"><caisi:isModuleLoad
			moduleName="TORONTO_RFQ" reverse="true">
			<a
				href="javascript:popupWindow('admincontrol.jsp?demographic_no=<%= head %>&displaymode=Demographic_Edit2&dboperation=<%= dbop %>')"><%=apptMainBean.getString(rs,"demographic_no")%></a>
		</caisi:isModuleLoad></td>
		<td align="center" width="20%" height="25"><%=apptMainBean.getString(rs,"last_name")%></td>
		<td align="center" width="20%" height="25"><%=apptMainBean.getString(rs,"first_name")%></td>
		<td align="center" width="10%" height="25"><%=age%></td>
		<td align="center" width="10%" height="25"><%=apptMainBean.getString(rs,"roster_status")%></td>
		<td align="center" width="10%" height="25"><%=apptMainBean.getString(rs,"sex")%></td>
		<td align="center" width="10%" height="25"><%=apptMainBean.getString(rs,"year_of_birth")+"-"+apptMainBean.getString(rs,"month_of_birth")+"-"+apptMainBean.getString(rs,"date_of_birth")%></td>
	</tr>
	<%
    }
}
%>

</table>

<br>
<% if (mergedSearch){%> <input type="submit"
	value="UnMerge Selected Records" onclick="UnMerge()" /> <%}else{%> <input
	type="submit" value="Merge Selected Records" /> <%}%> <br />
</form>
<%
int nLastPage=0,nNextPage=0;
nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
if(nLastPage>=0) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
}
if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
}
}else {// end if (request.getParameter("keyword") != null)
%>
<p>Please search for the records you wish to merge.</p>
<% } %>
</center>
<hr width="100%" color="navy">
</body>
</html>
