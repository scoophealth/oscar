<!--
/*
 *
 * Copyright (c) 2005 Department of Family Medicine, McMaster University. All Rights Reserved. *
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.sql.*, java.util.*,java.security.*,oscar.*,oscar.oscarDB.*, oscar.util.SqlUtils"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.securityupdate.title" /></title>
</head>
<link rel="stylesheet" href="../web.css" />
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.securityupdate.description" /></font></th>
	</tr>
</table>
<%
    StringBuffer sbTemp = new StringBuffer();
    MessageDigest md = MessageDigest.getInstance("SHA");
    byte[] btNewPasswd= md.digest(request.getParameter("password").getBytes());
    for(int i=0; i<btNewPasswd.length; i++) sbTemp = sbTemp.append(btNewPasswd[i]);

    String sPin = request.getParameter("pin");
    if (OscarProperties.getInstance().isPINEncripted()) sPin = Misc.encryptPIN((String)request.getParameter("pin"));

    int rowsAffected =0;
    
  String sDboperation=request.getParameter("dboperation");
  if( ("*********".equals(request.getParameter("password")) || request.getParameter("password")==null) && 
        ("****".equals(request.getParameter("pin")) || request.getParameter("pin")==null)){
      sDboperation = sDboperation + "4";  // no password and PIN update
      Object[] param =new Object[7];
	  param[0]=request.getParameter("user_name");
//      param[1]=sbTemp.toString();
	  param[1]=request.getParameter("provider_no");
//	  param[3]=sPin;
      param[2]=request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet");
	  param[3]=MyDateFormat.getSysDate(request.getParameter("date_ExpireDate"));
	  param[4]=request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet");
	  param[5]=request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet");
	  param[6]=Integer.parseInt(request.getParameter("security_no"));
      rowsAffected = oscarSuperManager.update("adminDao", sDboperation, param);
  }else if("*********".equals(request.getParameter("password")) || request.getParameter("password")==null){
      sDboperation = sDboperation + "3";  // no password update
      Object[] param =new Object[8];
	  param[0]=request.getParameter("user_name");
//      param[1]=sbTemp.toString();
	  param[1]=request.getParameter("provider_no");
	  param[2]=sPin;
      param[3]=request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet");
	  param[4]=MyDateFormat.getSysDate(request.getParameter("date_ExpireDate"));
	  param[5]=request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet");
	  param[6]=request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet");
	  param[7]=Integer.parseInt(request.getParameter("security_no"));
      rowsAffected = oscarSuperManager.update("adminDao", sDboperation, param);
  }else if("****".equals(request.getParameter("pin")) || request.getParameter("pin")==null){
      sDboperation = sDboperation + "2";  //no PIN update
      Object[] param =new Object[8];
	  param[0]=request.getParameter("user_name");
      param[1]=sbTemp.toString();
	  param[2]=request.getParameter("provider_no");
//	  param[3]=sPin;
      param[3]=request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet");
	  param[4]=MyDateFormat.getSysDate(request.getParameter("date_ExpireDate"));
	  param[5]=request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet");
	  param[6]=request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet");
	  param[7]=Integer.parseInt(request.getParameter("security_no"));
      rowsAffected = oscarSuperManager.update("adminDao", sDboperation, param);
  }else{
      Object[] param =new Object[9];
	  param[0]=request.getParameter("user_name");
      param[1]=sbTemp.toString();
	  param[2]=request.getParameter("provider_no");
	  param[3]=sPin;
      param[4]=request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet");
	  param[5]=MyDateFormat.getSysDate(request.getParameter("date_ExpireDate"));
	  param[6]=request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet");
	  param[7]=request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet");
	  param[8]=Integer.parseInt(request.getParameter("security_no"));
      rowsAffected = oscarSuperManager.update("adminDao", sDboperation, param);
  }
  if (rowsAffected ==1) {
      LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_SECURITY,
    		request.getParameter("security_no") + "->" + request.getParameter("user_name"), request.getRemoteAddr());
%>
<p>
<h2><bean:message key="admin.securityupdate.msgUpdateSuccess" /> <%=request.getParameter("provider_no")%></h2>
<%
  } else {
%>
<h1><bean:message key="admin.securityupdate.msgUpdateFailure" /><%= request.getParameter("provider_no") %>.</h1>
<%
  }
%>
</p>
<p></p>
<%@ include file="footer2htm.jsp"%>
</center>
</body>
</html:html>
