<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no =new Integer(request.getParameter("demographic_no")).intValue(); 
  String demographic_no =request.getParameter("demographic_no") ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.net.*,java.sql.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String param = request.getParameter("orderby")!=null?request.getParameter("orderby"):"form_date desc";
  String [][] dbQueries=new String[][] { 
{"search_deleted", "select * from eform_data where status = 0 and demographic_no= ?  order by " +
                    param + ", form_date desc, form_time desc" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
 
  ResultSet rs = myFormBean.queryResults(demographic_no, "search_deleted");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="eform.calldeletedformdata.title"/></title>
<link rel="stylesheet" href="../web.css">
</head>
<script language="javascript">
<!--
function newWindow(file,window) {
    msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
    if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica"><bean:message key="eform.calldeletedformdata.msgMyform"/></font></th></tr>
</table>

<table border="0" cellspacing="0" cellpadding="2" width="98%">
  <tr bgcolor=<%=weakColor%>>
    <td>Deleted Forms: </td>
    <td align='right'><a href="showmyform.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a> | 
      <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail"> <bean:message key="global.btnBack" /></a>
    </td> 
  </tr>
</table>
  
<table border="0" cellspacing="2" cellpadding="2" width="98%">
  <tr bgcolor=<%=deepColor%> >
  <th width=20%><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>&orderby=form_name"><bean:message key="eform.showmyform.btnFormName"/></a></th>
  <th width=40%><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>&orderby=subject"><bean:message key="eform.showmyform.btnSubject"/></a></th>
  <th><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formDate"/></a></th>
  <th><a href="calldeletedformdata.jsp?demographic_no=<%=demographic_no%>"><bean:message key="eform.showmyform.formTime"/></a></th> 
  <th>Action</th>
  </tr> 
<%
  String bgcolor = null;
  if (rs.next()){ 
    rs.beforeFirst();
    while (rs.next()){
      bgcolor = rs.getRow()%2==0?"white":weakColor ;
%>
  <tr bgcolor="<%=bgcolor%>">
  <td><a href="JavaScript:newWindow('showmyformdata.jsp?fdid=<%=rs.getInt("fdid")%>','window2')"><%=rs.getString("form_name")%></a></td>
  <td><%=rs.getString("subject")%></td>
  <td nowrap align='center'><%=rs.getString("form_date")%></td>
  <td nowrap align='center'><%=rs.getString("form_time")%></td>
  <td nowrap align='center'><a href="undeleteformdata.jsp?fdid=<%=rs.getInt("fdid")%>&demographic_no=<%=demographic_no%>"><bean:message key="eform.calldeletedformdata.btnUndelete"/></a></td>
  </tr>
<%
    }  
  }else {
%>
    <tr><td><bean:message key="eform.showmyform.msgNoData"/></td></tr>
<%
  }
  myFormBean.closePstmtConn();
%>               
</table>
</center>

</body>
</html:html>
 