<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="java.io.File" %>

<%

Provider provider = (Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
if (provider == null) {
%>NOT_AUTHORIZED<%
}
%>


<%

String signatureRequestId = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
String tempFilePath = DigitalSignatureUtils.getTempFilePath(signatureRequestId);
File f = new File(tempFilePath);
if(f.exists()) {
%>FOUND<%
} else {
%>NOT_FOUND<%
}
%>
