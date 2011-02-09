
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%><%@page import="org.oscarehr.web.eform.EfmpatientformlistSendPhrAction"%>
<%
String[] s=request.getParameterValues("sendToPhr");
EfmpatientformlistSendPhrAction bean=new EfmpatientformlistSendPhrAction(request);

String clientId = request.getParameter("clientId");

ArrayList<String> newDocIds=bean.sendEFormsToPhr(s);

StringBuilder sb=new StringBuilder();
sb.append(request.getContextPath());
sb.append("/dms/SendDocToPhr.do?demoId=");
sb.append(clientId);

for (String docId : newDocIds)
{
	sb.append("&docNo=");
	sb.append(docId);
}

response.sendRedirect(sb.toString());
%>