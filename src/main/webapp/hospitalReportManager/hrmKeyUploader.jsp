<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
String outcome = (String) request.getAttribute("outcome");
String filePath = (String) request.getAttribute("filePath");
String type = (String) request.getAttribute("type");
if(outcome != null){
    if(outcome.equals("success")){
%><script type="text/javascript">alert("Key uploaded successfully");opener.updateLink(<%=filePath%>,<%=type%>);</script>
<%
    }else if(outcome.equals("exception")){
%><script type="text/javascript">alert("Exception uploading the Key");</script>
<%
    }else{
%><script type="text/javascript">alert("Failed to upload Key");</script>
<%
    }
}
%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRM Key Uploader</title>
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript">
            function selectOther(){                
                if (document.UPLOAD.type.value == "PRIVATEKEY")
                    document.getElementById('PRIVATEKEY').style.visibility = "visible";
                else
                    document.getElementById('PRIVATEKEY').style.visibility = "hidden";                
            }
            function checkInput(){
                if (document.UPLOAD.lab.value ==""){
                    alert("Please select a file to upload");
                    return false;
                }else if (document.UPLOAD.type.value == "PRIVATEKEY" && document.UPLOAD.otherType.value == ""){
                    alert("Please specify the other message type");
                    return false;
                }else{
                    var lab = document.UPLOAD.lab.value;
                    var ext = lab.substring((lab.length - 3), lab.length);
                  //  if (ext != 'hl7' && ext != 'xml'){
                  //      alert("Error: The lab must be either a .xml or .hl7 file");
                  //      return false;
                  //  }
                }
                return true;
            }
        </script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
    $( document ).tooltip();
  });
</script>
</head>

<body>
<form method='POST' name="UPLOAD" enctype="multipart/form-data"
	action='<%=request.getContextPath()%>/hospitalReportManager/hrmKeyUploader.do'>
<table align="center" class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Upload <!--i18n--></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="Upload the KEY"> </td>
<!-- 			onclick="return checkInput()"> -->
		<td>
		<table>
			<tr>
				<td>Please select the key file:</td>
				<td><input type="file" name="importFile">
				<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
				</td>
			</tr>
			<tr>
				<td>Key type:</td>
				<td><select name="type" onClick="selectOther()">
					<option value="PRIVATEKEY">PRIVATE KEY</option>
					<option value="DECRYPTIONKEY">DECRYPTION KEY</option>
					</select></td>
			</tr>
			<tr>
				<td><input type='hidden' name="filePath" value="filePath"></td>
			<tr>
		</table>
		</td>
	</tr>
</table>
</form>

</body>
</html>
