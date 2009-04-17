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

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
//  mygroupno = (String) session.getAttribute("groupno");  
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();      
    }   
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%
  String content="";//default is not null temp=null, 
  content=SxmlMisc.createXmlDataString(request, "xml_");

  String[] param = new String[7];
  param[0]=request.getParameter("demographic_no");
  param[1]=request.getParameter("user_no");
  param[2]=request.getParameter("billing_date");
  param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("billing_time"));
  param[4]=request.getParameter("billing_name");
  param[5]=content;
  param[6]=request.getParameter("total");

  int rowsAffected = oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);
  if (rowsAffected == 1) {
	String[] param1 = new String[2];
	param1[0]="B";
	param1[1]=request.getParameter("appointment_no");
	rowsAffected = oscarSuperManager.update("providerDao", "updateapptstatus", param1);
	List<Map> resultList = oscarSuperManager.find("providerDao", "search_billing_no", new Object[] {request.getParameter("demographic_no")});
	for (Map bill: resultList) {
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      //self.top.location = 'providercontrol.jsp?appointment_no=<%=request.getParameter("appointment_no")%>&demographic_no=<%=Integer.parseInt(request.getParameter("demographic_no"))%>&curProvider_no=<%=curUser_no%>&username=<%= userfirstname+" "+userlastname %>&appointment_date=<%=request.getParameter("appointment_date")%>&start_time=<%=request.getParameter("start_time")%>&status=B&displaymode=encounter&dboperation=search_demograph&template=';
      self.opener.document.encounter.encounterattachment.value +="<billing>providercontrol.jsp?billing_no=<%=bill.get("billing_no")%>^displaymode=vary^displaymodevariable=billing<%=request.getParameter("billing_name")%>.jsp^dboperation=search_bill</billing>";
      self.opener.document.encounter.attachmentdisplay.value +="Billing "; //:<%=request.getParameter("billing_name")%> ";
      //self.opener.refresh();
</script>
<%
	  break; //get only one billing_no
	}//end of for
  } else {
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
</body>
</html>