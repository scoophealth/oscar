<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curProvider_no = (String) session.getAttribute("user");
  
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String demolastname = request.getParameter("last_name")==null?"":request.getParameter("last_name");
  String demofirstname = request.getParameter("first_name")==null?"":request.getParameter("first_name");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html>
<head>
<title> APPOINTMENT HISTORY</title>
<link rel="stylesheet" href="../web.css" >
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">

<script language="JavaScript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";//360,680
  var popup=window.open(page, "appthist", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function refresh() {
  history.go(0);
}

//-->
</SCRIPT>
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed"  topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepColor%>">
    <th><font face="Helvetica">APPOINTMENT HISTORY </font></th>
  </tr>
</table>

<table width="95%" border="0">
  <tr bgcolor="<%=weakColor%>"><td align="left"><i>Results for Demographic</i> :<%=demolastname%>,<%=demofirstname%> (<%=request.getParameter("demographic_no")%>)</td></tr>
</table>
<CENTER>
<table width="95%" border="0" bgcolor="#ffffff"> 
<tr bgcolor="<%=deepColor%>">
      <TH width="10%"><b>APPT DATE</b></TH>
      <TH width="10%"><b>FROM</b></TH>      
      <TH width="10%"><b>TO</b></TH>
      <TH width="15%"><b>REASON</b></TH>
      <TH width="15%"><b>PROVIDER</b></TH>
      <TH width="10%"><b>COMMENTS</b></TH>
</tr>
<%
  ResultSet rs=null ;
  rs = apptMainBean.queryResults(Integer.parseInt(request.getParameter("demographic_no")), request.getParameter("dboperation"));

  boolean bodd=false;
  int nItems=0;
  
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       
%>
<tr bgcolor="<%=bodd?weakColor:"white"%>">
      <td align="center"><a href=# onClick ="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=rs.getString("appointment_no")%>&displaymode=edit&dboperation=search');return false;" ><%=rs.getString("appointment_date")%></a></td>
      <td align="center"><%=rs.getString("start_time")%></td>
      <td align="center"><%=rs.getString("end_time")%></td>
      <td><%=rs.getString("reason")%></td>
      <td><%=rs.getString("last_name")+","+rs.getString("first_name")%></td>
      <td>&nbsp;<%=rs.getString("status")==null?"":(rs.getString("status").equals("N")?"No Show":(rs.getString("status").equals("C")?"Cancelled":"") ) %></td>
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
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> Next Page</a>
<%
}
%>
<p>
<%@ include file="zfooterbackclose.jsp" %> 
</center>
</body>
</html>
