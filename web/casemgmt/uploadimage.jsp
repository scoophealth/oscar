<%@ include file="/casemgmt/taglibs.jsp" %>

<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>

<%
	if(application.getAttribute("javax.servlet.context.tempdir") == null) {
		System.out.println("no javax.servlet.context.tempdir defined");
		String tmpDir = System.getProperty("java.io.tmpdir");
		System.out.println("tmpDir=" + tmpDir);
		application.setAttribute("javax.servlet.context.tempdir",new java.io.File(tmpDir));
	}
%>
<!-- <%@ include file="/casemgmt/taglibs.jsp" %> -->
<head>
<title>Client Image Manager</title>
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script>
	function init_page() {
		<%
			if(request.getAttribute("success") != null) {
				%>opener.document.caseManagementViewForm.submit(); self.close();<%
			}
		%>
	}
</script>
</head>
<body bgcolor="#C4D9E7" bgproperties="fixed" onLoad="self.focus();init_page();" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">Client Image Manager</font></th></tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C4D9E7">

	<html:form action="/ClientImage" enctype="multipart/form-data" method="post">
	<input type="hidden" name="method" value="saveImage"/>
	<%
	request.getSession().setAttribute("clientId",request.getParameter("demographicNo"));
	%>
		<tr valign="top"><td rowspan="2" ALIGN="right" valign="middle"> <font face="Verdana" color="#0000FF"><b><i>Add Image
        </i></b></font></td>


    <td valign="middle" rowspan="2" ALIGN="left">		
		<html:file property="clientImage.imagefile" size="30" accept="*.gif,*.jpg"/><br>
		<html:submit value="Upload" />
	</td>
		</tr>
	</html:form>
</table>
<br>
Attention:
<br>
Only gif and jpg image type are alowed for the client photo uploading.
<br>
<br>


<form>

  <input type="button" name="Button" value="cancel" onclick="self.close();"/>
</form>






</body>
