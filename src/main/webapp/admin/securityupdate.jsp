<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
        objectName="_admin,_admin.userAdmin" rights="r"
        reverse="<%=true%>">
        <%authed=false; %>
        <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page import="java.sql.*, java.util.*,java.security.*,oscar.*,oscar.oscarDB.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Security" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.managers.SecurityManager" %>
<%@ page import="oscar.login.PasswordHash" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><bean:message key="admin.securityupdate.title" /></title>
</head>
<link rel="stylesheet" href="../web.css" />
<body topmargin="0" leftmargin="0" rightmargin="0">
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
    if (OscarProperties.getInstance().isPINEncripted()) sPin = Misc.encryptPIN(request.getParameter("pin"));

    String hashedPassword = null;
	String hashedPin = null;
	
	boolean errorHashing=false;
	
	try {
		hashedPassword = PasswordHash.createHash(request.getParameter("password"));
		hashedPin = PasswordHash.createHash(request.getParameter("pin"));
	} catch(Exception e) {
		MiscUtils.getLogger().error("Error with hashing passwords on this system!",e);
		errorHashing=true;
	}
	
    int rowsAffected =0;

    Security s = securityManager.find(loggedInInfo,Integer.parseInt(request.getParameter("security_no")));
    if(!errorHashing && s != null) {
    	s.setUserName(request.getParameter("user_name"));
	    s.setProviderNo(request.getParameter("provider_no"));
	    s.setBExpireset(request.getParameter("b_ExpireSet")==null?0:Integer.parseInt(request.getParameter("b_ExpireSet")));
	    s.setDateExpiredate(MyDateFormat.getSysDate(request.getParameter("date_ExpireDate")));
	    s.setBLocallockset(request.getParameter("b_LocalLockSet")==null?0:Integer.parseInt(request.getParameter("b_LocalLockSet")));
	    s.setBRemotelockset(request.getParameter("b_RemoteLockSet")==null?0:Integer.parseInt(request.getParameter("b_RemoteLockSet")));

    	if(request.getParameter("password")==null || !"*********".equals(request.getParameter("password"))){
    		s.setPassword(hashedPassword);
    		s.setPasswordUpdateDate(new java.util.Date());
    	}

    	if(request.getParameter("pin")==null || !"****".equals(request.getParameter("pin"))) {
    		s.setPin(hashedPin);
    		s.setPinUpdateDate(new java.util.Date());
    	}
    	
    	if (request.getParameter("forcePasswordReset") != null && request.getParameter("forcePasswordReset").equals("1")) {
    	    s.setForcePasswordReset(Boolean.TRUE);
    	} else {
    		s.setForcePasswordReset(Boolean.FALSE);  
        }
    	s.setStorageVersion(Security.STORAGE_VERSION_2);
    	
    	securityManager.updateSecurityRecord(loggedInInfo, s);
    	rowsAffected=1;
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

</center>
</body>
</html:html>
