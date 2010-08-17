<%@page import="org.oscarehr.web.eform.EfmpatientformlistSendPhrAction"%>
<%
String[] s=request.getParameterValues("sendToPhr");
EfmpatientformlistSendPhrAction bean=new EfmpatientformlistSendPhrAction(request);
bean.sendEFormsToPhr(s);
%>