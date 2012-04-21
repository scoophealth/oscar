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

<%--
When including the "inWindow" parameter as "true" it is assumed that tabletSignature.jsp 
is hosted in an IFrame and that the IFrame's parent window implements signatureHandler(e)
--%>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>

<!DOCTYPE html> 
<html lang="en" manifest="cache.manifest"> 
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
String requestIdKey = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
if (requestIdKey == null) {
	requestIdKey = DigitalSignatureUtils.generateSignatureRequestId(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
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
