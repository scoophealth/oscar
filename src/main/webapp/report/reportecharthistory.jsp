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
<%
  

  //String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("appointment_date") ;
  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"0" ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*,oscar.oscarProvider.data.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />

<%
  String [][] dbQueries=new String[][] {
//{"search_appt","select appointment_no, appointment_date,start_time, end_time, reason from appointment where demographic_no=? order by ? desc limit ? offset ?" },
{"search_ect","select eChartId, providerNo, timeStamp, subject, encounter from eChart where demographicNo=? order by timeStamp desc limit ? offset ?" },
//{"search_splitectsize","select encounter from eChart where demographicNo=? and timeStamp > ? order by timeStamp limit 1" },
  };
  daySheetBean.doConfigure(dbQueries);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ENCOUNTER SHEET</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}

//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#CCCCFF">
		<th align=CENTER NOWRAP><font face="Helvetica"><bean:message
			key="oscarEncounter.echartHistory.title" /></font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="<bean:message key="oscarEncounter.echartHistory.buttonPrint"/>"
			onClick="window.print()"><input type="button" name="Button"
			value="<bean:message key="oscarEncounter.echartHistory.buttonExit"/>"
			onClick="window.close()"></th>
	</tr>
</table>

<table width="480" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="1"
	cellpadding="2">
	<tr bgcolor="#CCCCFF" align="center">
		<TH><b><bean:message
			key="oscarEncounter.echartHistory.apptDate" /></b></TH>
		<TH width="50%"><b><bean:message
			key="oscarEncounter.echartHistory.reason" /></b></TH>
		<!--TH width="10%"><b>Size</b></TH-->
		<th>Provider</th>
	</tr>
	<%
  ResultSet rsdemo = null ;
  ResultSet rsdemo1 = null ;
  //boolean bodd = false;
  boolean bfirsttime = true;
  int nItems=0;
  int ectsize=0;
  String datetime =null;
  String splitectsize = request.getParameter("splitectsize")!=null?request.getParameter("splitectsize"):"-" ;
  String bgcolor = null;

  String[] param =new String[1];
  param[0]=demographic_no;
  int[] itemp1 = new int[2];
  itemp1[1] = Integer.parseInt(strLimit1);
  itemp1[0] = Integer.parseInt(strLimit2);

  rsdemo = daySheetBean.queryResults(param,itemp1, "search_ect");
  while (rsdemo.next()) {
    //bodd = bodd?false:true;
	nItems++;
	bgcolor = nItems%2==0?"#EEEEFF":"white";
	if(rsdemo.getString("timeStamp").length() == 19) {
		datetime = rsdemo.getString("timeStamp");
	} else {
		datetime = rsdemo.getString("timeStamp").substring(0,4) +"-"+ rsdemo.getString("timeStamp").substring(4,6) +"-"+ rsdemo.getString("timeStamp").substring(6,8)+" "+ rsdemo.getString("timeStamp").substring(8,10)+":"+ rsdemo.getString("timeStamp").substring(10,12);
	}
    ectsize = rsdemo.getString("encounter").length() / 1024;

	//if (bfirsttime && splitectsize.equals("-")) {
		//get the split ect size
    //    rsdemo1 = daySheetBean.queryResults(new String[] {demographic_no, rsdemo.getString("timeStamp")}, "search_splitectsize");
    //    while (rsdemo1.next()) {
    //        splitectsize="" + rsdemo1.getString("encounter").length() / 1024;;
    //    }
	//}
	if (rsdemo.getString("subject") != null && rsdemo.getString("subject").equals("SPLIT CHART")) {
		splitectsize="" + ectsize;
		bgcolor = "gold";
	}
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align="center"><a
			href="../oscarEncounter/echarthistoryprint.jsp?echartid=<%=rsdemo.getString("eChartId")%>&demographic_no=<%=demographic_no%>"><%=datetime%></a></td>
		<td><%=rsdemo.getString("subject")!=null?rsdemo.getString("subject"):""%></td>
		<!--td align="center"><%--=ectsize + "KB" --%></td-->
		<td><%=ProviderData.getProviderName(rsdemo.getString("providerNo"))%></td>
	</tr>
	<%
  }
%>

</table>
<br>
<CENTER>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportecharthistory.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportecharthistory.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>&splitectsize=<%=splitectsize%>">
Next Page</a> <%
  }
%>
</CENTER>
</body>
</html>
