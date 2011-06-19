<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*,oscar.OscarProperties ,oscar.oscarReport.reportByTemplate.*,org.oscarehr.hospitalReportManager.*,org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
  
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    OscarProperties props = OscarProperties.getInstance();
    
    String userName = ""; // = getSettings().getId().getUUID().toString();
    
   
 	String location = SFTPConnector.getDownloadsDirectory();
 	String privateKey = SFTPConnector.getOMD_keyLocation();
 	String decryptionKey = SFTPConnector.getDecryptionKey();
    
 	//String filepath;
    
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Clinic</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
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
<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>HRM Preferences</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
		<tr>
			<td>HRM Settings</td>
		</tr>
		<form action="<%=request.getContextPath()%>/hospitalReportManager/HRMPreferences.do">
			
			<table>
			<tr>
				<td>User Name:</td>
				<td><input readonly="readonly" type="text" name="userName" value=<%=userName%> ></td>
				
			</tr>
				<td>SFTP Download folder:</td>
				<td><input type="text" name="location" value=<%=location%>></td>
			<tr>
			
			</tr>
			<tr>
				<td>Private Key</td>
				<td><a href="../<%=privateKey%>" id="pkeyLink">View Private Key</a></td>
				<td><input type="button" name="privateKey" value="Upload Private Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;'></td>
			</tr>
			<tr>
				<td>Decryption Key</td>
				<td><a href="../<%=decryptionKey%>" id="dkeyLink">View Decryption Key</a></td>
				<td><input type="button" name="decryptionKey" value="Upload Decryption Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;' ></td>
			</tr>
			<tr>
				<td> 
					<input type="submit" value="submit" />
				</td>
			</tr>
			</table>
		</form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
