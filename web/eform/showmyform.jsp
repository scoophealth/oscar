<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.sql.ResultSet" %> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"form_date desc";
  String [][] dbQueries=new String[][] { 
{"search_eformdatadefault", "select * from eform_data where status = 1 and demographic_no = ? order by " +
                            param + ", form_date desc, form_time desc" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);

  ResultSet rs = myFormBean.queryResults(demographic_no, "search_eformdatadefault");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html locale="true">
<SCRIPT LANGUAGE="JavaScript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</SCRIPT>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="eform.showmyform.title"/></title>
<link rel="stylesheet" href="../web.css">
</head>

<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica"><bean:message key="eform.showmyform.msgMyForm"/></font></th></tr>
</table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="<%=weakColor%>">
  <tr><td align='right'><a href="myform.jsp?demographic_no=<%=demographic_no%>" > <bean:message key="eform.showmyform.btnAddEForm"/></a>
  | <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"><bean:message key="global.btnBack" /> &nbsp;</a></td>
  </tr>
</table> 

<table border="0" cellspacing="0" cellpadding="0" width="95%">
  <tr><td><bean:message key="eform.showmyform.msgFormLybrary"/> </td>
  <td align='right'><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>"> 
    <bean:message key="eform.showmyform.btnDeleted"/> </a></td></tr>
</table>
  
<table border="0" cellspacing="2" cellpadding="2" width="95%">
  <tr bgcolor=<%=deepColor%> >
    <th><a href="showmyform.jsp?demographic_no=<%=demographic_no%>&orderby=form_name"><bean:message key="eform.showmyform.btnFormName"/></a></th>
    <th><a href="showmyform.jsp?demographic_no=<%=demographic_no%>&orderby=subject"><bean:message key="eform.showmyform.btnSubject"/></a></th>
    <th><a href="showmyform.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formDate"/></a></th>
    <th><a href="showmyform.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formTime"/></a></th> 
    <th></th> 
  </tr> 
<%
  boolean bodd = true ;
  if(rs.next()) {
    rs.beforeFirst();
    while (rs.next()) {
      bodd = bodd?false:true;
%>
      <tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
	  <td><a href="JavaScript:newWindow('showmyformdata.jsp?fdid=<%=rs.getInt("fdid")%>','_blank')"><%=rs.getString("form_name")%></a></td>
      <td><%=rs.getString("subject")%></td>
      <td align='center'><%=rs.getString("form_date")%></td>
	  <td align='center'><%=rs.getString("form_time")%></td>
	  <td align='center'><a href="deleteformdata.jsp?fdid=<%=rs.getInt("fdid")%>&demographic_no=<%=demographic_no%>">Delete</a></td>
	  </tr>
<%    }  
  }else {
%>
    <tr><td align='center' colspan='5'><bean:message key="eform.showmyform.msgNoData"/></td></tr>
<%
  }
  myFormBean.closePstmtConn();
%>               
</table>
</center>

</body>
</html:html>  