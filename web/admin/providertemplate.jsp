<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("admin"))
    response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,oscar.util.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_templatename", "select encountertemplate_name from encountertemplate where encountertemplate_name like ? order by encountertemplate_name" }, 
{"search_template", "select * from encountertemplate where encountertemplate_name = ?" }, 
{"delete_template", "delete from encountertemplate where encountertemplate_name = ? " }, 
{"add_template", "insert into encountertemplate (encountertemplate_name, encountertemplate_value, creator, createdatetime) values (?,?,?,?)" }, 
  };
  apptMainBean.doConfigure(dbParams,dbQueries);

  //save or delete the settings
  int rowsAffected = 0;
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 ||   
      request.getParameter("dboperation").equals("Delete") ) ) {
	GregorianCalendar now=new GregorianCalendar();
	String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

    String[] param = new String[4];
    param[0] = request.getParameter("name");
    param[1] = request.getParameter("value");
    param[2] = request.getParameter("creator");
    param[3] = strDateTime;
    rowsAffected = apptMainBean.queryExecuteUpdate(param[0],"delete_template");
    if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals(" Save ") ) 
      rowsAffected = apptMainBean.queryExecuteUpdate(param,"add_template");
  }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
<title><bean:message key="admin.providertemplate.title"/></title>
<link rel="stylesheet" href="../web.css" >
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.template.name.focus();
}
//-->
</SCRIPT>
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepcolor%>><th><font face="Helvetica"><bean:message key="admin.providertemplate.msgTitle"/></font></th>
  </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
<form name="edittemplate" method="post" action="providertemplate.jsp">
  <tr bgcolor=<%=weakcolor%>><td width="95%" align='right'><bean:message key="admin.providertemplate.formEdit"/>: 
    <select name="name" >
<%
  ResultSet rsdemo = null;
  rsdemo = apptMainBean.queryResults("%", "search_templatename");
  while (rsdemo.next()) { 
%>
    <option value="<%=rsdemo.getString("encountertemplate_name")%>"><%=rsdemo.getString("encountertemplate_name")%></option>
<%
  }
%>
    </select>
    <input type="hidden" value=" Edit " name="dboperation">
    <input type="button" value="<bean:message key="admin.providertemplate.btnEdit"/>" name="dboperation" onclick="document.forms['edittemplate'].dboperation.value=' Edit '; document.forms['edittemplate'].submit();">
  </td>
  <td>&nbsp;</td></tr>
</form>
</table>

<%
  boolean bEdit=request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals(" Edit ")?true:false;
  String tName = null;
  String tValue = null;
  if(bEdit) {
    rsdemo = apptMainBean.queryResults(request.getParameter("name"), "search_template");
    while (rsdemo.next()) { 
      tName = UtilMisc.charEscape(rsdemo.getString("encountertemplate_name"), '"');
      tValue = rsdemo.getString("encountertemplate_value");
	}
  }
  apptMainBean.closePstmtConn();
%>
<center>
<table width="90%" border="0"  cellspacing="2" cellpadding="2">
<form name="template" method="post" action="providertemplate.jsp">
  <input type="hidden" name="dboperation" value="">
  <tr>
    <td valign="top" width="20%" align="right" title='no symbol "'> Template Name:</td> 
    <td>  <input type="text" name="name" value="<%=bEdit?tName:""%>" style="width:100%" maxlength="20"></td>
  </tr><tr>
    <td valign="top" width="20%" align="right"> Template Text: </td>
    <td> <textarea name="value" style="width:100%" rows="20" ><%=bEdit?tValue:""%></textarea> </td>
  </tr>
</table>
  
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr bgcolor=<%=weakcolor%>>
    <td width="23%" align='right'><input type="button" value="<bean:message key="admin.providertemplate.btnDelete"/>" onClick="document.forms['template'].dboperation.value='Delete'; document.forms['template'].submit();"> </td>
    <td width="72%" align='right'>
          <INPUT TYPE="hidden" NAME="creator" VALUE="<%=curUser_no%>">
          <input type="button" value="<bean:message key="admin.providertemplate.btnSave"/>" onClick="document.forms['template'].dboperation.value=' Save '; document.forms['template'].submit();">
          <input type="button" name="Button" value="<bean:message key="admin.providertemplate.btnExit"/>" onClick="window.close();">
	</td>
	<td>&nbsp;</td></tr>
</form>
</table>
</center>
</body>
</html:html>
