<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*,oscar.OscarProperties ,oscar.oscarReport.reportByTemplate.*,org.oscarehr.hospitalReportManager.*,org.oscarehr.util.SpringUtils, org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty"%>
<% 
  
    OscarProperties props = OscarProperties.getInstance();
    
    UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
 	
    String userName = "";
    String location = "";
    String interval = "30";
    String privateKey = "";
    String decryptionKey = "";
    
    try {
    	userName = userPropertyDao.getProp("hrm_username").getValue();
    } catch (Exception e) {
    	userName = "";
    }
    
    try {
    	location = userPropertyDao.getProp("hrm_location").getValue();
    } catch (Exception e) {
    	location = "";
    }
    
    try {
    	interval = userPropertyDao.getProp("hrm_interval").getValue();
    } catch (Exception e) {
    	interval = "30";
    }
    
    try {
    	privateKey = userPropertyDao.getProp("hrm_privateKey").getValue();
    } catch (Exception e) {
    	privateKey = "";
    }
    
    try {
    	decryptionKey = userPropertyDao.getProp("hrm_decryptionKey").getValue();
    } catch (Exception e) {
    	decryptionKey = "";
    }
%>

<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<title>HRM Preferences</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   

	<script type="text/javascript" language="JavaScript" src="../share/javascript/prototype.js"></script>
	<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function updateLink(filePath,keytype){
	if (keytype=="PRIVATEKEY") {
		<%=privateKey%> = filePath;
	} else if (keytype=="DECRYPTIONKEY") {
		<%=decryptionKey%> = filePath;
	}
}
</script>
</head>
<body>
<h4>HRM Preferences</h4>
<form action="<%=request.getContextPath()%>/hospitalReportManager/HRMPreferences.do" method="post">
	<fieldset>
		<div class="control-group">
			<label class="control-label">User Name:</label>
			<div class="controls">
				<input readonly="readonly" type="text" name="userName" value="<%=userName%>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">SFTP Download Folder:</label>
			<div class="controls">
				<input type="text" name="location" value="<%=location%>"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Private Key:</label>
			<div class="controls">
				<a href="../<%=privateKey%>" id="pkeyLink">View Private Key</a>
				<input type="button" class="btn" name="privateKey" value="Upload Private Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;' />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Decryption Key:</label>
			<div class="controls">
				<a href="../<%=decryptionKey%>" id="dkeyLink">View Decryption Key</a>
				<input type="button" class="btn" name="decryptionKey" value="Upload Decryption Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;' />	
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Auto Polling Interval:</label>
			<div class="controls">
				<input type="text" name="interval" value="<%=interval %>" />
			</div>
		</div>
		<div class="control-group">
			<input type="submit" class="btn btn-primary" value="Submit" />
		</div>
	</table>
</form>
</html:html>
