<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.util.*" errorPage="errorpage.jsp" %>
<%
    //this is a quick independent page to let you add studying patient.
    if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
    String [][] dbQueries=new String[][] { 
        {"search_echart", "select * from eChart order by ? desc" }, 
    };
    studyBean.doConfigure(dbParams,dbQueries);
%>

<html>
<head>
<title>PATIENT STUDY SEARCH RESULTS </title>
<link rel="stylesheet" href="../web.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "studypopup", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepColor%>"><th>SEARCH FOR PATIENT STUDY RECORDS</th></tr>
</table>

<CENTER>
<table width="100%" border="0" bgcolor="#ffffff" > 
  <tr bgcolor="<%=deepColor%>">
  <TH width="10%">eChartId</TH>
  <TH width="10%">timeStamp</TH>
  <TH width="10%">demographicNo</TH>
  <TH width="3%">providerNo</TH>
  <TH width="10%">encounter</TH>
  </tr>
<%
    ResultSet rsdemo = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    rsdemo = studyBean.queryResults(new String[]{"timeStamp"}, "search_echart");
    while (rsdemo.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
		if (rsdemo.getString("encounter")!=null && rsdemo.getString("encounter").length()>32*1024) {
%>  
  <tr bgcolor="<%=bgcolor%>">
    <td align="center"><%=rsdemo.getString("eChartId")%></td>
    <td><%=rsdemo.getString("timeStamp")%></td>
    <td><a href=# onClick="popupPage(600,700, '../demographic/demographiccontrol.jsp?demographic_no=<%=rsdemo.getString("demographicNo")%>&displaymode=edit&dboperation=search_detail')"><%=rsdemo.getString("demographicNo")%></a></td>
    <td align="center"><%=rsdemo.getString("providerNo")%></td>
    <td align="center"><%=rsdemo.getString("encounter").length()%></td>
  </tr>
<%
		}
  }
  studyBean.closePstmtConn();
%> 

</table>
<br>

</CENTER>
</body>
</html>