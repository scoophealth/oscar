<%
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
String curUser_no = (String) session.getAttribute("user");
String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";

String strLimit1="0";
String strLimit2="100";  
String orderBy = request.getParameter("orderby")!=null ? request.getParameter("orderby") : "first_name" ;
if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

String fieldName = request.getParameter("fieldname")!=null ? request.getParameter("fieldname") : "pg1_priCare" ;
%>

<%@ page import="java.sql.*" errorPage="../errorpage.jsp"%>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="providerNameBean" class="java.util.Properties"
	scope="page" />

<% 
String [][] dbQueries=new String[][] { 
	{"search_provider", "select provider_no, last_name, first_name from provider where ohip_no!=\"\" or rma_no!=\"\" or billing_no!=\"\" or hso_no!=\"\" order by " + orderBy }, 
};
reportMainBean.doConfigure(dbQueries);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Provider List</title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">
<script language="JavaScript">
<!--

function typeInData(v) {
  self.close();
  <% if(fieldName.equals("pg1_priCare") ) {%>
  opener.document.forms[0].pg1_priCare.value = v ; 
  <% } else {%>
  opener.document.forms[0].<%=fieldName%>.value = v ; 
  <% }%>
}
//-->
</SCRIPT>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica">Provider List</font></th>
	</tr>
	<tr>
		<td align="right"><!--input type="button" name="Button" value="<bean:message key="global.btnPrint"/>" onClick="window.print()"-->
		<input type="button" name="Button"
			value="<bean:message key="global.btnCancel" />"
			onClick="window.close()">
		</th>
	</tr>
</table>

<CENTER>
<table width="100%" border="0" bgcolor="silver" cellspacing="2"
	cellpadding="2">
	<tr bgcolor='<%=deepcolor%>'>
		<TH align="center" width="50%" nowrap><a
			href="formbcarpg1namepopup.jsp?orderby=first_name">First Name</a></TH>
		<TH align="center" width="50%"><a
			href="formbcarpg1namepopup.jsp?orderby=last_name">Last Name</a></TH>
	</tr>
	<%
  int nItems=0;
  String bgColor = "white";
  String lastName = null, firstName = null;
  ResultSet rs = reportMainBean.queryResults("search_provider");
  while (rs.next()) { 
    providerNameBean.setProperty(reportMainBean.getString(rs,"provider_no"), new String( reportMainBean.getString(rs,"last_name")+","+reportMainBean.getString(rs,"first_name") ));
    nItems++; 
    bgColor = nItems%2==0?weakcolor:"white";
    lastName = reportMainBean.getString(rs,"last_name")!=null?reportMainBean.getString(rs,"last_name") : "" ;
    firstName = reportMainBean.getString(rs,"first_name")!=null?reportMainBean.getString(rs,"first_name") : "" ;
%>
	<tr bgcolor="<%=bgColor%>"
		onMouseOver="this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';"
		onClick='typeInData("<%=firstName + " " + lastName%>");'>
		<td nowrap><%=firstName%></td>
		<td nowrap><%=lastName%></td>
	</tr>
	<%
  }
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="formbcarpg1namepopup.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportnewdblist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="formbcarpg1namepopup.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportnewdblist.msgNextPage" /></a> <%
}
%>

</body>
</html:html>
