<%
	if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
	String curUser_no = (String) session.getAttribute("user");
	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";
  
	String strLimit1="0";
	String strLimit2="20";  
	if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
	if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>

<%@ page import="java.util.*, java.sql.*, oscar.*" errorPage="../errorpage.jsp" %>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="studyBean" class="java.util.Properties" scope="page" />

<%@ include file="../admin/dbconnection.jsp" %>
<% 
	String [][] dbQueries=new String[][] { 
		{"search_study", "select study_no, study_name, description from study where current = ?"}, 
		{"search_demostudy", "select s.demographic_no, s.study_no, d.last_name, d.first_name, d.provider_no from demographicstudy s left join demographic d on s.demographic_no=d.demographic_no order by d.last_name"}, 
	};
	reportMainBean.doConfigure(dbParams,dbQueries);
%>
 
<html>
<head>
<title> REPORT STUDY </title>
<!--link rel="stylesheet" href="../receptionist/receptionistapptstyle.css" -->
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}
//-->
</SCRIPT>
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica">Demographic Study List</font></th></tr>
  <tr>
	<td align="right" ><input type="button" name="Button" value="Print" onClick="window.print()">
	<input type="button" name="Button" value="Cancel" onClick="window.close()"></td>
  </tr>
</table>

<table width="100%" border="0" bgcolor="white" cellspacing="2" cellpadding="2"> 
<tr bgcolor='<%=deepcolor%>'>
  <TH width="30%" nowrap>Last Name</TH>
  <TH width="30%">First Name</TH>
  <TH width="20%">Study</TH>
  <TH>Provider</TH>
</tr>
<%
	ResultSet rs=null ;
	rs = reportMainBean.queryResults("1", "search_study");
	while (rs.next()) { 
		studyBean.setProperty(rs.getString("study_no"), rs.getString("study_name") );
		studyBean.setProperty(rs.getString("study_no") + rs.getString("study_name"), rs.getString("description") );
	}
    
	//int[] itemp1 = new int[2];  //itemp1[0] = Integer.parseInt(strLimit1);

	int nItems=0;
	rs = reportMainBean.queryResults("search_demostudy");
	while (rs.next()) {
		nItems++; 
%>
<tr bgcolor="<%=(nItems%2 == 0)?weakcolor:"white"%>">
  <td nowrap><a href="../demographic/demographiccontrol.jsp?demographic_no=<%=rs.getString("s.demographic_no")%>&displaymode=edit&dboperation=search_detail"><%=rs.getString("d.last_name")%></a></td>
  <td><%=rs.getString("d.first_name")%></td>
  <td  title='<%=studyBean.getProperty(rs.getString("s.study_no")+studyBean.getProperty(rs.getString("s.study_no")), "")%>'><%=studyBean.getProperty(rs.getString("s.study_no"), "")%></td>
  <td><%=providerBean.getProperty(rs.getString("d.provider_no"), "")%></td>
</tr>
<%
	}
	reportMainBean.closePstmtConn();
%> 
</table>
<br>
<%
	int nLastPage=0,nNextPage=0;
	nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
	nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
	if(nLastPage>=0) {
%>
<a href="demographicstudyreport.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
	}
	if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="demographicstudyreport.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> Next Page</a>
<%
	}
%>
</body>
</html>