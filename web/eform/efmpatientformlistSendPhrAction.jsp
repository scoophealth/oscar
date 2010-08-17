<%@page import="java.io.File"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.WKHtmlToPdfUtils"%>
<%@page import="java.util.Arrays"%><%
String[] s=request.getParameterValues("sendToPhr");
MiscUtils.getLogger().error("-------- s="+Arrays.toString(s));
WKHtmlToPdfUtils.convertToPdf("http://news.com");
File file=new File("/tmp/test.pdf");
WKHtmlToPdfUtils.convertToPdf("http://ibm.com", file);
%>