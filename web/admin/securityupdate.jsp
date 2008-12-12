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
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html:html locale="true">
<head>
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
//  int rowsAffected=0;
//  if("*********".equals(request.getParameter("password"))==false && "****".equals(request.getParameter("pin"))==false && 
//     request.getParameter("password")!=null && request.getParameter("pin")!=null) {
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
      DBPreparedHandlerParam[] param =new DBPreparedHandlerParam[7];
	  param[0]=new DBPreparedHandlerParam(request.getParameter("user_name"));
//      param[1]=new DBPreparedHandlerParam(sbTemp.toString());
	  param[1]=new DBPreparedHandlerParam(request.getParameter("provider_no"));
//	  param[3]=new DBPreparedHandlerParam(sPin);
      param[2]=new DBPreparedHandlerParam(request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet"));
	  param[3]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
	  param[4]=new DBPreparedHandlerParam(request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet"));
	  param[5]=new DBPreparedHandlerParam(request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet"));
	  param[6]=new DBPreparedHandlerParam(Integer.parseInt(request.getParameter("security_no")));
      rowsAffected = apptMainBean.queryExecuteUpdate(param, sDboperation);
    }else if("*********".equals(request.getParameter("password")) || request.getParameter("password")==null){
      sDboperation = sDboperation + "3";  // no password update
      DBPreparedHandlerParam[] param =new DBPreparedHandlerParam[8];
	  param[0]=new DBPreparedHandlerParam(request.getParameter("user_name"));
//      param[1]=new DBPreparedHandlerParam(sbTemp.toString());
	  param[1]=new DBPreparedHandlerParam(request.getParameter("provider_no"));
	  param[2]=new DBPreparedHandlerParam(sPin);
      param[3]=new DBPreparedHandlerParam(request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet"));
	  param[4]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
	  param[5]=new DBPreparedHandlerParam(request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet"));
	  param[6]=new DBPreparedHandlerParam(request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet"));
	  param[7]=new DBPreparedHandlerParam(Integer.parseInt(request.getParameter("security_no")));
      rowsAffected = apptMainBean.queryExecuteUpdate(param, sDboperation);
    }else if("****".equals(request.getParameter("pin")) || request.getParameter("pin")==null){
      sDboperation = sDboperation + "2";  //no PIN update
      DBPreparedHandlerParam[] param =new DBPreparedHandlerParam[8];
	  param[0]=new DBPreparedHandlerParam(request.getParameter("user_name"));
      param[1]=new DBPreparedHandlerParam(sbTemp.toString());
	  param[2]=new DBPreparedHandlerParam(request.getParameter("provider_no"));
//	  param[3]=new DBPreparedHandlerParam(sPin);
      param[3]=new DBPreparedHandlerParam(request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet"));
	  param[4]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
	  param[5]=new DBPreparedHandlerParam(request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet"));
	  param[6]=new DBPreparedHandlerParam(request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet"));
	  param[7]=new DBPreparedHandlerParam(Integer.parseInt(request.getParameter("security_no")));
      rowsAffected = apptMainBean.queryExecuteUpdate(param, sDboperation);
    }else{
      DBPreparedHandlerParam[] param =new DBPreparedHandlerParam[9];
	  param[0]=new DBPreparedHandlerParam(request.getParameter("user_name"));
      param[1]=new DBPreparedHandlerParam(sbTemp.toString());
	  param[2]=new DBPreparedHandlerParam(request.getParameter("provider_no"));
	  param[3]=new DBPreparedHandlerParam(sPin);
      param[4]=new DBPreparedHandlerParam(request.getParameter("b_ExpireSet")==null?"0":request.getParameter("b_ExpireSet"));
	  param[5]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
	  param[6]=new DBPreparedHandlerParam(request.getParameter("b_LocalLockSet")==null?"0":request.getParameter("b_LocalLockSet"));
	  param[7]=new DBPreparedHandlerParam(request.getParameter("b_RemoteLockSet")==null?"0":request.getParameter("b_RemoteLockSet"));
	  param[8]=new DBPreparedHandlerParam(Integer.parseInt(request.getParameter("security_no")));
      rowsAffected = apptMainBean.queryExecuteUpdate(param, sDboperation);
    }
    
//    int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
//  }
  if (rowsAffected ==1) {
%>
<p>
<h2><bean:message key="admin.securityupdate.msgUpdateSuccess" /> <%=request.getParameter("provider_no")%>
</h2>
<%
  } else {
%>
<h1><bean:message key="admin.securityupdate.msgUpdateFailure" /><%= request.getParameter("provider_no") %>.
<%
  }
  apptMainBean.closePstmtConn();
%>
<p></p>
<%@ include file="footer2htm.jsp"%>
</center>
</body>
</html:html>
