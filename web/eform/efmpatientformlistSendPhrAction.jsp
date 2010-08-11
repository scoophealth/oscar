
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.Arrays"%><%
String[] s=request.getParameterValues("sendToPhr");
MiscUtils.getLogger().error("-------- s="+Arrays.toString(s));
%>