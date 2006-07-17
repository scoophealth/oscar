

<!-- <%@ include file="/taglibs.jsp" %> -->
<head>
<title>up load image of client</title>
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
</head>
<body bgcolor="#C4D9E7" bgproperties="fixed" onLoad="self.focus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">UP LOAD CLIENT IMAGE HERE</font></th></tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7">
	<FORM NAME="aForm" ENCTYPE="multipart/form-data" ACTION="../../servlet/oscar.ImageUploadServlet" METHOD="POST">
	<%
	request.getSession().setAttribute("clientId",request.getParameter("demographicNo"));
	%>
		<tr valign="top"><td rowspan="2" ALIGN="right" valign="middle"> <font face="Verdana" color="#0000FF"><b><i>ADD IMAGE
        </i></b></font></td>


    <td valign="middle" rowspan="2" ALIGN="left">		
		<INPUT TYPE="file" SIZE="30" NAME="file1"><br>
		<INPUT TYPE="SUBMIT" VALUE="upload" onclick="javascript:opener.document.caseManagementViewForm.submit();">
	</td>
		</tr>
	</form>
</table>
<br>
Attention:
<br>
Only gif and jpg image type are alowed for the client photo uploading.
<br>
<br>
<%
String originalFile=(String)request.getSession().getAttribute("image");
%>
	Current ID Photo For the Client:<br><c:out value="${requestScope.casemgmt_demoName}" /> <img src="<%=originalFile %>" alt="id_photo"  onClick="popupUploadPage('uploadimage.jsp');return false;"/>	
<% 

%>

<form>

  <input type="button" name="Button" value="cancel" onclick="self.close();">
</form>






</body>
