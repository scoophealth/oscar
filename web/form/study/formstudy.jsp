<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.util.*" errorPage="../../errorpage.jsp" %>
<%
    //this is a quick independent page to let you select study.
    if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
    String demographic_no = request.getParameter("demographic_no")!=null ? request.getParameter("demographic_no") : "0";
    String curUser_no = (String) session.getAttribute("user");
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../../admin/dbconnection.jsp" %>
<% 
    String [][] dbQueries=new String[][] { 
        {"search_study", "select s.* from study s order by ? " }, 
        {"search_demostudy", "select d.demographic_no, s.* from demographicstudy d left join study s on d.study_no=s.study_no where d.demographic_no=? and s.current=1 order by d.study_no" }, 
	};
    studyBean.doConfigure(dbParams,dbQueries);
%>

<html>
<head>
<title>PATIENT STUDY</title>
<link rel="stylesheet" href="../web.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepColor%>"><th>PATIENT STUDY RECORDS</th></tr>
</table>

<table width="100%" border="0">
<tr><td align="left"><%=request.getParameter("name")!=null?request.getParameter("name"):""%></td></tr>
</table>

<CENTER>
<table width="70%" border="0" bgcolor="#ffffff" > 
<form method="post" name="study" action="demographicstudyselect.jsp" >
  <input type="hidden" name="demographic_no" value="<%=demographic_no%>">
  <tr bgcolor="<%=deepColor%>">
  <TH width="15%">Study No.</TH>
  <TH width="35%">Study Name</TH>
  <TH width="50%">Description</TH>
  </tr>
<%
    ResultSet rs = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    rs = studyBean.queryResults(new String[]{request.getParameter("demographic_no")}, "search_demostudy");
    while (rs.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
		System.out.println(nItems);
%>  
  <tr bgcolor="<%=bgcolor%>">
    <td align="center"><%=rs.getString("s.study_no")%></td>
    <td><a href="<%=rs.getString("s.study_link")%>?demographic_no=<%=request.getParameter("demographic_no")%>&study_no=<%=rs.getString("s.study_no")%>"><%=rs.getString("s.study_name")%></a></td>
    <td><%=rs.getString("s.description")%></td>
  </tr>
<%
	}
	studyBean.closePstmtConn();
%> 
<tr><td>&nbsp;</td><td></td><td></td></tr>
<tr align="center" bgcolor="<%=weakColor%>"><td colspan='3'>
  <input type="button" name="button" value=" Exit " onClick="window.close()">
  </td></tr>
</table>

</form>
</CENTER>
</body>
</html>