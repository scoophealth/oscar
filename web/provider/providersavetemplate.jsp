<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="../errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<%
  if(request.getParameter("submit").equals("Delete")) {
    String[] param = new String[1];
    param[0]=request.getParameter("encountertemplate_name");
    int rowsAffected = oscarSuperManager.update("providerDao", "delete_template", param);
    out.println("<script language='JavaScript'>self.close();</script>");
  } else {
%>


<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A TEMPLATE RECORD</font></th>
	</tr>
</table>
<%
	  //if action is good, then give me the result
	  String[] param =new String[4];
	  param[0]=request.getParameter("templatename");
	  param[1]=null;
	  param[2]=request.getParameter("templatetext"); //	"<table><tr><td width='10%'>Subject:</td><td><input type='text' name='xml_subject' style='width:100%' value='' size='60' maxlength='60'></td></tr><tr><td>Content:</td><td><textarea name='xml_content' style='width:100%' cols='60' rows='8'>" +request.getParameter("templatetext")+
	         // "</textarea></td></tr><input type='hidden' name='xml_subjectprefix' value='.'></talbe>";
	  param[3]="Unknown";

	  int rowsAffected = oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);
	  if (rowsAffected == 1) {
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
     	//self.opener.refresh();
</script> <%
	  }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
	  }
%>
<p></p>
<hr width="90%"/>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>

<%
  }
%>
</body>
</html>