<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");

  //String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("appointment_date") ;
  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"0" ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
  String strLimit1="0";
  String strLimit2="10";  
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
//{"search_appt","select appointment_no, appointment_date,start_time, end_time, reason from appointment where demographic_no=? order by ? desc limit ?, ?" }, 
{"search_ect","select eChartId, timeStamp, subject, encounter from eChart where demographicNo=? order by timeStamp desc limit ?, ?" }, 
//{"search_splitectsize","select encounter from eChart where demographicNo=? and timeStamp > ? order by timeStamp limit 1" }, 
  };
  daySheetBean.doConfigure(dbParams,dbQueries);
%>
<html>
<head>
<title>ENCOUNTER SHEET </title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
<link rel="stylesheet" href="../web.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=15";//360,680
  var popup=window.open(page, "apptday", windowprops);
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
  <tr bgcolor="#CCCCFF"><th align=CENTER NOWRAP><font face="Helvetica">ECHART HISTORY</font></th>
    <th width="10%" nowrap> 
      <input type="button" name="Button" value="Print" onClick="window.print()"><input type="button" name="Button" value=" Exit " onClick="window.close()"></th></tr>
</table>

<table width="480" border="0" cellspacing="1" cellpadding="0" ><tr> 
<td>  </td>
<td align="right"></td>
</tr></table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="1" cellpadding="2" > 
<tr bgcolor="#CCCCFF" align="center">
<TH width="25%"><b>Appt Date</b></TH>
<TH width="65%"><b>Reason</b></TH>
<!--TH width="10%"><b>Size</b></TH-->
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
  itemp1[0] = Integer.parseInt(strLimit1);
  itemp1[1] = Integer.parseInt(strLimit2);

  rsdemo = daySheetBean.queryResults(param,itemp1, "search_ect");
  while (rsdemo.next()) { 
    //bodd = bodd?false:true;
	nItems++;
	bgcolor = nItems%2==0?"#EEEEFF":"white";
	datetime = rsdemo.getString("timeStamp").substring(0,4) +"-"+ rsdemo.getString("timeStamp").substring(4,6) +"-"+ rsdemo.getString("timeStamp").substring(6,8)+" "+ rsdemo.getString("timeStamp").substring(8,10)+":"+ rsdemo.getString("timeStamp").substring(10,12); 
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
      <td align="center"><a href="../oscarEncounter/echarthistoryprint.jsp?echartid=<%=rsdemo.getString("eChartId")%>&demographic_no=<%=demographic_no%>"><%=datetime%></a></td>
      <td><%=rsdemo.getString("subject")!=null?rsdemo.getString("subject"):""%></td>
      <!--td align="center"><%--=ectsize + "KB" --%></td-->
</tr>
<%
  }
  daySheetBean.closePstmtConn();
%> 

</table>
<br>
<CENTER>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
<a href="reportecharthistory.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="reportecharthistory.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>&splitectsize=<%=splitectsize%>"> Next Page</a>
<%
  }
%>
</CENTER>
</body>
</html>