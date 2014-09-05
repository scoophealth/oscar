<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%--
When including the "inWindow" parameter as "true" it is assumed that tabletSignature.jsp 
is hosted in an IFrame and that the IFrame's parent window implements signatureHandler(e)
--%>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>

<!DOCTYPE html> 
<html lang="en"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Signature Pad</title>
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/share/css/TabletSignature.css" media="screen"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/TabletSignature.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery.form.js"></script>

</head>
<%
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String requestIdKey = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
if (requestIdKey == null) {
	requestIdKey = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
}
String imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+requestIdKey;
String storedImageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&digitalSignatureId=";
boolean saveToDB = "true".equals(request.getParameter("saveToDB"));
%>
<script type="text/javascript">
var _in_window = <%= "true".equals(request.getParameter("inWindow"))%>;

var requestIdKey = "<%= requestIdKey %>";

var previewImageUrl = "<%= imageUrl %>";

var storedImageUrl = "<%= storedImageUrl %>";

var contextPath = "<%=request.getContextPath() %>";

</script>

<body style="background-color: #555">

<div class="verticalCenterDiv">
	<div class="centerDiv">
		<canvas id='canvas'></canvas>
		<div><span id="signMessage" style="color:#FFFFFF;">Please sign in the box above this message.</span><button id="clear" style="display:none">Clear</button><button id="save" style="display:none;">Save</button></div>
	</div>
</div>

<form onsubmit="return submitSignature();" action="<%=request.getContextPath() %>/signature_pad/uploadSignature.jsp" id="signatureForm" method="POST">
	<input type="hidden" id="signatureImage" name="signatureImage" value="" />
	<input type="hidden" name="source" value="IPAD" />
	<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY %>" value="<%= requestIdKey %>" />
	<input type="hidden" name="demographicNo" value="<%= request.getParameter("demographicNo") %>" />
	<input type="hidden" name="saveToDB" value="<%=saveToDB%>" />
</form>

</body>
</html>
